/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 May 14, 2010 11:50:45 AM </copyright>
 */
package eu.cessar.ct.sdk;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;

import eu.cessar.ct.sdk.logging.ILogger;
import eu.cessar.ct.sdk.logging.LoggerFactory;

/**
 * A basic class that can be used for pluget development
 * 
 */
public abstract class AbstractCessarPluget implements ICessarPluget
{

	private IProject project;

	/**
	 * Get the logger
	 * 
	 * @return
	 */
	public static ILogger getLogger()
	{
		return LoggerFactory.getLogger();
	}

	/**
	 * Get the project
	 * 
	 * @return
	 */
	public IProject getProject()
	{
		return project;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.sdk.ICessarPluget#run(org.eclipse.core.resources.IProject, org.eclipse.core.runtime.IProgressMonitor, java.lang.String[])
	 */
	public final void run(IProject project, IProgressMonitor monitor, String[] args)
	{
		this.project = project;
		run(monitor, args);
	}

	/**
	 * @param monitor
	 * @param args
	 */
	public abstract void run(IProgressMonitor monitor, String[] args);

}
