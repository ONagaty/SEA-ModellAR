/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Nov 19, 2009 3:43:20 PM </copyright>
 */
package eu.cessar.ct.runtime.execution;

import java.util.Map;

import org.eclipse.core.resources.IProject;

import eu.cessar.ct.sdk.runtime.ICessarTaskManager;

/**
 * {@link ICessarTaskManager}'s are created by factories that implement this
 * interface. Each <code>eu.cessar.ct.runtime.cessarTask</code> extension point
 * define such a factory. The implementors should start from
 * {@link AbstractTaskManagerFactory} which provide a basic implementation
 * 
 */
public interface ICessarTaskManagerFactory<T>
{

	/**
	 * Initialize the factory using a descriptor.
	 * 
	 * @param descriptor
	 *        The descriptor
	 */
	public void initialize(ICessarTaskDescriptor descriptor);

	/**
	 * Return the descriptor that correspond to this factory
	 * 
	 * @return the descriptor
	 */
	public ICessarTaskDescriptor getDescriptor();

	/**
	 * 0 Return true if the project is valid for this descriptor.
	 * 
	 * @param project
	 *        the project
	 * @return true if the project can be used to execute tasks described by
	 *         this factory descriptor
	 */
	public boolean isValidProject(IProject project);

	/**
	 * Create a task manager of this type for the <code>project</code> argument.
	 * 
	 * @param project
	 *        a valid project. The project should pass
	 *        {@link #isValidProject(IProject)}
	 * @param map
	 *        map holding parameters that are of interest for the manager
	 * 
	 * @return a suitable manager
	 */
	public ICessarTaskManagerImpl<T> createManager(IProject project, Map<String, Object> map);

}
