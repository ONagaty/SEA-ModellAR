/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidt2045 Jun 22, 2011 4:43:23 PM </copyright>
 */
package eu.cessar.ct.core.security.internal;

/**
 * @author uidt2045
 *
 */
public class SecurityViolationError extends Error
{

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/**
	 *
	 */
	public SecurityViolationError()
	{
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 */
	public SecurityViolationError(String message, Throwable cause)
	{
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 */
	public SecurityViolationError(String message)
	{
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param cause
	 */
	public SecurityViolationError(Throwable cause)
	{
		super(cause);
		// TODO Auto-generated constructor stub
	}

}
