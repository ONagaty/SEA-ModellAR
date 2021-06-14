/**
 * 
 */
package eu.cessar.ct.ant.tasks;

import java.io.File;
import java.util.Date;
import java.util.Enumeration;
import java.util.Vector;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.util.FileUtils;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;

/**
 * 
 */
public class EclipseDependSet extends AbstractTask
{
	private static final FileUtils FILE_UTILS = FileUtils.getFileUtils();

	private Vector<FileSet> sourceFileSets = new Vector<FileSet>();
	private Vector<FileSet> targetFileSets = new Vector<FileSet>();
	private Vector<EclipseFileList> targetFileLists = new Vector<EclipseFileList>();

	/** property to be set only if the project is up-to-date */
	private String property;

	public EclipseDependSet()
	{
		// default constructor
	}

	/**
	 * Executes the task.
	 * 
	 * @throws BuildException
	 *         if errors occur.
	 */
	@Override
	public void execute() throws BuildException
	{
		checkArgs();
		processArgs();

		boolean upToDate = true;

		long now = (new Date()).getTime();
		/*
		 * We have to munge the time to allow for the filesystem time
		 * granularity.
		 */
		now += FILE_UTILS.getFileTimestampGranularity();

		// Grab all the target files specified via filesets:
		Vector<File> allTargets = new Vector<File>();
		long oldestTargetTime = 0;
		File oldestTarget = null;

		// parse the targetSets
		Enumeration<FileSet> enumTargetSets = targetFileSets.elements();
		while (enumTargetSets.hasMoreElements())
		{
			FileSet targetFS = enumTargetSets.nextElement();

			EclipseFileScanner targetDS = createEclipseScanner(targetFS, getProject(), iProject);

			String[] targetFiles = targetDS.getIncludedFiles();

			for (int i = 0; i < targetFiles.length; i++)
			{
				File dest = targetDS.getFile(targetFiles[i]);
				targetFS.log("Found included target file as: " + dest, Project.MSG_VERBOSE); //$NON-NLS-1$
				allTargets.addElement(dest);

				if (dest.lastModified() > now)
				{
					log("Warning: " + targetFiles[i] + " modified in the future.", Project.MSG_WARN); //$NON-NLS-1$//$NON-NLS-2$
				}
				if (oldestTarget == null || dest.lastModified() < oldestTargetTime)
				{
					oldestTargetTime = dest.lastModified();
					oldestTarget = dest;
				}
			}
		}
		// parse the targetLists

		Enumeration<EclipseFileList> enumTargetLists = targetFileLists.elements();
		while (enumTargetLists.hasMoreElements())
		{
			EclipseFileList fileList = enumTargetLists.nextElement();
			String dirName = fileList.getDirName();
			IFolder folder = null;
			if (dirName != null)
			{
				if (FILE_UTILS.isAbsolutePath(dirName))
				{
					fileList.doSetDir(FILE_UTILS.normalize(dirName));
				}
				else
				{
					// use Eclipse to look for dirName
					folder = iProject.getFolder(dirName);
					if (folder.exists() && folder.getLocation() != null)
					{
						fileList.doSetDir(folder.getLocation().toFile());
					}
					else
					{
						// assume not uptodate
						upToDate = false;
						break;
					}
				}
			}
			String[] files = fileList.getFiles(getProject());
			for (String file: files)
			{
				File targetFile = null;
				if (folder == null)
				{
					targetFile = new File(fileList.getDir(getProject()), file);
				}
				else
				{
					IFile iFile = folder.getFile(file);
					if (iFile != null && iFile.getLocation() != null)
					{
						targetFile = iFile.getLocation().toFile();
					}
				}
				if (targetFile == null)
				{
					// assume not uptodate
					upToDate = false;
					break;
				}
				else
				{
					allTargets.addElement(targetFile);

					if (targetFile.lastModified() > now)
					{
						log("Warning: " + file + " modified in the future.", Project.MSG_WARN); //$NON-NLS-1$//$NON-NLS-2$
					}
					if (oldestTarget == null || targetFile.lastModified() < oldestTargetTime)
					{
						oldestTargetTime = targetFile.lastModified();
						oldestTarget = targetFile;
					}
				}
			}

		}
		// Grab all the target files specified via filelists:
		if (oldestTarget != null)
		{
			log(oldestTarget + " is oldest target file", Project.MSG_VERBOSE); //$NON-NLS-1$
		}
		else
		{
			// no target files, then we cannot remove any target files and
			// skip the following tests right away
			upToDate = false;
		}
		// Check targets vs source files specified via filesets:
		if (upToDate)
		{
			Enumeration<?> enumSourceSets = sourceFileSets.elements();
			while (upToDate && enumSourceSets.hasMoreElements())
			{
				FileSet sourceFS = (FileSet) enumSourceSets.nextElement();
				EclipseFileScanner sourceDS = createEclipseScanner(sourceFS, getProject(), iProject);
				String[] sourceFiles = sourceDS.getIncludedFiles();

				for (int i = 0; upToDate && i < sourceFiles.length; i++)
				{
					File src = sourceDS.getFile(sourceFiles[i]);
					sourceFS.log("Found included source file as: " + src, Project.MSG_VERBOSE); //$NON-NLS-1$

					if (src.lastModified() > now)
					{
						log("Warning: " + sourceFiles[i] + " modified in the future.", //$NON-NLS-1$ //$NON-NLS-2$
							Project.MSG_WARN);
					}
					if (src.lastModified() > oldestTargetTime)
					{
						upToDate = false;
						log(oldestTarget + " is out of date with respect to " + sourceFiles[i], //$NON-NLS-1$
							Project.MSG_VERBOSE);
					}
				}
			}
		}
		if (upToDate)
		{
			// set property in case project is up-to-date and there is no need
			// to re-generate jet files
			if (property != null)
			{
				getProject().setUserProperty(property, "uptodate"); //$NON-NLS-1$
			}
		}
		else
		{
			log("Deleting all target files. ", Project.MSG_VERBOSE); //$NON-NLS-1$
			for (Enumeration<?> e = allTargets.elements(); e.hasMoreElements();)
			{
				File fileToRemove = (File) e.nextElement();
				log("Deleting file " + fileToRemove.getAbsolutePath(), Project.MSG_VERBOSE); //$NON-NLS-1$
				fileToRemove.delete();
			}
		}
	}

	@Override
	protected void checkArgs()
	{
		super.checkArgs();

		if ((projectPath == null && projectname == null))
		{
			throw new BuildException("projectname or projectpath must be set"); //$NON-NLS-1$
		}
		if (sourceFileSets.size() == 0)
		{
			throw new BuildException("At least one <srcfileset> element must be set"); //$NON-NLS-1$ 
		}
		if (targetFileSets.size() == 0 && targetFileLists.size() == 0)
		{
			throw new BuildException(
				"At least one <targetfileset> or <targetfilelist> element must be set"); //$NON-NLS-1$ 
		}
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.ant.tasks.AbstractTask#processArgs()
	 */
	@Override
	protected void processArgs()
	{
		super.processArgs();
		if (iProject == null || !iProject.isAccessible())
		{
			throw new BuildException("The project is not accesible"); //$NON-NLS-1$
		}
	}

	public void setProperty(String value)
	{
		property = value;
	}

	public String getProperty()
	{
		return property;
	}

	/**
	 * Add a set of source files.
	 * 
	 * @param fs
	 *        the FileSet to add.
	 */
	public void addConfiguredSrcfileset(final FileSet fs)
	{
		sourceFileSets.addElement(fs);
	}

	/**
	 * Add a set of target files.
	 * 
	 * @param fs
	 *        the FileSet to add.
	 */
	public void addConfiguredTargetfileset(final FileSet fs)
	{
		targetFileSets.addElement(fs);
	}

	/**
	 * @param fs
	 */
	public void addConfiguredTargetfilelist(final EclipseFileList fs)
	{
		targetFileLists.addElement(fs);
	}

	private EclipseFileScanner createEclipseScanner(final FileSet fSet, final Project antProject,
		final IProject eclipseProject)
	{
		EclipseFileScanner ds = new EclipseFileScanner(antProject, eclipseProject);
		fSet.setupDirectoryScanner(ds, antProject);
		ds.scan();
		return ds;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.ant.tasks.AbstractTask#isTaskInCompatibilityMode()
	 */
	@Override
	protected ETaskType getTaskCompatType()
	{
		return ETaskType.UNIVERSAL_TASK;
	}
}
