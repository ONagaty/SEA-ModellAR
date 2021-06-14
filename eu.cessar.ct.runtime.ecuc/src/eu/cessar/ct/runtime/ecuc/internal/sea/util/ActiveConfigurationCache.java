/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870<br/>
 * Jan 30, 2014 10:10:11 AM
 * 
 * </copyright>
 */
package eu.cessar.ct.runtime.ecuc.internal.sea.util;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

import eu.cessar.ct.sdk.sea.ISEAConfig;
import gautosar.gecucdescription.GModuleConfiguration;

/**
 * Keeps a mapping between {@link ISEAConfig}s and their active configuration
 * 
 * @author uidl6870
 * 
 *         %created_by: uidl6870 %
 * 
 *         %date_created: Wed Feb  5 09:36:30 2014 %
 * 
 *         %version: 2 %
 */
class ActiveConfigurationCache
{
	private final Map<ISEAConfig, GModuleConfiguration> cache = new WeakHashMap<ISEAConfig, GModuleConfiguration>();

	/**
	 * @param seaConfig
	 * @return the active configuration for the given SEA configuration wrapper, could be <code>null</code>
	 */
	GModuleConfiguration getActiveConfiguration(ISEAConfig seaConfig)
	{
		if (seaConfig == null)
		{
			return null;
		}

		GModuleConfiguration activeConfig = null;

		Set<ISEAConfig> keySet = cache.keySet();
		for (ISEAConfig iSeaConfig: keySet)
		{
			if (seaConfig.equals(iSeaConfig))
			{
				activeConfig = cache.get(iSeaConfig);
				break;
			}
		}

		return activeConfig;
	}

	/**
	 * Sets the active configuration of <code>seaConfig</code> to <code>activeConfiguration</code>
	 * 
	 * <b>NOTE:</b> The clients should check the active configuration validness. If the provided activeConfiguration is
	 * not among the wrapped configuration's fragments, the method simply returns (the cache is not updated).
	 * 
	 * @param seaConfig
	 * @param activeConfiguration
	 */
	void setActiveConfiguration(ISEAConfig seaConfig, GModuleConfiguration activeConfiguration)
	{
		if (seaConfig == null)
		{
			return;
		}

		if (activeConfiguration != null && !seaConfig.arGetConfigurations().contains(activeConfiguration))
		{
			return;
		}

		Set<ISEAConfig> keySet = cache.keySet();
		Iterator<ISEAConfig> iterator = keySet.iterator();
		while (iterator.hasNext())
		{
			ISEAConfig iseaConfig = iterator.next();
			if (seaConfig.equals(iseaConfig))
			{
				iterator.remove();
				break;
			}
		}

		if (activeConfiguration != null)
		{
			cache.put(seaConfig, activeConfiguration);
		}
	}
}
