/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Apr 30, 2010 12:57:54 PM </copyright>
 */
package eu.cessar.ct.runtime.internal.execution;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.Assert;

import eu.cessar.ct.runtime.execution.BundleClassLoader;
import eu.cessar.ct.runtime.execution.IBinaryClassResolver;
import eu.cessar.ct.runtime.execution.IModifiableExecutionLoader;
import eu.cessar.ct.runtime.execution.ReversedURLClassLoader;

/**
 * @author uidl6458
 * 
 */
public class ModifiableExecutionLoader extends ExecutionLoader implements
	IModifiableExecutionLoader
{

	private List<String> bundleIDs = new ArrayList<String>();

	private IBinaryClassResolver binaryResolver;

	private List<URL> customLibs = new ArrayList<URL>();

	private DelegatedClassLoader binaryClassLoader;

	private BundleClassLoader bundleClassLoader;

	private ReversedURLClassLoader customLibLoader;

	private ClassLoader classLoader;

	/**
	 * @param project
	 */
	public ModifiableExecutionLoader(IProject project)
	{
		super(project);
	}

	/**
	 * 
	 */
	private synchronized void initClassLoader()
	{
		if (classLoader == null)
		{
			ClassLoader parentLoader = null;
			bundleClassLoader = new BundleClassLoader(bundleIDs);
			parentLoader = bundleClassLoader;

			if (binaryResolver != null)
			{
				binaryClassLoader = new DelegatedClassLoader(parentLoader, binaryResolver);
				parentLoader = binaryClassLoader;
			}
			if (customLibs.size() > 0)
			{
				URL[] urls = customLibs.toArray(new URL[customLibs.size()]);
				customLibLoader = new ReversedURLClassLoader(urls, parentLoader);
				parentLoader = customLibLoader;
			}
			classLoader = parentLoader;
		}

	}

	/**
	 * 
	 */
	private void checkModifiable()
	{
		Assert.isTrue(classLoader == null, "Class loader already created, modifications forbidded"); //$NON-NLS-1$
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.runtime.execution.IExecutionLoader#getClassLoader()
	 */
	public ClassLoader getClassLoader()
	{
		initClassLoader();
		return classLoader;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.runtime.execution.IModifiableExecutionLoader#dispose()
	 */
	public synchronized void dispose()
	{
		classLoader = null;
		binaryClassLoader = null;
		bundleClassLoader = null;
		if (customLibLoader != null)
		{
			customLibLoader.close();
			customLibLoader = null;
		}
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.runtime.execution.IModifiableExecutionLoader#getBundleIDs()
	 */
	public List<String> getBundleIDs()
	{
		checkModifiable();
		return bundleIDs;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.runtime.execution.IModifiableExecutionLoader#getCustomLibraries()
	 */
	public List<URL> getCustomLibraries()
	{
		checkModifiable();
		return customLibs;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.runtime.execution.IModifiableExecutionLoader#setBinaryClassResolver(eu.cessar.ct.runtime.execution.IBinaryClassResolver)
	 */
	public void setBinaryClassResolver(IBinaryClassResolver resolver)
	{
		checkModifiable();
		binaryResolver = resolver;
	}

}
