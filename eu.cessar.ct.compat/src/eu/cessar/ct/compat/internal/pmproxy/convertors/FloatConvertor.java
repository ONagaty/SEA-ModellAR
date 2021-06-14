/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl7321 Oct 29, 2010 10:18:57 AM </copyright>
 */
package eu.cessar.ct.compat.internal.pmproxy.convertors;

import eu.cessar.ct.core.mms.IEcucMMService;
import eu.cessar.ct.core.mms.IMetaModelService;
import eu.cessar.ct.core.mms.MMSRegistry;
import eu.cessar.ct.core.mms.ecuc.convertors.GFloatConvertor;
import gautosar.gecucdescription.GFloatValue;
import gautosar.gecucparameterdef.GConfigParameter;
import gautosar.gecucparameterdef.GFloatParamDef;

/**
 * 
 * @Review uidl7321 - Apr 11, 2012
 * 
 */
public class FloatConvertor extends GFloatConvertor
{

	/**
	 * @param paramDefinition
	 */
	public FloatConvertor(GFloatParamDef paramDefinition)
	{
		super(paramDefinition);
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.runtime.ecuc.convertors.AbstractGAttributeConvertor#getValueForNull()
	 */
	@Override
	public Double getValueForNull()
	{
		GConfigParameter paramDef = getConfigParameterDefinition();
		if (paramDef != null)
		{
			IMetaModelService mmService = MMSRegistry.INSTANCE.getMMService(paramDef.eClass());
			if (mmService != null)
			{
				IEcucMMService ecucMMService = mmService.getEcucMMService();
				Object defaultValue = ecucMMService.getDefaultValue(paramDef);
				if (defaultValue instanceof Double)
				{
					return (Double) defaultValue;
				}
			}
		}

		return super.getValueForNull();
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.runtime.ecuc.convertors.GFloatConvertor#getValue(gautosar.gecucdescription.GFloatValue)
	 */
	@Override
	public Double getValue(GFloatValue paramValue)
	{
		if (paramValue == null)
		{
			return getValueForNull();
		}
		else
		{
			Double value = paramValue.gGetValue();
			if (value == null)
			{
				return getValueForNull();
			}
			return value;
		}
	}

}
