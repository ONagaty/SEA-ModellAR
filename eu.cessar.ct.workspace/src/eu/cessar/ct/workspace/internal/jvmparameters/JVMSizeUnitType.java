/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidw8762<br/>
 * Apr 4, 2014 10:38:48 AM
 * 
 * </copyright>
 */
package eu.cessar.ct.workspace.internal.jvmparameters;

import eu.cessar.req.Requirement;

/**
 * JVMSizeUnitType - enumeration type for representing the JVM parameter size unit - megabytes or gigabytes.
 * 
 * @author uidw8762
 * 
 *         %created_by: uidw8762 %
 * 
 *         %date_created: Wed Apr 16 13:04:53 2014 %
 * 
 *         %version: 3 %
 */
@Requirement(
	reqID = "REQ_WORKSP#ACTIVE_JVM#2")
public enum JVMSizeUnitType
{
	@SuppressWarnings("javadoc")
	NONE, @SuppressWarnings("javadoc")
	MB, @SuppressWarnings("javadoc")
	GB;
}
