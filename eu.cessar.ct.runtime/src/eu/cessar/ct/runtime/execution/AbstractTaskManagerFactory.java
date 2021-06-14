/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Nov 19, 2009 5:53:27 PM </copyright>
 */
package eu.cessar.ct.runtime.execution;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;

import eu.cessar.ct.core.platform.PlatformConstants;
import eu.cessar.ct.runtime.internal.CessarPluginActivator;

/**
 * Basic implementation class for {@link ICessarTaskManagerFactory}.
 * 
 */
public abstract class AbstractTaskManagerFactory<T> implements ICessarTaskManagerFactory<T>
{

	private ICessarTaskDescriptor descriptor;

	/* (non-Javadoc)
	 * @see eu.cessar.ct.runtime.execution.ICessarTaskManagerFactory#initialize(eu.cessar.ct.runtime.execution.ICessarTaskDescriptor)
	 */
	public void initialize(ICessarTaskDescriptor descriptor)
	{
		this.descriptor = descriptor;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.runtime.execution.ICessarTaskManagerFactory#getDescriptor()
	 */
	public ICessarTaskDescriptor getDescriptor()
	{
		return descriptor;
	}

	/**
	 * This implementation will return true if the project have Cessar nature.
	 * If more conditions are needed implementors should extend and call super
	 * 
	 * @see eu.cessar.ct.runtime.execution.ICessarTaskManagerFactory#isValidProject(org.eclipse.core.resources.IProject)
	 */
	public boolean isValidProject(IProject project)
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

}
