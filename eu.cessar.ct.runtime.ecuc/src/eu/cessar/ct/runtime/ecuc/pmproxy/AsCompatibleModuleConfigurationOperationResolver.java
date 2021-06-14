/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Jan 3, 2011 4:51:42 PM </copyright>
 */
package eu.cessar.ct.runtime.ecuc.pmproxy;

import org.eclipse.emf.ecore.EOperation;

import eu.cessar.ct.emfproxy.IEMFProxyEngine;
import eu.cessar.ct.emfproxy.IProxyOperationResolver;
import eu.cessar.ct.emfproxy.impl.EMFProxyObjectImpl;
import eu.cessar.ct.emfproxy.util.InternalProxyConfigurationError;
import eu.cessar.ct.runtime.ecuc.IEcucCore;
import eu.cessar.ct.runtime.ecuc.IEcucModel;
import gautosar.gecucparameterdef.GModuleDef;

/**
 * 
 */
public class AsCompatibleModuleConfigurationOperationResolver implements
	IProxyOperationResolver<Object>
{

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.IProxyOperationResolver#executeOperation(eu.cessar.ct.emfproxy.IEMFProxyEngine, eu.cessar.ct.emfproxy.impl.EMFProxyObjectImpl, org.eclipse.emf.ecore.EOperation, java.lang.Object[])
	 */
	public Object executeOperation(IEMFProxyEngine engine, EMFProxyObjectImpl proxyObject,
		EOperation operation, Object[] params)
	{
		if (proxyObject == null || engine == null || engine.getProject() == null)
		{
			return null;
		}

		// one parameter expected, the qualified name of a module def
		if (params == null || params.length != 1)
		{
			return null;

		}

		if (!(params[0] instanceof String))
		{
			return null;
		}

		InternalProxyConfigurationError.assertTrue(engine.getMasterObjects(proxyObject).size() >= 1);

		IEcucModel ecucModel = IEcucCore.INSTANCE.getEcucModel(engine.getProject());
		InternalProxyConfigurationError.assertTrue(ecucModel != null);

		// retrieve the module def or owner module def of the element to be
		// compatible with
		String qualifiedName = (String) params[0];
		GModuleDef gModuleDefToBeCompatibileWith = ecucModel.getModuleDef(qualifiedName);

		return engine.getSlaveObject(gModuleDefToBeCompatibileWith,
			engine.getMasterObjects(proxyObject).get(0));
	}
}
