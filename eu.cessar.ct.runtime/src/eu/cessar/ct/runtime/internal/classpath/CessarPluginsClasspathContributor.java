package eu.cessar.ct.runtime.internal.classpath;

import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.IJavaProject;

import eu.cessar.ct.core.platform.CESSARPreferencesAccessor;
import eu.cessar.ct.core.platform.ECompatibilityMode;
import eu.cessar.ct.runtime.classpath.AbstractPluginsClasspathContributor;

/**
 *
 */
public class CessarPluginsClasspathContributor extends AbstractPluginsClasspathContributor
{
	private static final String SDK_PLUGIN = "eu.cessar.ct.sdk"; //$NON-NLS-1$
	private static final String AAL_COMMON_PLUGIN = "org.artop.aal.common"; //$NON-NLS-1$

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.runtime.classpath.AbstractPluginsClasspathContributor#getBundleIDs()
	 */
	@Override
	public String[] getBundleIDs(IJavaProject javaProject)
	{
		IProject project = javaProject.getProject();

		ECompatibilityMode mode = CESSARPreferencesAccessor.getCompatibilityMode(project);
		if (mode.haveNewAPI())
		{
			return new String[] {SDK_PLUGIN, AAL_COMMON_PLUGIN};
		}

		return new String[0];

	}

}
