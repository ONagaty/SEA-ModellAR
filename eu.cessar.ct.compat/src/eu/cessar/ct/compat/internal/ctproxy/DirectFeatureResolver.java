/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * </copyright>
 */
package eu.cessar.ct.compat.internal.ctproxy;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;

import eu.cessar.ct.compat.internal.ctproxy.convertors.CTProxyConvertorsUtil;
import eu.cessar.ct.compat.internal.ctproxy.convertors.IDataTypeConvertor;
import eu.cessar.ct.compat.internal.ctproxy.wrap.MultiDirectEAttributeWrapper;
import eu.cessar.ct.compat.internal.ctproxy.wrap.MultiDirectEReferenceWrapper;
import eu.cessar.ct.compat.internal.ctproxy.wrap.ShortNameWrapper;
import eu.cessar.ct.compat.internal.ctproxy.wrap.SingleDirectEAttributeWrapper;
import eu.cessar.ct.compat.internal.ctproxy.wrap.SingleDirectEReferenceWrapper;
import eu.cessar.ct.emfproxy.IEMFProxyEngine;
import eu.cessar.ct.emfproxy.IMasterFeatureWrapper;
import eu.cessar.ct.emfproxy.IProxyFeatureResolver;
import eu.cessar.ct.emfproxy.impl.EMFProxyObjectImpl;
import eu.cessar.ct.emfproxy.util.InternalProxyConfigurationError;
import gautosar.ggenericstructure.ginfrastructure.GReferrable;

/**
 * @Review uidl7321 - Apr 11, 2012
 * 
 */
public class DirectFeatureResolver implements IProxyFeatureResolver<Object>
{

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.IProxyFeatureResolver#getFeatureWrapper(eu.cessar.ct.emfproxy.IEMFProxyEngine, eu.cessar.ct.emfproxy.impl.EMFProxyObjectImpl, org.eclipse.emf.ecore.EStructuralFeature)
	 */
	public IMasterFeatureWrapper<Object> getFeatureWrapper(IEMFProxyEngine engine,
		EMFProxyObjectImpl proxyObject, EStructuralFeature slaveFeature)
	{

		// master package and class inherited from the annotation of the owning
		// class
		EObject master = (EObject) proxyObject.eGetMasterWrapper().getAllMasterObjects().get(0);
		EClass masterEClass = master.eClass();

		// get the master feature
		// String annotation = engine.getProxyElementAnnotation(slaveFeature,
		// ICompatConstants.ANN_KEY_AR_FEATURE);
		// String masterFeatureName = (annotation != null) ? annotation :
		// slaveFeature.getName();
		String masterFeatureName = CTProxyUtils.getARFeatureAnnotation(slaveFeature,
			slaveFeature.getName());

		if (master instanceof GReferrable
			&& ("shortName".equals(masterFeatureName) || "gShortName".equals(masterFeatureName))) //$NON-NLS-1$//$NON-NLS-2$
		{
			return new ShortNameWrapper(engine, (GReferrable) master);
		}
		else
		{
			EStructuralFeature masterFeature = masterEClass.getEStructuralFeature(masterFeatureName);
			InternalProxyConfigurationError.assertTrue(masterFeature != null,
				"Cannot retrieve the master feature for the slave object " + proxyObject //$NON-NLS-1$
					+ " and slave feature " + slaveFeature.getName()); //$NON-NLS-1$

			if (masterFeature instanceof EAttribute)
			{
				// EAttribute
				EAttribute masterEAttribute = (EAttribute) masterFeature;
				// map the slave feature class and the master feature class
				Class<?> slaveEAttributeClass = ((EAttribute) slaveFeature).getEAttributeType().getInstanceClass();
				Class<?> masterEAttributeClass = masterEAttribute.getEAttributeType().getInstanceClass();
				@SuppressWarnings("unchecked")
				IDataTypeConvertor<Object, Object> convertor = (IDataTypeConvertor<Object, Object>) CTProxyConvertorsUtil.getConvertor(
					slaveEAttributeClass, masterEAttributeClass);

				if (masterEAttribute.isMany())
				{
					return new MultiDirectEAttributeWrapper(engine, master, masterEAttribute,
						convertor);
				}
				else
				{

					return new SingleDirectEAttributeWrapper(engine, master, masterEAttribute,
						convertor);
				}

			}
			else
			{
				// EReference
				EReference masterEReference = (EReference) masterFeature;
				if (masterEReference.isMany())
				{
					return new MultiDirectEReferenceWrapper(engine, master, masterEReference);
				}
				else
				{
					return new SingleDirectEReferenceWrapper(engine, master, masterEReference);
				}

			}
		}
	}
}
