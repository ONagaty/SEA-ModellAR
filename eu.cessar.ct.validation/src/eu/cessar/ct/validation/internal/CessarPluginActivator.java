/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870 Oct 18, 2010 4:54:45 PM </copyright>
 */
package eu.cessar.ct.validation.internal;

import org.osgi.framework.BundleContext;

import eu.cessar.ct.core.platform.AbstractCessarPlugin;

/**
 * @author uidl6870
 * 
 */
public class CessarPluginActivator extends AbstractCessarPlugin
{
	// The plug-in ID
	public static final String PLUGIN_ID = "eu.cessar.ct.validation"; //$NON-NLS-1$
	// The shared instance
	private static CessarPluginActivator plugin;

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.core.runtime.Plugins#start(org.osgi.framework.BundleContext)
	 */
	@Override
	public void start(BundleContext context) throws Exception
	{
		super.start(context);
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.core.runtime.Plugin#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(BundleContext context) throws Exception
	{
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static CessarPluginActivator getDefault()
	{
		return plugin;
	}
}
