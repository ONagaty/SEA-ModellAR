package eu.cessar.ct.runtime.ecuc.internal;

import org.osgi.framework.BundleContext;

import eu.cessar.ct.core.platform.AbstractCessarPlugin;

/**
 * The activator class controls the plug-in life cycle
 */
public class CessarPluginActivator extends AbstractCessarPlugin
{

	// The plug-in ID
	public static final String PLUGIN_ID = "eu.cessar.ct.runtime.ecuc"; //$NON-NLS-1$

	private static CessarPluginActivator plugin;

	public static boolean DBG_MODEL_LOADING = false;
	public static boolean DBG_MODEL_LOADING_MODULE_DEF = false;
	public static boolean DBG_MODEL_LOADING_MODULE_CFG = false;

	/* (non-Javadoc)
	 * @see eu.cessar.ct.core.platform.AbstractCessarPlugin#start(org.osgi.framework.BundleContext)
	 */
	@Override
	public void start(BundleContext context) throws Exception
	{
		super.start(context);
		plugin = this;
		if (isLicenseValid())
		{
			initDebugOptions();
		}
	}

	/**
	 * 
	 */
	private void initDebugOptions()
	{
		DBG_MODEL_LOADING = Boolean.valueOf(getDebugOption("/ecuc/model/loading/trace")); //$NON-NLS-1$
		DBG_MODEL_LOADING_MODULE_DEF = Boolean.valueOf(getDebugOption("/ecuc/model/loading/trace/moduleDef")); //$NON-NLS-1$
		DBG_MODEL_LOADING_MODULE_CFG = Boolean.valueOf(getDebugOption("/ecuc/model/loading/trace/moduleCfg")); //$NON-NLS-1$
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.core.platform.AbstractCessarPlugin#stop(org.osgi.framework.BundleContext)
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