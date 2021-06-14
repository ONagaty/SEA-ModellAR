package eu.cessar.ct.workspace.ui.logging;

import java.io.IOException;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.resource.ColorRegistry;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.ui.console.IConsoleView;
import org.eclipse.ui.console.IOConsoleOutputStream;
import org.eclipse.ui.part.IPageBookViewPage;
import org.eclipse.ui.themes.ITheme;

import eu.cessar.ct.core.platform.PlatformConstants;
import eu.cessar.ct.core.platform.ui.logging.AbstractCessarConsole;
import eu.cessar.ct.workspace.logging.ILogEventListener;
import eu.cessar.ct.workspace.logging.ILogger2;
import eu.cessar.ct.workspace.logging.LoggingConstants;
import eu.cessar.ct.workspace.ui.internal.CessarPluginActivator;
import eu.cessar.ct.workspace.ui.logging.hyperlink.AutosarObjectMatchListener;
import eu.cessar.ct.workspace.ui.logging.hyperlink.FilePatternMatchListener;
import eu.cessar.ct.workspace.ui.logging.hyperlink.StackTraceMatchListener;
import eu.cessar.ct.workspace.ui.utils.ConsoleUtils;

/**
 * Implementation of Console Reporting UI console. This class is used to display in application UI a CESSAR-CT console
 * associated with to an {@link ILogger2}. The logger instance output is displayed on this console.
 */
public class LoggerConsole extends AbstractCessarConsole
{

	/** the logger instance to which this console is associated with */
	private ILogger2 logger;

	/**
	 * The stream where all the writing will happen
	 */
	private IOConsoleOutputStream debugStream;
	private IOConsoleOutputStream infoStream;
	private IOConsoleOutputStream warnStream;
	private IOConsoleOutputStream errorStream;

	private ILogEventListener logListener = new ILogEventListener()
	{

		public void clearConsole()
		{
			LoggerConsole.this.clearConsole();
		}
	};

	/**
	 * Console constructor
	 */
	LoggerConsole(ILogger2 lg)
	{
		super(lg.getName(), PlatformConstants.PRODUCT_NAME, null, true);
		Assert.isNotNull(lg);
		logger = lg;

		debugStream = newOutputStream();
		infoStream = newOutputStream();
		warnStream = newOutputStream();
		errorStream = newOutputStream();

		logger.attachStream(debugStream, LoggingConstants.LEVEL_DEBUG);
		logger.attachStream(infoStream, LoggingConstants.LEVEL_INFO);
		logger.attachStream(warnStream, LoggingConstants.LEVEL_WARN);
		logger.attachStream(errorStream, LoggingConstants.LEVEL_ERROR);
		logger.addListener(logListener);
		addPatternMatchListener(new AutosarObjectMatchListener(this));
		addPatternMatchListener(new FilePatternMatchListener(this));
		addPatternMatchListener(new StackTraceMatchListener(this));

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.console.IOConsole#createPage(org.eclipse.ui.console.IConsoleView)
	 */
	@Override
	public IPageBookViewPage createPage(IConsoleView view)
	{
		initStreamColors();
		return super.createPage(view);
	}

	/**
	 * Init the colors for the streams
	 */
	private void initStreamColors()
	{
		debugStream.setColor(ConsoleUtils.getColorSettings(ConsoleUtils.ID_DEBUG_COLOR_EDIT));
		infoStream.setColor(ConsoleUtils.getColorSettings(ConsoleUtils.ID_INFO_COLOR_EDIT));
		warnStream.setColor(ConsoleUtils.getColorSettings(ConsoleUtils.ID_WARN_COLOR_EDIT));
		errorStream.setColor(ConsoleUtils.getColorSettings(ConsoleUtils.ID_ERROR_COLOR_EDIT));

		/**
		 * Listens for the changes that occur in the current theme and sets the colors of the output streams with those
		 * selected from the preference store
		 */
		IPropertyChangeListener currentThemeListener = new IPropertyChangeListener()
		{
			@Override
			public void propertyChange(PropertyChangeEvent event)
			{

				if (event.getSource() instanceof ColorRegistry)
				{
					debugStream.setColor(ConsoleUtils.getColorSettings(ConsoleUtils.ID_DEBUG_COLOR_EDIT));
					infoStream.setColor(ConsoleUtils.getColorSettings(ConsoleUtils.ID_INFO_COLOR_EDIT));
					warnStream.setColor(ConsoleUtils.getColorSettings(ConsoleUtils.ID_WARN_COLOR_EDIT));
					errorStream.setColor(ConsoleUtils.getColorSettings(ConsoleUtils.ID_ERROR_COLOR_EDIT));
				}
			}
		};
		ITheme currentTheme;
		currentTheme = ConsoleUtils.getCurrentTheme();
		currentTheme.addPropertyChangeListener(currentThemeListener);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.console.IOConsole#dispose()
	 */
	@Override
	protected void dispose()
	{

		logger.removeListener(logListener);
		logger.detachStream(debugStream);
		logger.detachStream(infoStream);
		logger.detachStream(warnStream);
		logger.detachStream(errorStream);
		try
		{
			debugStream.close();
			infoStream.close();
			warnStream.close();
			errorStream.close();
		}
		catch (IOException e)
		{
			// log and ignore
			CessarPluginActivator.getDefault().logError(e);
		}
		super.dispose();
	}

}
