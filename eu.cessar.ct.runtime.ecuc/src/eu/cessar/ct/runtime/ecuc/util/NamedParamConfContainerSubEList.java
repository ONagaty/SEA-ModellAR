/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidl6458 Mar 18, 2010 2:54:52 PM </copyright>
 */
package eu.cessar.ct.runtime.ecuc.util;

import eu.cessar.ct.core.platform.util.IModelChangeStampProvider;
import gautosar.gecucdescription.GContainer;
import gautosar.gecucparameterdef.GParamConfContainerDef;

import org.eclipse.emf.common.util.EList;

/**
 * A sub list view handling all containers with a particular definition and a particular name from a another list
 */
public class NamedParamConfContainerSubEList extends DelegatingWithSourceSubEList<GContainer> implements
ESplitableList<GContainer>
{

	/**
	 *
	 */
	private static final long serialVersionUID = 3119065661083594642L;
	private final GParamConfContainerDef definition;
	private final String name;

	/**
	 * @param parentList
	 * @param delegatedClass
	 */
	public NamedParamConfContainerSubEList(GParamConfContainerDef definition, String name,
		EList<? super GContainer> parentList, Object source, IModelChangeStampProvider changeProvider)
	{
		super(GContainer.class, parentList, source, changeProvider);
		this.name = name;
		this.definition = definition;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.core.platform.util.DelegatingSubEList#isDelegatedObject(java.lang.Object)
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
			if (SplitPMUtils.isEqualSplitDefinition(pValue.gGetDefinition(), definition))
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
		return false;
	}
}
