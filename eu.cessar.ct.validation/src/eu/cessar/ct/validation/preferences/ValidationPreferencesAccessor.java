/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 26.06.2012 17:32:38 </copyright>
 */
package eu.cessar.ct.validation.preferences;

import org.eclipse.core.resources.IProject;

import eu.cessar.ct.core.platform.AbstractPreferencesAccessor;

/**
 * 
 * Dedicated preference accessor for live validation
 * 
 * @author uidl6458
 * 
 */
public class ValidationPreferencesAccessor extends AbstractPreferencesAccessor
{

	/** Validation type key */
	public static final String KEY_VALIDATION_TYPE = "validationType";//$NON-NLS-1$

	// shares the same namespace as the EditingPreferencesAccessor
	/** Namespace */
	public static final String NAMESPACE = "eu.cessar.edit.ui"; //$NON-NLS-1$
	/** Project specific preferences key */
	public static final String KEY_PROJECT_SPECIFIC = "projectSpecific";//$NON-NLS-1$
	/**
	 * This key is used to store the preference for the filtered validation behavior
	 */
	public static final String KEY_HIDE_FILTERED_VALIDATION_WIZARD = "hideFilteredValidationWizard";//$NON-NLS-1$

	/**
	 * returns if flag is set for project preference (true) or workspace preference (false)
	 * 
	 * @param project
	 * @return project specific preference value
	 */
	public static boolean getProjectSpecific(IProject project)
	{
		return getBooleanPref(project, NAMESPACE, KEY_PROJECT_SPECIFIC, false);
	}

	/**
	 * @param project
	 * @param b
	 */
	public static void setProjectSpecific(IProject project, boolean b)
	{
		setBooleanPref(project, NAMESPACE, KEY_PROJECT_SPECIFIC, b);
	}

	/**
	 * Return the validation type associated with the workspace
	 * 
	 * @return validation type associated with the workspace
	 */
	public static EValidationType getValidationType()
	{
		String value = getWSStringPref(NAMESPACE, KEY_VALIDATION_TYPE, EValidationType.FULL.getName());
		return EValidationType.getEValidationTypeFromName(value);
	}

	/**
	 * Return the validation type associated with a project or the workspace one if there is no such validation flag set
	 * 
	 * @param project
	 * @return the validation type associated with a project or the workspace one
	 */
	public static EValidationType getValidationType(IProject project)
	{
		if (project == null)
		{
			return null;
		}
		if (getProjectSpecific(project))
		{
			String value = getStringPref(project, NAMESPACE, KEY_VALIDATION_TYPE, EValidationType.FULL.getName());
			return EValidationType.getEValidationTypeFromName(value);
		}
		else
		{
			return getValidationType();
		}
	}

	/**
	 * it will add the value to the workspace preferences
	 * 
	 * @param value
	 */
	public static void setValidationType(EValidationType value)
	{
		if (value != null)
		{
			setWorkspaceStringPref(NAMESPACE, KEY_VALIDATION_TYPE, value.getName());
		}
	}

	/**
	 * If the project has the project specific flag set <br>
	 * <b>then</b><br>
	 * it will add the preference to the project specific preferences,<br>
	 * <b>else</b> <br>
	 * it will add it to the workspace preferences
	 * 
	 * @param project
	 * @param value
	 */
	public static void setValidationType(IProject project, EValidationType value)
	{
		if (project != null)
		{
			if (getProjectSpecific(project))
			{
				// add to project preferences
				setEnumPref(project, NAMESPACE, KEY_VALIDATION_TYPE, value);
			}
			else
			{
				// add to workspace preferences
				setValidationType(value);
			}
		}
	}

	/**
	 * 
	 * Return the filtered validation type associated with a project or the workspace one if there is no such filtered validation
	 * flag set
	 * 
	 * @param project
	 * @return boolean filtered validation flag value
	 */
	public static boolean getHideFilteredValidationWizardFlag(IProject project)
	{
		if (project == null)
		{
			boolean hideFilteredValidationWizardFlag = getHideFilteredValidationWizardFlag();
			return hideFilteredValidationWizardFlag;
		}
		boolean projectSpecific = getProjectSpecific(project);
		if (projectSpecific)
		{
			boolean value = getBooleanPref(project, NAMESPACE, KEY_HIDE_FILTERED_VALIDATION_WIZARD, false);
			return value;
		}
		else
		{
			return getHideFilteredValidationWizardFlag();

		}
	}

	/**
	 * 
	 * Return the filtered validation type associated with the workspace
	 * 
	 * @param project
	 * @return boolean filtered validation flag value
	 */
	public static boolean getHideFilteredValidationWizardFlag()
	{
		boolean value = getWorkspaceBooleanPref(NAMESPACE, KEY_HIDE_FILTERED_VALIDATION_WIZARD);
		return value;
	}

	/**
	 * Set the filtered validation flag in the project preferences. <br>
	 * If the project has the project specific flag set <br>
	 * <b>then</b><br>
	 * it will add the preference to the project specific preferences,<br>
	 * <b>else</b> <br>
	 * it will add it to the workspace preferences
	 * 
	 * 
	 * @param project
	 * @param value
	 */
	public static void setHideFilteredValidationWizardFlag(IProject project, boolean value)
	{

		if (project != null)
		{
			if (getProjectSpecific(project))
			{
				// add to project preferences
				setBooleanPref(project, NAMESPACE, KEY_HIDE_FILTERED_VALIDATION_WIZARD, value);
			}
			else
			{
				// add to workspace preferences
				setHideFilteredValidationWizardFlag(value);
			}
		}

	}

	/**
	 * Sets the filtered validation flag in the workspace preferences
	 * 
	 * @param project
	 * @param value
	 */
	public static void setHideFilteredValidationWizardFlag(boolean value)
	{
		setWorkspaceBooleanPref(NAMESPACE, KEY_HIDE_FILTERED_VALIDATION_WIZARD, value);

	}
}
