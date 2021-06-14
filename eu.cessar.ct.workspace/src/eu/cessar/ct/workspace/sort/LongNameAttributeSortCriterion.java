/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidt2045 Jul 22, 2011 11:04:29 AM </copyright>
 */
package eu.cessar.ct.workspace.sort;

import gautosar.ggenericstructure.ginfrastructure.GIdentifiable;

import org.artop.aal.common.util.IdentifiableUtil;
import org.eclipse.emf.ecore.EObject;

/**
 * @author uidt2045
 *
 */
public class LongNameAttributeSortCriterion extends SimpleAttributeSortCriterion
{

	/**
	 * @param sortTarget
	 * @param attribute
	 */
	public LongNameAttributeSortCriterion(ISortTarget sortTarget)
	{
		super(sortTarget, null);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.workspace.sort.SimpleAttributeSortCriterion#getObjectToCompare(org.eclipse.emf.ecore.EObject)
	 */
	@Override
	public Object getObjectToCompare(EObject parent)
	{
		if (parent instanceof GIdentifiable)
		{
			return IdentifiableUtil.getLongName(parent, "EN");
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.workspace.sort.SimpleAttributeSortCriterion#getLabel()
	 */
	@Override
	public String getLabel()
	{
		return "longName"; //$NON-NLS-1$
	}

/*
 * (non-Javadoc)
 *
 * @see eu.cessar.ct.workspace.sort.SimpleAttributeSortCriterion#getTypeName()
 */
	@Override
	public String getTypeName()
	{
		return "longName"; //$NON-NLS-1$
	}
}
