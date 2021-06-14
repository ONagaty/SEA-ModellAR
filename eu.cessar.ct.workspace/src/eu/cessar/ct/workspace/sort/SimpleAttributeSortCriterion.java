/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidt2045 Jul 8, 2011 4:03:25 PM </copyright>
 */
package eu.cessar.ct.workspace.sort;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;

/**
 * @author uidt2045
 * 
 */
public class SimpleAttributeSortCriterion implements ISortCriterion
{
	private final EAttribute attribute;
	private final ISortTarget sortTarget;

	public SimpleAttributeSortCriterion(ISortTarget sortTarget, EAttribute attribute)
	{
		this.sortTarget = sortTarget;
		this.attribute = attribute;
	}

	/* (non-Javadoc)
	 * @see org.autosartools.general.core.sort.ISortCriterion#getObjectToCompare(org.eclipse.emf.ecore.EObject)
	 */
	public Object getObjectToCompare(EObject parent)
	{
		return parent.eGet(attribute);
	}

	/* (non-Javadoc)
	 * @see org.autosartools.general.core.sort.ISortCriterion#createDirectionalSortCriterion(org.autosartools.general.core.sort.ESortDirection)
	 */
	public IDirectionalSortCriterion createDirectionalSortCriterion(ESortDirection direction)
	{
		return new DirectionalSortCriterion(this, direction);
	}

	/* (non-Javadoc)
	 * @see org.autosartools.general.core.sort.ISortCriterion#getImage()
	 */
	public Object getImage()
	{
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.autosartools.general.core.sort.ISortCriterion#getLabel()
	 */
	public String getLabel()
	{
		return attribute.getName();
	}

	/* (non-Javadoc)
	 * @see org.autosartools.general.core.sort.ISortCriterion#getSortTarget()
	 */
	public ISortTarget getSortTarget()
	{
		return sortTarget;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.workspace.sort.ISortCriterion#getTypeName()
	 */
	public String getTypeName()
	{
		if (attribute == null)
		{
			return ""; //$NON-NLS-1$
		}
		return attribute.getName();
	}
}
