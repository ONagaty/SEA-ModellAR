package eu.cessar.ct.jet.core.compiler;

import java.io.InputStream;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IJavaProject;

import eu.cessar.ct.jet.core.compliance.EJetComplianceLevel;

/**
 * This interface must be inherited by all the clients that use
 * {@link IJETCompilationResultProvider} instance. Through this interface, the
 * JET services provider implementation obtain from the client (implementer)
 * information about the JET.
 */
public interface IJETContentProvider
{
	/**
	 * The Java project where the JET file is located and where the compilation
	 * will occur.
	 * 
	 * @return An {@link IJavaProject} instance; must not be <code>null</code>
	 */
	public IJavaProject getJavaProject();

	/**
	 * @return
	 */
	public EJetComplianceLevel getJetComplianceLevel();

	/**
	 * Return an open input stream with the JET content to be compiled.
	 * 
	 * @return An input stream with the JET content to be compiled
	 * @throws CoreException
	 *         if JET file is not accessible or is out of sync.
	 */
	public InputStream getContent() throws CoreException;

	/**
	 * Gets the name of the file whose content is provided
	 * 
	 * @return The name of JET file, must not be <code>null</code>.
	 */
	public String getJETFileName();

	/**
	 * 
	 * @return
	 */
	public IFile getJETFile();

	/**
	 * Setter and getter methods for the name of the resulting class. If not
	 * set, the provider will try to determine one by checking the jet header.
	 * 
	 * @param className
	 *        the name of the Java class; if <code>null</code> the class name
	 *        within the JET file will be used.
	 */
	public void setOutputClass(String className);

	public String getOutputClass();
}
