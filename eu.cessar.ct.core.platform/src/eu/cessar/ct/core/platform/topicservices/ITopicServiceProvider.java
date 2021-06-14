/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidg3464<br/>
 * May 20, 2015 1:39:19 PM
 *
 * </copyright>
 */
package eu.cessar.ct.core.platform.topicservices;

/**
 *
 * @author uidg3464
 *
 *         %created_by: uidg3464 %
 *
 *         %date_created: Fri Jul  3 14:43:03 2015 %
 *
 *         %version: 3 %
 *
 */
/**
 *
 * This class will be responsible for providing the needed service
 *
 * @param <T>
 *        - The type of the TopicService, must implement ITopicService
 */
public interface ITopicServiceProvider<T>
{

	/**
	 * Creates a service of the given type <T>
	 *
	 * @return -TopicService
	 */
	public T createService();

}
