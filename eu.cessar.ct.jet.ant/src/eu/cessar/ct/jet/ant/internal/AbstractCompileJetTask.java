/**
 * <copyright>
 *
 * Copyright (c) Continental AG and others.<br/>
 * http://www.continental-corporation.com All rights reserved.
 *
 * File created by uidu8153<br/>
 * May 25, 2015 7:33:10 AM
 *
 * </copyright>
 */
package eu.cessar.ct.jet.ant.internal;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.tools.ant.BuildException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.osgi.util.NLS;

import eu.cessar.ct.jet.core.JETCoreUtils;
import eu.cessar.ct.jet.core.JETPackerJob;
import eu.cessar.ct.runtime.ecuc.IEcucCore;
import eu.cessar.ct.runtime.ecuc.IEcucPresentationModel;
import eu.cessar.ct.sdk.ant.AbstractCessarTask;

/**
 * Abstract Ant task for compiling jet files.
 *
 * @author uidu8153
 *
 *         %created_by: uidu8153 %
 *
 *         %date_created: Wed Jun 3 12:41:32 2015 %
 *
 *         %version: 4 %
 */
public abstract class AbstractCompileJetTask extends AbstractCessarTask
{

	// required args
	private String jetFile;

	private List<IFile> targetJetFiles;

	/**
	 * Constructor
	 */
	public AbstractCompileJetTask()
	{
		targetJetFiles = new ArrayList<IFile>();
	}

	/**
	 * @return the listOfJetFiles
	 */
	public List<IFile> getTargetJetFiles()
	{
		return targetJetFiles;
	}

	/**
	 * @return the jetFileName
	 */
	public String getJetFile()
	{
		return jetFile;
	}

	/**
	 * @param jetFile
	 */
	public void setJetFile(final String jetFile)
	{
		this.jetFile = jetFile;
	}

	@Override
	protected void doExecute()
	{
		/*
		 * Performs Dump presentation model initially.
		 */

		IEcucPresentationModel ecpm = IEcucCore.INSTANCE.getEcucPresentationModel(iProject);
		String fName = "pmbin"; //$NON-NLS-1$
		ecpm.dumpPresentationModel(false, fName, null);

		IFolder saveFolder = JETCoreUtils.getDumpJavaSourceFolderPref(iProject);
		IJavaProject javaProject = JavaCore.create(iProject);
		boolean sourceFolderIsAdded = false;
		IClasspathEntry[] oldEntries;
		try
		{
			oldEntries = javaProject.getRawClasspath();
			for (IClasspathEntry iClasspathEntry: oldEntries)
			{
				if ((iClasspathEntry.getEntryKind() == IClasspathEntry.CPE_SOURCE)
					&& (iClasspathEntry.getPath().equals(saveFolder.getFullPath())))
				{
					sourceFolderIsAdded = true;
				}

			}
			if (!sourceFolderIsAdded)
			{
				// +1 for our src/main/java entry
				IClasspathEntry[] newEntries = new IClasspathEntry[oldEntries.length + 1];

				System.arraycopy(oldEntries, 0, newEntries, 0, oldEntries.length);

				IPackageFragmentRoot packageFragment = javaProject.getPackageFragmentRoot(saveFolder);
				newEntries[oldEntries.length] = JavaCore.newSourceEntry(packageFragment.getPath(), new Path[] {});
				javaProject.setRawClasspath(newEntries, null);
			}
		}
		catch (JavaModelException e)
		{
			throw new BuildException(e.getMessage());
		}

		JETPackerJob jetPacker = new JETPackerJob(iProject, JETCoreUtils.getJetComplianceLevel(iProject));

		Iterator<IFile> iterator = targetJetFiles.iterator();
		while (iterator.hasNext())
		{
			jetPacker.addFile(iterator.next());
		}
		jetPacker.schedule();

		// wait for JET packer job to finish compiling the JET
		try
		{
			jetPacker.join();
		}
		catch (InterruptedException exception)
		{
			throw new BuildException("Jet Compilation stopped because an error ocurred: " + (exception)); //$NON-NLS-1$
		}

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.ant.tasks.AbstractTask#checkArgs()
	 */
	@Override
	protected void checkArgs()
	{

		super.checkArgs();

		if (projectname == null)
		{
			throw new BuildException("The project name parameter must be set"); //$NON-NLS-1$
		}

		if (jetFile == null || jetFile.length() == 0)
		{
			throw new BuildException("At least one jet file should be set"); //$NON-NLS-1$
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.ant.tasks.AbstractTask#processArgs()
	 */
	@Override
	protected void processArgs()
	{
		// TODO Auto-generated method stub
		super.processArgs();

		// to compile jet out of the jetFiles arg
		StringTokenizer st = new StringTokenizer(jetFile, ","); //$NON-NLS-1$
		while (st.hasMoreTokens())
		{

			String relPath = st.nextToken().trim();

			IPath path = new Path(relPath).makeAbsolute();
			IFile file = iProject.getFile(path);

			if (file.exists())
			{
				if (JETCoreUtils.isJetFile(file))
				{
					targetJetFiles.add(file);
				}
				else
				{
					throw new BuildException(NLS.bind("File is not of type Jet", relPath)); //$NON-NLS-1$
				}
			}
			else
			{
				throw new BuildException(NLS.bind("Jet file not found", relPath)); //$NON-NLS-1$
			}

		}

	}
}
