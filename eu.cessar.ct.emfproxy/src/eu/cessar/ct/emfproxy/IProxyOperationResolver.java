/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Jan 3, 2011 4:36:01 PM </copyright>
 */
package eu.cessar.ct.emfproxy;

import org.eclipse.emf.ecore.EOperation;

import eu.cessar.ct.emfproxy.impl.EMFProxyObjectImpl;

/**
 * @author uidl6458
 * 
 */
public interface IProxyOperationResolver<T>
{

	/**
	 * @param engine
	 * @param proxyObject
	 * @param operation
	 * @param params
	 * @return
	 */
	public Object executeOperation(IEMFProxyEngine engine, EMFProxyObjectImpl proxyObject,
		EOperation operation, Object[] params);

}
