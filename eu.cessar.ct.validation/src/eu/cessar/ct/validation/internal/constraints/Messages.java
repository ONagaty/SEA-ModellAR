/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidj9791<br/>
 * Apr 18, 2016 3:16:22 PM
 *
 * </copyright>
 */
package eu.cessar.ct.validation.internal.constraints;

import org.eclipse.osgi.util.NLS;

/**
 * Validation externalizations
 *
 * @author uidj9791
 *
 *         %created_by: created by %
 *
 *         %date_created: creation date %
 *
 *         %version: version %
 */
public final class Messages extends NLS
{
	private static final String BUNDLE_NAME = "eu.cessar.ct.validation.internal.constraints.messages"; //$NON-NLS-1$

	// CHECKSTYLE:OFF
	/**
	 * Error message to be displayed if the postBuild constraints are not accomplished
	 */
	public static String postBuildChangeableCorrectnessContraint;
	/**
	 * Error message to be displayed if the non-postBuild constraints are not accomplished for parameters
	 */
	public static String parameterErrorMessage;
	/**
	 * Error message to be displayed if the non-postBuild constraints are not accomplished for references
	 */
	public static String referenceErrorMessage;

	// CHECKSTYLE:ON
	static
	{
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages()
	{
	}
}
