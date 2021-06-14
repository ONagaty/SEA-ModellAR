/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458<br/>
 * 16.09.2013 11:15:18
 * 
 * </copyright>
 */
package eu.cessar.ct.test.annotations;

import java.lang.annotation.Annotation;

/**
 * TODO: Please comment this class
 * 
 * @author uidl6458
 * 
 *         %created_by: uidl6458 %
 * 
 *         %date_created: Wed May 21 18:47:32 2014 %
 * 
 *         %version: 1 %
 */
public class ExpectedFailureImpl implements ExpectedFailure
{

	private Class<? extends Throwable> expected;
	private String message;
	private String crInfo;

	public ExpectedFailureImpl(Class<? extends Throwable> expected, String message, String crInfo)
	{
		this.expected = expected;
		this.message = message;
		this.crInfo = crInfo;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.annotation.Annotation#annotationType()
	 */
	public Class<? extends Annotation> annotationType()
	{
		return ExpectedFailure.class;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.test.annotations.ExpectedFailure#expected()
	 */
	public Class<? extends Throwable> expected()
	{
		return expected;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.test.annotations.ExpectedFailure#message()
	 */
	public String message()
	{
		return message;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.test.annotations.ExpectedFailure#CRInfo()
	 */
	public String CRInfo()
	{
		return crInfo;
	}

}
