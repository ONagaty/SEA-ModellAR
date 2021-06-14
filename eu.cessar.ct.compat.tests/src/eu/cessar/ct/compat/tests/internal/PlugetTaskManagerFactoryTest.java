package eu.cessar.ct.compat.tests.internal;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleException;

import eu.cessar.ct.sdk.runtime.ExecutionService;
import eu.cessar.ct.sdk.runtime.ICessarTaskManager;
import eu.cessar.ct.testutils.CessarTestCase;
import eu.cessar.ct.testutils.TestUtilities;

@SuppressWarnings("nls")
public class PlugetTaskManagerFactoryTest extends CessarTestCase
{

	private static final String PROJECT_ARCHIVE = "/resources/CompAPIProj.zip";

	private static final String EU_CESSAR_CT_RUNTIME = "eu.cessar.ct.runtime";

	IProject project;

	@Override
	protected void setUp()
	{

		Bundle bundle = Platform.getBundle(EU_CESSAR_CT_RUNTIME);
		try
		{
			bundle.start();
		}
		catch (BundleException e)
		{
			fail("A required bundle could not be started");
		}
	}

	/**
	 * Description: The test verifies the execution of the 3 plugets that are packed inside MyPluget.pluget jar: <li>
	 * org.plugets.MyPluget_1</li> <li>
	 * org.plugets.MyPluget_2</li> <li>org.plugets.MyPluget_3</li> <br>
	 * by checking the content of the output files. <br>
	 * <br>
	 * Each pluget produces a file with the name out[1-3].txt with the following info: <li>pluget class name</li>, <li>
	 * project name,</li> <li>received arguments</li>. <br>
	 * <br>
	 *
	 * Prerequisites: Import the project "CompAPIProj" from resources. The project has the "Use compatibility API" set
	 * on true <br>
	 * <br>
	 *
	 * Pass criteria: The produced files are the same as the expected files <br>
	 * <br>
	 *
	 * @throws CoreException
	 *
	 */
	public void testPlugetExecution_UseCompatibilityAPI()
	{
		project = importProject(CessarPluginActivator.PLUGIN_ID, new Path(PROJECT_ARCHIVE), false, false);

		assertTrue(project.isAccessible());

		IFile plugetJar = project.getFile("/MyPluget_6.pluget");
		assertTrue(plugetJar.isAccessible());

		@SuppressWarnings("unchecked")
		ICessarTaskManager<IFile> manager = (ICessarTaskManager<IFile>) ExecutionService.createManager(project,
			ExecutionService.TASK_TYPE_PLUGET);

		// set input
		manager.initialize(plugetJar);

		IStatus status = manager.execute(false, new String[] {"1", "2", "3", "4", "5"}, new NullProgressMonitor());

		assertStatusOK(status);

		compareFiles("/out6.txt", "/expected/out6_WithParameters.txt");

	}

	/**
	 * Check if the content of the generated files matches the expected content
	 *
	 * @param actual
	 * @param expected
	 */
	private void compareFiles(String actual, String expected)
	{
		IFile expectedFile = project.getFile(expected);
		assertTrue(expectedFile.isAccessible());

		IFile actualFile = project.getFile(actual);
		assertTrue(actualFile.isAccessible());

		try
		{
			assertTrue(TestUtilities.compare(actualFile, expectedFile));
		}
		catch (CoreException e)
		{
			fail("Compare operation between " + actual + " and " + expected + "failed");
		}
	}
}