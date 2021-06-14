/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Feb 2, 2010 8:41:14 PM </copyright>
 */
package eu.cessar.ct.runtime.ecuc.internal.pm.asm;

import org.eclipse.core.resources.IProject;
import org.eclipse.emf.ecore.EFactory;

import eu.cessar.ct.emfproxy.IEMFProxyEngine;
import eu.cessar.ct.emfproxy.impl.ProxifiedEPackageImpl;
import eu.cessar.ct.runtime.CessarRuntime;
import eu.cessar.ct.runtime.execution.IExecutionLoader;
import eu.cessar.ct.sdk.pm.PMRuntimeException;

/**
 * 
 */
public class DelegatedEPackageImpl extends ProxifiedEPackageImpl
{

	private EFactory factoryInstance;
	private ClassLoader classLoader;
	private final IProject project;

	public DelegatedEPackageImpl(IProject project, IEMFProxyEngine proxyEngine)
	{
		super(proxyEngine);
		this.project = project;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.emf.ecore.impl.EPackageImpl#getEFactoryInstance()
	 */
	@Override
	public EFactory getEFactoryInstance()
	{
		if (factoryInstance != null)
		{
			return factoryInstance;
		}
		else
		{
			ClassLoader classLoader = getRuntimeClassLoader();
			if (classLoader != null)
			{
				String factoryClass = ASMNames.getEFactoryClassCName(Constants.DOT, this);
				try
				{
					Class<?> fctClass = classLoader.loadClass(factoryClass);
					factoryInstance = (EFactory) fctClass.newInstance();
					return factoryInstance;
				}
				catch (Throwable t)
				{
					throw new PMRuntimeException(t);
				}
			}
			// not executed from a task manager, or class not found, maybe next
			// time
			return super.getEFactoryInstance();
		}
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.emfproxy.IProxifiedEPackage#getRuntimeClassLoader()
	 */
	@Override
	public ClassLoader getRuntimeClassLoader()
	{
		if (classLoader == null)
		{
			IExecutionLoader executionLoader = CessarRuntime.getExecutionSupport().getActiveExecutionLoader(
				project);
			if (executionLoader != null)
			{
				classLoader = executionLoader.getClassLoader();
			}
		}
		return classLoader;
	}

	/**
	 * 
	 */
	public void clearBinaryCache()
	{
		factoryInstance = null;
		classLoader = null;
	}
}
