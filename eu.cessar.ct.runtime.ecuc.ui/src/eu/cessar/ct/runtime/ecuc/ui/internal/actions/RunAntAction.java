package eu.cessar.ct.runtime.ecuc.ui.internal.actions;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import eu.cessar.ct.runtime.ecuc.ui.internal.dialogs.AntExecutionDialog;
import eu.cessar.ct.runtime.ecuc.util.RunPlugetUtils;

/**
 * This class is responsible to handle menu action from Run Ant
 *
 * @author uidr8466
 *
 *         %created_by: created by %
 *
 *         %date_created: creation date %
 *
 *         %version: version %
 */
public class RunAntAction implements IObjectActionDelegate
{
	private IProject project;

	/*
	 * Default run method
	 *
	 * @see org.eclipse.ui.IActionDelegate#run(org.eclipse.jface.action.IAction)
	 */
	@Override
	public void run(IAction action)
	{
		IWorkbench workbench = PlatformUI.getWorkbench();
		IWorkbenchWindow activeWorkbenchWindow = workbench.getActiveWorkbenchWindow();
		if ((activeWorkbenchWindow != null) && (project != null))
		{
			Shell shell = activeWorkbenchWindow.getShell();
			if (shell != null)
			{
				AntExecutionDialog antDialog = new AntExecutionDialog(shell, project);
				antDialog.open();
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
		project = null;
		if ((selection instanceof StructuredSelection))
		{
			Object element = ((IStructuredSelection) selection).getFirstElement();
			if (null != element)
			{
				project = RunPlugetUtils.INSTANCE.getProject(element);
			}
		}
		action.setEnabled(null != project);

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
		// Nothing to do
	}

}
