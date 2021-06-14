/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870<br/>
 * Aug 6, 2013 11:01:53 AM
 * 
 * </copyright>
 */
package eu.cessar.ct.runtime.ecuc.internal.sea.handlers;

import eu.cessar.ct.sdk.pm.IPMModuleConfiguration;
import eu.cessar.ct.sdk.sea.ISEAConfig;
import eu.cessar.ct.sdk.sea.ISEAModel;
import eu.cessar.ct.sdk.sea.util.ISEAList;
import gautosar.gecucdescription.GModuleConfiguration;

/**
 * Sea handler for manipulating module configurations
 * 
 * @author uidl6870
 * 
 *         %created_by: uidl6870 %
 * 
 *         %date_created: Mon Aug 19 09:43:51 2013 %
 * 
 *         %version: 1 %
 */
public interface ISeaConfigsHandler
{

	/**
	 * 
	 * @see ISEAModel#getConfiguration(String)
	 */
	@SuppressWarnings("javadoc")
	public ISEAConfig getConfiguration(ISEAModel seaModel, String defName);

	/**
	 * 
	 * @see ISEAModel#getConfigurations(String)
	 */
	@SuppressWarnings("javadoc")
	public ISEAList<ISEAConfig> getConfigurations(ISEAModel seaModel, String defName);

	/**
	 * 
	 * @see ISEAModel#searchForConfigurations(String)
	 */
	@SuppressWarnings("javadoc")
	public ISEAList<ISEAConfig> searchForConfigurations(ISEAModel seaModel, String pathFragment);

	/**
	 * 
	 * @see ISEAModel#getConfiguration(GModuleConfiguration)
	 */
	@SuppressWarnings("javadoc")
	public ISEAConfig getConfiguration(ISEAModel seaModel, GModuleConfiguration configuration);

	/**
	 * 
	 * @see ISEAModel#getConfiguration(IPMModuleConfiguration)
	 */
	@SuppressWarnings("javadoc")
	public ISEAConfig getConfiguration(ISEAModel seaModel, IPMModuleConfiguration configuration);
}
