/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidg3464<br/>
 * Jul 17, 2014 6:16:00 PM
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
public class SimpleWorkExecManagerImpl extends AbstractAsyncWorkExecManager<IAsyncWorker, IAsyncWorker> implements
	ISimpleWorkExecManager
{

	/**
	 * @param jobDelay
	 */
	protected SimpleWorkExecManagerImpl()
	{
		this(100);
	}

	/**
	 * @param jobDelay
	 */
	protected SimpleWorkExecManagerImpl(long jobDelay)
	{
		super(jobDelay);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.core.platform.concurrent.AbstractAsyncWorkExecManager#createJob()
	 */
	@Override
	protected AbstractAsyncJob<IAsyncWorker, IAsyncWorker> createJob()
	{
		return new SimpleAsyncJob("Async job", "Async task", this); //$NON-NLS-1$//$NON-NLS-2$
	}
}
