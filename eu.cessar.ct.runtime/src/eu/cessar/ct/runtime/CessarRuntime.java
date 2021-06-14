/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Sep 24, 2009 2:08:17 PM </copyright>
 */
package eu.cessar.ct.runtime;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResourceRuleFactory;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jdt.core.IClasspathContainer;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaModel;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;

import eu.cessar.ct.runtime.classpath.GenericClasspathContainer;
import eu.cessar.ct.runtime.classpath.IClasspathContributionsRegistry;
import eu.cessar.ct.runtime.classpath.ModelClasspathContainer;
import eu.cessar.ct.runtime.classpath.ProjectClasspathContainer;
import eu.cessar.ct.runtime.execution.IExecutionSupport;
import eu.cessar.ct.runtime.internal.CessarPluginActivator;
import eu.cessar.ct.runtime.internal.Messages;
import eu.cessar.ct.runtime.internal.classpath.ClasspathContributionsRegistry;
import eu.cessar.ct.runtime.internal.execution.ExecutionSupportImpl;

/**
 * Various utilities methods that support for runtime related tasks like class path management, class loading support.
 * 
 */
public final class CessarRuntime
{

	public static final String JAR_EXTENSION = ".jar"; //$NON-NLS-1$

	/** Java nature identifier */
	public static final String JAVA_NATURE = JavaCore.NATURE_ID;

	/** Java builder identifier */
	public static final String JAVA_BUILDER = JavaCore.BUILDER_ID;

	public static final String CONTAINER_ID_GENERIC = CessarPluginActivator.PLUGIN_ID
		+ ".classpath.genericClasspathContainer"; //$NON-NLS-1$

	public static final IPath CONTAINER_PATH_GENERIC = new Path(CONTAINER_ID_GENERIC);

	public static final String CONTAINER_DESCRIPTION_GENERIC = "CESSAR Libraries"; //$NON-NLS-1$

	public static final String CONTAINER_ID_PROJECT = CessarPluginActivator.PLUGIN_ID
		+ ".classpath.projectClasspathContainer"; //$NON-NLS-1$

	public static final IPath CONTAINER_PATH_PROJECT = new Path(CONTAINER_ID_PROJECT);

	public static final String CONTAINER_DESCRIPTION_PROJECT = "CESSAR Project Libraries"; //$NON-NLS-1$

	public static final String CONTAINER_ID_MODEL = CessarPluginActivator.PLUGIN_ID
		+ ".classpath.modelClasspathContainer"; //$NON-NLS-1$

	public static final IPath CONTAINER_PATH_MODEL = new Path(CONTAINER_ID_MODEL);

	public static final String CONTAINER_DESCRIPTION_MODEL = "CESSAR Model Libraries"; //$NON-NLS-1$

	/** Classpath container used for a project's JRE */
	public static final String CONTAINER_ID_JRE = "org.eclipse.jdt.launching.JRE_CONTAINER"; //$NON-NLS-1$

	public static final Path CONTAINER_PATH_JRE = new Path(CONTAINER_ID_JRE);

	/**
	 * The folder set as output for JDT, where the java sources will be compiled
	 */
	public static final String HIDDEN_BIN_FOLDER_NAME = ".bin"; //$NON-NLS-1$

	/**
	 * The (virtual) folder where the model will appear. Please note that this is a virtual folder, it will be linked to
	 * "cfs:/PROJECT_NAME"
	 */
	public static final String HIDDEN_MODEL_FOLDER_NAME = ".model"; //$NON-NLS-1$

	/**
	 * This is the scheme that will be used for the model virtual classes.
	 */
	public static final String CFS_SCHEME = "cfs"; //$NON-NLS-1$

	public static final Object FAMILY_CLASSPATH_UPDATE = new Object();

	// =======================Eclipse MANIFEST headers==================
	public static final String ECLIPSE_PATCH_FRAGMENT = "Eclipse-PatchFragment"; //$NON-NLS-1$
	public static final String ECLIPSE_EXTENSIBLE_API = "Eclipse-ExtensibleAPI"; //$NON-NLS-1$

	// ==================MANIFEST directives
	public static final String DIRECTIVE_VISIBILITY = "visibility"; //$NON-NLS-1$
	public static final String DIRECTIVE_REEXPORT = "reexport"; //$NON-NLS-1$

	public static final String CLASSPATH_FILE = ".classpath"; //$NON-NLS-1$
	// ===========.classpath attributes
	public static final String CLASSPATHENTRY = "classpathentry"; //$NON-NLS-1$
	public static final String ATTRIBUTE_KIND = "kind"; //$NON-NLS-1$
	public static final String ATTRIBUTE_PATH = "path"; //$NON-NLS-1$
	public static final String KIND_VALUE_OUTPUT = "output"; //$NON-NLS-1$

	public static final String BINARY_FILE_EXTENSION = ".class"; //$NON-NLS-1$
	/**
	 * The pluget content type ID
	 */
	public static final String PLUGET_CONTENT_TYPE_ID = "eu.cessar.ct.runtime.contenttype.pluget"; //$NON-NLS-1$

	/** Identifier of job family for running plugets jobs */
	public static final Object FAMILY_RUNNING_PLUGETS = "RUNNING_PLUGETS"; //$NON-NLS-1$

	/** pluget's main method name */
	public static final String PLUGET_RUN_METHOD_NAME = "run"; //$NON-NLS-1$

	/**
	 * The default name of the folder that will be used for code generation
	 */
	public static final String CODEGEN_DEFAULT_OUTPUT_FOLDER = "generated"; //$NON-NLS-1$

	private static IClasspathEntry[] cpEntries = null;

	// non-instantiable
	private CessarRuntime()
	{
	}

	/**
	 * Get the default Cessar classpath entries any Cessar project should have
	 * 
	 * @return
	 */
	public static IClasspathEntry[] getDefaultCessarClasspathEntries()
	{
		if (cpEntries == null)
		{
			synchronized (CessarRuntime.class)
			{
				if (cpEntries == null)
				{
					cpEntries = new IClasspathEntry[4];
					cpEntries[0] = JavaCore.newContainerEntry(CessarRuntime.CONTAINER_PATH_JRE);
					cpEntries[1] = JavaCore.newContainerEntry(CessarRuntime.CONTAINER_PATH_MODEL);
					cpEntries[2] = JavaCore.newContainerEntry(CessarRuntime.CONTAINER_PATH_PROJECT);
					cpEntries[3] = JavaCore.newContainerEntry(CessarRuntime.CONTAINER_PATH_GENERIC);
				}
			}
		}
		return cpEntries;
	}

	/**
	 * Return the IClasspathContributionRegistry
	 * 
	 * @return
	 */
	public static IClasspathContributionsRegistry getClasspathContributionRegistry()
	{
		return ClasspathContributionsRegistry.INSTANCE;
	}

	/**
	 * @return
	 */
	public static IExecutionSupport getExecutionSupport()
	{
		return ExecutionSupportImpl.INSTANCE;
	}

	/**
	 * Reset the {@link ModelClasspathContainer} to all projects
	 * 
	 * @param projects
	 * @throws JavaModelException
	 */
	public static void resetGenericContainer(final IJavaProject project)
	{
		// do nothing, not needed actually ?
		/*
		 * if (true) { return; }
		 */

		// create a job for each project, if we execute it now we could end
		// in a dead lock
		IResourceRuleFactory factory = ResourcesPlugin.getWorkspace().getRuleFactory();
		Job job = new Job(Messages.CessarRuntime_updateClassPathJob)
		{

			@Override
			protected IStatus run(IProgressMonitor monitor)
			{
				runGenericContainer(project, monitor);
				return Status.OK_STATUS;
			}

			@Override
			public final boolean belongsTo(Object family)
			{
				return CessarRuntime.FAMILY_CLASSPATH_UPDATE.equals(family);
			}

		};
		ISchedulingRule rule = factory.modifyRule(project.getProject().getParent());
		job.setRule(rule);
		job.setPriority(Job.BUILD);
		job.schedule();

	}

	/**
	 * Reset the {@link ModelClasspathContainer} to all projects
	 * 
	 * @param projects
	 * @throws JavaModelException
	 */
	public static void resetModelContainer(final IProject project)
	{
		// do nothing, not needed actually ?
		if (true)
		{
			return;
		}

		// create a job for each project, if we execute it now we could end
		// in a dead lock
		IResourceRuleFactory factory = ResourcesPlugin.getWorkspace().getRuleFactory();
		Job job = new Job(Messages.CessarRuntime_updateClassPathJob)
		{

			@Override
			protected IStatus run(IProgressMonitor monitor)
			{
				runResetModelContainer(project, monitor);
				return Status.OK_STATUS;
			}

			@Override
			public final boolean belongsTo(Object family)
			{
				return CessarRuntime.FAMILY_CLASSPATH_UPDATE.equals(family);
			}

		};
		ISchedulingRule rule = factory.modifyRule(project.getParent());
		job.setRule(rule);
		job.setPriority(Job.BUILD);
		job.schedule();

	}

	/**
	 * Reset the {@link ProjectClasspathContainer} to all projects
	 * 
	 * @param projects
	 * @throws JavaModelException
	 */
	public static void resetProjectContainer(final IProject[] projects)
	{
		if (projects == null || projects.length == 0)
		{
			// nothing to do
			return;
		}
		// create a job for this, if we to execute it now we could end in a dead
		// lock
		Job job = new Job(Messages.CessarRuntime_updateClassPathJob)
		{
			@Override
			protected IStatus run(IProgressMonitor monitor)
			{
				runResetProjectContainer(projects, monitor);
				return Status.OK_STATUS;
			}

			@Override
			public final boolean belongsTo(Object family)
			{
				return CessarRuntime.FAMILY_CLASSPATH_UPDATE.equals(family);
			}

		};
		job.setRule(ResourcesPlugin.getWorkspace().getRoot());
		job.setPriority(Job.BUILD);
		job.schedule();
	}

	/**
	 * Execute from the update classpath job
	 * 
	 * @param projects
	 * @param monitor
	 */
	private static void runGenericContainer(IJavaProject aProject, IProgressMonitor monitor)
	{
		// at the time of the execution we could have less projects available
		if (!aProject.getProject().isAccessible())
		{
			return;
		}
		// IJavaProject javaProject = JavaCore.create(aProject);
		try
		{
			JavaCore.setClasspathContainer(CONTAINER_PATH_GENERIC, new IJavaProject[] {aProject},
				new IClasspathContainer[] {new GenericClasspathContainer(aProject)}, monitor);

		}
		catch (JavaModelException e)
		{
			e.printStackTrace();
			// failed to update the classpath, log it
			CessarPluginActivator.getDefault().logError(e);
		}
	}

	/**
	 * Execute from the update classpath job
	 * 
	 * @param projects
	 * @param monitor
	 */
	private static void runResetModelContainer(IProject aProject, IProgressMonitor monitor)
	{
		// at the time of the execution we could have less projects available
		if (!aProject.isAccessible())
		{
			return;
		}
		IJavaProject javaProject = JavaCore.create(aProject);
		try
		{
			JavaCore.setClasspathContainer(CONTAINER_PATH_MODEL, new IJavaProject[] {javaProject},
				new IClasspathContainer[] {new ModelClasspathContainer(javaProject)}, monitor);

		}
		catch (JavaModelException e)
		{
			// failed to update the classpath, log it
			CessarPluginActivator.getDefault().logError(e);
		}
	}

	/**
	 * Execute from the update classpath job
	 * 
	 * @param projects
	 * @param monitor
	 */
	private static void runResetProjectContainer(IProject[] projects, IProgressMonitor monitor)
	{
		// at the time of the execution we could have less projects available
		List<IJavaProject> projectList = new ArrayList<IJavaProject>();
		List<ProjectClasspathContainer> containerList = new ArrayList<ProjectClasspathContainer>();
		for (int i = 0; i < projects.length; i++)
		{
			if (projects[i].isAccessible())
			{
				IJavaProject jProject = JavaCore.create(projects[i]);
				projectList.add(jProject);
				containerList.add(new ProjectClasspathContainer(jProject));
			}
		}
		try
		{
			IClasspathContainer[] array = new IClasspathContainer[projectList.size()];
			// reset CP container
			JavaCore.setClasspathContainer(CONTAINER_PATH_PROJECT,
				projectList.toArray(new IJavaProject[projectList.size()]), array, monitor);

			IJavaModel model = JavaCore.create(ResourcesPlugin.getWorkspace().getRoot());
			List<IJavaElement> javaElements = new ArrayList<IJavaElement>();
			javaElements.add(model);
			// refresh
			model.refreshExternalArchives(javaElements.toArray(new IJavaElement[javaElements.size()]), monitor);
		}
		catch (JavaModelException e)
		{
			// failed to update the classpath, log it
			CessarPluginActivator.getDefault().logError(e);
		}

	}

	/**
	 * Creates a ClassFolder that contains the implementations of the Interfaces from the PresentationModel, also add's
	 * all the new entry's to the Classpath
	 * 
	 * @param projectSelected
	 *        The project in which the PresentationModel is.The ClassFolder will be created for this PresentationModel
	 * @param dumpFolder
	 *        The folder in which the ClassFolder will be added, the same as the folder in which the PresentationModel
	 *        has been added
	 * @param monitor
	 * @throws JavaModelException
	 */

	public static void addClassFolder(IProject projectSelected, String dumpFolder, IProgressMonitor monitor)
		throws JavaModelException
	{
		IJavaProject jProject = JavaCore.create(projectSelected);
		IClasspathEntry[] oldClasspath = jProject.getRawClasspath();
		final IPath pmPath = projectSelected.getFolder(dumpFolder).getFullPath();
		for (IClasspathEntry entry: oldClasspath)
		{
			if (entry.getPath().equals(pmPath))
			{ // Already present, so don't add it again.
				return;
			}
		}
		IClasspathEntry[] newEntries = new IClasspathEntry[oldClasspath.length + 1];
		newEntries[0] = JavaCore.newLibraryEntry(pmPath, null, null);
		System.arraycopy(oldClasspath, 0, newEntries, 1, oldClasspath.length);
		jProject.setRawClasspath(newEntries, monitor);
	}

}
