/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Oct 12, 2009 1:04:53 PM </copyright>
 */
package eu.cessar.ct.core.mms;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;

import com.google.common.base.Function;

import eu.cessar.ct.core.mms.mdl.IModelDependencyLookup2;
import eu.cessar.ct.core.platform.util.ERadix;

/**
 * This interface provided a common place to access various services that are metamodel dependent. An instance can be
 * retrieved using {@link MMSRegistry#getMMService(EClass)} method
 * 
 * @Review uidl6458 - 30.03.2012
 */
public interface IMetaModelService
{
	/**
	 * Return a string list with LEnum languages
	 * 
	 * @return
	 */
	public List<String> getLanguages();

	public static enum IgnorableFeaturesAplication
	{
		Sorting, SysUI, TableUI
	}

	public static enum EcucModuleConfigGroups
	{
		RTE, SYSTEM, DIAGNOSE, ONBOARD_ABSTRACTION, MEMORY, MEMORY_ABSTRACTION, COMMUNICATION, CAN, CAN_ABSTRACTION, ETHERNET, ETHERNET_ABSTRACTION, FLEXRAY, FLEXRAY_ABSTRACTION, LIN, LIN_ABSTRACTION, OTHERS, CONTROLLER_DRIVER, MEMORY_DRIVER, COMMUNICATION_DRIVER, IO_DRIVER,
	}

	/**
	 * Get meta model services that deal with the ECUC side of the metamodel
	 * 
	 * @return
	 */
	public IEcucMMService getEcucMMService();

	/**
	 * Return the compatibility service
	 * 
	 * @return
	 */
	public ICompatibilityService getCompatibilityService();

	/**
	 * Return the generic factory instance, corresponding to the current MM version.
	 * 
	 * @return MM implementation of {@link IGenericFactory} interface.
	 */
	public IGenericFactory getGenericFactory();

	/**
	 * Return true if the feature is splitable. The splitable concept is MM dependent but at least the feature should be
	 * contained, should have an unlimited upper bound and the EClass of the feature type should be an Identifiable. If
	 * the method return true, values of such features can be stored in separate files.
	 * 
	 * @param feature
	 *        the feature to check for splitable
	 * @return true if the feature is splitable
	 */
	public boolean isSplitableReference(EReference feature);

	/**
	 * Return the list of feature of the <code>clz</code> argument that are splitable
	 * 
	 * @param clz
	 *        a MM class
	 * @return list of class references, never null
	 */
	public List<EReference> getSplitingReferences(EClass clz);

	/**
	 * The MDL member.
	 * 
	 * @return the MDL resolver for the model
	 */
	public IModelDependencyLookup2 getModelDependencyLookup();

	/**
	 * Return true if the <code></code> clz have at least one {@link EReference} that is s splitable reference.
	 * 
	 * @param clz
	 *        a MM clz
	 * @return true if the clz have splitable references, false otherwise
	 */
	public boolean isSplitable(EClass clz);

	/**
	 * Locates an EClass with the given name and returns it. If the EClass cannot be located, <code>null</code> is
	 * returned.
	 * 
	 * @param name
	 * @return
	 */
	public EClass findEClass(String name);

	/**
	 * Return the feature that represents the target for the given instance reference eClass.
	 * 
	 * <br>
	 * NOTE: If none or more than 1 reference target is located, an <code>IllegalArgumntException</code> will be thrown
	 * 
	 * @param clz
	 *        the eClass of a instance reference
	 * @throws IllegalArgumentException
	 *         if the EClass is not an instance reference or is not configured correctly
	 * @return the target feature of the given eClass
	 */
	public EReference getTargetFeature(EClass clz);

	/**
	 * Return the list of features that made the context of this instance reference.
	 * 
	 * @param clz
	 *        the eClass of a instance reference
	 * @throws IllegalArgumentException
	 *         if the EClass is not an instance reference or is not configured correctly
	 * @return the list of context features, never null
	 */
	public List<EReference> getContextFeatures(EClass clz);

	/**
	 * Return <code>true</code> if the given parameter represents the eClass of a System instance reference, otherwise
	 * return <code>false</code>
	 * 
	 * @param clz
	 * @return
	 */
	public boolean isSystemInstanceRef(EClass clz);

	/**
	 * Depending on the meta-model, returns weather System instance refs have a base reference. If <code>true</code>,
	 * the parent of instance ref's first context must conform to the EType of the base reference
	 * 
	 * @return <code>true</code> or <code>false</code> depending on the meta-model
	 */
	public boolean hasBaseInstanceRef();

	/**
	 * Return a matrix of features_No x 2, containing the multiplicity of each context feature
	 * 
	 * @param clz
	 *        the Class of A System Instance Reference
	 * @param usePureMM
	 *        if true the multiplicities of the strict model will be returned if the MM support it
	 * @return
	 */
	public int[][] getContextFeatureMultiplicities(EClass clz, boolean usePureMM);

	/**
	 * Returns true if model has support for changing radix information
	 * 
	 * @param object
	 * @param attribute
	 * 
	 * @return
	 */
	public boolean canStoreRadixInformation(EObject object, EAttribute attribute);

	/**
	 * Retrieve radix, if canStoreRadixInformation() return true defaults to ERadix.DECIMAL vs null
	 * 
	 * @param obj
	 * @param attr
	 * @param idx
	 *        for multiple attributes return the position
	 * @return
	 */
	public ERadix getRadix(EObject obj, EAttribute attr, int idx);

	/**
	 * Must be wrapped within a transaction
	 * 
	 * @param obj
	 * @param attr
	 * @param radix
	 * @param idx
	 */
	public void setRadix(EObject obj, EAttribute attr, ERadix radix, int idx);

	/**
	 * return the direct subclasses of ecls. If concreteOnly is true that only the non-abstract classes will be
	 * returned.
	 * 
	 * @param ecls
	 * @param concreteOnly
	 * @return a collection of EClass, never null;
	 */
	public Collection<EClass> getESubClasses(EClass ecls, boolean concreteOnly);

	/**
	 * return the direct or indirect subclasses of ecls. If concreteOnly is true that only the non-abstract classes will
	 * be returned.
	 * 
	 * @param ecls
	 * @param concreteOnly
	 * @return a collection of EClass, never null;
	 */
	public Collection<EClass> getEAllSubClasses(EClass ecls, boolean concreteOnly);

	/**
	 * Returns a list of features that will be ignored by the application corresponding to the given
	 * IgnorableFeaturesAplication element
	 * 
	 * @param aplication
	 *        TODO
	 * @param clz
	 * 
	 * @return a Collection of EClass, never null;
	 */
	public Collection<EStructuralFeature> getIgnorableFeatures(IgnorableFeaturesAplication aplication);

	/**
	 * @return one of the following integers: 21,3 or 4, depending on the model
	 */
	public int getAutosarReleaseOrdinal();

	/**
	 * Returns a collection of containment EReferences, that are of interest for the composition provider specialized on
	 * System side, for an input on ECUC type (GModuleConfiguration or GContainer)
	 * 
	 * @return
	 */
	public Collection<EReference> getAcceptedFeaturesForCompositionProvider();

	/**
	 * Returns the extensions supported
	 * 
	 * @return
	 */
	public String[] getFileExtensions();

	/**
	 * Return the label of the package as it is meant to be displayed into an UI
	 * 
	 * @param packageURI
	 * @return
	 */
	public String getPackageLabel(String packageURI);

	/**
	 * Return the URI of the root package of the metamodel
	 * 
	 * @return
	 */
	public String getRootPackageURI();

	/**
	 * Returns a collection of containment EReferences, that are of interest for the table viewer when creating the
	 * descriptors for new child elements, for an input on ECUC type (GModuleConfiguration or GContainer)
	 * 
	 * @return
	 */
	public Collection<EReference> getAcceptedFeaturesForTableViewerMenu();

	/**
	 * @return
	 */
	public Map<String, String> getAllPackages();

	/**
	 * Returns the mapping between the features whose XML names are to be ignored during the loading/serializing
	 * consistency check in case inconsistencies are found at element level, and the corresponding XML names. <br>
	 * <i>Note: </i> the first call will create an internal cache that will be reused for all subsequent calls; in order
	 * to clear it, call {@link #discardIgnorableFeatureToXMLNameMapping()}
	 * 
	 * @return a map where the key represents the feature and the value signifies its XML name
	 */
	public Map<EStructuralFeature, String> getIgnorableFeatureToXMLNameMapping();

	/**
	 * Discards the internal cache that keeps the mapping between the features whose XML names are to be ignored during
	 * the loading/serializing consistency check and the corresponding XML names
	 */
	public void discardIgnorableFeatureToXMLNameMapping();

	/**
	 * @return a map that shows the link between the ModuleConfigGroups and the standard module definition
	 */
	public Map<EcucModuleConfigGroups, Set<String>> groupedModuleDefinitionMap();

	/**
	 * @return returns a list of all module definition names assigned in the ModuleConfigGroups.
	 */
	public List<String> getAllAssignedMD();

	/**
	 * @return the EClass of the root element (AUTOSAR).
	 */
	EClass getRootEClass();

	/**
	 * Retrieve the root AUTOSAR object.
	 * 
	 * @param resource
	 *        the resource
	 * @return the root AUTOSAR object or <code>null</code> if not found
	 */
	EObject getRootAUTOSARObject(Resource resource);

	/**
	 * Create an instance of the same {@code EClass} as {@code eObject} and copy the split key features from
	 * {@code eObject} to the newly created instance.
	 * 
	 * @param eObject
	 *        the {@code EObject} to be "copied"
	 * @param parentFeature
	 *        the parent feature
	 * @return the newly created "copy" instance
	 */
	EObject splitEObject(EObject eObject);

	/**
	 * Get the split key associated with {@code feature}.
	 * 
	 * @param feature
	 *        the feature for which to compute the split key
	 * 
	 * @return the {atp.Splitkey component => getter/setter} map
	 */
	Map<String, List<Function<Object, Object>>> getSplitKey(EStructuralFeature feature);
}
