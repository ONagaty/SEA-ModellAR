/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidu2337<br/>
 * Sep 12, 2014 2:48:43 PM
 *
 * </copyright>
 */
package eu.cessar.ct.runtime.ecuc.util;

import org.eclipse.osgi.util.NLS;

/**
 * Error messages for {@link SplitPMUtils}.
 *
 * @author uidu2337
 *
 *         %created_by: uidu2337 %
 *
 *         %date_created: Mon Sep 15 13:00:48 2014 %
 *
 *         %version: 2 %
 */
@SuppressWarnings("javadoc")
public final class Messages extends NLS
{
	// CHECKSTYLE:OFF
	private static final String BUNDLE_NAME = "eu.cessar.ct.runtime.ecuc.util.messages"; //$NON-NLS-1$

	public static String splitPMUtils_SizeIndex;

	public static String splitPMUtils_NegativeIndex;

	// CHECKSTYLE:ON

	static
	{
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages()
	{
	}
}
