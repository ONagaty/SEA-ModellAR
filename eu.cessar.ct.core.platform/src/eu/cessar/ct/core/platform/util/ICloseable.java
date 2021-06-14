/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Apr 30, 2010 4:36:59 PM </copyright>
 */
package eu.cessar.ct.core.platform.util;

/**
 * Interface that can be implemented by any kind of class that performs IO
 * operation.
 * 
 * @Review uidl6458 - 18.04.2012
 * 
 */
public interface ICloseable
{

	/**
	 * Closes the opened streams
	 */
	public void close();

}
