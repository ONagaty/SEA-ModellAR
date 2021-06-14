/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870<br/>
 * 14.05.2013 09:07:18
 * 
 * </copyright>
 */
package eu.cessar.ct.runtime.ecuc.internal.sea.handlers.impl;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.EList;

import eu.cessar.ct.runtime.ecuc.internal.sea.util.SimpleReferencesElist;
import eu.cessar.ct.runtime.ecuc.util.ESplitableList;
import eu.cessar.ct.sdk.sea.ISEAContainer;
import eu.cessar.ct.sdk.sea.ISEAModel;
import eu.cessar.ct.sdk.sea.util.ISEAList;
import eu.cessar.ct.sdk.sea.util.ISeaOptions;
import gautosar.gecucdescription.GReferenceValue;
import gautosar.gecucparameterdef.GChoiceReferenceDef;
import gautosar.gecucparameterdef.GCommonConfigurationAttributes;
import gautosar.gecucparameterdef.GConfigReference;
import gautosar.gecucparameterdef.GContainerDef;
import gautosar.gecucparameterdef.GParamConfContainerDef;
import gautosar.gecucparameterdef.GReferenceDef;
import gautosar.ggenericstructure.ginfrastructure.GIdentifiable;

/**
 * Sea handler for simple reference values
 * 
 * @author uidl6870
 * 
 *         %created_by: uidl6870 %
 * 
 *         %date_created: Wed Apr 29 17:08:01 2015 %
 * 
 *         %version: 6 %
 */
public class SeaSimpleReferencesHandler extends AbstractSeaReferencesHandler<ISEAContainer>
{
	/**
	 * @param seaModel
	 * @param opStore
	 */
	public SeaSimpleReferencesHandler(ISEAModel seaModel, ISeaOptions opStore)
	{
		super(seaModel, opStore);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.cessar.ct.runtime.ecuc.internal.sea.AbstractSeaReferencesHandler#checkDestinationType(eu.cessar.ct.sdk.sea
	 * .ISEAContainer, java.lang.String, java.util.List)
	 */
	@Override
	public boolean checkDestinationType(ISEAContainer parent, String defName, GConfigReference configReference,
		List<ISEAContainer> values)
	{
		List<GParamConfContainerDef> allowedDestinations = getAllowedDestinationTypes(configReference);

		boolean valid = true;

		for (ISEAContainer value: values)
		{
			boolean v = false;
			for (GParamConfContainerDef allowedDest: allowedDestinations)
			{
				GParamConfContainerDef definition = value.arGetDefinition();

				List<GIdentifiable> containerDefsFamily = getEcucModel().getRefinedContainerDefsFamily(definition);
				if (containerDefsFamily.contains(allowedDest))
				{
					v = true;
					break;
				}
			}
			if (!v)
			{
				valid = false;
				break;
			}
		}

		if (!valid)
		{
			getSeaOptionsHolder().getErrorHandler().valueNotOfDestinationType(parent, defName, allowedDestinations,
				values);
		}

		return valid;
	}

	/**
	 * @param configReference
	 * @return
	 */
	private static List<GParamConfContainerDef> getAllowedDestinationTypes(GConfigReference configReference)
	{
		List<GParamConfContainerDef> allowedDestinations = new ArrayList<GParamConfContainerDef>();
		if (configReference instanceof GReferenceDef)
		{
			GContainerDef destination = ((GReferenceDef) configReference).gGetRefDestination();
			allowedDestinations.add((GParamConfContainerDef) destination);
		}
		else if (configReference instanceof GChoiceReferenceDef)
		{
			EList<GParamConfContainerDef> destinations = ((GChoiceReferenceDef) configReference).gGetDestinations();
			allowedDestinations = destinations;
		}

		return allowedDestinations;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.runtime.ecuc.internal.sea.handlers.ISeaReferencesHandler#getReferences(eu.cessar.ct.sdk.sea.
	 * ISEAContainer, gautosar.gecucparameterdef.GConfigReference)
	 */
	@Override
	public ISEAList<ISEAContainer> getReferences(ISEAContainer parent, GConfigReference configReference)
	{
		ESplitableList<GReferenceValue> splitedReferences = getEcucModel().getSplitedReferences(
			parent.arGetContainers(), GReferenceValue.class, configReference);

		return new SimpleReferencesElist(parent, getSeaOptionsHolder(), splitedReferences, configReference);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.runtime.ecuc.internal.sea.AbstractSeaReferencesHandler#getAllowedDefinitionTypes()
	 */
	@Override
	protected List<Class<? extends GCommonConfigurationAttributes>> getAllowedDefinitionTypes()
	{
		List<Class<? extends GCommonConfigurationAttributes>> allowed = new ArrayList<Class<? extends GCommonConfigurationAttributes>>();
		allowed.add(GReferenceDef.class);
		allowed.add(GChoiceReferenceDef.class);

		return allowed;
	}

}
