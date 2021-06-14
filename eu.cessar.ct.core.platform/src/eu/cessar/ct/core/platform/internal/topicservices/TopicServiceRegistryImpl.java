/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidg3464<br/>
 * May 20, 2015 4:47:58 PM
 *
 * </copyright>
 */
package eu.cessar.ct.core.platform.internal.topicservices;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.osgi.util.NLS;

import eu.cessar.ct.core.internal.platform.CessarPluginActivator;
import eu.cessar.ct.core.platform.topicservices.ITopicService;
import eu.cessar.ct.core.platform.topicservices.ITopicServiceProvider;
import eu.cessar.ct.core.platform.topicservices.ITopicServiceRegistry;
import eu.cessar.ct.core.platform.util.ExtensionClassWrapper;

/**
 *
 * @author uidg3464
 *
 *         %created_by: uidg3464 %
 *
 *         %date_created: Tue Jul  7 12:35:28 2015 %
 *
 *         %version: 7 %
 */

public class TopicServiceRegistryImpl implements ITopicServiceRegistry
{

	private IConfigurationElement cfgElement;

	/**
	 * The Topic linked with the instance of the TopicServiceRegistry
	 */
	private String topic;

	/**
	 * A map between a serviceID and a serviceClass
	 */
	private Map<String, String> servicesClassesMap;

	/**
	 * A map between a Descriptor and it's parentDescriptor
	 */
	private Map<String, String> descriptorIdMap;

	/**
	 * A map between a Descriptor and it's services
	 */
	private Map<String, List<String>> descriptorServiceMap;

	/**
	 * A list of all the serviceMappings
	 */
	private List<IConfigurationElement> serviceMappingList;

	/**
	 * Flag to track lazy loading.
	 */
	private boolean isInitialized;

	/**
	 * @param topic
	 *        - The Topic related to this TopicServiceRegistry
	 * @param iConfigurationElement
	 *        - The extension point of the Topic
	 */
	public TopicServiceRegistryImpl(String topic, IConfigurationElement iConfigurationElement)
	{
		cfgElement = iConfigurationElement;
		this.topic = topic;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.core.platform.topicservices.ITopicServiceRegistry#getTopic()
	 */
	@Override
	public String getTopic()
	{
		return topic;
	}

	private synchronized void checkInit()
	{
		if (!isInitialized)
		{
			// already set isInitialized to true before actual initializaton to avoid infinite recursion
			isInitialized = true;

			// Create the map between the Descriptors and the ParentDescriptors
			createTopicDescriptorsMap();

			// Create the map between the ServiceId and the ServiceClass
			createServiceClassMap();

			// Create both the ServiceMapping list ,that contains all the ServiceMappings, and the map between a
			// Descriptor and all of it's Services
			createServiceMappingList();
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.core.platform.topicservices.ITopicServiceRegistry#getProvider(java.lang.Class,
	 * java.lang.String, boolean)
	 */
	@Override
	public ITopicServiceProvider<?> getProvider(Class<? extends ITopicService> topicService, String descriptorID,
		boolean strictMode)
		{

		checkInit();
		Map<String, String> service_Class_map = getServicesClassesMap();

		String canonicalName = topicService.getCanonicalName();

		// Check to see if the service exists
		boolean containsKey = service_Class_map.containsValue(canonicalName);
		if (!containsKey)
		{
			// log a error
			CessarPluginActivator.getDefault().logError(
				NLS.bind(Messages.PROVIDER_DOES_NOT_EXIST, new Object[] {topicService}));
			return null;
		}
		String descriptor = null;

		// Check to see if the Descriptor exists
		boolean descriptorExists = descriptorIdExists(descriptorID);
		if (!descriptorExists)
		{

			// log a warning
			CessarPluginActivator.getDefault().logError(
				NLS.bind(Messages.DOES_NOT_CONTAIN_GIVEN_DESCRIPTOR, new Object[] {topic, descriptorID}));

		}
		else
		{
			descriptor = descriptorID;
		}
		if (descriptor == null)
		{
			// log a warning
			CessarPluginActivator.getDefault().logError(
				NLS.bind(Messages.TOPIC_HAS_NO_DESCRIPTORS, new Object[] {topic}));

		}

		ITopicServiceProvider<?> provider = doGetProvider(descriptor, strictMode, topicService);
		return provider;
		}

	/**
	 * Returns a Provider for the given Descriptor and Service. If none is found then return null
	 *
	 * @param descriptor
	 * @param topicService
	 * @return
	 */
	private ITopicServiceProvider<?> doGetProvider(String descriptor, boolean strictMode,
		Class<? extends ITopicService> topicService)
	{
		ExtensionClassWrapper<ITopicServiceProvider<?>> provider;
		ITopicServiceProvider<?> topicServiceProvider = null;

		// Checks recursively if the given Descriptor or it's parentDescriptor contains the Service
		String descriptorWithGivenService = obtainServiceForGivenDescriptor(descriptor, topicService, strictMode);
		if (descriptorWithGivenService != null)
		{
			for (IConfigurationElement iConfigurationElement: serviceMappingList)
			{
				String descriptorID = iConfigurationElement.getAttribute(TopicServicesConstants.DESCRIPTORID);
				if (descriptorWithGivenService.equals(descriptorID))
				{

					String serviceID = iConfigurationElement.getAttribute(TopicServicesConstants.SERVICEID);
					String simpleName = topicService.getSimpleName();
					if (simpleName.equals(serviceID))
					{
						boolean checkedTopicService = checkTopicService(topicService);
						if (checkedTopicService)
						{
							provider = new ExtensionClassWrapper<>(iConfigurationElement,
								TopicServicesConstants.PROVIDERID, true);
							try
							{
								ITopicServiceProvider<?> tServiceProvider = provider.getInstance();
								topicServiceProvider = tServiceProvider;
								break;
							}
							// SUPPRESS CHECKSTYLE catch all per contract
							catch (Throwable e)
							{
								CessarPluginActivator.getDefault().logError(e);
								continue;
							}
						}
					}
				}
			}
		}
		else
		{
			CessarPluginActivator.getDefault().logError(
				NLS.bind(Messages.TOPIC_HAS_NO_SERVICE_FOR_GIVEN_DESCRIPTOR, new Object[] {topic, descriptor,
					topicService}));
		}
		return topicServiceProvider;
	}

	/**
	 * Checks if the givenClass or if any of it's SuperClasses implement ITopicService
	 *
	 * @param givenClass
	 */
	private boolean checkTopicService(Class<?> givenClass)
	{
		boolean implementationStatus = false;
		Class<?>[] interfaces = givenClass.getInterfaces();
		for (Class<?> implementedInterface: interfaces)
		{
			boolean equals = TopicServicesConstants.I_TOPIC_SERVICE.equals(implementedInterface.getSimpleName());
			if (equals)
			{
				implementationStatus = equals;
				break;
			}
		}
		if (!implementationStatus)
		{
			Class<?> superclass = givenClass.getSuperclass();
			return checkTopicService(superclass);
		}
		return implementationStatus;
	}

	/**
	 * Checks recursively if the given Descriptor or it's parentDescriptor contains the Service
	 *
	 * @param descriptorID
	 *        - The Descriptor to be checked
	 * @param topicService
	 *        - The searched Service
	 * @param strictMode
	 *        - when false it will search also in the Parent Descriptor for the Service - when true it will only search
	 *        in the given Descriptor for the Service
	 * @return
	 *
	 */
	private String obtainServiceForGivenDescriptor(String descriptorID, Class<? extends ITopicService> topicService,
		boolean strictMode)
	{
		if (descriptorID != null)
		{
			List<String> list = descriptorServiceMap.get(descriptorID);
			String topicServiceName = topicService.getSimpleName();
			boolean descriptorHasService;
			if (list != null)
			{
				descriptorHasService = list.contains(topicServiceName);
			}
			else
			{
				descriptorHasService = false;
			}
			if (!descriptorHasService)
			{
				if (!strictMode)
				{
					String parentDescriptorId = descriptorIdMap.get(descriptorID);
					return obtainServiceForGivenDescriptor(parentDescriptorId, topicService, strictMode);
				}
				else
				{
					return null;
				}
			}
			else
			{
				return descriptorID;

			}
		}
		else
		{
			// log an error
			CessarPluginActivator.getDefault().logError(
				NLS.bind(Messages.TOPIC_HAS_NO_SERVICE_FOR_GIVEN_DESCRIPTOR, new Object[] {topic, descriptorID,
					topicService.getName()}));
			return null;
		}

	}

	/**
	 * Creates a map between the Descriptor and it's ParentDescriptor
	 *
	 * @param topic
	 * @param listOfDescriptors
	 */
	private void createTopicDescriptorsMap()
	{
		if (descriptorIdMap == null)
		{
			Map<String, String> descriptorMap = new HashMap<>();

			// Create descriptors list for the specified topic
			IConfigurationElement[] listOfDescriptors = cfgElement.getChildren(TopicServicesConstants.DESCRIPTOR);
			if (listOfDescriptors == null || listOfDescriptors.length == 0)
			{
				// log a warning
				CessarPluginActivator.getDefault().logError(
					NLS.bind(Messages.TOPIC_HAS_NO_DESCRIPTORS, new Object[] {topic}));
			}

			for (IConfigurationElement descriptor: listOfDescriptors)
			{
				// Obtain the Id of the current descriptor

				String descriptorID = descriptor.getAttribute(TopicServicesConstants.ID);
				if (descriptorID == null)
				{
					CessarPluginActivator.getDefault().logError(Messages.NULL_DESCRIPTORID_ERROR);
					continue;

				}

				// Obtain the parentDescriptors of the current descriptor

				String parentDescriptor = descriptor.getAttribute(TopicServicesConstants.PARENT_DESCRIPTOR);
				descriptorMap.put(descriptorID, parentDescriptor);
			}
			descriptorIdMap = descriptorMap;
		}
	}

	/**
	 * @return the topicDescriptorIDMap
	 */
	private Map<String, String> getDescriptorIDMap()
	{

		return descriptorIdMap;
	}

	/**
	 * Creates a map between the ServiceId and the ServiceClass
	 */
	private void createServiceClassMap()
	{

		if (servicesClassesMap == null)
		{
			Map<String, String> servicesClasses_Map = new HashMap<>();
			// Create services list for the specified topic
			IConfigurationElement[] listOfServices = cfgElement.getChildren(TopicServicesConstants.SERVICE);
			if (listOfServices == null || listOfServices.length == 0)
			{
				// log a warning
				CessarPluginActivator.getDefault().logError(
					NLS.bind(Messages.TOPIC_HAS_NO_SERVICES, new Object[] {topic}));
			}
			for (IConfigurationElement service: listOfServices)
			{
				// Obtain the ServiceID
				String serviceID = service.getAttribute(TopicServicesConstants.ID);
				if (serviceID == null)
				{
					CessarPluginActivator.getDefault().logError(Messages.NULL_SERVICEID_ERROR);
					continue;

				}
				// Obtain the ServiceClass
				String serviceClass = service.getAttribute(TopicServicesConstants.CLASS);
				if (serviceClass == null)
				{
					CessarPluginActivator.getDefault().logError(Messages.NULL_SERVICE_CLASS);
					continue;

				}
				// Create ServicesClasses map

				servicesClasses_Map.put(serviceID, serviceClass);
			}
			servicesClassesMap = servicesClasses_Map;
		}
	}

	/**
	 * Creates the Map of all the ServiceMappings and the map between the Descriptor and all of it's Services
	 */
	private void createServiceMappingList()
	{
		if ((serviceMappingList == null) && (descriptorServiceMap == null))
		{
			List<IConfigurationElement> serviceMapping_List = new ArrayList<>();
			Map<String, List<String>> descriptorService_Map = new HashMap<>();
			IConfigurationElement[] elements = Platform.getExtensionRegistry().getConfigurationElementsFor(
				TopicServicesConstants.TOPIC_SERVICE_EXTENSION_ID);
			for (IConfigurationElement element: elements)
			{
				String topicID = element.getAttribute(TopicServicesConstants.TOPIC_ID);

				if (topic.equals(topicID))
				{
					String descriptorID = element.getAttribute(TopicServicesConstants.DESCRIPTORID);
					if (descriptorID != null)
					{
						String serviceId = element.getAttribute(TopicServicesConstants.SERVICEID);
						if (serviceId != null)
						{
							serviceMapping_List.add(element);
							List<String> list = descriptorService_Map.get(descriptorID);
							if (list != null)
							{
								list.add(serviceId);
								descriptorService_Map.put(descriptorID, list);
							}
							else
							{
								list = new ArrayList<>();
								list.add(serviceId);
								descriptorService_Map.put(descriptorID, list);
							}
						}
					}
				}
			}
			serviceMappingList = serviceMapping_List;
			descriptorServiceMap = descriptorService_Map;
		}

	}

	/**
	 * @return the servicesClassesMap
	 */
	private Map<String, String> getServicesClassesMap()
	{
		return servicesClassesMap;
	}

	/**
	 * @param descriptorID
	 * @return
	 */
	private boolean descriptorIdExists(String descriptorID)
	{
		boolean descriptorIdExists = getDescriptorIDMap().containsKey(descriptorID);
		return descriptorIdExists;
	}

}
