/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870<br/>
 * Feb 3, 2014 11:15:42 AM
 * 
 * </copyright>
 */
package eu.cessar.ct.runtime.ecuc.internal.sea.util;

import org.eclipse.emf.ecore.resource.Resource;

import eu.cessar.ct.sdk.sea.ISEAConfig;
import gautosar.gecucdescription.GModuleConfiguration;

/**
 * Keeps track of the global active configuration per {@link ISEAConfig} and the temporary active one.
 * 
 * @author uidl6870
 * 
 *         %created_by: uidl6870 %
 * 
 *         %date_created: Tue Feb 4 18:08:00 2014 %
 * 
 *         %version: 1 %
 */
public final class SeaActiveConfigurationManager
{
	/** the singleton */
	public static final SeaActiveConfigurationManager INSTANCE = new SeaActiveConfigurationManager();

	private Resource temporaryActiveResource;

	private final ThreadLocal<ActiveConfigurationCache> activeConfigurationCacheThreadLocal = new ThreadLocal<ActiveConfigurationCache>()
	{
		@Override
		protected ActiveConfigurationCache initialValue()
		{
			return new ActiveConfigurationCache();
		}
	};

	/**
	 * @param seaConfig
	 * @return the global active configuration for the given SEA configuration wrapper, could be <code>null</code>
	 */
	public GModuleConfiguration getGlobalActiveConfiguration(ISEAConfig seaConfig)
	{
		return activeConfigurationCacheThreadLocal.get().getActiveConfiguration(seaConfig);
	}

	/**
	 * Sets the active configuration of <code>seaConfig</code> to <code>activeConfiguration</code>
	 * 
	 * @param seaConfig
	 * @param activeConfiguration
	 *        the active configuration to be set, could be <code>null</code>
	 */
	public void setGlobalActiveConfiguration(ISEAConfig seaConfig, GModuleConfiguration activeConfiguration)
	{
		activeConfigurationCacheThreadLocal.get().setActiveConfiguration(seaConfig, activeConfiguration);
	}

	/**
	 * Sets the new temporary resource. It is called within setter/create methods implementation that accept an
	 * additional parameter for specifying the active configuration/container.
	 * 
	 * @param tempActiveResource
	 *        the new temporary resource, could be <code>null</code>
	 */
	public void setTemporaryActiveResource(Resource tempActiveResource)
	{
		temporaryActiveResource = tempActiveResource;
	}

	/**
	 * Returns the temporary active resource
	 * 
	 * @return the temporary active resource
	 */
	public Resource getTemporaryActiveResource()
	{
		return temporaryActiveResource;
	}

	/**
	 * Returns the current temporary active resource if configured, otherwise returns the global active resource for the
	 * specified <code>seaConfig</code>.
	 * 
	 * @param seaConfig
	 *        the {@link ISEAConfig} for which the active resource is requested
	 * @return the active resource to be used, could be <code>null</code>
	 */
	public Resource getActualActiveResource(ISEAConfig seaConfig)
	{
		if (temporaryActiveResource != null)
		{
			return temporaryActiveResource;
		}
		else
		{
			GModuleConfiguration globalActiveConfiguration = getGlobalActiveConfiguration(seaConfig);
			if (globalActiveConfiguration != null)
			{
				return globalActiveConfiguration.eResource();
			}
		}
		return null;
	}

}
