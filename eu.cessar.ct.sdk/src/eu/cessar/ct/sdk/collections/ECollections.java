/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Jun 25, 2010 4:09:15 PM </copyright>
 */
package eu.cessar.ct.sdk.collections;

import java.util.Comparator;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

/**
 * Various EMF collection utilities
 */
public final class ECollections
{

	private static class FeatureNameComparator<T extends EObject> implements Comparator<T>
	{

		private final String featureName;

		private FeatureNameComparator(String featureName)
		{
			this.featureName = featureName;
		}

		/* (non-Javadoc)
		 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
		 */
		public int compare(T o1, T o2)
		{
			if (o1 == null && o2 == null)
			{
				return 0;
			}
			if (o1 == null && o2 != null)
			{
				return -1;
			}
			if (o1 != null && o2 == null)
			{
				return 1;
			}
			// at this point both are not null
			EStructuralFeature f1 = o1.eClass().getEStructuralFeature(featureName);
			EStructuralFeature f2 = o2.eClass().getEStructuralFeature(featureName);
			if (f1 == null)
			{
				throw new IllegalArgumentException(o1 + " does not have a feature named " //$NON-NLS-1$
					+ featureName);
			}
			if (f2 == null)
			{
				throw new IllegalArgumentException(o2 + " does not have a feature named " //$NON-NLS-1$
					+ featureName);
			}
			Object value1 = o1.eGet(f1);
			Object value2 = o2.eGet(f1);
			if (value1 == null && value2 == null)
			{
				return 0;
			}
			if (value1 == null && value2 != null)
			{
				return -1;
			}
			if (value1 != null && value2 == null)
			{
				return 1;
			}
			// at this point both are not null
			if (value1 instanceof Comparable<?>)
			{

				return ((Comparable) value1).compareTo(value2);
			}
			else
			{
				throw new IllegalArgumentException(
					"Cannot compare selected features, please use a custom comparator"); //$NON-NLS-1$
			}
		}

	}

	/**
	 * Return a copy of the <code>list</code> argument. Please note that the
	 * returned list itself can be modified. However the list components might
	 * not be modifiable.
	 * 
	 * @param <T>
	 * @param list
	 * @return a copy of the list or null if the <code>list</code> argument is
	 *         null
	 */
	public static <T extends EObject> EList<T> copyList(EList<T> list)
	{
		if (list == null)
		{
			return null;
		}
		return new BasicEList<T>(list);
	}

	/**
	 * Return a sorted copy of the <code>list</code>. The sorting is done by
	 * inspecting the values of <code>featureName</code> from each list member.
	 * 
	 * @param <T>
	 * @param list
	 * @return
	 */
	public static <T extends EObject> EList<T> sortCopyList(EList<T> list, String featureName)
	{
		EList<T> copy = copyList(list);
		org.eclipse.emf.common.util.ECollections.sort(copy, new FeatureNameComparator<T>(
			featureName));
		return copy;
	}

	/**
	 * @param <T>
	 * @param list
	 * @param featureName
	 * @return
	 */
	public static <T extends EObject> EList<T> sortCopyList(EList<T> list, Comparator<T> comparator)
	{
		EList<T> copy = copyList(list);
		org.eclipse.emf.common.util.ECollections.sort(copy, comparator);
		return copy;
	}

	/**
	 * Return a sorted copy of the <code>list</code>. The sorting is done by
	 * inspecting for each element of the list the value of a feature named
	 * <code>shortName</code>.
	 * 
	 * @param <T>
	 * @param list
	 * @return
	 */
	public static <T extends EObject> EList<T> sortCopyListByShortName(EList<T> list)
	{
		return sortCopyList(list, "shortName"); //$NON-NLS-1$
	}
}
