/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870<br/>
 * Jun 1, 2013 10:09:32 AM
 * 
 * </copyright>
 */
package eu.cessar.ct.sdk.sea.util;

import eu.cessar.req.Requirement;

/**
 * Exception indicating that a write operation using SEA, has failed.
 * 
 * @author uidl6870
 * 
 *         %created_by: uidl6870 %
 * 
 *         %date_created: Thu Aug 29 15:57:44 2013 %
 * 
 *         %version: 1 %
 */
@Requirement(
	reqID = "REQ_API#SEA#11")
public class SEAWriteOperationException extends RuntimeException
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @param message
	 * @param cause
	 */
	public SEAWriteOperationException(String message, Throwable cause)
	{
		super(message, cause);
	}

	/**
	 * @param message
	 */
	public SEAWriteOperationException(String message)
	{
		this(message, null);
	}

	/**
	 * @param cause
	 */
	public SEAWriteOperationException(Throwable cause)
	{
		super(cause);
	}
}
