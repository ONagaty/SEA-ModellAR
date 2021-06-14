/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidl6458 Mar 4, 2010 11:36:24 AM </copyright>
 */
package eu.cessar.ct.validation.ui.internal;

import org.eclipse.osgi.util.NLS;

/**
 * Validation Messages
 *
 * @author uidl6870
 *
 *         %created_by: uidl6458 %
 *
 *         %date_created: Wed Sep 17 14:46:06 2014 %
 *
 *         %version: 6 %
 */
@SuppressWarnings("javadoc")
public class Messages extends NLS
{
	// CHECKSTYLE:OFF

	public static String EMFIntrinsicConstraintsStatusEnabled;
	public static String EMFIntrinsicConstraintsStatusDisabled;

	public static String AUTOSARValidationPreferences;

	public static String ValidationTitle;
	public static String ValidationMessage;

	// CHECKSTYLE:ON

	static
	{
		NLS.initializeMessages(Messages.class.getCanonicalName(), Messages.class);
	}

}
