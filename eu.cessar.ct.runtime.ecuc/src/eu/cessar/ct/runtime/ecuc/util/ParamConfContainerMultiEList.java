/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidl6458 Mar 18, 2010 2:57:23 PM </copyright>
 */
package eu.cessar.ct.runtime.ecuc.util;

import eu.cessar.ct.core.platform.util.IModelChangeStampProvider;
import gautosar.gecucdescription.GContainer;
import gautosar.gecucparameterdef.GParamConfContainerDef;

import java.util.List;

import org.eclipse.emf.common.util.EList;

/**
 * A sub list handling all containers with a particular definition from multiple lists
 */
public class ParamConfContainerMultiEList extends DelegatingWithSourceMultiEList<GContainer>
{

	private static final long serialVersionUID = 5191812933354524936L;
	private final GParamConfContainerDef definition;

	/**
	 * @param delegatedClass
	 * @param parentLists
	 */
	public ParamConfContainerMultiEList(GParamConfContainerDef definition, IModelChangeStampProvider changeProvider)
	{
		super(GContainer.class, changeProvider);
		this.definition = definition;
	}

	/**
	 * @param parentLists
	 */
	public ParamConfContainerMultiEList(GParamConfContainerDef definition, List<EList<? super GContainer>> parentLists,
		IModelChangeStampProvider changeProvider)
	{
		super(GContainer.class, parentLists, changeProvider);
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
			GContainer pValue = (GContainer) parentElement;
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
