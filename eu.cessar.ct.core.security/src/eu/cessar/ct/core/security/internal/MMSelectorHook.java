/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidl6458<br/>
 * 6 apr. 2015 17:51:39
 *
 * </copyright>
 */
package eu.cessar.ct.core.security.internal;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.osgi.internal.hookregistry.ActivatorHookFactory;
import org.eclipse.osgi.service.environment.EnvironmentInfo;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import eu.cessar.ct.core.security.descriptor.MMDescriptor;
import eu.cessar.ct.core.security.descriptor.MMDescriptorUtils;

/**
 * The activator hook that take care to select the right metamodels
 *
 * @author uidl6458
 *
 *         %created_by: uidl7321 %
 *
 *         %date_created: Wed Jul 1 16:19:27 2015 %
 *
 *         %version: 1 %
 */
@SuppressWarnings("restriction")
public final class MMSelectorHook implements BundleActivator
{

	/**
	 * The factory of the hook
	 */
	public static final ActivatorHookFactory FACTORY = new ActivatorHookFactory()
	{

		/*
		 * (non-Javadoc)
		 *
		 * @see org.eclipse.osgi.internal.hookregistry.ActivatorHookFactory#createActivator()
		 */
		@Override
		public BundleActivator createActivator()
		{
			// TODO Auto-generated method stub
			return new MMSelectorHook();
		}

	};

	private static final String OSGI_CONFIGURATION_AREA = "osgi.configuration.area"; //$NON-NLS-1$

	// constant copied from org.eclipse.equinox.internal.simpleconfigurator.utils.SimpleConfiguratorConstants
	private static final String PROP_KEY_CONFIGURL = "org.eclipse.equinox.simpleconfigurator.configUrl"; //$NON-NLS-1$

	private static final String CONFIGURATOR_FOLDER = "org.eclipse.equinox.simpleconfigurator"; //$NON-NLS-1$

	private static final String CONFIG_LIST = "bundles.info"; //$NON-NLS-1$

	private static boolean removeUI = false;

	// used to remove unnecessary UI bundles when running in non UI mode
	private static final String[] ADDITIONAL_REMOVED_UI_BUNDLES = {"org.artop.artext.ui", //$NON-NLS-1$
		"org.artop.aal.examples.browser", "org.artop.artext.formula.ui", "org.artop.artext.variationpoint.ui", //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$
		"org.artop.artext.variant.ui", "org.artop.artext.timing.ui", "org.artop.artext.ecuc.ui", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		"org.artop.artext.bswmc.ui", "org.artop.artext.swcd.ui", "org.artop.artext.variationpoint.ui.example", //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$
		"eu.cessar.ct.variant.r40.ui"}; //$NON-NLS-1$

	private static final String[] REMOVED_BUNDLES = {"org.artop.aal.examples.common", "org.artop.aal.autosar445.edit", //$NON-NLS-1$ //$NON-NLS-2$
		"org.artop.aal.autosar445", "com.continental_corporation.automotive.powertrain.artop.autosar.r445.source", //$NON-NLS-1$//$NON-NLS-2$
		"com.continental_corporation.automotive.powertrain.artop.autosar.r445", "org.artop.aal.autosar445.edit.source", //$NON-NLS-1$//$NON-NLS-2$
		"org.artop.aal.autosar445.services.source", "org.artop.aal.autosar445.services", //$NON-NLS-1$ //$NON-NLS-2$
		"org.artop.aal.autosar445.source", "org.artop.aal.autosar443.edit", "org.artop.aal.autosar443", //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
		"org.artop.aal.autosar443.edit.source", "org.artop.aal.autosar443.services.source", //$NON-NLS-1$//$NON-NLS-2$
		"org.artop.aal.autosar443.services", "org.artop.aal.autosar443.source", "org.artop.aal.autosar442.edit", //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
		"org.artop.aal.autosar442", "org.artop.aal.autosar442.edit.source", "org.artop.aal.autosar442.services.source", //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$
		"org.artop.aal.autosar442.services", "org.artop.aal.autosar442.source"}; //$NON-NLS-1$//$NON-NLS-2$

	private MMDescriptor active4xMMDescriptor = MMDescriptorUtils.getDefaultMMDescriptor4X();

	private MMDescriptor active3xMMDescriptor = MMDescriptorUtils.getDefaultMMDescriptor3X();

	/**
	 *
	 */
	private MMSelectorHook()
	{

		// if the MM 4x property is set it will take precedence of MM 40
		String mmStringID = System.getProperty(MMDescriptorUtils.SYS_PROP_MM4X);
		String property = System.getProperty("eclipse.commands"); //$NON-NLS-1$
		// Check if tool starts as application or product, products are always UI and applications are non UI
		if (!property.contains("-product\neu.cessar.ct.product.CESSAR-CT")) //$NON-NLS-1$
		{
			removeUI = true;
			// System.out.println("Remove UI Components"); //$NON-NLS-1$
		}
		if (mmStringID != null)
		{
			System.setProperty(MMDescriptorUtils.SYS_PROP_MM40, mmStringID);
		}
		active4xMMDescriptor = lookupMMDescriptor(MMDescriptorUtils.SYS_PROP_MM40, MMDescriptorUtils.MM_DESCRIPTORS_4,
			MMDescriptorUtils.getDefaultMMDescriptor4X());
		// be sure that both properties are set for backward compatibility reasons
		System.setProperty(MMDescriptorUtils.SYS_PROP_MM4X, System.getProperty(MMDescriptorUtils.SYS_PROP_MM40));

		active3xMMDescriptor = lookupMMDescriptor(MMDescriptorUtils.SYS_PROP_MM3X, MMDescriptorUtils.MM_DESCRIPTORS_3,
			MMDescriptorUtils.getDefaultMMDescriptor3X());
	}

	/**
	 * @param spec
	 * @return
	 * @throws MalformedURLException
	 */
	private static URL buildURL(String spec) throws MalformedURLException
	{
		if (spec == null)
		{
			throw new NullPointerException("URL spec is null."); //$NON-NLS-1$
		}
		// Construct the URL carefully so as to preserve UNC paths etc.
		if (spec.startsWith("file:")) //$NON-NLS-1$
		{
			// need to do this for UNC paths
			File file = new File(spec.substring(5));
			if (file.isAbsolute())
			{
				return file.toURI().toURL();
			}
		}
		return new URL(spec);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(BundleContext context) throws Exception
	{
		// nothing to do
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	@Override
	public void start(BundleContext context) throws Exception
	{
		ServiceReference<EnvironmentInfo> reference = context.getServiceReference(EnvironmentInfo.class);
		if (reference == null)
		{
			// nothing to do, exit.
			return;
		}
		EnvironmentInfo info = context.getService(reference);
		URL url = getConfigurationURL(info);
		if (url != null)
		{
			// load the configuration
			try
			{
				File tempBuildInfo = File.createTempFile("CESSAR_CT_" + System.currentTimeMillis(), null); //$NON-NLS-1$
				tempBuildInfo.deleteOnExit();
				BufferedReader source = new BufferedReader(new InputStreamReader(url.openStream()));
				BufferedWriter target = new BufferedWriter(new FileWriter(tempBuildInfo));
				try
				{
					migrateBundlesInfo(source, target);
				}
				finally
				{
					target.close();
					source.close();
				}
				// looks good, change the configUrl

				// System.err.println(tempBuildInfo.toURI().toURL().toExternalForm());
				info.setProperty(PROP_KEY_CONFIGURL, tempBuildInfo.toURI().toURL().toExternalForm());

			}
			catch (IOException e)
			{
				// SUPPRESS CHECKSTYLE print on the console because we are in very early stage of execution
				e.printStackTrace();
			}
		}
	}

	/**
	 * @param info
	 * @return
	 */
	private static URL getConfigurationURL(EnvironmentInfo info)
	{
		String specifiedURL = info.getProperty(PROP_KEY_CONFIGURL);
		if (specifiedURL == null)
		{
			specifiedURL = "file:" + CONFIGURATOR_FOLDER + "/" + CONFIG_LIST; //$NON-NLS-1$//$NON-NLS-2$
		}

		try
		{
			// If it is not a file URL use it as is
			if (!specifiedURL.startsWith("file:")) //$NON-NLS-1$
			{
				return new URL(specifiedURL);
			}
			// if it is an absolute file URL, use it as is
			boolean done = false;
			URL url = null;
			String file = specifiedURL;
			while (!done)
			{
				// what is this while loop for? nested file:file:file: urls?
				try
				{
					url = buildURL(file);
					file = url.getFile();
				}
				catch (java.net.MalformedURLException e)
				{
					done = true;
				}
			}
			if (url != null && new File(url.getFile()).isAbsolute())
			{
				return url;
			}

			// if it is an relative file URL, then resolve it against the
			// configuration area
			String urlLoc = info.getProperty(OSGI_CONFIGURATION_AREA);
			if (urlLoc != null)
			{
				File userConfig = new File(new URL(urlLoc).getFile(), url.getFile());
				return userConfig.exists() ? userConfig.toURI().toURL() : null;
			}
		}
		catch (MalformedURLException e)
		{
			// SUPPRESS CHECKSTYLE print on the console because we are in very early stage of execution
			e.printStackTrace();
			return null;
		}

		// Last resort
		try
		{
			return buildURL(specifiedURL);
		}
		catch (MalformedURLException e)
		{
			// SUPPRESS CHECKSTYLE print on the console because we are in very early stage of execution
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Lookup what minor metamodel shall be used on a particular branch
	 *
	 * @param sysPropName
	 * @param availableDescriptors
	 * @param defaultDescriptor
	 * @return
	 */
	private static MMDescriptor lookupMMDescriptor(String sysPropName, MMDescriptor[] availableDescriptors,
		MMDescriptor defaultDescriptor)
	{
		String mmStringID = System.getProperty(sysPropName);
		if (mmStringID != null)
		{
			for (MMDescriptor descr: availableDescriptors)
			{
				if (descr.matchesVersion(mmStringID))
				{
					return descr;
				}
			}
		}
		// no version found, return the first one and set the property
		System.setProperty(sysPropName, defaultDescriptor.getVersion());
		return defaultDescriptor;
	}

	/**
	 * Migrate a bundles.info file from source to target
	 *
	 * @param url
	 * @param tempBuildInfo
	 * @throws IOException
	 */
	private void migrateBundlesInfo(BufferedReader source, BufferedWriter target) throws IOException
	{
		String inLine;
		while ((inLine = source.readLine()) != null)
		{
			inLine = inLine.trim();
			if (inLine.length() > 0 && inLine.charAt(0) != '#')
			{
				int commaPos = inLine.indexOf(',');
				if (commaPos > 0)
				{
					// try to process it
					String bundleName = inLine.substring(0, commaPos);
					if (!isAccepted(bundleName))
					{
						inLine = null;
					}
					if ("org.eclipse.update.configurator".equals(bundleName)) //$NON-NLS-1$
					{
						System.setProperty("org.eclipse.update.reconcile", "false"); //$NON-NLS-1$ //$NON-NLS-2$
					}
				}
			}
			if (inLine != null)
			{
				target.write(inLine);
				target.newLine();
			}
		}
	}

	/**
	 * Return true if the bundle shall be accepted
	 *
	 * @param bundleName
	 * @return
	 */
	private boolean isAccepted(String bundleName)
	{
		for (MMDescriptor descr: MMDescriptorUtils.MM_DESCRIPTORS_3)
		{
			if (descr != active3xMMDescriptor && descr.patternMatched(bundleName))
			{
				return false;
			}
		}
		for (MMDescriptor descr: MMDescriptorUtils.MM_DESCRIPTORS_4)
		{
			if (descr != active4xMMDescriptor && descr.patternMatched(bundleName))
			{
				return false;
			}
		}
		for (String ignored: REMOVED_BUNDLES)
		{
			if (ignored.equals(bundleName))
			{
				return false;
			}
		}

		if (removeUI)
		{
			for (String ignored: ADDITIONAL_REMOVED_UI_BUNDLES)
			{
				if (ignored.equals(bundleName))
				{
					return false;
				}
			}
		}
		return true;
	}
}
