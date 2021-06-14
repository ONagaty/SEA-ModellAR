/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458<br/>
 * 31.05.2013 15:09:54
 * 
 * </copyright>
 */
package eu.cessar.ct.core.platform.emf;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.common.util.BasicDiagnostic;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecore.resource.Resource;

import eu.cessar.ct.core.internal.platform.CessarPluginActivator;

/**
 * Various utilities regarding {@link Diagnostic}
 * 
 * @author uidl6458
 * 
 *         %created_by: uidl6458 %
 * 
 *         %date_created: Mon Jun  3 10:38:54 2013 %
 * 
 *         %version: 1 %
 */
public final class DiagnosticUtils
{

	private DiagnosticUtils()
	{
		// avoid instance
	}

	/**
	 * Create a diagnostic from a {@link IMarker resource marker}
	 * 
	 * @param marker
	 *        a valid resource marker
	 * @return a newly created Diagnostic
	 */
	public static Diagnostic fromMarker(IMarker marker)
	{
		String message;
		try
		{
			// avoid null handling
			message = "" + marker.getAttribute(IMarker.MESSAGE); //$NON-NLS-1$
		}
		catch (CoreException e)
		{
			message = "Cannot retrieve marker message: " + e.getMessage(); //$NON-NLS-1$
			CessarPluginActivator.getDefault().logError(e);
		}
		int mSeverity = marker.getAttribute(IMarker.SEVERITY, IMarker.SEVERITY_INFO);
		String source = "Unknown"; //$NON-NLS-1$
		try
		{
			source = marker.getType();
		}
		catch (CoreException e)
		{
			CessarPluginActivator.getDefault().logError(e);
		}
		int severity;
		switch (mSeverity)
		{
			case IMarker.SEVERITY_INFO:
				severity = Diagnostic.INFO;
				break;
			case IMarker.SEVERITY_WARNING:
				severity = Diagnostic.WARNING;
				break;
			default:
				severity = Diagnostic.ERROR;
		}
		return new BasicDiagnostic(severity, source, (int) marker.getId(), message, null);
	}

	/**
	 * Create a diagnostic from a {@link Resource.Diagnostic EMF Resource diagnostic}
	 * 
	 * @param severity
	 *        one of the allowed Diagnostic severities
	 * @param diagnostic
	 *        a valid EMF resource diagnostic
	 * @return a newly created Diagnostic
	 */
	public static Diagnostic fromResourceDiagnostic(int severity, Resource.Diagnostic diagnostic)
	{
		return new BasicDiagnostic(severity, diagnostic.getLocation(), 0, diagnostic.toString(), null);
	}
}
