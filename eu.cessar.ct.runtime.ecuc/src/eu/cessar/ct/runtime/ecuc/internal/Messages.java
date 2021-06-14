/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidl6870 Nov 4, 2009 5:19:23 PM </copyright>
 */
package eu.cessar.ct.runtime.ecuc.internal;

import org.eclipse.osgi.util.NLS;

/**
 *
 */
@SuppressWarnings("javadoc")
public class Messages extends NLS
{

	public static String InvalidShortName;
	public static String UnsetShortName;
	public static String ModuleDefsWithSameQualifiedName;
	public static String ModuleDef_ARPackageWithSameQualifiedName;
	public static String Project_not_valid;

	public static String NoActiveConfiguration;
	public static String Invalid_null_argument;
	public static String Invalid_null_argument_withName;
	public static String Invalid_activeSource;
	public static String Could_Not_Save;
	/**
	 * Error message for null module configuration
	 */
	public static String errorNullModuleConfig;
	public static String errorNullRefinedDefinition;
	public static String errorNullStandardDefinition;
	public static String infoExecuteRefinedToStandardConversion;
	public static String infoExecuteStandardToRefinedConversion;
	public static String Transaction_Failed;

	public static String message_dump;
	public static String message_writePM;
	public static String message_reload;

	public static String DEST_URI_POLICY;

	private static final String BUNDLE_NAME = Messages.class.getCanonicalName();

	static
	{
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}
}
