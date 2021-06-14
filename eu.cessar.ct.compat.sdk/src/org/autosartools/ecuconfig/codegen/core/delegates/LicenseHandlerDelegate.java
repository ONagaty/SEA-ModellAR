/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidt2045 Oct 28, 2010 9:32:35 AM </copyright>
 */
package org.autosartools.ecuconfig.codegen.core.delegates;

/**
 * @author uidt2045
 * 
 */
public class LicenseHandlerDelegate
{
	// this class is created for the JetUtil API
	// it is deprecated

	/** Singleton */
	private static LicenseHandlerDelegate __instance = null;

	/**
	 * Default constructor
	 */
	private LicenseHandlerDelegate()
	{
	}

	/**
	 * Return the singleton
	 * 
	 * @return the singleton
	 */
	public static LicenseHandlerDelegate getInstance()
	{

		if (__instance == null)
		{
			__instance = new LicenseHandlerDelegate();
		}
		return __instance;
	}

	@SuppressWarnings("unused")
	public boolean checkLicense(final String productName, final String moduleName)
	{
		return true;
	}

	@SuppressWarnings("unused")
	public boolean checkLicenseAndInstall(final String productName, final String moduleName)
	{
		return true;

	}
}
