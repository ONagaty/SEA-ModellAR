/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * </copyright>
 */
package eu.cessar.ct.compat.internal.ctproxy;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;

import eu.cessar.ct.compat.internal.CompatibilitySupport;
import eu.cessar.ct.compat.internal.ctproxy.wrap.MultiEREFWrapper;
import eu.cessar.ct.compat.internal.ctproxy.wrap.SingleEREFWrapper;
import eu.cessar.ct.emfproxy.IEMFProxyEngine;
import eu.cessar.ct.emfproxy.IMasterFeatureWrapper;
import eu.cessar.ct.emfproxy.IProxyFeatureResolver;
import eu.cessar.ct.emfproxy.impl.EMFProxyObjectImpl;
import eu.cessar.ct.emfproxy.util.InternalProxyConfigurationError;

/**
 * @author uidl7321
 * @Review uidl7321 - Apr 11, 2012
 */
public class EREFFeatureResolver implements IProxyFeatureResolver<Object>
{

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.IProxyFeatureResolver#getFeatureWrapper(eu.cessar.ct.emfproxy.IEMFProxyEngine, eu.cessar.ct.emfproxy.impl.EMFProxyObjectImpl, org.eclipse.emf.ecore.EStructuralFeature)
	 */
	public IMasterFeatureWrapper<Object> getFeatureWrapper(IEMFProxyEngine engine,
		EMFProxyObjectImpl proxyObject, EStructuralFeature slaveFeature)
	{

		EObject masterReferringObject = (EObject) proxyObject.eGetMasterWrapper().getAllMasterObjects().get(
			0);

		if (masterReferringObject == null)
		{
			// look into usage
			String usage = CTProxyUtils.getUsageAnnotation(proxyObject.eClass(), null);
			if (usage != null)
			{
				int index = usage.indexOf("#"); //$NON-NLS-1$
				if (index != -1)
				{
					String masterReferringObjectClassName = usage.substring(index + 1);
					EClassifier masterReferringObjectClass = CompatibilitySupport.getModelConstants(
						engine.getProject()).getMasterRootPackage().getEClassifier(
						masterReferringObjectClassName);
					masterReferringObject = CompatibilitySupport.getModelConstants(
						engine.getProject()).getMasterRootFactory().create(
						(EClass) masterReferringObjectClass);
					// only an intermediate object may not have a master set
					engine.updateSlave(ICompatConstants.INTERMEDIATE_CONTEXT, proxyObject,
						masterReferringObject);
				}
			}

		}

		InternalProxyConfigurationError.assertTrue(masterReferringObject != null,
			"Cannot retrieve the master referring object for the slave object " + proxyObject //$NON-NLS-1$
				+ " and slave reference " + slaveFeature.getName()); //$NON-NLS-1$

		// get the full classifier annotation
		String arFullClassifier = CTProxyUtils.getARFullClassifierAnnotation(slaveFeature,
			slaveFeature.getName());
		String[] split = arFullClassifier.split("#"); //$NON-NLS-1$
		InternalProxyConfigurationError.assertTrue(
			split.length == 2,
			"AR_FULL_CLASSIFIER not as expected (package#class) for feature " + slaveFeature.getName() + "of slave object " + proxyObject); //$NON-NLS-1$ //$NON-NLS-2$
		// String packageName = split[0];
		String className = split[1];

		// get the master feature name
		String masterFeatureName = CTProxyUtils.getARFeatureAnnotation(slaveFeature,
			slaveFeature.getName());
		EClass masterEREFClass = CompatibilitySupport.getModelConstants(engine.getProject()).getMasterEClass(
			className);
		// search the master feature
		EStructuralFeature masterFeature = masterReferringObject.eClass().getEStructuralFeature(
			masterFeatureName);

		InternalProxyConfigurationError.assertTrue(masterFeature instanceof EReference,
			"Cannot retrieve the master reference for the slave object " + proxyObject //$NON-NLS-1$
				+ " and slave reference " + slaveFeature.getName()); //$NON-NLS-1$
		InternalProxyConfigurationError.assertTrue(
			masterEREFClass.isSuperTypeOf(((EReference) masterFeature).getEReferenceType()),
			"Master reference" + masterFeatureName + " for the slave object " + proxyObject //$NON-NLS-1$ //$NON-NLS-2$
				+ "not of the expected type " + masterEREFClass.getName()); //$NON-NLS-1$

		if (masterFeature.isMany())
		{
			return new MultiEREFWrapper(engine, masterReferringObject, (EReference) masterFeature);
		}
		else
		{
			return new SingleEREFWrapper(engine, masterReferringObject, (EReference) masterFeature);

		}
	}
}
