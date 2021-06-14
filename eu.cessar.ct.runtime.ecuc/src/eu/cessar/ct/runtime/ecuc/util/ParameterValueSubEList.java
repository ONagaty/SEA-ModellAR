/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidl6458 Mar 17, 2010 11:34:29 AM </copyright>
 */
package eu.cessar.ct.runtime.ecuc.util;

import eu.cessar.ct.core.platform.util.IModelChangeStampProvider;
import gautosar.gecucdescription.GParameterValue;
import gautosar.gecucparameterdef.GConfigParameter;

import org.eclipse.emf.common.util.EList;

/**
 * A delegating EList specialized for ECUC parameter values that also checks the definitions.<br/>
 * Sample usage:<br/>
 *
 * <pre>
 * GContainer c = ...;
 * GStringParamDef stringDef = ...;
 * EList&lt;GStringValue&gt; stringValues = new ParameterValueSubEList&lt;GStringValue&gt;(c.gGetParameterValues(),
 * 	GStringValue.class, stringDef);
 * </pre>
 *
 */
public class ParameterValueSubEList<E extends GParameterValue> extends DelegatingWithSourceSubEList<E> implements
	ESplitableList<E>
{

	/**
	 *
	 */
	private static final long serialVersionUID = 7458306836647269003L;

	private final GConfigParameter definition;

	/**
	 * @param parentList
	 * @param delegatedClass
	 */
	public ParameterValueSubEList(Class<E> delegatedClass, GConfigParameter definition, EList<? super E> parentList,
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
			GParameterValue pValue = (GParameterValue) parentElement;
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
