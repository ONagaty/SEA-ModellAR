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
package eu.cessar.ct.workspace.internal.jvmparameters;

import eu.cessar.ct.workspace.internal.Messages;
import eu.cessar.req.Requirement;

/**
 * JVMConstants class.
 *
 * @author uidw8762
 *
 *         %created_by: uidw8762 %
 *
 *         %date_created: Mon Apr 14 14:38:37 2014 %
 *
 *         %version: 2 %
 */
@Requirement(
	reqID = "REQ_WORKSP#ACTIVE_JVM#2")
public final class JVMConstants
{
	private JVMConstants()
	{
	}

	// CHECKSTYLE:OFF
	/** The Constant JVM_PARAM_XMX of JVM parameter for defining the maximum heap size */
	public static final String JVM_PARAM_XMX = "Xmx"; //$NON-NLS-1$

	/** The Constant JVM_PARAM_XSS of JVM parameter for defining the stack size */
	public static final String JVM_PARAM_XSS = "Xss"; //$NON-NLS-1$

	/** The Constant JVMParameterList. */
	public static final String[] JVMParameterList = new String[] {JVM_PARAM_XMX, JVM_PARAM_XSS};

	/** The Constant JVMParameterDescriptionList. */
	public static final String[] JVMParameterDescriptionList = new String[] {
		Messages.JVMParameters_Xmx_detailed_description, Messages.JVMParameters_Xss_detailed_description};
	// CHECKSTYLE:ON
}
