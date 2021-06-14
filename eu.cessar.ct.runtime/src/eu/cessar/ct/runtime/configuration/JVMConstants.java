/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidw8762<br/>
 * Apr 2, 2014 1:21:05 PM
 *
 * </copyright>
 */
package eu.cessar.ct.runtime.configuration;

import eu.cessar.ct.runtime.internal.Messages;

/**
 * VMConstants class.
 *
 * @author uidw8762
 *
 *         %created_by: uidw8762 %
 *
 *         %date_created: Mon Apr 7 17:35:44 2014 %
 *
 *         %version: 1 %
 */
public final class JVMConstants
{
	private JVMConstants()
	{
	}

	// CHECKSTYLE:OFF
	/** The Constant JVM_ARGS_XMS of JVM parameter for defining the minimum heap size */
	public static final String JVM_ARGS_XMS = "Xms"; //$NON-NLS-1$

	/** The Constant JVM_ARGS_XMX of JVM parameter for defining the maximum heap size */
	public static final String JVM_ARGS_XMX = "Xmx"; //$NON-NLS-1$

	/** The Constant JVM_ARGS_XSS of JVM parameter for defining the stack size */
	public static final String JVM_ARGS_XSS = "Xss"; //$NON-NLS-1$

	public static final String[] JVMParameterList = new String[] {JVM_ARGS_XMS, JVM_ARGS_XMX, JVM_ARGS_XSS};
	public static final String[] JVMParameterDescriptionList = new String[] {
		Messages.VMArguments_Xms_detailed_description, Messages.VMArguments_Xmx_detailed_description,
		Messages.VMArguments_Xss_detailed_description};
	// CHECKSTYLE:ON
}
