package eu.cessar.ct.testutils;

import org.eclipse.jdt.core.IJavaProject;

import eu.cessar.ct.runtime.classpath.AbstractPluginsClasspathContributor;


public class TestUtilsClassPathProvider extends AbstractPluginsClasspathContributor
{

	private static final String[] bundleIDs = {"eu.cessar.ct.testutils"}; //$NON-NLS-1$

	@Override
	protected String[] getBundleIDs(IJavaProject javaProject)
	{
		// TODO Auto-generated method stub
		return bundleIDs;
	}

}
