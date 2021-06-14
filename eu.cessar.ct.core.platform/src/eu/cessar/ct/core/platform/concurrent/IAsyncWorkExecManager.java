/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458<br/>
 * 15 iul. 2014 13:39:41
 * 
 * </copyright>
 */
package eu.cessar.ct.core.platform.concurrent;

/**
 * 
 * @author uidl6458
 * 
 *         %created_by: uidg3464 %
 * 
 *         %date_created: Fri Jul 18 09:09:14 2014 %
 * 
 *         %version: 3 %
 * @param <T>
 *        - the type of the target of the job
 * @param <V>
 *        - the type of the callBack that has to be notified when the job has finished
 */
public interface IAsyncWorkExecManager<T, V>
{

	/**
	 * Ask the manager to run the given job on the <code>target</code>, on the next reasonable opportunity, registering
	 * with given <code>callBack</code> in order to receive feedback when the job has finished
	 * 
	 * @param target
	 *        the target of the job
	 * @param callBack
	 *        the callBack that has to be notified when the job has finished
	 */
	public void performWork(T target, ICallback<V> callBack);

	/**
	 * Unregister a previously added callback
	 * 
	 * @param callBack
	 */
	public void forgetWork(ICallback<V> callBack);
}
