/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl7321 Oct 29, 2010 10:18:22 AM </copyright>
 */
package eu.cessar.ct.compat.internal.pmproxy.convertors;

import eu.cessar.ct.core.mms.IEcucMMService;
import eu.cessar.ct.core.mms.IMetaModelService;
import eu.cessar.ct.core.mms.MMSRegistry;
import eu.cessar.ct.core.mms.ecuc.convertors.GBooleanConvertor;
import gautosar.gecucdescription.GBooleanValue;
import gautosar.gecucparameterdef.GBooleanParamDef;
import gautosar.gecucparameterdef.GConfigParameter;

/**
 * @Review uidl7321 - Apr 11, 2012
 */
public class BooleanConvertor extends GBooleanConvertor
{

	/**
	 * @param paramDefinition
	 */
	public BooleanConvertor(GBooleanParamDef paramDefinition)
	{
		super(paramDefinition);
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.runtime.ecuc.convertors.AbstractGAttributeConvertor#getValueForNull()
	 */
	@Override
	public Boolean getValueForNull()
	{
		GConfigParameter paramDef = getConfigParameterDefinition();
		if (paramDef != null)
		{
			IMetaModelService mmService = MMSRegistry.INSTANCE.getMMService(paramDef.eClass());
			if (mmService != null)
			{
				IEcucMMService ecucMMService = mmService.getEcucMMService();
				Object defaultValue = ecucMMService.getDefaultValue(paramDef);
				if (defaultValue instanceof Boolean)
				{
					return (Boolean) defaultValue;
				}
			}
		}

		return super.getValueForNull();
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.runtime.ecuc.convertors.GBooleanConvertor#getValue(gautosar.gecucdescription.GBooleanValue)
	 */
	@Override
	public Boolean getValue(GBooleanValue paramValue)
	{
		if (paramValue == null)
		{
			return getValueForNull();
		}
		else
		{
			Boolean value = paramValue.gGetValue();
			if (value == null)
			{
				return getValueForNull();
			}
			else
			{
				// be sure that Boolean.TRUE or Boolean.FALSE is returned and
				// not a new instance of the Boolean class
				return Boolean.valueOf(value);
			}
		}
	}

}
