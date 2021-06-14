/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870 Nov 11, 2010 1:08:53 PM </copyright>
 */
package eu.cessar.ct.runtime.internal.extension;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceProxy;
import org.eclipse.core.resources.IResourceProxyVisitor;
import org.eclipse.core.runtime.CoreException;

import eu.cessar.ct.core.platform.PlatformConstants;
import eu.cessar.ct.runtime.internal.CessarPluginActivator;

/**
 * @author uidl6870
 * 
 */
public abstract class AbstractResChangeListener implements IResourceChangeListener
{

	/**
	 * mapping between projects from workspace and IFiles that are of interest
	 */
	private Map<IProject, List<IFile>> map = new HashMap<IProject, List<IFile>>();

	/* (non-Javadoc)
	 * @see org.eclipse.core.resources.IResourceChangeListener#resourceChanged(org.eclipse.core.resources.IResourceChangeEvent)
	 */
	public void resourceChanged(IResourceChangeEvent event)
	{
		synchronized (AbstractResChangeListener.this)
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
				default:
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
					List<IResourceDelta> affectedFiles = getAffectedFiles(projDelta);

					for (IResourceDelta affectedFile: affectedFiles)
					{
						IResource resource = affectedFile.getResource();

						// filter after IFiles
						if (resource.getType() == IResource.FILE)
						{
							IFile ifile = (IFile) resource;

							List<IFile> list = map.get(project);

							addFilesDependingOnChange(affectedFile, ifile, list);

						}
					}
				}
			}

		}
	}

	private void addFilesDependingOnChange(IResourceDelta affectedFile, IFile ifile, List<IFile> list)
	{
		switch (affectedFile.getKind())
		{
			case IResourceDelta.ADDED:

				if (isApplicable(ifile))
				{
					if (!list.contains(ifile))
					{
						list.add(ifile);

					}
				}
				break;

			case IResourceDelta.CHANGED:

				if (list.contains(ifile))
				{
					// remove old entry
					list.remove(ifile);

				}
				if (isApplicable(ifile))
				{
					// add the new IFile only if
					// applicable
					list.add(ifile);

				}
				break;
			case IResourceDelta.REMOVED:

				if (list.contains(ifile))
				{
					list.remove(ifile);

				}
				break;
			default:
		}
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
	 * Return an array of IFiles that are of interest from the given project
	 * 
	 * @param project
	 * @return an array of IFile, never null
	 */
	protected synchronized IFile[] getIFiles(IProject project)
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
					if (proxy.getType() == IResource.FILE && isApplicable(proxy))
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

	protected abstract boolean isApplicable(IFile iFile);

	protected abstract boolean isApplicable(IResourceProxy proxy);

	/**
	 * Return <code>true</code> if given project exists, is opened and has
	 * Cessar nature
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

}
