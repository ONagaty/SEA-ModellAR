/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Nov 20, 2009 9:15:30 AM </copyright>
 */
package eu.cessar.ct.runtime.execution;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A composite class loader will delegate load request to other class loaders.
 * The order in which the class loaders are added is relevant.
 */
public abstract class CompositeClassLoader extends ClassLoader
{
	protected List<ClassLoader> classLoaders;

	private Set<ClassLoader> loadStack;

	public CompositeClassLoader()
	{
		classLoaders = new ArrayList<ClassLoader>();
		loadStack = new HashSet<ClassLoader>();
	}

	/**
	 * Add to this class loader all the class loader from the argument
	 * 
	 * @param classLoaders
	 *        a list of class loaders
	 */
	public void addClassLoaders(List<ClassLoader> classLoaders)
	{
		this.classLoaders.addAll(classLoaders);
	}

	/**
	 * Add to this class loader all the class loader from the argument
	 * 
	 * @param classLoaders
	 *        an array of class loaders
	 */
	public void addClassLoaders(ClassLoader[] classLoaders)
	{
		this.classLoaders.addAll(Arrays.asList(classLoaders));
	}

	/**
	 * Add the <code>classLoader</code> argument to the end of the existing
	 * class loaders
	 * 
	 * @param classLoader
	 *        a ClassLoader
	 */
	public void addClassLoader(ClassLoader classLoader)
	{
		classLoaders.add(classLoader);
	}

	/* (non-Javadoc)
	 * @see java.lang.ClassLoader#getResource(java.lang.String)
	 */
	@Override
	public URL getResource(String name)
	{
		// delegate to the classLoaders, return null if not found
		for (int i = classLoaders.size() - 1; i >= 0; i--)
		{
			ClassLoader classLoader = classLoaders.get(i);
			URL resource = classLoader.getResource(name);
			if (resource != null)
			{
				return resource;
			}
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see java.lang.ClassLoader#findClass(java.lang.String)
	 */
	@Override
	protected Class<?> findClass(String name) throws ClassNotFoundException
	{
		// delegate to the classLoaders, throw ClassNotFoundException if not
		// found
		for (int i = classLoaders.size() - 1; i >= 0; i--)
		{
			ClassLoader classLoader = classLoaders.get(i);
			if (loadStack.add(classLoader))
			{
				try
				{
					Class<?> loadedClass = classLoader.loadClass(name);
					return loadedClass;
				}
				catch (ClassNotFoundException e)
				{
					// ignore the error, will throw at end
					continue;
				}
				finally
				{
					loadStack.remove(classLoader);
				}
			}
		}
		throw new ClassNotFoundException(name);
	}
}
