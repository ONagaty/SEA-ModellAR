package eu.cessar.ct.compat.tests.ant;

import org.apache.tools.ant.BuildException;
import org.eclipse.core.resources.IFile;

import eu.cessar.ct.compat.ant.internal.CompatConvertPath;

public class CompatConvertPathTaskTests extends AbstractCompatAntTaskTests
{
	private CompatConvertPath compatConvertPath;
	private static final String CESSAR_PROJECT_NAME = "CESSAR_PROJECT_NAME"; //$NON-NLS-1$
	private static final String PROPERTY1 = "full.ant.file";
	private static final String PROPERTY2 = "relative.ant.file";

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.sdk.ant.tests.compat.AbstractCompatAntTaskTests#doCreateAntTask()
	 */
	@Override
	protected void doCreateAntTask()
	{
		compatConvertPath = new CompatConvertPath();
		compatConvertPath.setProject(antProject);
	}

	/**
	 *
	 */
	public void testInvalidArgs1()
	{
		compatConvertPath.setFilesystempath("/full/file/path"); //$NON-NLS-1$
		compatConvertPath.setResourcepath("/resourcepath"); //$NON-NLS-1$

		try
		{
			compatConvertPath.execute();
			fail();
		}

		catch (BuildException e)
		{
			assertEquals("filesystempath and resourcepath cannot be both set", e.getMessage()); //$NON-NLS-1$
		}
	}

	/**
	 *
	 */
	public void testInvalidArgs2()
	{
		compatConvertPath.setFilesystempath("/full/file/path"); //$NON-NLS-1$

		try
		{
			compatConvertPath.execute();
			fail();
		}

		catch (BuildException e)
		{
			assertEquals("property must be set", e.getMessage()); //$NON-NLS-1$
		}
	}

	/**
	 * Tests the conversion of a resource from relative to absolute path.
	 */
	public void testConvertResourcePath()
	{
		IFile antFile = iProject.getFile(ANT_FILE);

		try
		{
			compatConvertPath.setProperty(PROPERTY1);
			compatConvertPath.setResourcepath(antFile.getFullPath().toString());
			compatConvertPath.execute();
			assertEquals(antFile.getLocation().toString(), antProject.getUserProperty(PROPERTY1));

		}
		catch (BuildException e)
		{
			fail();
		}

	}

	/**
	 * Tests the conversion of a resource from absolute to relative path.
	 */
	public void testConvertResourcePathFail()
	{
		IFile antFile = iProject.getFile(ANT_FILE);

		try
		{
			compatConvertPath.setProperty(PROPERTY1);
			compatConvertPath.setResourcepath(antFile.getLocation().toString());
			compatConvertPath.execute();
			fail();

		}
		catch (BuildException e)
		{
			assertEquals("Cannot get location for " + antFile.getLocation().toString(), e.getMessage()); //$NON-NLS-1$
		}
	}

	/**
	 * Tests the conversion of a resource from absolute to relative path, with no project path/CESSAR_PROJET_NAME set.
	 */
	public void testConvertFileSystemPathNoProjectInfoSet()
	{
		IFile antFile = iProject.getFile(ANT_FILE);

		try
		{
			compatConvertPath.setProperty(PROPERTY2);
			compatConvertPath.setFilesystempath(antFile.getLocation().toString());
			compatConvertPath.execute();
			assertEquals(antFile.getFullPath().toString(), antProject.getUserProperty(PROPERTY2));

		}
		catch (BuildException e)
		{
			fail();
		}
	}

	/**
	 * Tests the conversion of a resource from absolute to relative path., with project path set.
	 */
	public void testConvertFileSystemPathProjectPathSet()
	{
		IFile antFile = iProject.getFile(ANT_FILE);

		try
		{
			compatConvertPath.setProjectPath(iProject.getLocation().toString());
			compatConvertPath.setProperty(PROPERTY2);
			compatConvertPath.setFilesystempath(antFile.getLocation().toString());
			compatConvertPath.execute();
			assertEquals(antFile.getFullPath().toString(), antProject.getUserProperty(PROPERTY2));

		}
		catch (BuildException e)
		{
			fail();
		}
	}

	/**
	 * Tests the conversion of a resource from absolute to relative path., with CESSAR_PROJECT_NAME property set.
	 */
	public void testConvertFileSystemPathCessarProjectNameSet()
	{
		IFile antFile = iProject.getFile(ANT_FILE);

		try
		{
			compatConvertPath.getProject().setUserProperty(CESSAR_PROJECT_NAME, iProject.getName());
			compatConvertPath.setProperty(PROPERTY2);
			compatConvertPath.setFilesystempath(antFile.getLocation().toString());
			compatConvertPath.execute();
			assertEquals(antFile.getFullPath().toString(), antProject.getUserProperty(PROPERTY2));

		}
		catch (BuildException e)
		{
			fail();
		}
	}

}
