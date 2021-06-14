/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870<br/>
 * Aug 6, 2013 11:06:52 AM
 * 
 * </copyright>
 */
package eu.cessar.ct.runtime.ecuc.internal.sea.handlers.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.Assert;

import eu.cessar.ct.runtime.ecuc.internal.sea.handlers.ISeaConfigsHandler;
import eu.cessar.ct.runtime.ecuc.internal.sea.impl.SEAConfigImpl;
import eu.cessar.ct.runtime.ecuc.internal.sea.util.MatchingDefinitionHelper;
import eu.cessar.ct.runtime.ecuc.sea.util.SeaUtils;
import eu.cessar.ct.sdk.pm.IPMModuleConfiguration;
import eu.cessar.ct.sdk.sea.ISEAConfig;
import eu.cessar.ct.sdk.sea.ISEAModel;
import eu.cessar.ct.sdk.sea.util.ISEAList;
import eu.cessar.ct.sdk.sea.util.ISeaOptions;
import eu.cessar.ct.sdk.utils.PMUtils;
import eu.cessar.ct.sdk.utils.SplitUtils;
import gautosar.gecucdescription.GModuleConfiguration;
import gautosar.gecucparameterdef.GModuleDef;
import gautosar.ggenericstructure.ginfrastructure.GARObject;

/**
 * Implementation of {@link ISeaConfigsHandler}
 * 
 * @author uidl6870
 * 
 *         %created_by: uidl6870 %
 * 
 *         %date_created: Wed May 27 16:33:56 2015 %
 * 
 *         %version: 6 %
 */
public class SeaConfigsHandler extends AbstractSeaHandler implements ISeaConfigsHandler
{

	/**
	 * @param seaModel
	 * @param opStore
	 */
	public SeaConfigsHandler(ISEAModel seaModel, ISeaOptions opStore)
	{
		super(seaModel, opStore);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.cessar.ct.runtime.ecuc.internal.sea.handlers.ISeaConfigurationsHandler#getConfiguration(eu.cessar.ct.sdk.sea
	 * .ISEAModel, java.lang.String)
	 */
	@Override
	public ISEAConfig getConfiguration(ISEAModel seaModel, String defName)
	{
		ISEAConfig seaConfig = null;
		Map<String, List<GModuleConfiguration>> splitedModuleCfgs = getSplitedModuleCfgs(seaModel, defName, false);

		GModuleConfiguration toWrap = selectConfiguration(seaModel, splitedModuleCfgs, defName);
		if (toWrap != null)
		{
			// return the first found configuration
			seaConfig = createSeaConfigWrapper(seaModel, toWrap);
		}

		return seaConfig;
	}

	/**
	 * First, checks whether there is at least one definition that matches the given <code>arg</code>. If not, will call
	 * the error handler and ask for a default one. Next, will find the module configurations that are instance of the
	 * resolved definition.
	 * 
	 * @param seaModel
	 * @param arg
	 *        a short name or a path fragment
	 * @param isPathFragment
	 *        whether <code>arg</code> represents a short name or a path fragment
	 * @return a map with module configurations, never null
	 */
	private Map<String, List<GModuleConfiguration>> getSplitedModuleCfgs(ISEAModel seaModel, String arg,
		boolean isPathFragment)
	{
		checkArgument(arg);

		List<GModuleDef> moduleDefs;
		if (isPathFragment)
		{
			moduleDefs = MatchingDefinitionHelper.searchForMatchingModuleDefs(getSeaModel().getProject(), arg);
		}
		else
		{
			moduleDefs = MatchingDefinitionHelper.getMatchingModuleDefs(getSeaModel().getProject(), arg);
		}

		List<GModuleDef> list = new ArrayList<GModuleDef>();
		if (moduleDefs.isEmpty())
		{
			GARObject definition = handleNoDefinition(seaModel, arg);

			if (definition != null)
			{
				Assert.isTrue(definition instanceof GModuleDef);
				list.add((GModuleDef) definition);
			}
		}
		else
		{
			list.addAll(moduleDefs);
		}

		Map<String, List<GModuleConfiguration>> allSplitedModuleCfgs = new HashMap<String, List<GModuleConfiguration>>();
		for (GModuleDef moduleDef: list)
		{
			Map<String, List<GModuleConfiguration>> splitedModuleCfgs = getEcucModel().getSplitedModuleCfgs(moduleDef);
			if (!splitedModuleCfgs.isEmpty())
			{
				allSplitedModuleCfgs.putAll(splitedModuleCfgs);
			}
			else
			{
				List<GModuleDef> refinedModuleDefsFamily = getEcucModel().getRefinedModuleDefsFamily(moduleDef);
				for (GModuleDef gModuleDef: refinedModuleDefsFamily)
				{
					allSplitedModuleCfgs.putAll(getEcucModel().getSplitedModuleCfgs(gModuleDef));
					if (!allSplitedModuleCfgs.isEmpty())
					{
						break;
					}
				}
			}
		}

		return allSplitedModuleCfgs;
	}

	/**
	 * Create a Sea wrapper for the given configuration. <br>
	 * 
	 * @param toWrap
	 *        the module configuration to be wrapped
	 * @return the created wrapper
	 */
	private ISEAConfig createSeaConfigWrapper(ISEAModel seaModel, GModuleConfiguration toWrap)
	{
		return new SEAConfigImpl(seaModel, toWrap, getSeaOptionsHolder());
	}

	/**
	 * If <code>map</code> has one entry, the corresponding module configuration is returned. <br>
	 * If the map has more than one entry, the error handler is invoked and asked for a value to be used.
	 * 
	 * @param seaModel
	 * @param map
	 * @param defName
	 *        module definition short name
	 * @return the selected module configuration, could be <code>null</code>
	 */
	private GModuleConfiguration selectConfiguration(ISEAModel seaModel, Map<String, List<GModuleConfiguration>> map,
		String defName)
	{
		GModuleConfiguration selectedConfig = null;

		int configsCount = map.size();
		if (configsCount == 1)
		{
			Set<String> keySet = map.keySet();
			String next = keySet.iterator().next();
			List<GModuleConfiguration> list = map.get(next);
			selectedConfig = list.get(0);
		}
		else if (configsCount > 1)
		{
			List<GModuleConfiguration> mergedConfigs = new ArrayList<GModuleConfiguration>();

			Set<String> keySet = map.keySet();
			for (String string: keySet)
			{
				List<GModuleConfiguration> list = map.get(string);
				GModuleConfiguration moduleConfiguration = list.get(0);
				GModuleConfiguration mergedInstance = SplitUtils.getMergedInstance(moduleConfiguration);
				mergedConfigs.add(mergedInstance);
			}

			selectedConfig = handleMultipleValues(seaModel, defName, mergedConfigs);
		}

		return selectedConfig;
	}

	/**
	 * Calls the error handler, asking for a value to be used.
	 * 
	 * @param defName
	 * @param mergedConfigs
	 * @return a module configuration to be used, could be <code>null</code>
	 */
	private GModuleConfiguration handleMultipleValues(ISEAModel seaModel, String defName,
		List<GModuleConfiguration> mergedConfigs)
	{
		GARObject selected = getSeaOptionsHolder().getErrorHandler().multipleValuesFound(seaModel, defName,
			mergedConfigs);
		if (selected != null)
		{
			Assert.isTrue(selected instanceof GModuleConfiguration);
		}
		return (GModuleConfiguration) selected;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.cessar.ct.runtime.ecuc.internal.sea.handlers.ISeaConfigurationsHandler#getConfigurations(eu.cessar.ct.sdk.
	 * sea.ISEAModel, java.lang.String)
	 */
	@Override
	public ISEAList<ISEAConfig> getConfigurations(ISEAModel seaModel, String defName)
	{
		return lookupConfigurations(seaModel, defName, false);
	}

	/**
	 * @param seaModel
	 * @param arg
	 *        a short name or a path fragment
	 * @param isPathFragment
	 *        whether <code>arg</code> represents a short name of a path fragment
	 * @return
	 */
	private ISEAList<ISEAConfig> lookupConfigurations(ISEAModel seaModel, String arg, boolean isPathFragment)
	{
		List<ISEAConfig> list = new ArrayList<ISEAConfig>();

		Map<String, List<GModuleConfiguration>> splitedModuleCfgs = getSplitedModuleCfgs(seaModel, arg, isPathFragment);
		Set<String> keySet = splitedModuleCfgs.keySet();
		for (String string: keySet)
		{
			List<GModuleConfiguration> list2 = splitedModuleCfgs.get(string);
			GModuleConfiguration moduleConfiguration = list2.get(0);

			list.add(createSeaConfigWrapper(seaModel, moduleConfiguration));
		}

		return SeaUtils.unmodifiableSEAEList(list.size(), list);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.cessar.ct.runtime.ecuc.internal.sea.handlers.ISeaConfigurationsHandler#searchForConfigurations(eu.cessar.ct
	 * .sdk.sea.ISEAModel, java.lang.String)
	 */
	@Override
	public ISEAList<ISEAConfig> searchForConfigurations(ISEAModel seaModel, String pathFragment)
	{
		return lookupConfigurations(seaModel, pathFragment, true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.cessar.ct.runtime.ecuc.internal.sea.handlers.ISeaConfigurationsHandler#getConfiguration(eu.cessar.ct.sdk.sea
	 * .ISEAModel, gautosar.gecucdescription.GModuleConfiguration)
	 */
	@Override
	public ISEAConfig getConfiguration(ISEAModel seaModel, GModuleConfiguration configuration)
	{
		assertNotNull(configuration);

		return createSeaConfigWrapper(seaModel, configuration);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.cessar.ct.runtime.ecuc.internal.sea.handlers.ISeaConfigurationsHandler#getConfiguration(eu.cessar.ct.sdk.sea
	 * .ISEAModel, eu.cessar.ct.sdk.pm.IPMModuleConfiguration)
	 */
	@Override
	public ISEAConfig getConfiguration(ISEAModel seaModel, IPMModuleConfiguration configuration)
	{
		assertNotNull(configuration);

		List<GModuleConfiguration> configurations = PMUtils.getModuleConfigurations(configuration);
		Assert.isTrue(configurations.size() > 0);

		ISEAConfig seaConfiguration = getConfiguration(seaModel, configurations.get(0));
		return seaConfiguration;
	}

}
