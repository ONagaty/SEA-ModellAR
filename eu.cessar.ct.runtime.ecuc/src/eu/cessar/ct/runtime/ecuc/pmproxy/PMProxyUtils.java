/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Jan 6, 2011 12:48:27 PM </copyright>
 */
package eu.cessar.ct.runtime.ecuc.pmproxy;

import eu.cessar.ct.emfproxy.IEMFProxyEngine;

/**
 * 
 */
public class PMProxyUtils
{

	private static final String PARAM_REFINEMENT_SUPPORT = "REFINEMENT_SUPPORT"; //$NON-NLS-1$

	/**
	 * 
	 */
	private PMProxyUtils()
	{
		// do nothing
	}

	/**
	 * Return true if the underling project have support for the refinement
	 * concept. By default, all projects have except the pre CESSAR-CT 4.x
	 * compatible ones
	 * 
	 * @param engine
	 * @return
	 */
	public static boolean haveRefinementSupport(IEMFProxyEngine engine)
	{
		String value = engine.getParameterValue(PARAM_REFINEMENT_SUPPORT);
		if (value != null)
		{
			return Boolean.valueOf(value);
		}
		else
		{
			return true;
		}
	}
}
