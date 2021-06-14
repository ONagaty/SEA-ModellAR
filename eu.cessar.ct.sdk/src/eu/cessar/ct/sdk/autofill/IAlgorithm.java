/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidw8762<br/>
 * Nov 29, 2013 10:17:15 AM
 *
 * </copyright>
 */
package eu.cessar.ct.sdk.autofill;

import gautosar.gecucdescription.GContainer;
import gautosar.ggenericstructure.ginfrastructure.GReferrable;

/**
 * Algorithm to be used for {@link GContainer}s
 *
 * @author uidw8762
 *
 *         %created_by: uidl6870 %
 *
 *         %date_created: Mon Feb 10 09:11:31 2014 %
 *
 *         %version: 1 %
 * @param <T>
 *        value type
 */
public interface IAlgorithm<T> extends IGenericAlgorithm<T, GContainer, GReferrable>
{

	/**
	 * @param container
	 * @param parameterDefinition
	 * @return the value
	 */
	public T getValue(GContainer container, GReferrable parameterDefinition);

}
