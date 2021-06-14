/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870 Jun 23, 2010 10:46:44 AM </copyright>
 */
package eu.cessar.ct.ant.tasks;

import java.io.File;
import java.net.URI;

import org.apache.tools.ant.BuildException;
import org.eclipse.core.filesystem.URIUtil;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.osgi.util.NLS;

/**
 * Ant Task for obtaining
 * <ul>
 * <li>the workspace relative path of a resource given by it's absolute path</li>
 * <li>the absolute path of a resource given by it's workspace relative path.</li>
 * </ul>
 * <br/>
 * NOTE: If multiple workspace relative resources are found for a given
 * <code>filesystempath</code> and <code>projectname</code> is not set, a
 * {@link BuildException} is thrown indicating that in this case,
 * <code>projectname</code> argument is mandatory
 * 
 */
public abstract class AbstractConvertPath extends AbstractTask
{
	private String filesystempath;

	private String resourcepath;

	private String property;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.tools.ant.Task#execute()
	 */
	@Override
	public void execute() throws BuildException
	{
		checkArgs();
		processArgs();

		if (filesystempath != null)
		{
			convertFileSystemPath();
		}
		else
		{
			convertResourcePath();
		}
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.ant.tasks.AbstractTask#checkArgs()
	 */
	@Override
	protected void checkArgs()
	{
		super.checkArgs();

		if (filesystempath == null && resourcepath == null)
		{
			throw new BuildException("filesystempath or resourcepath must be set"); //$NON-NLS-1$
		}
		if (filesystempath != null && resourcepath != null)
		{
			throw new BuildException("filesystempath and resourcepath cannot be both set"); //$NON-NLS-1$
		}
		if (property == null)
		{
			throw new BuildException("property must be set"); //$NON-NLS-1$
		}
	}

	/**
	 * 
	 */
	private void convertResourcePath()
	{
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();

		IPath path = new Path(resourcepath);
		IFolder folder = root.getFolder(path);
		IPath location = folder.getLocation();
		if (location == null)
		{
			throw new BuildException(NLS.bind("Cannot get location for {0}", resourcepath)); //$NON-NLS-1$
		}
		else
		{
			getProject().setUserProperty(property, location.toPortableString());
		}
	}

	/**
	 * 
	 */
	private void convertFileSystemPath()
	{
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();

		IPath path = new Path(filesystempath).makeAbsolute();
		URI uri = URIUtil.toURI(path);
		File file = path.toFile();

		if (file.exists())
		{
			IResource[] resources;

			boolean isDir = false;
			if (file.isDirectory())
			{
				resources = root.findContainersForLocationURI(uri);
				isDir = true;
			}
			else
			{
				resources = root.findFilesForLocationURI(uri);
			}

			doConvertFileSystemPath(resources);

		}
	}

	/**
	 * 
	 * @param resources
	 */
	protected abstract void doConvertFileSystemPath(IResource[] resources);

	public void setFilesystempath(String path)
	{

		this.filesystempath = path;
	}

	public String getFilesystempath()
	{

		return filesystempath;
	}

	public void setResourcepath(String path)
	{

		this.resourcepath = path;
	}

	public String getResourcepath()
	{

		return resourcepath;
	}

	public void setProperty(String name)
	{

		this.property = name;
	}

	public String getProperty()
	{

		return property;
	}

}
