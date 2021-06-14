/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidu0944 Aug 21, 2012 11:53:18 AM </copyright>
 */
package eu.cessar.ct.workspace.ui.logging.hyperlink;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;

import eu.cessar.ct.workspace.ui.internal.CessarPluginActivator;

/**
 * @author uidu0944
 * 
 */
public class GenericHyperLink
{

	/**
	 * @param fileName
	 * @return project related to fileName or null if no such project.
	 */
	protected IJavaProject lookupProject(String fileName)
	{
		IJavaProject javaProject = null;
		IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
		if (projects != null && projects.length > 0)
		{
			for (int i = 0; i < projects.length; i++)
			{
				if (projects[i] != null && projects[i].isAccessible())
				{
					IResource res = getResource(gatherSourceFolders(projects[i], fileName), fileName);
					if (res != null)
					{
						javaProject = JavaCore.create(projects[i]);
					}
				}
			}
		}
		return javaProject;
	}

	/**
	 * Returns the eclipse resource by name by scanning all workspace projects
	 * 
	 * @param fileName
	 * @return resource related to fileName or null.
	 */
	protected IResource lookupResource(String fileName)
	{
		IResource res = null;
		IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
		if (projects != null)
		{
			for (int i = 0; i < projects.length; i++)
			{
				if (projects[i] != null && projects[i].isAccessible())
				{
					res = getResource(gatherSourceFolders(projects[i], fileName), fileName);
					if (res != null)
					{
						return res;
					}
				}
			}
		}
		return res;
	}

	/**
	 * Returns the eclipse resource in the specified folder list
	 * 
	 * @param folders
	 * @param fileName
	 * @return resource related to fileName or null if no suitable resource.
	 */
	@SuppressWarnings("static-method")
	protected IResource getResource(List<IFolder> folders, String fileName)
	{
		for (IFolder iFolder: folders)
		{
			IResource[] res;
			try
			{
				res = iFolder.members();
				if (res != null)
				{
					for (int i = 0; i < res.length; i++)
					{
						if (res[i].getFullPath().toPortableString().indexOf(fileName) > 0)
						{
							return res[i];
						}
					}
				}
			}
			catch (CoreException e)
			{
				CessarPluginActivator.getDefault().logError(e);
			}
		}
		return null;
	}

	/**
	 * Returns the list of folders in a project
	 * 
	 * @param res
	 * @param fileLocation
	 * @return list of folders
	 */
	protected List<IFolder> gatherSourceFolders(IResource res, String fileLocation)
	{
		List<IFolder> folders = new ArrayList<>();
		if (res instanceof IContainer)
		{
			IContainer container = (IContainer) res;
			try
			{
				boolean foundSources = false;
				for (IResource child: container.members())
				{
					if (!(child instanceof IFile))
					{
						continue;
					}
					IFile file = (IFile) child;
					if (file.toString().indexOf(fileLocation) > 0)
					{
						foundSources = true;
					}
				}
				if (foundSources && container instanceof IFolder)
				{
					folders.add((IFolder) container);
				}
				for (IResource child: container.members())
				{
					if (!(child instanceof IContainer))
					{
						continue;
					}
					folders.addAll(gatherSourceFolders(child, fileLocation));
				}
			}
			catch (CoreException e)
			{
				CessarPluginActivator.getDefault().logError(e);
			}
		}
		return folders;
	}

}
