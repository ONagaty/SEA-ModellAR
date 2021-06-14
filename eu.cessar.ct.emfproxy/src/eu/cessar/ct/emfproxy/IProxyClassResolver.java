/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Nov 30, 2009 9:20:21 AM </copyright>
 */
package eu.cessar.ct.emfproxy;

import java.util.Map;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;

import eu.cessar.ct.emfproxy.impl.EMFProxyObjectImpl;
import eu.cessar.ct.sdk.pm.IEMFProxyObject;

/**
 * @author uidl6458
 * 
 */
public interface IProxyClassResolver<T>
{

	/**
	 * @return
	 */
	public Class<? extends T> getMasterClassType(IEMFProxyEngine engine);

	/**
	 * Return true if the <code>object</code> is a valid master for objects of
	 * this type
	 * 
	 * @param engine
	 * @param master
	 * @param params
	 * @return
	 */
	public boolean isValidMasterObject(IEMFProxyEngine engine, Object context, Object master,
		Map<String, Object> parameters);

	/**
	 * Resolve the master, if necessary and supported. If another master shall
	 * be used instead, like a de-proxified one a non-null value shall be
	 * returned. If the same master shall be used then the <code>master</code>
	 * argument shall be returned.
	 * 
	 * @param engine
	 * @param context
	 * @param master
	 * @return
	 */
	public T resolveMaster(IEMFProxyEngine engine, Object context, T master);

	/**
	 * @param engine
	 * @param master
	 * @return
	 */
	public EClass getEClass(IEMFProxyEngine engine, Object context, T master,
		Map<String, Object> parameters);

	/**
	 * @param engine
	 * @param master
	 * @param params
	 * @return
	 */
	public IMasterObjectWrapper<? extends T> getWrapper(IEMFProxyEngine engine, Object context,
		IEMFProxyObject slave, T master, Map<String, Object> parameters);

	/**
	 * @param proxyEngine
	 * @param emfProxyObjectImpl
	 * @return
	 */
	public IMasterObjectWrapper<? extends T> createWrapper(IEMFProxyEngine engine, Object context,
		EMFProxyObjectImpl emfProxyObjectImpl);

	/**
	 * @param engine
	 * @param master
	 * @param slave
	 */
	public void postCreateSlave(IEMFProxyEngine engine, Object context, T master,
		IEMFProxyObject slave);

	/**
	 * @param emfProxyStore
	 * @param parentObject
	 * @param childObject
	 * @return
	 */
	public EStructuralFeature getContainingFeature(IEMFProxyEngine engine,
		EMFProxyObjectImpl parentObject, EMFProxyObjectImpl childObject);

	/**
	 * Return true if the slave objects constructed by this resolver support
	 * multiple context or false otherwise
	 * 
	 * @param engine
	 * @return
	 */
	public boolean supportMultiContext(IEMFProxyEngine engine);

	/**
	 * Return the standard context of the slave object. If the class resolver
	 * does not support multiple context the result shall be
	 * {@link IEMFProxyEngine#DEFAULT_CONTEXT the default context}
	 * 
	 * @param engine
	 * @param slave
	 * @return
	 */
	public Object getStandardContext(IEMFProxyEngine engine, IEMFProxyObject slave);
}
