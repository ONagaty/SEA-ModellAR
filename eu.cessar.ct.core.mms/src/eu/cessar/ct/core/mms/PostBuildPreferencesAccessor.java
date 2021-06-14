/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 27.06.2012 14:28:58 </copyright>
 */
package eu.cessar.ct.core.mms;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ProjectScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.osgi.service.prefs.BackingStoreException;

import eu.cessar.ct.core.mms.ecuc.PostBuildContext;
import eu.cessar.ct.core.mms.internal.CessarPluginActivator;
import eu.cessar.ct.core.platform.AbstractPreferencesAccessor;
import eu.cessar.ct.sdk.IPostBuildContext;

/**
 * @author uidl6458
 * 
 */
public class PostBuildPreferencesAccessor extends AbstractPreferencesAccessor
{

	public static final String NAMESPACE = "eu.cessar.ct.mms.postbuild"; //$NON-NLS-1$

	public static final String KEY_PROJECT_SPECIFIC = "projectSpecific"; //$NON-NLS-1$

	public static final String KEY_HAVE_PB_CONTEXT = "enablePBContext"; //$NON-NLS-1$

	public static final String KEY_PREFIX_PBCONTEXT = "pbcontext."; //$NON-NLS-1$

	/**
	 * returns if flag is set for project preference (true) or workspace
	 * preference (false)
	 * 
	 * @param project
	 * @return
	 */
	public static boolean isProjectSpecificSettings(IProject project)
	{
		return getBooleanPref(project, NAMESPACE, KEY_PROJECT_SPECIFIC, false);
	}

	/**
	 * Sets the project specific Flag. If the flag is true, the preferences will
	 * be returned from the Project preference store, if the flag is false the
	 * preferences will be returned from the Workspace preference store
	 * 
	 * @param project
	 */
	public static void setProjectSpecificSettings(IProject project, boolean value)
	{
		setBooleanPref(project, NAMESPACE, KEY_PROJECT_SPECIFIC, value);
	}

	/**
	 * Return true if post build context is enabled for the given project or
	 * false otherwise
	 * 
	 * @param project
	 * @return
	 */
	public static boolean isEnabledPostBuildContext(IProject project)
	{
		return getBooleanPrefWithWSDefault(project, NAMESPACE, KEY_PROJECT_SPECIFIC,
			KEY_HAVE_PB_CONTEXT, false);
	}

	/**
	 * @param project
	 * @param value
	 */
	public static void setEnabledPostBuildContext(IProject project, boolean value)
	{
		setBooleanPref(project, NAMESPACE, KEY_HAVE_PB_CONTEXT, value);
	}

	/**
	 * @param project
	 * @param value
	 */
	public static void setEnabledPostBuildContextInWs(boolean value)
	{
		setWorkspaceBooleanPref(NAMESPACE, KEY_HAVE_PB_CONTEXT, value);
	}

	/**
	 * @param project
	 * @return
	 */
	public static List<IPostBuildContext> getPostBuildContexts(IProject project)
	{
		List<IPostBuildContext> result = new ArrayList<IPostBuildContext>();
		List<String> keys = getAllKeys(project, NAMESPACE);
		for (String key: keys)
		{
			if (key.startsWith(KEY_PREFIX_PBCONTEXT))
			{
				String value = getStringPref(project, NAMESPACE, key, "-1"); //$NON-NLS-1$
				try
				{
					int id = Integer.parseInt(value);
					result.add(new PostBuildContext(key.substring(KEY_PREFIX_PBCONTEXT.length()),
						id, false));
				}
				catch (NumberFormatException ex)
				{
					CessarPluginActivator.getDefault().logError(ex);
					result.add(new PostBuildContext(key.substring(KEY_PREFIX_PBCONTEXT.length()),
						-1, false));
				}
			}
		}
		return result;
	}

	/**
	 * @param project
	 * @param contexts
	 */
	public static void setPostBuildContexts(IProject project, List<IPostBuildContext> contexts)
	{
		List<String> keys = getAllKeys(project, NAMESPACE);
		// first, remove all existing
		for (String key: keys)
		{
			if (key.startsWith(KEY_PREFIX_PBCONTEXT))
			{
				removePreference(project, NAMESPACE, key);
			}

		}

		for (IPostBuildContext context: contexts)
		{
			setStringPref(project, NAMESPACE, KEY_PREFIX_PBCONTEXT + context.getName(),
				String.valueOf(context.getID()));
		}
	}

	public static void setPostBuildConfiguration(IProject project, String id, String value)
	{
		if (project != null)
		{
			setStringPrefNoDuplicates(project, NAMESPACE, KEY_PREFIX_PBCONTEXT + value, id);
		}
		else
		{
			setWorkspaceStringPref(NAMESPACE, KEY_PREFIX_PBCONTEXT + value, id);
		}
	}

	private static void setStringPrefNoDuplicates(IProject project, String namespace, String key,
		String value)
	{
		ProjectScope projectScope = new ProjectScope(project);
		IEclipsePreferences projectPreferences = projectScope.getNode(namespace);
		if (value == null)
		{
			projectPreferences.remove(key);
		}
		else
		{
			try
			{
				// find existing key for the value
				String[] keys = projectPreferences.keys();
				for (int i = 0; i < keys.length; i++)
				{
					if (value.equals(projectPreferences.get(keys[i], "-1")))
					{
						projectPreferences.remove(keys[i]);
					}
				}

				projectPreferences.put(key, value);
			}
			catch (BackingStoreException e)
			{
				CessarPluginActivator.getDefault().logError(e);
			}

		}
		try
		{
			projectPreferences.flush();
		}
		catch (BackingStoreException e)
		{
			CessarPluginActivator.getDefault().logError(e);
		}
	}
}
