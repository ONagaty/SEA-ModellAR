package eu.cessar.ct.compat.tests.ant;

import org.apache.tools.ant.Project;
import org.eclipse.ant.core.AntCorePlugin;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.Path;

import eu.cessar.ct.compat.tests.internal.CessarPluginActivator;
import eu.cessar.ct.core.platform.util.PlatformUtils;
import eu.cessar.ct.testutils.CessarTestCase;

public abstract class AbstractCompatAntTaskTests extends CessarTestCase
{
	protected IProject iProject;
	protected Project antProject;
	protected static final String PROJECT_PATH = "/resources/test_compat_antTasks/TestAntCompat3.x.zip"; //$NON-NLS-1$
	protected static final String ANT_FILE = "main.ant"; //$NON-NLS-1$

	@Override
	protected void setUp() throws Exception
	{
		iProject = importProject(CessarPluginActivator.PLUGIN_ID, new Path(PROJECT_PATH), false, false);
		assertNotNull(iProject);
		assertTrue(iProject.isAccessible());
		PlatformUtils.waitForModelLoading(iProject, null);

		antProject = new Project();
		antProject.setCoreLoader(AntCorePlugin.getPlugin().getNewClassLoader());
		antProject.setBasedir(iProject.getLocation().toString());

		// private ClassLoader getClassLoader() {
		// if (customClasspath == null) {
		// return AntCorePlugin.getPlugin().getNewClassLoader();
		// }
		// AntCorePreferences preferences = AntCorePlugin.getPlugin().getPreferences();
		// List fullClasspath= new ArrayList();
		// fullClasspath.addAll(Arrays.asList(customClasspath));
		// fullClasspath.addAll(Arrays.asList(preferences.getExtraClasspathURLs()));
		// return new AntClassLoader((URL[])fullClasspath.toArray(new URL[fullClasspath.size()]),
		// preferences.getPluginClassLoaders());
		// }

		doCreateAntTask();
	}

	/**
	 *
	 */
	protected abstract void doCreateAntTask();

}
