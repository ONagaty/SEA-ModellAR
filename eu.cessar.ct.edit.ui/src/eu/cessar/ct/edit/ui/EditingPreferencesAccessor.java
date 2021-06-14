/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidl6458 Aug 5, 2010 7:02:31 PM </copyright>
 */
package eu.cessar.ct.edit.ui;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ProjectScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;

import eu.cessar.ct.core.platform.AbstractPreferencesAccessor;
import eu.cessar.ct.core.platform.util.ERadix;

/**
 * @author uidl6458
 *
 */
public class EditingPreferencesAccessor extends AbstractPreferencesAccessor
{
	public static final String NAMESPACE = "eu.cessar.ct.edit.ui"; //$NON-NLS-1$

	public static final String KEY_PROJECT_SPECIFIC = "projectSpecific";//$NON-NLS-1$

	public static final String KEY_FEATURE_NAME_PREFERENCE = "treeEditor.showFeatureName"; //$NON-NLS-1$

	public static final String KEY_FEATURE_TYPE_PREFERENCE = "treeEditor.showFeatureType"; //$NON-NLS-1$

	public static final String KEY_STRICT_MODE = "strictMode"; //$NON-NLS-1$

	public static final String KEY_SYS_INSTANCE_REF_PREFERENCE = "sysInstRef.showCandidatesForIncompleteConfigs"; //$NON-NLS-1$

	public static final String KEY_SHOW_AUTOM_FILL_VALUES_CONF_DIALOG = "showAutomaticFillOfValuesConfirmationDialog"; //$NON-NLS-1$

	public static final String KEY_SHOW__READ_ONLY_READ_WRITE_MSG_DIALOG = "showReadOnlyToReadWriteMessageDialog"; //$NON-NLS-1$

	public static final String KEY_ALLOW_TOWRITE_INSIDE_READONLY_FILE = "allowToEditReadOnlyFile";

	public static final String KEY_DONTALLOW_TOWRITE_INSIDE_READONLY_FILE = "doNotAllowToEditReadOnlyFile";

	public static final String KEY_OS_CLIPBOARD_TB_HEADER_ENABLED_PREFERENCE = "osClipboardText.headerEnabled"; //$NON-NLS-1$
	public static final boolean DEFAULT_OS_CLIPBOARD_TB_HEADER_ENABLED = true;

	public static final String KEY_CONFIG_RADIX = "configuration.radix"; //$NON-NLS-1$

	/**
	 * Key to store the configuration inside the preferences
	 */
	public static final String KEY_LINK_WITH_EDITOR_PREFERENCE = "linkWithEditor";

	/**
	 * returns if flag is set for project preference (true) or workspace preference (false)
	 *
	 * @param project
	 * @return
	 */
	public static boolean getProjectSpecific(IProject project)
	{
		return getBooleanPref(project, NAMESPACE, KEY_PROJECT_SPECIFIC, false);
	}

	/**
	 * Sets the project specific Flag. If the flag is true, the preferences will be returned from the Project preference
	 * store, if the flag is false the preferences will be returned from the workspace preference store
	 *
	 * @param project
	 */
	public static void setProjectSpecific(IProject project, boolean value)
	{
		setBooleanPref(project, NAMESPACE, KEY_PROJECT_SPECIFIC, value);
	}

	/**
	 * returns the active featureType flag. By active we understand the preference form the project preferences (if the
	 * ProjectSpecific flag is true), or from the workspace preferences (if the ProjectSpecific flag is false)
	 *
	 * @param project
	 * @return
	 */
	public static boolean getFeatureType(IProject project)
	{
		return getBooleanPrefWithWSDefault(project, NAMESPACE, KEY_PROJECT_SPECIFIC, KEY_FEATURE_TYPE_PREFERENCE, false);
	}

	/**
	 * sets the feature type flag in the project preferences
	 *
	 * @param projectSpecific
	 * @param project
	 * @param namespace
	 * @param key
	 * @param value
	 */
	public static void setFeatureTypeInProject(IProject project, boolean value)
	{
		setBooleanPref(project, NAMESPACE, KEY_FEATURE_TYPE_PREFERENCE, value);
	}

	/**
	 * sets the feature type flag in the workspace preference
	 *
	 * @param project
	 * @param value
	 */
	public static void setFeatureTypeInWS(boolean value)
	{
		setWorkspaceBooleanPref(NAMESPACE, KEY_FEATURE_TYPE_PREFERENCE, value);
	}

	/**
	 *
	 * return the active feature name flag. By active we understand the preference form the project preferences if the
	 * ProjectSpecific flag is true, or the preference form the workspace preferences if the ProjectSpecific flag is
	 * false
	 *
	 *
	 * @param project
	 * @return
	 */
	public static boolean getFeatureName(IProject project)
	{
		return getBooleanPrefWithWSDefault(project, NAMESPACE, KEY_PROJECT_SPECIFIC, KEY_FEATURE_NAME_PREFERENCE, false);
	}

	/**
	 * sets the feature Name flag in the project preferences
	 *
	 * @param project
	 * @param value
	 */
	public static void setFeatureNameInProject(IProject project, boolean value)
	{
		setBooleanPref(project, NAMESPACE, KEY_FEATURE_NAME_PREFERENCE, value);
	}

	/**
	 * sets the feature Name flag in the workspace preferences
	 *
	 * @param project
	 * @param value
	 */
	public static void setFeatureNameInWS(boolean value)
	{
		setWorkspaceBooleanPref(NAMESPACE, KEY_FEATURE_NAME_PREFERENCE, value);
	}

	/**
	 * returns the active StrictMode flag. By active we understand the preference form the project preferences if the
	 * ProjectSpecific flag is true, or the preference form the workspace preferences if the ProjectSpecific flag is
	 * false
	 *
	 * @param project
	 * @return
	 */
	public static boolean getStrictMode(IProject project)
	{
		return getBooleanPrefWithWSDefault(project, NAMESPACE, KEY_PROJECT_SPECIFIC, KEY_STRICT_MODE, false);
	}

	/**
	 * sets the strictMode flag in the project preference
	 *
	 * @param project
	 * @param value
	 */
	public static void setStrictModeInProject(IProject project, boolean value)
	{
		setBooleanPref(project, NAMESPACE, KEY_STRICT_MODE, value);
	}

	/**
	 * sets the strictMode flag in the workspace preference
	 *
	 * @param value
	 */
	public static void setStrictModeInWS(boolean value)
	{
		setWorkspaceBooleanPref(NAMESPACE, KEY_STRICT_MODE, value);
	}

	/**
	 * returns the active configRadix value. By active we understand the preference from the project preferences if the
	 * ProjectSpecific flag is true, or the preference from the workspace preferences if the ProjectSpecific flag is
	 * false
	 *
	 * @param project
	 * @return
	 */
	public static String getConfigRadix(IProject project)
	{
		return getStringPrefWithWSDefault(project, NAMESPACE, KEY_PROJECT_SPECIFIC, KEY_CONFIG_RADIX,
			ERadix.DECIMAL.getLiteral());
	}

	/**
	 * sets the configRadix flag in the project preference
	 *
	 * @param project
	 * @param value
	 */
	public static void setConfigRadixInProject(IProject project, String value)
	{
		// setBooleanPref(project, NAMESPACE, KEY_CONFIG_RADIX, value);
		setStringPref(project, NAMESPACE, KEY_CONFIG_RADIX, value);
	}

	/**
	 * sets the configRadix flag in the workspace preference
	 *
	 * @param value
	 */
	public static void setConfigRadixInWS(String value)
	{
		setWorkspaceStringPref(NAMESPACE, KEY_CONFIG_RADIX, value);

	}

	/**
	 * Get the preference regarding how the candidates for a system instance reference shall be computed (whether
	 * complete/partial configuration are permitted)
	 *
	 * @param project
	 * @return <code>true</code> if
	 */
	public static boolean getSysInstanceRefPref(IProject project)
	{
		return getBooleanPrefWithWSDefault(project, NAMESPACE, KEY_PROJECT_SPECIFIC, KEY_SYS_INSTANCE_REF_PREFERENCE,
			false);
	}

	/**
	 * Sets flag for the instance references in the project preferences if projectSpecific flag is enabled, otherwise in
	 * the workspace preference
	 *
	 * @param value
	 */
	public static void setSysInstanceRefPrefInProject(IProject project, boolean value)
	{
		boolean projectSpecificEnabled = false;
		if (project != null)
		{
			ProjectScope projectScope = new ProjectScope(project);
			IEclipsePreferences projectPreferences = projectScope.getNode(NAMESPACE);
			projectSpecificEnabled = projectPreferences.getBoolean(KEY_PROJECT_SPECIFIC, true);
		}
		if (projectSpecificEnabled)
		{
			setBooleanPref(project, NAMESPACE, KEY_SYS_INSTANCE_REF_PREFERENCE, value);
		}
		else
		{
			setSysInstanceRefPrefInWS(value);
		}
	}

	/**
	 * Get the preference regarding display of the confirmation dialog for the automatic fill of values.
	 *
	 * @param project
	 * @return <code>true</code> if
	 */
	public static boolean getAutomaticFillOfValuesConfirmationDialogStatus()
	{
		return getWorkspaceBooleanPref(NAMESPACE, KEY_SHOW_AUTOM_FILL_VALUES_CONF_DIALOG);
	}

	/**
	 * Sets flag for the display of the confirmation dialog for the automatic fill of values.
	 *
	 * @param project
	 *
	 * @param value
	 */
	public static void setAutomaticFillOfValuesConfirmationDialogStatus(boolean value)
	{
		setWorkspaceBooleanPref(NAMESPACE, KEY_SHOW_AUTOM_FILL_VALUES_CONF_DIALOG, value);
	}

	/**
	 * Get the preference regarding display of the Message dialog for the autosar read only files.
	 *
	 * @param project
	 * @return <code>true</code> if
	 */
	public static boolean getReadOnlyToReadWriteMessageDialogStatus()
	{
		return getWorkspaceBooleanPref(NAMESPACE, KEY_SHOW__READ_ONLY_READ_WRITE_MSG_DIALOG);
	}

	/**
	 * Sets flag for the display of the Message dialog for the autosar read only files.
	 *
	 * @param value
	 */
	public static void setReadOnlyToReadWriteMessageDialogStatus(boolean value)
	{
		setWorkspaceBooleanPref(NAMESPACE, KEY_SHOW__READ_ONLY_READ_WRITE_MSG_DIALOG, value);
	}

	/**
	 * set Flag true when "Do not show dialog is selected " and "OK" is pressed.
	 *
	 * @return
	 */
	public static boolean getAllowToWriteInsideReadOnlyFiles()
	{

		return getWorkspaceBooleanPref(NAMESPACE, KEY_ALLOW_TOWRITE_INSIDE_READONLY_FILE);
	}

	/**
	 * Gets the preference regarding Allow to write inside RO files.
	 *
	 * @param value
	 * @return <code>true</code> if
	 */
	public static void setAllowToWriteInsideReadOnlyFiles(boolean value)
	{
		setWorkspaceBooleanPref(NAMESPACE, KEY_ALLOW_TOWRITE_INSIDE_READONLY_FILE, value);
	}

	/**
	 * Gets the preference regarding Do now Allow to write inside RO files.
	 *
	 * @return <code>true</code> if
	 */
	public static boolean getDoNotAllowToWriteInsideReadOnlyFiles()
	{

		return getWorkspaceBooleanPref(NAMESPACE, KEY_DONTALLOW_TOWRITE_INSIDE_READONLY_FILE);
	}

	/**
	 * set Flag true when "Do not show dialog is selected " and "Cancel" is pressed.
	 */
	public static void setDoNotAllowToWriteInsideReadOnlyFiles(boolean value)
	{
		setWorkspaceBooleanPref(NAMESPACE, KEY_DONTALLOW_TOWRITE_INSIDE_READONLY_FILE, value);
	}

	/**
	 * Sets flag for the instance references in the workspace preference
	 *
	 * @param project
	 * @param value
	 */
	public static void setSysInstanceRefPrefInWS(Boolean value)
	{
		setWorkspaceBooleanPref(NAMESPACE, KEY_SYS_INSTANCE_REF_PREFERENCE, value);
	}

	/**
	 * Sets the copy to Clipboard table header generation preference in project/workspace.
	 *
	 * @param project
	 *        the project to save the preference in
	 * @param selection
	 *        the value to set
	 */
	public static void setCopyToOSClipboardHeaderGenerationEnabled(IProject project, boolean value)
	{
		if (project != null)
		{
			setBooleanPref(project, NAMESPACE, KEY_OS_CLIPBOARD_TB_HEADER_ENABLED_PREFERENCE, value);
		}
		else
		{
			setWorkspaceBooleanPref(NAMESPACE, KEY_OS_CLIPBOARD_TB_HEADER_ENABLED_PREFERENCE, value);
		}

	}

	/**
	 * Gets the copy to Clipboard table header generation preference in project/workspace.
	 *
	 * @param project
	 *        the project to get the preference from
	 * @return the preference boolean value
	 */
	public static boolean getCopyToOSClipboardHeaderGenerationEnabled(IProject project)
	{
		return getBooleanPrefWithWSDefault(project, NAMESPACE, KEY_PROJECT_SPECIFIC,
			KEY_OS_CLIPBOARD_TB_HEADER_ENABLED_PREFERENCE, DEFAULT_OS_CLIPBOARD_TB_HEADER_ENABLED);
	}

	/**
	 * Sets the value of the Link With Editor option from Preferences -> CESSAR -> Table View
	 *
	 * @param project
	 * @param value
	 */
	public static void setLinkWithEditor(IProject project, boolean value)
	{
		if (project != null)
		{
			setBooleanPref(project, NAMESPACE, KEY_LINK_WITH_EDITOR_PREFERENCE, value);
		}
		else
		{
			setWorkspaceBooleanPref(NAMESPACE, KEY_LINK_WITH_EDITOR_PREFERENCE, value);
		}
	}

	/**
	 * Gets the value of Link With Editor option from Preferences -> CESSAR -> Table View
	 *
	 * @param project
	 * @return {@link Boolean}
	 */
	public static boolean getLinkWithEditor(IProject project)
	{
		return getBooleanPrefWithWSDefault(project, NAMESPACE, KEY_PROJECT_SPECIFIC, KEY_LINK_WITH_EDITOR_PREFERENCE,
			false);
	}

}
