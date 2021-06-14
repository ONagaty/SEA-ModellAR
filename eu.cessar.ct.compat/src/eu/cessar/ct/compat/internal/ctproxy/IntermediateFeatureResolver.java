/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * </copyright>
 */
package eu.cessar.ct.compat.internal.ctproxy;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;

import eu.cessar.ct.compat.internal.ctproxy.wrap.SingleIntermediateFeatureWrapper;
import eu.cessar.ct.emfproxy.IEMFProxyEngine;
import eu.cessar.ct.emfproxy.IMasterFeatureWrapper;
import eu.cessar.ct.emfproxy.IProxyFeatureResolver;
import eu.cessar.ct.emfproxy.impl.EMFProxyObjectImpl;
import eu.cessar.ct.emfproxy.util.InternalProxyConfigurationError;

/**
 * @Review uidl7321 - Apr 11, 2012
 * 
 */
public class IntermediateFeatureResolver implements IProxyFeatureResolver<EObject>
{

	public IntermediateFeatureResolver()
	{
		// TODO Auto-generated constructor stub
	}

	public IMasterFeatureWrapper<EObject> getFeatureWrapper(IEMFProxyEngine engine,
		EMFProxyObjectImpl proxyObject, EStructuralFeature slaveFeature)
	{
		EObject master = (EObject) proxyObject.eGetMasterWrapper().getAllMasterObjects().get(0);
		InternalProxyConfigurationError.assertTrue(master != null,
			"Cannot retrieve the master for the slave object " + proxyObject //$NON-NLS-1$
				+ " and slave feature " + slaveFeature.getName()); //$NON-NLS-1$

		String masterFeatureName = engine.getProxyElementAnnotation(slaveFeature,
			ICompatConstants.ANN_KEY_AR_FEATURE);
		if (masterFeatureName == null)
		{
			masterFeatureName = slaveFeature.getName();
		}
		EReference masterFeature = (EReference) master.eClass().getEStructuralFeature(
			masterFeatureName);
		InternalProxyConfigurationError.assertTrue(masterFeature != null,
			"Cannot retrieve the master feature for the slave object " + proxyObject //$NON-NLS-1$
				+ " and slave feature " + slaveFeature.getName()); //$NON-NLS-1$

		if (!slaveFeature.isMany())
		{
			return new SingleIntermediateFeatureWrapper(engine, /*proxyObject,*/
			(EReference) slaveFeature, master, masterFeature);
		}
		return null;
	}
}
