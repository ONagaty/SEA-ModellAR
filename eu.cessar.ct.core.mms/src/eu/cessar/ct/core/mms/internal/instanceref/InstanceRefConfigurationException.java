/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870 Oct 5, 2011 11:09:49 AM </copyright>
 */
package eu.cessar.ct.core.mms.internal.instanceref;

/**
 * Exception indicating that an error has occurred during the computation of candidates for the configuration of an
 * instance reference.
 * 
 * @author uidl6870
 * 
 */
public class InstanceRefConfigurationException extends Exception
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @param msg
	 */
	public InstanceRefConfigurationException(String msg)
	{
		super(msg);
	}
}
