/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Dec 8, 2009 9:58:11 AM </copyright>
 */
package eu.cessar.ct.runtime.ecuc.pmproxy;

import eu.cessar.ct.core.mms.EObjectLookupUtils;
import eu.cessar.ct.core.mms.MMSRegistry;
import eu.cessar.ct.core.mms.MetaModelUtils;
import eu.cessar.ct.emfproxy.IEMFProxyEngine;
import eu.cessar.ct.emfproxy.IMasterObjectWrapper;
import eu.cessar.ct.emfproxy.impl.EMFProxyObjectImpl;
import eu.cessar.ct.emfproxy.util.InternalProxyConfigurationError;
import eu.cessar.ct.runtime.ecuc.IEcucPresentationModel;
import eu.cessar.ct.runtime.ecuc.pmproxy.wrap.GInstanceReferenceObjectWrapper;
import eu.cessar.ct.sdk.pm.IEMFProxyObject;
import gautosar.gecucdescription.GInstanceReferenceValue;
import gautosar.gecucparameterdef.GInstanceReferenceDef;
import gautosar.gecucparameterdef.GModuleDef;

import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

/**
 * 
 */
public class GInstanceReferenceDefClassResolver extends
	AbstractECUCClassResolver<GInstanceReferenceValue>
{
	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.IProxyClassResolver#getMasterClassType(eu.cessar.ct.emfproxy.IEMFProxyEngine)
	 */
	public Class<GInstanceReferenceValue> getMasterClassType(IEMFProxyEngine engine)
	{
		// TODO Auto-generated method stub
		return GInstanceReferenceValue.class;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.IProxyClassResolver#getEClass(eu.cessar.ct.emfproxy.IEMFProxyEngine, java.lang.Object, java.lang.Object, java.util.Map)
	 */
	public EClass getEClass(IEMFProxyEngine engine, Object context, GInstanceReferenceValue master,
		Map<String, Object> parameters)
	{
		IEcucPresentationModel model = getEcucPresentationModel(engine);
		GInstanceReferenceDef def = (GInstanceReferenceDef) master.gGetDefinition();
		if (PMProxyUtils.haveRefinementSupport(engine) && context instanceof GModuleDef)
		{
			GInstanceReferenceDef realDef = (GInstanceReferenceDef) getEcucModel(engine).getRefinedReferenceDefFamily(
				(GModuleDef) context, def);
			return (EClass) model.getPMClassifier(realDef);
		}
		else
		{
			return (EClass) model.getPMClassifier(def);
		}
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.IProxyClassResolver#getWrapper(eu.cessar.ct.emfproxy.IEMFProxyEngine, java.lang.Object, eu.cessar.ct.sdk.pm.IEMFProxyObject, java.lang.Object, java.util.Map)
	 */
	public IMasterObjectWrapper<GInstanceReferenceValue> getWrapper(IEMFProxyEngine engine,
		Object context, IEMFProxyObject slave, GInstanceReferenceValue master,
		Map<String, Object> parameters)
	{
		return new GInstanceReferenceObjectWrapper(engine, master);
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.IProxyClassResolver#createWrapper(eu.cessar.ct.emfproxy.IEMFProxyEngine, java.lang.Object, eu.cessar.ct.emfproxy.impl.EMFProxyObjectImpl)
	 */
	public IMasterObjectWrapper<GInstanceReferenceValue> createWrapper(IEMFProxyEngine engine,
		Object context, EMFProxyObjectImpl emfProxyObjectImpl)
	{
		String uri = engine.getProxyElementAnnotation(emfProxyObjectImpl.eClass(), ATTR_URI);
		List<EObject> objects = EObjectLookupUtils.getEObjectsWithQName(getEcucModel(engine).getResourcesWithModuleDefs(), uri);
		InternalProxyConfigurationError.assertTrue(objects.size() == 1);
		// should not be null
		InternalProxyConfigurationError.assertTrue(objects.get(0) instanceof GInstanceReferenceDef);
		// proxyObject is mapped to GModuleConfiguration or GContainer
		GInstanceReferenceDef def = (GInstanceReferenceDef) objects.get(0);

		GInstanceReferenceValue refValue = (GInstanceReferenceValue) MMSRegistry.INSTANCE.getGenericFactory(
			engine.getProject()).createReferenceValue(def);
		return new GInstanceReferenceObjectWrapper(engine, refValue);
	}

}
