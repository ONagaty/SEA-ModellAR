package eu.cessar.ct.runtime.ui.internal;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

import eu.cessar.ct.core.platform.ui.AbstractCessarUIPlugin;

/**
 * The activator class controls the plug-in life cycle
 */
public class CessarPluginActivator extends AbstractCessarUIPlugin
{

	// The plug-in ID
	public static final String PLUGIN_ID = "eu.cessar.ct.runtime.ui"; //$NON-NLS-1$

	/**
	 *
	 */
	// SUPPRESS CHECKSTYLE Avoid Checkstyle errors
	public final static String CHECKED = "checked.gif"; //$NON-NLS-1$

	/**
	 *
	 */
	// SUPPRESS CHECKSTYLE Avoid Checkstyle errors
	public final static String UNCHECKED = "unchecked.gif"; //$NON-NLS-1$
	// The shared instance
	private static CessarPluginActivator plugin;

	private BundleContext context;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	@Override
	public void start(BundleContext context) throws Exception
	{
		super.start(context);
		this.context = context;
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
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

		if (isLicenseValid())
		{
			Bundle bundle = plugin.getBundle();
			// add to registry the image for reference dialog
			ImageDescriptor checked = ImageDescriptor.createFromURL(FileLocator.find(bundle, new Path(
				"icons/checked.gif"), null)); //$NON-NLS-1$
			registry.put(CessarPluginActivator.CHECKED, checked);

			ImageDescriptor unchecked = ImageDescriptor.createFromURL(FileLocator.find(bundle, new Path(
				"icons/unchecked.gif"), null)); //$NON-NLS-1$
			registry.put(CessarPluginActivator.UNCHECKED, unchecked);

		}
	}

	/**
	 * @return the context
	 */
	public BundleContext getContext()
	{
		return context;
	}

}
