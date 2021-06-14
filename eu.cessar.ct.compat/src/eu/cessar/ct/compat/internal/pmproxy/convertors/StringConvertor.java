/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl7321 Oct 26, 2010 2:33:18 PM </copyright>
 */
package eu.cessar.ct.compat.internal.pmproxy.convertors;

import eu.cessar.ct.core.mms.IEcucMMService;
import eu.cessar.ct.core.mms.IMetaModelService;
import eu.cessar.ct.core.mms.MMSRegistry;
import eu.cessar.ct.core.mms.ecuc.convertors.GStringConvertor;
import gautosar.gecucdescription.GStringValue;
import gautosar.gecucparameterdef.GAbstractStringParamDef;
import gautosar.gecucparameterdef.GConfigParameter;

/**
 * 
 * @Review uidl7321 - Apr 11, 2012
 * 
 */
public class StringConvertor extends GStringConvertor
{

	/**
	 * @param paramDefinition
	 */
	public StringConvertor(GAbstractStringParamDef paramDefinition)
	{
		super(paramDefinition);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.runtime.ecuc.convertors.AbstractGAttributeConvertor#getValueForNull()
	 */
	@Override
	public String getValueForNull()
	{
		GConfigParameter paramDef = getConfigParameterDefinition();
		if (paramDef != null)
		{
			IMetaModelService mmService = MMSRegistry.INSTANCE.getMMService(paramDef.eClass());
			if (mmService != null)
			{
				IEcucMMService ecucMMService = mmService.getEcucMMService();
				Object defaultValue = ecucMMService.getDefaultValue(paramDef);
				if (defaultValue instanceof String)
				{
					return (String) defaultValue;
				}
			}
		}

		return super.getValueForNull();
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.runtime.ecuc.convertors.GStringConvertor#getValue(gautosar.gecucdescription.GStringValue)
	 */
	@Override
	public String getValue(GStringValue paramValue)
	{
		if (paramValue == null)
		{
			return getValueForNull();
		}
		else
		{
			String value = paramValue.gGetValue();

			if (value == null)
			{
				return getValueForNull();
			}
			if (value.length() == 0)
			{
				return null;
			}

			return value;// .intern();
		}
	}
}
