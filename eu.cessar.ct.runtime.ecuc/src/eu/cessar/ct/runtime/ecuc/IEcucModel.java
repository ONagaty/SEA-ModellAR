/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidl6458 Oct 8, 2009 10:56:40 AM </copyright>
 */
package eu.cessar.ct.runtime.ecuc;

import eu.cessar.ct.core.mms.IMetaModelService;
import eu.cessar.ct.core.mms.ecuc.IBSWModuleDescription;
import eu.cessar.ct.runtime.ecuc.util.ESplitableList;
import eu.cessar.ct.runtime.ecuc.util.SplitedEntry;
import eu.cessar.ct.sdk.IPostBuildContext;
import eu.cessar.ct.sdk.sea.ISEAModel;
import gautosar.gecucdescription.GConfigReferenceValue;
import gautosar.gecucdescription.GContainer;
import gautosar.gecucdescription.GModuleConfiguration;
import gautosar.gecucdescription.GParameterValue;
import gautosar.gecucparameterdef.GConfigParameter;
import gautosar.gecucparameterdef.GConfigReference;
import gautosar.gecucparameterdef.GContainerDef;
import gautosar.gecucparameterdef.GModuleDef;
import gautosar.gecucparameterdef.GParamConfContainerDef;
import gautosar.ggenericstructure.ginfrastructure.GARPackage;
import gautosar.ggenericstructure.ginfrastructure.GIdentifiable;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.resource.Resource;

/**
 * The ECUC Model class provides various methods to handle the BSW model, that's it the model provided by the MM classes
 * from the ecucdescription and ecucparamdef packages<br/>
 * There are three category of methods that this class provides:<br/>
 * <ul>
 * <li>Methods to locate module definitions and arpackages that contain module definitions</li>
 * <li>Methods to go thru refinements concept for modules and containers</li>
 * <li>Methods to locate ecucdescription elements</li>
 * </ul>
 *
 */
public interface IEcucModel
{

	/**
	 * Clear whatever private cache informations are stored
	 */
	public void modelChanged();

	/**
	 * Return all EMF resources that exists inside the project, never null.
	 *
	 * @return the collection with the resources, never null
	 */
	public Collection<Resource> getAllResources();

	/**
	 * Return all EMF resources from the project that contains module definitions, never null.
	 *
	 * @return the collection with the resources, never null
	 */
	public Collection<Resource> getResourcesWithModuleDefs();

	/**
	 * Return the root ARPackages from the model that contain directly or indirectly module definitions. Because a
	 * particular ARPackage could be defined multiple times in different package (it is &lt;&lt;atSplitable&gt;&gt;) a
	 * map will be returned. As key is the package short name, and as value is the list of GARPackages, never null.
	 *
	 * @return a map with all root ar packages that contains module definitions
	 */
	public Map<String, List<GARPackage>> getRootArPackagesWithModuleDefs();

	/**
	 * Return the root ARPackages from the model that contain directly or indirectly module configurations. Because a
	 * particular ARPackage could be defined multiple times in different package (it is &lt;&lt;atSplitable&gt;&gt;) a
	 * map will be returned. As key is the package short name, and as value is the list of GARPackages, never null.
	 *
	 * @return a map with all root ar packages that contains module configurations
	 */
	public Map<String, List<GARPackage>> getRootArPackagesWithModuleConfigurations();

	/**
	 * Return all ARPackages that are direct children of the ARPackge denoted by the <code>ownerPackageQName</code>
	 * qualified name and contain, directly or indirectly, at least a module definition. Because a particular ARPackage
	 * could be defined multiple times in different package (it is &lt;&lt;atSplitable&gt;&gt;) a map will be returned.
	 * As key is the package short name, and as value is the list of GARPackages, never null.<br/>
	 * If the <code>ownerPackageQName</code> is null or equal with "/" then the root packages will be returned.
	 *
	 * @param ownerPackageQName
	 * @return a map with children arpackages that contain directly or indirectly module definitions
	 */
	public Map<String, List<GARPackage>> getArPackagesWithModuleDefs(String ownerPackageQName);

	/**
	 * Return all ARPackages that are direct children of the ARPackge denoted by the <code>ownerPackageQName</code>
	 * qualified name and contain, directly or indirectly, at least a module configuration. Because a particular
	 * ARPackage could be defined multiple times in different package (it is &lt;&lt;atSplitable&gt;&gt;) a map will be
	 * returned. As key is the package short name, and as value is the list of GARPackages, never null.<br/>
	 * If the <code>ownerPackageQName</code> is null or equal with "/" then the root packages will be returned.
	 *
	 * @param ownerPackageQName
	 * @return a map with children arpackages that contain directly or indirectly module configurations
	 */
	public Map<String, List<GARPackage>> getArPackagesWithModuleCfgs(String ownerPackageQName);

	/**
	 * Return all arpackages that have the qualified name equal with <code>packQName</code> and contain, directly or
	 * indirectly module configurations
	 *
	 * @param packQName
	 * @return the list of packages never null
	 */
	public List<GARPackage> getSplitedPackagesWithModuleCfg(String packQName);

	/**
	 * Return the module definitions that are direct under the ARPackage denoted by the <code>ownerPackageQName</code>
	 * qualified name.
	 *
	 * @param ownerPackageQName
	 * @return a list with module definitions, never null
	 */
	public List<GModuleDef> getModuleDefsFromPackage(String ownerPackageQName);

	/**
	 * Lookup inside the project for a module definition with a particular qualified name. Return <code>null</code> if
	 * there is no such module
	 *
	 * @param moduleDefQName
	 *        the qualified name of a module definition
	 * @return the module definition or null if there is no such module definition
	 */
	public GModuleDef getModuleDef(String moduleDefQName);

	/**
	 * Return all module definitions that are available in a particular project
	 *
	 * @return a list of module definitions, never null
	 */
	public List<GModuleDef> getAllModuleDefs();

	/**
	 * Return a list of modules that directly refines the <code>module</code>
	 *
	 * @param module
	 * @return a list of modules, never null
	 */
	public List<GModuleDef> getRefiningModuleDefs(GModuleDef module);

	/**
	 * Returns a list of all the modules that directly or indirectly refines the given <code>module</code> <br>
	 * For example: <br>
	 *
	 * <pre>
	 *        |--B<--A
	 *   C<---|
	 *        |--D<--E
	 * </pre>
	 *
	 * if b refines c, <br>
	 * and a refines b, <br>
	 * and d refines c. <br>
	 * and e refines d.<br>
	 * Then the method getAllRefiningModulesDefs(c) will return a list which contains b,a,d,e (In this order).
	 *
	 * @param module
	 * @return a list of all the modules that refine, directly or indirectly, the given <code>module</code>. <br>
	 *         OR NULL if a cycle was detected
	 */
	public List<GModuleDef> getAllRefiningModuleDefs(GModuleDef module);

	/**
	 * Returns a list that contains all the module definitions that are in the <code>module</code>'s family.<br>
	 * By family we understand all the module definitions that refine or are refined by the module, including the module <br>
	 * The returned list will contain the modules in the following order: <br>
	 * the list returned by getRefinedModuleDefs(<code>module</code>), in inverse order,<br>
	 * concatenated with<br>
	 * the given </code>module</code>,<br>
	 * concatenated with<br>
	 * the list returned by getAllRefiningModuleDefs( <code>module</code> ). <br>
	 * <br>
	 * FOR EXAMPLE: if we have the following situation:<br>
	 *
	 * <pre>
	 *                  |--module_B<--module<--module_D
	 *   module_C<---|
	 *                  |--module_E
	 * </pre>
	 *
	 * <code>module</code> refines module_b <br>
	 * module_b refines module_c<br>
	 * module_c is a standard module def<br>
	 * module refines module_b<br>
	 * module_d refines <code>module</module><br>
	 * module_e refines module_c<br>
	 * The method will return a list with the elements in the following order:<br>
	 * module_c,module_b,<code>module</code>,module_d
	 *
	 * @param module
	 * @return the list of modules never null
	 */
	public List<GModuleDef> getRefinedModuleDefsFamily(GModuleDef module);

	/**
	 * Returns a list that contains all the container definitions that are in the <code>container</code>'s family.<br>
	 * By family we understand all the container definitions that refine or are refined by the container, including the
	 * given container <br>
	 * The returned list will contain the containers in the following order: <br>
	 * the list returned by getRefinedContainerDefs(<code>module</code>), in inverse order,<br>
	 * concatenated with<br>
	 * the given </code>container</code>,<br>
	 * concatenated with<br>
	 * the list returned by getAllRefiningContainerDefs(<code>container</code> ). <br>
	 * <br>
	 * FOR EXAMPLE: if we have the following situation:<br>
	 *
	 * <pre>
	 *                  |--container_B<--container<--Container_D
	 *   container_C<---|
	 *                  |--container_E
	 * </pre>
	 *
	 * <code>container</code> refines container_b <br>
	 * container_b refines container_c<br>
	 * container_c is a standard container def<br>
	 * container refines <code>container_b</module><br>
	 * container_d refines <code>container</module><br>
	 * container_E refines container_C<br>
	 * The method will return a list with the elements in the following order:<br>
	 * container_c,container_b,<code>container</code>,container_d
	 *
	 * @param container
	 *        the container
	 *
	 * @return the list of containers never null
	 */
	public List<GIdentifiable> getRefinedContainerDefsFamily(GContainerDef container);

	/**
	 * Returns a mapping between the module definitions that are in the same family as the given <code>moduleDef</code>
	 * and their module configurations. <br>
	 * Because a module configuration might be splitable (MM specific, see
	 * {@link IMetaModelService#isSplitable(org.eclipse.emf.ecore.EClass)}) the map has as value the mapping between the
	 * module configurations' qualified name and the list of actual module configurations. <br>
	 * <i>NOTE </i>: module definitions that do not have module configuration(s) will not be part of the mapping
	 *
	 * @param moduleDef
	 * @return the map, never null
	 */
	public Map<GModuleDef, Map<String, List<GModuleConfiguration>>> getSplitedModuleCfgsFromSameFamily(
		GModuleDef moduleDef);

	/**
	 * Return the module definition that owns (directly or indirectly) the <code>container</code>. If the container is
	 * not owned by a module definition null will be returned and an error will be logged
	 *
	 * @param container
	 *        a container definition
	 * @return the owner GModuleDef or null if none found
	 */
	public GModuleDef getModuleDef(GContainerDef container);

	/**
	 * Return the module definition that owns the <code>parameter</code>. If the parameter is not owned by a module
	 * definition null will be returned and an error will be logged
	 *
	 * @param parameter
	 *        a parameter definition
	 * @return the owner GModuleDef or null if none found
	 */
	public GModuleDef getModuleDef(GConfigParameter parameter);

	/**
	 * Return the module definition that owns the <code>reference</code>. If the reference is not owned by a module
	 * definition null will be returned and an error will be logged
	 *
	 * @param reference
	 *        a reference definition
	 * @return the owner GModuleDef or null if none found
	 */
	public GModuleDef getModuleDef(GConfigReference reference);

	/**
	 * Return a list of module definitions that the <code>module</code> refines. The order in the list is relevant and
	 * follow the following rule:<br/>
	 * Suppose we have the following scenario:<br/>
	 * <code>module</code> refines <code>module_A</code><br/>
	 * <code>module_A</code> refines <code>module_B</code><br/>
	 * <code>module_B</code> is a standard module def<br/>
	 * Then the returned list will have the following content:<br/>
	 * list[0] will be <code>module_A</code><br/>
	 * list[1] will be <code>module_B</code>
	 *
	 * @param module
	 * @return a list of module definitions start with the most refined one.
	 */
	public List<GModuleDef> getRefinedModuleDefs(GModuleDef module);

	/**
	 * Return a list of GIdentifiable (usually of type GContainerDef but please check the <strong>Note</strong>) that
	 * the <code>container</code> refines. The order in the list is relevant and follow the following rule:<br/>
	 * Suppose we have the following scenario:<br/>
	 * <code>container</code> refines <code>container_A</code><br/>
	 * <code>container_A</code> refines <code>container_B</code><br/>
	 * <code>container_B</code> is inside a standard module def<br/>
	 * Then the returned list will have the following content:<br/>
	 * list[0] will be <code>container_A</code><br/>
	 * list[1] will be <code>container_B</code><br/>
	 * <br/>
	 * <strong>Note:</strong> Normally, the returned list should contain objects of type GContainerDef, however there
	 * are certain situations when inside the list other types of object could be found. Suppose the following
	 * scenario:</br> Assume that the <code>container</code> have the QName: <code>/pack/mod/cnt1/container</code><br/>
	 * The owner Module definitions is <code>/pack/mod</code> which is a refinement of <code>/pack/parentmod</code><br/>
	 * If inside <code>/pack/parentmod</code> exist an integer definition with the QName
	 * <code>/pack/parentmod/cnt1/container</code> then that object will be returned by the method
	 *
	 *
	 * @param container
	 * @return the list, never null
	 */
	public List<GIdentifiable> getRefinedContainerDefs(GContainerDef container);

	/**
	 * Lookup a compatible (in the refinement sense) container definition that is defined inside the target module.
	 * Return null if no such container can be found
	 *
	 * @param targetModule
	 * @param refinedContainer
	 * @return the container def, could be null
	 */
	public GContainerDef getRefinedContainerDefFamily(GModuleDef targetModule, GContainerDef refinedContainer);

	/**
	 * Returns a list of container def that directly refine the <code>container</code>
	 *
	 * @param container
	 *
	 * @return a list of container defs, never null
	 */
	public List<GIdentifiable> getRefiningContainerDefs(GContainerDef container);

	/**
	 * Returns a list of all the containers that directly or indirectly refine the given <code>container</code> <br>
	 * For example: <br>
	 *
	 * <pre>
	 *        |--B<--A
	 *   C<---|
	 *        |--D<--E
	 * </pre>
	 *
	 * if b refines c, <br>
	 * and a refines b, <br>
	 * and d refines c. <br>
	 * and e refines d.<br>
	 * Then the method getAllRefiningModulesDefs(c) will return a list which contains b,a,d,e (In this order).
	 *
	 * @param container
	 * @return a list of all the containers that refine, directly or indirectly, the given <code>container</code>. <br>
	 *         OR NULL if a cycle was detected
	 */
	public List<GIdentifiable> getAllRefiningContainerDefs(GContainerDef container);

	/**
	 * Return a list of GIdentifiable (usually of type GConfigParameter but please check the <strong>Note</strong>) that
	 * the <code>parameter</code> refines. The order in the list is relevant and follow the following rule:<br/>
	 * Suppose we have the following scenario:<br/>
	 * <code>parameter</code> refines <code>parameter_A</code><br/>
	 * <code>parameter_A</code> refines <code>parameter_B</code><br/>
	 * <code>parameter_B</code> is inside container from a standard module def<br/>
	 * Then the returned list will have the following content:<br/>
	 * list[0] will be <code>parameter_A</code><br/>
	 * list[1] will be <code>parameter_B</code><br/>
	 * <br/>
	 * <strong>Note:</strong> Please check the similar note from {@link #getRefinedContainerDefs(GContainerDef)}
	 *
	 * @param parameter
	 * @return the list of parameter defs, never null
	 */
	public List<GIdentifiable> getRefinedParameterDefs(GConfigParameter parameter);

	/**
	 * Lookup a compatible (in the refinement sense) parameter definition that is defined inside the target module.
	 * Return null if no such parameter can be found
	 *
	 * @param targetModule
	 * @param refinedParameter
	 * @return the parameter or null if not found
	 */
	public GConfigParameter getRefinedParameterDefFamily(GModuleDef targetModule, GConfigParameter refinedParameter);

	/**
	 * Return a list of GIdentifiable (usually of type GConfigReference but please check the <strong>Note</strong>) that
	 * the <code>parameter</code> refines. The order in the list is relevant and follow the following rule:<br/>
	 * Suppose we have the following scenario:<br/>
	 * <code>parameter</code> refines <code>parameter_A</code><br/>
	 * <code>parameter_A</code> refines <code>parameter_B</code><br/>
	 * <code>parameter_B</code> is inside container from a standard module def<br/>
	 * Then the returned list will have the following content:<br/>
	 * list[0] will be <code>parameter_A</code><br/>
	 * list[1] will be <code>parameter_B</code><br/>
	 * <br/>
	 * <strong>Note:</strong> Please check the similar note from {@link #getRefinedContainerDefs(GContainerDef)}
	 *
	 * @param reference
	 * @return
	 */
	public List<GIdentifiable> getRefinedReferenceDefs(GConfigReference reference);

	/**
	 * Lookup a compatible (in the refinement sense) reference definition that is defined inside the target module.
	 * Return null if no such parameter can be found
	 *
	 * @param targetModule
	 * @param refinedReference
	 * @return the reference or null if not found
	 */
	public GConfigReference getRefinedReferenceDefFamily(GModuleDef targetModule, GConfigReference refinedReference);

	/**
	 * Return one of the <code>GContainerDef</code> that is used as a possible destination inside the
	 * <code>definition</code> and is also in the same family as the definition of the <code>instance</code>. If such a
	 * definition cannot be found, null will be returned. Of course, the definition is valid to be only Choice reference
	 * def, Symbolic or simple reference def. It is an error to call this method either with a ForeignReferenceDef or
	 * with an InstanceReferenceDef.
	 *
	 * @param definition
	 * @param instance
	 * @return the container def or null if not found
	 */
	public GContainerDef getRefinedContainerDef(GConfigReference definition, GContainer instance);

	/**
	 * Return a list of container definitions that are defined by <code>module</code> or by any of the modules that the
	 * module refines
	 *
	 * @param module
	 * @return
	 */
	public List<GContainerDef> collectContainerDefs(GModuleDef module);

	/**
	 * Return a list of container definitions that are defined by <code>container</code> or by any of the containers
	 * that the container refines
	 *
	 * @param container
	 * @return
	 */
	public List<GContainerDef> collectContainerDefs(GContainerDef container);

	/**
	 * Searches for container definitions inside the given <code>context</code>'s subtree (itself included), that match
	 * the specified <code>pathFragment</code>.
	 *
	 * @param context
	 *        container definition where to start the search at
	 * @param pathFragment
	 *        part of a definition's qualified name as per {@link ISEAModel#pathFragment}.
	 * @return the list with all matching container definitions from the <code>context</code>'s subtree
	 */
	public List<GContainerDef> collectMatchingContainerDefs(GContainerDef context, String pathFragment);

	/**
	 * Return a list of parameter definitions that are defined by <code>container</code> or by any of the containers
	 * that that container refines
	 *
	 * @param container
	 * @return
	 */
	public List<GConfigParameter> collectParameterDefs(GParamConfContainerDef container);

	/**
	 * Return a list of reference definitions that are defined by <code>container</code> or by any of the containers
	 * that the container refines
	 *
	 * @param container
	 * @return
	 */
	public List<GConfigReference> collectReferenceDefs(GParamConfContainerDef container);

	/**
	 * Return all EMF resources from the project that contains module configurations, never null.
	 *
	 * @return
	 */
	public Collection<Resource> getResourcesWithModuleCfgs();

	/**
	 * Return all module configurations that are available in a particular project
	 *
	 * @return a list of module configurations, never null
	 */
	public List<GModuleConfiguration> getAllModuleCfgs();

	/**
	 * Return true if the <code>config</code> is a preconfigured configuration, false otherwise
	 *
	 * @param config
	 * @return true if is a preconfigured configuration
	 */
	public boolean isPreconfiguredConfiguration(GModuleConfiguration config);

	/**
	 * Return true if the <code>config</code> is a recommended configuration, false otherwise
	 *
	 * @param config
	 * @return true if is a recommended configuration
	 */
	public boolean isRecommendedConfiguration(GModuleConfiguration config);

	/**
	 * Return true if the <code>config</code> is a regular configuration (not recommended and not preconfigured).
	 *
	 * @param config
	 * @return true if is a regular configuration
	 */
	public boolean isRegularConfiguration(GModuleConfiguration config);

	/**
	 * Return a read-only list of BSW Module descriptions associated with the <code>moduleDef</code>, never null.
	 *
	 * @param moduleDef
	 * @return
	 */
	public List<IBSWModuleDescription> getBSWModuleDescriptions(GModuleDef moduleDef);

	/**
	 * Lookup inside the project for all module configurations with a particular qualified name.
	 *
	 * @param moduleCfgQName
	 *        the qualified name of a module configuration
	 * @return a list of module configurations, never null
	 */
	public List<GModuleConfiguration> getModuleCfg(String moduleCfgQName);

	/**
	 * Return all module configurations for a particular module definition
	 *
	 * @param moduleDef
	 *        a module definition
	 * @return a list of module configurations, never null
	 */
	public List<GModuleConfiguration> getModuleCfgs(GModuleDef moduleDef);

	/**
	 * Return the module configurations that are direct under the ARPackage denoted by the
	 * <code>ownerPackageQName</code> qualified name.
	 *
	 * @param ownerPackageQName
	 * @return a list with module configurations, never null
	 */
	public List<GModuleConfiguration> getModuleCfgsFromPackage(String ownerPackageQName);

	/**
	 * Return the module configuration that owns (directly or indirectly) the <code>container</code>. If the container
	 * is not owned by a module configuration, null will be returned and an error will be logged.
	 *
	 * @param container
	 *        a container instance
	 * @return the owner {@link GModuleConfiguration} or null if none found
	 */
	public GModuleConfiguration getModuleCfg(GContainer container);

	/**
	 * Return all module configurations that exists inside the project for a particular module definition. Because
	 * module configuration might be splitable (MM specific, see
	 * {@link IMetaModelService#isSplitable(org.eclipse.emf.ecore.EClass)}) a map will be returned. As key the fully
	 * qualified name of the module configurations will be set and as value is a list of module configurations.
	 *
	 * @param module
	 *        a module definition
	 * @return a map with module configurations, never null
	 */
	public Map<String, List<GModuleConfiguration>> getSplitedModuleCfgs(GModuleDef module);

	/**
	 * @param moduleDefs
	 *        list with module definitions for which configurations are asked
	 * @return a <K, <S, L>> mapping, where: K - the module definition for which at least one configuration has been
	 *         found, S- qualified name of the configuration, L- list of a configuration's fragments
	 */
	public Map<GModuleDef, Map<String, List<GModuleConfiguration>>> getSplitedModuleCfgs(List<GModuleDef> moduleDefs);

	/**
	 * Return a list with all module configurations that have the same QName as the <code>cfg</code> argument and same
	 * {@link GModuleDef} definition. The list will always contain as first element the <code>cfg</code> argument.
	 *
	 * @param cfg
	 *        a Module configuration
	 * @return a list of module configurations
	 */
	public List<GModuleConfiguration> getSplitedModuleCfgs(GModuleConfiguration cfg);

	/**
	 * @param owners
	 *        parents of the searched containers
	 * @param definition
	 *        definition of searched split containers
	 * @return list with elements of {@link SplitedEntry} type, each element representing a wrapper over a split
	 *         container of the given definition, whose parent is among the provided configurations
	 */
	public List<SplitedEntry<GContainer>> getSplitedContainersForModule(List<GModuleConfiguration> owners,
		GContainerDef definition);

	/**
	 *
	 * @param ancestors
	 *        ancestors of the searched containers
	 * @param definition
	 *        definition of searched split containers
	 * @return list with elements of {@link SplitedEntry} type, each element representing a wrapper over a split
	 *         container of the given definition, that reside directly or indirectly in the given module configurations
	 */
	public List<SplitedEntry<GContainer>> getSplitedContainersFromModule(List<GModuleConfiguration> ancestors,
		GContainerDef definition);

	/**
	 *
	 * @param owners
	 *        parents of the searched containers
	 * @param definition
	 *        definition of searched split containers
	 * @return list with elements of {@link SplitedEntry} type, each element representing a wrapper over a split
	 *         container of the given definition, whose parent is among the provided containers
	 */
	public List<SplitedEntry<GContainer>> getSplitedContainersForContainer(List<GContainer> owners,
		GContainerDef definition);

	/**
	 *
	 * @param ancestors
	 *        ancestors of the searched containers
	 * @param definition
	 *        definition of searched split containers
	 * @return list with elements of {@link SplitedEntry} type, each element representing a wrapper over a split
	 *         container of the given definition, that reside directly or indirectly in the given containers
	 */
	public List<SplitedEntry<GContainer>> getSplitedContainersFromContainer(List<GContainer> ancestors,
		GContainerDef definition);

	/**
	 * Returns a list with elements of {@link SplitedEntry} type, each element representing a wrapper over a split
	 * container of the given definition
	 *
	 * @param definition
	 * @return list with split containers; if the given <code>definition</code> is null, <code>null</code> is returned
	 */
	public List<SplitedEntry<GContainer>> getSplitedContainers(GContainerDef definition);

	/**
	 * @param container
	 * @return
	 */
	public ESplitableList<GContainer> getSplitedSiblingContainers(GContainer container);

	/**
	 * @param <T>
	 * @param owners
	 * @param paramClass
	 * @param paramDef
	 * @return
	 */
	public <T extends GParameterValue> ESplitableList<T> getSplitedParameters(List<GContainer> owners,
		Class<T> paramClass, GConfigParameter paramDef);

	/**
	 * @param <T>
	 * @param owners
	 * @param paramClass
	 * @param paramDef
	 * @return
	 */
	public <T extends GConfigReferenceValue> ESplitableList<T> getSplitedReferences(List<GContainer> owners,
		Class<T> paramClass, GConfigReference paramDef);

	/**
	 * Returns the list of container definitions from the given <code>moduleDef</code>, that have the feature multiple
	 * configuration container, set.
	 *
	 * @param moduleDef
	 *        the given module definition
	 * @return the list of container definitions or an empty list if no container is found or the model not support the
	 *         feature (for example MM20).
	 */
	public List<GParamConfContainerDef> getMultipleConfigurationContainerDefs(GModuleDef moduleDef);

	/**
	 * Return all post build contexts defined inside the module configurations from the current project. If
	 * <code>includeNotUsed</code> is true then the one defined in project properties and not used, if any, are also
	 * returned.
	 *
	 * @param includeNotUsed
	 *        if true the unused build contexts are also returned
	 * @return
	 */
	public Map<IPostBuildContext, List<GContainer>> getAllPostBuildContexts(boolean includeNotUsed);

	/**
	 * Return all post build contexts defined inside the provided module configuration
	 *
	 * @param moduleCfg
	 * @return
	 */
	public Map<IPostBuildContext, List<GContainer>> getPostBuildContexts(GModuleConfiguration moduleCfg);

	/**
	 * Return a list of container definitions owned by the given <code>parent</code> module definition, having the
	 * provided <code>defName</code> short name. An empty list is returned if no container definition could be found.<br>
	 * <i>NOTE</i>: the return type is a list so to be able to identify cases where there is more than one container
	 * definition with same name in the same <code>parent</code>.
	 *
	 * @param parent
	 *        module definition where to search for the definition
	 * @param defName
	 *        the short name of the searched container definition
	 * @return the list containing found container definition(s) or an empty list if no container definition with
	 *         <code>defName</code> short name is defined in given module definition
	 */
	public List<GContainerDef> getChildContainerDefs(GModuleDef parent, String defName);

	/**
	 * Return a list of container definitions owned by the given <code>parent</code> that have the provided
	 * <code>defName</code> short name. An empty list is returned if no container definition could be found.<br>
	 * <i>NOTE</i>: The return type is a list so to be able to identify cases where there is more than one container
	 * definition with same name in the same <code>parent</code>.
	 *
	 * @param parent
	 *        container definition where to search for the definition
	 * @param defName
	 *        the short name of the searched container definition
	 * @return the list containing found container definition(s) or an empty list if no container definition with
	 *         <code>defName</code> short name is defined in given <code>parent</code>
	 */
	public List<GContainerDef> getChildContainerDefs(GContainerDef parent, String defName);

	/**
	 * Retrieves the status of ECUC model preparation.
	 *
	 * Used to signal errors like multiple definitions of an AUTOSAR module.
	 *
	 * @return the status of ECUC model preparation
	 */
	public IStatus getInitStatus();

	/**
	 * Recursively updates the definitions of subContainers, parameterValues, and referenceValues to point to the
	 * corresponding ones in the given {@code targetModuleDef}.If setContainerDef is true then the definition of the
	 * root container will also be set.
	 *
	 * @param container
	 *        the root container
	 * @param targetModuleDef
	 *        the target module definition (usually, the refined one)
	 * @param setContainerDef
	 *        flag that specifies if the container's definition should also be set
	 */
	public void recursiveDefUpdate(GContainer container, GModuleDef targetModuleDef, boolean setContainerDef);

	/**
	 * Recursively updates the definitions of subContainers, parameterValues, and referenceValues to point to the
	 * corresponding ones in the given {@code targetModuleDef}.By default it will also set the definition of the root
	 * container
	 *
	 * @param container
	 *        the root container
	 * @param ecucModel
	 *        the current {@linkplain IEcucModel}
	 * @param targetModuleDef
	 *        the target module definition (usually, the refined one)
	 */
	public void recursiveDefUpdate(GContainer container, GModuleDef targetModuleDef);

	/**
	 * Recursively removes all the references from the container and all of it's subContainers that have a different
	 * definition that the target one
	 *
	 * @param container
	 *        the root container
	 * @param targetModuleDef
	 *        the target module definition (usually, the refined one)
	 */
	public void recursivelyRemoveReferences(GContainer container, GModuleDef targetModuleDef);
}
