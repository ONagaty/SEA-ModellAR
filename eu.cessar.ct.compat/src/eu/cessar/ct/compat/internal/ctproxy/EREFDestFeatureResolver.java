/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * </copyright>
 */
package eu.cessar.ct.compat.internal.ctproxy;

import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;

import eu.cessar.ct.compat.internal.ctproxy.wrap.SingleEREFDestFeatureWrapper;
import eu.cessar.ct.emfproxy.IEMFProxyEngine;
import eu.cessar.ct.emfproxy.IMasterFeatureWrapper;
import eu.cessar.ct.emfproxy.IProxyFeatureResolver;
import eu.cessar.ct.emfproxy.impl.EMFProxyObjectImpl;

/**
 * @author uidl7321
 * @Review uidl7321 - Apr 11, 2012
 * 
 */
public class EREFDestFeatureResolver implements IProxyFeatureResolver<String>
{

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.IProxyFeatureResolver#getFeatureWrapper(eu.cessar.ct.emfproxy.IEMFProxyEngine, eu.cessar.ct.emfproxy.impl.EMFProxyObjectImpl, org.eclipse.emf.ecore.EStructuralFeature)
	 */
	public IMasterFeatureWrapper<String> getFeatureWrapper(IEMFProxyEngine engine,
		EMFProxyObjectImpl proxyObject, EStructuralFeature feature)
	{
		EObject master = (EObject) proxyObject.eGetMasterWrapper().getAllMasterObjects().get(0);

		EObject masterReferringObject = null;
		EReference masterReference = null;

		Map<String, Object> parameters = proxyObject.eGetMasterWrapper().getParameters();
		if (parameters != null)
		{
			masterReferringObject = (EObject) parameters.get(ICompatConstants.KEY_EREF_MASTER_REFERRING_OBJECT);
			masterReference = (EReference) parameters.get(ICompatConstants.KEY_EREF_MASTER_REFERENCE);
		}

		return new SingleEREFDestFeatureWrapper(engine, master, masterReferringObject,
			masterReference);
	}

}
