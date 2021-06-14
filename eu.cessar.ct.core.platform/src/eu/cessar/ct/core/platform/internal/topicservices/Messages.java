/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidl6458 Oct 20, 2009 6:18:45 PM </copyright>
 */
package eu.cessar.ct.core.platform.internal.topicservices;

import org.eclipse.osgi.util.NLS;

/**
 * @author uidl6458
 *
 *         CHECKSTYLE:OFF
 */
@SuppressWarnings("javadoc")
public class Messages extends NLS
{
	private final static String BUNDLE_NAME = Messages.class.getCanonicalName();
	public static String NULL_TOPIC_ERROR;
	public static String NULL_DESCRIPTORID_ERROR;
	public static String NULL_SERVICEID_ERROR;
	public static String TOPIC_HAS_NO_DESCRIPTORS;
	public static String TOPIC_HAS_NO_SERVICE_FOR_GIVEN_DESCRIPTOR;
	public static String DOES_NOT_CONTAIN_GIVEN_DESCRIPTOR;
	public static String PROVIDER_DOES_NOT_EXIST;
	public static String TOPIC_HAS_NO_SERVICES;
	public static String NULL_SERVICE_CLASS;

	// CHECKSTYLE:ON

	static
	{
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

}
