/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidl6458 Nov 19, 2009 2:19:50 PM </copyright>
 */
package eu.cessar.ct.sdk.runtime;

import java.util.Map;

import org.eclipse.core.resources.IProject;

import eu.cessar.ct.core.platform.util.PlatformUtils;
import eu.cessar.ct.sdk.IPluggable;

/**
 * This is the entry point into the execution services of Cessar. Using the facilities of this class, someone can:<br/>
 * <ul>
 * <li>find out what kind of use code a particular project support execution</li>
 * <li>get a {@link ICessarTaskManager} capable to execute that code</li>
 * </ul>
 *
 *
 */
public final class ExecutionService
{

	/**
	 * The jet task identifiable. When creating such a task manager use IFile as the type of input:
	 *
	 * <pre>
	 * ICessarTaskManager&lt;IFile&gt; manager = (ICessarTaskManager&lt;IFile&gt;)ExecutionService.createManager(project, &quot;jet);
	 * </pre>
	 */
	public static final String TASK_TYPE_JET = "jet"; //$NON-NLS-1$

	/**
	 * The pluget task manager. Accepts IFile as inputs
	 */
	public static final String TASK_TYPE_PLUGET = "pluget"; //$NON-NLS-1$

	/**
	 * Another pluget task manager but this one shall be used for not deployed tasks. It handles class names as inputs,
	 * for example: mypack.MyPluget
	 */
	public static final String TASK_TYPE_PLUGET_CLASS = "pluget.class"; //$NON-NLS-1$

	/**
	 * Pluget task manager, this on is usesd for external plugets, it handle URL to the pluget
	 */
	public static final String TASK_TYPE_PLUGET_EXTERNAL = "pluget.external"; //$NON-NLS-1$

	/**
	 * A task manager capable to execute "public static void main(String[])" method of a class. It handles class names
	 * as input.
	 */
	public static final String TASK_TYPE_JAVA_MAIN_CLASS = "java.main.class"; //$NON-NLS-1$

	/**
	 * Task manager for executing non deployed form pages. Accepts IFile as input which must point to the main .xwt file
	 */
	public static final String TASK_TYPE_XWT = "xwt"; //$NON-NLS-1$

	/**
	 * Task manager for executing FlatMap generator.
	 */
	public static final String TASK_TYPE_FLAT_MAP = "flat.map"; //$NON-NLS-1$

	/**
	 * Key from the map that is passed to the the ExecusionService when requesting a manager for executing forms. The
	 * value must be a Composite
	 *
	 */
	public static final String XWT_KEY_PARENT = "parent"; //$NON-NLS-1$

	private static final Service SERVICE = PlatformUtils.getService(Service.class);

	private ExecutionService()
	{
		// avoid instance
	}

	/**
	 *
	 * NOT PUBLIC API
	 *
	 */
	public static interface Service
	{

		/**
		 * @param project
		 * @return String[]
		 */
		public String[] getCessarTasksTypes(IProject project);

		/**
		 * @param project
		 * @param type
		 * @param map
		 * @return ICessarTaskManager<?>
		 */
		public ICessarTaskManager<?> createManager(IProject project, String type, Map<String, Object> map);

		/**
		 * @param project
		 * @return ICessarTaskManager<?>
		 */
		public ICessarTaskManager<?> getRunningManager(IProject project);

	}

	/**
	 * Return the types of Cessar tasks that are available for a particular project
	 *
	 * @param project
	 * @return String[]
	 */
	public static String[] getCessarTasksTypes(IProject project)
	{
		return SERVICE.getCessarTasksTypes(project);
	}

	/**
	 * Create an ICessarTaskManager of a specific type for a specific project.
	 *
	 * @param project
	 *        a valid project
	 * @param type
	 *        one of the allowed type
	 *
	 * @return a suitable manager or <code>null</code> if no such manager can be located
	 */
	public static ICessarTaskManager<?> createManager(IProject project, String type)
	{
		return SERVICE.createManager(project, type, null);
	}

	/**
	 * Create an ICessarTaskManager of a specific type for a specific project.
	 *
	 * @param project
	 *        a valid project
	 * @param type
	 *        one of the allowed type
	 * @param map
	 *        map holding data which is of interest for the manager
	 *
	 * @return a suitable manager or <code>null</code> if no such manager can be located
	 */
	public static ICessarTaskManager<?> createManager(IProject project, String type, Map<String, Object> map)
	{
		return SERVICE.createManager(project, type, map);
	}

	/**
	 * Return true if there is currently a task manager that run tasks, false otherwise
	 *
	 * @param project
	 *
	 * @return boolean
	 */
	public static boolean isRunning(IProject project)
	{
		return SERVICE.getRunningManager(project) != null;
	}

	/**
	 * Return the current running manager. This method shall be executed only within a {@link IPluggable}
	 *
	 * @param project
	 * @return the current manager for the project or null if none
	 */
	public static ICessarTaskManager<?> getRunningManager(IProject project)
	{
		return SERVICE.getRunningManager(project);
	}
}
