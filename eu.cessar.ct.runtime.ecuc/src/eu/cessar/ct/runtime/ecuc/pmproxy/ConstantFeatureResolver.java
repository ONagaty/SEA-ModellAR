/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Nov 30, 2009 5:14:00 PM </copyright>
 */
package eu.cessar.ct.runtime.ecuc.pmproxy;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.util.EcoreUtil;

import eu.cessar.ct.emfproxy.IEMFProxyEngine;
import eu.cessar.ct.emfproxy.IMasterFeatureWrapper;
import eu.cessar.ct.emfproxy.impl.EMFProxyObjectImpl;
import eu.cessar.ct.runtime.ecuc.pmproxy.wrap.PlainSingleFeatureWrapper;

/**
 * 
 */
public class ConstantFeatureResolver extends AbstractEAttributeFeatureResolver<Object>
{

	/* (non-Javadoc)
	 * @see eu.cessar.ct.runtime.ecuc.pmproxy.AbstractEAttributeFeatureResolver#getAttributeWrapper(eu.cessar.ct.emfproxy.IEMFProxyEngine, eu.cessar.ct.emfproxy.impl.EMFProxyObjectImpl, org.eclipse.emf.ecore.EAttribute)
	 */
	@Override
	protected IMasterFeatureWrapper<Object> getAttributeWrapper(IEMFProxyEngine engine,
		EMFProxyObjectImpl proxyObject, EAttribute attribute)
	{
		String string = engine.getProxyElementAnnotation(attribute, ATTR_VALUE);
		Object object = EcoreUtil.createFromString(attribute.getEAttributeType(), string);
		PlainSingleFeatureWrapper<Object> result = new PlainSingleFeatureWrapper<Object>(engine,
			object, true);
		return result;
	}

}
