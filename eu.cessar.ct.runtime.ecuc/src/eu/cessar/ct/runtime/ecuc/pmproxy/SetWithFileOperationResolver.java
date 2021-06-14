/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidg4020<br/>
 * Jan 26, 2015 2:20:49 PM
 * 
 * </copyright>
 */
package eu.cessar.ct.runtime.ecuc.pmproxy;

import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EParameter;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;

import eu.cessar.ct.emfproxy.IEMFProxyEngine;
import eu.cessar.ct.emfproxy.IMasterFeatureWrapper;
import eu.cessar.ct.emfproxy.IProxyOperationResolver;
import eu.cessar.ct.emfproxy.impl.EMFProxyObjectImpl;
import eu.cessar.ct.emfproxy.util.InternalProxyConfigurationError;
import eu.cessar.ct.runtime.ecuc.IEcucCore;
import eu.cessar.ct.runtime.ecuc.IEcucModel;
import eu.cessar.ct.runtime.ecuc.pmproxy.wrap.Messages;
import eu.cessar.ct.runtime.ecuc.pmproxy.wrap.SingleGParameterValueWrapper;
import eu.cessar.ct.sdk.utils.ModelUtils;
import eu.cessar.req.Requirement;
import gautosar.gecucdescription.GContainer;

/**
 * Operation resolver for the set method with an additional parameter referencing the target resource to write in.
 * 
 * @author uidg4020
 * 
 *         %created_by: uidg4020 %
 * 
 *         %date_created: Wed Feb 18 08:55:53 2015 %
 * 
 *         %version: 4 %
 */
@Requirement(
	reqID = "29641")
public class SetWithFileOperationResolver implements IProxyOperationResolver<Object>
{

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.emfproxy.IProxyOperationResolver#executeOperation(eu.cessar.ct.emfproxy.IEMFProxyEngine,
	 * eu.cessar.ct.emfproxy.impl.EMFProxyObjectImpl, org.eclipse.emf.ecore.EOperation, java.lang.Object[])
	 */
	@Override
	public Object executeOperation(IEMFProxyEngine engine, EMFProxyObjectImpl proxyObject, EOperation operation,
		final Object[] params)
	{

		if (proxyObject == null || engine == null || engine.getProject() == null)
		{
			return null;
		}

		// two parameters expected, the parameter value and the target IFile
		if (params == null || params.length != 2)
		{
			return null;

		}

		if (!(params[1] instanceof IFile))
		{
			return null;
		}
		IFile targetFile = (IFile) params[1];
		EParameter eParameter = operation.getEParameters().get(0);
		if (eParameter == null)
		{
			return null;
		}
		final String featureName = eParameter.getName();
		final EStructuralFeature eStructuralFeature = proxyObject.eClass().getEStructuralFeature(featureName);
		if (eStructuralFeature == null)
		{
			return null;
		}
		List<GContainer> emasterObjects = engine.getMasterObjects(proxyObject);

		for (final GContainer container: emasterObjects)
		{
			Resource definedResource = ModelUtils.getDefinedResource(targetFile);

			if ((definedResource != null) && (definedResource.equals(container.eResource())))
			{

				IEcucModel ecucModel = IEcucCore.INSTANCE.getEcucModel(engine.getProject());
				InternalProxyConfigurationError.assertTrue(ecucModel != null, Messages.NO_ECUC_MODEL_ERROR);

				IMasterFeatureWrapper<?> masterFeatureWrapper = engine.getMasterFeatureWrapper(proxyObject,
					eStructuralFeature, true);

				if (masterFeatureWrapper instanceof SingleGParameterValueWrapper<?, ?, ?>)
				{
					SingleGParameterValueWrapper<?, ?, ?> attributeWrapper = (SingleGParameterValueWrapper<?, ?, ?>) masterFeatureWrapper;

					// first unset value to be sure there will be only one value set for this attribute

					attributeWrapper.unsetValue();
					attributeWrapper.setValue(params[0], definedResource);
				}
			}
		}

		return null;
	}
}
