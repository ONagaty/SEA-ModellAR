/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870 Sep 24, 2009 9:52:41 AM </copyright>
 */
package eu.cessar.ct.core.platform.nature;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IProjectNature;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

import eu.cessar.ct.core.internal.platform.CessarPluginActivator;
import eu.cessar.ct.core.platform.PlatformConstants;

/**
 * Nature that indicates that a project is a Cessar project
 * 
 * @author uidl6870
 * 
 * @Review uidl6458 - 12.04.2012
 */
public class CessarNature implements IProjectNature
{
	protected IProject project;

	/**
	 * Set Cessar nature to given project
	 * 
	 * @param project
	 * @param monitor
	 * @throws CoreException
	 */
	public static void setNature(IProject project, IProgressMonitor monitor) throws CoreException
	{
		if (!project.hasNature(PlatformConstants.CESSAR_NATURE))
		{
			IProjectDescription description = project.getDescription();
			String[] natureIds = description.getNatureIds();
			String[] newNatureIds = new String[natureIds.length + 1];
			System.arraycopy(natureIds, 0, newNatureIds, 0, natureIds.length);
			newNatureIds[natureIds.length] = PlatformConstants.CESSAR_NATURE;
			description.setNatureIds(newNatureIds);
			project.setDescription(description, monitor);
		}
	}

	/**
	 * Check if a project have Cessar nature
	 * 
	 * @param project
	 *        a project
	 * @return <code>true</code> if the project have Cessar nature, false
	 *         otherwise
	 */
	public static boolean haveNature(IProject project)
	{
		try
		{
			return project.hasNature(PlatformConstants.CESSAR_NATURE);
		}
		catch (CoreException e)
		{
			CessarPluginActivator.getDefault().logError(e);
			return false;
		}
	}

	/** {@inheritDoc} */
	public void setProject(final IProject project)
	{
		this.project = project;
	}

	/** {@inheritDoc} */
	public IProject getProject()
	{
		return project;
	}

	/**
	 * @see org.eclipse.core.resources.IProjectNature#configure()
	 */
	public void configure() throws CoreException
	{
		// nothing to configure
	}

	/**
	 * @see org.eclipse.core.resources.IProjectNature#deconfigure()
	 */
	public void deconfigure() throws CoreException
	{
		// nothing to deconfigure
	}

}
