/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Jan 3, 2011 4:50:27 PM </copyright>
 */
package eu.cessar.ct.runtime.ecuc.pmproxy;

import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EOperation;

import eu.cessar.ct.emfproxy.IEMFProxyEngine;
import eu.cessar.ct.emfproxy.IProxyOperationResolver;
import eu.cessar.ct.emfproxy.impl.EMFProxyObjectImpl;
import eu.cessar.ct.emfproxy.util.InternalProxyConfigurationError;
import eu.cessar.ct.runtime.ecuc.IEcucCore;
import eu.cessar.ct.runtime.ecuc.IEcucModel;
import gautosar.gecucdescription.GContainer;
import gautosar.gecucdescription.GModuleConfiguration;
import gautosar.gecucparameterdef.GContainerDef;
import gautosar.gecucparameterdef.GModuleDef;

/**
 * 
 */
public class IsCompatibleWithOperationResolver implements IProxyOperationResolver<Object>
{

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.IProxyOperationResolver#executeOperation(eu.cessar.ct.emfproxy.IEMFProxyEngine, eu.cessar.ct.emfproxy.impl.EMFProxyObjectImpl, org.eclipse.emf.ecore.EOperation, java.lang.Object[])
	 */
	public Object executeOperation(IEMFProxyEngine engine, EMFProxyObjectImpl proxyObject,
		EOperation operation, Object[] params)
	{
		if (proxyObject == null || engine == null || engine.getProject() == null)
		{
			return Boolean.FALSE;
		}

		// one parameter expected, the qualified name of a module def
		if (params == null || params.length != 1)
		{
			return Boolean.FALSE;

		}

		if (!(params[0] instanceof String))
		{
			return Boolean.FALSE;
		}

		InternalProxyConfigurationError.assertTrue(engine.getMasterObjects(proxyObject).size() >= 1);

		IEcucModel ecucModel = IEcucCore.INSTANCE.getEcucModel(engine.getProject());
		InternalProxyConfigurationError.assertTrue(ecucModel != null);

		// retrieve the module def or owner module def of the element to be
		// compatible with
		String qualifiedName = (String) params[0];
		GModuleDef gModuleDefToBeCompatibileWith = ecucModel.getModuleDef(qualifiedName);

		// master for which to make the compatibility check
		EObject masterObject = (EObject) engine.getMasterObjects(proxyObject).get(0);

		// master object is GContainer or GModuleConfiguration
		if (masterObject instanceof GContainer)
		{
			// search in its family a container def that has as owner the module
			// def 'gModuleDefToBeCompatibileWith'
			GContainerDef gContainerDef = ((GContainer) masterObject).gGetDefinition();
			if (ecucModel.getRefinedContainerDefFamily(gModuleDefToBeCompatibileWith, gContainerDef) != null)
			{
				return Boolean.TRUE;
			}
		}
		else if (masterObject instanceof GModuleConfiguration)
		{
			// search in its family the module def
			// 'gModuleDefToBeCompatibileWith'
			GModuleDef gModuleDefOwner = ((GModuleConfiguration) masterObject).gGetDefinition();
			List<GModuleDef> refinedModuleDefsFamily = ecucModel.getRefinedModuleDefsFamily(gModuleDefOwner);
			if (refinedModuleDefsFamily.contains(gModuleDefToBeCompatibileWith))
			{
				return Boolean.TRUE;
			}
		}
		return Boolean.FALSE;

	}
}
