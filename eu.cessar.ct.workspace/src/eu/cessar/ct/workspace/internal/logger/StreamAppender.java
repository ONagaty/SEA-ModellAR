/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 May 20, 2010 5:00:51 PM </copyright>
 */
package eu.cessar.ct.workspace.internal.logger;

import java.io.OutputStream;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.WriterAppender;
import eu.cessar.ct.workspace.logging.LoggingConstants;

/**
 * A stream appender simply output everything to an OutputStream. A the end of its life cycle, the output stream will
 * not be closed
 */
public class StreamAppender extends WriterAppender<ILoggingEvent>
{

	private OutputStream output;
	private int eventLevel;

	/**
	 * Creates a new StreamAppender that will allow only messages with a level matching a particular
	 * <code>eventLevel</code>.
	 * 
	 * @param eventLevel
	 *        one or more event flags or'ed together. For a list of flags check flags defined in
	 *        {@link LoggingConstants#LEVEL_DEBUG LoggingConstants.LEVEL_###}
	 * 
	 * @param eventLevel
	 */
	public StreamAppender(int eventLevel)
	{
		this.eventLevel = eventLevel;
	}

	/**
	 * @param outputStream
	 *        the output to set
	 */
	public void setOutputStream(OutputStream outputStream)
	{
		output = outputStream;
	}

	/**
	 * @return the output
	 */
	public OutputStream getOutputStream()
	{
		return output;
	}

	@Override
	public void start()
	{
		setWriter(createWriter(output));
		super.start();
	}

	/**
	 * This method overrides the parent {@link WriterAppender#closeWriter} implementation because the console stream is
	 * not ours to close.
	 */
	@Override
	protected final void closeWriter()
	{
		// do nothing, if we call super it will close the writer, it's not ours
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ch.qos.logback.core.WriterAppender#append(java.lang.Object)
	 */
	/**
	 * Checks the Level of the eventObject in order to see what kind it is (For a list of flags check flags defined in
	 * {@link LoggingConstants#LEVEL_DEBUG LoggingConstants.LEVEL_###}). If the eventObject is either of the above then
	 * it appends it to the output
	 */
	@Override
	protected void append(ILoggingEvent eventObject)
	{
		int requestedLevel = eventObject.getLevel().toInt();
		if (acceptEvent(convertToCtLevel(requestedLevel)))
		{
			super.append(eventObject);
		}
	}

	/**
	 * Convert an logback level definition to CT definition. If conversion cannot be performed INFO will be returned
	 * 
	 * @param logBackLevel
	 * @return
	 */
	private int convertToCtLevel(int logBackLevel)
	{
		switch (logBackLevel)
		{
			case Level.DEBUG_INT:
				return LoggingConstants.LEVEL_DEBUG;
			case Level.WARN_INT:
				return LoggingConstants.LEVEL_WARN;
			case Level.ERROR_INT:
				return LoggingConstants.LEVEL_ERROR;
			default:
				return LoggingConstants.LEVEL_INFO;
		}
	}

	/**
	 * Return true if this appender will accept event of a particular level, false otherwise
	 * 
	 * @param requestedLevel
	 *        one of the {@link LoggingConstants#LEVEL_DEBUG LoggingConstants.LEVEL_###} level
	 * @return true if the event is accepted, false otherwise
	 */
	public boolean acceptEvent(int requestedLevel)
	{
		return (eventLevel & requestedLevel) != 0;
	}
}
