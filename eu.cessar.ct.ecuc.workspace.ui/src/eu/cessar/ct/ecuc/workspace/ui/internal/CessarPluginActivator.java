package eu.cessar.ct.ecuc.workspace.ui.internal;

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
	// @TODO: Add logging facilities

	// The plug-in ID
	public static final String PLUGIN_ID = "eu.cessar.ct.ui.wizards"; //$NON-NLS-1$

	// Images
	public static final String ICON_BSW_PROJECT = "BswProject"; //$NON-NLS-1$
	public static final String ICON_BSW_MODULE = "BswModule"; //$NON-NLS-1$
	public static final String ICON_AR_PACKAGE = "ARPackage"; //$NON-NLS-1$
	public static final String ICON_ECUC_FILE = "EcuCFile"; //$NON-NLS-1$
	public static final String ICON_REGULAR_FILE = "RegularFile"; //$NON-NLS-1$
	public static final String ICON_FOLDER = "Folder"; //$NON-NLS-1$

	// The shared instance
	private static CessarPluginActivator plugin;

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	@Override
	public void start(BundleContext context) throws Exception
	{
		super.start(context);
		plugin = this;
	}

	/*
	 * (non-Javadoc)
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

	/**
	 * Returns an image descriptor for the image file at the given plug-in
	 * relative path
	 * 
	 * @param path
	 *        the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path)
	{
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#initializeImageRegistry(org.eclipse.jface.resource.ImageRegistry)
	 */
	@Override
	protected void initializeImageRegistry(ImageRegistry registry)
	{
		super.initializeImageRegistry(registry);

		if (isLicenseValid())
		{
			Bundle bundle = getDefault().getBundle();

			ImageDescriptor refImage = ImageDescriptor.createFromURL(FileLocator.find(bundle,
				new Path("icons/bswProject.gif"), null)); //$NON-NLS-1$
			registry.put(ICON_BSW_PROJECT, refImage);
			refImage = ImageDescriptor.createFromURL(FileLocator.find(bundle, new Path(
				"icons/bsw_md.gif"), null)); //$NON-NLS-1$
			registry.put(ICON_BSW_MODULE, refImage);
			refImage = ImageDescriptor.createFromURL(FileLocator.find(bundle, new Path(
				"icons/ARPackage.gif"), null)); //$NON-NLS-1$
			registry.put(ICON_AR_PACKAGE, refImage);

			refImage = ImageDescriptor.createFromURL(FileLocator.find(bundle, new Path(
				"icons/ecuc_file.gif"), null)); //$NON-NLS-1$
			registry.put(ICON_ECUC_FILE, refImage);
			refImage = ImageDescriptor.createFromURL(FileLocator.find(bundle, new Path(
				"icons/regular_file.gif"), null)); //$NON-NLS-1$
			registry.put(ICON_REGULAR_FILE, refImage);

			refImage = ImageDescriptor.createFromURL(FileLocator.find(bundle, new Path(
				"icons/folder.gif"), null)); //$NON-NLS-1$
			registry.put(ICON_FOLDER, refImage);
		}
	}
}
