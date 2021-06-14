package eu.cessar.ct.workspace.ui.internal.logging;

import org.eclipse.ui.console.IConsoleFactory;

import eu.cessar.ct.sdk.logging.LoggerFactory;

/**
 * The class that construct default logger when CESSAR-CT console is activated
 * in the application UI.
 */
public class LoggerConsoleFactory implements IConsoleFactory
{
	/* (non-Javadoc)
	 * @see org.eclipse.ui.console.IConsoleFactory#openConsole()
	 */
	public void openConsole()
	{
		LoggerFactory.getLogger();
	}
}