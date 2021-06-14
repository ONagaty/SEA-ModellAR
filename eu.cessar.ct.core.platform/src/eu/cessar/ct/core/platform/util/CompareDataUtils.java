/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidv3687<br/>
 * Apr 1, 2013 11:34:40 AM
 * 
 * </copyright>
 */
package eu.cessar.ct.core.platform.util;

/**
 * Utility class dealing with different data type comparison. Useful especially for dealing cases when data are null.
 * 
 * @author uidv3687
 * 
 *         %created_by: uidv3687 %
 * 
 *         %date_created: Wed Apr 10 16:39:07 2013 %
 * 
 *         %version: 2 %
 */
public final class CompareDataUtils
{
	private CompareDataUtils()
	{
		// no call of constructor
	}

	/**
	 * Compares the two objects. The result is <code>true</code> if the two objects are not <code>null</code> and have
	 * the same value. If both objects are <code>null</code>, <code>true</code> is also return. Has to be used for types
	 * that implement <code>t1.equals(t2)</code> method
	 * 
	 * @param t1
	 *        first generic object
	 * @param t2
	 *        second generic object
	 * @return <code>true</code> if the values are the same; <code>false</code> otherwise.
	 */

	public static <T> boolean equal(T t1, T t2)
	{
		return ((null == t1 && null == t2) || ((null != t1) && t1.equals(t2)));
	}
}
