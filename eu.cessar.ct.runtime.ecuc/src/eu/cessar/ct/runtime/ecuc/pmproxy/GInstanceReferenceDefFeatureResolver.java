/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Mar 15, 2010 11:39:08 AM </copyright>
 */
package eu.cessar.ct.runtime.ecuc.pmproxy;

import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;

import eu.cessar.ct.core.mms.EObjectLookupUtils;
import eu.cessar.ct.core.mms.IEcucMMService;
import eu.cessar.ct.core.mms.MMSRegistry;
import eu.cessar.ct.emfproxy.IEMFProxyEngine;
import eu.cessar.ct.emfproxy.IMasterFeatureWrapper;
import eu.cessar.ct.emfproxy.impl.EMFProxyObjectImpl;
import eu.cessar.ct.emfproxy.util.InternalProxyConfigurationError;
import eu.cessar.ct.runtime.ecuc.IEcucModel;
import eu.cessar.ct.runtime.ecuc.pmproxy.wrap.GContainerObjectWrapper;
import eu.cessar.ct.runtime.ecuc.pmproxy.wrap.MultiGInstanceReferenceValueReferenceWrapper;
import eu.cessar.ct.runtime.ecuc.pmproxy.wrap.SingleGInstanceReferenceValueReferenceWrapper;
import eu.cessar.ct.runtime.ecuc.util.ESplitableList;
import gautosar.gecucdescription.GContainer;
import gautosar.gecucdescription.GInstanceReferenceValue;
import gautosar.gecucparameterdef.GInstanceReferenceDef;
import gautosar.gecucparameterdef.GModuleDef;

/**
 * 
 */
public class GInstanceReferenceDefFeatureResolver extends AbstractEReferenceFeatureResolver
{

	@Override
	protected IMasterFeatureWrapper<EObject> getReferenceWrapper(IEMFProxyEngine engine,
		EMFProxyObjectImpl proxyObject, EReference reference)
	{
		GContainerObjectWrapper wrapper = (GContainerObjectWrapper) proxyObject.eGetMasterWrapper();
		List<GContainer> containers = wrapper.getMasterObject();

		GModuleDef realModuleDef = getEcucModel(engine).getModuleDef(containers.get(0).gGetDefinition());

		String uri = engine.getProxyElementAnnotation(reference.getEReferenceType(), ATTR_URI);
		InternalProxyConfigurationError.assertTrue(uri != null);

		IEcucModel model = getEcucModel(engine);

		List<EObject> defs = EObjectLookupUtils.getEObjectsWithQName(getEcucModel(engine).getResourcesWithModuleDefs(),
			uri);
		InternalProxyConfigurationError.assertTrue(defs.size() == 1);
		// should not be null
		InternalProxyConfigurationError.assertTrue(defs.get(0) instanceof GInstanceReferenceDef);
		// proxyObject is mapped to GModuleConfiguration or GContainer
		GInstanceReferenceDef def = (GInstanceReferenceDef) defs.get(0);

		if (PMProxyUtils.haveRefinementSupport(engine) && realModuleDef != proxyObject.eGetContext())
		{
			// refinement concept, locate the definition of the corresponding
			// reference
			def = (GInstanceReferenceDef) model.getRefinedReferenceDefFamily(realModuleDef, def);
		}
		final IEcucMMService mmService = MMSRegistry.INSTANCE.getMMService(engine.getProject()).getEcucMMService();

		ESplitableList<GInstanceReferenceValue> references = model.getSplitedReferences(containers,
			GInstanceReferenceValue.class, def);

		// All childrens should be Containers, otherwise fail to load
		if (reference.isMany())
		{
			return new MultiGInstanceReferenceValueReferenceWrapper(engine, references, def);
		}
		else
		{
			if (references.size() > 1)
			{
				engine.getLogger().logMultiplicityWarning(reference, references.size());
			}
			return new SingleGInstanceReferenceValueReferenceWrapper(engine, references, containers, def);
		}
	}
}
