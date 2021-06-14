package eu.cessar.ct.runtime.internal.classpath;

import org.eclipse.jdt.core.IJavaProject;

import eu.cessar.ct.runtime.classpath.AbstractPluginsClasspathContributor;
import eu.cessar.ct.runtime.utils.RequiredPluginsAccessor;

/**
 * @author uidu0944
 * 
 */
public class PropertyPluginsClasspathContributor extends AbstractPluginsClasspathContributor
{

	/* (non-Javadoc)
	 * @see eu.cessar.ct.runtime.classpath.AbstractPluginsClasspath-Contributor#getBundleIDs()
	 */
	@Override
	public String[] getBundleIDs(IJavaProject javaProject)
	{
		return RequiredPluginsAccessor.getPluginList(javaProject.getProject());
	}

}
