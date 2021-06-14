/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870<br/>
 * 09.05.2013 16:41:24
 * 
 * </copyright>
 */
package eu.cessar.ct.runtime.ecuc.internal.sea.handlers.impl;

import eu.cessar.ct.runtime.ecuc.internal.sea.util.ForeignReferencesElist;
import eu.cessar.ct.runtime.ecuc.util.ESplitableList;
import eu.cessar.ct.sdk.sea.ISEAContainer;
import eu.cessar.ct.sdk.sea.ISEAModel;
import eu.cessar.ct.sdk.sea.util.ISEAList;
import eu.cessar.ct.sdk.sea.util.ISeaOptions;
import gautosar.gecucdescription.GReferenceValue;
import gautosar.gecucparameterdef.GCommonConfigurationAttributes;
import gautosar.gecucparameterdef.GConfigReference;
import gautosar.gecucparameterdef.GForeignReferenceDef;
import gautosar.ggenericstructure.ginfrastructure.GIdentifiable;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EClass;

/**
 * Sea handler for foreign reference values
 * 
 * @author uidl6870
 * 
 *         %created_by: uidl6870 %
 * 
 *         %date_created: Thu Aug 29 16:01:17 2013 %
 * 
 *         %version: 5 %
 */
public class SeaForeignReferencesHandler extends AbstractSeaReferencesHandler<GIdentifiable>
{
	/**
	 * @param seaModel
	 * @param opStore
	 */
	public SeaForeignReferencesHandler(ISEAModel seaModel, ISeaOptions opStore)
	{
		super(seaModel, opStore);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.cessar.ct.runtime.ecuc.internal.sea.AbstractSeaReferencesHandler#checkDestinationType(eu.cessar.ct.sdk.sea
	 * .ISEAContainer, java.lang.String, gautosar.gecucparameterdef.GConfigReference, java.util.List)
	 */
	@Override
	public boolean checkDestinationType(ISEAContainer parent, String defName, GConfigReference configReference,
		List<GIdentifiable> values)
	{
		boolean matchDestination = true;

		String destType = ((GForeignReferenceDef) configReference).gGetDestinationType();
		EClass eClass = getMMService().findEClass(destType);

		if (eClass == null)
		{
			matchDestination = false;
		}
		else
		{
			for (GIdentifiable i: values)
			{
				if (!eClass.isInstance(i))
				{
					matchDestination = false;
					break;
				}
			}
		}
		if (!matchDestination)
		{
			getSeaOptionsHolder().getErrorHandler().valueNotOfDestinationType(parent, defName, eClass, values);
			return false;
		}

		return matchDestination;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.runtime.ecuc.internal.sea.handlers.ISeaReferencesHandler#getReferences(eu.cessar.ct.sdk.sea.
	 * ISEAContainer, gautosar.gecucparameterdef.GConfigReference)
	 */
	@Override
	public ISEAList<GIdentifiable> getReferences(ISEAContainer parent, GConfigReference configReference)
	{
		ESplitableList<GReferenceValue> splitedReferences = getEcucModel().getSplitedReferences(
			parent.arGetContainers(), GReferenceValue.class, configReference);
		ISEAList<GIdentifiable> list = new ForeignReferencesElist(parent, splitedReferences, configReference,
			getSeaOptionsHolder());

		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.runtime.ecuc.internal.sea.AbstractSeaReferencesHandler#getAllowedDefinitionTypes()
	 */
	@Override
	protected List<Class<? extends GCommonConfigurationAttributes>> getAllowedDefinitionTypes()
	{
		List<Class<? extends GCommonConfigurationAttributes>> l = new ArrayList<Class<? extends GCommonConfigurationAttributes>>();
		l.add(GForeignReferenceDef.class);

		return l;
	}

}
