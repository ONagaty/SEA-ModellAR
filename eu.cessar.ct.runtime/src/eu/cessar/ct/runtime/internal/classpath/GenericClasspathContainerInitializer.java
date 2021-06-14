/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Sep 22, 2009 11:21:21 AM </copyright>
 */
package eu.cessar.ct.runtime.internal.classpath;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.ClasspathContainerInitializer;
import org.eclipse.jdt.core.IClasspathContainer;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;

import eu.cessar.ct.runtime.classpath.GenericClasspathContainer;

/**
 * Provides the static, project independent, classpath container. It will
 * collect IClasspathEntry's from contributors using the
 * <code>eu.cessar.ct.runtime.classpath.genericClasspathContainerInitializer</code>
 * id
 * 
 * @author uidl6458
 * 
 */
public class GenericClasspathContainerInitializer extends ClasspathContainerInitializer
{

	/* (non-Javadoc)
	 * @see org.eclipse.jdt.core.ClasspathContainerInitializer#initialize(org.eclipse.core.runtime.IPath, org.eclipse.jdt.core.IJavaProject)
	 */
	@Override
	public void initialize(IPath containerPath, IJavaProject project) throws CoreException
	{
		// TODO Auto-generated method stub
		JavaCore.setClasspathContainer(containerPath, new IJavaProject[] {project},
			new IClasspathContainer[] {new GenericClasspathContainer(project)},
			new NullProgressMonitor());
	}
}
