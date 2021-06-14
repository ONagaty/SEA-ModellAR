/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidu2337<br/>
 * Nov 13, 2013 5:41:52 PM
 * 
 * </copyright>
 */
package eu.cessar.ct.sdk.runtime;

/**
 * This runtime exception is intended to be thrown when a resource saving failure occurs. It is meant to be catchable in
 * user code (plugets) that may encounter issues saving changed resources (e.g. read-only files).
 * 
 * @author uidu2337
 * 
 *         %created_by: uidu2337 %
 * 
 *         %date_created: Thu Nov 28 17:02:28 2013 %
 * 
 *         %version: 1 %
 */
public class CessarSaveException extends RuntimeException
{
	private static final long serialVersionUID = 7213561755706058082L;

	/**
	 * @param message
	 */
	public CessarSaveException(String message)
	{
		super(message);
	}
}
