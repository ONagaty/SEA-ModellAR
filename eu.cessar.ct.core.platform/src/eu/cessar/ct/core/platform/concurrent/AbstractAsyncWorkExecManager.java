/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458<br/>
 * 15 iul. 2014 13:42:05
 * 
 * </copyright>
 */
package eu.cessar.ct.core.platform.concurrent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author uidl6458
 * 
 *         %created_by: uidg3464 %
 * 
 *         %date_created: Wed Jul 16 15:13:44 2014 %
 * 
 *         %version: 2 %
 * @param <T>
 *        - the type of the target of the job
 * @param <V>
 *        - the type of the callBack that has to be notified when the job has finished
 */
public abstract class AbstractAsyncWorkExecManager<T, V> implements IAsyncWorkExecManager<T, V>
{
	/** mapping between the target of the job and the interested listeners */
	protected Map<T, List<ICallback<V>>> eObjectToCallbackMapping = new IdentityHashMap<>();

	/** mapping between the interested listeners and the target of the job */
	protected Map<ICallback<V>, List<T>> callbackToEObjectMapping = new IdentityHashMap<>();

	private AbstractAsyncJob<T, V> job;

	private long jobDelay;

	/**
	 * @param jobDelay
	 *        - the delay that we wan't for the current job
	 */
	protected AbstractAsyncWorkExecManager(long jobDelay)
	{
		this.jobDelay = jobDelay;
	}

	/**
	 * @return - the job that will be executed
	 */
	protected synchronized AbstractAsyncJob<T, V> getJob()
	{
		if (job == null)
		{
			job = createJob();
		}
		return job;
	}

	/**
	 * @return a new AbstractAsyncJob
	 */
	protected abstract AbstractAsyncJob<T, V> createJob();

	/**
	 * Schedule's the current job with the delay that we have set
	 */
	protected void scheduleJob()
	{
		getJob().schedule(jobDelay);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.validation.IAsyncWorkExecManager#performWork(java.lang.Object,
	 * eu.cessar.ct.validation.ICallback)
	 */
	public synchronized void performWork(final T target, final ICallback<V> callBack)
	{
		executeJob(target, callBack);
	}

	/**
	 * Ask the manager to execute the given job on the <code>target</code>, on the next reasonable opportunity,
	 * registering with given <code>callBack</code> in order to receive feedback when the job has finished
	 * 
	 * @param target
	 * @param callBack
	 */
	protected synchronized void executeJob(T target, ICallback<V> callBack)
	{

		if (!eObjectToCallbackMapping.containsKey(target))
		{
			eObjectToCallbackMapping.put(target, new ArrayList<ICallback<V>>());
		}
		eObjectToCallbackMapping.get(target).add(callBack);

		if (!callbackToEObjectMapping.containsKey(callBack))
		{
			callbackToEObjectMapping.put(callBack, new ArrayList<T>());
		}
		callbackToEObjectMapping.get(callBack).add(target);

		scheduleJob();
	}

	/**
	 * @param callBack
	 *        - the callBack that has to be informed when the job has finished
	 */
	public synchronized void forgetWork(ICallback<V> callBack)
	{
		List<T> list = callbackToEObjectMapping.remove(callBack);
		if (list != null)
		{
			for (T target: list)
			{
				List<ICallback<V>> callBackList = eObjectToCallbackMapping.get(target);
				if (callBackList != null)
				{
					callBackList.remove(callBack);
					if (callBackList.isEmpty())
					{
						eObjectToCallbackMapping.remove(target);
					}
				}
			}
		}
		if (eObjectToCallbackMapping.isEmpty())
		{
			cancelJob();
		}
	}

	private void cancelJob()
	{
		getJob().cancel();
	}

	/**
	 * Return the next target that will be used for the job or null if there is no such target available.
	 * 
	 * @return the next target of the job, from map or null if there is none
	 */
	public synchronized T getNextValidationTarget()
	{
		if (eObjectToCallbackMapping.isEmpty())
		{
			return null;
		}
		else
		{
			return eObjectToCallbackMapping.keySet().iterator().next();
		}
	}

	/**
	 * Return the callBacks associated with the target and remove them from the internal map. If there is no such target
	 * anymore in the internal map, an empty list will be returned.
	 * 
	 * @param target
	 *        - the target of the job
	 * @return the callback list for the provided target or an empty list if there is none
	 */
	public synchronized List<ICallback<V>> peekCallbacks(T target)
	{
		List<ICallback<V>> list = eObjectToCallbackMapping.remove(target);
		if (list != null)
		{
			for (ICallback<V> callback: list)
			{
				List<T> eObjList = callbackToEObjectMapping.get(callback);
				if (eObjList != null)
				{
					eObjList.remove(target);
					if (eObjList.isEmpty())
					{
						callbackToEObjectMapping.remove(callback);
					}
				}
			}
		}
		else
		{
			list = Collections.emptyList();
		}
		return list;
	}

	/**
	 * @return the map between the target of the job and the interested listeners
	 */
	public synchronized Map<T, List<ICallback<V>>> peekCurrentRequests()
	{
		Map<T, List<ICallback<V>>> result = eObjectToCallbackMapping;
		eObjectToCallbackMapping = new HashMap<>();
		callbackToEObjectMapping.clear();
		return result;
	}

}
