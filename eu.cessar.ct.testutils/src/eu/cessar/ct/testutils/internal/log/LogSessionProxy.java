/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458<br/>
 * 16.10.2012 12:23:52
 * 
 * </copyright>
 */
package eu.cessar.ct.testutils.internal.log;

import java.util.Date;

import org.eclipse.ui.internal.views.log.LogSession;

import eu.cessar.ct.testutils.log.ILogSession;

/**
 * TODO: Please comment this class
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
public class LogSessionProxy implements ILogSession
{

	private final LogSession toProxy;

	public LogSessionProxy(LogSession toProxy)
	{
		this.toProxy = toProxy;
	}

	public Date getDate()
	{
		return toProxy.getDate();
	}

	public String getSessionData()
	{
		return toProxy.getSessionData();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (obj instanceof LogSessionProxy)
		{
			return toProxy == ((LogSessionProxy) obj).toProxy;
		}
		else
		{
			return false;
		}
	}
}
