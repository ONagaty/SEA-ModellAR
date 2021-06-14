/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Nov 19, 2009 6:03:23 PM </copyright>
 */
package eu.cessar.ct.sdk.runtime;

import org.eclipse.core.runtime.IStatus;

/**
 * Provide basic implementation for an {@link ICessarTaskListener}. This
 * implementation does nothing on all implemented methods.
 * 
 */
public class CessarTaskAdapter<T> implements ICessarTaskListener<T>
{

	/* (non-Javadoc)
	 * @see eu.cessar.ct.sdk.runtime.ICessarTaskListener#executionStarted(eu.cessar.ct.sdk.runtime.ICessarTaskManager, int)
	 */
	public void executionStarted(ICessarTaskManager<T> manager, int noOfTasks)
	{
		// do nothing
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.sdk.runtime.ICessarTaskListener#taskStarted(eu.cessar.ct.sdk.runtime.ICessarTaskManager, java.lang.String)
	 */
	public void taskStarted(ICessarTaskManager<T> manager, T task)
	{
		// do nothing
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.sdk.runtime.ICessarTaskListener#taskEnded(eu.cessar.ct.sdk.runtime.ICessarTaskManager, java.lang.String, org.eclipse.core.runtime.IStatus, java.lang.Object)
	 */
	public void taskEnded(ICessarTaskManager<T> manager, T task, IStatus status, Object result)
	{
		// do nothing
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.sdk.runtime.ICessarTaskListener#executionEnded(eu.cessar.ct.sdk.runtime.ICessarTaskManager, org.eclipse.core.runtime.IStatus)
	 */
	public void executionEnded(ICessarTaskManager<T> manager, IStatus status)
	{
		// do nothing
	}

}
