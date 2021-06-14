package eu.cessar.ct.compat.tests.ant;

import org.apache.tools.ant.BuildException;

import eu.cessar.ct.compat.ant.internal.CompatGetVariantTask;
import eu.cessar.ct.core.platform.EProjectVariant;

public class CompatGetVariantTaskTests extends AbstractCompatAntTaskTests
{
	private CompatGetVariantTask compatGetVariantTask;
	private static final String ECUCONFIG_FILE1 = "Adc.ecuconfig"; //$NON-NLS-1$
	private static final String ECUCONFIG_FILE2 = "Can.ecuconfig"; //$NON-NLS-1$
	private static final String ECUCONFIG_FILE3 = "Multiple.ecuconfig"; //$NON-NLS-1$

	private static final String PROPERTY = "variant"; //$NON-NLS-1$
	private static final String EXPECTED_VARIANT1 = "VARIANT-PRE-COMPILE"; //$NON-NLS-1$
	private static final String EXPECTED_VARIANT2 = EProjectVariant.DEVELOPMENT.toString();

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.sdk.ant.tests.compat.AbstractCompatAntTaskTests#doCreateAntTask()
	 */
	@Override
	protected void doCreateAntTask()
	{
		compatGetVariantTask = new CompatGetVariantTask();
		compatGetVariantTask.setProject(antProject);
	}

	/**
	 *
	 */
	public void testInvalidArgs()
	{
		try
		{
			compatGetVariantTask.setProperty(PROPERTY);
			compatGetVariantTask.setProjectPath(iProject.getLocation().toString());
			compatGetVariantTask.execute();
			fail();
		}
		catch (BuildException e)
		{
			assertEquals("ecuconfig attribute is mandatory!", e.getMessage()); //$NON-NLS-1$
		}

	}

	/**
	 * Tests the retrieve of a variant from a file with no variant set on the module config. The project variant is
	 * expected.
	 */
	public void testVariantNotSet()
	{
		try
		{
			compatGetVariantTask.setProperty(PROPERTY);
			compatGetVariantTask.setEcuconfig(ECUCONFIG_FILE2);
			compatGetVariantTask.setProjectPath(iProject.getLocation().toString());
			compatGetVariantTask.execute();
			assertEquals(EXPECTED_VARIANT2, antProject.getProperty(PROPERTY));
		}
		catch (BuildException e)
		{
			fail(e);
		}
	}

	/**
	 * Tests the retrieve of a variant from a file with the variant set on the module config.
	 */
	public void testGetVariantFromOneEcuconfig()
	{
		try
		{
			compatGetVariantTask.setProperty(PROPERTY);
			compatGetVariantTask.setEcuconfig(ECUCONFIG_FILE1);
			compatGetVariantTask.setProjectPath(iProject.getLocation().toString());
			compatGetVariantTask.execute();
			assertEquals(EXPECTED_VARIANT1, antProject.getProperty(PROPERTY));
		}
		catch (BuildException e)
		{
			fail(e);
		}
	}

	/**
	 * Tests the retrieve of a variant from a file with multiple ecuconfigs.
	 */

	public void testGetVariantFail()
	{
		try
		{
			compatGetVariantTask.setProperty(PROPERTY);
			compatGetVariantTask.setEcuconfig(ECUCONFIG_FILE3);
			compatGetVariantTask.setProjectPath(iProject.getLocation().toString());
			compatGetVariantTask.execute();
			fail();
		}
		catch (BuildException e)
		{
			assertEquals(
				"2 module configuration(s) have been found inside " + ECUCONFIG_FILE3 + " file.", e.getMessage()); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}
}
