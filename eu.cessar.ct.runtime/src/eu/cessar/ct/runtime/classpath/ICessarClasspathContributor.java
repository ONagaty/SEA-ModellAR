/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Sep 22, 2009 1:42:59 PM </copyright>
 */
package eu.cessar.ct.runtime.classpath;

import java.util.List;

import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;

import eu.cessar.ct.runtime.execution.IModifiableExecutionLoader;

/**
 * This interface is used by the implementors of the
 * <code>cessarClasspathContributor</code> extension point. There are two
 * services that should be provided by implementors:<br/>
 * <ul>
 * <li>provide the libraries that will be contributed to the Cessar project
 * class path. This is done by the method
 * {@link #getClasspathEntries(List, IPath, IJavaProject)}</li>
 * <li>provide a {@link ClassLoader} to be used on runtime when some user code
 * will be executed. This is done by the method
 * {@link #getClasspathClassLoader(IPath, IJavaProject)}</li>
 * </ul>
 * 
 * @author uidl6458
 * 
 */
public interface ICessarClasspathContributor
{
	/**
	 * Return the contributed classpath entries. Only classpath entries of type
	 * CPE_LIBRARY and CPE_PROJECT should be returned.
	 * 
	 * @param classPathEntries
	 *        a non-modifiable list of already existing entries. The
	 *        implementors should verify if the entries that he wants to return
	 *        does not already exists. The existing entries shall not be
	 *        returned.
	 * @param containerPath
	 * @param javaProject
	 *        the Java project in which context the container is to be resolved
	 * @return an array of IClasspathEntry, never null
	 */
	public IClasspathEntry[] getClasspathEntries(List<IClasspathEntry> existingEntries,
		String containerID, IJavaProject javaProject);

	/**
	 * @param executionLoader
	 */
	public void updateExecutionLoader(IModifiableExecutionLoader executionLoader);

}
