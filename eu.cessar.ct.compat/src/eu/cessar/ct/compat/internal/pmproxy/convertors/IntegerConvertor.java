/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl7321 Oct 29, 2010 10:18:44 AM </copyright>
 */
package eu.cessar.ct.compat.internal.pmproxy.convertors;

import java.math.BigInteger;

import eu.cessar.ct.core.mms.IEcucMMService;
import eu.cessar.ct.core.mms.IMetaModelService;
import eu.cessar.ct.core.mms.MMSRegistry;
import eu.cessar.ct.core.mms.ecuc.convertors.GIntegerConvertor;
import gautosar.gecucdescription.GIntegerValue;
import gautosar.gecucparameterdef.GConfigParameter;
import gautosar.gecucparameterdef.GIntegerParamDef;

/**
 * 
 * @Review uidl7321 - Apr 11, 2012
 * 
 */
public class IntegerConvertor extends GIntegerConvertor
{

	/**
	 * @param paramDefinition
	 */
	public IntegerConvertor(GIntegerParamDef paramDefinition)
	{
		super(paramDefinition);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.runtime.ecuc.convertors.AbstractGAttributeConvertor#getValueForNull()
	 */
	@Override
	public BigInteger getValueForNull()
	{
		GConfigParameter paramDef = getConfigParameterDefinition();
		if (paramDef != null)
		{
			IMetaModelService mmService = MMSRegistry.INSTANCE.getMMService(paramDef.eClass());
			if (mmService != null)
			{
				IEcucMMService ecucMMService = mmService.getEcucMMService();
				Object defaultValue = ecucMMService.getDefaultValue(paramDef);
				if (defaultValue instanceof BigInteger)
				{
					return (BigInteger) defaultValue;
				}
			}
		}

		return super.getValueForNull();
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.runtime.ecuc.convertors.GIntegerConvertor#getValue(gautosar.gecucdescription.GIntegerValue)
	 */
	@Override
	public BigInteger getValue(GIntegerValue paramValue)
	{
		if (paramValue == null)
		{
			return getValueForNull();
		}
		else
		{
			BigInteger value = paramValue.gGetValue();
			if (value == null)
			{
				return getValueForNull();
			}
			return value;
		}
	}

}
