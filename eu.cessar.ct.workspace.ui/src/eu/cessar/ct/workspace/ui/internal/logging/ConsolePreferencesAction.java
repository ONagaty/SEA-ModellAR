package eu.cessar.ct.workspace.ui.internal.logging;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleView;

import eu.cessar.ct.workspace.ui.logging.LoggerConsole;

/**
 * Implementation class for the action displayed in console view menu that allow
 * opening of the console preferences dialog.
 */
public class ConsolePreferencesAction implements IViewActionDelegate
{
	private IViewPart consoleView;

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IViewActionDelegate#init(org.eclipse.ui.IViewPart)
	 */
	public void init(IViewPart view)
	{
		consoleView = view;
	}

	public void run(IAction action)
	{
		Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
		LoggerConsole console = getLoggerConsole();
		if (console == null)
		{
			MessageDialog.openError(shell, "Save console",
				"Active console is not a recongnized CESSAR-CT console.");
		}
		else
		{
			ConsolePreferencesDialog prefsDialog = new ConsolePreferencesDialog(shell);
			prefsDialog.open();
		}
	}

	private LoggerConsole getLoggerConsole()
	{
		if (consoleView instanceof IConsoleView)
		{
			IConsole console = ((IConsoleView) consoleView).getConsole();
			if (console instanceof LoggerConsole)
			{
				return (LoggerConsole) console;
			}
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IActionDelegate#selectionChanged(org.eclipse.jface.action.IAction, org.eclipse.jface.viewers.ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection)
	{
		// nothing to do here
	}
}
