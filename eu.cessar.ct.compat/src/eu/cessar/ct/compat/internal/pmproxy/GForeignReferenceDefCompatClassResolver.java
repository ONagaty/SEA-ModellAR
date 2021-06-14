/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * </copyright>
 */
package eu.cessar.ct.compat.internal.pmproxy;

import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

import eu.cessar.ct.compat.internal.pmproxy.wrap.GForeignReferenceCompatObjectWrapper;
import eu.cessar.ct.core.mms.EObjectLookupUtils;
import eu.cessar.ct.core.mms.MMSRegistry;
import eu.cessar.ct.emfproxy.IEMFProxyEngine;
import eu.cessar.ct.emfproxy.IMasterObjectWrapper;
import eu.cessar.ct.emfproxy.impl.EMFProxyObjectImpl;
import eu.cessar.ct.emfproxy.util.InternalProxyConfigurationError;
import eu.cessar.ct.runtime.ecuc.IEcucPresentationModel;
import eu.cessar.ct.runtime.ecuc.pmproxy.AbstractECUCClassResolver;
import eu.cessar.ct.runtime.ecuc.pmproxy.PMProxyUtils;
import eu.cessar.ct.sdk.pm.IEMFProxyObject;
import gautosar.gecucdescription.GReferenceValue;
import gautosar.gecucparameterdef.GForeignReferenceDef;
import gautosar.gecucparameterdef.GModuleDef;

/**
 * @author uidl7321
 * @Review uidl7321 - Apr 11, 2012
 */
public class GForeignReferenceDefCompatClassResolver extends
	AbstractECUCClassResolver<GReferenceValue>
{

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.IProxyClassResolver#getMasterClassType(eu.cessar.ct.emfproxy.IEMFProxyEngine)
	 */
	public Class<? extends GReferenceValue> getMasterClassType(IEMFProxyEngine engine)
	{
		return GReferenceValue.class;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.IProxyClassResolver#getEClass(eu.cessar.ct.emfproxy.IEMFProxyEngine, java.lang.Object, java.lang.Object, java.util.Map)
	 */
	public EClass getEClass(IEMFProxyEngine engine, Object context, GReferenceValue master,
		Map<String, Object> parameters)
	{
		IEcucPresentationModel model = getEcucPresentationModel(engine);
		GForeignReferenceDef def = (GForeignReferenceDef) master.gGetDefinition();
		if (PMProxyUtils.haveRefinementSupport(engine) && context instanceof GModuleDef)
		{
			GForeignReferenceDef realDef = (GForeignReferenceDef) getEcucModel(engine).getRefinedReferenceDefFamily(
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
	public IMasterObjectWrapper<? extends GReferenceValue> getWrapper(IEMFProxyEngine engine,
		Object context, IEMFProxyObject slave, GReferenceValue master,
		Map<String, Object> parameters)
	{
		return new GForeignReferenceCompatObjectWrapper(engine, master);

	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.IProxyClassResolver#createWrapper(eu.cessar.ct.emfproxy.IEMFProxyEngine, java.lang.Object, eu.cessar.ct.emfproxy.impl.EMFProxyObjectImpl)
	 */
	public IMasterObjectWrapper<? extends GReferenceValue> createWrapper(IEMFProxyEngine engine,
		Object context, EMFProxyObjectImpl emfProxyObjectImpl)
	{
		String uri = engine.getProxyElementAnnotation(emfProxyObjectImpl.eClass(), ATTR_URI);
		List<EObject> objects = EObjectLookupUtils.getEObjectsWithQName(
			getEcucModel(engine).getResourcesWithModuleDefs(), uri);
		InternalProxyConfigurationError.assertTrue(objects.size() == 1);
		// should not be null
		InternalProxyConfigurationError.assertTrue(objects.get(0) instanceof GForeignReferenceDef);
		// proxyObject is mapped to GModuleConfiguration or GContainer
		GForeignReferenceDef def = (GForeignReferenceDef) objects.get(0);

		GReferenceValue refValue = (GReferenceValue) MMSRegistry.INSTANCE.getGenericFactory(
			engine.getProject()).createReferenceValue(def);
		return new GForeignReferenceCompatObjectWrapper(engine, refValue);
	}

}
