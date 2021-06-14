/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Sep 23, 2009 3:38:40 PM </copyright>
 */
package eu.cessar.ct.runtime.classpath;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;

import eu.cessar.ct.core.platform.util.CollectionUtils;
import eu.cessar.ct.runtime.execution.IModifiableExecutionLoader;
import eu.cessar.ct.runtime.internal.classpath.util.BundleVariableRegistry;
import eu.cessar.ct.runtime.utils.BundleUtils;

/**
 * 
 * Base class that shall be used by Cessar classpath contributors when it needs
 * to return classpath entries for plugins
 * 
 * @author uidl6458
 * 
 */
public abstract class AbstractPluginsClasspathContributor implements ICessarClasspathContributor
{

	/**
	 * Return the array of bundles IDs provided by the contributor. <br>
	 * </br> NOTE: The given <code>javaProject</code> will be used only by
	 * AutosarModelClasspathContributor
	 * 
	 * @param javaProject
	 * @return an array, never null
	 */
	protected abstract String[] getBundleIDs(IJavaProject javaProject);

	/**
	 * Return whether the dependencies for the bundles returned by
	 * <code>getBundleIDs()</code> should be collected or not. By default,
	 * return true.
	 * 
	 * @return
	 */
	protected boolean collectDependencies()
	{
		return true;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.runtime.classpath.ICessarClasspathContributor#getClasspathEntries(org.eclipse.core.runtime.IPath, org.eclipse.jdt.core.IJavaProject)
	 */
	public IClasspathEntry[] getClasspathEntries(List<IClasspathEntry> existingEntries,
		String containerID, IJavaProject project)
	{
		List<IClasspathEntry> result = new ArrayList<IClasspathEntry>();

		List<IClasspathEntry> allExistingEntries = new ArrayList<IClasspathEntry>();
		allExistingEntries.addAll(existingEntries);

		// bundle IDs provided by contributor
		String[] bundleIDs = getBundleIDs(project);

		List<String> bundleList = BundleUtils.getDependencies(bundleIDs, collectDependencies());

		for (String bundleID: bundleList)
		{
			List<IClasspathEntry> bundleEntries = BundleVariableRegistry.INSTANCE.createCPEntries(
				bundleID, Collections.unmodifiableList(allExistingEntries));
			allExistingEntries.addAll(bundleEntries);
			// collect CPEntries
			result.addAll(bundleEntries);
		}
		return result.toArray(new IClasspathEntry[result.size()]);
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.runtime.classpath.ICessarClasspathContributor#updateExecutionLoader(eu.cessar.ct.runtime.execution.IModifiableExecutionLoader)
	 */
	public void updateExecutionLoader(IModifiableExecutionLoader executionLoader)
	{
		IJavaProject jProject = JavaCore.create(executionLoader.getProject());
		CollectionUtils.addNoDuplicates(executionLoader.getBundleIDs(), getBundleIDs(jProject));
	}

	/**
	 * @param project
	 * @return
	 */
	public String[] getBundles(IJavaProject project)
	{
		return getBundleIDs(project);
	}
}
