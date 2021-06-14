/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Jul 28, 2010 5:58:44 PM </copyright>
 */
package org.autosartools.ecuconfig.pdk.core;

import org.autosartools.general.core.project.IAutosarProject;
import org.autosartools.general.core.validation.IPluggable;
import org.eclipse.core.runtime.IProgressMonitor;

/**
 * @author uidl6458
 * @Review uidl7321 - Apr 3, 2012 Common interface of all plugets.
 */
public interface IMosartPluget extends IPluggable
{
	/**
	 * The main method of a pluget.
	 * 
	 * @param project
	 *        the project containing the pluget
	 * @param monitor
	 *        progress indicatior
	 * @param args
	 *        arguments passed to the pluget
	 */
	public void run(IAutosarProject project, IProgressMonitor monitor, String[] args);

}
