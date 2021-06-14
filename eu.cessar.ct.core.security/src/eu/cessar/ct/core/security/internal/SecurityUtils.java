/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidt2045 Jun 22, 2011 3:56:00 PM </copyright>
 */
package eu.cessar.ct.core.security.internal;

import org.eclipse.osgi.storage.BundleInfo.Generation;
import org.eclipse.osgi.storage.bundlefile.BundleFile;

import eu.cessar.ct.core.security.license.CessarOldLicenseConstants;
import eu.cessar.ct.core.security.license.CessarOldLicenseHandler;

/**
 * @author uidt2045
 *
 */
@SuppressWarnings("restriction")
public class SecurityUtils
{

	/**
	 *
	 */
	// key for old license
	private static final byte[] KEY = {(byte) 0x43, (byte) 0x99, (byte) 0xE8, (byte) 0xFA, (byte) 0x95, (byte) 0x00,
		(byte) 0x18, (byte) 0x64, (byte) 0x24, (byte) 0xB1, (byte) 0x8F, (byte) 0x51, (byte) 0x1D, (byte) 0x84,
		(byte) 0x0E, (byte) 0xF7};

	private static final String KEY_RESOLVER_CLASS = "eu.cessar.ct.core.security.licenseui.internal.CessarKeyResolver"; //$NON-NLS-1$

	// private static BaseData baseData;

	private static boolean licenseAsked; // = false;

	private static byte[] key; // = null;

	private static BundleFile bundleFile;

	private static Generation generation;

	/**
	 *
	 */
	private SecurityUtils()
	{
		// do not instantiate
	}

	/**
	 * @return
	 */
	/* package */static byte[] getDecryptionKey()
	{
		if (!licenseAsked)
		{
			synchronized (SecurityUtils.class)
			{
				if (!licenseAsked)
				{
					key = loadLicenseVerification();
					licenseAsked = true;
				}
			}
		}
		return key;

	}

	/**
	 * @return true if a valid license exists, false otherwise
	 */
	/* package */static boolean haveLicense()
	{
		return getDecryptionKey() != null;
	}

	/**
	 * Loads license verification
	 */
	private static byte[] loadLicenseVerification()
	{
		try
		{
			Object result = checkLicense();
			return (byte[]) result;
		}
		// SUPPRESS CHECKSTYLE Catch everyting, we don't want to crash like this in this moment
		catch (Throwable t)
		{
			throw new SecurityViolationError(t);
		}
	}

	/**
	 * @param bundleFile
	 * @param generation
	 */
	public static void setInitData(BundleFile bundleFile, Generation generation)
	{
		SecurityUtils.bundleFile = bundleFile;
		// TODO Auto-generated method stub
		SecurityUtils.generation = generation;

	}

	/**
	 * @return the license key
	 */
	private static byte[] checkLicense()
	{
		Boolean forceUnlicensedStart;
		String unlicensedStart = System.getProperty("ForceUnlicensedStart");
		if (unlicensedStart != null && "true".equalsIgnoreCase(unlicensedStart))
		{
			forceUnlicensedStart = true;
		}
		else
		{
			forceUnlicensedStart = false;
		}

		if (!forceUnlicensedStart)
		{
			if (CessarOldLicenseHandler.checkLicense(CessarOldLicenseConstants.PRODUCT_CESSAR,
				CessarOldLicenseConstants.MODULE_CESSAR))

			{
				return KEY;
			}
			else
			{
				return null;
			}
		}
		else
		{
			// check .lf first
			if (CessarOldLicenseHandler.checkLicense(CessarOldLicenseConstants.PRODUCT_CESSAR,
				CessarOldLicenseConstants.MODULE_CESSAR))
			{
				return KEY;
			}
			else
			{
				return null;
			}
		}
	}

}
