/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidu2337<br/>
 * Aug 13, 2014 4:56:31 PM
 * 
 * </copyright>
 */
package eu.cessar.ct.sdk.pm;

/**
 * Runtime exception thrown by various PM (presentation model) operations in the splitable context.
 * 
 * @author uidu2337
 * 
 *         %created_by: uidu2337 %
 * 
 *         %date_created: Mon Aug 18 10:55:01 2014 %
 * 
 *         %version: 1 %
 */
public class PMInvalidSplitException extends PMRuntimeException
{
	/**
	 * For serialization purposes.
	 */
	private static final long serialVersionUID = 3355282638881079110L;

	/**
	 * Default constructor.
	 */
	public PMInvalidSplitException()
	{
		super();
	}

	/**
	 * Message and cause based constructor.
	 * 
	 * @param message
	 *        exception message
	 * @param cause
	 *        actual cause
	 */
	public PMInvalidSplitException(String message, Throwable cause)
	{
		super(message, cause);
	}

	/**
	 * Message-based constructor.
	 * 
	 * @param message
	 *        exception message
	 */
	public PMInvalidSplitException(String message)
	{
		super(message);
	}

	/**
	 * Cause-based constructor.
	 * 
	 * @param cause
	 *        actual cause
	 */
	public PMInvalidSplitException(Throwable cause)
	{
		super(cause);
	}
}
