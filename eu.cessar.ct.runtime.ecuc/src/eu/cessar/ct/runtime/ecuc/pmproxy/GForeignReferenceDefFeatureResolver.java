/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Dec 8, 2009 9:04:51 AM </copyright>
 */
package eu.cessar.ct.runtime.ecuc.pmproxy;

import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;

import eu.cessar.ct.core.mms.EObjectLookupUtils;
import eu.cessar.ct.core.platform.util.AbstractDelegationList;
import eu.cessar.ct.core.platform.util.IFilter;
import eu.cessar.ct.emfproxy.IEMFProxyEngine;
import eu.cessar.ct.emfproxy.IMasterFeatureWrapper;
import eu.cessar.ct.emfproxy.impl.EMFProxyObjectImpl;
import eu.cessar.ct.emfproxy.util.InternalProxyConfigurationError;
import eu.cessar.ct.runtime.ecuc.IEcucModel;
import eu.cessar.ct.runtime.ecuc.pmproxy.wrap.GContainerObjectWrapper;
import eu.cessar.ct.runtime.ecuc.pmproxy.wrap.MultiGForeignReferenceValueReferenceWrapper;
import eu.cessar.ct.runtime.ecuc.pmproxy.wrap.SingleGForeignReferenceValueReferenceWrapper;
import eu.cessar.ct.sdk.utils.PMUtils;
import gautosar.gecucdescription.GContainer;
import gautosar.gecucdescription.GReferenceValue;
import gautosar.gecucparameterdef.GConfigReference;
import gautosar.gecucparameterdef.GForeignReferenceDef;
import gautosar.gecucparameterdef.GModuleDef;
import gautosar.gecucparameterdef.GParamConfContainerDef;

/**
 * Feature resolver for all references except instance ref
 */
public class GForeignReferenceDefFeatureResolver extends AbstractEReferenceFeatureResolver
{

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.cessar.ct.runtime.ecuc.pmproxy.AbstractEReferenceFeatureResolver#getReferenceWrapper(eu.cessar.ct.emfproxy
	 * .IEMFProxyEngine, eu.cessar.ct.emfproxy.impl.EMFProxyObjectImpl, org.eclipse.emf.ecore.EReference)
	 */
	@Override
	protected IMasterFeatureWrapper<EObject> getReferenceWrapper(IEMFProxyEngine engine,
		EMFProxyObjectImpl proxyObject, EReference reference)
	{
		GContainerObjectWrapper wrapper = (GContainerObjectWrapper) proxyObject.eGetMasterWrapper();

		List<GContainer> containers = wrapper.getMasterObject();
		GModuleDef realModuleDef = getEcucModel(engine).getModuleDef(containers.get(0).gGetDefinition());

		String contDefURI = engine.getProxyElementAnnotation(proxyObject.eClass(), ATTR_URI);
		InternalProxyConfigurationError.assertTrue(contDefURI != null);

		String ecucRefName = engine.getProxyElementAnnotation(reference, ATTR_NAME);
		InternalProxyConfigurationError.assertTrue(ecucRefName != null);

		List<EObject> cntDefs = EObjectLookupUtils.getEObjectsWithQName(
			getEcucModel(engine).getResourcesWithModuleDefs(), contDefURI);
		InternalProxyConfigurationError.assertTrue(cntDefs.size() == 1);
		InternalProxyConfigurationError.assertTrue(cntDefs.get(0) instanceof GParamConfContainerDef);

		GParamConfContainerDef cntDef = (GParamConfContainerDef) cntDefs.get(0);

		IEcucModel model = getEcucModel(engine);

		if (PMProxyUtils.haveRefinementSupport(engine) && realModuleDef != proxyObject.eGetContext())
		{
			// refinement concept, locate the definition of the corresponding
			// container
			cntDef = (GParamConfContainerDef) model.getRefinedContainerDefFamily(realModuleDef, cntDef);
		}

		GConfigReference referenceDef = PMUtils.getReferenceDefinition(cntDef, ecucRefName);

		EList<GReferenceValue> references = model.getSplitedReferences(containers, GReferenceValue.class, referenceDef);
		if (references instanceof AbstractDelegationList<?>)
		{
			AbstractDelegationList<GReferenceValue> delegation = (AbstractDelegationList<GReferenceValue>) references;
			delegation.addFilter(new IFilter<GReferenceValue>()
			{

				@Override
				public boolean isPassingFilter(GReferenceValue element)
				{
					return element.gGetValue() != null;
				}
			});
		}

		return getReferenceWrapper(engine, reference, references, containers, referenceDef);
	}

	/**
	 * Create the corresponding reference wrapper
	 * 
	 * @param engine
	 * @param reference
	 * @param references
	 * @param owners
	 * @param definition
	 * @return the wrapper
	 */
	@SuppressWarnings("static-method")
	// do not declare as static because it will be override
	protected IMasterFeatureWrapper<EObject> getReferenceWrapper(IEMFProxyEngine engine, EReference reference,
		EList<GReferenceValue> references, List<GContainer> owners, GConfigReference definition)
	{
		GForeignReferenceDef def = (GForeignReferenceDef) definition;
		if (reference.isMany())
		{
			return new MultiGForeignReferenceValueReferenceWrapper(engine, references, def);
		}
		else
		{
			if (references.size() >= 1)
			{
				GReferenceValue fRef = references.get(0);
				if (references.size() > 1)
				{
					engine.getLogger().logMultiplicityWarning(reference, references.size());
				}
				return new SingleGForeignReferenceValueReferenceWrapper(engine, references, fRef, owners, def);
			}
			else
			{
				return new SingleGForeignReferenceValueReferenceWrapper(engine, references, null, owners, def);
			}
		}
	}

}
