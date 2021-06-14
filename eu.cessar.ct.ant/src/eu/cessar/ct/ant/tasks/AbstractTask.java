/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870 Jun 29, 2010 11:01:25 AM </copyright>
 */
package eu.cessar.ct.ant.tasks;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.eclipse.core.resources.IProject;

import eu.cessar.ct.ant.tasks.util.AntUtils;
import eu.cessar.ct.core.platform.CESSARPreferencesAccessor;
import eu.cessar.ct.core.platform.ECompatibilityMode;

/**
 * Base implementation for Ant Tasks that may require projectPath or projectName
 * attribute to be set.
 * 
 */
public abstract class AbstractTask extends Task
{
	protected String projectPath;

	protected String projectname;

	private static final String CESSAR_PROJECT_NAME = "CESSAR_PROJECT_NAME"; //$NON-NLS-1$

	private static final String PROJECT_NAME = "project.name"; //$NON-NLS-1$

	protected IProject iProject;

	protected enum ETaskType
	{
		UNIVERSAL_TASK("Universal"), COMPAT_TASK("Compat_Task"), NON_COMPAT_TASK("NonCompat_Task"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		private String type;

		/**
		 * 
		 */
		private ETaskType(String type)
		{
			this.type = type;
		}

		/**
		 * @return the type
		 */
		public String getType()
		{
			return type;
		}

	}

	/**
	 * Returns whether the current task is a dedicated one for projects in
	 * compatibility mode(mosart.<name_of_task).
	 * 
	 * @return
	 */
	protected abstract ETaskType getTaskCompatType();

	/**
	 * Check validity of arguments
	 */
	protected void checkArgs()
	{
		if (projectPath != null && projectname != null)
		{
			throw new BuildException("projectname and projectpath cannot be both set"); //$NON-NLS-1$
		}
	}

	/**
	 * Process given arguments
	 */
	protected void processArgs()
	{
		// check if projectname is set
		if (projectname != null)
		{
			iProject = AntUtils.getProjectByName(projectname);
		}
		else if (projectPath != null)
		{
			iProject = AntUtils.getProject(projectPath);
		}

		else
		{
			Project antProject = getProject();
			if (antProject != null)
			{
				String projNameProperty = antProject.getUserProperty(CESSAR_PROJECT_NAME);
				if (projNameProperty == null)
				{
					projNameProperty = antProject.getUserProperty(PROJECT_NAME);
				}

				if (projNameProperty == null)
				{
					projNameProperty = antProject.getUserProperty("project.dir"); //$NON-NLS-1$
				}

				if (projNameProperty != null)
				{
					iProject = AntUtils.getProjectByName(projNameProperty);
				}
			}
		}

		// check if project is in compatibility mode

		if (iProject != null)
		{
			ECompatibilityMode compatibilityMode = CESSARPreferencesAccessor.getCompatibilityMode(iProject);
			if (getTaskCompatType() == ETaskType.COMPAT_TASK
				&& compatibilityMode == ECompatibilityMode.NONE)
			{
				throw new BuildException(
					"Project " + iProject.getName() + " is not in compatibility mode! Please use the task cessar.<name_of_the_task>, instead of " + getTaskName()); //$NON-NLS-1$ //$NON-NLS-2$
			}
			else if (getTaskCompatType() == ETaskType.NON_COMPAT_TASK
				&& compatibilityMode != ECompatibilityMode.NONE)
			{
				throw new BuildException(
					"Project " + iProject.getName() + " is in compatibility mode! Please use the task mosart.<name_of_the_task>, instead of " + getTaskName()); //$NON-NLS-1$ //$NON-NLS-2$
			}
		}

	}

	/**
	 * @return Returns the projectPath.
	 */
	public String getProjectPath()
	{
		return projectPath;
	}

	/**
	 * @param projectPath
	 *        The projectPath to set.
	 */
	public void setProjectPath(final String projectPath)
	{
		this.projectPath = projectPath;
	}

	/**
	 * 
	 * @return
	 */
	public String getProjectname()
	{
		return projectname;
	}

	/**
	 * 
	 * @param name
	 */
	public void setProjectname(final String name)
	{
		this.projectname = name;
	}

}
