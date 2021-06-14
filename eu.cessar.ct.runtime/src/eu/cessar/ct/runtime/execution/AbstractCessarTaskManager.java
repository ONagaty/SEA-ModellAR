/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidl6458 Jan 29, 2010 4:05:07 PM </copyright>
 */
package eu.cessar.ct.runtime.execution;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.common.EventManager;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.SafeRunner;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.transaction.TransactionalEditingDomain;

import eu.cessar.ct.core.mms.CessarTransactionUtils;
import eu.cessar.ct.core.mms.MetaModelUtils;
import eu.cessar.ct.core.platform.util.SafeRunnable;
import eu.cessar.ct.runtime.internal.execution.ExecutionServiceImpl;
import eu.cessar.ct.sdk.logging.ILogger;
import eu.cessar.ct.sdk.logging.LoggerFactory;
import eu.cessar.ct.sdk.runtime.ICessarTaskListener;
import eu.cessar.ct.sdk.runtime.ICessarTaskManager;

/**
 * @author uidl6458
 *
 */
public abstract class AbstractCessarTaskManager<T> extends EventManager implements ICessarTaskManagerImpl<T>
{
	private final ICessarTaskDescriptor descriptor;
	private final IProject project;
	private IStatus executionStatus;

	private IExecutionLoader executionLoader;

	private List<T> input = new ArrayList<T>();
	private Map<T, Object> executionResult = new HashMap<T, Object>();
	private TransactionalEditingDomain editingDomain;
	private ICessarTaskManager<?> upperManager;

	private Map<String, Object> cache = new HashMap<String, Object>();

	public AbstractCessarTaskManager(ICessarTaskDescriptor descriptor, IProject project)
	{
		this.descriptor = descriptor;
		this.project = project;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.runtime.execution.ICessarTaskManagerImpl#getCachedData(java.lang.String)
	 */
	public Object getCachedData(String key)
	{
		Object result = cache.get(key);
		if (result instanceof SoftReference<?>)
		{
			Object unwrapped = ((SoftReference<T>) result).get();
			if (unwrapped == null)
			{
				cache.remove(key);
			}
			return unwrapped;
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.runtime.execution.ICessarTaskManagerImpl#removeCachedData(java.lang.String)
	 */
	public void removeCachedData(String key)
	{
		cache.remove(key);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.runtime.execution.ICessarTaskManagerImpl#setCachedData(java.lang.String, java.lang.Object,
	 * boolean)
	 */
	public void setCachedData(String key, Object data, boolean useSoftReference)
	{
		Object newData = data;
		if (useSoftReference)
		{
			newData = new SoftReference<Object>(data);
		}
		cache.put(key, newData);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.runtime.execution.ICessarTaskManagerImpl#setUpperManager(eu.cessar.ct.runtime.execution.
	 * ICessarTaskManagerImpl)
	 */
	public void setUpperManager(ICessarTaskManagerImpl<?> upperManager)
	{
		this.upperManager = upperManager;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.sdk.runtime.ICessarTaskManager#canWrite()
	 */
	public final boolean canWrite()
	{
		return canThisManagerWrite() && (upperManager == null || upperManager.canWrite());
	}

	protected abstract boolean canThisManagerWrite();

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.sdk.runtime.ICessarTaskManager#getProject()
	 */
	public IProject getProject()
	{
		return project;
	}

	/**
	 * @return
	 */
	public ICessarTaskDescriptor getDescriptor()
	{
		return descriptor;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.sdk.runtime.ICessarTaskManager#getCessarTaskID()
	 */
	public String getCessarTaskID()
	{
		return descriptor.getID();
	}

	/**
	 * @return
	 */
	protected List<T> getInput()
	{
		return input;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.sdk.runtime.ICessarTaskManager#addListener(eu.cessar.ct.sdk.runtime.ICessarTaskListener)
	 */
	public void addListener(ICessarTaskListener<T> listener)
	{
		addListenerObject(listener);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.sdk.runtime.ICessarTaskManager#removeListener(eu.cessar.ct.sdk.runtime.ICessarTaskListener)
	 */
	public void removeListener(ICessarTaskListener<T> listener)
	{
		removeListenerObject(listener);
	}

	/**
	 * Call {@link ICessarTaskListener#executionEnded(ICessarTaskManager, IStatus)} method of all attached listeners
	 *
	 * @param manager
	 * @param status
	 */
	@SuppressWarnings("unchecked")
	protected void notifyExecutionEnded()
	{
		ExecutionServiceImpl.eINSTANCE.notifyGlobalExecutionEnded(this, executionStatus);
		Object[] listeners = getListeners();
		for (final Object object: listeners)
		{
			SafeRunner.run(new SafeRunnable()
			{

				public void run() throws Exception
				{
					((ICessarTaskListener<T>) object).executionEnded(AbstractCessarTaskManager.this, executionStatus);
				}
			});
		}
	}

	/**
	 * Call the {@link ICessarTaskListener#executionStarted(ICessarTaskManager, int)} method of all attached listeners
	 *
	 * @param manager
	 * @param noOfTasks
	 */
	@SuppressWarnings("unchecked")
	protected void notifyExecutionStarted(final int noOfTasks)
	{
		ExecutionServiceImpl.eINSTANCE.notifyGlobalExecutionStarted(this, noOfTasks);
		Object[] listeners = getListeners();

		for (final Object object: listeners)
		{
			SafeRunner.run(new SafeRunnable()
			{

				public void run() throws Exception
				{
					((ICessarTaskListener<T>) object).executionStarted(AbstractCessarTaskManager.this, noOfTasks);
				}
			});
		}
	}

	/**
	 * Call {@link ICessarTaskListener#taskEnded(ICessarTaskManager, String, IStatus, Object)} method of all attached
	 * listeners
	 *
	 * @param manager
	 * @param taskName
	 * @param status
	 * @param result
	 */
	@SuppressWarnings("unchecked")
	protected void notifyTaskEnded(final T task, final IStatus status, final Object result)
	{
		ExecutionServiceImpl.eINSTANCE.notifyGlobalTaskEnded(this, task, status, result);

		Object[] listeners = getListeners();
		{
			for (final Object object: listeners)
			{
				SafeRunner.run(new SafeRunnable()
				{

					public void run() throws Exception
					{
						((ICessarTaskListener<T>) object).taskEnded(AbstractCessarTaskManager.this, task, status,
							result);
					}
				});
			}
		}
	}

	/**
	 * Call {@link ICessarTaskListener#taskStarted(ICessarTaskManager, String)} method of all attached listeners
	 *
	 * @param manager
	 * @param taskName
	 */
	@SuppressWarnings("unchecked")
	protected void notifyTaskStarted(final T task)
	{
		ExecutionServiceImpl.eINSTANCE.notifyGlobalTaskStarted(this, task);

		Object[] listeners = getListeners();
		for (final Object object: listeners)
		{
			SafeRunner.run(new SafeRunnable()
			{

				public void run() throws Exception
				{
					((ICessarTaskListener<T>) object).taskStarted(AbstractCessarTaskManager.this, task);
				}
			});
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.sdk.runtime.ICessarTaskManager#getExecutionResult()
	 */
	public Map<T, Object> getExecutionResult()
	{
		// TODO Auto-generated method stub
		return executionResult;
	}

	/**
	 * @param result
	 */
	protected synchronized void combineExecutionStatus(IStatus status)
	{
		if (executionStatus == Status.OK_STATUS)
		{
			// first call
			executionStatus = status;
		}
		else
		{
			MultiStatus result;
			if (executionStatus instanceof MultiStatus)
			{
				result = (MultiStatus) executionStatus;
			}
			else
			{
				result = new MultiStatus(executionStatus.getPlugin(), executionStatus.getCode(),
					executionStatus.getMessage(), executionStatus.getException());
			}
			result.add(status);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.sdk.runtime.ICessarTaskManager#getExecutionStatus()
	 */
	public IStatus getExecutionStatus()
	{
		// TODO Auto-generated method stub
		return executionStatus;
	}

	/**
	 * @param status
	 */
	protected void setExecutionStatus(IStatus status)
	{
		executionStatus = status;
	}

	/**
	 * @param executionLoader
	 */
	protected void setExecutionLoader(IExecutionLoader executionLoader)
	{
		this.executionLoader = executionLoader;
	}

	/**
	 * @return
	 */
	protected IExecutionLoader getExecutionLoader()
	{
		return executionLoader;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.sdk.runtime.ICessarTaskManager#initialize(java.lang.Object)
	 */
	public void initialize(T inputObject)
	{
		doResetInput();
		input.add(inputObject);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.sdk.runtime.ICessarTaskManager#initialize(java.util.List)
	 */
	public void initialize(List<T> inputList)
	{
		doResetInput();
		input.addAll(inputList);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.sdk.runtime.ICessarTaskManager#reset(boolean)
	 */
	public final void reset(boolean removeListeners)
	{
		if (removeListeners)
		{
			clearListeners();
		}
		doResetInput();
	}

	/**
	 * Called right after everything has been executed, with of without success. The default implementation does
	 * nothing.
	 */
	protected void doPostExecute()
	{
		cache.clear();
	}

	/**
	 * Reset the input
	 */
	protected void doResetInput()
	{
		input.clear();
		executionStatus = Status.OK_STATUS;
		executionResult.clear();
		cache.clear();
	}

	/**
	 * @return
	 */
	protected TransactionalEditingDomain getEditingDomain()
	{
		if (editingDomain == null)
		{
			editingDomain = MetaModelUtils.getEditingDomain(project);
		}
		return editingDomain;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.sdk.runtime.ICessarTaskManager#updateModel(java.lang.Runnable)
	 */
	public void updateModel(Runnable runnable) throws ExecutionException
	{
		if (!this.canWrite())
		{
			throw new ExecutionException("Model is read only"); //$NON-NLS-1$
		}
		else
		{
			IStatus executeInWriteTransactionStatus = CessarTransactionUtils.executeInWriteTransaction(
				getEditingDomain(), runnable, "Updating PM model"); //$NON-NLS-1$
			if (!executeInWriteTransactionStatus.isOK())
			{
				String message = executeInWriteTransactionStatus.getMessage();
				Throwable exception = executeInWriteTransactionStatus.getException();
				String executionMessage;
				if (message != null)
				{
					executionMessage = message;
				}
				else
				{
					executionMessage = "The writing transaction has failed"; //$NON-NLS-1$
				}

				throw new ExecutionException(executionMessage, exception);

			}
		}
	}

	/**
	 * Return the logger to be used during task execution
	 *
	 * @return
	 */
	protected ILogger getLogger()
	{
		return LoggerFactory.getLogger();
	}
}
