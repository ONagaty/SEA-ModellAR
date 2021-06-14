/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870<br/>
 * 26.10.2012 11:30:59
 * 
 * </copyright>
 */
package eu.cessar.ct.core.mms;

import java.util.Comparator;
import java.util.List;

/**
 * Base implementation.
 * 
 * @author uidl6870
 * 
 *         %created_by: uidl6870 %
 * 
 *         %date_created: Mon Apr  1 09:10:29 2013 %
 * 
 *         %version: 1 %
 * @param <T>
 */
public abstract class AbstractCessarComparator<T> implements Comparator<T>
{

	/**
	 * @param leftObj
	 * @param rightObj
	 * @return the comparison result
	 */
	protected static int doCompareObjects(Object leftObj, Object rightObj)
	{
		int result = 0;

		if (leftObj == null && rightObj == null)
		{
			result = 0;
		}
		else if (leftObj == null && rightObj != null)
		{
			result = -1;
		}
		else if (leftObj != null && rightObj == null)
		{
			result = 1;
		}
		else
		{
			if (leftObj instanceof Comparable<?> && rightObj instanceof Comparable<?> && !(leftObj instanceof String)
				&& !(rightObj instanceof String))
			{

				result = ((Comparable) leftObj).compareTo(rightObj);
			}
			else
			{
				result = leftObj.toString().compareToIgnoreCase(rightObj.toString());
			}
		}

		return result;
	}

	/**
	 * Compares the elements from the two given lists one by one, by calling {@link #doCompareObjects(Object, Object)}
	 * with elements from the first position; if <code>0</code> is received, it continues with the elements from the
	 * second position, and so on, until <code>-1</code> or <code>1</code> is obtained.
	 * 
	 * @param list1
	 *        the first list to be compared
	 * @param list2
	 *        the second list to be compared
	 * @return the comparison result
	 */
	protected static int doCompareLists(List<Object> list1, List<Object> list2)
	{
		int result = 0;
		int i = 0;

		Object leftObj = null;
		Object rightObj = null;

		do
		{
			if (list1.size() > i)
			{
				leftObj = list1.get(i);
			}

			if (list2.size() > i)
			{
				rightObj = list2.get(i);
			}

			int partialResult = doCompareObjects(leftObj, rightObj);
			if (partialResult != 0)
			{
				// found a difference, store the comparison result and exit
				// loop
				result = partialResult;
				break;
			}
			else
			{
				i++;
			}

			leftObj = null;
			rightObj = null;

		}
		while (list1.size() > i && list2.size() > i);

		if (result == 0)
		{
			int size1 = list1.size();
			int size2 = list2.size();

			if (size1 != size2)
			{
				result = (size1 > size2) ? 1 : -1;
			}
		}

		return result;
	}

}
