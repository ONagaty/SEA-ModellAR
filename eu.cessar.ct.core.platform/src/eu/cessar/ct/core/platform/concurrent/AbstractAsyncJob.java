/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458<br/>
 * 15 iul. 2014 13:45:35
 * 
 * </copyright>
 */
package eu.cessar.ct.core.platform.concurrent;

import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.SafeRunner;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

import eu.cessar.ct.core.platform.util.SafeRunnable;

/**
 * 
 * @author uidl6458
 * 
 *         %created_by: uidg3464 %
 * 
 *         %date_created: Wed Jul 16 15:13:43 2014 %
 * 
 *         %version: 2 %
 * @param <T>
 *        - the target of the job
 * @param <V>
 *        - the callback that waits for the result of the job
 */
public abstract class AbstractAsyncJob<T, V> extends Job
{

	private final AbstractAsyncWorkExecManager<T, V> asyncManager;
	private String taskName;

	/**
	 * @param name
	 *        - the name of the job
	 * @param taskName
	 *        - the name of it's task
	 * @param manager
	 *        - the manager that checks this job
	 */
	public AbstractAsyncJob(String name, String taskName, AbstractAsyncWorkExecManager<T, V> manager)
	{
		super(name);
		this.taskName = taskName;
		this.asyncManager = manager;
	}

	/**
	 * @return manager - the manager that checks this job
	 */
	protected AbstractAsyncWorkExecManager<T, V> getManager()
	{
		return asyncManager;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.jobs.Job#run(org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	protected IStatus run(IProgressMonitor monitor)
	{
		T target = getManager().getNextValidationTarget();
		monitor.beginTask("", IProgressMonitor.UNKNOWN); //$NON-NLS-1$
		monitor.setTaskName(taskName);
		try
		{
			while (target != null)
			{
				// System.err.println("Performing validation of " + target);
				V diagnostic = performWork(target);
				if (monitor.isCanceled())
				{
					return Status.CANCEL_STATUS;
				}
				List<ICallback<V>> callbacks = getManager().peekCallbacks(target);
				notifyResult(callbacks, diagnostic);
				if (monitor.isCanceled())
				{
					return Status.CANCEL_STATUS;
				}
				target = getManager().getNextValidationTarget();
			}
		}
		catch (InterruptedException e)
		{
			return Status.CANCEL_STATUS;
		}
		finally
		{
			monitor.done();
		}
		return Status.OK_STATUS;
	}

	/**
	 * @param target
	 *        - the object on which the job does it work
	 * @return the result of the job
	 * @throws InterruptedException
	 */
	protected abstract V performWork(T target) throws InterruptedException;

	/**
	 * @param callBackList
	 *        - the list of callBacks that are interested of the job
	 * @param diagnostic
	 *        - the result of the job
	 */
	protected void notifyResult(List<ICallback<V>> callBackList, final V diagnostic)
	{
		for (final ICallback<V> callback: callBackList)
		{
			SafeRunner.run(new SafeRunnable()
			{

				public void run() throws Exception
				{
					callback.workDone(diagnostic);
				}
			});
		}
	}
}
