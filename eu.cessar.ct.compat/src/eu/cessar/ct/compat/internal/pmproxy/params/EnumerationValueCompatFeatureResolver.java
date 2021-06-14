/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl7321 Oct 28, 2010 9:26:22 AM </copyright>
 */
package eu.cessar.ct.compat.internal.pmproxy.params;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EEnum;

import eu.cessar.ct.compat.internal.pmproxy.convertors.EnumConvertor;
import eu.cessar.ct.core.mms.ecuc.convertors.IParameterValueConvertor;
import eu.cessar.ct.runtime.ecuc.pmproxy.params.GEnumerationParamDefFeatureResolver;
import gautosar.gecucdescription.GEnumerationValue;
import gautosar.gecucparameterdef.GEnumerationParamDef;

/**
 * 
 * @Review uidl7321 - Apr 11, 2012
 * 
 */
public class EnumerationValueCompatFeatureResolver extends GEnumerationParamDefFeatureResolver
{

	/* (non-Javadoc)
	 * @see eu.cessar.ct.runtime.ecuc.pmproxy.params.GEnumerationParamDefFeatureResolver#getConvertor(org.eclipse.emf.ecore.EAttribute)
	 */
	@Override
	protected IParameterValueConvertor<GEnumerationValue, GEnumerationParamDef, Object> getConvertor(
		EAttribute attribute, GEnumerationParamDef paramDefinition)
	{
		EEnum eEnum = (EEnum) attribute.getEType();
		return new EnumConvertor(eEnum, paramDefinition);
	}
}
