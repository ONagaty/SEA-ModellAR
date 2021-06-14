package eu.cessar.ct.workspace.ui.internal;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.osgi.framework.BundleContext;

import eu.cessar.ct.core.platform.ui.AbstractCessarUIPlugin;
import eu.cessar.ct.workspace.logging.LoggerFactoryImpl;
import eu.cessar.ct.workspace.ui.logging.LoggerFactoryListener;

/**
 * The activator class controls the plug-in life cycle
 */
public class CessarPluginActivator extends AbstractCessarUIPlugin
{
	// The plug-in ID
	public static final String PLUGIN_ID = "eu.cessar.ct.workspace.ui"; //$NON-NLS-1$

	public static final String EDITOR_ARLOGO_ICON_ID = "arlogo16.png";

	// The shared instance
	private static CessarPluginActivator plugin;

	LoggerFactoryListener logFactoryListener = new LoggerFactoryListener();

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

		if (isLicenseValid())
		{
			LoggerFactoryImpl.getInstance().addLoggerFactoryListener(logFactoryListener);
		}
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
		if (isLicenseValid())
		{
			LoggerFactoryImpl.getInstance().removeLoggerFactoryListener(logFactoryListener);
		}
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
	protected void initializeImageRegistry(ImageRegistry reg)
	{
		super.initializeImageRegistry(reg);

		if (isLicenseValid())
		{
			// add to registry the image for reference dialog
			ImageDescriptor decoImage = ImageDescriptor.createFromURL(FileLocator.find(getBundle(), new Path(
				"images/decorator.jpg"), null)); //$NON-NLS-1$
			reg.put(WorkspaceUIConstants.IMAGE_ID_RO_DECO, decoImage);

			// add to registry the image for reference dialog
			ImageDescriptor projectPhaseImage = ImageDescriptor.createFromURL(FileLocator.find(getBundle(), new Path(
				"images/production_phase_decorator.jpg"), null)); //$NON-NLS-1$
			reg.put(WorkspaceUIConstants.IMAGE_ID_PROJECT_PHASE_DECO, projectPhaseImage);

			// add artop log for read only to read write dialog
			ImageDescriptor image = ImageDescriptor.createFromURL(FileLocator.find(getBundle(), new Path(
				"icons/arlogo16.png"), null));
			reg.put(EDITOR_ARLOGO_ICON_ID, image);

		}
	}

}
