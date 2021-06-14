/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Sep 24, 2009 10:42:17 AM </copyright>
 */
package eu.cessar.ct.runtime.classpath;

import org.eclipse.jdt.core.IClasspathContainer;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;

import eu.cessar.ct.runtime.CessarRuntime;

/**
 * @author uidl6458
 * 
 *         Base implementation for Cessar classpath containers
 */
public abstract class AbstractContributableClasspathContainer implements IClasspathContainer
{

	protected final IJavaProject javaProject;

	public AbstractContributableClasspathContainer(IJavaProject project)
	{
		javaProject = project;
	}

	/**
	 * @return the ID of classpath container
	 */
	public abstract String getContributionID();

	/* (non-Javadoc)
	 * @see org.eclipse.jdt.core.IClasspathContainer#getClasspathEntries()
	 */
	public IClasspathEntry[] getClasspathEntries()
	{
		return CessarRuntime.getClasspathContributionRegistry().getContributedClasspathEntry(
			javaProject, getContributionID());
	}
}
