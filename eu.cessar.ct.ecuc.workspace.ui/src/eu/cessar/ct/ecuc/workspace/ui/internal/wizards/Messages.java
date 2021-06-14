/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 *
 * File created by uid95856<br/>
 * 8 ian. 2015 11:42:06
 *
 * </copyright>
 */
package eu.cessar.ct.ecuc.workspace.ui.internal.wizards;

import org.eclipse.osgi.util.NLS;

/**
 * TODO: Please comment this class
 *
 * @author uid95856
 *
 *         %created_by: uid95856 %
 *
 *         %date_created: Fri Feb  6 12:31:22 2015 %
 *
 *         %version: 2 %
 */
public class Messages extends NLS
{
	private static final String BUNDLE_NAME = "eu.cessar.ct.ecuc.workspace.ui.internal.wizards.messages"; //$NON-NLS-1$
	public static String CLASS_NAME;
	public static String PACKAGE_NAME;
	public static String DOT;
	public static String LINE_SEPARATOR;
	public static String NEW_CESSAR_CT_PLUGET;
	public static String NEW_PLUGET_PAGE;
	public static String PACKAGE;
	public static String PATH_TO_TEMPLATE_SIMPLE;
	public static String PATH_TO_TEMPLATE_ADVANCED;
	public static String SLASH;
	static
	{
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages()
	{
	}
}
