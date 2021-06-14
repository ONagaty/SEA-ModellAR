/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870<br/>
 * Jan 15, 2014 11:57:46 AM
 * 
 * </copyright>
 */
package eu.cessar.ct.sdk.sea.util;

import org.eclipse.osgi.util.NLS;

import eu.cessar.ct.sdk.sea.ISEAConfig;
import gautosar.gecucdescription.GModuleConfiguration;

/**
 * Exception indicating that an attempt has been made to set the active configuration to a wrong value
 * 
 * @author uidl6870
 * 
 *         %created_by: uidl6870 %
 * 
 *         %date_created: Mon Jan 20 14:01:33 2014 %
 * 
 *         %version: 1 %
 */
public class SEAWrongActiveConfigurationException extends AbstractSEARuntimeException
{
	private static final String MSG = "{0} is not a valid active configuration for {1}!"; //$NON-NLS-1$

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @param seaConfiguration
	 * @param activeConfiguration
	 */
	public SEAWrongActiveConfigurationException(ISEAConfig seaConfiguration, GModuleConfiguration activeConfiguration)
	{
		super(NLS.bind(MSG, new Object[] {activeConfiguration, seaConfiguration}));
	}

}
