/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidj9791<br/>
 * Jan 7, 2015 5:34:55 PM
 *
 * </copyright>
 */
package eu.cessar.ct.validation.ui.preferences;

import org.eclipse.osgi.util.NLS;

/**
 * Accesor class for externalized strings.
 *
 * @author uidj9791
 *
 *         %created_by: uidj9791 %
 *
 *         %date_created: Mon Jan 12 11:48:40 2015 %
 *
 *         %version: 1 %
 */
public class CessarValidationUIMessages extends NLS
{
	private static final String BUNDLE_NAME = "eu.cessar.ct.validation.ui.preferences.CessarValidationUIMessages"; //$NON-NLS-1$

	public static String preferences_no_selection;
	public static String preferences_no_category_description;
	public static String preferences_no_constraint_description;
	public static String preferences_mandatory_category;
	public static String preferences_mandatory_constraint;
	public static String preferences_error_constraint_description;
	public static String preferences_constraint_description;

	static
	{
		NLS.initializeMessages(BUNDLE_NAME, CessarValidationUIMessages.class);
	}
}
