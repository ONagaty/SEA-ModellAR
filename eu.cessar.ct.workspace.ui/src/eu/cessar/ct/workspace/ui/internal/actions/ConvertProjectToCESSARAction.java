/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 *
 * File created by uid95513<br/>
 * Feb 5, 2015 11:09:11 AM
 *
 * </copyright>
 */
package eu.cessar.ct.workspace.ui.internal.actions;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import eu.cessar.ct.workspace.ui.internal.wizards.ProjectConversionWizard;

/**
 * Action for opening the wizard for project conversion to CESSAR-CT.
 *
 * @author uid95513
 *
 *         %created_by: uid95513 %
 *
 *         %date_created: Mon Feb 9 18:08:30 2015 %
 *
 *         %version: 1 %
 */
public class ConvertProjectToCESSARAction implements IObjectActionDelegate
{
	private IProject selectedProject;
	private ISelection activeselection;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IActionDelegate#run(org.eclipse.jface.action.IAction)
	 */
	public void run(IAction action)
	{
		if (activeselection instanceof StructuredSelection)
		{
			Object firstElement = ((StructuredSelection) activeselection).getFirstElement();
			if (firstElement instanceof IProject)
			{
				selectedProject = (IProject) firstElement;
			}
		}

		if (selectedProject == null)
		{
			return;
		}

		IWorkbench workbench = PlatformUI.getWorkbench();
		IWorkbenchWindow activeWorkbenchWindow = workbench.getActiveWorkbenchWindow();
		if (activeWorkbenchWindow != null)
		{
			ProjectConversionWizard wizard = new ProjectConversionWizard(selectedProject);

			Shell shell = activeWorkbenchWindow.getShell();
			if (shell != null)
			{
				WizardDialog dialog = new WizardDialog(shell, wizard);
				dialog.create();
				dialog.open();
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IActionDelegate#selectionChanged(org.eclipse.jface.action.IAction,
	 * org.eclipse.jface.viewers.ISelection)
	 */
	@Override
	public void selectionChanged(IAction action, ISelection selection)
	{
		activeselection = selection;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IObjectActionDelegate#setActivePart(org.eclipse.jface.action.IAction,
	 * org.eclipse.ui.IWorkbenchPart)
	 */
	@Override
	public void setActivePart(IAction action, IWorkbenchPart targetPart)
	{
		// no action
	}

}
