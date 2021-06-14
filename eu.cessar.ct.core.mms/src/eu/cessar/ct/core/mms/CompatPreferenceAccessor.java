/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidt2045 Nov 25, 2010 9:59:22 AM </copyright>
 */
package eu.cessar.ct.core.mms;

import org.eclipse.core.resources.IProject;

import eu.cessar.ct.core.platform.AbstractPreferencesAccessor;

/**
 * Provides access to the compatibility related preferences. The preferences are
 * located here instead of somewhere in the compat plugins because of the plugin
 * interdependencies.
 * 
 * @Review uidl6458 - 29.03.2012
 * 
 */
public class CompatPreferenceAccessor extends AbstractPreferencesAccessor
{

	public static final String NAMESPACE = "eu.cessar.ct.core.mms"; //$NON-NLS-1$
	public static final String KEY_PROJECT_SPECIFIC = "projectSpecific";//$NON-NLS-1$

	public static final String KEY_POST_BUILD = "post.build.changeable"; //$NON-NLS-1$
	public static final String KEY_MULTI_CONF_CONTAINER = "multiple.configuration.container"; //$NON-NLS-1$
	public static final String KEY_USE_REFINEMENT = "uses.refinement"; //$NON-NLS-1$

	/**
	 * Returns true if the projects useRefinementConcept flag is set. <br>
	 * Returns false otherwise.
	 * 
	 * @param project
	 * @return
	 */
	public static boolean isUseRefinement(IProject project)
	{
		return getBooleanPrefWithWSDefault(project, NAMESPACE, KEY_PROJECT_SPECIFIC,
			KEY_USE_REFINEMENT, false);
	}

	/**
	 * Sets the projects UseRefinementConcept flag to value
	 * 
	 * @param project
	 * @param value
	 */
	public static void setUseRefinementInProject(IProject project, Boolean value)
	{
		setBooleanPref(project, NAMESPACE, KEY_USE_REFINEMENT, value);
	}

	/**
	 * Sets the workspaces UseRefinementConcept flag to value
	 * 
	 * @param value
	 */
	public static void setUseRefinementInWS(Boolean value)
	{
		setWorkspaceBooleanPref(NAMESPACE, KEY_USE_REFINEMENT, value);
	}

	/**
	 * Returns true if the projects postBuilbChangeable flag is set. <br>
	 * Returns false otherwise.
	 * 
	 * @param project
	 * @return
	 */
	public static boolean isPostBuild(IProject project)
	{
		return getBooleanPrefWithWSDefault(project, NAMESPACE, KEY_PROJECT_SPECIFIC,
			KEY_POST_BUILD, true);
	}

	/**
	 * Sets the projects PostBuildChangeable flag to value
	 * 
	 * @param project
	 * @param value
	 */
	public static void setPostBuildInProject(IProject project, Boolean value)
	{
		setBooleanPref(project, NAMESPACE, KEY_POST_BUILD, value);
	}

	/**
	 * Sets the workspaces PostBuildChangeable flag to value
	 * 
	 * @param value
	 */
	public static void setPostBuildInWS(Boolean value)
	{
		setWorkspaceBooleanPref(NAMESPACE, KEY_POST_BUILD, value);
	}

	/**
	 * Returns the value of the multiConfigurationContainer flag of the
	 * specified project
	 * 
	 * @param project
	 * @return
	 */
	public static boolean isMultiConfContainter(IProject project)
	{
		return getBooleanPrefWithWSDefault(project, NAMESPACE, KEY_PROJECT_SPECIFIC,
			KEY_MULTI_CONF_CONTAINER, false);
	}

	/**
	 * Set the MultiConfigurationContainer to value for the specified project
	 * 
	 * @param project
	 * @return
	 */
	public static void setMultiConfContainerInProject(IProject project, Boolean value)
	{
		setBooleanPref(project, NAMESPACE, KEY_MULTI_CONF_CONTAINER, value);
	}

	/**
	 * Set the MultiConfigurationContainer to value for the workspace
	 * 
	 * @param project
	 * @return
	 */
	public static void setMultiConfContainerInWS(Boolean value)
	{
		setWorkspaceBooleanPref(NAMESPACE, KEY_MULTI_CONF_CONTAINER, value);
	}

	/**
	 * returns if flag is set for project preference (true) or workspace
	 * preference (false)
	 * 
	 * @param project
	 * @return
	 */
	public static boolean isProjectSpecificSettings(IProject project)
	{
		return getBooleanPref(project, NAMESPACE, KEY_PROJECT_SPECIFIC, true);
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

}
