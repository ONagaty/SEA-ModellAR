/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Sep 24, 2009 1:59:27 PM </copyright>
 */
package eu.cessar.ct.runtime.classpath;

import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IJavaProject;

import eu.cessar.ct.runtime.CessarRuntime;

/**
 * @author uidl6458
 * 
 */
public class ProjectClasspathContainer extends AbstractContributableClasspathContainer
{
	/**
	 * @param contributionID
	 */
	public ProjectClasspathContainer(IJavaProject project)
	{
		super(project);
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.runtime.classpath.AbstractContributableClasspathContainer#getContributionID()
	 */
	@Override
	public String getContributionID()
	{
		return CessarRuntime.CONTAINER_ID_PROJECT;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jdt.core.IClasspathContainer#getDescription()
	 */
	public String getDescription()
	{
		return CessarRuntime.CONTAINER_DESCRIPTION_PROJECT;
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
		return CessarRuntime.CONTAINER_PATH_PROJECT;
	}

}
