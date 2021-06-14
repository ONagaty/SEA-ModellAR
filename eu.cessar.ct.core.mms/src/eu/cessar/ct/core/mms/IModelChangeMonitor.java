/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458<br/>
 * 05.09.2013 16:37:52
 * 
 * </copyright>
 */
package eu.cessar.ct.core.mms;

import org.eclipse.core.resources.IProject;

import eu.cessar.ct.core.mms.internal.monitor.ModelChangeMonitor;
import eu.cessar.ct.core.platform.util.IModelChangeStampProvider;

/**
 * This class provide AUTOSAR metamodel monitoring service. Using {@link IModelChangeStampProvider} is possible to check
 * if the model has been changed from the last call. The check works even during a transaction.
 * 
 * @author uidl6458
 * 
 *         %created_by: uidl6458 %
 * 
 *         %date_created: Mon Sep  9 14:20:06 2013 %
 * 
 *         %version: 2 %
 */
public interface IModelChangeMonitor
{

	/**
	 * The singleton instance
	 */
	public static final IModelChangeMonitor INSTANCE = new ModelChangeMonitor();

	/**
	 * Return the change stamp provider for the project.
	 * 
	 * @param project
	 * @return
	 */
	public IModelChangeStampProvider getChangeStampProvider(IProject project);

	/**
	 * Return the change stamp provider for the project that contain the object
	 * 
	 * @param object
	 * @return
	 */
	public IModelChangeStampProvider getChangeStampProvider(Object object);

}
