/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870<br/>
 * 28.08.2013 14:44:53
 * 
 * </copyright>
 */
package eu.cessar.ct.sdk.sea.util;

import org.eclipse.osgi.util.NLS;

import eu.cessar.ct.sdk.sea.ISEAObject;

/**
 * Exception indicating that no definition has been found.
 * 
 * @author uidl6870
 * 
 *         %created_by: uidl6870 %
 * 
 *         %date_created: Thu Aug 29 15:57:43 2013 %
 * 
 *         %version: 1 %
 */
public class SEANoDefinitionFoundException extends AbstractSEARuntimeException
{
	private static final String MSG = "No definition found for {0} in {1}!"; //$NON-NLS-1$

	private static final long serialVersionUID = 1L;

	/**
	 * @param parent
	 *        the object on which the method was executed
	 * @param defName
	 *        the used definition name
	 */
	public SEANoDefinitionFoundException(ISEAObject parent, String defName)
	{
		super(NLS.bind(MSG, new Object[] {defName, parent.toString()}));
	}

}
