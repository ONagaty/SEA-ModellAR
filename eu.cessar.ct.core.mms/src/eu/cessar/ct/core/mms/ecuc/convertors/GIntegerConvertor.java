/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Mar 18, 2010 10:35:25 AM </copyright>
 */
package eu.cessar.ct.core.mms.ecuc.convertors;

import gautosar.gecucdescription.GIntegerValue;
import gautosar.gecucparameterdef.GIntegerParamDef;

import java.math.BigInteger;

/**
 * 
 */
public class GIntegerConvertor extends AbstractGAttributeConvertor<GIntegerValue, GIntegerParamDef, BigInteger>
{

	/**
	 * @param paramDefinition
	 */
	public GIntegerConvertor(GIntegerParamDef paramDefinition)
	{
		super(paramDefinition);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.runtime.ecuc.convertors.IParameterValueConvertor#getValueClass()
	 */
	public Class<BigInteger> getValueClass()
	{
		return BigInteger.class;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.cessar.ct.runtime.ecuc.convertors.IParameterValueConvertor#getValue(gautosar.gecucdescription.GParameterValue)
	 */
	public BigInteger getValue(GIntegerValue paramValue)
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
	public boolean isSetValue(GIntegerValue paramValue)
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
	public void setValue(GIntegerValue paramValue, BigInteger dataValue)
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
