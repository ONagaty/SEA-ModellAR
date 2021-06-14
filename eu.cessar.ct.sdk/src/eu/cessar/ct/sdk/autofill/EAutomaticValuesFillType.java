/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidw8762<br/>
 * Dec 2, 2013 10:41:02 AM
 * 
 * </copyright>
 */
package eu.cessar.ct.sdk.autofill;

/**
 * Enumeration of the possible ways to fill the parameters with a specified type and value.
 * 
 * FILL_ALL - fill all elements; FILL_SET - fill only already set elements; FILL_NOT_SET - fill only not set elements;
 * 
 * @author uidw8762
 * 
 *         %created_by: uidl6870 %
 * 
 *         %date_created: Mon Feb 10 09:11:28 2014 %
 * 
 *         %version: 1 %
 */
public enum EAutomaticValuesFillType
{

	/**
	 * 
	 */
	FILL_NONE,

	/**
	 * 
	 */
	FILL_ALL, /**
	 * 
	 */
	FILL_SET, /**
	 * 
	 */
	FILL_NOT_SET;

}
