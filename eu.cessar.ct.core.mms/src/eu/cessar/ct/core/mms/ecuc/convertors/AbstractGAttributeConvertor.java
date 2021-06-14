/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Mar 18, 2010 10:28:10 AM </copyright>
 */
package eu.cessar.ct.core.mms.ecuc.convertors;

import eu.cessar.ct.core.mms.IGenericFactory;
import eu.cessar.ct.core.mms.MMSRegistry;
import gautosar.gecucdescription.GParameterValue;
import gautosar.gecucparameterdef.GConfigParameter;

/**
 * Abstract generic class for the parameters that have GAUTOSAR support
 * 
 * @param <V>
 * @param <D>
 * @param <T>
 * 
 * @author uidl6458
 * 
 *         %created_by: uidl6870 %
 * 
 *         %date_created: Thu Apr 11 11:11:51 2013 %
 * 
 *         %version: 1 %
 */
public abstract class AbstractGAttributeConvertor<V extends GParameterValue, D extends GConfigParameter, T> extends
	AbstractGAttributeConvertorWithDef<V, D, T>
{

	/**
	 * Create a new converter
	 * 
	 * @param paramDefinition
	 */
	public AbstractGAttributeConvertor(D paramDefinition)
	{
		super(paramDefinition);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.cessar.ct.runtime.ecuc.convertors.IParameterValueConvertor#createValue(gautosar.gecucparameterdef.GConfigParameter
	 * , java.lang.Object)
	 */
	@SuppressWarnings("deprecation")
	public V createValue(D paramDefinition, T dataValue)
	{
		IGenericFactory factory = MMSRegistry.INSTANCE.getGenericFactory(paramDefinition);
		V result = (V) factory.createParameterValue(paramDefinition);
		setValue(result, dataValue);
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.runtime.ecuc.convertors.IParameterValueConvertor#getValueForNull()
	 */
	public T getValueForNull()
	{
		return null;
	}
}
