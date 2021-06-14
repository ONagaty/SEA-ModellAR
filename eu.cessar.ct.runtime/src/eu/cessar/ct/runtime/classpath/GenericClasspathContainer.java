/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Sep 24, 2009 10:53:13 AM </copyright>
 */
package eu.cessar.ct.runtime.classpath;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;

import eu.cessar.ct.runtime.CessarRuntime;
import eu.cessar.ct.runtime.internal.classpath.PropertyPluginsClasspathContributor;
import eu.cessar.ct.runtime.internal.classpath.util.BundleVariableRegistry;
import eu.cessar.ct.runtime.utils.BundleUtils;
import eu.cessar.ct.runtime.utils.RequiredPluginsAccessor;

/**
 * @author uidl6458
 * 
 */
public class GenericClasspathContainer extends AbstractContributableClasspathContainer
{

	/* (non-Javadoc)
	 * Caters for PropertyPluginsClasspathContributor implementation (preference file
	 * which includes any additional Java bundles to be enabled inside the CT project)    
	 * @see org.eclipse.jdt.core.IClasspathContainer#getClasspathEntries()
	 */
	@Override
	public IClasspathEntry[] getClasspathEntries()
	{
		List<IClasspathEntry> all = new ArrayList<IClasspathEntry>();
		List<ICessarClasspathContributor> contributors = CessarRuntime.getClasspathContributionRegistry().getContributors(
			getContributionID());

		if (contributors != null)
		{
			// iterate over all contributors of containerID
			for (ICessarClasspathContributor contributor: contributors)
			{
				if (!(contributor instanceof PropertyPluginsClasspathContributor))
				{
					IClasspathEntry[] classpathEntries = contributor.getClasspathEntries(
						Collections.unmodifiableList(all), getContributionID(), javaProject);
					all.addAll(Arrays.asList(classpathEntries));
				}
				else
				{
					String[] bundleIDs = RequiredPluginsAccessor.getPluginList(javaProject.getProject());
					List<String> bundleList = BundleUtils.getDependencies(bundleIDs, true);
					for (String bundleID: bundleList)
					{
						List<IClasspathEntry> bundleEntries = BundleVariableRegistry.INSTANCE.createCPEntries(
							bundleID, Collections.unmodifiableList(all));
						all.addAll(bundleEntries);
					}
				}
			}
		}
		IClasspathEntry[] result = new IClasspathEntry[all.size()];
		all.toArray(result);
		Arrays.sort(result, new CPEComparator());

		return result;
	}

	private class CPEComparator implements Comparator<IClasspathEntry>
	{
		/* (non-Javadoc)
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

	/**
	 * @param contributionID
	 */
	public GenericClasspathContainer(IJavaProject project)
	{
		super(project);
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.runtime.classpath.AbstractContributableClasspathContainer#getContributionID()
	 */
	@Override
	public String getContributionID()
	{
		return CessarRuntime.CONTAINER_ID_GENERIC;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jdt.core.IClasspathContainer#getDescription()
	 */
	public String getDescription()
	{
		return CessarRuntime.CONTAINER_DESCRIPTION_GENERIC;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jdt.core.IClasspathContainer#getKind()
	 */
	public int getKind()
	{
		return K_SYSTEM;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jdt.core.IClasspathContainer#getPath()
	 */
	public IPath getPath()
	{
		return CessarRuntime.CONTAINER_PATH_GENERIC;
	}

}
