package eu.cessar.ct.runtime.ecuc.ui.internal.actions;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import eu.cessar.ct.runtime.ecuc.ui.internal.menu.RunPlugetClassAction;

/**
 *
 * @author uidu8153
 *
 *         %created_by: uidg3464 %
 *
 *         %date_created: Tue Mar 17 18:05:20 2015 %
 *
 *         %version: TAUTOSAR~3 %
 */
public class RunPlugetHandler extends AbstractHandler
{

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException
	{
		IWorkbenchWindow activeWorkbenchWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		if (activeWorkbenchWindow != null)
		{
			RunPlugetClassAction action = new RunPlugetClassAction();
			ISelectionService selectionService = activeWorkbenchWindow.getSelectionService();
			ISelection selection = selectionService.getSelection();

			if (selection != null)
			{
				if (selection instanceof IStructuredSelection)

				{
					Object element = ((IStructuredSelection) selection).getFirstElement();
					if (element instanceof ICompilationUnit)
					{

						action.selectionChanged(null, selection);
						action.run(null);
					}

				}
			}
		}

		return null;

	}

}
