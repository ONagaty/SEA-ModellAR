/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidw8762<br/>
 * Jun 30, 2014 12:26:52 PM
 * 
 * </copyright>
 */
package eu.cessar.ct.validation;

import org.eclipse.osgi.util.NLS;

/**
 * EMFValidationMessages class used by ValidationUtilsCommon methods.
 * 
 * @author uidw8762
 * 
 *         %created_by: uidw8762 %
 * 
 *         %date_created: Tue Jul  1 09:00:29 2014 %
 * 
 *         %version: 1 %
 */
public final class EMFValidationMessages extends NLS
{
	// CHECKSTYLE:OFF

	/** The EMF intrinsic constraints status enabled. */
	public static String EMFIntrinsicConstraintsStatusEnabled;

	/** The EMF intrinsic constraints status disabled. */
	public static String EMFIntrinsicConstraintsStatusDisabled;

	private static final String BUNDLE_NAME = "eu.cessar.ct.validation.EMFValidationMessages"; //$NON-NLS-1$

	// CHECKSTYLE:ON

	static
	{
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, EMFValidationMessages.class);
	}

	private EMFValidationMessages()
	{
	}
}