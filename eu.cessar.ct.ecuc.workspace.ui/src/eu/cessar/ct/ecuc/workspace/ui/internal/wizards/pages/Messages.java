/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 *
 * File created by uid95856<br/>
 * 8 ian. 2015 11:38:05
 *
 * </copyright>
 */
package eu.cessar.ct.ecuc.workspace.ui.internal.wizards.pages;

import org.eclipse.osgi.util.NLS;

/**
 * UI texts for this package.
 *
 * @author uid95856
 *
 *         %created_by: uidu4748 %
 *
 *         %date_created: Wed May 20 16:48:37 2015 %
 *
 *         %version: RAUTOSAR~4 %
 */
@SuppressWarnings("javadoc")
public final class Messages extends NLS
{
	private static final String BUNDLE_NAME = "eu.cessar.ct.ecuc.workspace.ui.internal.wizards.pages.messages"; //$NON-NLS-1$

	// CHECKSTYLE:OFF
	public static String ADVANCED;
	public static String ADVANCED_DESCRIPTION;
	public static String CESSAR_CT_PLUGET;
	public static String CLASS_ALREADY_EXISTS_IN_FOLDER;
	public static String DEFAULT_PACKAGE;
	public static String EMPTY_STRING;
	public static String ENTER_CLASS_NAME;
	public static String ENTER_PACKAGE;
	public static String ENTER_VALID_CLASSNAME;
	public static String CLASS_NAME_REGEX;
	public static String CODE_TEMPLATE;
	public static String PACKAGE_NAME_REGEX;
	public static String INVALID_PACKAGE;
	public static String JAVA_EXT;
	public static String NEW_PLUGET_WIZARD_PAGE;
	public static String NEWPLUGET_PACKAGE;
	public static String CREATE_NEW_SOURCE_FOLDER;
	public static String NO_SOURCE_FOLDERS;
	public static String PACKAGE_NAME;
	public static String PLUGET_CLASS_NAME;
	public static String SELECT_SOURCE_FOLDER;
	public static String SIMPLE;
	public static String SIMPLE_DESCRIPTION;
	public static String SLASH;
	public static String SOURCE_FOLDER;
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
