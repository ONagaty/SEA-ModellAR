/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Nov 30, 2009 7:14:05 PM </copyright>
 */
package eu.cessar.ct.runtime.ecuc.pmproxy;

import org.eclipse.emf.ecore.EAttribute;

import eu.cessar.ct.emfproxy.IEMFProxyEngine;
import eu.cessar.ct.emfproxy.IMasterFeatureWrapper;
import eu.cessar.ct.emfproxy.impl.EMFProxyObjectImpl;
import eu.cessar.ct.emfproxy.util.InternalProxyConfigurationError;
import eu.cessar.ct.runtime.ecuc.pmproxy.wrap.INonsplitedSingleEAttributeAccessor;
import eu.cessar.ct.runtime.ecuc.pmproxy.wrap.INonsplitedSingleEAttributeAccessorProvider;
import eu.cessar.ct.runtime.ecuc.pmproxy.wrap.SingleDirectMultiFeatureWrapper;

/**
 * 
 */
public class DirectMultiFeatureResolver extends AbstractEAttributeFeatureResolver<Object>
{

	/* (non-Javadoc)
	 * @see eu.cessar.ct.runtime.ecuc.pmproxy.AbstractEAttributeFeatureResolver#getAttributeWrapper(eu.cessar.ct.emfproxy.IEMFProxyEngine, eu.cessar.ct.emfproxy.impl.EMFProxyObjectImpl, org.eclipse.emf.ecore.EAttribute)
	 */
	@Override
	protected IMasterFeatureWrapper<Object> getAttributeWrapper(IEMFProxyEngine engine,
		EMFProxyObjectImpl proxyObject, EAttribute attribute)
	{
		InternalProxyConfigurationError.assertTrue(!attribute.isMany());
		String targetFeature = engine.getProxyElementAnnotation(attribute, ATTR_TARGET);

		INonsplitedSingleEAttributeAccessorProvider provider = (INonsplitedSingleEAttributeAccessorProvider) proxyObject.eGetMasterWrapper();
		INonsplitedSingleEAttributeAccessor accessor = provider.getAccessor(targetFeature);

		return new SingleDirectMultiFeatureWrapper(engine, accessor);
	}
}
