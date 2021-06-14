/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Jan 28, 2010 7:01:12 PM </copyright>
 */
package eu.cessar.ct.runtime.internal.execution;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.eclipse.core.commands.common.EventManager;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.SafeRunner;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;

import eu.cessar.ct.core.platform.util.SafeRunnable;
import eu.cessar.ct.runtime.CessarRuntime;
import eu.cessar.ct.runtime.execution.IExecutionLoader;
import eu.cessar.ct.runtime.execution.IExecutionSupport;
import eu.cessar.ct.runtime.execution.IExecutionSupportListener;
import eu.cessar.ct.runtime.execution.IModifiableExecutionLoader;
import eu.cessar.ct.runtime.internal.CessarPluginActivator;
import eu.cessar.ct.sdk.runtime.ICessarTaskManager;

/**
 * @author uidl6458
 * 
 */
public final class ExecutionSupportImpl extends EventManager implements IExecutionSupport
{
	/**
	 * the singleton
	 */
	public static final IExecutionSupport INSTANCE = new ExecutionSupportImpl();

	/**
	 * @author uidl6458
	 * 
	 */
	private final class ExecutionLoaderWrapper
	{

		private Stack<ICessarTaskManager<?>> clients = new Stack<ICessarTaskManager<?>>();

		private IModifiableExecutionLoader loader;

		private ExecutionLoaderWrapper(IProject project)
		{
			loader = new ModifiableExecutionLoader(project);
		}

		/**
		 * @param manager
		 */
		public void aquire(ICessarTaskManager<?> manager)
		{
			clients.push(manager);
		}

		/**
		 * @return current manager
		 */
		public ICessarTaskManager<?> getCurrentManager()
		{
			if (clients.size() > 0)
			{
				return clients.peek();
			}
			else
			{
				return null;
			}
		}

		public void release()
		{
			if (clients.size() > 0)
			{
				clients.pop();
			}
		}

	}

	private Map<IProject, ExecutionLoaderWrapper> executionLoaders;

	/**
	 * The private constructor of the singleton
	 */
	private ExecutionSupportImpl()
	{
		executionLoaders = new HashMap<IProject, ExecutionLoaderWrapper>();
		ResourcesPlugin.getWorkspace().addResourceChangeListener(new IResourceChangeListener()
		{

			@Override
			public void resourceChanged(IResourceChangeEvent event)
			{
				executionLoaders.remove(event.getResource());
			}
		}, IResourceChangeEvent.PRE_CLOSE | IResourceChangeEvent.PRE_DELETE);
	}

	/**
	 * @param project
	 * @return
	 */
	private static List<URL> getBinaryOutputsAndExternalLibraries(IJavaProject project)
	{
		try
		{
			List<URL> result = new ArrayList<URL>();
			boolean defaultOutputUsed = false;
			IClasspathEntry[] cpEntries = project.getRawClasspath();

			List<File> files = new ArrayList<File>();

			// collect pathnames corresponding to the output folders and to the external libraries
			for (IClasspathEntry cpEntry: cpEntries)
			{
				if (cpEntry.getEntryKind() == IClasspathEntry.CPE_SOURCE)
				{
					IPath outputLocation = cpEntry.getOutputLocation();
					if (outputLocation == null)
					{
						defaultOutputUsed = true;
					}
					else
					{
						files.add(outputLocation.toFile());
					}
				}
				else if (isExternalLibrary(project, cpEntry))
				{
					File file = cpEntry.getPath().toFile();
					files.add(file);
				}
				else if (cpEntry.getEntryKind() == IClasspathEntry.CPE_PROJECT)
				{
					// add the binaries of the referenced project
					IResource member = ResourcesPlugin.getWorkspace().getRoot().findMember(cpEntry.getPath());
					if (member instanceof IProject && member.isAccessible())
					{
						IJavaProject javaProject = JavaCore.create((IProject) member);
						result.addAll(getBinaryOutputsAndExternalLibraries(javaProject));
					}
				}
			}

			if (defaultOutputUsed)
			{
				IPath path = project.getOutputLocation();
				IFolder folder = ResourcesPlugin.getWorkspace().getRoot().getFolder(path);
				files.add(folder.getLocation().toFile());
			}

			// create URLs out of the collected files
			for (File pathName: files)
			{
				try
				{
					URL url = pathName.toURI().toURL();
					result.add(url);
				}
				catch (MalformedURLException e)
				{
					CessarPluginActivator.getDefault().logError(e);
				}
			}

			return result;
		}
		catch (JavaModelException e)
		{
			// log and ignore, return a EmptyClassLoader
			CessarPluginActivator.getDefault().logError(e);
			return Collections.emptyList();

		}
	}

	/**
	 * Return whether the <code>cpEntry</code> represents an external library (JAR or class folder) on the specified
	 * <code>javaProject</code>'s build path
	 * 
	 * @param javaProject
	 * @param cpEntry
	 * @return
	 * @throws JavaModelException
	 */
	private static boolean isExternalLibrary(IJavaProject javaProject, IClasspathEntry cpEntry)
		throws JavaModelException
	{
		boolean isExternalLibrary = false;
		if (cpEntry.getEntryKind() == IClasspathEntry.CPE_LIBRARY
			&& cpEntry.getContentKind() == IPackageFragmentRoot.K_BINARY)
		{
			IPath path = cpEntry.getPath();
			IPackageFragmentRoot packageFragmentRoot = javaProject.findPackageFragmentRoot(path);

			isExternalLibrary = (packageFragmentRoot != null && packageFragmentRoot.isExternal());
		}
		return isExternalLibrary;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.cessar.ct.runtime.execution.IExecutionSupport#acquireExecutionLoader(eu.cessar.ct.sdk.runtime.ICessarTaskManager
	 * )
	 */
	public IExecutionLoader acquireExecutionLoader(ICessarTaskManager<?> manager)
	{
		IProject project = manager.getProject();

		ExecutionLoaderWrapper wrapper = executionLoaders.get(project);
		if (wrapper == null)
		{
			wrapper = new ExecutionLoaderWrapper(project);
			wrapper.loader.getCustomLibraries().addAll(getBinaryOutputsAndExternalLibraries(JavaCore.create(project)));
			CessarRuntime.getClasspathContributionRegistry().updateExecutionLoader(wrapper.loader);
			executionLoaders.put(project, wrapper);
		}
		wrapper.aquire(manager);
		// notifyAquired(project, manager, wrapper.classLoader);
		notifyAquired(project, manager, wrapper.loader);
		return wrapper.loader;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.cessar.ct.runtime.execution.IExecutionSupport#releaseExecutionLoader(eu.cessar.ct.sdk.runtime.ICessarTaskManager
	 * )
	 */
	public void releaseExecutionLoader(ICessarTaskManager<?> manager)
	{
		IProject project = manager.getProject();
		ExecutionLoaderWrapper wrapper = executionLoaders.get(project);
		if (wrapper == null)
		{
			throw new AssertionError("No IExecutionLoader aquired yet for the project " //$NON-NLS-1$
				+ project.getName());
		}
		ICessarTaskManager<?> topManager = wrapper.getCurrentManager();
		if (topManager != manager)
		{
			throw new AssertionError("IExecutionLoader acquisition broken, last acquisitor is " //$NON-NLS-1$
				+ topManager.getCessarTaskID() + " but the releasor is" + manager.getCessarTaskID()); //$NON-NLS-1$
		}
		wrapper.release();
		IModifiableExecutionLoader loader = wrapper.loader;
		if (wrapper.clients.size() == 0)
		{
			// drop the class loader completely
			executionLoaders.remove(project);
			loader.dispose();
			loader = null;
		}
		notifyReleased(project, manager, loader);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.cessar.ct.runtime.execution.IExecutionSupport#getActiveExecutionLoader(org.eclipse.core.resources.IProject)
	 */
	public IExecutionLoader getActiveExecutionLoader(IProject project)
	{
		if (executionLoaders.containsKey(project))
		{
			return executionLoaders.get(project).loader;
		}
		else
		{
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.runtime.execution.IExecutionSupport#getActiveManager(org.eclipse.core.resources.IProject)
	 */
	public ICessarTaskManager<?> getCurrentManager(IProject project)
	{
		if (executionLoaders.containsKey(project))
		{
			return executionLoaders.get(project).getCurrentManager();
		}
		else
		{
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.cessar.ct.runtime.execution.IClassLoaderService#addListener(eu.cessar.ct.runtime.execution.IClassLoaderListener
	 * )
	 */
	public void addListener(IExecutionSupportListener listener)
	{
		addListenerObject(listener);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.cessar.ct.runtime.execution.IClassLoaderService#removeListener(eu.cessar.ct.runtime.execution.IClassLoaderListener
	 * )
	 */
	public void removeListener(IExecutionSupportListener listener)
	{
		removeListenerObject(listener);
	}

	private void notifyAquired(final IProject project, final ICessarTaskManager<?> manager,
		final IModifiableExecutionLoader executionLoader)
	{
		Object[] listeners = getListeners();
		for (final Object object: listeners)
		{
			final IExecutionSupportListener listener = (IExecutionSupportListener) object;

			SafeRunner.run(new SafeRunnable()
			{

				public void run() throws Exception
				{
					listener.executionSupportAquired(project, manager, executionLoader);
				}
			});
		}
	}

	/**
	 * @param project
	 * @param manager
	 * @param classLoader
	 */
	private void notifyReleased(final IProject project, final ICessarTaskManager<?> manager,
		final IModifiableExecutionLoader loader)
	{
		Object[] listeners = getListeners();
		for (final Object object: listeners)
		{
			final IExecutionSupportListener listener = (IExecutionSupportListener) object;

			SafeRunner.run(new SafeRunnable()
			{

				public void run() throws Exception
				{
					listener.executionSupportReleased(project, manager, loader);
				}
			});
		}
	}
}
