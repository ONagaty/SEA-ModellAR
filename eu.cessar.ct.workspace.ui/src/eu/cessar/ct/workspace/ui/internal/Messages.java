/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidl6870 Sep 24, 2009 1:58:59 PM </copyright>
 */
package eu.cessar.ct.workspace.ui.internal;

import org.eclipse.osgi.util.NLS;

/**
 * @author uidl6870
 *
 */
@SuppressWarnings("javadoc")
public class Messages extends NLS
{
	private final static String BUNDLE_NAME = Messages.class.getCanonicalName();

	// CHECKSTYLE:OFF
	public static String label_newProjectWizardTitle;
	public static String label_newProjectTitle;
	public static String label_newProjectDescription;

	public static String label_newAUTOSARFileDescription;

	public static String label_ProjectConversionTitle;
	public static String label_ProjectConversionDescription;

	public static String link_console_cannot_load_source;

	public static String link_console_stacktrace_unavailable;

	public static String xmlChecker_title;

	public static String xmlChecker_troubleshooting_msg_1;

	public static String xmlChecker_troubleshooting_msg_2;
	public static String xmlChecker_wizard_troubleshooting_hint1;
	public static String xmlChecker_wizard_troubleshooting_hint2;
	public static String xmlChecker_wizard_troubleshooting_hint3;
	public static String xmlChecker_wizard_troubleshooting_detail1;
	public static String xmlChecker_wizard_troubleshooting_detail2;
	public static String xmlChecker_wizard_troubleshooting_detail3;

	public static String xmlChecker_wizard_troubleshooting_note;

	public static String error_accesingResource;

	public static String noFilesFound_UnderSelection;
	public static String noFilesSelected;
	public static String selectFiles_message;

	public static String FAQ_url;
	public static String error_openingFAQ_title;
	public static String error_openingFAQ_message;

	public static String ModelResourceListener_DESC;

	public static String ModelResourceListener_ERROR;

	public static String ModelResourceListener_ERROR_DESC;

	public static String ModelResourceListener_MESSAGE;

	public static String ModelResourceListener_TITLE;

	public static String NEW_CESSAR_FILE_WIZARD_CONTEXT;

	public static String NewCessarFileWizard_DISABLE_LINKING_PREFERENCE;

	public static String ORG_ECLIPSE_CORE_RESOURCES;

	public static String projectChecker_title;
	public static String projectChecker_workspace_preferences_title;
	public static String ProjectConsistencyCheckWizard_title;

	public static String ProjectConsistencySettingsPage_title;
	public static String ProjectConsistencySettingsPage_message;
	public static String ProjectConsistencySettingsPage_check_for_duplicate_module_definitions;
	public static String ProjectConsistencySettingsPage_check_for_files_of_wrong_metamodel;
	public static String ProjectConsistencySettingsPage_check_for_duplicate_module_definitions_description;
	public static String ProjectConsistencySettingsPage_check_for_files_of_wrong_metamodel_description;
	public static String ProjectConsistencySettingsPage_check_for_jet_verification;
	public static String ProjectConsistencySettingsPage_check_for_jet_verification_description;

	public static String ProjectConsistencySettingsPage_Check_For_Classpath_Verification;

	public static String ProjectConsistencySettingsPage_Check_For_Classpath_Verification_Desc;

	public static String ProjectConsistencySettingsPage_Check_For_pmbin_Verification;

	public static String ProjectConsistencySettingsPage_Check_For_pmbin_Verification_Desc;

	public static String ProjectConsistencySettingsPage_Check_For_Project_Verification;

	public static String ProjectConsistencySettingsPage_Check_For_Project_Verification_Desc;

	public static String ProjectConsistencySettingsPage_Check_For_Settings_Verification;

	public static String ProjectConsistencySettingsPage_Check_For_Settings_Verification_Desc;

	public static String ProjectConsistencyReportPage_title;
	public static String ProjectConsistencyReportPage_message;
	public static String ProjectConsistencyReportPage_column_severity;
	public static String ProjectConsistencyReportPage_column_type;
	public static String ProjectConsistencyReportPage_column_description;
	public static String ProjectConsistencyReportPage_column_file_name;
	public static String ProjectConsistencyReportPage_button_copy_to_clipboard;

	// CHECKSTYLE:ON
	static
	{
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}
}
