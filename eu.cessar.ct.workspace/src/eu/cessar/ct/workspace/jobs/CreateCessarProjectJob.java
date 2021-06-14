package eu.cessar.ct.workspace.jobs;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.artop.aal.common.metamodel.AutosarReleaseDescriptor;
import org.artop.aal.workspace.jobs.CreateNewAutosarProjectJob;
import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ProjectScope;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.sphinx.platform.util.ExtendedPlatform;
import org.osgi.service.prefs.BackingStoreException;

import eu.cessar.ct.core.platform.CESSARPreferencesAccessor;
import eu.cessar.ct.core.platform.EProjectVariant;
import eu.cessar.ct.core.platform.PlatformConstants;
import eu.cessar.ct.runtime.CessarRuntime;
import eu.cessar.ct.workspace.internal.CessarPluginActivator;
import eu.cessar.ct.workspace.internal.Messages;

/**
 * A job capable to create a Cessar project
 */
@SuppressWarnings("nls")
public class CreateCessarProjectJob extends CreateNewAutosarProjectJob
{

	private EProjectVariant configVariant = EProjectVariant.DEVELOPMENT;

	/**
	 * Creates the job associated with the creation of a Cessar project.
	 *
	 * @param project
	 *        the associated project
	 * @param location
	 *        the project's location
	 * @param autosarRelease
	 *        the release associated with the project
	 */
	public CreateCessarProjectJob(IProject project, URI location, AutosarReleaseDescriptor autosarRelease)
	{
		super(Messages.job_creatingAutosarProject, project, location, autosarRelease);
	}

	/**
	 * Sets the value of configuration mode that will be associated with the project.
	 *
	 * @param configVariant
	 *        the value that will be associated with the compatibility mode.<br>
	 *        Can be one of the following:
	 *        <ul>
	 *        <li><code>EProjectVariant.PRE_COMPILE</code></li>
	 *        <li><code>EProjectVarian.LINK_TIME</code></li>
	 *        <li><code>EProjectVarian.POST_BUILD</code></li>
	 *        <li><code>EProjectVarian.OTHER</code></li>
	 *        </ul>
	 *        The default value is <code>PlatformConstants.OTHER</code>.
	 */
	public void setConfigVariant(EProjectVariant configVariant)
	{
		this.configVariant = configVariant;
	}

	/**
	 * Returns the value of configuration mode associated with the project.
	 *
	 * @param configVariant
	 *        the value that will be associated with the compatibility mode.<br>
	 *        Can be one of the following:
	 *        <ul>
	 *        <li><code>EProjectVariant.PRE_COMPILE</code></li>
	 *        <li><code>EProjectVarian.LINK_TIME</code></li>
	 *        <li><code>EProjectVarian.POST_BUILD</code></li>
	 *        <li><code>EProjectVarian.OTHER</code></li>
	 *        </ul>
	 *        The default value is <code>PlatformConstants.OTHER</code>.
	 * @return The project variant
	 */
	public EProjectVariant getConfigVariant()
	{
		return configVariant;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.artop.aal.workspace.jobs.CreateArtopProjectJob#runInWorkspace(org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	public IStatus runInWorkspace(IProgressMonitor monitor) throws CoreException
	{
		IStatus status = super.runInWorkspace(monitor);
		if (!status.isOK())
		{
			return status;
		}
		addPrefs();
		return Status.OK_STATUS;
	}

	/**
	 * Provide access to the newly created project
	 *
	 * @return
	 */
	private IProject getProject()
	{
		return newProject;
	}

	/**
	 * Adds the preferences associated with the configuration mode and configuration variant.
	 */
	private void addPrefs()
	{
		ProjectScope projectScope = new ProjectScope(getProject());
		IEclipsePreferences projectPreferences = projectScope.getNode(CESSARPreferencesAccessor.NAMESPACE);
		projectPreferences.put(CESSARPreferencesAccessor.KEY_CONFIGURATION_VARIANT, getConfigVariant().toString());

		try
		{
			projectPreferences.flush();
		}
		catch (BackingStoreException e)
		{
			CessarPluginActivator.getDefault().logError(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.artop.aal.workspace.jobs.CreateArtopProjectJob#addNatures(org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	protected void addNatures(IProgressMonitor monitor) throws CoreException
	{
		super.addNatures(monitor);
		monitor.subTask(Messages.CessarProjectJob_AddNatures);

		// if Java nature exists, remove it before adding the CESSAR nature.
		// Java nature needs to be added after the CESSAR nature; their ordering affects the display of CESSAR or J sign
		// for the project
		IProjectDescription description = getProject().getDescription();
		if (description.hasNature(CessarRuntime.JAVA_NATURE))
		{
			ExtendedPlatform.removeNature(getProject(), CessarRuntime.JAVA_NATURE, new SubProgressMonitor(monitor, 1));
		}
		ExtendedPlatform.addNature(getProject(), PlatformConstants.CESSAR_NATURE, new SubProgressMonitor(monitor, 1));
		// CessarNature.setNature(getProject(), new SubProgressMonitor(monitor, 1));
		convertToJavaProject(new SubProgressMonitor(monitor, 1));
	}

	/**
	 * Converts the given project to a Java project
	 *
	 * @param project
	 * @param monitor
	 * @throws CoreException
	 */
	private void convertToJavaProject(IProgressMonitor monitor) throws CoreException
	{
		IProject theProject = getProject();
		ExtendedPlatform.addNature(theProject, CessarRuntime.JAVA_NATURE, monitor);
		// set output folder and the classpath entries
		IJavaProject javaProject = JavaCore.create(theProject);
		IFolder binFolder = theProject.getFolder(CessarRuntime.HIDDEN_BIN_FOLDER_NAME);
		if (!binFolder.exists())
		{
			binFolder.create(true, true, monitor);
		}

		// create the .model folder only if the CFS is installed
		boolean cfsInstalled = true;
		try
		{
			EFS.getFileSystem(CessarRuntime.CFS_SCHEME);
			// FIXME: re-enable this when CFS is done
			cfsInstalled = false;
		}
		catch (CoreException c)
		{
			// CFS not installed or failed to initialize
			cfsInstalled = false;
		}

		List<IClasspathEntry> cpEntries = new ArrayList<IClasspathEntry>();

		addExistingClassPath(cpEntries, theProject, javaProject);

		if (cfsInstalled)
		{
			IFolder modelFolder = theProject.getFolder(CessarRuntime.HIDDEN_MODEL_FOLDER_NAME);
			try
			{
				modelFolder.createLink(new URI(CessarRuntime.CFS_SCHEME + "://" + theProject.getName()),
					IResource.ALLOW_MISSING_LOCAL | IResource.BACKGROUND_REFRESH, monitor);
			}
			catch (URISyntaxException e)
			{
				throw new CoreException(CessarPluginActivator.getDefault().createStatus(e));
			}
		}
		else
		{
			// FIXME: remove this when CFS is done
			// IFolder folder = theProject.getFolder("pmbin");
			// if (!folder.exists())
			// {
			// folder.create(true, true, monitor);
			// }
			// IClasspathEntry entryPMBIN = JavaCore.newLibraryEntry(folder.getFullPath(), null, null);
			// if (!classpathExists(entryPMBIN, cpEntries))
			// {
			// cpEntries.add(entryPMBIN);
			// }
		}
		// overwrite Output location
		javaProject.setOutputLocation(binFolder.getFullPath(), monitor);

		// add all entries from the default classpath if they don't exist
		IClasspathEntry[] cpDefaultEntries = CessarRuntime.getDefaultCessarClasspathEntries();
		for (IClasspathEntry defaultEntry: cpDefaultEntries)
		{
			if (!classpathExists(defaultEntry, cpEntries))
			{
				cpEntries.add(defaultEntry);
			}
		}

		javaProject.setRawClasspath(cpEntries.toArray(new IClasspathEntry[cpEntries.size()]), true, monitor);
		javaProject.setOption(JavaCore.COMPILER_COMPLIANCE, "1.5");
		javaProject.setOption(JavaCore.COMPILER_SOURCE, "1.5");
		javaProject.setOption(JavaCore.COMPILER_CODEGEN_TARGET_PLATFORM, "1.5");
	}

	/**
	 * append to the given list of classpath entries the entries present in .classpath file
	 *
	 * @param cpEntries
	 * @param theProject
	 * @param javaProject
	 * @throws JavaModelException
	 */
	private void addExistingClassPath(List<IClasspathEntry> cpEntries, IProject theProject, IJavaProject javaProject)
		throws JavaModelException
	{
		IFile file = theProject.getFile(".classpath");
		boolean classPathFileExists = file.exists();
		// if .classpath file does not exist, getRawClasspath() is not accessed because it returns a default entry
		if (classPathFileExists)
		{
			// add in the list all existing classpath entries
			for (IClasspathEntry entry: javaProject.getRawClasspath())
			{
				cpEntries.add(entry);
			}
		}
	}

	/**
	 * Verify if the given entry is present in the entry list, or if the entry path is contained in the path of one of
	 * the entries of the same type
	 *
	 * @return
	 */
	private boolean classpathExists(IClasspathEntry entry, List<IClasspathEntry> cpEntriesList)
	{
		Iterator<IClasspathEntry> iterator = cpEntriesList.iterator();
		while (iterator.hasNext())
		{
			IClasspathEntry currentEntry = iterator.next();
			if (currentEntry == null)
			{
				continue;
			}
			// verify if the entry is present in the list
			if (entry.equals(currentEntry))
			{
				return true;
			}
			// verify if the entry is present in the list but with a different path
			if ((entry.getEntryKind() == currentEntry.getEntryKind())
				&& (entry.getPath().isPrefixOf(currentEntry.getPath())))
			{
				return true;
			}

		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.sphinx.emf.workspace.jobs.CreateNewModelProjectJob#createNewProject(org.eclipse.core.runtime.
	 * IProgressMonitor)
	 */
	@Override
	protected void createNewProject(IProgressMonitor monitor) throws CoreException
	{
		IProject iProject = getProject();
		if (!iProject.exists())
		{
			super.createNewProject(monitor);
		}
		if (!iProject.isOpen())
		{
			iProject.open(monitor);
		}
	}
}
