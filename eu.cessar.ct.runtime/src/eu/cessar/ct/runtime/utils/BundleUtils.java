/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidl6870 Nov 5, 2009 1:38:29 PM </copyright>
 */
package eu.cessar.ct.runtime.utils;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.osgi.util.ManifestElement;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleException;
import org.osgi.framework.Constants;

import eu.cessar.ct.core.platform.xml.StopParsingException;
import eu.cessar.ct.runtime.CessarRuntime;
import eu.cessar.ct.runtime.classpath.AbstractPluginsClasspathContributor;
import eu.cessar.ct.runtime.classpath.ICessarClasspathContributor;
import eu.cessar.ct.runtime.internal.CessarPluginActivator;
import eu.cessar.ct.runtime.internal.Messages;
import eu.cessar.ct.runtime.internal.classpath.PropertyPluginsClasspathContributor;
import eu.cessar.ct.runtime.internal.classpath.util.ClasspathHandler;

/**
 * Provides utility methods for resolving bundle dependencies
 *
 */
public final class BundleUtils
{
	/**
	 *
	 */
	private static final String DOT = "."; //$NON-NLS-1$

	private BundleUtils()
	{
		// avoid instance
	}

	/**
	 * If <code>collectDependencies</code> set on <code>false</code>, simply return a list of the given
	 * <code>bundleIDs</code>, otherwise return a list containing the <code>bundleIDs</code> and also all their
	 * dependencies (all required fragments and re-exported bundles)
	 *
	 * @param bundleIDs
	 *        array of bundle IDs provided by a contributor
	 * @param collectDependencies
	 * @return list of bundle IDs, never null
	 */
	public static List<String> getDependencies(String[] bundleIDs, boolean collectDependencies)
	{
		List<String> result = new ArrayList<String>();
		List<Bundle> allDependencies = new ArrayList<Bundle>();

		if (bundleIDs.length > 0)
		{
			for (String bundleID: bundleIDs)
			{
				Bundle bundle = Platform.getBundle(bundleID);
				if (bundle == null)
				{
					CessarPluginActivator.getDefault().logError(Messages.UnresolvedBundle, bundleID);
					continue;
				}

				// a list containing bundle, all it's required fragments and
				// exported bundles
				List<Bundle> bundleList = new ArrayList<Bundle>();
				addRequiredBundles(bundleList, bundle, collectDependencies);
				allDependencies.addAll(bundleList);
			}
		}

		for (Bundle bundle: allDependencies)
		{
			result.add(bundle.getSymbolicName());
		}

		return result;
	}

	/**
	 * Return a list of paths that consist the classpath of a bundle.
	 *
	 * @param bundle
	 * @return
	 */
	public static List<IPath> getBundleClassPath(Bundle bundle, boolean ignoreExports)
	{
		List<IPath> result = new ArrayList<IPath>();
		if (Platform.inDevelopmentMode())
		{
			IPath path = getDevModeClasspathEntry(bundle);
			if (path != null)
			{
				result.add(path);
			}
		}
		result.addAll(getRunModeClasspathEntries(bundle, ignoreExports));
		return result;
	}

	/**
	 * @param bundle
	 * @return
	 * @throws MalformedURLException
	 */
	public static URL[] getBundleClassPathAsURL(Bundle bundle, boolean ignoreExports) throws MalformedURLException
	{
		List<IPath> paths = getBundleClassPath(bundle, ignoreExports);
		URL[] result = new URL[paths.size()];
		for (int i = 0; i < paths.size(); i++)
		{
			result[i] = paths.get(i).toFile().toURL();
		}
		return result;
	}

	/**
	 * Using <u>ClasspathHandler</u>, parses the .classpath file of the bundle to identify the <code>output</code>.
	 *
	 * @param bundle
	 * @return
	 */
	private static IPath getDevModeClasspathEntry(Bundle bundle)
	{
		URL claspathURL = bundle.getEntry(CessarRuntime.CLASSPATH_FILE);
		if (claspathURL != null)
		{
			try
			{
				SAXParserFactory factory = SAXParserFactory.newInstance();
				SAXParser parser = factory.newSAXParser();

				ClasspathHandler handler = new ClasspathHandler();
				try
				{
					parser.parse(claspathURL.toURI().toString(), handler);
				}
				catch (StopParsingException e)
				{// SUPPRESS CHECKSTYLE change in future
					// ignore it, expected exception
				}
				String output = handler.getOutput();
				if (output != null)
				{
					URL outputURL = bundle.getEntry(output);
					if (outputURL != null)
					{
						URL resolvedURL = FileLocator.resolve(outputURL);
						return new Path(resolvedURL.getPath());
					}
				}
			}
			catch (Exception e) // SUPPRESS CHECKSTYLE change in future
			{
				CessarPluginActivator.getDefault().logError(e);
			}
		}
		return null;
	}

	private static List<IPath> getRunModeClasspathEntries(Bundle bundle, boolean ignoreExports)
	{

		// from MANIFEST.MF: get Bundle-ClassPath, Fragment-Host and
		// Export-Package headers

		List<IPath> cpEntries = new ArrayList<IPath>();

		Object exportHeader = bundle.getHeaders().get(Constants.EXPORT_PACKAGE);
		Object fragmHostHeader = bundle.getHeaders().get(Constants.FRAGMENT_HOST);

		// if the bundle exports something or it's a fragment ->look inside
		// "Bundle-ClassPath" header for jars
		if (ignoreExports || exportHeader != null || fragmHostHeader != null)
		{
			cpEntries.addAll(lookInsideBudleClasspathForJars(bundle));
		}

		// finally, if the bundle is packaged as a jar then return a CPEntry for
		// that jar
		try
		{
			File bundleFile = FileLocator.getBundleFile(bundle);
			if (!bundleFile.isDirectory())
			{
				Path path = new Path(bundleFile.getPath());
				cpEntries.add(path);
			}
		}
		catch (IOException e)
		{
			CessarPluginActivator.getDefault().logError(e);
		}
		return cpEntries;

	}

	private static List<IPath> lookInsideBudleClasspathForJars(Bundle bundle)
	{
		List<IPath> cpEntries = new ArrayList<IPath>();

		Object cpHeader = bundle.getHeaders().get(Constants.BUNDLE_CLASSPATH);
		if (cpHeader != null && !DOT.equals(cpHeader))
		{
			try
			{
				ManifestElement[] manifestElmns = ManifestElement.parseHeader(Constants.BUNDLE_CLASSPATH,
					(String) cpHeader);
				if (manifestElmns != null)
				{
					cpEntries.addAll(getCPFromManifestElements(bundle, manifestElmns));
				}
			}
			catch (BundleException e)
			{
				CessarPluginActivator.getDefault().logError(e, Messages.InvalidHeaderValue, cpHeader);
			}
		}

		return cpEntries;
	}

	private static List<IPath> getCPFromManifestElements(Bundle bundle, ManifestElement[] manifestElmns)
	{
		List<IPath> cpEntries = new ArrayList<IPath>();

		URL jarURL = null;
		for (ManifestElement elem: manifestElmns)
		{
			jarURL = null;
			String value = elem.getValue();

			if (value != null && !DOT.equals(value))
			{
				jarURL = bundle.getEntry(value);
				// could not locate the jar inside the bundle,
				// could be in a fragment but is not required to
				// solve it now
				if (jarURL == null)
				{
					continue;
				}
				try
				{
					// jarURL = FileLocator.find(jarURL);
					// URL resolvedURL =
					// FileLocator.resolve(jarURL);
					URL resolvedURL = FileLocator.toFileURL(jarURL);

					IPath cpPath = new Path(resolvedURL.getPath());
					cpEntries.add(cpPath);
				}
				catch (IOException e)
				{
					CessarPluginActivator.getDefault().logError(e, Messages.UnableToResolveURL, jarURL);
				}
			}
		}

		return cpEntries;
	}

	/**
	 *
	 * If <code>collectDependencies</code> is set on <code>false</code>, add only the given bundle to the given
	 * <code>finalResult</code> list , else computes recursively a list containing the bundle,all it's required
	 * fragments and all re-exported bundles
	 *
	 * @param finalResult
	 * @param bundle
	 * @param collectDependencies
	 */
	private static void addRequiredBundles(List<Bundle> finalResult, Bundle bundle, boolean collectDependencies)
	{
		if (finalResult.contains(bundle))
		{
			return;
		}
		List<Bundle> result = new ArrayList<Bundle>();

		// add the bundle itself
		result.add(bundle);

		if (!collectDependencies)
		{
			finalResult.addAll(result);
			return;
		}

		Bundle[] fragments = Platform.getFragments(bundle);
		if (fragments != null)
		{
			List<Bundle> fragmentList = Arrays.asList(fragments);

			// if the host bundle has the "Eclipse-ExtensibleAPI" header on
			// true, use all it's fragments
			Object object = bundle.getHeaders().get(CessarRuntime.ECLIPSE_EXTENSIBLE_API);

			if ("true".equals(object)) //$NON-NLS-1$
			{
				result.addAll(fragmentList);
			}
			else
			{
				// missing header or set on false -> add only it's patched
				// fragments
				result.addAll(getPatchedFragments(bundle));
			}
		}

		finalResult.addAll(result);
		for (Bundle bdl: result)
		{
			List<Bundle> dependencies = getReexportedPlugins(bdl);
			for (Bundle b: dependencies)
			{
				addRequiredBundles(finalResult, b, collectDependencies);
			}
		}
	}

	/**
	 * For the given <code>bundle</code>, returns a list containing all required and re-exported bundles
	 *
	 * @param bundle
	 * @return a list of reexported plugins, never null
	 */
	private static List<Bundle> getReexportedPlugins(Bundle bundle)
	{
		// Platform.getConfigurationLocation()
		List<Bundle> result = new ArrayList<Bundle>();
		String reqHeader = bundle.getHeaders().get(Constants.REQUIRE_BUNDLE);

		try
		{
			ManifestElement[] manifestElmns = ManifestElement.parseHeader(Constants.REQUIRE_BUNDLE, reqHeader);

			if (manifestElmns != null)
			{
				for (ManifestElement elem: manifestElmns)
				{
					String value = elem.getDirective(CessarRuntime.DIRECTIVE_VISIBILITY);
					if (CessarRuntime.DIRECTIVE_REEXPORT.equals(value))
					{
						String reexBundleID = elem.getValue();
						Bundle reqBundle = Platform.getBundle(reexBundleID);
						// if it's optional and not available it will be null
						if (reqBundle != null)
						{
							result.add(reqBundle);
						}
					}
				}
			}
		}
		catch (BundleException e)
		{
			CessarPluginActivator.getDefault().logError(Messages.InvalidHeaderValue, reqHeader);
		}
		return result;
	}

	/**
	 * For the given <code>bundle</code> compute and return a list of all it's fragments that have
	 * "Eclipse-PatchFragment" header set on true
	 *
	 * @param fragBundles
	 * @return a list, never null
	 */
	private static List<Bundle> getPatchedFragments(Bundle bundle)
	{
		List<Bundle> patchedFragments = new ArrayList<Bundle>();

		Bundle[] fragments = Platform.getFragments(bundle);
		for (Bundle fragment: fragments)
		{
			Object object = fragment.getHeaders().get(CessarRuntime.ECLIPSE_PATCH_FRAGMENT);
			if (object != null)
			{
				String value = (String) object;
				if ("true".equals(value)) //$NON-NLS-1$
				{
					patchedFragments.add(fragment);
				}
			}
		}
		return patchedFragments;
	}

	/**
	 * Returns an array of contributed bundles to a specific <code>containerID</code>.
	 *
	 * @param project
	 *        the given project
	 * @param containerID
	 *        the ID of the container to which the bundles are contributed
	 * @return the array of contributed bundles
	 */
	public static String[] loadContributedBundles(IJavaProject project, String containerID)
	{
		String[] cessarBundles = null;
		java.util.List<ICessarClasspathContributor> contributors = CessarRuntime.getClasspathContributionRegistry().getContributors(
			containerID);

		if (contributors != null)
		{
			java.util.List<String> cessarBundleList = new ArrayList<String>();
			for (ICessarClasspathContributor contributor: contributors)
			{
				if (contributor instanceof AbstractPluginsClasspathContributor
					&& !(contributor instanceof PropertyPluginsClasspathContributor))
				{
					String[] bundleIDs = ((AbstractPluginsClasspathContributor) contributor).getBundles(project);
					List<String> bundleList = BundleUtils.getDependencies(bundleIDs, true);
					for (String bundle: bundleList)
					{
						cessarBundleList.add(bundle);
					}
				}
			}
			cessarBundles = cessarBundleList.toArray(new String[0]);
		}
		return cessarBundles;
	}

}
