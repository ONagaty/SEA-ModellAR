/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidl6458 Oct 14, 2009 4:29:18 PM </copyright>
 */
package eu.cessar.ct.runtime.internal;

import org.eclipse.osgi.util.NLS;

/**
 * @author uidl6458
 *
 */
public class Messages extends NLS
{
	// CHECKSTYLE:OFF
	/**
	 * Updating class path
	 */
	public static String CessarRuntime_updateClassPathJob;

	/**
	 * The bundle with symbolic name {0} could not be resolved
	 */
	public static String UnresolvedBundle;

	/**
	 * Unable to resolve {0} URL
	 */
	public static String UnableToResolveURL;

	/**
	 * Invalid header value {0}
	 */
	public static String InvalidHeaderValue;

	/**
	 * Invalid extension class, expected {0} got {1}
	 */
	public static String InvalidExtension;

	/**
	 * Pluget parameters should be null or a String[] but is a {0}
	 */
	public static String InvalidPlugetParameter;
	// CHECKSTYLE:ON

	private static final String BUNDLE_NAME = Messages.class.getCanonicalName();

	/**
	 * The VM parameter Xms detailed_description.
	 */
	public static String VMArguments_Xms_detailed_description;

	/**
	 * The VM parameter Xmx detailed_description.
	 */
	public static String VMArguments_Xmx_detailed_description;

	/**
	 * The VM parameter Xss detailed_description.
	 */
	public static String VMArguments_Xss_detailed_description;

	static
	{
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

}
