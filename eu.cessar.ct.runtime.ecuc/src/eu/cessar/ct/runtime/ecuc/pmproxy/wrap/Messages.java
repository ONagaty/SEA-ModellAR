/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidg4020<br/>
 * Sep 30, 2014 2:09:00 PM
 * 
 * </copyright>
 */
package eu.cessar.ct.runtime.ecuc.pmproxy.wrap;

import org.eclipse.osgi.util.NLS;

/**
 * Error messages
 * 
 * @author uidg4020
 * 
 *         %created_by: uidg4020 %
 * 
 *         %date_created: Sat May  9 15:18:49 2015 %
 * 
 *         %version: 3 %
 */
public final class Messages extends NLS
{
	private static final String BUNDLE_NAME = "eu.cessar.ct.runtime.ecuc.pmproxy.wrap.messages"; //$NON-NLS-1$
	/**
	 * Error thrown if a project cannot have an ecuc model
	 */
	public static String NO_ECUC_MODEL_ERROR;
	/** 
	 */
	public static String cannotChangeSplitFeatureException;
	public static String COULD_NOT_SET_CONTAINER;
	static
	{
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}
}
