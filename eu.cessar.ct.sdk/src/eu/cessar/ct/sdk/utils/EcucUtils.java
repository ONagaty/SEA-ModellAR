package eu.cessar.ct.sdk.utils;

import java.util.Collection;
import java.util.List;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IStatus;

import eu.cessar.ct.core.platform.EProjectVariant;
import eu.cessar.ct.core.platform.util.PlatformUtils;
import eu.cessar.req.Requirement;
import gautosar.gecucdescription.GContainer;
import gautosar.gecucdescription.GModuleConfiguration;
import gautosar.gecucparameterdef.GContainerDef;
import gautosar.gecucparameterdef.GModuleDef;
import gautosar.ggenericstructure.ginfrastructure.GARPackage;
import gautosar.ggenericstructure.ginfrastructure.GAUTOSAR;

/**
 *
 * ECUC utilities methods
 *
 */
public final class EcucUtils
{
	private static final Service SERVICE = PlatformUtils.getService(Service.class);

	private EcucUtils()
	{
		// avoid instance
	}

	/**
	 * NOT PUBLIC API, DO NOT USE
	 */
	public static interface Service
	{
		@SuppressWarnings("javadoc")
		public GModuleConfiguration createModuleConfiguration(GAUTOSAR root, GModuleDef moduleDef,
			String moduleConfigQualifiedName, boolean replace) throws ExecutionException;

		@SuppressWarnings("javadoc")
		public GModuleConfiguration createModuleConfiguration(GARPackage pack, GModuleDef moduleDef,
			String moduleConfigShortName, boolean replace) throws ExecutionException;

		@SuppressWarnings("javadoc")
		public Collection<GModuleDef> getAvailableModules(IProject project);

		@SuppressWarnings("javadoc")
		public Collection<GModuleConfiguration> getAvailableInstances(IProject project, GModuleDef moduleDef);

		@SuppressWarnings("javadoc")
		public Collection<GModuleConfiguration> getAvailableInstances(IProject project, GModuleDef moduleDef,
			boolean merged);

		@SuppressWarnings("javadoc")
		public Collection<GModuleDef> getAvailableModules(IProject project, boolean merged);

		@SuppressWarnings("javadoc")
		public boolean isCreatedAtPostBuild(GContainer container);

		@SuppressWarnings("javadoc")
		public void setContainerCreationPhase(GContainer container, EProjectVariant phase);

		@SuppressWarnings("javadoc")
		public void setContainerCreationPhase(GContainer container, String phase);

		@SuppressWarnings("javadoc")
		public List<GContainer> getContainerInstances(GModuleConfiguration parent, GContainerDef containerDef);

		@SuppressWarnings("javadoc")
		public List<GContainer> getContainerInstances(GContainer parent, GContainerDef containerDef);

		@SuppressWarnings("javadoc")
		public List<GContainer> getContainerInstances(GContainer parent, String containerDef);

		@SuppressWarnings("javadoc")
		public List<GContainer> getContainerInstances(GModuleConfiguration parent, String containerDef);

		@SuppressWarnings("javadoc")
		public IStatus convertEcucFromStandardToRefined(GModuleConfiguration config, GModuleDef moduleDef);

		@SuppressWarnings("javadoc")
		public IStatus convertEcucFromRefinedToStandard(GModuleConfiguration config);
	}

	/**
	 * Returns the Module Definition object with the given fully qualified name, within the given project.<br>
	 * Note: This is equivalent with
	 * <code>ModelUtils.getEObjectFromQualifiedName(IProject project,String qualifiedName)</code><br>
	 *
	 * <strong>Note:</strong> If the found object with <code>moduleDefQualifiedName</code> is not a module definition a
	 * {@link ClassCastException} will be thrown. </ul>
	 *
	 * @param project
	 *        the project where to search
	 * @param moduleDefQualifiedName
	 *        the fully qualified name of the searched Module Definition
	 * @return the found Module Definition, or <br>
	 *         <code>null</code>, if no such object is found or more than one object is found
	 */
	public static GModuleDef getModuleDefinition(IProject project, String moduleDefQualifiedName)
	{
		return (GModuleDef) ModelUtils.getEObjectWithQualifiedName(project, moduleDefQualifiedName);
	}

	/**
	 * Returns the Module Definition object with the given fully qualified name, within the given project.<br>
	 * Note: This is equivalent with
	 * <code>{@link ModelUtils#getEObjectWithQualifiedName(IProject, String, boolean)}</code><br>
	 * <strong>Note:</strong> If the found object with <code>moduleDefQualifiedName</code> is not a module definition a
	 * {@link ClassCastException} will be thrown. </ul> <strong>Having the same module definition stored in several
	 * resources is not a valid AUTOSAR configuration</strong>
	 *
	 * @param project
	 *        the project where to search
	 * @param moduleDefQualifiedName
	 *        the fully qualified name of the searched Module Definition
	 * @param merged
	 *        if <code>true</code> indicates that the returned object shall be merged.<br>
	 *        Calling {@link SplitUtils#isMergedInstace(gautosar.ggenericstructure.ginfrastructure.GARObject)} for it
	 *        returns <code>true</code>.
	 * @return the found Module Definition, or <br>
	 *         <code>null</code>, if no such object is found or more than one object is found.
	 */
	public static GModuleDef getModuleDefinition(IProject project, String moduleDefQualifiedName, boolean merged)
	{
		return (GModuleDef) ModelUtils.getEObjectWithQualifiedName(project, moduleDefQualifiedName, merged);
	}

	/**
	 * Returns the available Module Definition objects,within the given project.
	 *
	 * @param project
	 *        The project where to search
	 * @return the list of found Module Definition objects, or an empty one if no object is found
	 */
	public static Collection<GModuleDef> getAvailableModules(IProject project)
	{
		return SERVICE.getAvailableModules(project);
	}

	/**
	 * Returns the available Module Definition objects,within the given project.<br>
	 * <strong>Having the same module definition stored in several resources is not a valid AUTOSAR
	 * configuration</strong>
	 *
	 * @param project
	 *        The project where to search
	 * @param merged
	 *        if <code>true</code> indicates that the returned objects shall be merged.<br>
	 *        Calling {@link SplitUtils#isMergedInstace(gautosar.ggenericstructure.ginfrastructure.GARObject)} for them
	 *        returns <code>true</code>.
	 * @return the list of found Module Definition objects, or an empty one if no object is found
	 */
	public static Collection<GModuleDef> getAvailableModules(IProject project, boolean merged)
	{
		return SERVICE.getAvailableModules(project, merged);
	}

	/**
	 * Returns the available Module Configuration objects with a given definition, within a given project.
	 *
	 * @param project
	 *        the project where to search
	 * @param moduleDef
	 *        the Module Definition object to search the instances for
	 * @return the list of found Module Configuration objects, or an empty list if no instance is found
	 */
	public static Collection<GModuleConfiguration> getAvailableInstances(IProject project, GModuleDef moduleDef)
	{
		return SERVICE.getAvailableInstances(project, moduleDef);
	}

	/**
	 * Returns the available Module Configuration objects with a given definition, within a given project.
	 *
	 * @param project
	 *        the project where to search
	 * @param moduleDef
	 *        the Module Definition object to search the instances for
	 * @param merged
	 *        if <code>true</code> indicates that the operation is performed in a merged context otherwise it is
	 *        performed in file-based context
	 * @return the list of found Module Configuration objects, or an empty list if no instance is found
	 */
	public static Collection<GModuleConfiguration> getAvailableInstances(IProject project, GModuleDef moduleDef,
		boolean merged)
	{
		return SERVICE.getAvailableInstances(project, moduleDef, merged);
	}

	/**
	 * Returns the Module Configuration with the given fully qualified name, within the given project.<br>
	 * Note: This is equivalent with <code>{@link ModelUtils#getEObjectWithQualifiedName(IProject, String)}</code><br>
	 *
	 * <strong>Note:</strong> If the found object with <code>moduleConfigQualifiedName</code> is not a module
	 * configuration a {@link ClassCastException} will be thrown. </ul>
	 *
	 * @param project
	 *        the project where to search
	 * @param moduleConfigQualifiedName
	 *        the fully qualified name of the Module Configuration
	 * @return the found Module Configuration, or <br>
	 *         <code>null</code>, if no such object is found or more than one object is found
	 */
	public static GModuleConfiguration getModuleConfiguration(IProject project, String moduleConfigQualifiedName)
	{
		return (GModuleConfiguration) ModelUtils.getEObjectWithQualifiedName(project, moduleConfigQualifiedName);
	}

	/**
	 * Returns the Module Configuration with the given fully qualified name, within the given project.<br>
	 * Note: This is equivalent with
	 * <code>{@link ModelUtils#getEObjectWithQualifiedName(IProject, String, boolean)}</code><br>
	 *
	 * <strong>Note:</strong> If the found object with <code>moduleConfigQualifiedName</code> is not a module
	 * configuration a {@link ClassCastException} will be thrown. </ul>
	 *
	 * @param project
	 *        the project where to search
	 * @param moduleConfigQualifiedName
	 *        the fully qualified name of the Module Configuration
	 * @param merged
	 *        if <code>true</code> indicates that the operation is performed in a merged context otherwise it is
	 *        performed in file-based context
	 * @return the found Module Configuration, or <br>
	 *         <code>null</code>, if no such object is found or more than one object is found.
	 */
	public static GModuleConfiguration getModuleConfiguration(IProject project, String moduleConfigQualifiedName,
		boolean merged)
	{
		return (GModuleConfiguration) ModelUtils.getEObjectWithQualifiedName(project, moduleConfigQualifiedName, merged);
	}

	/**
	 * Creates a new Module Configuration.
	 *
	 * @param root
	 *        the root of the new Module Configuration
	 * @param moduleDef
	 *        the definition of the new Module Configuration
	 * @param moduleConfigQualifiedName
	 *        the fully qualified name of the new Module Configuration. There should be at least one package and the
	 *        short name of the module configuration specified
	 * @param replace
	 *        if <code>true</code> and the Module Configuration already exists, it will be replaced with the new created
	 *        one.<br>
	 *        if <code>false</code> and the Module Configuration already exists, the existing one will be returned.
	 * @return the existing or created Module Configuration
	 * @throws ExecutionException
	 *         if called with a merged object argument
	 */
	public static GModuleConfiguration createModuleConfiguration(GAUTOSAR root, GModuleDef moduleDef,
		String moduleConfigQualifiedName, boolean replace) throws ExecutionException
	{
		return SERVICE.createModuleConfiguration(root, moduleDef, moduleConfigQualifiedName, replace);
	}

	/**
	 * Creates a new Module Configuration.
	 *
	 * @param pack
	 *        An ARPackage where the module configuration should be created
	 * @param moduleDef
	 *        the definition of the new Module Configuration
	 * @param moduleConfigShortName
	 *        the short name of the new module configuration
	 * @param replace
	 *        if <code>true</code> and the Module Configuration already exists, it will be replaced with the new created
	 *        one.<br>
	 *        if <code>false</code> and the Module Configuration already exists, the existing one will be returned.
	 * @return the existing or created Module Configuration
	 * @throws ExecutionException
	 *         if called with a merged object argument
	 */
	public static GModuleConfiguration createModuleConfiguration(GARPackage pack, GModuleDef moduleDef,
		String moduleConfigShortName, boolean replace) throws ExecutionException
	{
		return SERVICE.createModuleConfiguration(pack, moduleDef, moduleConfigShortName, replace);
	}

	/**
	 * Returns true if the container was created at post build time and false otherwise. A container is considered
	 * created at post build time if it was created when project phase was 'PRODUCTION'. If the container is null
	 * returns false.
	 *
	 * @param container
	 *        the container which should be verified if it was created at post build
	 * @return <code> true </code> if the container was created at post build time, else returns <code> false </code>
	 */
	public static boolean isCreatedAtPostBuild(GContainer container)
	{
		return SERVICE.isCreatedAtPostBuild(container);
	}

	/**
	 * Set the creation phase context of the given container to a particular value.If the container is a merged
	 * instance, set the creation phase on each fragment. If the container is null, nothing happens. If the
	 * <code>phase</code> is null, the container phase is set to 'DEVELOPMENT'.
	 *
	 * @deprecated this method should not be used because it requires to import the <b>eu.cessar.ct.core.platform.ui</b>
	 *             library which is an internal library from plugets perspective.
	 * @param container
	 *        the container to set creation phase to
	 * @param phase
	 *        creation phase of the container. Can be one of {@link EProjectVariant#DEVELOPMENT} or
	 *        {@link EProjectVariant#PRODUCTION}
	 */
	@Deprecated
	public static void setContainerCreationPhase(GContainer container, EProjectVariant phase)
	{
		SERVICE.setContainerCreationPhase(container, phase);
	}

	/**
	 * Set the creation phase context of the given container to a particular value.If the container is a merged
	 * instance, set the creation phase on each fragment. If the container is null, nothing happens. If the
	 * <code>phase</code> is null, the container phase is set to 'DEVELOPMENT'.
	 *
	 * @param container
	 *        the container to set creation phase to
	 * @param phase
	 *        creation phase of the container. Can be one of {@link EProjectVariant#DEVELOPMENT} or
	 *        {@link EProjectVariant#PRODUCTION}
	 */
	public static void setContainerCreationPhase(GContainer container, String phase)
	{
		SERVICE.setContainerCreationPhase(container, phase);
	}

	/**
	 * Return a list with all direct container instances under specified parent, that have the given container
	 * definition. The parent must be a parent container.If the parent is merged then the list will contain the
	 * container instances from all the files where it is split.
	 *
	 * @param parent
	 *        another container instance
	 * @param containerDef
	 *        the container definition whose instances must be returned
	 * @return A list with all container instances, or empty list if the parent is not another container instance.
	 */
	@Requirement(
		reqID = "23603")
	public static List<GContainer> getContainerInstances(GContainer parent, GContainerDef containerDef)
	{
		return EcucUtils.SERVICE.getContainerInstances(parent, containerDef);
	}

	/**
	 * Return a list with all direct container instances under specified parent, that have the given container
	 * definition.The container definition is given as Qualified name ,the parent must be a parent container.If the
	 * parent is merged then the list will contain the container instances from all the files where it is split.
	 *
	 * @param parent
	 *        another container instance
	 * @param containerDef
	 *        the container definition whose instances must be returned
	 * @return A list with all container instances, or empty list if the parent is not another container instance.
	 */
	@Requirement(
		reqID = "23599")
	public static List<GContainer> getContainerInstances(GContainer parent, String containerDef)
	{
		return EcucUtils.SERVICE.getContainerInstances(parent, containerDef);
	}

	/**
	 * Return a list with all direct container instances under specified parent, that have the given container
	 * definition. The parent must be a Module configuration.If the parent is merged then the list will contain the
	 * container instances from all the files where it is split.
	 *
	 * @param parent
	 *        a module configuration
	 * @param containerDef
	 *        the container definition whose instances must be returned
	 * @return A list with all container instances, or empty list if the parent is not a module configuration.
	 */
	@Requirement(
		reqID = "23601")
	public static List<GContainer> getContainerInstances(GModuleConfiguration parent, GContainerDef containerDef)
	{
		return EcucUtils.SERVICE.getContainerInstances(parent, containerDef);
	}

	/**
	 * Return a list with all direct container instances under specified parent, that have the given container
	 * definition. The container definition is given as Qualified name ,the parent must be a Module configuration.If the
	 * parent is merged then the list will contain the container instances from all the files where it is split.
	 *
	 * @param parent
	 *        a module configuration
	 * @param containerDef
	 *        the container definition whose instances must be returned
	 * @return A list with all container instances, or empty list if the parent is not another container instance.
	 */
	@Requirement(
		reqID = "23593")
	public static List<GContainer> getContainerInstances(GModuleConfiguration parent, String containerDef)
	{
		return EcucUtils.SERVICE.getContainerInstances(parent, containerDef);
	}

	/**
	 * Converts an ECUC config based on a standard definition into an ECUC config based on a refined definition.
	 *
	 * @param config
	 *        - The moduleConfiguration from the ECUC config based on the standard definition
	 * @param moduleDef
	 *        - The moduleDefinition of the Refined Autosar, based on which the conversion will be made
	 * @return {@link IStatus} <br>
	 *         <code>IStatus.ERROR</code>, in the case that the conversion cannot be completed or
	 *         <code>IStatus.OK</code> otherwise.
	 */
	public static IStatus convertEcucFromStandardToRefined(GModuleConfiguration config, GModuleDef moduleDef)
	{
		return EcucUtils.SERVICE.convertEcucFromStandardToRefined(config, moduleDef);
	}

	/**
	 * Converts an ECUC config based on a refined definition into an ECUC config based on a standard definition.
	 *
	 * @param config
	 *        The moduleConfiguration from the ECUC config based on the Refined Autosar
	 * @return {@link IStatus} <br>
	 *         <code>IStatus.ERROR</code>, in the case that the conversion cannot be completed or
	 *         <code>IStatus.OK</code> otherwise.
	 */
	public static IStatus convertEcucFromRefinedToStandard(GModuleConfiguration config)
	{
		return EcucUtils.SERVICE.convertEcucFromRefinedToStandard(config);
	}
}
