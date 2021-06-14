/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl7321 Dec 17, 2010 2:49:43 PM </copyright>
 */
package eu.cessar.ct.compat.internal.ctproxy.wrap;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;

import eu.cessar.ct.compat.internal.ctproxy.convertors.IDataTypeConvertor;
import eu.cessar.ct.emfproxy.IEMFProxyConstants;
import eu.cessar.ct.emfproxy.IEMFProxyEngine;
import eu.cessar.ct.runtime.ecuc.pmproxy.wrap.AbstractMasterFeatureWrapper;

/**
 * @author uidl7321
 * @param <T>
 * @Review uidl7321 - Apr 11, 2012
 */
public abstract class AbstractDirectEAttributeWrapper extends AbstractMasterFeatureWrapper<Object>
{

	protected EObject master;
	protected EAttribute eAttribute;
	protected IDataTypeConvertor<Object, Object> convertor;

	/**
	 * @param engine
	 */
	public AbstractDirectEAttributeWrapper(IEMFProxyEngine engine, EObject master,
		EAttribute eAttribute, IDataTypeConvertor<Object, Object> convertor)
	{
		super(engine);
		this.master = master;
		this.eAttribute = eAttribute;
		this.convertor = convertor;
	}

	public Class<Object> getFeatureClass()
	{
		return Object.class;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.runtime.ecuc.pmproxy.wrap.AbstractWrapper#getContext()
	 */
	@Override
	protected Object getContext()
	{
		return IEMFProxyConstants.DEFAULT_CONTEXT;
	}

}
