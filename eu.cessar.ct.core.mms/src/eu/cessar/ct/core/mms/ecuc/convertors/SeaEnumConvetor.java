/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870<br/>
 * 15.04.2013 10:53:48
 * 
 * </copyright>
 */
package eu.cessar.ct.core.mms.ecuc.convertors;

import eu.cessar.ct.core.mms.IMetaModelService;
import eu.cessar.ct.core.mms.MMSRegistry;
import gautosar.gecucdescription.GEnumerationValue;
import gautosar.gecucparameterdef.GEnumerationParamDef;

/**
 * Converter for enum parameters, used by the Sea model
 * 
 * @author uidl6870
 * 
 *         %created_by: uidl6870 %
 * 
 *         %date_created: Tue Apr 16 17:37:34 2013 %
 * 
 *         %version: 1 %
 */
public class SeaEnumConvetor extends
	AbstractGAttributeConvertorWithDef<GEnumerationValue, GEnumerationParamDef, String>
{
	/**
	 * @param paramDefinition
	 */
	public SeaEnumConvetor(GEnumerationParamDef paramDefinition)
	{
		super(paramDefinition);
	}

	private IMetaModelService getMMService()
	{
		return MMSRegistry.INSTANCE.getMMService(getConfigParameterDefinition().eClass());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.core.mms.ecuc.convertors.IParameterValueConvertor#getValueClass()
	 */
	@Override
	public Class<String> getValueClass()
	{
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.core.mms.ecuc.convertors.IParameterValueConvertor#getValueForNull()
	 */
	@Override
	public String getValueForNull()
	{
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.cessar.ct.core.mms.ecuc.convertors.IParameterValueConvertor#getValue(gautosar.gecucdescription.GParameterValue
	 * )
	 */
	@Override
	public String getValue(GEnumerationValue paramValue)
	{
		Object value = getMMService().getEcucMMService().getParameterValue(paramValue);
		return (String) value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.cessar.ct.core.mms.ecuc.convertors.IParameterValueConvertor#setValue(gautosar.gecucdescription.GParameterValue
	 * , java.lang.Object)
	 */
	@Override
	public void setValue(GEnumerationValue paramValue, String dataValue)
	{
		paramValue.gSetValue(dataValue);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.core.mms.ecuc.convertors.IParameterValueConvertor#createValue(gautosar.gecucparameterdef.
	 * GConfigParameter, java.lang.Object)
	 */
	@Override
	public GEnumerationValue createValue(GEnumerationParamDef paramDefinition, String dataValue)
	{
		GEnumerationValue parameterValue = (GEnumerationValue) getMMService().getGenericFactory().createParameterValue(
			paramDefinition);
		setValue(parameterValue, dataValue);

		return parameterValue;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.cessar.ct.core.mms.ecuc.convertors.IParameterValueConvertor#isSetValue(gautosar.gecucdescription.GParameterValue
	 * )
	 */
	@Override
	public boolean isSetValue(GEnumerationValue paramValue)
	{
		return paramValue != null && paramValue.gIsSetValue();
	}

}
