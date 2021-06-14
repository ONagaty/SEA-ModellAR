/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl7321 Oct 29, 2010 1:10:25 PM </copyright>
 */
package eu.cessar.ct.core.mms.ecuc.convertors;

import gautosar.gecucdescription.GParameterValue;
import gautosar.gecucparameterdef.GConfigParameter;

/**
 * Abstract base implementation for converters
 * 
 * @param <V>
 * @param <D>
 * @param <T>
 * @author uidl6458
 * 
 *         %created_by: uidl6870 %
 * 
 *         %date_created: Thu Apr 11 11:11:51 2013 %
 * 
 *         %version: 1 %
 */
public abstract class AbstractGAttributeConvertorWithDef<V extends GParameterValue, D extends GConfigParameter, T>
	implements IParameterValueConvertor<V, D, T>
{
	private D paramDefinition;

	/**
	 * Create a new converter
	 * 
	 * @param paramDefinition
	 */
	public AbstractGAttributeConvertorWithDef(D paramDefinition)
	{
		this.paramDefinition = paramDefinition;
	}

	/**
	 * @return the converter MM definition
	 */
	public D getConfigParameterDefinition()
	{
		return paramDefinition;
	}

}
