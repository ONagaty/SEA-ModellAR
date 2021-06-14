/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458<br/>
 * Oct 17, 2012 10:49:25 AM
 * 
 * </copyright>
 */
package eu.cessar.req;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Requirement annotation. It have only SOURCE retention and can be used on any program element.
 * 
 * @author uidl6458
 * 
 *         %created_by: uidl6458 %
 * 
 *         %date_created: Wed Oct 17 11:35:25 2012 %
 * 
 *         %version: 1 %
 */
@Retention(RetentionPolicy.SOURCE)
public @interface Requirement
{

	/**
	 * The ID of the requirement.
	 * 
	 * @return The ID of the requirement
	 */
	String reqID();
}
