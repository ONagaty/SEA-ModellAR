/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uid94246 Sep 21, 2009 4:44:45 PM </copyright>
 */
package eu.cessar.ct.core.platform;

import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;
import org.osgi.framework.Constants;

import eu.cessar.ct.core.internal.platform.CessarPluginActivator;

/**
 * This interface should contains CESSAR-CT common platform constants
 * 
 * @Review uidl6458 - 12.04.2012
 */
public class PlatformConstants
{

	private static String getProductVersion()
	{
		Bundle productBundle = Platform.getBundle("eu.cessar.ct.product"); //$NON-NLS-1$
		if (productBundle == null)
		{
			productBundle = CessarPluginActivator.getDefault().getBundle();
		}
		String property = productBundle.getHeaders().get(Constants.BUNDLE_VERSION);
		return property;
	}

	public final static String PRODUCT_NAME = "CESSAR-CT"; //$NON-NLS-1$

	public final static String PRODUCT_VERSION = getProductVersion();

	/** CESSAR-CT perspective ID */
	public final static String CESSAR_CT_PERSPECTIVE = "eu.cessar.ct.platform.CessarPerspective"; //$NON-NLS-1$

	/** CessarNature ID */
	public static final String CESSAR_NATURE = "eu.cessar.ct.core.platform.cessarnature"; //$NON-NLS-1$

	public static final String PROJECT_CONFIG_RADIX = "configuration.radix"; //$NON-NLS-1$

	/** Path separator */
	public static final String PATH_SEPARATOR = "/"; //$NON-NLS-1$

}
