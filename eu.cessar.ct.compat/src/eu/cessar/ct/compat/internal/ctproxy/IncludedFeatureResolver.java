/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl7321 Dec 15, 2010 4:10:17 PM </copyright>
 */
package eu.cessar.ct.compat.internal.ctproxy;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

import eu.cessar.ct.compat.internal.CompatibilitySupport;
import eu.cessar.ct.compat.internal.ctproxy.convertors.CTProxyConvertorsUtil;
import eu.cessar.ct.compat.internal.ctproxy.convertors.IDataTypeConvertor;
import eu.cessar.ct.compat.internal.ctproxy.wrap.MultiIncludedEAttributeWrapper;
import eu.cessar.ct.compat.internal.ctproxy.wrap.MultiIncludedEREFWrapper;
import eu.cessar.ct.compat.internal.ctproxy.wrap.MultiIncludedEReferenceWrapper;
import eu.cessar.ct.compat.internal.ctproxy.wrap.SingleIncludedEAttributeWrapper;
import eu.cessar.ct.compat.internal.ctproxy.wrap.SingleIncludedEREFWrapper;
import eu.cessar.ct.compat.internal.ctproxy.wrap.SingleIncludedEReferenceWrapper;
import eu.cessar.ct.emfproxy.IEMFProxyEngine;
import eu.cessar.ct.emfproxy.IMasterFeatureWrapper;
import eu.cessar.ct.emfproxy.IProxyFeatureResolver;
import eu.cessar.ct.emfproxy.impl.EMFProxyObjectImpl;
import eu.cessar.ct.emfproxy.util.InternalProxyConfigurationError;

/**
 * @author uidl7321
 * @Review uidl7321 - Apr 11, 2012
 */
public class IncludedFeatureResolver implements IProxyFeatureResolver<Object>
{

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.IProxyFeatureResolver#getFeatureWrapper(eu.cessar.ct.emfproxy.IEMFProxyEngine, eu.cessar.ct.emfproxy.impl.EMFProxyObjectImpl, org.eclipse.emf.ecore.EStructuralFeature)
	 */
	public IMasterFeatureWrapper<Object> getFeatureWrapper(IEMFProxyEngine engine,
		EMFProxyObjectImpl proxyObject, EStructuralFeature feature)
	{
		EObject master = (EObject) proxyObject.eGetMasterWrapper().getAllMasterObjects().get(0);

		String lvl1MasterFeatureName = engine.getProxyElementAnnotation(feature,
			ICompatConstants.ANN_KEY_AR_FEATURE_1);
		String lvl1MasterExpectedTypeName = engine.getProxyElementAnnotation(feature,
			ICompatConstants.ANN_KEY_AR_FEATURE_CLASS_1);
		EClass lvl1MasterExpectedType = locateArtopEClass(engine, lvl1MasterExpectedTypeName);
		EStructuralFeature lvl1MasterFeature = master.eClass().getEStructuralFeature(
			lvl1MasterFeatureName);

		String lvl2MasterFeatureName = engine.getProxyElementAnnotation(feature,
			ICompatConstants.ANN_KEY_AR_FEATURE_2);
		String lvl2MasterExpectedTypeName = engine.getProxyElementAnnotation(feature,
			ICompatConstants.ANN_KEY_AR_FEATURE_CLASS_2);
		EClass lvl2MasterExpectedType = locateArtopEClass(engine, lvl2MasterExpectedTypeName);
		EStructuralFeature lvl2MasterFeature;
		if (lvl2MasterFeatureName == null)
		{
			lvl2MasterFeature = null; // 1 level include
		}
		else
		{
			lvl2MasterFeature = lvl1MasterExpectedType.getEStructuralFeature(lvl2MasterFeatureName);
		}

		String finalMasterFeatureName = engine.getProxyElementAnnotation(feature,
			ICompatConstants.ANN_KEY_AR_INCLUDED_FEATURE);
		EStructuralFeature finalMasterFeature;
		if (lvl2MasterExpectedType != null)
		{
			finalMasterFeature = lvl2MasterExpectedType.getEStructuralFeature(finalMasterFeatureName);
		}
		else
		{
			finalMasterFeature = lvl1MasterExpectedType.getEStructuralFeature(finalMasterFeatureName);
		}
		if (feature instanceof EAttribute)
		{
			// get also a convertor
			Class<?> slaveAttrClass = ((EAttribute) feature).getEAttributeType().getInstanceClass();
			Class<?> masterAttrClass = ((EAttribute) finalMasterFeature).getEAttributeType().getInstanceClass();
			@SuppressWarnings("unchecked")
			IDataTypeConvertor<Object, Object> convertor = (IDataTypeConvertor<Object, Object>) CTProxyConvertorsUtil.getConvertor(
				slaveAttrClass, masterAttrClass);
			if (feature.isMany())
			{
				return new MultiIncludedEAttributeWrapper(engine, master, lvl1MasterFeature,
					lvl1MasterExpectedType, lvl2MasterFeature, lvl2MasterExpectedType,
					finalMasterFeature, convertor);
			}
			else
			{
				return new SingleIncludedEAttributeWrapper(engine, master, lvl1MasterFeature,
					lvl1MasterExpectedType, lvl2MasterFeature, lvl2MasterExpectedType,
					finalMasterFeature, convertor);
			}
		}
		else
		{
			EClass erefEClass = CompatibilitySupport.getModelConstants(engine.getProject()).getEREFEClass();
			if (feature.isMany())
			{

				if (feature.getEType() == erefEClass)
				{
					return new MultiIncludedEREFWrapper(engine, master, lvl1MasterFeature,
						lvl1MasterExpectedType, finalMasterFeature);
				}
				return new MultiIncludedEReferenceWrapper(engine, master, lvl1MasterFeature,
					lvl1MasterExpectedType, lvl2MasterFeature, lvl2MasterExpectedType,
					finalMasterFeature);
			}
			else
			{
				if (feature.getEType() == erefEClass)
				{
					return new SingleIncludedEREFWrapper(engine, master, lvl1MasterFeature,
						lvl1MasterExpectedType, finalMasterFeature);
				}

				return new SingleIncludedEReferenceWrapper(engine, master, lvl1MasterFeature,
					lvl1MasterExpectedType, lvl2MasterFeature, lvl2MasterExpectedType,
					finalMasterFeature);
			}
		}
	}

	private EClass locateArtopEClass(IEMFProxyEngine engine, String qName)
	{
		if (qName == null)
		{
			return null;
		}
		String[] split = qName.split("#"); //$NON-NLS-1$
		InternalProxyConfigurationError.assertTrue(split.length > 1);
		return CompatibilitySupport.getModelConstants(engine.getProject()).getMasterEClass(split[1]);
	}

}
