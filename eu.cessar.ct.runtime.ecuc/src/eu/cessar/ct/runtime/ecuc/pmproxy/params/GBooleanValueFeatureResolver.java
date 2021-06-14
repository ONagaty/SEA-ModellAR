/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Mar 16, 2010 5:36:41 PM </copyright>
 */
package eu.cessar.ct.runtime.ecuc.pmproxy.params;

import eu.cessar.ct.core.mms.ecuc.convertors.GBooleanConvertor;
import eu.cessar.ct.core.mms.ecuc.convertors.IParameterValueConvertor;
import gautosar.gecucdescription.GBooleanValue;
import gautosar.gecucparameterdef.GBooleanParamDef;

import org.eclipse.emf.ecore.EAttribute;

/**
 * 
 */
public class GBooleanValueFeatureResolver extends
	GConfigParameterFeatureResolver<GBooleanValue, GBooleanParamDef, Boolean>
{

	/* (non-Javadoc)
	 * @see eu.cessar.ct.runtime.ecuc.pmproxy.params.GConfigParameterFeatureResolver#getAttributeValueClass()
	 */
	@Override
	protected Class<GBooleanValue> getParameterValueClass()
	{
		return GBooleanValue.class;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.runtime.ecuc.pmproxy.params.GConfigParameterFeatureResolver#getParameterDefinitionClass()
	 */
	@Override
	protected Class<GBooleanParamDef> getParameterDefinitionClass()
	{
		return GBooleanParamDef.class;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.runtime.ecuc.pmproxy.params.GConfigParameterFeatureResolver#getConvertor()
	 */
	@Override
	protected IParameterValueConvertor<GBooleanValue, GBooleanParamDef, Boolean> getConvertor(
		EAttribute attribute, GBooleanParamDef paramDefinition)
	{
		// return Convertors.ECUC_GBOOLEAN_CONVERTOR;
		return new GBooleanConvertor(paramDefinition);
	}

}
