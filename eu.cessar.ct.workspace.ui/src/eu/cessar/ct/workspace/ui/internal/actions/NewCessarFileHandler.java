package eu.cessar.ct.workspace.ui.internal.actions;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

/**
 * TODO: Please comment this class
 * 
 * @author uidu8153
 * 
 *         %created_by: uidu8153 %
 * 
 *         %date_created: Thu Mar 12 13:00:15 2015 %
 * 
 *         %version: 1 %
 */
public class NewCessarFileHandler extends AbstractHandler
{

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException
	{
		IWorkbenchWindow activeWorkbenchWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		if (activeWorkbenchWindow != null)
		{
			NewCessarFileAction cessarFileAction = new NewCessarFileAction();
			cessarFileAction.run(null);
		}
		return null;
	}

}
