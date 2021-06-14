/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458<br/>
 * 28.03.2013 13:57:08
 * 
 * </copyright>
 */
package eu.cessar.ct.sdk.sea;

import org.eclipse.core.resources.IProject;

import eu.cessar.ct.sdk.pm.IPMContainer;
import eu.cessar.ct.sdk.pm.IPMModuleConfiguration;
import eu.cessar.ct.sdk.sea.util.ISEAErrorHandler;
import eu.cessar.ct.sdk.sea.util.ISEAList;
import eu.cessar.ct.sdk.sea.util.ISeaOptions;
import eu.cessar.ct.sdk.sea.util.SEAModelUtil;
import eu.cessar.req.Requirement;
import gautosar.gecucdescription.GContainer;
import gautosar.gecucdescription.GModuleConfiguration;

/**
 * Root interface that provides access to the <b>Simple ECUC API Model</b>. Such an instance can be obtained by calling
 * one of the methods:
 * <ul>
 * <li>{@link SEAModelUtil#getSEAModel(IProject)}</li>
 * <li>
 * {@link SEAModelUtil#getSEAModel(IProject, java.util.Map)}</li>
 * </ul>
 * <strong>Path fragment</strong>
 * <p/>
 * In the SEA context, a path fragment denotes a (back)slash separated path representing a part of the qualified name of
 * a searched definition. <br>
 * Such a path fragment has to end with the short name of the definition. Each part of the fragment is case insensitive.
 * <br>
 * <p>
 * Valid path fragments include:
 * <ul>
 * <li><code>RTE</code> or <code>ECUCDefs/rte</code>: could match the module configuration for
 * <code>AUTOSAR/EcucDefs/Rte</code>, but also for <code>VendorX/Rte</code>
 * <li><code>AUTOSAR/EcucDefs/Rte</code>: perfect match for Rte module configuration on AUTOSAR 4, but not on 3 or 2
 * </ul>
 * while <code>AUTOSAR/EcucDefs</code> will match no container or module configuration.
 * 
 * @see SEAModelUtil
 * @author uidl6458
 * 
 *         %created_by: uidl6870 %
 * 
 *         %date_created: Thu Aug 29 15:59:07 2013 %
 * 
 *         %version: 2 %
 */
@Requirement(
	reqID = "REQ_API#SEA#3")
public interface ISEAModel extends ISEAObject
{
	/**
	 * Returns the module configuration that is an instance of the ECUC module definition with the specified short name.
	 * <p/>
	 * If there is <b>no such definition</b>, an unchecked exception will be thrown. <br>
	 * If there are <b>multiple module configurations</b> with a definition that matches the given short name, even
	 * different definitions, <i>the first one found</i> will be returned.
	 * <p>
	 * {@link ISEAErrorHandler Error handling}<br/>
	 * 
	 * <ul>
	 * <li>{@link ISEAErrorHandler#noDefinitionFound(String)} if no module definition was found
	 * <li> {@link ISEAErrorHandler#multipleValuesFound(ISEAObject, String, java.util.List)} if more than one module
	 * configuration was found
	 * </ul>
	 * 
	 * @param defName
	 *        the short name of the ECUC module definition for which the module configuration is requested
	 * @return a module configuration wrapper or <code>null</code> if no module configuration was found
	 */
	public ISEAConfig getConfiguration(String defName);

	/**
	 * Returns all the module configurations that are instances of the ECUC module definitions with the specified short
	 * name. <br>
	 * The returned list is <b>not</b> live.
	 * <p/>
	 * If there is <b>no such definition</b>, an unchecked exception will be thrown.
	 * <p>
	 * {@link ISEAErrorHandler Error handling}<br/>
	 * 
	 * <ul>
	 * <li>{@link ISEAErrorHandler#noDefinitionFound(String)} if no module definition was found
	 * </ul>
	 * 
	 * @param defName
	 *        the short name of the ECUC module definition(s) for which the module configurations are requested
	 * @return A list with all matching configurations in no particular order, never <code>null</code>
	 */
	public ISEAList<ISEAConfig> getConfigurations(String defName);

	/**
	 * Searches for all module configurations that are instances of the ECUC module definitions that match the specified
	 * {@link #pathFragment path fragment}. <br/>
	 * The returned list is <b>not</b> live.
	 * 
	 * <p/>
	 * If there is <b>no such definition</b>, an unchecked exception will be thrown.
	 * <p>
	 * {@link ISEAErrorHandler Error handling}<br/>
	 * 
	 * <ul>
	 * <li>{@link ISEAErrorHandler#noDefinitionFound(String)} if no module definition was found
	 * </ul>
	 * 
	 * @param pathFragment
	 *        a (back)slash separated path that represents a part of the qualified name of the searched definition, as
	 *        per {@linkplain ISEAModel#pathFragment}
	 * 
	 * @return A list with all matching configurations in no particular order, never <code>null</code>
	 */
	public ISEAList<ISEAConfig> searchForConfigurations(String pathFragment);

	/**
	 * Returns the SEA module configuration wrapper for the specified AUTOSAR module configuration
	 * 
	 * @param configuration
	 *        module configuration for which to obtain the SEA module configuration wrapper
	 * @return corresponding SEA module configuration wrapper
	 */
	public ISEAConfig getConfiguration(GModuleConfiguration configuration);

	/**
	 * Returns the SEA container wrapper for the specified AUTOSAR container
	 * 
	 * @param container
	 *        container for which to obtain the SEA container wrapper
	 * @return corresponding SEA container wrapper
	 */
	public ISEAContainer getContainer(GContainer container);

	/**
	 * Returns the corresponding SEA container wrapper for the given Presentation Model object
	 * 
	 * @param container
	 *        the presentation model object for which the corresponding SEA container wrapper is requested
	 * @return the corresponding SEA container wrapper
	 */
	public ISEAContainer getContainer(IPMContainer container);

	/**
	 * Returns the corresponding SEA module configuration for the given Presentation Model object
	 * 
	 * @param configuration
	 *        the presentation model object for which the corresponding SEA module configuration wrapper is requested
	 * @return the corresponding SEA configuration wrapper
	 */
	public ISEAConfig getConfiguration(IPMModuleConfiguration configuration);

	/**
	 * Returns the {@linkplain IProject} associated with this object
	 * 
	 * @return the project
	 */
	public IProject getProject();

	/**
	 * Returns the options is use by the SEA model
	 * 
	 * @return the options used by this SEA model
	 */
	public ISeaOptions getOptions();

}
