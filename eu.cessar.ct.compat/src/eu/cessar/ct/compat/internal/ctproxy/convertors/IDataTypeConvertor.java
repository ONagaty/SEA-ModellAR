/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl7321 Dec 16, 2010 3:54:19 PM </copyright>
 */
package eu.cessar.ct.compat.internal.ctproxy.convertors;

/**
 * @author uidl7321
 * @Review uidl7321 - Apr 11, 2012
 */

// S-slave class, M-master class
public interface IDataTypeConvertor<S, M>
{
	/**
	 * @return
	 */
	public Class<? extends S> getSlaveClass();

	/**
	 * @return
	 */
	public Class<? extends M> getMasterClass();

	/**
	 * 
	 * @param slaveValue
	 * @return
	 */
	public M convertToMasterValue(S slaveValue);

	/**
	 * 
	 * @param masterValue
	 * @return
	 */
	public S convertToSlaveValue(M masterValue);

}
