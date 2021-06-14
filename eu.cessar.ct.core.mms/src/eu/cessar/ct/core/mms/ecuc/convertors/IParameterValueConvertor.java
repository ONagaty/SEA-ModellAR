/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Mar 17, 2010 6:14:42 PM </copyright>
 */
package eu.cessar.ct.core.mms.ecuc.convertors;

import gautosar.gecucdescription.GParameterValue;
import gautosar.gecucparameterdef.GConfigParameter;

/**
 * An IParameterValueConvertor is responsible to convert parameter values from metamodel representation to external
 * representation.
 * 
 * 
 * @param <S>
 *        the MM value class
 * @param <T>
 *        the MM definition class
 * @param <R>
 *        the external class
 * 
 * @author uidl6458
 * 
 *         %created_by: uidl6870 %
 * 
 *         %date_created: Thu Dec 12 11:58:53 2013 %
 * 
 *         %version: 2 %
 */
public interface IParameterValueConvertor<S extends GParameterValue, T extends GConfigParameter, R>
{

	/**
	 * @return the parameter definition
	 */
	public T getConfigParameterDefinition();

	/**
	 * @return The class that is used to represent the external value.
	 */
	public Class<R> getValueClass();

	/**
	 * @return Return the external value used to represent a not set (aka null) value
	 */
	public R getValueForNull();

	/**
	 * @param paramValue
	 *        the MM value
	 * @return the external value equivalent of the MM value of <code>paramValue</code>
	 */
	public R getValue(S paramValue);

	/**
	 * Set the MM value
	 * 
	 * @param paramValue
	 * @param dataValue
	 */
	public void setValue(S paramValue, R dataValue);

	/**
	 * Create a new MM value
	 * 
	 * @param paramDefinition
	 *        the definition
	 * @param dataValue
	 * @return a new MM value
	 */
	public S createValue(T paramDefinition, R dataValue);

	/**
	 * 
	 * @param paramValue
	 * @return true if the value is set, false otherwise
	 */
	public boolean isSetValue(S paramValue);

}
