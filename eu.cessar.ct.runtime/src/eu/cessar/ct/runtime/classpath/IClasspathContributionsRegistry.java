/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Sep 24, 2009 2:15:02 PM </copyright>
 */
package eu.cessar.ct.runtime.classpath;

import java.util.List;

import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;

import eu.cessar.ct.runtime.execution.IModifiableExecutionLoader;

/**
 * @author uidl6458
 * 
 */
public interface IClasspathContributionsRegistry
{
	/**
	 * Return the Cessar classpath contributors to a specific containerID
	 * 
	 * @param containerID
	 * @return The contributors list, never null
	 */
	public List<ICessarClasspathContributor> getContributors(String containerID);

	/**
	 * Return the classpath entries contributed to a specific containerID.
	 * 
	 * @param javaProject
	 * @param containerID
	 * @return the classpath entries, never null
	 */
	public IClasspathEntry[] getContributedClasspathEntry(IJavaProject javaProject,
		String containerID);

	/**
	 * @param executionLoader
	 */
	public void updateExecutionLoader(IModifiableExecutionLoader executionLoader);

	/**
	 * @param executionLoader
	 * @param containerID
	 */
	public void updateExecutionLoader(IModifiableExecutionLoader executionLoader, String containerID);

}
