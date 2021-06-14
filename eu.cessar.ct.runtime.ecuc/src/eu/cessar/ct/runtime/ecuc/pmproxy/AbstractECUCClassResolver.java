/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Jan 4, 2011 4:58:35 PM </copyright>
 */
package eu.cessar.ct.runtime.ecuc.pmproxy;

import java.util.List;

import org.eclipse.emf.ecore.EObject;

import eu.cessar.ct.core.mms.EObjectLookupUtils;
import eu.cessar.ct.core.mms.MetaModelUtils;
import eu.cessar.ct.emfproxy.IEMFProxyEngine;
import eu.cessar.ct.sdk.pm.IEMFProxyObject;
import gautosar.gecucparameterdef.GModuleDef;

/**
 * 
 */
public abstract class AbstractECUCClassResolver<T> extends AbstractClassResolver<T>
{

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.IProxyClassResolver#supportMultiContext(eu.cessar.ct.emfproxy.IEMFProxyEngine)
	 */
	public boolean supportMultiContext(IEMFProxyEngine engine)
	{
		return true;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.IProxyClassResolver#getStandardContext(eu.cessar.ct.emfproxy.IEMFProxyEngine, eu.cessar.ct.sdk.pm.IEMFProxyObject)
	 */
	public Object getStandardContext(IEMFProxyEngine engine, IEMFProxyObject slave)
	{
		String uri = engine.getProxyElementAnnotation(slave.eClass(), ATTR_URI);
		List<EObject> objects = EObjectLookupUtils.getEObjectsWithQName(getEcucModel(engine).getResourcesWithModuleDefs(), uri);
		if (objects.size() > 0)
		{
			// should not be null
			EObject eObject = objects.get(0);
			while (eObject != null && !(eObject instanceof GModuleDef))
			{
				eObject = eObject.eContainer();
			}
			if (eObject == null)
			{
				// should not happen
				return DEFAULT_CONTEXT;
			}
			else
			{
				return eObject;
			}
		}
		else
		{
			return DEFAULT_CONTEXT;
		}
	}

}
