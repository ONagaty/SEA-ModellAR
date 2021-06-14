/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870<br/>
 * 01.04.2013 10:28:21
 * 
 * </copyright>
 */
package eu.cessar.ct.runtime.ecuc.internal.sea.impl;

import org.eclipse.core.resources.IProject;

import eu.cessar.ct.runtime.ecuc.internal.sea.handlers.ISeaConfigsHandler;
import eu.cessar.ct.runtime.ecuc.internal.sea.handlers.ISeaContainersHandler;
import eu.cessar.ct.runtime.ecuc.internal.sea.handlers.SeaHandlersManager;
import eu.cessar.ct.sdk.pm.IPMContainer;
import eu.cessar.ct.sdk.pm.IPMModuleConfiguration;
import eu.cessar.ct.sdk.sea.ISEAConfig;
import eu.cessar.ct.sdk.sea.ISEAContainer;
import eu.cessar.ct.sdk.sea.ISEAModel;
import eu.cessar.ct.sdk.sea.util.ISEAList;
import eu.cessar.ct.sdk.sea.util.ISeaOptions;
import gautosar.gecucdescription.GContainer;
import gautosar.gecucdescription.GModuleConfiguration;

/**
 * Implementation of the Sea model root
 * 
 * @author uidl6870
 * 
 *         %created_by: uidl6870 %
 * 
 *         %date_created: Thu Aug 29 16:04:14 2013 %
 * 
 *         %version: 7 %
 */
public class SEAModelImpl extends AbstractSEAObject implements ISEAModel
{
	private final IProject project;

	/**
	 * @param project
	 * @param optionsHolder
	 * @param params
	 */
	public SEAModelImpl(IProject project, ISeaOptions optionsHolder)
	{
		super(null, optionsHolder);
		this.project = project;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return getProject().getName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.sdk.sea.ISEAModel#getProject()
	 */
	@Override
	public IProject getProject()
	{
		return project;
	}

	private ISeaConfigsHandler getSeaConfigsHandler()
	{
		return SeaHandlersManager.getSeaConfigsHandler(getSEAModel(), getSeaOptionsHolder());
	}

	private ISeaContainersHandler getSeaContainerHandler()
	{
		return SeaHandlersManager.getSeaContainersHandler(getSEAModel(), getSeaOptionsHolder());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.sdk.sea.ISEAModel#getConfiguration(java.lang.String)
	 */
	@Override
	public ISEAConfig getConfiguration(String defName)
	{
		return getSeaConfigsHandler().getConfiguration(this, defName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.sdk.sea.ISEAModel#getConfigurations(java.lang.String)
	 */
	@Override
	public ISEAList<ISEAConfig> getConfigurations(String defName)
	{
		return getSeaConfigsHandler().getConfigurations(this, defName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.sdk.sea.ISEAModel#searchForConfigurations(java.lang.String)
	 */
	@Override
	public ISEAList<ISEAConfig> searchForConfigurations(String pathFragment)
	{
		return getSeaConfigsHandler().searchForConfigurations(this, pathFragment);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.sdk.sea.ISEAModel#getConfiguration(gautosar.gecucdescription.GModuleConfiguration)
	 */
	@Override
	public ISEAConfig getConfiguration(GModuleConfiguration configuration)
	{
		return getSeaConfigsHandler().getConfiguration(this, configuration);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.sdk.sea.ISEAModel#getContainer(gautosar.gecucdescription.GContainer)
	 */
	@Override
	public ISEAContainer getContainer(GContainer container)
	{
		return getSeaContainerHandler().getContainer(this, container);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.sdk.sea.ISEAObject#getSEAModel()
	 */
	@Override
	public ISEAModel getSEAModel()
	{
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.sdk.sea.ISEAModel#getContainer(eu.cessar.ct.sdk.pm.IPMContainer)
	 */
	@Override
	public ISEAContainer getContainer(IPMContainer container)
	{
		return getSeaContainerHandler().getContainer(this, container);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.sdk.sea.ISEAModel#getConfiguration(eu.cessar.ct.sdk.pm.IPMModuleConfiguration)
	 */
	@Override
	public ISEAConfig getConfiguration(IPMModuleConfiguration configuration)
	{
		return getSeaConfigsHandler().getConfiguration(this, configuration);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.sdk.sea.ISEAModel#getOptions()
	 */
	@Override
	public ISeaOptions getOptions()
	{
		return getSeaOptionsHolder();
	}
}
