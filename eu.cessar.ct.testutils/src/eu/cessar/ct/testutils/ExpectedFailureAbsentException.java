/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidu2337<br/>
 * May 13, 2013 9:17:08 AM
 * 
 * </copyright>
 */
package eu.cessar.ct.testutils;

/**
 * Exception is used if a test with an expected Throwable does not fail.
 * 
 * @author uidu2337
 * 
 *         %created_by: uidu2337 %
 * 
 *         %date_created: Thu May 16 09:29:58 2013 %
 * 
 *         %version: 1 %
 */
public class ExpectedFailureAbsentException extends Exception
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6501438950736208238L;

	/**
	 * Default constructor
	 */
	public ExpectedFailureAbsentException()
	{
		super();
	}

	/**
	 * Constructs a ExpectedFailureAbsentException with the specified detail message.
	 * 
	 * @param message
	 *        the message
	 */
	public ExpectedFailureAbsentException(final String message)
	{
		super(message);
	}

	/**
	 * Constructs a ExpectedFailureAbsentException with the specified detail message and cause.
	 * 
	 * @param message
	 *        the message
	 * @param cause
	 *        the cause
	 */
	public ExpectedFailureAbsentException(final String message, final Throwable cause)
	{
		super(message, cause);
	}

	/**
	 * Constructs a ExpectedFailureAbsentException with the specified cause.
	 * 
	 * @param cause
	 *        the cause
	 */
	public ExpectedFailureAbsentException(final Throwable cause)
	{
		super(cause);
	}
}
