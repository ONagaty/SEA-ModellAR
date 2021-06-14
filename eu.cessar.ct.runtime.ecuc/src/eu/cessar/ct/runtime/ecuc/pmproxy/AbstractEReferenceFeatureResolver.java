/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Mar 13, 2010 7:06:24 PM </copyright>
 */
package eu.cessar.ct.runtime.ecuc.pmproxy;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;

import eu.cessar.ct.emfproxy.IEMFProxyEngine;
import eu.cessar.ct.emfproxy.IMasterFeatureWrapper;
import eu.cessar.ct.emfproxy.impl.EMFProxyObjectImpl;
import eu.cessar.ct.emfproxy.util.InternalProxyConfigurationError;

/**
 * 
 */
public abstract class AbstractEReferenceFeatureResolver extends AbstractPMFeatureResolver<EObject>
{

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.IProxyFeatureResolver#getFeatureWrapper(eu.cessar.ct.emfproxy.IEMFProxyEngine, eu.cessar.ct.emfproxy.impl.EMFProxyObjectImpl, org.eclipse.emf.ecore.EStructuralFeature)
	 */
	public IMasterFeatureWrapper<EObject> getFeatureWrapper(IEMFProxyEngine engine,
		EMFProxyObjectImpl proxyObject, EStructuralFeature feature)
	{
		InternalProxyConfigurationError.assertTrue(feature instanceof EReference);
		EReference reference = (EReference) feature;
		return getReferenceWrapper(engine, proxyObject, reference);
	}

	/**
	 * @param engine
	 * @param proxyObject
	 * @param reference
	 * @return
	 */
	protected abstract IMasterFeatureWrapper<EObject> getReferenceWrapper(IEMFProxyEngine engine,
		EMFProxyObjectImpl proxyObject, EReference reference);
}
