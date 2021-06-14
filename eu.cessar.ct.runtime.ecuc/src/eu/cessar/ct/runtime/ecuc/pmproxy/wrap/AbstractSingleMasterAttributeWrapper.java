/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Mar 17, 2010 2:49:43 PM </copyright>
 */
package eu.cessar.ct.runtime.ecuc.pmproxy.wrap;

import eu.cessar.ct.emfproxy.IEMFProxyEngine;
import eu.cessar.ct.emfproxy.IMasterAttributeWrapper;
import eu.cessar.ct.emfproxy.ISingleMasterFeatureWrapper;

/**
 * @author uidl6458
 * 
 */
public abstract class AbstractSingleMasterAttributeWrapper<T> extends
	AbstractMasterFeatureWrapper<T> implements ISingleMasterFeatureWrapper<T>,
	IMasterAttributeWrapper<T>
{

	/**
	 * @param engine
	 */
	public AbstractSingleMasterAttributeWrapper(IEMFProxyEngine engine)
	{
		super(engine);
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.IMasterFeatureWrapper#getHashCode()
	 */
	public int getHashCode()
	{
		T t = getValue();
		if (t == null)
		{
			return 0;
		}
		else
		{
			return t.hashCode();
		}
	}

}
