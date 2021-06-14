/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458<br/>
 * 16.10.2012 12:22:25
 * 
 * </copyright>
 */
package eu.cessar.ct.testutils.log;

import java.util.Date;

/**
 * TODO: Please comment this class
 * 
 * @author uidl6458
 * 
 *         %created_by: uidv6916 %
 * 
 *         %date_created: Tue Oct 16 18:10:08 2012 %
 * 
 *         %version: 1 %
 */
public interface ILogSession
{

	public String getSessionData();

	public Date getDate();

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj);

}
