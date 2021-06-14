/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870<br/>
 * 15.05.2013 10:37:50
 * 
 * </copyright>
 */
package eu.cessar.ct.runtime.ecuc.internal.sea.handlers;

import eu.cessar.ct.sdk.sea.ISEAContainer;
import gautosar.gecucparameterdef.GCommonConfigurationAttributes;

import java.util.List;

/**
 * Sea attributes handler defining methods that are applicable for both parameter and reference values
 * 
 * @author uidl6870
 * 
 *         %created_by: uidl6870 %
 * 
 *         %date_created: Tue Feb 4 16:16:32 2014 %
 * 
 *         %version: 3 %
 * @param <T>
 */
public interface ISeaAttributesHandler<T extends GCommonConfigurationAttributes>
{

	/**
	 * @param parent
	 * @param definition
	 * @return whether given <code>parent</code> has at least one parameter/reference value set for the provided
	 *         <code>definition</code>
	 */
	public boolean isSet(ISEAContainer parent, T definition);

	/**
	 * Removes the parameter/reference values with the provided <code>definition</code> from the given
	 * <code>parent</code>
	 * 
	 * @param parent
	 * @param definition
	 */
	public void unSet(ISEAContainer parent, T definition);

	/**
	 * Checks the validness of the provided <code>values</code>. <br>
	 * If at least one value does not pass the check, all of them are sent to the error handler who may decide to
	 * recover by providing other values instead.
	 * 
	 * @param parent
	 *        the SEA container wrapper
	 * @param definition
	 *        the definition
	 * @param values
	 *        the list with values to be set in the given parent for the given definition
	 * @return the accepted values or an empty list
	 */
	public <V> List<V> getAcceptedValues(ISEAContainer parent, T definition, List<V> values);

}
