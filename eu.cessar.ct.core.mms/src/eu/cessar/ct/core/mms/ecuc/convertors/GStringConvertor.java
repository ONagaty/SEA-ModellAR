/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Mar 18, 2010 10:36:54 AM </copyright>
 */
package eu.cessar.ct.core.mms.ecuc.convertors;

import gautosar.gecucdescription.GStringValue;
import gautosar.gecucparameterdef.GAbstractStringParamDef;

/**
 * 
 */
public class GStringConvertor extends AbstractGAttributeConvertor<GStringValue, GAbstractStringParamDef, String>
{
	/**
	 * @param paramDefinition
	 */
	public GStringConvertor(GAbstractStringParamDef paramDefinition)
	{
		super(paramDefinition);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.runtime.ecuc.convertors.IParameterValueConvertor#getValueClass()
	 */
	public Class<String> getValueClass()
	{
		return String.class;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.cessar.ct.runtime.ecuc.convertors.IParameterValueConvertor#getValue(gautosar.gecucdescription.GParameterValue)
	 */
	public String getValue(GStringValue paramValue)
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
	public boolean isSetValue(GStringValue paramValue)
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
	public void setValue(GStringValue paramValue, String dataValue)
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
