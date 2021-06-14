/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidw8762<br/>
 * Feb 27, 2014 10:43:10 AM
 * 
 * </copyright>
 */
package eu.cessar.ct.workspace.internal.preferences;

import org.eclipse.core.resources.IProject;

import eu.cessar.ct.core.platform.AbstractPreferencesAccessor;
import eu.cessar.ct.workspace.consistencycheck.EProjectInconsistencyType;

/**
 * ProjectConsistencyPreferencesAccessor class for encapsulation of project consistency check type preferences.
 * 
 * @author uidw8762
 * 
 *         %created_by: uidu8153 %
 * 
 *         %date_created: Tue Jun 23 10:48:24 2015 %
 * 
 *         %version: RAUTOSAR~7 %
 */
public class ProjectConsistencyPreferencesAccessor extends AbstractPreferencesAccessor
{

	/** The Constant KEY_CHECK_DUPLICATE_MODULE_DEFINITIONS. */
	public static final String KEY_CHECK_DUPLICATE_MODULE_DEFINITIONS = "checkDuplicateModuleDefinitions";//$NON-NLS-1$

	/** The Constant KEY_CHECK_WRONG_METAMODEL_FILES. */
	public static final String KEY_CHECK_WRONG_METAMODEL_FILES = "checkWrongMetamodelFiles";//$NON-NLS-1$

	/** The Constant KEY_CHECK_JET_VERIFICATION. */
	public static final String KEY_CHECK_JET_VERIFICATION = "checkJetVerification";//$NON-NLS-1$

	/** The Constant KEY_CHECK_PROJECT_VERIFICATION. */
	public static final String KEY_CHECK_PROJECT_VERIFICATION = "checkProjectVerification";//$NON-NLS-1$

	/** The Constant KEY_CHECK_CLASSPATH_VERIFICATION. */
	public static final String KEY_CHECK_CLASSPATH_VERIFICATION = "checkClasspathVerification";//$NON-NLS-1$

	/** The Constant KEY_CHECK_SETTINGS_VERIFICATION. */
	public static final String KEY_CHECK_SETTINGS_VERIFICATION = "checkSettingsVerification";//$NON-NLS-1$

	/** The Constant KEY_CHECK_PMBIN_VERIFICATION. */
	public static final String KEY_CHECK_PMBIN_VERIFICATION = "checkPmbinVerification";//$NON-NLS-1$

	/** The Constant KEY_CHECK_NONE. */
	public static final String KEY_CHECK_NONE = "checkNone";//$NON-NLS-1$

	// shares the same namespace as the EditingPreferencesAccessor
	public static final String NAMESPACE = "eu.cessar.project.consistency"; //$NON-NLS-1$

	/** The Constant KEY_PROJECT_SPECIFIC. */
	public static final String KEY_PROJECT_SPECIFIC = "projectSpecific";//$NON-NLS-1$

	/**
	 * Returns if flag is set for project preference (true) or workspace preference (false).
	 * 
	 * @param project
	 *        the project
	 * @return the project specific
	 */
	public static boolean getProjectSpecific(IProject project)
	{
		return getBooleanPref(project, NAMESPACE, KEY_PROJECT_SPECIFIC, false);
	}

	/**
	 * Sets the project specific.
	 * 
	 * @param project
	 *        the project
	 * @param value
	 *        the value
	 */
	public static void setProjectSpecific(IProject project, boolean value)
	{
		setBooleanPref(project, NAMESPACE, KEY_PROJECT_SPECIFIC, value);
	}

	/**
	 * Return the project consistency check type associated with the workspace
	 * 
	 * @param projectInconsistencyType
	 * 
	 * @param EProjectInconsistencyType
	 * 
	 * @return EProjectInconsistencyType
	 */
	public static EProjectInconsistencyType getEProjectInconsistencyType(
		EProjectInconsistencyType projectInconsistencyType)
	{
		String value = ""; //$NON-NLS-1$

		switch (projectInconsistencyType)
		{
			case DUPLICATE_MODULE_DEF:
				value = getWSStringPref(NAMESPACE, KEY_CHECK_DUPLICATE_MODULE_DEFINITIONS,
					EProjectInconsistencyType.DUPLICATE_MODULE_DEF.getName());
				break;

			case WRONG_MODEL:
				value = getWSStringPref(NAMESPACE, KEY_CHECK_WRONG_METAMODEL_FILES,
					EProjectInconsistencyType.WRONG_MODEL.getName());
				break;

			case JET_PROBLEMS:
				value = getWSStringPref(NAMESPACE, KEY_CHECK_JET_VERIFICATION,
					EProjectInconsistencyType.JET_PROBLEMS.getName());
				break;

			case PROJECT_PROBLEMS:
				value = getWSStringPref(NAMESPACE, KEY_CHECK_PROJECT_VERIFICATION,
					EProjectInconsistencyType.PROJECT_PROBLEMS.getName());
				break;

			case CLASSPATH_PROBLEMS:
				value = getWSStringPref(NAMESPACE, KEY_CHECK_CLASSPATH_VERIFICATION,
					EProjectInconsistencyType.CLASSPATH_PROBLEMS.getName());
				break;

			case SETTINGS_PROBLEMS:
				value = getWSStringPref(NAMESPACE, KEY_CHECK_SETTINGS_VERIFICATION,
					EProjectInconsistencyType.SETTINGS_PROBLEMS.getName());
				break;

			case PMBIN_PROBLEMS:
				value = getWSStringPref(NAMESPACE, KEY_CHECK_PMBIN_VERIFICATION,
					EProjectInconsistencyType.PMBIN_PROBLEMS.getName());
				break;
			default:
				value = getWSStringPref(NAMESPACE, KEY_CHECK_NONE,
					eu.cessar.ct.workspace.consistencycheck.EProjectInconsistencyType.NONE.getName());
		}

		return EProjectInconsistencyType.getProjectConsistencyTypeFromName(value);
	}

	/**
	 * Gets the project consistency check type.
	 * 
	 * @param project
	 *        the project
	 * @param projectInconsistencyType
	 * @param EProjectInconsistencyType
	 *        the project consistency check type
	 * @return the project consistency check type
	 */
	public static EProjectInconsistencyType getEProjectInconsistencyType(IProject project,
		EProjectInconsistencyType projectInconsistencyType)
	{
		EProjectInconsistencyType result = eu.cessar.ct.workspace.consistencycheck.EProjectInconsistencyType.NONE;

		if (project != null)
		{
			if (getProjectSpecific(project))
			{
				String value = ""; //$NON-NLS-1$

				switch (projectInconsistencyType)
				{
					case DUPLICATE_MODULE_DEF:
						value = getStringPref(
							project,
							NAMESPACE,
							KEY_CHECK_DUPLICATE_MODULE_DEFINITIONS,
							eu.cessar.ct.workspace.consistencycheck.EProjectInconsistencyType.DUPLICATE_MODULE_DEF.getName());
						break;

					case WRONG_MODEL:
						value = getStringPref(project, NAMESPACE, KEY_CHECK_WRONG_METAMODEL_FILES,
							eu.cessar.ct.workspace.consistencycheck.EProjectInconsistencyType.WRONG_MODEL.getName());
						break;

					case JET_PROBLEMS:
						value = getStringPref(project, NAMESPACE, KEY_CHECK_JET_VERIFICATION,
							eu.cessar.ct.workspace.consistencycheck.EProjectInconsistencyType.JET_PROBLEMS.getName());
						break;

					case PROJECT_PROBLEMS:
						value = getStringPref(
							project,
							NAMESPACE,
							KEY_CHECK_PROJECT_VERIFICATION,
							eu.cessar.ct.workspace.consistencycheck.EProjectInconsistencyType.PROJECT_PROBLEMS.getName());
						break;

					case CLASSPATH_PROBLEMS:
						value = getStringPref(
							project,
							NAMESPACE,
							KEY_CHECK_CLASSPATH_VERIFICATION,
							eu.cessar.ct.workspace.consistencycheck.EProjectInconsistencyType.CLASSPATH_PROBLEMS.getName());
						break;

					case SETTINGS_PROBLEMS:
						value = getStringPref(
							project,
							NAMESPACE,
							KEY_CHECK_SETTINGS_VERIFICATION,
							eu.cessar.ct.workspace.consistencycheck.EProjectInconsistencyType.SETTINGS_PROBLEMS.getName());
						break;

					case PMBIN_PROBLEMS:
						value = getStringPref(project, NAMESPACE, KEY_CHECK_PMBIN_VERIFICATION,
							eu.cessar.ct.workspace.consistencycheck.EProjectInconsistencyType.PMBIN_PROBLEMS.getName());
						break;
					default:
						value = getStringPref(project, NAMESPACE, KEY_CHECK_NONE,
							eu.cessar.ct.workspace.consistencycheck.EProjectInconsistencyType.NONE.getName());
				}

				result = eu.cessar.ct.workspace.consistencycheck.EProjectInconsistencyType.getProjectConsistencyTypeFromName(value);
			}
			else
			{
				result = getEProjectInconsistencyType(projectInconsistencyType);
			}
		}
		else
		{
			result = getEProjectInconsistencyType(projectInconsistencyType);
		}
		return result;
	}

	/**
	 * Sets the project consistency check type.
	 * 
	 * @param value
	 *        the new project consistency check type
	 */
	public static void setEProjectInconsistencyType(EProjectInconsistencyType value)
	{
		switch (value)
		{
			case DUPLICATE_MODULE_DEF:
				setWorkspaceStringPref(NAMESPACE, KEY_CHECK_DUPLICATE_MODULE_DEFINITIONS, value.getName());
				break;

			case WRONG_MODEL:
				setWorkspaceStringPref(NAMESPACE, KEY_CHECK_WRONG_METAMODEL_FILES, value.getName());
				break;

			case JET_PROBLEMS:
				setWorkspaceStringPref(NAMESPACE, KEY_CHECK_JET_VERIFICATION, value.getName());
				break;

			case PROJECT_PROBLEMS:
				setWorkspaceStringPref(NAMESPACE, KEY_CHECK_PROJECT_VERIFICATION, value.getName());
				break;

			case CLASSPATH_PROBLEMS:
				setWorkspaceStringPref(NAMESPACE, KEY_CHECK_CLASSPATH_VERIFICATION, value.getName());
				break;

			case SETTINGS_PROBLEMS:
				setWorkspaceStringPref(NAMESPACE, KEY_CHECK_SETTINGS_VERIFICATION, value.getName());
				break;
			case PMBIN_PROBLEMS:
				setWorkspaceStringPref(NAMESPACE, KEY_CHECK_PMBIN_VERIFICATION, value.getName());
				break;
			default:
		}
	}

	/**
	 * Reset workspace preferences.
	 */
	public static void resetWSPreferences()
	{
		setWorkspaceStringPref(NAMESPACE, KEY_CHECK_DUPLICATE_MODULE_DEFINITIONS,
			EProjectInconsistencyType.NONE.getName());
		setWorkspaceStringPref(NAMESPACE, KEY_CHECK_WRONG_METAMODEL_FILES, EProjectInconsistencyType.NONE.getName());
		setWorkspaceStringPref(NAMESPACE, KEY_CHECK_JET_VERIFICATION, EProjectInconsistencyType.NONE.getName());
		setWorkspaceStringPref(NAMESPACE, KEY_CHECK_PROJECT_VERIFICATION, EProjectInconsistencyType.NONE.getName());
		setWorkspaceStringPref(NAMESPACE, KEY_CHECK_CLASSPATH_VERIFICATION, EProjectInconsistencyType.NONE.getName());
		setWorkspaceStringPref(NAMESPACE, KEY_CHECK_SETTINGS_VERIFICATION, EProjectInconsistencyType.NONE.getName());
		setWorkspaceStringPref(NAMESPACE, KEY_CHECK_PMBIN_VERIFICATION, EProjectInconsistencyType.NONE.getName());

	}

	/**
	 * Reset project preferences.
	 * 
	 * @param project
	 */
	public static void resetProjectPreferences(IProject project)
	{
		if (project != null)
		{
			setStringPref(project, NAMESPACE, KEY_CHECK_DUPLICATE_MODULE_DEFINITIONS,
				EProjectInconsistencyType.NONE.getName());
			setStringPref(project, NAMESPACE, KEY_CHECK_WRONG_METAMODEL_FILES, EProjectInconsistencyType.NONE.getName());
			setStringPref(project, NAMESPACE, KEY_CHECK_JET_VERIFICATION, EProjectInconsistencyType.NONE.getName());
			setStringPref(project, NAMESPACE, KEY_CHECK_PROJECT_VERIFICATION, EProjectInconsistencyType.NONE.getName());
			setStringPref(project, NAMESPACE, KEY_CHECK_CLASSPATH_VERIFICATION,
				EProjectInconsistencyType.NONE.getName());
			setStringPref(project, NAMESPACE, KEY_CHECK_SETTINGS_VERIFICATION, EProjectInconsistencyType.NONE.getName());
			setStringPref(project, NAMESPACE, KEY_CHECK_PMBIN_VERIFICATION, EProjectInconsistencyType.NONE.getName());
		}
	}

	/**
	 * Sets the project consistency check type.
	 * 
	 * @param project
	 *        the project
	 * @param value
	 *        the value
	 */
	public static void setEProjectInconsistencyType(IProject project, EProjectInconsistencyType value)
	{
		if (project != null)
		{
			if (getProjectSpecific(project))
			{
				// Add to project preferences
				switch (value)
				{
					case DUPLICATE_MODULE_DEF:
						setStringPref(project, NAMESPACE, KEY_CHECK_DUPLICATE_MODULE_DEFINITIONS, value.getName());
						break;

					case WRONG_MODEL:
						setStringPref(project, NAMESPACE, KEY_CHECK_WRONG_METAMODEL_FILES, value.getName());
						break;

					case JET_PROBLEMS:
						setStringPref(project, NAMESPACE, KEY_CHECK_JET_VERIFICATION, value.getName());
						break;

					case PROJECT_PROBLEMS:
						setStringPref(project, NAMESPACE, KEY_CHECK_PROJECT_VERIFICATION, value.getName());
						break;

					case CLASSPATH_PROBLEMS:
						setStringPref(project, NAMESPACE, KEY_CHECK_CLASSPATH_VERIFICATION, value.getName());
						break;

					case SETTINGS_PROBLEMS:
						setStringPref(project, NAMESPACE, KEY_CHECK_SETTINGS_VERIFICATION, value.getName());
						break;

					case PMBIN_PROBLEMS:
						setStringPref(project, NAMESPACE, KEY_CHECK_PMBIN_VERIFICATION, value.getName());
						break;

					default:
						setStringPref(project, NAMESPACE, KEY_CHECK_NONE, value.getName());
				}
			}
		}
		else
		{
			// Add to workspace preferences
			setEProjectInconsistencyType(value);
		}
	}

}
