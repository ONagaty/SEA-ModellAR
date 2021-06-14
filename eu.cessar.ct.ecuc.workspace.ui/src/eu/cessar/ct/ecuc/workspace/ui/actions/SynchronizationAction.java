package eu.cessar.ct.ecuc.workspace.ui.actions;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;

import eu.cessar.ct.ecuc.workspace.ui.internal.wizards.sync.SynchronizerWizard;

public class SynchronizationAction implements IObjectActionDelegate
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
			IWizard wizard = new SynchronizerWizard(selection);
			WizardDialog dialog = new WizardDialog(targetPart.getSite().getShell(), wizard);
			dialog.create();
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
