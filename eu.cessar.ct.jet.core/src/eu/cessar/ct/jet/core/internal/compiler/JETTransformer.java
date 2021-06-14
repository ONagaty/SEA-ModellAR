package eu.cessar.ct.jet.core.internal.compiler;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.core.internal.events.BuildManager;
import org.eclipse.core.internal.resources.ICoreConstants;
import org.eclipse.core.internal.resources.Workspace;
import org.eclipse.core.resources.IBuildConfiguration;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.codegen.CodeGenPlugin;
import org.eclipse.emf.codegen.jet.JETCharDataGenerator;
import org.eclipse.emf.codegen.jet.JETCompiler;
import org.eclipse.emf.codegen.jet.JETException;
import org.eclipse.emf.codegen.jet.JETGenerator;
import org.eclipse.emf.codegen.jet.JETMark;
import org.eclipse.jdt.core.compiler.IProblem;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.internal.compiler.problem.DefaultProblem;
import org.eclipse.jdt.internal.compiler.problem.ProblemSeverities;

import eu.cessar.ct.core.platform.util.Keywords;
import eu.cessar.ct.core.platform.util.ResourceUtils;
import eu.cessar.ct.jet.core.EJetExecutionMode;
import eu.cessar.ct.jet.core.JETCoreConstants;
import eu.cessar.ct.jet.core.JETCoreUtils;
import eu.cessar.ct.jet.core.compiler.IJETContentProvider;
import eu.cessar.ct.jet.core.internal.CessarPluginActivator;
import eu.cessar.ct.jet.core.internal.JETCoreMessages;

/**
 * This is the class that implements JET transformer. It is responsible with conversion from JET to Java content. Also
 * the transformer is capable of detecting and reporting JET or Java problems.
 */
@SuppressWarnings({"restriction", "nls"})
public class JETTransformer extends CessarJETCompiler
{

	private static final String JET_CHAR_STOP = "jetCharStop";
	private static final String JET_CHAR_START = "jetCharStart";
	private static final String JET_LINE_NUMBER = "jetLineNumber";
	private IJETContentProvider contentProvider;
	private List<IProblem> problems;
	private Stack<SourceRange> rangeStack = new Stack<>();
	private Map<JETGenerator, SourceRange> jetGenerators2JetRanges = new HashMap<>();
	private Map<SourceRange, SourceRange> java2Jet = new HashMap<>();
	private Map<String, Integer> jetLinesMap;
	private Map<Integer, String> javaLinesMap;
	// Maps that contain the jet2java mapping for scriplet and expression
	// regions
	// only
	private Map<SourceRange, SourceRange> javaCodeJet2Java = new HashMap<>();
	private Map<SourceRange, SourceRange> javaCodeJava2Jet = new HashMap<>();

	private String javaSource = "";
	private IFile javaIFile;
	private SourceRange directiveRange;
	private CompilationUnit astCompilationUnit;

	/**
	 * Default constructor
	 *
	 * @param provider
	 *        an {@link IJETContentProvider} instance that is used to obtain JET information
	 * @throws CoreException
	 * @throws JETException
	 */
	JETTransformer(final IJETContentProvider provider) throws JETException, CoreException
	{
		super(provider.getJavaProject().getProject().getName() + IPath.SEPARATOR + provider.getJETFileName(),
			provider.getContent(), "UTF8");

		fNoNewLineForScriptlets = true;
		contentProvider = provider;
		problems = new ArrayList<>();
	}

	/**
	 * Converts Java offset position into JET offset
	 *
	 * @param javaOffset
	 *        the Java offset to be converted
	 * @return The corresponding JET offset.
	 */
	int convertJava2JetOffset(final int javaOffset)
	{
		for (Entry<SourceRange, SourceRange> entry: java2Jet.entrySet())
		{
			SourceRange javaRange = entry.getKey();
			if ((javaOffset >= javaRange.getStartOffset()) && (javaOffset <= javaRange.getEndOffset()))
			{
				SourceRange jetRange = entry.getValue();
				int off = javaOffset - javaRange.getStartOffset();
				int result = jetRange.getStartOffset() + off;
				// find out in which generator we are
				for (Entry<JETGenerator, SourceRange> jetGeneratorToJetRange: jetGenerators2JetRanges.entrySet())
				{
					if (jetGeneratorToJetRange.getValue().equals(jetRange))
					{
						JETGenerator jetGenerator = jetGeneratorToJetRange.getKey();
						if (jetGenerator instanceof JETExpressionGenerator2)
						{
							result -= JETCharDataGenerator2.FUNCTION_CALL_BEGIN.length();
						}
					}
				}
				if (result > jetRange.getEndOffset())
				{
					result = jetRange.getEndOffset();
				}
				return result;
			}
		}
		return -1;
	}

	/**
	 * Converts JET offset position into Java offset
	 *
	 * @param jetOffset
	 *        the JET offset to be converted
	 * @return The corresponding Java offset.
	 */
	int convertJet2JavaOffset(final int jetOffset)
	{

		for (Entry<SourceRange, SourceRange> entry: java2Jet.entrySet())
		{
			SourceRange jetRange = entry.getValue();
			if (jetRange.getStartOffset() > 0 && (jetOffset >= jetRange.getStartOffset())
				&& (jetOffset <= jetRange.getEndOffset()))
			{
				SourceRange javaRange = entry.getKey();
				int off = jetOffset - jetRange.getStartOffset();
				int result = javaRange.getStartOffset() + off;
				// find out in which generator we are
				for (Entry<JETGenerator, SourceRange> jetGeneratorToJetRange: jetGenerators2JetRanges.entrySet())
				{
					if (jetGeneratorToJetRange.getValue().equals(jetRange))
					{
						JETGenerator jetGenerator = jetGeneratorToJetRange.getKey();
						if (jetGenerator instanceof JETExpressionGenerator2)
						{
							result += JETCharDataGenerator2.FUNCTION_CALL_BEGIN.length();
						}
					}
				}

				if (result > javaRange.getEndOffset())
				{
					result = javaRange.getEndOffset();
				}
				return result;
			}
		}
		return -1;
	}

	/**
	 * @return
	 */
	private SourceRange[] getCodeJavaRanges()
	{
		return javaCodeJava2Jet.keySet().toArray(new SourceRange[javaCodeJava2Jet.keySet().size()]);
	}

	/**
	 * @param number
	 * @return
	 */
	public SourceRange findNextScripletOrExpression(final int number)
	{
		SourceRange[] javaRanges = getCodeJavaRanges();
		SourceRange[] jetRanges = new SourceRange[javaRanges.length];
		for (int i = 0; i < javaRanges.length; i++)
		{
			jetRanges[i] = getJetRange(javaRanges[i]);
		}
		Arrays.sort(jetRanges, new Comparator<SourceRange>()
		{
			public int compare(final SourceRange o1, final SourceRange o2)
			{
				return new Integer(o1.getStartLine()).compareTo(new Integer(o2.getStartLine()));
			}
		});

		SourceRange result = null;
		for (int i = jetRanges.length - 1; i >= 0; i--)
		{

			SourceRange jetRange = jetRanges[i];
			if (isLineInRange(number, jetRange))
			{
				return jetRange;
			}
			if (isLineBeforeRange(number, jetRange))
			{
				result = jetRange;
			}

		}
		return result;
	}

	/**
	 * @param number
	 * @param jetRange
	 * @return
	 */
	private boolean isLineInRange(final int number, final SourceRange jetRange)
	{
		return jetRange.getStartLine() <= number && jetRange.getEndLine() >= number;
	}

	/**
	 * @param number
	 * @param jetRange
	 * @return
	 */
	private boolean isLineBeforeRange(final int number, final SourceRange jetRange)
	{
		return jetRange.getStartLine() > number;
	}

	/**
	 * Method getJetRange.
	 *
	 * @param javaRange
	 *        Range
	 * @return Range
	 */
	private SourceRange getJetRange(final SourceRange javaRange)
	{
		return java2Jet.get(javaRange);
	}

	/**
	 * @return
	 */
	IFile getJavaFile()
	{
		return javaIFile;
	}

	/**
	 * The method that transform a JET content into Java source.
	 *
	 * @param inDebugMode
	 *
	 * @param monitor
	 *        a progress monitor instance
	 */
	void transform(boolean inDebugMode, final IProgressMonitor monitor)
	{
		try
		{

			problems.clear();
			rangeStack.clear();
			jetGenerators2JetRanges.clear();
			parse();

			ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
			generate(arrayOutputStream);
			saveJavaSourceIfNeeded(inDebugMode, arrayOutputStream, monitor);

			javaSource = arrayOutputStream.toString();

			processJavaSource(monitor);
			startBuild();
			createJavaLinesMap();
			createJetLinesMap();
		}
		catch (JETException ex)
		{
			problems.add(createExceptionProblem(ex));
		}
	}

	private synchronized void startBuild()
	{
		IProject project = javaIFile.getProject();
		Workspace workspace = (Workspace) project.getWorkspace();
		BuildManager buildManager = workspace.getBuildManager();
		IBuildConfiguration[] buildOrder = workspace.getBuildOrder();
		IBuildConfiguration[] neededBuildOrder = new IBuildConfiguration[1];
		for (IBuildConfiguration iBuildConfiguration: buildOrder)
		{
			if (iBuildConfiguration.getProject().equals(project))
			{
				neededBuildOrder[0] = iBuildConfiguration;
				break;
			}
		}
		buildManager.build(neededBuildOrder, ICoreConstants.EMPTY_BUILD_CONFIG_ARRAY, 9, null);
	}

	private void processJavaSource(final IProgressMonitor monitor)
	{
		// initialize AST with Java source code
		ASTParser parser = ASTParser.newParser(AST.JLS8);
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setProject(contentProvider.getJavaProject());
		parser.setSource(javaSource.toCharArray());
		astCompilationUnit = (CompilationUnit) parser.createAST(monitor);
		// compute the first JET line number
		// at this point in rangeStack should be only the range for JET
		// directive, so the end line for this directive can be used to compute
		// the first JET line number.
		directiveRange = rangeStack.pop();
		int jetLineNumber = 1;
		if (directiveRange != null)
		{
			jetLineNumber = directiveRange.getEndLine() + 1;
		}

		// initialize Java to JET mapping
		for (JETGenerator jetGenerator: generators)
		{
			// Compute the body lines for this generator
			String bodyLine = jetGenerator.generate();
			int startOffset = javaSource.indexOf(bodyLine);
			if (startOffset == -1)
			{
				continue; // code generated should be in Java source
			}

			int endOffset = startOffset + bodyLine.length();
			SourceRange javaRange = new SourceRange(startOffset, endOffset,
				astCompilationUnit.getLineNumber(startOffset), astCompilationUnit.getLineNumber(endOffset),
				bodyLine.toCharArray());
			SourceRange jetRange = jetGenerators2JetRanges.get(jetGenerator);

			// JET range is missing line number information
			if (!jetRange.isComplete())
			{
				jetRange.setLines(jetLineNumber, jetLineNumber + 1);
				jetLineNumber++;
			}
			else
			{
				// move jet line number to the end of current JET range
				if (jetRange.getEndLine() > jetLineNumber)
				{
					jetLineNumber = jetRange.getEndLine() + 1;
				}
			}
			java2Jet.put(javaRange, jetRange);

			// Initialize mapping for scriplet/expression areas
			if (jetGenerator instanceof JETScriptletGenerator2 || jetGenerator instanceof JETExpressionGenerator2)
			{
				javaCodeJet2Java.put(jetRange, javaRange);
				javaCodeJava2Jet.put(javaRange, jetRange);
			}
		}
		problems.addAll(cleanUpdateProblems(astCompilationUnit.getProblems()));
	}

	/**
	 * This method remove duplicate problems and convert Java lines to JET lines.
	 *
	 * @param javaProblems
	 * @return list of problems
	 */
	public List<IProblem> cleanUpdateProblems(final IProblem[] javaProblems)
	{
		List<IProblem> validProblems = new ArrayList<>();
		if ((javaProblems == null) || (javaProblems.length == 0))
		{
			return validProblems;
		}

		List<String> pbMessages = new ArrayList<>();
		for (IProblem iProblem: javaProblems)
		{
			// problem must be processed if its not generated by a JETException
			if (iProblem.getID() != IProblem.ExternalProblemFixable)
			{
				// process problem if it was not already processed
				// the same problem can appear in multiple lines, all of which should be shown
				String msg = iProblem.getMessage() + iProblem.getSourceLineNumber();

				if (!pbMessages.contains(msg))
				{
					convertJava2JetProblem(iProblem);
					validProblems.add(iProblem);
					pbMessages.add(msg);
				}
			}
			else
			{
				validProblems.add(iProblem);
			}
		}
		return validProblems;
	}

	private IProblem createExceptionProblem(final Exception exception)
	{
		String message = exception.getMessage();
		String problem = message;
		int lineNumber = 0;
		int columnNumber = 0;
		int start = message.lastIndexOf('(');
		int end = message.lastIndexOf(')');
		int startPosition = 0;
		int endPosition = 0;
		CessarJETMark problemMark = reader.mark();

		if (start != -1 && end != -1)
		{
			// message might contain line and column positioning in the format:
			// error_message (line,column)
			String position = message.substring(start + 1, end);

			int ind = position.indexOf(',');
			if (ind != -1)
			{
				try
				{
					lineNumber = Integer.parseInt(position.substring(0, ind));
					columnNumber = Integer.parseInt(position.substring(ind + 1));
				}
				catch (NumberFormatException e)
				{
					// not valid line/column information
					lineNumber = 0;
					columnNumber = 0;
				}
			}
			problem = message.substring(0, start);
		}
		else
		{
			String lineHint = "line";
			String columnHint = "column";
			start = message.indexOf(lineHint);
			end = message.indexOf(columnHint);
			// exception can be in format: error_message at line x column y
			if (start != -1 && end != -1)
			{
				try
				{
					String messageBeforeLineNumber = message.substring(start + lineHint.length() + 1);
					int spaceIndex = messageBeforeLineNumber.indexOf(' ');
					if (spaceIndex != -1)
					{
						lineNumber = Integer.parseInt(messageBeforeLineNumber.substring(0, spaceIndex));
					}
					else
					{
						lineNumber = Integer.parseInt(messageBeforeLineNumber.substring(0));
					}
					String messageAfterLineBeforeColumn = message.substring(end + columnHint.length() + 1);
					spaceIndex = messageAfterLineBeforeColumn.indexOf(' ');
					if (spaceIndex != -1)
					{
						columnNumber = Integer.parseInt(messageAfterLineBeforeColumn.substring(0, spaceIndex));
					}
					else
					{
						columnNumber = Integer.parseInt(messageAfterLineBeforeColumn.substring(0));
					}
				}
				catch (NumberFormatException e)
				{
					// not valid line/column information
					lineNumber = 0;
					columnNumber = 0;
				}
			}
			else
			{
				// get it from the JETMark
				lineNumber = getMemberValue(problemMark, "line") + 1;
				columnNumber = getMemberValue(problemMark, "col");
				// lineNumber = getMemberValue(problemMark, "line");
				// columnNumber = getMemberValue(problemMark, "column");
			}
		}

		// compute start position and end position
		start = message.lastIndexOf('[');
		end = message.lastIndexOf(']');
		if (start != -1 && end != -1)
		{
			// message might contain start and end positions
			String position = message.substring(start + 1, end);

			int ind = position.indexOf(',');
			if (ind != -1)
			{
				try
				{
					startPosition = Integer.parseInt(position.substring(0, ind));
					endPosition = Integer.parseInt(position.substring(ind + 1));
				}
				catch (NumberFormatException e)
				{
					// not valid start/end position information
					startPosition = 0;
					endPosition = 0;
				}
			}
		}
		else
		{
			// try to compute the start and end position
			// startPosition = CessarJETParser.isDirective ?
			// getMemberValue(problemMark, "cursor")
			// - columnNumber : 0;
			startPosition = getMemberValue(problemMark, "cursor") - columnNumber;
			endPosition = startPosition + 1;
		}

		return new DefaultProblem(contentProvider.getJETFileName().toCharArray(),
			JETCoreMessages.error_JetParsingProblem + " " + problem, IProblem.ExternalProblemFixable, new String[0],
			ProblemSeverities.Error, startPosition, endPosition, lineNumber, columnNumber);
	}

	/**
	 * Converts a Java line number into a Jet line number
	 *
	 * @param lineNumber
	 *        the Java line number to be converted
	 * @return the corresponding Jet line number if it can be computed, or -1 otherwise
	 */
	int convertJava2JetLine(final int lineNumber)
	{
		int jetLineNumber = -1;

		for (Entry<SourceRange, SourceRange> entry: java2Jet.entrySet())
		{
			SourceRange javaRange = entry.getKey();
			if (lineNumber >= javaRange.startLine && lineNumber <= javaRange.endLine)
			{
				int lineOffset = lineNumber - javaRange.startLine;
				SourceRange jetRange = entry.getValue();

				lineOffset = calculateLineOffset(jetRange, lineOffset);

				jetLineNumber = calculateJetLineNumber(lineOffset, jetRange);
			}
		}

		return jetLineNumber;
	}

	private int calculateJetLineNumber(int lineOffset, SourceRange jetRange)
	{
		int jetLineNumber;
		jetLineNumber = jetRange.getStartLine() + lineOffset;
		return jetLineNumber;
	}

	/**
	 * Converts a Jet line number into a Java line number
	 *
	 * @param lineNumber
	 *        the Jet line number to be converted
	 * @return the corresponding Java line number if it can be computed, or -1 otherwise
	 */
	int convertJet2JavaLine(final int lineNumber)
	{
		int javaLineNumber = -1;
		for (Entry<SourceRange, SourceRange> entry: java2Jet.entrySet())
		{
			SourceRange jetRange = entry.getValue();
			if (lineNumber >= jetRange.startLine && lineNumber <= jetRange.endLine)
			{
				int lineOffset = lineNumber - jetRange.startLine;
				SourceRange javaRange = entry.getKey();

				lineOffset = calculateLineOffset(javaRange, lineOffset);

				javaLineNumber = calculateJetLineNumber(lineOffset, javaRange);
			}
		}

		return javaLineNumber;
	}

	private void convertJava2JetProblem(final IProblem problem)
	{
		int lineNumber = problem.getSourceLineNumber();
		int maxJetEndLineNumber = 0;
		int maxJetOffset = 0;
		createJavaLinesMap();
		createJetLinesMap();
		String string = javaLinesMap.get(problem.getSourceLineNumber() - 1);
		String str_import = "import";
		int lastIndexOf = string.lastIndexOf(str_import);
		// We check to see if the new problem that we want to create is in the import part or in the
		// expression/scriplet part of the jet
		if (lastIndexOf >= 0)
		{
			String substring = string.substring(lastIndexOf + 6, string.length());
			String replaceFirst = substring.replaceFirst(";", "");
			replaceFirst = replaceFirst.replaceFirst(" ", "");
			Set<Entry<String, Integer>> entrySet = jetLinesMap.entrySet();
			// The jetLinesMap contains a map between the jet import part and each line it's on
			// We iterate each of the jet import lines to find the corresponding line

			for (Iterator<Entry<String, Integer>> iterator = entrySet.iterator(); iterator.hasNext();)
			{
				// we format the string to obtain only the import
				Entry<String, Integer> entry1 = iterator.next();
				String jetString = entry1.getKey();
				substring = substring.replaceAll("\\s", "");
				jetString = jetString.replaceAll("\\s", "") + ";";

				// we check to see if the import matches the code that is in the java part
				if (substring.equals(jetString))
				{
					repositionProblemInJetImportPart(problem, substring, entry1);

					break;
				}
			}
		}
		else
		{
			for (Entry<SourceRange, SourceRange> entry: java2Jet.entrySet())
			{
				SourceRange javaRange = entry.getKey();
				SourceRange jetRange = entry.getValue();

				// compute the max jet end line number and offset
				maxJetEndLineNumber = Math.max(maxJetEndLineNumber, jetRange.getStartLine());
				maxJetOffset = maxJetOffset > convertJava2JetOffset(javaRange.getStartOffset()) ? maxJetOffset
					: convertJava2JetOffset(javaRange.getStartOffset());

				if (lineNumber >= javaRange.startLine && lineNumber <= javaRange.endLine)
				{
					repositionProblemInJetCodePart(problem, lineNumber, javaRange, jetRange);
					return;
				}
			}

			if (lineNumber > convertJet2JavaLine(maxJetEndLineNumber))
			{
				int sourceEnd = maxJetOffset + 1;
				positionProblem(problem, maxJetEndLineNumber, maxJetOffset, sourceEnd);
			}
			else
			{
				// at this point, the problem could not be placed according with
				// Java
				// ranges, so the
				// problem is updated to point first position of the JET file.
				positionProblem(problem, 1, 0, 0);
			}
		}
	}

	private void positionProblem(final IProblem problem, int maxJetEndLineNumber, int maxJetOffset, int sourceEnd)
	{
		problem.setSourceLineNumber(maxJetEndLineNumber);
		problem.setSourceStart(maxJetOffset);
		problem.setSourceEnd(sourceEnd);
	}

	private void repositionProblemInJetCodePart(final IProblem problem, int lineNumber, SourceRange javaRange,
		SourceRange jetRange)
	{
		int textOffset = problem.getSourceStart() - javaRange.getStartOffset();
		int lineOffset = lineNumber - javaRange.startLine;

		// if offset exceed the JET range, compute the jet line number
		// in the middle
		lineOffset = calculateLineOffset(jetRange, lineOffset);

		textOffset = calculateTextOffset(jetRange, textOffset);

		// set computed JET line number into the problem

		int jetLineNumber = calculateJetLineNumber(lineOffset, jetRange);
		problem.setSourceLineNumber(jetLineNumber);

		// set computed JET offset into the problem
		// int jetOffset = jetRange.getStartOffset() + textOffset;
		int jetOffset = convertJava2JetOffset(javaRange.getStartOffset()) + textOffset;
		int len = problem.getSourceEnd() - problem.getSourceStart();

		problem.setSourceStart(jetOffset);
		problem.setSourceEnd(jetOffset + len + 1);
	}

	private void repositionProblemInJetImportPart(final IProblem problem, String substring,
		Entry<String, Integer> entry1)
	{
		int lineNr = entry1.getValue() + 1;
		problem.setSourceLineNumber(lineNr);
		String jetImportContent = getJetImportPart();
		String string = substring.replaceFirst(";", "");
		int indexOf = jetImportContent.indexOf(string);
		int char_Start = indexOf;
		int char_End = char_Start + 1 + string.length();
		problem.setSourceStart(char_Start);
		problem.setSourceEnd(char_End);
	}

	private int calculateLineOffset(SourceRange jetRange, int offset)
	{
		int jetOffset = 0;
		if (offset > jetRange.countLines())
		{
			jetOffset = jetRange.countLines() / 2;
		}
		else
		{
			jetOffset = offset;
		}
		return jetOffset;
	}

	private Map<String, Integer> convertJavaMarkerLine2JetMarkerLine(final IMarker problem) throws CoreException
	{
		int lineNumber = (int) problem.getAttribute(IMarker.LINE_NUMBER);
		int maxJetEndLineNumber = 0;
		int maxJetOffset = 0;
		Map<String, Integer> markerInfoMap = new HashMap<>();
		for (Entry<SourceRange, SourceRange> entry: java2Jet.entrySet())
		{
			SourceRange javaRange = entry.getKey();
			SourceRange jetRange = entry.getValue();

			// compute the max jet end line number and offset
			maxJetEndLineNumber = Math.max(maxJetEndLineNumber, jetRange.getStartLine());
			maxJetOffset = maxJetOffset > convertJava2JetOffset(javaRange.getStartOffset()) ? maxJetOffset
				: convertJava2JetOffset(javaRange.getStartOffset());

			if (lineNumber >= javaRange.startLine && lineNumber <= javaRange.endLine)
			{
				int textOffset = (int) problem.getAttribute(IMarker.CHAR_START) - javaRange.getStartOffset();
				int lineOffset = lineNumber - javaRange.startLine;

				lineOffset = calculateLineOffset(jetRange, lineOffset);

				textOffset = calculateTextOffset(jetRange, textOffset);

				// set computed JET line number into the problem
				int jetLineNumber = calculateJetLineNumber(lineOffset, jetRange);
				int jetOffset = convertJava2JetOffset(javaRange.getStartOffset()) + textOffset;
				int len = (int) problem.getAttribute(IMarker.CHAR_END) - (int) problem.getAttribute(IMarker.CHAR_START);

				markerInfoMap.put(JET_LINE_NUMBER, jetLineNumber);
				markerInfoMap.put(JET_CHAR_START, jetOffset);
				markerInfoMap.put(JET_CHAR_STOP, jetOffset + len + 1);

				return markerInfoMap;
			}
		}

		if (lineNumber > convertJet2JavaLine(maxJetEndLineNumber))
		{

			markerInfoMap.put(JET_LINE_NUMBER, maxJetEndLineNumber);
			markerInfoMap.put(JET_CHAR_START, maxJetOffset);
			markerInfoMap.put(JET_CHAR_STOP, maxJetOffset + 1);

			return markerInfoMap;
		}
		else
		{
			markerInfoMap.put(JET_LINE_NUMBER, 1);
			markerInfoMap.put(JET_CHAR_START, 0);
			markerInfoMap.put(JET_CHAR_STOP, 0);

			return markerInfoMap;
		}
	}

	private int calculateTextOffset(SourceRange jetRange, int offset)
	{
		int textOffset = 0;
		if (offset > jetRange.getEndOffset())
		{
			textOffset = jetRange.getEndOffset() - 1;
		}
		else
		{
			textOffset = offset;
		}
		return textOffset;
	}

	/**
	 * @param iMarker
	 * @param jetFile
	 * @param allProblems
	 * @return problem
	 */
	public IProblem convertJava2JetMarker(final IMarker iMarker, IFile jetFile, List<IProblem> allProblems)
	{
		IMarker marker = null;
		int lineNumber;
		IProblem problem = null;
		try
		{
			// we obtain all the problem markers that already exist on the jet file
			IMarker[] findMarkers = jetFile.findMarkers(IMarker.PROBLEM, true, IResource.DEPTH_INFINITE);

			lineNumber = (int) iMarker.getAttribute(IMarker.LINE_NUMBER);

			int jetLineWithProblem = 1;
			String javaCodeOnLineWithProblem = javaLinesMap.get(lineNumber - 1);
			String str_import = "import";
			int lastIndexOf = javaCodeOnLineWithProblem.lastIndexOf(str_import);
			// We check to see if the new problem that we want to create is in the import part or in the
			// expression/scriplet part of the jet
			if (lastIndexOf >= 0)
			{
				String substring = javaCodeOnLineWithProblem.substring(lastIndexOf + 6,
					javaCodeOnLineWithProblem.length());
				Set<Entry<String, Integer>> entrySet = jetLinesMap.entrySet();
				// The jetLinesMap contains a map between the jet import part and each line it's on
				// We iterate each of the jet import lines to find the corresponding line
				for (Iterator<Entry<String, Integer>> iterator = entrySet.iterator(); iterator.hasNext();)
				{
					// we format the string to obtain only the import
					Entry<String, Integer> entry = iterator.next();
					substring = substring.replaceAll("\\s", "");

					String string = entry.getKey();

					string = formatJetLineString(string);

					// we check to see if the import matches the code that is in the java part
					if (substring.equals(string))
					{
						// we obtain the jet import part to obtain the index of the problem, so that we can calculate
						// the exact start and end of the error
						String jetImportContent = getJetImportPart();
						string = string.replaceFirst(";", "");
						int indexOf = jetImportContent.indexOf(string);
						int char_Start = indexOf;
						int char_End = char_Start + 1 + string.length();
						String message = iMarker.getAttribute(IMarker.MESSAGE, "");
						jetLineWithProblem = entry.getValue();

						boolean problemExists = false;
						for (Iterator<IProblem> itr = allProblems.iterator(); itr.hasNext();)
						{
							IProblem iProblem = itr.next();
							if (iProblem.getMessage().equals(message))
							{
								if (iProblem.getSourceLineNumber() == (jetLineWithProblem + 1))
								{
									if (iProblem.getSourceStart() == char_Start)
									{
										problemExists = true;
										break;
									}

								}
								else if (iProblem.getSourceLineNumber() == 1)
								{
									iProblem.setSourceLineNumber(jetLineWithProblem + 1);
									iProblem.setSourceStart(char_Start);
									iProblem.setSourceEnd(char_End);
									problemExists = true;
									problem = iProblem;

									for (int i = 0; i < findMarkers.length; i++)
									{

										repositionMarkers(iMarker, findMarkers, jetLineWithProblem, char_Start,
											char_End, message, i);
									}

									break;
								}
							}

						}

						if (!problemExists)
						{
							problem = createMarkerAndProblemInJetImportPart(iMarker, jetFile, jetLineWithProblem,
								char_Start, char_End, message);
							break;
						}
					}
				}
			}
			else
			{

				Object message = iMarker.getAttribute(IMarker.MESSAGE);

				// If the problem is not inside the import part, then we have to convert the java marker line to a jet
				// marker line
				Map<String, Integer> markerInfoMap = convertJavaMarkerLine2JetMarkerLine(iMarker);

				// obtain the line, the start and the stop for the problem from the map we just created
				int jetLineProblem = markerInfoMap.get(JET_LINE_NUMBER);
				Integer charStart = markerInfoMap.get(JET_CHAR_START);
				Integer charStop = markerInfoMap.get(JET_CHAR_STOP);

				boolean problemExists = checkIfProblemExists(allProblems, message, jetLineProblem, charStart);

				if (!problemExists)
				{

					// Check to see if the marker already exists
					for (IMarker existingMarker: findMarkers)
					{

						Object existingLine_Number = existingMarker.getAttribute(IMarker.LINE_NUMBER);

						Object existingMessage = existingMarker.getAttribute(IMarker.MESSAGE);

						if (existingMessage.equals(message))
						{
							if (existingLine_Number.equals(jetLineProblem))
							{

								return null;

							}
						}
					}

					problem = createMarkerAndProblemInJetCodePart(iMarker, jetFile, message, jetLineProblem, charStart,
						charStop);
				}
			}

		}
		catch (

		CoreException e)

		{
			CessarPluginActivator.getDefault().logError(e);
		}
		return problem;

	}

	private IProblem createMarkerAndProblemInJetCodePart(final IMarker iMarker, IFile jetFile, Object message,
		int jetLineProblem, Integer charStart, Integer charStop) throws CoreException
	{
		IMarker marker;
		IProblem problem;
		// we create a new jet marker
		marker = createNewJetMarker(iMarker, jetFile);

		// we set the lineNumber, the start and the end of the problem
		marker.setAttribute(IMarker.LINE_NUMBER, jetLineProblem);
		marker.setAttribute(IMarker.CHAR_START, charStart);
		marker.setAttribute(IMarker.CHAR_END, charStop);
		String name = jetFile.getName();
		int severity = (int) marker.getAttribute(IMarker.SEVERITY);

		char[] charArray = name.toCharArray();

		// we create a new problem that will be returned to the jetServiceProvider
		problem = new DefaultProblem(charArray, (String) message, IProblem.ExternalProblemFixable, new String[0],
			severity, charStart, charStop, jetLineProblem, 0);
		return problem;
	}

	private IProblem createMarkerAndProblemInJetImportPart(final IMarker iMarker, IFile jetFile, int jetLineWithProblem,
		int char_Start, int char_End, String message) throws CoreException
	{
		IMarker marker;
		int lineNumber;
		IProblem problem;
		// if it matches we create a new jet marker
		marker = createNewJetMarker(iMarker, jetFile);

		// we set the line number for the marker
		marker.setAttribute(IMarker.LINE_NUMBER, jetLineWithProblem + 1);

		lineNumber = (int) marker.getAttribute(IMarker.LINE_NUMBER);

		String name = jetFile.getName();

		// we set the message , the severity and the exact start and end of the problem marker

		int severity = (int) iMarker.getAttribute(IMarker.SEVERITY);
		marker.setAttribute(IMarker.CHAR_START, char_Start);
		marker.setAttribute(IMarker.CHAR_END, char_End);
		char[] charArray = name.toCharArray();

		// we create a new problem that will be returned to the jetServiceProvider
		problem = new DefaultProblem(charArray, message, IProblem.ExternalProblemFixable, new String[0], severity,
			char_Start, char_End, jetLineWithProblem + 1, 0);
		return problem;
	}

	private void repositionMarkers(final IMarker iMarker, IMarker[] findMarkers, int jetLineWithProblem, int char_Start,
		int char_End, String message, int i) throws CoreException
	{
		IMarker existingMarker = findMarkers[i];
		int lineNr = (int) existingMarker.getAttribute(IMarker.LINE_NUMBER);
		if ((lineNr == 1) && (existingMarker.getAttribute(IMarker.MESSAGE).equals(message)))
		{
			existingMarker.setAttribute(IMarker.LINE_NUMBER, jetLineWithProblem + 1);
			existingMarker.setAttribute(IMarker.CHAR_START, char_Start);
			existingMarker.setAttribute(IMarker.CHAR_END, char_End);

			iMarker.setAttribute(IMarker.LINE_NUMBER, jetLineWithProblem + 1);
			iMarker.setAttribute(IMarker.CHAR_START, char_Start);
			iMarker.setAttribute(IMarker.CHAR_END, char_End);
		}
	}

	private String formatJetLineString(String string)
	{
		String jetLinestring = string.replaceAll("\\s", "") + ";";
		String str_imports = "imports=";
		if (jetLinestring.contains(str_imports))
		{
			jetLinestring = jetLinestring.replace(str_imports, "");
			jetLinestring = jetLinestring.replaceFirst("\"", "");
		}
		jetLinestring = jetLinestring.replaceFirst("\"", "");
		return jetLinestring;
	}

	/**
	 * Check to see if the newProblem already exists or not In order to consider that a problem already exists, it has
	 * to have the same message, to be on the same line and to start on exactly the same character We could have for
	 * example, the same message, the same line number but start on different characters, in that case the newProblem is
	 * valid
	 *
	 * @param allProblems
	 * @param message
	 * @param jetLineProblem
	 * @param charStart
	 * @return problemExists
	 */
	public boolean checkIfProblemExists(List<IProblem> allProblems, Object message, int jetLineProblem,
		Integer charStart)
	{
		boolean problemExists = false;
		for (Iterator<IProblem> iterator = allProblems.iterator(); iterator.hasNext();)
		{
			IProblem iProblem = iterator.next();
			if (iProblem.getMessage().equals(message) && (iProblem.getSourceLineNumber() == jetLineProblem)
				&& (iProblem.getSourceStart() == charStart))
			{
				problemExists = true;
				break;
			}

		}
		return problemExists;
	}

	private String getJetImportPart()
	{
		String jetImportSource = directiveRange.toString();
		Pattern p = Pattern.compile("(<%.*?%>)", Pattern.DOTALL);
		// Pattern p = Pattern.compile("imports=\"(.*?)\"", Pattern.DOTALL);
		// get a matcher object
		Matcher m = p.matcher(jetImportSource);
		String jetImportContent = "";
		while (m.find())
		{

			jetImportContent = m.group();
		}
		return jetImportContent;
	}

	private IMarker createNewJetMarker(final IMarker iMarker, IFile jetFile) throws CoreException
	{
		IMarker marker;
		marker = jetFile.createMarker(IMarker.PROBLEM);
		marker.setAttribute(IMarker.PRIORITY, IMarker.PRIORITY_NORMAL);
		if (iMarker.getAttribute(IMarker.SEVERITY).equals(IMarker.SEVERITY_ERROR))
		{
			marker.setAttribute(IMarker.SEVERITY, IMarker.SEVERITY_ERROR);
		}
		else if (iMarker.getAttribute(IMarker.SEVERITY).equals(IMarker.SEVERITY_WARNING))
		{
			marker.setAttribute(IMarker.SEVERITY, IMarker.SEVERITY_WARNING);
		}
		else
		{
			marker.setAttribute(IMarker.SEVERITY, IMarker.SEVERITY_INFO);
		}
		marker.setAttribute(IMarker.MESSAGE, iMarker.getAttribute(IMarker.MESSAGE, ""));
		return marker;
	}

	void createJavaLinesMap()
	{
		String[] javaSourceSplitted = javaSource.split("\\n", 0);
		javaLinesMap = new HashMap<>();
		for (int i = 0; i < javaSourceSplitted.length; i++)
		{
			javaLinesMap.put(i, javaSourceSplitted[i]);
		}
	}

	void createJetLinesMap()
	{
		String jetImportContent = getJetImportPart();

		String[] jetImportContentSplitted = jetImportContent.split("\\n", 0);
		jetLinesMap = new HashMap<>();
		for (int i = 0; i < jetImportContentSplitted.length; i++)
		{

			jetLinesMap.put(jetImportContentSplitted[i], i);
		}
	}

	private void saveJavaSourceIfNeeded(boolean inDebugMode, final ByteArrayOutputStream outStream,
		final IProgressMonitor monitor)
	{
		ByteArrayInputStream stream = null;
		try
		{
			IProject project = contentProvider.getJavaProject().getProject();
			boolean saveJava = inDebugMode;
			IFolder saveFolder = JETCoreUtils.getDumpJavaSourceFolderPref(project);

			if (!saveJava)
			{
				// maybe the project setting is enabled, check that
				saveJava = JETCoreUtils.getDumpJavaSourcePref(project);
			}
			if (saveJava)
			{
				String srcPackage = skeleton.getPackageName();
				String srcClass = skeleton.getClassName();
				String packPath = srcPackage.replace('.', '/');
				Path path = new Path(packPath + File.separator + srcClass + ".java");
				IFile file = saveFolder.getFile(path);
				IPath filePath = file.getProjectRelativePath();

				javaIFile = (IFile) ResourceUtils.createResource(project, filePath, true, monitor);

				if (javaIFile.exists())
				{
					IWorkspaceRunnable runnable = new IWorkspaceRunnable()
					{
						public void run(IProgressMonitor iMonitor) throws CoreException
						{

							javaIFile.delete(true, iMonitor);
						}
					};

					ResourcesPlugin.getWorkspace().run(runnable, javaIFile.getProject(), 0, monitor);

				}

				stream = new ByteArrayInputStream(outStream.toByteArray());
				javaIFile.create(stream, true, monitor);
				javaIFile.touch(monitor);
			}
		}
		catch (CoreException e)
		{
			CessarPluginActivator.getDefault().logWarning(e);
		}
		finally
		{
			try
			{
				if (outStream != null)
				{
					outStream.close();
				}

				if (stream != null)
				{
					stream.close();
				}
			}
			catch (IOException e)
			{
				CessarPluginActivator.getDefault().logError(e);
			}
		}
	}

	/**
	 * Handle the directive part from a Jet File. Modified to generated the package name from the filename and location
	 * in the project
	 */
	@Override
	public void handleDirective(final String directive, final CessarJETMark start, final CessarJETMark stop,
		final Map<String, String> attributes) throws JETException
	{
		pushRangeToRangeStack(start, stop);
		if (JETCoreConstants.JET.equals(directive))
		{
			internalHandleDirective(start, attributes);
		}
		else
		{
			super.handleDirective(directive, start, stop, attributes);
		}
	}

	/**
	 * @return
	 */
	private boolean useExtendedSkeleton()
	{
		return contentProvider.getJetComplianceLevel().getComplianceSettings().useExtendedSkeleton();
	}

	/**
	 *
	 */
	private void initSkeleton()
	{
		if (skeleton == null)
		{
			if (useExtendedSkeleton())
			{
				skeleton = new JETSkeletonExtended();
				((JETSkeletonExtended) skeleton).setOutputFileName(
					JETCoreUtils.getJetOutputFileName(contentProvider.getJETFileName()));
			}
			else
			{
				skeleton = new JETSkeleton();
			}

			String outputClass = contentProvider.getOutputClass();
			if (outputClass != null)
			{
				skeleton.setClassName(outputClass);
			}
		}
	}

	/**
	 * Custom implementation of {@link JETCompiler#handleDirective(String, JETMark, JETMark, Map)} method, needed to
	 * insert an interface from which JET class will inherit.
	 */
	private void internalHandleDirective(final CessarJETMark start, final Map<String, String> attributes)
		throws JETException
	{
		// Process this first.
		initSkeleton();

		String skeletonURI = attributes.get("skeleton");
		if (skeletonURI != null)
		{
			try
			{
				BufferedInputStream bufferedInputStream = new BufferedInputStream(
					openStream(resolveLocation(templateURIPath, templateURI, skeletonURI)[1]));
				byte[] input = new byte[bufferedInputStream.available()];
				bufferedInputStream.read(input);
				bufferedInputStream.close();
				skeleton.setCompilationUnitContents(new String(input));
			}
			catch (IOException exception)
			{
				throw new JETException(exception);
			}
		}

		for (Map.Entry<String, String> entry: attributes.entrySet())
		{
			// Ignore this now
			if ("skeleton".equals(entry.getKey()))
			{
				// Ignore
			}
			else if ("package".equals(entry.getKey()))
			{
				skeleton.setPackageName(entry.getValue());
			}
			else if ("imports".equals(entry.getKey()))
			{
				String importList = entry.getValue();

				CessarJETMark currentMark = reader.mark();
				for (StringTokenizer stringTokenizer = new StringTokenizer(importList,
					" \t\n\r"); stringTokenizer.hasMoreTokens();)
				{
					reader.reset(start);
					reader.skipUntil("imports");
					String token = stringTokenizer.nextToken();
					if (token.length() > 0)
					{
						validateImportPart(start, token);
					}
				}
				skeleton.addImports(importList);
				reader.reset(currentMark);
			}
			else if ("class".equals(entry.getKey()))
			{
				skeleton.setClassName(entry.getValue());
			}
			else if ("parallel".equals(entry.getKey()) && (skeleton instanceof JETSkeletonExtended))
			{
				((JETSkeletonExtended) skeleton).setParallel(entry.getValue());
			}
			else if ("nlString".equals(entry.getKey()))
			{
				skeleton.setNLString(entry.getValue());
			}
			else if ("startTag".equals(entry.getKey()))
			{
				parser.setStartTag(entry.getValue());
			}
			else if ("endTag".equals(entry.getKey()))
			{
				parser.setEndTag(entry.getValue());
			}
			else if ("version".equals(entry.getKey()))
			{
				// Ignore the version
			}
			else
			{
				throw new JETException(CodeGenPlugin.getPlugin().getString("jet.error.bad.attribute",
					new Object[] {entry.getKey(), start.format("jet.mark.file.line.column")}));
			}
		}
		fSavedLine = null;
	}

	/**
	 * @param start
	 * @param token
	 * @throws JETException
	 */
	private void validateImportPart(CessarJETMark start, String token) throws JETException
	{
		CessarJETMark problemMark = null;
		int startPosition = 0;
		int endPosition = 0;

		if (Keywords.isJavaReservedWord(token))
		{
			// necessary to make sure it is found by
			// reader.skipUntil()
			String tokenToFind = token.charAt(0) + token.substring(1).toLowerCase();
			problemMark = reader.skipUntil(tokenToFind);
			char[] chars = reader.getChars(start, problemMark);
			if (chars != null)
			{
				startPosition = chars.length;
				endPosition = startPosition + token.length();
			}
			throw createJETException(JETCoreMessages.error_ReservedWordInImport, problemMark, startPosition,
				endPosition, token);
		}

		char ch = token.charAt(0);
		if (!Character.isJavaIdentifierStart(ch))
		{
			// necessary to make sure it is found by
			// reader.skipUntil()
			String tokenToFind = token.charAt(0) + token.substring(1).toLowerCase();
			problemMark = reader.skipUntil(tokenToFind);
			char[] chars = reader.getChars(start, problemMark);
			if (chars != null)
			{
				startPosition = chars.length;
				endPosition = startPosition + token.length();
			}
			throw createJETException(JETCoreMessages.error_invalidIdentifierStartInImport, problemMark, startPosition,
				endPosition, ch);
		}

		ch = token.charAt(token.length() - 1);
		if (ch == '.')
		{
			// get the last part of the token, the one that ends
			// with '.'
			String[] subTokens = token.split("\\.");
			String part = subTokens[subTokens.length - 1];
			String partToFind = part.charAt(0) + part.substring(1).toLowerCase();
			problemMark = reader.skipUntil(partToFind);
			char[] charsBetween = reader.getChars(start, problemMark);
			if (charsBetween != null)
			{
				startPosition = charsBetween.length + part.length();
				endPosition = startPosition + 1;
			}
			throw createJETException(JETCoreMessages.error_invalidIdentifierEndInImport, problemMark, startPosition,
				endPosition, ch);
		}

		String[] subTokens = token.split("\\.");
		int tIdx = 0;
		for (String part: subTokens)
		{
			// fix '..' sequence [uidu0944]
			if (part.length() == 0)
			{
				int prevIdx = tIdx - 1;
				if (prevIdx >= 0 && subTokens[prevIdx].length() > 0)
				{
					String tokenToFind = subTokens[prevIdx].charAt(0) + subTokens[prevIdx].substring(1).toLowerCase();
					problemMark = reader.skipUntil(tokenToFind);
					char[] chars = reader.getChars(start, problemMark);
					if (chars != null)
					{
						startPosition = reader.getChars(start, problemMark).length;
						endPosition = startPosition + subTokens[prevIdx].length();
					}
					throw createJETException(JETCoreMessages.error_invalidIdentifierPartInImport, problemMark,
						startPosition, endPosition, "..");
				}
			}
			for (int i = 0; i < part.length(); i++)
			{
				ch = part.charAt(i);
				if (ch != '*' && !Character.isJavaIdentifierPart(ch))
				{
					String partToFind = part.charAt(0) + part.substring(1).toLowerCase();
					problemMark = reader.skipUntil(partToFind);
					char[] chars = reader.getChars(start, problemMark);
					if (chars != null)
					{
						startPosition = reader.getChars(start, problemMark).length;
						endPosition = startPosition + part.length();
					}
					throw createJETException(JETCoreMessages.error_invalidIdentifierPartInImport, problemMark,
						startPosition, endPosition, ch);
				}
				else
				{
					// no other characters allowed after '*' character in the
					// import statement
					int prevIdx = i - 1;
					if ((ch != '\n' || ch != '\r' || ch != '\t') && prevIdx >= 0 && part.charAt(prevIdx) == '*')
					{
						String tokenToFind = subTokens[prevIdx].charAt(0)
							+ subTokens[prevIdx].substring(1).toLowerCase();
						problemMark = reader.skipUntil(tokenToFind);
						char[] chars = reader.getChars(start, problemMark);
						if (chars != null)
						{
							startPosition = reader.getChars(start, problemMark).length;
							endPosition = startPosition + subTokens[prevIdx].length();
						}
						throw createJETException(JETCoreMessages.error_invalidIdentifierPartInImport, problemMark,
							startPosition, endPosition, part);
					}
				}
			}
			tIdx++;
		}
	}

	/**
	 * @param key
	 * @param problemMark
	 * @param startPosition
	 * @param endPosition
	 * @param arguments
	 * @return
	 */
	private JETException createJETException(String key, CessarJETMark problemMark, int startPosition, int endPosition,
		Object... arguments)
	{
		int line = getMemberValue(problemMark, "line") + 1;
		int col = getMemberValue(problemMark, "col") + 1;
		String lineColumnInfo = "(" + line + "," + col + ")";
		return new JETException(JETCoreMessages.format(key, arguments) + (problemMark != null ? lineColumnInfo : "")
			+ "[" + startPosition + "," + endPosition + "]");
	}

	private void pushRangeToRangeStack(final CessarJETMark start, final CessarJETMark stop)
	{
		char[] chars = reader.getChars(start, stop);
		int startLine = getMemberValue(start, "line") + 1;
		int endLine = getMemberValue(stop, "line") + 1;

		SourceRange jetRange = new SourceRange(start.getCursor(), stop.getCursor() + 2/* "%>" */, startLine, endLine,
			chars);
		rangeStack.push(jetRange);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.emf.codegen.jet.JETCompiler#handleScriptlet(org.eclipse.emf.codegen.jet.JETMark,
	 * org.eclipse.emf.codegen.jet.JETMark, java.util.Map)
	 */
	@Override
	public void handleScriptlet(final CessarJETMark start, final CessarJETMark stop,
		final Map<String, String> attributes) throws JETException
	{
		pushRangeToRangeStack(start, stop);
		super.handleScriptlet(start, stop, attributes);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 *
	 * org.eclipse.emf.codegen.jet.JETParseEventListener#handleExpression(org.eclipse.emf.codegen.jet.JETMark,
	 * org.eclipse.emf.codegen.jet.JETMark, java.util.Map)
	 */
	@Override
	public void handleExpression(final CessarJETMark start, final CessarJETMark stop,
		final Map<String, String> attributes) throws JETException
	{
		pushRangeToRangeStack(start, stop);
		super.handleExpression(start, stop, attributes);
	}

	@Override
	public void addGenerator(final JETGenerator gen) throws JETException
	{
		SourceRange jetRange = rangeStack.pop();
		jetGenerators2JetRanges.put(gen, jetRange);
		super.addGenerator(gen);
	}

	@Override
	public void doAddCharDataGenerator(char[] chars) throws JETException
	{
		SourceRange jetRange = new SourceRange(0, chars.length, chars);

		if (fUseStaticFinalConstants)
		{
			JETConstantDataGenerator2 gen = constantDictionary.get(chars);
			if (gen == null)
			{
				if (constantCount == 0)
				{
					chars = stripFirstNewLineWithBlanks(chars);
				}
				++constantCount;
				String label = CONSTANT_PREFIX + constantCount;
				gen = new JETConstantDataGenerator2(chars, label);
				constantDictionary.put(chars, gen);
				constants.add(gen);
			}
			generators.add(gen);
			jetGenerators2JetRanges.put(gen, jetRange);
		}
		else
		{
			JETCharDataGenerator gen2 = new JETCharDataGenerator2(chars);
			generators.add(gen2);
			jetGenerators2JetRanges.put(gen2, jetRange);
		}
	}

	private int getMemberValue(final CessarJETMark mark, final String memberName)
	{
		Field field;
		try
		{
			field = mark.getClass().getDeclaredField(memberName);
		}
		catch (Exception e)
		{
			return 1;
		}
		boolean accessible = field.isAccessible();
		try
		{
			field.setAccessible(true);
			Object object = field.get(mark);

			if (object instanceof Integer)
			{
				return ((Integer) object).intValue();
			}
		}
		catch (Exception e)
		{
			// ignore exception return default value
		}
		finally
		{
			field.setAccessible(accessible);
		}
		return 1;
	}

	String getJavaSource()
	{
		return javaSource;
	}

	String getJavaClassName()
	{

		if (skeleton == null)
		{
			initSkeleton();
		}
		return skeleton.getClassName();

	}

	String getJavaPackage()
	{
		return skeleton.getPackageName();
	}

	List<IProblem> getProblems()
	{
		return problems;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return contentProvider.getJETFileName();
	}

	/**
	 * @return
	 */
	public EJetExecutionMode getExecutionMode()
	{
		if (!(skeleton instanceof JETSkeletonExtended))
		{
			return EJetExecutionMode.SEQUENTIAL;
		}
		else
		{
			if (((JETSkeletonExtended) skeleton).isParallel())
			{
				return EJetExecutionMode.PARALLEL;
			}
			else
			{
				return EJetExecutionMode.SEQUENTIAL;
			}
		}
	}
}
