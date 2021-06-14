/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Mar 19, 2010 5:33:00 PM </copyright>
 */
package eu.cessar.ct.sdk.pm;

/**
 * 
 */
public class PMRuntimeException extends RuntimeException
{
	/**
	 * For serialization purposes.
	 */
	private static final long serialVersionUID = 921975372014385899L;

	/**
	 * Default constructor.
	 */
	public PMRuntimeException()
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
	public PMRuntimeException(String message, Throwable cause)
	{
		super(message, cause);
	}

	/**
	 * Message-based constructor.
	 * 
	 * @param message
	 *        exception message
	 */
	public PMRuntimeException(String message)
	{
		super(message);
	}

	/**
	 * @param cause
	 *        actual cause
	 */
	public PMRuntimeException(Throwable cause)
	{
		super(cause);
	}
}
