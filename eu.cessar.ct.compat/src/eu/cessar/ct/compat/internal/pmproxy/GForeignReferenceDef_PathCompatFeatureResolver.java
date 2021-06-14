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

import eu.cessar.ct.compat.internal.pmproxy.wrap.GForeignReferenceCompatObjectWrapper;
import eu.cessar.ct.compat.internal.pmproxy.wrap.GForeignReferenceDef_PathCompatFeatureWrapper;
import eu.cessar.ct.emfproxy.IEMFProxyEngine;
import eu.cessar.ct.emfproxy.IMasterFeatureWrapper;
import eu.cessar.ct.emfproxy.impl.EMFProxyObjectImpl;
import eu.cessar.ct.runtime.ecuc.pmproxy.AbstractEAttributeFeatureResolver;
import gautosar.gecucdescription.GReferenceValue;

/**
 * @author uidl7321
 * @Review uidl7321 - Apr 11, 2012
 */
public class GForeignReferenceDef_PathCompatFeatureResolver extends
	AbstractEAttributeFeatureResolver<Object>
{

	/* (non-Javadoc)
	 * @see eu.cessar.ct.runtime.ecuc.pmproxy.AbstractEAttributeFeatureResolver#getAttributeWrapper(eu.cessar.ct.emfproxy.IEMFProxyEngine, eu.cessar.ct.emfproxy.impl.EMFProxyObjectImpl, org.eclipse.emf.ecore.EAttribute)
	 */
	@SuppressWarnings({"unchecked", "rawtypes"})
	@Override
	protected IMasterFeatureWrapper getAttributeWrapper(IEMFProxyEngine engine,
		EMFProxyObjectImpl proxyObject, EAttribute attribute)
	{
		GForeignReferenceCompatObjectWrapper wrapper = (GForeignReferenceCompatObjectWrapper) proxyObject.eGetMasterWrapper();
		GReferenceValue refValue = wrapper.getMasterObject();
		return new GForeignReferenceDef_PathCompatFeatureWrapper(engine, refValue);
	}

}
