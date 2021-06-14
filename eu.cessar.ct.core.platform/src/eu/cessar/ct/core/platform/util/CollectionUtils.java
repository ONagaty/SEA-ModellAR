/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Jan 14, 2010 10:37:42 AM </copyright>
 */
package eu.cessar.ct.core.platform.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.runtime.Assert;

import eu.cessar.ct.core.platform.results.Result;

/**
 * Various collection related utilities
 * 
 * @author uidl6458
 * 
 */
public final class CollectionUtils
{

	private CollectionUtils()
	{
		// do nothing
	}

	/**
	 * Join two lists of the same type. The result list is unmodifiable
	 * 
	 * @param <T>
	 * @param first
	 * @param second
	 * @return a list contain all items
	 */
	public static final <T> List<T> joinLists(List<? extends T> first, List<? extends T> second)
	{
		List<T> result = new ArrayList<T>();
		result.addAll(first);
		result.addAll(second);
		return Collections.unmodifiableList(result);
	}

	/**
	 * Returns an unmodifiable list with the elements that are contained in the first list, and are not contained in the
	 * second list.
	 * 
	 * @param <T>
	 * @param first
	 * @param second
	 * @return a list with the differences
	 */
	public static final <T> List<T> differenceLists(List<? extends T> first, List<? extends T> second)
	{
		List<T> result = new ArrayList<T>();
		for (T t: first)
		{
			if (!second.contains(t))
			{
				result.add(t);
			}
		}
		return Collections.unmodifiableList(result);
	}

	/**
	 * Returns a string with a list's elements, separated by the given <code>separator</code>
	 * 
	 * @param list
	 * @param separator
	 * @return the string, null if the list is null
	 */
	@SuppressWarnings("unchecked")
	public static final String toString(List<?> list, String separator)
	{
		return toString((List<Object>) list, separator, new Result<String, Object>()
		{
			@Override
			public String run(Object param)
			{
				return String.valueOf(param);
			}
		});
	}

	/**
	 * Return a string with all list elements separated by the given separator. The transformation to String is
	 * performed by the provided {@link Result transformer}
	 * 
	 * @param list
	 *        the list to act on, can be also null
	 * @param separator
	 *        the separator, should not be null
	 * @param transformer
	 *        transformation class, should not be null
	 * @return a string or null of the list is null
	 */
	public static final <T> String toString(List<T> list, String separator, Result<String, T> transformer)
	{
		if (list == null)
		{
			return null;
		}
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < list.size(); i++)
		{
			sb.append(transformer.run(list.get(i)));
			if (i < list.size() - 1)
			{
				sb.append(separator);
			}
		}
		return sb.toString();
	}

	/**
	 * 
	 * Add all <code>elements</code> to the <code>list</code> avoiding duplicates. The {@link List#contains(Object)}
	 * method will be used to search for duplicates.
	 * 
	 * @param <T>
	 * @param list
	 * @param elements
	 */
	public static <T> void addNoDuplicates(List<T> list, T... elements)
	{
		Assert.isNotNull(list);
		for (T t: elements)
		{
			if (!list.contains(t))
			{
				list.add(t);
			}
		}
	}

	/**
	 * @param <T>
	 * @param target
	 * @param source
	 */
	public static <T> void addNoDuplicates(List<T> target, List<T> source)
	{
		Assert.isNotNull(target);
		Assert.isNotNull(source);
		for (T t: source)
		{
			if (!target.contains(t))
			{
				target.add(t);
			}
		}

	}

	/**
	 * Return true if both collections are null or contain exactly the same element maybe in a different order
	 * 
	 * @param coll1
	 * @param coll2
	 * @return
	 */
	public static boolean sameContent(Collection<Object> coll1, Collection<Object> coll2)
	{
		if (coll1 == null && coll2 == null)
		{
			return true;
		}
		else if (coll1 == null || coll2 == null)
		{
			return false;
		}
		else
		{
			return coll1.containsAll(coll2) && coll2.containsAll(coll1);
		}
	}

	/**
	 * Convert a list of Integer to an array of int's
	 * 
	 * @param list
	 * @return
	 */
	public static int[] toIntArray(List<Integer> list)
	{
		int[] result = new int[list.size()];
		for (int i = 0; i < list.size(); i++)
		{
			result[i] = list.get(i);
		}
		return result;
	}
}
