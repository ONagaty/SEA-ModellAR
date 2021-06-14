/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Dec 4, 2009 10:29:29 AM </copyright>
 */
package eu.cessar.ct.runtime.ecuc.pmproxy.params;

import eu.cessar.ct.core.mms.EObjectLookupUtils;
import eu.cessar.ct.core.mms.ecuc.convertors.IParameterValueConvertor;
import eu.cessar.ct.core.platform.util.AbstractDelegationList;
import eu.cessar.ct.core.platform.util.IFilter;
import eu.cessar.ct.emfproxy.IEMFProxyEngine;
import eu.cessar.ct.emfproxy.IMasterFeatureWrapper;
import eu.cessar.ct.emfproxy.IMultiMasterFeatureWrapper;
import eu.cessar.ct.emfproxy.ISingleMasterFeatureWrapper;
import eu.cessar.ct.emfproxy.impl.EMFProxyObjectImpl;
import eu.cessar.ct.emfproxy.util.InternalProxyConfigurationError;
import eu.cessar.ct.runtime.ecuc.IEcucModel;
import eu.cessar.ct.runtime.ecuc.pmproxy.AbstractEAttributeFeatureResolver;
import eu.cessar.ct.runtime.ecuc.pmproxy.PMProxyUtils;
import eu.cessar.ct.runtime.ecuc.pmproxy.wrap.GContainerObjectWrapper;
import eu.cessar.ct.runtime.ecuc.pmproxy.wrap.MissingContainerObjectWrapper;
import eu.cessar.ct.runtime.ecuc.pmproxy.wrap.MultiGParameterValueWrapper;
import eu.cessar.ct.runtime.ecuc.pmproxy.wrap.SingleGParameterValueWrapper;
import eu.cessar.ct.sdk.utils.PMUtils;
import gautosar.gecucdescription.GContainer;
import gautosar.gecucdescription.GParameterValue;
import gautosar.gecucparameterdef.GConfigParameter;
import gautosar.gecucparameterdef.GModuleDef;
import gautosar.gecucparameterdef.GParamConfContainerDef;

import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;

/**
 * Abstract super class for all GConfigParameters
 * 
 * @param <V>
 * @param <D>
 * @param <T>
 * 
 * @author uidl6458
 * 
 *         %created_by: uidl6870 %
 * 
 *         %date_created: Thu Apr 11 10:22:12 2013 %
 * 
 *         %version: 14 %
 */
// SUPPRESS CHECKSTYLE avoid complain about non "Abstract*" name rule
public abstract class GConfigParameterFeatureResolver<V extends GParameterValue, D extends GConfigParameter, T> extends
	AbstractEAttributeFeatureResolver<T>
{

	/**
	 * @param proxyObject
	 * @return
	 */
	private static List<GContainer> getOwnerContainers(EMFProxyObjectImpl proxyObject)
	{
		GContainerObjectWrapper masterWrapper = (GContainerObjectWrapper) proxyObject.eGetMasterWrapper();
		return masterWrapper.getMasterObject();
	}

	/**
	 * @param engine
	 * @param proxyObject
	 * @param attribute
	 * @return
	 */
	private D getParameterDefinition(IEMFProxyEngine engine, EMFProxyObjectImpl proxyObject, EAttribute attribute)
	{
		List<Object> masterObjects = engine.getMasterObjects(proxyObject);
		InternalProxyConfigurationError.assertTrue(masterObjects.size() >= 1);
		InternalProxyConfigurationError.assertTrue(masterObjects.get(0) instanceof GContainer);

		GParamConfContainerDef cntDef = (GParamConfContainerDef) ((GContainer) masterObjects.get(0)).gGetDefinition();
		String ecucAttrName = engine.getProxyElementAnnotation(attribute, ATTR_NAME);

		GModuleDef realModuleDef = getEcucModel(engine).getModuleDef(
			((GContainer) masterObjects.get(0)).gGetDefinition());

		boolean useRefinement = PMProxyUtils.haveRefinementSupport(engine) && realModuleDef != null
			&& realModuleDef != proxyObject.eGetContext();
		if (useRefinement || cntDef == null)
		{
			// look for the cnt def inside the API
			String contDefURI = engine.getProxyElementAnnotation(proxyObject.eClass(), ATTR_URI);

			InternalProxyConfigurationError.assertTrue(contDefURI != null);

			InternalProxyConfigurationError.assertTrue(ecucAttrName != null);

			List<EObject> cntDefs = EObjectLookupUtils.getEObjectsWithQName(
				getEcucModel(engine).getResourcesWithModuleDefs(), contDefURI);
			InternalProxyConfigurationError.assertTrue(cntDefs.size() == 1);
			InternalProxyConfigurationError.assertTrue(cntDefs.get(0) instanceof GParamConfContainerDef);

			cntDef = (GParamConfContainerDef) cntDefs.get(0);

			if (useRefinement)
			{
				// refinement concept, locate the definition of the
				// corresponding
				// container
				cntDef = (GParamConfContainerDef) getEcucModel(engine).getRefinedContainerDefFamily(realModuleDef,
					cntDef);
			}
		}

		GConfigParameter definition = PMUtils.getParameterDefinition(cntDef, ecucAttrName);

		InternalProxyConfigurationError.assertTrue(getParameterDefinitionClass().isInstance(definition));
		@SuppressWarnings("unchecked")
		D paramDef = (D) definition;
		return paramDef;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.cessar.ct.runtime.ecuc.pmproxy.AbstractEAttributeFeatureResolver#getAttributeWrapper(eu.cessar.ct.emfproxy
	 * .IEMFProxyEngine, eu.cessar.ct.emfproxy.impl.EMFProxyObjectImpl, org.eclipse.emf.ecore.EAttribute)
	 */
	@Override
	protected IMasterFeatureWrapper<T> getAttributeWrapper(IEMFProxyEngine engine, EMFProxyObjectImpl proxyObject,
		EAttribute attribute)
	{
		D paramDef = getParameterDefinition(engine, proxyObject, attribute);
		final IParameterValueConvertor<V, D, T> convertor = getConvertor(attribute, paramDef);
		boolean missingContainer = proxyObject.eGetMasterWrapper() instanceof MissingContainerObjectWrapper;
		EList<V> paramValues = null;
		if (!missingContainer)
		{
			List<GContainer> containers = getOwnerContainers(proxyObject);

			IEcucModel model = getEcucModel(engine);

			paramValues = model.getSplitedParameters(containers, getParameterValueClass(), paramDef);
			if (paramValues instanceof AbstractDelegationList<?>)
			{
				// add a filter that will remove from result those that doesn't have values inside
				AbstractDelegationList<V> delegationList = (AbstractDelegationList<V>) paramValues;
				delegationList.addFilter(new IFilter<V>()
				{

					public boolean isPassingFilter(V element)
					{
						return convertor.isSetValue(element);
					}

				});
			}
		}
		if (attribute.isMany())
		{
			return getMultiAttributeWrapper(engine, convertor, paramValues, paramDef, missingContainer);
		}
		else
		{
			return getSingleAttributeWrapper(engine, convertor, paramValues, paramDef, missingContainer);
		}
	}

	/**
	 * @param engine
	 * @param convertor
	 * @param paramValues
	 * @param definition
	 * @param missingContainer
	 * @return the wrapper for multi attributes
	 */
	protected IMultiMasterFeatureWrapper<T> getMultiAttributeWrapper(IEMFProxyEngine engine,
		IParameterValueConvertor<V, D, T> convertor, EList<V> paramValues, D definition, boolean missingContainer)
	{
		return new MultiGParameterValueWrapper<V, D, T>(engine, paramValues, definition, convertor, missingContainer);
	}

	/**
	 * @param engine
	 * @param convertor
	 * @param paramValues
	 * @param definition
	 * @param missingContainer
	 * @return the wrapper for single attributes
	 */
	protected ISingleMasterFeatureWrapper<T> getSingleAttributeWrapper(IEMFProxyEngine engine,
		IParameterValueConvertor<V, D, T> convertor, EList<V> paramValues, D definition, boolean missingContainer)
	{
		return new SingleGParameterValueWrapper<V, D, T>(engine, paramValues, definition, convertor, missingContainer);
	}

	/**
	 * @return the parameter value class
	 */
	protected abstract Class<V> getParameterValueClass();

	/**
	 * @return the parameter definition class
	 */
	protected abstract Class<D> getParameterDefinitionClass();

	/**
	 * @param attribute
	 * @param parameterDefinition
	 * @return the convertor
	 */
	protected abstract IParameterValueConvertor<V, D, T> getConvertor(EAttribute attribute, D parameterDefinition);

}
