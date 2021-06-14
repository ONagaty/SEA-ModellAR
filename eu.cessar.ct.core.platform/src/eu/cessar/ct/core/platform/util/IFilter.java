/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458<br/>
 * 18.12.2012 11:26:56
 * 
 * </copyright>
 */
package eu.cessar.ct.core.platform.util;

/**
 * Generic filtering interface. This interface does not define what "filtering" means, the consumer shall specify this
 * 
 * @author uidl6458
 * 
 *         %created_by: uidl6458 %
 * 
 *         %date_created: Tue Dec 18 17:39:31 2012 %
 * 
 *         %version: 1 %
 */
public interface IFilter<E>
{

	/**
	 * Check if the element pass the filter
	 * 
	 * @param element
	 * @return true if the element pass the filter, false otherwise.
	 */
	public boolean isPassingFilter(E element);

}
