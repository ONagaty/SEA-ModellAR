/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Oct 13, 2009 2:22:20 PM </copyright>
 */
package eu.cessar.ct.runtime.ecuc.internal;

import java.util.HashMap;
import java.util.Map;

import org.artop.aal.workspace.natures.AutosarNature;
import org.eclipse.core.commands.common.EventManager;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.sphinx.emf.util.EcorePlatformUtil;

import eu.cessar.ct.core.platform.PlatformConstants;
import eu.cessar.ct.runtime.CessarRuntime;
import eu.cessar.ct.runtime.ecuc.IEcucCore;
import eu.cessar.ct.runtime.ecuc.IEcucModel;
import eu.cessar.ct.runtime.ecuc.IEcucPresentationModel;
import eu.cessar.ct.runtime.ecuc.IModelListener;
import eu.cessar.ct.runtime.ecuc.internal.model.EcucModelImpl;
import eu.cessar.ct.runtime.ecuc.internal.pm.EcucPresentationModelImpl;

/**
 * 
 */
public class EcucCoreImpl extends EventManager implements IEcucCore
{

	private Map<IProject, EcucModelEntry> ecucModels = new HashMap<IProject, EcucModelEntry>();

	private class EcucModelEntry
	{
		private IEcucModel ecucModel;

		private IEcucPresentationModel ecucJavaModel;

	}

	public EcucCoreImpl()
	{
		// do nothing
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.runtime.ecuc.IEcucCore#addModelListener(eu.cessar.ct.runtime.ecuc.IModelListener)
	 */
	public void addModelListener(IModelListener listener)
	{
		addListenerObject(listener);
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.runtime.ecuc.IEcucCore#removeModelListener(eu.cessar.ct.runtime.ecuc.IModelListener)
	 */
	public void removeModelListener(IModelListener listener)
	{
		removeListenerObject(listener);
	}

	private void notifyModelChanged(IProject project)
	{
		Object[] listeners = getListeners();
		for (Object object: listeners)
		{
			IModelListener listener = (IModelListener) object;
			try
			{
				listener.modelChanged(project);
			}
			catch (Throwable t)
			{
				CessarPluginActivator.getDefault().logError(t);
			}
		}
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.runtime.ecuc.IEcucCore#isValidEcucProject(org.eclipse.core.resources.IProject)
	 */
	public boolean isValidEcucProject(IProject project)
	{
		boolean valid = project != null && project.isAccessible();
		try
		{
			valid = valid && project.hasNature(PlatformConstants.CESSAR_NATURE);
			valid = valid && project.hasNature(CessarRuntime.JAVA_NATURE);
			valid = valid && project.hasNature(AutosarNature.ID);
			return valid;
		}
		catch (CoreException e)
		{
			// consider that is invalid
			CessarPluginActivator.getDefault().logError(e);
			return false;
		}
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.runtime.ecuc.IEcucCore#getEcucJavaModel(org.eclipse.core.resources.IProject)
	 */
	public IEcucPresentationModel getEcucPresentationModel(IProject project)
	{
		EcucModelEntry modelEntry = getModelEntry(project);
		if (modelEntry == null)
		{
			return null;
		}
		else
		{
			return modelEntry.ecucJavaModel;
		}
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.runtime.ecuc.IEcucCore#getEcucPresentationModel(org.eclipse.emf.ecore.EObject)
	 */
	public IEcucPresentationModel getEcucPresentationModel(EObject object)
	{
		IFile file = EcorePlatformUtil.getFile(object);
		if (file != null)
		{
			return getEcucPresentationModel(file.getProject());
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.runtime.ecuc.IEcucCore#getEcucPresentationModel(java.lang.String)
	 */
	public IEcucPresentationModel getEcucPresentationModel(String projectName)
	{
		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
		return getEcucPresentationModel(project);
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.runtime.ecuc.IEcucCore#getEcucModel(org.eclipse.core.resources.IProject)
	 */
	public IEcucModel getEcucModel(IProject project)
	{
		EcucModelEntry modelEntry = getModelEntry(project);
		if (modelEntry == null)
		{
			return null;
		}
		else
		{
			return modelEntry.ecucModel;
		}
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.runtime.ecuc.IEcucCore#getEcucModel(org.eclipse.emf.ecore.EObject)
	 */
	public IEcucModel getEcucModel(EObject object)
	{
		IFile file = EcorePlatformUtil.getFile(object);
		if (file != null)
		{
			return getEcucModel(file.getProject());
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.runtime.ecuc.IEcucCore#getEcucModel(java.lang.String)
	 */
	public IEcucModel getEcucModel(String projectName)
	{
		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
		return getEcucModel(project);
	}

	/**
	 * Get the model entry for a particular project. If there is none one will
	 * be created
	 * 
	 * @param project
	 * @return
	 */
	private EcucModelEntry getModelEntry(IProject project)
	{
		synchronized (ecucModels)
		{
			EcucModelEntry modelEntry = ecucModels.get(project);
			if (modelEntry != null)
			{
				return modelEntry;
			}
			else
			{
				// create a new one if possible
				if (isValidEcucProject(project))
				{
					modelEntry = new EcucModelEntry();
					modelEntry.ecucModel = new EcucModelImpl(project);
					modelEntry.ecucJavaModel = new EcucPresentationModelImpl(project);
					ecucModels.put(project, modelEntry);
					return modelEntry;
				}
				else
				{
					return null;
				}
			}
		}
	}

	/**
	 * Trigger that the Ecuc model of the attached projects has been changed
	 * 
	 * @param projects
	 */
	public void modelChanged(IProject[] projects)
	{
		for (IProject project: projects)
		{
			modelChanged(project);
		}
	}

	/**
	 * Signal that the Ecuc model of the <code>project</code> argument has been
	 * changed
	 * 
	 * @param project
	 */
	public void modelChanged(IProject project)
	{
		EcucModelEntry modelEntry = ecucModels.get(project);
		if (modelEntry != null)
		{
			if (modelEntry.ecucModel != null)
			{
				modelEntry.ecucModel.modelChanged();
			}
			if (modelEntry.ecucJavaModel != null)
			{
				modelEntry.ecucJavaModel.modelChanged();
			}
		}
		notifyModelChanged(project);
	}

	/**
	 * Called when a project is closed to clear the model
	 * 
	 * @param project
	 */
	public void projectClosed(IProject project)
	{
		// clear the model first
		modelChanged(project);
		ecucModels.remove(project);
	}
}
