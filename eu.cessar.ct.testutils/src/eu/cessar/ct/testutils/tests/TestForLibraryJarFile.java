/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidu8153<br/>
 * Jan 13, 2015 10:33:41 AM
 *
 * </copyright>
 */
package eu.cessar.ct.testutils.tests;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.eclipse.core.runtime.Platform;
import org.eclipse.osgi.service.resolver.BundleDescription;
import org.eclipse.osgi.util.ManifestElement;
import org.eclipse.pde.core.plugin.IPluginModelBase;
import org.eclipse.pde.core.plugin.PluginRegistry;
import org.junit.Test;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleException;
import org.osgi.framework.Constants;

import eu.cessar.ct.testutils.CessarPluginActivator;

/**
 * TODO: Please comment this class
 *
 * @author uidu8153
 *
 *         %created_by: uidu8153 %
 *
 *         %date_created: Wed Feb 11 10:22:22 2015 %
 *
 *         %version: 3 %
 */
public class TestForLibraryJarFile extends TestCase

{

	ArrayList<Bundle> pluginIds = new ArrayList<Bundle>();

	private void getTestUtilsPlugin()
	{

		IPluginModelBase model = PluginRegistry.findModel("eu.cessar.ct.testutils");
		if (model == null)
		{
			throw new IllegalArgumentException("Cannot locate bundle with ID " + "eu.cessar.ct.testutils");
		}

		Bundle bundle = Platform.getBundle(model.getBundleDescription().getName());

		pluginIds.add(bundle);
	}

	@Test
	public void testIfPluginHasLibraryJarFile()
	{

		try
		{
			getTestUtilsPlugin();
			IPluginModelBase[] allModels = PluginRegistry.getActiveModels(false);

			BundleDescription bundleDescription;
			for (IPluginModelBase model: allModels)
			{
				bundleDescription = model.getBundleDescription();

				Bundle bundle = Platform.getBundle(bundleDescription.getName());

				if (bundle != null && bundle.getSymbolicName().startsWith("eu.cessar.")
					&& (bundle.getSymbolicName().matches(".*(test|tests)$")))

				{

					pluginIds.add(bundle);
				}

			}

			List<String> pluginNameWithoutLibraryJar = new ArrayList<String>();
			boolean ifPluginHaslibraryJar = false;

			for (Bundle bundle: pluginIds)
			{
				String classpath = bundle.getHeaders().get(Constants.BUNDLE_CLASSPATH);
				System.out.println(bundle.getSymbolicName());
				try
				{
					String message;
					ManifestElement[] manifestElmns = ManifestElement.parseHeader(Constants.BUNDLE_CLASSPATH, classpath);
					if (manifestElmns != null)
					{
						ifPluginHaslibraryJar = checkIfLibraryJarInManifestElements(manifestElmns);

						if (!ifPluginHaslibraryJar)
						{
							pluginNameWithoutLibraryJar.add(bundle.getSymbolicName());
						}
					}
					else
					{
						pluginNameWithoutLibraryJar.add(bundle.getSymbolicName());
						System.out.println(bundle.getSymbolicName()
							+ " is missing Bundle-ClassPath: library.jar in manifest.");
					}
				}
				catch (BundleException e)
				{
					CessarPluginActivator.getDefault().logError(e);
				}

			}

			if (!pluginNameWithoutLibraryJar.isEmpty())
			{
				StringBuilder pluginNames = new StringBuilder();
				for (int i = 0; i < pluginNameWithoutLibraryJar.size(); i++)
				{
					pluginNames.append(pluginNameWithoutLibraryJar.get(i));

					if (i != pluginNameWithoutLibraryJar.size() - 1)
					{
						pluginNames.append(", ");
					}

				}

				assertFalse("The following plugin doesn't have Library.jar File( " + pluginNames.toString() + ")",
					ifPluginHaslibraryJar);
			}
		}
		catch (Exception e)
		{
			CessarPluginActivator.getDefault().logError(e);
		}

	}

	private boolean checkIfLibraryJarInManifestElements(ManifestElement[] manifestElmns)
	{

		boolean isLibraryFileExists = false;

		for (ManifestElement elem: manifestElmns)
		{

			if (elem.getValue().equalsIgnoreCase("library.jar"))
			{
				isLibraryFileExists = true;
				break;

			}
			else
			{
				isLibraryFileExists = false;
			}

		}

		return isLibraryFileExists;
	}
}
