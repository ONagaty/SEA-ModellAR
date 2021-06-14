package eu.cessar.ct.runtime.internal;

import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.ResourcesPlugin;
import org.osgi.framework.BundleContext;

import eu.cessar.ct.core.platform.AbstractCessarPlugin;
import eu.cessar.ct.runtime.internal.classpath.ProjectLibrariesManager;
import eu.cessar.ct.runtime.internal.classpath.util.BundleVariableRegistry;
import eu.cessar.ct.runtime.internal.extension.ExtensionFileManager;

/**
 * The activator class controls the plug-in life cycle
 */
public class CessarPluginActivator extends AbstractCessarPlugin
{

	// The plug-in ID
	public static final String PLUGIN_ID = "eu.cessar.ct.runtime"; //$NON-NLS-1$

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
		if (isLicenseValid())
		{
			BundleVariableRegistry.INSTANCE.init();

			registerListener(ProjectLibrariesManager.eINSTANCE);
			registerListener(ExtensionFileManager.CES_INSTANCE);
			registerListener(ExtensionFileManager.CED_INSTANCE);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.core.runtime.Plugin#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(BundleContext context) throws Exception
	{
		try
		{
			if (isLicenseValid())
			{
				unRegisterListener(ProjectLibrariesManager.eINSTANCE);
				unRegisterListener(ExtensionFileManager.CED_INSTANCE);
				unRegisterListener(ExtensionFileManager.CES_INSTANCE);

				BundleVariableRegistry.INSTANCE.dispose();
			}
		}
		finally
		{
			plugin = null;
			super.stop(context);
		}
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

	private void registerListener(IResourceChangeListener listener)
	{
		// CHECKSTYLE:OFF
		ResourcesPlugin.getWorkspace().addResourceChangeListener(
			listener,
			IResourceChangeEvent.POST_BUILD | IResourceChangeEvent.POST_CHANGE
				| IResourceChangeEvent.PRE_BUILD | IResourceChangeEvent.PRE_CLOSE
				| IResourceChangeEvent.PRE_DELETE | IResourceChangeEvent.PRE_REFRESH);
		// CHECKSTYLE:ON
	}

	private void unRegisterListener(IResourceChangeListener listener)
	{
		ResourcesPlugin.getWorkspace().removeResourceChangeListener(listener);
	}
}
