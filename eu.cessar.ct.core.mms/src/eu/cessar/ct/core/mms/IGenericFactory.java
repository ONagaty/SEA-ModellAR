/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 * 
 * </copyright>
 */
package eu.cessar.ct.core.mms;

import gautosar.gecucdescription.GConfigReferenceValue;
import gautosar.gecucdescription.GContainer;
import gautosar.gecucdescription.GModuleConfiguration;
import gautosar.gecucdescription.GParameterValue;
import gautosar.gecucparameterdef.GConfigParameter;
import gautosar.gecucparameterdef.GConfigReference;
import gautosar.gecucparameterdef.GModuleDef;
import gautosar.ggenericstructure.ggeneraltemplateclasses.gadmindata.GAdminData;
import gautosar.ggenericstructure.ggeneraltemplateclasses.gspecialdata.GSd;
import gautosar.ggenericstructure.ggeneraltemplateclasses.gspecialdata.GSdg;
import gautosar.ggenericstructure.ggeneraltemplateclasses.gspecialdata.GSdgContents;
import gautosar.ggenericstructure.ginfrastructure.GARPackage;
import gautosar.ggenericstructure.ginfrastructure.GAUTOSAR;

import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;

/**
 * This interface must have an implementation for each AUTOSAR MM version. It is responsible with creation of elements
 * within BSW side. Use {@link IMetaModelService#getGenericFactory()} to get an instance.
 * 
 * @Review uidl6458 - 30.03.2012
 */
public interface IGenericFactory
{

	/**
	 * Return the concrete EClass that shall be used for a generic EClass in the current AUTOSAR model.
	 * 
	 * @param genericClass
	 *        the EClass from GAUTOSAR
	 * @return the concrete EClass if such a class exist or null
	 */
	public EClass getConcreteEClass(EClass genericClass);

	/**
	 * Create a module configuration
	 * 
	 * @return a new module configuration
	 */
	public GModuleConfiguration createModuleConfiguration();

	/**
	 * Create a container
	 * 
	 * @return a new container
	 */
	public GContainer createContainer();

	/**
	 * Create the {@link GParameterValue} corresponding to the {@link GConfigParameter paramDef} definition. The
	 * destination of the newly created value will be set to the <code>paramDef</code>
	 * 
	 * @param paramDef
	 *        the parameter def
	 * @return a new parameter value, never null
	 */
	public GParameterValue createParameterValue(GConfigParameter paramDef);

	/**
	 * If the <code>useDefaultValue</code> is false the method is identical with
	 * {@link #createParameterValue(GConfigParameter)}. If is true and a default value is set a {@link GParameterValue}
	 * corresponding to the {@link GConfigParameter paramDef} definition will be created and the value will be set to
	 * the default one. If there is no default value set null will be returned.
	 * 
	 * @param paramDef
	 *        the parameter definition
	 * @param useDefaultValue
	 *        true if defaults shall be used
	 * @return a parameter value if the definition have default or null if default are required but not found
	 * @deprecated 4.0.3.4<br/>
	 *             The method is misleading, use either {@link #createParameterValue(GConfigParameter)} or
	 *             {@link #createParameterValueWithDefault(GConfigParameter)} instead. This method will delegate to the
	 *             mentioned methods depending on the status of the <code>useDefaultValue</code> flag
	 */
	@Deprecated
	public GParameterValue createParameterValue(GConfigParameter paramDef, boolean useDefaultValue);

	/**
	 * Create a new parameter value that will have also the value set to the default value of the parameter definition.
	 * If there is no default available, null will be returned.
	 * 
	 * @param paramDef
	 *        the parameter definition
	 * @return a new parameter value or null if nod defaults are found
	 */
	public GParameterValue createParameterValueWithDefault(GConfigParameter paramDef);

	/**
	 * Create the {@link GConfigReferenceValue} corresponding to the {@link GConfigReference refDef} definition. The
	 * destination of the newly created value will be set to the <code>refDef</code>
	 * 
	 * @param refDef
	 * @return a newly created object
	 */
	public GConfigReferenceValue createReferenceValue(GConfigReference refDef);

	/**
	 * Create an ARPackage specific to this metamodel
	 * 
	 * @return a newly created object
	 */
	public GARPackage createARPackage();

	/**
	 * Create a Module definition specific to this metamodel
	 * 
	 * @return a newly created object
	 */
	public GModuleDef createModuleDef();

	/**
	 * Create an AUTOSAR specific to this metamodel
	 * 
	 * @return a newly created object
	 */
	public GAUTOSAR createAUTOSAR();

	/**
	 * Return a list with all classifiers defined inside the model.
	 * 
	 * @return a list with all classifiers, never null. Even better, with the same model it shall return the same result
	 *         so the index is of a particular classifier is stable in time even between sessions.
	 */
	public List<EClassifier> getAllClassifiers();

	/**
	 * Create an AdminData specific to this metamodel
	 *
	 * @return a newly created AdminData object
	 */
	public GAdminData createAdminData();

	/**
	 * Create an Sdg specific to this metamodel
	 *
	 * @return a newly created object
	 */
	public GSdg createSdg();

	/**
	 * Create an SdgContents specific to this metamodel
	 *
	 * @return a newly created object
	 */
	public GSdgContents createSdgContents();

	/**
	 * Create an Sd specific to this metamodel
	 *
	 * @return a newly created object
	 */
	public GSd createSd();
}
