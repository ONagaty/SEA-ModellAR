package eu.cessar.ct.runtime.internal.classpath;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;

import eu.cessar.ct.runtime.classpath.ICessarClasspathContributor;
import eu.cessar.ct.runtime.execution.IModifiableExecutionLoader;
import eu.cessar.ct.runtime.internal.CessarPluginActivator;

/**
 * {@link ICessarClasspathContributor} for finding the Presentation Model library and including it on the classpath of
 * the project.
 *
 * @author uidg4098
 *
 *         %created_by: created by %
 *
 *         %date_created: creation date %
 *
 *         %version: version %
 */
public class PMClasspathContributor implements ICessarClasspathContributor
{

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.runtime.classpath.ICessarClasspathContributor#getClasspathEntries(java.util.List,
	 * org.eclipse.core.runtime.IPath, org.eclipse.jdt.core.IJavaProject)
	 */
	public IClasspathEntry[] getClasspathEntries(List<IClasspathEntry> existingEntries, String containerID,
		IJavaProject javaProject)
	{
		List<IClasspathEntry> entries = new ArrayList<IClasspathEntry>();

		List<File> pmJars = findPMJars(javaProject.getProject());

		if (!pmJars.isEmpty())
		{
			IPath location;
			for (File file: pmJars)
			{
				String projPath = javaProject.getProject().getLocation().toString();
				String filePath = file.getAbsolutePath().toString().replaceAll("\\\\", "/").substring(projPath.length());

				location = new Path("/" + javaProject.getProject().getName() + filePath);

				IClasspathEntry entry = JavaCore.newLibraryEntry(location, null, null);
				entries.add(entry);
			}
		}
		return entries.toArray(new IClasspathEntry[entries.size()]);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * eu.cessar.ct.runtime.classpath.ICessarClasspathContributor#updateExecutionLoader(eu.cessar.ct.runtime.execution
	 * .IModifiableExecutionLoader)
	 */
	public void updateExecutionLoader(IModifiableExecutionLoader executionLoader)
	{
		// nothing to do
	}

	/**
	 * This method returns the {@link File}s representing the .jar files which hold the presentation model.
	 *
	 * @return the {@link List}s holding the .jar files or an empty list if no jars were found.
	 */
	private static List<File> findPMJars(IProject project)
	{
		IFolder pmFolder = project.getFolder("pmbin"); //$NON-NLS-1$
		List<File> pmJars = new ArrayList<File>();
		try
		{
			if (pmFolder.exists() && pmFolder.members().length > 0)
			{
				File[] files = pmFolder.getLocation().toFile().listFiles(new FilenameFilter()
				{
					@Override
					public boolean accept(File dir, String name)
					{
						return name.endsWith(".jar"); //$NON-NLS-1$
					}
				});

				if (files != null && files.length > 0)
				{
					pmJars = Arrays.asList(files);
				}
			}
		}
		catch (CoreException e)
		{
			CessarPluginActivator.getDefault().logError(e);
		}

		return pmJars;
	}
}