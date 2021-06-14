package eu.cessar.ct.workspace.ui.logging;

import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleConstants;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.IConsoleView;

import eu.cessar.ct.workspace.logging.ILogger2;
import eu.cessar.ct.workspace.logging.ILoggerFactoryListener;
import eu.cessar.ct.workspace.ui.internal.CessarPluginActivator;

/**
 * A concrete implementation of {@link ILoggerFactoryListener} interface, registered to monitor when loggers are used,
 * so that corresponding consoles are created, activated or disposed.
 */
public final class LoggerFactoryListener implements ILoggerFactoryListener
{
	/** the Eclipse platform console manager */
	private IConsoleManager consoleManager;

	/**
	 * Default class constructor
	 */
	public LoggerFactoryListener()
	{
		ConsolePlugin plugin = ConsolePlugin.getDefault();
		consoleManager = plugin.getConsoleManager();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.workspace.logging.ILoggerFactoryListener#loggerCreated(eu.cessar.ct.workspace.logging.ILogger2)
	 */
	public void loggerCreated(ILogger2 logger)
	{
		consoleCreateOrActivate(logger);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.cessar.ct.workspace.logging.ILoggerFactoryListener#loggerActivated(eu.cessar.ct.workspace.logging.ILogger2)
	 */
	public void loggerActivated(ILogger2 logger)
	{
		consoleCreateOrActivate(logger);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.cessar.ct.workspace.logging.ILoggerFactoryListener#loggerDisposed(eu.cessar.ct.workspace.logging.ILogger2)
	 */
	public void loggerDisposed(ILogger2 logger)
	{
		LoggerConsole console = findConsole(logger.getName());
		if (console != null)
		{
			console.dispose();
		}
	}

	private LoggerConsole findConsole(String name)
	{
		IConsole[] existing = consoleManager.getConsoles();
		for (int i = 0; i < existing.length; i++)
		{
			if (name.equals(existing[i].getName()) && (existing[i] instanceof LoggerConsole))
			{
				return (LoggerConsole) existing[i];
			}
		}
		return null;
	}

	/**
	 * Create a new UI console or activate the existing one
	 * 
	 * @param logger
	 */
	private void consoleCreateOrActivate(ILogger2 logger)
	{
		if (!PlatformUI.isWorkbenchRunning())
		{
			// without the workbench there is no point in creating a console
			return;
		}
		final LoggerConsole[] loggerConsole = new LoggerConsole[] {findConsole(logger.getName())};
		if (loggerConsole[0] == null)
		{
			// no console found for specified logger
			loggerConsole[0] = new LoggerConsole(logger);
			consoleManager.addConsoles(loggerConsole);
		}

		// try to display and activate the Console view
		Display display = PlatformUI.getWorkbench().getDisplay();
		display.syncExec(new Runnable() // SUPPRESS CHECKSTYLE length OK
		{
			// if this anonymous inner class grows in size, please consider
			// extracting it
			public void run()
			{
				IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
				if (window != null)
				{
					IWorkbenchPage page = window.getActivePage();
					if (page != null)
					{
						try
						{
							IConsoleView view = null;
							for (IViewReference refView: page.getViewReferences())
							{
								if (IConsoleConstants.ID_CONSOLE_VIEW.equals(refView.getId()))
								{
									view = (IConsoleView) refView.getView(true);
									break;
								}
							}
							if (view == null)
							{
								view = (IConsoleView) page.showView(IConsoleConstants.ID_CONSOLE_VIEW);
							}
							if (view.getConsole() != loggerConsole[0])
							{
								view.display(loggerConsole[0]);
							}
							if (!page.isPartVisible(view))
							{
								page.activate(view);
							}
						}
						catch (PartInitException e)
						{
							CessarPluginActivator.getDefault().logError(e);
						}
					}
				}
			}
		});
	}
}
