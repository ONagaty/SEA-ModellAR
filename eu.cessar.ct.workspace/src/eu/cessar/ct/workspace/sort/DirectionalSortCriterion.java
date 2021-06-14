/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidt2045 Jul 8, 2011 4:04:18 PM </copyright>
 */
package eu.cessar.ct.workspace.sort;

import org.eclipse.emf.ecore.EObject;

import eu.cessar.ct.workspace.internal.CessarPluginActivator;

/**
 * @author uidt2045
 * 
 */
public class DirectionalSortCriterion implements IDirectionalSortCriterion
{

	private final ISortCriterion criterion;
	private final ESortDirection direction;

	public DirectionalSortCriterion(ISortCriterion criterion, ESortDirection direction)
	{
		this.criterion = criterion;
		this.direction = direction;
	}

	/* (non-Javadoc)
	 * @see org.autosartools.general.core.sort.IDirectionalSortCriterion#getSortDirection()
	 */
	public ESortDirection getSortDirection()
	{
		return direction;
	}

	/* (non-Javadoc)
	 * @see org.autosartools.general.core.sort.IDirectionalSortCriterion#getSortCriterion()
	 */
	public ISortCriterion getSortCriterion()
	{
		return criterion;
	}

	/* (non-Javadoc)
	 * @see org.autosartools.general.core.sort.IDirectionalSortCriterion#compare(org.eclipse.emf.ecore.EObject, org.eclipse.emf.ecore.EObject)
	 */
	public int compare(EObject left, EObject right)
	{
		Object leftObj = null;
		int ascResult;
		if (left != null)
		{
			leftObj = criterion.getObjectToCompare(left);
		}
		Object rightObj = null;
		if (right != null)
		{
			rightObj = criterion.getObjectToCompare(right);
		}
		if (leftObj == null && rightObj == null)
		{
			ascResult = 0;
		}
		else if (leftObj == null && rightObj != null)
		{
			ascResult = -1;
		}
		else if (leftObj != null && rightObj == null)
		{
			ascResult = 1;
		}
		else
		// both not null, try to compare them as comparator
		if (leftObj instanceof Comparable<?> && rightObj instanceof Comparable<?>
			&& !(leftObj instanceof String) && !(rightObj instanceof String))
		{
			try
			{
				ascResult = ((Comparable) leftObj).compareTo(rightObj);
			}
			catch (Throwable t)
			{
				CessarPluginActivator.getDefault().logError(t);
				// assume equals
				ascResult = 0;
			}
		}
		else
		{
			ascResult = leftObj.toString().compareToIgnoreCase(rightObj.toString());
		}
		if (direction == ESortDirection.DESCENDING)
		{
			ascResult = -ascResult;
		}
		return ascResult;

	}

}
