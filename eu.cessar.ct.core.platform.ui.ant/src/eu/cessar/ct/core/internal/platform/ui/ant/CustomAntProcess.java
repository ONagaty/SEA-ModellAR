/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl7321 Jun 28, 2010 5:15:01 PM </copyright>
 */
package eu.cessar.ct.core.internal.platform.ui.ant;

import java.util.Map;

import org.eclipse.ant.internal.launching.launchConfigurations.AntProcess;
import org.eclipse.debug.core.ILaunch;

/**
 * @author uidl7321
 * 
 */
@SuppressWarnings("restriction")
public class CustomAntProcess extends AntProcess
{
	private int exitCode;

	public CustomAntProcess(String label, ILaunch launch, Map attributes)
	{
		super(label, launch, attributes);
	}

	protected void terminated(int exit)
	{
		super.terminated();
		exitCode = exit;
	}

	@Override
	protected void terminated()
	{
		super.terminated();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ant.internal.ui.launchConfigurations.AntProcess#getExitValue()
	 */
	@Override
	public int getExitValue()
	{
		return exitCode;
	}
}
