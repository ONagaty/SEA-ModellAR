package eu.cessar.ct.runtime.ecuc.ui.internal.menu;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import eu.cessar.ct.runtime.ecuc.ui.internal.wizards.ExportPlugetWizard;

/**
 *
 * Action to start the export pluget wizard {@link ExportPlugetWizard}
 *
 * @author uidg4449
 *
 *         %created_by: uidg4449 %
 *
 *         %date_created: Mon Jul  6 14:37:42 2015 %
 *
 *         %version: 2 %
 */
public class ExportPlugetClassAction extends RunPlugetClassAction
{

	private Shell parentShell;

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.ui.IActionDelegate#run(org.eclipse.jface.action.IAction)
	 */
	@Override
	public void run(IAction action)
	{
		String plugetFileName = getPlugetFileName();
		WizardDialog dialog = new WizardDialog(getShell(), new ExportPlugetWizard(getProject(), plugetFileName));

		dialog.open();
	}

	/**
	 * gets the name of the pluget class name
	 *
	 * @return
	 */
	private String getPlugetFileName()
	{
		IWorkbenchWindow activeWorkbenchWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		if (activeWorkbenchWindow != null)
		{
			ISelectionService selectionService = activeWorkbenchWindow.getSelectionService();
			ISelection selection = selectionService.getSelection();
			if (selection != null)
			{
				if (selection instanceof IStructuredSelection)

				{
					Object element = ((IStructuredSelection) selection).getFirstElement();
					if (element instanceof ICompilationUnit)
					{
						String elementName = ((ICompilationUnit) element).getElementName();
						return elementName;
					}

				}
			}
		}
		return null;
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
		parentShell = targetPart.getSite().getWorkbenchWindow().getShell();

	}

	/**
	 * @return the current shell
	 */
	@Override
	protected Shell getShell()
	{
		return parentShell;
	}

}
