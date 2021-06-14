/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Mar 18, 2010 1:51:07 PM </copyright>
 */
package eu.cessar.ct.runtime.ecuc.pmproxy.wrap;

import java.util.Collections;
import java.util.List;

import eu.cessar.ct.emfproxy.IEMFProxyEngine;

/**
 * @author uidl6458
 * 
 */
public abstract class PlainMasterObjectWrapper<T> extends AbstractPMObjectWrapper<T>
{
	private T masterObject;

	public PlainMasterObjectWrapper(IEMFProxyEngine engine, Class<T> masterClass, T masterObject)
	{
		super(engine, masterClass);
		this.masterObject = masterObject;
	}

	/**
	 * @param masterObject
	 */
	protected void setMasterObject(T masterObject)
	{
		this.masterObject = masterObject;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.IMasterObjectWrapper#getMasterObject()
	 */
	public T getMasterObject()
	{
		// TODO Auto-generated method stub
		return masterObject;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.IMasterObjectWrapper#getAllMasterObjects()
	 */
	public List<T> getAllMasterObjects()
	{
		return Collections.singletonList(getMasterObject());
	}

}
