/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidl6870 Sep 24, 2009 12:38:35 PM </copyright>
 */
package eu.cessar.ct.workspace.internal;

import org.eclipse.osgi.util.NLS;

/**
 * @author uidl6870
 *
 */
@SuppressWarnings("javadoc")
public class Messages extends NLS
{

	// CHECKSTYLE:OFF

	public static String CessarProjectJob_AddNatures;
	public static String CheckForClasspathVerification_classpath;
	public static String CheckForClasspathVerification_classpath_Node;
	public static String CheckForClasspathVerification_error_classpathLibraries;
	public static String CheckForClasspathVerification_path_Node;
	public static String CheckForPmbinFolderVerification_error_pmbinMissing;
	public static String CheckForPmbinFolderVerification_pmbin_FolderName;
	public static String CheckForProjectVerification_buildCommand_nodePath;
	public static String CheckForProjectVerification_error_BuildCommands;
	public static String CheckForProjectVerification_error_projectNatures;
	public static String CheckForProjectVerification_natures_nodePath;
	public static String CheckForProjectVerification_project_FolderName;
	public static String CheckForSettingsVerification_artop_Preference;
	public static String CheckForSettingsVerification_core_Preferences;
	public static String CheckForSettingsVerification_error_AutosarVersion;
	public static String CheckForSettingsVerification_error_ConfigurationVariant;
	public static String CheckForSettingsVerification_error_settings;
	public static String CheckForSettingsVerification_setting_FolderName;
	public static String job_creatingAutosarProject;

	public static String saxException_element_message_detailed;
	public static String saxException_value_message_detailed;
	public static String saxException_attribute_message_detailed;

	// consistency check
	public static String extension_missing_element;

	public static String wrongModel_inconsistency;
	public static String error_invalidVersion;

	public static String error_UnCompilledJet;
	public static String error_OldJarFile;

	public static String JVMParameters_noINIFileFound;
	public static String JVMParameters_invalid_parameter_value;

	public static String JVMParameters_Xmx_detailed_description;
	public static String JVMParameters_Xss_detailed_description;
	public static String JVMParameters_validation_max_value;

	public static String LoggerImpl_EMPTY_STRING;
	public static String LoggerImpl_NO_MESSAGE_FOR_ID;
	public static String LoggerImpl_STACK_TRACE_FORMAT;
	// CHECKSTYLE:ON
	private final static String BUNDLE_NAME = Messages.class.getCanonicalName();

	static
	{
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

}
