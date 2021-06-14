/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Jan 29, 2010 9:42:51 AM </copyright>
 */
package eu.cessar.ct.runtime.internal.execution;

import eu.cessar.ct.runtime.execution.CompositeClassLoader;

/**
 * @author uidl6458
 * 
 */
public class CompositeClassLoaderImpl extends CompositeClassLoader
{

	/**
	 * Remove the <code>classLoader</code> from the list of class loaders
	 * 
	 * @param classLoader
	 */
	public void removeClassLoader(ClassLoader classLoader)
	{
		classLoaders.remove(classLoader);
	}

	/**
	 * @return
	 */
	public int getClassLoadersSize()
	{
		return classLoaders.size();
	}

	/**
	 * @param newSize
	 */
	public void shrinkClassLoaders(int newSize)
	{
		if (newSize < 0)
		{
			return;
		}
		while (classLoaders.size() > newSize)
		{
			classLoaders.remove(classLoaders.size() - 1);
		}
	}
}
