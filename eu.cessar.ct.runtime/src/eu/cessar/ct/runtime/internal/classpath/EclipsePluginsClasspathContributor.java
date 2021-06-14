package eu.cessar.ct.runtime.internal.classpath;

import org.eclipse.jdt.core.IJavaProject;

import eu.cessar.ct.runtime.classpath.AbstractPluginsClasspathContributor;

public class EclipsePluginsClasspathContributor extends AbstractPluginsClasspathContributor
{
	private static final String CONTRIBUTED_PLUGINS[] = { //
	"org.eclipse.emf.workspace",// //$NON-NLS-1$
		"org.eclipse.emf.ecore.xmi",// //$NON-NLS-1$
		"javax.xml", //$NON-NLS-1$
	};

	/* (non-Javadoc)
	 * @see eu.cessar.ct.runtime.classpath.AbstractPluginsClasspathContributor#getBundleIDs()
	 */
	@Override
	public String[] getBundleIDs(IJavaProject project)
	{

		return CONTRIBUTED_PLUGINS;
	}

}
