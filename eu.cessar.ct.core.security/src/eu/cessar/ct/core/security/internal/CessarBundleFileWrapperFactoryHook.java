/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidt2045 Jun 21, 2011 2:37:14 PM </copyright>
 */
package eu.cessar.ct.core.security.internal;

import org.eclipse.osgi.internal.hookregistry.BundleFileWrapperFactoryHook;
import org.eclipse.osgi.storage.BundleInfo.Generation;
import org.eclipse.osgi.storage.bundlefile.BundleFile;
import org.eclipse.osgi.storage.bundlefile.BundleFileWrapper;

/**
 * @author uidt2045
 *
 */
@SuppressWarnings("restriction")
public class CessarBundleFileWrapperFactoryHook implements BundleFileWrapperFactoryHook
{

	private static final String KEY_RESOLVER_PLUGIN = "eu.cessar.ct.core.security.licenseui"; //$NON-NLS-1$
	private static final String CESSAR_BUNDLE_PREFIX = "eu.cessar.ct."; //$NON-NLS-1$
	@SuppressWarnings("nls")
	private static final String[] CESSAR_SECURITY_BUNDLES = {"eu.cessar.ct.core.security",
		"eu.cessar.ct.core.security.license", "eu.cessar.ct.core.security.licenseui", "eu.cessar.ct.license",
		"eu.cessar.ct.license.ui", "eu.cessar.ct.license.generator.ui", "eu.cessar.ct.product"};

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.osgi.internal.hookregistry.BundleFileWrapperFactoryHook#wrapBundleFile(org.eclipse.osgi.storage.
	 * bundlefile.BundleFile, org.eclipse.osgi.storage.BundleInfo.Generation, boolean)
	 */
	@Override
	public BundleFileWrapper wrapBundleFile(BundleFile bundleFile, Generation generation, boolean base)
	{
		String bundleLocation = bundleFile.getBaseFile().getAbsolutePath();
		if (bundleLocation == null)
		{
			// nothing to do
			return null;
		}
		if (bundleLocation.contains(KEY_RESOLVER_PLUGIN))
		{
			SecurityUtils.setInitData(bundleFile, generation);
		}
		if (bundleLocation.contains(CESSAR_BUNDLE_PREFIX) && !isCessarSecurityBundle(bundleLocation))
		{
			// return Cessar wrapped bundle
			return new CessarBundleFileWrapper(bundleFile);
		}

		return null;
	}

	/**
	 * @param startname
	 * @return true if the name is one of the security names
	 */
	private boolean isCessarSecurityBundle(String startname)
	{
		for (String securityName: CESSAR_SECURITY_BUNDLES)
		{
			if (startname.contains(securityName))
			{
				return true;
			}
		}
		return false;
	}

}
