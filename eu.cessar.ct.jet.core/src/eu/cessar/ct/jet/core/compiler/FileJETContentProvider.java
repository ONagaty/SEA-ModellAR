package eu.cessar.ct.jet.core.compiler;

import java.io.InputStream;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IJavaProject;

import eu.cessar.ct.jet.core.compliance.EJetComplianceLevel;

/**
 * A concrete implementation of {@link IJETContentProvider} interface, that can
 * be constructed around an {@link IFile}.
 */
public class FileJETContentProvider extends AbstractJetContentProvider
{
	private IFile jetFile;

	/**
	 * Default class constructor
	 * 
	 * @param file
	 *        the instance of the file around the provider is build on
	 */
	public FileJETContentProvider(IJavaProject jProject, IFile file,
		EJetComplianceLevel complianceLevel)
	{
		super(jProject, complianceLevel);
		Assert.isNotNull(file);
		jetFile = file;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.newversion.jet.core.IJETContentProvider#getContent()
	 */
	public InputStream getContent() throws CoreException
	{
		return jetFile.getContents();
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.jet.core.compiler.IJETContentProvider#getJETFileName()
	 */
	public String getJETFileName()
	{
		return jetFile.getName();
	}

	/**
	 * 
	 * @return
	 */
	public IFile getJETFile()
	{
		return jetFile;
	}
}
