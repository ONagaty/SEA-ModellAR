/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidl6458 Aug 5, 2010 5:13:05 PM </copyright>
 */
package eu.cessar.ct.core.platform;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.artop.aal.common.metamodel.AutosarMetaModelVersionData;
import org.artop.aal.common.metamodel.AutosarReleaseDescriptor;
import org.artop.aal.workspace.preferences.IAutosarWorkspacePreferences;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ProjectScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.osgi.service.prefs.BackingStoreException;

import eu.cessar.ct.core.internal.platform.CessarPluginActivator;

/**
 * Abstract class that shall be extended by all preference accessors
 *
 * @author uidt2045
 *
 * @Review uidl6458 - 12.04.2012
 */
public abstract class AbstractPreferencesAccessor
{

	/**
	 *
	 * @param prefs
	 */
	private static void flushPreferences(IEclipsePreferences prefs)
	{
		try
		{
			prefs.flush();
		}
		catch (BackingStoreException e)
		{
			CessarPluginActivator.getDefault().logError(e);
		}
	}

	/**
	 * @param project
	 * @return
	 */
	private static AutosarReleaseDescriptor getProjectRelease(IProject project)
	{
		return IAutosarWorkspacePreferences.AUTOSAR_RELEASE.get(project);
	}

	/**
	 * Return the Autosar release of a particular project
	 *
	 * @param project
	 * @return
	 */
	protected static AutosarMetaModelVersionData getAutosarVersionData(IProject project)
	{
		AutosarReleaseDescriptor release = getProjectRelease(project);
		if (release != null)
		{
			return release.getAutosarVersionData();
		}
		else
		{
			return null;
		}
	}

	/**
	 * returns the String value of the preference 'key' from the project preferences
	 *
	 * @param project
	 * @param namespace
	 * @param key
	 * @return
	 */
	protected static String getStringPref(IProject project, String namespace, String key, String def)
	{
		ProjectScope projectScope = new ProjectScope(project);
		IEclipsePreferences projectPreferences = projectScope.getNode(namespace);
		return projectPreferences.get(key, def);
	}

	/**
	 * @param project
	 * @param namespace
	 * @param enablementKey
	 * @param preferenceKey
	 * @param def
	 * @return
	 */
	protected static String getStringPrefWithWSDefault(IProject project, String namespace, String enablementKey,
		String preferenceKey, String def)
	{
		boolean enabled = false;
		IEclipsePreferences projectPreferences = null;
		if (project != null)
		{
			ProjectScope projectScope = new ProjectScope(project);
			projectPreferences = projectScope.getNode(namespace);
			enabled = projectPreferences.getBoolean(enablementKey, false);
		}

		if (enabled)
		{
			return projectPreferences.get(preferenceKey, def);
		}
		else
		{
			IEclipsePreferences preference = InstanceScope.INSTANCE.getNode(namespace);
			return preference.get(preferenceKey, def); // workspaceSeting
		}
	}

	/**
	 * @param namespace
	 * @param preferenceKey
	 * @param def
	 * @return
	 */
	protected static String getWSStringPref(String namespace, String preferenceKey, String def)
	{
		IEclipsePreferences preference = InstanceScope.INSTANCE.getNode(namespace);
		return preference.get(preferenceKey, def); // workspaceSeting
	}

	/**
	 * return the boolean value of the preference 'key' from the project preferences
	 *
	 * @param project
	 * @param namespace
	 * @param key
	 * @param def
	 * @return
	 */
	protected static boolean getBooleanPref(IProject project, String namespace, String key, boolean def)
	{
		ProjectScope projectScope = new ProjectScope(project);
		IEclipsePreferences projectPreferences = projectScope.getNode(namespace);
		return projectPreferences.getBoolean(key, def);
	}

	/**
	 * TODO: returns the boolean value of the preference 'preferenceKey'. If enablementKey is true it returns it from
	 * the project preferences, else it returns it from the workspace preferences
	 *
	 * @param project
	 * @param namespace
	 * @param enablementKey
	 * @param preferenceKey
	 * @param def
	 * @return
	 */
	protected static boolean getBooleanPrefWithWSDefault(IProject project, String namespace, String enablementKey,
		String preferenceKey, boolean def)
	{
		boolean enabled = false;
		IEclipsePreferences projectPreferences = null;

		if (project != null)
		{
			ProjectScope projectScope = new ProjectScope(project);
			projectPreferences = projectScope.getNode(namespace);
			enabled = projectPreferences.getBoolean(enablementKey, false);
		}
		if (enabled)
		{
			return projectPreferences.getBoolean(preferenceKey, def);
		}
		else
		{
			IEclipsePreferences preference = InstanceScope.INSTANCE.getNode(namespace);
			return preference.getBoolean(preferenceKey, def); // workspaceSeting
		}
	}

	/**
	 * sets the preference 'key' with the String 'value' in the project preferences. If the value is null it removes the
	 * preference from the project
	 *
	 * @param project
	 * @param namespace
	 * @param key
	 * @param value
	 */
	protected static void setStringPref(IProject project, String namespace, String key, String value)
	{
		ProjectScope projectScope = new ProjectScope(project);
		IEclipsePreferences projectPreferences = projectScope.getNode(namespace);
		if (value == null)
		{
			projectPreferences.remove(key);
		}
		else
		{
			projectPreferences.put(key, value);
		}
		flushPreferences(projectPreferences);
	}

	/**
	 * sets the preference 'key' with the String 'value' in the workspace preferences
	 *
	 * @param namespace
	 * @param key
	 * @param value
	 */
	protected static void setWorkspaceStringPref(String namespace, String key, String value)
	{
		IEclipsePreferences pref = InstanceScope.INSTANCE.getNode(namespace);
		if (value == null)
		{
			pref.remove(key);
		}
		else
		{
			pref.put(key, value);
		}
		flushPreferences(pref);
	}

	/**
	 * Sets the preference 'key' in the project preferences with the boolean value. If the boolean value is null it
	 * removes the preference from the project preferences
	 *
	 * @param project
	 * @param namespace
	 * @param key
	 * @param value
	 */
	protected static void setBooleanPref(IProject project, String namespace, String key, Boolean value)
	{

		ProjectScope projectScope = new ProjectScope(project);
		IEclipsePreferences projectPreferences = projectScope.getNode(namespace);

		if (value == null)
		{
			projectPreferences.remove(key);
		}
		else
		{
			projectPreferences.putBoolean(key, value);
		}
		flushPreferences(projectPreferences);
	}

	/**
	 * sets the preference 'key' in the workspace preference
	 *
	 * @param Namespace
	 * @param key
	 * @param value
	 */
	protected static void setWorkspaceBooleanPref(String Namespace, String key, boolean value)
	{
		IEclipsePreferences instancePreferences = InstanceScope.INSTANCE.getNode(Namespace);
		instancePreferences.putBoolean(key, value);
		flushPreferences(instancePreferences);
	}

	/**
	 * gets the preference 'key' from the workspace preference
	 *
	 * @param Namespace
	 * @param key
	 * @param value
	 */
	protected static boolean getWorkspaceBooleanPref(String Namespace, String key)
	{
		IEclipsePreferences instancePreferences = InstanceScope.INSTANCE.getNode(Namespace);
		boolean result = instancePreferences.getBoolean(key, false);
		flushPreferences(instancePreferences);
		return result;
	}

	/**
	 * Removes the preference file from the given project for the given Namespace
	 *
	 * @param Namespace
	 */
	protected static void removePreference(IProject project, String Namespace)
	{
		ProjectScope projectScope = new ProjectScope(project);
		IEclipsePreferences projectPreferences = projectScope.getNode(Namespace);
		try
		{
			projectPreferences.clear();
			flushPreferences(projectPreferences);
		}
		catch (BackingStoreException e)
		{
			CessarPluginActivator.getDefault().logError(e);
		}
	}

	/**
	 * @param project
	 * @param Namespace
	 * @param key
	 */
	protected static void removePreference(IProject project, String Namespace, String key)
	{
		ProjectScope projectScope = new ProjectScope(project);
		IEclipsePreferences projectPreferences = projectScope.getNode(Namespace);
		projectPreferences.remove(key);
		flushPreferences(projectPreferences);
	}

	/**
	 * Removes the preference file for the given Namespace
	 *
	 * @param Namespace
	 */
	protected static void removePreference(String Namespace)
	{
		IEclipsePreferences instancePreferences = InstanceScope.INSTANCE.getNode(Namespace);
		try
		{
			instancePreferences.clear();
			flushPreferences(instancePreferences);
		}
		catch (BackingStoreException e)
		{
			CessarPluginActivator.getDefault().logError(e);
		}
	}

	/**
	 * @param <E>
	 * @param project
	 * @param namespace
	 * @param key
	 * @param eClass
	 * @return
	 */
	protected static <E extends Enum<E>> E getEnumPref(IProject project, String namespace, String key, Class<E> eClass,
		E def)
	{
		String value = getStringPref(project, namespace, key, def == null ? null : def.name());
		if (value == null)
		{
			return def;
		}
		else
		{
			try
			{
				return Enum.valueOf(eClass, value);
			}
			catch (IllegalArgumentException ex)
			{
				return def;
			}
		}
	}

	/**
	 * @param <E>
	 * @param project
	 * @param namespace
	 * @param key
	 * @param value
	 */
	protected static <E extends Enum<E>> void setEnumPref(IProject project, String namespace, String key, E value)
	{
		if (value == null)
		{
			setStringPref(project, namespace, key, null);
		}
		else
		{
			setStringPref(project, namespace, key, value.name());
		}

	}

	/**
	 * @param NAMESPACE
	 * @return returns the entire list of keys from the given NAMESPACE, or an empty list if the aren't any
	 */
	public static List<String> getAllKeys(String NAMESPACE)
	{
		List<String> result = new ArrayList<String>();
		IEclipsePreferences preference = InstanceScope.INSTANCE.getNode(NAMESPACE);
		try
		{
			String[] keys = preference.keys();
			if (keys.length > 0)
			{
				result.addAll(Arrays.asList(keys));
			}
		}
		catch (BackingStoreException e)
		{
			CessarPluginActivator.getDefault().logError(e);
		}
		return result;
	}

	/**
	 * @param project
	 * @param NAMESPACE
	 * @return returns the entire list of keys from the given NAMESPACE from the given project, or an empty list if the
	 *         aren't any
	 */
	public static List<String> getAllKeys(IProject project, String NAMESPACE)
	{
		List<String> result = new ArrayList<String>();
		ProjectScope projectScope = new ProjectScope(project);
		IEclipsePreferences projectPreferences = projectScope.getNode(NAMESPACE);
		try
		{
			String[] keys = projectPreferences.keys();
			if (keys.length > 0)
			{
				result.addAll(Arrays.asList(keys));
			}
		}
		catch (BackingStoreException e)
		{
			CessarPluginActivator.getDefault().logError(e);
		}
		return result;
	}

}
