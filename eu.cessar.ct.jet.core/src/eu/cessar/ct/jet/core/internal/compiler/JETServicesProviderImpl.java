package eu.cessar.ct.jet.core.internal.compiler;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.codegen.jet.JETException;
import org.eclipse.jdt.core.*;
import org.eclipse.jdt.core.compiler.IProblem;

import eu.cessar.ct.core.platform.util.ResourceUtils;
import eu.cessar.ct.jet.core.EJetExecutionMode;
import eu.cessar.ct.jet.core.JETCoreConstants;
import eu.cessar.ct.jet.core.JETCoreUtils;
import eu.cessar.ct.jet.core.compiler.IJETContentProvider;
import eu.cessar.ct.jet.core.compiler.IJETServicesProvider;
import eu.cessar.ct.jet.core.internal.CessarPluginActivator;
import eu.cessar.ct.sdk.logging.LoggerFactory;

/**
 * Internal implementation of {@link IJETServicesProvider} interface. This class contains an implementation of all JET
 * transformation services that can be provided: transform, compile, get problems, convert JET to Java offsets.
 */
public class JETServicesProviderImpl implements IJETServicesProvider
{
	/**
	 *
	 */
	private static final String PACKAGE = "package"; //$NON-NLS-1$

	/** the JET content provider for which the implemented services are provided */
	private IJETContentProvider contentProvider;

	/**
	 * the transformer implementation that allow conversion of a JET text into Java source code
	 */
	private JETTransformer transformer;

	/**
	 * the array where are stored the problems found during JET transformation or Java code compilation
	 */
	private List<IProblem> problems;

	private List<IProblem> restOfproblems = new ArrayList<>();

	/** JDT compilation unit for Java source code obtained out of a JET file */
	private ICompilationUnit compilationUnit;

	/**
	 * Default class constructor
	 */
	public JETServicesProviderImpl()
	{
		problems = new ArrayList<>();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.jet.core.compiler.IJETServicesProvider#setContentProvider(eu.cessar.ct.jet.core.compiler.
	 * IJETContentProvider)
	 */
	public void setContentProvider(final IJETContentProvider provider)
	{
		contentProvider = provider;

		// forget about old compilation result; when needed create a new one
		touch();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.jet.core.compiler.IJETServicesProvider#touch()
	 */
	public synchronized void touch()
	{
		// forget about old compilation result; when needed create a new one
		transformer = null;
		compilationUnit = null;
		problems.clear();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.jet.core.compiler.IJETServicesProvider#isValid()
	 */
	public boolean isValid()
	{
		for (IProblem prob: problems)
		{
			if (prob.isError())
			{
				return false; // an error was found
			}
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.jet.core.compiler.IJETServicesProvider#getProblems()
	 */
	public List<IProblem> getProblems()
	{
		return problems;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.jet.core.compiler.IJETServicesProvider#transform(org.eclipse.core.runtime.IProgressMonitor)
	 */
	public synchronized void transform(boolean inDebugMode, final IProgressMonitor monitor)
	{
		if (transformer == null)
		{

			problems.clear();
			try
			{
				transformer = new JETTransformer(contentProvider);
				transformer.transform(true, monitor);
				problems.addAll(transformer.getProblems());
			}
			catch (JETException | CoreException e)
			{
				CessarPluginActivator.getDefault().logError(e);
			}

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.jet.core.compiler.IJETServicesProvider#getJavaSource()
	 */
	public String getJavaSource()
	{
		if (transformer != null)
		{
			return transformer.getJavaSource();
		}
		return null;
	}

	public String getJETClassName()
	{
		if (transformer != null)
		{
			return transformer.getJavaClassName();
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.jet.core.compiler.IJETServicesProvider#convertJet2JavaOffset(int)
	 */
	public int convertJet2JavaOffset(final int offset)
	{
		if (transformer != null)
		{
			return transformer.convertJet2JavaOffset(offset);
		}
		return -1;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.jet.core.compiler.IJETServicesProvider#convertJava2JetOffset(int)
	 */
	public int convertJava2JetOffset(final int javaOoffset)
	{
		if (transformer != null)
		{
			return transformer.convertJava2JetOffset(javaOoffset);
		}
		return -1;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.jet.core.compiler.IJETServicesProvider#convertJava2JetLine(int)
	 */
	public int convertJava2JetLine(final int lineNumber)
	{
		if (transformer != null)
		{
			return transformer.convertJava2JetLine(lineNumber);
		}

		return -1;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.jet.core.compiler.IJETServicesProvider#convertJet2JavaLine(int)
	 */
	public int convertJet2JavaLine(int lineNumber)
	{
		if (transformer != null)
		{
			return transformer.convertJet2JavaLine(lineNumber);
		}

		return -1;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * eu.cessar.ct.jet.core.compiler.IJETServicesProvider#getCompilationUnit(org.eclipse.core.runtime.IProgressMonitor)
	 */
	public ICompilationUnit getCompilationUnit(boolean inDebugMode, final IProgressMonitor monitor)
		throws JavaModelException
	{
		if (compilationUnit == null)
		{
			IJavaProject javaProject = contentProvider.getJavaProject();
			IProject project = javaProject.getProject();
			try
			{
				IFolder saveFolder = JETCoreUtils.getDumpJavaSourceFolderPref(project);
				createSaveFolderIfNeededAndBuildProject(saveFolder);
				boolean sourceFolderIsAdded = false;
				IClasspathEntry[] oldEntries;

				oldEntries = javaProject.getRawClasspath();

				for (IClasspathEntry iClasspathEntry: oldEntries)
				{
					if (iClasspathEntry.getPath().equals(saveFolder.getFullPath()))
					{
						sourceFolderIsAdded = true;
					}
				}
				if (!sourceFolderIsAdded)
				{
					// +1 for our src/main/java entry
					IClasspathEntry[] newEntries = new IClasspathEntry[oldEntries.length + 1];

					System.arraycopy(oldEntries, 0, newEntries, 0, oldEntries.length);

					IPackageFragmentRoot packageRoot = javaProject.getPackageFragmentRoot(saveFolder);
					newEntries[oldEntries.length] = JavaCore.newSourceEntry(packageRoot.getPath(), new Path[] {});
					javaProject.setRawClasspath(newEntries, null);
				}
			}
			catch (JavaModelException e)
			{
				CessarPluginActivator.getDefault().logError(e);
			}

			IFolder saveFolder = JETCoreUtils.getDumpJavaSourceFolderPref(project);
			transform(inDebugMode, monitor);
			IPackageFragment iPackageFragment = null;
			IPackageFragment[] packageFragments = javaProject.getPackageFragments();
			for (int i = 0; i < packageFragments.length; i++)
			{
				IPath packagePath = packageFragments[i].getPath();
				String saveFolderName = saveFolder.getName();
				if (packagePath.toString().endsWith(saveFolderName))
				{
					iPackageFragment = packageFragments[i];
					break;
				}
			}
			if (iPackageFragment == null)
			{
				iPackageFragment = packageFragments[0];
			}
			ICompilationUnit icu = iPackageFragment.getCompilationUnit(getJETClassName() + JETCoreConstants.JAVA);
			IProblemRequestor problemRequestor = new IProblemRequestor()
			{

				public void acceptProblem(IProblem problem)
				{

					String message = problem.getMessage();
					if (!message.contains(PACKAGE))
					{
						IProblem[] test = new IProblem[] {problem};
						List<IProblem> cleanUpdateProblems = transformer.cleanUpdateProblems(test);
						for (IProblem newProblem: cleanUpdateProblems)
						{
							int sourceLineNumber = newProblem.getSourceLineNumber();
							int sourceStart = newProblem.getSourceStart();
							boolean problemExists = transformer.checkIfProblemExists(problems, message,
								sourceLineNumber, sourceStart);
							if (!problemExists)
							{
								problems.add(newProblem);
							}

						}

					}

				}

				public void beginReporting()
				{
					// DoNothing
				}

				public void endReporting()
				{
					// DoNothing

				}

				public boolean isActive()
				{
					return true;
				} // will detect problems if active
			};
			WorkingCopyOwner wco = new WorkingCopyOwner()
			{
				/*
				 * (non-Javadoc)
				 *
				 * @see org.eclipse.jdt.core.WorkingCopyOwner#getProblemRequestor(org.eclipse.jdt.core.ICompilationUnit)
				 */
				@Override
				public IProblemRequestor getProblemRequestor(ICompilationUnit workingCopy)
				{
					return problemRequestor;
				}
			};

			ICompilationUnit workingCopy = icu.getWorkingCopy(wco, monitor);

			workingCopy.getBuffer().setContents(transformer.getJavaSource());
			compilationUnit = workingCopy;

		}
		return compilationUnit;
	}

	private synchronized void createSaveFolderIfNeededAndBuildProject(IFolder saveFolder)
	{
		if (saveFolder == null || !saveFolder.exists())
		{
			try
			{
				ResourceUtils.mkDirs(saveFolder, null);

			}
			catch (CoreException e)
			{
				LoggerFactory.getLogger().log(e);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.jet.core.compiler.IJETServicesProvider#compile(org.eclipse.core.runtime.IProgressMonitor)
	 */
	public synchronized void compile(boolean inDebugMode, final IProgressMonitor monitor)
	{
		try
		{
			if (compilationUnit != null)
			{
				compilationUnit.close();

				compilationUnit = null;
			}
			getCompilationUnit(inDebugMode, monitor);

			WorkingCopyOwner owner = compilationUnit.getOwner();

			if (compilationUnit != null)
			{

				compilationUnit.reconcile(ICompilationUnit.NO_AST, true, owner, monitor);

			}

			IFile jetFile = getJetFile();
			JETCoreUtils.updateJETMarkers(jetFile, problems);
			IFile javaFile = getJavaFile(monitor);
			IResourceChangeListener listener = new IResourceChangeListener()
			{

				@Override
				public void resourceChanged(IResourceChangeEvent event)
				{

					try
					{
						if (getJavaFile(null) != null)
						{
							IMarker[] findMarkers = javaFile.findMarkers(IMarker.PROBLEM, true,
								IResource.DEPTH_INFINITE);
							// we remove the jet error markers because we are going to recreate each one on the specific
							// line
							jetFile.deleteMarkers(IMarker.PROBLEM, true, IResource.DEPTH_INFINITE);
							// we create the java lines map and the jet lines map so that we can use them to set the
							// error markers
							transformer.createJavaLinesMap();
							transformer.createJetLinesMap();

							for (IMarker iMarker: findMarkers)
							{

								IFile jetFileToUse = getJetFile();

								if (transformer != null)
								{
									// each java error marker that we found has to be changed into a jet marker on the
									// corresponding jet line
									IProblem problem = transformer.convertJava2JetMarker(iMarker, jetFileToUse,
										problems);
									if (problems != null && problem != null)
									{
										int sourceLineNumber = problem.getSourceLineNumber();
										int sourceStart = problem.getSourceStart();
										String message = problem.getMessage();
										// we check if the problem exists already or not
										boolean problemExists = transformer.checkIfProblemExists(problems, message,
											sourceLineNumber, sourceStart);
										if (!problemExists)
										{
											problems.add(problem);
										}
									}
								}
							}

							// we delete the java file error markers because we don't want to show them any more inside
							// the ProblemsView. We want to only have jet markers visible.
							javaFile.deleteMarkers(IMarker.PROBLEM, true, IResource.DEPTH_INFINITE);

						}
						ResourcesPlugin.getWorkspace().removeResourceChangeListener(this);
					}
					catch (Exception e)
					{
						CessarPluginActivator.getDefault().logError(e);
					}
				}
			};
			ResourcesPlugin.getWorkspace().addResourceChangeListener(listener, IResourceChangeEvent.POST_BUILD);

		}
		catch (

		CoreException e)

		{
			CessarPluginActivator.getDefault().logError(e);
		}

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.jet.core.compiler.IJETServicesProvider#getClassFiles()
	 */
	public IClassFile[] getClassFiles()
	{

		IJavaProject javaProject = compilationUnit.getJavaProject();

		String outputLocation = null;
		try
		{
			outputLocation = javaProject.getOutputLocation().toString();
		}
		catch (JavaModelException e)
		{
			CessarPluginActivator.getDefault().logError(e);
		}
		String projPath = javaProject.getProject().getLocation().toString();
		projPath = projPath.substring(0, projPath.lastIndexOf("/")); //$NON-NLS-1$
		String outPath = projPath + outputLocation;
		String javaPackage = getJavaPackage();
		String replace = javaPackage.replace(".", "/"); //$NON-NLS-1$ //$NON-NLS-2$
		File file = new File(outPath + "/" + replace + "/"); //$NON-NLS-1$ //$NON-NLS-2$
		List<IClassFile> classlist = new ArrayList<>();
		if (file.exists() && file.isDirectory())
		{
			String jetClassName = getJETClassName();
			for (File f: file.listFiles())
			{

				if (f.getName().startsWith(jetClassName))
				{
					IWorkspace workspace = ResourcesPlugin.getWorkspace();
					IPath location = Path.fromOSString(f.getAbsolutePath());
					IFile ifile = workspace.getRoot().getFileForLocation(location);
					IClassFile createClassFileFrom = JavaCore.createClassFileFrom(ifile);
					classlist.add(createClassFileFrom);
				}
			}
		}
		return classlist.toArray(new IClassFile[0]);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.jet.core.compiler.IJETServicesProvider#findNextScripletOrExpression(int)
	 */
	public SourceRange findNextScripletOrExpression(int number)
	{
		if (transformer != null)
		{
			return transformer.findNextScripletOrExpression(number);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.jet.core.compiler.IJETServicesProvider#getJetFile()
	 */
	public IFile getJetFile()
	{
		if (contentProvider != null)
		{
			return contentProvider.getJETFile();
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.jet.core.compiler.IJETServicesProvider#getJavaFile()
	 */
	public IFile getJavaFile(IProgressMonitor monitor)
	{
		transform(true, monitor);
		return transformer.getJavaFile();
	}

	public EJetExecutionMode getExecutionMode(IProgressMonitor monitor)
	{
		transform(false, monitor);
		return transformer.getExecutionMode();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.jet.core.compiler.IJETServicesProvider#getJavaPackage()
	 */
	public String getJavaPackage()
	{
		return transformer.getJavaPackage();
	}
}
