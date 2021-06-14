/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidu8153<br/>
 * Nov 11, 2014 6:14:01 AM
 *
 * </copyright>
 */
package eu.cessar.ct.workspace.ui.internal;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourceAttributes;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.transaction.NotificationFilter;
import org.eclipse.emf.transaction.ResourceSetChangeEvent;
import org.eclipse.emf.transaction.ResourceSetListenerImpl;
import org.eclipse.emf.transaction.RollbackException;
import org.eclipse.jface.dialogs.MessageDialogWithToggle;
import org.eclipse.jface.preference.PreferenceStore;
import org.eclipse.jface.window.Window;
import org.eclipse.sphinx.emf.util.EcorePlatformUtil;
import org.eclipse.sphinx.emf.util.EcoreResourceUtil;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;

import eu.cessar.ct.edit.ui.EditingPreferencesAccessor;
import eu.cessar.ct.sdk.utils.ModelUtils;

/**
 * Entity interested in being notified of read-write events before they occur (pre-commit events) on the AUTOSAR model.
 * The purpose is to enable automatic change of the read-only attribute for the affected read-only files.
 *
 * @author uidu8153
 *
 *         %created_by: uidl6870 %
 *
 *         %date_created: Mon Feb 16 12:10:44 2015 %
 *
 *         %version: TAUTOSAR~7.1.2 %
 */
public final class ModelResourceListener extends ResourceSetListenerImpl
{

	/**
	 * the singleton
	 */
	public static final ModelResourceListener eINSTANCE = new ModelResourceListener();

	private MessageDialogWithToggle confirmationDialog;

	private ModelResourceListener()
	{
		super(NotificationFilter.createEventTypeFilter(Notification.ADD_MANY).or(
			NotificationFilter.createEventTypeFilter(Notification.REMOVE_MANY).or(
				NotificationFilter.createEventTypeFilter(Notification.ADD).or(
					NotificationFilter.createEventTypeFilter(Notification.REMOVE).or(
						NotificationFilter.createEventTypeFilter(Notification.SET).or(
							NotificationFilter.createEventTypeFilter(Notification.UNSET)))))));
	}

	/**
	 * @param resource
	 * @return
	 */
	@SuppressWarnings("static-method")
	private boolean checkIfResourceReadOnly(Resource resource)
	{
		boolean isReadOnly = false;

		if (resource != null && EcoreResourceUtil.isReadOnly(resource.getURI()))
		{
			isReadOnly = true;
		}
		return isReadOnly;
	}

	/**
	 * @param allResources
	 */
	@SuppressWarnings("static-method")
	private void setResourceToReadWriteAndRefreshResource(Set<Resource> allResources)
	{

		ResourceAttributes resourceAttributes;
		try
		{
			for (Resource eResource: allResources)
			{
				IFile definingFile = ModelUtils.getDefiningFile(eResource);

				resourceAttributes = definingFile.getResourceAttributes();
				resourceAttributes.setReadOnly(false);

				definingFile.setResourceAttributes(resourceAttributes);

				definingFile.refreshLocal(IResource.DEPTH_INFINITE, new NullProgressMonitor());

			}
		}
		catch (CoreException e)
		{
			CessarPluginActivator.getDefault().logError(e);
		}
	}

	/**
	 * @param allResources
	 */
	@SuppressWarnings("static-method")
	private void setResourceToReadWriteAndRefreshProject(Set<Resource> allResources)
	{

		ResourceAttributes resourceAttributes;
		try
		{
			for (Resource eResource: allResources)
			{
				IFile definingFile = EcorePlatformUtil.getFile(eResource);

				resourceAttributes = definingFile.getResourceAttributes();
				resourceAttributes.setReadOnly(false);

				definingFile.setResourceAttributes(resourceAttributes);
				final IProject project = definingFile.getProject();
				if (project != null)
				{

					project.refreshLocal(IResource.DEPTH_INFINITE, new NullProgressMonitor());

				}

			}
		}
		catch (CoreException e)
		{
			CessarPluginActivator.getDefault().logError(e);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.emf.transaction.ResourceSetListenerImpl#isPrecommitOnly()
	 */
	@Override
	public boolean isPrecommitOnly()
	{
		return true;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.emf.transaction.ResourceSetListenerImpl#transactionAboutToCommit(org.eclipse.emf.transaction.
	 * ResourceSetChangeEvent)
	 */
	@Override
	public Command transactionAboutToCommit(ResourceSetChangeEvent event) throws RollbackException
	{
		boolean checkIfResourceRO = false;
		boolean isPopUpShown = false;
		boolean allowAllReadOnlyFilesToBeModified = false;
		EObject object = null;
		final Set<Resource> readOnlyResources = new HashSet<Resource>();

		List<Resource> processedResources = new ArrayList<Resource>();
		for (Notification notification: event.getNotifications())
		{
			if (notification.getNotifier() instanceof EObject)
			{
				object = (EObject) notification.getNotifier();

				Resource eResource = object.eResource();
				if (!processedResources.contains(eResource))
				{
					checkIfResourceRO = checkIfResourceReadOnly(eResource);

					// Allows resource which is read only.
					if (eResource != null && checkIfResourceRO)
					{
						readOnlyResources.add(eResource);
					}

					processedResources.add(eResource);
				}
			}
		}
		// check if cessar preference has "Show Message popup" checked.
		isPopUpShown = EditingPreferencesAccessor.getReadOnlyToReadWriteMessageDialogStatus();
		allowAllReadOnlyFilesToBeModified = EditingPreferencesAccessor.getAllowToWriteInsideReadOnlyFiles();

		boolean projectHasReadOnlyElements = readOnlyResources.isEmpty();

		if (!isPopUpShown && !projectHasReadOnlyElements)
		{

			String errorMessage;
			if (PlatformUI.isWorkbenchRunning())
			{
				handleROfilesWhenWorkbenchRunning(readOnlyResources);
			}
			else
			{
				errorMessage = createErrorMessage(readOnlyResources);

				throw new RollbackException(new Status(IStatus.ERROR, CessarPluginActivator.PLUGIN_ID, errorMessage));

			}

		}
		else if (isPopUpShown && allowAllReadOnlyFilesToBeModified)
		{
			setResourceToReadWriteAndRefreshProject(readOnlyResources);
		}

		return null;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.emf.transaction.ResourceSetListenerImpl#resourceSetChanged(org.eclipse.emf.transaction.
	 * ResourceSetChangeEvent)
	 */
	@Override
	public void resourceSetChanged(ResourceSetChangeEvent event)
	{
		// TODO Auto-generated method stub
		super.resourceSetChanged(event);
	}

	/**
	 * @param readOnlyResources
	 * @throws RollbackException
	 */
	private void handleROfilesWhenWorkbenchRunning(final Set<Resource> readOnlyResources) throws RollbackException
	{
		String errorMessage;
		Display.getDefault().syncExec(new Runnable()
		{
			public void run()
			{
				PreferenceStore store = new PreferenceStore();
				StringBuilder builder = new StringBuilder();
				for (Resource resources: readOnlyResources)
				{
					IFile definingFile = EcorePlatformUtil.getFile(resources);

					String resourceName = definingFile.getName();
					builder.append(resourceName).append("\n"); //$NON-NLS-1$

				}

				confirmationDialog = MessageDialogWithToggle.openOkCancelConfirm(
					null,
					Messages.ModelResourceListener_TITLE,
					"Do you want to change the following read only file(s) to read write file(s)? \n\n" + builder.toString(), //$NON-NLS-1$
					Messages.ModelResourceListener_MESSAGE, false, store, ""); //$NON-NLS-1$
			}
		});

		boolean toggle = confirmationDialog.getToggleState();

		if (toggle)
		{
			EditingPreferencesAccessor.setReadOnlyToReadWriteMessageDialogStatus(true);
		}
		int confirmResult = confirmationDialog.getReturnCode();

		if (confirmResult == Window.OK)
		{

			if (toggle)
			{
				EditingPreferencesAccessor.setAllowToWriteInsideReadOnlyFiles(true);
				EditingPreferencesAccessor.setDoNotAllowToWriteInsideReadOnlyFiles(false);

			}

			setResourceToReadWriteAndRefreshResource(readOnlyResources);
		}
		else if (confirmResult == Window.CANCEL)
		{
			if (toggle)
			{
				EditingPreferencesAccessor.setAllowToWriteInsideReadOnlyFiles(false);
				EditingPreferencesAccessor.setDoNotAllowToWriteInsideReadOnlyFiles(true);
			}
			errorMessage = createErrorMessage(readOnlyResources);
			throw new RollbackException(new Status(IStatus.ERROR, CessarPluginActivator.PLUGIN_ID, errorMessage));

		}
	}

	/**
	 * Looks at all the read-only files and create an appropriate error message
	 *
	 * @param allResources
	 */
	private static String createErrorMessage(Set<Resource> allResources)
	{

		List<String> lastSegmentsList = new ArrayList<String>();
		String errorMessage = ""; //$NON-NLS-1$
		for (Resource resource: allResources)
		{
			URI uri = resource.getURI();
			List<String> segmentsList = uri.segmentsList();
			int segmentsListSize = segmentsList.size();
			String string = segmentsList.get(segmentsListSize - 1);
			lastSegmentsList.add(string);
		}
		if (lastSegmentsList.size() > 1)
		{
			errorMessage = "The files " + lastSegmentsList.toString() + " are read-only and can not be modified"; //$NON-NLS-1$ //$NON-NLS-2$
		}
		else
		{
			errorMessage = errorMessage + "The file " + lastSegmentsList.toString() //$NON-NLS-1$
				+ " is read-only and can not be modified"; //$NON-NLS-1$
		}
		return errorMessage;
	}
}
