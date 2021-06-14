package eu.cessar.ct.runtime.ecuc.ui.internal.menu;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import eu.cessar.ct.core.platform.nature.CessarNature;
import eu.cessar.ct.runtime.ecuc.IEcucCore;
import eu.cessar.ct.runtime.ecuc.IEcucPresentationModel;
import eu.cessar.ct.runtime.ecuc.ui.internal.CessarPluginActivator;

/**
 * Dumps the Presentation Model interfaces and implementations
 * 
 * @author uidg3464
 * 
 *         %created_by: uidg3464 %
 * 
 *         %date_created: Tue Jun  3 11:28:57 2014 %
 * 
 *         %version: 7 %
 */
public class DumpPresentationModelImplAction implements IObjectActionDelegate
{

	private IProject selectedProject;

	public void setActivePart(IAction action, IWorkbenchPart targetPart)
	{
		// TODO Auto-generated method stub

	}

	public void run(IAction action)
	{
		if (selectedProject == null)
		{
			return;
		}
		IWorkbench workbench = PlatformUI.getWorkbench();
		IWorkbenchWindow activeWorkbenchWindow = workbench.getActiveWorkbenchWindow();
		Shell shell = activeWorkbenchWindow.getShell();
		ProgressMonitorDialog dialog = new ProgressMonitorDialog(shell);

		final IEcucPresentationModel ecpm = IEcucCore.INSTANCE.getEcucPresentationModel(selectedProject);
		// The name of the folder in which the presentation model will be dumped
		final String fName = "pmbin"; //$NON-NLS-1$
		try
		{
			dialog.run(true, false, new IRunnableWithProgress()
			{

				@Override
				public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException
				{
					// TODO Auto-generated method stub
					ecpm.dumpPresentationModel(true, fName, monitor);
				}
			});
		}
		catch (InvocationTargetException e)
		{
			// TODO Auto-generated catch block
			CessarPluginActivator.getDefault().logError(e);
		}
		catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			CessarPluginActivator.getDefault().logError(e);
		}

	}

	public void selectionChanged(IAction action, ISelection selection)
	{
		selectedProject = null;
		if (selection instanceof IStructuredSelection)
		{
			Object firstElement = ((IStructuredSelection) selection).getFirstElement();
			if (firstElement instanceof IProject)
			{
				IProject aProject = (IProject) firstElement;
				if (CessarNature.haveNature(aProject))
				{
					selectedProject = aProject;
				}
			}
		}
	}
}
