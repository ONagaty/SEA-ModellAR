/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Mar 18, 2010 10:33:16 AM </copyright>
 */
package eu.cessar.ct.core.mms.ecuc.convertors;

import gautosar.gecucdescription.GFloatValue;
import gautosar.gecucparameterdef.GFloatParamDef;

/**
 * 
 */
public class GFloatConvertor extends AbstractGAttributeConvertor<GFloatValue, GFloatParamDef, Double>
{

	/**
	 * @param paramDefinition
	 */
	public GFloatConvertor(GFloatParamDef paramDefinition)
	{
		super(paramDefinition);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.runtime.ecuc.convertors.IParameterValueConvertor#getValueClass()
	 */
	public Class<Double> getValueClass()
	{
		return Double.class;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.cessar.ct.runtime.ecuc.convertors.IParameterValueConvertor#getValue(gautosar.gecucdescription.GParameterValue)
	 */
	public Double getValue(GFloatValue paramValue)
	{
		if (paramValue == null || !paramValue.gIsSetValue())
		{
			return getValueForNull();
		}
		else
		{
			return paramValue.gGetValue();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.cessar.ct.runtime.ecuc.convertors.IParameterValueConvertor#isSetValue(gautosar.gecucdescription.GParameterValue
	 * )
	 */
	public boolean isSetValue(GFloatValue paramValue)
	{
		return paramValue != null && paramValue.gIsSetValue();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.cessar.ct.runtime.ecuc.convertors.IParameterValueConvertor#setValue(gautosar.gecucdescription.GParameterValue,
	 * java.lang.Object)
	 */
	public void setValue(GFloatValue paramValue, Double dataValue)
	{
		if (dataValue == null)
		{
			paramValue.gUnsetValue();
		}
		else
		{
			paramValue.gSetValue(dataValue);
		}
	}

}
