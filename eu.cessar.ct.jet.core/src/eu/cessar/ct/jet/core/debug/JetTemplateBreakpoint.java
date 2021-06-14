/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl7321 Jun 4, 2010 4:09:57 PM </copyright>
 */
package eu.cessar.ct.jet.core.debug;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.model.IBreakpoint;
import org.eclipse.debug.core.model.LineBreakpoint;

import eu.cessar.ct.jet.core.JETCoreConstants;

/**
 * @author uidl7321
 * 
 */
public class JetTemplateBreakpoint extends LineBreakpoint
{
	private IBreakpoint correspondingJavaBreakpoint;

	public JetTemplateBreakpoint()
	{
		super();
	}

	public JetTemplateBreakpoint(final IFile resource, final int lineNumber,
		final IBreakpoint correspondingJavaBreakpoint) throws CoreException
	{
		super();
		setUp(resource, lineNumber);
		this.correspondingJavaBreakpoint = correspondingJavaBreakpoint;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.debug.core.model.IBreakpoint#getModelIdentifier()
	 */
	public String getModelIdentifier()
	{
		return JETCoreConstants.JET_DEBUG_MODEL_ID;
	}

	public IBreakpoint getCorrespondingJavaBreakpoint()
	{
		return this.correspondingJavaBreakpoint;
	}

	void setCorrespondingJavaBreakpoint(final IBreakpoint javaBp)
	{
		this.correspondingJavaBreakpoint = javaBp;
	}

	public int getJETLineNumber() throws CoreException
	{
		return super.getLineNumber() - 1;
	}

	// public int getJetLineNumber() throws CoreException {
	// return lineNumber + 1;
	// }

	private void setUp(final IFile resource, final int lineNumber) throws CoreException
	{
		IMarker marker = resource.createMarker(JETCoreConstants.JET_DEBUG_MARKER_TYPE);
		marker.setAttribute(IMarker.LINE_NUMBER, lineNumber + 1);
		marker.setAttribute(IBreakpoint.ID, getModelIdentifier());
		setMarker(marker);
		setEnabled(true);
	}

}
