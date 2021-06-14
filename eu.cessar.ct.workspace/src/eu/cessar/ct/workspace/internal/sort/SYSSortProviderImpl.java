/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidt2045 Jul 7, 2011 2:57:24 PM </copyright>
 */
package eu.cessar.ct.workspace.internal.sort;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.transaction.TransactionalEditingDomain;

import eu.cessar.ct.workspace.sort.AbstractSortProviderImpl;
import eu.cessar.ct.workspace.sort.ISortTarget;
import eu.cessar.ct.workspace.sort.SortUtils;

/**
 * @author uidt2045
 * 
 */
public class SYSSortProviderImpl extends AbstractSortProviderImpl
{

	/**
	 * @param parentObject
	 */
	public SYSSortProviderImpl(TransactionalEditingDomain domain, EObject parentObject)
	{
		super(domain, parentObject);
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.ecuc.workspace.internal.sort.AbstractSortProviderImpl#doGetSortTargets()
	 */
	@Override
	protected List<ISortTarget> doGetSortTargets()
	{
		List<ISortTarget> targets = new ArrayList<ISortTarget>();
		EList<EObject> children = parentObject.eContents();
		List<EClass> childClasses = new ArrayList<EClass>();
		for (EObject chObj: children)
		{
			if (!childClasses.contains(chObj.eClass()))
			{
				childClasses.add(chObj.eClass());
			}
		}
		SortUtils.computeSortTargets(this, targets, parentObject.eClass(), childClasses);
		// computeSortTargets(targets, parentObject.eClass(), childClasses);

		return targets;
	}

}
