package eu.cessar.ct.core.platform.ui.ant;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;

import eu.cessar.ct.core.internal.platform.ui.ant.AntLaunchShortcut;
import eu.cessar.req.Requirement;

/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidg4449<br/>
 * Jan 9, 2014 11:20:54 AM
 * 
 * </copyright>
 */

/**
 * Auxiliary class to call ant files from code
 * 
 * @author uidg4449
 * 
 *         %created_by: uidg4449 %
 * 
 *         %date_created: Tue Jan 21 18:50:56 2014 %
 * 
 *         %version: 2 %
 */
@Requirement(
	reqID = "REQ_RBUI_CUSTOM#TASK#6")
public final class AntRunner
{
	/**
	 * Private constructor
	 */
	private AntRunner()
	{
	}

	/**
	 * Executes a cessar-ct ant task
	 * 
	 * @param file
	 * 
	 * @param mode
	 *        the mode in which the build file should be executed, if null it will be called with run
	 * @param targetAttribute
	 *        Ant call target, if null default will be called
	 * @return status
	 */
	public static IStatus runAnt(IFile file, String mode, String targetAttribute)
	{

		IPath antPath = file.getFullPath();
		IProject project = file.getProject();

		AntLaunchShortcut launcher = new AntLaunchShortcut();
		launcher.testSetBackgroundMode(true);
		if (mode != null)
		{
			launcher.launch(antPath, project, mode, targetAttribute);

		}
		else
		{
			launcher.launch(antPath, project, "run", targetAttribute); //$NON-NLS-1$
		}
		IStatus result = launcher.getTerminationStatus();
		return result;
	}
}
