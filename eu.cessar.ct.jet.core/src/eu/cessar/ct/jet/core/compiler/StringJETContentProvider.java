/**
 * 
 */
package eu.cessar.ct.jet.core.compiler;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IJavaProject;

import eu.cessar.ct.jet.core.compliance.EJetComplianceLevel;

/**
 * A concrete implementation of {@link IJETContentProvider} interface, that can
 * be constructed based on a String.
 */
public class StringJETContentProvider extends AbstractJetContentProvider
{
	private String jetFileName;
	private String jetSource;

	/**
	 * Default constructor
	 * 
	 * @param jProject
	 *        a Java project instance
	 * @param fileName
	 *        the name of the JET file with specified content
	 * @param source
	 *        the content of the JET file
	 */
	public StringJETContentProvider(IJavaProject jProject, String fileName, String source,
		EJetComplianceLevel complianceLevel)
	{
		super(jProject, complianceLevel);
		jetFileName = fileName;
		jetSource = source;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.jet.core.compiler.IJETContentProvider#getContent()
	 */
	public InputStream getContent() throws CoreException
	{
		return new ByteArrayInputStream(jetSource.getBytes());
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.jet.core.compiler.IJETContentProvider#getJETFileName()
	 */
	public String getJETFileName()
	{
		return jetFileName;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.jet.core.compiler.IJETContentProvider#getJETFile()
	 */
	public IFile getJETFile()
	{
		// TODO Auto-generated method stub
		return null;
	}

}
