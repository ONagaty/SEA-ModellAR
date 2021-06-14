package eu.cessar.ct.core.mms.internal;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.content.IContentType;
import org.osgi.framework.BundleContext;

import eu.cessar.ct.core.platform.AbstractCessarPlugin;
import eu.cessar.ct.core.security.descriptor.MMDescriptorUtils;

/**
 * The activator class controls the plug-in life cycle
 */
public class CessarPluginActivator extends AbstractCessarPlugin
{

	// The plug-in ID
	/**
	 *
	 */
	public static final String PLUGIN_ID = "eu.cessar.ct.core.mms"; //$NON-NLS-1$

	// The shared instance
	private static CessarPluginActivator plugin;

	/**
	 * The constructor
	 */
	public CessarPluginActivator()
	{
		// nothing to do
	}

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

		updateARContentTypePreferences();
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

	/**
	 * Update custom AR extensions
	 *
	 * @param metaModelDescriptors
	 */
	private void updateARContentTypePreferences()
	{
		IContentType[] allContentTypes = Platform.getContentTypeManager().getAllContentTypes();

		// remove already existing AR extensions
		removeARExtensions(allContentTypes);

		String initialMM3x = System.getProperty(MMDescriptorUtils.SYS_PROP_MM3X);
		String initialMM4x = System.getProperty(MMDescriptorUtils.SYS_PROP_MM4X);

		String metamodels[] = new String[] {initialMM3x, initialMM4x};

		for (String metamodel: metamodels)
		{
			if (metamodel != null)
			{
				String extension = Messages.AR + metamodel.replace(Messages.DOT, Messages.BLANK);

				// add AR extensions for metamodel
				addARExtensions(metamodel, extension, allContentTypes);
			}
		}
	}

	/**
	 * Add custom AR extensions
	 *
	 * @param metamodel
	 * @param extension
	 * @param allContentTypes
	 */
	private void addARExtensions(String metamodel, String extension, IContentType[] allContentTypes)
	{
		for (IContentType iContentType: allContentTypes)
		{
			String name = iContentType.getName();

			if ((Messages.AUTOSAR_XML_FILE.equals(name))
				|| ((Messages.AUTOSAR_SPACE + metamodel + Messages.XML_FILE).equals(name)))
			{
				try
				{
					iContentType.addFileSpec(extension,
						org.eclipse.core.runtime.content.IContentType.FILE_EXTENSION_SPEC);
				}
				catch (CoreException e)
				{
					CessarPluginActivator.getDefault().logError(e);
				}
			}
		}
	}

	/**
	 * Remove already existing ar extension
	 *
	 * @param allContentTypes
	 */
	private void removeARExtensions(IContentType[] allContentTypes)
	{
		for (IContentType iContentType: allContentTypes)
		{
			String name = iContentType.getName();

			if (name.contains(Messages.AUTOSAR))
			{
				String[] fileSpecs = iContentType.getFileSpecs(IContentType.FILE_EXTENSION_SPEC);
				for (String ext: fileSpecs)
				{

					// check if extensions starts with ar and number - case sensitive
					if (ext.matches(Messages.AR_REGULAR_EXPRESSION))
					{
						try
						{
							iContentType.removeFileSpec(ext, IContentType.FILE_EXTENSION_SPEC);
						}
						catch (CoreException e)
						{
							CessarPluginActivator.getDefault().logError(e);
						}
					}
				}
			}
		}
	}
}
