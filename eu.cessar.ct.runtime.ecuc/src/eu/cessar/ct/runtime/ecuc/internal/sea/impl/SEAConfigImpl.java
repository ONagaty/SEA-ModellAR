/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870<br/>
 * 02.04.2013 15:21:27
 * 
 * </copyright>
 */
package eu.cessar.ct.runtime.ecuc.internal.sea.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.eclipse.emf.ecore.EObject;

import eu.cessar.ct.core.mms.splittable.SplitableUtils;
import eu.cessar.ct.runtime.ecuc.IEcucCore;
import eu.cessar.ct.runtime.ecuc.IEcucModel;
import eu.cessar.ct.runtime.ecuc.internal.sea.util.SeaActiveConfigurationManager;
import eu.cessar.ct.sdk.pm.IPMModuleConfiguration;
import eu.cessar.ct.sdk.sea.ISEAConfig;
import eu.cessar.ct.sdk.sea.ISEAContainer;
import eu.cessar.ct.sdk.sea.ISEAModel;
import eu.cessar.ct.sdk.sea.util.ISeaOptions;
import eu.cessar.ct.sdk.utils.ModelUtils;
import eu.cessar.ct.sdk.utils.PMUtils;
import gautosar.gecucdescription.GModuleConfiguration;
import gautosar.gecucparameterdef.GModuleDef;

/**
 * Implementation of the wrapper over an ECUC Module Configuration, used by the SEA
 * 
 * @author uidl6870
 * 
 *         %created_by: uidl6870 %
 * 
 *         %date_created: Fri Jun 19 15:09:42 2015 %
 * 
 *         %version: 16 %
 */
public class SEAConfigImpl extends AbstractSEAContainerParent implements ISEAConfig
{
	private GModuleConfiguration wrappedConfiguration;

	/**
	 * @param seaModel
	 * @param moduleConfiguration
	 * @param optionsHolder
	 */
	public SEAConfigImpl(ISEAModel seaModel, GModuleConfiguration moduleConfiguration, ISeaOptions optionsHolder)
	{
		super(seaModel, optionsHolder);
		wrappedConfiguration = moduleConfiguration;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.sdk.sea.ISEAConfig#setActiveConfiguration(gautosar.gecucdescription.GModuleConfiguration)
	 */
	@Override
	public void setActiveConfiguration(GModuleConfiguration activeConfiguration)
	{
		if (activeConfiguration != null && !arGetConfigurations().contains(activeConfiguration))
		{
			getSeaOptionsHolder().getErrorHandler().wrongActiveConfiguration(this, activeConfiguration);
		}

		SeaActiveConfigurationManager.INSTANCE.setGlobalActiveConfiguration(this, activeConfiguration);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.sdk.sea.ISEAConfig#getActiveConfiguration()
	 */
	@Override
	public GModuleConfiguration getActiveConfiguration()
	{
		return SeaActiveConfigurationManager.INSTANCE.getGlobalActiveConfiguration(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		List<GModuleConfiguration> configurations = arGetConfigurations();
		if (configurations.size() > 0)
		{
			return ModelUtils.getAbsoluteQualifiedName(configurations.get(0));
		}
		return super.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.sdk.sea.ISEAConfig#arGetConfigurations()
	 */
	@Override
	public List<GModuleConfiguration> arGetConfigurations()
	{
		if (wrappedConfiguration.eResource() == null)
		{
			return Collections.singletonList(wrappedConfiguration);
		}

		IEcucModel ecucModel = IEcucCore.INSTANCE.getEcucModel(wrappedConfiguration);
		List<GModuleConfiguration> splitedModuleCfgs = ecucModel.getSplitedModuleCfgs(wrappedConfiguration);
		List<GModuleConfiguration> list = new ArrayList<GModuleConfiguration>();
		list.addAll(splitedModuleCfgs);

		return Collections.unmodifiableList(list);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.sdk.sea.ISEAConfig#arGetDefinition()
	 */
	@Override
	public GModuleDef arGetDefinition()
	{
		return SplitableUtils.INSTANCE.unWrapMergedObject(getWrappedElement().gGetDefinition());
	}

	@Override
	protected GModuleDef getDefinition()
	{
		return arGetDefinition();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.runtime.ecuc.internal.sea.impl.AbstractSEAContainerParent#getWrappedElement()
	 */
	@Override
	protected GModuleConfiguration getWrappedElement()
	{
		return wrappedConfiguration;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.runtime.ecuc.internal.sea.impl.AbstractSEAContainerParent#getParent()
	 */
	@Override
	public ISEAModel getParent()
	{
		List<GModuleConfiguration> configurations = arGetConfigurations();
		if (configurations.size() > 0)
		{
			GModuleConfiguration configuration = configurations.get(0);
			EObject eContainer = configuration.eContainer();
			if (eContainer != null)
			{
				return getSEAModel();
			}
		}

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.sdk.sea.ISEAConfig#pmgetConfiguration()
	 */
	@Override
	public IPMModuleConfiguration pmGetConfiguration()
	{
		List<GModuleConfiguration> configurations = arGetConfigurations();
		IPMModuleConfiguration pmModuleConfiguration = PMUtils.getPMModuleConfiguration(configurations.get(0));

		return pmModuleConfiguration;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.sdk.sea.ISEAConfig#createContainer(gautosar.gecucdescription.GModuleConfiguration,
	 * java.lang.String)
	 */
	@Override
	public ISEAContainer createContainer(GModuleConfiguration activeConfiguration, String defName)
	{// TODO: implement
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.sdk.sea.ISEAConfig#createContainer(gautosar.gecucdescription.GModuleConfiguration,
	 * java.lang.String, java.lang.String)
	 */
	@Override
	public ISEAContainer createContainer(GModuleConfiguration activeConfiguration, String defName, String shortName)
	{// TODO: implement
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.sdk.sea.ISEAConfig#createContainer(gautosar.gecucdescription.GModuleConfiguration,
	 * java.lang.String, java.lang.String, boolean)
	 */
	@Override
	public ISEAContainer createContainer(GModuleConfiguration activeConfiguration, String defName, String shortName,
		boolean deriveNameFromSuggestion)
	{// TODO: implement
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.sdk.sea.ISEAConfig#createChoiceContainer(gautosar.gecucdescription.GModuleConfiguration,
	 * java.lang.String, java.lang.String)
	 */
	@Override
	public ISEAContainer createChoiceContainer(GModuleConfiguration activeConfiguration, String defName,
		String chosenDefName)
	{// TODO: implement
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.sdk.sea.ISEAConfig#createChoiceContainer(gautosar.gecucdescription.GModuleConfiguration,
	 * java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public ISEAContainer createChoiceContainer(GModuleConfiguration activeConfiguration, String defName,
		String chosenDefName, String shortName)
	{// TODO: implement
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.sdk.sea.ISEAConfig#createChoiceContainer(gautosar.gecucdescription.GModuleConfiguration,
	 * java.lang.String, java.lang.String, java.lang.String, boolean)
	 */
	@Override
	public ISEAContainer createChoiceContainer(GModuleConfiguration activeConfiguration, String defName,
		String chosenDefName, String shortName, boolean deriveNameFromSuggestion)
	{// TODO: implement
		throw new UnsupportedOperationException();
	}

	/**
	 * The equality implies that the {@link #arGetConfigurations()} contain the same elements, but not necessarily in
	 * the same order
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (obj instanceof ISEAConfig)
		{
			List<GModuleConfiguration> configs1 = ((ISEAConfig) obj).arGetConfigurations();
			List<GModuleConfiguration> configs2 = arGetConfigurations();

			int size1 = configs1.size();
			int size2 = configs2.size();

			return size1 == size2 && configs1.containsAll(configs2);

		}

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		HashCodeBuilder hashCodeBuilder = new HashCodeBuilder(17, 37);

		List<GModuleConfiguration> configurations = arGetConfigurations();

		List<GModuleConfiguration> copyConfigurations = new ArrayList<GModuleConfiguration>(configurations);
		if (copyConfigurations.size() > 1)
		{
			sort(copyConfigurations);
		}

		for (GModuleConfiguration configuration: copyConfigurations)
		{
			hashCodeBuilder.append(configuration.hashCode());
		}

		return hashCodeBuilder.toHashCode();
	}
}
