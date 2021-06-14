package eu.cessar.ct.core.internal.platform.ui;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

import eu.cessar.ct.core.platform.ui.AbstractCessarUIPlugin;
import eu.cessar.ct.core.platform.ui.PlatformUIConstants;

/**
 * The activator class controls the plug-in life cycle
 */
public class CessarPluginActivator extends AbstractCessarUIPlugin
{

	// The plug-in ID
	public static final String PLUGIN_ID = "eu.cessar.ct.core.platform.ui"; //$NON-NLS-1$

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

		if (isLicenseValid())
		{
			Bundle bundle = plugin.getBundle();
			// add to registry the image for reference dialog
			ImageDescriptor mvImage = ImageDescriptor.createFromURL(FileLocator.find(bundle, new Path(
				"images/MultiValueDialog.png"), null)); //$NON-NLS-1$
			registry.put(PlatformUIConstants.IMAGE_ID_MULTI_VALUE, mvImage);

			ImageDescriptor booleanImage = ImageDescriptor.createFromURL(FileLocator.find(bundle, new Path(
				"images/full/obj16/BooleanParamDef.gif"), null)); //$NON-NLS-1$
			registry.put(PlatformUIConstants.IMAGE_ID_BOOLEAN, booleanImage);

			ImageDescriptor integerImage = ImageDescriptor.createFromURL(FileLocator.find(bundle, new Path(
				"images/full/obj16/IntegerParamDef.gif"), null)); //$NON-NLS-1$
			registry.put(PlatformUIConstants.IMAGE_ID_INTEGER, integerImage);

			ImageDescriptor longImage = ImageDescriptor.createFromURL(FileLocator.find(bundle, new Path(
				"images/full/obj16/LongParamDef.gif"), null)); //$NON-NLS-1$
			registry.put(PlatformUIConstants.IMAGE_ID_LONG, longImage);

			ImageDescriptor floatImage = ImageDescriptor.createFromURL(FileLocator.find(bundle, new Path(
				"images/full/obj16/FloatParamDef.gif"), null)); //$NON-NLS-1$
			registry.put(PlatformUIConstants.IMAGE_ID_FLOAT, floatImage);

			ImageDescriptor enumImage = ImageDescriptor.createFromURL(FileLocator.find(bundle, new Path(
				"images/full/obj16/EnumerationParamDef.gif"), null)); //$NON-NLS-1$
			registry.put(PlatformUIConstants.IMAGE_ID_ENUMERATION, enumImage);

			ImageDescriptor stringImage = ImageDescriptor.createFromURL(FileLocator.find(bundle, new Path(
				"images/full/obj16/StringParamDef.gif"), null)); //$NON-NLS-1$
			registry.put(PlatformUIConstants.IMAGE_ID_STRING, stringImage);

			ImageDescriptor dateTimeImage = ImageDescriptor.createFromURL(FileLocator.find(bundle, new Path(
				"images/full/obj16/DateTime.gif"), null)); //$NON-NLS-1$
			registry.put(PlatformUIConstants.IMAGE_ID_DATE_TIME, dateTimeImage);

			ImageDescriptor primitiveImage = ImageDescriptor.createFromURL(FileLocator.find(bundle, new Path(
				"images/full/obj16/Primitive.gif"), null)); //$NON-NLS-1$
			registry.put(PlatformUIConstants.IMAGE_ID_PRIMITIVE, primitiveImage);

			ImageDescriptor multiplicityImage = ImageDescriptor.createFromURL(FileLocator.find(bundle, new Path(
				"images/full/obj16/Multiplicity.gif"), null)); //$NON-NLS-1$
			registry.put(PlatformUIConstants.IMAGE_ID_MULTIPLICITY, multiplicityImage);

			ImageDescriptor integerRadixImage = ImageDescriptor.createFromURL(FileLocator.find(bundle, new Path(
				"images/full/obj16/IntegerRadix.gif"), null)); //$NON-NLS-1$
			registry.put(PlatformUIConstants.IMAGE_ID_INTEGER_RADIX, integerRadixImage);

			ImageDescriptor arrowDown = ImageDescriptor.createFromURL(FileLocator.find(bundle, new Path(
				"images/full/obj16/arrow_down.png"), null)); //$NON-NLS-1$
			registry.put(PlatformUIConstants.KEY_ARROW_DOWN, arrowDown);

			ImageDescriptor arrowLeft = ImageDescriptor.createFromURL(FileLocator.find(bundle, new Path(
				"images/full/obj16/arrow_left.png"), null)); //$NON-NLS-1$
			registry.put(PlatformUIConstants.KEY_ARROW_LEFT, arrowLeft);

			ImageDescriptor arrowRight = ImageDescriptor.createFromURL(FileLocator.find(bundle, new Path(
				"images/full/obj16/arrow_right.png"), null)); //$NON-NLS-1$
			registry.put(PlatformUIConstants.KEY_ARROW_RIGHT, arrowRight);

			ImageDescriptor linkWithEditor = ImageDescriptor.createFromURL(FileLocator.find(bundle, new Path(
				"images/synced.gif"), null)); //$NON-NLS-1$
			registry.put(PlatformUIConstants.IMAGE_LINK_WITH_EDITOR, linkWithEditor);

			ImageDescriptor collapseAll = ImageDescriptor.createFromURL(FileLocator.find(bundle, new Path(
				"images/collapse_all_mini.gif"), null)); //$NON-NLS-1$
			registry.put(PlatformUIConstants.IMAGE_COLLAPSE_ALL, collapseAll);

			ImageDescriptor expandAll = ImageDescriptor.createFromURL(FileLocator.find(bundle, new Path(
				"images/expandall.gif"), null)); //$NON-NLS-1$
			registry.put(PlatformUIConstants.IMAGE_EXPAND_ALL, expandAll);

		}

	}
}
