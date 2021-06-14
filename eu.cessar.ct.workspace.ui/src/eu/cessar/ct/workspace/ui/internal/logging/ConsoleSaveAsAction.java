package eu.cessar.ct.workspace.ui.internal.logging;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleView;

import eu.cessar.ct.workspace.ui.logging.LoggerConsole;

/**
 * The class that implements 'Save as...' action, contributed to CESSAR-CT
 * console. The action allow users to specify a file where to save the current
 * content of the console.
 */
public class ConsoleSaveAsAction implements IViewActionDelegate
{
	private IViewPart consoleView;

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IViewActionDelegate#init(org.eclipse.ui.IViewPart)
	 */
	public void init(IViewPart view)
	{
		consoleView = view;
	}

	@SuppressWarnings("nls")
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
			FileDialog fd = new FileDialog(shell, SWT.SAVE);
			fd.setText("Save console");
			String[] filterExt = {"*.txt", "*.log", "*.*"};
			fd.setFilterExtensions(filterExt);
			String selected = fd.open();
			if (selected != null)
			{
				MessageDialog.openError(shell, "N/A", "Not implemented");
				// File file = new File(selected);
				// console.saveConsoleToFile(file);
			}
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

	public void selectionChanged(IAction action, ISelection selection)
	{
		// nothing to do here
	}
}
