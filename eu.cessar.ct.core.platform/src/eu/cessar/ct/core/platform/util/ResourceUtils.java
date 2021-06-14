/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 * 
 * </copyright>
 */
package eu.cessar.ct.core.platform.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.internal.resources.LocationValidator;
import org.eclipse.core.internal.resources.Workspace;
import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.core.runtime.content.IContentDescription;
import org.eclipse.core.runtime.content.IContentType;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.eclipse.core.runtime.jobs.Job;

/**
 * Resource utility methods.
 */
// SUPPRESS CHECKSTYLE allow a higher complexity as this is an utility class
@SuppressWarnings("restriction")
public final class ResourceUtils
{
	private static LocationValidator locValidator;

	private ResourceUtils()
	{
		// this class should not be inherited
	}

	static
	{
		locValidator = new LocationValidator((Workspace) ResourcesPlugin.getWorkspace());
	}

	/**
	 * Validates a path.
	 * 
	 * @param path
	 *        the path to validate
	 * @param type
	 *        the resource's type; can be <code>IResource.FILE, IResource.FOLDER</code> or
	 *        <code>IResource.PROJECT</code>
	 * @return <code>true</code>, if the given path is valid<br>
	 *         <code>false</code>, otherwise
	 */
	public static boolean isValidPath(String path, int type)
	{
		IStatus status = locValidator.validatePath(path, type);
		return status.isOK();
	}

	/**
	 * Creates an empty IFile or an IFolder depending on the <code>isFile</code> flag, in the path specified by
	 * <code>location</code>, path which must be relative to the given <code>parent</code> container. <br>
	 * The folders corresponding to the intermediate segments of path <code>location</code>, will be automatically
	 * created if missing.
	 * <p>
	 * <strong>NOTE:</strong> if the provided <code>location</code> corresponds to an exiting IFile and
	 * <code>isFile</code>==<code>true</code>, the respective IFile will be simply returned!
	 * 
	 * <P>
	 * Usage:
	 * 
	 * <pre>
	 * 	IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject("Test");  //access an existing project
	 * 	Assert.isTrue(project.isAccessible())
	 * 		
	 * 	IFile file = (IFile) ResourceUtils.createResource(project, new Path("/dir1/di2/file.txt", true, new NullProgressMonitor()); // create a file; neither dir1, nor dir2 need to exist (will be created automatically) 
	 *  IFolder folder = (IFolder)ResourceUtils.createResource(project, new Path("/dirA/diB/dirC", false, new NullProgressMonitor());  // create a folder; neither dirA, nor dirB need to exist (will be created automatically)
	 * 
	 * </pre>
	 * 
	 * @param parent
	 *        the root container; must not be <code>null</code>
	 * @param location
	 *        the (parent relative) path to the resource to be created; must not be <code>null</code>
	 * @param isFile
	 *        if <code>true</code>, for the last <code>location</code> segment a file will be created and returned (or
	 *        simply returned if already existing), if <code>false</code>, a folder will be created and returned
	 * @param monitor
	 *        a progress monitor instance
	 * @return A {@link IResource} instance that can be an {@link IFile} if <code>isFile</code> is <code>true</code>, or
	 *         an {@link IFolder} if <code>isFile</code> is <code>false</code>
	 * @throws CoreException
	 *         if the operation is canceled or one of the path segments cannot be created.
	 */
	public static IResource createResource(final IContainer parent, final IPath location, final boolean isFile,
		final IProgressMonitor monitor) throws CoreException
	{
		Assert.isNotNull(parent);
		Assert.isNotNull(location);

		IContainer tmpFolder = parent;
		String[] segments = location.segments();
		for (int i = 0; i < segments.length; i++)
		{
			boolean isLastElement = (i == segments.length - 1);
			if (isFile && isLastElement)
			{
				break;
			}

			IFolder folder = tmpFolder.getFolder(new Path(segments[i]));
			if (!folder.exists())
			{
				folder.create(true, true, monitor);
			}
			tmpFolder = folder;
		}

		IResource result;

		if (isFile)
		{
			Assert.isLegal(location.segmentCount() > 0);

			result = tmpFolder.getFile(new Path(segments[segments.length - 1]));

			// create an empty file
			if (!result.exists())
			{
				InputStream source = new ByteArrayInputStream(new byte[0]);
				((IFile) result).create(source, IResource.NONE, new NullProgressMonitor());
			}
		}
		else
		{
			result = tmpFolder;
		}

		return result;
	}

	/**
	 * Creates the directory named by this abstract pathname, including any necessary but nonexistent parent
	 * directories. Note that if this operation fails it may have succeeded in creating some of the necessary parent
	 * directories.<br/>
	 * The method will run in a IWorkspaceRunnable
	 * 
	 * @param folder
	 * @param monitor
	 * @throws CoreException
	 */
	public static void mkDirs(IFolder folder, IProgressMonitor monitor) throws CoreException
	{
		if (folder.exists())
		{
			return;
		}
		else
		{
			final List<IFolder> todos = new ArrayList<IFolder>();
			IFolder aFolder = folder;
			ISchedulingRule rule = aFolder.getParent();
			while (!aFolder.exists())
			{
				todos.add(aFolder);
				rule = aFolder.getParent();
				if (aFolder.getParent() instanceof IFolder)
				{
					aFolder = (IFolder) aFolder.getParent();
				}
				else
				{
					break;
				}
			}
			folder.getWorkspace().run(new IWorkspaceRunnable()
			{

				public void run(IProgressMonitor innerMonitor) throws CoreException
				{
					for (int i = todos.size() - 1; i >= 0; i--)
					{
						if (!todos.get(i).exists())
						{
							todos.get(i).create(true, true, innerMonitor);
						}
					}
				}
			}, rule, IWorkspace.AVOID_UPDATE, monitor);
		}
	}

	/**
	 * Separate the files from the input list to a map of project / file
	 * 
	 * @param files
	 * @return a map of project - files[]
	 */
	public static Map<IProject, List<IFile>> separateByProject(List<IFile> files)
	{
		Map<IProject, List<IFile>> result = new HashMap<IProject, List<IFile>>();

		for (IFile aFile: files)
		{
			IProject project = aFile.getProject();
			List<IFile> fileList = result.get(project);
			if (fileList == null)
			{
				fileList = new ArrayList<IFile>();
				result.put(project, fileList);
			}
			if (!fileList.contains(aFile))
			{
				fileList.add(aFile);
			}
		}
		return result;
	}

	/**
	 * Locate a resource into a project that can be found at a particular system location
	 * 
	 * @param project
	 * @param location
	 * @return the first resource or null if there is no such resource
	 * @throws CoreException
	 */
	public static IResource getResourceForLocation(final IProject project, final IPath location) throws CoreException
	{
		final String resName = location.lastSegment();
		final IResource result[] = new IResource[1];

		project.accept(new IResourceProxyVisitor()
		{
			public boolean visit(final IResourceProxy proxy) throws CoreException
			{
				if (result[0] != null)
				{
					return false;
				}
				if (proxy.getName().equals(resName))
				{
					// verify the full name
					IResource res = proxy.requestResource();
					if (res.getLocation().equals(location))
					{
						result[0] = res;
						return false;
					}
				}
				return true;
			}
		}, 0);
		return result[0];
	}

	/**
	 * @param fileName
	 * @return the extension of the file
	 */
	public static String getExtension(String fileName)
	{
		if (fileName == null)
		{
			return null;
		}
		int index = fileName.lastIndexOf('.');
		if (index == -1)
		{
			return null;
		}
		return fileName.substring(index + 1);
	}

	/**
	 * @param project
	 * @param extension
	 * @return all files from a project that have a particular extension, never null
	 * @throws CoreException
	 */
	public static List<IFile> getProjectFiles(IProject project, final String extension) throws CoreException
	{
		final List<IFile> result = new ArrayList<IFile>();
		project.accept(new IResourceProxyVisitor()
		{

			public boolean visit(IResourceProxy proxy) throws CoreException
			{
				if (proxy.getType() == IResource.FILE)
				{
					if (extension.equalsIgnoreCase(getExtension(proxy.getName())))
					{
						result.add((IFile) proxy.requestResource());
					}
				}
				return true;
			}
		}, 0);
		return result;
	}

	/**
	 * Returns the {@link IFile} corresponding to the given <code>file</code>. <code>null</code> is returned if the
	 * given file is not under the location of any existing project in the workspace.
	 * 
	 * 
	 * @param file
	 *        the file for which to get the corresponding {@link IFile}
	 * @return the corresponding {@link IFile} or <code>null</code> if the given file is not under the location of any
	 *         existing project in the workspace.
	 */
	public static IFile getIFile(File file)
	{
		IFile iFile = null;
		if (file != null)
		{
			IWorkspace workspace = ResourcesPlugin.getWorkspace();
			IPath location = Path.fromOSString(file.getAbsolutePath());
			iFile = workspace.getRoot().getFileForLocation(location);
		}

		return iFile;
	}

	/**
	 * Gets all files with {@link fileName} inside the {@link project}
	 * 
	 * @param project
	 *        to search into
	 * @param fileName
	 *        to search for
	 * @return all files with the given {@link fileName} inside the {@link project}
	 * @throws CoreException
	 */
	public static List<IFile> getIFileFromProject(IProject project, final String fileName) throws CoreException
	{
		final List<IFile> result = new ArrayList<IFile>();
		project.accept(new IResourceProxyVisitor()
		{

			public boolean visit(IResourceProxy proxy) throws CoreException
			{
				if (proxy.getType() == IResource.FILE)
				{
					if (fileName.equalsIgnoreCase(proxy.getName()))
					{
						result.add((IFile) proxy.requestResource());
					}
				}
				return true;
			}
		}, 0);
		return result;
	}

	/**
	 * Gets all files having the content type {@link contentType} inside the {@link project}
	 * 
	 * @param project
	 *        to search into
	 * @param contentType
	 *        content type of the searched files
	 * @return all files having the content type {@link contentType} inside the {@link project}
	 * @throws CoreException
	 */
	public static List<IFile> getIFileFromProjectForContentType(IProject project, final IContentType contentType)
		throws CoreException
	{
		final List<IFile> result = new ArrayList<IFile>();
		project.accept(new IResourceProxyVisitor()
		{

			public boolean visit(final IResourceProxy proxy) throws CoreException
			{
				if (proxy.getType() == IResource.FILE && contentType.isAssociatedWith(proxy.getName()))
				{
					result.add((IFile) proxy.requestResource());
				}
				return true;
			}
		}, 0);
		return result;
	}

	/**
	 * Returns the content type of the given <code>iFile</code>.
	 * 
	 * @param iFile
	 * @return the file's content type or <code>null</code>
	 * @throws CoreException
	 *         if the method fails
	 */
	public static IContentType getContentType(IFile iFile) throws CoreException
	{
		IContentType contentType = null;
		IContentDescription contentDescription = iFile.getContentDescription();
		if (contentDescription != null)
		{
			contentType = contentDescription.getContentType();
		}
		return contentType;
	}

	/**
	 * Remove unused links from specified project.
	 * 
	 * @param project
	 *        the instance of IProject from where the links must be removed
	 * @param monitor
	 *        The monitor to be used, should not be null
	 * @return the number of removed links
	 * @throws CoreException
	 */
	public static int removeUnusedLinks(final IProject project, final IProgressMonitor monitor) throws CoreException
	{
		monitor.subTask("Removing invalid or inexistent links"); //$NON-NLS-1$

		// collect all linked resources from the project
		final List<IResource> missingLinkedResources = new ArrayList<IResource>();
		project.accept(new IResourceProxyVisitor()
		{
			public boolean visit(final IResourceProxy resProxy) throws CoreException
			{
				if (isTopDotFolder(resProxy) || monitor.isCanceled())
				{
					return false;
				}
				if (resProxy.isLinked())
				{
					IResource resource = resProxy.requestResource();
					if (!resource.getLocation().toFile().exists())
					{
						missingLinkedResources.add(resource);
					}
				}
				return true;
			}
		}, 0);

		if (monitor.isCanceled())
		{
			return 0;
		}
		// remove invalid or missing resources detected in the previous step
		for (IResource resource: missingLinkedResources)
		{
			doubleTryDelete(resource, false, new SubProgressMonitor(monitor, 1));
		}

		return missingLinkedResources.size();
	}

	/**
	 * Returns true if the provided resource is a top level folder (it's parent is a project) and have it's name started
	 * with a dot
	 * 
	 * @param resource
	 *        resource to be checked if is relevant
	 * @return <code>true</code> if condition is met.
	 */
	public static boolean isTopDotFolder(final IResourceProxy resource)
	{
		return (resource.getType() == IResource.FOLDER && resource.getName().startsWith(".") //$NON-NLS-1$
		&& resource.requestResource().getParent().getType() == IResource.PROJECT);
	}

	/**
	 * Tries to delete a resource in a "safe" way to overcome a <a
	 * href="https://bugs.eclipse.org/bugs/show_bug.cgi?id=332607">Eclipse Windows 7 bug</a>
	 * 
	 * @param resource
	 * @param force
	 * @param monitor
	 * @throws CoreException
	 */
	public static void doubleTryDelete(IResource resource, boolean force, IProgressMonitor monitor)
		throws CoreException
	{
		try
		{
			resource.delete(force, monitor);
		}
		catch (CoreException e)
		{
			// it failed to delete, wait a bit and try again
			synchronized (ResourceUtils.class)
			{
				try
				{
					ResourceUtils.class.wait(250);
				}
				catch (InterruptedException e1)
				{
					// ignore it
				}
			}
			// this time do not catch the exception, let it throw
			resource.delete(force, monitor);
		}
	}

	/**
	 * Tries to delete a project in a "safe" way to overcome a <a
	 * href="https://bugs.eclipse.org/bugs/show_bug.cgi?id=332607">Eclipse Windows 7 bug</a>
	 * 
	 * @param project
	 * @param deleteContent
	 * @param force
	 * @param monitor
	 * @throws CoreException
	 */
	public static void doubleTryDelete(IProject project, boolean deleteContent, boolean force, IProgressMonitor monitor)
		throws CoreException
	{
		try
		{
			project.delete(deleteContent, force, monitor);
		}
		catch (CoreException e)
		{
			// it failed to delete, wait a bit and try again
			synchronized (ResourceUtils.class)
			{
				try
				{
					ResourceUtils.class.wait(500);
				}
				catch (InterruptedException e1)
				{
					// ignore it
				}
			}
			// this time do not catch the exception, let it throw
			try
			{
				project.delete(deleteContent, force, monitor);
			}
			catch (CoreException e1)
			{
				// try to GC the JVM, hopefully will close the streams of all out of scope streams.
				System.gc();
				System.gc();
				// last try
				try
				{
					Thread.sleep(250);
				}
				catch (InterruptedException e2)
				{
					// ignore
				}
				project.delete(deleteContent, force, monitor);
				// if we reach this point the delete was successful after the GC, print this
				System.out.println("KiRo: Successful delete after GC of the project: " + project);
			}
		}
	}

	/**
	 * Set all resources from <code>resourceList</code> to Read/Only or Read/Write.
	 * 
	 * @param resourceList
	 *        the resource list to set the status
	 * @param readOnly
	 *        the read only flag
	 * @throws CoreException
	 *         if error occur, for example when the resource does not exists.
	 * @throws InterruptedException
	 * @see setResourceReadOnly
	 */
	public static void setResourcesReadOnly(Collection<IResource> resourceList, boolean readOnly) throws CoreException,
		InterruptedException
	{
		for (IResource resource: resourceList)
		{
			setResourceReadOnly(resource, readOnly);
		}
	}

	/**
	 * Set the <code>resource</code> to Read/Only or Read/Write. The method will use the {@link ResourceAttributes} of
	 * the {@link IResource} and will also wait for the {@link ResourcesPlugin#FAMILY_AUTO_REFRESH} job to finish.
	 * 
	 * @param resource
	 *        the resource to set the status
	 * @param readOnly
	 *        the read only flag
	 * @throws CoreException
	 *         if error occur, for example when the resource does not exists.
	 * @throws InterruptedException
	 */
	public static void setResourceReadOnly(IResource resource, boolean readOnly) throws CoreException,
		InterruptedException
	{
		ResourceAttributes attr = resource.getResourceAttributes();
		if (attr.isReadOnly() != readOnly)
		{
			attr.setReadOnly(readOnly);
			resource.setResourceAttributes(attr);
			// It might happen that we don't see the RO status immediately, a refresh is happening in the background.
			// Wait for it.
			Job.getJobManager().join(ResourcesPlugin.FAMILY_AUTO_REFRESH, null);
			resource.refreshLocal(IResource.DEPTH_ZERO, null);
			// verify that the resource is read-only, if is not try to locate the File behind and make that one
			// read-only
			URI uri = resource.getLocationURI();
			if (uri == null)
			{
				// no uri, no file, there is nothing we can do more
				return;
			}
			File file = new File(uri);
			if (file.exists() && (file.canWrite() == readOnly))
			{
				// a read only file should have the canWrite on false
				// try to alter the RO status on the disk and ask Eclipse to refresh the resource
				file.setWritable(!readOnly);
				resource.refreshLocal(IResource.DEPTH_ZERO, null);
			}
		}
	}
}
