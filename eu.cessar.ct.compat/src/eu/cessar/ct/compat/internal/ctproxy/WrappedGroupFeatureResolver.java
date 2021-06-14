/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * </copyright>
 */
package eu.cessar.ct.compat.internal.ctproxy;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;

import eu.cessar.ct.compat.internal.CompatibilitySupport;
import eu.cessar.ct.compat.internal.IModelConstants;
import eu.cessar.ct.compat.internal.ctproxy.wrap.MultiWrappedGroupEREFWrapper;
import eu.cessar.ct.compat.internal.ctproxy.wrap.MultiWrappedGroupEReferenceWrapper;
import eu.cessar.ct.compat.internal.ctproxy.wrap.SingleWrappedGroupEReferenceWrapper;
import eu.cessar.ct.emfproxy.IEMFProxyEngine;
import eu.cessar.ct.emfproxy.IMasterFeatureWrapper;
import eu.cessar.ct.emfproxy.IProxyFeatureResolver;
import eu.cessar.ct.emfproxy.impl.EMFProxyObjectImpl;
import eu.cessar.ct.emfproxy.util.InternalProxyConfigurationError;

/**
 * @author uidl7321
 * @Review uidl7321 - Apr 11, 2012
 */
public class WrappedGroupFeatureResolver implements IProxyFeatureResolver<Object>
{

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.IProxyFeatureResolver#getFeatureWrapper(eu.cessar.ct.emfproxy.IEMFProxyEngine, eu.cessar.ct.emfproxy.impl.EMFProxyObjectImpl, org.eclipse.emf.ecore.EStructuralFeature)
	 */
	public IMasterFeatureWrapper<Object> getFeatureWrapper(IEMFProxyEngine engine,
		EMFProxyObjectImpl proxyObject, EStructuralFeature slaveFeature)
	{
		// search for wrapper class and feature from artop
		String annotation = CTProxyUtils.getWrappedUsageAnnotation(slaveFeature, ""); //$NON-NLS-1$
		InternalProxyConfigurationError.assertTrue(!annotation.equals(""), //$NON-NLS-1$
			"WRAPPED_USAGE not set for " + slaveFeature.getName()); //$NON-NLS-1$

		// can be a list package#class#feature,package#class#feature...
		String[] splitedWrappedUsage = annotation.split(","); //$NON-NLS-1$
		EClass masterWrappedUsageClass;
		EStructuralFeature masterWrappedUsageFeature = null;
		EObject masterWrappedUsageObject = null;
		boolean foundUsage = false;

		for (int i = 0; i < splitedWrappedUsage.length; i++)
		{

			String wrappedUsage = splitedWrappedUsage[i];
			String[] split = wrappedUsage.split("#"); //$NON-NLS-1$
			InternalProxyConfigurationError.assertTrue(
				split.length == 3,
				"WRAPPED_USAGE " + wrappedUsage + " not as expected (package#class#feature) for " + slaveFeature.getName() + "of " + proxyObject); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			String masterWrappedUsageClassName = split[1];
			String masterWrappedUsageFeatureName = split[2];

			masterWrappedUsageObject = (EObject) proxyObject.eGetMasterWrapper().getAllMasterObjects().get(
				0);
			IModelConstants modelConstants = CompatibilitySupport.getModelConstants(engine.getProject());
			if (masterWrappedUsageObject == null)
			{

				masterWrappedUsageObject = modelConstants.getMasterRootFactory().create(
					modelConstants.getMasterEClass(masterWrappedUsageClassName));
				// only an intermediate object may not have a master set
				engine.updateSlave(ICompatConstants.INTERMEDIATE_CONTEXT, proxyObject,
					masterWrappedUsageObject);

			}

			// search for the right usage corresponding to the
			// masterWrappedUsageObject
			masterWrappedUsageClass = modelConstants.getMasterEClass(masterWrappedUsageClassName);
			masterWrappedUsageFeature = masterWrappedUsageClass.getEStructuralFeature(masterWrappedUsageFeatureName);
			if (masterWrappedUsageClass.isSuperTypeOf(masterWrappedUsageObject.eClass()))
			{
				foundUsage = true;
				break;
			}
		}
		InternalProxyConfigurationError.assertTrue(masterWrappedUsageObject != null,
			"Master object that uses the intermediate object " + proxyObject + " is not set"); //$NON-NLS-1$ //$NON-NLS-2$

		InternalProxyConfigurationError.assertTrue(
			foundUsage,
			"None of the master class names from " + annotation + " is the same with expected: " + masterWrappedUsageObject.eClass().getInstanceClassName() + " for proxy object " + proxyObject); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		// check if it's the case of a wrapped group EREF
		EClass erefEClass = CompatibilitySupport.getModelConstants(engine.getProject()).getEREFEClass();

		EList<EReference> eAllReferences = proxyObject.eClass().getEAllReferences();
		for (EReference eReference: eAllReferences)
		{
			if (eReference.getEType() == erefEClass)
			{
				return new MultiWrappedGroupEREFWrapper(engine, proxyObject,
					masterWrappedUsageObject, masterWrappedUsageFeature);
			}
		}

		if (masterWrappedUsageFeature.isMany())
		{
			return new MultiWrappedGroupEReferenceWrapper(engine, proxyObject,
				masterWrappedUsageObject, masterWrappedUsageFeature);
		}
		else
		{
			return new SingleWrappedGroupEReferenceWrapper(engine, proxyObject,
				masterWrappedUsageObject, masterWrappedUsageFeature);
		}
	}
}
