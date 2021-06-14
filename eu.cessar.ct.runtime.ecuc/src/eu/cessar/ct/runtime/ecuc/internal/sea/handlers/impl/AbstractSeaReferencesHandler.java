/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870<br/>
 * 14.05.2013 09:12:31
 * 
 * </copyright>
 */
package eu.cessar.ct.runtime.ecuc.internal.sea.handlers.impl;

import eu.cessar.ct.runtime.ecuc.internal.sea.handlers.ISeaReferencesHandler;
import eu.cessar.ct.runtime.ecuc.internal.sea.util.MatchingDefinitionHelper;
import eu.cessar.ct.runtime.ecuc.sea.util.SeaUtils;
import eu.cessar.ct.sdk.sea.ISEAContainer;
import eu.cessar.ct.sdk.sea.ISEAModel;
import eu.cessar.ct.sdk.sea.util.ISEAList;
import eu.cessar.ct.sdk.sea.util.ISeaOptions;
import gautosar.gecucdescription.GContainer;
import gautosar.gecucparameterdef.GCommonConfigurationAttributes;
import gautosar.gecucparameterdef.GConfigReference;
import gautosar.ggenericstructure.ginfrastructure.GARObject;

import java.util.Collections;
import java.util.List;

import org.eclipse.core.runtime.Assert;
import org.eclipse.emf.common.util.EList;

/**
 * Base implementation of all Sea handlers meant to operate on reference values
 * 
 * @author uidl6870
 * 
 *         %created_by: uidl6870 %
 * 
 *         %date_created: Thu Aug 21 14:53:53 2014 %
 * 
 *         %version: 7 %
 * @param <T>
 */
public abstract class AbstractSeaReferencesHandler<T> extends AbstractSeaAttributesHandler<GConfigReference> implements
	ISeaReferencesHandler<T>
{
	/**
	 * @param seaModel
	 * @param opStore
	 */
	public AbstractSeaReferencesHandler(ISEAModel seaModel, ISeaOptions opStore)
	{
		super(seaModel, opStore);
	}

	/**
	 * @param parent
	 * @param definition
	 * @return a list with the values for the reference with the provided <code>definition</code> from the given
	 *         <code>parent</code>
	 */
	protected abstract ISEAList<T> getReferences(ISEAContainer parent, GConfigReference definition);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.cessar.ct.runtime.ecuc.internal.sea.ISeaSimpleReferencesHandler#getReference(eu.cessar.ct.sdk.sea.ISEAContainer
	 * , java.lang.String)
	 */
	@Override
	public T getReference(ISEAContainer parent, String defName)
	{
		GConfigReference configReference = getReferenceDefByName(parent, defName);
		if (configReference == null || !assertIsDefinitionOfExpectedType(parent, configReference))
		{
			return null;
		}

		EList<T> list = getReferences(parent, configReference);

		T value = null;
		if (list.size() == 1)
		{
			value = list.get(0);
		}
		else if (list.size() > 1)
		{
			value = handleMultipleValuesFound(parent, defName, list);
		}

		return value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.cessar.ct.runtime.ecuc.internal.sea.ISeaSimpleReferencesHandler#getReferences(eu.cessar.ct.sdk.sea.ISEAContainer
	 * , java.lang.String)
	 */
	@Override
	public ISEAList<T> getReferences(ISEAContainer parent, String defName)
	{
		GConfigReference configReference = getReferenceDefByName(parent, defName);

		if (configReference == null || !assertIsDefinitionOfExpectedType(parent, configReference))
		{
			return SeaUtils.emptyList();
		}

		return getReferences(parent, configReference);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.cessar.ct.runtime.ecuc.internal.sea.handlers.ISeaReferencesHandler#setReference(gautosar.gecucdescription.
	 * GContainer, eu.cessar.ct.sdk.sea.ISEAContainer, java.lang.String, java.lang.Object)
	 */
	public void setReference(GContainer activeContainer, ISEAContainer parent, String defName, T value)
	{
		setReference(activeContainer, parent, defName, Collections.singletonList(value));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.cessar.ct.runtime.ecuc.internal.sea.handlers.ISeaReferencesHandler#setReference(gautosar.gecucdescription.
	 * GContainer, eu.cessar.ct.sdk.sea.ISEAContainer, java.lang.String, java.util.List)
	 */
	@Override
	public void setReference(GContainer activeContainer, ISEAContainer parent, String defName, List<T> values)
	{
		assertNotNull(defName);

		GConfigReference configReference = getReferenceDefByName(parent, defName);
		if (configReference == null || !assertIsDefinitionOfExpectedType(parent, configReference))
		{
			return;
		}

		setReference(activeContainer, parent, configReference, values);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.cessar.ct.runtime.ecuc.internal.sea.handlers.ISeaAttributesHandler#getAcceptedValues(eu.cessar.ct.sdk.sea.
	 * ISEAContainer, gautosar.gecucparameterdef.GCommonConfigurationAttributes, java.util.List)
	 */
	@Override
	public <V> List<V> getAcceptedValues(ISEAContainer parent, GConfigReference definition, List<V> values)
	{
		// TODO:
		if (!checkDestinationType(parent, definition.gGetShortName(), definition, (List<T>) values))
		{
			return Collections.EMPTY_LIST;
		}

		return values;
	}

	/**
	 * @param activeContainer
	 * @param parent
	 * @param configReference
	 * @param values
	 */
	protected void setReference(GContainer activeContainer, ISEAContainer parent, GConfigReference configReference,
		List<T> values)
	{
		checkActiveContainerValidity(activeContainer, parent);

		EList<T> list = getReferences(parent, configReference);
		if (!isModifiable(list))
		{
			return;
		}

		// set value to null
		if (values.size() == 1 && values.get(0) == null)
		{
			unSet(parent, configReference);
		}
		else
		{
			doSetValues(list, parent, activeContainer, isMany(configReference), configReference, values);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.cessar.ct.runtime.ecuc.internal.sea.ISeaSimpleReferencesHandler#isSetReference(eu.cessar.ct.sdk.sea.ISEAContainer
	 * , java.lang.String)
	 */
	@Override
	public boolean isSet(ISEAContainer parent, String defName)
	{
		return getReferences(parent, defName).size() != 0;
	}

	@Override
	public boolean isSet(ISEAContainer parent, GConfigReference definition)
	{
		return getReferences(parent, definition).size() != 0;
	}

	@Override
	public void unSet(ISEAContainer parent, String defName)
	{
		GConfigReference configReference = getReferenceDefByName(parent, defName);

		if (configReference == null || !assertIsDefinitionOfExpectedType(parent, configReference))
		{
			return;
		}
		unSet(parent, configReference);
	}

	@Override
	public void unSet(ISEAContainer parent, GConfigReference definition)
	{
		ISEAList<T> list = getReferences(parent, definition);
		if (!isModifiable(list))
		{
			return;
		}

		list.clear();
	}

	/**
	 * @return the allowed definition types
	 */
	protected abstract List<Class<? extends GCommonConfigurationAttributes>> getAllowedDefinitionTypes();

	/**
	 * Returns the reference definition with specified name from the given parent. Also calls the error handler in case
	 * no such reference exists or severals were found.
	 * 
	 * @param parent
	 * @param defName
	 *        shortName of the searched reference definition
	 * @return the resolved {@link GConfigReference } or <code>null</code>
	 */
	protected GConfigReference getReferenceDefByName(ISEAContainer parent, String defName)
	{
		List<GConfigReference> matchingReferenceDefs = MatchingDefinitionHelper.getMatchingReferenceDefs(
			parent.arGetDefinition(), defName);

		return selectRefDefinition(parent, matchingReferenceDefs, defName);
	}

	/**
	 * @param parent
	 * @param matchingReferenceDefs
	 * @param defName
	 * @return the definition to be used, could be <code>null</code>
	 */
	@SuppressWarnings("unchecked")
	private <D extends GConfigReference> D selectRefDefinition(ISEAContainer parent,
		List<GConfigReference> matchingReferenceDefs, String defName)
	{
		GARObject definition = selectDefinition(parent, matchingReferenceDefs, defName);
		if (definition != null)
		{
			Assert.isTrue(definition instanceof GConfigReference);
		}

		return (D) definition;
	}

	/**
	 * Checks whether the passed <code>definition</code> matches one of the allowed expected types. If not, will call
	 * the error handler
	 * 
	 * @param parent
	 * 
	 * @param definition
	 *        the object to check
	 * 
	 * 
	 * @return <code>true</code> if the definition is of the expected type, <code>false</code> otherwise
	 */
	protected boolean assertIsDefinitionOfExpectedType(ISEAContainer parent, GConfigReference definition)
	{
		boolean valid = false;
		List<Class<? extends GCommonConfigurationAttributes>> types = getAllowedDefinitionTypes();
		for (Class<?> cls: types)
		{
			if (cls.isInstance(definition))
			{
				valid = true;
				break;
			}
		}

		if (!valid)
		{
			getSeaOptionsHolder().getErrorHandler().definitionNotOfExpectedType(parent, definition.gGetShortName(),
				types, definition.getClass());
			return false;
		}

		return true;
	}

}
