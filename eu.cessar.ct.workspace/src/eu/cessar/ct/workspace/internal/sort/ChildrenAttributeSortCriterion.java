/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidt2045 Jul 8, 2011 4:06:39 PM </copyright>
 */
package eu.cessar.ct.workspace.internal.sort;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EReference;

import eu.cessar.ct.workspace.sort.ISortTarget;
import eu.cessar.ct.workspace.sort.SimpleAttributeSortCriterion;

/**
 * @author uidt2045
 * 
 */
public class ChildrenAttributeSortCriterion extends SimpleAttributeSortCriterion
{

	private final EReference childrenRef;

	/**
	 * @param sortTarget
	 * @param attribute
	 */
	public ChildrenAttributeSortCriterion(ISortTarget sortTarget, EReference childrenRef,
		EAttribute attribute)
	{
		super(sortTarget, attribute);
		this.childrenRef = childrenRef;
	}

	/* (non-Javadoc)
	 * @see org.autosartools.general.core.internal.sort.SimpleAttributeSortCriterion#getLabel()
	 */
	@Override
	public String getLabel()
	{
		return childrenRef.getName() + "->" + super.getLabel(); //$NON-NLS-1$
	}

}
