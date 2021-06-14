/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Jan 28, 2010 6:58:50 PM </copyright>
 */
package eu.cessar.ct.runtime.execution;

import org.eclipse.core.resources.IProject;

import eu.cessar.ct.sdk.runtime.ICessarTaskManager;

/**
 * @author uidl6458
 * 
 */
public interface IExecutionSupport
{

	/**
	 * @param manager
	 * @return
	 */
	public IExecutionLoader acquireExecutionLoader(ICessarTaskManager<?> manager);

	/**
	 * @param manager
	 * @return
	 */
	public void releaseExecutionLoader(ICessarTaskManager<?> manager);

	/**
	 * @param project
	 * @return
	 */
	public IExecutionLoader getActiveExecutionLoader(IProject project);

	/**
	 * @param project
	 * @return
	 */
	public ICessarTaskManager<?> getCurrentManager(IProject project);

	/**
	 * @param listener
	 */
	public void addListener(IExecutionSupportListener listener);

	/**
	 * @param listener
	 */
	public void removeListener(IExecutionSupportListener listener);

}
