/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870<br/>
 * 29.08.2013 11:54:50
 * 
 * </copyright>
 */
package eu.cessar.ct.sdk.sea.util;

/**
 * 
 * Base implementation of all SEA unchecked exceptions
 * 
 * @author uidl6870
 * 
 *         %created_by: uidl6870 %
 * 
 *         %date_created: Thu Aug 29 15:57:42 2013 %
 * 
 *         %version: 1 %
 */
public abstract class AbstractSEARuntimeException extends RuntimeException
{
	private static final long serialVersionUID = 1L;

	/**
	 * @param msg
	 */
	public AbstractSEARuntimeException(String msg)
	{
		super(msg);
	}

}
