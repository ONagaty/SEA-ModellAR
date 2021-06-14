/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Nov 20, 2009 9:54:14 AM </copyright>
 */
package eu.cessar.ct.runtime.execution;

import java.net.URL;

/**
 * An empty class loader will throw a {@link ClassNotFoundException} for all
 * request.
 * 
 */
public class EmptyClassLoader extends ClassLoader
{
	/* (non-Javadoc)
	 * @see java.lang.ClassLoader#loadClass(java.lang.String)
	 */
	@Override
	public Class<?> loadClass(String name) throws ClassNotFoundException
	{
		throw new ClassNotFoundException(name);
	}

	/* (non-Javadoc)
	 * @see java.lang.ClassLoader#getResource(java.lang.String)
	 */
	@Override
	public URL getResource(String name)
	{
		return null;
	}
}