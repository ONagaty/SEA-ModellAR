/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * </copyright>
 */
package eu.cessar.ct.compat.internal.pmproxy;

import org.eclipse.emf.ecore.EAttribute;

import eu.cessar.ct.compat.internal.pmproxy.wrap.GInstanceReference_TargetCompatFeatureWrapper;
import eu.cessar.ct.emfproxy.IEMFProxyEngine;
import eu.cessar.ct.emfproxy.IMasterFeatureWrapper;
import eu.cessar.ct.emfproxy.impl.EMFProxyObjectImpl;
import eu.cessar.ct.runtime.ecuc.pmproxy.AbstractEAttributeFeatureResolver;
import eu.cessar.ct.runtime.ecuc.pmproxy.wrap.GInstanceReferenceObjectWrapper;
import gautosar.gecucdescription.GInstanceReferenceValue;

/**
 * @author uidl7321
 * @Review uidl7321 - Apr 11, 2012
 */
public class GInstanceReferenceValue_TargetCompatFeatureResolver extends
	AbstractEAttributeFeatureResolver<Object>
{

	/* (non-Javadoc)
	 * @see eu.cessar.ct.runtime.ecuc.pmproxy.AbstractEAttributeFeatureResolver#getAttributeWrapper(eu.cessar.ct.emfproxy.IEMFProxyEngine, eu.cessar.ct.emfproxy.impl.EMFProxyObjectImpl, org.eclipse.emf.ecore.EAttribute)
	 */
	@SuppressWarnings({"rawtypes", "unchecked"})
	@Override
	protected IMasterFeatureWrapper getAttributeWrapper(IEMFProxyEngine engine,
		EMFProxyObjectImpl proxyObject, EAttribute attribute)
	{
		GInstanceReferenceObjectWrapper wrapper = (GInstanceReferenceObjectWrapper) proxyObject.eGetMasterWrapper();
		GInstanceReferenceValue iRefValue = wrapper.getMasterObject();
		return new GInstanceReference_TargetCompatFeatureWrapper(engine, iRefValue);
	}

}
