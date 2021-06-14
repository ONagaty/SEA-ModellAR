package eu.cessar.ct.edit.ui.internal;

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
	public static final String REFERENCE_IMAGE_ID = "reference.icon"; //$NON-NLS-1$

	public static final String INSTANCE_REF_IMAGE_ID = "instance_ref.icon"; //$NON-NLS-1$
	private static final String INSTANCE_REF_IMAGE_PATH = "/icons/instanceref.png"; //$NON-NLS-1$

	public static final String MULTI_VALUE_IMAGE_ID = "multi_value.icon"; //$NON-NLS-1$

	public static final String EXECUTE_ACTION_ICON_ID = "execute_action.icon"; //$NON-NLS-1$

	/** IDs for validation icons displayed inside Property view */
	public static final String ERROR_ICON_ID = "error_tsk.gif"; //$NON-NLS-1$
	public static final String WARN_ICON_ID = "warn_tsk.gif"; //$NON-NLS-1$
	public static final String INFO_ICON_ID = "info_tsk.gif"; //$NON-NLS-1$
	public static final String WAIT_ICON_ID = "wait_tsk.gif"; //$NON-NLS-1$

	public static final String FEATURE_NAME_ICON_ID = "feature_name.gif"; //$NON-NLS-1$
	public static final String FEATURE_TYPE_ICON_ID = "feature_type.gif"; //$NON-NLS-1$

	public static final String EDITOR_DEFAULT_MULTIINPUT_ICON_ID = "default_multi_input.gif"; //$NON-NLS-1$

	public static final String TABLE_VIEW_LINC_WITH_EDITOR = "sync_with_editor.png"; //$NON-NLS-1$

	// The plug-in ID
	public static final String PLUGIN_ID = "eu.cessar.ct.edit.ui"; //$NON-NLS-1$

	// icons for the EditingFacilityComposite
	public static final String EDITOR_EXPAND_ENABLED_ICON_ID = "expand_enabled.png"; //$NON-NLS-1$
	public static final String EDITOR_EXPAND_DISABLED_ICON_ID = "expand_disabled.png"; //$NON-NLS-1$
	public static final String EDITOR_COLLAPSE_MULTIINPUT_ICON_ID = "collapse_multi_input.png"; //$NON-NLS-1$
	public static final String EDITOR_ADD_MULTIINPUT_ICON_ID = "add_multi_input.png"; //$NON-NLS-1$
	public static final String EDITOR_DELETE_MULTIINPUT_ICON_ID = "delete_multi_input.png"; //$NON-NLS-1$

	// The shared instance
	private static CessarPluginActivator plugin;

	/*
	 * (non-Javadoc)
	 * 
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
		Bundle bundle = getDefault().getBundle();

		// add to registry the image for reference dialog
		ImageDescriptor image = ImageDescriptor.createFromURL(FileLocator.find(bundle, new Path(
			"icons/ReferenceSelectionDialog.png"), null)); //$NON-NLS-1$
		registry.put(REFERENCE_IMAGE_ID, image);

		image = ImageDescriptor.createFromURL(FileLocator.find(getBundle(), new Path(INSTANCE_REF_IMAGE_PATH), null));
		registry.put(INSTANCE_REF_IMAGE_ID, image);

		// add to registry the image for multi value dialog
		image = ImageDescriptor.createFromURL(FileLocator.find(bundle, new Path("icons/MultiValueDialog.png"), null)); //$NON-NLS-1$
		registry.put(MULTI_VALUE_IMAGE_ID, image);

		// add to registry the image for reference dialog
		image = ImageDescriptor.createFromURL(FileLocator.find(bundle, new Path("icons/ExecuteAction.png"), null)); //$NON-NLS-1$
		registry.put(EXECUTE_ACTION_ICON_ID, image);

		// add error icon
		image = ImageDescriptor.createFromURL(FileLocator.find(bundle, new Path("icons/error_tsk.gif"), null)); //$NON-NLS-1$
		registry.put(ERROR_ICON_ID, image);

		// add warn icon
		image = ImageDescriptor.createFromURL(FileLocator.find(bundle, new Path("icons/warn_tsk.gif"), null)); //$NON-NLS-1$
		registry.put(WARN_ICON_ID, image);

		// add info icon
		image = ImageDescriptor.createFromURL(FileLocator.find(bundle, new Path("icons/info_tsk.gif"), null)); //$NON-NLS-1$
		registry.put(INFO_ICON_ID, image);

		image = ImageDescriptor.createFromURL(FileLocator.find(bundle, new Path("icons/wait_tsk.gif"), null)); //$NON-NLS-1$
		registry.put(WAIT_ICON_ID, image);

		// add feature name icon
		image = ImageDescriptor.createFromURL(FileLocator.find(bundle, new Path("icons/feature_name.gif"), null)); //$NON-NLS-1$
		registry.put(FEATURE_NAME_ICON_ID, image);

		// add info icon
		image = ImageDescriptor.createFromURL(FileLocator.find(bundle, new Path("icons/feature_type.gif"), null)); //$NON-NLS-1$
		registry.put(FEATURE_TYPE_ICON_ID, image);

		// add default editor icon to handle multi input
		image = ImageDescriptor.createFromURL(FileLocator.find(bundle, new Path("icons/default_multi_input.gif"), null)); //$NON-NLS-1$
		registry.put(EDITOR_DEFAULT_MULTIINPUT_ICON_ID, image);

		// add icon for sync with editor
		image = ImageDescriptor.createFromURL(FileLocator.find(bundle, new Path("icons/sync_with_editor.png"), null)); //$NON-NLS-1$
		registry.put(TABLE_VIEW_LINC_WITH_EDITOR, image);

		// add editor expand enabled icon
		image = ImageDescriptor.createFromURL(FileLocator.find(bundle, new Path("icons/expand_enabled.png"), null)); //$NON-NLS-1$
		registry.put(EDITOR_EXPAND_ENABLED_ICON_ID, image);

		// add editor expand disabled icon
		image = ImageDescriptor.createFromURL(FileLocator.find(bundle, new Path("icons/expand_disabled.png"), null)); //$NON-NLS-1$
		registry.put(EDITOR_EXPAND_DISABLED_ICON_ID, image);

		// add editor collapse icon
		image = ImageDescriptor.createFromURL(FileLocator.find(bundle, new Path("icons/collapse_multi_input.png"), null)); //$NON-NLS-1$
		registry.put(EDITOR_COLLAPSE_MULTIINPUT_ICON_ID, image);

		// add editor overlay icon for add new multi input
		image = ImageDescriptor.createFromURL(FileLocator.find(bundle, new Path("icons/add_multi_input.png"), null)); //$NON-NLS-1$
		registry.put(EDITOR_ADD_MULTIINPUT_ICON_ID, image);

		// add editor overlay icon for delete multi input
		image = ImageDescriptor.createFromURL(FileLocator.find(bundle, new Path("icons/delete_multi_input.png"), null)); //$NON-NLS-1$
		registry.put(EDITOR_DELETE_MULTIINPUT_ICON_ID, image);
	}

	/**
	 * Returns an image descriptor for the image with the key <code>key</code> that has been priorly registered into the
	 * plugin image registry
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
