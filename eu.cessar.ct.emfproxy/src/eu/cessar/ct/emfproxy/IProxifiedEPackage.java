/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Mar 15, 2010 7:51:01 PM </copyright>
 */
package eu.cessar.ct.emfproxy;

import org.eclipse.emf.ecore.EPackage;

/**
 * @author uidl6458
 * 
 */
public interface IProxifiedEPackage extends EPackage
{

	/**
	 * @return
	 */
	public IEMFProxyEngine eGetProxyEngine();

	/**
	 * @return
	 */
	public ClassLoader getRuntimeClassLoader();

}
