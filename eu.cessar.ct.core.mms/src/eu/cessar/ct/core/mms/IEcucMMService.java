/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 * 
 * </copyright>
 */
package eu.cessar.ct.core.mms;

import eu.cessar.ct.core.mms.ecuc.IBSWModuleDescription;
import eu.cessar.ct.core.mms.ecuc.convertors.IParameterValueConvertor;
import eu.cessar.ct.core.platform.EExternalRepresentationType;
import gautosar.gecucdescription.GConfigReferenceValue;
import gautosar.gecucdescription.GContainer;
import gautosar.gecucdescription.GInstanceReferenceValue;
import gautosar.gecucdescription.GModuleConfiguration;
import gautosar.gecucdescription.GParameterValue;
import gautosar.gecucdescription.GReferenceValue;
import gautosar.gecucparameterdef.GCommonConfigurationAttributes;
import gautosar.gecucparameterdef.GConfigParameter;
import gautosar.gecucparameterdef.GModuleDef;
import gautosar.gecucparameterdef.GParamConfContainerDef;
import gautosar.gecucparameterdef.GParamConfMultiplicity;
import gautosar.ggenericstructure.ginfrastructure.GIdentifiable;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.Enumerator;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;

/**
 * Various utilities regarding ECUC but MM dependent. Use {@link IMetaModelService#getEcucMMService()} to get an
 * instance
 * 
 * @author uidl6458
 * 
 * @Review uidl6458 - 29.03.2012
 */
public interface IEcucMMService
{
	/**
	 * The constant used to represent a "*" inside the upperMultiplicity field
	 */
	public static final BigInteger MULTIPLICITY_STAR = new BigInteger("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF", 16); //$NON-NLS-1$

	/**
	 * 
	 */
	public static final int ERROR_ELEMENT_NULL = -1;
	/**
	 * 
	 */
	public static final int ERROR_ELEMENT_PROXY = -2;
	/**
	 * 
	 */
	public static final int ERROR_DEF_NULL = -3;
	/**
	 * 
	 */
	public static final int ERROR_DEF_PROXY = -4;
	/**
	 * 
	 */
	public static final int ERROR_DIFF_PARENTS = -5;
	/**
	 * 
	 */
	public static final int ERROR_ELEMENT_PARENT_NOT_VALID = -6;
	/**
	 * 
	 */
	public static final int ERROR_DEF_PARENT_NOT_VALID = -7;
	/**
	 * 
	 */
	public static final int ERROR_DIFF_TYPES = -8;
	/**
	 * 
	 */
	public static final int ERROR_ELEMENT_PARENT_NULL = -9;
	/**
	 * 
	 */
	public static final int ERROR_DEF_PARENT_NULL = -10;
	/**
	 * 
	 */
	public static final int ERROR_DEF_NOT_VALID = -11;

	/**
	 * Return true if a CESSAR-CT compatible API could be used for this MM version or false otherwise
	 * 
	 * @return true if this MM support a compatibility layer
	 */
	public boolean haveCompatibilityLayer();

	/**
	 * Return the base URI that will be used by the presentation model API
	 * 
	 * @param project
	 * @param compatibility
	 *        if true the URI for compatibility mode will be returned
	 * @return the URI of the project
	 */
	public String getPresentationModelURI(IProject project, boolean compatibility);

	/**
	 * Return true if the <code>feature</code> is part of the ecuc API definition. An ecuc API definition feature is a
	 * feature which, if changed, the PM api need to be regenerated. Please note that this should includes only features
	 * that exists inside a Module Definition.
	 * 
	 * @param object
	 *        a valid EObject
	 * @param feature
	 *        a valid feature of that EObject
	 * @return true if the feature object is part of an ecuc API definition feature
	 */
	public boolean isEcucAPIDefinitionFeature(EObject object, EStructuralFeature feature);

	/**
	 * Return true if the <code>eClass</code> is part of the ecuc definition API
	 * 
	 * @param eClass
	 *        a valid EClass
	 * @return true if the EClass object is part of an ecuc API definition feature
	 */
	public boolean isEcucAPIDefinitionClass(EClass eClass);

	/**
	 * Return the target of the instance reference
	 * 
	 * @param iRefValue
	 * @return the iref target
	 */
	public GIdentifiable getInstanceRefTarget(GInstanceReferenceValue iRefValue);

	/**
	 * Set the target of an instance reference. You should use an EMF transaction.
	 * 
	 * @param iRefValue
	 * @param newTarget
	 */
	public void setInstanceRefTarget(GInstanceReferenceValue iRefValue, GIdentifiable newTarget);

	/**
	 * Return the context of the instance ref. The list is live.
	 * 
	 * @param iRefValue
	 * @return the iref context
	 */
	public EList<GIdentifiable> getInstanceRefContext(GInstanceReferenceValue iRefValue);

	/**
	 * Replace the existing context with a new one. An EMF transaction shall be used.
	 * 
	 * @param iRefValue
	 * @param newContext
	 */
	public void setInstanceRefContext(GInstanceReferenceValue iRefValue, List<GIdentifiable> newContext);

	/**
	 * Returns whether a <code>GContainer</code> is editable depending on the project configuration variant and
	 * <code>configDefinition</code>'s configuration class
	 * 
	 * @param container
	 *        the object to edit
	 * @param configDefinition
	 *        the parameter/reference definition
	 * @return <code>true</code> if <code>container</code> is editable<br>
	 *         <code>false</code>, otherwise
	 */
	public boolean isVariantEditable(GContainer container, GCommonConfigurationAttributes configDefinition);

	/**
	 * Return true if the model needs an intermediate container for choice
	 * 
	 * @return true if an intermediate container is needed for choice container
	 */
	public boolean needChoiceIntermediateContainer();

	/**
	 * Returns the String representation of the ImplementationConfigVariant of the given <code>modConfiguration</code>
	 * 
	 * @param modConfiguration
	 * @return return the config variant as a string
	 */

	public String getImplementationConfigVariantAsString(GModuleConfiguration modConfiguration);

	/**
	 * Returns whether the implementationConfigVariant feature is set for the given <code>gModuleConfiguration</code>.
	 * 
	 * @param gModuleConfiguration
	 *        the given module configuration
	 * @return true if the config variant is set
	 */
	public boolean isSetImplementationConfigVariant(GModuleConfiguration gModuleConfiguration);

	/**
	 * Determine the validity of the lower multiplicity.
	 * 
	 * @param numberOfObjects
	 *        the number of objects
	 * @param gParamConfMultiplicity
	 *        the lower multiplicity setting
	 * @return true if valid, false otherwise
	 */
	public boolean isValidLowerMultiplicity(int numberOfObjects, GParamConfMultiplicity gParamConfMultiplicity);

	/**
	 * Return the lower multiplicity of the respective {@link GParamConfMultiplicity}. If there is no lower multiplicity
	 * set the provided default value will be returned instead.
	 * 
	 * @param multiplicity
	 *        the multiplicity setting
	 * @param defaultValue
	 *        the default value as {@code BigInteger}
	 * @param ecucVariant
	 *        the variant
	 * @return return the lower multiplicity of the container
	 */
	public BigInteger getLowerMultiplicity(GParamConfMultiplicity multiplicity, BigInteger defaultValue,
		boolean ecucVariant);

	/**
	 * Determine the validity of the upper multiplicity.
	 * 
	 * @param numberOfObjects
	 *        the number of objects
	 * @param gParamConfMultiplicity
	 *        the upper multiplicity setting
	 * @return true if valid, false otherwise
	 */
	public boolean isValidUpperMultiplicity(int numberOfObjects, GParamConfMultiplicity gParamConfMultiplicity);

	/**
	 * Return the upper multiplicity of the respective {@link GParamConfMultiplicity}. If there is no upper multiplicity
	 * set the provided default value will be returned instead. If the upper multiplicity is set on infinite the
	 * {@link #MULTIPLICITY_STAR} constant will be returned.
	 * 
	 * @param multiplicity
	 *        the multiplicity setting
	 * @param defaultValue
	 *        the default value as {@code BigInteger}
	 * @param ecucVariant
	 *        the variant
	 * @return the upper multiplicity of the container
	 */
	public BigInteger getUpperMultiplicity(GParamConfMultiplicity multiplicity, BigInteger defaultValue,
		boolean ecucVariant);

	/**
	 * 
	 * @return the EClass used to hold the BSW Module Description
	 */
	public EClass getBSWModuleDescriptionClass();

	/**
	 * Create a wrapper for the BSW Module Description. The <code>bswModuleDescription</code> must be an instance of the
	 * EClass returned by {@link #getBSWModuleDescriptionClass()}
	 * 
	 * @param bswModuleDescription
	 * @return a wrapper for the bsw module description
	 */
	public IBSWModuleDescription getBSWModuleDescriptionWrapper(EObject bswModuleDescription);

	/**
	 * Checks whether the given container definition has the <code>multipleConfigurationContainer</code> feature set.
	 * 
	 * @param gContainerDef
	 *        the given container definition
	 * @return <code>true</code>, if the feature is set<br>
	 *         <code>false</code>, otherwise
	 */
	public boolean isMultipleConfigurationContainer(GParamConfContainerDef gContainerDef);

	/**
	 * Return the list of supported configuration variants of the given module definition.
	 * 
	 * @param gModuleDef
	 *        the given module definition
	 * @return the list of configuration variants or an empty list if no configuration variant is found or the feature
	 *         is not applicable (ex. for MM 2.0,2.1).
	 */
	public List<? extends Enumerator> getSupportedConfigVariants(GModuleDef gModuleDef);

	/**
	 * 
	 * @param source
	 * @return a map of the supported configuration variants of the given GCommonConfigurationAttributes
	 */
	public Map<String, List<String>> getSupportedVariants(GCommonConfigurationAttributes source);

	/**
	 * 
	 * @param source
	 * @param literal
	 * @param value
	 */
	public void setSupportedVariant(GCommonConfigurationAttributes source, String literal, String value);

	/**
	 * @param source
	 * @param value
	 * @param literal
	 */
	public void unsetSupportedVariant(GCommonConfigurationAttributes source, String value, String literal);

	/**
	 * Sets the value of the implementation configuration variant feature of the given module configuration or unsets it
	 * if <code>value</code> is <code>null</code>.
	 * 
	 * @param gModuleConfiguration
	 *        the given module configuration
	 * @param value
	 */
	public void setImplementationConfigVariantAsString(GModuleConfiguration gModuleConfiguration, String value);

	/**
	 * Returns true if the given parameter definition has a default value set.
	 * 
	 * @param paramDef
	 *        the parameter definition to investigate
	 * @return <code>true</code>, if a default value is set<br>
	 *         <code>false</code>, otherwise
	 */
	public boolean isSetDefaultValue(GConfigParameter paramDef);

	/**
	 * Returns the default value of the given parameter definition.
	 * 
	 * @param paramDef
	 *        the parameter definition to investigate
	 * @return the default value, if it is set<br>
	 *         <code>null</code>, otherwise
	 */
	public Object getDefaultValue(GConfigParameter paramDef);

	/**
	 * Returns the value of the given <code>paramValue</code> as a string. For a textual parameter value it return its
	 * value.<br>
	 * For a numerical parameter value, it returns the string corresponding to the evaluation of its variation point.
	 * 
	 * @param paramValue
	 *        the parameter value
	 * @return the value of the given <code>paramValue</code> as a string
	 */
	public String getParameterValueAsString(GParameterValue paramValue);

	/**
	 * Returns the value of the given <code>paramValue</code>.
	 * 
	 * @param paramValue
	 *        the parameter value
	 * @return the value of the given <code>paramValue</code>
	 */
	public Object getParameterValue(GParameterValue paramValue);

	/**
	 * Checks if a {@link GModuleConfiguration} is correctly defined.
	 * 
	 * @param config
	 * @return returns {@link IStatus#isOK()} if is OK, false otherwise and in that case the error code will be one of
	 *         the constants defined in this interface
	 */
	public IStatus validateDefinition(GModuleConfiguration config);

	/**
	 * Checks if a {@link GContainer} is correctly defined.
	 * 
	 * @param container
	 * @return returns {@link IStatus#isOK()} if is OK, false otherwise and in that case the error code will be one of
	 *         the constants defined in this interface
	 */
	public IStatus validateDefinition(GContainer container);

	/**
	 * Checks if a {@link GParameterValue} is correctly defined.
	 * 
	 * @param parameter
	 * @return returns {@link IStatus#isOK()} if is OK, false otherwise and in that case the error code will be one of
	 *         the constants defined in this interface
	 */
	public IStatus validateDefinition(GParameterValue parameter);

	/**
	 * Checks if a {@link GConfigReferenceValue} is correctly defined.
	 * 
	 * @param reference
	 * @return returns {@link IStatus#isOK()} if is OK, false otherwise and in that case the error code will be one of
	 *         the constants defined in this interface
	 */
	public IStatus validateDefinition(GConfigReferenceValue reference);

	/**
	 * Return the status of the multiple configuration container flag. It will return null if the flag is not set.
	 * 
	 * @param cntDef
	 * @return the value of the multiple configuration container flag, could return null
	 */
	public Boolean getMultipleConfigurationContainer(GParamConfContainerDef cntDef);

	/**
	 * Returns the {@link org.eclipse.emf.ecore.EReference#isContainment containment} feature that properly contains the
	 * {@link GParameterValue}(s)
	 * 
	 * @return the containment feature of {@link GParameterValue}(s)
	 */
	public EReference getParameterValuesFeature();

	/**
	 * Returns the {@link org.eclipse.emf.ecore.EReference#isContainment containment} feature that properly contains the
	 * {@link GReferenceValue}(s)
	 * 
	 * @return the containment feature of {@link GReferenceValue}(s)
	 */
	public EReference getReferenceValuesFeature();

	/**
	 * Return the mapping between the parameter values type and their external representation.
	 * 
	 * @return a mapping where the key represents the external representation and the value is the parameter value type
	 */
	public Map<EExternalRepresentationType, Class<? extends GParameterValue>> getParameterTypeConversionMatrix();

	/**
	 * Creates and returns the converter suitable for the provided parameter definition.
	 * 
	 * @param definition
	 *        definition for which to obtain the conveter
	 * @return the created converter
	 */
	public <S extends GParameterValue, T extends GConfigParameter, R> IParameterValueConvertor<S, T, R> getParameterValueConvertor(
		GConfigParameter definition);

	/**
	 * Sets NOTE: in case the <code>parameterValue</code> is not of the corresponding type, an unchecked exception will
	 * be throws
	 * 
	 * @param parameterValue
	 * @param value
	 */
	public void setBooleanParameterValue(GParameterValue parameterValue, Boolean value);

	/**
	 * NOTE: in case the <code>parameterValue</code> is not of the corresponding type, an unchecked exception will be
	 * 
	 * @param parameterValue
	 * @param value
	 */
	public void setFLoatParameterValue(GParameterValue parameterValue, Double value);

	/**
	 * NOTE: in case the <code>parameterValue</code> is not of the corresponding type, an unchecked exception will be
	 * 
	 * @param parameterValue
	 * @param value
	 */
	public void setIntegerParameterValue(GParameterValue parameterValue, BigInteger value);

	/**
	 * Sets the specified <code>parameter value</code> <br>
	 * NOTE: in case the <code>parameterValue</code> is not of the corresponding type, an unchecked exception will be
	 * 
	 * @param parameterValue
	 * @param value
	 */
	public void setStringParameterValue(GParameterValue parameterValue, String value);
}
