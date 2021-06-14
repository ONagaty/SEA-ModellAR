/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870<br/>
 * 15.05.2013 10:43:40
 * 
 * </copyright>
 */
package eu.cessar.ct.runtime.ecuc.internal.sea.handlers.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.Assert;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

import com.google.common.primitives.Booleans;

import eu.cessar.ct.core.mms.ecuc.convertors.IParameterValueConvertor;
import eu.cessar.ct.core.platform.EExternalRepresentationType;
import eu.cessar.ct.runtime.ecuc.internal.sea.handlers.ISeaParametersHandler;
import eu.cessar.ct.runtime.ecuc.internal.sea.util.MatchingDefinitionHelper;
import eu.cessar.ct.runtime.ecuc.internal.sea.util.ParameterEList;
import eu.cessar.ct.runtime.ecuc.sea.util.SeaUtils;
import eu.cessar.ct.runtime.ecuc.util.DelegatingWithSourceMultiEList;
import eu.cessar.ct.runtime.ecuc.util.ESplitableList;
import eu.cessar.ct.sdk.sea.ISEAContainer;
import eu.cessar.ct.sdk.sea.ISEAModel;
import eu.cessar.ct.sdk.sea.util.ISEAList;
import eu.cessar.ct.sdk.sea.util.ISeaOptions;
import gautosar.gecucdescription.GContainer;
import gautosar.gecucdescription.GParameterValue;
import gautosar.gecucparameterdef.*;
import gautosar.ggenericstructure.ginfrastructure.GARObject;

/**
 * Implementation of {@link ISeaParametersHandler}
 * 
 * @author uidl6870
 * 
 *         %created_by: uidl6870 %
 * 
 *         %date_created: Wed Jun 17 18:43:49 2015 %
 * 
 *         %version: 11 %
 */
public class SeaParametersHandler extends AbstractSeaAttributesHandler<GConfigParameter> implements
	ISeaParametersHandler
{

	/**
	 * @param seaModel
	 * @param opStore
	 */
	public SeaParametersHandler(ISEAModel seaModel, ISeaOptions opStore)
	{
		super(seaModel, opStore);
	}

	@Override
	public boolean isSet(ISEAContainer parent, GConfigParameter paramDef)
	{
		EList<?> list = getParameters(parent, paramDef);

		if (!isModifiable(list))
		{
			return false;
		}

		return list.size() > 0;
	}

	/**
	 * @param parent
	 * @param paramDef
	 * @return the list with parameters of the given definition from the given parent, or an unmodifiable list if the
	 *         provided definition is <code>null </code> or unrecognized
	 */
	private EList<?> getParameters(ISEAContainer parent, GConfigParameter paramDef)
	{
		EList<?> list = null;

		if (paramDef instanceof GBooleanParamDef)
		{
			list = getBooleans(parent, paramDef);
		}
		else if (paramDef instanceof GEnumerationParamDef)
		{
			list = getEnums(parent, paramDef);
		}
		else if (paramDef instanceof GIntegerParamDef)
		{
			list = getIntegers(parent, paramDef);
		}
		else if (paramDef instanceof GFloatParamDef)
		{
			list = getFloats(parent, paramDef);
		}
		else if (paramDef instanceof GAbstractStringParamDef)
		{
			list = getStrings(parent, paramDef);
		}
		else
		{
			list = SeaUtils.emptyList();
		}

		return list;
	}

	/**
	 * Lookups for parameter definition named <code>defName</code> in the given <code>parent</code>. If none or more
	 * than one is found, calls the error handler and asks for a definition to be used in turn.
	 * 
	 * @param parent
	 *        the SEA container wrapper that holds the parameter definition
	 * @param defName
	 *        parameter definition short name
	 * @return the parameter definition to be used, could be <code>null</code>
	 */
	private GConfigParameter getParamDefByName(ISEAContainer parent, String defName)
	{
		List<GConfigParameter> matchingParameterDefs = MatchingDefinitionHelper.getMatchingParameterDefs(
			parent.arGetDefinition(), defName);

		return selectParamDefinition(parent, matchingParameterDefs, defName);
	}

	/**
	 * @param matchingParameterDefs
	 * @param defName
	 * @param mandatory
	 * @return the definition to be used
	 */
	private GConfigParameter selectParamDefinition(ISEAContainer parent, List<GConfigParameter> matchingParameterDefs,
		String defName)
	{
		GARObject definition = selectDefinition(parent, matchingParameterDefs, defName);
		if (definition != null)
		{
			Assert.isTrue(definition instanceof GConfigParameter);
		}

		return (GConfigParameter) definition;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.cessar.ct.runtime.ecuc.internal.sea.handlers.ISeaParametersHandler#unSet(eu.cessar.ct.sdk.sea.ISEAContainer,
	 * gautosar.gecucparameterdef.GConfigParameter)
	 */
	@Override
	public void unSet(ISEAContainer parent, GConfigParameter paramDef)
	{
		EList<?> list = getParameters(parent, paramDef);

		if (!isModifiable(list))
		{
			return;
		}

		list.clear();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.runtime.ecuc.internal.sea.ISeaParametersHandler#getBoolean(eu.cessar.ct.sdk.sea.ISEAContainer,
	 * java.lang.String)
	 */
	@Override
	public Boolean getBoolean(ISEAContainer parent, String defName)
	{
		return getBoolean(parent, defName, null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.runtime.ecuc.internal.sea.ISeaParametersHandler#getInteger(eu.cessar.ct.sdk.sea.ISEAContainer,
	 * java.lang.String)
	 */
	@Override
	public BigInteger getInteger(ISEAContainer parent, String defName)
	{
		return getInteger(parent, defName, null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.runtime.ecuc.internal.sea.ISeaParametersHandler#getFloat(eu.cessar.ct.sdk.sea.ISEAContainer,
	 * java.lang.String)
	 */
	@Override
	public Double getFloat(ISEAContainer parent, String defName)
	{
		return getFloat(parent, defName, null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.runtime.ecuc.internal.sea.ISeaParametersHandler#getEnum(eu.cessar.ct.sdk.sea.ISEAContainer,
	 * java.lang.String)
	 */
	@Override
	public String getEnum(ISEAContainer parent, String defName)
	{
		return getEnum(parent, defName, null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.runtime.ecuc.internal.sea.ISeaParametersHandler#getString(eu.cessar.ct.sdk.sea.ISEAContainer,
	 * java.lang.String)
	 */
	@Override
	public String getString(ISEAContainer parent, String defName)
	{
		return getString(parent, defName, null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.runtime.ecuc.internal.sea.ISeaParametersHandler#getBoolean(eu.cessar.ct.sdk.sea.ISEAContainer,
	 * java.lang.String, java.lang.Boolean)
	 */
	@Override
	public Boolean getBoolean(ISEAContainer parent, String defName, Boolean defaultValue)
	{
		Boolean value = null;
		EList<Boolean> booleans = getBooleans(parent, defName);
		value = getValue(parent, booleans, defaultValue, defName);

		return value;
	}

	/**
	 * Called internally by getter methods (on parameters) that can return only one value.<br>
	 * If <code>values</code> list has one element, the element is simply returned; if empty, <code>defaultValue</code>
	 * is returned; if the list has more than one element, will return whatever the error handler provides.
	 * 
	 * @param parent
	 *        SEA container wrapper on which the getter method was invoked (needed to be passed to the error handler)
	 * @param values
	 *        list whose count is inspected
	 * @param defaultValue
	 *        default value to be returned in case <code>values</code> list is empty
	 * @param defName
	 *        name of the parameter, needed in order to be further passed to the error handler if the case
	 * @return
	 */
	private <T> T getValue(ISEAContainer parent, List<T> values, T defaultValue, String defName)
	{
		T value = null;
		int size = values.size();
		if (size == 0)
		{
			value = defaultValue;
		}
		if (size == 1)
		{
			value = values.get(0);
		}
		else if (size > 1)
		{
			T chosen = handleMultipleValuesFound(parent, defName, values);
			value = chosen;
		}

		return value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.runtime.ecuc.internal.sea.ISeaParametersHandler#getInteger(eu.cessar.ct.sdk.sea.ISEAContainer,
	 * java.lang.String, java.math.BigInteger)
	 */
	@Override
	public BigInteger getInteger(ISEAContainer parent, String defName, BigInteger defaultValue)
	{
		BigInteger value = null;
		EList<BigInteger> integers = getIntegers(parent, defName);
		value = getValue(parent, integers, defaultValue, defName);

		return value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.runtime.ecuc.internal.sea.ISeaParametersHandler#getFloat(eu.cessar.ct.sdk.sea.ISEAContainer,
	 * java.lang.String, java.lang.Double)
	 */
	@Override
	public Double getFloat(ISEAContainer parent, String defName, Double defaultValue)
	{
		Double value = null;
		EList<Double> values = getFloats(parent, defName);
		value = getValue(parent, values, defaultValue, defName);

		return value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.runtime.ecuc.internal.sea.ISeaParametersHandler#getEnum(eu.cessar.ct.sdk.sea.ISEAContainer,
	 * java.lang.String, java.lang.String)
	 */
	@Override
	public String getEnum(ISEAContainer parent, String defName, String defaultValue)
	{
		checkArgument(defName);

		GConfigParameter definition = getParamDefByName(parent, defName);
		if (definition == null)
		{
			return null;
		}

		boolean isDefValid = assertIsDefinitionOfExpectedType(parent, defName, definition.getClass(),
			GEnumerationParamDef.class);
		if (!isDefValid)
		{
			return null;
		}

		GEnumerationParamDef enumDef = (GEnumerationParamDef) definition;
		EList<String> enums = getEnums(parent, enumDef);

		// check provided default value
		String newDefaultValue = checkLiteralValidity(parent, enumDef, defaultValue);

		return getValue(parent, enums, newDefaultValue, defName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.runtime.ecuc.internal.sea.ISeaParametersHandler#getString(eu.cessar.ct.sdk.sea.ISEAContainer,
	 * java.lang.String, java.lang.String)
	 */
	@Override
	public String getString(ISEAContainer parent, String defName, String defaultValue)
	{
		EList<String> list = getStrings(parent, defName);
		return getValue(parent, list, defaultValue, defName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.runtime.ecuc.internal.sea.ISeaParametersHandler#getBooleans(eu.cessar.ct.sdk.sea.ISEAContainer,
	 * java.lang.String)
	 */
	@Override
	public ISEAList<Boolean> getBooleans(ISEAContainer parent, String defName)
	{
		checkArgument(defName);

		GConfigParameter definition = getParamDefByName(parent, defName);
		return getBooleans(parent, definition);
	}

	private ISEAList<Boolean> getBooleans(ISEAContainer parent, GConfigParameter definition)
	{
		return getValuesElist(parent, definition, EExternalRepresentationType.BOOLEAN, Boolean.class,
			GBooleanParamDef.class);
	}

	/**
	 * @param parent
	 *        owner of the parameter values list
	 * @param definition
	 *        parameter definition
	 * @param extType
	 *        external representation type
	 * @param returnedType
	 *        returned type
	 * @param expectedDefClass
	 *        expected definition type
	 * @return a modifiable list
	 */
	@SuppressWarnings("unchecked")
	private <T> ISEAList<T> getValuesElist(ISEAContainer parent, GConfigParameter definition,
		EExternalRepresentationType extType, @SuppressWarnings("unused") Class<T> returnedType,
		Class<? extends GCommonConfigurationAttributes> expectedDefClass)
	{
		if (definition == null
			|| !assertIsDefinitionOfExpectedType(parent, definition.gGetShortName(), definition.getClass(),
				expectedDefClass))
		{
			return SeaUtils.emptyList();
		}

		Class<? extends GParameterValue> paramValueClass = getParamValueClass(extType);
		@SuppressWarnings("rawtypes")
		ESplitableList splitedParameters = getEcucModel().getSplitedParameters(parent.arGetContainers(),
			paramValueClass, definition);
		@SuppressWarnings("rawtypes")
		IParameterValueConvertor converter = getMMService().getEcucMMService().getParameterValueConvertor(definition);

		ISEAList<T> list = new ParameterEList<T, GConfigParameter>(parent, splitedParameters, definition, converter,
			getSeaOptionsHolder());

		return list;
	}

	private Class<? extends GParameterValue> getParamValueClass(EExternalRepresentationType extRepresentation)
	{
		Map<EExternalRepresentationType, Class<? extends GParameterValue>> matrix = getMMService().getEcucMMService().getParameterTypeConversionMatrix();
		return matrix.get(extRepresentation);
	}

	/**
	 * Checks whether the passed <code>definition</code> matches the expected type. If not, will call the error handler
	 * 
	 * @param definition
	 *        the object to check
	 * @param expectedType
	 *        type to check against
	 * 
	 * @return <code>true</code> if the definition is of the expected type, <code>false</code> otherwise
	 */
	private boolean assertIsDefinitionOfExpectedType(ISEAContainer parent, String defName,
		Class<? extends GCommonConfigurationAttributes> actualType,
		Class<? extends GCommonConfigurationAttributes> expectedType)
	{
		if (!expectedType.isAssignableFrom(actualType))
		{
			List<Class<? extends GCommonConfigurationAttributes>> expectedTypes = new ArrayList<Class<? extends GCommonConfigurationAttributes>>();
			expectedTypes.add(expectedType);
			getSeaOptionsHolder().getErrorHandler().definitionNotOfExpectedType(parent, defName, expectedTypes,
				actualType);
			return false;
		}

		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.runtime.ecuc.internal.sea.ISeaParametersHandler#getIntegers(eu.cessar.ct.sdk.sea.ISEAContainer,
	 * java.lang.String)
	 */
	@Override
	public ISEAList<BigInteger> getIntegers(ISEAContainer parent, String defName)
	{
		checkArgument(defName);

		GConfigParameter definition = getParamDefByName(parent, defName);
		return getIntegers(parent, definition);
	}

	private ISEAList<BigInteger> getIntegers(ISEAContainer parent, GConfigParameter definition)
	{
		return getValuesElist(parent, definition, EExternalRepresentationType.INTEGER, BigInteger.class,
			GIntegerParamDef.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.runtime.ecuc.internal.sea.ISeaParametersHandler#getFloats(eu.cessar.ct.sdk.sea.ISEAContainer,
	 * java.lang.String)
	 */
	@Override
	public ISEAList<Double> getFloats(ISEAContainer parent, String defName)
	{
		checkArgument(defName);

		GConfigParameter definition = getParamDefByName(parent, defName);
		return getFloats(parent, definition);
	}

	private ISEAList<Double> getFloats(ISEAContainer parent, GConfigParameter definition)
	{
		return getValuesElist(parent, definition, EExternalRepresentationType.FLOAT, Double.class, GFloatParamDef.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.runtime.ecuc.internal.sea.ISeaParametersHandler#getEnums(eu.cessar.ct.sdk.sea.ISEAContainer,
	 * java.lang.String)
	 */
	@Override
	public ISEAList<String> getEnums(ISEAContainer parent, String defName)
	{
		checkArgument(defName);

		GConfigParameter definition = getParamDefByName(parent, defName);
		return getEnums(parent, definition);
	}

	private ISEAList<String> getEnums(ISEAContainer parent, GConfigParameter definition)
	{
		return getValuesElist(parent, definition, EExternalRepresentationType.ENUM, String.class,
			GEnumerationParamDef.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.runtime.ecuc.internal.sea.ISeaParametersHandler#getStrings(eu.cessar.ct.sdk.sea.ISEAContainer,
	 * java.lang.String)
	 */
	@Override
	public ISEAList<String> getStrings(ISEAContainer parent, String defName)
	{
		checkArgument(defName);

		GConfigParameter definition = getParamDefByName(parent, defName);
		return getStrings(parent, definition);
	}

	private ISEAList<String> getStrings(ISEAContainer parent, GConfigParameter definition)
	{
		Class<? extends GCommonConfigurationAttributes> defClass = null;
		if (definition instanceof GStringParamDef)
		{
			defClass = GStringParamDef.class;
		}
		else if (definition instanceof GFunctionNameDef)
		{
			defClass = GFunctionNameDef.class;
		}
		else if (definition instanceof GLinkerSymbolDef)
		{
			defClass = GLinkerSymbolDef.class;
		}

		if (defClass == null)
		{
			// default expected definition
			defClass = GStringParamDef.class;
		}

		return getValuesElist(parent, definition, EExternalRepresentationType.STRING, String.class, defClass);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.cessar.ct.runtime.ecuc.internal.sea.ISeaParametersHandler#isSetBoolean(eu.cessar.ct.sdk.sea.ISEAContainer,
	 * java.lang.String)
	 */
	@Override
	public boolean isSetBoolean(ISEAContainer parent, String defName)
	{
		return getBooleans(parent, defName).size() > 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.cessar.ct.runtime.ecuc.internal.sea.ISeaParametersHandler#isSetInteger(eu.cessar.ct.sdk.sea.ISEAContainer,
	 * java.lang.String)
	 */
	@Override
	public boolean isSetInteger(ISEAContainer parent, String defName)
	{
		return getIntegers(parent, defName).size() > 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.runtime.ecuc.internal.sea.ISeaParametersHandler#isSetFloat(eu.cessar.ct.sdk.sea.ISEAContainer,
	 * java.lang.String)
	 */
	@Override
	public boolean isSetFloat(ISEAContainer parent, String defName)
	{
		return getFloats(parent, defName).size() > 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.runtime.ecuc.internal.sea.ISeaParametersHandler#isSetEnum(eu.cessar.ct.sdk.sea.ISEAContainer,
	 * java.lang.String)
	 */
	@Override
	public boolean isSetEnum(ISEAContainer parent, String defName)
	{
		return getEnums(parent, defName).size() > 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.runtime.ecuc.internal.sea.ISeaParametersHandler#isSetString(eu.cessar.ct.sdk.sea.ISEAContainer,
	 * java.lang.String)
	 */
	@Override
	public boolean isSetString(ISEAContainer parent, String defName)
	{
		return getStrings(parent, defName).size() > 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.cessar.ct.runtime.ecuc.internal.sea.ISeaParametersHandler#unSetBoolean(eu.cessar.ct.sdk.sea.ISEAContainer,
	 * java.lang.String)
	 */
	@Override
	public void unSetBoolean(ISEAContainer parent, String defName)
	{
		ISEAList<Boolean> list = getBooleans(parent, defName);

		clearIfModifiable(list);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.cessar.ct.runtime.ecuc.internal.sea.ISeaParametersHandler#unSetInteger(eu.cessar.ct.sdk.sea.ISEAContainer,
	 * java.lang.String)
	 */
	@Override
	public void unSetInteger(ISEAContainer parent, String defName)
	{
		ISEAList<BigInteger> list = getIntegers(parent, defName);
		clearIfModifiable(list);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.runtime.ecuc.internal.sea.ISeaParametersHandler#unSetFloat(eu.cessar.ct.sdk.sea.ISEAContainer,
	 * java.lang.String)
	 */
	@Override
	public void unSetFloat(ISEAContainer parent, String defName)
	{
		ISEAList<Double> list = getFloats(parent, defName);
		clearIfModifiable(list);
	}

	private static <E> void clearIfModifiable(ISEAList<E> list)
	{
		if (!isModifiable(list))
		{
			return;
		}

		list.clear();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.runtime.ecuc.internal.sea.ISeaParametersHandler#unSetEnum(eu.cessar.ct.sdk.sea.ISEAContainer,
	 * java.lang.String)
	 */
	@Override
	public void unSetEnum(ISEAContainer parent, String defName)
	{
		ISEAList<String> list = getEnums(parent, defName);
		clearIfModifiable(list);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.runtime.ecuc.internal.sea.ISeaParametersHandler#unSetString(eu.cessar.ct.sdk.sea.ISEAContainer,
	 * java.lang.String)
	 */
	@Override
	public void unSetString(ISEAContainer parent, String defName)
	{
		ISEAList<String> list = getStrings(parent, defName);
		clearIfModifiable(list);
	}

	private <T> void setValues(GContainer activeContainer, ISEAContainer parent, String defName,
		EExternalRepresentationType extType, Class<T> returnedType,
		Class<? extends GCommonConfigurationAttributes> expectedDefClass, T... values)
	{
		checkArgument(defName);

		// compute definition
		GConfigParameter definition = getParamDefByName(parent, defName);

		// obtain live list
		EList<T> list = getValuesElist(parent, definition, extType, returnedType, expectedDefClass);

		if (!isModifiable(list))
		{
			return;
		}
		GContainer actualActiveContainer = activeContainer;

		checkActiveContainerValidity(activeContainer, parent);

		if (activeContainer == null && getSeaOptionsHolder().reuseFragment())
		{
			if (!list.isEmpty())
			{
				actualActiveContainer = getContainerToBeReused(parent, extType, definition);
			}
		}

		int length = values.length;
		if (length == 0 || (length == 1 && values[0] == null))
		{
			unSet(parent, definition);
		}
		else
		{
			doSetValues(list, parent, actualActiveContainer, isMany(definition), definition, Arrays.asList(values));
		}
	}

	/**
	 * @param parent
	 * @param extType
	 * @param definition
	 * @return the fragment that holds all the parameter values with the specified <code>definition</code> if any, else
	 *         <code>null</code>
	 */
	private GContainer getContainerToBeReused(ISEAContainer parent, EExternalRepresentationType extType,
		GConfigParameter definition)
	{
		GContainer activeContainer = null;

		ESplitableList<? extends GParameterValue> splitedParameters = getEcucModel().getSplitedParameters(
			parent.arGetContainers(), getParamValueClass(extType), definition);

		if (splitedParameters.isSplited() && splitedParameters instanceof DelegatingWithSourceMultiEList)
		{
			List<?> parentELists = ((DelegatingWithSourceMultiEList<?>) splitedParameters).getParentELists();
			List<GContainer> fragmentsWithValue = new ArrayList<GContainer>();
			for (Object parentElist: parentELists)
			{
				if (parentElist instanceof List)
				{
					for (Object paramValue: (List<?>) parentElist)
					{
						if (paramValue instanceof GParameterValue)
						{
							if (((GParameterValue) paramValue).gGetDefinition().gGetShortName().equals(
								definition.gGetShortName()))
							{
								EObject eContainer = ((GParameterValue) paramValue).eContainer();
								fragmentsWithValue.add((GContainer) eContainer);
								break;
							}
						}
					}
				}
			}

			if (fragmentsWithValue.size() == 1)
			{
				// one fragment with the parameter value -> reuse it
				activeContainer = fragmentsWithValue.get(0);
			}
		}

		return activeContainer;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.cessar.ct.runtime.ecuc.internal.sea.handlers.ISeaAttributesHandler#getAcceptedValues(eu.cessar.ct.sdk.sea.
	 * ISEAContainer, gautosar.gecucparameterdef.GCommonConfigurationAttributes, java.util.List)
	 */
	@Override
	public <V> List<V> getAcceptedValues(ISEAContainer parent, GConfigParameter definition, List<V> values)
	{
		if (definition instanceof GEnumerationParamDef)
		{
			String[] valuesToBeUsed = checkLiteralsValidity(parent, (GEnumerationParamDef) definition,
				(values.toArray(new String[values.size()])));

			return (List<V>) Arrays.asList(valuesToBeUsed);
		}

		return values;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.cessar.ct.runtime.ecuc.internal.sea.handlers.ISeaParametersHandler#setBoolean(gautosar.gecucdescription.GContainer
	 * , eu.cessar.ct.sdk.sea.ISEAContainer, java.lang.String, boolean)
	 */
	@Override
	public void setBoolean(GContainer activeContainer, ISEAContainer parent, String defName, boolean value)
	{
		setValues(activeContainer, parent, defName, EExternalRepresentationType.BOOLEAN, Boolean.class,
			GBooleanParamDef.class, value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.cessar.ct.runtime.ecuc.internal.sea.handlers.ISeaParametersHandler#setInteger(gautosar.gecucdescription.GContainer
	 * , eu.cessar.ct.sdk.sea.ISEAContainer, java.lang.String, java.math.BigInteger)
	 */
	@Override
	public void setInteger(GContainer activeContainer, ISEAContainer parent, String defName, BigInteger value)
	{
		setValues(activeContainer, parent, defName, EExternalRepresentationType.INTEGER, BigInteger.class,
			GIntegerParamDef.class, value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.cessar.ct.runtime.ecuc.internal.sea.handlers.ISeaParametersHandler#setFloat(gautosar.gecucdescription.GContainer
	 * , eu.cessar.ct.sdk.sea.ISEAContainer, java.lang.String, java.lang.Double)
	 */
	@Override
	public void setFloat(GContainer activeContainer, ISEAContainer parent, String defName, Double value)
	{
		setValues(activeContainer, parent, defName, EExternalRepresentationType.FLOAT, Double.class,
			GFloatParamDef.class, value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.cessar.ct.runtime.ecuc.internal.sea.handlers.ISeaParametersHandler#setEnum(gautosar.gecucdescription.GContainer
	 * , eu.cessar.ct.sdk.sea.ISEAContainer, java.lang.String, java.lang.String)
	 */
	@Override
	public void setEnum(GContainer activeContainer, ISEAContainer parent, String defName, String value)
	{
		setValues(activeContainer, parent, defName, EExternalRepresentationType.ENUM, String.class,
			GEnumerationParamDef.class, value);
	}

	/**
	 * Checks whether given <code>value</code> corresponds to a literal defined by <code>enumDef</code>. If it doesn't,
	 * the error handler is called and asked for a valid value instead. If the provided value is still invalid,
	 * <code>null</code> will be returned.
	 * 
	 * @param parent
	 *        SEA container wrapper, needed to be further passed to the error handler if the case
	 * @param enumDef
	 *        enumeration parameter definition
	 * @param value
	 *        String to be checked against <code>enumDef</code>'s literals
	 * @return a valid literal or <code>null</code>
	 */
	private String checkLiteralValidity(ISEAContainer parent, GEnumerationParamDef enumDef, String value)
	{
		// check the provided literal
		if (isLiteralValid(enumDef, value))
		{
			return value;
		}
		else
		{
			// try to recover
			String toBeUsed = getSeaOptionsHolder().getErrorHandler().enumLiteralNotFound(parent,
				enumDef.gGetShortName(), getLiterals(enumDef), value);
			if (!isLiteralValid(enumDef, toBeUsed))
			{
				return null;
			}
			else
			{
				return toBeUsed;
			}
		}
	}

	private static boolean isLiteralValid(GEnumerationParamDef enumDef, String literalName)
	{
		EList<GEnumerationLiteralDef> literals = enumDef.gGetLiterals();
		boolean found = false;
		for (GEnumerationLiteralDef literal: literals)
		{
			if (literalName == null)
			{
				break;
			}
			else if (literalName.equalsIgnoreCase(literal.gGetShortName()))
			{
				found = true;
				break;
			}
		}

		return found;
	}

	private static List<String> getLiterals(GEnumerationParamDef definition)
	{
		List<String> literals = new ArrayList<String>();

		for (GEnumerationLiteralDef literal: definition.gGetLiterals())
		{
			literals.add(literal.gGetShortName());
		}

		return literals;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.cessar.ct.runtime.ecuc.internal.sea.handlers.ISeaParametersHandler#setString(gautosar.gecucdescription.GContainer
	 * , eu.cessar.ct.sdk.sea.ISEAContainer, java.lang.String, java.lang.String)
	 */
	@Override
	public void setString(GContainer activeContainer, ISEAContainer parent, String defName, String value)
	{
		setValues(activeContainer, parent, defName, EExternalRepresentationType.STRING, String.class,
			GAbstractStringParamDef.class, value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.cessar.ct.runtime.ecuc.internal.sea.handlers.ISeaParametersHandler#setBoolean(gautosar.gecucdescription.GContainer
	 * , eu.cessar.ct.sdk.sea.ISEAContainer, java.lang.String, boolean[])
	 */
	@Override
	public void setBoolean(GContainer activeContainer, ISEAContainer parent, String defName, boolean... values)
	{
		List<Boolean> asList = Booleans.asList(values);

		setValues(activeContainer, parent, defName, EExternalRepresentationType.BOOLEAN, Boolean.class,
			GBooleanParamDef.class, asList.toArray(new Boolean[asList.size()]));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.cessar.ct.runtime.ecuc.internal.sea.handlers.ISeaParametersHandler#setInteger(gautosar.gecucdescription.GContainer
	 * , eu.cessar.ct.sdk.sea.ISEAContainer, java.lang.String, java.math.BigInteger[])
	 */
	@Override
	public void setInteger(GContainer activeContainer, ISEAContainer parent, String defName, BigInteger... values)
	{
		setValues(activeContainer, parent, defName, EExternalRepresentationType.INTEGER, BigInteger.class,
			GIntegerParamDef.class, values);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.cessar.ct.runtime.ecuc.internal.sea.handlers.ISeaParametersHandler#setFloat(gautosar.gecucdescription.GContainer
	 * , eu.cessar.ct.sdk.sea.ISEAContainer, java.lang.String, java.lang.Double[])
	 */
	@Override
	public void setFloat(GContainer activeContainer, ISEAContainer parent, String defName, Double... values)
	{
		setValues(null, parent, defName, EExternalRepresentationType.FLOAT, Double.class, GFloatParamDef.class, values);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.cessar.ct.runtime.ecuc.internal.sea.handlers.ISeaParametersHandler#setEnum(gautosar.gecucdescription.GContainer
	 * , eu.cessar.ct.sdk.sea.ISEAContainer, java.lang.String, java.lang.String[])
	 */
	@Override
	public void setEnum(GContainer activeContainer, ISEAContainer parent, String defName, String... values)
	{
		setValues(activeContainer, parent, defName, EExternalRepresentationType.ENUM, String.class,
			GEnumerationParamDef.class, values);
	}

	/**
	 * Checks whether each String from the given <code>values</code> corresponds to a literal defined by
	 * <code>enumDef</code>. In not, the error handler is called and ask for a valid value instead. If the provided
	 * value(s) are still invalid, an empty array will be returned.
	 * 
	 * @param parent
	 * @param enumDef
	 * @param value
	 * @return
	 */
	private String[] checkLiteralsValidity(ISEAContainer parent, GEnumerationParamDef enumDef, String... values)
	{
		String[] valuesToBeUsed = new String[values.length];

		boolean literalsValid = true;
		for (int i = 0; i < values.length; i++)
		{
			String usedValue = checkLiteralValidity(parent, enumDef, values[i]);
			if (usedValue == null)
			{
				literalsValid = false;
				break;
			}
			valuesToBeUsed[i] = usedValue;
		}

		if (!literalsValid)
		{
			return new String[0];
		}

		return valuesToBeUsed;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.cessar.ct.runtime.ecuc.internal.sea.handlers.ISeaParametersHandler#setString(gautosar.gecucdescription.GContainer
	 * , eu.cessar.ct.sdk.sea.ISEAContainer, java.lang.String, java.lang.String[])
	 */
	@Override
	public void setString(GContainer activeContainer, ISEAContainer parent, String defName, String... values)
	{
		setValues(activeContainer, parent, defName, EExternalRepresentationType.STRING, String.class,
			GAbstractStringParamDef.class, values);
	}

}
