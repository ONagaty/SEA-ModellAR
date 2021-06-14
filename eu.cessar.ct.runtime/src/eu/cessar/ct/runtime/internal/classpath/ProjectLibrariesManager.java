/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870 Feb 1, 2010 3:53:25 PM </copyright>
 */
package eu.cessar.ct.runtime.internal.classpath;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

import eu.cessar.ct.core.platform.PlatformConstants;
import eu.cessar.ct.runtime.CessarRuntime;
import eu.cessar.ct.runtime.internal.CessarPluginActivator;

/**
 * Keeps a in-memory mapping between projects and their JAR files. <br>
 * <br>
 * Note: jars of the following categories are ignored:
 * 
 * <li>jars that contain a root folder named "ecuc"</li> <li>jars whose manifest contain "Jet-class" directive</li>
 * 
 */
public final class ProjectLibrariesManager implements IResourceChangeListener
{

	/**
	 * @author uidu3379
	 * 
	 */
	private final class VerifyJarRunnable implements IWorkspaceRunnable
	{
		private final boolean[] result;
		private final IFile iFile;

		/**
		 * Jar Verifier - checks if the jar file contains compiles jets and if the PM is not an older version.
		 * 
		 * @param result
		 *        the result of the verification
		 * @param iFile
		 *        the jar file to verify
		 */
		private VerifyJarRunnable(boolean[] result, IFile iFile)
		{
			this.result = result;
			this.iFile = iFile;
		}

		public void run(IProgressMonitor monitor) throws CoreException
		{
			JarFile jarFile = null;
			try
			{
				jarFile = new JarFile(iFile.getRawLocation().toOSString());

				if (isCompiledJet(jarFile))
				{
					return;
				}
				Enumeration<JarEntry> entries = jarFile.entries();
				while (entries.hasMoreElements())
				{
					JarEntry element = entries.nextElement();

					if (isOldPM(element))
					{
						return;
					}
				}
				result[0] = true;
			}
			catch (IOException e)
			{
				CessarPluginActivator.getDefault().logError(e);
			}
			finally
			{
				try
				{
					if (jarFile != null)
					{
						jarFile.close();
					}
				}
				catch (IOException e)
				{
					CessarPluginActivator.getDefault().logError(e);
				}
			}
		}
	}

	public static final ProjectLibrariesManager eINSTANCE = new ProjectLibrariesManager();

	/**
	 * mapping between projects from workspace and IFiles representing user contributed jars
	 */
	private Map<IProject, List<IFile>> map = new HashMap<IProject, List<IFile>>();

	/**
	 * 
	 */
	private ProjectLibrariesManager()
	{
		// do nothing
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.core.resources.IResourceChangeListener#resourceChanged(org.eclipse.core.resources.IResourceChangeEvent
	 * )
	 */
	public void resourceChanged(IResourceChangeEvent event)
	{
		synchronized (ProjectLibrariesManager.this)
		{
			switch (event.getType())
			{
				case IResourceChangeEvent.POST_CHANGE:
					doPostChange(event);
					break;
				case IResourceChangeEvent.PRE_CLOSE:
					doPreClose(event);
					break;
				case IResourceChangeEvent.PRE_DELETE:
					doPreDelete(event);
					break;
				default: // nothing
			}
		}
	}

	/**
	 * @param event
	 */
	private void doPostChange(IResourceChangeEvent event)
	{
		IResourceDelta delta = event.getDelta();

		if (delta != null)
		{
			IResourceDelta[] projectsDelta = delta.getAffectedChildren();

			// list of projects for which Project container has to be reset
			List<IProject> projectList = new ArrayList<IProject>();

			for (IResourceDelta projDelta: projectsDelta)
			{
				IProject project = (IProject) projDelta.getResource();

				if (!isApplicable(project))
				{
					return;
				}

				if (!map.containsKey(project))
				{
					map.put(project, new ArrayList<IFile>());
					visitProject(project, map.get(project));
				}
				else
				{
					projectList.addAll(getProjectsToUpdate(projDelta, project));
				}
			}

			// reset Project container
			if (projectList.size() > 0)
			{
				CessarRuntime.resetProjectContainer(projectList.toArray(new IProject[projectList.size()]));
			}
		}
	}

	/**
	 * Gets the projects in which jar files were updated.
	 * 
	 * @param projDelta
	 *        the changes
	 * @param project
	 *        the project
	 * @return
	 */
	private List<IProject> getProjectsToUpdate(IResourceDelta projDelta, IProject project)
	{
		List<IProject> projectList = new ArrayList<IProject>();

		List<IResourceDelta> affectedFiles = getAffectedFiles(projDelta);

		for (IResourceDelta affectedFile: affectedFiles)
		{
			IResource resource = affectedFile.getResource();

			// filter after IFiles having ".jar" extension
			if (resource.getType() == IResource.FILE && resource.getName().endsWith(CessarRuntime.JAR_EXTENSION))
			{
				// true if the proj must be updated
				boolean updateProj = filterJars(project, affectedFile, resource);
				if (updateProj && !projectList.contains(project))
				{
					projectList.add(project);
				}
			}
		}

		return projectList;
	}

	/**
	 * Checks if the provided resource is a valid jar file (as stated by {@link ProjectLibrariesManager#isApplicable()})
	 * and was updated. Also updates the {@link ProjectLibrariesManager#map} variable.
	 * 
	 * @param project
	 *        the project to verify against
	 * @param affectedFile
	 *        the changes inside the project
	 * @param resource
	 *        the resource to verify
	 * @return <code>true</code> if the project must be updated, <code>false</code> otherwise
	 */
	private boolean filterJars(IProject project, IResourceDelta affectedFile, IResource resource)
	{
		boolean updateProj = false;

		IFile ifile = (IFile) resource;

		List<IFile> list = map.get(project);

		switch (affectedFile.getKind())
		{
			case IResourceDelta.ADDED:

				if (isApplicable(ifile))
				{
					if (!list.contains(ifile))
					{
						list.add(ifile);
						updateProj = true;
					}
				}
				break;

			case IResourceDelta.CHANGED:

				if (list.contains(ifile))
				{
					// remove old library
					list.remove(ifile);
					updateProj = true;
				}
				if (isApplicable(ifile))
				{
					// add the modified library only if
					// applicable
					list.add(ifile);
					updateProj = true;
				}
				break;
			case IResourceDelta.REMOVED:

				if (list.contains(ifile))
				{
					list.remove(ifile);
					updateProj = true;
				}
				break;
			default:
				updateProj = false;
		}
		return updateProj;
	}

	/**
	 * @param event
	 */
	private void doPreClose(IResourceChangeEvent event)
	{
		IResource resource = event.getResource();
		if (resource instanceof IProject)
		{
			map.remove(resource);
		}
	}

	/**
	 * @param event
	 */
	private void doPreDelete(IResourceChangeEvent event)
	{
		IResource resource = event.getResource();
		if (resource instanceof IProject)
		{
			map.remove(resource);
		}
	}

	private List<IResourceDelta> getAffectedFiles(final IResourceDelta delta)
	{
		List<IResourceDelta> result = new ArrayList<IResourceDelta>();

		IResourceDelta[] affectedResources = delta.getAffectedChildren();
		for (IResourceDelta resourceDelta: affectedResources)
		{
			if (resourceDelta.getResource() instanceof IFile)
			{
				result.add(resourceDelta);
			}
			else if (resourceDelta.getResource() instanceof IFolder)
			{
				// Recursive call to gather the children
				result.addAll(getAffectedFiles(resourceDelta));
			}
		}
		return result;
	}

	/**
	 * Return an array of IFiles representing archives located inside given project
	 * 
	 * @param project
	 * @return an array of IFile, never null
	 */
	public synchronized IFile[] getLibraries(IProject project)
	{
		if (!isApplicable(project))
		{
			return new IFile[0];
		}
		if (!map.containsKey(project))
		{
			List<IFile> list = new ArrayList<IFile>();
			map.put(project, list);
			visitProject(project, list);
		}
		List<IFile> list = map.get(project);

		return list.toArray(new IFile[list.size()]);
	}

	private void visitProject(IProject project, final List<IFile> list)
	{
		try
		{
			project.accept(new IResourceProxyVisitor()
			{
				public boolean visit(IResourceProxy proxy) throws CoreException
				{
					if (proxy.getType() == IResource.FILE && proxy.getName().endsWith(CessarRuntime.JAR_EXTENSION))
					{
						IFile ifile = (IFile) proxy.requestResource();

						if (isApplicable(ifile))
						{
							list.add(ifile);
						}
					}
					return true;
				}
			}, 0);
		}
		catch (CoreException e)
		{
			CessarPluginActivator.getDefault().logError(e);
			return;
		}
	}

	/**
	 * Return <code>true</code> if given argument <code>iFile</code> satisfies the following: <li>represents a JAR file</li>
	 * <li>does not contain a root directory named "ecuc"</li> <li>the manifest(if any) of the jar does not contain the
	 * "Jet-class" directive</li><br>
	 * <br>
	 * If one of the above condition is not met, <code>false</code> is returned.
	 * 
	 * @param iFile
	 * 
	 * @return <code>true</code> if all conditions are met, <code>false</code> otherwise
	 */
	private boolean isApplicable(final IFile iFile)
	{
		final boolean[] result = {false};
		try
		{
			IWorkspaceRunnable runnable = new VerifyJarRunnable(result, iFile);
			// this code lead to deadlock
			// if (ResourcesPlugin.getWorkspace().isTreeLocked())
			// {
			runnable.run(null);
			// }
			// else
			// {
			// ResourcesPlugin.getWorkspace().run(runnable, iFile,
			// IWorkspace.AVOID_UPDATE,
			// new NullProgressMonitor());
			// }
		}
		catch (CoreException e)
		{
			CessarPluginActivator.getDefault().logError(e);
		}
		return result[0];
	}

	/**
	 * Return <code>true</code> if given project exists, is opened and has Cessar nature
	 * 
	 * @param project
	 * @return
	 */
	private boolean isApplicable(IProject project)
	{
		try
		{
			return project.isAccessible() && project.hasNature(PlatformConstants.CESSAR_NATURE);
		}
		catch (CoreException e)
		{
			// should never get here
			CessarPluginActivator.getDefault().logError(e);
			return false;
		}
	}

	/**
	 * Return true if the manifest of the jar contains "Jet-class" directive. This indicates the jar represents a
	 * compiled jet and therefore is not off interest
	 * 
	 * @param element
	 * @return
	 */
	private boolean isCompiledJet(JarFile jarFile)
	{
		try
		{
			Manifest manifest = jarFile.getManifest();

			if (manifest != null)
			{
				String value = manifest.getMainAttributes().getValue("Jet-class"); //$NON-NLS-1$
				return value != null;
			}
			else
			{
				return false;
			}
		}
		catch (IOException e)
		{
			CessarPluginActivator.getDefault().logError(e);
			return false;
		}
	}

	/**
	 * Return <code>true</code> if the root folder of the given jar entry is named "ecuc", otherwise return
	 * <code>false</code>
	 * 
	 * @param element
	 * @return
	 */
	private boolean isOldPM(JarEntry element)
	{
		return element.getName().startsWith("ecuc/"); //$NON-NLS-1$
	}
}
