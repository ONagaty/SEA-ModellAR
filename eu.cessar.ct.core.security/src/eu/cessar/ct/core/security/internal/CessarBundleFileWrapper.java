/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidt2045 Jun 21, 2011 2:52:42 PM </copyright>
 */
package eu.cessar.ct.core.security.internal;

import java.io.File;
import java.util.Enumeration;

import org.eclipse.osgi.storage.bundlefile.BundleEntry;
import org.eclipse.osgi.storage.bundlefile.BundleFile;
import org.eclipse.osgi.storage.bundlefile.BundleFileWrapper;

/**
 * @author uidt2045
 *
 */
@SuppressWarnings("restriction")
public class CessarBundleFileWrapper extends BundleFileWrapper
{

	/**
	 * @param wrapped
	 * @param data
	 */
	public CessarBundleFileWrapper(BundleFile wrapped)
	{
		super(wrapped);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.osgi.baseadaptor.bundlefile.BundleFile#toString()
	 */
	@Override
	public String toString()
	{
		return "Cessar wrapper of " + getBundleFile().toString(); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.osgi.baseadaptor.bundlefile.BundleFile#getFile(java.lang.String, boolean)
	 */

	@Override
	public File getFile(String path, boolean nativeCode)
	{
		if (isUnlicensedPluginXML(path))
		{
			return null;
		}
		return super.getFile(path, nativeCode);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.osgi.baseadaptor.bundlefile.BundleFile#getEntry(java.lang.String)
	 */
	@Override
	public BundleEntry getEntry(String path)
	{
		if (isUnlicensedPluginXML(path))
		{
			return null;
		}
		return super.getEntry(path);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.osgi.baseadaptor.bundlefile.BundleFile#getEntryPaths(java.lang.String)
	 */
	@Override
	public Enumeration<String> getEntryPaths(String path)
	{
		if (isUnlicensedPluginXML(path))
		{
			return null;
		}
		return super.getEntryPaths(path);
	}

	/**
	 * @param path
	 * @return
	 */
	private boolean isUnlicensedPluginXML(String path)
	{
		if ("plugin.xml".equals(path) && !SecurityUtils.haveLicense()) //$NON-NLS-1$
		{
			return true;
		}
		return false;
	}
}
