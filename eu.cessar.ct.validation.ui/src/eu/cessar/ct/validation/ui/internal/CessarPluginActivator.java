/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidl6458<br/>
 * 17.09.2012 16:02:31
 *
 * </copyright>
 */
package eu.cessar.ct.validation.ui.internal;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

import eu.cessar.ct.core.platform.ui.AbstractCessarUIPlugin;

/**
 * The activator class controls the plug-in life cycle
 *
 * @author uidl6458
 *
 *         %created_by: uidl7321 % %date_created: Wed Feb  4 15:15:23 2015 % %version: 4 %
 */
public class CessarPluginActivator extends AbstractCessarUIPlugin
{

	/**
	 * The ID of the Plugin
	 */
	public static final String PLUGIN_ID = "eu.cessar.ct.validation.ui"; //$NON-NLS-1$

	/**
	 * image used for tree expanded
	 */
	public static final String EXPANDED_ICON_ID = "expanded.png"; //$NON-NLS-1$
	/**
	 * image used for tree collapsed
	 */
	public static final String COLLAPSED_ICON_ID = "collapsed.png"; //$NON-NLS-1$

	// The shared instance
	private static CessarPluginActivator plugin;

	/*
	 * (non-Javadoc)
	 * 
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
	 * 
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#initializeImageRegistry(org.eclipse.jface.resource.ImageRegistry)
	 */
	@Override
	protected void initializeImageRegistry(ImageRegistry registry)
	{
		super.initializeImageRegistry(registry);
		Bundle bundle = getDefault().getBundle();

		ImageDescriptor image = ImageDescriptor.createFromURL(FileLocator.find(bundle,
			new Path("icons/expanded.png"), null)); //$NON-NLS-1$
		registry.put(EXPANDED_ICON_ID, image);

		image = ImageDescriptor.createFromURL(FileLocator.find(bundle, new Path("icons/collapsed.png"), null)); //$NON-NLS-1$
		registry.put(COLLAPSED_ICON_ID, image);

	}

	/**
	 * Returns an image descriptor for the image with the key <code>key</code> that has been priorly registered into the
	 * plugin image registry
	 *
	 * @param key
	 *
	 * @param path
	 *        the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String key)
	{
		return getDefault().getImageRegistry().getDescriptor(key);
	}

}
