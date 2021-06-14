/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Mar 15, 2010 7:44:34 PM </copyright>
 */
package eu.cessar.ct.emfproxy.impl;

import org.eclipse.emf.ecore.impl.EPackageImpl;

import eu.cessar.ct.emfproxy.IEMFProxyEngine;
import eu.cessar.ct.emfproxy.IProxifiedEPackage;

/**
 * @author uidl6458
 * 
 */
public class ProxifiedEPackageImpl extends EPackageImpl implements IProxifiedEPackage
{

	private IEMFProxyEngine proxyEngine;

	public ProxifiedEPackageImpl(IEMFProxyEngine proxyEngine)
	{
		this.proxyEngine = proxyEngine;
	}

	/**
	 * @return
	 */
	public IEMFProxyEngine eGetProxyEngine()
	{
		return proxyEngine;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.IProxifiedEPackage#getRuntimeClassLoader()
	 */
	public ClassLoader getRuntimeClassLoader()
	{
		return getClass().getClassLoader();
	}

}
