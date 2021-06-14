/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidt2045 Jul 24, 2012 11:48:34 AM </copyright>
 */
package eu.cessar.ct.workspace.ui.internal.actions;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;

import eu.cessar.ct.workspace.ui.internal.model.documentation.wizard.ModelDocumentationWizard;

/**
 * @author uidt2045
 * 
 */
public class ModelDocumentationAction implements IObjectActionDelegate
{
	private ISelection selection;
	private IWorkbenchPart targetPart;

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IActionDelegate#run(org.eclipse.jface.action.IAction)
	 */
	public void run(IAction action)
	{
		if (PlatformUI.getWorkbench().saveAllEditors(true))
		{
			if (!(selection instanceof IStructuredSelection))
			{
				return;
			}
			IWizard wizard = new ModelDocumentationWizard((IStructuredSelection) selection);
			WizardDialog dialog = new WizardDialog(targetPart.getSite().getShell(), wizard);

			dialog.open();
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IActionDelegate#selectionChanged(org.eclipse.jface.action.IAction, org.eclipse.jface.viewers.ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection)
	{
		this.selection = selection;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IObjectActionDelegate#setActivePart(org.eclipse.jface.action.IAction, org.eclipse.ui.IWorkbenchPart)
	 */
	public void setActivePart(IAction action, IWorkbenchPart targetPart)
	{
		this.targetPart = targetPart;
	}

}
