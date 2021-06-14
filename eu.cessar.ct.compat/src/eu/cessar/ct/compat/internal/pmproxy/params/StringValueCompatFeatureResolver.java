/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl7321 Oct 26, 2010 2:21:03 PM </copyright>
 */
package eu.cessar.ct.compat.internal.pmproxy.params;

import org.eclipse.emf.ecore.EAttribute;

import eu.cessar.ct.compat.internal.pmproxy.convertors.StringConvertor;
import eu.cessar.ct.core.mms.ecuc.convertors.IParameterValueConvertor;
import eu.cessar.ct.runtime.ecuc.pmproxy.params.GStringValueFeatureResolver;
import gautosar.gecucdescription.GStringValue;
import gautosar.gecucparameterdef.GAbstractStringParamDef;

/**
 * 
 * @Review uidl7321 - Apr 11, 2012
 * 
 */
public class StringValueCompatFeatureResolver extends GStringValueFeatureResolver
{

	/* (non-Javadoc)
	 * @see eu.cessar.ct.runtime.ecuc.pmproxy.params.GStringValueFeatureResolver#getConvertor(org.eclipse.emf.ecore.EAttribute)
	 */
	@Override
	protected IParameterValueConvertor<GStringValue, GAbstractStringParamDef, String> getConvertor(
		EAttribute attribute, GAbstractStringParamDef paramDefinition)
	{
		return new StringConvertor(paramDefinition);
	}
}
