/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Mar 13, 2010 6:59:52 PM </copyright>
 */
package eu.cessar.ct.runtime.ecuc.pmproxy;

import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;

import eu.cessar.ct.emfproxy.IEMFProxyEngine;
import eu.cessar.ct.emfproxy.IMasterFeatureWrapper;
import eu.cessar.ct.emfproxy.impl.EMFProxyObjectImpl;
import eu.cessar.ct.emfproxy.util.InternalProxyConfigurationError;
import eu.cessar.ct.runtime.ecuc.IEcucModel;
import eu.cessar.ct.runtime.ecuc.pmproxy.wrap.GPackageReferenceWrapper;
import gautosar.ggenericstructure.ginfrastructure.GARPackage;

/**
 * 
 */
public class GARPackageFeatureResolver extends AbstractEReferenceFeatureResolver
{

	/* (non-Javadoc)
	 * @see eu.cessar.ct.runtime.ecuc.pmproxy.AbstractEReferenceFeatureResolver#getReferenceWrapper(eu.cessar.ct.emfproxy.IEMFProxyEngine, eu.cessar.ct.emfproxy.impl.EMFProxyObjectImpl, org.eclipse.emf.ecore.EReference)
	 */
	@Override
	protected IMasterFeatureWrapper<EObject> getReferenceWrapper(IEMFProxyEngine engine,
		EMFProxyObjectImpl proxyObject, EReference reference)
	{
		String uri = engine.getProxyElementAnnotation(reference.getEReferenceType(), ATTR_URI);
		InternalProxyConfigurationError.assertTrue(uri != null);
		IEcucModel model = getEcucModel(engine);
		List<GARPackage> packs = model.getSplitedPackagesWithModuleCfg(uri);
		return new GPackageReferenceWrapper(engine, packs);
	}
}
