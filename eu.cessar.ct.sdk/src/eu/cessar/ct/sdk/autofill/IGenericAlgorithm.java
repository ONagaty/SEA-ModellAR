/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870<br/>
 * Dec 12, 2013 11:09:51 AM
 * 
 * </copyright>
 */
package eu.cessar.ct.sdk.autofill;

/**
 * 
 * @author uidl6870
 * 
 *         %created_by: uidl6870 %
 * 
 *         %date_created: Mon Feb 10 09:11:33 2014 %
 * 
 *         %version: 1 %
 * @param <T>
 *        value type
 * @param <E>
 *        element type
 * @param <F>
 *        feature type
 */
public interface IGenericAlgorithm<T, E, F>
{

	/**
	 * @param element
	 * @param feature
	 * @return the value
	 */
	public T getValue(E element, F feature);

	/**
	 * Reset the algorithm in order to be re-used (called internally)
	 */
	public void reset();

}
