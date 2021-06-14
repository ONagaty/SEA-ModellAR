/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Feb 9, 2010 3:07:35 PM </copyright>
 */
package eu.cessar.ct.core.mms.internal;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.sphinx.platform.util.RadixConverter;

import eu.cessar.ct.core.mms.IEcucMMService;
import eu.cessar.ct.core.mms.ecuc.convertors.GBooleanConvertor;
import eu.cessar.ct.core.mms.ecuc.convertors.GFloatConvertor;
import eu.cessar.ct.core.mms.ecuc.convertors.GIntegerConvertor;
import eu.cessar.ct.core.mms.ecuc.convertors.GStringConvertor;
import eu.cessar.ct.core.mms.ecuc.convertors.IParameterValueConvertor;
import eu.cessar.ct.core.mms.ecuc.convertors.SeaEnumConvetor;
import eu.cessar.ct.core.platform.EExternalRepresentationType;
import gautosar.gecucdescription.GBooleanValue;
import gautosar.gecucdescription.GContainer;
import gautosar.gecucdescription.GEnumerationValue;
import gautosar.gecucdescription.GFloatValue;
import gautosar.gecucdescription.GIntegerValue;
import gautosar.gecucdescription.GModuleConfiguration;
import gautosar.gecucdescription.GParameterValue;
import gautosar.gecucdescription.GStringValue;
import gautosar.gecucparameterdef.*;

/**
 * @author uidl6458
 * 
 */
public abstract class GenericEcucMMService implements IEcucMMService
{

	private static final String STAR = "*"; //$NON-NLS-1$

	/**
	 * conversion matrix between parameter value type and its external representation
	 */
	protected Map<EExternalRepresentationType, Class<? extends GParameterValue>> conversionMatrix = new HashMap<EExternalRepresentationType, Class<? extends GParameterValue>>();

	/**
	 * Return the multiplicity value as a {@code BigInteger}.
	 * 
	 * @param value
	 *        the value as a {@code String}
	 * @param defaultValue
	 *        the supplied default value
	 * @return the value as a {@code BigInteger}
	 */
	public static BigInteger convertMultiplicity(String value, BigInteger defaultValue)
	{
		BigInteger result = null;

		if (value == null)
		{
			result = defaultValue;
		}
		else if (STAR.equals(value))
		{
			result = MULTIPLICITY_STAR;
		}
		else
		{
			// try to convert
			int radix = RadixConverter.getRadix(value);
			if (radix != 0)
			{
				try
				{
					result = new BigInteger(value, radix);
				}
				catch (NumberFormatException e)
				{
					result = defaultValue;
				}
			}
			else
			{
				result = defaultValue;
			}
		}

		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.core.mms.IEcucMMService#isValidLowerMultiplicity(int,
	 * gautosar.gecucparameterdef.GParamConfMultiplicity)
	 */
	public boolean isValidLowerMultiplicity(int numberOfObjects, GParamConfMultiplicity gParamConfMultiplicity)
	{
		Assert.isNotNull(gParamConfMultiplicity);

		BigInteger lowerMultiplicity = getLowerMultiplicity(gParamConfMultiplicity, BigInteger.ONE, true);
		return numberOfObjects >= lowerMultiplicity.intValue();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.core.mms.IEcucMMService#getLowerMultiplicity(gautosar.gecucparameterdef.GParamConfMultiplicity,
	 * java.math.BigInteger, boolean)
	 */
	public BigInteger getLowerMultiplicity(GParamConfMultiplicity multiplicity, BigInteger defaultValue,
		boolean ecucVariant)
	{
		String value = multiplicity.gGetLowerMultiplicityAsString();
		return convertMultiplicity(value, defaultValue);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.core.mms.IEcucMMService#isValidUpperMultiplicity(int,
	 * gautosar.gecucparameterdef.GParamConfMultiplicity)
	 */
	public boolean isValidUpperMultiplicity(int numberOfObjects, GParamConfMultiplicity gParamConfMultiplicity)
	{
		Assert.isNotNull(gParamConfMultiplicity);

		if (gParamConfMultiplicity.gGetUpperMultiplicityInfinite())
		{
			return true;
		}
		BigInteger upperMultiplicity = getUpperMultiplicity(gParamConfMultiplicity, BigInteger.ONE, true);

		if (upperMultiplicity.compareTo(MULTIPLICITY_STAR) != 0)
		{
			return numberOfObjects <= upperMultiplicity.intValue();
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.core.mms.IEcucMMService#getUpperMultiplicity(gautosar.gecucparameterdef.GParamConfMultiplicity,
	 * java.math.BigInteger, boolean)
	 */
	public BigInteger getUpperMultiplicity(GParamConfMultiplicity multiplicity, BigInteger defaultValue,
		boolean ecucVariant)
	{
		String value = multiplicity.gGetUpperMultiplicityAsString();
		return convertMultiplicity(value, defaultValue);
	}

	protected IStatus createErrorStatus(int severity, String message)
	{
		return new Status(IStatus.ERROR, CessarPluginActivator.PLUGIN_ID, severity, message, null);
		// return new Status(severity, Activator.PLUGIN_ID, message);
	}

	/**
	 * @param config
	 *        the
	 * @return returns <code>true</code> if the given objDef is the definition of objInstance, <br>
	 *         <code>false</code> otherwise
	 */
	public IStatus validateDefinition(GModuleConfiguration config)
	{
		IStatus status = null;

		if (config == null)
		{
			status = createErrorStatus(ERROR_ELEMENT_NULL, Messages.VALIDATION_ECUC_ELEM_IS_NULL);
		}
		else if (config.eIsProxy())
		{
			status = createErrorStatus(ERROR_ELEMENT_PROXY, Messages.VALIDATION_ECUC_ELEM_IS_PROXY);
		}
		else if (config.gGetDefinition() == null)
		{
			status = createErrorStatus(ERROR_DEF_NULL, Messages.VALIDATION_ECUC_DEF_IS_NULL);
		}
		else if (config.gGetDefinition().eIsProxy())
		{
			status = createErrorStatus(ERROR_DEF_PROXY, Messages.VALIDATION_ECUC_DEF_IS_PROXY);
		}
		else if (!definitionCheck(config.gGetDefinition()))
		{
			status = createErrorStatus(ERROR_DEF_NOT_VALID, Messages.VALIDATION_ECUC_DEF_NOT_VALID);
		}

		if (status == null)
		{
			status = new Status(IStatus.OK, CessarPluginActivator.PLUGIN_ID, Messages.VALIDATION_ECUC_DEF_OK);
		}

		return status;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.core.mms.IEcucMMService#validateDefinition(gautosar.gecucdescription.GContainer)
	 */
	public IStatus validateDefinition(GContainer container)
	{
		IStatus status = getContainerErrorStatus(container);

		GContainerDef containerDef = null;
		if (status == null)
		{
			containerDef = container.gGetDefinition();
			status = getContainerDefErrorStatus(containerDef);
		}

		if (status == null)
		{
			status = getParentOfContainerErrorStatus(container, containerDef);
		}

		if (status == null)
		{
			status = new Status(IStatus.OK, CessarPluginActivator.PLUGIN_ID, Messages.VALIDATION_ECUC_DEF_OK);
		}

		return status;
	}

	private IStatus getContainerErrorStatus(GContainer container)
	{
		IStatus status = null;

		if (container == null)
		{
			status = createErrorStatus(ERROR_ELEMENT_NULL, Messages.VALIDATION_ECUC_ELEM_IS_NULL);
		}
		else if (container.eIsProxy())
		{
			status = createErrorStatus(ERROR_ELEMENT_PROXY, Messages.VALIDATION_ECUC_ELEM_IS_PROXY);
		}
		return status;
	}

	private IStatus getContainerDefErrorStatus(GContainerDef containerDef)
	{
		IStatus status = null;
		if (containerDef == null)
		{
			status = createErrorStatus(ERROR_DEF_NULL, Messages.VALIDATION_ECUC_DEF_IS_NULL);
		}
		else if (containerDef.eIsProxy())
		{
			status = createErrorStatus(ERROR_DEF_PROXY, Messages.VALIDATION_ECUC_DEF_IS_PROXY);
		}
		else if (!definitionCheck(containerDef))
		{
			status = createErrorStatus(ERROR_DEF_NOT_VALID, Messages.VALIDATION_ECUC_DEF_NOT_VALID);
		}
		return status;
	}

	private IStatus getParentOfContainerErrorStatus(GContainer container, GContainerDef containerDef)
	{
		IStatus status = null;

		EObject parentOfContainer = container.eContainer();

		if (parentOfContainer == null)
		{
			status = createErrorStatus(ERROR_ELEMENT_PARENT_NULL, Messages.VALIDATION_ECUC_PARENT_OF_ELEM_NULL);
		}
		else if (parentOfContainer instanceof GModuleConfiguration)
		{
			if (((GModuleConfiguration) parentOfContainer).gGetDefinition() != getDefParent(containerDef))
			{
				status = createErrorStatus(ERROR_DIFF_PARENTS, Messages.VALIDATION_ECUC_PARENTS_ARE_DIFF);
			}
		}
		else
		{
			if (parentOfContainer instanceof GContainer)
			{
				if ((((GContainer) parentOfContainer).gGetDefinition() != getDefParent(containerDef))
					&& (((GContainer) parentOfContainer).gGetDefinition() != containerDef.eContainer()))
				{
					status = createErrorStatus(ERROR_DIFF_PARENTS, Messages.VALIDATION_ECUC_PARENTS_ARE_DIFF);
				}
			}
			else
			{
				status = createErrorStatus(ERROR_ELEMENT_PARENT_NOT_VALID,
					Messages.VALIDATION_ECUC_PARENT_OF_ELEM_NOT_VALID);
			}
		}

		return status;
	}

	/**
	 * @param containerDef
	 * @return
	 */
	protected EObject getDefParent(GContainerDef containerDef)
	{
		EObject result = containerDef.eContainer();
		if (result instanceof GChoiceContainerDef)
		{
			return getDefParent((GContainerDef) result);
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.core.mms.IEcucMMService#getParameterTypeConversionMatrix()
	 */
	@Override
	public Map<EExternalRepresentationType, Class<? extends GParameterValue>> getParameterTypeConversionMatrix()
	{
		if (conversionMatrix.isEmpty())
		{
			synchronized (this)
			{
				if (conversionMatrix.isEmpty())
				{
					populateParameterTypeConversionMatrix();
				}
			}
		}

		return conversionMatrix;
	}

	/**
	 * Populates the conversion matrix according to the specifics of the meta-model
	 */
	protected void populateParameterTypeConversionMatrix()
	{
		conversionMatrix.put(EExternalRepresentationType.BOOLEAN, GBooleanValue.class);
		conversionMatrix.put(EExternalRepresentationType.ENUM, GEnumerationValue.class);
		conversionMatrix.put(EExternalRepresentationType.FLOAT, GFloatValue.class);
		conversionMatrix.put(EExternalRepresentationType.INTEGER, GIntegerValue.class);
		conversionMatrix.put(EExternalRepresentationType.STRING, GStringValue.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.core.mms.IEcucMMService#getParameterValueConvertor(gautosar.gecucparameterdef.GConfigParameter)
	 */
	@Override
	public IParameterValueConvertor<?, ?, ?> getParameterValueConvertor(GConfigParameter definition)
	{
		IParameterValueConvertor<?, ?, ?> converter = null;
		if (definition instanceof GBooleanParamDef)
		{
			converter = new GBooleanConvertor((GBooleanParamDef) definition);
		}
		else if (definition instanceof GEnumerationParamDef)
		{
			converter = new SeaEnumConvetor((GEnumerationParamDef) definition);
		}
		else if (definition instanceof GFloatParamDef)
		{
			converter = new GFloatConvertor((GFloatParamDef) definition);
		}
		else if (definition instanceof GIntegerParamDef)
		{
			converter = new GIntegerConvertor((GIntegerParamDef) definition);
		}
		else if (definition instanceof GAbstractStringParamDef)
		{
			converter = new GStringConvertor((GAbstractStringParamDef) definition);
		}

		return converter;
	}

	/**
	 * @param containerDef
	 * @return true if the containerDef is an instance of the correct Interface
	 */
	protected abstract boolean definitionCheck(GContainerDef containerDef);

	/**
	 * @param containerDef
	 * @return true if the containerDef is an instance of the correct Interface
	 */
	protected abstract boolean definitionCheck(GModuleDef moduleDef);

	public void setBooleanParameterValue(GParameterValue parameterValue, Boolean value)
	{
		if (!(parameterValue instanceof GBooleanValue))
		{
			throw new IllegalArgumentException("Argument is not of expected type!"); //$NON-NLS-1$
		}

		GBooleanValue booleanParamValue = (GBooleanValue) parameterValue;
		booleanParamValue.gSetValue(value);

	}

	public void setFLoatParameterValue(GParameterValue parameterValue, Double value)
	{
		if (!(parameterValue instanceof GFloatValue))
		{

			throw new IllegalArgumentException("Argument is not of expected type!"); //$NON-NLS-1$
		}

		GFloatValue floatParamValue = (GFloatValue) parameterValue;
		floatParamValue.gSetValue(value);
	}

	public void setIntegerParameterValue(GParameterValue parameterValue, BigInteger value)
	{
		if (!(parameterValue instanceof GIntegerValue))
		{

			throw new IllegalArgumentException("Argument is not of expected type!"); //$NON-NLS-1$
		}

		GIntegerValue integerParamValue = (GIntegerValue) parameterValue;
		integerParamValue.gSetValue(value);
	}

	public void setStringParameterValue(GParameterValue parameterValue, String value)
	{
		if (!(parameterValue instanceof GStringValue))
		{

			throw new IllegalArgumentException("Argument is not of expected type!"); //$NON-NLS-1$
		}

		GStringValue stringParamValue = (GStringValue) parameterValue;
		stringParamValue.gSetValue(value);

	}

}
