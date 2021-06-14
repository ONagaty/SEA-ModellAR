/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidl6458 Jan 29, 2010 6:33:29 PM </copyright>
 */
package eu.cessar.ct.runtime.execution;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.core.resources.WorkspaceJob;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.jobs.Job;

import eu.cessar.ct.sdk.logging.ILogger;
import eu.cessar.ct.sdk.logging.LoggerFactory;

/**
 * @author uidl6458
 *
 */
public abstract class CessarTask<T> extends WorkspaceJob
{

	private static final Map<String, Object> CESSAR_TASK_OPTIONS = new ConcurrentHashMap<>();

	protected Object executionResult;
	protected T input;
	protected Object family;
	protected ClassLoader parentClassLoader;
	private IProgressMonitor monitor;
	private ICessarTaskManagerImpl<T> taskManager;
	private String taskName;

	private IStatus status;

	// /**
	// * Set a specific option to a value to a cessar task (pluget,code generator)
	// *
	// * Parallel tasks can have different values for the same option.
	// *
	// * @param key
	// * @param value
	// */
	// @Requirement(
	// reqID = "229717")
	// public void setTaskOption(String key, Object value)
	// {
	//		CESSAR_TASK_OPTIONS.put(taskName + "_" + key, value); //$NON-NLS-1$
	// }
	//
	// /**
	// * Get the value of the given option for the current cessar task.
	// *
	// * @param key
	// * @return option value
	// */
	// @Requirement(
	// reqID = "229717")
	// public Object getTaskOption(String key)
	// {
	//		return CESSAR_TASK_OPTIONS.get(taskName + "_" + key); //$NON-NLS-1$
	// }

	/**
	 * @param name
	 * @param monitor
	 */
	public CessarTask(ICessarTaskManagerImpl<T> manager, String name, T input, ClassLoader parentClassLoader)
	{

		super(name);
		this.taskName = name;
		taskManager = manager;
		this.input = input;
		this.parentClassLoader = parentClassLoader;

		// // default values for SPLIT_CHECKING and MERGED_REFERENCES
		// setTaskOption(ETaskOption.SPLIT_CHECKING.getOption(), true);
		// setTaskOption(ETaskOption.MERGED_REFERENCES.getOption(), false);
	}

	/**
	 * @param family
	 */
	public void setFamilyObject(Object family)
	{
		this.family = family;
	}

	/**
	 * @param monitor
	 */
	public void setMonitor(IProgressMonitor monitor)
	{
		this.monitor = monitor;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.core.resources.WorkspaceJob#runInWorkspace(org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	public final IStatus runInWorkspace(IProgressMonitor jobMonitor) throws CoreException
	{
		if (monitor == null)
		{
			return runTask(jobMonitor);
		}
		else
		{
			return runTask(monitor);
		}
	}

	/**
	 * @param monitor2
	 * @return
	 */
	protected abstract IStatus runTask(IProgressMonitor monitor) throws CoreException;

	/**
	 * @return
	 */
	public T getInput()
	{
		return input;
	}

	/**
	 * @return
	 */
	public Object getExecutionResult()
	{
		return executionResult;
	}

	/**
	 * @return
	 */
	public ClassLoader getParentClassLoader()
	{
		return parentClassLoader;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.core.runtime.jobs.Job#belongsTo(java.lang.Object)
	 */
	@Override
	public boolean belongsTo(Object family)
	{
		return this.family == family;
	}

	/**
	 * Return the logger that shall be used by the task
	 *
	 * @return
	 */
	public ILogger getLogger()
	{
		return LoggerFactory.getLogger();
	}

	/**
	 * Return the CessarTask which is currently executing by the calling thread. If the calling thread does not execute
	 * a task <code>null</code> will be returned. This method should be executed only within a task.
	 *
	 * @return
	 */
	public static CessarTask<?> getCurrentTask()
	{
		Job job = getJobManager().currentJob();
		if (job instanceof CessarTask<?>)
		{
			return (CessarTask<?>) job;
		}
		else
		{
			return null;
		}
	}

	/**
	 * Return the manager used to execute the task
	 *
	 * @return the taskManager
	 */
	public ICessarTaskManagerImpl<T> getTaskManager()
	{
		return taskManager;
	}

	/**
	 * Dispose all required information
	 */
	protected void dispose()
	{
		executionResult = null;
		input = null;
		family = null;
		parentClassLoader = null;
		monitor = null;
		taskManager = null;
	}

	/**
	 * @return the status
	 */
	public IStatus getJetStatus()
	{
		return status;
	}

	/**
	 * @param status
	 *        the status to set
	 */
	public void setJetStatus(IStatus status)
	{
		this.status = status;
	}
}
