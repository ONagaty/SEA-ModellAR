/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Nov 30, 2009 4:02:13 PM </copyright>
 */
package eu.cessar.ct.emfproxy;

import org.eclipse.emf.ecore.EStructuralFeature;

import eu.cessar.ct.emfproxy.impl.EMFProxyObjectImpl;

/**
 * @author uidl6458
 * 
 */
public interface IProxyFeatureResolver<T>
{

	/**
	 * @param engine
	 * @param proxyObject
	 * @param feature
	 * @return
	 */
	public IMasterFeatureWrapper<T> getFeatureWrapper(IEMFProxyEngine engine,
		EMFProxyObjectImpl proxyObject, EStructuralFeature feature);

}
