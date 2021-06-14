/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Mar 18, 2010 3:46:08 PM </copyright>
 */
package eu.cessar.ct.runtime.ecuc.pmproxy.wrap;

import java.util.Map;

import org.eclipse.emf.edit.domain.EditingDomain;

import eu.cessar.ct.core.mms.MetaModelUtils;
import eu.cessar.ct.emfproxy.IEMFProxyEngine;
import eu.cessar.ct.emfproxy.IMasterObjectWrapper;

/**
 * @author uidl6458
 * 
 */
public abstract class AbstractMasterObjectWrapper<T> extends AbstractWrapper implements IMasterObjectWrapper<T>
{
	private final Class<T> masterClass;

	/**
	 * @param masterClass
	 */
	public AbstractMasterObjectWrapper(IEMFProxyEngine engine, Class<T> masterClass)
	{
		super(engine);
		this.masterClass = masterClass;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.emfproxy.IMasterObjectWrapper#getMasterClass()
	 */
	public Class<T> getMasterClass()
	{
		// TODO Auto-generated method stub
		return masterClass;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.emfproxy.IMasterObjectWrapper#isWrapperForClass(java.lang.Class)
	 */
	public boolean isWrapperForClass(Class<?> clz)
	{
		return masterClass.isAssignableFrom(clz);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.emfproxy.IMasterObjectWrapper#getParameters()
	 */
	public Map<String, Object> getParameters()
	{
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.emfproxy.IMasterObjectWrapper#getEditingDomain()
	 */
	@Override
	public EditingDomain getEditingDomain()
	{
		return MetaModelUtils.getEditingDomain(getEngine().getProject());
	}
}
