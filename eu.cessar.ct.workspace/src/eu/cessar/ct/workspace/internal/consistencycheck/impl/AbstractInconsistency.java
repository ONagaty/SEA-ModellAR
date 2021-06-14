/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl7321<br/>
 * Feb 27, 2014 1:48:34 PM
 * 
 * </copyright>
 */
package eu.cessar.ct.workspace.internal.consistencycheck.impl;

import eu.cessar.ct.workspace.consistencycheck.ESeverity;
import eu.cessar.ct.workspace.consistencycheck.IInconsistency;

/**
 * Abstract implementation of {@link IInconsistency}.
 * 
 * @author uidl7321
 * 
 *         %created_by: uidl7321 %
 * 
 *         %date_created: Thu Feb 27 14:18:35 2014 %
 * 
 *         %version: 1 %
 */
public abstract class AbstractInconsistency implements IInconsistency
{
	private String message;
	private ESeverity severity;

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.workspace.xmlchecker.IInconsistency#getMessage()
	 */
	public String getMessage()
	{
		return message;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.workspace.xmlchecker.IInconsistency#setMessage(java.lang.String)
	 */
	public void setMessage(String message)
	{
		this.message = message;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.workspace.xmlchecker.IInconsistency#getSeverity()
	 */
	public ESeverity getSeverity()
	{
		return severity;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.workspace.xmlchecker.IInconsistency#setSeverity(ESeverity)
	 */
	public void setSeverity(ESeverity severity)
	{
		this.severity = severity;
	}

}
