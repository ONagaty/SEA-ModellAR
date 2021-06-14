/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 *
 * </copyright>
 */
package eu.cessar.ct.core.mms;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.artop.aal.common.Activator;
import org.artop.aal.common.library.AutosarLibraryDescriptor;
import org.artop.aal.common.library.AutosarLibraryIDEnumerator;
import org.artop.aal.common.metamodel.AutosarReleaseDescriptor;
import org.artop.aal.extender.autosar.Sd;
import org.artop.aal.extender.autosar.SpecialDataFactory;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.osgi.util.NLS;
import org.eclipse.sphinx.emf.metamodel.IMetaModelDescriptor;
import org.eclipse.sphinx.emf.metamodel.MetaModelDescriptorRegistry;
import org.eclipse.sphinx.emf.util.WorkspaceTransactionUtil;
import org.eclipse.sphinx.platform.util.PlatformLogUtil;

import eu.cessar.ct.core.mms.ecuc.PostBuildContext;
import eu.cessar.ct.core.mms.internal.CessarPluginActivator;
import eu.cessar.ct.core.mms.internal.Messages;
import eu.cessar.ct.core.platform.CESSARPreferencesAccessor;
import eu.cessar.ct.core.platform.EProjectVariant;
import eu.cessar.ct.core.platform.util.ERadix;
import eu.cessar.ct.core.platform.util.PlatformUtils;
import eu.cessar.ct.sdk.IPostBuildContext;
import eu.cessar.ct.sdk.utils.SplitUtils;
import gautosar.gecucdescription.GConfigReferenceValue;
import gautosar.gecucdescription.GContainer;
import gautosar.gecucdescription.GModuleConfiguration;
import gautosar.gecucdescription.GParameterValue;
import gautosar.gecucparameterdef.GChoiceContainerDef;
import gautosar.gecucparameterdef.GConfigParameter;
import gautosar.gecucparameterdef.GConfigReference;
import gautosar.gecucparameterdef.GContainerDef;
import gautosar.gecucparameterdef.GIntegerParamDef;
import gautosar.gecucparameterdef.GModuleDef;
import gautosar.gecucparameterdef.GParamConfContainerDef;
import gautosar.gecucparameterdef.GParamConfMultiplicity;
import gautosar.ggenericstructure.ggeneraltemplateclasses.gadmindata.GAdminData;
import gautosar.ggenericstructure.ggeneraltemplateclasses.gspecialdata.GSd;
import gautosar.ggenericstructure.ggeneraltemplateclasses.gspecialdata.GSdg;
import gautosar.ggenericstructure.ggeneraltemplateclasses.gspecialdata.GSdgContents;
import gautosar.ggenericstructure.ginfrastructure.GARObject;
import gautosar.ggenericstructure.ginfrastructure.GARPackage;
import gautosar.ggenericstructure.ginfrastructure.GAUTOSAR;
import gautosar.ggenericstructure.ginfrastructure.GIdentifiable;

// TODO: Auto-generated Javadoc
/**
 * ECUC metamodel utilities methods that are independant of the metamodel.
 *
 * @Review uidl6458 - 29.03.2012
 */
public final class EcucMetaModelUtils
{

	/** The Constant INTEGER_RADIX_PREF. */
	public static final String INTEGER_RADIX_PREF = "cessar.prefs.integer.radix"; //$NON-NLS-1$

	/** The Constant PB_SUPPORT_PREF. */
	public static final String PB_SUPPORT_PREF = "cessar.prefs.postbuild.supported"; //$NON-NLS-1$

	/** The Constant PB_CONTEXT_PREF. */
	public static final String PB_CONTEXT_PREF = "cessar.prefs.postbuild.context"; //$NON-NLS-1$

	/** The Constant CONTAINER_CREATION_PHASE. */
	public static final String CONTAINER_CREATION_PHASE = "cessar.creation.phase.container"; //$NON-NLS-1$

	/** The Constant EXTENSION_KEY. */
	public static final String EXTENSION_KEY = "_ExtensionKey"; //$NON-NLS-1$

	/** The Constant TYPE. */
	public static final String TYPE = "_Type"; //$NON-NLS-1$

	/** The Constant VALUE. */
	public static final String VALUE = "_Value"; //$NON-NLS-1$

	/**
	 * Instantiates a new ecuc meta model utils.
	 */
	private EcucMetaModelUtils()
	{
		// do not instantiate
	}

	/**
	 * Returns the service for ecuc utilities, for the given object.
	 *
	 * @param eObject
	 *        the given object
	 * @return the ecuc meta model service
	 */
	public static IEcucMMService getEcucMMService(EObject eObject)
	{
		IProject project = MetaModelUtils.getProject(eObject);
		if (project != null)
		{
			IMetaModelService mmService = MMSRegistry.INSTANCE.getMMService(project);
			if (mmService != null)
			{
				return mmService.getEcucMMService();
			}
		}
		return null;
	}

	/**
	 * Return a list with all direct container instances under specified parent, that have the given container
	 * definition. The parent must be a Module configuration or a parent container.
	 *
	 * @param parent
	 *        a module configuration or another container instance
	 * @param containerDef
	 *        the container definition whose instances must be returned
	 * @return A list with all container instances, or empty list if the parent is not a module configuration or another
	 *         container instance.
	 */
	public static List<GContainer> getContainerInstances(Object parent, GContainerDef containerDef)
	{
		EList<? extends GContainer> subContainers = null;
		if (parent instanceof GModuleConfiguration)
		{
			subContainers = ((GModuleConfiguration) parent).gGetContainers();
		}
		else if (parent instanceof GContainer)
		{
			subContainers = ((GContainer) parent).gGetSubContainers();
		}
		if ((subContainers == null) || (containerDef == null))
		{
			return Collections.emptyList();
		}

		// retrieve only container instances having specified definition
		List<GContainer> result = new ArrayList<GContainer>();
		for (GContainer containerInst: subContainers)
		{
			if (containerDef.equals(containerInst.gGetDefinition()))
			{
				result.add(containerInst);
			}
		}

		((ArrayList<GContainer>) result).trimToSize();
		return result;
	}

	/**
	 * Return a list with all direct parameter instances under specified parent, that have the given parameter
	 * definition. The parent must be a container.
	 *
	 * @param parent
	 *        a container instance
	 * @param parameterDef
	 *        the parameter definition whose instances must be returned
	 * @return A list with all parameter instances, or empty list
	 */
	public static List<GParameterValue> getParameterInstances(GContainer parent, GConfigParameter parameterDef)
	{
		EList<GParameterValue> parameterValues = null;

		parameterValues = parent.gGetParameterValues();

		if ((parameterValues == null) || (parameterDef == null))
		{
			return Collections.emptyList();
		}

		// retrieve only parameter instances having specified definition
		List<GParameterValue> result = new ArrayList<GParameterValue>();
		for (GParameterValue parameter: parameterValues)
		{
			if (parameterDef.equals(parameter.gGetDefinition()))
			{
				result.add(parameter);
			}
		}

		((ArrayList<GParameterValue>) result).trimToSize();
		return result;
	}

	/**
	 * Return a list with all direct reference instances under specified parent, that have the given reference
	 * definition. The parent must be a container.
	 *
	 * @param parent
	 *        a container instance
	 * @param referenceDef
	 *        the reference definition whose instances must be returned
	 * @return A list with all reference instances, or empty list
	 */
	public static List<GConfigReferenceValue> getReferenceInstances(GContainer parent, GConfigReference referenceDef)
	{
		EList<GConfigReferenceValue> referenceValues = null;

		referenceValues = parent.gGetReferenceValues();

		if ((referenceValues == null) || (referenceDef == null))
		{
			return Collections.emptyList();
		}

		// retrieve only reference instances having specified definition
		List<GConfigReferenceValue> result = new ArrayList<GConfigReferenceValue>();
		for (GConfigReferenceValue reference: referenceValues)
		{
			if (referenceDef.equals(reference.gGetDefinition()))
			{
				result.add(reference);
			}
		}

		((ArrayList<GConfigReferenceValue>) result).trimToSize();
		return result;
	}

	/**
	 * Verify if a new container instance for the provided definition can be created in the provided owner.
	 *
	 * @param owner
	 *        the owner object where the container should be created, it should be either a GModuleConfiguration or a
	 *        GContainer
	 * @param definition
	 *        the definition
	 * @return <code>true</code> if the container instance can be created, or <code>false</code> otherwise.
	 */
	public static boolean canCreateContainer(GIdentifiable owner, GContainerDef definition)
	{
		IEcucMMService ecucMMService = MMSRegistry.INSTANCE.getMMService(owner.eClass()).getEcucMMService();
		List<GContainer> instances = EcucMetaModelUtils.getContainerInstances(owner, definition);
		boolean canCreatedIfInProduction = canContainerBeCreatedIfInProduction(definition, owner);
		// if the project is in production phase, creation of new container is allowed only if their definition has
		// postBuildChangeable attribute set to true or if its parent is created in production phase
		if (!canCreatedIfInProduction)
		{
			return false;
		}
		int noInstances = 0;
		if (instances != null)
		{
			noInstances = instances.size();
		}

		BigInteger upper = ecucMMService.getUpperMultiplicity(definition, BigInteger.ONE, true);
		if (upper.equals(IEcucMMService.MULTIPLICITY_STAR))
		{
			return true;
		}
		else
		{
			long longUpper = upper.longValue();
			return longUpper > noInstances;
		}
	}

	/**
	 * Verifies if a new container having this definition can be created, regarding the project phase and restriction.
	 *
	 * @param containerDef
	 *        -container definition
	 * @param owner
	 *        -the owner object where the container should be created
	 * @return true if:</br>
	 *         1. the project is in development phase or </br>
	 *         2. the {@code owner} is a GContainer and is created in production phase or </br>
	 *         3. the container definition has the postBuildChangeable attribute or the multipleConfigurationContainer
	 *         set to true.
	 */
	public static boolean canContainerBeCreatedIfInProduction(GContainerDef containerDef, EObject owner)
	{
		IProject project = MetaModelUtils.getProject(containerDef);
		if (project == null || (!project.isAccessible()))
		{
			return false;
		}
		EProjectVariant projectVariant = CESSARPreferencesAccessor.getProjectVariant(project);
		if (EProjectVariant.PRODUCTION == projectVariant)
		{
			// check the owner object where the container should be created. If the owner is a GContainer and is created
			// in Production Phase, the container can be created
			// regardless the postBuildChangeable attribute of the container definition
			if (owner != null && owner instanceof GContainer)
			{
				EProjectVariant containerCreationPhase = getContainerCreationPhase((GContainer) owner);
				if (containerCreationPhase == EProjectVariant.PRODUCTION)
				{
					return true;
				}
			}
			Boolean postBuildChangeable = containerDef.gGetPostBuildChangeable();
			// check if the containerDef has postBuildChangeable set to true
			if (!postBuildChangeable)
			{ // check if the containerDef has multipleConfigurationContainer set
				IMetaModelService mmService = MMSRegistry.INSTANCE.getMMService(project);
				if (containerDef instanceof GParamConfContainerDef)
				{
					boolean multipleConfigurationContainer = mmService.getEcucMMService().isMultipleConfigurationContainer(
						(GParamConfContainerDef) containerDef);

					if (multipleConfigurationContainer)
					{
						return true;
					}
				}
				return false;

			}
		}
		return true;
	}

	/**
	 * Get the subcontainers eList from the parent. The parent can be only a module configuration or another container.
	 * If it's not the case an error will be throw
	 *
	 * @param parent
	 *        the parent
	 * @return the subcontainers attached to the parent
	 */
	public static EList<GContainer> getSubContainers(Object parent)
	{
		if (parent instanceof GModuleConfiguration)
		{
			return ((GModuleConfiguration) parent).gGetContainers();
		}
		if (parent instanceof GContainer)
		{
			return ((GContainer) parent).gGetSubContainers();
		}
		throw new IllegalArgumentException("Only GModuleConfiguration or GContainer accepted"); //$NON-NLS-1$
	}

	/**
	 * Get the preferred ERadix that shall be used to display integer values with definition <code>param</code>. If
	 * there is no preferred ERadix set for the <code>param</code> the project preference will be used.
	 *
	 * @param param
	 *        the param
	 * @return the preferred radix
	 */
	public static ERadix getPreferredRadix(GIntegerParamDef param)
	{
		ERadix result = null;

		if (param == null)
		{
			result = ERadix.DECIMAL;
		}
		else
		{
			Object value = param.gGetExtensions().get(INTEGER_RADIX_PREF);
			if (value == null)
			{
				IProject project = MetaModelUtils.getProject(param);
				if (project == null)
				{
					result = ERadix.DECIMAL;
				}
				else
				{
					result = PlatformUtils.getProjectRadixSettings(project);
				}
			}
			else
			{
				try
				{
					result = ERadix.valueOf(value.toString());
				}
				catch (IllegalArgumentException e)
				{
					result = ERadix.DECIMAL;
				}
			}
		}

		return result;
	}

	/**
	 * Returns whether the preferred ERadix to display integer values is set with definition <code>param</code>.
	 *
	 * @param param
	 *        the param
	 * @return true, if is sets the preferred radix
	 */
	public static boolean isSetPreferredRadix(GIntegerParamDef param)
	{
		if (param == null)
		{
			return false;
		}

		Object value = param.gGetExtensions().get(INTEGER_RADIX_PREF);
		if (value == null)
		{
			return false;
		}
		return true;

	}

	/**
	 * Get the creation phase for the <code>container</code>. If a container is a merged instance, returns 'PRODUCTION'
	 * only if each fragment was created when project phase was 'PRODUCTION'. If container is null or it doesn't have a
	 * creation phase, 'DEVELOPMENT' is returned.
	 *
	 * @param container
	 *        -container to get creation phase for
	 * @return the creation phase for the <code>container</code>.
	 */
	public static EProjectVariant getContainerCreationPhase(GContainer container)
	{
		if (container == null)
		{
			return EProjectVariant.DEVELOPMENT;
		}

		// if the container is a merged instance return 'PRODUCTION' only if each fragment was created when project
		// phase was 'PRODUCTION'
		boolean isMergedInstace = SplitUtils.isMergedInstace(container);
		if (isMergedInstace)
		{

			Collection<GContainer> fragments = SplitUtils.getConcreteInstances(container);
			for (GContainer fragment: fragments)
			{
				EProjectVariant fragmentCreationPhase = getNonMergedContainerCreationPhase(fragment);
				if (EProjectVariant.DEVELOPMENT == fragmentCreationPhase)
				{
					// return DEVELOPMENT as soon as we find a DEVELOPMENT fragment container
					return EProjectVariant.DEVELOPMENT;
				}
			}
			// all fragment containers were created in 'PRODUCTION', return 'PRODUCTION' phase
			return EProjectVariant.PRODUCTION;
		}

		// if this container is not a merged instance, just get container creation phase
		EProjectVariant containerCreationPhase = getNonMergedContainerCreationPhase(container);
		return containerCreationPhase;

	}

	/**
	 * Set the creation phase context of the provided container to a particular value. If the <code>phase</code> is
	 * null, the behavior is equivalent to setting creation phase to 'DEVELOPMENT' and any previously existing context
	 * will be removed.If the container is a merged instance, set the creation phase on each fragment.
	 *
	 * @param container
	 *        the container
	 * @param phase
	 *        the phase value
	 */
	public static void setContainerCreationPhase(GContainer container, EProjectVariant phase)
	{
		if (container == null)
		{
			return;
		}
		// if the container is a merged instance set phase to each fragment
		boolean isMergedInstace = SplitUtils.isMergedInstace(container);
		if (isMergedInstace)
		{

			Collection<GContainer> fragments = SplitUtils.getConcreteInstances(container);
			for (GContainer fragment: fragments)
			{
				doSetContainerCreationPhase(fragment, phase);
				// if parent is a choice container, also set creation phase on it
				setContainerPhaseOnChoiceContainerParent(fragment, phase);
			}

		}
		else
		{
			// if this container is not a merged instance, just set container creation phase
			doSetContainerCreationPhase(container, phase);
			// if parent is a choice container, also set creation phase on it
			setContainerPhaseOnChoiceContainerParent(container, phase);

		}
	}

	/**
	 * @param container
	 * @param phase
	 */
	private static void setContainerPhaseOnChoiceContainerParent(GContainer container, EProjectVariant phase)
	{
		EObject parentObject = container.eContainer();
		if (parentObject instanceof GContainer)
		{
			GContainer parentContainer = (GContainer) parentObject;
			GContainerDef parentDefinition = parentContainer.gGetDefinition();
			if (parentDefinition instanceof GChoiceContainerDef)
			{
				doSetContainerCreationPhase(parentContainer, phase);
			}
		}
	}

	/**
	 * Set the creation phase context of the provided container to a particular value. If the <code>phase</code> is
	 * null, any previously existing context will be removed.
	 *
	 * @param container
	 *        the container
	 * @param phase
	 *        the phase value
	 */
	private static void doSetContainerCreationPhase(GContainer container, EProjectVariant phase)
	{
		if (container == null)
		{
			return;
		}

		if (phase == EProjectVariant.DEVELOPMENT)
		{
			phase = null; // SUPPRESS CHECKSTYLE ignore this
		}
		if (phase != null)
		{
			// Set Production Flag replacement to old impl 'gGetExtensions.put(CONTAINER_CREATION_PHASE, phaseName)'
			IProject project = MetaModelUtils.getProject(container);
			IGenericFactory ecucGenericFactory = MMSRegistry.INSTANCE.getGenericFactory(project);

			String phaseName = phase.toString();
			EPackage specialDataPackage = getSpecialDataPackage(container);
			GAdminData adminData = ecucGenericFactory.createAdminData();
			GSdg sdg = ecucGenericFactory.createSdg();
			GSdgContents sdgContents = ecucGenericFactory.createSdgContents();
			sdg.gSetSdgContentsType(sdgContents);
			adminData.gGetSdgs().add(sdg);
			container.gSetAdminData(adminData);

			encodePrimitiveTypePrefix(sdgContents, CONTAINER_CREATION_PHASE, String.class.getName(), phaseName,
				specialDataPackage, ecucGenericFactory);
		}
		else
		{
			// Remove Production Flag replacement to old impl 'gGetExtensions.remove(CONTAINER_CREATION_PHASE)'
			Map<String, Object> gGetExtensions = container.gGetExtensions();
			if (gGetExtensions != null)
			{
				gGetExtensions.remove(CONTAINER_CREATION_PHASE);
			}
			GAdminData adminData = container.gGetAdminData();
			if (adminData == null)
			{
				return;
			}
			TransactionalEditingDomain editingDomain = TransactionUtil.getEditingDomain(adminData);
			try
			{
				WorkspaceTransactionUtil.executeInWriteTransaction(editingDomain, new Runnable()
				{
					public void run()
					{
						Command command = RemoveCommand.create(editingDomain, adminData);
						command.execute();
					}
				}, "Remove post build production flag"); //$NON-NLS-1$
			}
			catch (OperationCanceledException | ExecutionException e)
			{
				CessarPluginActivator.getDefault().logError(e);
			}
		}

	}

	/**
	 * This method is responsible to create new Sds and assign them to SdgContents
	 *
	 * @param sdgContents
	 * @param extensionKey
	 * @param extensionTypeName
	 * @param value
	 * @param specialDataPackage
	 * @param ecucGenericFactory
	 */
	private static void encodePrimitiveTypePrefix(GSdgContents sdgContents, String extensionKey,
		String extensionTypeName, String value, EPackage specialDataPackage, IGenericFactory ecucGenericFactory)
	{
		if (sdgContents == null || extensionTypeName == null || specialDataPackage == null)
		{
			return;
		}
		List<GSd> sds = sdgContents.gGetSds();
		if (extensionKey != null)
		{
			GSd sdExtensionKey = ecucGenericFactory.createSd();
			sdExtensionKey.gSetGid(EXTENSION_KEY);
			setTextToSd(sdExtensionKey, extensionKey);
			sds.add(sdExtensionKey);
		}

		GSd sdType = ecucGenericFactory.createSd();
		sdType.gSetGid(TYPE);
		setTextToSd(sdType, extensionTypeName);
		sds.add(sdType);

		GSd sdValue = ecucGenericFactory.createSd();
		sdValue.gSetGid(VALUE);
		setTextToSd(sdValue, value);
		sds.add(sdValue);
	}

	/**
	 * Utility method to set text to Sd
	 *
	 * @param sd
	 * @param text
	 */
	private static void setTextToSd(GSd sd, String text)
	{
		Sd wrappedSd = SpecialDataFactory.createSd(sd);

		FeatureMap map = wrappedSd.getMixed();
		if (map != null)
		{
			map.add(org.eclipse.emf.ecore.xml.type.XMLTypePackage.eINSTANCE.getXMLTypeDocumentRoot_Text(), text);
		}
		else
		{
			wrappedSd.setValue(text);
		}
	}

	/**
	 * To get EPackage from EObject provided.
	 *
	 * @param resource
	 * @return Object of Type EPackage
	 */
	private static EPackage getSpecialDataPackage(EObject eObject)
	{
		IMetaModelDescriptor descriptor = MetaModelDescriptorRegistry.INSTANCE.getDescriptor(eObject);
		if (descriptor == null)
		{
			PlatformLogUtil.logAsError(Activator.getPlugin(),
				NLS.bind("Unable to retrieve meta model release descriptor for object", eObject)); //$NON-NLS-1$
		}
		return descriptor.getRootEPackage();
	}

	/**
	 * Returns the definition of the given <code>eObject</code>, that may be a <code>GModuleConfiguration</code> or a
	 * <code>GContainer</code>, or <code>null</code> otherwise.
	 *
	 * @param eObject
	 *        the e object
	 * @return the definition
	 */
	public static GARObject getDefinition(EObject eObject)
	{
		if (eObject instanceof GModuleConfiguration)
		{
			return ((GModuleConfiguration) eObject).gGetDefinition();
		}

		if (eObject instanceof GContainer)
		{
			return ((GContainer) eObject).gGetDefinition();
		}
		return null;
	}

	/**
	 * Gets the standard module defs.
	 *
	 * @param autosarReleaseDescriptor
	 *        the autosar release descriptor
	 * @return the standard module defs
	 */
	public static List<GModuleDef> getStandardModuleDefs(AutosarReleaseDescriptor autosarReleaseDescriptor)
	{
		AutosarLibraryDescriptor descriptor = autosarReleaseDescriptor.getAutosarLibraryDescriptors().get(
			AutosarLibraryIDEnumerator.ECU_C_PARAM_DEF_LIB_ID);
		URI uri = descriptor.getFileURIs().iterator().next();

		ResourceSet resourceSet = new ResourceSetImpl();
		Resource resource = resourceSet.createResource(uri, autosarReleaseDescriptor.getContentTypeIds().get(0));
		// Load the resource file
		Map<String, Object> options = new HashMap<String, Object>();
		options.put(XMLResource.OPTION_ENCODING, "UTF-8"); //$NON-NLS-1$
		options.put(XMLResource.OPTION_KEEP_DEFAULT_CONTENT, Boolean.TRUE);

		try
		{
			resource.load(options);
		}
		catch (Exception e) // SUPPRESS CHECKSTYLE returning empty list
		{
			CessarPluginActivator.getDefault().logError(e);
			return Collections.emptyList();
		}

		// Navigate to the package containing EcuCDefs
		if (resource.getContents().isEmpty())
		{
			return Collections.emptyList();
		}
		GAUTOSAR rootObject = (GAUTOSAR) resource.getContents().get(0);
		Assert.isNotNull(rootObject);
		GARPackage rootPackage = rootObject.gGetArPackages().get(0);
		Assert.isNotNull(rootPackage);

		List<GModuleDef> moduleDefs = new ArrayList<GModuleDef>();
		TreeIterator<EObject> rootIterator = rootObject.eAllContents();
		while (rootIterator.hasNext())
		{
			EObject obj = rootIterator.next();
			// if (GModuleDef.class.isAssignableFrom(obj.getClass()))
			if (obj instanceof GModuleDef)
			{
				moduleDefs.add((GModuleDef) obj);
			}
		}
		return moduleDefs;
	}

	/**
	 * Gets the external standard module defs.
	 *
	 * @param autosarReleaseDescriptor
	 *        the autosar release descriptor
	 * @param externalStandardImportFilePath
	 *        the external standard import file path
	 * @return a list with the {@link GModuleDef}s stored in the file given by its absolute path:
	 *         {@code externalStandardImportFilePath}, never <code>null</code>.
	 */
	public static List<GModuleDef> getExternalStandardModuleDefs(AutosarReleaseDescriptor autosarReleaseDescriptor,
		String externalStandardImportFilePath)
	{
		URI uri = URI.createFileURI(externalStandardImportFilePath);

		if (uri == null)
		{
			return Collections.emptyList();
		}

		ResourceSet resourceSet = new ResourceSetImpl();
		Resource resource = resourceSet.createResource(uri, autosarReleaseDescriptor.getContentTypeIds().get(0));
		// Load the resource file
		Map<String, Object> options = new HashMap<String, Object>();
		options.put(XMLResource.OPTION_ENCODING, "UTF-8"); //$NON-NLS-1$
		options.put(XMLResource.OPTION_KEEP_DEFAULT_CONTENT, Boolean.TRUE);

		try
		{
			resource.load(options);
		}
		catch (Exception e) // SUPPRESS CHECKSTYLE returning empty list
		{
			CessarPluginActivator.getDefault().logError(e);
			return Collections.emptyList();
		}

		// Navigate to the package containing EcuCDefs

		if (resource.getContents().isEmpty())
		{
			return Collections.emptyList();
		}
		GAUTOSAR rootObject = (GAUTOSAR) resource.getContents().get(0);
		Assert.isNotNull(rootObject);
		GARPackage rootPackage = rootObject.gGetArPackages().get(0);
		Assert.isNotNull(rootPackage);

		List<GModuleDef> moduleDefs = new ArrayList<GModuleDef>();
		TreeIterator<EObject> rootIterator = rootObject.eAllContents();
		while (rootIterator.hasNext())
		{
			EObject obj = rootIterator.next();

			if (obj instanceof GModuleDef)
			{
				moduleDefs.add((GModuleDef) obj);
			}
		}
		return moduleDefs;
	}

	/**
	 * Return true if the provided container definition does have support for post build context, false otherwise.
	 *
	 * @param cntDef
	 *        the cnt def
	 * @return the post build context support
	 */
	public static boolean getPostBuildContextSupport(GParamConfContainerDef cntDef)
	{
		Boolean isConfigSet = MMSRegistry.INSTANCE.getMMService(
			cntDef).getEcucMMService().getMultipleConfigurationContainer(cntDef);
		if (Boolean.TRUE.equals(isConfigSet))
		{
			return true;
		}
		else
		{
			Map<String, Object> gGetExtensions = cntDef.gGetExtensions();
			Object object = gGetExtensions.get(EcucMetaModelUtils.PB_SUPPORT_PREF);
			return Boolean.TRUE.equals(object);
		}
	}

	/**
	 * Set the post build context support for the provided container def. The caller shall use a transaction.
	 *
	 * @param cntDef
	 *        the cnt def
	 * @param value
	 *        the value
	 */
	public static void setPostBuildContextSupport(GParamConfContainerDef cntDef, boolean value)
	{
		if (value)
		{
			cntDef.gGetExtensions().put(EcucMetaModelUtils.PB_SUPPORT_PREF, Boolean.valueOf(value));
		}
		else
		{
			cntDef.gGetExtensions().remove(EcucMetaModelUtils.PB_SUPPORT_PREF);
		}
	}

	/**
	 * Gets the post build context.
	 *
	 * @param container
	 *        the container
	 * @param isRecursive
	 *        the is recursive
	 * @return the post build context
	 */
	public static IPostBuildContext getPostBuildContext(GContainer container, boolean isRecursive)
	{
		IPostBuildContext ctx = null;
		if (!isRecursive)
		{
			ctx = getContainerPostBuildContext(container);
		}
		else
		{
			ctx = getContainerPostBuildContext(container);
			if (ctx == null)
			{
				EObject parent = container.eContainer();
				if (!(parent instanceof GContainer))
				{
					return null;
				}
				else
				{
					return getPostBuildContext((GContainer) parent, isRecursive);
				}
			}
		}
		return ctx;
	}

	/**
	 * Return the post build context of a particular container. If the container does not have a post build context it
	 * will return null
	 *
	 * @param container
	 *        the container
	 * @return the container post build context
	 */
	public static IPostBuildContext getContainerPostBuildContext(GContainer container)
	{
		Object object = container.gGetExtensions().get(EcucMetaModelUtils.PB_CONTEXT_PREF);
		if (object instanceof String)
		{
			// should be a string=int pair.
			String value = (String) object;
			int id = -1;
			int idx = value.indexOf('=');
			if (idx > -1)
			{
				// if there is no ID we will default to -1
				try
				{
					id = Integer.parseInt(value.substring(idx + 1));
				}
				catch (NumberFormatException ex)
				{
					CessarPluginActivator.getDefault().logError(ex);
				}
				value = value.substring(0, idx);
			}
			final String fValue = value;
			final int fID = id;
			return new PostBuildContext(fValue, fID, true);
		}
		return null;
	}

	/**
	 * Set the post build context of the provided container to a particular value. If the postbuild context is null, any
	 * previously existing context will be removed. The caller shall use a transaction.
	 *
	 * @param container
	 *        the container
	 * @param context
	 *        the context
	 */
	public static void setPostBuildContext(GContainer container, IPostBuildContext context)
	{
		if (context == null)
		{
			container.gGetExtensions().remove(EcucMetaModelUtils.PB_CONTEXT_PREF);
		}
		else
		{
			container.gGetExtensions().put(EcucMetaModelUtils.PB_CONTEXT_PREF,
				context.getName() + "=" + context.getID()); //$NON-NLS-1$
		}
	}

	/**
	 * Returns whether the upper multiplicity (as per.
	 *
	 * @param multiplicity
	 *        the {@link GParamConfMultiplicity} whose upper multiplicity is checked
	 * @return whether the upper multiplicity of <code>multiplicity</code> is greater than one
	 *         {@linkplain IEcucMMService#getUpperMultiplicity(GParamConfMultiplicity, BigInteger, boolean)}) of the
	 *         given {@link GParamConfMultiplicity} is greater than one.
	 */
	public static boolean isMulti(GParamConfMultiplicity multiplicity)
	{
		BigInteger upperMultiplicity = getEcucMMService(multiplicity).getUpperMultiplicity(multiplicity, BigInteger.ONE,
			false);

		return upperMultiplicity.compareTo(BigInteger.ONE) == 1;
	}

	/**
	 * Returns true if the received container is located within a multipleConfigurationContainer, else returns false
	 *
	 * @param containerDef
	 * @return true if the postBuildChangeable flag can be set, else false
	 */
	public static boolean isLocatedInMultipleConfigurationContainer(GContainerDef containerDef)
	{
		boolean canSetPostBuild = false;

		GModuleDef moduleDef = getModuleDef(containerDef);
		List<GParamConfContainerDef> multipleConfigContainerDefs = getMultipleConfigurationContainerDefs(moduleDef);
		int size = multipleConfigContainerDefs.size();
		if (size > 0)
		{

			EObject eContainer = containerDef.eContainer();
			while (eContainer != null && !(eContainer instanceof GContainer))
			{
				if (multipleConfigContainerDefs.contains(eContainer))
				{
					canSetPostBuild = true;
					break;
				}
				eContainer = eContainer.eContainer();
			}
		}
		return canSetPostBuild;
	}

	/**
	 * Returns the multipleConfigurationContainer definition containing this containerDef, if there is one, else returns
	 * null
	 *
	 * @param containerDef
	 * @return the multipleConfigurationContainer definition containing this containerDef
	 */
	public static GParamConfContainerDef getParentMultipleConfigurationContainerDef(GContainerDef containerDef)
	{
		GParamConfContainerDef multipleConfigurationContainerDef = null;
		GModuleDef moduleDef = getModuleDef(containerDef);
		List<GParamConfContainerDef> multipleConfigContainerDefs = getMultipleConfigurationContainerDefs(moduleDef);
		int size = multipleConfigContainerDefs.size();
		if (size > 0)
		{

			EObject eContainer = containerDef.eContainer();
			while (eContainer != null && !(eContainer instanceof GContainer))
			{
				if (multipleConfigContainerDefs.contains(eContainer))
				{
					multipleConfigurationContainerDef = (GParamConfContainerDef) eContainer;
					break;
				}
				eContainer = eContainer.eContainer();
			}
		}
		return multipleConfigurationContainerDef;
	}

	/**
	 * Returns the multipleConfigurationContainer instance containing this container, if there is one, else returns null
	 *
	 * @param container
	 *
	 * @return the multipleConfigurationContainer containing this container
	 */
	public static GContainer getParentMultipleConfigurationContainer(GContainer container)
	{
		GContainer multipleConfigurationContainer = null;
		GContainerDef containerDef = container.gGetDefinition();
		GModuleDef moduleDef = getModuleDef(containerDef);
		List<GParamConfContainerDef> multipleConfigContainerDefs = getMultipleConfigurationContainerDefs(moduleDef);
		int size = multipleConfigContainerDefs.size();
		if (size > 0)
		{

			EObject eContainer = container.eContainer();
			while (eContainer != null && (eContainer instanceof GContainer))
			{
				GContainer gContainer = (GContainer) eContainer;
				GContainerDef gContainerDef = gContainer.gGetDefinition();
				if (multipleConfigContainerDefs.contains(gContainerDef))
				{

					multipleConfigurationContainer = (GContainer) eContainer;
					break;
				}
				eContainer = eContainer.eContainer();
			}
		}
		return multipleConfigurationContainer;
	}

	/**
	 * Returns true if the received container definition is a multipleConfigurationContainer, else returns false
	 *
	 * @param containerDef
	 * @return true if the received container is a multipleConfigurationContainer
	 */
	public static boolean isMultipleConfigurationContainer(GContainerDef containerDef)
	{
		if (containerDef == null)
		{
			return false;
		}
		GModuleDef moduleDef = getModuleDef(containerDef);

		if (moduleDef == null)
		{
			return false;
		}
		// start collecting multiple configuration container definitions
		EList<GContainerDef> containers = moduleDef.gGetContainers();
		List<GParamConfContainerDef> multiConfigContainerDefs = new ArrayList<GParamConfContainerDef>();
		for (GContainerDef gContainerDef: containers)
		{
			collectSubContainers(gContainerDef, multiConfigContainerDefs);
			boolean contains = multiConfigContainerDefs.contains(containerDef);
			// return true as soon as found that the received containerDef is a multiple configuration container
			if (contains)
			{
				return true;
			}
		}

		// the received container is not a multiple configuration container
		return false;
	}

	/**
	 * Return the module definition that owns (directly or indirectly) the <code>container</code>. If the container is
	 * not owned by a module definition null will be returned and an error will be logged
	 *
	 * @param container
	 *        a container definition
	 * @return the owner GModuleDef or null if none found
	 */
	private static GModuleDef getModuleDef(GContainerDef container)
	{
		if (container == null || container.eIsProxy())
		{
			return null;
		}
		EObject owner = container.eContainer();

		while (!(owner instanceof GModuleDef))
		{
			// starting with Autosar 4.2.2 the owner can be a EcucDestinationUriPolicy
			if (owner != null && owner.eClass().getName().equals(Messages.DEST_URI_POLICY))
			{
				return null;
			}
			if (owner == null)
			{
				// should not happen
				CessarPluginActivator.getDefault().logError(new Throwable(),
					"Got a container without a module definition: {0}", //$NON-NLS-1$
					container);
				return null;
			}
			owner = owner.eContainer();
		}
		return (GModuleDef) owner;
	}

	/**
	 * Returns the list of container definitions from the given <code>moduleDef</code>, that have the feature multiple
	 * configuration container, set.
	 *
	 * @param moduleDef
	 *        the given module definition
	 * @return the list of container definitions or an empty list if no container is found or the model not support the
	 *         feature (for example MM20).
	 */

	private static List<GParamConfContainerDef> getMultipleConfigurationContainerDefs(GModuleDef moduleDef)
	{
		List<GParamConfContainerDef> multiConfigContainerDefs = new ArrayList<GParamConfContainerDef>();
		if (moduleDef != null)
		{
			EList<GContainerDef> containers = moduleDef.gGetContainers();
			for (GContainerDef gContainerDef: containers)
			{
				collectSubContainers(gContainerDef, multiConfigContainerDefs);
			}

		}
		return multiConfigContainerDefs;
	}

	/**
	 * @param gContainerDef
	 * @param result
	 */
	private static void collectSubContainers(GContainerDef gContainerDef, List<GParamConfContainerDef> result)
	{
		IProject project = MetaModelUtils.getProject(gContainerDef);
		if (project != null)
		{
			IMetaModelService mmService = MMSRegistry.INSTANCE.getMMService(project);
			EList<? extends GContainerDef> subContainers = null;
			if (gContainerDef instanceof GParamConfContainerDef)
			{
				if (mmService.getEcucMMService().isMultipleConfigurationContainer(
					(GParamConfContainerDef) gContainerDef))
				{
					result.add((GParamConfContainerDef) gContainerDef);
				}
				subContainers = ((GParamConfContainerDef) gContainerDef).gGetSubContainers();
			}
			else if (gContainerDef instanceof GChoiceContainerDef)
			{
				subContainers = ((GChoiceContainerDef) gContainerDef).gGetChoices();
			}
			if (subContainers != null)
			{
				for (GContainerDef gSubContainerDef: subContainers)
				{
					collectSubContainers(gSubContainerDef, result);
				}
			}
		}
	}

	/**
	 * Get the creation phase for the a <code>container</code>. This method does not treat the case when this container
	 * is a merged instance. If container is null or it doesn't have a creation phase, 'DEVELOPMENT' is returned.
	 */
	private static EProjectVariant getNonMergedContainerCreationPhase(GContainer container)
	{
		EProjectVariant result = null;
		// for a null container return DEVELOPMENT phase
		if (container == null)
		{
			return EProjectVariant.DEVELOPMENT;
		}

		Map<String, Object> gGetExtensions = container.gGetExtensions();
		// if container has no extensions,the result is DEVELOPMENT phase
		if (gGetExtensions == null)
		{
			result = EProjectVariant.DEVELOPMENT;
		}
		else
		{
			String value = (String) container.gGetExtensions().get(CONTAINER_CREATION_PHASE);
			if (value == null)
			{
				GAdminData adminData = container.gGetAdminData();// eAllContents();
				if (adminData != null)
				{
					result = EProjectVariant.PRODUCTION;
				}
				else
				{
					result = EProjectVariant.DEVELOPMENT;
				}
			}
			else
			{
				try
				{
					result = EProjectVariant.valueOf(value);
				}
				catch (IllegalArgumentException e)
				{
					result = EProjectVariant.DEVELOPMENT;
				}
			}
		}

		return result;
	}

}
