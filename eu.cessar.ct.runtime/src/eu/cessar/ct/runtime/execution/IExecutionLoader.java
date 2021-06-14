/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Apr 30, 2010 10:58:22 AM </copyright>
 */
package eu.cessar.ct.runtime.execution;

import org.eclipse.core.resources.IProject;

/**
 * The execution loader provide all class loading services for running user code
 * 
 */
public interface IExecutionLoader
{

	/**
	 * @return
	 */
	public IProject getProject();

	/**
	 * Return the class loader that is used by this execution framework
	 * 
	 * @return
	 */
	public ClassLoader getClassLoader();

}
