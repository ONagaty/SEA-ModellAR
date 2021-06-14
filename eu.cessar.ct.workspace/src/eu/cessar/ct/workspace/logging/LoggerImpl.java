package eu.cessar.ct.workspace.logging;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.commands.common.EventManager;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.SafeRunner;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.osgi.util.NLS;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.FileAppender;
import eu.cessar.ct.core.mms.MetaModelUtils;
import eu.cessar.ct.core.platform.util.SafeRunnable;
import eu.cessar.ct.workspace.internal.Messages;
import eu.cessar.ct.workspace.internal.logger.StreamAppender;
import eu.cessar.req.Requirement;

/**
 * CESSAR-CT logger implementation
 */
@Requirement(
	reqID = "202167")
public class LoggerImpl extends EventManager implements ILogger2
{

	private boolean rte;
	private int originDepth = 4;

	// Logback logger implementation
	private Logger logbackLogger;

	// logger appenders
	private FileAppender<ILoggingEvent> userFileAppender;

	private Map<OutputStream, StreamAppender> streamAppenders = new HashMap<>();

	private IErrorCatalogueDB errorCatalogueDB;

	/**
	 * Default class constructor
	 *
	 * @param name
	 *        - a name for the logger instance
	 */
	/* package */ LoggerImpl(String name)
	{
		logbackLogger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(name);
		logbackLogger.setAdditive(false);
		errorCatalogueDB = ErrorCatalogueDBFactory.getDatabase();
		// TODO: Find another solution, for example configuration of logger
		if ("Rte-Logger console".equals(name)) //$NON-NLS-1$
		{
			rte = true;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.workspace.logging.ILogger2#addListener(eu.cessar.ct.workspace.logging.ILogEventListener)
	 */
	public void addListener(ILogEventListener listener)
	{
		addListenerObject(listener);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.workspace.logging.ILogger2#removeListener(eu.cessar.ct.workspace.logging.ILogEventListener)
	 */
	public void removeListener(ILogEventListener listener)
	{
		removeListenerObject(listener);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.workspace.logging.ILogger#getName()
	 */
	public String getName()
	{
		return logbackLogger.getName();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.workspace.logging.ILogger2#dispose()
	 */
	public synchronized void dispose()
	{
		logbackLogger.detachAndStopAllAppenders();
		logbackLogger = null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.workspace.logging.ILogger2#clear()
	 */
	public void clear()
	{
		Object[] listeners = getListeners();
		for (Object object: listeners)
		{
			final ILogEventListener lst = (ILogEventListener) object;
			SafeRunner.run(new SafeRunnable()
			{
				public void run() throws Exception
				{
					lst.clearConsole();
				}
			});
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.sdk.logging.ILogger#debug(boolean)
	 */
	public void debug(boolean b)
	{
		debug(String.valueOf(b));
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.sdk.logging.ILogger#debug(char)
	 */
	public void debug(char c)
	{
		debug(String.valueOf(c));
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.sdk.logging.ILogger#debug(long)
	 */
	public void debug(long l)
	{
		debug(String.valueOf(l));
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.sdk.logging.ILogger#debug(double)
	 */
	public void debug(double d)
	{
		debug(String.valueOf(d));
	}

	public void debug(String message)
	{
		logbackLogger.debug(message);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.sdk.logging.ILogger#debug(java.lang.Object)
	 */
	public void debug(Object o)
	{
		if (o instanceof Throwable)
		{
			logbackLogger.debug(((Throwable) o).getMessage(), (Throwable) o);
		}
		else
		{
			debug(computeString(o));
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.sdk.logging.ILogger#error(boolean)
	 */
	public void error(boolean b)
	{
		error(String.valueOf(b));
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.sdk.logging.ILogger#error(char)
	 */
	public void error(char c)
	{
		error(String.valueOf(c));
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.sdk.logging.ILogger#error(long)
	 */
	public void error(long l)
	{
		error(String.valueOf(l));
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.sdk.logging.ILogger#error(double)
	 */
	public void error(double d)
	{
		error(String.valueOf(d));
	}

	public void error(String message)
	{
		logbackLogger.error(message);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.sdk.logging.ILogger#error(java.lang.Object)
	 */
	public void error(Object o)
	{
		if (o instanceof Throwable)
		{
			logbackLogger.error(((Throwable) o).getMessage(), (Throwable) o);
		}
		else
		{
			error(computeString(o));
		}
	}

	/**
	 * Compute the string representation of the passed object
	 *
	 * @param value
	 * @return the string representation of the object, never null
	 */
	@Requirement(
		reqID = "201807")
	private String computeString(Object value)
	{
		if (value instanceof EObject)
		{
			// call a method from MMU to compute the external form of the object
			return MetaModelUtils.getURIForLogging((EObject) value);
		}
		else
		{
			return String.valueOf(value);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.workspace.logging.ILogger#info(java.lang.String)
	 */
	public void info(String message)
	{
		logbackLogger.info(message);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.sdk.logging.ILogger#info(boolean)
	 */
	public void info(boolean b)
	{
		info(String.valueOf(b));
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.sdk.logging.ILogger#info(char)
	 */
	public void info(char c)
	{
		info(String.valueOf(c));
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.sdk.logging.ILogger#info(long)
	 */
	public void info(long l)
	{
		info(String.valueOf(l));
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.sdk.logging.ILogger#info(double)
	 */
	public void info(double d)
	{
		info(String.valueOf(d));
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.sdk.logging.ILogger#info(java.lang.Object)
	 */
	public void info(Object o)
	{
		if (o instanceof Throwable)
		{
			logbackLogger.info(((Throwable) o).getMessage(), (Throwable) o);
		}
		else
		{
			info(computeString(o));
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.sdk.logging.ILogger#warn(boolean)
	 */
	public void warn(boolean b)
	{
		warn(String.valueOf(b));
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.sdk.logging.ILogger#warn(char)
	 */
	public void warn(char c)
	{
		warn(String.valueOf(c));
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.sdk.logging.ILogger#warn(long)
	 */
	public void warn(long l)
	{
		warn(String.valueOf(l));
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.sdk.logging.ILogger#warn(double)
	 */
	public void warn(double d)
	{
		warn(String.valueOf(d));
	}

	public void warn(String message)
	{
		logbackLogger.warn(message);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.sdk.logging.ILogger#warn(java.lang.Object)
	 */
	public void warn(Object o)
	{
		if (o instanceof Throwable)
		{
			logbackLogger.warn(((Throwable) o).getMessage(), (Throwable) o);
		}
		else
		{
			warn(computeString(o));
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.workspace.logging.ILogger#log(java.lang.Throwable)
	 */
	public void log(Throwable ex)
	{
		logbackLogger.error(ex.getMessage(), ex);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.workspace.logging.ILogger#log(org.eclipse.core.runtime.IStatus)
	 */
	public void log(IStatus status)
	{
		Throwable ex = status.getException();
		String message = status.getMessage();
		switch (status.getSeverity())
		{
			case IStatus.ERROR:
				if (ex != null)
				{
					logbackLogger.error(message, ex);
				}
				else
				{
					error(message);
				}
				break;
			case IStatus.WARNING:
				if (ex != null)
				{
					logbackLogger.warn(message, ex);
				}
				else
				{
					warn(message);
				}
				break;
			case IStatus.INFO:
				if (ex != null)
				{
					logbackLogger.info(message, ex);
				}
				else
				{
					info(message);
				}
				break;
			default:
				if (ex != null)
				{
					logbackLogger.debug(message, ex);
				}
				else
				{
					debug(message);
				}
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.sdk.logging.ILogger#attach(org.eclipse.core.resources.IFile, boolean)
	 */
	public void attachFile(IFile iFile, boolean append)
	{
		File file = iFile.getLocation().toFile();
		attachFile(file, append);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.sdk.logging.ILogger#attach(java.lang.String, boolean)
	 */
	@Requirement(
		reqID = "215911")
	public synchronized void attachFile(File file, boolean append)
	{
		if (file == null)
		{
			return;
		}

		// detach old file appender
		detachFile();

		// initialize an appender for specified file and add it to the logger
		userFileAppender = new FileAppender<>();
		userFileAppender.setLayout(createPattern(LoggingConstants.DEFAULT_LOGGING_PATTERN));
		userFileAppender.setFile(file.getAbsolutePath());
		userFileAppender.setAppend(append);
		attachAppender(userFileAppender);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.workspace.logging.ILogger#detachFile()
	 */
	public synchronized void detachFile()
	{
		// close and detach file appender if any
		if (userFileAppender != null)
		{
			if (userFileAppender.isStarted())
			{
				userFileAppender.stop();
			}

			logbackLogger.detachAppender(userFileAppender);
			userFileAppender = null;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.workspace.logging.ILogger2#setPattern()
	 */
	public PatternLayout createPattern(String pattern)
	{
		if ((pattern == null) || pattern.length() == 0)
		{
			return null;
		}
		PatternLayout loggerLayout = new PatternLayout();
		loggerLayout.setContext(logbackLogger.getLoggerContext());
		loggerLayout.setPattern(pattern);
		loggerLayout.start();
		return loggerLayout;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.workspace.logging.ILogger2#attachAppender(ch.qos.logback.core.Appender)
	 */
	public synchronized void attachAppender(Appender<ILoggingEvent> appender)
	{
		if (appender != null)
		{
			appender.setContext(logbackLogger.getLoggerContext());
			appender.start();
			logbackLogger.addAppender(appender);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.workspace.logging.ILogger2#detachAppender(ch.qos.logback.core.Appender)
	 */
	public synchronized void detachAppender(Appender<ILoggingEvent> appender)
	{
		if (appender != null)
		{
			appender.stop();
			logbackLogger.detachAppender(appender);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.sdk.logging.ILogger#attachStream(java.io.OutputStream)
	 */
	public synchronized void attachStream(OutputStream stream)
	{
		attachStream(stream, LoggingConstants.DEFAULT_LOGGING_PATTERN, LoggingConstants.LEVEL_ALL);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.workspace.logging.ILogger2#attachStream(java.io.OutputStream, int)
	 */
	@Override
	public void attachStream(OutputStream stream, int eventLevel)
	{
		attachStream(stream, LoggingConstants.DEFAULT_LOGGING_PATTERN, eventLevel);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.sdk.logging.ILogger#attachStream(java.io.OutputStream, java.lang.String)
	 */
	public void attachStream(OutputStream stream, String pattern)
	{
		attachStream(stream, pattern, LoggingConstants.LEVEL_ALL);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.workspace.logging.ILogger2#attachStream(java.io.OutputStream, java.lang.String, int)
	 */
	@Override
	public void attachStream(OutputStream stream, String pattern, int eventLevel)
	{
		if (!streamAppenders.containsKey(stream))
		{
			StreamAppender appender = new StreamAppender(eventLevel);
			appender.setLayout(createPattern(pattern));
			appender.setOutputStream(stream);
			attachAppender(appender);
			streamAppenders.put(stream, appender);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.sdk.logging.ILogger#detachConsole(java.io.OutputStream)
	 */
	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.sdk.logging.ILogger#detachStream(java.io.OutputStream)
	 */
	public synchronized void detachStream(OutputStream stream)
	{
		if (streamAppenders.containsKey(stream))
		{
			detachAppender(streamAppenders.remove(stream));
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.workspace.logging.ILogger2#getCurrentStreams()
	 */
	public List<OutputStream> getCurrentStreams()
	{
		return getCurrentStreams(LoggingConstants.LEVEL_ALL);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.workspace.logging.ILogger2#flushStreams()
	 */
	@Override
	public void flushStreams() throws IOException
	{
		for (OutputStream stream: getCurrentStreams())
		{
			stream.flush();
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.workspace.logging.ILogger2#getCurrentStreams(int)
	 */
	@Override
	public List<OutputStream> getCurrentStreams(int eventLevel)
	{
		if (streamAppenders.isEmpty())
		{
			return Collections.emptyList();
		}
		else
		{
			Set<OutputStream> keySet = streamAppenders.keySet();
			List<OutputStream> result = new ArrayList<>();
			for (OutputStream outputStream: keySet)
			{
				StreamAppender appender = streamAppenders.get(outputStream);
				if (appender.acceptEvent(eventLevel))
				{
					result.add(outputStream);
				}
			}
			return result;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.sdk.logging.ILogger#error(java.lang.String, java.lang.Object[])
	 */
	@Override
	public void error(String messageId, String contextMessage, Object... parameters)
	{
		String message = errorCatalogueDB.getMessageById(messageId);
		if (message == null)
		{
			warn(NLS.bind(Messages.LoggerImpl_NO_MESSAGE_FOR_ID, new String[] {messageId}));
		}
		else
		{
			String toLog = null;

			if (contextMessage == null || contextMessage.length() == 0)
			{
				toLog = createMessageToLog(message, parameters);
			}
			else
			{
				toLog = createMessageToLog(message + "; " + contextMessage, parameters); //$NON-NLS-1$
			}

			logbackLogger.error(toLog);
		}

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.sdk.logging.ILogger#info(java.lang.String, java.lang.Object[])
	 */
	@Override
	public void info(String messageId, String contextMessage, Object... parameters)
	{
		String message = errorCatalogueDB.getMessageById(messageId);
		if (message == null)
		{
			warn(NLS.bind(Messages.LoggerImpl_NO_MESSAGE_FOR_ID, new String[] {messageId}));
		}
		else
		{
			String toLog = null;

			if (contextMessage == null || contextMessage.length() == 0)
			{
				toLog = processAndBindParameters(message, parameters);
			}
			else
			{
				toLog = processAndBindParameters(message + "; " + contextMessage, parameters); //$NON-NLS-1$
			}

			logbackLogger.info(toLog);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.sdk.logging.ILogger#warn(java.lang.String, java.lang.Object[])
	 */
	@Override
	public void warn(String messageId, String contextMessage, Object... parameters)
	{
		String message = errorCatalogueDB.getMessageById(messageId);
		if (message == null)
		{
			warn(NLS.bind(Messages.LoggerImpl_NO_MESSAGE_FOR_ID, new String[] {messageId}));
		}
		else
		{
			String toLog = null;

			if (contextMessage == null || contextMessage.length() == 0)
			{
				toLog = createMessageToLog(message, parameters);
			}
			else
			{
				toLog = createMessageToLog(message + "; " + contextMessage, parameters); //$NON-NLS-1$
			}
			logbackLogger.warn(toLog);
		}

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.sdk.logging.ILogger#debug(java.lang.String, java.lang.Object[])
	 */
	@Override
	public void debug(String messageId, String contextMessage, Object... parameters)
	{
		String message = errorCatalogueDB.getMessageById(messageId);
		if (message == null)
		{
			warn(NLS.bind(Messages.LoggerImpl_NO_MESSAGE_FOR_ID, new String[] {messageId}));
		}
		else
		{
			String toLog = null;

			if (contextMessage == null || contextMessage.length() == 0)
			{
				toLog = createMessageToLog(message, parameters);
			}
			else
			{
				toLog = createMessageToLog(message + "; " + contextMessage, parameters); //$NON-NLS-1$
			}
			logbackLogger.debug(toLog);
		}

	}

	/**
	 * Creates the message that will be logged.
	 *
	 * @param message
	 *        fetched from database or null if no message could be found for {@link messageId}
	 * @param parameters
	 *        the list of parameter values that are used to substitute the placeholders ("{0}", "{1}", etc.) in the
	 *        message text from the error catalogue
	 *
	 * @return the concrete message that will be logged
	 */
	@Requirement(
		reqID = "201803")
	private String createMessageToLog(String message, Object... parameters)
	{
		String toLog = processAndBindParameters(message, parameters);
		toLog += getOriginInfo(originDepth);
		return toLog;
	}

	/**
	 * Process and bind the given message's substitution locations with the given string values.
	 *
	 * @param message
	 *        the message to be manipulated
	 * @param parameters
	 *        An array of objects to be inserted into the message
	 * @return the manipulated String
	 */
	@Requirement(
		reqID = "201958")
	private String processAndBindParameters(String message, Object... parameters)
	{
		Object[] processedParameters = null;

		if (parameters != null)
		{
			processedParameters = new String[parameters.length];
			for (int i = 0; i < processedParameters.length; i++)
			{
				processedParameters[i] = computeString(parameters[i]);
			}
		}

		return NLS.bind(message, processedParameters);
	}

	@Requirement(
		reqID = "202098")
	private String getOriginInfo(int depth)
	{
		StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
		int actDepth = depth;
		if (rte)
		{
			boolean processedRteLogger = false;
			for (int i = 0; i < stackTrace.length; i++)
			{
				String actClassNameFQ = stackTrace[i].getClassName();
				String[] split = actClassNameFQ.split("\\."); //$NON-NLS-1$
				if (split.length > 0 && split[split.length - 1].equals("RteLogger")) //$NON-NLS-1$
				{
					actDepth++;
					processedRteLogger = true;
				}
				else if (processedRteLogger)
				{
					break;
				}
			}
		}

		if (stackTrace.length > actDepth)
		{
			String[] elementsToBind = new String[4];
			elementsToBind[0] = stackTrace[actDepth].getClassName();
			elementsToBind[1] = stackTrace[actDepth].getMethodName();
			elementsToBind[2] = stackTrace[actDepth].getFileName();
			elementsToBind[3] = String.valueOf(stackTrace[actDepth].getLineNumber());

			return NLS.bind(Messages.LoggerImpl_STACK_TRACE_FORMAT, elementsToBind);
		}

		return Messages.LoggerImpl_EMPTY_STRING;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.workspace.logging.ILogger2#getLogger()
	 */
	@Override
	public Logger getLogger()
	{
		return logbackLogger;
	}

}
