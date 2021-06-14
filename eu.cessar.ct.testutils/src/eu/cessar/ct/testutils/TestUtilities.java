package eu.cessar.ct.testutils;

import static org.junit.Assert.assertNotNull;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.sphinx.platform.IExtendedPlatformConstants;
import org.eclipse.sphinx.platform.util.ExtendedPlatform;
import org.osgi.framework.Bundle;

import eu.cessar.ct.core.platform.util.PlatformUtils;
import eu.cessar.ct.core.platform.util.ResourceUtils;

@SuppressWarnings("nls")
public class TestUtilities
{

	static final int BUFFER_SIZE = 1024;

	/**
	 * Import the project archived under <code>pluginID\projectArchive</code> file into the existing workspace and
	 * return the resulting IProject. Throws CoreException if the project cannot be imported.<br/>
	 * Note: in order for this method to work correctly the project archive has to follow the following rules:<br/>
	 * <ul>
	 * <li>An archive should contain exactly one project</li>
	 * <li>The name of the archive should match the name of the project</li>
	 * <li>The content of the archive should be a single folder named the same as the project</li>
	 * </ul>
	 *
	 * @param pluginID
	 *        the ID of the plug-in the contain the project archive
	 * @param projectArchive
	 *        a path inside the pluginID that contain the project archive
	 * @param override
	 *        if true and there is already a project named projectArchive the already added project have to be deleted
	 *        and re-imported. If false and the project is already imported it will be returned
	 * @return the imported project
	 */
	public static IProject importProject(final String pluginID, final IPath projectArchive, final boolean override)
		throws CoreException
	{
		String projectName = projectArchive.removeFileExtension().lastSegment().toString();

		return importProject(pluginID, projectArchive, projectName, override);
	}

	/**
	 * Specified by {@link#importProject}. <br>
	 * Adds the option to specify the name of the imported project archive (i.e. the archive name is irrelevant)
	 */
	public static IProject importProject(final String pluginID, final IPath projectArchive, final String projectName,
		final boolean override) throws CoreException
	{
		final IWorkspace ws = ResourcesPlugin.getWorkspace();

		final IProject proj = ws.getRoot().getProject(projectName);

		if (proj.exists())
		{
			if (!override)
			{
				return proj;
			}
			else
			{
				deleteProject(proj);
			}
		}
		IWorkspaceRunnable importer = new IWorkspaceRunnable()
		{
			public void run(IProgressMonitor monitor) throws CoreException
			{
				runImportProject(pluginID, projectArchive, projectName, proj);
			}
		};
		ws.run(importer, ws.getRuleFactory().createRule(proj), IWorkspace.AVOID_UPDATE, null);

		return proj;
	}

	/**
	 * Executed from importer IWorkspaceRunnable
	 *
	 * @param pluginID
	 * @param projectArchive
	 * @param proj
	 * @param override
	 * @throws CoreException
	 */
	private static void runImportProject(String pluginID, IPath projectArchive, String projectName, IProject proj)
		throws CoreException
	{

		String workspaceLocation = ResourcesPlugin.getWorkspace().getRoot().getLocation().toOSString();
		String projectArchiveName = "";

		Bundle bundle = Platform.getBundle(pluginID);
		assertNotNull("Cannot locate plugin " + pluginID, bundle);
		URL findEntry = FileLocator.find(bundle, projectArchive, null);

		if (findEntry != null)
		{
			try
			{
				projectArchiveName = FileLocator.toFileURL(findEntry).getFile();
			}
			catch (IOException e1)
			{
				throw (CoreException) new CoreException(new Status(IStatus.ERROR, pluginID, e1.getMessage(), e1)).initCause(e1);
			}
		}
		try
		{

			ZipFile zipFile = new ZipFile(new File(projectArchiveName));

			Enumeration<ZipEntry> entries = (Enumeration<ZipEntry>) zipFile.entries();

			// process each entry
			while (entries.hasMoreElements())
			{
				ZipEntry currentEntry = entries.nextElement();
				String currentEntryName = currentEntry.getName();
				BufferedInputStream entryStream = null;
				BufferedOutputStream destStream = null;

				String archiveName = projectArchive.removeFileExtension().lastSegment().toString();
				currentEntryName = currentEntryName.replaceFirst(archiveName, projectName);
				File destFile = new File(workspaceLocation, currentEntryName);
				File destFileParent = destFile.getParentFile();
				destFileParent.mkdirs();

				// extract current entry if is a file
				if (!currentEntry.isDirectory())
				{
					try
					{
						// read from the archive entry
						entryStream = new BufferedInputStream(zipFile.getInputStream(currentEntry));
						destStream = new BufferedOutputStream(new FileOutputStream(destFile));
						int currentByte;
						byte[] content = new byte[BUFFER_SIZE];
						while ((currentByte = entryStream.read(content, 0, BUFFER_SIZE)) != -1)
						{
							destStream.write(content, 0, currentByte);
						}

					}
					catch (IOException e)
					{
						throw (CoreException) new CoreException(new Status(IStatus.ERROR, pluginID, e.getMessage(), e)).initCause(e);

					}
					finally
					{
						destStream.flush();
						destStream.close();
						entryStream.close();
					}
				}
			}

		}
		catch (Exception e)
		{
			throw (CoreException) new CoreException(new Status(IStatus.ERROR, pluginID, e.getMessage(), e)).initCause(e);
		}

		proj.create(null);
		proj.open(null);

	}

	/**
	 * Delete the project given by the last segment of the projectArchive Throws CoreException if the project cannot be
	 * deleted.
	 *
	 * @param projectArchive
	 *        a path inside the pluginID that contain the project archive
	 */
	public static void deleteProject(IPath projectArchive) throws CoreException
	{
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IWorkspaceRoot root = workspace.getRoot();
		String projectName = projectArchive.removeFileExtension().lastSegment().toString();

		IProject proj = root.getProject(projectName);
		if (proj.exists())
		{
			deleteProject(proj);
		}
	}

	public static void deleteProject(final IProject project) throws CoreException
	{
		// maybe sphinx is right in the middle of loading the resources, cancel but wait for it

		Object[] jobFamilies = {IExtendedPlatformConstants.FAMILY_MODEL_LOADING, ResourcesPlugin.FAMILY_AUTO_BUILD,
			ResourcesPlugin.FAMILY_AUTO_REFRESH, ResourcesPlugin.FAMILY_MANUAL_BUILD,
			ResourcesPlugin.FAMILY_MANUAL_REFRESH};
		PlatformUtils.waitForJobs(true, true, jobFamilies);
		IWorkspace ws = ResourcesPlugin.getWorkspace();
		IWorkspaceRunnable runnable = new IWorkspaceRunnable()
		{

			public void run(IProgressMonitor monitor) throws CoreException
			{
				ResourceUtils.doubleTryDelete(project, true, true, monitor);
			}
		};
		ws.run(runnable, ws.getRuleFactory().deleteRule(project), IWorkspace.AVOID_UPDATE, null);
		// we have to wait in this moment for the model descriptors to
		// unload the resources
		// maybe sphinx is right in the middle of loading the resources, cancel but wait for it
		PlatformUtils.waitForJobs(false, true, jobFamilies);
	}

	/**
	 * Retrieves the input stream for the resource located in the plugin given by <code>pluginID</code>
	 *
	 * @param pluginID
	 *        the ID of the plugin that contains the resource
	 * @param resource
	 *        a path inside the pluginID that contains the resource
	 * @return the resource input stream
	 * @throws CoreException
	 *         if the input stream cannot be obtained
	 */
	public static InputStream getInputStream(String pluginID, IPath resource) throws CoreException
	{
		Bundle bundle = Platform.getBundle(pluginID);
		assertNotNull("Cannot locate plugin " + pluginID, bundle);
		URL findEntry = FileLocator.find(bundle, resource, null);
		String resourceFileName = "";

		if (findEntry != null)
		{
			try
			{
				resourceFileName = FileLocator.toFileURL(findEntry).getFile();

			}
			catch (IOException e)
			{
				throw (CoreException) new CoreException(new Status(IStatus.ERROR, pluginID, e.getMessage(), e)).initCause(e);
			}
		}

		File resourceFile = new File(resourceFileName);
		try
		{
			return new FileInputStream(resourceFile);
		}
		catch (FileNotFoundException e)
		{
			throw (CoreException) new CoreException(new Status(IStatus.ERROR, pluginID, e.getMessage(), e)).initCause(e);
		}
	}

	/**
	 * Creates a new File instance corresponding to the given <code>path</code>, in the plugin given by the
	 * <code>pluginID</code>.
	 *
	 * @param pluginID
	 *        the ID of the plugin
	 * @param path
	 *        a path inside the pluginID
	 * @return
	 * @throws IOException
	 */
	public static File getResourceByPath(String pluginID, String path) throws IOException
	{
		Bundle bundle = Platform.getBundle(pluginID);
		URL findEntry = FileLocator.find(bundle, new Path(path), null);

		if (findEntry != null)
		{
			return new File(FileLocator.toFileURL(findEntry).getPath());
		}
		return null;
	}

	/**
	 * Compares the content of two input streams.
	 *
	 * @param input1
	 *        the first input stream
	 * @param input2
	 *        the second input stream
	 * @return <code>true</code>, if the contents of the input streams are the same<br>
	 *         <code>false</code>, otherwise
	 * @throws IOException
	 *         if the copy operation failed
	 *
	 */
	public static boolean compare(InputStream input1, InputStream input2) throws IOException
	{

		int input1Byte, input2Byte;
		do
		{
			input1Byte = input1.read();
			input2Byte = input2.read();
			if (input1Byte != input2Byte)
			{
				return false;
			}
		}
		while (input1Byte != -1 && input2Byte != -1);

		return true;
	}

	/**
	 * Compare two input stream line by line. A {@link BufferedReader} will be used for this purpose. The streams will
	 * not be closed at the end of the process
	 *
	 * @param lInput
	 * @param rInput
	 */
	public static DifferencesReport lineDifferences(InputStream lInput, InputStream rInput) throws IOException
	{
		DifferencesReport result = new DifferencesReport();
		BufferedReader lReader = new BufferedReader(new InputStreamReader(lInput));
		BufferedReader rReader = new BufferedReader(new InputStreamReader(rInput));
		try
		{
			String lLine = lReader.readLine();
			String rLine = rReader.readLine();
			while (lLine != null && rLine != null)
			{
				result.leftLines++;
				result.rightLines++;
				if (!lLine.equals(rLine))
				{
					result.differentLines++;
				}
				lLine = lReader.readLine();
				rLine = rReader.readLine();
			}
			// maybe only one of the file ended
			while (lLine != null)
			{
				result.leftLines++;
				result.differentLines++;
				lLine = lReader.readLine();
			}
			while (rLine != null)
			{
				result.rightLines++;
				result.differentLines++;
				rLine = rReader.readLine();
			}
		}
		finally
		{
			lReader.close();
			rReader.close();
		}
		return result;
	}

	/**
	 * Compares the content of a resource located inside a plugin given by <code>pluginID</code> with the content of a
	 * file from the workspace.
	 *
	 * @param pluginID
	 *        the ID of the plugin that contains the resource
	 * @param resource
	 *        a path inside the pluginID that contains the resource
	 * @param other
	 *        a file from the workspace
	 * @return <code>true</code> if the content of the resource is the same with the content of the <code>other</code>
	 *         file<br>
	 *         <code>false</code>,otherwise
	 * @throws CoreException
	 *         if the compare operation failed.
	 *
	 */
	public static boolean compare(String pluginID, IPath resource, IFile other) throws CoreException
	{
		boolean areEqual;

		InputStream is1 = null;
		InputStream is2 = null;
		try
		{
			is1 = getInputStream(pluginID, resource);
			is2 = other.getContents();
			areEqual = compare(is1, is2);
		}
		catch (Exception e)
		{
			throw (CoreException) new CoreException(new Status(IStatus.ERROR, pluginID, e.getMessage(), e)).initCause(e);
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
				throw (CoreException) new CoreException(new Status(IStatus.ERROR, pluginID, e.getMessage(), e)).initCause(e);
			}
		}
		return areEqual;

	}

	/**
	 * Compares the contents of two files from the workspace.
	 *
	 * @param input
	 *        a file from the workspace
	 * @param other
	 *        other file from the workspace
	 * @return <code>true</code> if the content of the <code>input</code> file is the same with the content of the
	 *         <code>other</code> file<br>
	 *         <code>false</code>, otherwise
	 * @throws CoreException
	 *         if the compare operation failed.
	 */
	public static boolean compare(IFile input, IFile other) throws CoreException
	{

		boolean areEqual;
		InputStream is1 = null;
		InputStream is2 = null;
		try
		{
			is1 = input.getContents();
			is2 = other.getContents();

			areEqual = compare(is1, is2);
		}
		catch (IOException e)
		{
			throw (CoreException) new CoreException(new Status(IStatus.ERROR, "", e.getMessage(), e)).initCause(e);
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
				throw (CoreException) new CoreException(new Status(IStatus.ERROR, "", e.getMessage(), e)).initCause(e);
			}
		}

		return areEqual;
	}

	/**
	 * Compares the contents of two folders from the workspace.
	 *
	 * @param input
	 *        a folder from the workspace
	 * @param other
	 *        other folder from the workspace
	 * @return <code>true</code> if the content of the <code>input</code> folder is the same with the content of the
	 *         <code>other</code> folder<br>
	 *         <code>false</code>, otherwise
	 * @throws CoreException
	 *         if the compare operation failed.
	 */
	public static boolean compare(IFolder input, IFolder other) throws CoreException
	{
		try
		{
			return compare(input.getLocation().toFile(), other.getLocation().toFile());
		}
		catch (Exception e)
		{
			throw (CoreException) new CoreException(new Status(IStatus.ERROR, "", e.getMessage(), e)).initCause(e);
		}
	}

	/**
	 * Compares the contents of two files.
	 *
	 * @param input
	 *        an existing file
	 * @param other
	 *        other existing file
	 * @return <code>true</code> if the content of the <code>input</code> file is the same with the content of the
	 *         <code>other</code> file<br>
	 *         <code>false</code>, otherwise
	 * @throws FileNotFoundException
	 *         if the files cannot be found.
	 * @throws IOException
	 *         if the compare operation failed
	 */
	public static boolean compare(File input, File other) throws FileNotFoundException, IOException
	{
		boolean areEqual = false;

		if (input.isFile() && other.isFile())
		{
			InputStream is1 = new FileInputStream(input);
			InputStream is2 = new FileInputStream(other);

			try
			{
				areEqual = compare(is1, is2);
			}
			finally
			{
				ExtendedPlatform.safeClose(is1);
				ExtendedPlatform.safeClose(is2);
			}
			if (areEqual == false)
			{
				return false;
			}
		}
		else
		{
			File[] members1 = input.listFiles();
			File[] members2 = other.listFiles();
			if (members1.length != members2.length)
			{
				return false;
			}

			for (int i = 0; i < members1.length; i++)
			{
				if (!members1[i].getName().equals(members2[i].getName()))
				{
					return false;
				}
				areEqual = compare(members1[i], members2[i]);
			}
		}
		return areEqual;
	}

	/**
	 * Compares the lines of two files that contain the given tag.
	 *
	 * @param input
	 *        an existing file
	 * @param other
	 *        other existing file
	 * @param tag
	 * @return <code>true</code> if the content of the <code>input</code> file is the same with the content of the
	 *         <code>other</code> file<br>
	 *         <code>false</code>, otherwise
	 * @throws FileNotFoundException
	 *         if the files cannot be found.
	 * @throws IOException
	 *         if the compare operation failed
	 */
	public static boolean compareIgnoreOrder(File input, File other, String tag) throws IOException
	{
		List<String> inputLines = getFileLines(input, tag);
		List<String> otherLines = getFileLines(other, tag);

		if (inputLines.isEmpty() && otherLines.isEmpty())
		{
			return true;
		}

		inputLines.retainAll(otherLines);

		return inputLines.size() == otherLines.size();
	}

	private static List<String> getFileLines(File input, String tag) throws IOException
	{
		List<String> lines = new ArrayList<>();
		BufferedReader reader = new BufferedReader(new FileReader(input));

		String line;
		while ((line = reader.readLine()) != null)
		{
			if (line.contains(tag))
			{
				lines.add(line);
			}
		}
		reader.close();

		return lines;
	}

	/**
	 * Copy the content of an input stream into an output stream.
	 *
	 * @param input
	 *        the input stream whom content will be copied
	 * @param output
	 *        the output stream in which the content will be copied
	 * @throws IOException
	 *         if the copy operation failed
	 */
	public static void copyStream(InputStream input, OutputStream output) throws IOException
	{

		int currentByte;
		byte[] content = new byte[BUFFER_SIZE];

		while ((currentByte = input.read(content, 0, BUFFER_SIZE)) != -1)
		{
			output.write(content, 0, currentByte);
		}
	}

	/**
	 * Copy the content of a resource located in the plugin given by <code>pluginID</code> into a file from the
	 * workspace.
	 *
	 * @param pluginID
	 *        the ID of the plugin that contains the resource
	 * @param resource
	 *        a path inside the pluginID that contains the resource
	 * @param target
	 *        the target file from the workspace
	 * @param override
	 *        if <code>true</code> and <code> target</code> file exists,it will be overridden <br>
	 *        if <code>false</code>,the <code>target</code> file must not exist in the workspace.
	 * @throws CoreException
	 *         if the copy operation failed
	 */
	public static void copyFile(String pluginID, IPath resource, IFile target, boolean override) throws CoreException
	{

		InputStream is1 = getInputStream(pluginID, resource);

		if (target.exists())
		{
			if (override == false)
			{
				throw new CoreException(new Status(IStatus.ERROR, pluginID, "the target file already exists,override"
					+ " must be true", new Exception()));
			}

			target.setContents(is1, true, false, null);
		}

		else
		{
			target.create(is1, true, null);
		}

		if (is1 != null)
		{
			try
			{
				is1.close();
			}
			catch (IOException e)
			{
				throw (CoreException) new CoreException(new Status(IStatus.ERROR, "", e.getMessage(), e)).initCause(e);
			}
		}
	}

	/**
	 * Copy the content of a resource located in a plugin given by <code>pluginID</code> into a file.
	 *
	 * @param pluginID
	 *        the ID of the plugin that contains the resource
	 * @param resource
	 *        a path inside the pluginID that contains the resource
	 * @param target
	 *        the target file from the workspace
	 * @param override
	 *        if <code>true</code> and <code>target</code> exists,it will be overridden <br>
	 *        if <code>false</code> , <code>target</code> must not exist
	 * @throws CoreException
	 *         if the copy operation failed
	 */
	public static void copyFile(String pluginID, IPath resource, File target, boolean override) throws CoreException
	{

		if (target.exists())
		{
			if (override == false)
			{
				throw new CoreException(new Status(IStatus.ERROR, pluginID, "the target file already exists,override"
					+ " must be true", new Exception()));
			}
		}
		else
		{
			try
			{
				target.createNewFile();
			}
			catch (IOException e)
			{
				throw (CoreException) new CoreException(new Status(IStatus.ERROR, pluginID, e.getMessage(), e)).initCause(e);
			}
		}

		InputStream is = null;
		OutputStream targetStream = null;

		try
		{
			targetStream = new FileOutputStream(target);
			is = getInputStream(pluginID, resource);
			copyStream(is, targetStream);
		}
		catch (Exception e)
		{
			throw (CoreException) new CoreException(new Status(IStatus.ERROR, "", e.getMessage(), e)).initCause(e);
		}
		finally
		{
			try
			{
				if (is != null)
				{
					is.close();
				}
				if (targetStream != null)
				{
					targetStream.close();
				}
			}
			catch (IOException e)
			{
				throw (CoreException) new CoreException(new Status(IStatus.ERROR, "", e.getMessage(), e)).initCause(e);
			}
		}
	}

	/**
	 * Copy the contents of a source file into a target file from the workspace.
	 *
	 * @param source
	 *        the source file from the workspace
	 * @param target
	 *        the target file from the workspace
	 * @param override
	 *        if <code>true</code> and <code>target</code> exists,it will be overridden <br>
	 *        if <code>false</code>, <code> target</code> must not exist.
	 * @throws CoreException
	 *         if the copy operation failed
	 */
	public static void copyFile(IFile source, IFile target, boolean override) throws CoreException
	{

		InputStream is = source.getContents();

		if (target.exists())
		{
			if (override == false)
			{
				throw new CoreException(new Status(IStatus.ERROR, "", "the target file already exists,override"
					+ " must be true", new Exception()));
			}

			target.setContents(is, true, false, null);
		}
		else
		{
			target.create(is, true, null);
		}

		if (is != null)
		{
			try
			{
				is.close();
			}
			catch (IOException e)
			{
				throw (CoreException) new CoreException(new Status(IStatus.ERROR, "", e.getMessage(), e)).initCause(e);
			}
		}
	}

	/**
	 * Copy the <code>sourceFile</code> to <code>destFile</code>. If <code>destFile</code> does not exist it will be
	 * created, otherwise will be overwritten
	 *
	 * @param sourceFile
	 *        the file to copy from
	 * @param destFile
	 *        the file to copy to
	 * @throws CoreException
	 *         if fails to handle the files
	 */
	public static void copyFile(File sourceFile, File destFile) throws CoreException
	{
		FileInputStream inStream = null;
		FileOutputStream outStream = null;

		try
		{
			inStream = new FileInputStream(sourceFile);
			outStream = new FileOutputStream(destFile);

			byte[] buffer = new byte[1024];

			int length;
			while ((length = inStream.read(buffer)) > 0)
			{
				outStream.write(buffer, 0, length);
			}
		}
		catch (IOException e)
		{
			throw (CoreException) new CoreException(new Status(IStatus.ERROR, "", e.getMessage(), e)).initCause(e);
		}
		finally
		{
			if (inStream != null)
			{
				try
				{
					inStream.close();
				}
				catch (IOException e)
				{
					throw (CoreException) new CoreException(new Status(IStatus.ERROR, "", e.getMessage(), e)).initCause(e);
				}
			}

			if (outStream != null)
			{
				try
				{
					outStream.close();
				}
				catch (IOException e)
				{
					throw (CoreException) new CoreException(new Status(IStatus.ERROR, "", e.getMessage(), e)).initCause(e);
				}
			}
		}
	}

	/**
	 * Copy the contents of a resource located inside a plugin given by <code>pluginID</code> into a container from the
	 * workspace.
	 *
	 * @param pluginID
	 *        the ID of the plugin that contains the resource
	 * @param resource
	 *        a path inside the pluginID that contains the resource
	 * @param target
	 *        the target container from the workspace
	 * @param override
	 *        if <code>true</code> and <code>target</code> exists, <code>merge</code> will decide the copy style <br>
	 *        if <code>false </code>, <code>target</code> must not exist in the workspace
	 * @param merge
	 *        if <code>true</code>, a merge operation between the resource and target contents will be performed<br>
	 *        if <code>false </code>,the target's initial content will be deleted.
	 * @throws CoreException
	 *         if the copy operation failed
	 */

	public static void copyFolder(String pluginID, IPath resource, IContainer target, boolean override, boolean merge)
		throws CoreException
	{

		Bundle bundle = Platform.getBundle(pluginID);
		URL findEntry = FileLocator.find(bundle, resource, null);
		String resourceFileName = "";

		if (findEntry != null)
		{
			try
			{
				resourceFileName = FileLocator.toFileURL(findEntry).getFile();

			}
			catch (IOException e)
			{
				throw (CoreException) new CoreException(new Status(IStatus.ERROR, pluginID, e.getMessage(), e)).initCause(e);
			}
		}

		if (!target.exists())
		{
			if (target.getType() == IResource.FOLDER)
			{
				((IFolder) target).create(true, false, null);
			}
			else
			{
				((IProject) target).create(null);
			}
		}

		else
		{
			if (override == false)
			{
				throw new CoreException(new Status(IStatus.ERROR, "", "the target folder already exists,override"
					+ " must be true", new Exception()));
			}
			if (merge == false)
			{
				IResource[] targetMembers = target.members();
				for (int i = 0; i < targetMembers.length; i++)
				{
					ResourceUtils.doubleTryDelete(targetMembers[i], true, null);
				}
			}

		}
		File resourceFolder = new File(resourceFileName);

		// isRootFolder=true,to copy only the content of resourceFolder
		visitFolder(resourceFolder, target, true);
	}

	private static void visitFolder(File resourceFolder, IContainer target, boolean isRootFolder) throws CoreException
	{

		InputStream is = null;

		if (resourceFolder.isFile())
		{
			IFile file = target.getFile(new Path(resourceFolder.getName()));
			try
			{

				is = new FileInputStream(resourceFolder);
				if (file.exists())
				{
					file.setContents(is, true, false, null);
				}
				else
				{
					file.create(is, true, null);
				}
			}
			catch (FileNotFoundException e)
			{
				throw (CoreException) new CoreException(new Status(IStatus.ERROR, "", e.getMessage(), e)).initCause(e);
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
						throw (CoreException) new CoreException(new Status(IStatus.ERROR, "", e.getMessage(), e)).initCause(e);
					}
				}
			}
		}
		else
		{
			IContainer folder = null;
			if (isRootFolder)
			{
				folder = target;
			}
			else
			{
				folder = target.getFolder(new Path(resourceFolder.getName()));
				if (folder.exists())
				{
					ResourceUtils.doubleTryDelete(folder, true, null);
				}
				((IFolder) folder).create(true, false, null);
			}
			String[] membersNames = resourceFolder.list();
			for (int i = 0; i < membersNames.length; i++)
			{
				visitFolder(new File(resourceFolder, membersNames[i]), folder, false);
			}
		}
	}

	/**
	 * Copy the content of a resource located in the plugin give by <code>pluginID</code> in a target file.
	 *
	 * @param pluginID
	 *        the ID of the plugin that contains the resource
	 * @param resource
	 *        a path inside the pluginID that contains the resource
	 * @param target
	 *        the target folder
	 * @param override
	 *        if <code>true</code> and <code>target</code> folder exists,it will be overridden <br>
	 *        if <code>false</code>,the <code>target</code> folder must not exist.
	 * @param merge
	 *        if <code>true</code>, a merge operation between the resource and target contents will be performed<br>
	 *        if <code>false </code>,the target's initial content will be deleted.
	 *
	 * @throws CoreException
	 *         if the copy operation failed
	 */
	public static void copyFolder(String pluginID, IPath resource, File target, boolean override, boolean merge)
		throws CoreException
	{

		Bundle bundle = Platform.getBundle(pluginID);
		URL findEntry = FileLocator.find(bundle, resource, null);
		String resourceFileName = "";

		if (findEntry != null)
		{
			try
			{
				resourceFileName = FileLocator.toFileURL(findEntry).getFile();

			}
			catch (IOException e)
			{
				throw (CoreException) new CoreException(new Status(IStatus.ERROR, pluginID, e.getMessage(), e)).initCause(e);
			}
		}

		if (!target.exists())
		{
			target.mkdir();
		}
		else
		{
			if (override == false)
			{
				throw new CoreException(new Status(IStatus.ERROR, "", "the target folder already exists,override"
					+ " must be true", new Exception()));
			}
			if (merge == false)
			{
				target.delete();
			}

		}

		File resourceFolder = new File(resourceFileName);

		// isRootFolder=true,to copy only the content of resourceFolder
		visitFolder(resourceFolder, target, true);
	}

	private static void visitFolder(File resource, File target, boolean isRootFolder) throws CoreException
	{

		InputStream is = null;
		OutputStream os = null;

		File resourceTargetFile = null;
		File parentFile = null;

		if (!isRootFolder)
		{
			resourceTargetFile = new File(target, resource.getName());
			if (resourceTargetFile.exists() && resourceTargetFile.isDirectory())
			{
				File[] dirFiles = resourceTargetFile.listFiles();
				for (int i = 0; i < dirFiles.length; i++)
				{
					deleteDirContent(dirFiles[i]);
				}

			}

			parentFile = resourceTargetFile.getParentFile();
			parentFile.mkdirs();
		}
		else
		{
			resourceTargetFile = target;
		}

		if (resource.isFile())
		{
			try
			{
				is = new FileInputStream(resource);
				os = new FileOutputStream(resourceTargetFile);

				copyStream(is, os);
			}
			catch (Exception e)
			{
				throw (CoreException) new CoreException(new Status(IStatus.ERROR, "", e.getMessage(), e)).initCause(e);
			}
			finally
			{
				try
				{
					if (is != null)
					{
						is.close();
					}
					if (os != null)
					{
						os.close();
					}
				}
				catch (IOException e)
				{
					throw (CoreException) new CoreException(new Status(IStatus.ERROR, "", e.getMessage(), e)).initCause(e);
				}
			}
		}
		else
		{
			String membersNames[] = resource.list();
			if (membersNames != null)
			{
				for (int i = 0; i < membersNames.length; i++)
				{
					visitFolder(new File(resource, membersNames[i]), resourceTargetFile, false);
				}
			}
		}
	}

	public static void deleteDirContent(File file)
	{

		if (file.isFile())
		{
			file.delete();
		}
		else
		{
			File[] fileMembers = file.listFiles();
			for (int i = 0; i < fileMembers.length; i++)
			{
				deleteDirContent(fileMembers[i]);
			}
		}
	}

	/**
	 *
	 * @param source
	 *        the source folder from the workspace whom contents will be copied
	 * @param target
	 *        the target container from the workspace
	 * @param override
	 *        if <code>true</code> and <code>target</code> folder exists, <code>merge</code> parameter will decide the
	 *        copy style <br>
	 *        if <code>false</code>,the <code>target</code> container must not exist in the workspace
	 * @param merge
	 *        if <code>true</code>, a merge operation between the source and target contents will be performed<br>
	 *        if <code>false</code>,the target's initial content will be deleted.
	 * @throws CoreException
	 *         if the copy operation failed.
	 */
	public static void copyFolder(IFolder source, IContainer target, boolean override, boolean merge)
		throws CoreException
	{

		InputStream is = null;

		if (target.exists())
		{
			if (override == false)
			{
				throw new CoreException(new Status(IStatus.ERROR, "", "the target folder already exists,override"
					+ " must be true", new Exception()));
			}
			if (merge == false)
			{
				// delete target's content
				IResource[] targetMembers = target.members();
				for (int i = 0; i < targetMembers.length; i++)
				{
					ResourceUtils.doubleTryDelete(targetMembers[i], true, null);
				}

				// copy new content
				IResource[] resourceMembers = source.members();
				for (int i = 0; i < resourceMembers.length; i++)
				{
					resourceMembers[i].copy(target.getFolder(new Path(resourceMembers[i].getName())).getFullPath(),
						true, null);
				}

			}
			else
			{
				// append content
				IResource[] sourceMembers = source.members();
				for (int i = 0; i < sourceMembers.length; i++)
				{
					IResource currentResource = sourceMembers[i];

					if (currentResource.getType() == IResource.FILE)
					{
						// check for existence
						IFile existentFile = target.getFile(new Path(currentResource.getName()));
						if (existentFile.exists())
						{
							is = ((IFile) currentResource).getContents();
							existentFile.setContents(is, true, false, null);
							try
							{
								is.close();
							}
							catch (IOException e)
							{
								throw (CoreException) new CoreException(
									new Status(IStatus.ERROR, "", e.getMessage(), e)).initCause(e);
							}
						}
						else
						{
							currentResource.copy(target.getFile(new Path(currentResource.getName())).getFullPath(),
								true, null);
						}
					}
					else
					{
						// folder
						// check for existence
						IFolder existentFolder = target.getFolder(new Path(currentResource.getName()));
						if (existentFolder.exists())
						{
							ResourceUtils.doubleTryDelete(existentFolder, true, null);
						}
						currentResource.copy(target.getFolder(new Path(currentResource.getName())).getFullPath(), true,
							null);
					}
				}
			}

		}
		else
		{
			source.copy(target.getFullPath(), true, null);

		}
	}

	public static void deleteFolder(File folder)
	{
		if (folder.isFile())
		{
			folder.delete();
		}
		else
		{
			File[] fileMembers = folder.listFiles();
			for (int i = 0; i < fileMembers.length; i++)
			{
				deleteFolder(fileMembers[i]);
			}
			folder.delete();
		}
	}

	/**
	 * Copy all tests from source to target
	 *
	 * @param target
	 * @param source
	 */
	public static void combineTestSuites(TestSuite target, TestSuite source)
	{
		Enumeration<Test> tests = source.tests();
		while (tests.hasMoreElements())
		{
			target.addTest(tests.nextElement());
		}
	}

	public static String loadFile(final IProject project, final String filePath)
	{
		// read from project specified file
		IFile iJavaFile = project.getFile(filePath);
		Assert.assertTrue(iJavaFile.isAccessible());

		String result = null;
		try
		{
			InputStream inputStream = iJavaFile.getContents();

			byte[] fileData = new byte[inputStream.available()];
			inputStream.read(fileData);
			inputStream.close();
			result = new String(fileData);
		}
		catch (Exception e)
		{
			Assert.fail("Error occured while loading file: " + filePath + " \nDetails: " + e.getMessage());
		}
		return result;
	}

	/**
	 * Compute the path of the .zip file containing the project to be imported
	 *
	 * @param pluginID
	 * @param projectArchivePath
	 * @return
	 */
	public static String getProjectArchivePath(String pluginID, IPath projectArchivePath)
	{
		String projectArchiveName = null;
		Bundle bundle = Platform.getBundle(pluginID);
		URL findEntry = FileLocator.find(bundle, projectArchivePath, null);

		if (findEntry != null)
		{
			try
			{
				projectArchiveName = FileLocator.toFileURL(findEntry).getFile();
			}
			catch (IOException e)
			{
				CessarTestCase.fail(e);
			}
		}
		return projectArchiveName;
	}

	/**
	 * @param projectArchive
	 * @param override
	 * @return
	 */
	public static IProject importProject(IPath projectArchive, boolean override) throws CoreException
	{
		String projectName = projectArchive.removeFileExtension().lastSegment().toString();

		return importProject(projectArchive, projectName, override);
	}

	/**
	 * @param projectArchive
	 * @param projectName
	 * @param override
	 * @return
	 */
	public static IProject importProject(IPath projectArchive, String projectName, boolean override)
		throws CoreException
	{
		final IWorkspace ws = ResourcesPlugin.getWorkspace();

		final IProject proj = ws.getRoot().getProject(projectName);

		if (proj.exists())
		{
			if (!override)
			{
				return proj;
			}
			else
			{
				deleteProject(proj);
			}
		}
		IWorkspaceRunnable importer = new IWorkspaceRunnable()
		{
			public void run(IProgressMonitor monitor) throws CoreException
			{
				runImportProject(projectArchive, projectName, proj);
			}

		};
		ws.run(importer, ws.getRuleFactory().createRule(proj), IWorkspace.AVOID_UPDATE, null);

		return proj;
	}

	private static void runImportProject(IPath projectArchive, String projectName, IProject proj) throws CoreException
	{
		String workspaceLocation = ResourcesPlugin.getWorkspace().getRoot().getLocation().toOSString();
		try
		{

			ZipFile zipFile = new ZipFile(new File(projectArchive.toString()));
			// ZipFile zipFile = new ZipFile(new File(
			// "D:/GitTestProjects/testprojects/projects/performance/BMWSAS35up.zip"));

			Enumeration<ZipEntry> entries = (Enumeration<ZipEntry>) zipFile.entries();

			// process each entry
			while (entries.hasMoreElements())
			{
				ZipEntry currentEntry = entries.nextElement();
				String currentEntryName = currentEntry.getName();
				BufferedInputStream entryStream = null;
				BufferedOutputStream destStream = null;

				String archiveName = projectArchive.removeFileExtension().lastSegment().toString();
				currentEntryName = currentEntryName.replaceFirst(archiveName, projectName);
				File destFile = new File(workspaceLocation, currentEntryName);
				File destFileParent = destFile.getParentFile();
				destFileParent.mkdirs();

				// extract current entry if is a file
				if (!currentEntry.isDirectory())
				{
					try
					{
						// read from the archive entry
						entryStream = new BufferedInputStream(zipFile.getInputStream(currentEntry));
						destStream = new BufferedOutputStream(new FileOutputStream(destFile));
						int currentByte;
						byte[] content = new byte[BUFFER_SIZE];
						while ((currentByte = entryStream.read(content, 0, BUFFER_SIZE)) != -1)
						{
							destStream.write(content, 0, currentByte);
						}

					}
					catch (IOException e)
					{
						throw (CoreException) new CoreException(new Status(IStatus.ERROR, "no plugin", e.getMessage(),
							e)).initCause(e);

					}
					finally
					{
						destStream.flush();
						destStream.close();
						entryStream.close();
					}
				}
			}

		}
		catch (Exception e)
		{
			throw (CoreException) new CoreException(new Status(IStatus.ERROR, "no plugin", e.getMessage(), e)).initCause(e);
		}

		proj.create(null);
		proj.open(null);

	}

}
