/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl7321 Jul 13, 2012 3:43:18 PM </copyright>
 */
package eu.cessar.ct.ecuc.workspace.ui.actions;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
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

import eu.cessar.ct.core.platform.CESSARPreferencesAccessor;
import eu.cessar.ct.core.platform.EProjectVariant;
import eu.cessar.ct.ecuc.workspace.ui.internal.Messages;
import eu.cessar.ct.ecuc.workspace.ui.internal.wizards.ModuleConfigurationWizard;

/**
 * Action for opening the wizard for new module configuration.
 * 
 */
public class NewModuleConfigurationAction implements IObjectActionDelegate
{

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IActionDelegate#run(org.eclipse.jface.action.IAction)
	 */
	public void run(IAction action)
	{
		IWorkbench workbench = PlatformUI.getWorkbench();
		IWorkbenchWindow activeWorkbenchWindow = workbench.getActiveWorkbenchWindow();
		if (activeWorkbenchWindow != null)
		{

			ModuleConfigurationWizard wizard = new ModuleConfigurationWizard();
			ISelection selection = activeWorkbenchWindow.getSelectionService().getSelection();
			if (!(selection instanceof StructuredSelection))
			{

				selection = StructuredSelection.EMPTY;
			}

			IStructuredSelection structuredSelection = (IStructuredSelection) selection;
			Shell shell = activeWorkbenchWindow.getShell();
			if (null != selection && !selection.isEmpty())
			{

				IProject selectedProject = ((IResource) structuredSelection.getFirstElement()).getProject();
				EProjectVariant projectVariant = CESSARPreferencesAccessor.getProjectVariant(selectedProject);
				if (EProjectVariant.PRODUCTION == projectVariant)
				{
					// not allowed to create ecuc module configuration in 'PRODUCTION'
					boolean changeToDevelopment = showNoEcucConfigAllowedDialog(shell, selectedProject);
					if (!changeToDevelopment)
					{
						return;
					}
				}
			}
			wizard.init(workbench, (IStructuredSelection) selection);
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
	public void selectionChanged(IAction action, ISelection selection)
	{
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IObjectActionDelegate#setActivePart(org.eclipse.jface.action.IAction,
	 * org.eclipse.ui.IWorkbenchPart)
	 */
	public void setActivePart(IAction action, IWorkbenchPart targetPart)
	{
		// TODO Auto-generated method stub

	}

	/**
	 * Display a question dialog to inform the user that he cannot create an ecuc config in 'PRODUCTION'phase, and give
	 * him the possibility to change phase to 'DEVELOPMENT'
	 * 
	 * @param shell
	 *        shell to display dialog on
	 * @param project
	 *        current project
	 */
	@SuppressWarnings("static-method")
	private boolean showNoEcucConfigAllowedDialog(Shell shell, IProject project)
	{
		boolean confirm = MessageDialog.openQuestion(shell, Messages.ModuleConfigurationCreationNotAllowedTitle,
			Messages.ModuleConfigurationCreationNotAllowedQuestion);

		if (confirm)
		{
			CESSARPreferencesAccessor.setProjectVariant(project, EProjectVariant.DEVELOPMENT);
			return true;
		}
		return false;
	}

}
