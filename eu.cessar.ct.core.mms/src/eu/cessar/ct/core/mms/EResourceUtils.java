/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidl6458 Jun 16, 2010 5:17:30 PM </copyright>
 */
package eu.cessar.ct.core.mms;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.IOperationHistory;
import org.eclipse.core.commands.operations.IUndoableOperation;
import org.eclipse.core.internal.events.NotificationManager;
import org.eclipse.core.internal.resources.Workspace;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.emf.common.command.BasicCommandStack;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.workspace.AbstractEMFOperation;
import org.eclipse.osgi.util.NLS;
import org.eclipse.sphinx.emf.Activator;
import org.eclipse.sphinx.emf.saving.SaveIndicatorUtil;
import org.eclipse.sphinx.emf.util.EcorePlatformUtil;
import org.eclipse.sphinx.emf.util.EcoreResourceUtil;
import org.eclipse.sphinx.emf.util.WorkspaceEditingDomainUtil;
import org.eclipse.sphinx.emf.util.WorkspaceTransactionUtil;
import org.eclipse.sphinx.emf.workspace.saving.ModelSaveManager;
import org.eclipse.sphinx.platform.util.StatusUtil;

import eu.cessar.ct.core.mms.internal.Messages;
import eu.cessar.ct.core.platform.util.PlatformUtils;
import eu.cessar.ct.sdk.runtime.CessarSaveException;
import eu.cessar.req.Requirement;

/**
 * This class contains static methods that deal with AUTOSAR resources
 *
 * @Review uidl6458 - 29.03.2012
 */
public final class EResourceUtils
{
	/**
	 * @author uidu3379
	 *
	 */
	private static final class SaveResourceEMFOperation extends AbstractEMFOperation
	{
		private final Map<?, ?> saveOptions;
		private final Collection<Resource> resources;
		private final TransactionalEditingDomain editingDomain;

		private SaveResourceEMFOperation(TransactionalEditingDomain domain, String label, Map<?, ?> options,
			Map<?, ?> saveOptions, Collection<Resource> resources, TransactionalEditingDomain editingDomain)
		{
			super(domain, label, options);
			this.saveOptions = saveOptions;
			this.resources = resources;
			this.editingDomain = editingDomain;
		}

		@Override
		protected IStatus doExecute(IProgressMonitor monitor, IAdaptable info) throws ExecutionException
		{
			IWorkspaceRunnable runnable = new SaveResourceWorkspaceRunnable(resources, editingDomain, saveOptions);
			try
			{
				// Execute save operation as IWorkspaceRunnable on workspace
				// in order to avoid resource
				// change notifications during transaction execution
				/*
				 * !! Important Note !! Only set IWorkspace.AVOID_UPDATE flag but don't define any scheduling
				 * restrictions for the save operation right here (this must only be done on outer workspace jobs or
				 * workspace runnables from which this method is called). Otherwise it would be likely to end up in
				 * deadlocks with operations which already have acquired exclusive access to the workspace but are
				 * waiting for exclusive access to the model (i.e. for the transaction).
				 */
				ResourcesPlugin.getWorkspace().run(runnable, null, 0, monitor);
				return Status.OK_STATUS;
			}
			catch (CoreException ex)
			{
				return ex.getStatus();
			}
		}

		@Override
		public boolean canUndo()
		{
			// Avoid that save operation appears in the undo menu
			return false;
		}
	}

	/**
	 * @author uidu3379
	 *
	 */
	private static final class SaveResourceWorkspaceRunnable implements IWorkspaceRunnable
	{
		private final Collection<Resource> resources;
		private final TransactionalEditingDomain editingDomain;
		private final Map<?, ?> saveOptions;

		private SaveResourceWorkspaceRunnable(Collection<Resource> resources, TransactionalEditingDomain editingDomain,
			Map<?, ?> saveOptions)
		{
			this.resources = resources;
			this.editingDomain = editingDomain;
			this.saveOptions = saveOptions;
		}

		public void run(IProgressMonitor monitor) throws CoreException
		{
			final SubMonitor progress = SubMonitor.convert(monitor, resources.size());
			for (Resource resource: resources)
			{
				progress.subTask(NLS.bind(Messages.subtask_savingResource, resource.getURI().toString()));

				// Save resource
				/*
				 * !! Important Note !! Resource must be saved before marking it as freshly saved because otherwise the
				 * resource would loose its dirty state and consequently not be saved at all.
				 */
				EcoreResourceUtil.saveModelResource(resource, saveOptions);

				// Mark resource as freshly saved in order to avoid
				// that it gets automatically reloaded
				SaveIndicatorUtil.setSaved(editingDomain, resource);

				progress.worked(1);
			}

			// Refresh command stack state of associated editing
			// domain
			((BasicCommandStack) editingDomain.getCommandStack()).saveIsDone();
		}
	}

	private EResourceUtils()
	{
		// do nothing
	}

	/**
	 * Return all AUTOSAR-like files a project have
	 *
	 * @param project
	 *        the AUTOSAR project
	 * @return a collection of files, never null;
	 */
	public static Collection<IFile> getProjectFiles(IProject project)
	{
		Collection<Resource> resources = getProjectResources(project);
		if (resources.isEmpty())
		{
			return Collections.emptyList();
		}
		Collection<IFile> result = new ArrayList<>();
		for (Resource resource: resources)
		{
			IFile file = EcorePlatformUtil.getFile(resource);
			result.add(file);
		}
		return result;
	}

	/**
	 * Return a list with all resources within the given project
	 *
	 * @param project
	 * @return list, never null
	 */
	public static Collection<Resource> getProjectResources(IProject project)
	{
		// check to see if the project have some editing domains available
		if (MetaModelUtils.haveEditingDomain(project))
		{

			List<Resource> toSortList = new ArrayList<>(EcorePlatformUtil.getResourcesInModels(project,
				MetaModelUtils.getAutosarRelease(project), false));

			Collections.sort(toSortList, ResourcesComparator.INSTANCE);

			return toSortList;
		}
		else
		{
			return Collections.emptyList();
		}
	}

	/**
	 * Returns the resource from the specified <code>project</code> whose name (i.e. last segment of the resource's URI)
	 * matches the given one <code>name</code> <br>
	 * NOTE: if several such resources exist, the first one found will be returned
	 *
	 * @param project
	 *        project where to search
	 * @param name
	 *        last segment of the resource's URI
	 * @return the resource
	 */
	public static Resource getResourceWithName(IProject project, String name)
	{
		Resource foundResource = null;
		for (Resource resource: EResourceUtils.getProjectResources(project))
		{
			if (resource.getURI().lastSegment().endsWith(name))
			{
				foundResource = resource;
				break;
			}
		}
		return foundResource;
	}

	/**
	 * Return all resources from a project that are dirty.
	 *
	 * @param project
	 * @return
	 */
	public static Collection<Resource> getDirtyResources(IProject project)
	{
		TransactionalEditingDomain domain = MetaModelUtils.getEditingDomain(project);
		Collection<Resource> dirtyResources = SaveIndicatorUtil.getResourceSaveIndicator(domain).getDirtyResources();

		List<Resource> result = new ArrayList<>();
		for (Resource resource: dirtyResources)
		{
			IProject resourceProject = MetaModelUtils.getProject(resource);

			if (resource.getResourceSet() != null && resourceProject == project)
			{
				result.add(resource);
			}
		}
		return result;
	}

	/**
	 * Save all <code>resources</code>. If <code>useWorkspaceRunnable</code> is true then an {@link IWorkspaceRunnable}
	 * will be used for saving with the project as lock.
	 *
	 * @param resources
	 * @param monitor
	 * @param useWorkspaceRunnable
	 *        true if {@link IWorkspaceRunnable} shall be used, false otherwise
	 */
	public static void saveResources(final Collection<Resource> resources, IProgressMonitor monitor,
		boolean useWorkspaceRunnable) throws CoreException
	{
		if (resources.size() > 0)
		{
			if (monitor == null)
			{
				monitor = new NullProgressMonitor(); // SUPPRESS CHECKSTYLE OK
			}
			monitor.beginTask("Saving resources...", resources.size()); //$NON-NLS-1$

			try
			{
				avoidNotifications(true);

				final Resource resource = (Resource) resources.toArray()[0];
				IProject project = MetaModelUtils.getProject(resource);

				final TransactionalEditingDomain editingDomain = WorkspaceEditingDomainUtil.getEditingDomain(resource);

				if (useWorkspaceRunnable)
				{
					IWorkspaceRunnable runnable = new IWorkspaceRunnable()
					{
						public void run(IProgressMonitor monitor) throws CoreException
						{

							EResourceUtils.runSave(resources, editingDomain, EcoreResourceUtil.getDefaultSaveOptions(),
								monitor);

							ModelSaveManager.INSTANCE.handleDirtyStateChanged(resource);

						}
					};
					ResourcesPlugin.getWorkspace().run(runnable, project, 0, monitor);

				}
				else
				{
					EResourceUtils.runSave(resources, editingDomain, EcoreResourceUtil.getDefaultSaveOptions(), monitor);

					ModelSaveManager.INSTANCE.handleDirtyStateChanged(resource);
				}

			}
			finally
			{
				avoidNotifications(false);
				monitor.done();
			}
		}

	}

	/**
	 * Adds the calling thread to the list of threads that are currently avoiding notifications
	 *
	 * @param avoid
	 *        whether notifications should be avoided for the calling thread
	 */
	private static void avoidNotifications(boolean avoid)
	{
		Workspace workspace = (Workspace) ResourcesPlugin.getWorkspace();
		NotificationManager notificationManager = workspace.getNotificationManager();
		if (avoid)
		{
			notificationManager.beginAvoidNotify();
		}
		else
		{
			notificationManager.endAvoidNotify();
		}

	}

	/**
	 * Attempt to retrieve the cause of the resource saving error.
	 *
	 * @param status
	 *        The error status.
	 * @return The message representing a cause of the save error (might not be the root/only cause).
	 */
	private static String getSaveErrorCause(IStatus status)
	{
		String cause = ""; //$NON-NLS-1$
		// Search for the first child status that indicates an ERROR,
		// if none found, settle for the first child status.
		IStatus[] children = status.getChildren();
		if (children.length > 0)
		{
			IStatus firstError = children[0];
			for (IStatus child: children)
			{
				if (IStatus.ERROR == child.getCode())
				{
					firstError = child;
					break;
				}
			}
			// This should come from Sphinx.
			cause = ", because: " + firstError.getMessage(); //$NON-NLS-1$
		}
		return cause;
	}

	/**
	 * Save resources, reporting unsaved ones together with the reason via a runtime {@code CessarSaveException}.
	 *
	 * @param resources
	 *        The resources to save.
	 * @param editingDomain
	 *        The editing domain.
	 * @param saveOptions
	 *        The save options.
	 * @param monitor
	 *        The progress monitor.
	 */
	@Requirement(
		reqID = "REQ_PLUGET_READONLY#1")
	private static void runSave(final Collection<Resource> resources, final TransactionalEditingDomain editingDomain,
		final Map<?, ?> saveOptions, final IProgressMonitor monitor)
	{

		final IUndoableOperation operation = new SaveResourceEMFOperation(editingDomain,
			"Saving resources", WorkspaceTransactionUtil.getDefaultSaveTransactionOptions(), //$NON-NLS-1$
			saveOptions, resources, editingDomain);

		IOperationHistory history = WorkspaceTransactionUtil.getOperationHistory(editingDomain);

		// Save timestamps of resources before attempting to save.
		Map<Resource, Long> timeStamps = new HashMap<>();
		for (Resource rc: resources)
		{
			timeStamps.put(rc, rc.getTimeStamp());
		}

		// Attempt save.
		try
		{
			history.execute(operation, monitor, null);
		}
		catch (ExecutionException ex)
		{
			IStatus status = StatusUtil.createErrorStatus(Activator.getPlugin(), ex);

			StringBuilder sb = new StringBuilder();
			int i = 0;

			// Compare current and saved timestamps
			for (Resource rc: resources)
			{
				// if unchanged, resource was not saved, so add it to the exception's message.
				if (rc.getTimeStamp() == timeStamps.get(rc))
				{
					// place comma starting with the second element
					sb.append(((i > 0) ? ", " : "") + rc.getURI().lastSegment()); //$NON-NLS-1$ //$NON-NLS-2$
					i++;
				}
			}

			String message = "Error saving resources: " + resources; //$NON-NLS-1$
			if (0 != sb.length())
			{
				String notSaved = "Unable to save the following resource(s): "; //$NON-NLS-1$
				message = notSaved + sb.toString() + getSaveErrorCause(status);
			}

			IStatus multi = StatusUtil.createMultiErrorStatus(Activator.getPlugin(), status.getCode(), message,
				Collections.EMPTY_LIST, ex);
			multi = PlatformUtils.combineStatus(multi, status);

			// This can be caught from user code (i.e. plugets).
			throw new CessarSaveException(multi.getMessage());
		}
	}
}
