/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidl6458 Mar 17, 2010 11:34:29 AM </copyright>
 */
package eu.cessar.ct.runtime.ecuc.util;

import eu.cessar.ct.core.platform.util.IModelChangeStampProvider;
import gautosar.gecucdescription.GConfigReferenceValue;
import gautosar.gecucparameterdef.GConfigReference;

import org.eclipse.emf.common.util.EList;

/**
 * A delegating EList specialized for ECUC reference values that also checks the definitions.<br/>
 * Sample usage:<br/>
 *
 * <pre>
 * GContainer c = ...;
 * GInstanceReferenceDef referenceDef = ...;
 * EList&lt;GInstanceReferenceValue&gt; stringValues = new ReferenceValueSubEList&lt;GStringValue&gt;(c.gGetReferenceValues(),
 * 	GInstanceReferenceValue.class, referenceDef);
 * </pre>
 *
 */
public class ReferenceValueSubEList<E extends GConfigReferenceValue> extends DelegatingWithSourceSubEList<E> implements
ESplitableList<E>
{

	/**
	 *
	 */
	private static final long serialVersionUID = 7458306836647269003L;

	private final GConfigReference definition;

	/**
	 * @param parentList
	 * @param delegatedClass
	 */
	public ReferenceValueSubEList(Class<E> delegatedClass, GConfigReference definition, EList<? super E> parentList,
		Object source, IModelChangeStampProvider changeProvider)
	{
		super(delegatedClass, parentList, source, changeProvider);
		// TODO Auto-generated constructor stub
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
		return false;
	}

}
