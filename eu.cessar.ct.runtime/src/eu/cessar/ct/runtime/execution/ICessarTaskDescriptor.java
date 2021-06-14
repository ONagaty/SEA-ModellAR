/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Nov 19, 2009 3:51:50 PM </copyright>
 */
package eu.cessar.ct.runtime.execution;

/**
 * One cessar task descriptor will be created for each registry contribution to
 * the <code>eu.cessar.ct.runtime.cessarTask</code> extension point.
 */
public interface ICessarTaskDescriptor
{

	/**
	 * The ID of the task descriptor.
	 * 
	 * @return the ID of the task descriptor
	 */
	public String getID();

	/**
	 * A descriptive text for the task descriptor
	 * 
	 * @return
	 */
	public String getDescription();

	/**
	 * The factory object that is capable to execute the selected tasks
	 * 
	 * @return the factory
	 */
	public ICessarTaskManagerFactory<?> getTaskManagerFactory();
}
