/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidg3464<br/>
 * Jul 17, 2014 6:18:19 PM
 * 
 * </copyright>
 */
package eu.cessar.ct.core.platform.concurrent;

/**
 * TODO: Please comment this class
 * 
 * @author uidg3464
 * 
 *         %created_by: uidg3464 %
 * 
 *         %date_created: Fri Jul 18 09:09:11 2014 %
 * 
 *         %version: 1 %
 */
public class SimpleAsyncJob extends AbstractAsyncJob<IAsyncWorker, IAsyncWorker>
{

	/**
	 * @param name
	 * @param taskName
	 * @param manager
	 */
	public SimpleAsyncJob(String name, String taskName, AbstractAsyncWorkExecManager<IAsyncWorker, IAsyncWorker> manager)
	{
		super(name, taskName, manager);
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.core.platform.concurrent.AbstractAsyncJob#performWork(java.lang.Object)
	 */
	@Override
	protected IAsyncWorker performWork(IAsyncWorker target) throws InterruptedException
	{
		target.performWork();
		return target;
	}

}
