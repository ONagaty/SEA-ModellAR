/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidt2045 Feb 10, 2012 3:52:04 PM </copyright>
 */
package eu.cessar.ct.edit.ui.facility.preferences.internal;

import org.eclipse.core.resources.IProject;

import eu.cessar.ct.core.platform.AbstractPreferencesAccessor;
import eu.cessar.ct.edit.ui.internal.CessarPluginActivator;

/**
 * @author uidt2045
 *
 */
/**
 * @author uidt2045
 *
 */
public class EditingPreferencesAccessor extends AbstractPreferencesAccessor
{

	/**
	 * Puts in the given project preference for the given editor ID the given preference with the given value.
	 *
	 * if project is <b>null</b> does nothing and logs an error
	 *
	 * @param project
	 *        should not be null
	 * @param namespace
	 *        should not be null
	 * @param key
	 *        should not be null
	 * @param preference
	 *        should not be null
	 * @param value
	 */
	public static void setKey(IProject project, String namespace, String key, String preference, String value)
	{
		if (project != null && namespace != null && key != null && preference != null)
		{
			setStringPref(project, namespace, key + CessarEditingPreferencesConstants.DELIMITER + preference, value);
		}
		else
		{
			CessarPluginActivator.getDefault().logError(
				new Exception(CessarEditingPreferencesConstants.ERROR_MESSAGE_PROJECT));
		}
	}

	/**
	 * Puts in the workspace preference for the given editor ID the given preference with the given value.
	 *
	 * @param key
	 *        should not be null
	 * @param preference
	 *        should not be null
	 * @param value
	 *
	 */
	public static void setKey(String namespace, String key, String preference, String value)
	{
		if (namespace != null && key != null && preference != null)
		{
			setWorkspaceStringPref(namespace, key + CessarEditingPreferencesConstants.DELIMITER + preference, value);
		}
		else
		{
			CessarPluginActivator.getDefault().logError(
				new Exception(CessarEditingPreferencesConstants.ERROR_MESSAGE_PROJECT));
		}
	}

	/**
	 * @param namespace
	 * @param key
	 *        should not be null
	 * @param preference
	 *        should not be null
	 * @param def
	 * @return def parameter if the given preference is not in the workspace preferences<br>
	 *         or<br>
	 *         the value of the preference <br>
	 *         or<br>
	 *         null if one or all of the parameters are null
	 */
	public static String getKey(String namespace, String key, String preference, String def)
	{
		if (namespace != null && key != null && preference != null)
		{
			return getWSStringPref(namespace, key + CessarEditingPreferencesConstants.DELIMITER + preference, def);
		}
		else
		{
			CessarPluginActivator.getDefault().logError(
				new Exception(CessarEditingPreferencesConstants.ERROR_MESSAGE_PROJECT));
			return null;
		}
	}

	/**
	 *
	 * @param project
	 *        should not be null
	 * @param key
	 *        should not be null
	 * @param preference
	 *        should not be null
	 * @param def
	 * @return def parameter if the given preference is not in the project preferences<br>
	 *         or<br>
	 *         the value of the preference<br>
	 *         or<br>
	 *         null if one or all of the parameters are null
	 */
	public static String getKey(IProject project, String namespace, String key, String preference, String def)
	{
		if (project != null && namespace != null && key != null && preference != null)
		{
			return getStringPref(project, namespace, key + CessarEditingPreferencesConstants.DELIMITER + preference,
				def);
		}
		else
		{
			CessarPluginActivator.getDefault().logError(
				new Exception(CessarEditingPreferencesConstants.ERROR_MESSAGE_PROJECT));
			return null;
		}
	}

	/**
	 * Remove all the keys from the instanceScope(workspace) preference with the given NAMESPACE
	 *
	 * @param namespace
	 */
	public static void removeAllPreferences(String namespace)
	{
		removePreference(namespace);
	}

	/**
	 * Remove all the keys from the project preference with the given NAMESPACE
	 *
	 * @param project
	 * @param namespace
	 */
	public static void removeAllPreferences(IProject project, String namespace)
	{
		removePreference(project, namespace);
	}
}
