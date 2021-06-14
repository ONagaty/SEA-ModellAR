/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Mar 16, 2010 5:04:11 PM </copyright>
 */
package eu.cessar.ct.runtime.ecuc.pmproxy;

import org.eclipse.core.resources.IProject;

import eu.cessar.ct.emfproxy.IEMFProxyEngine;
import eu.cessar.ct.emfproxy.IProxyFeatureResolver;
import eu.cessar.ct.emfproxy.util.InternalProxyConfigurationError;
import eu.cessar.ct.runtime.CessarRuntime;
import eu.cessar.ct.sdk.runtime.ICessarTaskManager;

/**
 * 
 */
public abstract class AbstractPMFeatureResolver<T> extends AbstractProxyWrapperResolver implements
	IProxyFeatureResolver<T>
{
	/**
	 * @param string
	 * @return
	 */
	protected InternalProxyConfigurationError error(String message)
	{
		return new InternalProxyConfigurationError(message);
	}

	/**
	 * @param engine
	 */
	protected void assertCanWrite(IEMFProxyEngine engine)
	{
		if (engine == null)
		{
			throw error("Cannot write with a null engine"); //$NON-NLS-1$
		}
		IProject project = engine.getProject();
		if (project == null)
		{
			throw error("Cannot write with a null project"); //$NON-NLS-1$
		}
		ICessarTaskManager<?> manager = CessarRuntime.getExecutionSupport().getCurrentManager(
			engine.getProject());
		if (manager == null)
		{
			throw error("Cannot write with a null manager from project " + project.getName()); //$NON-NLS-1$
		}
		if (!manager.canWrite())
		{
			throw error("Current manager for project " + project.getName() //$NON-NLS-1$
				+ " does not accept writing"); //$NON-NLS-1$
		}
	}

}
