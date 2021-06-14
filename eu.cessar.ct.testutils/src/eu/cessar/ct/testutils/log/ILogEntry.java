/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458<br/>
 * 16.10.2012 12:22:14
 * 
 * </copyright>
 */
package eu.cessar.ct.testutils.log;

import java.util.Date;

/**
 * Encapsulates a log entry
 * 
 * @author uidl6458
 * 
 *         %created_by: uidv6916 %
 * 
 *         %date_created: Tue Oct 16 18:10:08 2012 %
 * 
 *         %version: 1 %
 */
public interface ILogEntry
{

	/**
	 * Returns the code for this entry
	 * 
	 * @return the code for this entry
	 */
	public int getCode();

	/**
	 * Returns the date for this entry or the epoch if the current date value is <code>null</code>
	 * 
	 * @return the entry date or the epoch if there is no date entry
	 */
	public Date getDate();

	/**
	 * Returns a pretty-print formatting for the date for this entry
	 * 
	 * @return the formatted date for this entry
	 */
	public String getFormattedDate();

	/**
	 * Returns the message for this entry or <code>null</code> if there is no message
	 * 
	 * @return the message or <code>null</code>
	 */
	public String getMessage();

	/**
	 * Returns the id of the plugin that generated this entry
	 * 
	 * @return the plugin id of this entry
	 */
	public String getPluginId();

	/**
	 * Returns the severity of this entry.
	 * 
	 * @return the severity
	 * @see IStatus#OK
	 * @see IStatus#WARNING
	 * @see IStatus#INFO
	 * @see IStatus#ERROR
	 */
	public int getSeverity();

	/**
	 * Returns the {@link ILogSession} for this entry or the parent {@link LogSession} iff:
	 * <ul>
	 * <li>The session is <code>null</code> for this entry</li>
	 * <li>The parent of this entry is not <code>null</code> and is a {@link LogEntry}</li>
	 * </ul>
	 * 
	 * @return the {@link ILogSession} for this entry
	 */
	public ILogSession getSession();

	/**
	 * Returns the human-readable text representation of the integer severity value or '<code>?</code>' if the severity
	 * is unknown.
	 * 
	 * @return the text representation of the severity
	 */
	public String getSeverityText();

	/**
	 * Returns the stack trace for this entry or <code>null</code> if there is no stack trace
	 * 
	 * @return the stack trace or <code>null</code>
	 */
	public String getStack();

	/**
	 * Returns if the severity of this entry is {@link IStatus#OK}
	 * 
	 * @return if the entry is OK or not
	 */
	public boolean isOK();

}
