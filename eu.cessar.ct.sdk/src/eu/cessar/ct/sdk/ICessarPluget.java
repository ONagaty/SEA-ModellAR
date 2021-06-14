/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870 Jan 13, 2010 12:49:33 PM </copyright>
 */
package eu.cessar.ct.sdk;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;

/**
 * @author uidl6870
 * 
 *         Common interface of all plugets
 */
public interface ICessarPluget extends IPluggable
{

	/**
	 * The main method of a pluget
	 * 
	 * @param project
	 *        the project containing the pluget
	 * @param monitor
	 * @param args
	 *        arguments passed to the pluget
	 */
	void run(IProject project, IProgressMonitor monitor, String[] args);
}
