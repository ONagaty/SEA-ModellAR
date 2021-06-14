package eu.cessar.ct.testutils.tests;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;

import eu.cessar.ct.testutils.CessarTestCase;
import eu.cessar.ct.testutils.TestUtilities;

public class TestCaseSample extends CessarTestCase
{
	private static final String PLUGIN_ID = "eu.cessar.ct.testutils";

	public void testImportProjNotOverride()
	{
		IProject proj = importProject(PLUGIN_ID, new Path("/resources/TestUtilities/newTestProject.zip"), false, false);
		assertEquals(true, proj.exists());

	}

	public void testImportProjOverride()
	{
		IProject proj = importProject(PLUGIN_ID, new Path("/resources/TestUtilities/newTestProject.zip"), true, true);
		assertEquals(true, proj.exists());

	}

	public void testDeleteProj()
	{
		try
		{
			IProject proj = importProject(PLUGIN_ID, new Path("/resources/TestUtilities/newTestProject.zip"), true,
				true);
			TestUtilities.deleteProject(new Path("/resources/TestUtilities/newTestProject.zip"));
			assertEquals(false, proj.exists());

		}
		catch (CoreException e)
		{
			fail(e);
		}
	}

	public void testImportNamedProjNotOverride()
	{
		IProject proj = importProject(PLUGIN_ID, new Path("/resources/TestUtilities/newTestProject.zip"),
			"AnotherName", false, false);
		assertEquals(true, proj.exists());

	}

	public void testImportNamedProjOverride()
	{
		IProject proj = importProject(PLUGIN_ID, new Path("/resources/TestUtilities/newTestProject.zip"),
			"AnotherName", true, true);
		assertEquals(true, proj.exists());

	}

	public void testDeleteNamedProj()
	{
		try
		{
			IProject proj = importProject(PLUGIN_ID, new Path("/resources/TestUtilities/newTestProject.zip"),
				"AnotherName", true, true);
			TestUtilities.deleteProject(proj);
			assertEquals(false, proj.exists());

		}
		catch (CoreException e)
		{
			fail(e);
		}
	}

	public void testImportSameProjWithDiffNames()
	{
		IProject proj1 = importProject(PLUGIN_ID, new Path("/resources/TestUtilities/newTestProject.zip"),
			"FirstProject", true, true);

		IProject proj2 = importProject(PLUGIN_ID, new Path("/resources/TestUtilities/newTestProject.zip"),
			"SecondProject", true, true);

		assertEquals(true, proj1.exists());
		assertEquals(true, proj2.exists());
	}

	public void testgetInputStream()
	{
		InputStream is = null;
		try
		{

			is = TestUtilities.getInputStream(PLUGIN_ID, new Path("/resources/TestUtilities/TestFile.java"));
			while (is.read() != -1)
			{
				// do nothing, just let it read the file.
				// System.out.print((char) currentByte);
			}
		}
		catch (Exception e)
		{
			fail(e);
		}
		finally
		{
			if (is != null)
			{
				try
				{
					is.close();
				}
				catch (IOException e)
				{
					fail(e);
				}
			}
		}

	}

	public void testCompareStreamsEqual()
	{
		InputStream is1 = null, is2 = null;
		try
		{
			is1 = TestUtilities.getInputStream(PLUGIN_ID, new Path("/resources/TestUtilities/TestFileCompare1.java"));
			is2 = TestUtilities.getInputStream(PLUGIN_ID, new Path("/resources/TestUtilities/TestFileCompare2.java"));
			assertEquals(true, TestUtilities.compare(is1, is2));
		}
		catch (Exception e)
		{
			fail(e);
		}
		finally
		{
			try
			{
				if (is1 != null)
				{
					is1.close();
				}
				if (is2 != null)
				{
					is2.close();
				}
			}
			catch (IOException e)
			{
				fail(e);
			}
		}

	}

	public void testCompareStreamsNotEqual()
	{
		InputStream is1 = null, is2 = null;
		try
		{
			is1 = TestUtilities.getInputStream(PLUGIN_ID, new Path("/resources/TestUtilities/TestFile.java"));
			is2 = TestUtilities.getInputStream(PLUGIN_ID, new Path("/resources/TestUtilities/TestFileCompare2.java"));
			assertEquals(false, TestUtilities.compare(is1, is2));

		}
		catch (Exception e)
		{
			fail(e);
		}
		finally
		{
			try
			{
				if (is1 != null)
				{
					is1.close();
				}
				if (is2 != null)
				{
					is2.close();
				}
			}
			catch (IOException e)
			{
				fail(e);
			}
		}

	}

	public void testCompareResourceWithIFile()
	{
		try
		{
			IProject containerProject = importProject(PLUGIN_ID,
				new Path("/resources/TestUtilities/newTestProject.zip"), true, true);
			IFolder containerFolder = containerProject.getFolder("dir1");
			IFile fileToCompare = containerFolder.getFile("File1.java");
			assertEquals(false,
				TestUtilities.compare(PLUGIN_ID, new Path("/resources/TestUtilities/TestFile.java"), fileToCompare));
		}
		catch (CoreException e1)
		{
			fail(e1);
		}
	}

	public void testCompareIFileWithIFile()
	{
		try
		{
			IProject containerProject = importProject(PLUGIN_ID,
				new Path("/resources/TestUtilities/newTestProject.zip"), true, true);
			IFolder containerFolder = containerProject.getFolder("dir1");
			IFile fileToCompare1 = containerFolder.getFile("File1.java");
			IFile fileToCompare2 = containerFolder.getFile("File2.java");
			assertEquals(false, TestUtilities.compare(fileToCompare1, fileToCompare2));
		}
		catch (CoreException e1)
		{
			fail(e1);
		}
	}

	private File getFileForResource(String pluginID, IPath resource) throws IOException
	{
		Bundle bundle = Platform.getBundle(pluginID);
		URL findEntry = FileLocator.find(bundle, resource, null);
		String resourceFileName = "";

		if (findEntry != null)
		{
			resourceFileName = FileLocator.toFileURL(findEntry).getFile();
		}
		File resourceFile = new File(resourceFileName);
		return resourceFile;
	}

	public void testCompareFileWithFile()
	{
		try
		{
			File file1 = getFileForResource(PLUGIN_ID, new Path("/resources/TestUtilities/TestFileCompare1.java"));
			File file2 = getFileForResource(PLUGIN_ID, new Path("/resources/TestUtilities/TestFileCompare2.java"));
			assertEquals(true, TestUtilities.compare(file1, file2));
		}
		catch (Exception e)
		{
			fail(e);
		}
	}

	public void testCompareFolderWithFolder()
	{
		try
		{
			File folder1 = getFileForResource(PLUGIN_ID, new Path("/resources/TestUtilities/foldertest"));
			File folder2 = getFileForResource(PLUGIN_ID, new Path("/resources/TestUtilities/foldertest1"));
			assertEquals(true, TestUtilities.compare(folder1, folder2));
		}
		catch (Exception e)
		{
			fail(e);
		}
	}

	public void testCompareFolderWithFolderNotEqual()
	{
		try
		{
			File folder1 = getFileForResource(PLUGIN_ID, new Path("/resources/TestUtilities/foldertest"));
			File folder2 = getFileForResource(PLUGIN_ID, new Path("/resources/TestUtilities/foldertest3"));
			assertEquals(false, TestUtilities.compare(folder1, folder2));
		}
		catch (Exception e)
		{
			fail(e);
		}
	}

	public void testCopyResourceToExistingIFile()
	{
		// copy TestFile to File3,then use the comparison method
		IProject containerProject;

		try
		{
			containerProject = importProject(PLUGIN_ID, new Path("/resources/TestUtilities/newTestProject.zip"), true,
				true);

			IFolder containerFolder = containerProject.getFolder("dir1");
			IFile fileTarget = containerFolder.getFile("File3.java");
			TestUtilities.copyFile(PLUGIN_ID, new Path("/resources/TestUtilities/TestFile.java"), fileTarget, true);
			assertEquals(true,
				TestUtilities.compare(PLUGIN_ID, new Path("/resources/TestUtilities/TestFile.java"), fileTarget));

		}
		catch (CoreException e)
		{
			fail(e);
		}
	}

	public void testCopyResourceToNonExistingIFile()
	{
		// copy TestFile to File4
		IProject containerProject;

		try
		{
			containerProject = importProject(PLUGIN_ID, new Path("/resources/TestUtilities/newTestProject.zip"), true,
				true);
			IFolder containerFolder = containerProject.getFolder("dir1");
			IFile fileTarget = containerFolder.getFile("File4.java");
			TestUtilities.copyFile(PLUGIN_ID, new Path("/resources/TestUtilities/TestFile.java"), fileTarget, true);
			assertEquals(true,
				TestUtilities.compare(PLUGIN_ID, new Path("/resources/TestUtilities/TestFile.java"), fileTarget));

		}
		catch (CoreException e)
		{
			fail(e);
		}
	}

	public void testCopyResourceToExistingFile()
	{
		// copy TestFile to File3

		IProject containerProject;

		try
		{
			containerProject = importProject(PLUGIN_ID, new Path("/resources/TestUtilities/newTestProject.zip"), true,
				true);
			IFolder containerFolder = containerProject.getFolder("dir1");
			IFile ifileTarget = containerFolder.getFile("File3.java");
			File fileTarget = ifileTarget.getLocation().toFile();

			TestUtilities.copyFile(PLUGIN_ID, new Path("/resources/TestUtilities/TestFile.java"), fileTarget, true);
			assertEquals(true, TestUtilities.compare(
				getFileForResource(PLUGIN_ID, new Path("/resources/TestUtilities/TestFile.java")), fileTarget));

		}
		catch (Exception e)
		{
			fail(e);
		}
	}

	public void testCopyResourceToNonExistingFile()
	{
		// copy TestFile to File4

		IProject containerProject;

		try
		{
			containerProject = importProject(PLUGIN_ID, new Path("/resources/TestUtilities/newTestProject.zip"), true,
				true);
			IFolder containerFolder = containerProject.getFolder("dir1");
			IFile ifileTarget = containerFolder.getFile("File4.java");
			File fileTarget = ifileTarget.getLocation().toFile();

			TestUtilities.copyFile(PLUGIN_ID, new Path("/resources/TestUtilities/TestFile.java"), fileTarget, true);
			assertEquals(true, TestUtilities.compare(
				getFileForResource(PLUGIN_ID, new Path("/resources/TestUtilities/TestFile.java")), fileTarget));

		}
		catch (Exception e)
		{
			fail(e);
		}
	}

	public void testCopyIFileToExistingIFile()
	{
		// copy File1 to File3
		IProject containerProject;

		try
		{
			containerProject = importProject(PLUGIN_ID, new Path("/resources/TestUtilities/newTestProject.zip"), true,
				true);
			IFolder containerFolder = containerProject.getFolder("dir1");
			IFile source = containerFolder.getFile("File1.java");
			IFile target = containerFolder.getFile("File3.java");
			TestUtilities.copyFile(source, target, true);
			assertEquals(true, TestUtilities.compare(source, target));
		}
		catch (CoreException e)
		{
			fail(e);
		}

	}

	public void testCopyIFileToNonExistingIFile()
	{
		// copy File1 to File4
		IProject containerProject;

		try
		{
			containerProject = importProject(PLUGIN_ID, new Path("/resources/TestUtilities/newTestProject.zip"), true,
				true);
			IFolder containerFolder = containerProject.getFolder("dir1");
			IFile source = containerFolder.getFile("File1.java");
			IFile target = containerFolder.getFile("File4.java");
			TestUtilities.copyFile(source, target, true);
			assertEquals(true, TestUtilities.compare(source, target));
		}
		catch (CoreException e)
		{
			fail(e);
		}

	}

	private static long getResourceSize(File resource)
	{
		if (resource.isFile())
		{
			return resource.length();
		}
		else
		{
			File[] folderFiles = resource.listFiles();
			long size = 0;
			for (int i = 0; i < folderFiles.length; i++)
			{
				size += getResourceSize(folderFiles[i]);
			}
			return size;
		}
	}

	public void testCopyIFolderToNonExistingIFolder()
	{
		// copy folder1 to dir1/folder4

		IProject containerProject;

		try
		{
			containerProject = importProject(PLUGIN_ID, new Path("/resources/TestUtilities/newTestProject.zip"), true,
				true);
			IFolder sourceFolder = containerProject.getFolder("folder1");
			IFolder targetFolder = containerProject.getFolder("dir1").getFolder("folder4");

			TestUtilities.copyFolder(sourceFolder, targetFolder, true, false);
			long size1 = getResourceSize(sourceFolder.getLocation().toFile());
			long size2 = getResourceSize(targetFolder.getLocation().toFile());
			assertEquals(size1, size2);

		}
		catch (CoreException e)
		{
			fail(e);
		}

	}

	public void testCopyIFolderToExistingIFolder()
	{
		// copy folder1 to dir1/folder3,merge=false

		IProject containerProject;
		try
		{
			containerProject = importProject(PLUGIN_ID, new Path("/resources/TestUtilities/newTestProject.zip"), true,
				true);
			IFolder sourceFolder = containerProject.getFolder("folder1");
			IFolder targetFolder = containerProject.getFolder("dir1").getFolder("folder3");

			TestUtilities.copyFolder(sourceFolder, targetFolder, true, false);
			long size1 = getResourceSize(sourceFolder.getLocation().toFile());
			long size2 = getResourceSize(targetFolder.getLocation().toFile());
			assertEquals(size1, size2);
		}
		catch (CoreException e)
		{
			fail(e);
		}

	}

	public void testCopyIFolderToExistingIFolderMerge()
	{
		// copy folder1 to dir1/folder3,merge=true

		IProject containerProject;

		try
		{
			containerProject = importProject(PLUGIN_ID, new Path("/resources/TestUtilities/newTestProject.zip"), true,
				true);
			IFolder sourceFolder = containerProject.getFolder("folder1");
			IFolder targetFolder = containerProject.getFolder("dir1").getFolder("folder3");

			TestUtilities.copyFolder(sourceFolder, targetFolder, true, true);

			long size1 = getResourceSize(sourceFolder.getLocation().toFile());
			long size2 = getResourceSize(targetFolder.getLocation().toFile());
			assertEquals(true, size2 >= size1);

		}
		catch (CoreException e)
		{
			fail(e);
		}

	}

	public void testCopyResourceToNonExistingIFolder()
	{
		IProject containerProject;

		try
		{
			containerProject = importProject(PLUGIN_ID, new Path("/resources/TestUtilities/newTestProject.zip"), true,
				true);
			IFolder targetFolder = containerProject.getFolder("dir1").getFolder("folder4");
			TestUtilities.copyFolder(PLUGIN_ID, new Path("/resources/TestUtilities/foldertest"), targetFolder, true,
				false);
			Bundle bundle = Platform.getBundle(PLUGIN_ID);
			URL findEntry = FileLocator.find(bundle, new Path("/resources/TestUtilities/foldertest"), null);
			String resourceFileName = "";

			if (findEntry != null)
			{
				resourceFileName = FileLocator.toFileURL(findEntry).getFile();
			}

			long size1 = getResourceSize(new File(resourceFileName));
			long size2 = getResourceSize(targetFolder.getLocation().toFile());
			assertEquals(size1, size2);

		}
		catch (Exception e)
		{
			fail(e);
		}
	}

	public void testCopyResourceToExistingIFolder()
	{
		// merge=false
		IProject containerProject;

		try
		{
			containerProject = importProject(PLUGIN_ID, new Path("/resources/TestUtilities/newTestProject.zip"), true,
				true);
			IFolder targetFolder = containerProject.getFolder("dir1").getFolder("folder3");
			TestUtilities.copyFolder(PLUGIN_ID, new Path("/resources/TestUtilities/foldertest"), targetFolder, true,
				false);

			Bundle bundle = Platform.getBundle(PLUGIN_ID);
			URL findEntry = FileLocator.find(bundle, new Path("/resources/TestUtilities/foldertest"), null);
			String resourceFileName = "";

			if (findEntry != null)
			{
				resourceFileName = FileLocator.toFileURL(findEntry).getFile();
			}

			long size1 = getResourceSize(new File(resourceFileName));
			long size2 = getResourceSize(targetFolder.getLocation().toFile());
			assertEquals(size1, size2);

		}
		catch (Exception e)
		{
			fail(e);
		}
	}

	public void testCopyResourceToExistingIFolderMerge()
	{
		// merge=true
		IProject containerProject;

		try
		{
			containerProject = importProject(PLUGIN_ID, new Path("/resources/TestUtilities/newTestProject.zip"), true,
				true);
			IFolder targetFolder = containerProject.getFolder("dir1").getFolder("folder3");
			TestUtilities.copyFolder(PLUGIN_ID, new Path("/resources/TestUtilities/foldertest"), targetFolder, true,
				true);

			Bundle bundle = Platform.getBundle(PLUGIN_ID);
			URL findEntry = FileLocator.find(bundle, new Path("/resources/TestUtilities/foldertest"), null);
			String resourceFileName = "";

			if (findEntry != null)
			{
				resourceFileName = FileLocator.toFileURL(findEntry).getFile();
			}

			long size1 = getResourceSize(new File(resourceFileName));
			long size2 = getResourceSize(targetFolder.getLocation().toFile());
			assertEquals(true, size2 >= size1);

		}
		catch (Exception e)
		{
			fail(e);
		}
	}

	public void testCopyResourceToFolder()
	{
		// merge=false;
		IProject containerProject;

		try
		{
			containerProject = importProject(PLUGIN_ID, new Path("/resources/TestUtilities/newTestProject.zip"), true,
				true);
			IFolder targetIFolder = containerProject.getFolder("dir1").getFolder("folder4");

			File targetFolder = targetIFolder.getLocation().toFile();
			TestUtilities.copyFolder(PLUGIN_ID, new Path("/resources/TestUtilities/foldertest"), targetFolder, true,
				false);

			Bundle bundle = Platform.getBundle(PLUGIN_ID);
			URL findEntry = FileLocator.find(bundle, new Path("/resources/TestUtilities/foldertest"), null);
			String resourceFileName = "";

			if (findEntry != null)
			{
				resourceFileName = FileLocator.toFileURL(findEntry).getFile();
			}

			long size1 = getResourceSize(new File(resourceFileName));
			long size2 = getResourceSize(targetFolder);
			assertEquals(size1, size2);

		}
		catch (Exception e)
		{
			fail(e);
		}
	}

	public void testCopyResourceToFolderMerge()
	{
		// merge=true
		IProject containerProject;

		try
		{
			containerProject = importProject(PLUGIN_ID, new Path("/resources/TestUtilities/newTestProject.zip"), true,
				true);
			IFolder targetIFolder = containerProject.getFolder("dir1").getFolder("folder3");

			File targetFolder = targetIFolder.getLocation().toFile();
			TestUtilities.copyFolder(PLUGIN_ID, new Path("/resources/TestUtilities/foldertest"), targetFolder, true,
				true);

			Bundle bundle = Platform.getBundle(PLUGIN_ID);
			URL findEntry = FileLocator.find(bundle, new Path("/resources/TestUtilities/foldertest"), null);
			String resourceFileName = "";

			if (findEntry != null)
			{
				resourceFileName = FileLocator.toFileURL(findEntry).getFile();
			}

			long size1 = getResourceSize(new File(resourceFileName));
			long size2 = getResourceSize(targetFolder);
			assertEquals(true, size2 >= size1);

		}
		catch (Exception e)
		{
			fail(e);
		}
	}
}
