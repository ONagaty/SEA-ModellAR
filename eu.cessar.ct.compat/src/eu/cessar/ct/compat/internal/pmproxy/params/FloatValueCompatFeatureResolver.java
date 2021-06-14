/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl7321 Oct 29, 2010 4:12:27 PM </copyright>
 */
package eu.cessar.ct.compat.internal.pmproxy.params;

import org.eclipse.emf.ecore.EAttribute;

import eu.cessar.ct.compat.internal.pmproxy.convertors.FloatConvertor;
import eu.cessar.ct.core.mms.ecuc.convertors.IParameterValueConvertor;
import eu.cessar.ct.runtime.ecuc.pmproxy.params.GFloatValueFeatureResolver;
import gautosar.gecucdescription.GFloatValue;
import gautosar.gecucparameterdef.GFloatParamDef;

/**
 * 
 * @Review uidl7321 - Apr 11, 2012
 * 
 */
public class FloatValueCompatFeatureResolver extends GFloatValueFeatureResolver
{

	/* (non-Javadoc)
	 * @see eu.cessar.ct.runtime.ecuc.pmproxy.params.GFloatValueFeatureResolver#getConvertor(org.eclipse.emf.ecore.EAttribute, gautosar.gecucparameterdef.GFloatParamDef)
	 */
	@Override
	protected IParameterValueConvertor<GFloatValue, GFloatParamDef, Double> getConvertor(
		EAttribute attribute, GFloatParamDef paramDefinition)
	{
		return new FloatConvertor(paramDefinition);
	}
}
