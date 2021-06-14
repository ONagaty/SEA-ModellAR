/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870<br/>
 * 04.06.2013 16:50:09
 * 
 * </copyright>
 */
package eu.cessar.ct.runtime.ecuc.internal.sea.handlers.impl;

import eu.cessar.ct.runtime.ecuc.internal.sea.handlers.ISeaParametersHandler;
import eu.cessar.ct.runtime.ecuc.internal.sea.handlers.ISeaReferencesHandler;
import eu.cessar.ct.runtime.ecuc.internal.sea.handlers.SeaHandlersManager;
import eu.cessar.ct.runtime.ecuc.internal.sea.util.MatchingDefinitionHelper;
import eu.cessar.ct.sdk.sea.ISEAContainer;
import eu.cessar.ct.sdk.sea.ISEAModel;
import eu.cessar.ct.sdk.sea.util.ISeaOptions;
import gautosar.gecucparameterdef.GChoiceReferenceDef;
import gautosar.gecucparameterdef.GCommonConfigurationAttributes;
import gautosar.gecucparameterdef.GConfigParameter;
import gautosar.gecucparameterdef.GConfigReference;
import gautosar.gecucparameterdef.GForeignReferenceDef;
import gautosar.gecucparameterdef.GInstanceReferenceDef;
import gautosar.gecucparameterdef.GReferenceDef;
import gautosar.ggenericstructure.ginfrastructure.GARObject;

import java.util.List;

import org.eclipse.core.runtime.Assert;

/**
 * Sea handler for {@link GCommonConfigurationAttributes}
 * 
 * @author uidl6870
 * 
 *         %created_by: uidl6870 %
 * 
 *         %date_created: Fri Aug 23 15:53:12 2013 %
 * 
 *         %version: 3 %
 */
public class SeaAttributesHandler extends AbstractSeaHandler
{

	/**
	 * @param seaModel
	 * @param opStore
	 */
	public SeaAttributesHandler(ISEAModel seaModel, ISeaOptions opStore)
	{
		super(seaModel, opStore);
	}

	/**
	 * Unsets the {@link GCommonConfigurationAttributes} having the provided <code>defName</code> shortName from the
	 * provided <code>container</code>.
	 * 
	 * @param container
	 *        owner of the reference to be unset
	 * @param defName
	 *        short name of the reference to be unset
	 */
	public void unSet(ISEAContainer container, String defName)
	{
		GCommonConfigurationAttributes definition = getDefinition(container, defName);
		if (definition instanceof GConfigParameter)
		{
			getSeaParametersHandler().unSet(container, (GConfigParameter) definition);
		}
		else if (definition instanceof GConfigReference)
		{
			GConfigReference refDef = (GConfigReference) definition;
			getSeaReferencesHandler(refDef).unSet(container, refDef);
		}
	}

	/**
	 * Returns whether the {@link GCommonConfigurationAttributes} with the specified <code>defName</code> shortName is
	 * set in the given <code>parent</code>
	 * 
	 * @param parent
	 * @param defName
	 *        short name of the reference to be checked whether set
	 * @return whether the specified {@link GCommonConfigurationAttributes} is set in the provided <code>parent</code>
	 */
	public boolean isSet(ISEAContainer parent, String defName)
	{
		GCommonConfigurationAttributes definition = getDefinition(parent, defName);
		if (definition instanceof GConfigParameter)
		{
			return getSeaParametersHandler().isSet(parent, (GConfigParameter) definition);
		}
		else if (definition instanceof GConfigReference)
		{
			GConfigReference refDef = (GConfigReference) definition;
			return getSeaReferencesHandler(refDef).isSet(parent, refDef);
		}

		return false;
	}

	private ISeaParametersHandler getSeaParametersHandler()
	{
		return SeaHandlersManager.getSeaParametersHandler(getSeaModel(), getSeaOptionsHolder());
	}

	/**
	 * @param attrDef
	 * @return the proper {@link ISeaReferencesHandler} to be used for the specified <code>attrDef</code> definition
	 */
	private ISeaReferencesHandler<?> getSeaReferencesHandler(GConfigReference attrDef)
	{
		if (attrDef instanceof GReferenceDef || attrDef instanceof GChoiceReferenceDef)
		{
			return SeaHandlersManager.getSeaSimpleReferencesHandler(getSeaModel(), getSeaOptionsHolder());
		}
		else if (attrDef instanceof GForeignReferenceDef)
		{
			return SeaHandlersManager.getSeaForeignReferencesHandler(getSeaModel(), getSeaOptionsHolder());
		}
		else if (attrDef instanceof GInstanceReferenceDef)
		{
			return SeaHandlersManager.getSeaInstanceReferencesHandler(getSeaModel(), getSeaOptionsHolder());
		}
		return null;
	}

	/**
	 * Searches for the attribute with the <code>defName</code> in the <code>container</code> parent. If none or several
	 * are found, the error handler is invoked and ask for one to be used.
	 * 
	 * @param container
	 *        the container where the attribute is expected to be defined
	 * @param defName
	 *        the name of the attribute
	 * @return the resolved attribute, could be <code>null</code>
	 */
	private GCommonConfigurationAttributes getDefinition(ISEAContainer container, String defName)
	{
		List<? extends GCommonConfigurationAttributes> matchingAttributeDefs = MatchingDefinitionHelper.getMatchingAttributeDefs(
			container.arGetDefinition(), defName);

		GARObject definition = selectDefinition(container, matchingAttributeDefs, defName);
		if (definition != null)
		{
			Assert.isTrue(definition instanceof GCommonConfigurationAttributes);
		}

		return (GCommonConfigurationAttributes) definition;
	}
}
