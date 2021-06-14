/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidl6458 Apr 30, 2010 1:12:52 PM </copyright>
 */
package eu.cessar.ct.runtime.internal.execution;

import org.eclipse.core.runtime.Assert;

import eu.cessar.ct.runtime.execution.IBinaryClassResolver;

/**
 * @author uidl6458
 *
 */
public class DelegatedClassLoader extends ClassLoader
{

	private final IBinaryClassResolver resolver;

	/**
	 * @param resolver
	 *
	 */
	public DelegatedClassLoader(IBinaryClassResolver resolver)
	{
		this(null, resolver);
	}

	/**
	 * @param parent
	 * @param resolver
	 */
	public DelegatedClassLoader(ClassLoader parent, IBinaryClassResolver resolver)
	{
		super(parent);
		Assert.isNotNull(resolver);
		this.resolver = resolver;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.ClassLoader#loadClass(java.lang.String, boolean)
	 */
	@Override
	protected synchronized Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException
	{

		// First, check if the class has already been loaded
		Class<?> c = findLoadedClass(name);
		if (c == null)
		{
			try
			{
				c = findClass(name);
			}
			catch (ClassNotFoundException e)
			{
				// look inside the parent
				ClassLoader cl = getParent();
				if (cl != null)
				{
					c = cl.loadClass(name);
				}
			}
		}
		if (resolve)
		{
			resolveClass(c);
		}
		return c;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.ClassLoader#findClass(java.lang.String)
	 */
	@Override
	protected Class<?> findClass(String name) throws ClassNotFoundException
	{
		byte[] binary = resolver.resolveClass(name);
		if (binary != null)
		{
			return defineClass(name, binary, 0, binary.length);
		}
		else
		{
			throw new ClassNotFoundException(name);
		}

	}
}
