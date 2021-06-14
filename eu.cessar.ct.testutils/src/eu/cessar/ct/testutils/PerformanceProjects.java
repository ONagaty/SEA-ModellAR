/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidu2337<br/>
 * Dec 6, 2013 12:11:38 PM
 * 
 * </copyright>
 */
package eu.cessar.ct.testutils;

import org.eclipse.osgi.util.NLS;

/**
 * Performance test projects.
 * 
 * @author uidu2337
 * 
 *         %created_by: uidu2337 %
 * 
 *         %date_created: Fri Dec  6 17:09:39 2013 %
 * 
 *         %version: 1 %
 */
// CHECKSTYLE:OFF
public class PerformanceProjects extends NLS
{
	public static String Volvo_VDDM;
	private static final String BUNDLE_NAME = "eu.cessar.ct.testutils.PerformanceProjects"; //$NON-NLS-1$

	// CHECKSTYLE:ON

	static
	{
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, PerformanceProjects.class);
	}

}
