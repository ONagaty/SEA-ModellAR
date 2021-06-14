/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidw8762 Mon Feb 24 15:21:19 2014 </copyright>
 */
package eu.cessar.ct.workspace.ui.internal.actions;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Set;

import org.artop.aal.common.metamodel.AutosarReleaseDescriptor;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import eu.cessar.ct.core.mms.MetaModelUtils;
import eu.cessar.ct.core.platform.ui.util.SelectionUtils;
import eu.cessar.ct.core.platform.util.PlatformUtils;
import eu.cessar.ct.workspace.ui.internal.CessarPluginActivator;
import eu.cessar.ct.workspace.ui.internal.Messages;
import eu.cessar.ct.workspace.ui.internal.wizards.ProjectConsistencyCheckWizard;
import eu.cessar.req.Requirement;

/**
 * Action that runs the project consistency check on the AUTOSAR projects under selection and presents the final report
 * in a {@link ProjectConsistencyCheckWizard}.
 * 
 * @author uidw8762
 * 
 *         %created_by: uidl6870 %
 * 
 *         %date_created: Thu Sep 18 11:19:37 2014 %
 * 
 *         %version: 7 %
 * 
 */
@Requirement(
	reqID = "REQ_CHECK#4")
public class StartProjectConsistencyCheckAction implements IObjectActionDelegate
{
	private IStructuredSelection currentSelection;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IActionDelegate#run(org.eclipse.jface.action.IAction)
	 */
	public void run(IAction action)
	{
		runWaitForModelLoading(getProject());
		startWizard(getProject());
	}

	/**
	 * Start wizard.
	 * 
	 * @param project
	 *        the project
	 * @param inconsistencies
	 *        the inconsistencies
	 */
	private static void startWizard(IProject project)
	{
		IWorkbench workbench = PlatformUI.getWorkbench();
		IWorkbenchWindow activeWorkbenchWindow = workbench.getActiveWorkbenchWindow();

		IWizard wizard = new ProjectConsistencyCheckWizard(project);
		WizardDialog wizCheckProjectConsistency = new WizardDialog(activeWorkbenchWindow.getShell(), wizard);

		wizCheckProjectConsistency.create();
		wizCheckProjectConsistency.open();

	}

	/**
	 * Run wait for model loading.
	 * 
	 * @param project
	 *        the project
	 */
	private static void runWaitForModelLoading(final IProject project)
	{
		ProgressMonitorDialog dialog = new ProgressMonitorDialog(getActiveShell());
		try
		{
			dialog.run(true, false, new IRunnableWithProgress()
			{
				public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException
				{
					PlatformUtils.waitForModelLoading(project, monitor);
				}
			});
		}
		catch (InvocationTargetException e)
		{
			CessarPluginActivator.getDefault().logError(e);
		}
		catch (InterruptedException e)
		{
			CessarPluginActivator.getDefault().logError(e);
		}
	}

	private static void handleEmptySelection(IStatus status)
	{
		if (status.isOK())
		{
			MessageDialog.openInformation(getActiveShell(), Messages.projectChecker_title,
				Messages.noFilesFound_UnderSelection);
		}
		else
		{
			IStatus[] children = status.getChildren();
			for (IStatus child: children)
			{
				CessarPluginActivator.getDefault().logError(child.getException());
			}

			MessageDialog.openError(getActiveShell(), Messages.projectChecker_title,
				NLS.bind(Messages.error_accesingResource, new Object[] {children.length}));
		}
	}

	/**
	 * Run collect files from selection.
	 * 
	 * @param filesUnderSelection
	 *        the files under selection
	 * @return the i status
	 */
	private IStatus runCollectFilesFromSelection(final Set<IFile> filesUnderSelection)
	{
		final IStatus[] status = new IStatus[1];

		BusyIndicator.showWhile(Display.getCurrent(), new Runnable()
		{
			public void run()
			{
				IStatus stat = SelectionUtils.collectFilesFromSelection(currentSelection, filesUnderSelection,
					getAcceptedContentTypeIds(getProject()));
				status[0] = stat;
			}
		});

		return status[0];
	}

	/**
	 * Gets the project.
	 * 
	 * @return the project
	 */
	private IProject getProject()
	{
		IResource resource = (IResource) currentSelection.getFirstElement();

		return resource.getProject();
	}

	/**
	 * Gets the active shell.
	 * 
	 * @return the active shell
	 */
	private static Shell getActiveShell()
	{
		return Display.getCurrent().getActiveShell();
	}

	/**
	 * Gets the accepted content type ids.
	 * 
	 * @param project
	 *        the project
	 * @return the accepted content type ids
	 */
	private static List<String> getAcceptedContentTypeIds(IProject project)
	{
		AutosarReleaseDescriptor autosarRelease = MetaModelUtils.getAutosarRelease(project);

		List<String> contentTypeIds = autosarRelease.getContentTypeIds();
		return contentTypeIds;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IActionDelegate#selectionChanged(org.eclipse.jface.action.IAction,
	 * org.eclipse.jface.viewers.ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection)
	{
		currentSelection = (IStructuredSelection) selection;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IObjectActionDelegate#setActivePart(org.eclipse.jface.action.IAction,
	 * org.eclipse.ui.IWorkbenchPart)
	 */
	public void setActivePart(IAction action, IWorkbenchPart targetPart)
	{
		// do nothing
	}

}
