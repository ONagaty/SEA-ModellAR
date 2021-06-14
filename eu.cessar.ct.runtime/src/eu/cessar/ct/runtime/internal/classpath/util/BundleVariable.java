/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870 Oct 26, 2009 1:34:57 PM </copyright>
 */
package eu.cessar.ct.runtime.internal.classpath.util;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.core.IAccessRule;
import org.eclipse.jdt.core.IClasspathAttribute;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.osgi.util.ManifestElement;
import org.eclipse.pde.core.plugin.IPluginModelBase;
import org.eclipse.pde.core.plugin.PluginRegistry;
import org.eclipse.pde.internal.core.ClasspathUtilCore;
import org.eclipse.pde.internal.core.PDECore;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleException;
import org.osgi.framework.Constants;

import eu.cessar.ct.core.platform.xml.StopParsingException;
import eu.cessar.ct.runtime.CessarRuntime;
import eu.cessar.ct.runtime.internal.CessarPluginActivator;
import eu.cessar.ct.runtime.internal.Messages;

/**
 * An instance is created by {@link BundleVariableRegistry} for each bundle
 * received from the contributor. Basically it encapsulates all the information
 * from a certain bundle, information that is necessary and sufficient for
 * creating CPEntries for that bundle <br>
 * </br>
 */
@SuppressWarnings("restriction")
/* package*/class BundleVariable implements Serializable
{
	/**
	 * 
	 */
	private static final String DOT = "."; //$NON-NLS-1$

	private static final long serialVersionUID = -4258424486308820402L;

	/** bundleID */
	private String symbolicName;

	transient private Bundle bundle;

	/** indicates weather the disk cache should be updated or not */
	transient private boolean dirty = false;

	/** a list of CPE wrappers for a particular bundle */
	private List<CPEWrapper> wrapperList;

	public BundleVariable(String symbolicName)
	{
		this.symbolicName = symbolicName;
		bundle = Platform.getBundle(symbolicName);
	}

	/**
	 * A wrapper for a particular CPEntry. It contains all necessary information
	 * for creating a certain CPEntry
	 */
	private class CPEWrapper implements Serializable
	{
		private static final long serialVersionUID = 8419721789439191201L;

		private String javaDocLocation;
		private String library;
		private String source;

		boolean modelFound = false;

		public CPEWrapper(IPath path)
		{
			library = path.toString();
		}

		private IPath getPath()
		{
			// path to the library
			return new Path(library);
		}

		private IPath getSourceLocation()
		{
			IPath path = null;
			if (source != null)
			{
				path = new Path(source);
			}
			return path;
		}

		private void setSourceLocation(IPath sourceLocation)
		{
			if (sourceLocation != null)
			{
				source = sourceLocation.toString();
			}
		}

		private String getJavaDocLocation()
		{
			return javaDocLocation;
		}

		private void setJavaDocLocation(String javaDocLocation)
		{
			this.javaDocLocation = javaDocLocation;
		}
	}

	/**
	 * Return the corresponding bundle ID
	 * 
	 * @return
	 */
	public String getSymbolicName()
	{
		return symbolicName;

	}

	/**
	 * Return the corresponding bundle
	 * 
	 * @return
	 */
	private Bundle getBundle()
	{
		return bundle;

	}

	/**
	 * Create and return a CPEntry using data from given <code>wrapper</code>
	 * 
	 * @param wrapper
	 * @return
	 */
	private IClasspathEntry createClasspathEntry(CPEWrapper wrapper)
	{
		if (!wrapper.modelFound)
		{
			synchronized (wrapper)
			{
				if (!wrapper.modelFound)
				{
					IPluginModelBase model = PluginRegistry.findModel(getSymbolicName());
					// if it's not too early
					if (model != null)
					{
						if (wrapper.getSourceLocation() == null)
						{
							// set source attachment
							IPath sourceAnnotation = ClasspathUtilCore.getSourceAnnotation(model,
								wrapper.getPath().lastSegment());
							wrapper.setSourceLocation(sourceAnnotation);
							dirty = true;
						}

						if (wrapper.getJavaDocLocation() == null)
						{
							// set javadoc
							String javaDoc = PDECore.getDefault().getJavadocLocationManager().getJavadocLocation(
								model);
							wrapper.setJavaDocLocation(javaDoc);
							dirty = true;
						}
						wrapper.modelFound = true;
					}
				}
			}
		}
		if (wrapper.getJavaDocLocation() == null)
		{
			return JavaCore.newLibraryEntry(wrapper.getPath(), wrapper.getSourceLocation(), null);
		}
		return JavaCore.newLibraryEntry(
			wrapper.getPath(),
			wrapper.getSourceLocation(),
			null,
			new IAccessRule[0],
			new IClasspathAttribute[] {JavaCore.newClasspathAttribute(
				IClasspathAttribute.JAVADOC_LOCATION_ATTRIBUTE_NAME, wrapper.getJavaDocLocation())},
			false);
	}

	/**
	 * Checks if a wrapper for the given library path <code>path</code> exists,
	 * if not, creates it, adds it to the <code>wrapperList</code> and finally,
	 * returns it
	 * 
	 * @param path
	 * @return
	 */
	private CPEWrapper lookupWrapperEntry(List<CPEWrapper> entries, IPath path)
	{

		for (CPEWrapper wrapper: entries)
		{
			if (wrapper.getPath().lastSegment().equals(path.lastSegment()))
			{
				return wrapper;
			}
		}
		CPEWrapper cpeWrapper = new CPEWrapper(path);
		entries.add(cpeWrapper);

		return cpeWrapper;
	}

	/**
	 * Create and return an array of CPEntries for the bundle returned by
	 * <code>getBundle()</code>, avoiding duplicates by looking inside
	 * <code>existingEntries</code>
	 * 
	 * The returned array contains a CPE for the output of the bundle, if the
	 * platform is running in development mode or a CPE for the bundle itself if
	 * the bundle is packaged as a jar, and also CPEntries for all the jars that
	 * are exported by the bundle
	 * 
	 * @param existingEntries
	 * @return an array of IClasspathEntry, never null
	 */
	public List<IClasspathEntry> getClassPathEntries(List<IClasspathEntry> existingEntries)
	{
		if (wrapperList == null)
		{
			synchronized (this)
			{
				if (wrapperList == null)
				{
					// check storage and load if exits else
					// first call, fill it with data
					List<CPEWrapper> entries = new ArrayList<CPEWrapper>();
					if (Platform.inDevelopmentMode())
					{
						addCPEWrapperFromOutput(entries);
					}
					addCPEWrappersFromJars(entries);
					dirty = true;
					wrapperList = entries;
				}
			}
		}
		return createCPEntries(existingEntries);
	}

	/**
	 * 
	 * @param existingEntries
	 * @return
	 */
	private List<IClasspathEntry> createCPEntries(List<IClasspathEntry> existingEntries)
	{
		List<IClasspathEntry> result = new ArrayList<IClasspathEntry>();
		for (CPEWrapper wrapper: wrapperList)
		{
			if (!hasCPE(existingEntries, wrapper.getPath()))
			{
				result.add(createClasspathEntry(wrapper));
			}
		}
		return result;
	}

	/**
	 * Return weather the disk cache should be updated or not.
	 * 
	 * @return
	 */
	public boolean isDirty()
	{
		return dirty;
	}

	/**
	 * Set on <code>true</code> when the disk cache should be updates, <false>
	 * otherwise. The disk cache has to be updated in one of the 2 cases: at
	 * first startup/after running Cessar with -clean option
	 * 
	 * @param value
	 */
	public void setDirty(boolean value)
	{
		dirty = value;
	}

	/**
	 * Using <u>ClasspathHandler</u>, parses the .classpath file of the bundle
	 * to identify the <code>output</code>. Also creates and puts inside given
	 * list <code>entries</code>, the corresponding wrapper for the CPE, if not
	 * already created
	 * 
	 * @param entries
	 */
	private void addCPEWrapperFromOutput(List<CPEWrapper> entries)
	{

		Bundle bundle = getBundle();

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
				{
					// ignore it, expected exception
				}
				String output = handler.getOutput();
				if (output != null)
				{
					URL outputURL = bundle.getEntry(output);
					if (outputURL != null)
					{
						URL resolvedURL = FileLocator.resolve(outputURL);
						Path path = new Path(resolvedURL.getPath());

						lookupWrapperEntry(entries, path);
					}
				}
			}
			catch (Exception e) // SUPPRESS CHECKSTYLE change in future
			{
				CessarPluginActivator.getDefault().logError(e);
			}
		}
	}

	/**
	 * By avoiding duplicates, creates wrappers of the CPEntries for the
	 * exported jars of the bundle and also a wrapper for the CPE of the bundle
	 * itself, if the bundle is packaged as a jar
	 * 
	 * @param entries
	 * 
	 */
	private void addCPEWrappersFromJars(List<CPEWrapper> entries)
	{
		Bundle bundle = getBundle();
		// from MANIFEST.MF: get Bundle-ClassPath, Fragment-Host and
		// Export-Package headers

		Object exportHeader = bundle.getHeaders().get(Constants.EXPORT_PACKAGE);
		Object fragmHostHeader = bundle.getHeaders().get(Constants.FRAGMENT_HOST);

		// if the bundle exports something or it's a fragment ->look inside
		// "Bundle-ClassPath" header for jars
		if (exportHeader != null || fragmHostHeader != null)
		{
			Object cpHeader = bundle.getHeaders().get(Constants.BUNDLE_CLASSPATH);
			if (cpHeader != null && !DOT.equals(cpHeader))
			{
				try
				{
					ManifestElement[] manifestElmns = ManifestElement.parseHeader(
						Constants.BUNDLE_CLASSPATH, (String) cpHeader);
					if (manifestElmns != null)
					{
						getCPFromManifestElems(entries, bundle, manifestElmns);
					}
				}
				catch (BundleException e)
				{
					CessarPluginActivator.getDefault().logError(e, Messages.InvalidHeaderValue,
						cpHeader);
				}
			}
		}

		// finally, if the bundle is packaged as a jar then return a CPEntry for
		// that jar
		try
		{
			File bundleFile = FileLocator.getBundleFile(bundle);
			if (!bundleFile.isDirectory())
			{
				Path path = new Path(bundleFile.getPath());

				lookupWrapperEntry(entries, path);
			}
		}
		catch (IOException e)
		{
			CessarPluginActivator.getDefault().logError(e);
		}
	}

	private void getCPFromManifestElems(List<CPEWrapper> entries, Bundle bundle,
		ManifestElement[] manifestElmns)
	{
		for (ManifestElement elem: manifestElmns)
		{
			URL jarURL = null;
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
					lookupWrapperEntry(entries, cpPath);
				}
				catch (IOException e)
				{
					CessarPluginActivator.getDefault().logError(e, Messages.UnableToResolveURL,
						jarURL);
				}
			}
		}
	}

	/**
	 * Return <code>true</code> if the <code>entries</code> list contains a CPE
	 * with given <code>path</code>, <code>false</code> otherwise
	 * 
	 * @param entries
	 * @param path
	 * @return
	 */
	private static boolean hasCPE(List<IClasspathEntry> entries, IPath path)
	{
		for (IClasspathEntry entry: entries)
		{
			if (entry.getPath().equals(path))
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * Return true if this BundleVariable have all paths valid.
	 * 
	 * @return
	 */
	public boolean isValid()
	{
		boolean isValid = true;

		if (wrapperList != null)
		{
			for (CPEWrapper wrapper: wrapperList)
			{
				IPath path = wrapper.getPath();
				if (path == null)
				{
					isValid = false;
					break;
				}
				else
				{
					if (!path.toFile().exists())
					{
						isValid = false;
						break;
					}
					path = wrapper.getSourceLocation();
					if (path != null && !path.toFile().exists())
					{
						isValid = false;
						break;
					}
				}
			}
		}
		return isValid;
	}

}
