/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidt2045 Jul 11, 2012 4:39:44 PM </copyright>
 */
package eu.cessar.ct.validation;

import org.eclipse.emf.common.util.BasicDiagnostic;
import org.eclipse.emf.common.util.Diagnostic;

/**
 * @author uidt2045
 * 
 *         This diagnostic exists only to notify the user about the validation
 *         mode flag (is the validation live, on demand or inactive) <br>
 *         By default it is a warning
 */
public class ValidationModeDiagnostic extends BasicDiagnostic
{
	public ValidationModeDiagnostic(int severity, String source, int code, String message,
		Object[] data)
	{
		super(severity, source, code, message, data);
	}

	public ValidationModeDiagnostic(String source, int code, String message, Object[] data)
	{
		super(Diagnostic.WARNING, source, code, message, data);
	}
}
