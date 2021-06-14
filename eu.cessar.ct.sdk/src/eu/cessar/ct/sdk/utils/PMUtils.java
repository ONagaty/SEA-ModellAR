/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidl6458 Feb 3, 2010 12:19:24 PM </copyright>
 */
package eu.cessar.ct.sdk.utils;

import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.Assert;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

import eu.cessar.ct.core.platform.EProjectVariant;
import eu.cessar.ct.core.platform.util.PlatformUtils;
import eu.cessar.ct.sdk.pm.IPMContainer;
import eu.cessar.ct.sdk.pm.IPMElement;
import eu.cessar.ct.sdk.pm.IPMInstanceRef;
import eu.cessar.ct.sdk.pm.IPMModuleConfiguration;
import eu.cessar.ct.sdk.pm.IPMPackage;
import eu.cessar.ct.sdk.pm.IPresentationModel;
import eu.cessar.ct.sdk.pm.PMInvalidSplitException;
import eu.cessar.req.Requirement;
import gautosar.gecucdescription.GContainer;
import gautosar.gecucdescription.GInstanceReferenceValue;
import gautosar.gecucdescription.GModuleConfiguration;
import gautosar.gecucparameterdef.GConfigParameter;
import gautosar.gecucparameterdef.GConfigReference;
import gautosar.gecucparameterdef.GContainerDef;
import gautosar.gecucparameterdef.GModuleDef;
import gautosar.gecucparameterdef.GParamConfContainerDef;
import gautosar.ggenericstructure.ginfrastructure.GARPackage;

/**
 * @author uidl6458
 *
 */
public final class PMUtils
{
	private static final Service SERVICE = PlatformUtils.getService(Service.class);

	/**
	 *
	 */
	private PMUtils()
	{
		// do not allow instantiation by the users
	}

	/**
	 *
	 * The internal service definition, should not be used directly by clients
	 *
	 */
	public static interface Service
	{
		@SuppressWarnings("javadoc")
		public IPresentationModel getPMRoot(IProject project);

		@SuppressWarnings("javadoc")
		public IPMElement getPMObject(EObject autosarObject);

		@SuppressWarnings("javadoc")
		public List<GContainer> getContainers(IPMContainer pmContainer);

		@SuppressWarnings("javadoc")
		public List<GModuleConfiguration> getModuleConfigurations(IPMModuleConfiguration pmModuleConfiguration);

		@SuppressWarnings("javadoc")
		public Map<IPMContainer, List<String>> getReferencedBy(IPMContainer pmContainer);

		@SuppressWarnings("javadoc")
		public List<GInstanceReferenceValue> getInstanceRef(IPMInstanceRef value);

		@SuppressWarnings("javadoc")
		public List<GARPackage> getPackages(IPMPackage pmPackage);

		@SuppressWarnings("javadoc")
		public void setUsingMergedReferences(boolean mergedReferences);

		@SuppressWarnings("javadoc")
		public boolean isUsingMergedReferences();

		@SuppressWarnings("javadoc")
		public void setSplitChecking(boolean splitChecking);

		@SuppressWarnings("javadoc")
		public boolean isSplitChecking();

		@SuppressWarnings("javadoc")
		public List<GContainer> getContainers(IPMContainer pmContainer, boolean merged);

		@SuppressWarnings("javadoc")
		public GInstanceReferenceValue getInstanceRefObject(IPMInstanceRef pmInstanceRef);

		@SuppressWarnings("javadoc")
		public List<GModuleConfiguration> getModuleConfigurations(IPMModuleConfiguration pmModuleConfiguration,
			boolean merged);

		@SuppressWarnings("javadoc")
		public boolean isCreatedAtPostBuild(IPMContainer pmContainer);

		@SuppressWarnings("javadoc")
		public void setContainerCreationPhase(IPMContainer pmContainer, EProjectVariant phase);

		@SuppressWarnings("javadoc")
		public void setContainerCreationPhase(IPMContainer pmContainer, String phase);
	}

	/**
	 * Returns the presentation model root within the given project.
	 *
	 * @param project
	 *        the given project
	 * @return the presentation model root
	 */
	public static IPresentationModel getPMRoot(IProject project)
	{
		return SERVICE.getPMRoot(project);
	}

	/**
	 * Change how the references from presentation model to the system side (foreign references and instance reference)
	 * are to be returned:<br/>
	 * <ul>
	 * <li>If <code>mergedReferences</code> is <strong>false</strong>, the returned object will be a physical object
	 * stored in a file. <strong>This is the default behavior</strong></li>
	 * <li>If <code>mergedReferences</code> is <strong>true</strong>, the returned object will be a merged object</li>
	 * </ul>
	 * <strong>Note:</strong> <i>Only</i> the calling thread will be affected by this change. Also note that the
	 * references that have been accessed prior to calling the method will <code>not</code> be impacted. <br>
	 *
	 * @param mergedReferences
	 *        <code>true</code> if references shall be returned from the merged model, <code>false</code> otherwise
	 */
	public static void setUsingMergedReferences(boolean mergedReferences)
	{
		SERVICE.setUsingMergedReferences(mergedReferences);
	}

	/**
	 * Return how the references from presentation model to the system side (foreign references and instance reference)
	 * will be returned for the calling thread.
	 *
	 * @return <code>true</code> if the references will be returned from the merged model, <code>false</code> otherwise
	 */
	public static boolean isUsingMergedReferences()
	{
		return SERVICE.isUsingMergedReferences();
	}

	/**
	 * Controls whether split validity is checked for parameters and references with runtime exceptions throwing.
	 * Enabled by default.
	 *
	 * The checks occur in getters and setters, and auxiliary methods like isSet. The thrown runtime exceptions for
	 * invalid split elements are of type {@link PMInvalidSplitException}.
	 *
	 * <strong>Note:</strong> The intent is that <i>only</i> the calling thread will be affected by this change.
	 *
	 * @param splitChecking
	 *        <code>true</code> to enable split checking, <code>false</code> otherwise
	 */
	@Requirement(
		reqID = "REQ_RUNTIME#2")
	public static void setSplitChecking(boolean splitChecking)
	{
		SERVICE.setSplitChecking(splitChecking);
	}

	/**
	 * Returns whether split validity is checked for parameters and references with runtime exceptions throwing.
	 *
	 * The checks occur in getters and setters, and auxiliary methods like isSet. The thrown runtime exceptions for
	 * invalid split elements are of type {@link PMInvalidSplitException}.
	 *
	 * @return <code>true</code> <b>iff</b> split checking with runtime exception throwing is enabled,
	 *         <code>false</code> otherwise
	 */
	@Requirement(
		reqID = "REQ_RUNTIME#2")
	public static boolean isSplitChecking()
	{
		return SERVICE.isSplitChecking();
	}

	/**
	 * Returns the presentation model object associated with the given Module Configuration object.
	 *
	 * @param moduleConfiguration
	 *        the Module Configuration object
	 * @return the corresponding presentation model
	 */
	public static IPMModuleConfiguration getPMModuleConfiguration(GModuleConfiguration moduleConfiguration)
	{
		return (IPMModuleConfiguration) SERVICE.getPMObject(moduleConfiguration);
	}

	/**
	 * Returns the presentation model object associated with the given Container.
	 *
	 * @param container
	 *        the Container object, can be a merged object
	 * @return the corresponding presentation model
	 */
	public static IPMContainer getPMContainer(GContainer container)
	{
		return (IPMContainer) SERVICE.getPMObject(container);
	}

	/**
	 * Return the concrete PM Container that actually holds the values referenced by <code>container</code>. For example
	 * the argument could be an instance of /AUTOSAR/EcucDefs/Can/CanGeneral but it could hold informations from a
	 * container who's definition is /CESSAR/Can/CanGeneral where /CESSAR/Can is a refined version of
	 * /AUTOSAR/EcucDef/Can. In such a case, the method will return an instance of /CESSAR/Can/CanGeneral.
	 *
	 * @param container
	 * @return UnsupportedOperationException
	 * @deprecated UnsupportedOperationException
	 *
	 */
	@SuppressWarnings("unused")
	@Deprecated
	public static IPMContainer getConcretePMContainer(IPMContainer container)
	{
		throw new UnsupportedOperationException();
	}

	/**
	 * Return a refined version of <code>container</code>. Suppose that <code>container</code> it's an instance of
	 * /AUTOSAR/EcucDefs/Can/CanGeneral. If <code>refinedModule</code> point to /CESSAR/Can then the returned value will
	 * point to /CESSAR/Can/CanGeneral. Please note that any vendor specific api on the returned CanGeneral will fail
	 * with some unchecked exception
	 *
	 * @param container
	 * @param refinedModule
	 * @return UnsupportedOperationException
	 * @deprecated UnsupportedOperationException
	 */
	@SuppressWarnings("unused")
	@Deprecated
	public static IPMContainer getRefinedPMContainer(IPMContainer container, IPMModuleConfiguration refinedModule)
	{
		throw new UnsupportedOperationException();
	}

	/**
	 * Returns a copy of an existing presentation model object associated with a Module Configuration.
	 *
	 * @param pmModuleConfiguration
	 *        the given presentation model object
	 * @param inDepth
	 *        if <code>true</code>, the copy is made in depth; all the hierarchy below
	 *        <code>pmModuleConfiguration</code> is copied<br>
	 *        if <code>false</code>, only <code>pmModuleConfiguration</code> is copied
	 * @return the duplicate object
	 * @deprecated UnsupportedOperationException
	 */
	@SuppressWarnings("unused")
	@Deprecated
	public static IPMModuleConfiguration duplicatePMModuleConfiguration(IPMModuleConfiguration pmModuleConfiguration,
		boolean inDepth)
	{
		throw new UnsupportedOperationException();
	}

	/**
	 * Returns a copy of an existing presentation model object associated with a Container.
	 *
	 * @param pmContainer
	 *        the given presentation model object
	 * @param inDepth
	 *        if <code>true</code>, the copy is made in depth; all the hierarchy below <code>pmContainer</code> is
	 *        copied<br>
	 *        if <code>false</code>, only <code>pmContainer</code> is copied
	 * @return the duplicate object
	 * @deprecated UnsupportedOperationException
	 */
	@SuppressWarnings("unused")
	@Deprecated
	public static IPMContainer duplicatePMContainer(IPMContainer pmContainer, boolean inDepth)
	{
		throw new UnsupportedOperationException();
	}

	/**
	 * Returns the corresponding Module Configuration object from a given presentation model object.
	 *
	 * @param pmModuleConfiguration
	 *        the given presentation model object
	 * @return the Module Configuration object
	 */
	public static List<GModuleConfiguration> getModuleConfigurations(IPMModuleConfiguration pmModuleConfiguration)
	{
		return SERVICE.getModuleConfigurations(pmModuleConfiguration);
	}

	/**
	 * Returns the corresponding Module Configuration object from a given presentation model object.
	 *
	 * @param pmModuleConfiguration
	 *        the given presentation model object
	 * @param merged
	 *        if <code>true</code> indicates that the operation is performed in a merged context otherwise it is
	 *        performed in file-based context
	 * @return the Module Configuration object
	 */
	public static List<GModuleConfiguration> getModuleConfigurations(IPMModuleConfiguration pmModuleConfiguration,
		boolean merged)
		{
		return SERVICE.getModuleConfigurations(pmModuleConfiguration, merged);
		}

	/**
	 * Returns the corresponding Container object from a given presentation model object.
	 *
	 * @param pmContainer
	 *        the given presentation model object
	 * @return the Container object or <code>null</code> if no definition found
	 */
	public static List<GContainer> getContainers(IPMContainer pmContainer)
	{
		return SERVICE.getContainers(pmContainer);
	}

	/**
	 * Returns the corresponding Container object from a given presentation model object.
	 *
	 * @param pmContainer
	 *        the given presentation model object
	 * @param merged
	 *        if <code>true</code> indicates that the operation is performed in a merged context otherwise it is
	 *        performed in file-based context
	 * @return the Container object or <code>null</code> if no definition found
	 */
	public static List<GContainer> getContainers(IPMContainer pmContainer, boolean merged)
	{
		return SERVICE.getContainers(pmContainer, merged);
	}

	/**
	 * For the given presentation model object returns the corresponding Module Definition used to define that object.
	 *
	 * @param pmModuleConfiguration
	 *        the given presentation model object
	 * @return the Module Definition object
	 */
	public static GModuleDef getModuleDefinition(IPMModuleConfiguration pmModuleConfiguration)
	{
		List<GModuleConfiguration> configurations = getModuleConfigurations(pmModuleConfiguration);
		if (configurations.size() > 0)
		{
			return configurations.get(0).gGetDefinition();
		}
		else
		{
			return null;
		}
	}

	/**
	 * For the given presentation model object returns the corresponding Module Definition used to define that object.
	 *
	 * @param pmModuleConfiguration
	 *        the given presentation model object
	 * @param merged
	 *        if <code>true</code> indicates that the operation is performed in a merged context otherwise it is
	 *        performed in file-based context
	 * @return the Module Definition object
	 */
	public static GModuleDef getModuleDefinition(IPMModuleConfiguration pmModuleConfiguration, boolean merged)
	{
		if (merged)
		{
			return SplitUtils.getMergedInstance(getModuleDefinition(pmModuleConfiguration));
		}
		return getModuleDefinition(pmModuleConfiguration);
	}

	/**
	 * For the given presentation model object returns the corresponding Container Definition used to define that
	 * object.
	 *
	 * @param pmContainer
	 *        the given presentation model object
	 * @return the Container Definition object
	 */
	public static GContainerDef getContainerDefinition(IPMContainer pmContainer)
	{
		List<GContainer> containers = getContainers(pmContainer);
		if (containers.size() > 0)
		{
			return containers.get(0).gGetDefinition();
		}
		else
		{
			return null;
		}
	}

	/**
	 * For the given presentation model object returns the corresponding Container Definition used to define that
	 * object.
	 *
	 * @param pmContainer
	 *        the given presentation model object
	 * @param merged
	 *        if <code>true</code> indicates that the operation is performed in a merged context otherwise it is
	 *        performed in file-based context
	 * @return the Container Definition object
	 */
	public static GContainerDef getContainerDefinition(IPMContainer pmContainer, boolean merged)
	{
		if (merged)
		{
			return SplitUtils.getMergedInstance(getContainerDefinition(pmContainer));
		}
		return getContainerDefinition(pmContainer);
	}

	/**
	 *
	 * Return the {@link GConfigParameter} named <code>parameterName</code> from the {@link GContainer#gGetDefinition()
	 * definition} of the <code>pmContainer</code>. <code>Null</code> will be returned if the container definition is
	 * not a {@link GParamConfContainerDef} or there is no such parameter
	 *
	 * @param pmContainer
	 *        the container
	 * @param parameterName
	 *        the parameter name
	 * @return the {@link GConfigParameter} or null if one such parameter cannot be found
	 */
	public static GConfigParameter getParameterDefinition(IPMContainer pmContainer, String parameterName)
	{
		GContainerDef definition = getContainerDefinition(pmContainer);
		if (definition instanceof GParamConfContainerDef)
		{
			return getParameterDefinition((GParamConfContainerDef) definition, parameterName);
		}
		else
		{
			return null;
		}
	}

	/**
	 * Return the {@link GConfigParameter} named <code>parameterName</code> from the <code>definition</code> or null if
	 * no such parameter can be found
	 *
	 * @param definition
	 * @param parameterName
	 * @return {@link GConfigParameter}
	 */
	public static GConfigParameter getParameterDefinition(GParamConfContainerDef definition, String parameterName)
	{
		Assert.isNotNull(parameterName, "Parameter name cannot be null"); //$NON-NLS-1$
		EList<GConfigParameter> parameters = definition.gGetParameters();
		for (GConfigParameter config: parameters)
		{
			if (parameterName.equals(config.gGetShortName()))
			{
				return config;
			}
		}
		return null;
	}

	/**
	 *
	 * Return the {@link GConfigReference} named <code>referenceName</code> from the {@link GContainer#gGetDefinition()
	 * definition} of the <code>pmContainer</code>. <code>Null</code> will be returned if the container definition is
	 * not a {@link GParamConfContainerDef} or there is no such reference
	 *
	 * @param pmContainer
	 *        the container
	 * @param referenceName
	 *        the reference name
	 * @return the {@link GConfigReference} or null if one such reference cannot be found
	 */
	public static GConfigReference getReferenceDefinition(IPMContainer pmContainer, String referenceName)
	{
		GContainerDef definition = getContainerDefinition(pmContainer);
		if (definition instanceof GParamConfContainerDef)
		{
			return getReferenceDefinition((GParamConfContainerDef) definition, referenceName);
		}
		else
		{
			return null;
		}
	}

	/**
	 * Return the {@link GConfigReference} named <code>referenceName</code> from the <code>definition</code> or null if
	 * no such reference can be found.
	 *
	 * @param definition
	 * @param referenceName
	 * @return the {@link GConfigReference}
	 */
	public static GConfigReference getReferenceDefinition(GParamConfContainerDef definition, String referenceName)
	{
		Assert.isNotNull(referenceName, "Parameter name cannot be null"); //$NON-NLS-1$
		EList<GConfigReference> references = definition.gGetReferences();
		for (GConfigReference config: references)
		{
			if (referenceName.equals(config.gGetShortName()))
			{
				return config;
			}
		}
		return null;
	}

	/**
	 * Returns a mapping (Container,list of references' names) that refer the <code>pmContainer</code> argument.<br>
	 * For example, if X and Y are containers and REF is a reference from X that point to Y then getReferencedBy(Y) will
	 * return a map with X as key and as value a list with REF as the sole element.
	 *
	 * @param pmContainer
	 *        the referenced presentation model container
	 * @return the corresponding map, or an empty one if no reference is found.
	 */
	public static Map<IPMContainer, List<String>> getReferencedBy(IPMContainer pmContainer)
	{
		return SERVICE.getReferencedBy(pmContainer);
	}

	/**
	 * Returns the corresponding GInstanceReferenceValue object from a given presentation model object.
	 *
	 * @param pmInstanceRef
	 *        the given presentation model object
	 * @return the GInstanceReferenceValue object
	 *
	 * @deprecated Due to wrong return type, only one object will be returned. Use
	 *             {@link PMUtils#getInstanceRefObject(IPMInstanceRef)}
	 */
	@Deprecated
	public static List<GInstanceReferenceValue> getInstanceRef(IPMInstanceRef pmInstanceRef)
	{
		return SERVICE.getInstanceRef(pmInstanceRef);
	}

	/**
	 * Returns the corresponding GInstanceReferenceValue object from a given presentation model object.
	 *
	 * @param pmInstanceRef
	 *        the given presentation model object
	 * @return the GInstanceReferenceValue object
	 *
	 */
	public static GInstanceReferenceValue getInstanceRefObject(IPMInstanceRef pmInstanceRef)
	{
		return SERVICE.getInstanceRefObject(pmInstanceRef);
	}

	/**
	 * Returns the corresponding GInstanceReferenceValue object from a given presentation model object.
	 *
	 * @param pmInstanceRef
	 *        the given presentation model object
	 * @param merged
	 *        if <code>true</code> indicates that the operation is performed in a merged context otherwise it is
	 *        performed in file-based context
	 * @return the GInstanceReferenceValue object
	 */
	public static GInstanceReferenceValue getInstanceRef(IPMInstanceRef pmInstanceRef, boolean merged)
	{
		if (merged)
		{
			return SplitUtils.getMergedInstance(getInstanceRefObject(pmInstanceRef));
		}
		return getInstanceRefObject(pmInstanceRef);
	}

	/**
	 * Returns the corresponding GARPackage object from a given presentation model object.
	 *
	 * @param pmPackage
	 *        the given presentation model object
	 * @return the GARPackage object from model definition
	 */
	public static List<GARPackage> getPackages(IPMPackage pmPackage)
	{
		return SERVICE.getPackages(pmPackage);
	}

	/**
	 * Returns <code>true</code> if the presentation model container was created at post build time and
	 * <code>false</code> otherwise. A presentation model container stored in a single file is considered created at
	 * post build time if it was created when project phase was 'PRODUCTION'. In case the presentation model container
	 * is stored in multiple files, this method will return <code>true</code> only if all container files were created
	 * when project phase was 'PRODUCTION'. If the container is null or the given container is not a valid object from
	 * the presentation model, returns <code>false</code>.
	 *
	 * @param pmContainer
	 *        the given presentation model object which should be verified if it was created at post build
	 * @return <code> true </code> if the container was created at post build time, else returns <code> false </code>
	 */
	public static boolean isCreatedAtPostBuild(IPMContainer pmContainer)
	{
		return SERVICE.isCreatedAtPostBuild(pmContainer);
	}

	/**
	 * Set the creation phase context of the given pmContainer to a particular value. In case the presentation model
	 * container is stored in multiple files, this method will set the creation phase context to all container files. If
	 * the container is null or the given container is not a valid object from the presentation model, does nothing. If
	 * the <code> phase </code> is null, container phase is set to 'DEVELOPMENT'.
	 *
	 * @param pmContainer
	 *        the given presentation model object to set creation phase to
	 * @param phase
	 *        creation phase of the pmContainer. Can be one of {@link EProjectVariant#DEVELOPMENT} or
	 *        {@link EProjectVariant#PRODUCTION}
	 * @deprecated Use {@link #setContainerCreationPhase(IPMContainer, String)} instead. The current implementation
	 *             force the adopter to use an enum which is not published.
	 */
	@Deprecated
	public static void setContainerCreationPhase(IPMContainer pmContainer, EProjectVariant phase)
	{
		SERVICE.setContainerCreationPhase(pmContainer, phase);
	}

	/**
	 * Set the creation phase context of the given pmContainer to a particular value. In case the presentation model
	 * container is stored in multiple files, this method will set the creation phase context to all container files. If
	 * the container is null or the given container is not a valid object from the presentation model, does nothing. If
	 * the <code> phase </code> is null, container phase is set to 'DEVELOPMENT'.
	 *
	 * @param pmContainer
	 *        the given presentation model object to set creation phase to
	 * @param phase
	 *        creation phase of the pmContainer. Can be one of <b>DEVELOPMENT</b> or <b>PRODUCTION</b>
	 */
	public static void setContainerCreationPhase(IPMContainer pmContainer, String phase)
	{
		SERVICE.setContainerCreationPhase(pmContainer, phase);
	}
}
