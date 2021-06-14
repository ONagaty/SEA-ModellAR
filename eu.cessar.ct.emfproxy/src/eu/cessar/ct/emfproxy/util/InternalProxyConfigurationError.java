/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Nov 30, 2009 4:19:32 PM </copyright>
 */
package eu.cessar.ct.emfproxy.util;

/**
 * @author uidl6458
 * 
 */
public class InternalProxyConfigurationError extends Error
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7596346375770555553L;

	/**
	 * 
	 */
	public InternalProxyConfigurationError()
	{
		super();
	}

	/**
	 * @param message
	 */
	public InternalProxyConfigurationError(Throwable t)
	{
		super(t);
	}

	/**
	 * @param format
	 */
	public InternalProxyConfigurationError(String message)
	{
		super(message);
	}

	/**
	 * @param condition
	 */
	public static void assertTrue(boolean condition)
	{
		if (!condition)
		{
			throw new InternalProxyConfigurationError();
		}
	}

	/**
	 * @param condition
	 */
	public static void assertTrue(boolean condition, String message)
	{
		if (!condition)
		{
			throw new InternalProxyConfigurationError(message);
		}
	}
}
