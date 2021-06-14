/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458<br/>
 * 02.09.2013 17:35:03
 * 
 * </copyright>
 */
package eu.cessar.ct.core.platform.util;


/**
 * An interface capable to provider a simple model changing services.
 * 
 * @author uidl6458
 * 
 *         %created_by: uidl6458 %
 * 
 *         %date_created: Fri Sep  6 16:58:21 2013 %
 * 
 *         %version: 1 %
 */
public interface IModelChangeStampProvider
{

	/**
	 * Return the current change stamp. It is guaranteed that every change into the model will trigger a new change
	 * stamp. The change stamp will not be neccesary greater then the previous one neither the difference from a
	 * previous change stamp reflect the number of changes that occurred in the model. Is just an indication that
	 * something happen.
	 * 
	 * @return the change stamp value
	 */
	public long getCurrentChangeStamp();
}
