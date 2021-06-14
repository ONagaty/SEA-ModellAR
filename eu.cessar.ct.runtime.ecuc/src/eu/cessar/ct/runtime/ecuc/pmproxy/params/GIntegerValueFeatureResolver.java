/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Mar 16, 2010 5:37:17 PM </copyright>
 */
package eu.cessar.ct.runtime.ecuc.pmproxy.params;

import eu.cessar.ct.core.mms.ecuc.convertors.GIntegerConvertor;
import eu.cessar.ct.core.mms.ecuc.convertors.IParameterValueConvertor;
import gautosar.gecucdescription.GIntegerValue;
import gautosar.gecucparameterdef.GIntegerParamDef;

import java.math.BigInteger;

import org.eclipse.emf.ecore.EAttribute;

/**
 * 
 */
public class GIntegerValueFeatureResolver extends
	GConfigParameterFeatureResolver<GIntegerValue, GIntegerParamDef, BigInteger>
{

	/* (non-Javadoc)
	 * @see eu.cessar.ct.runtime.ecuc.pmproxy.params.GConfigParameterFeatureResolver#getAttributeValueClass()
	 */
	@Override
	protected Class<GIntegerValue> getParameterValueClass()
	{
		return GIntegerValue.class;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.runtime.ecuc.pmproxy.params.GConfigParameterFeatureResolver#getParameterDefinitionClass()
	 */
	@Override
	protected Class<GIntegerParamDef> getParameterDefinitionClass()
	{
		return GIntegerParamDef.class;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.runtime.ecuc.pmproxy.params.GConfigParameterFeatureResolver#getConvertor()
	 */
	@Override
	protected IParameterValueConvertor<GIntegerValue, GIntegerParamDef, BigInteger> getConvertor(
		EAttribute attribute, GIntegerParamDef paramDefinition)
	{
		// return Convertors.ECUC_GINTEGER_CONVERTOR;
		return new GIntegerConvertor(paramDefinition);
	}

}
