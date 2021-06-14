/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Nov 19, 2009 4:37:23 PM </copyright>
 */
package eu.cessar.ct.sdk.runtime;

import org.eclipse.core.runtime.IStatus;

/**
 * Instances of this class can be used to track execution progress. An instance
 * of this class or, better, {@link CessarTaskAdapter} shall be used with
 * {@link ICessarTaskManager#addListener(ICessarTaskListener)}.
 * 
 */
public interface ICessarTaskListener<T>
{

	/**
	 * This is the first method to be called during execution of a Cessar Task
	 * 
	 * @param manager
	 *        the manager
	 * @param noOfTasks
	 *        the number of task that have to be executed by the manager
	 */
	public void executionStarted(ICessarTaskManager<T> manager, int noOfTasks);

	/**
	 * This is execute right before a cessar task is about to start
	 * 
	 * @param manager
	 *        the manager
	 * @param taskName
	 *        a descriptive name of the task
	 */
	public void taskStarted(ICessarTaskManager<T> manager, T task);

	/**
	 * Executed right after the task ended
	 * 
	 * @param manager
	 *        the manager
	 * @param taskName
	 *        a descriptive name of the task
	 * @param status
	 *        the result status of the task
	 * @param result
	 *        the result of the task, can be null
	 */
	public void taskEnded(ICessarTaskManager<T> manager, T task, IStatus status, Object result);

	/**
	 * Last method executed after the last task
	 * 
	 * @param manager
	 *        the manager
	 * @param status
	 *        a status that is a merge of all task statuses
	 */
	public void executionEnded(ICessarTaskManager<T> manager, IStatus status);
}
