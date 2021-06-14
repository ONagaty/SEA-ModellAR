/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458<br/>
 * 28.03.2013 14:58:34
 * 
 * </copyright>
 */
package eu.cessar.ct.sdk.sea;

import java.util.List;

import eu.cessar.ct.sdk.sea.util.ISEAErrorHandler;
import eu.cessar.ct.sdk.sea.util.ISEAList;
import eu.cessar.req.Requirement;

/**
 * Wrapper around a model element capable of holding ECUC containers (i.e. model element representing either an AUTOSAR
 * Module Configuration or a Container), used by the Simple ECUC API
 * 
 * @author uidl6458
 * 
 *         %created_by: uidl6870 %
 * 
 *         %date_created: Thu Aug 29 15:59:01 2013 %
 * 
 *         %version: 2 %
 */
@Requirement(
	reqID = "REQ_API#SEA#4")
public interface ISEAContainerParent extends ISEAObject
{
	/**
	 * Returns the short name of the wrapped element.
	 * 
	 * @return the element's short name
	 */
	public String getShortName();

	/**
	 * Sets the short name of the wrapped element to the <code>shortName</code> value. Passing <code>null</code> as
	 * argument is equivalent with calling {@link #unSetShortName()} method.
	 * 
	 * @param shortName
	 *        the new short name, could be <code>null</code>
	 * @return a reference to this object
	 */
	public ISEAContainerParent setShortName(String shortName);

	/**
	 * Unsets the short name of the wrapped element.
	 * 
	 * @return a reference to this object
	 */
	public ISEAContainerParent unSetShortName();

	/**
	 * Returns whether the short name of the wrapped element is set.
	 * 
	 * @return <code>true</code> if the short name is set, <code>false</code> otherwise
	 */
	public boolean isSetShortName();

	/**
	 * Returns the parent of this object
	 * 
	 * @return SEA wrapper for the parent
	 */
	public ISEAObject getParent();

	/**
	 * If <code>defName</code> represents the short name of a ECUC container definition, returns the direct child
	 * container that is an instance of the respective definition. <br>
	 * If <code>defName</code> represents the short name of a ECUC <b>choice</b> container definition, returns the
	 * instance of the respective definition's chosen container definition.
	 * <p/>
	 * 
	 * If there is <b>no such definition</b> or there are <b>multiple definitions</b>, an unchecked exception will be
	 * thrown. <br>
	 * If there are <b>multiple containers</b>, <i>the first one found</i> will be returned.
	 * <p>
	 * {@link ISEAErrorHandler Error handling}<br/>
	 * 
	 * <ul>
	 * 
	 * <li>{@link ISEAErrorHandler#noDefinitionFound(ISEAObject, String)} if no definition was found
	 * <li>{@link ISEAErrorHandler#multipleDefinitionsFound(ISEAObject, String, List)} if more than one definition was
	 * found
	 * <li> {@link ISEAErrorHandler#multipleValuesFound(ISEAObject, String, List)} if more than one container was found
	 * </ul>
	 * 
	 * @param defName
	 * <br>
	 *        <ul>
	 *        the short name of the ECUC container definition for which to obtain the instance, or <br>
	 *        the short name of the ECUC <b>choice</b> container definition for which to obtain the instance of the
	 *        chosen container definition
	 *        </ul>
	 * @return a container wrapper or <code>null</code> if no container was found
	 */
	public ISEAContainer getContainer(String defName);

	/**
	 * If <code>defName</code> represents the short name of a ECUC container definition, returns the direct child
	 * containers that are an instances of the respective definition. <br>
	 * If <code>defName</code> represents the short name of a ECUC <b>choice</b> container definition, returns the
	 * instances of the respective definition's chosen container definitions. <br>
	 * <br>
	 * The returned list is modifiable.
	 * <p/>
	 * If there is <b>no such definition</b> or there are <b>multiple definitions</b>, an unchecked exception will be
	 * thrown.
	 * <p/>
	 * 
	 * {@link ISEAErrorHandler Error handling}<br/>
	 * 
	 * <ul>
	 * <li>{@link ISEAErrorHandler#noDefinitionFound(ISEAObject, String)} if no definition was found
	 * <li>{@link ISEAErrorHandler#multipleDefinitionsFound(ISEAObject, String, List)} if more than one definition was
	 * found
	 * </ul>
	 * 
	 * @param defName
	 * <br>
	 *        <ul>
	 *        the short name of the ECUC container definition for which to obtain the instances, or <br>
	 *        the short name of the ECUC <b>choice</b> container definition for which to obtain the instances of the
	 *        chosen container definition
	 *        </ul>
	 * 
	 * @return a live list with container wrappers, never <code>null</code>
	 */
	public ISEAList<ISEAContainer> getContainers(String defName);

	/**
	 * Searches for all the containers that are descendants of this parent, that are instances of the ECUC container
	 * definitions that match the specified {@link ISEAModel#pathFragment path fragment}. <br>
	 * The returned list is <b>not</b> modifiable.
	 * <p>
	 * If there is <b>no such definition</b>, an unchecked exception will be thrown.
	 * <p/>
	 * {@link ISEAErrorHandler Error handling}<br/>
	 * 
	 * <ul>
	 * <li>{@link ISEAErrorHandler#noDefinitionFound(ISEAObject, String)} if no definition was found
	 * </ul>
	 * 
	 * @param pathFragment
	 *        a (back)slash separated path that represents a part of the qualified name of the searched definition, as
	 *        per {@linkplain ISEAModel#pathFragment} .<br>
	 * @return A list with all matching containers in no particular order, never <code>null</code>
	 */
	public ISEAList<ISEAContainer> searchForContainers(String pathFragment);

	/**
	 * If <code>defName</code> represents the short name of a ECUC container definition, will return whether there is at
	 * least one child container representing an instance of the respective definition. <br>
	 * If <code>defName</code> represents the short name of a ECUC <b>choice</b> container definition, will return
	 * whether there is at least one instance of any of the respective definition's to be chosen container definitions. <br>
	 * <p>
	 * If there is <b>no such definition</b> or there are <b>multiple definitions</b>, an unchecked exception will be
	 * thrown.
	 * <p/>
	 * {@link ISEAErrorHandler Error handling}<br/>
	 * <ul>
	 * 
	 * <li>{@link ISEAErrorHandler#noDefinitionFound(ISEAObject, String)} if no definition was found
	 * <li>{@link ISEAErrorHandler#multipleDefinitionsFound(ISEAObject, String, List)} if more than one definition was
	 * found
	 * </ul>
	 * 
	 * @param defName
	 * <br>
	 *        <ul>
	 *        the short name of a ECUC container definition or <br>
	 *        the short name of a ECUC <b>choice</b> container definition
	 *        </ul>
	 * @return <code>true</code> if at least one instance exists, <code>false</code> otherwise
	 */
	public boolean isSetContainer(String defName);

	/**
	 * If <code>defName</code> represents the short name of a ECUC container definition, will remove all child
	 * containers that are instances of the respective definition. <br>
	 * If <code>defName</code> represents the short name of a ECUC <b>choice</b> container definition, will remove all
	 * instances of the respective definition's to be chosen container definitions. <br>
	 * 
	 * <p>
	 * If there is <b>no such definition</b> or there are <b>multiple definitions</b>, an unchecked exception will be
	 * thrown.
	 * <p>
	 * {@link ISEAErrorHandler Error handling}<br/>
	 * 
	 * <ul>
	 * <li>{@link ISEAErrorHandler#noDefinitionFound(ISEAObject, String)} if no definition was found.
	 * <li>{@link ISEAErrorHandler#multipleDefinitionsFound(ISEAObject, String, List)} if more than one definition was
	 * found
	 * </ul>
	 * 
	 * @param defName
	 * <br>
	 *        <ul>
	 *        the short name of a ECUC container definition or <br>
	 *        the short name of a ECUC <b>choice</b> container definition
	 *        </ul>
	 * @return a reference to this parent container object
	 */
	public ISEAContainerParent unSetContainer(String defName);

	/**
	 * Creates a child container in this parent, that is an instance of the container definition having the
	 * <code>defName</code> short name. <br>
	 * The newly created container will have an unique, automatically generated short name derived from the definition.
	 * 
	 * <p>
	 * If there is <b>no such definition</b> or there are <b>multiple definitions</b>, an unchecked exception will be
	 * thrown. <br>
	 * If the container definition's multiplicity <i>does not allow more than one value</i>, all its existing instances
	 * will be removed prior to creating the new one.
	 * <p/>
	 * {@link ISEAErrorHandler Error handling}<br/>
	 * 
	 * <ul>
	 * <li>{@link ISEAErrorHandler#noDefinitionFound(ISEAObject, String)} if no definition was found.
	 * <li>{@link ISEAErrorHandler#multipleDefinitionsFound(ISEAObject, String, List)} if more than one definition was
	 * found
	 * </ul>
	 * 
	 * @param defName
	 *        short name of the container definition to be instantiated
	 * @return the SEA wrapper around the newly created container
	 */
	public ISEAContainer createContainer(String defName);

	/**
	 * Creates a child container that is an instance of the container definition having the <code>defName</code> short
	 * name. <br>
	 * <b>NOTE:</b> The newly created container will be set with the exact specified <code>shortName</code>, no
	 * uniqueness verifications being made.
	 * 
	 * <p>
	 * If there is <b>no such definition</b> or there are <b>multiple definitions</b>, an unchecked exception will be
	 * thrown. <br>
	 * If the container definition's multiplicity <i>does not allow more than one value</i>, all its existing instances
	 * will be removed prior to creating the new one.
	 * <p/>
	 * {@link ISEAErrorHandler Error handling}<br/>
	 * 
	 * <ul>
	 * <li>{@link ISEAErrorHandler#noDefinitionFound(ISEAObject, String)} if no definition was found.
	 * <li>{@link ISEAErrorHandler#multipleDefinitionsFound(ISEAObject, String, List)} if more than one definition was
	 * found
	 * </ul>
	 * 
	 * @param defName
	 *        short name of the container definition to be instantiated
	 * @param shortName
	 *        the short name to be set for the new container
	 * @return the SEA wrapper around the newly created container
	 */
	public ISEAContainer createContainer(String defName, String shortName);

	/**
	 * Creates a child container that is an instance of the container definition having the <code>defName</code> short
	 * name. <br>
	 * The newly created container will be set with the exact <code>shortName</code> or an unique one, derived from the
	 * suggestion, based on the <code>deriveNameFromSuggestion</code> flag.
	 * <p>
	 * If there is <b>no such definition</b> or there are <b>multiple definitions</b>, an unchecked exception will be
	 * thrown. <br>
	 * If the container definition's multiplicity <i>does not allow more than one value</i>, all its existing instances
	 * will be removed prior to creating the new one.
	 * 
	 * <p/>
	 * {@link ISEAErrorHandler Error handling}<br/>
	 * 
	 * <ul>
	 * <li>{@link ISEAErrorHandler#noDefinitionFound(ISEAObject, String)} if no definition was found.
	 * <li>{@link ISEAErrorHandler#multipleDefinitionsFound(ISEAObject, String, List)} if more than one definition was
	 * found
	 * </ul>
	 * 
	 * @param defName
	 *        container definition's short name
	 * @param shortName
	 *        the suggested short name to be set for the new container
	 * @param deriveNameFromSuggestion
	 *        whether to derive a unique short name from the provided one, by appending an index suffix
	 * @return the SEA wrapper around the newly created container
	 * 
	 */
	public ISEAContainer createContainer(String defName, String shortName, boolean deriveNameFromSuggestion);

	/**
	 * Creates a child container that is an instance of the container definition with the specified
	 * <code>chosenDefName</code> short name, representing a choice for the ECUC choice container definition given by
	 * the <code>defName</code> short name. <br>
	 * The newly created container will have an unique, automatically generated short name derived from the definition.
	 * <p>
	 * If there is <b>no such definition</b> or there are <b>multiple definitions</b>, an unchecked exception will be
	 * thrown. <br>
	 * If the choice container definition's multiplicity <i>does not allow more than one value</i>, all its existing
	 * instances will be removed prior to creating the new one.
	 * 
	 * <p/>
	 * {@link ISEAErrorHandler Error handling}<br/>
	 * 
	 * <ul>
	 * <li>{@link ISEAErrorHandler#noDefinitionFound(ISEAObject, String)} if no definition was found.
	 * <li>{@link ISEAErrorHandler#multipleDefinitionsFound(ISEAObject, String, List)} if more than one definition was
	 * found
	 * </ul>
	 * 
	 * @param defName
	 *        short name of the ECUC choice container definition holding the choice to be instantiated
	 * @param chosenDefName
	 *        short name of the chosen container definition
	 * @return the SEA wrapper around the newly created container
	 */
	public ISEAContainer createChoiceContainer(String defName, String chosenDefName);

	/**
	 * Creates a child container that is an instance of the specified <code>chosenDefName</code> choice of the ECUC
	 * choice container definition given by the <code>defName</code> short name. <br>
	 * <b>NOTE:</b> The newly created container will be set with the exact specified <code>shortName</code>, no
	 * uniqueness verifications being made.
	 * <p>
	 * If there is <b>no such definition</b> or there are <b>multiple definitions</b>, an unchecked exception will be
	 * thrown. <br>
	 * If the choice container definition's multiplicity <i>does not allow more than one value</i>, all its existing
	 * instances will be removed prior to creating the new one.
	 * 
	 * <p/>
	 * {@link ISEAErrorHandler Error handling}<br/>
	 * 
	 * <ul>
	 * <li>{@link ISEAErrorHandler#noDefinitionFound(ISEAObject, String)} if no definition was found.
	 * <li>{@link ISEAErrorHandler#multipleDefinitionsFound(ISEAObject, String, List)} if more than one definition was
	 * found
	 * </ul>
	 * 
	 * @param defName
	 *        short name of the ECUC choice container definition holding the choice to be instantiated
	 * @param chosenDefName
	 *        short name of the chosen container definition
	 * @param shortName
	 *        the short name to be set for the new container
	 * @return the SEA wrapper around the newly created container
	 */
	public ISEAContainer createChoiceContainer(String defName, String chosenDefName, String shortName);

	/**
	 * Creates a child container that is an instance of the specified <code>chosenDefName</code> choice of the ECUC
	 * choice container definition given by the <code>defName</code> short name. <br>
	 * The newly created container will be set with the exact <code>shortName</code> or an unique one, derived from the
	 * suggestion, based on the <code>deriveNameFromSuggestion</code> flag.
	 * <p>
	 * If there is <b>no such definition</b> or there are <b>multiple definitions</b>, an unchecked exception will be
	 * thrown. <br>
	 * If the choice container definition's multiplicity <i>does not allow more than one value</i>, all its existing
	 * instances will be removed prior to creating the new one.
	 * 
	 * <p/>
	 * {@link ISEAErrorHandler Error handling}<br/>
	 * 
	 * <ul>
	 * <li>{@link ISEAErrorHandler#noDefinitionFound(ISEAObject, String)} if no definition was found.
	 * <li>{@link ISEAErrorHandler#multipleDefinitionsFound(ISEAObject, String, List)} if more than one definition was
	 * found
	 * </ul>
	 * 
	 * @param defName
	 *        short name of the ECUC choice container definition holding the choice to be instantiated
	 * @param chosenDefName
	 *        short name of the chosen container definition
	 * @param shortName
	 *        the suggested short name to be set for the new container
	 * @param deriveNameFromSuggestion
	 *        whether to derive a unique short name from the provided one, by appending an index suffix
	 * @return the SEA wrapper around the newly created container
	 */
	public ISEAContainer createChoiceContainer(String defName, String chosenDefName, String shortName,
		boolean deriveNameFromSuggestion);
}
