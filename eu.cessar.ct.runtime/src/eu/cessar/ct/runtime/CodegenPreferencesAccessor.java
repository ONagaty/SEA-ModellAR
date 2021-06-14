/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidl6458 Aug 6, 2010 5:16:37 PM </copyright>
 */
package eu.cessar.ct.runtime;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;

import eu.cessar.ct.core.platform.AbstractPreferencesAccessor;
import eu.cessar.ct.core.platform.util.PlatformUtils;
import eu.cessar.req.Requirement;

/**
 * @author uidl6458
 *
 */
public class CodegenPreferencesAccessor extends AbstractPreferencesAccessor
{

	public static final String NAMESPACE = "eu.cessar.ct.runtime"; //$NON-NLS-1$

	public static final String KEY_PROJECT_SPECIFIC = "projectSpecific";//$NON-NLS-1$

	public static final String KEY_OUTPUT_CUSTOM = "gen.output.custom"; //$NON-NLS-1$

	public static final String KEY_OUTPUT_CUSTOM_FOLDER = "gen.output.custom.folder"; //$NON-NLS-1$

	public static final String KEY_DUMP_JET_SOURCE = "jet.dump.source"; //$NON-NLS-1$

	public static final String KEY_DUMP_JET_SOURCE_FOLDER = "jet.dump.source.folder"; //$NON-NLS-1$

	public static final String KEY_STOP_ON_ERROR = "gen.stopOnError"; //$NON-NLS-1$

	private static final String DUMP_JET_FOLDER_DEFAULT = ".src"; //$NON-NLS-1$

	private static final String OUTPUT_FOLDER_DEFAULT = "generated"; //$NON-NLS-1$

	public static final String KEY_RELOAD_EXTERNAL_ARXML = "reload.external.arxml"; //$NON-NLS-1$

	/**
	 * returns if flag is set for project preference (true) or workspace preference (false)
	 *
	 * @param project
	 * @return
	 */
	public static boolean isProjectSpecificSettings(IProject project)
	{
		return getBooleanPref(project, NAMESPACE, KEY_PROJECT_SPECIFIC, false);
	}

	/**
	 * Sets the project specific Flag. If the flag is true, the preferences will be returned from the Project preference
	 * store, if the flag is false the preferences will be returned from the Workspace preference store
	 *
	 * @param project
	 */
	public static void setProjectSpecificSettings(IProject project, boolean value)
	{
		setBooleanPref(project, NAMESPACE, KEY_PROJECT_SPECIFIC, value);
	}

	/**
	 * Return true if a custom output folder is set for the project, false otherwise
	 *
	 * @param project
	 * @return
	 */
	public static boolean isUsingCustomOutputFolder(IProject project)
	{
		return getBooleanPrefWithWSDefault(project, NAMESPACE, KEY_PROJECT_SPECIFIC, KEY_OUTPUT_CUSTOM, false);
	}

	/**
	 * Sets the project flag with 'value'. The flag is a boolean value which determines if the project uses a custom
	 * output folder.
	 *
	 * @param project
	 * @param value
	 */
	public static void setUsingCustomOutputFolderInProject(IProject project, boolean value)
	{
		setBooleanPref(project, NAMESPACE, KEY_OUTPUT_CUSTOM, value);
	}

	/**
	 * Sets the project flag with 'value'. The flag is a boolean value which determines if the Workspace uses a custom
	 * output folder.
	 *
	 * @param value
	 */
	public static void setUsingCustomOutputFolderInWS(boolean value)
	{
		setWorkspaceBooleanPref(NAMESPACE, KEY_OUTPUT_CUSTOM, value);
	}

	/**
	 * Return the path to the custom output folder that shall be used by the project.
	 * <p/>
	 * Note: the path might contain variables. If string substitutions are required the
	 * {@link #getResolvedCustomOutputFolder(IProject)} method shall be used instead.
	 *
	 * @param project
	 * @return
	 */
	public static String getCustomOutputFolder(IProject project)
	{
		return getStringPrefWithWSDefault(project, NAMESPACE, KEY_PROJECT_SPECIFIC, KEY_OUTPUT_CUSTOM_FOLDER,
			OUTPUT_FOLDER_DEFAULT);
	}

	/**
	 * Return the resolved path for the custom output folder. The path is relative to the workspace
	 *
	 * @param project
	 *        the project used to resolve the path
	 * @return a path relative to the workspace
	 * @throws CoreException
	 */
	public static IPath getResolvedCustomOutputFolder(IProject project) throws CoreException
	{
		String folder = getCustomOutputFolder(project);
		return PlatformUtils.resolveVariablePath(project, folder);
	}

	/**
	 * Sets the output folder path
	 *
	 * @param project
	 * @param path
	 */
	public static void setCustomOutputFolderInProject(IProject project, String path)
	{
		setStringPref(project, NAMESPACE, KEY_OUTPUT_CUSTOM_FOLDER, path);
	}

	/**
	 * Sets the output folder path
	 *
	 * @param path
	 */
	public static void setCustomOutputFolderInWS(String path)
	{
		setWorkspaceStringPref(NAMESPACE, KEY_OUTPUT_CUSTOM_FOLDER, path);
	}

	/**
	 * returns true if the flag is set to dump source, else returns false
	 *
	 * @param project
	 * @return
	 */
	public static boolean isDumpingJetSource(IProject project)
	{
		return getBooleanPrefWithWSDefault(project, NAMESPACE, KEY_PROJECT_SPECIFIC, KEY_DUMP_JET_SOURCE, false);
	}

	/**
	 * Sets the project flag with 'value'. The flag is a boolean value which determines if the jet sources(generated
	 * java files) are saved for debugging purposes
	 *
	 * @param project
	 * @param value
	 */
	public static void setDumpJetSourceInProject(IProject project, boolean value)
	{
		setBooleanPref(project, NAMESPACE, KEY_DUMP_JET_SOURCE, value);
	}

	/**
	 * Sets the workspace flag with 'value'. The flag is a boolean value which determines if the jet sources(generated
	 * java files) are saved for debugging purposes
	 *
	 * @param value
	 */
	public static void setDumpJetSourceInWS(boolean value)
	{
		setWorkspaceBooleanPref(NAMESPACE, KEY_DUMP_JET_SOURCE, value);
	}

	/**
	 * Returns the Path where the sources are dumped. It takes into account the project specific or workspace specific
	 * flag.
	 * <p/>
	 * Note: the path might contain variables. If string substitutions are required the
	 * {@link #getResolvedDumpJetSourceFolder(IProject)} method shall be used instead.
	 *
	 * @param project
	 * @return
	 */
	public static String getDumpJetSourceFolder(IProject project)
	{
		return getStringPrefWithWSDefault(project, NAMESPACE, KEY_PROJECT_SPECIFIC, KEY_DUMP_JET_SOURCE_FOLDER,
			DUMP_JET_FOLDER_DEFAULT);
	}

	/**
	 * Return the resolved path for the dump jet source folder
	 *
	 * @param project
	 * @return
	 * @throws CoreException
	 */
	public static IPath getResolvedDumpJetSourceFolder(IProject project) throws CoreException
	{
		String folder = getDumpJetSourceFolder(project);
		return PlatformUtils.resolveVariablePath(project, folder);
	}

	/**
	 * Sets the path were the java files will be dumped
	 *
	 * @param project
	 * @param path
	 */
	public static void setDumpJetSourceFolderInProject(IProject project, String path)
	{
		setStringPref(project, NAMESPACE, KEY_DUMP_JET_SOURCE_FOLDER, path);
	}

	/**
	 * Sets the path were the java files will be dumped
	 *
	 *
	 *
	 * @param path
	 */
	public static void setDumpJetSourceFolderInWS(String path)
	{
		setWorkspaceStringPref(NAMESPACE, KEY_DUMP_JET_SOURCE_FOLDER, path);
	}

	/**
	 * returns true if it stops the code generation on the first error.
	 *
	 * @param project
	 * @return
	 */
	public static boolean isStopingOnError(IProject project)
	{
		return getBooleanPrefWithWSDefault(project, NAMESPACE, KEY_PROJECT_SPECIFIC, KEY_STOP_ON_ERROR, false);
	}

	/**
	 * Sets the project preference flag to stop code generation at first error
	 *
	 * @param project
	 * @param value
	 */
	public static void setStopingOnErrorInProject(IProject project, boolean value)
	{
		setBooleanPref(project, NAMESPACE, KEY_STOP_ON_ERROR, value);
	}

	/**
	 * Sets the workspace preference flag to stop code generation at first error
	 *
	 * @param value
	 */
	public static void setStopingOnErrorInWS(boolean value)
	{
		setWorkspaceBooleanPref(NAMESPACE, KEY_STOP_ON_ERROR, value);
	}

	/**
	 * @param project
	 * @return true if it takes into account the changes made on arxml files outside Cessar-CT
	 */
	@Requirement(
		reqID = "227455")
	public static boolean isExternalChangesOption(IProject project)
	{
		boolean booleanPref = getBooleanPrefWithWSDefault(project, NAMESPACE, KEY_PROJECT_SPECIFIC,
			KEY_RELOAD_EXTERNAL_ARXML, false);
		return booleanPref;
	}

	/**
	 * Sets the project preference flag to take into account the changes made on arxml files outside Cessar-CT
	 *
	 * @param project
	 * @param value
	 */
	public static void setExternalChangesOptionInProject(IProject project, boolean value)
	{
		setBooleanPref(project, NAMESPACE, KEY_RELOAD_EXTERNAL_ARXML, value);
	}

	/**
	 * Sets the workspace preference flag to take into account the changes made on arxml files outside Cessar-CT
	 *
	 * @param value
	 */
	public static void setExternalChangesOptionInWS(boolean value)
	{
		setWorkspaceBooleanPref(NAMESPACE, KEY_RELOAD_EXTERNAL_ARXML, value);
	}

}
