/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Mar 16, 2010 5:39:54 PM </copyright>
 */
package eu.cessar.ct.runtime.ecuc.pmproxy.params;

import eu.cessar.ct.core.mms.ecuc.convertors.GFloatConvertor;
import eu.cessar.ct.core.mms.ecuc.convertors.IParameterValueConvertor;
import gautosar.gecucdescription.GFloatValue;
import gautosar.gecucparameterdef.GFloatParamDef;

import org.eclipse.emf.ecore.EAttribute;

/**
 * 
 */
public class GFloatValueFeatureResolver extends
	GConfigParameterFeatureResolver<GFloatValue, GFloatParamDef, Double>
{

	/* (non-Javadoc)
	 * @see eu.cessar.ct.runtime.ecuc.pmproxy.params.GConfigParameterFeatureResolver#getAttributeValueClass()
	 */
	@Override
	protected Class<GFloatValue> getParameterValueClass()
	{
		return GFloatValue.class;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.runtime.ecuc.pmproxy.params.GConfigParameterFeatureResolver#getParameterDefinitionClass()
	 */
	@Override
	protected Class<GFloatParamDef> getParameterDefinitionClass()
	{
		return GFloatParamDef.class;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.runtime.ecuc.pmproxy.params.GConfigParameterFeatureResolver#getConvertor()
	 */
	@Override
	protected IParameterValueConvertor<GFloatValue, GFloatParamDef, Double> getConvertor(
		EAttribute attribute, GFloatParamDef paramDefinition)
	{
		// return Convertors.ECUC_GFLOAT_CONVERTOR;
		return new GFloatConvertor(paramDefinition);
	}

}
