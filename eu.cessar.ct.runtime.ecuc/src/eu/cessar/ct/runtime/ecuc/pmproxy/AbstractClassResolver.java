/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Mar 13, 2010 6:08:28 PM </copyright>
 */
package eu.cessar.ct.runtime.ecuc.pmproxy;

import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.emf.ecore.EStructuralFeature;

import eu.cessar.ct.core.mms.MetaModelUtils;
import eu.cessar.ct.emfproxy.IEMFProxyEngine;
import eu.cessar.ct.emfproxy.IProxyClassResolver;
import eu.cessar.ct.emfproxy.impl.EMFProxyObjectImpl;

/**
 * 
 */
public abstract class AbstractClassResolver<T> extends AbstractProxyWrapperResolver implements
	IProxyClassResolver<T>
{

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.IProxyClassResolver#isValidMasterObject(eu.cessar.ct.emfproxy.IEMFProxyEngine, java.lang.Object, java.lang.Object, java.util.Map)
	 */
	public boolean isValidMasterObject(IEMFProxyEngine engine, Object context, Object object,
		Map<String, Object> parameters)
	{
		if (engine == null || engine.getProject() == null)
		{
			return false;
		}
		if (object == null)
		{
			return false;
		}
		IProject objProject = MetaModelUtils.getProject(object);
		if (objProject == null || objProject == engine.getProject())
		{
			return getMasterClassType(engine).isInstance(object);
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.IProxyClassResolver#resolveMaster(eu.cessar.ct.emfproxy.IEMFProxyEngine, java.lang.Object, java.lang.Object)
	 */
	public T resolveMaster(IEMFProxyEngine engine, Object context, T master)
	{
		// the default implementation just return the master
		return master;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.IProxyClassResolver#getContainingFeature(eu.cessar.ct.emfproxy.internal.EMFProxyStore, eu.cessar.ct.emfproxy.impl.EMFProxyObjectImpl, eu.cessar.ct.emfproxy.impl.EMFProxyObjectImpl)
	 */
	public EStructuralFeature getContainingFeature(IEMFProxyEngine engine,
		EMFProxyObjectImpl parentObject, EMFProxyObjectImpl childObject)
	{
		String featureName = engine.getProxyElementAnnotation(childObject.eClass(),
			ATTR_CONTAINING_FEATURE);
		if (featureName == null)
		{
			return null;
		}
		else
		{
			return parentObject.eClass().getEStructuralFeature(featureName);
		}
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.IProxyClassResolver#postCreateSlave(eu.cessar.ct.emfproxy.IEMFProxyEngine, java.lang.Object, java.lang.Object, eu.cessar.ct.sdk.pm.IEMFProxyObject)
	 */
	public void postCreateSlave(IEMFProxyEngine engine, Object context, T master,
		eu.cessar.ct.sdk.pm.IEMFProxyObject slave)
	{
		// do nothing
	}
}
