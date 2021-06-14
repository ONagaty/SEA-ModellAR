/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl7321 Oct 29, 2010 4:11:45 PM </copyright>
 */
package eu.cessar.ct.compat.internal.pmproxy.params;

import org.eclipse.emf.ecore.EAttribute;

import eu.cessar.ct.compat.internal.pmproxy.convertors.BooleanConvertor;
import eu.cessar.ct.core.mms.ecuc.convertors.IParameterValueConvertor;
import eu.cessar.ct.runtime.ecuc.pmproxy.params.GBooleanValueFeatureResolver;
import gautosar.gecucdescription.GBooleanValue;
import gautosar.gecucparameterdef.GBooleanParamDef;

/**
 * 
 * @Review uidl7321 - Apr 11, 2012
 * 
 */
public class BooleanValueCompatFeatureResolver extends GBooleanValueFeatureResolver
{

	/* (non-Javadoc)
	 * @see eu.cessar.ct.runtime.ecuc.pmproxy.params.GBooleanValueFeatureResolver#getConvertor(org.eclipse.emf.ecore.EAttribute, gautosar.gecucparameterdef.GBooleanParamDef)
	 */
	@Override
	protected IParameterValueConvertor<GBooleanValue, GBooleanParamDef, Boolean> getConvertor(
		EAttribute attribute, GBooleanParamDef paramDefinition)
	{
		return new BooleanConvertor(paramDefinition);
	}
}
