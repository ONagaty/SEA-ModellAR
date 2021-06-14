package eu.cessar.ct.validation.ui.internal.actions;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

/**
 * Clean problem markers handler implementation
 *
 * @author uidu8153
 *
 *         %created_by: uidg3464 %
 *
 *         %date_created: Tue Mar 17 18:05:20 2015 %
 *
 *         %version: TAUTOSAR~3 %
 */
public class CleanProblemMarkersHandler extends AbstractHandler
{

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException
	{
		IWorkbenchWindow activeWorkbenchWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		if (activeWorkbenchWindow != null)
		{
			ISelection selection = activeWorkbenchWindow.getSelectionService().getSelection();
			if (selection instanceof IStructuredSelection)
			{
				IStructuredSelection structuredSelection = (IStructuredSelection) selection;

				CleanProblemMarkers cleanProblemMarkers = new CleanProblemMarkers();
				cleanProblemMarkers.setCurrentSelection(structuredSelection);
				cleanProblemMarkers.clean(false);
			}
		}

		return null;
	}
}
