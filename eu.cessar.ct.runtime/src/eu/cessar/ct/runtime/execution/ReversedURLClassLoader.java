/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Jan 27, 2010 5:24:00 PM </copyright>
 */
package eu.cessar.ct.runtime.execution;

import java.net.URL;
import java.net.URLStreamHandlerFactory;

/**
 * A ReversedURLClassLoader works exactly as an URLClassLoader with the
 * difference that it will first try to locate classes inside the URLs then
 * inside the parent
 * 
 */
public class ReversedURLClassLoader extends ClosableURLClassLoader
{

	/**
	 * @param urls
	 * @param parent
	 * @param factory
	 */
	public ReversedURLClassLoader(URL[] urls, ClassLoader parent, URLStreamHandlerFactory factory)
	{
		super(urls, parent, factory);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param urls
	 * @param parent
	 */
	public ReversedURLClassLoader(URL[] urls, ClassLoader parent)
	{
		super(urls, parent);
	}

	/**
	 * @param urls
	 */
	public ReversedURLClassLoader(URL[] urls)
	{
		super(urls);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see java.lang.ClassLoader#loadClass(java.lang.String, boolean)
	 */
	@Override
	protected synchronized Class<?> loadClass(String name, boolean resolve)
		throws ClassNotFoundException
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
}
