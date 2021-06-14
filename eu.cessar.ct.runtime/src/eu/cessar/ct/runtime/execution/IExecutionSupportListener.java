/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Feb 3, 2010 4:38:06 PM </copyright>
 */
package eu.cessar.ct.runtime.execution;

import org.eclipse.core.resources.IProject;

import eu.cessar.ct.sdk.runtime.ICessarTaskManager;

/**
 * @author uidl6458
 * 
 */
public interface IExecutionSupportListener
{

	/**
	 * Notify when a class loader chain is acquired for execution
	 * 
	 * @param project
	 *        the project
	 * @param manager
	 *        the manager that request the chain
	 * @param executionLoader
	 *        the class loader chain that will be used by the manager for
	 *        execution
	 */
	public void executionSupportAquired(IProject project, ICessarTaskManager<?> manager,
		IExecutionLoader executionLoader);

	/**
	 * Notify that a classLoader chain has been released for a particular
	 * project
	 * 
	 * @param project
	 *        the project where the class loader chain existed
	 * @param manager
	 * 		the manager that made the release
	 * @param executionLoader
	 *        the class loader chain after the release, could be null if is a
	 *        release from the primordial task manager
	 */
	public void executionSupportReleased(IProject project, ICessarTaskManager<?> manager,
		IExecutionLoader executionLoader);
}
