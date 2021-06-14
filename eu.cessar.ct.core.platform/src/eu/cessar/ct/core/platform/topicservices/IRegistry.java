/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidg3464<br/>
 * May 20, 2015 1:45:52 PM
 *
 * </copyright>
 */
package eu.cessar.ct.core.platform.topicservices;

import eu.cessar.ct.core.platform.internal.topicservices.RegistryImpl;

/**
 *
 * @author uidg3464
 *
 *         %created_by: uidg3464 %
 *
 *         %date_created: Fri Jul  3 14:43:03 2015 %
 *
 *         %version: 3 %
 */
/**
 * This represents a registry of all the existing Topics. It will return a new TopicServiceRegistry for the given Topic
 */
public interface IRegistry
{

	/**
	 *
	 */
	public static final IRegistry INSTANCE = new RegistryImpl();

	/**
	 * Creates and returns a TopicServiceRegistry for the given Topic, or null if the Topic does not exist
	 *
	 * @param topic
	 *        - The subject
	 * @return TopicServiceRegistry - the TopicServiceRegistry for the given Topic. TopicServiceRegistry can be null
	 */
	public ITopicServiceRegistry getRegistry(String topic);

}
