/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 * 
 * </copyright>
 */
package eu.cessar.ct.compat.internal.pmproxy;

import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;

import eu.cessar.ct.compat.internal.pmproxy.wrap.MultiGForeignReferenceValueCompatReferenceWrapper;
import eu.cessar.ct.compat.internal.pmproxy.wrap.SingleGForeignReferenceValueCompatReferenceWrapper;
import eu.cessar.ct.core.mms.EObjectLookupUtils;
import eu.cessar.ct.emfproxy.IEMFProxyEngine;
import eu.cessar.ct.emfproxy.IMasterFeatureWrapper;
import eu.cessar.ct.emfproxy.impl.EMFProxyObjectImpl;
import eu.cessar.ct.emfproxy.util.InternalProxyConfigurationError;
import eu.cessar.ct.runtime.ecuc.IEcucModel;
import eu.cessar.ct.runtime.ecuc.pmproxy.AbstractEReferenceFeatureResolver;
import eu.cessar.ct.runtime.ecuc.pmproxy.wrap.GContainerObjectWrapper;
import eu.cessar.ct.sdk.utils.PMUtils;
import gautosar.gecucdescription.GContainer;
import gautosar.gecucdescription.GReferenceValue;
import gautosar.gecucparameterdef.GConfigReference;
import gautosar.gecucparameterdef.GForeignReferenceDef;
import gautosar.gecucparameterdef.GParamConfContainerDef;

/**
 * @author uidl7321
 * @Review uidl7321 - Apr 11, 2012
 */
public class GForeignReferenceDefCompatFeatureResolver extends AbstractEReferenceFeatureResolver
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
		// GModuleDef realModuleDef = getEcucModel(engine).getModuleDef(
		// containers.get(0).gGetDefinition());

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

		GConfigReference referenceDef = PMUtils.getReferenceDefinition(cntDef, ecucRefName);

		EList<GReferenceValue> references = model.getSplitedReferences(containers, GReferenceValue.class, referenceDef);
		// TODO Auto-generated method stub

		return getReferenceWrapper(engine, reference, references, containers, referenceDef);
	}

	/**
	 * @param reference
	 * @param references
	 * @return
	 */
	protected IMasterFeatureWrapper<EObject> getReferenceWrapper(IEMFProxyEngine engine, EReference reference,
		EList<GReferenceValue> references, List<GContainer> owners, GConfigReference definition)
	{
		GForeignReferenceDef def = (GForeignReferenceDef) definition;
		if (reference.isMany())
		{
			return new MultiGForeignReferenceValueCompatReferenceWrapper(engine, references, def);
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
				return new SingleGForeignReferenceValueCompatReferenceWrapper(engine, fRef, owners, def);
			}
			else
			{
				return new SingleGForeignReferenceValueCompatReferenceWrapper(engine, null, owners, def);
			}
		}
	}

}
