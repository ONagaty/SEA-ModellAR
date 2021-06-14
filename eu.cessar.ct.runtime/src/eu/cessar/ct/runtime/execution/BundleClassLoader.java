/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Nov 19, 2009 3:11:48 PM </copyright>
 */
package eu.cessar.ct.runtime.execution;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;

/**
 * This class loader delegate the class loading request to the class loaders of
 * bundles.
 */
public class BundleClassLoader extends ClassLoader
{

	private final String[] bundleIDs;

	private Bundle[] bundles;

	/**
	 * Create the class loaders that will respond on request by using the
	 * already initialized bundles
	 * 
	 * @param bundleIDs
	 */
	public BundleClassLoader(String[] bundleIDs)
	{
		super();
		this.bundleIDs = new String[bundleIDs.length];
		System.arraycopy(bundleIDs, 0, this.bundleIDs, 0, bundleIDs.length);
	}

	/**
	 * @param bundleIDs
	 */
	public BundleClassLoader(List<String> bundleIDs)
	{
		super();
		this.bundleIDs = bundleIDs.toArray(new String[bundleIDs.size()]);
	}

	/**
	 * Check if the bundle objects are available and locate them if necessary
	 */
	private synchronized void checkInit()
	{
		// Convert from bundleIDs string to Bundle objects if necessary
		if (bundles == null)
		{
			bundles = new Bundle[bundleIDs.length];
			for (int i = 0; i < bundles.length; i++)
			{
				bundles[i] = Platform.getBundle(bundleIDs[i]);
			}
		}
	}

	/* (non-Javadoc)
	 * @see java.lang.ClassLoader#findClass(java.lang.String)
	 */
	@Override
	protected Class<?> findClass(String name) throws ClassNotFoundException
	{
		checkInit();
		for (int i = 0; i < bundles.length; i++)
		{
			try
			{
				return bundles[i].loadClass(name);
			}
			catch (ClassNotFoundException e)
			{
				// Ignore the error for allow searching for classes in the rest
				// of the bundles.
			}
		}
		throw new ClassNotFoundException(name);
	}

	/* (non-Javadoc)
	 * @see java.lang.ClassLoader#findResource(java.lang.String)
	 */
	@Override
	protected URL findResource(String name)
	{
		checkInit();
		for (int i = 0; i < bundles.length; i++)
		{
			URL result = bundles[i].getResource(name);
			if (result != null)
			{
				return result;
			}
		}
		// resource not found
		return null;
	}

	/* (non-Javadoc)
	 * @see java.lang.ClassLoader#findResources(java.lang.String)
	 */
	@Override
	protected Enumeration<URL> findResources(String name) throws IOException
	{
		List<URL> result = new ArrayList<URL>();
		URL single = findResource(name);
		if (single != null)
		{
			result.add(single);
		}
		return Collections.enumeration(result);
	}

}
