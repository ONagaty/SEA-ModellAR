/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * </copyright>
 */
package eu.cessar.ct.compat.internal.execution.pluget;

import java.util.Map;

import org.eclipse.core.resources.IProject;

import eu.cessar.ct.runtime.execution.AbstractTaskManagerFactory;
import eu.cessar.ct.runtime.execution.ICessarTaskManagerImpl;

/**
 * 
 * @Review uidl7321 - Apr 11, 2012
 * 
 */
public class PlugetClassCompatTaskManagerFactory extends AbstractTaskManagerFactory<String>
{

	/* (non-Javadoc)
	 * @see eu.cessar.ct.runtime.execution.ICessarTaskManagerFactory#createManager(org.eclipse.core.resources.IProject, java.util.Map)
	 */
	public ICessarTaskManagerImpl<String> createManager(IProject project, Map<String, Object> map)
	{
		PlugetClassCompatTaskManager manager = new PlugetClassCompatTaskManager(getDescriptor(),
			project);
		return manager;
	}
}
