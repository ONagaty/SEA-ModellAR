/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidg4020<br/>
 * Jun 6, 2014 2:51:42 PM
 * 
 * </copyright>
 */
package eu.cessar.ct.workspace.ui.internal.decorator;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.ProjectScope;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.IEclipsePreferences.IPreferenceChangeListener;
import org.eclipse.core.runtime.preferences.IEclipsePreferences.PreferenceChangeEvent;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.BaseLabelProvider;
import org.eclipse.jface.viewers.IDecoration;
import org.eclipse.jface.viewers.ILightweightLabelDecorator;
import org.eclipse.sphinx.platform.IExtendedPlatformConstants;
import org.eclipse.sphinx.platform.messages.PlatformMessages;
import org.eclipse.ui.IDecoratorManager;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.progress.UIJob;

import eu.cessar.ct.core.platform.CESSARPreferencesAccessor;
import eu.cessar.ct.core.platform.EProjectVariant;
import eu.cessar.ct.workspace.ui.internal.CessarPluginActivator;
import eu.cessar.ct.workspace.ui.internal.WorkspaceUIConstants;

/**
 * Decorates the project based on the project phase:DEVELOPMENT or PRODUCTION.
 * 
 * @author uidg4020
 * 
 *         %created_by: uidg4020 %
 * 
 *         %date_created: Tue Jun 10 10:10:23 2014 %
 * 
 *         %version: 1 %
 */
public class ProjectPhaseDecorator extends BaseLabelProvider implements ILightweightLabelDecorator
{
	Map<IProject, IPreferenceChangeListener> mapProjectToPreferenceListener = Collections.synchronizedMap(new HashMap<IProject, IPreferenceChangeListener>());
	Map<IProject, IResourceChangeListener> mapProjectToResourceListener = Collections.synchronizedMap(new HashMap<IProject, IResourceChangeListener>());

	/**
	 * Listens when the project phase has changed
	 * 
	 * @author uidg4020
	 * 
	 *         %created_by: uidg4020 %
	 * 
	 *         %date_created: Tue Jun 10 10:10:23 2014 %
	 * 
	 *         %version: 1 %
	 */
	public class ProjectPhaseChangeListener implements IPreferenceChangeListener
	{

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.core.runtime.preferences.IEclipsePreferences.IPreferenceChangeListener#preferenceChange(org.eclipse
		 * .core.runtime.preferences.IEclipsePreferences.PreferenceChangeEvent)
		 */
		@Override
		public void preferenceChange(PreferenceChangeEvent event)
		{
			String key = event.getKey();
			if (key.equals(CESSARPreferencesAccessor.KEY_CONFIGURATION_VARIANT))
			{

				UIJob job = new UIJob(PlatformMessages.job_updatingLabelDecoration)
				{
					@Override
					public IStatus runInUIThread(IProgressMonitor monitor)
					{
						updateLabelDecorator();
						return Status.OK_STATUS;
					}

					@Override
					public boolean belongsTo(Object family)
					{
						return IExtendedPlatformConstants.FAMILY_LABEL_DECORATION.equals(family);
					}
				};
				/*
				 * !! Important Note !! Schedule updating label decoration job only if no such is already underway
				 * because running multiple label decoration updates concurrently causes deadlocks.
				 */
				if (Job.getJobManager().find(IExtendedPlatformConstants.FAMILY_LABEL_DECORATION).length == 0)
				{
					job.setPriority(Job.BUILD);
					job.setSystem(true);
					job.schedule();
				}

			}
		}

		private void updateLabelDecorator()
		{
			IDecoratorManager decoratorManager = PlatformUI.getWorkbench().getDecoratorManager();
			decoratorManager.update("eu.cessar.ct.workspace.ui.ProjectPhaseDecorator"); //$NON-NLS-1$}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.ILightweightLabelDecorator#decorate(java.lang.Object,
	 * org.eclipse.jface.viewers.IDecoration)
	 */
	@Override
	public void decorate(Object element, IDecoration decoration)
	{
		ImageDescriptor creationPhaseDescr = CessarPluginActivator.getDefault().getImageRegistry().getDescriptor(
			WorkspaceUIConstants.IMAGE_ID_PROJECT_PHASE_DECO);
		if (element instanceof IProject)
		{
			IProject project = (IProject) element;
			assurePreferencesListenerInstalled(project);
			assureResourceListenerInstalled(project);

			EProjectVariant projectVariant = CESSARPreferencesAccessor.getProjectVariant(project);
			if (EProjectVariant.PRODUCTION == projectVariant)
			{
				decoration.addOverlay(creationPhaseDescr, IDecoration.BOTTOM_RIGHT);
			}
		}

	}

	/**
	 * Checks if the project has an instance that implements {@link IPreferenceChangeListener} associated in the
	 * {@link #mapProjectToPreferenceListener}. If it does not have, it creates one and adds it to project preferences
	 * listeners and to the map.
	 * 
	 * @param project
	 */
	private void assurePreferencesListenerInstalled(IProject project)
	{
		synchronized (mapProjectToPreferenceListener)
		{

			IPreferenceChangeListener iPreferenceChangeListener = mapProjectToPreferenceListener.get(project);
			if (iPreferenceChangeListener == null)
			{
				// create the listener and install it
				ProjectScope projectScope = new ProjectScope(project);
				IEclipsePreferences projectPreferences = projectScope.getNode("eu.cessar.ct.core.platform"); //$NON-NLS-1$
				iPreferenceChangeListener = new ProjectPhaseChangeListener();
				projectPreferences.addPreferenceChangeListener(iPreferenceChangeListener);
				mapProjectToPreferenceListener.put(project, iPreferenceChangeListener);

			}

		}
	}

	/**
	 * Checks if the project has an instance that implements {@link IResourceChangeListener} associated in the
	 * {@link #mapProjectToResourceListener}. If it does not have, it creates a resource listener to be called when the
	 * project is closed and remove the preference listener and remove the key from
	 * {@link #mapProjectToPreferenceListener}
	 * 
	 * @param project
	 */
	private void assureResourceListenerInstalled(final IProject project)
	{
		IResourceChangeListener iResourceChangeListener;
		synchronized (mapProjectToResourceListener)
		{
			iResourceChangeListener = mapProjectToResourceListener.get(project);
		}
		if (iResourceChangeListener == null)
		{
			// create resource listener
			iResourceChangeListener = new IResourceChangeListener()
			{

				@Override
				public void resourceChanged(IResourceChangeEvent event)
				{
					IResource resource = event.getResource();
					final IProject resourceProject = resource.getProject();
					if (project != resourceProject)
					{
						return;
					}
					// remove project from preferences and from map
					ProjectScope projectScope = new ProjectScope(project);
					IEclipsePreferences projectPreferences = projectScope.getNode(CessarPluginActivator.PLUGIN_ID);
					if (projectPreferences != null)
					{
						projectPreferences.remove(CESSARPreferencesAccessor.KEY_CONFIGURATION_VARIANT);
						synchronized (mapProjectToPreferenceListener)
						{
							mapProjectToPreferenceListener.remove(project);
						}
					}
				}
			};
			synchronized (mapProjectToResourceListener)
			{
				mapProjectToResourceListener.put(project, iResourceChangeListener);
			}
			// add a listener for the close and delete events
			ResourcesPlugin.getWorkspace().addResourceChangeListener(iResourceChangeListener,
				IResourceChangeEvent.PRE_CLOSE | IResourceChangeEvent.PRE_DELETE);
		}

	}

}
