/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidg4098<br/>
 * 13.08.2015 13:36:39
 *
 * </copyright>
 */
package eu.cessar.ct.runtime.ecuc.internal.pm;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubProgressMonitor;

import eu.cessar.ct.core.platform.util.JarBuilder;
import eu.cessar.ct.runtime.ecuc.internal.CessarPluginActivator;
import eu.cessar.ct.runtime.ecuc.internal.Messages;
import eu.cessar.ct.runtime.ecuc.internal.pm.asm.IPMBinaryClassWrapper;
import eu.cessar.req.Requirement;

/**
 * Class containing methods for packaging the Presentation Model .jar file and adding it to the project's classpath.
 *
 * @author uidg4098
 *
 *         %created_by: created by %
 *
 *         %date_created: creation date %
 *
 *         %version: version %
 */
@Requirement(
	reqID = "137964")
public class PMPackageManager
{
	private List<File> jarFiles = new ArrayList<File>();
	private IProject project;

	/**
	 * Constructor for the PMPackageManager
	 *
	 * @param project
	 *        the currently active project
	 */
	public PMPackageManager(IProject project)
	{
		this.project = project;
	}

	/*
	 * Because the .class files have a path also containing the path of the pmbin folder, this part of the path shall be
	 * trimmed.
	 */
	private String getRelFolderStructure(File f, String pmbinFolder)
	{
		String res = f.getAbsolutePath();
		res = res.substring(res.indexOf(pmbinFolder) + pmbinFolder.length() + 2);
		if (f.isDirectory())
		{
			res += "/"; //$NON-NLS-1$
		}
		return res;
	}

	/**
	 * Method for packing the PM class files into a .jar file.
	 *
	 * @param outputFolder
	 *        the pmbin folder where the .jar is saved
	 * @param monitor
	 *        the progress monitor
	 * @param dumpImpl
	 *        sets whether the implementation classes shall be dumped
	 * @param pmBinaryClasses
	 *        the PM classes to be packaged in the .jar file
	 * @return the {@link IStatus} whether the packaging in the .jar succeeded.
	 */
	public IStatus packPMToJar(IFolder outputFolder, IProgressMonitor monitor, boolean dumpImpl,
		Map<String, IPMBinaryClassWrapper> pmBinaryClasses)
	{
		try
		{
			initJarFilesList(outputFolder);

			JarBuilder jb;
			String pmbinPath = outputFolder.getLocation().toString();

			if (monitor != null)
			{
				monitor.beginTask(Messages.message_dump, pmBinaryClasses.size());
				monitor.subTask(Messages.message_writePM);
			}

			SubProgressMonitor subMonitor = monitor == null ? null : new SubProgressMonitor(monitor, 1);

			for (String jarFile: groupClassesToJars(pmBinaryClasses).keySet())
			{
				File file = new File(outputFolder.getLocation().toString() + "/" + jarFile); //$NON-NLS-1$
				jb = new JarBuilder(file);

				for (IPMBinaryClassWrapper wrapper: pmBinaryClasses.values())
				{
					if (wrapper.isImplClass() && !dumpImpl)
					{
						continue;
					}
					IFile outFile = outputFolder.getFile(wrapper.getClassFileName());
					ByteArrayInputStream bIn = new ByteArrayInputStream(wrapper.getBinary(dumpImpl));
					jb.addToJar(getRelFolderStructure(outFile.getLocation().toFile(), pmbinPath), bIn, subMonitor);
				}

				jb.done(monitor);

				jarFiles.add(file);
			}

			if (monitor != null)
			{
				monitor.beginTask(Messages.message_reload, IProgressMonitor.UNKNOWN);
			}

			project.refreshLocal(IResource.DEPTH_INFINITE, monitor);

			if (monitor != null)
			{
				monitor.done();
			}

		}
		catch (FileNotFoundException e)
		{
			return CessarPluginActivator.getDefault().createStatus(e);
		}
		catch (IOException e)
		{
			return CessarPluginActivator.getDefault().createStatus(e);
		}
		catch (CoreException e)
		{
			return CessarPluginActivator.getDefault().createStatus(e);
		}

		return Status.OK_STATUS;
	}

	/**
	 * Retrieves the existing .jar files from the output folder and deletes them.
	 *
	 * @param outputFolder
	 *        the folder where the PM jars are generated.
	 * @throws CoreException
	 */
	private void initJarFilesList(IFolder outputFolder) throws CoreException
	{
		if (outputFolder.exists() && outputFolder.members().length > 0)
		{
			File[] files = outputFolder.getLocation().toFile().listFiles(new FilenameFilter()
			{
				@Override
				public boolean accept(File file, String name)
				{
					return name.endsWith(".jar"); //$NON-NLS-1$
				}
			});

			if (files != null && files.length > 0)
			{
				jarFiles = Arrays.asList(files);
			}
		}

		for (File file: jarFiles)
		{
			file.delete();
		}

		jarFiles = new ArrayList<File>();
	}

	/**
	 * Method handling the grouping of PM classes into .jars. This method shall be updated to group the class files into
	 * several separate jars if needed later.
	 *
	 * @param pmBinaryClasses
	 * @return
	 */
	private Map<String, List<IPMBinaryClassWrapper>> groupClassesToJars(
		Map<String, IPMBinaryClassWrapper> pmBinaryClasses)
		{
		Map<String, List<IPMBinaryClassWrapper>> jar2ClassesMap = new HashMap<String, List<IPMBinaryClassWrapper>>();

		jar2ClassesMap.put(PMJavaConstants.PM_DUMP_ARCHIVE,
			new ArrayList<IPMBinaryClassWrapper>(pmBinaryClasses.values()));

		return jar2ClassesMap;
		}

}
