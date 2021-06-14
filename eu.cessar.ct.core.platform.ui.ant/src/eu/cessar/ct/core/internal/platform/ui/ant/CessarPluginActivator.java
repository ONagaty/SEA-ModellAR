package eu.cessar.ct.core.internal.platform.ui.ant;

import org.osgi.framework.BundleContext;

import eu.cessar.ct.core.platform.ui.AbstractCessarUIPlugin;

/**
 * The activator class controls the plug-in life cycle
 */
public class CessarPluginActivator extends AbstractCessarUIPlugin
{

	// The plug-in ID
	public static final String PLUGIN_ID = "eu.cessar.ct.core.platform.ui.ant"; //$NON-NLS-1$

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
