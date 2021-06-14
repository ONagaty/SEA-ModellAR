/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Nov 29, 2010 1:58:15 PM </copyright>
 */
package eu.cessar.ct.compat.internal.pmproxy;

import java.util.List;
import java.util.Map;

import org.artop.aal.common.metamodel.AutosarReleaseDescriptor;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;

import eu.cessar.ct.compat.internal.CompatibilitySupport;
import eu.cessar.ct.compat.internal.IModelConstants;
import eu.cessar.ct.core.mms.EObjectLookupUtils;
import eu.cessar.ct.core.mms.MetaModelUtils;
import eu.cessar.ct.emfproxy.EMFProxyRegistry;
import eu.cessar.ct.emfproxy.IEMFProxyEngine;
import eu.cessar.ct.emfproxy.impl.EMFProxyObjectImpl;
import eu.cessar.ct.emfproxy.util.InternalProxyConfigurationError;
import eu.cessar.ct.runtime.ecuc.IEcucPresentationModel;
import eu.cessar.ct.runtime.ecuc.pmproxy.GContainerDefClassResolver;
import eu.cessar.ct.sdk.utils.PMUtils;
import gautosar.gecucparameterdef.GConfigReference;
import gautosar.gecucparameterdef.GParamConfContainerDef;
import gautosar.gecucparameterdef.GReferenceDef;
import gautosar.ggenericstructure.ginfrastructure.GIdentifiable;

/**
 * @author uidl6458
 * @Review uidl7321 - Apr 11, 2012
 */
public class GContainerDefCompatClassResolver extends GContainerDefClassResolver
{

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.cessar.ct.runtime.ecuc.pmproxy.GContainerDefClassResolver#getEClassForProxyContainer(gautosar.ggenericstructure
	 * .ginfrastructure.GIdentifiable, java.util.Map)
	 */
	@Override
	protected EClass getEClassForProxyContainer(IEMFProxyEngine engine, GIdentifiable master,
		Map<String, Object> parameters)
	{
		// check to see if there are enough informations inside the parameters
		// to locate the real EClass
		if (parameters != null)
		{
			Object pmFeatureObj = parameters.get(PARAM_PM_FEATURE);
			Object pmOwnerObj = parameters.get(PARAM_PM_OWNER);
			if (pmFeatureObj instanceof EReference && pmOwnerObj instanceof EMFProxyObjectImpl)
			{
				// get the URI of the owner
				EMFProxyObjectImpl pmOwner = (EMFProxyObjectImpl) pmOwnerObj;
				EReference pmFeature = (EReference) pmFeatureObj;

				String uri = engine.getProxyElementAnnotation(pmOwner.eClass(), ATTR_URI);
				List<EObject> objects = EObjectLookupUtils.getEObjectsWithQName(
					getEcucModel(engine).getResourcesWithModuleDefs(), uri);
				InternalProxyConfigurationError.assertTrue(objects.size() == 1);
				// should not be null
				InternalProxyConfigurationError.assertTrue(objects.get(0) instanceof GParamConfContainerDef);
				// proxyObject is mapped to GModuleConfiguration or GContainer
				GParamConfContainerDef ownerContainer = (GParamConfContainerDef) objects.get(0);
				// locate the reference
				String refName = engine.getProxyElementAnnotation(pmFeature, ATTR_NAME);
				GConfigReference referenceDef = PMUtils.getReferenceDefinition(ownerContainer, refName);
				if (referenceDef instanceof GReferenceDef)
				{
					GReferenceDef refDef = (GReferenceDef) referenceDef;
					IEcucPresentationModel model = getEcucPresentationModel(engine);
					if (refDef.gGetRefDestination() != null && !refDef.gGetRefDestination().eIsProxy())
					{
						return (EClass) model.getPMClassifier(refDef.gGetRefDestination());
					}
				}
			}
		}
		return getEClassForProxyContainer(engine);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.cessar.ct.runtime.ecuc.pmproxy.GContainerDefClassResolver#getEClassForProxyContainer(eu.cessar.ct.emfproxy
	 * .IEMFProxyEngine)
	 */
	@Override
	protected EClass getEClassForProxyContainer(IEMFProxyEngine engine)
	{
		IModelConstants iModelConstants = CompatibilitySupport.getModelConstants(engine.getProject());
		EClass resultClass = iModelConstants.getMissingContainerEClass();

		AutosarReleaseDescriptor autosarRelease = MetaModelUtils.getAutosarRelease(engine.getProject());
		String masterURI = autosarRelease.getNamespace();

		// we have to be sure that the engine is started at this point
		EMFProxyRegistry.eINSTANCE.getEMFProxyEngine(masterURI, iModelConstants.getSlaveMMURI(), engine.getProject());
		return resultClass;
	}
}
