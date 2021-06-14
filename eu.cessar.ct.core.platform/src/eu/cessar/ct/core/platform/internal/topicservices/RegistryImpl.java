/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidg3464<br/>
 * May 20, 2015 3:03:51 PM
 *
 * </copyright>
 */
package eu.cessar.ct.core.platform.internal.topicservices;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;

import eu.cessar.ct.core.platform.topicservices.IRegistry;
import eu.cessar.ct.core.platform.topicservices.ITopicServiceRegistry;

/**
 *
 * @author uidg3464
 *
 *         %created_by: uidg3464 %
 *
 *         %date_created: Fri Jul  3 14:43:02 2015 %
 *
 *         %version: 5 %
 */
/**
 * This represents a registry between Topics and their related Topic service registry
 */
public final class RegistryImpl implements IRegistry
{

	// The map between the Topic and it's TopicServiceRegistry
	private volatile Map<String, ITopicServiceRegistry> topicRegistries;

	/**
	 *
	 */
	public RegistryImpl()
	{
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.core.platform.topicservices.IRegistry#getRegistry(java.lang.String)
	 */
	@Override
	public ITopicServiceRegistry getRegistry(String topic)
	{
		checkInit(topic);

		ITopicServiceRegistry iTopicServiceRegistry = topicRegistries.get(topic);
		return iTopicServiceRegistry;
	}

	/**
	 * Load's the Topics from inside all the plugins , and creates the map between each Topic and its
	 * TopicServiceRegistry
	 * 
	 * @param searchedTopic
	 */
	private void checkInit(String searchedTopic)
	{
		if (topicRegistries == null)
		{
			synchronized (INSTANCE)
			{
				if (topicRegistries == null)
				{
					Map<String, ITopicServiceRegistry> registries = new HashMap<>();

					// Get all the configuration elements for the TopicService Extension Point
					IConfigurationElement[] elements = Platform.getExtensionRegistry().getConfigurationElementsFor(
						TopicServicesConstants.TOPIC_SERVICE_EXTENSION_ID);
					for (IConfigurationElement cfgElement: elements)
					{
						// Get the id of each Topic
						String topic = cfgElement.getAttribute(TopicServicesConstants.ID);
						if (searchedTopic.equals(topic))
						{
							// CessarPluginActivator.getDefault().logError(Messages.NULL_TOPIC_ERROR);
							// continue;
							// }

						// Create a new TopicServiceRegistry for each Topic
						TopicServiceRegistryImpl topicRegistry = new TopicServiceRegistryImpl(topic, cfgElement);

						registries.put(topic, topicRegistry);
						}
					}

					topicRegistries = registries;
				}
			}
		}
	}
}
