/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidl6458 Jun 16, 2010 2:41:17 PM </copyright>
 */
package eu.cessar.ct.runtime.ecuc.internal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.sphinx.emf.util.EcorePlatformUtil;
import org.eclipse.sphinx.emf.util.WorkspaceTransactionUtil;

import eu.cessar.ct.core.mms.EcucMetaModelUtils;
import eu.cessar.ct.core.mms.IGenericFactory;
import eu.cessar.ct.core.mms.MMSRegistry;
import eu.cessar.ct.core.mms.MetaModelUtils;
import eu.cessar.ct.core.mms.splittable.SplitableUtils;
import eu.cessar.ct.core.platform.EProjectVariant;
import eu.cessar.ct.runtime.ecuc.IEcucCore;
import eu.cessar.ct.runtime.ecuc.IEcucModel;
import eu.cessar.ct.sdk.runtime.ExecutionService;
import eu.cessar.ct.sdk.utils.EcucUtils.Service;
import eu.cessar.ct.sdk.utils.ModelUtils;
import eu.cessar.ct.sdk.utils.SplitUtils;
import eu.cessar.req.Requirement;
import gautosar.gecucdescription.GContainer;
import gautosar.gecucdescription.GModuleConfiguration;
import gautosar.gecucparameterdef.GContainerDef;
import gautosar.gecucparameterdef.GModuleDef;
import gautosar.ggenericstructure.ginfrastructure.GARObject;
import gautosar.ggenericstructure.ginfrastructure.GARPackage;
import gautosar.ggenericstructure.ginfrastructure.GAUTOSAR;

/**
 *
 */
public final class SDKEcucUtils implements Service
{

	/**
	 * SDKEcucUtils instance
	 */
	public static final SDKEcucUtils eINSTANCE = new SDKEcucUtils();
	private static final IStatus STATUS_OK = new Status(IStatus.OK, CessarPluginActivator.PLUGIN_ID, ""); //$NON-NLS-1$

	private SDKEcucUtils()
	{
		// do nothing
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * eu.cessar.ct.sdk.utils.EcucUtils.Service#createModuleConfiguration(gautosar.ggenericstructure.ginfrastructure
	 * .GAUTOSAR, gautosar.gecucparameterdef.GModuleDef, java.lang.String, boolean)
	 */
	@Requirement(
		reqID = "REQ_API_ECUCUTILS_MERGED#2")
	public GModuleConfiguration createModuleConfiguration(final GAUTOSAR root, GModuleDef moduleDef,
		String moduleConfigQualifiedName, boolean replace) throws ExecutionException
	{
		if (SplitableUtils.INSTANCE.isSplitable(root))
		{
			throw new IllegalArgumentException("GAUTOSAR is a merged object. It shall be file-based!"); //$NON-NLS-1$
		}

		final String[] splitQName = MetaModelUtils.splitQualifiedName(moduleConfigQualifiedName);
		Assert.isTrue(splitQName.length > 1, "At least two segments need to be specified in the qualified name"); //$NON-NLS-1$
		@SuppressWarnings("deprecation")
		final IGenericFactory factory = MMSRegistry.INSTANCE.getGenericFactory(root);

		final GARPackage[] pack = new GARPackage[1];
		IFile file = EcorePlatformUtil.getFile(root);
		Assert.isLegal(file != null, "The autosar root should be stored inside a file"); //$NON-NLS-1$
		IProject project = file.getProject();
		// CHECKSTYLE:OFF not needed
		ExecutionService.getRunningManager(project).updateModel(new Runnable()
		{
			public void run()
			{
				// look for an ARPackage with the right name
				GARPackage found = null;
				for (GARPackage gPack: root.gGetArPackages())
				{
					if (splitQName[0].equals(gPack.gGetShortName()))
					{
						found = gPack;
						break;
					}
				}
				if (found == null)
				{
					found = factory.createARPackage();
					found.gSetShortName(splitQName[0]);
					root.gGetArPackages().add(found);
				}
				for (int i = 1; i < splitQName.length - 1; i++)
				{
					GARPackage subFound = null;
					for (GARPackage gPack: found.gGetSubPackages())
					{
						if (splitQName[i].equals(gPack.gGetShortName()))
						{
							subFound = gPack;
							break;
						}
					}
					if (subFound == null)
					{
						subFound = factory.createARPackage();
						subFound.gSetShortName(splitQName[i]);
						found.gGetSubPackages().add(subFound);
					}
					found = subFound;
				}
				pack[0] = found;
			}
		});
		return createModuleConfiguration(pack[0], moduleDef, splitQName[splitQName.length - 1], replace);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * eu.cessar.ct.sdk.utils.EcucUtils.Service#createModuleConfiguration(gautosar.ggenericstructure.ginfrastructure
	 * .GARPackage, gautosar.gecucparameterdef.GModuleDef, java.lang.String, boolean)
	 */
	@Requirement(
		reqID = "REQ_API_ECUCUTILS_MERGED#2")
	public GModuleConfiguration createModuleConfiguration(final GARPackage pack, final GModuleDef moduleDef,
		final String moduleConfigShortName, final boolean replace) throws ExecutionException
	{
		if (SplitableUtils.INSTANCE.isSplitable(pack))
		{
			throw new IllegalArgumentException("GARPackage is a merged object. It shall be file-based!"); //$NON-NLS-1$
		}
		@SuppressWarnings("deprecation")
		final IGenericFactory factory = MMSRegistry.INSTANCE.getGenericFactory(pack);
		final GModuleConfiguration[] result = new GModuleConfiguration[1];

		IFile file = EcorePlatformUtil.getFile(pack);
		Assert.isLegal(file != null, "The package should be stored inside a file"); //$NON-NLS-1$
		IProject project = file.getProject();
		ExecutionService.getRunningManager(project).updateModel(new Runnable()
		{

			public void run()
			{
				EList<GModuleConfiguration> modules = ModelUtils.getFilteredList(GModuleConfiguration.class,
					pack.gGetElements());
				Iterator<GModuleConfiguration> it = modules.iterator();
				while (it.hasNext())
				{
					GModuleConfiguration module = it.next();
					if (moduleConfigShortName.equals(module.gGetShortName()))
					{
						if (!replace)
						{
							result[0] = module;
							return;
						}
						else
						{
							// delete this one and create another
							it.remove();
							break;
						}
					}
				}
				// create the module configuration
				result[0] = factory.createModuleConfiguration();
				result[0].gSetShortName(moduleConfigShortName);
				result[0].gSetDefinition(moduleDef);
				modules.add(result[0]);
			}
		});
		return result[0];
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.sdk.utils.EcucUtils.Service#getAvailableModules(org.eclipse.core.resources.IProject)
	 */
	public Collection<GModuleDef> getAvailableModules(IProject project)
	{
		Assert.isNotNull(project, "Project cannot be null"); //$NON-NLS-1$
		IEcucModel model = IEcucCore.INSTANCE.getEcucModel(project);
		if (model == null)
		{
			return Collections.emptyList();
		}
		return model.getAllModuleDefs();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.sdk.utils.EcucUtils.Service#getAvailableModules(org.eclipse.core.resources.IProject, boolean)
	 */
	@Requirement(
		reqID = "REQ_API_ECUCUTILS_MERGED#1")
	@Override
	public Collection<GModuleDef> getAvailableModules(IProject project, boolean merged)
	{
		Collection<GModuleDef> cfgs = getAvailableModules(project);
		if (merged)
		{
			Set<GModuleDef> mergedModules = new HashSet<GModuleDef>();
			for (GModuleDef cfg: cfgs)
			{
				GModuleDef mCfg = SplitUtils.getMergedInstance(cfg);
				mergedModules.add(mCfg);
			}
			return mergedModules;
		}
		return cfgs;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.sdk.utils.EcucUtils.Service#getAvailableInstances(org.eclipse.core.resources.IProject,
	 * gautosar.gecucparameterdef.GModuleDef)
	 */
	public Collection<GModuleConfiguration> getAvailableInstances(IProject project, GModuleDef moduleDef)
	{
		Assert.isNotNull(project, "Project cannot be null"); //$NON-NLS-1$
		IEcucModel model = IEcucCore.INSTANCE.getEcucModel(project);
		if (model == null)
		{
			return Collections.emptyList();
		}
		return model.getModuleCfgs(moduleDef);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.sdk.utils.EcucUtils.Service#getAvailableInstances(org.eclipse.core.resources.IProject,
	 * gautosar.gecucparameterdef.GModuleDef, boolean)
	 */
	@Requirement(
		reqID = "REQ_API_ECUCUTILS_MERGED#1")
	@Override
	public Collection<GModuleConfiguration> getAvailableInstances(IProject project, GModuleDef moduleDef, boolean merged)
	{
		GModuleDef concreteInstance = moduleDef;
		if (SplitUtils.isMergedInstace(moduleDef))
		{
			concreteInstance = (GModuleDef) SplitUtils.getConcreteInstances((GARObject) moduleDef).iterator().next();
		}
		Collection<GModuleConfiguration> cfgs = getAvailableInstances(project, concreteInstance);
		if (merged)
		{
			Set<GModuleConfiguration> mergedCfgs = new HashSet<GModuleConfiguration>();
			for (GModuleConfiguration cfg: cfgs)
			{
				GModuleConfiguration mCfg = SplitUtils.getMergedInstance(cfg);
				mergedCfgs.add(mCfg);
			}
			return mergedCfgs;
		}
		return cfgs;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.sdk.utils.EcucUtils.Service#isCreatedAtPostBuild(gautosar.gecucdescription.GContainer)
	 */
	@Requirement(
		reqID = "REQ_API_ECUCUTILS_POST_BUILD#1")
	@Override
	public boolean isCreatedAtPostBuild(GContainer container)
	{
		// for a null container, EcucMetaModelUtils#getContainerCreationPhase method returns the default phase,
		// 'DEVELOPMENT'
		EProjectVariant containerCreationPhase = EcucMetaModelUtils.getContainerCreationPhase(container);
		if (EProjectVariant.PRODUCTION == containerCreationPhase)
		{
			return true;
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.sdk.utils.EcucUtils.Service#setContainerCreationPhase(gautosar.gecucdescription.GContainer,
	 * eu.cessar.ct.core.platform.EProjectVariant)
	 */
	@Override
	public void setContainerCreationPhase(GContainer container, EProjectVariant phase)
	{
		EcucMetaModelUtils.setContainerCreationPhase(container, phase);
	}

	/**
	 * Return a list with all direct container instances under specified parent, that have the given container
	 * definition. The parent must be a Module configuration.
	 *
	 * @param parent
	 *        a module configuration
	 * @param containerDef
	 *        the container definition whose instances must be returned
	 * @return A list with all container instances, or empty list if no instance is found.
	 */
	public List<GContainer> getContainerInstances(GModuleConfiguration parent, GContainerDef containerDef)
	{
		EList<? extends GContainer> subContainers = null;

		if ((parent == null) || (containerDef == null))
		{
			return Collections.emptyList();
		}
		else
		{
			subContainers = parent.gGetContainers();
		}

		List<GContainer> result = getContainerInstancesWithSpecifiedDefinition(subContainers, containerDef);
		return result;
	}

	/**
	 * Return a list with all direct container instances under specified parent, that have the given container
	 * definition. The parent must be a parent container.
	 *
	 * @param parent
	 *        another container instance
	 * @param containerDef
	 *        the container definition whose instances must be returned
	 * @return A list with all container instances, or empty list if no instance is found.
	 */
	public List<GContainer> getContainerInstances(GContainer parent, GContainerDef containerDef)
	{
		EList<? extends GContainer> subContainers = null;

		if ((parent == null) || (containerDef == null))
		{
			return Collections.emptyList();
		}
		else
		{
			subContainers = parent.gGetSubContainers();
		}

		List<GContainer> result = getContainerInstancesWithSpecifiedDefinition(subContainers, containerDef);
		return result;
	}

	/**
	 * Return a list with all direct container instances under specified parent, that have the given container
	 * definition.The parent must be a Module configuration, the container definition is given as the Fully Qualified
	 * name. For ex: "/AUTOSAR/Can0/CanConfigSet"
	 *
	 * @param parent
	 *        a module configuration
	 * @param qualifiedName
	 *        the container definition whose instances must be returned
	 * @return A list with all container instances,or empty list if no instance is found.
	 */
	public List<GContainer> getContainerInstances(GModuleConfiguration parent, String qualifiedName)
	{
		EList<? extends GContainer> subContainers = null;
		IProject project = MetaModelUtils.getProject(parent);
		GContainerDef containerDef = null;
		if (project != null)
		{
			containerDef = (GContainerDef) ModelUtils.getEObjectWithQualifiedName(project, qualifiedName);
		}
		if ((parent == null) || (containerDef == null))
		{
			return Collections.emptyList();
		}
		else
		{
			subContainers = parent.gGetContainers();
		}

		List<GContainer> result = getContainerInstancesWithSpecifiedDefinition(subContainers, containerDef);
		return result;
	}

	/**
	 * Return a list with all direct container instances under specified parent, that have the given container
	 * definition.The parent must be a parent container,the container definition is given as the Fully Qualified
	 * name.For ex:"/AUTOSAR/Can0/CanConfigSet"
	 *
	 * @param parent
	 *        another container instance
	 * @param qualifiedName
	 *        the container definition whose instances must be returned
	 * @return A list with all container instances,or empty list if no instance is found.
	 */
	public List<GContainer> getContainerInstances(GContainer parent, String qualifiedName)
	{
		EList<? extends GContainer> subContainers = null;

		IProject project = MetaModelUtils.getProject(parent);
		GContainerDef containerDef = null;
		if (project != null)
		{
			containerDef = (GContainerDef) ModelUtils.getEObjectWithQualifiedName(project, qualifiedName);
		}
		if ((parent == null) || (containerDef == null))
		{
			return Collections.emptyList();
		}
		else
		{
			subContainers = parent.gGetSubContainers();
		}

		List<GContainer> result = getContainerInstancesWithSpecifiedDefinition(subContainers, containerDef);
		return result;
	}

	/**
	 * @param subContainers
	 * @param containerDef
	 * @return
	 */
	private List<GContainer> getContainerInstancesWithSpecifiedDefinition(EList<? extends GContainer> subContainers,
		GContainerDef containerDef)
		{
		// retrieve only container instances having specified definition
		// in order for the method to work on both split and non-split configuration the check
		// has to be done after both the qualifiedName and the eclass
		List<GContainer> result = new ArrayList<GContainer>();
		for (GContainer containerInst: subContainers)
		{
			GContainerDef gGetDefinition = containerInst.gGetDefinition();
			if (gGetDefinition != null)
			{
				EClass wantedDefinition = containerDef.eClass();
				EClass elementDefinition = gGetDefinition.eClass();
				String objectFromList = ModelUtils.getAbsoluteQualifiedName(gGetDefinition);
				String wantedObject = ModelUtils.getAbsoluteQualifiedName(containerDef);
				if ((objectFromList != null) && (wantedObject != null))
				{
					if ((wantedDefinition == elementDefinition) && (objectFromList.equals(wantedObject)))
					{
						result.add(containerInst);
					}
				}
			}
		}

		((ArrayList<GContainer>) result).trimToSize();
		return result;
		}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.sdk.utils.EcucUtils.Service#convertEcucFromStandardToRefined(gautosar.gecucdescription.
	 * GModuleConfiguration, gautosar.gecucparameterdef.GModuleDef)
	 */
	@Requirement(
		reqID = "29615")
	@Override
	public IStatus convertEcucFromStandardToRefined(final GModuleConfiguration config, final GModuleDef refinedModuleDef)
	{
		if (config == null)
		{
			return new Status(IStatus.ERROR, CessarPluginActivator.PLUGIN_ID, Messages.errorNullModuleConfig);
		}
		if (refinedModuleDef == null)
		{
			return new Status(IStatus.ERROR, CessarPluginActivator.PLUGIN_ID, Messages.errorNullRefinedDefinition);
		}

		final IProject project = MetaModelUtils.getProject(config);
		final GModuleDef standardDef = (GModuleDef) EcucMetaModelUtils.getDefinition(config);
		if (standardDef == null)
		{
			return new Status(IStatus.ERROR, CessarPluginActivator.PLUGIN_ID, Messages.errorNullStandardDefinition);
		}

		TransactionalEditingDomain editingDomain = MetaModelUtils.getEditingDomain(project);
		Runnable conversionRunnable = new Runnable()
		{
			@Override
			public void run()
			{
				executeStandardToRefinedConversion(config, project, refinedModuleDef);
			}
		};

		IStatus iStatus = executeConversion(conversionRunnable, editingDomain, project);
		return iStatus;
		// try
		// {
		// WorkspaceTransactionUtil.executeInWriteTransaction(editingDomain, new Runnable()
		// {
		// @Override
		// public void run()
		// {
		// executeStandardToRefinedConversion(config, project, refinedModuleDef);
		// }
		// }, Messages.infoExecuteStandardToRefinedConversion);
		// }
		// catch (Exception e)
		// {
		// CessarPluginActivator.getDefault().logError(e);
		// return new Status(IStatus.ERROR, CessarPluginActivator.PLUGIN_ID, Messages.Transaction_Failed, e);
		// }
		//
		// try
		// {
		// // save modified resources
		// ModelUtils.saveResources(ModelUtils.getDirtyResources(project), new NullProgressMonitor());
		// }
		// catch (Exception e)
		// {
		// CessarPluginActivator.getDefault().logError(e);
		// return new Status(IStatus.ERROR, CessarPluginActivator.PLUGIN_ID, Messages.Could_Not_Save, e);
		// }
		//
		// return STATUS_OK;

	}

	private IStatus executeConversion(Runnable conversionRunnable, TransactionalEditingDomain editingDomain,
		IProject project)
	{

		try
		{
			WorkspaceTransactionUtil.executeInWriteTransaction(editingDomain, conversionRunnable,
				Messages.infoExecuteStandardToRefinedConversion);
		}
		catch (Exception e)
		{
			CessarPluginActivator.getDefault().logError(e);
			return new Status(IStatus.ERROR, CessarPluginActivator.PLUGIN_ID, Messages.Transaction_Failed, e);
		}

		try
		{
			// save modified resources
			ModelUtils.saveResources(ModelUtils.getDirtyResources(project), new NullProgressMonitor());
		}
		catch (Exception e)
		{
			CessarPluginActivator.getDefault().logError(e);
			return new Status(IStatus.ERROR, CessarPluginActivator.PLUGIN_ID, Messages.Could_Not_Save, e);
		}
		return STATUS_OK;
	}

/*
 * (non-Javadoc)
 *
 * @see eu.cessar.ct.sdk.utils.EcucUtils.Service#convertEcucFromRefinedToStandard(gautosar.gecucdescription.
 * GModuleConfiguration)
 */
	@Requirement(
		reqID = "29619")
	@Override
	public IStatus convertEcucFromRefinedToStandard(final GModuleConfiguration config)
	{
		if (config != null)
		{
			final IProject project = MetaModelUtils.getProject(config);
			final GModuleDef refinedDef = (GModuleDef) EcucMetaModelUtils.getDefinition(config);
			if (refinedDef != null)
			{
				final GModuleDef standardDef = refinedDef.gGetRefinedModuleDef();
				TransactionalEditingDomain editingDomain = MetaModelUtils.getEditingDomain(project);
				if (standardDef != null)
				{
					try
					{
						WorkspaceTransactionUtil.executeInWriteTransaction(editingDomain, new Runnable()
						{
							@Override
							public void run()
							{
								executeConversionFromRefinedToStandard(config, project, standardDef);
							}
						}, Messages.infoExecuteRefinedToStandardConversion);
					}
					catch (Exception e)
					{
						CessarPluginActivator.getDefault().logError(e);
						return new Status(IStatus.ERROR, CessarPluginActivator.PLUGIN_ID, Messages.Transaction_Failed,
							e);
					}

					try
					{
						// save modified resources
						ModelUtils.saveResources(ModelUtils.getDirtyResources(project), new NullProgressMonitor());
					}
					catch (Exception e)
					{
						CessarPluginActivator.getDefault().logError(e);
						return new Status(IStatus.ERROR, CessarPluginActivator.PLUGIN_ID, Messages.Could_Not_Save, e);
					}
				}
				else
				{
					return new Status(IStatus.ERROR, CessarPluginActivator.PLUGIN_ID,
						Messages.errorNullStandardDefinition);
				}

			}
			else
			{
				return new Status(IStatus.ERROR, CessarPluginActivator.PLUGIN_ID, Messages.errorNullRefinedDefinition);
			}
			return STATUS_OK;
		}
		else
		{
			return new Status(IStatus.ERROR, CessarPluginActivator.PLUGIN_ID, Messages.errorNullModuleConfig);
		}
	}

	/**
	 * @param config
	 * @param project
	 * @param standardDef
	 * @param refinedDef
	 */
	private void executeConversionFromRefinedToStandard(GModuleConfiguration config, IProject project,
		GModuleDef standardDef)
	{
		config.gSetDefinition(standardDef);
		IEcucModel ecucModel = IEcucCore.INSTANCE.getEcucModel(project);
		EList<GContainer> gGetContainers = config.gGetContainers();
		for (GContainer gContainer: gGetContainers)
		{
			ecucModel.recursivelyRemoveReferences(gContainer, standardDef);
		}
		List<GContainer> removeGetContainers = new ArrayList<GContainer>();
		for (GContainer gContainer: gGetContainers)
		{
			ecucModel.recursiveDefUpdate(gContainer, standardDef, false);
			GContainerDef containerTargetDef = ecucModel.getRefinedContainerDefFamily(standardDef,
				gContainer.gGetDefinition());
			if (containerTargetDef != null)
			{
				gContainer.gSetDefinition(containerTargetDef);
			}
			else
			{
				removeGetContainers.add(gContainer);
			}
		}
		gGetContainers.removeAll(removeGetContainers);
	}

	/**
	 * @param config
	 * @param project
	 * @param refinedDef
	 * @param refinedDef
	 */
	private void executeStandardToRefinedConversion(GModuleConfiguration config, IProject project, GModuleDef refinedDef)
	{
		config.gSetDefinition(refinedDef);
		IEcucModel ecucModel = IEcucCore.INSTANCE.getEcucModel(project);
		EList<GContainer> gGetContainers = config.gGetContainers();
		for (GContainer gContainer: gGetContainers)
		{
			ecucModel.recursivelyRemoveReferences(gContainer, refinedDef);
		}
		for (GContainer gContainer: gGetContainers)
		{
			ecucModel.recursiveDefUpdate(gContainer, refinedDef, true);

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.sdk.utils.EcucUtils.Service#setContainerCreationPhase(gautosar.gecucdescription.GContainer,
	 * java.lang.String)
	 */
	@Override
	public void setContainerCreationPhase(GContainer container, String phase)
	{
		if (phase != null)
		{
			EcucMetaModelUtils.setContainerCreationPhase(container, EProjectVariant.getProjectVariant(phase));
		}
		else
		{
			EcucMetaModelUtils.setContainerCreationPhase(container, (EProjectVariant) null);
		}

	}
}
