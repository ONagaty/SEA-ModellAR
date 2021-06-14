package eu.cessar.ct.runtime.ui.internal.classpath;

import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.IJavaProject;

import eu.cessar.ct.core.platform.CESSARPreferencesAccessor;
import eu.cessar.ct.core.platform.ECompatibilityMode;
import eu.cessar.ct.runtime.classpath.AbstractPluginsClasspathContributor;

/**
 * Classpath contributor for the SDK UI of CESSAR
 * 
 * @author uidl6458
 * 
 *         %created_by: uidl6458 %
 * 
 *         %date_created: Wed May 29 12:32:33 2013 %
 * 
 *         %version: 1 %
 */
public class CessarUIPluginsClasspathContributor extends AbstractPluginsClasspathContributor
{
	private static final String SDK_PLUGIN = "eu.cessar.ct.sdk.ui"; //$NON-NLS-1$

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
			return new String[] {SDK_PLUGIN};
		}
		else
		{
			return new String[0];
		}
	}

}
