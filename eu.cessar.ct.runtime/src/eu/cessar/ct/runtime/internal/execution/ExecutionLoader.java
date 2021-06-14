/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Apr 30, 2010 12:56:42 PM </copyright>
 */
package eu.cessar.ct.runtime.internal.execution;

import org.eclipse.core.resources.IProject;

import eu.cessar.ct.runtime.execution.IExecutionLoader;

/**
 * @author uidl6458
 * 
 */
public abstract class ExecutionLoader implements IExecutionLoader
{

	private final IProject project;

	/**
	 * @param project
	 */
	public ExecutionLoader(IProject project)
	{
		this.project = project;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.runtime.execution.IExecutionLoader#getProject()
	 */
	public IProject getProject()
	{
		return project;
	}

}
