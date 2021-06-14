/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870<br/>
 * Jan 15, 2014 12:02:03 PM
 * 
 * </copyright>
 */
package eu.cessar.ct.sdk.sea.util;

import org.eclipse.osgi.util.NLS;

import eu.cessar.ct.sdk.sea.ISEAContainer;
import gautosar.gecucdescription.GContainer;

/**
 * Exception indicating that an attempt has been made to set the active container to a wrong value
 * 
 * @author uidl6870
 * 
 *         %created_by: uidl6870 %
 * 
 *         %date_created: Mon Jan 20 14:01:33 2014 %
 * 
 *         %version: 1 %
 */
public class SEAWrongActiveContainerException extends AbstractSEARuntimeException
{
	private static final String MSG = "{0} is not a valid active container for {1}!"; //$NON-NLS-1$
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @param seaContainer
	 * @param activeContainer
	 */
	public SEAWrongActiveContainerException(ISEAContainer seaContainer, GContainer activeContainer)
	{
		super(NLS.bind(MSG, new Object[] {activeContainer, seaContainer}));
	}

}
