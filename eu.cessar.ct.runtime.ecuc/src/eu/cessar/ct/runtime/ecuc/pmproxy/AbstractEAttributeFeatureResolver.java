/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Mar 13, 2010 7:06:34 PM </copyright>
 */
package eu.cessar.ct.runtime.ecuc.pmproxy;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EStructuralFeature;

import eu.cessar.ct.emfproxy.IEMFProxyEngine;
import eu.cessar.ct.emfproxy.IMasterFeatureWrapper;
import eu.cessar.ct.emfproxy.impl.EMFProxyObjectImpl;
import eu.cessar.ct.emfproxy.util.InternalProxyConfigurationError;

/**
 * 
 */
public abstract class AbstractEAttributeFeatureResolver<T> extends AbstractPMFeatureResolver<T>
{

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.IProxyFeatureResolver#getFeatureWrapper(eu.cessar.ct.emfproxy.IEMFProxyEngine, eu.cessar.ct.emfproxy.impl.EMFProxyObjectImpl, org.eclipse.emf.ecore.EStructuralFeature)
	 */
	public IMasterFeatureWrapper<T> getFeatureWrapper(IEMFProxyEngine engine,
		EMFProxyObjectImpl proxyObject, EStructuralFeature feature)
	{
		InternalProxyConfigurationError.assertTrue(feature instanceof EAttribute);
		EAttribute attribute = (EAttribute) feature;
		return getAttributeWrapper(engine, proxyObject, attribute);
	}

	/**
	 * @param engine
	 * @param proxyObject
	 * @param attribute
	 * @return
	 */
	protected abstract IMasterFeatureWrapper<T> getAttributeWrapper(IEMFProxyEngine engine,
		EMFProxyObjectImpl proxyObject, EAttribute attribute);

}
