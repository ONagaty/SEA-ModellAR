/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Mar 18, 2010 10:25:45 AM </copyright>
 */
package eu.cessar.ct.core.mms.ecuc.convertors;

import gautosar.gecucdescription.GBooleanValue;
import gautosar.gecucparameterdef.GBooleanParamDef;

/**
 * Converter for GBooleanValue
 * 
 * @author uidl6458
 * 
 *         %created_by: uidl6870 %
 * 
 *         %date_created: Thu Apr 11 11:11:52 2013 %
 * 
 *         %version: 1 %
 */
public class GBooleanConvertor extends AbstractGAttributeConvertor<GBooleanValue, GBooleanParamDef, Boolean>
{

	/**
	 * @param paramDefinition
	 */
	public GBooleanConvertor(GBooleanParamDef paramDefinition)
	{
		super(paramDefinition);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.runtime.ecuc.convertors.IParameterValueConvertor#getValueClass()
	 */
	public Class<Boolean> getValueClass()
	{
		return Boolean.class;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.cessar.ct.runtime.ecuc.convertors.IParameterValueConvertor#getValue(gautosar.gecucdescription.GParameterValue)
	 */
	public Boolean getValue(GBooleanValue paramValue)
	{
		if (paramValue == null || !paramValue.gIsSetValue())
		{
			return getValueForNull();
		}
		else
		{
			Boolean result = paramValue.gGetValue();
			if (result != null)
			{
				// be sure that Boolean.TRUE or Boolean.FALSE is returned and
				// not a new instance of the Boolean class
				return Boolean.valueOf(result);
			}
			else
			{
				return getValueForNull();
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.cessar.ct.runtime.ecuc.convertors.IParameterValueConvertor#isSetValue(gautosar.gecucdescription.GParameterValue
	 * )
	 */
	public boolean isSetValue(GBooleanValue paramValue)
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
	public void setValue(GBooleanValue paramValue, Boolean dataValue)
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
