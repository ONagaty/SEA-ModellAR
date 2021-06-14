/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl7321 Oct 29, 2010 4:13:02 PM </copyright>
 */
package eu.cessar.ct.compat.internal.pmproxy.params;

import java.math.BigInteger;

import org.eclipse.emf.ecore.EAttribute;

import eu.cessar.ct.compat.internal.pmproxy.convertors.IntegerConvertor;
import eu.cessar.ct.core.mms.ecuc.convertors.IParameterValueConvertor;
import eu.cessar.ct.runtime.ecuc.pmproxy.params.GIntegerValueFeatureResolver;
import gautosar.gecucdescription.GIntegerValue;
import gautosar.gecucparameterdef.GIntegerParamDef;

/**
 * 
 * @Review uidl7321 - Apr 11, 2012
 * 
 */
public class IntegerValueCompatFeatureResolver extends GIntegerValueFeatureResolver
{

	/* (non-Javadoc)
	 * @see eu.cessar.ct.runtime.ecuc.pmproxy.params.GIntegerValueFeatureResolver#getConvertor(org.eclipse.emf.ecore.EAttribute, gautosar.gecucparameterdef.GIntegerParamDef)
	 */
	@Override
	protected IParameterValueConvertor<GIntegerValue, GIntegerParamDef, BigInteger> getConvertor(
		EAttribute attribute, GIntegerParamDef paramDefinition)
	{
		return new IntegerConvertor(paramDefinition);
	}
}
