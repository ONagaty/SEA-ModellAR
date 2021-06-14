/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidu3379 Sep 28, 2011 12:25:36 PM </copyright>
 */
package eu.cessar.ct.core.mms.splittable;

/**
 * Specialized exception
 * 
 * @author uidu3379
 * 
 */
public class SplittableException extends Exception
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7296802604990551620L;

	/**
	 * Creates a {@link SplittableException} instance.
	 */
	public SplittableException()
	{
		super();
	}

	/**
	 * Creates a {@link SplittableException} instance using the provided
	 * parameters.
	 * 
	 * @param paramString
	 * @param paramThrowable
	 */
	public SplittableException(String paramString, Throwable paramThrowable)
	{
		super(paramString, paramThrowable);
	}

	/**
	 * Creates a {@link SplittableException} instance using the provided String
	 * parameter.
	 * 
	 * @param paramString
	 */
	public SplittableException(String paramString)
	{
		super(paramString);
	}

	/**
	 * Creates a {@link SplittableException} instance using the provided
	 * {@link Throwable} parameter.
	 * 
	 * @param paramThrowable
	 */
	public SplittableException(Throwable paramThrowable)
	{
		super(paramThrowable);
	}

}
