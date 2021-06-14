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

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;

import eu.cessar.ct.runtime.execution.AbstractTaskManagerFactory;
import eu.cessar.ct.runtime.execution.ICessarTaskManagerImpl;

/**
 * 
 * @Review uidl7321 - Apr 11, 2012
 * 
 */
public class PlugetCompatTaskManagerFactory extends AbstractTaskManagerFactory<IFile>
{

	/* (non-Javadoc)
	 * @see eu.cessar.ct.runtime.execution.ICessarTaskManagerFactory#createManager(org.eclipse.core.resources.IProject, java.util.Map)
	 */
	public ICessarTaskManagerImpl<IFile> createManager(IProject project, Map<String, Object> map)
	{
		PlugetCompatTaskManager manager = new PlugetCompatTaskManager(getDescriptor(), project);
		return manager;
	}
}
