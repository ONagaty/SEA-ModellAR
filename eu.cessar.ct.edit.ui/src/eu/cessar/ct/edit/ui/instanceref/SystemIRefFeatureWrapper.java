/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870 Jun 10, 2010 5:10:58 PM </copyright>
 */
package eu.cessar.ct.edit.ui.instanceref;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;

/**
 * A wrapper around a feature(target feature or context feature) of a System Instance Reference
 * 
 */
public class SystemIRefFeatureWrapper
{
	private EObject input;
	private EReference feature;

	/** the value of the feature */
	private Object value;

	/**
	 * @param input
	 * @param feature
	 * 
	 */
	public SystemIRefFeatureWrapper(EObject input, EReference feature)
	{
		this.input = input;
		this.feature = feature;
	}

	public Object getValue()
	{
		return value;
	}

	public void setValue(Object value)
	{
		this.value = value;
	}

	public EReference getFeature()
	{
		return feature;
	}

	public EObject getInput()
	{
		return input;
	}

}
