package eu.cessar.ct.workspace.logging;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import eu.cessar.ct.sdk.logging.ILogger;

/**
 * Logger interface for CESSAR-CT components. This interface extends the public interface {@link ILogger} with
 * additional methods that allow more actions on logger instances.
 */
public interface ILogger2 extends ILogger
{
	/**
	 * @return logger
	 */
	public Logger getLogger();

	/**
	 * @param listener
	 */
	public void addListener(ILogEventListener listener);

	/**
	 * @param listener
	 */
	public void removeListener(ILogEventListener listener);

	/**
	 * Create a logger pattern instance, for current logger instance and based on specified pattern string.
	 *
	 * @param pattern
	 *        the string describing the pattern to be used
	 * @return A {@link PatternLayout} instance based on specified string and in context of current logger instance.
	 */
	public PatternLayout createPattern(String pattern);

	/**
	 * Allow clients to attach an {@link Appender} instance to current logger.
	 *
	 * @param appender
	 *        the {@link Appender} instance to be attached to this logger.
	 */
	public void attachAppender(Appender<ILoggingEvent> appender);

	/**
	 * Attaches the OutputStream with the given eventLevel to the logger
	 *
	 * @param stream
	 *        Represents the Output stream that will be sent to the Console
	 * @param eventLevel
	 *        one or more event flags or'ed together. For a list of flags check flags defined in
	 *        {@link LoggingConstants#LEVEL_DEBUG LoggingConstants.LEVEL_###}.
	 */
	public void attachStream(OutputStream stream, int eventLevel);

	/**
	 * Attaches the OutputStream with the given eventLevel to the logger and sets the specified pattern to it
	 *
	 * @param stream
	 *        Represents the Output stream that will be sent to the Console
	 * @param pattern
	 *        Represents the Pattern of the OutputStream
	 * @param eventLevel
	 *        one or more event flags or'ed together. For a list of flags check flags defined in
	 *        {@link LoggingConstants#LEVEL_DEBUG LoggingConstants.LEVEL_###}.
	 */
	public void attachStream(OutputStream stream, String pattern, int eventLevel);

	/**
	 * Allow clients to detach an {@link Appender} instance from current logger.
	 *
	 * @param appender
	 *        the {@link Appender} instance to be detached from this logger.
	 */
	public void detachAppender(Appender<ILoggingEvent> appender);

	/**
	 * Return the streams that are currently in use by the logger if any
	 *
	 * @return CurrentStreams
	 */
	public List<OutputStream> getCurrentStreams();

	/**
	 * Return the streams that are currently in use by a particular event level
	 *
	 * @param eventLevel
	 *        one or more event flags or'ed together. For a list of flags check flags defined in
	 *        {@link LoggingConstants#LEVEL_DEBUG LoggingConstants.LEVEL_###}.
	 * @return a list of streams, never null
	 */
	public List<OutputStream> getCurrentStreams(int eventLevel);

	/**
	 * Flush all output streams
	 *
	 * @throws IOException
	 *         if errors occurs
	 */
	public void flushStreams() throws IOException;

	/**
	 * The method that allow to dispose this logger instance.
	 */
	public void dispose();

	/**
	 * Clear the console, if possible
	 */
	public void clear();
}
