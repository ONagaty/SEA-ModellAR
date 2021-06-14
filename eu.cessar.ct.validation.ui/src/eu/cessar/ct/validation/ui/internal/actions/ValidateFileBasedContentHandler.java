package eu.cessar.ct.validation.ui.internal.actions;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

/**
 *
 * @author uidu8153
 *
 *         %created_by: uidg3464 %
 *
 *         %date_created: Tue Mar 17 18:05:47 2015 %
 *
 *         %version: TAUTOSAR~3 %
 */
public class ValidateFileBasedContentHandler extends AbstractHandler
{

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.core.commands.IHandler#execute(org.eclipse.core.commands.ExecutionEvent)
	 */
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException
	{
		IWorkbenchWindow activeWorkbenchWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		if (activeWorkbenchWindow != null)
		{

			ValidateAutosarContentAction action = new ValidateAutosarContentAction();
			ISelectionService selectionService = activeWorkbenchWindow.getSelectionService();
			ISelection selection = selectionService.getSelection();
			if (selection != null)
			{
				if (selection instanceof IStructuredSelection)
				{
					if (selection != StructuredSelection.EMPTY)
					{
						runAction(action, selection);
					}
				}
			}

		}
		return null;
	}

	private void runAction(ValidateAutosarContentAction action, ISelection selection)
	{
		Object firstElement = ((StructuredSelection) selection).getFirstElement();
		if (firstElement != null)
		{
			action.selectionChanged(action, selection);
			action.run(null);
		}
	}

}
