/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458<br/>
 * 28.03.2013 14:07:01
 * 
 * </copyright>
 */
package eu.cessar.ct.sdk.sea.util;

import java.util.List;

import org.eclipse.emf.ecore.EClass;

import eu.cessar.ct.sdk.sea.ISEAConfig;
import eu.cessar.ct.sdk.sea.ISEAContainer;
import eu.cessar.ct.sdk.sea.ISEAContainerParent;
import eu.cessar.ct.sdk.sea.ISEAObject;
import eu.cessar.req.Requirement;
import gautosar.gecucdescription.GContainer;
import gautosar.gecucdescription.GModuleConfiguration;
import gautosar.gecucparameterdef.GCommonConfigurationAttributes;
import gautosar.gecucparameterdef.GParamConfContainerDef;
import gautosar.ggenericstructure.ginfrastructure.GARObject;
import gautosar.ggenericstructure.ginfrastructure.GIdentifiable;

/**
 * SEA Error handling mechanism. For a default implementation that can be extended, see {@link SEADefaultErrorHandler}
 * 
 * @author uidl6458
 * 
 *         %created_by: uidl6870 %
 * 
 *         %date_created: Wed Jan 15 11:56:05 2014 %
 * 
 *         %version: 3 %
 */
@Requirement(
	reqID = "REQ_API#SEA#9")
public interface ISEAErrorHandler
{
	/**
	 * Called when no definition was found.
	 * 
	 * @param parent
	 *        the object on which the method was executed
	 * @param defName
	 *        the used definition name
	 * @return a definition to be used instead, could be <code>null</code>
	 */
	public GARObject noDefinitionFound(ISEAObject parent, String defName);

	/**
	 * Called when more then one definition was found
	 * 
	 * @param parent
	 *        the object on which the method was executed
	 * @param defName
	 *        the used definition name
	 * @param result
	 *        list with found definitions, never <code>null</code> or empty
	 * @return a definition to be used from the <code>result</code> list, could be <code>null</code>
	 */
	public GARObject multipleDefinitionsFound(ISEAObject parent, String defName, List<? extends GARObject> result);

	/**
	 * Called when there were multiple values found, but the executed method can return only one.
	 * 
	 * @param parent
	 *        the object on which the method was executed
	 * @param defName
	 *        the used definition name
	 * @param result
	 *        list with found values, never <code>null</code> or empty
	 * @return a value to be used, could be <code>null</code>
	 */
	public <T> T multipleValuesFound(ISEAObject parent, String defName, List<T> result);

	/**
	 * Called when trying to access a parameter or a reference definition of an expected type, but it resulted to be of
	 * a different one. <br>
	 * E.g: calling {@link ISEAContainer#getBoolean(String)}) with a String that actually corresponds to the short name
	 * of an integer parameter definition instead of a boolean one.
	 * 
	 * @param owner
	 *        the SEA container on which the method was executed
	 * @param defName
	 *        used short name for the parameter/reference definition
	 * @param expectedTypes
	 *        expected definition types, derived from the name of the executed method
	 * @param actualType
	 *        actual type of the accessed definition
	 */
	public void definitionNotOfExpectedType(ISEAContainer owner, String defName,
		List<Class<? extends GCommonConfigurationAttributes>> expectedTypes,
		Class<? extends GCommonConfigurationAttributes> actualType);

	/**
	 * Called when trying to set a simple/symbolic/choice reference to a wrong destination.
	 * 
	 * @param owner
	 *        the SEA container on which the setter method was executed
	 * @param defName
	 *        the short name of the used reference definition
	 * @param allowedDefs
	 *        list with the allowed destination(s) for the reference, never <code>null</code>
	 * @param values
	 *        used list containing invalid target(s), never <code>null</code> or empty
	 */
	public void valueNotOfDestinationType(ISEAContainer owner, String defName,
		List<GParamConfContainerDef> allowedDefs, List<ISEAContainer> values);

	/**
	 * Called when attempting to set a foreign reference to a wrong destination.
	 * 
	 * @param owner
	 *        the SEA container on which the setter method was executed
	 * @param defName
	 *        the short name of the used foreign reference definition
	 * @param expectedType
	 *        expected destination type
	 * @param values
	 *        used list containing invalid target(s), never <code>null</code> or empty
	 */
	public void valueNotOfDestinationType(ISEAContainer owner, String defName, EClass expectedType,
		List<GIdentifiable> values);

	/**
	 * Called when attempting to add wrong container(s) as children to the specified container parent.
	 * 
	 * @param owner
	 *        the SEA container parent
	 * @param childContainers
	 *        list with containers used to be added as child containers to the <code>owner</code>; never
	 *        <code>null</code> or empty
	 */
	public void definitionNotAllowed(ISEAContainerParent owner, List<ISEAContainer> childContainers);

	/**
	 * Called when an attempt has been made to set an enum parameter with a value that is not among the parameter
	 * definition's literals.
	 * 
	 * @param container
	 *        the SEA container on which the setter method was executed
	 * @param defName
	 *        used definition name
	 * @param acceptedLiterals
	 *        list with literals declared by the parameter definition
	 * @param value
	 *        used value
	 * @return a value to be used from the provided <code>acceptedLiterals</code> list, could be <code>null</code>
	 */
	public String enumLiteralNotFound(ISEAContainer container, String defName, List<String> acceptedLiterals,
		String value);

	/**
	 * Called when an attempt has been made to set the active configuration of <code>config</code> to a value not being
	 * among the {@link ISEAConfig#arGetConfigurations()}
	 * 
	 * @param config
	 *        the SEA configuration on which the method was called
	 * @param activeConfiguration
	 *        used active configuration
	 */
	public void wrongActiveConfiguration(ISEAConfig config, GModuleConfiguration activeConfiguration);

	/**
	 * Called when an attempt has been made to set the active container of <code>container</code> to a value not being
	 * among the {@link ISEAContainer#arGetContainers()}
	 * 
	 * @param container
	 *        the SEA container on which the method was called
	 * @param activeContainer
	 *        used active container
	 */
	public void wrongActiveContainer(ISEAContainer container, GContainer activeContainer);
}
