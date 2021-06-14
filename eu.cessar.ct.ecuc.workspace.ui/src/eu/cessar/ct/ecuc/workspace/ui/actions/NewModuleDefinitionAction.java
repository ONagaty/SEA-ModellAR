/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl7321 Jul 13, 2012 3:43:18 PM </copyright>
 */
package eu.cessar.ct.ecuc.workspace.ui.actions;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import eu.cessar.ct.ecuc.workspace.ui.internal.wizards.ModuleDefinitionWizard;

/**
 * Action for opening the wizard for new module definition.
 * 
 */
public class NewModuleDefinitionAction implements IObjectActionDelegate
{

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IActionDelegate#run(org.eclipse.jface.action.IAction)
	 */
	public void run(IAction action)
	{
		IWorkbench workbench = PlatformUI.getWorkbench();
		IWorkbenchWindow activeWorkbenchWindow = workbench.getActiveWorkbenchWindow();
		if (activeWorkbenchWindow != null)
		{
			ModuleDefinitionWizard wizard = new ModuleDefinitionWizard();
			ISelection selection = activeWorkbenchWindow.getSelectionService().getSelection();
			if (!(selection instanceof StructuredSelection))
			{

				selection = StructuredSelection.EMPTY;
			}
			wizard.init(workbench, (IStructuredSelection) selection);

			Shell shell = activeWorkbenchWindow.getShell();
			if (shell != null)
			{
				WizardDialog dialog = new WizardDialog(shell, wizard);
				dialog.create();
				dialog.open();
			}
		}

	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IActionDelegate#selectionChanged(org.eclipse.jface.action.IAction, org.eclipse.jface.viewers.ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection)
	{
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IObjectActionDelegate#setActivePart(org.eclipse.jface.action.IAction, org.eclipse.ui.IWorkbenchPart)
	 */
	public void setActivePart(IAction action, IWorkbenchPart targetPart)
	{
		// TODO Auto-generated method stub

	}

}
