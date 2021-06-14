/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidl6458 Mar 17, 2010 4:59:19 PM </copyright>
 */
package eu.cessar.ct.runtime.ecuc.util;

import eu.cessar.ct.core.platform.util.IModelChangeStampProvider;
import gautosar.gecucdescription.GConfigReferenceValue;
import gautosar.gecucparameterdef.GConfigReference;

import java.util.List;

import org.eclipse.emf.common.util.EList;

/**
 *
 */
public class ReferenceValueMultiEList<E extends GConfigReferenceValue> extends DelegatingWithSourceMultiEList<E>
{

	private static final long serialVersionUID = 431716339134057604L;

	private final GConfigReference definition;

	/**
	 * @param delegatedClass
	 * @param definition
	 * @param parentLists
	 */
	public ReferenceValueMultiEList(Class<E> delegatedClass, GConfigReference definition,
		IModelChangeStampProvider changeProvider)
	{
		super(delegatedClass, changeProvider);
		this.definition = definition;
	}

	/**
	 * @param delegatedClass
	 * @param definition
	 * @param parentLists
	 */
	public ReferenceValueMultiEList(Class<E> delegatedClass, GConfigReference definition,
		List<EList<? super E>> parentLists, IModelChangeStampProvider changeProvider)
	{
		super(delegatedClass, parentLists, changeProvider);
		this.definition = definition;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.core.platform.util.DelegatingMultiEList#isDelegatedObject(java.lang.Object)
	 */
	@Override
	protected boolean isDelegatedObject(Object parentElement)
	{
		if (parentElement == null)
		{
			return false;
		}
		if (getDelegatedClass().isInstance(parentElement))
		{
			GConfigReferenceValue pValue = (GConfigReferenceValue) parentElement;
			return SplitPMUtils.isEqualSplitDefinition(pValue.gGetDefinition(), definition);
		}
		else
		{
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.runtime.ecuc.util.ESplitableList#isSplited()
	 */
	public boolean isSplited()
	{
		return true;
	}
}
