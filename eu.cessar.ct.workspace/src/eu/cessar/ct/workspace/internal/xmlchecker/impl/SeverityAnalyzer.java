/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870 11.09.2012 16:28:05 </copyright>
 */
package eu.cessar.ct.workspace.internal.xmlchecker.impl;

import eu.cessar.ct.workspace.consistencycheck.ESeverity;
import eu.cessar.ct.workspace.xmlchecker.IInconsistencyDetail;
import eu.cessar.ct.workspace.xmlchecker.IXMLCheckerInconsistency;

/**
 * Analyzer of {@link IXMLCheckerInconsistency}'s severity
 * 
 * @author uidl6870
 * 
 */
public final class SeverityAnalyzer
{
	private static final String UNKOWN_DEST = "UNKNOWN"; //$NON-NLS-1$

	private SeverityAnalyzer()
	{
		// hide
	}

	/**
	 * Returns the severity of the given <code>inconsistency</code>
	 * 
	 * @param inconsistency
	 * @return the severity of <code>inconsistency</code>
	 */
	public static ESeverity getSeverity(IXMLCheckerInconsistency inconsistency)
	{
		ESeverity result = ESeverity.ERROR;
		IInconsistencyDetail original = inconsistency.getDetailFromOriginalFile();
		String value = original.getItem();

		if (UNKOWN_DEST.equals(value))
		{
			result = ESeverity.WARNING;
		}

		return result;
	}
}
