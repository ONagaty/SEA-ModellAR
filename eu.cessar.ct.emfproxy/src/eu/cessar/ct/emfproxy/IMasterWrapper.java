/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Mar 22, 2010 12:58:33 PM </copyright>
 */
package eu.cessar.ct.emfproxy;

import eu.cessar.ct.emfproxy.impl.EMFProxyObjectImpl;

/**
 * @author uidl6458
 * 
 */
public interface IMasterWrapper
{

	/**
	 * Set the slave object where this wrapper is located
	 * 
	 * @param proxyObject
	 */
	public void setProxyObject(EMFProxyObjectImpl proxyObject);

	/**
	 * Get the slave object where this wrapper is located
	 * 
	 * @return
	 */
	public EMFProxyObjectImpl getProxyObject();

}
