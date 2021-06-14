package eu.cessar.ct.runtime.ecuc.ui.internal.actions;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import eu.cessar.ct.core.platform.nature.CessarNature;
import eu.cessar.ct.runtime.ecuc.IEcucCore;
import eu.cessar.ct.runtime.ecuc.IEcucPresentationModel;
import eu.cessar.ct.runtime.ecuc.ui.internal.CessarPluginActivator;

/**
 * TODO: Please comment this class
 *
 * @author uidu8153
 *
 *         %created_by: uidg3464 %
 *
 *         %date_created: Tue Mar 17 18:05:19 2015 %
 *
 *         %version: TAUTOSAR~3 %
 */
public class DumpPresentationModelHandler extends AbstractHandler
{

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException
	{
		IWorkbenchWindow activeWorkbenchWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		if (activeWorkbenchWindow != null)
		{

			Shell shell = activeWorkbenchWindow.getShell();
			ProgressMonitorDialog dialog = new ProgressMonitorDialog(shell);

			IProject selectionChanged = selectionChanged(activeWorkbenchWindow);
			if (selectionChanged != null)
			{
				final IEcucPresentationModel ecpm = IEcucCore.INSTANCE.getEcucPresentationModel(selectionChanged);

				// The name of the folder in which the presentation model will be dumped
				final String fName = "pmbin"; //$NON-NLS-1$
				try
				{
					dialog.run(true, false, new IRunnableWithProgress()
					{

						@Override
						public void run(IProgressMonitor monitor) throws InvocationTargetException,
						InterruptedException
						{
							ecpm.dumpPresentationModel(false, fName, monitor);
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
		}
		return null;
	}

	/**
	 * @param workbenchWindow
	 * @param action
	 * @param selection
	 * @return
	 */
	public IProject selectionChanged(IWorkbenchWindow workbenchWindow)
	{

		ISelection selection = workbenchWindow.getSelectionService().getSelection();
		if (selection instanceof IStructuredSelection)
		{
			IStructuredSelection structuredSelection = (IStructuredSelection) selection;
			if (structuredSelection != StructuredSelection.EMPTY)
			{
				Object firstElement = structuredSelection.getFirstElement();

				if (firstElement instanceof IProject)
				{
					IProject aProject = (IProject) firstElement;
					if (CessarNature.haveNature(aProject))
					{
						return aProject;
					}

				}
				else if (firstElement instanceof IResource)
				{
					return ((IResource) firstElement).getProject();
				}
			}
		}
		return null;

	}
}
