/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidl6870 Oct 15, 2009 4:58:01 PM </copyright>
 */
package eu.cessar.ct.runtime.internal.classpath;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;

import eu.cessar.ct.runtime.classpath.ICessarClasspathContributor;
import eu.cessar.ct.runtime.classpath.IClasspathContributionsRegistry;
import eu.cessar.ct.runtime.execution.IModifiableExecutionLoader;
import eu.cessar.ct.runtime.internal.CessarPluginActivator;
import eu.cessar.ct.runtime.internal.Messages;

/**
 * @author uidl6870
 *
 */
public final class ClasspathContributionsRegistry implements IClasspathContributionsRegistry
{
	private static final boolean TRACE_CP_CONTRIB = Boolean.valueOf(
		CessarPluginActivator.getDefault().getDebugOption("/trace/classpath/contribution"));

	/** the singleton */
	public static final ClasspathContributionsRegistry INSTANCE = new ClasspathContributionsRegistry();

	private static final String CESSAR_CP_EXTENSION_ID = CessarPluginActivator.PLUGIN_ID
		+ ".cessarClasspathContributor"; //$NON-NLS-1$

	private static final String ATTRIBUTE_CLASS = "class"; //$NON-NLS-1$

	private static final String ATTRIBUTE_CONTAINER = "container"; //$NON-NLS-1$

	/**
	 * a mapping between class path containers ID and a list of their contributors
	 */
	private Map<String, List<ICessarClasspathContributor>> map;

	private ClasspathContributionsRegistry()
	{
		// avoid instantiation
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * eu.cessar.ct.runtime.classpath.IClasspathContributionsRegistry#getContributedClasspathEntry(java.lang.String)
	 */
	public IClasspathEntry[] getContributedClasspathEntry(IJavaProject javaProject, String containerID)
	{
		List<IClasspathEntry> all = new ArrayList<IClasspathEntry>();
		List<ICessarClasspathContributor> contributors = getContributors(containerID);

		if (contributors != null)
		{
			// iterate over all contributors of containerID
			for (ICessarClasspathContributor contributor: contributors)
			{
				IClasspathEntry[] classpathEntries = contributor.getClasspathEntries(Collections.unmodifiableList(all),
					containerID, javaProject);
				if (TRACE_CP_CONTRIB)
				{
					System.out.println(
						"Libraries contributed by:" + javaProject.getProject().getName() + "/" + containerID);
					for (IClasspathEntry cpEntry: classpathEntries)
					{
						System.out.println("  " + cpEntry.getPath());
					}
				}
				// trace...
				all.addAll(Arrays.asList(classpathEntries));
			}
		}
		IClasspathEntry[] result = new IClasspathEntry[all.size()];
		all.toArray(result);
		Arrays.sort(result, new CPEComparator());

		return result;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.runtime.classpath.IClasspathContributionsRegistry#getContributors(java.lang.String)
	 */
	public List<ICessarClasspathContributor> getContributors(String containerID)
	{
		checkInit();
		List<ICessarClasspathContributor> contributors = map.get(containerID);
		if (contributors != null)
		{
			return contributors;
		}
		// return an empty list if no contributors found
		return Collections.emptyList();
	}

	/**
	 * Initialize by reading from plugin registry
	 */
	private void checkInit()
	{
		if (map == null)
		{
			// Display display = PlatformUI.getWorkbench().getDisplay();
			// display.syncExec(new Runnable()
			// {
			//
			// @Override
			// public void run()
			// {
			synchronized (INSTANCE)
			{
				if (map == null)
				{
					Map<String, List<ICessarClasspathContributor>> result = new HashMap<>();

					IConfigurationElement[] elements = Platform.getExtensionRegistry().getConfigurationElementsFor(
						CESSAR_CP_EXTENSION_ID);

					for (IConfigurationElement cfgElement: elements)
					{
						String descriptor = cfgElement.getAttribute(ATTRIBUTE_CONTAINER);
						try
						{
							Object extension = cfgElement.createExecutableExtension(ATTRIBUTE_CLASS);

							if (!(extension instanceof ICessarClasspathContributor))
							{
								CessarPluginActivator.getDefault().logError(Messages.InvalidExtension,
									ICessarClasspathContributor.class, extension.getClass());
							}
							else
							{
								List<ICessarClasspathContributor> contributors;
								if (result.containsKey(descriptor))
								{
									contributors = result.get(descriptor);
								}
								else
								{
									contributors = new ArrayList<>();
									result.put(descriptor, contributors);
								}
								contributors.add((ICessarClasspathContributor) extension);
							}
						}
						catch (CoreException e)
						{
							CessarPluginActivator.getDefault().logError(e);
							continue;
						}
					}
					map = result;
				}
			}

			// }
			// });
		}
	}

	/**
	 * Comparator for CPEntries. Sorts CPEntries alphabetically after the last segment of their path
	 *
	 * @author uidl6870
	 */
	private class CPEComparator implements Comparator<IClasspathEntry>
	{
		/*
		 * (non-Javadoc)
		 *
		 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
		 */
		public int compare(IClasspathEntry entry1, IClasspathEntry entry2)
		{
			if (entry1 != null && entry2 != null)
			{
				IPath path1 = entry1.getPath();
				IPath path2 = entry2.getPath();

				return path1.lastSegment().compareTo(path2.lastSegment());
			}
			return 0;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.runtime.classpath.IClasspathContributionsRegistry#updateExecutionLoader(eu.cessar.ct.runtime.
	 * execution.IModifiableExecutionLoader)
	 */
	public void updateExecutionLoader(IModifiableExecutionLoader executionLoader)
	{
		checkInit();

		for (String contributorID: map.keySet())
		{
			updateExecutionLoader(executionLoader, contributorID);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.runtime.classpath.IClasspathContributionsRegistry#updateExecutionLoader(eu.cessar.ct.runtime.
	 * execution.IModifiableExecutionLoader, java.lang.String)
	 */
	public void updateExecutionLoader(IModifiableExecutionLoader executionLoader, String containerID)
	{
		checkInit();
		List<ICessarClasspathContributor> contributors = map.get(containerID);
		if (contributors != null)
		{
			for (int i = 0; i < contributors.size(); i++)
			{
				contributors.get(i).updateExecutionLoader(executionLoader);
			}
		}
	}

}
