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
import gautosar.gecucparameterdef.GChoiceContainerDef;
import gautosar.gecucparameterdef.GParamConfMultiplicity;

import java.util.List;

import org.eclipse.emf.common.util.EList;

/**
 * A sub list handling all containers with a particular definition and a particular name from multiple lists
 */
public class NamedChoiceContainerMultiEList extends DelegatingWithSourceMultiEList<GContainer>
{

	private static final long serialVersionUID = 5191812933354524936L;
	private final GChoiceContainerDef definition;
	private final String name;

	/**
	 * @param delegatedClass
	 * @param parentLists
	 */
	public NamedChoiceContainerMultiEList(GChoiceContainerDef definition, String name,
		IModelChangeStampProvider changeProvider)
	{
		super(GContainer.class, changeProvider);
		this.definition = definition;
		this.name = name;
	}

	/**
	 * @param parentLists
	 */
	public NamedChoiceContainerMultiEList(GChoiceContainerDef definition, String name,
		List<EList<? super GContainer>> parentLists, IModelChangeStampProvider changeProvider)
	{
		super(GContainer.class, parentLists, changeProvider);
		this.definition = definition;
		this.name = name;
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
			boolean sameDef = false;
			if (pValue.gGetDefinition() != null)
			{
				sameDef = SplitPMUtils.isEqualSplitDefinition(
					(GParamConfMultiplicity) pValue.gGetDefinition().eContainer(), definition);
			}
			else
			{
				sameDef = definition == null;
			}
			if (sameDef)
			{
				if (name == null)
				{
					return pValue.gGetShortName() == null;
				}
				else
				{
					return name.equals(pValue.gGetShortName());
				}
			}
			else
			{
				return false;
			}
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
