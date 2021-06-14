/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870 04.09.2012 14:21:32 </copyright>
 */
package eu.cessar.ct.workspace.consistencycheck;

import eu.cessar.ct.workspace.xmlchecker.IXMLCheckerInconsistency;

/**
 * Enumeration for severity types assigned to {@link IXMLCheckerInconsistency}s
 * 
 * @author uidl6870
 * 
 */
public enum ESeverity
{
	/**
	 * 
	 */
	@SuppressWarnings("nls")
	ERROR("Error"),

	/**
	 * 
	 */
	WARNING("Warning");//$NON-NLS-1$ 
	private final String severity;

	ESeverity(String severity)
	{
		this.severity = severity;
	}

	/**
	 * Returns the corresponding name of the literal.
	 * 
	 * @return the name
	 */
	public String getName()
	{
		return severity;
	}

}
