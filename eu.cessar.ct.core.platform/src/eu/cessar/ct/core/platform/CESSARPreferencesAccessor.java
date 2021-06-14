/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidl6458 Aug 5, 2010 3:39:03 PM </copyright>
 */
package eu.cessar.ct.core.platform;

import org.artop.aal.common.metamodel.AutosarMetaModelVersionData;
import org.eclipse.core.resources.IProject;

/**
 * @author uidl6458
 *
 * @Review uidl6458 - 12.04.2012
 */
public class CESSARPreferencesAccessor extends AbstractPreferencesAccessor
{

	/**
	 * Namespace used by accessor
	 */
	public static final String NAMESPACE = "eu.cessar.ct.core.platform"; //$NON-NLS-1$

	/**
	 *
	 */
	public static final String EDITING_NAMESPACE = "eu.cessar.ct.edit.ui"; //$NON-NLS-1$

	/**
	 * Key used in the case the options are project specific
	 */
	public static final String KEY_PROJECT_SPECIFIC = "projectSpecific";//$NON-NLS-1$

	/**
	 * Key used for the enablement of the Filter on key pressed option
	 */
	public static final String KEY_ENABLEMENT_OF_FILTER_LIMIT_VALUE = "EnablementOfFilterOnKeyPressed"; //$NON-NLS-1$

	/**
	 * Key used for the value of limit when the Filter on key pressed option is disabled
	 */
	public static final String KEY_FILTER_LIMIT_VALUE = "FilterOnKeyPressedLimitValue"; //$NON-NLS-1$

	/**
	 * Key to store the configuration variant inside the preference
	 */
	public static final String KEY_CONFIGURATION_VARIANT = "configuration.variant"; //$NON-NLS-1$

	/**
	 * Return the compatibility mode of the project. For metamodel 3.1.5 {@link ECompatibilityMode#FULL} will returned
	 * for anything else {@link ECompatibilityMode#NONE} will be returned, never null
	 *
	 * @param project
	 * @return the compatibility mode
	 */
	public static ECompatibilityMode getCompatibilityMode(IProject project)
	{
		AutosarMetaModelVersionData data = getAutosarVersionData(project);
		if (data != null && data.getMajor() == 3 && data.getMinor() == 1)
		{
			return ECompatibilityMode.FULL;
		}
		else
		{
			return ECompatibilityMode.NONE;
		}
	}

	/**
	 * @param project
	 * @return project variant:DEVELOPMENT or PRODUCTION. If the project is null, return DEVELOPMENT
	 */
	public static EProjectVariant getProjectVariant(IProject project)
	{
		// return getEnumPref(project, NAMESPACE, KEY_CONFIGURATION_VARIANT, EProjectVariant.class,
		// EProjectVariant.OTHER);
		if (project == null)
		{
			return EProjectVariant.DEVELOPMENT;
		}
		String stringPref = getStringPref(project, NAMESPACE, KEY_CONFIGURATION_VARIANT,
			EProjectVariant.DEVELOPMENT.toString());
		EProjectVariant projectVariant = EProjectVariant.getProjectVariant(stringPref);
		return projectVariant;
	}

	/**
	 * @param project
	 * @param variant
	 */
	public static void setProjectVariant(IProject project, EProjectVariant variant)
	{
		setEnumPref(project, NAMESPACE, KEY_CONFIGURATION_VARIANT, variant);
	}

	/**
	 * Gets the project specific preference for the Enablement of FilterOnKeyPressed
	 *
	 * @param project
	 *        - the project on which the preference exists
	 * @return - the state of FilterOnKeyPressed
	 */
	public static boolean getEnablementOfFilterOnKeyPressed(IProject project)
	{
		return getBooleanPrefWithWSDefault(project, EDITING_NAMESPACE, KEY_PROJECT_SPECIFIC,
			KEY_ENABLEMENT_OF_FILTER_LIMIT_VALUE, false);
	}

	/**
	 * Gets the project specific preference for the FilterOnKeyPressedLimitValue
	 *
	 * @param project
	 *        - the project on which the preference exists
	 * @return - the value at which the FilterOnKeyPressed will be disabled
	 */
	public static String getFilterOnKeyPressedLimitValue(IProject project)
	{
		return getStringPrefWithWSDefault(project, EDITING_NAMESPACE, KEY_PROJECT_SPECIFIC, KEY_FILTER_LIMIT_VALUE,
			"30"); //$NON-NLS-1$
	}

	/**
	 * @param project
	 *        - the project on which the preference is set
	 * @param value
	 *        - the state of FilterOnKeyPressed
	 */
	public static void setEnablementOfFilterOnKeyPressedInProject(IProject project, boolean value)
	{
		setBooleanPref(project, EDITING_NAMESPACE, KEY_ENABLEMENT_OF_FILTER_LIMIT_VALUE, value);

	}

	/**
	 * @param project
	 *        - the project on which the preference is set
	 * @param filterLimitValue
	 *        - the value at which the FilterOnKeyPressed will be disabled
	 */
	public static void setFilterOnKeyPressedLimitValueInProject(IProject project, String filterLimitValue)
	{
		setStringPref(project, EDITING_NAMESPACE, KEY_FILTER_LIMIT_VALUE, filterLimitValue);
	}

	/**
	 * @param selection
	 *        - the state of FilterOnKeyPressed
	 */
	public static void setEnablementOfFilterOnKeyPressedInWs(boolean selection)
	{
		setWorkspaceBooleanPref(EDITING_NAMESPACE, KEY_ENABLEMENT_OF_FILTER_LIMIT_VALUE, selection);

	}

	/**
	 * @param filterLimitValue
	 *        - the value at which the FilterOnKeyPressed will be disabled
	 */
	public static void setFilterOnKeyPressedLimitValueInWs(String filterLimitValue)
	{
		setWorkspaceStringPref(EDITING_NAMESPACE, KEY_FILTER_LIMIT_VALUE, filterLimitValue);

	}

	/**
	 * Sets the project specific Flag. If the flag is true, the preferences will be returned from the Project preference
	 * store, if the flag is false the preferences will be returned from the workspace preference store
	 *
	 * @param project
	 * @param value
	 */
	public static void setProjectSpecific(IProject project, boolean value)
	{
		setBooleanPref(project, EDITING_NAMESPACE, KEY_PROJECT_SPECIFIC, value);
	}
}
