/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Nov 19, 2009 3:04:33 PM </copyright>
 */
package eu.cessar.ct.runtime.internal.execution;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.commands.common.EventManager;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.SafeRunner;

import eu.cessar.ct.core.platform.util.SafeRunnable;
import eu.cessar.ct.runtime.CessarRuntime;
import eu.cessar.ct.runtime.execution.ICessarTaskDescriptor;
import eu.cessar.ct.runtime.execution.ICessarTaskManagerImpl;
import eu.cessar.ct.runtime.internal.CessarPluginActivator;
import eu.cessar.ct.sdk.runtime.ExecutionService;
import eu.cessar.ct.sdk.runtime.ICessarTaskListener;
import eu.cessar.ct.sdk.runtime.ICessarTaskManager;

/**
 * The <code>internal</code> implementation of the {@link ExecutionService}
 * 
 */
public final class ExecutionServiceImpl extends EventManager implements ExecutionService.Service
{

	public static final ExecutionServiceImpl eINSTANCE = new ExecutionServiceImpl();

	private ExecutionServiceImpl()
	{
		// singleton class
	}

	private static final String CESSAR_TASK_EXTENSION_ID = CessarPluginActivator.PLUGIN_ID
		+ ".cessarTask"; //$NON-NLS-1$
	private static final String ATTR_ID = "id"; //$NON-NLS-1$

	/**
	 * key: cessarTask ID (like "jet", "pluget"....)<br/>
	 * value: the corresponding descriptor
	 */
	private Map<String, ICessarTaskDescriptor> descriptors;

	/**
	 * Look inside plugin registry for all cessarTask implementations if
	 * necessary
	 */
	private synchronized void checkInit()
	{
		if (descriptors == null)
		{
			// create and populate
			descriptors = new HashMap<String, ICessarTaskDescriptor>();

			IConfigurationElement[] configurationElements = Platform.getExtensionRegistry().getConfigurationElementsFor(
				CESSAR_TASK_EXTENSION_ID);
			for (IConfigurationElement configEl: configurationElements)
			{
				String attribute = configEl.getAttribute(ATTR_ID);
				ICessarTaskDescriptor descriptor = new BasicCessarTaskDescriptor();
				((BasicCessarTaskDescriptor) descriptor).initialize(configEl);
				descriptors.put(attribute, descriptor);
			}
		}
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.sdk.runtime.ICessarTaskManager#addListener(eu.cessar.ct.sdk.runtime.ICessarTaskListener)
	 */
	public void addGlobalListener(ICessarTaskListener listener)
	{
		addListenerObject(listener);
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.sdk.runtime.ICessarTaskManager#removeListener(eu.cessar.ct.sdk.runtime.ICessarTaskListener)
	 */
	public void removeGlobalListener(ICessarTaskListener listener)
	{
		removeListenerObject(listener);
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.sdk.runtime.ExecutionService.Service#createManager(org.eclipse.core.resources.IProject, java.lang.String, java.util.Map)
	 */
	public ICessarTaskManager<?> createManager(IProject project, String type,
		Map<String, Object> map)
	{
		checkInit();

		ICessarTaskManagerImpl<?> runningManager = (ICessarTaskManagerImpl<?>) getRunningManager(project);

		ICessarTaskDescriptor descriptor = descriptors.get(type);
		if (descriptor != null)
		{
			ICessarTaskManagerImpl<?> newManager = descriptor.getTaskManagerFactory().createManager(
				project, map);
			newManager.setUpperManager(runningManager);
			return newManager;
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.sdk.runtime.ExecutionService.Service#getCessarTasksTypes(org.eclipse.core.resources.IProject)
	 */
	public String[] getCessarTasksTypes(IProject project)
	{
		checkInit();

		List<String> taskTypes = new ArrayList<String>();
		Set<String> keySet = descriptors.keySet();

		for (String type: keySet)
		{
			ICessarTaskDescriptor descriptor = descriptors.get(type);
			if (descriptor.getTaskManagerFactory().isValidProject(project))
			{
				taskTypes.add(type);
			}
		}
		return taskTypes.toArray(new String[taskTypes.size()]);
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.sdk.runtime.ExecutionService.Service#getRunningManager(org.eclipse.core.resources.IProject)
	 */
	public ICessarTaskManager<?> getRunningManager(IProject project)
	{
		checkInit();
		return CessarRuntime.getExecutionSupport().getCurrentManager(project);
	}

	/**
	 * Call
	 * {@link ICessarTaskListener#executionEnded(ICessarTaskManager, IStatus)}
	 * method of all attached listeners
	 * 
	 * @param manager
	 * @param status
	 */
	@SuppressWarnings("unchecked")
	public void notifyGlobalExecutionEnded(final ICessarTaskManager manager, final IStatus status)
	{
		Object[] listeners = getListeners();
		for (final Object object: listeners)
		{
			SafeRunner.run(new SafeRunnable()
			{

				public void run() throws Exception
				{
					((ICessarTaskListener) object).executionEnded(manager, status);
				}
			});
		}
	}

	/**
	 * Call the
	 * {@link ICessarTaskListener#executionStarted(ICessarTaskManager, int)}
	 * method of all attached listeners
	 * 
	 * @param manager
	 * @param noOfTasks
	 */
	@SuppressWarnings("unchecked")
	public void notifyGlobalExecutionStarted(final ICessarTaskManager manager, final int noOfTasks)
	{
		Object[] listeners = getListeners();

		for (final Object object: listeners)
		{
			SafeRunner.run(new SafeRunnable()
			{

				public void run() throws Exception
				{
					((ICessarTaskListener) object).executionStarted(manager, noOfTasks);
				}
			});
		}
	}

	/**
	 * Call
	 * {@link ICessarTaskListener#taskEnded(ICessarTaskManager, String, IStatus, Object)}
	 * method of all attached listeners
	 * 
	 * @param manager
	 * @param taskName
	 * @param status
	 * @param result
	 */
	@SuppressWarnings("unchecked")
	public void notifyGlobalTaskEnded(final ICessarTaskManager manager, final Object task,
		final IStatus status, final Object result)
	{
		Object[] listeners = getListeners();
		{
			for (final Object object: listeners)
			{
				SafeRunner.run(new SafeRunnable()
				{

					public void run() throws Exception
					{
						((ICessarTaskListener) object).taskEnded(manager, task, status, result);
					}
				});
			}
		}
	}

	/**
	 * Call {@link ICessarTaskListener#taskStarted(ICessarTaskManager, String)}
	 * method of all attached listeners
	 * 
	 * @param manager
	 * @param taskName
	 */
	@SuppressWarnings("unchecked")
	public void notifyGlobalTaskStarted(final ICessarTaskManager manager, final Object task)
	{
		Object[] listeners = getListeners();
		for (final Object object: listeners)
		{
			SafeRunner.run(new SafeRunnable()
			{

				public void run() throws Exception
				{
					((ICessarTaskListener) object).taskStarted(manager, task);
				}
			});
		}
	}
}
