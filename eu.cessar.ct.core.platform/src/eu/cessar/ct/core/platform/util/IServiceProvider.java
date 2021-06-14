/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Feb 12, 2010 5:00:44 PM </copyright>
 */
package eu.cessar.ct.core.platform.util;

/**
 * Interface that need to be implemented by an SDK Service.
 * 
 * @see PlatformUtils#getService(Class, Object...)
 * 
 * @author uidl6458
 * 
 */
public interface IServiceProvider<T>
{

	/**
	 * Return the service corresponding to the serviceClass
	 * 
	 * @param serviceClass
	 *        the service class
	 * @param args
	 *        service specific arguments
	 * @return
	 */
	public T getService(Class<T> serviceClass, Object... args);

}
