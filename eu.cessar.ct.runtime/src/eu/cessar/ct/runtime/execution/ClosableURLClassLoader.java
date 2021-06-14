/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidl6458 Apr 30, 2010 4:31:16 PM </copyright>
 */
package eu.cessar.ct.runtime.execution;

import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLStreamHandlerFactory;

import eu.cessar.ct.core.platform.util.ICloseable;

/**
 * @author uidl6458
 *
 */
public class ClosableURLClassLoader extends URLClassLoader implements ICloseable
{

	/**
	 * @param urls
	 * @param parent
	 * @param factory
	 */
	public ClosableURLClassLoader(URL[] urls, ClassLoader parent, URLStreamHandlerFactory factory)
	{
		super(urls, parent, factory);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param urls
	 * @param parent
	 */
	public ClosableURLClassLoader(URL[] urls, ClassLoader parent)
	{
		super(urls, parent);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param urls
	 */
	public ClosableURLClassLoader(URL[] urls)
	{
		super(urls);
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.core.platform.util.ICloseable#close()
	 */
	@Override
	public void close()
	{
		try
		{
			super.close();
			Class<?> clazz = java.net.URLClassLoader.class;
			java.lang.reflect.Field ucp = clazz.getDeclaredField("ucp"); //$NON-NLS-1$
			ucp.setAccessible(true);
			Object sun_misc_URLClassPath = ucp.get(this);
			java.lang.reflect.Field loaders = sun_misc_URLClassPath.getClass().getDeclaredField("loaders"); //$NON-NLS-1$
			loaders.setAccessible(true);
			Object java_util_Collection = loaders.get(sun_misc_URLClassPath);
			for (Object sun_misc_URLClassPath_JarLoader: ((java.util.Collection) java_util_Collection).toArray())
			{
				try
				{
					java.lang.reflect.Field loader = sun_misc_URLClassPath_JarLoader.getClass().getDeclaredField("jar"); //$NON-NLS-1$
					loader.setAccessible(true);
					Object java_util_jar_JarFile = loader.get(sun_misc_URLClassPath_JarLoader);
					((java.util.jar.JarFile) java_util_jar_JarFile).close();
				}
				catch (Throwable t) // SUPPRESS CHECKSTYLE to be changed
				{
					// if we got this far, this is probably not a JAR loader so
					// skip it
				}
			}
		}
		catch (Throwable t) // SUPPRESS CHECKSTYLE to be changed
		{
			// probably not a SUN VM
		}
		return;
	}

}
