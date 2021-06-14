package eu.cessar.ct.jet.core.compiler;

import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IClassFile;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.compiler.IProblem;

import eu.cessar.ct.jet.core.EJetExecutionMode;
import eu.cessar.ct.jet.core.internal.compiler.SourceRange;
import eu.cessar.req.Requirement;

/**
 * The interface through which the clients can have access to JET services.<br>
 * This interface is not intended to be implemented by the clients. A concrete implementation of this interface can be
 * obtained through {@link JETServicesProviderFactory} class.
 */
@SuppressWarnings("restriction")
@Requirement(
	reqID = "300982")
public interface IJETServicesProvider
{
	/**
	 * Allow clients to specify a JET content provider. Calling this method will touch (reset) the internal state of the
	 * JET services provider.
	 *
	 * @param contentProvider
	 *        an {@link IJETContentProvider} instance, but must not be <code>null</code>
	 */
	void setContentProvider(IJETContentProvider contentProvider);

	/**
	 * Force to clear all internal content and to put the object in the initial state. (Force transformation or
	 * compilation of the JET file).
	 */
	void touch();

	/**
	 * Transform the JET provided by specified {@link IJETContentProvider} instance to Java source code. If problems
	 * were found during transformation, the clients can get these problems using {@link #getProblems()} method.
	 *
	 * @param inDebugMode
	 *        if <code>true</code>, the Java source will be dumped on the disk
	 * @param monitor
	 *        a progress monitor instance
	 */
	void transform(boolean inDebugMode, IProgressMonitor monitor);

	/**
	 * COmpile the Java source code obtained by calling {@link #transform(IProgressMonitor)} method, into binary code.
	 * <br>
	 * If Java source code is not available, yet, this method will transform the JET first (by calling
	 * {@link #transform(IProgressMonitor)}) and if no error occurred during transformation, compile the Java source
	 * code.<br>
	 * If problems were found during compilation, the clients can get these problems using {@link #getProblems()}
	 * method.
	 *
	 * @param inDebugMode
	 *        if <code>true</code>, the Java source will be dumped on the disk
	 * @param monitor
	 *        a progress monitor instance
	 */
	void compile(boolean inDebugMode, IProgressMonitor monitor);

	/**
	 * This method specify if JET file does not have errors after transformation or compilation of the Java code. Method
	 * {@link #getProblems()} might still return warnings.
	 *
	 * @return <code>true</code> if the JET files has no ERRORS, but might have WARNINGS, <code>false</code> otherwise.
	 */
	boolean isValid();

	/**
	 * Return an array with the problems (ERRORS or WARNINGS) found during the transformation or the compilation of the
	 * JET file.
	 *
	 * @return An array with the problems (ERRORS or WARNINGS), or empty array if everything is OK, but never
	 *         <code>null</code>.
	 */
	List<IProblem> getProblems();

	/**
	 * Return the Java source obtained from JET transformation.
	 *
	 * @return The Java source as a String if the JET was transformed, or <code>null</code> if the source is not
	 *         accessible.
	 */
	String getJavaSource();

	/**
	 * Gets the root JET class name (NOT qualified name of the class)
	 *
	 * @return The name of the Java class, or <code>null</code> if the JET was not processed yet.
	 */
	String getJETClassName();

	/**
	 * This method allow clients to obtain the binary code for the Java classes obtained after transformation and
	 * compilation of a JET file. This binary code can be saved into a .class file (into a jar file).
	 *
	 * @return The binary code obtained after JET transformation and compilation.
	 */
	IClassFile[] getClassFiles();

	/**
	 * Obtain a JDT {@link ICompilationUnit} implementation for a JET file. Such compilation unit can be used to provide
	 * code assistance.
	 *
	 * @param inDebugMode
	 *        if <code>true</code>, the Java source will be dumped on the disk
	 * @param monitor
	 *        a progress monitor instance
	 * @return A {@link ICompilationUnit} instance, or <code>null</code> if JET file is invalid.
	 * @throws JavaModelException
	 *         Thrown if input data are invalid or not accessible.
	 */
	public ICompilationUnit getCompilationUnit(boolean inDebugMode, IProgressMonitor monitor) throws JavaModelException;

	/**
	 * This method can be used by clients (or editors) to convert a JET offset into corresponding Java offset.
	 *
	 * @param jetOoffset
	 *        the JET offset to be converted
	 * @return The corresponding Java offset if can be computed, or -1 otherwise.
	 */
	public int convertJet2JavaOffset(int jetOoffset);

	/**
	 * This method can be used by clients (or editors) to convert a Java offset into corresponding JET offset.
	 *
	 * @param javaOoffset
	 *        the Java offset to be converted
	 * @return The corresponding JET offset if can be computed, or -1 otherwise.
	 */
	public int convertJava2JetOffset(int javaOoffset);

	/**
	 * Converts a Java line number into a Jet line number.
	 *
	 * @param lineNumber
	 *        the Java line number to be converted
	 * @return the corresponding Jet line number if it can be computed, or -1 otherwise
	 */
	public int convertJava2JetLine(int lineNumber);

	/**
	 * Converts a Jet line number into a Java line number
	 *
	 * @param lineNumber
	 *        the Jet line number to be converted
	 * @return the corresponding Java line number if it can be computed, or -1 otherwise
	 */
	public int convertJet2JavaLine(final int lineNumber);

	/**
	 *
	 * @param number
	 * @return
	 */
	public SourceRange findNextScripletOrExpression(final int number);

	/**
	 *
	 * @return
	 */
	public IFile getJetFile();

	/**
	 *
	 * @return
	 */
	public IFile getJavaFile(IProgressMonitor monitor);

	/**
	 * Return the execution mode of the jet. If there is no execution mode specified, {@link EJetExecutionMode#PARALEL
	 * PARALEL} will be returned
	 *
	 * @return the execution mode, never null
	 */
	EJetExecutionMode getExecutionMode(IProgressMonitor monitor);

	/**
	 * The method will return the java package for the transformed class
	 *
	 * @return string
	 */
	public String getJavaPackage();
}
