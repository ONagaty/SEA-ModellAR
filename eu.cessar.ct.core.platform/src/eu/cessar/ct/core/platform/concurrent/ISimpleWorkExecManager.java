/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidg3464<br/>
 * Jul 17, 2014 6:14:03 PM
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
 *         %date_created: Fri Jul 18 09:09:10 2014 %
 * 
 *         %version: 1 %
 */
public interface ISimpleWorkExecManager extends IAsyncWorkExecManager<IAsyncWorker, IAsyncWorker>
{

	public static final ISimpleWorkExecManager INSTANCE = new SimpleWorkExecManagerImpl();
}
