/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Apr 30, 2010 11:28:24 AM </copyright>
 */
package eu.cessar.ct.runtime.execution;

/**
 * @author uidl6458
 * 
 */
public interface IBinaryClassResolver
{

	/**
	 * Try to resolve the the <code>className</code>. If succeeded the binary
	 * representation of the class will be returned, otherwise null.
	 * 
	 * @param className
	 * @return
	 */
	public byte[] resolveClass(String className);

}
