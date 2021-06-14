/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidt2045 Jul 11, 2012 3:53:14 PM </copyright>
 */
package eu.cessar.ct.validation;

import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.BasicDiagnostic;
import org.eclipse.emf.common.util.Diagnostic;

/**
 * @author uidt2045
 * 
 */
public class FeatureBoundDiagnostic extends BasicDiagnostic
{
	private List<String> features;

	public FeatureBoundDiagnostic()
	{
		super();
	}

	public FeatureBoundDiagnostic(String source, int code, String message, Object[] data)
	{
		super(source, code, message, data);
	}

	public FeatureBoundDiagnostic(int severity, String source, List<String> features, int code,
		String message, Object[] data)
	{
		super(severity, source, code, message, data);
		this.features = features;
	}

	public FeatureBoundDiagnostic(String source, int code, List<? extends Diagnostic> children,
		String message, Object[] data)
	{
		super(source, code, children, message, data);
	}

	public List<String> getFeatures()
	{
		if (features == null)
		{
			return Collections.emptyList();
		}
		else
		{
			return features;
		}
	}

}
