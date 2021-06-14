package eu.cessar.ct.jet.core.compiler;

import org.eclipse.core.resources.IFile;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;

import eu.cessar.ct.jet.core.compliance.EJetComplianceLevel;
import eu.cessar.ct.jet.core.internal.compiler.JETServicesProviderImpl;

/**
 * Class that allows clients to obtain a concrete JET services provider for a
 * specified JET file.
 */
public final class JETServicesProviderFactory
{
	private JETServicesProviderFactory()
	{
		// this class should not be instantiated
	}

	/**
	 * Create a JET services provider staring from a JET file.
	 * 
	 * @param file
	 *        {@link IFile} instance for which JET services are required
	 * @param complianceLevel
	 *        {@link EJetComplianceLevel} the desired compliance level of the
	 *        provider
	 * @return An {@link IJETServicesProvider} concrete instance.
	 */
	public static IJETServicesProvider createJETServicesProvider(IFile file,
		EJetComplianceLevel complianceLevel)
	{
		IJavaProject jProject = JavaCore.create(file.getProject());
		return createJETServicesProvider(new FileJETContentProvider(jProject, file, complianceLevel));
	}

	/**
	 * Create a JET services provider
	 * 
	 * @param contentProvider
	 *        a {@link IJETContentProvider} concrete implementation used to
	 *        obtain JET information
	 * @return An {@link IJETServicesProvider} concrete instance.
	 */
	public static IJETServicesProvider createJETServicesProvider(
		final IJETContentProvider contentProvider)
	{
		IJETServicesProvider jetServices = new JETServicesProviderImpl();
		jetServices.setContentProvider(contentProvider);
		return jetServices;
	}
}
