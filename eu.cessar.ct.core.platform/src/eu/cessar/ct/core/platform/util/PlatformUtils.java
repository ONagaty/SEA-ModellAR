/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidl6458 Jan 26, 2010 1:46:38 PM </copyright>
 */
package eu.cessar.ct.core.platform.util;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ProjectScope;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.resources.WorkspaceJob;
import org.eclipse.core.runtime.*;
import org.eclipse.core.runtime.jobs.IJobManager;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.variables.VariablesPlugin;
import org.eclipse.sphinx.emf.workspace.loading.ModelLoadManager;
import org.eclipse.sphinx.platform.IExtendedPlatformConstants;
import org.eclipse.sphinx.platform.util.StatusUtil;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleException;
import org.osgi.service.prefs.BackingStoreException;

import eu.cessar.ct.core.internal.platform.CessarPluginActivator;
import eu.cessar.ct.core.platform.CESSARPreferencesAccessor;
import eu.cessar.ct.core.platform.PlatformConstants;

/**
 * Various project related utilities
 *
 * @Review uidl6458 - 18.04.2012
 *
 */
public class PlatformUtils
{

	private static boolean artopWorkspaceInitialized = false;

	private static final String EXT_ID_SDK_SERVICE = "sdkService"; //$NON-NLS-1$

	private static final String ATTR_SERVICE_ID = "serviceID"; //$NON-NLS-1$

	private static final String ATTR_PROVIDER = "provider"; //$NON-NLS-1$

	private static Map<String, ExtensionClassWrapper<IServiceProvider<?>>> services;

	private static int firstRun = 0;

	private PlatformUtils()
	{
		// do not instantiate
	}

	/**
	 *
	 */
	private static void checkInitServices()
	{
		if (services == null)
		{
			synchronized (PlatformUtils.class)
			{
				if (services == null)
				{
					Map<String, ExtensionClassWrapper<IServiceProvider<?>>> result = new HashMap<String, ExtensionClassWrapper<IServiceProvider<?>>>();
					IConfigurationElement[] serviceElements = Platform.getExtensionRegistry().getConfigurationElementsFor(
						CessarPluginActivator.PLUGIN_ID, EXT_ID_SDK_SERVICE);
					for (IConfigurationElement serviceElement: serviceElements)
					{
						String serviceID = serviceElement.getAttribute(ATTR_SERVICE_ID);
						try
						{
							ExtensionClassWrapper<IServiceProvider<?>> wrapper = new ExtensionClassWrapper<IServiceProvider<?>>(
								serviceElement, ATTR_PROVIDER);
							result.put(serviceID, wrapper);
						}
						catch (Throwable t)
						{
							CessarPluginActivator.getDefault().logError(t);
						}
					}

					services = result;
				}
			}
		}
	}

	/**
	 * Return the SDK Service for a particular serviceDefinition
	 *
	 * @param <T>
	 * @param serviceDefinition
	 * @return
	 */
	public static <T> T getService(Class<T> serviceDefinition, Object... args)
	{
		checkInitServices();
		ExtensionClassWrapper<IServiceProvider<?>> wrapper = services.get(serviceDefinition.getName());
		if (wrapper != null)
		{
			try
			{
				@SuppressWarnings("unchecked")
				IServiceProvider<T> provider = (IServiceProvider<T>) wrapper.getInstance();
				T service = provider.getService(serviceDefinition, args);
				return service;
			}
			catch (Throwable e)
			{
				CessarPluginActivator.getDefault().logError(e);
				return null;
			}
		}
		else
		{
			CessarPluginActivator.getDefault().logError("No service defined for {0}", //$NON-NLS-1$
				serviceDefinition.getName());
			return null;
		}
	}

	/**
	 * Utility method to run a {@link WorkspaceJob} as an {@link IWorkspaceRunnable}
	 *
	 * @param job
	 * @param monitor
	 * @throws CoreException
	 */
	public static IStatus runInWorkspace(final WorkspaceJob job, IProgressMonitor monitor) throws CoreException
	{
		final IStatus[] result = new IStatus[1];
		ResourcesPlugin.getWorkspace().run(new IWorkspaceRunnable()
		{

			public void run(IProgressMonitor monitor) throws CoreException
			{
				result[0] = job.runInWorkspace(monitor);
			}
		}, job.getRule(), IWorkspace.AVOID_UPDATE, monitor);
		return result[0];
	}

	/**
	 *
	 * Combine two {@link IStatus}'es into an {@link MultiStatus}
	 *
	 * @param s1
	 * @param s2
	 * @return
	 */
	public static IStatus combineStatus(IStatus s1, IStatus s2)
	{
		IStatus result;
		if (s1.isMultiStatus())
		{
			result = s1;
			((MultiStatus) result).add(s2);
		}
		else if (s2.isMultiStatus())
		{
			result = s2;
			((MultiStatus) result).add(s1);
		}
		else
		{
			if (s1 == Status.OK_STATUS)
			{
				result = s2;
			}
			else if (s2 == Status.OK_STATUS)
			{
				result = s1;
			}
			else
			{
				result = new MultiStatus(s1.getPlugin(), s1.getCode(), s1.getMessage(), s1.getException());
				((MultiStatus) result).add(s2);
			}
		}
		return result;
	}

	/**
	 * Cancel and wait for jobs in {@code families} to finish.
	 *
	 * @param cancel
	 *        indicates whether to trigger cancel for the jobs
	 * @param rethrow
	 *        indicates whether to rethrow exceptions as {@code CoreException}
	 * @param families
	 *        the job families to process
	 * @throws CoreException
	 *
	 */
	public static void waitForJobs(boolean cancel, boolean rethrow, Object... families) throws CoreException
	{
		IJobManager jobManager = Job.getJobManager();
		for (Object family: families)
		{
			if (cancel)
			{
				jobManager.cancel(family);
			}
			try
			{
				jobManager.join(family, null);
			}
			catch (OperationCanceledException e)
			{
				if (rethrow)
				{
					throw new CoreException(StatusUtil.createErrorStatus(CessarPluginActivator.getDefault(), e));
				}
			}
			catch (InterruptedException e)
			{
				if (rethrow)
				{
					throw new CoreException(StatusUtil.createErrorStatus(CessarPluginActivator.getDefault(), e));
				}
			}
		}
	}

	/**
	 * Wait the current thread for the model loader to finish loading the model. This method should not be executed from
	 * the Job that actually load the model otherwise some unchecked exception will be throw in order to avoid a
	 * deadlock.
	 *
	 * @param project
	 * @param monitor
	 */
	public static void waitForModelLoading(IProject project, IProgressMonitor monitor)
	{
		Job currentJob = Job.getJobManager().currentJob();
		boolean inBlockingJob = false;
		if (currentJob != null)
		{
			if (currentJob.belongsTo(IExtendedPlatformConstants.FAMILY_MODEL_LOADING))
			{
				throw new RuntimeException(
					"Deadlock detected: Do not call waitForModelLoading from the model loading job"); //$NON-NLS-1$
			}
			else
			{
				// we are in another job, the ShedulingRule has to be checked in
				// order to avoid a dead lock
				ISchedulingRule rule = currentJob.getRule();
				if (rule != null && rule.contains(project))
				{
					// synchron execution required otherwise a deadlock will
					// occur
					inBlockingJob = true;
				}
			}

		}
		try
		{
			// it's possible that at this moment the workspace listener that
			// create the job is not installed, so let's force this
			if (!artopWorkspaceInitialized)
			{
				Bundle b = Platform.getBundle("eclipse.sphinx.emf.workspace"); //$NON-NLS-1$
				if (b != null && b.getState() != Bundle.ACTIVE)
				{
					try
					{
						b.start();
					}
					catch (BundleException e)
					{
						CessarPluginActivator.getDefault().logError(e);
					}
				}
				artopWorkspaceInitialized = true;
			}
			ModelLoadManager.INSTANCE.loadProject(project, true, !inBlockingJob, monitor);
			if (!inBlockingJob)
			{
				Job.getJobManager().join(ResourcesPlugin.FAMILY_AUTO_REFRESH, monitor);
				Job.getJobManager().join(ResourcesPlugin.FAMILY_MANUAL_REFRESH, monitor);
				Job.getJobManager().join(ResourcesPlugin.FAMILY_AUTO_BUILD, monitor);
				Job.getJobManager().join(ResourcesPlugin.FAMILY_MANUAL_BUILD, monitor);
				Job.getJobManager().join(IExtendedPlatformConstants.FAMILY_MODEL_LOADING, monitor);
				Job.getJobManager().join(IExtendedPlatformConstants.FAMILY_LONG_RUNNING, monitor);
			}
		}
		catch (InterruptedException e)
		{
			CessarPluginActivator.getDefault().logError(e);
		}
	}

	/**
	 * Returns the preferred integer radix associated with the given project.
	 *
	 * @param project
	 *        the given project
	 * @return the corresponding <code>ERadix</code>, that can be one of the following:
	 *         <ul>
	 *         <li><code>ERadix.DECIMAL</code></li>
	 *         <li><code>ERadix.HEXA</code></li>
	 *         <li><code>ERadix.BINARY</code></li>
	 *         </ul>
	 */
	public static ERadix getProjectRadixSettings(IProject project)
	{
		// get project integer representation preference
		ProjectScope projectScope = new ProjectScope(project);
		IEclipsePreferences projectPreferences = projectScope.getNode(CessarPluginActivator.PLUGIN_ID);
		String radix = projectPreferences.get(PlatformConstants.PROJECT_CONFIG_RADIX, null);

		// if not found try to find it in Editing preferences of project
		if (radix == null)
		{
			projectPreferences = projectScope.getNode(CESSARPreferencesAccessor.EDITING_NAMESPACE);
			radix = projectPreferences.get(PlatformConstants.PROJECT_CONFIG_RADIX, ERadix.DECIMAL.toString());
		}

		try
		{
			return ERadix.getERadixByLiteral(radix);// ERadix.valueOf(radix);
		}
		catch (IllegalArgumentException e)
		{
			return ERadix.DECIMAL;
		}
	}

	/**
	 * Sets the preferred integer radix for the given <code>project</code> with the given <code>radix</code>, that can
	 * be one of the following:
	 * <ul>
	 * <li><code>ERadix.DECIMAL</code></li>
	 * <li><code>ERadix.HEXA</code></li>
	 * <li><code>ERadix.BINARY</code></li>
	 * </ul>
	 *
	 * @param project
	 *        the given project for which to set the preference
	 * @param radix
	 *        the preferred integer radix
	 */
	public static void setProjectRadixSettings(IProject project, ERadix radix)
	{
		// set project integer representation preference
		ProjectScope projectScope = new ProjectScope(project);
		IEclipsePreferences projectPreferences = projectScope.getNode(CessarPluginActivator.PLUGIN_ID);
		projectPreferences.put(PlatformConstants.PROJECT_CONFIG_RADIX, radix.toString());

		try
		{
			projectPreferences.flush();
		}
		catch (BackingStoreException e)
		{
			CessarPluginActivator.getDefault().logError(e);
		}
	}

	/**
	 *
	 * @param path
	 * @return
	 * @throws CoreException
	 */
	public static IPath resolveVariablePath(IProject project, String stringPath) throws CoreException
	{
		if (stringPath == null)
		{
			return null;
		}
		String result = VariablesPlugin.getDefault().getStringVariableManager().performStringSubstitution(stringPath,
			false);

		IPath path = null;
		if (stringPath.indexOf("${") > 0) //$NON-NLS-1$
		{
			// relative to the workspace
			path = new Path(result);
		}
		else
		{
			// it is either absolute or relative to the project
			path = new Path(result);
			if (!path.isAbsolute())
			{
				path = project.getFullPath().append(path);
			}
		}

		// IPath relativePath =
		// path.makeRelativeTo(project.getWorkspace().getRoot().getLocation());
		// IFolder folder = project.getFolder(relativePath);
		// if (folder != null && folder.exists())
		// {
		// return relativePath;
		// }
		// // this is a system absolute path
		return path;
	}

	/**
	 * Checks if the plugin <b>"eu.cessar.ct.testutils"</b> is loaded. <br>
	 *
	 * @return true if the tool has the specified test plugin loaded
	 */
	public static boolean isTestPluginLoaded()
	{
		if (firstRun == 0)
		{
			Bundle bundle = Platform.getBundle("eu.cessar.ct.testutils"); //$NON-NLS-1$
			if (bundle == null)
			{
				firstRun = -1;
			}
			else
			{
				firstRun = 1;
			}
		}

		return firstRun > 0;
	}
}
