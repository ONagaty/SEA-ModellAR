/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Mar 17, 2010 2:48:40 PM </copyright>
 */
package eu.cessar.ct.runtime.ecuc.pmproxy.wrap;

import eu.cessar.ct.emfproxy.IEMFProxyEngine;

/**
 * @author uidl6458
 * 
 */
public class PlainSingleFeatureWrapper<T> extends AbstractSingleMasterAttributeWrapper<T>
{

	private T value;

	/**
	 * @param initialValue
	 */
	public PlainSingleFeatureWrapper(IEMFProxyEngine engine, T initialValue)
	{
		this(engine, initialValue, false);
	}

	/**
	 * @param initialValue
	 * @param readOnly
	 */
	public PlainSingleFeatureWrapper(IEMFProxyEngine engine, T initialValue, boolean readOnly)
	{
		super(engine);
		this.value = initialValue;
		setReadOnly(readOnly);
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.IMasterFeatureWrapper#getFeatureClass()
	 */
	public Class<T> getFeatureClass()
	{
		if (value != null)
		{
			return (Class<T>) value.getClass();
		}
		else
		{
			return (Class<T>) Object.class;
		}
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.ISingleMasterFeatureWrapper#getValue()
	 */
	public T getValue()
	{
		return value;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.ISingleMasterFeatureWrapper#setValue(java.lang.Object)
	 */
	public void setValue(Object newValue)
	{
		value = (T) newValue;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.ISingleMasterFeatureWrapper#isSetValue()
	 */
	public boolean isSetValue()
	{
		return value != null;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.ISingleMasterFeatureWrapper#unsetValue()
	 */
	public void unsetValue()
	{
		value = null;
	}
}
