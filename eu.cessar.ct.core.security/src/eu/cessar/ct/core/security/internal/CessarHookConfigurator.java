/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidt2045 Jun 22, 2011 9:48:01 AM </copyright>
 */
package eu.cessar.ct.core.security.internal;

import org.eclipse.osgi.internal.hookregistry.HookConfigurator;
import org.eclipse.osgi.internal.hookregistry.HookRegistry;

/**
 * @author uidt2045
 *
 */
@SuppressWarnings("restriction")
public class CessarHookConfigurator implements HookConfigurator
{

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.osgi.baseadaptor.HookConfigurator#addHooks(org.eclipse.osgi.baseadaptor.HookRegistry)
	 */
	public void addHooks(HookRegistry hookRegistry)
	{
		// CessarAdaptorHook adaptorHook = new CessarAdaptorHook();
		// hookRegistry.addAdaptorHook(adaptorHook); // replaced by addActivatorHookFactory
		hookRegistry.addActivatorHookFactory(MMSelectorHook.FACTORY);

		CessarClassLoadingHook clHook = new CessarClassLoadingHook();
		hookRegistry.addClassLoaderHook(clHook); // replaced by addClassLoaderHook

		CessarBundleFileWrapperFactoryHook cessarBundlerWrapper = new CessarBundleFileWrapperFactoryHook();
		hookRegistry.addBundleFileWrapperFactoryHook(cessarBundlerWrapper);

		// System.err.println("Hooks installed");
	}
}
