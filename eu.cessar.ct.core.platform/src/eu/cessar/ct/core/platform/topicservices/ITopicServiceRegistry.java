/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidg3464<br/>
 * May 20, 2015 1:41:33 PM
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
 *         %date_created: Fri Jul  3 14:43:04 2015 %
 *
 *         %version: 4 %
 */
/**
 * Is responsible for the lazy loading of the needed informations, the descriptors,the services and the
 * serviceMappings.It also returns a Provider for the given Service and Descriptor
 */
public interface ITopicServiceRegistry
{

	/**
	 * Will return the Topic that is linked with this TopicServiceRegistry
	 *
	 * @return - topic
	 */
	public String getTopic();

	/**
	 * Searches inside the informations of the attached Topic, and returns a Provider for the specified service and
	 * descriptor, or null if none is found
	 *
	 * @param topicService
	 *        - The service for the given topic must implement ITopicService
	 * @param descriptorID
	 *        - The descriptor's id
	 * @param strictMode
	 *        - when false the method will also try to find a TopicServiceProvider for the parentDescriptor of the given
	 *        descriptor - when true then the method will only try to find a TopicServiceProvider for the given
	 *        descriptor
	 * @return - ITopicServiceProvider or null if none is found
	 */
	public ITopicServiceProvider<?> getProvider(Class<? extends ITopicService> topicService, String descriptorID,
		boolean strictMode);

}
