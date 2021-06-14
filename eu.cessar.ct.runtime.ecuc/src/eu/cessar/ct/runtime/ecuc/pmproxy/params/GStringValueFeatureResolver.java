/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Mar 16, 2010 5:40:17 PM </copyright>
 */
package eu.cessar.ct.runtime.ecuc.pmproxy.params;

import eu.cessar.ct.core.mms.ecuc.convertors.GStringConvertor;
import eu.cessar.ct.core.mms.ecuc.convertors.IParameterValueConvertor;
import gautosar.gecucdescription.GStringValue;
import gautosar.gecucparameterdef.GAbstractStringParamDef;

import org.eclipse.emf.ecore.EAttribute;

/**
 * 
 */
public class GStringValueFeatureResolver extends
	GConfigParameterFeatureResolver<GStringValue, GAbstractStringParamDef, String>
{

	/* (non-Javadoc)
	 * @see eu.cessar.ct.runtime.ecuc.pmproxy.params.GConfigParameterFeatureResolver#getAttributeValueClass()
	 */
	@Override
	protected Class<GStringValue> getParameterValueClass()
	{
		return GStringValue.class;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.runtime.ecuc.pmproxy.params.GConfigParameterFeatureResolver#getParameterDefinitionClass()
	 */
	@Override
	protected Class<GAbstractStringParamDef> getParameterDefinitionClass()
	{
		// TODO Auto-generated method stub
		return GAbstractStringParamDef.class;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.runtime.ecuc.pmproxy.params.GConfigParameterFeatureResolver#getConvertor()
	 */
	@Override
	protected IParameterValueConvertor<GStringValue, GAbstractStringParamDef, String> getConvertor(
		EAttribute attribute, GAbstractStringParamDef paramDefinition)
	{
		// return Convertors.ECUC_GSTRING_CONVERTOR;
		return new GStringConvertor(paramDefinition);
	}

}
