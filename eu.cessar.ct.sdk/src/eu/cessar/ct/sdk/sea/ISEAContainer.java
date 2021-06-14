// SUPPRESS CHECKSTYLE OK ignore file length restrictions
/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458<br/>
 * 28.03.2013 14:01:02
 * 
 * </copyright>
 */
package eu.cessar.ct.sdk.sea;

import java.math.BigInteger;
import java.util.List;

import org.eclipse.emf.ecore.EClass;

import eu.cessar.ct.sdk.pm.IPMContainer;
import eu.cessar.ct.sdk.sea.util.ISEAErrorHandler;
import eu.cessar.ct.sdk.sea.util.ISEAList;
import eu.cessar.req.Requirement;
import gautosar.gecucdescription.GContainer;
import gautosar.gecucparameterdef.GParamConfContainerDef;
import gautosar.ggenericstructure.ginfrastructure.GIdentifiable;

/**
 * Wrapper around an AUTOSAR Container, used by the Simple ECUC API
 * 
 * @author uidl6458
 * 
 *         %created_by: uidl6870 %
 * 
 *         %date_created: Mon Feb  3 11:04:50 2014 %
 * 
 *         %version: 4 %
 */
@Requirement(
	reqID = "REQ_API#SEA#6")
public interface ISEAContainer extends ISEAContainerParent
{
	/**
	 * Returns the fragments (file-based objects) of the wrapped container.
	 * <p>
	 * <strong>NOTE:</strong> the return type is a list, as some meta-model versions allow splitting of
	 * {@linkplain GContainer}s
	 * 
	 * 
	 * @return a list with the wrapped container's fragments, never <code>null</code>
	 */
	public List<GContainer> arGetContainers();

	/**
	 * Returns the ECUC container definition of the wrapped container
	 * 
	 * @return the ECUC container definition of the wrapped container
	 */
	public GParamConfContainerDef arGetDefinition();

	/**
	 * Returns the Presentation Model object associated with the wrapped container.
	 * <p>
	 * <strong>Important note:</strong> calling this method outside a user code (e.g. pluget or jet), will throw an
	 * unchecked exception!
	 * 
	 * @return the corresponding Presentation Model object
	 */
	public IPMContainer pmGetContainer();

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.sdk.sea.ISEAContainerParent#getParent()
	 */
	@Override
	public ISEAContainerParent getParent();

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.sdk.sea.ISEAContainerParent#unSetContainer(java.lang.String)
	 */
	public ISEAContainer unSetContainer(String defName);

	/**
	 * Returns the value of the parameter specified by the short name of its definition.
	 * <p/>
	 * If there is <b>no such definition</b> or there are <b>multiple definitions</b> or the <b>definition</b> is <b>not
	 * of expected type</b>, an unchecked exception will be thrown. <br>
	 * If there are <b>multiple values</b>, <i>the first one found</i> will be returned. <br>
	 * <p>
	 * {@link ISEAErrorHandler Error handling}<br/>
	 * <ul>
	 * 
	 * <li>{@link ISEAErrorHandler#noDefinitionFound(ISEAObject, String)} if no definition was found
	 * <li>{@link ISEAErrorHandler#multipleDefinitionsFound(ISEAObject, String, List)} if more than one definition was
	 * found
	 * <li>{@link ISEAErrorHandler#definitionNotOfExpectedType(ISEAContainer, String, Class, Class)} if the definition
	 * is of a different type than the one expected
	 * 
	 * <li> {@link ISEAErrorHandler#multipleValuesFound(ISEAObject, String, List)} if more than one value was found
	 * 
	 * </ul>
	 * 
	 * @param defName
	 *        short name of the parameter definition
	 * @return the value of the parameter or <code>null</code> if the parameter is not set
	 */
	public Boolean getBoolean(String defName);

	/**
	 * Returns the value of the parameter specified by the short name of its definition.
	 * <p/>
	 * If there is <b>no such definition</b> or there are <b>multiple definitions</b> or the <b>definition</b> is <b>not
	 * of expected type</b>, an unchecked exception will be thrown. <br>
	 * If there are <b>multiple values</b>, <i>the first one found</i> will be returned. <br>
	 * <p>
	 * {@link ISEAErrorHandler Error handling}<br/>
	 * <ul>
	 * 
	 * <li>{@link ISEAErrorHandler#noDefinitionFound(ISEAObject, String)} if no definition was found
	 * <li>{@link ISEAErrorHandler#multipleDefinitionsFound(ISEAObject, String, List)} if more than one definition was
	 * found
	 * <li>{@link ISEAErrorHandler#definitionNotOfExpectedType(ISEAContainer, String, Class, Class)} if the definition
	 * is of a different type than the one expected
	 * 
	 * <li> {@link ISEAErrorHandler#multipleValuesFound(ISEAObject, String, List)} if more than one value was found
	 * 
	 * </ul>
	 * 
	 * @param defName
	 *        short name of the parameter definition
	 * @return the value of the parameter or <code>null</code> if the parameter is not set
	 */
	public BigInteger getInteger(String defName);

	/**
	 * Returns the value of the parameter specified by the short name of its definition.
	 * <p/>
	 * If there is <b>no such definition</b> or there are <b>multiple definitions</b> or the <b>definition</b> is <b>not
	 * of expected type</b>, an unchecked exception will be thrown. <br>
	 * If there are <b>multiple values</b>, <i>the first one found</i> will be returned. <br>
	 * <p>
	 * {@link ISEAErrorHandler Error handling}<br/>
	 * <ul>
	 * 
	 * <li>{@link ISEAErrorHandler#noDefinitionFound(ISEAObject, String)} if no definition was found
	 * <li>{@link ISEAErrorHandler#multipleDefinitionsFound(ISEAObject, String, List)} if more than one definition was
	 * found
	 * <li>{@link ISEAErrorHandler#definitionNotOfExpectedType(ISEAContainer, String, Class, Class)} if the definition
	 * is of a different type than the one expected
	 * 
	 * <li> {@link ISEAErrorHandler#multipleValuesFound(ISEAObject, String, List)} if more than one value was found
	 * 
	 * </ul>
	 * 
	 * @param defName
	 *        short name of the parameter definition
	 * @return the value of the parameter or <code>null</code> if the parameter is not set
	 */
	public Double getFloat(String defName);

	/**
	 * Returns the value of the parameter specified by the short name of its definition.
	 * <p/>
	 * If there is <b>no such definition</b> or there are <b>multiple definitions</b> or the <b>definition</b> is <b>not
	 * of expected type</b>, an unchecked exception will be thrown. <br>
	 * If there are <b>multiple values</b>, <i>the first one found</i> will be returned. <br>
	 * <p>
	 * {@link ISEAErrorHandler Error handling}<br/>
	 * <ul>
	 * 
	 * <li>{@link ISEAErrorHandler#noDefinitionFound(ISEAObject, String)} if no definition was found
	 * <li>{@link ISEAErrorHandler#multipleDefinitionsFound(ISEAObject, String, List)} if more than one definition was
	 * found
	 * <li>{@link ISEAErrorHandler#definitionNotOfExpectedType(ISEAContainer, String, Class, Class)} if the definition
	 * is of a different type than the one expected
	 * 
	 * <li> {@link ISEAErrorHandler#multipleValuesFound(ISEAObject, String, List)} if more than one value was found
	 * 
	 * </ul>
	 * 
	 * @param defName
	 *        short name of the parameter definition
	 * @return the value of the parameter or <code>null</code> if the parameter is not set
	 */
	public String getEnum(String defName);

	/**
	 * Returns the value of the parameter specified by the short name of its definition.
	 * <p/>
	 * If there is <b>no such definition</b> or there are <b>multiple definitions</b> or the <b>definition</b> is <b>not
	 * of expected type</b>, an unchecked exception will be thrown. <br>
	 * If there are <b>multiple values</b>, <i>the first one found</i> will be returned. <br>
	 * <p>
	 * {@link ISEAErrorHandler Error handling}<br/>
	 * <ul>
	 * 
	 * <li>{@link ISEAErrorHandler#noDefinitionFound(ISEAObject, String)} if no definition was found
	 * <li>{@link ISEAErrorHandler#multipleDefinitionsFound(ISEAObject, String, List)} if more than one definition was
	 * found
	 * <li>{@link ISEAErrorHandler#definitionNotOfExpectedType(ISEAContainer, String, Class, Class)} if the definition
	 * is of a different type than the one expected
	 * 
	 * <li> {@link ISEAErrorHandler#multipleValuesFound(ISEAObject, String, List)} if more than one value was found
	 * 
	 * </ul>
	 * 
	 * @param defName
	 *        short name of the parameter definition
	 * @return the value of the parameter or <code>null</code> if the parameter is not set
	 */
	public String getString(String defName);

	/**
	 * Returns the value of the parameter specified by the short name of its definition. If the parameter is not set,
	 * will return the specified <code>defaultValue</code>.
	 * 
	 * <p/>
	 * If there is <b>no such definition</b> or there are <b>multiple definitions</b> or the <b>definition</b> is <b>not
	 * of expected type</b>, an unchecked exception will be thrown. <br>
	 * If there are <b>multiple values</b>, <i>the first one found</i> will be returned. <br>
	 * <p>
	 * {@link ISEAErrorHandler Error handling}<br/>
	 * 
	 * <ul>
	 * <li>{@link ISEAErrorHandler#noDefinitionFound(ISEAObject, String)} if no definition was found
	 * <li>{@link ISEAErrorHandler#multipleDefinitionsFound(ISEAObject, String, List)} if more than one definition was
	 * found
	 * <li>{@link ISEAErrorHandler#definitionNotOfExpectedType(ISEAContainer, String, Class, Class)} if the definition
	 * is of a different type than the one expected
	 * <li> {@link ISEAErrorHandler#multipleValuesFound(ISEAObject, String, List)} if more than one value was found
	 * </ul>
	 * 
	 * @param defName
	 *        the short name of the parameter definition
	 * @param defaultValue
	 *        default value to be returned in case the parameter is not set
	 * @return the value of the parameter or the provided <code>defaultValue</code> if the parameter is not set
	 */
	public Boolean getBoolean(String defName, Boolean defaultValue);

	/**
	 * Returns the value of the parameter specified by the short name of its definition. If the parameter is not set,
	 * will return the specified <code>defaultValue</code>.
	 * 
	 * <p/>
	 * If there is <b>no such definition</b> or there are <b>multiple definitions</b> or the <b>definition</b> is <b>not
	 * of expected type</b>, an unchecked exception will be thrown. <br>
	 * If there are <b>multiple values</b>, <i>the first one found</i> will be returned. <br>
	 * <p>
	 * {@link ISEAErrorHandler Error handling}<br/>
	 * 
	 * <ul>
	 * <li>{@link ISEAErrorHandler#noDefinitionFound(ISEAObject, String)} if no definition was found
	 * <li>{@link ISEAErrorHandler#multipleDefinitionsFound(ISEAObject, String, List)} if more than one definition was
	 * found
	 * <li>{@link ISEAErrorHandler#definitionNotOfExpectedType(ISEAContainer, String, Class, Class)} if the definition
	 * is of a different type than the one expected
	 * <li> {@link ISEAErrorHandler#multipleValuesFound(ISEAObject, String, List)} if more than one value was found
	 * </ul>
	 * 
	 * @param defName
	 *        the short name of the parameter definition
	 * @param defaultValue
	 *        default value to be returned in case the parameter is not set
	 * @return the value of the parameter or the provided <code>defaultValue</code> if the parameter is not set
	 */
	public BigInteger getInteger(String defName, BigInteger defaultValue);

	/**
	 * Returns the value of the parameter specified by the short name of its definition. If the parameter is not set,
	 * will return the specified <code>defaultValue</code>.
	 * 
	 * <p/>
	 * If there is <b>no such definition</b> or there are <b>multiple definitions</b> or the <b>definition</b> is <b>not
	 * of expected type</b>, an unchecked exception will be thrown. <br>
	 * If there are <b>multiple values</b>, <i>the first one found</i> will be returned. <br>
	 * <p>
	 * {@link ISEAErrorHandler Error handling}<br/>
	 * 
	 * <ul>
	 * <li>{@link ISEAErrorHandler#noDefinitionFound(ISEAObject, String)} if no definition was found
	 * <li>{@link ISEAErrorHandler#multipleDefinitionsFound(ISEAObject, String, List)} if more than one definition was
	 * found
	 * <li>{@link ISEAErrorHandler#definitionNotOfExpectedType(ISEAContainer, String, Class, Class)} if the definition
	 * is of a different type than the one expected
	 * <li> {@link ISEAErrorHandler#multipleValuesFound(ISEAObject, String, List)} if more than one value was found
	 * </ul>
	 * 
	 * @param defName
	 *        the short name of the parameter definition
	 * @param defaultValue
	 *        default value to be returned in case the parameter is not set
	 * @return the value of the parameter or the provided <code>defaultValue</code> if the parameter is not set
	 */
	public Double getFloat(String defName, Double defaultValue);

	/**
	 * Returns the value of the parameter specified by the short name of its definition. If the parameter is not set,
	 * will return the specified <code>defaultValue</code>.
	 * 
	 * <p/>
	 * If there is <b>no such definition</b> or there are <b>multiple definitions</b> or the <b>definition</b> is <b>not
	 * of expected type</b>, an unchecked exception will be thrown. <br>
	 * If there are <b>multiple values</b>, <i>the first one found</i> will be returned. <br>
	 * <p>
	 * {@link ISEAErrorHandler Error handling}<br/>
	 * 
	 * <ul>
	 * <li>{@link ISEAErrorHandler#noDefinitionFound(ISEAObject, String)} if no definition was found
	 * <li>{@link ISEAErrorHandler#multipleDefinitionsFound(ISEAObject, String, List)} if more than one definition was
	 * found
	 * <li>{@link ISEAErrorHandler#definitionNotOfExpectedType(ISEAContainer, String, Class, Class)} if the definition
	 * is of a different type than the one expected
	 * <li> {@link ISEAErrorHandler#multipleValuesFound(ISEAObject, String, List)} if more than one value was found
	 * <li>{@link ISEAErrorHandler#enumLiteralNotFound(ISEAContainer, String, List, String)} if the provided default
	 * value is not among the defined literals
	 * </ul>
	 * 
	 * @param defName
	 *        the short name of the parameter definition
	 * @param defaultValue
	 *        default value to be returned in case the parameter is not set
	 * @return the value of the parameter or the provided <code>defaultValue</code> if the parameter is not set
	 */
	public String getEnum(String defName, String defaultValue);

	/**
	 * Returns the value of the parameter specified by the short name of its definition. If the parameter is not set,
	 * will return the specified <code>defaultValue</code>.
	 * 
	 * <p/>
	 * If there is <b>no such definition</b> or there are <b>multiple definitions</b> or the <b>definition</b> is <b>not
	 * of expected type</b>, an unchecked exception will be thrown. <br>
	 * If there are <b>multiple values</b>, <i>the first one found</i> will be returned. <br>
	 * <p>
	 * {@link ISEAErrorHandler Error handling}<br/>
	 * 
	 * <ul>
	 * <li>{@link ISEAErrorHandler#noDefinitionFound(ISEAObject, String)} if no definition was found
	 * <li>{@link ISEAErrorHandler#multipleDefinitionsFound(ISEAObject, String, List)} if more than one definition was
	 * found
	 * <li>{@link ISEAErrorHandler#definitionNotOfExpectedType(ISEAContainer, String, Class, Class)} if the definition
	 * is of a different type than the one expected
	 * <li> {@link ISEAErrorHandler#multipleValuesFound(ISEAObject, String, List)} if more than one value was found
	 * </ul>
	 * 
	 * @param defName
	 *        the short name of the parameter definition
	 * @param defaultValue
	 *        default value to be returned in case the parameter is not set
	 * @return the value of the parameter or the provided <code>defaultValue</code> if the parameter is not set
	 */
	public String getString(String defName, String defaultValue);

	/**
	 * Returns a list with the values for the parameter specified by the short name of its definition. <br>
	 * The list is modifiable.
	 * 
	 * <p/>
	 * If there is <b>no such definition</b> or there are <b>multiple definitions</b> or the <b>definition</b> is <b>not
	 * of expected type</b>, an unchecked exception will be thrown. <br>
	 * <p>
	 * {@link ISEAErrorHandler Error handling} <br/>
	 * <ul>
	 * 
	 * <li>{@link ISEAErrorHandler#noDefinitionFound(ISEAObject, String)} if no definition was found
	 * <li>{@link ISEAErrorHandler#multipleDefinitionsFound(ISEAObject, String, List)} if more than one definition was
	 * found
	 * <li>{@link ISEAErrorHandler#definitionNotOfExpectedType(ISEAContainer, String, Class, Class)} if the definition
	 * is of a different type than the one expected
	 * </ul>
	 * 
	 * @param defName
	 *        the short name of the parameter definition
	 * @return a modifiable list with the parameter values
	 */
	public ISEAList<Boolean> getBooleans(String defName);

	/**
	 * Returns a list with the values for the parameter specified by the short name of its definition. <br>
	 * The list is modifiable.
	 * 
	 * <p/>
	 * If there is <b>no such definition</b> or there are <b>multiple definitions</b> or the <b>definition</b> is <b>not
	 * of expected type</b>, an unchecked exception will be thrown. <br>
	 * <p>
	 * {@link ISEAErrorHandler Error handling} <br/>
	 * <ul>
	 * 
	 * <li>{@link ISEAErrorHandler#noDefinitionFound(ISEAObject, String)} if no definition was found
	 * <li>{@link ISEAErrorHandler#multipleDefinitionsFound(ISEAObject, String, List)} if more than one definition was
	 * found
	 * <li>{@link ISEAErrorHandler#definitionNotOfExpectedType(ISEAContainer, String, Class, Class)} if the definition
	 * is of a different type than the one expected
	 * </ul>
	 * 
	 * @param defName
	 *        the short name of the parameter definition
	 * @return a modifiable list with the parameter values
	 */
	public ISEAList<BigInteger> getIntegers(String defName);

	/**
	 * Returns a list with the values for the parameter specified by the short name of its definition. <br>
	 * The list is modifiable.
	 * 
	 * <p/>
	 * If there is <b>no such definition</b> or there are <b>multiple definitions</b> or the <b>definition</b> is <b>not
	 * of expected type</b>, an unchecked exception will be thrown. <br>
	 * <p>
	 * {@link ISEAErrorHandler Error handling} <br/>
	 * <ul>
	 * 
	 * <li>{@link ISEAErrorHandler#noDefinitionFound(ISEAObject, String)} if no definition was found
	 * <li>{@link ISEAErrorHandler#multipleDefinitionsFound(ISEAObject, String, List)} if more than one definition was
	 * found
	 * <li>{@link ISEAErrorHandler#definitionNotOfExpectedType(ISEAContainer, String, Class, Class)} if the definition
	 * is of a different type than the one expected
	 * </ul>
	 * 
	 * @param defName
	 *        the short name of the parameter definition
	 * @return a modifiable list with the parameter values
	 */
	public ISEAList<Double> getFloats(String defName);

	/**
	 * Returns a list with the values for the parameter specified by the short name of its definition. <br>
	 * The list is modifiable.
	 * 
	 * <p/>
	 * If there is <b>no such definition</b> or there are <b>multiple definitions</b> or the <b>definition</b> is <b>not
	 * of expected type</b>, an unchecked exception will be thrown. <br>
	 * <p>
	 * {@link ISEAErrorHandler Error handling} <br/>
	 * <ul>
	 * 
	 * <li>{@link ISEAErrorHandler#noDefinitionFound(ISEAObject, String)} if no definition was found
	 * <li>{@link ISEAErrorHandler#multipleDefinitionsFound(ISEAObject, String, List)} if more than one definition was
	 * found
	 * <li>{@link ISEAErrorHandler#definitionNotOfExpectedType(ISEAContainer, String, Class, Class)} if the definition
	 * is of a different type than the one expected
	 * </ul>
	 * 
	 * @param defName
	 *        the short name of the parameter definition
	 * @return a modifiable list with the parameter values
	 */
	public ISEAList<String> getEnums(String defName);

	/**
	 * Returns a list with the values for the parameter specified by the short name of its definition. <br>
	 * The list is modifiable.
	 * 
	 * <p/>
	 * If there is <b>no such definition</b> or there are <b>multiple definitions</b> or the <b>definition</b> is <b>not
	 * of expected type</b>, an unchecked exception will be thrown. <br>
	 * <p>
	 * {@link ISEAErrorHandler Error handling} <br/>
	 * <ul>
	 * 
	 * <li>{@link ISEAErrorHandler#noDefinitionFound(ISEAObject, String)} if no definition was found
	 * <li>{@link ISEAErrorHandler#multipleDefinitionsFound(ISEAObject, String, List)} if more than one definition was
	 * found
	 * 
	 * <li>{@link ISEAErrorHandler#definitionNotOfExpectedType(ISEAContainer, String, Class, Class)} if the definition
	 * is of a different type than the one expected
	 * </ul>
	 * 
	 * @param defName
	 *        the short name of the parameter definition
	 * @return a modifiable list with the parameter values
	 */
	public ISEAList<String> getStrings(String defName);

	/**
	 * Returns whether the parameter specified by the short name of its definition, is set.
	 * 
	 * <p/>
	 * If there is <b>no such definition</b> or there are <b>multiple definitions</b> or the <b>definition</b> is <b>not
	 * of expected type</b>, an unchecked exception will be thrown. <br>
	 * <p>
	 * {@link ISEAErrorHandler Error handling}<br/>
	 * 
	 * <ul>
	 * <li>{@link ISEAErrorHandler#noDefinitionFound(ISEAObject, String)} if no definition is found
	 * <li>{@link ISEAErrorHandler#multipleDefinitionsFound(ISEAObject, String, List)} if more than one definition is
	 * found
	 * <li>{@link ISEAErrorHandler#definitionNotOfExpectedType(ISEAContainer, String, Class, Class)} if the definition
	 * is of a different type than the one expected
	 * </ul>
	 * 
	 * @param defName
	 *        the short name of the parameter definition
	 * @return whether the parameter with the specified definition is set.
	 */
	public boolean isSetBoolean(String defName);

	/**
	 * Returns whether the parameter specified by the short name of its definition, is set.
	 * 
	 * <p/>
	 * If there is <b>no such definition</b> or there are <b>multiple definitions</b> or the <b>definition</b> is <b>not
	 * of expected type</b>, an unchecked exception will be thrown. <br>
	 * <p>
	 * {@link ISEAErrorHandler Error handling}<br/>
	 * 
	 * <ul>
	 * <li>{@link ISEAErrorHandler#noDefinitionFound(ISEAObject, String)} if no definition is found
	 * <li>{@link ISEAErrorHandler#multipleDefinitionsFound(ISEAObject, String, List)} if more than one definition is
	 * found
	 * <li>{@link ISEAErrorHandler#definitionNotOfExpectedType(ISEAContainer, String, Class, Class)} if the definition
	 * is of a different type than the one expected
	 * </ul>
	 * 
	 * @param defName
	 *        the short name of the parameter definition
	 * @return whether the parameter with the specified definition is set.
	 */
	public boolean isSetInteger(String defName);

	/**
	 * Returns whether the parameter specified by the short name of its definition, is set.
	 * 
	 * <p/>
	 * If there is <b>no such definition</b> or there are <b>multiple definitions</b> or the <b>definition</b> is <b>not
	 * of expected type</b>, an unchecked exception will be thrown. <br>
	 * <p>
	 * {@link ISEAErrorHandler Error handling}<br/>
	 * 
	 * <ul>
	 * <li>{@link ISEAErrorHandler#noDefinitionFound(ISEAObject, String)} if no definition is found
	 * <li>{@link ISEAErrorHandler#multipleDefinitionsFound(ISEAObject, String, List)} if more than one definition is
	 * found
	 * <li>{@link ISEAErrorHandler#definitionNotOfExpectedType(ISEAContainer, String, Class, Class)} if the definition
	 * is of a different type than the one expected
	 * </ul>
	 * 
	 * @param defName
	 *        the short name of the parameter definition
	 * @return whether the parameter with the specified definition is set.
	 */
	public boolean isSetFloat(String defName);

	/**
	 * Returns whether the parameter specified by the short name of its definition, is set.
	 * 
	 * <p/>
	 * If there is <b>no such definition</b> or there are <b>multiple definitions</b> or the <b>definition</b> is <b>not
	 * of expected type</b>, an unchecked exception will be thrown. <br>
	 * <p>
	 * {@link ISEAErrorHandler Error handling}<br/>
	 * 
	 * <ul>
	 * <li>{@link ISEAErrorHandler#noDefinitionFound(ISEAObject, String)} if no definition is found
	 * <li>{@link ISEAErrorHandler#multipleDefinitionsFound(ISEAObject, String, List)} if more than one definition is
	 * found
	 * <li>{@link ISEAErrorHandler#definitionNotOfExpectedType(ISEAContainer, String, Class, Class)} if the definition
	 * is of a different type than the one expected
	 * </ul>
	 * 
	 * @param defName
	 *        the short name of the parameter definition
	 * @return whether the parameter with the specified definition is set.
	 */
	public boolean isSetEnum(String defName);

	/**
	 * Returns whether the parameter specified by the short name of its definition, is set.
	 * 
	 * <p/>
	 * If there is <b>no such definition</b> or there are <b>multiple definitions</b> or the <b>definition</b> is <b>not
	 * of expected type</b>, an unchecked exception will be thrown. <br>
	 * <p>
	 * {@link ISEAErrorHandler Error handling}<br/>
	 * 
	 * <ul>
	 * <li>{@link ISEAErrorHandler#noDefinitionFound(ISEAObject, String)} if no definition is found
	 * <li>{@link ISEAErrorHandler#multipleDefinitionsFound(ISEAObject, String, List)} if more than one definition is
	 * found
	 * <li>{@link ISEAErrorHandler#definitionNotOfExpectedType(ISEAContainer, String, Class, Class)} if the definition
	 * is of a different type than the one expected
	 * </ul>
	 * 
	 * @param defName
	 *        the short name of the parameter definition
	 * @return whether the parameter with the specified definition is set.
	 */
	public boolean isSetString(String defName);

	/**
	 * Removes all parameter values having the definition with the specified short name: <code>defName</code>, from this
	 * container.
	 * 
	 * <p/>
	 * If there is <b>no such definition</b> or there are <b>multiple definitions</b> or the <b>definition</b> is <b>not
	 * of expected type</b>, an unchecked exception will be thrown. <br>
	 * <p>
	 * {@link ISEAErrorHandler Error handling}<br/>
	 * 
	 * <ul>
	 * <li>{@link ISEAErrorHandler#noDefinitionFound(ISEAObject, String)} if no definition is found
	 * <li>{@link ISEAErrorHandler#multipleDefinitionsFound(ISEAObject, String, List)} if more than one definition is
	 * found
	 * <li>{@link ISEAErrorHandler#definitionNotOfExpectedType(ISEAContainer, String, Class, Class)} if the definition
	 * is of a different type than the one expected
	 * </ul>
	 * 
	 * @param defName
	 *        the short name of the parameter definition whose instances are to be removed
	 * @return a reference to this container
	 */
	public ISEAContainer unSetBoolean(String defName);

	/**
	 * Removes all parameter values having the definition with the specified short name: <code>defName</code> from this
	 * container.
	 * 
	 * <p/>
	 * If there is <b>no such definition</b> or there are <b>multiple definitions</b> or the <b>definition</b> is <b>not
	 * of expected type</b>, an unchecked exception will be thrown. <br>
	 * <p>
	 * {@link ISEAErrorHandler Error handling}<br/>
	 * 
	 * <ul>
	 * <li>{@link ISEAErrorHandler#noDefinitionFound(ISEAObject, String)} if no definition is found
	 * <li>{@link ISEAErrorHandler#multipleDefinitionsFound(ISEAObject, String, List)} if more than one definition is
	 * found
	 * <li>{@link ISEAErrorHandler#definitionNotOfExpectedType(ISEAContainer, String, Class, Class)} if the definition
	 * is of a different type than the one expected
	 * </ul>
	 * 
	 * @param defName
	 *        the short name of the parameter definition whose instances are to be removed
	 * @return a reference to this container
	 */
	public ISEAContainer unSetInteger(String defName);

	/**
	 * Removes all parameter values having the definition with the specified short name: <code>defName</code>, from this
	 * container.
	 * 
	 * <p/>
	 * If there is <b>no such definition</b> or there are <b>multiple definitions</b> or the <b>definition</b> is <b>not
	 * of expected type</b>, an unchecked exception will be thrown. <br>
	 * <p>
	 * {@link ISEAErrorHandler Error handling}<br/>
	 * 
	 * <ul>
	 * <li>{@link ISEAErrorHandler#noDefinitionFound(ISEAObject, String)} if no definition is found
	 * <li>{@link ISEAErrorHandler#multipleDefinitionsFound(ISEAObject, String, List)} if more than one definition is
	 * found
	 * <li>{@link ISEAErrorHandler#definitionNotOfExpectedType(ISEAContainer, String, Class, Class)} if the definition
	 * is of a different type than the one expected
	 * </ul>
	 * 
	 * @param defName
	 *        the short name of the parameter definition whose instances are to be removed
	 * @return a reference to this container
	 */
	public ISEAContainer unSetFloat(String defName);

	/**
	 * Removes all parameter values having the definition with the specified short name: <code>defName</code>, from this
	 * container.
	 * 
	 * <p/>
	 * If there is <b>no such definition</b> or there are <b>multiple definitions</b> or the <b>definition</b> is <b>not
	 * of expected type</b>, an unchecked exception will be thrown. <br>
	 * <p>
	 * {@link ISEAErrorHandler Error handling}<br/>
	 * 
	 * <ul>
	 * <li>{@link ISEAErrorHandler#noDefinitionFound(ISEAObject, String)} if no definition is found
	 * <li>{@link ISEAErrorHandler#multipleDefinitionsFound(ISEAObject, String, List)} if more than one definition is
	 * found
	 * <li>{@link ISEAErrorHandler#definitionNotOfExpectedType(ISEAContainer, String, Class, Class)} if the definition
	 * is of a different type than the one expected
	 * </ul>
	 * 
	 * @param defName
	 *        the short name of the parameter definition whose instances are to be removed
	 * @return a reference to this container
	 */
	public ISEAContainer unSetEnum(String defName);

	/**
	 * Removes all parameter values having the definition with the specified short name: <code>defName</code>, from this
	 * container.
	 * 
	 * <p/>
	 * If there is <b>no such definition</b> or there are <b>multiple definitions</b> or the <b>definition</b> is <b>not
	 * of expected type</b>, an unchecked exception will be thrown. <br>
	 * <p>
	 * {@link ISEAErrorHandler Error handling}<br/>
	 * 
	 * <ul>
	 * <li>{@link ISEAErrorHandler#noDefinitionFound(ISEAObject, String)} if no definition is found
	 * <li>{@link ISEAErrorHandler#multipleDefinitionsFound(ISEAObject, String, List)} if more than one definition is
	 * found
	 * <li>{@link ISEAErrorHandler#definitionNotOfExpectedType(ISEAContainer, String, Class, Class)} if the definition
	 * is of a different type than the one expected
	 * </ul>
	 * 
	 * @param defName
	 *        the short name of the parameter definition whose instances are to be removed
	 * @return a reference to this container
	 */
	public ISEAContainer unSetString(String defName);

	/**
	 * Sets the value of the parameter specified by the short name of its definition: <code>defName</code> to the
	 * provided <code>value</code>. <br/>
	 * <br>
	 * <b>NOTE</b>: All existing parameter values owned by this container, that are instances of the specified
	 * definition, will be removed prior to setting the new value. <br/>
	 * 
	 * <p/>
	 * If there is <b>no such definition</b> or there are <b>multiple definitions</b> or the <b>definition</b> is <b>not
	 * of expected type</b>, an unchecked exception will be thrown. <br>
	 * <p>
	 * {@link ISEAErrorHandler Error handling}<br/>
	 * 
	 * <ul>
	 * <li>{@link ISEAErrorHandler#noDefinitionFound(ISEAObject, String)} if no definition was found
	 * <li>{@link ISEAErrorHandler#multipleDefinitionsFound(ISEAObject, String, List)} if more than one definition was
	 * found
	 * <li>{@link ISEAErrorHandler#definitionNotOfExpectedType(ISEAContainer, String, Class, Class)} if the definition
	 * is of a different type than the one expected
	 * 
	 * </ul>
	 * 
	 * @param defName
	 *        the short name of the parameter definition
	 * 
	 * @param value
	 *        the value to be set
	 * 
	 * @return a reference to this container
	 */
	public ISEAContainer setBoolean(String defName, boolean value);

	/**
	 * Works just like {@link #setBoolean(String, boolean)}, the only difference being that it supports handling a split
	 * container by accepting an additional parameter that specifies the fragment where the value should be added.
	 * 
	 * <p>
	 * {@link ISEAErrorHandler Error handling}<br/>
	 * 
	 * <ul>
	 * <li>{@link ISEAErrorHandler#wrongActiveContainer(ISEAContainer, GContainer)} if the used
	 * <code>activeContainer</code> is not among the {@link ISEAContainer#arGetContainers()} list
	 * 
	 * </ul>
	 * 
	 * @see #setBoolean(String, boolean)
	 * 
	 * @param activeContainer
	 *        the actual container in which the value will be stored, one of {@link #arGetContainers()}
	 */

	@SuppressWarnings("javadoc")
	public ISEAContainer setBoolean(GContainer activeContainer, String defName, boolean value);

	/**
	 * Sets the value of the parameter specified by the short name of its definition: <code>defName</code> to the
	 * provided <code>value</code>. <br/>
	 * <br>
	 * <b>NOTE</b>: All existing parameter values owned by this container, that are instances of the specified
	 * definition, will be removed prior to setting the new value. <br/>
	 * 
	 * <p/>
	 * If there is <b>no such definition</b> or there are <b>multiple definitions</b> or the <b>definition</b> is <b>not
	 * of expected type</b>, an unchecked exception will be thrown. <br>
	 * <p>
	 * {@link ISEAErrorHandler Error handling}<br/>
	 * 
	 * <ul>
	 * <li>{@link ISEAErrorHandler#noDefinitionFound(ISEAObject, String)} if no definition was found
	 * <li>{@link ISEAErrorHandler#multipleDefinitionsFound(ISEAObject, String, List)} if more than one definition was
	 * found
	 * <li>{@link ISEAErrorHandler#definitionNotOfExpectedType(ISEAContainer, String, Class, Class)} if the definition
	 * is of a different type than the one expected
	 * 
	 * </ul>
	 * 
	 * @param defName
	 *        the short name of the parameter definition
	 * 
	 * @param value
	 *        the value to be set
	 * 
	 * @return a reference to this container
	 */
	public ISEAContainer setInteger(String defName, BigInteger value);

	/**
	 * Works just like {@link #setInteger(String, BigInteger)}, the only difference being that it supports handling a
	 * split container by accepting an additional parameter that specifies the fragment where the value should be added.
	 * 
	 * <p>
	 * {@link ISEAErrorHandler Error handling}<br/>
	 * 
	 * <ul>
	 * <li>{@link ISEAErrorHandler#wrongActiveContainer(ISEAContainer, GContainer)} if the used
	 * <code>activeContainer</code> is not among the {@link ISEAContainer#arGetContainers()} list
	 * 
	 * </ul>
	 * 
	 * @see #setInteger(String, BigInteger)
	 * 
	 * @param activeContainer
	 *        the actual container in which the value will be stored, one of {@link #arGetContainers()}
	 */

	@SuppressWarnings("javadoc")
	public ISEAContainer setInteger(GContainer activeContainer, String defName, BigInteger value);

	/**
	 * Sets the value of the parameter specified by the short name of its definition: <code>defName</code> to the
	 * provided <code>value</code>. <br/>
	 * <br>
	 * <b>NOTE</b>: All existing parameter values owned by this container, that are instances of the specified
	 * definition, will be removed prior to setting the new value. <br/>
	 * 
	 * <p/>
	 * If there is <b>no such definition</b> or there are <b>multiple definitions</b> or the <b>definition</b> is <b>not
	 * of expected type</b>, an unchecked exception will be thrown. <br>
	 * <p>
	 * {@link ISEAErrorHandler Error handling}<br/>
	 * 
	 * <ul>
	 * <li>{@link ISEAErrorHandler#noDefinitionFound(ISEAObject, String)} if no definition was found
	 * <li>{@link ISEAErrorHandler#multipleDefinitionsFound(ISEAObject, String, List)} if more than one definition was
	 * found
	 * <li>{@link ISEAErrorHandler#definitionNotOfExpectedType(ISEAContainer, String, Class, Class)} if the definition
	 * is of a different type than the one expected
	 * 
	 * </ul>
	 * 
	 * @param defName
	 *        the short name of the parameter definition
	 * 
	 * @param value
	 *        the value to be set
	 * 
	 * @return a reference to this container
	 */
	public ISEAContainer setFloat(String defName, Double value);

	/**
	 * Works just like {@link #setFloat(String, Double)}, the only difference being that it supports handling a split
	 * container by accepting an additional parameter that specifies the fragment where the value should be added.
	 * 
	 * <p>
	 * {@link ISEAErrorHandler Error handling}<br/>
	 * 
	 * <ul>
	 * <li>{@link ISEAErrorHandler#wrongActiveContainer(ISEAContainer, GContainer)} if the used
	 * <code>activeContainer</code> is not among the {@link ISEAContainer#arGetContainers()} list
	 * 
	 * </ul>
	 * 
	 * @see #setFloat(String, Double)
	 * 
	 * @param activeContainer
	 *        the actual container in which the value will be stored, one of {@link #arGetContainers()}
	 */

	@SuppressWarnings("javadoc")
	public ISEAContainer setFloat(GContainer activeContainer, String defName, Double value);

	/**
	 * Sets the value of the parameter specified by the short name of its definition: <code>defName</code> to the
	 * provided <code>value</code>. <br/>
	 * <br>
	 * <b>NOTE</b>: All existing parameter values owned by this container, that are instances of the specified
	 * definition, will be removed prior to setting the new value. <br/>
	 * 
	 * <p/>
	 * If there is <b>no such definition</b> or there are <b>multiple definitions</b> or the <b>definition</b> is <b>not
	 * of expected type</b>, an unchecked exception will be thrown. <br>
	 * <p>
	 * {@link ISEAErrorHandler Error handling}<br/>
	 * 
	 * <ul>
	 * <li>{@link ISEAErrorHandler#noDefinitionFound(ISEAObject, String)} if no definition was found
	 * <li>{@link ISEAErrorHandler#multipleDefinitionsFound(ISEAObject, String, List)} if more than one definition was
	 * found
	 * <li>{@link ISEAErrorHandler#definitionNotOfExpectedType(ISEAContainer, String, Class, Class)} if the definition
	 * is of a different type than the one expected
	 * <li>{@link ISEAErrorHandler#enumLiteralNotFound(ISEAContainer, String, List, String)} if the provided value is
	 * not among the defined literals
	 * 
	 * </ul>
	 * 
	 * @param defName
	 *        the short name of the parameter definition
	 * 
	 * @param value
	 *        the value to be set
	 * 
	 * @return a reference to this container
	 */
	public ISEAContainer setEnum(String defName, String value);

	/**
	 * Works just like {@link #setEnum(String, String)}, the only difference being that it supports handling a split
	 * container by accepting an additional parameter that specifies the fragment where the value should be added.
	 * 
	 * <p>
	 * {@link ISEAErrorHandler Error handling}<br/>
	 * 
	 * <ul>
	 * <li>{@link ISEAErrorHandler#wrongActiveContainer(ISEAContainer, GContainer)} if the used
	 * <code>activeContainer</code> is not among the {@link ISEAContainer#arGetContainers()} list
	 * 
	 * </ul>
	 * 
	 * @see #setEnum(String, String)
	 * 
	 * @param activeContainer
	 *        the actual container in which the value will be stored, one of {@link #arGetContainers()}
	 */
	@SuppressWarnings("javadoc")
	public ISEAContainer setEnum(GContainer activeContainer, String defName, String value);

	/**
	 * Sets the value of the parameter specified by the short name of its definition: <code>defName</code> to the
	 * provided <code>value</code>. <br/>
	 * <br>
	 * <b>NOTE</b>: All existing parameter values owned by this container, that are instances of the specified
	 * definition, will be removed prior to setting the new value. <br/>
	 * 
	 * <p/>
	 * If there is <b>no such definition</b> or there are <b>multiple definitions</b> or the <b>definition</b> is <b>not
	 * of expected type</b>, an unchecked exception will be thrown. <br>
	 * <p>
	 * {@link ISEAErrorHandler Error handling}<br/>
	 * 
	 * <ul>
	 * <li>{@link ISEAErrorHandler#noDefinitionFound(ISEAObject, String)} if no definition was found
	 * <li>{@link ISEAErrorHandler#multipleDefinitionsFound(ISEAObject, String, List)} if more than one definition was
	 * found
	 * <li>{@link ISEAErrorHandler#definitionNotOfExpectedType(ISEAContainer, String, Class, Class)} if the definition
	 * is of a different type than the one expected
	 * 
	 * </ul>
	 * 
	 * @param defName
	 *        the short name of the parameter definition
	 * 
	 * @param value
	 *        the value to be set
	 * 
	 * @return a reference to this container
	 */
	public ISEAContainer setString(String defName, String value);

	/**
	 * Works just like {@link #setString(String, String)}, the only difference being that it supports handling a split
	 * container by accepting an additional parameter that specifies the fragment where the value should be added.
	 * 
	 * <p>
	 * {@link ISEAErrorHandler Error handling}<br/>
	 * 
	 * <ul>
	 * <li>{@link ISEAErrorHandler#wrongActiveContainer(ISEAContainer, GContainer)} if the used
	 * <code>activeContainer</code> is not among the {@link ISEAContainer#arGetContainers()} list
	 * 
	 * </ul>
	 * 
	 * @see #setString(String, String)
	 * 
	 * @param activeContainer
	 *        the actual container in which the value will be stored, one of {@link #arGetContainers()}
	 */
	@SuppressWarnings("javadoc")
	public ISEAContainer setString(GContainer activeContainer, String defName, String value);

	/**
	 * Sets the value(s) of the parameter specified by the short name of its definition. <br>
	 * 
	 * <b>NOTE:</b> All existing parameter values owned by this container, that are instances of the specified
	 * definition, will be removed prior to setting the new values.
	 * <p>
	 * The method allows passing an arbitrary set of values. The two calls shown in the snippet bellow are equivalent:
	 * 
	 * <pre>
	 * public static final String RTE_DEV_ERROR_DETECT = &quot;RteDevErrorDetect&quot;;
	 * 
	 * container.setBooleans(RTE_DEV_ERROR_DETECT, true, true, false);
	 * container.setBooleans(RTE_DEV_ERROR_DETECT, new boolean[] {true, true, false});
	 * </pre>
	 * 
	 * <p/>
	 * If there is <b>no such definition</b> or there are <b>multiple definitions</b> or the <b>definition</b> is <b>not
	 * of expected type</b>, an unchecked exception will be thrown. <br>
	 * If the parameter definition's multiplicity <i>does not allow more than one value</i>, and <i>multiple values are
	 * provided</i>, only <i>the first one will be used</i> to set the parameter!
	 * <p>
	 * {@link ISEAErrorHandler Error handling}<br/>
	 * 
	 * <ul>
	 * <li>{@link ISEAErrorHandler#noDefinitionFound(ISEAObject, String)} if no definition was found
	 * <li>{@link ISEAErrorHandler#multipleDefinitionsFound(ISEAObject, String, List)} if more than one definition was
	 * found
	 * 
	 * @param defName
	 *        the short name of the parameter definition
	 * @param values
	 *        the new value(s) for the parameter, in the form of an array or as a sequence of arguments
	 * @return a reference to this container
	 */
	public ISEAContainer setBooleans(String defName, boolean... values);

	/**
	 * Works just like {@link #setBooleans(String, boolean...)}, the only difference being that it supports handling a
	 * split container by accepting an additional parameter that specifies the fragment where the values should be
	 * added.
	 * 
	 * <p>
	 * {@link ISEAErrorHandler Error handling}<br/>
	 * 
	 * <ul>
	 * <li>{@link ISEAErrorHandler#wrongActiveContainer(ISEAContainer, GContainer)} if the used
	 * <code>activeContainer</code> is not among the {@link ISEAContainer#arGetContainers()} list
	 * 
	 * </ul>
	 * 
	 * @see #setBooleans(String, boolean...)
	 * 
	 * @param activeContainer
	 *        the actual container in which the values will be stored, one of {@link #arGetContainers()}
	 */
	@SuppressWarnings("javadoc")
	public ISEAContainer setBooleans(GContainer activeContainer, String defName, boolean... values);

	/**
	 * Sets the value(s) of the parameter specified by the short name of its definition. <br>
	 * 
	 * <b>NOTE:</b> All existing parameter values owned by this container, that are instances of the specified
	 * definition, will be removed prior to setting the new values.
	 * <p>
	 * The method allows passing an arbitrary set of values. The two calls shown in the snippet bellow are equivalent:
	 * 
	 * <pre>
	 * public static final String RTE_BSW_POSITION_IN_TASK = &quot;RteBswPositionInTask&quot;;
	 * 
	 * BigInteger val1 = new BigInteger(&quot;1&quot;);
	 * BigInteger val2 = new BigInteger(&quot;2&quot;);
	 * BigInteger val3 = new BigInteger(&quot;3&quot;);
	 * 
	 * container.setIntegers(RTE_BSW_POSITION_IN_TASK, val1, val2, val3);
	 * container.setIntegers(RTE_BSW_POSITION_IN_TASK, new BigInteger[] {val1, val2, val3});
	 * </pre>
	 * 
	 * <p/>
	 * If there is <b>no such definition</b> or there are <b>multiple definitions</b> or the <b>definition</b> is <b>not
	 * of expected type</b>, an unchecked exception will be thrown. <br>
	 * If the parameter definition's multiplicity <i>does not allow more than one value</i>, and <i>multiple values are
	 * provided</i>, only <i>the first one will be used</i> to set the parameter!
	 * <p>
	 * {@link ISEAErrorHandler Error handling}<br/>
	 * 
	 * <ul>
	 * <li>{@link ISEAErrorHandler#noDefinitionFound(ISEAObject, String)} if no definition was found
	 * <li>{@link ISEAErrorHandler#multipleDefinitionsFound(ISEAObject, String, List)} if more than one definition was
	 * found
	 * <li>{@link ISEAErrorHandler#definitionNotOfExpectedType(ISEAContainer, String, Class, Class)} if the definition
	 * is of a different type than the one expected
	 * 
	 * @param defName
	 *        the short name of the parameter definition
	 * @param values
	 *        the new value(s) for the parameter, in the form of an array or as a sequence of arguments
	 * @return a reference to this container
	 */
	public ISEAContainer setIntegers(String defName, BigInteger... values);

	/**
	 * Works just like {@link #setIntegers(String, BigInteger...)}, the only difference being that it supports handling
	 * a split container by accepting an additional parameter that specifies the fragment where the values should be
	 * added.
	 * 
	 * <p>
	 * {@link ISEAErrorHandler Error handling}<br/>
	 * 
	 * <ul>
	 * <li>{@link ISEAErrorHandler#wrongActiveContainer(ISEAContainer, GContainer)} if the used
	 * <code>activeContainer</code> is not among the {@link ISEAContainer#arGetContainers()} list
	 * 
	 * </ul>
	 * 
	 * @see #setIntegers(String, BigInteger...)
	 * 
	 * @param activeContainer
	 *        the actual container in which the values will be stored, one of {@link #arGetContainers()}
	 */
	@SuppressWarnings("javadoc")
	public ISEAContainer setIntegers(GContainer activeContainer, String defName, BigInteger... values);

	/**
	 * Sets the value(s) of the parameter specified by the short name of its definition. <br>
	 * 
	 * <b>NOTE:</b> All existing parameter values owned by this container, that are instances of the specified
	 * definition, will be removed prior to setting the new values.
	 * <p>
	 * The method allows passing an arbitrary set of values. The two calls shown in the snippet bellow are equivalent:
	 * 
	 * <pre>
	 * public static final String RTE_BSW_ACTIVATON_OFFSET = &quot;RteBswActivationOffset&quot;;
	 * 
	 * container.setFloats(RTE_BSW_ACTIVATON_OFFSET, 1.1, 1.2, 1.3);
	 * container.setFloats(RTE_BSW_ACTIVATON_OFFSET, new Double[] {1.1, 1.2, 1.3});
	 * </pre>
	 * 
	 * <p/>
	 * If there is <b>no such definition</b> or there are <b>multiple definitions</b> or the <b>definition</b> is <b>not
	 * of expected type</b>, an unchecked exception will be thrown. <br>
	 * If the parameter definition's multiplicity <i>does not allow more than one value</i>, and <i>multiple values are
	 * provided</i>, only <i>the first one will be used</i> to set the parameter!
	 * <p>
	 * {@link ISEAErrorHandler Error handling}<br/>
	 * 
	 * <ul>
	 * <li>{@link ISEAErrorHandler#noDefinitionFound(ISEAObject, String)} if no definition was found
	 * <li>{@link ISEAErrorHandler#multipleDefinitionsFound(ISEAObject, String, List)} if more than one definition was
	 * found
	 * </ul>
	 * 
	 * @param defName
	 *        the short name of the parameter definition
	 * @param values
	 *        the new value(s) for the parameter, in the form of an array or as a sequence of arguments
	 * @return a reference to this container
	 */
	public ISEAContainer setFloats(String defName, Double... values);

	/**
	 * Works just like {@link #setFloats(String, Double...)}, the only difference being that it supports handling a
	 * split container by accepting an additional parameter that specifies the fragment where the values should be
	 * added.
	 * 
	 * <p>
	 * {@link ISEAErrorHandler Error handling}<br/>
	 * 
	 * <ul>
	 * <li>{@link ISEAErrorHandler#wrongActiveContainer(ISEAContainer, GContainer)} if the used
	 * <code>activeContainer</code> is not among the {@link ISEAContainer#arGetContainers()} list
	 * 
	 * </ul>
	 * 
	 * @see #setFloats(String, Double...)
	 * 
	 * @param activeContainer
	 *        the actual container in which the values will be stored, one of {@link #arGetContainers()}
	 */
	@SuppressWarnings("javadoc")
	public ISEAContainer setFloats(GContainer activeContainer, String defName, Double... values);

	/**
	 * Sets the value(s) of the parameter specified by the short name of its definition. <br>
	 * 
	 * <b>NOTE:</b> All existing parameter values owned by this container, that are instances of the specified
	 * definition, will be removed prior to setting the new values.
	 * <p>
	 * The method allows passing an arbitrary set of values. The two calls shown in the snippet bellow are equivalent:
	 * 
	 * <pre>
	 * public static final String RTE_OS_SCHEDULE_POINT = &quot;RteOsSchedulePoint&quot;;
	 * public static final String CONDITIONAL = &quot;CONDITIONAL&quot;;
	 * public static final String NONE = &quot;NONE&quot;;
	 * 
	 * container.setEnums(RTE_OS_SCHEDULE_POINT, CONDITIONAL, NONE);
	 * container.setEnums(RTE_OS_SCHEDULE_POINT, new String[] {CONDITIONAL, NONE});
	 * </pre>
	 * 
	 * <p/>
	 * If there is <b>no such definition</b> or there are <b>multiple definitions</b> or the <b>definition</b> is <b>not
	 * of expected type</b>, an unchecked exception will be thrown. <br>
	 * If the parameter definition's multiplicity <i>does not allow more than one value</i>, and <i>multiple values are
	 * provided</i>, only <i>the first one will be used</i> to set the parameter!
	 * <p>
	 * {@link ISEAErrorHandler Error handling}<br/>
	 * 
	 * <ul>
	 * <li>{@link ISEAErrorHandler#noDefinitionFound(ISEAObject, String)} if no definition is found
	 * <li>{@link ISEAErrorHandler#multipleDefinitionsFound(ISEAObject, String, List)} if more than one definition is
	 * found
	 * <li>{@link ISEAErrorHandler#definitionNotOfExpectedType(ISEAContainer, String, Class, Class)} if the definition
	 * is of a different type than the one expected
	 * <li>{@link ISEAErrorHandler#enumLiteralNotFound(ISEAContainer, String, List, String)} if the provided values are
	 * not among the defined literals
	 * </ul>
	 * 
	 * @param defName
	 *        the short name of the parameter definition
	 * @param values
	 *        the new value(s) for the parameter, in the form of an array or as a sequence of arguments
	 * @return a reference to this container
	 */
	public ISEAContainer setEnums(String defName, String... values);

	/**
	 * Works just like {@link #setEnums(String, String...)}, the only difference being that it supports handling a split
	 * container by accepting an additional parameter that specifies the fragment where the values should be added.
	 * 
	 * <p>
	 * {@link ISEAErrorHandler Error handling}<br/>
	 * 
	 * <ul>
	 * <li>{@link ISEAErrorHandler#wrongActiveContainer(ISEAContainer, GContainer)} if the used
	 * <code>activeContainer</code> is not among the {@link ISEAContainer#arGetContainers()} list
	 * 
	 * </ul>
	 * 
	 * @see #setEnums(String, String...)
	 * 
	 * @param activeContainer
	 *        the actual container in which the values will be stored, one of {@link #arGetContainers()}
	 */
	@SuppressWarnings("javadoc")
	public ISEAContainer setEnums(GContainer activeContainer, String defName, String... values);

	/**
	 * Sets the value(s) of the parameter specified by the short name of its definition. <br>
	 * 
	 * <b>NOTE:</b> All existing parameter values owned by this container, that are instances of the specified
	 * definition, will be removed prior to setting the new values.
	 * <p>
	 * The method allows passing an arbitrary set of values. The two calls shown in the snippet bellow are equivalent:
	 * 
	 * <pre>
	 * public static final String RTE_SECTION_INITIALIZATION_POLICY = &quot;RteSectionInitializationPolicy&quot;;
	 * 
	 * container.setStrings(RTE_SECTION_INITIALIZATION_POLICY, &quot;abc&quot;, &quot;def&quot;);
	 * container.setStrings(RTE_SECTION_INITIALIZATION_POLICY, new String[] {&quot;abc&quot;, &quot;def&quot;});
	 * </pre>
	 * 
	 * <p/>
	 * If there is <b>no such definition</b> or there are <b>multiple definitions</b> or the <b>definition</b> is <b>not
	 * of expected type</b>, an unchecked exception will be thrown. <br>
	 * If the parameter definition's multiplicity <i>does not allow more than one value</i>, and <i>multiple values are
	 * provided</i>, only <i>the first one will be used</i> to set the parameter!
	 * <p>
	 * {@link ISEAErrorHandler Error handling}<br/>
	 * 
	 * <ul>
	 * <li>{@link ISEAErrorHandler#noDefinitionFound(ISEAObject, String)} if no definition is found
	 * <li>{@link ISEAErrorHandler#multipleDefinitionsFound(ISEAObject, String, List)} if more than one definition is
	 * found
	 * <li>{@link ISEAErrorHandler#definitionNotOfExpectedType(ISEAContainer, String, Class, Class)} if the definition
	 * is of a different type than the one expected
	 * </ul>
	 * 
	 * @param defName
	 *        the short name of the parameter definition
	 * @param values
	 *        the new value(s) for the parameter, in the form of an array or as a sequence of arguments
	 * @return a reference to this container
	 */
	public ISEAContainer setStrings(String defName, String... values);

	/**
	 * Works just like {@link #setStrings(String, String...)}, the only difference being that it supports handling a
	 * split container by accepting an additional parameter that specifies the fragment where the values should be
	 * added.
	 * 
	 * <p>
	 * {@link ISEAErrorHandler Error handling}<br/>
	 * 
	 * <ul>
	 * <li>{@link ISEAErrorHandler#wrongActiveContainer(ISEAContainer, GContainer)} if the used
	 * <code>activeContainer</code> is not among the {@link ISEAContainer#arGetContainers()} list
	 * 
	 * </ul>
	 * 
	 * @see #setStrings(String, String...)
	 * 
	 * @param activeContainer
	 *        the actual container in which the values will be stored, one of {@link #arGetContainers()}
	 */
	@SuppressWarnings("javadoc")
	public ISEAContainer setStrings(GContainer activeContainer, String defName, String... values);

	/**
	 * Returns the value of the reference specified by the short name of its definition.
	 * <p/>
	 * If there is <b>no such definition</b> or there are <b>multiple definitions</b> or the <b>definition</b> is <b>not
	 * of expected type</b>, an unchecked exception will be thrown. <br>
	 * If there are <b>multiple values</b>, <i>the first one found</i> will be returned. <br>
	 * <p>
	 * {@link ISEAErrorHandler Error handling}<br/>
	 * <ul>
	 * 
	 * <li>{@link ISEAErrorHandler#noDefinitionFound(ISEAObject, String)} if no definition was found
	 * <li>{@link ISEAErrorHandler#multipleDefinitionsFound(ISEAObject, String, List)} if more than one definition was
	 * found
	 * <li>{@link ISEAErrorHandler#definitionNotOfExpectedType(ISEAContainer, String, Class, Class)} if the definition
	 * is of a different type than the one expected
	 * <li> {@link ISEAErrorHandler#multipleValuesFound(ISEAObject, String, List)} if more than one value was found
	 * </ul>
	 * 
	 * @param defName
	 *        short name of the foreign reference definition
	 * @return the value of the reference or <code>null</code> if the reference is not set
	 */
	public GIdentifiable getForeignReference(String defName);

	/**
	 * Returns a list with the values for the foreign reference specified by the short name of its definition. <br>
	 * The list is modifiable.
	 * <p>
	 * If there is <b>no such definition</b> or there are <b>multiple definitions</b> or the <b>definition</b> is <b>not
	 * of expected type</b>, an unchecked exception will be thrown. <br>
	 * <p>
	 * {@link ISEAErrorHandler Error handling} <br/>
	 * <ul>
	 * 
	 * <li>{@link ISEAErrorHandler#noDefinitionFound(ISEAObject, String)} if no definition was found
	 * <li>{@link ISEAErrorHandler#multipleDefinitionsFound(ISEAObject, String, List)} if more than one definition was
	 * found
	 * <li>{@link ISEAErrorHandler#definitionNotOfExpectedType(ISEAContainer, String, Class, Class)} if the definition
	 * is of a different type than the one expected
	 * 
	 * </ul>
	 * 
	 * @param defName
	 *        the short name of the foreign reference definition
	 * @return a modifiable list with the reference's values or an unmodifiable and empty list if there is no such
	 *         reference definition
	 */
	public ISEAList<GIdentifiable> getForeignReferences(String defName);

	/**
	 * Sets the value of the foreign reference specified by the short name of its definition: <code>defName</code> to
	 * the provided value.<br>
	 * <b>NOTE</b>: Existing reference values of the given definition will be removed prior to setting the new value. <br/>
	 * <p/>
	 * If there is <b>no such reference definition</b>, <i>the call will have no effect</i>. <br>
	 * If there is<b> more than one reference definition</b> with the provided short name, <i>the first one found will
	 * be used</i>. <br>
	 * 
	 * <br/>
	 * {@link ISEAErrorHandler Error handling}<br/>
	 * 
	 * <ul>
	 * <li>{@link ISEAErrorHandler#noDefinitionFound(ISEAObject, String)} if no definition was found
	 * <li>{@link ISEAErrorHandler#multipleDefinitionsFound(ISEAObject, String, List)} if more than one definition was
	 * found
	 * <li>{@link ISEAErrorHandler#definitionNotOfExpectedType(ISEAContainer, String, Class, Class)} if the definition
	 * is of a different type than the one expected
	 * <li>{@link ISEAErrorHandler#valueNotOfDestinationType(ISEAContainer, String, EClass, List)} if the new value does
	 * not match the definition's destination type
	 * </ul>
	 * 
	 * @param defName
	 *        the short name of the foreign reference definition
	 * 
	 * @param value
	 *        the value to be set
	 * 
	 * @return a reference to this container
	 */
	public ISEAContainer setForeignReference(String defName, GIdentifiable value);

	/**
	 * Works just like {@link #setForeignReference(String, GIdentifiable)}, the only difference being that it supports
	 * handling a split container by accepting an additional parameter that specifies the fragment where the value
	 * should be added.
	 * 
	 * <p>
	 * {@link ISEAErrorHandler Error handling}<br/>
	 * 
	 * <ul>
	 * <li>{@link ISEAErrorHandler#wrongActiveContainer(ISEAContainer, GContainer)} if the used
	 * <code>activeContainer</code> is not among the {@link ISEAContainer#arGetContainers()} list
	 * 
	 * </ul>
	 * 
	 * @see #setForeignReference(String, GIdentifiable)
	 * 
	 * @param activeContainer
	 *        the actual container in which the value will be stored, one of {@link #arGetContainers()}
	 */
	@SuppressWarnings("javadoc")
	public ISEAContainer setForeignReference(GContainer activeContainer, String defName, GIdentifiable value);

	/**
	 * Sets the value(s) of the reference specified by the short name of its definition to the provided values. <br>
	 * 
	 * <b>NOTE:</b> All existing reference values owned by this container, that are instances of the specified
	 * definition, will be removed prior to setting the new values.
	 * <p/>
	 * If there is <b>no such definition</b> or there are <b>multiple definitions</b> or the <b>definition</b> is <b>not
	 * of expected type</b>, an unchecked exception will be thrown. <br>
	 * If the reference definition's multiplicity <i>does not allow more than one value</i>, and <i>more than one value
	 * is given</i>, only <i>the first one will be used</i> to set the reference! <br/>
	 * <p>
	 * 
	 * {@link ISEAErrorHandler Error handling}<br/>
	 * 
	 * <ul>
	 * <li>{@link ISEAErrorHandler#noDefinitionFound(ISEAObject, String)} if no definition is found
	 * <li>{@link ISEAErrorHandler#multipleDefinitionsFound(ISEAObject, String, List)} if more than one definition is
	 * found
	 * <li>{@link ISEAErrorHandler#definitionNotOfExpectedType(ISEAContainer, String, Class, Class)} if the definition
	 * is of a different type than the one expected
	 * <li>{@link ISEAErrorHandler#valueNotOfDestinationType(ISEAContainer, String, EClass, List)} if the new value(s)
	 * do not match the definition's destination type
	 * </ul>
	 * 
	 * @param defName
	 *        the short name of the foreign reference definition
	 * @param values
	 *        the new value(s) for the reference
	 * 
	 * @return a reference to this container
	 */
	public ISEAContainer setForeignReferences(String defName, List<GIdentifiable> values);

	/**
	 * Works just like {@link #setForeignReferences(String, List)}, the only difference being that it supports handling
	 * a split container by accepting an additional parameter that specifies the fragment where the values should be
	 * added.
	 * 
	 * <p>
	 * {@link ISEAErrorHandler Error handling}<br/>
	 * 
	 * <ul>
	 * <li>{@link ISEAErrorHandler#wrongActiveContainer(ISEAContainer, GContainer)} if the used
	 * <code>activeContainer</code> is not among the {@link ISEAContainer#arGetContainers()} list
	 * 
	 * </ul>
	 * 
	 * @see #setForeignReferences(String, List)
	 * 
	 * @param activeContainer
	 *        the actual container in which the values will be stored, one of {@link #arGetContainers()}
	 */
	@SuppressWarnings("javadoc")
	public ISEAContainer setForeignReferences(GContainer activeContainer, String defName, List<GIdentifiable> values);

	/**
	 * Removes all references values having the definition specified by its the short name: <code>defName</code>, from
	 * this container.
	 * 
	 * <p/>
	 * If there is <b>no such definition</b> or there are <b>multiple definitions</b> or the <b>definition</b> is <b>not
	 * of expected type</b>, an unchecked exception will be thrown.
	 * <p>
	 * {@link ISEAErrorHandler Error handling}<br/>
	 * 
	 * <ul>
	 * <li>{@link ISEAErrorHandler#noDefinitionFound(ISEAObject, String)} if no definition was found
	 * <li>{@link ISEAErrorHandler#multipleDefinitionsFound(ISEAObject, String, List)} if more than one definition was
	 * found
	 * <li>{@link ISEAErrorHandler#definitionNotOfExpectedType(ISEAContainer, String, Class, Class)} if the definition
	 * is of a different type than the one expected
	 * 
	 * </ul>
	 * 
	 * @param defName
	 *        the short name of the foreign reference definition whose instances are to be removed
	 * @return a reference to this container
	 */
	public ISEAContainer unSetForeignReference(String defName);

	/**
	 * Returns whether the foreign reference specified by the short name of its definition, is set.
	 * <p/>
	 * If there is <b>no such definition</b> or there are <b>multiple definitions</b> or the <b>definition</b> is <b>not
	 * of expected type</b>, an unchecked exception will be thrown.
	 * <p>
	 * {@link ISEAErrorHandler Error handling}<br/>
	 * 
	 * <ul>
	 * <li>{@link ISEAErrorHandler#noDefinitionFound(ISEAObject, String)} if no definition was found
	 * <li>{@link ISEAErrorHandler#multipleDefinitionsFound(ISEAObject, String, List)} if more than one definition was
	 * found
	 * <li>{@link ISEAErrorHandler#definitionNotOfExpectedType(ISEAContainer, String, Class, Class)} if the definition
	 * is of a different type than the one expected
	 * </ul>
	 * 
	 * @param defName
	 *        the short name of the foreign reference definition
	 * @return whether the reference with the specified definition is set.
	 */
	public boolean isSetForeignReference(String defName);

	/**
	 * Returns the value of the reference specified by the short name of its definition.
	 * <p/>
	 * If there is <b>no such definition</b> or there are <b>multiple definitions</b> or the <b>definition</b> is <b>not
	 * of expected type</b>, an unchecked exception will be thrown. <br>
	 * If there are <b>multiple values</b>, <i>the first one found</i> will be returned. <br>
	 * <p>
	 * {@link ISEAErrorHandler Error handling}<br/>
	 * <ul>
	 * <li>{@link ISEAErrorHandler#noDefinitionFound(ISEAObject, String)} if no definition was found
	 * <li>{@link ISEAErrorHandler#multipleDefinitionsFound(ISEAObject, String, List)} if more than one definition was
	 * found
	 * <li>{@link ISEAErrorHandler#definitionNotOfExpectedType(ISEAContainer, String, Class, Class)} if the definition
	 * is of a different type than the one expected
	 * <li> {@link ISEAErrorHandler#multipleValuesFound(ISEAObject, String, List)} if more than one value was found
	 * </ul>
	 * 
	 * @param defName
	 *        short name of the reference definition
	 * @return the value of the reference or <code>null</code> if the reference is not set
	 */
	public ISEAContainer getReference(String defName);

	/**
	 * Returns a list with the values for the reference specified by the short name of its definition. <br>
	 * The list is modifiable.
	 * 
	 * <p>
	 * If there is <b>no such definition</b> or there are <b>multiple definitions</b> or the <b>definition</b> is <b>not
	 * of expected type</b>, an unchecked exception will be thrown. <br>
	 * <p>
	 * {@link ISEAErrorHandler Error handling} <br/>
	 * <ul>
	 * 
	 * <li>{@link ISEAErrorHandler#noDefinitionFound(ISEAObject, String)} if no definition was found
	 * <li>{@link ISEAErrorHandler#multipleDefinitionsFound(ISEAObject, String, List)} if more than one definition is
	 * found
	 * <li>{@link ISEAErrorHandler#definitionNotOfExpectedType(ISEAContainer, String, Class, Class)} if the definition
	 * is of a different type than the one expected
	 * </ul>
	 * 
	 * @param defName
	 *        the short name of the reference definition
	 * @return a list with {@link ISEAContainer} wrappers around the values <br>
	 *         or an unmodifiable and empty list if no definition could be found for the specified short name
	 */
	public ISEAList<ISEAContainer> getReferences(String defName);

	/**
	 * Sets the value for the reference specified by the short name of its definition: <code>defName</code> to the
	 * provided value. <br>
	 * <b>NOTE:</b> All existing reference values owned by this container, that are instances of the specified
	 * definition, will be removed prior to setting the new value.
	 * 
	 * <p/>
	 * If there is <b>no such reference definition</b>, <i>the call will have no effect</i>. <br>
	 * If there is <b>ore than one reference definition</b> with the provided short name, the first one found will be
	 * used.
	 * <p>
	 * {@link ISEAErrorHandler Error handling}<br/>
	 * 
	 * <ul>
	 * 
	 * <li>{@link ISEAErrorHandler#noDefinitionFound(ISEAObject, String)} if no definition was found
	 * <li>{@link ISEAErrorHandler#multipleDefinitionsFound(ISEAObject, String, List)} if more than one definition was
	 * found
	 * <li>{@link ISEAErrorHandler#definitionNotOfExpectedType(ISEAContainer, String, Class, Class)} if the definition
	 * is of a different type than the one expected
	 * <li>{@link ISEAErrorHandler#valueNotOfDestinationType(ISEAContainer, String, EClass, List)} if the new value does
	 * not match the definition's destination type
	 * </ul>
	 * 
	 * @param defName
	 *        the short name of the reference definition
	 * 
	 * @param value
	 *        a {@link ISEAContainer} wrapper around the value to be set
	 * 
	 * @return a reference to this container
	 */
	public ISEAContainer setReference(String defName, ISEAContainer value);

	/**
	 * Works just like {@link #setReference(String, ISEAContainer)}, the only difference being that it supports handling
	 * a split container by accepting an additional parameter that specifies the fragment where the value should be
	 * added.
	 * 
	 * <p>
	 * {@link ISEAErrorHandler Error handling}<br/>
	 * 
	 * <ul>
	 * <li>{@link ISEAErrorHandler#wrongActiveContainer(ISEAContainer, GContainer)} if the used
	 * <code>activeContainer</code> is not among the {@link ISEAContainer#arGetContainers()} list
	 * 
	 * </ul>
	 * 
	 * @see #setReference(String, ISEAContainer)
	 * 
	 * @param activeContainer
	 *        the actual container in which the value will be stored, one of {@link #arGetContainers()}
	 */
	@SuppressWarnings("javadoc")
	public ISEAContainer setReference(GContainer activeContainer, String defName, ISEAContainer value);

	/**
	 * Sets the value(s) for the reference specified by the short name of its definition to the provided values.<br>
	 * 
	 * <b>NOTE:</b> All existing reference values owned by this container, that are instances of the specified
	 * definition, will be removed prior to setting the new values.
	 * <p/>
	 * If there is <b>no such definition</b> or there are <b>multiple definitions</b> or the <b>definition</b> is <b>not
	 * of expected type</b>, an unchecked exception will be thrown. <br>
	 * If the reference definition's multiplicity <i>does not allow more than one value</i>, and <i>more than one value
	 * is given</i>, only <i>the first one will be used</i> to set the reference! <br/>
	 * <p>
	 * {@link ISEAErrorHandler Error handling}<br/>
	 * 
	 * <ul>
	 * <li>{@link ISEAErrorHandler#noDefinitionFound(ISEAObject, String)} if no definition was found
	 * <li>{@link ISEAErrorHandler#multipleDefinitionsFound(ISEAObject, String, List)} if more than one definition was
	 * found
	 * <li>{@link ISEAErrorHandler#definitionNotOfExpectedType(ISEAContainer, String, Class, Class)} if the definition
	 * is of a different type than the one expected
	 * <li>{@link ISEAErrorHandler#valueNotOfDestinationType(ISEAContainer, String, EClass, List)} if the new value(s)
	 * do not match the definition's destination type
	 * </ul>
	 * 
	 * @param defName
	 *        the short name of the reference definition
	 * @param values
	 *        the new value(s) for the reference
	 * 
	 * @return a reference to this container
	 */
	public ISEAContainer setReferences(String defName, List<ISEAContainer> values);

	/**
	 * Works just like {@link #setReferences(String, List)}, the only difference being that it supports handling a split
	 * container by accepting an additional parameter that specifies the fragment where the values should be added.
	 * 
	 * <p>
	 * {@link ISEAErrorHandler Error handling}<br/>
	 * 
	 * <ul>
	 * <li>{@link ISEAErrorHandler#wrongActiveContainer(ISEAContainer, GContainer)} if the used
	 * <code>activeContainer</code> is not among the {@link ISEAContainer#arGetContainers()} list
	 * 
	 * </ul>
	 * 
	 * @see #setReferences(String, List)
	 * 
	 * @param activeContainer
	 *        the actual container in which the values will be stored, one of {@link #arGetContainers()}
	 */
	@SuppressWarnings("javadoc")
	public ISEAContainer setReferences(GContainer activeContainer, String defName, List<ISEAContainer> values);

	/**
	 * Removes all reference values having the definition with the specified short name: <code>defName</code>, from this
	 * container.
	 * 
	 * <p/>
	 * If there is <b>no such definition</b> or there are <b>multiple definitions</b> or the <b>definition</b> is <b>not
	 * of expected type</b>, an unchecked exception will be thrown.
	 * <p>
	 * {@link ISEAErrorHandler Error handling}<br/>
	 * 
	 * <ul>
	 * <li>{@link ISEAErrorHandler#noDefinitionFound(ISEAObject, String)} if no definition was found
	 * <li>{@link ISEAErrorHandler#multipleDefinitionsFound(ISEAObject, String, List)} if more than one definition was
	 * found
	 * <li>{@link ISEAErrorHandler#definitionNotOfExpectedType(ISEAContainer, String, Class, Class)} if the definition
	 * is of a different type than the one expected
	 * </ul>
	 * 
	 * @param defName
	 *        the short name of the reference definition
	 * @return a reference to this container
	 */
	public ISEAContainer unSetReference(String defName);

	/**
	 * Returns whether the reference specified by the short name of its definition, is set.
	 * 
	 * <p/>
	 * If there is <b>no such definition</b> or there are <b>multiple definitions</b> or the <b>definition</b> is <b>not
	 * of expected type</b>, an unchecked exception will be thrown.
	 * <p>
	 * {@link ISEAErrorHandler Error handling}<br/>
	 * 
	 * <ul>
	 * <li>{@link ISEAErrorHandler#noDefinitionFound(ISEAObject, String)} if no definition is found
	 * <li>{@link ISEAErrorHandler#multipleDefinitionsFound(ISEAObject, String, List)} if more than one definition is
	 * found
	 * <li>{@link ISEAErrorHandler#definitionNotOfExpectedType(ISEAContainer, String, Class, Class)} if the definition
	 * is of a different type than the one expected
	 * </ul>
	 * 
	 * @param defName
	 *        the short name of the reference definition
	 * @return whether the reference with the specified definition is set.
	 */
	public boolean isSetReference(String defName);

	/**
	 * Returns a wrapper around the value of the instance reference specified by the short name of its definition.
	 * <p/>
	 * If there is <b>no such definition</b> or there are <b>multiple definitions</b> or the <b>definition</b> is <b>not
	 * of expected type</b>, an unchecked exception will be thrown. <br>
	 * If there are <b>multiple values</b>, <i>the first one found</i> will be returned. <br>
	 * <p>
	 * {@link ISEAErrorHandler Error handling}<br/>
	 * <ul>
	 * <li>{@link ISEAErrorHandler#noDefinitionFound(ISEAObject, String)} if no definition was found
	 * <li>{@link ISEAErrorHandler#multipleDefinitionsFound(ISEAObject, String, List)} if more than one definition was
	 * found
	 * <li>{@link ISEAErrorHandler#definitionNotOfExpectedType(ISEAContainer, String, Class, Class)} if the reference
	 * value is of a different type than the one expected
	 * <li> {@link ISEAErrorHandler#multipleValuesFound(ISEAObject, String, List)} if more than one value was found
	 * </ul>
	 * 
	 * @param defName
	 *        short name of the instance reference definition
	 * @return a wrapper around the instance reference value or <code>null</code> if the reference is not set
	 */
	public ISEAInstanceReference getInstanceReference(String defName);

	/**
	 * Returns a list with wrappers around the values of the instance reference specified by the short name of its
	 * definition. <br>
	 * The list is modifiable.
	 * <p>
	 * If there is <b>no such definition</b> or there are <b>multiple definitions</b> or the <b>definition</b> is <b>not
	 * of expected type</b>, an unchecked exception will be thrown. <br>
	 * <p>
	 * {@link ISEAErrorHandler Error handling} <br/>
	 * <ul>
	 * 
	 * <li>{@link ISEAErrorHandler#noDefinitionFound(ISEAObject, String)} if no definition was found
	 * <li>{@link ISEAErrorHandler#multipleDefinitionsFound(ISEAObject, String, List)} if more than one definition was
	 * found
	 * <li>{@link ISEAErrorHandler#definitionNotOfExpectedType(ISEAContainer, String, Class, Class)} if the provided
	 * name matched a reference of a different type than the one expected
	 * 
	 * </ul>
	 * 
	 * @param defName
	 *        the short name of the instance reference definition
	 * @return a modifiable list with wrappers around the values or an unmodifiable and empty list if there is no such
	 *         reference definition
	 */
	public ISEAList<ISEAInstanceReference> getInstanceReferences(String defName);

	/**
	 * Removes all instance references values having the definition specified by the short name of its definition:
	 * <code>defName</code>, from this container.
	 * 
	 * <p/>
	 * If there is <b>no such definition</b> or there are <b>multiple definitions</b> or the <b>definition</b> is <b>not
	 * of expected type</b>, an unchecked exception will be thrown.
	 * <p>
	 * {@link ISEAErrorHandler Error handling}<br/>
	 * 
	 * <ul>
	 * <li>{@link ISEAErrorHandler#noDefinitionFound(ISEAObject, String)} if no definition was found
	 * <li>{@link ISEAErrorHandler#multipleDefinitionsFound(ISEAObject, String, List)} if more than one definition was
	 * found
	 * <li>{@link ISEAErrorHandler#definitionNotOfExpectedType(ISEAContainer, String, Class, Class)} if the definition
	 * is of a different type than the one expected
	 * </ul>
	 * 
	 * @param defName
	 *        the short name of the instance reference definition whose instances are to be removed
	 * @return a reference to this container
	 */
	public ISEAContainer unSetInstanceReference(String defName);

	/**
	 * Returns whether the instance reference specified by the short name of its definition, is set.
	 * <p/>
	 * If there is <b>no such definition</b> or there are <b>multiple definitions</b> or the <b>definition</b> is <b>not
	 * of expected type</b>, an unchecked exception will be thrown.
	 * <p>
	 * {@link ISEAErrorHandler Error handling}<br/>
	 * 
	 * <ul>
	 * <li>{@link ISEAErrorHandler#noDefinitionFound(ISEAObject, String)} if no definition was found
	 * <li>{@link ISEAErrorHandler#multipleDefinitionsFound(ISEAObject, String, List)} if more than one definition was
	 * found
	 * <li>{@link ISEAErrorHandler#definitionNotOfExpectedType(ISEAContainer, String, Class, Class)} if the definition
	 * is of a different type than the one expected
	 * </ul>
	 * 
	 * @param defName
	 *        the short name of the instance reference definition
	 * @return whether the instance reference with the specified definition is set.
	 */
	public boolean isSetInstanceReference(String defName);

	/**
	 * Creates an instance reference value having the definition with the specified <code>defName</code> short name.
	 * <p>
	 * 
	 * If there is <b>no such reference definition</b>, <i>the call will have no effect</i>. <br>
	 * If there is <b>more than one definition</b> with the provided short name, <i>the first one will be used </i>.<br>
	 * If the reference definition's multiplicity <i>does not allow more than one value</i>, all its existing instances
	 * will be removed prior to creating the new one.
	 * <p>
	 * {@link ISEAErrorHandler Error handling}<br/>
	 * 
	 * <ul>
	 * <li>{@link ISEAErrorHandler#noDefinitionFound(ISEAObject, String)} if no definition was found
	 * <li>{@link ISEAErrorHandler#multipleDefinitionsFound(ISEAObject, String, List)} if more than one definition was
	 * found
	 * <li>{@link ISEAErrorHandler#definitionNotOfExpectedType(ISEAContainer, String, Class, Class)} if the definition
	 * is of a different type than the one expected
	 * </ul>
	 * 
	 * @param defName
	 *        short name of the instance reference definition
	 * @return the SEA wrapper around the created instance reference value or <code>null</code> if no definition could
	 *         be found
	 */
	public ISEAInstanceReference createInstanceReference(String defName);

	/**
	 * Works just like {@link #createInstanceReference(String)}, the only difference being that it supports handling a
	 * split container by accepting an additional parameter that specifies the fragment where the value should be added.
	 * 
	 * <p>
	 * {@link ISEAErrorHandler Error handling}<br/>
	 * 
	 * <ul>
	 * <li>{@link ISEAErrorHandler#wrongActiveContainer(ISEAContainer, GContainer)} if the used
	 * <code>activeContainer</code> is not among the {@link ISEAContainer#arGetContainers()} list
	 * 
	 * </ul>
	 * 
	 * @see #createInstanceReference(String)
	 * 
	 * @param activeContainer
	 *        the actual container in which the values will be stored, one of {@link #arGetContainers()}
	 */
	@SuppressWarnings("javadoc")
	public ISEAInstanceReference createInstanceReference(GContainer activeContainer, String defName);

	/**
	 * Returns whether the parameter/reference specified by the short name of its definition, is set.
	 * <p/>
	 * If there is <b>no such definition</b> or there are <b>multiple definitions</b>, an unchecked exception will be
	 * thrown.
	 * <p>
	 * {@link ISEAErrorHandler Error handling}<br/>
	 * 
	 * <ul>
	 * <li>{@link ISEAErrorHandler#noDefinitionFound(ISEAObject, String)} if no definition was found
	 * <li>{@link ISEAErrorHandler#multipleDefinitionsFound(ISEAObject, String, List)} if more than one definition was
	 * found
	 * </ul>
	 * 
	 * @param defName
	 *        the short name of the parameter/reference definition to be checked
	 * @return whether the parameter/reference with the given name is set
	 */
	public boolean isSet(String defName);

	/**
	 * Removes all the parameter/reference values given by the short name of its definition, from this container.
	 * <p/>
	 * If there is <b>no such definition</b> or there are <b>multiple definitions</b>, an unchecked exception will be
	 * thrown.
	 * <p>
	 * {@link ISEAErrorHandler Error handling}<br/>
	 * 
	 * <ul>
	 * <li>{@link ISEAErrorHandler#noDefinitionFound(ISEAObject, String)} if no definition was found
	 * <li>{@link ISEAErrorHandler#multipleDefinitionsFound(ISEAObject, String, List)} if more than one definition was
	 * found
	 * </ul>
	 * 
	 * @param defName
	 *        the short name of the parameter/reference definition whose instances are to be removed
	 * @return a reference to this container
	 */
	public ISEAContainer unSet(String defName);

	/**
	 * Works just like {@link #createContainer(String)}, the only difference being that it supports handling a split
	 * container by accepting an additional parameter that specifies the fragment where the child should be added.
	 * 
	 * <p>
	 * {@link ISEAErrorHandler Error handling}<br/>
	 * 
	 * <ul>
	 * <li>{@link ISEAErrorHandler#wrongActiveContainer(ISEAContainer, GContainer)} if the used
	 * <code>activeContainer</code> is not among the {@link ISEAContainer#arGetContainers()} list
	 * 
	 * </ul>
	 * 
	 * @see #createContainer(String)
	 * 
	 * @param activeContainer
	 *        the actual container in which the child will be stored, one of {@link #arGetContainers()}
	 */
	@SuppressWarnings("javadoc")
	public ISEAContainer createContainer(GContainer activeContainer, String defName);

	/**
	 * Works just like {@link #createContainer(String, String)}, the only difference being that it supports handling a
	 * split container by accepting an additional parameter that specifies the fragment where the child should be added.
	 * 
	 * <p>
	 * {@link ISEAErrorHandler Error handling}<br/>
	 * 
	 * <ul>
	 * <li>{@link ISEAErrorHandler#wrongActiveContainer(ISEAContainer, GContainer)} if the used
	 * <code>activeContainer</code> is not among the {@link ISEAContainer#arGetContainers()} list
	 * 
	 * </ul>
	 * 
	 * @see #createContainer(String, String)
	 * 
	 * @param activeContainer
	 *        the actual container in which the child will be stored, one of {@link #arGetContainers()}
	 */
	@SuppressWarnings("javadoc")
	public ISEAContainer createContainer(GContainer activeContainer, String defName, String shortName);

	/**
	 * Works just like {@link #createContainer(String, String, boolean)}, the only difference being that it supports
	 * handling a split container by accepting an additional parameter that specifies the fragment where the child
	 * should be added.
	 * 
	 * <p>
	 * {@link ISEAErrorHandler Error handling}<br/>
	 * 
	 * <ul>
	 * <li>{@link ISEAErrorHandler#wrongActiveContainer(ISEAContainer, GContainer)} if the used
	 * <code>activeContainer</code> is not among the {@link ISEAContainer#arGetContainers()} list
	 * 
	 * </ul>
	 * 
	 * @see #createContainer(String, String, boolean)
	 * 
	 * @param activeContainer
	 *        the actual container in which the child will be stored, one of {@link #arGetContainers()}
	 */
	@SuppressWarnings("javadoc")
	public ISEAContainer createContainer(GContainer activeContainer, String defName, String shortName,
		boolean deriveNameFromSuggestion);

	/**
	 * Works just like {@link #createChoiceContainer(String, String)}, the only difference being that it supports
	 * handling a split container by accepting an additional parameter that specifies the fragment where the child
	 * should be added.
	 * 
	 * <p>
	 * {@link ISEAErrorHandler Error handling}<br/>
	 * 
	 * <ul>
	 * <li>{@link ISEAErrorHandler#wrongActiveContainer(ISEAContainer, GContainer)} if the used
	 * <code>activeContainer</code> is not among the {@link ISEAContainer#arGetContainers()} list
	 * 
	 * </ul>
	 * 
	 * @see #createChoiceContainer(String, String)
	 * 
	 * @param activeContainer
	 *        the actual container in which the child will be stored, one of {@link #arGetContainers()}
	 */
	@SuppressWarnings("javadoc")
	public ISEAContainer createChoiceContainer(GContainer activeContainer, String defName, String chosenDefName);

	/**
	 * Works just like {@link #createChoiceContainer(String, String, String)}, the only difference being that it
	 * supports handling a split container by accepting an additional parameter that specifies the fragment where the
	 * child should be added.
	 * 
	 * <p>
	 * {@link ISEAErrorHandler Error handling}<br/>
	 * 
	 * <ul>
	 * <li>{@link ISEAErrorHandler#wrongActiveContainer(ISEAContainer, GContainer)} if the used
	 * <code>activeContainer</code> is not among the {@link ISEAContainer#arGetContainers()} list
	 * 
	 * </ul>
	 * 
	 * @see #createChoiceContainer(String, String, String)
	 * 
	 * @param activeContainer
	 *        the actual container in which the child will be stored, one of {@link #arGetContainers()}
	 */
	@SuppressWarnings("javadoc")
	public ISEAContainer createChoiceContainer(GContainer activeContainer, String defName, String chosenDefName,
		String shortName);

	/**
	 * Works just like {@link #createChoiceContainer(String, String, String, boolean)}, the only difference being that
	 * it supports handling a split container by accepting an additional parameter that specifies the fragment where the
	 * child should be added.
	 * 
	 * <p>
	 * {@link ISEAErrorHandler Error handling}<br/>
	 * 
	 * <ul>
	 * <li>{@link ISEAErrorHandler#wrongActiveContainer(ISEAContainer, GContainer)} if the used
	 * <code>activeContainer</code> is not among the {@link ISEAContainer#arGetContainers()} list
	 * 
	 * </ul>
	 * 
	 * @see #createChoiceContainer(String, String, String, boolean)
	 * 
	 * @param activeContainer
	 *        the actual container in which the child will be stored, one of {@link #arGetContainers()}
	 */
	@SuppressWarnings("javadoc")
	public ISEAContainer createChoiceContainer(GContainer activeContainer, String defName, String chosenDefName,
		String shortName, boolean deriveNameFromSuggestion);
}
