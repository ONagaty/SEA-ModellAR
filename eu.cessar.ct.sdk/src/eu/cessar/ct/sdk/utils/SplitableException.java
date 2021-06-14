/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidu2337<br/>
 * Mar 6, 2014 8:31:39 AM
 * 
 * </copyright>
 */
package eu.cessar.ct.sdk.utils;

/**
 * SplitableException class.
 * 
 * @author uidu2337
 * 
 *         %created_by: uidu2337 %
 * 
 *         %date_created: Thu Mar  6 09:35:54 2014 %
 * 
 *         %version: 1 %
 */
public class SplitableException extends Exception
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5089855874605959443L;

	/**
	 * Creates a {@link SplitableException} instance.
	 */
	public SplitableException()
	{
		super();
	}

	/**
	 * Creates a {@link SplitableException} instance using the provided parameters.
	 * 
	 * @param paramString
	 * @param paramThrowable
	 */
	public SplitableException(String paramString, Throwable paramThrowable)
	{
		super(paramString, paramThrowable);
	}

	/**
	 * Creates a {@link SplitableException} instance using the provided String parameter.
	 * 
	 * @param paramString
	 */
	public SplitableException(String paramString)
	{
		super(paramString);
	}

	/**
	 * Creates a {@link SplitableException} instance using the provided {@link Throwable} parameter.
	 * 
	 * @param paramThrowable
	 */
	public SplitableException(Throwable paramThrowable)
	{
		super(paramThrowable);
	}

}
