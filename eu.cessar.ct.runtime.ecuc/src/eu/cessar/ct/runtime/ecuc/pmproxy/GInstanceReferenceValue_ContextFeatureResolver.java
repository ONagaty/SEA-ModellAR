/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Feb 4, 2010 7:44:45 PM </copyright>
 */
package eu.cessar.ct.runtime.ecuc.pmproxy;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;

import eu.cessar.ct.emfproxy.IEMFProxyEngine;
import eu.cessar.ct.emfproxy.IMasterFeatureWrapper;
import eu.cessar.ct.emfproxy.impl.EMFProxyObjectImpl;
import eu.cessar.ct.runtime.ecuc.pmproxy.wrap.GInstanceReferenceContextFeatureWrapper;
import eu.cessar.ct.runtime.ecuc.pmproxy.wrap.GInstanceReferenceObjectWrapper;
import gautosar.gecucdescription.GInstanceReferenceValue;

/**
 * 
 */
public class GInstanceReferenceValue_ContextFeatureResolver extends
	AbstractEReferenceFeatureResolver
{
	/* (non-Javadoc)
	 * @see eu.cessar.ct.runtime.ecuc.pmproxy.AbstractEReferenceFeatureResolver#getReferenceWrapper(eu.cessar.ct.emfproxy.IEMFProxyEngine, eu.cessar.ct.emfproxy.impl.EMFProxyObjectImpl, org.eclipse.emf.ecore.EReference)
	 */
	@Override
	protected IMasterFeatureWrapper<EObject> getReferenceWrapper(IEMFProxyEngine engine,
		EMFProxyObjectImpl proxyObject, EReference reference)
	{
		GInstanceReferenceObjectWrapper wrapper = (GInstanceReferenceObjectWrapper) proxyObject.eGetMasterWrapper();
		GInstanceReferenceValue iRefValue = wrapper.getMasterObject();
		return new GInstanceReferenceContextFeatureWrapper(engine, iRefValue);
	}

}
