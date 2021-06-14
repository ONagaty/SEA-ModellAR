/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870 Feb 1, 2010 3:05:20 PM </copyright>
 */
package eu.cessar.ct.runtime.internal.classpath;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;

import eu.cessar.ct.core.platform.util.CollectionUtils;
import eu.cessar.ct.runtime.classpath.ICessarClasspathContributor;
import eu.cessar.ct.runtime.execution.IModifiableExecutionLoader;
import eu.cessar.ct.runtime.internal.CessarPluginActivator;

/**
 * @author uidl6870
 * 
 */
public class ProjectLibrariesClasspathContributor implements ICessarClasspathContributor
{

	/* (non-Javadoc)
	 * @see eu.cessar.ct.runtime.classpath.ICessarClasspathContributor#getClasspathEntries(java.util.List, org.eclipse.core.runtime.IPath, org.eclipse.jdt.core.IJavaProject)
	 */
	public IClasspathEntry[] getClasspathEntries(List<IClasspathEntry> existingEntries,
		String containerID, IJavaProject javaProject)
	{
		List<IClasspathEntry> entries = new ArrayList<IClasspathEntry>();

		IFile[] libraries = ProjectLibrariesManager.eINSTANCE.getLibraries(javaProject.getProject());

		for (IFile ifile: libraries)
		{
			IPath location = ifile.getLocation();
			IClasspathEntry entry = JavaCore.newLibraryEntry(location, null, null);
			entries.add(entry);
		}

		return entries.toArray(new IClasspathEntry[entries.size()]);
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.runtime.classpath.ICessarClasspathContributor#updateExecutionLoader(eu.cessar.ct.runtime.execution.IModifiableExecutionLoader)
	 */
	public void updateExecutionLoader(IModifiableExecutionLoader executionLoader)
	{
		IFile[] libraries = ProjectLibrariesManager.eINSTANCE.getLibraries(executionLoader.getProject());

		List<URL> urls = new ArrayList<URL>();
		for (IFile file: libraries)
		{
			try
			{
				URL url = file.getRawLocation().toFile().toURI().toURL();
				urls.add(url);
			}
			catch (MalformedURLException e)
			{
				CessarPluginActivator.getDefault().logError(e);
			}
		}
		CollectionUtils.addNoDuplicates(executionLoader.getCustomLibraries(), urls);
	}
}
