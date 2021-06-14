/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidu0944 Jan 30, 2012 2:02:46 PM </copyright>
 */
package eu.cessar.ct.runtime.utils;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ProjectScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.osgi.service.prefs.BackingStoreException;

import eu.cessar.ct.core.platform.AbstractPreferencesAccessor;

/**
 * @author uidu0944
 * 
 */
public class RequiredPluginsAccessor extends AbstractPreferencesAccessor
{

	public static final String NAMESPACE = "eu.cessar.ct.runtime.bundles"; //$NON-NLS-1$

	public static final String KEY_PROJECT_SPECIFIC = "projectSpecific";//$NON-NLS-1$

	public static String[] getPluginList(IProject project)
	{
		ProjectScope projectScope = new ProjectScope(project);
		List<String> prefs = new ArrayList<String>();
		IEclipsePreferences projectPreferences = projectScope.getNode(NAMESPACE);
		try
		{
			String[] keys = projectPreferences.keys();
			if (keys != null && keys.length > 0)
			{
				for (String string: keys)
				{
					if ("1".equals(projectPreferences.get(string, "-1"))) //$NON-NLS-1$ //$NON-NLS-2$
					{
						prefs.add(string);
					}
				}
			}
		}
		catch (BackingStoreException e)
		{
			e.printStackTrace();
		}
		return prefs.toArray(new String[0]);
	}

	/**
	 * Sets the bundle
	 * 
	 * @param path
	 */
	public static void setBundle(IProject project, String bundleId, String value)
	{
		setStringPref(project, NAMESPACE, bundleId, value);
	}

}
