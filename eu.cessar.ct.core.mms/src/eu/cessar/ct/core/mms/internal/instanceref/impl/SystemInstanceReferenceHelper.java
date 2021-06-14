/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870<br/>
 * 05.07.2013 11:20:44
 * 
 * </copyright>
 */
package eu.cessar.ct.core.mms.internal.instanceref.impl;

import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.sphinx.emf.model.IModelDescriptor;

import eu.cessar.ct.core.mms.instanceref.ISystemInstanceReferenceHelper;
import eu.cessar.ct.core.mms.instanceref.InstanceReferenceUtils;
import eu.cessar.ct.core.mms.internal.instanceref.IContextToken;
import eu.cessar.ct.core.mms.internal.instanceref.InstanceRefConfigurationException;
import gautosar.ggenericstructure.ginfrastructure.GIdentifiable;

/**
 */
public class SystemInstanceReferenceHelper extends AbstractInstanceReferenceHelper implements
	ISystemInstanceReferenceHelper

{
	private List<EReference> contextFeatures;
	private EClass instanceRefEClass;

	/**
	 * @param modelDescriptor
	 */
	public SystemInstanceReferenceHelper(IModelDescriptor modelDescriptor)
	{
		super(modelDescriptor);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.core.mms.instanceref.IInstanceReferenceHelper#init(java.lang.Object)
	 */
	@Override
	public void init(EClass input) throws InstanceRefConfigurationException
	{
		instanceRefEClass = input;
		boolean isSystemInstanceRef = getMetaModelService().isSystemInstanceRef(input);

		if (!isSystemInstanceRef)
		{
			throw new IllegalArgumentException("{0} argument is not an instance reference!"); //$NON-NLS-1$
		}

		EReference targetFeature = getMetaModelService().getTargetFeature(input);

		setTargetType((EClass) targetFeature.getEType());

		contextFeatures = getMetaModelService().getContextFeatures(input);

		if (contextFeatures.isEmpty())
		{
			throw new InstanceRefConfigurationException("No candidates for the instance reference could be located"); //$NON-NLS-1$
		}

		int[][] multiplicities = getMetaModelService().getContextFeatureMultiplicities(input, true);

		String[] contextTypes = new String[contextFeatures.size()];

		int i = 0;
		for (EReference ref: contextFeatures)
		{
			String suffix = ""; //$NON-NLS-1$
			if (multiplicities[i][0] == 0 && multiplicities[i][1] == 1)
			{
				suffix += QUESTION;
			}
			else if (multiplicities[i][0] == 0 && multiplicities[i][1] == -1)
			{
				suffix += STAR;
			}
			else if (multiplicities[i][0] == 1 && multiplicities[i][1] == -1)
			{
				suffix += PLUS;
			}

			contextTypes[i] = ref.getEType().getName() + suffix;
			i++;
		}

		init(contextTypes);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.cessar.ct.core.mms.internal.instanceref.impl.AbstractInstanceReferenceHelper#shouldAdd(eu.cessar.ct.core.mms
	 * .internal.instanceref.impl.IContextToken, gautosar.ggenericstructure.ginfrastructure.GIdentifiable)
	 */
	@Override
	protected boolean shouldAdd(IContextToken currentToken, GIdentifiable context)
	{
		if (getRootToken() == currentToken)
		{
			if (getMetaModelService().hasBaseInstanceRef())
			{

				EStructuralFeature baseFeat = instanceRefEClass.getEStructuralFeature(InstanceReferenceUtils.BASE_FEAT_INSTANCEREF);
				if (baseFeat instanceof EReference)
				{
					EClass baseType = (EClass) baseFeat.getEType();
					EObject eContainer = context.eContainer();
					if (eContainer != null && !baseType.isInstance(eContainer))
					{

						return false;
					}
				}
			}
		}

		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.cessar.ct.core.mms.internal.instanceref.impl.AbstractInstanceReferenceHelper#createContextToken(java.lang.
	 * String)
	 */
	@Override
	protected IContextToken createContextToken(int index, String string)
	{
		SystemContextToken token = new SystemContextToken(string, contextFeatures.get(index));
		token.setIndex(index);

		return token;
	}

}
