/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidg3464<br/>
 * Jul 17, 2014 6:08:21 PM
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
 *         %date_created: Fri Jul 18 09:09:09 2014 %
 * 
 *         %version: 1 %
 */
public interface IAsyncWorker
{

	/**
	 * The work that has to be performed after the asyncJob has been finished
	 */
	public void performWork();

	/**
	 * @return
	 */
	public Object getResult();

}
