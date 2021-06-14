/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458<br/>
 * 28.03.2013 14:00:53
 * 
 * </copyright>
 */
package eu.cessar.ct.sdk.sea;

import java.util.List;

import eu.cessar.ct.sdk.pm.IPMModuleConfiguration;
import eu.cessar.ct.sdk.sea.util.ISEAErrorHandler;
import eu.cessar.req.Requirement;
import gautosar.gecucdescription.GModuleConfiguration;
import gautosar.gecucparameterdef.GModuleDef;

/**
 * Wrapper around an AUTOSAR Module Configuration, used by the Simple ECUC API
 * 
 * @author uidl6458
 * 
 *         %created_by: uidl6870 %
 * 
 *         %date_created: Thu Jan 30 09:36:54 2014 %
 * 
 *         %version: 4 %
 */
@Requirement(
	reqID = "REQ_API#SEA#5")
public interface ISEAConfig extends ISEAContainerParent
{
	/**
	 * Returns the fragments (file-based objects) of the wrapped module configuration.
	 * <p>
	 * 
	 * <strong>NOTE:</strong> the return type is a list, as some meta-model versions allow splitting of
	 * {@linkplain GModuleConfiguration}s
	 * 
	 * 
	 * @return a list with the wrapped module configuration's fragments, never <code>null</code>
	 */
	public List<GModuleConfiguration> arGetConfigurations();

	/**
	 * Returns the ECUC module definition of the wrapped module configuration
	 * 
	 * @return the ECUC module definition of the wrapped module configuration
	 */
	public GModuleDef arGetDefinition();

	/**
	 * Returns the Presentation Model object associated with the wrapped module configuration.<br>
	 * <br>
	 * <strong>Important note:</strong> calling this method outside a user code (e.g. pluget or jet), will throw an
	 * unchecked exception!
	 * 
	 * @return the corresponding Presentation Model object
	 */
	public IPMModuleConfiguration pmGetConfiguration();

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.sdk.sea.ISEAContainerParent#getParent()
	 */
	@Override
	public ISEAModel getParent();

	/**
	 * Sets the active configuration. <br>
	 * In case of a <b>split configuration</b>, this setting allows specifying the configuration that will act as the
	 * 'active' one, meaning that it will be further used for storing values. <br>
	 * The provided configuration must be one of the {@link #arGetConfigurations()} values.
	 * 
	 * <p>
	 * <b>NOTE</b>: This is a global setting. Once configured, all write methods that do not have a parameter for
	 * specifying the active configuration (e.g {@link ISEAContainer#setBoolean(String, boolean)}), will use it. <br>
	 * However, the write methods accepting such parameter (e.g
	 * {@link ISEAContainer#setBoolean(gautosar.gecucdescription.GContainer, String, boolean)}), will take into account
	 * the provided parameter first, and only if this is <code>null</code>, will use the global setting instead.
	 * 
	 * <p>
	 * {@link ISEAErrorHandler Error handling}<br/>
	 * 
	 * <ul>
	 * <li>{@link ISEAErrorHandler#wrongActiveConfiguration(ISEAConfig, GModuleConfiguration)} if the used
	 * <code>activeConfiguration</code> is not among the {@link #arGetConfigurations()} list
	 * </ul>
	 * 
	 * @param activeConfiguration
	 *        the configuration to be set as the 'active' one, can be <code>null</code>
	 */
	public void setActiveConfiguration(GModuleConfiguration activeConfiguration);

	/**
	 * Returns the 'active' configuration. <br>
	 * If the active configuration hasn't been set prior to calling this method, <code>null</code> will be returned.
	 * 
	 * @return the configuration that is considered to be the 'active' one, can be <code>null</code>
	 */
	public GModuleConfiguration getActiveConfiguration();

	/**
	 * Works just like {@link #createContainer(String)}, the only difference being that it supports handling a split
	 * module configuration by accepting an additional parameter that specifies the fragment where the child should be
	 * added.
	 * 
	 * <p>
	 * {@link ISEAErrorHandler Error handling}<br/>
	 * 
	 * <ul>
	 * <li>{@link ISEAErrorHandler#wrongActiveConfiguration(ISEAConfig, GModuleConfiguration)} if the used
	 * <code>activeConfiguration</code> is not among the {@link ISEAConfig#arGetConfigurations()} list
	 * </ul>
	 * 
	 * @see #createContainer(String)
	 * 
	 * @param activeConfiguration
	 *        the actual module configuration in which the child will be stored, one of {@link #arGetConfigurations()}
	 */
	@SuppressWarnings("javadoc")
	public ISEAContainer createContainer(GModuleConfiguration activeConfiguration, String defName);

	/**
	 * Works just like {@link #createContainer(String, String)}, the only difference being that it supports handling a
	 * split module configuration by accepting an additional parameter that specifies the fragment where the child
	 * should be added.
	 * 
	 * <p>
	 * {@link ISEAErrorHandler Error handling}<br/>
	 * 
	 * <ul>
	 * <li>{@link ISEAErrorHandler#wrongActiveConfiguration(ISEAConfig, GModuleConfiguration)} if the used
	 * <code>activeConfiguration</code> is not among the {@link ISEAConfig#arGetConfigurations()} list
	 * </ul>
	 * 
	 * @see #createContainer(String, String)
	 * 
	 * @param activeConfiguration
	 *        the actual module configuration in which the child will be stored, one of {@link #arGetConfigurations()}
	 */
	@SuppressWarnings("javadoc")
	public ISEAContainer createContainer(GModuleConfiguration activeConfiguration, String defName, String shortName);

	/**
	 * Works just like {@link #createContainer(String, String, boolean)}, the only difference being that it supports
	 * handling a split module configuration by accepting an additional parameter that specifies the fragment where the
	 * child should be added.
	 * 
	 * <p>
	 * {@link ISEAErrorHandler Error handling}<br/>
	 * 
	 * <ul>
	 * <li>{@link ISEAErrorHandler#wrongActiveConfiguration(ISEAConfig, GModuleConfiguration)} if the used
	 * <code>activeConfiguration</code> is not among the {@link ISEAConfig#arGetConfigurations()} list
	 * 
	 * </ul>
	 * 
	 * @see #createContainer(String, String, boolean)
	 * 
	 * @param activeConfiguration
	 *        the actual module configuration in which the child will be stored, one of {@link #arGetConfigurations()}
	 */
	@SuppressWarnings("javadoc")
	public ISEAContainer createContainer(GModuleConfiguration activeConfiguration, String defName, String shortName,
		boolean deriveNameFromSuggestion);

	/**
	 * Works just like {@link #createChoiceContainer(String, String)}, the only difference being that it supports
	 * handling a split module configuration by accepting an additional parameter that specifies the fragment where the
	 * child should be added.
	 * 
	 * <p>
	 * {@link ISEAErrorHandler Error handling}<br/>
	 * 
	 * <ul>
	 * <li>{@link ISEAErrorHandler#wrongActiveConfiguration(ISEAConfig, GModuleConfiguration)} if the used
	 * <code>activeConfiguration</code> is not among the {@link ISEAConfig#arGetConfigurations()} list
	 * </ul>
	 * 
	 * @see #createChoiceContainer(String, String)
	 * 
	 * @param activeConfiguration
	 *        the actual module configuration in which the child will be stored, one of {@link #arGetConfigurations()}
	 */
	@SuppressWarnings("javadoc")
	public ISEAContainer createChoiceContainer(GModuleConfiguration activeConfiguration, String defName,
		String chosenDefName);

	/**
	 * Works just like {@link #createChoiceContainer(String, String, String)}, the only difference being that it
	 * supports handling a split module configuration by accepting an additional parameter that specifies the fragment
	 * where the child should be added.
	 * 
	 * <p>
	 * {@link ISEAErrorHandler Error handling}<br/>
	 * 
	 * <ul>
	 * <li>{@link ISEAErrorHandler#wrongActiveConfiguration(ISEAConfig, GModuleConfiguration)} if the used
	 * <code>activeConfiguration</code> is not among the {@link ISEAConfig#arGetConfigurations()} list
	 * 
	 * </ul>
	 * 
	 * @see #createChoiceContainer(String, String, String)
	 * 
	 * @param activeConfiguration
	 *        the actual module configuration in which the child will be stored, one of {@link #arGetConfigurations()}
	 */
	@SuppressWarnings("javadoc")
	public ISEAContainer createChoiceContainer(GModuleConfiguration activeConfiguration, String defName,
		String chosenDefName, String shortName);

	/**
	 * Works just like {@link #createChoiceContainer(String, String, String, boolean)}, the only difference being that
	 * it supports handling a split module configuration by accepting an additional parameter that specifies the
	 * fragment where the child should be added.
	 * 
	 * <p>
	 * {@link ISEAErrorHandler Error handling}<br/>
	 * 
	 * <ul>
	 * <li>{@link ISEAErrorHandler#wrongActiveConfiguration(ISEAConfig, GModuleConfiguration)} if the used
	 * <code>activeConfiguration</code> is not among the {@link ISEAConfig#arGetConfigurations()} list
	 * </ul>
	 * 
	 * @see #createChoiceContainer(String, String, String, boolean)
	 * 
	 * @param activeConfiguration
	 *        the actual module configuration in which the child will be stored, one of {@link #arGetConfigurations()}
	 */
	@SuppressWarnings("javadoc")
	public ISEAContainer createChoiceContainer(GModuleConfiguration activeConfiguration, String defName,
		String chosenDefName, String shortName, boolean deriveNameFromSuggestion);

}
