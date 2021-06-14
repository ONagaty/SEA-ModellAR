/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl7321 Dec 17, 2010 4:42:45 PM </copyright>
 */
package eu.cessar.ct.compat.internal.ctproxy.wrap;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;

import eu.cessar.ct.emfproxy.IEMFProxyConstants;
import eu.cessar.ct.emfproxy.IEMFProxyEngine;
import eu.cessar.ct.runtime.ecuc.pmproxy.wrap.AbstractMasterFeatureWrapper;

/**
 * @author uidl7321
 * @Review uidl7321 - Apr 11, 2012
 */
public abstract class AbstractDirectEReferenceWrapper extends AbstractMasterFeatureWrapper<Object>
{
	protected EObject master;
	protected EReference eReference;

	/**
	 * @param engine
	 */
	public AbstractDirectEReferenceWrapper(IEMFProxyEngine engine, EObject master,
		EReference eReference)
	{
		super(engine);
		this.master = master;
		this.eReference = eReference;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.IMasterFeatureWrapper#getFeatureClass()
	 */
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
