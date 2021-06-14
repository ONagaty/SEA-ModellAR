/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidl6458 Jan 29, 2010 4:09:27 PM </copyright>
 */
package eu.cessar.ct.runtime.execution;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.IJobManager;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.sphinx.platform.util.StatusUtil;

import eu.cessar.ct.core.mms.splittable.SplitableUtils;
import eu.cessar.ct.runtime.CessarRuntime;
import eu.cessar.ct.runtime.CodegenPreferencesAccessor;
import eu.cessar.ct.runtime.internal.CessarPluginActivator;
import eu.cessar.ct.sdk.utils.PMUtils;

/**
 * @author uidl6458
 * @param <T>
 *
 */
public abstract class AbstractJobBasedTaskManager<T> extends AbstractCessarTaskManager<T>
{

	private Semaphore semaphore;

	private JobChangeAdapter listener = new JobChangeAdapter()
	{

		@Override
		public void aboutToRun(IJobChangeEvent event)
		{
			@SuppressWarnings("unchecked")
			CessarTask<T> task = (CessarTask<T>) event.getJob();
			processAboutToRunEvent(task);
		}

		@Override
		public void done(IJobChangeEvent event)
		{
			try
			{
				// reset the value of the current thread's thread-local variable related to how the references from the
				// PM
				// to the system side (foreign references and instance reference) are to be returned
				PMUtils.setUsingMergedReferences(false);

				@SuppressWarnings("unchecked")
				CessarTask<T> task = (CessarTask<T>) event.getJob();
				IStatus result = event.getResult();
				processDoneEvent(task, result);
			}
			finally
			{
				releaseSemaphore();
			}
		}

	};

	/**
	 * @param descriptor
	 * @param project
	 */
	public AbstractJobBasedTaskManager(ICessarTaskDescriptor descriptor, IProject project)
	{
		super(descriptor, project);
	}

	/**
	 * @param task
	 */
	protected synchronized void addExecutionResult(CessarTask<T> task)
	{
		Object result = task.getExecutionResult();
		T taskInput = task.getInput();
		getExecutionResult().put(taskInput, result);
	}

	public IStatus execute(boolean background, Object parameter, IProgressMonitor monitor)
	{
		if (monitor == null)
		{
			monitor = new NullProgressMonitor(); // SUPPRESS CHECKSTYLE init
		}
		monitor.beginTask("Executing tasks...", 10 + 1000 * getInput().size()); //$NON-NLS-1$
		try
		{
			notifyExecutionStarted(getInput().size());
			IExecutionLoader loader = CessarRuntime.getExecutionSupport().acquireExecutionLoader(this);

			setExecutionLoader(loader);

			monitor.worked(10);
			try
			{
				doPreExecute();
				List<CessarTask<T>> jobs = createJobs(parameter, background, monitor);

				for (CessarTask<T> cessarTask: jobs)
				{
					IStatus jetStatus = cessarTask.getJetStatus();
					if (null != jetStatus)
					{
						if (!Status.OK_STATUS.equals(jetStatus)
							&& jetStatus.getMessage().startsWith(
								"Multiple definitions for module with fully qualified name: ")) //$NON-NLS-1$
						{
							throw new Exception("Execution stopped due to: " + jetStatus.getMessage()); //$NON-NLS-1$
						}
					}
				}

				if (jobs.size() > 0)
				{
					if (isUsingJobScheduling())
					{
						scheduleJobs(jobs, monitor);
					}
					else
					{
						executeJobs(jobs, monitor);
					}
				}
			}
			catch (Throwable t) // SUPPRESS CHECKSTYLE see below
			// It's ok to catch all exception a code generation could throw, we
			// don't want to let unchecked exception to propagate in the rest of
			// the system
			{
				StringWriter sw = new StringWriter();
				t.printStackTrace(new PrintWriter(sw));
				getLogger().error(sw.toString());
				combineExecutionStatus(StatusUtil.createErrorStatus(CessarPluginActivator.getDefault(), t));
			}
			finally
			{
				notifyExecutionEnded();
				setExecutionLoader(null);
				CessarRuntime.getExecutionSupport().releaseExecutionLoader(this);
				doPostExecute();
			}
		}
		finally
		{
			monitor.done();
		}
		return getExecutionStatus();
	}

	/**
	 * Execute the jobs in the current thread.
	 *
	 * @param jobs
	 * @param monitor
	 */
	private void executeJobs(List<CessarTask<T>> jobs, IProgressMonitor monitor)
	{
		IJobManager jobManager = Job.getJobManager();
		int ticks = (getInput().size() * 1000) / jobs.size();
		for (CessarTask<T> task: jobs)
		{
			ISchedulingRule taskRule = task.getRule();
			try
			{
				jobManager.beginRule(taskRule, monitor);
			}
			catch (IllegalArgumentException ex)
			{ // invalid rule
				combineExecutionStatus(StatusUtil.createErrorStatus(CessarPluginActivator.getDefault(),
					"The current scheduling rule does not permit execution of task " + task.toString())); //$NON-NLS-1$
				break;
			}
			try
			{
				// the scheduling rule is prepared, run the task
				processAboutToRunEvent(task);
				IStatus result = null;
				try
				{
					result = task.runInWorkspace(new SubProgressMonitor(monitor, ticks));
				}
				catch (Throwable t) // SUPPRESS CHECKSTYLE see below
				// It's ok to catch all exception a code generation could throw,
				// we don't want to let unchecked exception to propagate in the
				// rest of the system
				{
					result = StatusUtil.createErrorStatus(CessarPluginActivator.getDefault(), t);
				}
				processDoneEvent(task, result);
			}
			finally
			{
				releaseSemaphore();
				jobManager.endRule(taskRule);
			}
		}
	}

	/**
	 * Execute all the <code>jobs</code> and wait for execution to finish. The execution is done by scheduling the tasks
	 * with the {@link IJobManager}.
	 *
	 * @param jobs
	 * @param monitor
	 */
	protected void scheduleJobs(List<CessarTask<T>> jobs, IProgressMonitor monitor)
	{
		semaphore = new Semaphore(0);
		int noOfJobs = jobs.size();
		for (CessarTask<T> task: jobs)
		{
			task.addJobChangeListener(listener);
			task.schedule();
		}
		// we have to wait for all tasks to finish execution
		try
		{
			for (int i = 0; i < noOfJobs; i++)
			{
				boolean aquired = true;
				while (!semaphore.tryAcquire(1, 1, TimeUnit.SECONDS))
				{
					if (monitor.isCanceled())
					{
						aquired = false;
						break;
					}
				}
				if (!aquired)
				{
					// canceled, set the status
					combineExecutionStatus(Status.CANCEL_STATUS);
					break;
				}
			}
		}
		catch (InterruptedException e)
		{
			monitor.setCanceled(true);
			combineExecutionStatus(Status.CANCEL_STATUS);
		}
	}

	/**
	 * @param task
	 */
	private void processAboutToRunEvent(CessarTask<T> task)
	{
		notifyTaskStarted(task.getInput());
		doPreExecuteTask(task);
	}

	/**
	 * @param task
	 * @param result
	 */
	private void processDoneEvent(CessarTask<T> task, IStatus result)
	{
		notifyTaskEnded(task.getInput(), result, task.getExecutionResult());
		addExecutionResult(task);
		combineExecutionStatus(result);
		doPostExecuteTask(task);
		// cleanup task data
		task.dispose();
	}

	/**
	 * Release semaphore (no matter what! :-)
	 */
	private void releaseSemaphore()
	{
		if (semaphore != null)
		{
			semaphore.release();
		}
	}

	/**
	 * Return true if the jobs should be scheduled to be executed by {@link IJobManager} or false if the jobs should be
	 * executed by the current thread in sequence.
	 *
	 * @return boolean
	 */
	private boolean isUsingJobScheduling()
	{
		return Job.getJobManager().currentJob() == null;
	}

	/**
	 * Create a <code>CessarTask</code> for each task that have to be executed. The tasks should be only created at this
	 * point but not scheduled for execution.
	 *
	 * @param parameter
	 *        A task specific parameter, might be null
	 * @param background
	 *        if true then the job is not presented to the user
	 * @param monitor
	 *        the monitor to use, it should not be null
	 * @return A list with all Cessar tasks, never null
	 */
	private List<CessarTask<T>> createJobs(Object parameter, boolean background, IProgressMonitor monitor)
	{
		List<CessarTask<T>> result = new ArrayList<>();
		for (T inputObject: getInput())
		{
			List<CessarTask<T>> tasks = createCessarTask(inputObject, parameter);
			if (tasks != null)
			{
				for (CessarTask<T> task: tasks)
				{
					task.setUser(!background);
					task.setPriority(Job.INTERACTIVE); // highest
					// priority
					task.setMonitor(new SubProgressMonitor(monitor, 1000 / tasks.size()));
					task.setFamilyObject(this);
				}
				result.addAll(tasks);
			}
		}
		return result;
	}

	/**
	 * Called right before executing anything.
	 */
	protected void doPreExecute() throws CoreException
	{
		getLogger().info("Preparing to execute " + getInput().size() + " task(s)."); //$NON-NLS-1$ //$NON-NLS-2$
		if (!canWrite())
		{
			SplitableUtils.INSTANCE.enterReadOnly();
		}

		// perform a project refresh if the option to reload arxml files is enabled
		if (CodegenPreferencesAccessor.isExternalChangesOption(getProject()))
		{
			try
			{
				getProject().refreshLocal(IResource.DEPTH_INFINITE, new NullProgressMonitor());
			}
			catch (CoreException e)
			{
				CessarPluginActivator.getDefault().logError(e);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.runtime.execution.AbstractCessarTaskManager#doPostExecute()
	 */
	@Override
	protected void doPostExecute()
	{
		super.doPostExecute();
		if (!canWrite())
		{
			SplitableUtils.INSTANCE.leaveReadOnly();
			// MarkerJob.INSTANCE.setEnable(true);
			// CessarProxyManager.INSTANCE.leaveReadOnly();
		}
	}

	/**
	 * Called right before a task start execution. The default implementation does nothing.
	 */
	protected void doPreExecuteTask(CessarTask<T> task)
	{
		initPMUtilsVariables();
	}

	/**
	 * Called right after a task has finish execution. The default implementation does nothing.
	 */
	protected void doPostExecuteTask(CessarTask<T> task)
	{
		initPMUtilsVariables();
	}

	/**
	 * @param input
	 * @param parameter
	 * @param inputClassLoader
	 * @param owningFamily
	 * @param subProgressMonitor
	 * @return List<CessarTask<T>>
	 */
	protected abstract List<CessarTask<T>> createCessarTask(T input, Object parameter);

	/**
	 * Initialize the PMUtils variables to their default values
	 */
	protected void initPMUtilsVariables()
	{
		PMUtils.setUsingMergedReferences(false);
		PMUtils.setSplitChecking(true);
	}

}
