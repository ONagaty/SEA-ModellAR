/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidt2045 Jun 27, 2011 5:45:44 PM </copyright>
 */
package eu.cessar.ct.core.internal.platform.ui.ant;

import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.preferences.ScopedPreferenceStore;

import com.continental.ibs.swemt.stephandler.extensionPoint.IStepHandlerCommand2;

import eu.cessar.ct.core.internal.platform.ui.CessarPluginActivator;

/**
 * @author uidt2045
 * 
 */
@SuppressWarnings("nls")
public class AntRunnerSHCommand implements IStepHandlerCommand2
{

	// command parameters keywords
	private final static String CMD_KEYWORD_BUILD_FILE = "buildfile";
	private final static String CMD_KEYWORD_REBUILD = "rebuild";
	private final static String CMD_KEYWORD_PROJECT = "project";
	private final static String CMD_KEYWORD_DISABLE_DIALOG = "ShowErrorsDialog";

	/**
	 * this member holds the value of 'rebuild' parameter; default is
	 * <code>false</code>
	 */
	boolean rebuild = false;

	/**
	 * the instance of the project on which ant runner will build the ANT files.
	 */
	IProject theProject = null;

	/** the name of the project received as parameter */
	String projectName = null;

	/** the full path to the build ANT file to be executed */
	String buildFileLoc = null;

	/**
	 * if <code>true</code> a dialog showing ANT build problems will be
	 * displayed; if not specified, the environment value will be used
	 */
	Boolean errorsDialog = null;

	/* (non-Javadoc)
	 * @see com.continental.ibs.swemt.stephandler.extensionPoint.IStepHandlerCommand1#run(java.util.Map, org.eclipse.core.runtime.IProgressMonitor)
	 */
	public IStatus run(Map<String, String> map, IProgressMonitor monitor)
	{
		// read command parameters
		buildFileLoc = map.get(CMD_KEYWORD_BUILD_FILE);
		rebuild = Boolean.parseBoolean(map.get(CMD_KEYWORD_REBUILD));
		projectName = map.get(CMD_KEYWORD_PROJECT);

		if ((buildFileLoc == null) || (projectName == null))
		{
			return new Status(
				IStatus.ERROR,
				CessarPluginActivator.PLUGIN_ID,
				"At least the 'buildfile' and 'project' parameters need to be specified for CESSAR-CT AntRunner Command");
		}
		theProject = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
		errorsDialog = Boolean.valueOf(map.get(CMD_KEYWORD_DISABLE_DIALOG));

		if (!theProject.exists())
		{
			return new Status(IStatus.ERROR, CessarPluginActivator.PLUGIN_ID,
				"The specified project:" + projectName + " does not exist in the workspace:");
		}

		// memorize the environment value for showing ANT Build errors dialog
		IPreferenceStore antPreferences = new ScopedPreferenceStore(new InstanceScope(),
			"org.eclipse.ant.ui");
		boolean initialValue = antPreferences.getBoolean("errorDialog");

		// if a value for showing ANT build errors dialog was received, set it
		// to the environment
		if (errorsDialog != null)
		{
			antPreferences.setValue("errorDialog", errorsDialog.booleanValue());
		}
		try
		{
			// execute AntRunner
			return runAntBuild();
		}
		catch (Exception e)
		{
			return new Status(IStatus.ERROR, CessarPluginActivator.PLUGIN_ID,
				"Error encountered while running AntCommand!", e);
		}
		finally
		{
			if (errorsDialog != null)
			{
				// restore the value for showing ANT Build errors dialog to it's
				// original state
				antPreferences.setValue("errorDialog", initialValue);
			}
		}
	}

	/**
	 * Internal method that create ANT launcher configuration and execute it.
	 */
	private IStatus runAntBuild()
	{
		int i = buildFileLoc.lastIndexOf(projectName);
		String antFileName;
		if (i == -1)
		{
			// consider a relative file
			antFileName = buildFileLoc;
		}
		else
		{
			antFileName = buildFileLoc.substring(i + projectName.length());
		}
		IFile file = theProject.getFile(antFileName);

		// if the ant file does not exist or it isn't in the project root
		if (!file.exists() || file.getParent() != theProject)
		{
			// problems with the ant file
			return new Status(IStatus.ERROR, CessarPluginActivator.PLUGIN_ID, "the ant file "
				+ antFileName + " doesn't exist or is not in the root of the project");
		}

		final IPath buildPath = file.getFullPath();
		final IStatus result[] = new IStatus[1];

		Display.getDefault().syncExec(new Runnable()
		{
			public void run()
			{
				AntLaunchShortcut launcher = new AntLaunchShortcut();
				launcher.setRebuild(rebuild);
				launcher.testSetBackgroundMode(false);

				launcher.launch(buildPath, theProject, "run", null);
				result[0] = launcher.getTerminationStatus();
			}
		});
		return result[0];
	}

}
