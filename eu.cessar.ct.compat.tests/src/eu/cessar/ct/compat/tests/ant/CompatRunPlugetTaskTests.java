package eu.cessar.ct.compat.tests.ant;

import org.apache.tools.ant.BuildException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;

import eu.cessar.ct.compat.ant.internal.CompatRunPlugetTask;

public class CompatRunPlugetTaskTests extends AbstractCompatAntTaskTests
{
	private CompatRunPlugetTask compatRunPluget;
	private static final String PLUGET_FILE = "/AUTOSAR/plugets/TestPluget.pluget"; //$NON-NLS-1$
	private static final String DEFAULT_CREATED_FILE = "Default.txt"; //$NON-NLS-1$
	private static final String NEW_CREATED_FILE = "Test.txt"; //$NON-NLS-1$

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.sdk.ant.tests.compat.AbstractCompatAntTaskTests#doCreateAntTask()
	 */
	@Override
	protected void doCreateAntTask()
	{
		compatRunPluget = new CompatRunPlugetTask();
		compatRunPluget.setProject(antProject);
	}

	/**
	 * 
	 */
	public void testInvalidArgs()
	{
		try
		{
			compatRunPluget.setProjectPath(iProject.getLocation().toString());
			compatRunPluget.execute();
			fail();
		}
		catch (BuildException e)
		{
			assertEquals("At least one target pluget or target Pluget ID should be set", e.getMessage()); //$NON-NLS-1$
		}
	}

	/**
	 * Tests the execution of an invalid pluget.
	 */
	public void testRunInvalidPluget()
	{
		try
		{
			compatRunPluget.setProjectPath(iProject.getLocation().toString());
			compatRunPluget.setTargetPlugets("Pluget.pluget"); //$NON-NLS-1$
			compatRunPluget.execute();
			fail();
		}
		catch (Exception e)
		{
			assertEquals("Cannot locate pluget: Pluget.pluget", e.getMessage()); //$NON-NLS-1$
		}
	}

	/**
	 * Tests the execution of a pluget.
	 */
	public void testRunPluget()
	{
		try
		{
			IFile pluget = iProject.getFile(PLUGET_FILE);
			assertTrue(pluget.isAccessible());
			compatRunPluget.setProjectPath(iProject.getLocation().toString());
			compatRunPluget.setTargetPlugets(PLUGET_FILE);
			compatRunPluget.execute();

			iProject.refreshLocal(IResource.DEPTH_INFINITE, null);
			IFile generated = iProject.getFile(DEFAULT_CREATED_FILE);
			assertTrue(generated.isAccessible());
		}
		catch (Exception e)
		{
			fail();
		}
	}

	/**
	 * Tests the execution of a pluget, with parameters.
	 */
	public void testRunPlugetWithParams()
	{
		try
		{
			IFile pluget = iProject.getFile(PLUGET_FILE);
			assertTrue(pluget.isAccessible());
			compatRunPluget.setProjectPath(iProject.getLocation().toString());
			compatRunPluget.setTargetPlugets(PLUGET_FILE);
			compatRunPluget.setParameters(NEW_CREATED_FILE);
			compatRunPluget.execute();

			iProject.refreshLocal(IResource.DEPTH_INFINITE, null);
			IFile generated = iProject.getFile(NEW_CREATED_FILE);
			assertTrue(generated.isAccessible());
		}
		catch (Exception e)
		{
			fail();
		}
	}

}
