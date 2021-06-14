/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458<br/>
 * 16.10.2012 12:23:40
 * 
 * </copyright>
 */
package eu.cessar.ct.testutils.internal.log;

import java.util.Date;

import org.eclipse.ui.internal.views.log.LogEntry;

import eu.cessar.ct.testutils.log.ILogEntry;
import eu.cessar.ct.testutils.log.ILogSession;

/**
 * Wrapper for the LogEntry
 * 
 * @author uidl6458
 * 
 *         %created_by: uidv6916 %
 * 
 *         %date_created: Tue Oct 16 18:10:07 2012 %
 * 
 *         %version: 1 %
 */
@SuppressWarnings("restriction")
public class LogEntryProxy implements ILogEntry
{

	private final LogEntry toProxy;

	public LogEntryProxy(LogEntry toProxy)
	{
		this.toProxy = toProxy;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.testutils.log.ILogEntry#getCode()
	 */
	public int getCode()
	{
		return toProxy.getCode();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.testutils.log.ILogEntry#getDate()
	 */
	public Date getDate()
	{
		return toProxy.getDate();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.testutils.log.ILogEntry#getFormattedDate()
	 */
	public String getFormattedDate()
	{
		return toProxy.getFormattedDate();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.testutils.log.ILogEntry#getMessage()
	 */
	public String getMessage()
	{
		return toProxy.getMessage();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.testutils.log.ILogEntry#getPluginId()
	 */
	public String getPluginId()
	{
		return toProxy.getPluginId();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.testutils.log.ILogEntry#getSeverity()
	 */
	public int getSeverity()
	{
		return toProxy.getSeverity();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.testutils.log.ILogEntry#getSession()
	 */
	public ILogSession getSession()
	{
		return new LogSessionProxy(toProxy.getSession());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.testutils.log.ILogEntry#getSeverityText()
	 */
	public String getSeverityText()
	{
		return toProxy.getSeverityText();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.testutils.log.ILogEntry#getStack()
	 */
	public String getStack()
	{
		return toProxy.getStack();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.testutils.log.ILogEntry#isOK()
	 */
	public boolean isOK()
	{
		return toProxy.isOK();
	}

}
