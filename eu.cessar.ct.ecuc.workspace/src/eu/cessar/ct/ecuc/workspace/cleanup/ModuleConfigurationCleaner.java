/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by aurel_avramescu Aug 13, 2010 1:10:20 PM </copyright>
 */
package eu.cessar.ct.ecuc.workspace.cleanup;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IProject;

import eu.cessar.ct.core.mms.IMetaModelService;
import eu.cessar.ct.core.mms.MMSRegistry;
import eu.cessar.ct.core.mms.MetaModelUtils;
import eu.cessar.ct.ecuc.workspace.internal.Messages;
import eu.cessar.ct.ecuc.workspace.jobs.MultiplicityUtils;
import eu.cessar.ct.runtime.ecuc.IEcucCore;
import eu.cessar.ct.runtime.ecuc.IEcucModel;
import eu.cessar.ct.runtime.ecuc.util.ESplitableList;
import eu.cessar.ct.runtime.ecuc.util.SplitedEntry;
import gautosar.gecucdescription.GConfigReferenceValue;
import gautosar.gecucdescription.GContainer;
import gautosar.gecucdescription.GModuleConfiguration;
import gautosar.gecucdescription.GParameterValue;
import gautosar.gecucparameterdef.GConfigParameter;
import gautosar.gecucparameterdef.GConfigReference;
import gautosar.gecucparameterdef.GContainerDef;
import gautosar.gecucparameterdef.GModuleDef;
import gautosar.gecucparameterdef.GParamConfContainerDef;

/**
 * @author aurel_avramescu
 * 
 */
public class ModuleConfigurationCleaner
{
	private IEcucModel ecucModel = null;
	private IMetaModelService mmService;
	private List<IEcucCleaningAction> cleaningActions = new ArrayList<IEcucCleaningAction>(10);
	private GModuleConfiguration config;

	/**
	 * Default constructor
	 * 
	 * @param project
	 */
	public ModuleConfigurationCleaner(IProject project)
	{
		ecucModel = IEcucCore.INSTANCE.getEcucModel(project);
		mmService = MMSRegistry.INSTANCE.getMMService(project);
	}

	public ModuleConfigurationCleaner(IProject project, GModuleConfiguration config)
	{
		ecucModel = IEcucCore.INSTANCE.getEcucModel(project);
		mmService = MMSRegistry.INSTANCE.getMMService(project);
		this.config = config;
	}

	public List<GModuleConfiguration> performSync()
	{
		return performSync(this.config);
	}

	/**
	 * 
	 * @param config
	 * @return
	 */
	public List<GModuleConfiguration> performSync(GModuleConfiguration config)
	{
		GModuleDef def = config.gGetDefinition();
		List<GModuleConfiguration> splitedModulesConf = ecucModel.getSplitedModuleCfgs(config);
		// List<LinkedHashMap<String, ESplitableList<GContainer>>>
		// allSplitedContainers = new ArrayList<LinkedHashMap<String,
		// ESplitableList<GContainer>>>();
		for (GModuleConfiguration module: splitedModulesConf)
		{
			if (module.gGetContainers() != null)
			{
				for (GContainer container: module.gGetContainers())
				{
					if (container.gGetDefinition() == null || container.gGetDefinition().eIsProxy())
					{
						IEcucCleaningAction action = new EcucCleaningActionImpl(
							MetaModelUtils.getAbsoluteQualifiedName(container),
							ECleaningProblemsType.DEFINITION_IS_MISSING,
							Messages.ModuleConfigurationCleaner_Container, new SyncActionDelete(
								container));
						cleaningActions.add(action);
					}
				}
			}
		}
		// /////////////////////////////////////////////////////
		// Count splited containers
		// ////////////////////////////////////////////////////
		for (GContainerDef containerDef: def.gGetContainers())
		{
			List<SplitedEntry<GContainer>> splitedContainers = ecucModel.getSplitedContainersForModule(
				splitedModulesConf, containerDef);
			boolean goToNextLevel = splitedContainers != null && !splitedContainers.isEmpty();
			if (goToNextLevel)
			{
				splitedContainers = ecucModel.getSplitedContainersForModule(splitedModulesConf,
					containerDef);
				syncChildren(containerDef, splitedContainers);
			}
		}

		return splitedModulesConf;
	}

	/**
	 * 
	 * @param splitedContainers
	 * @param containerDef
	 */
	private void performSync(List<SplitedEntry<GContainer>> splitedContainers,
		GContainerDef containerDef)
	{
		Collection<ESplitableList<GContainer>> splitedCollection = new ArrayList<ESplitableList<GContainer>>();
		for (SplitedEntry<GContainer> splitedEntry: splitedContainers)
		{
			splitedCollection.add(splitedEntry.getSplitableList());
		}
		Iterator<ESplitableList<GContainer>> iterator = splitedCollection.iterator();
		List<String> shortNames = new ArrayList<String>();
		while (iterator.hasNext())
		{
			ESplitableList<GContainer> eSplitableList = iterator.next();
			if (eSplitableList.size() > 0)
			{
				if (!shortNames.contains(eSplitableList.get(0).gGetShortName()))
				{
					shortNames.add(eSplitableList.get(0).gGetShortName());
				}
				else
				{
					continue;
				}

				for (GContainer container: eSplitableList)
				{
					if (container.gGetSubContainers() != null)
					{
						for (GContainer subContainer: container.gGetSubContainers())
						{
							if (subContainer.gGetDefinition() == null
								|| subContainer.gGetDefinition().eIsProxy())
							{
								IEcucCleaningAction action = new EcucCleaningActionImpl(
									MetaModelUtils.getAbsoluteQualifiedName(subContainer),
									ECleaningProblemsType.DEFINITION_IS_MISSING,
									Messages.ModuleConfigurationCleaner_Container,
									new SyncActionDelete(subContainer));
								cleaningActions.add(action);
							}
						}
					}
				}
				List<GContainerDef> subcontainersDefs = ecucModel.collectContainerDefs(containerDef);
				for (GContainerDef subContainerDef: subcontainersDefs)
				{

					List<SplitedEntry<GContainer>> splitedSubContainers = ecucModel.getSplitedContainersForContainer(
						eSplitableList, subContainerDef);

					boolean goToNextLevel = splitedSubContainers != null
						&& !splitedSubContainers.isEmpty();
					if (goToNextLevel)
					{
						splitedSubContainers = ecucModel.getSplitedContainersForContainer(
							eSplitableList, subContainerDef);
						syncChildren(subContainerDef, splitedSubContainers);
					}

				}
			}
		}
	}

	/**
	 * Parameters synchronization
	 * 
	 * @param owners
	 * @param containerDef
	 */
	private void performParametersSync(List<GContainer> owners, GContainerDef containerDef)
	{
		for (GContainer container: owners)
		{
			if (container.gGetParameterValues() != null)
			{
				for (GParameterValue value: container.gGetParameterValues())
				{
					if (value.gGetDefinition() == null || value.gGetDefinition().eIsProxy())
					{
						IEcucCleaningAction action = new EcucCleaningActionImpl(
							MetaModelUtils.getAbsoluteQualifiedName(container),
							ECleaningProblemsType.DEFINITION_IS_MISSING,
							Messages.ModuleConfigurationCleaner_Parameter, new SyncActionDelete(
								value));
						cleaningActions.add(action);
					}
					else
					{
						GParameterValue aValue = mmService.getGenericFactory().createParameterValue(
							value.gGetDefinition());
						if (aValue.eClass() != value.eClass())
						{
							IEcucCleaningAction action = new EcucCleaningActionImpl(
								MetaModelUtils.getAbsoluteQualifiedName(container),
								ECleaningProblemsType.DEFINITION_HAS_BEEN_CHANGED,
								Messages.ModuleConfigurationCleaner_Parameter,
								new SyncActionDelete(value));
							cleaningActions.add(action);
						}

					}
				}

			}
		}
		// List<GConfigParameter> parametersConfList =
		// ecucModel.collectParameterDefs((GParamConfContainerDef)
		// containerDef);
		for (GConfigParameter parameterDef: ((GParamConfContainerDef) containerDef).gGetParameters())
		{
			List<GParameterValue> parameters = ecucModel.getSplitedParameters(owners,
				GParameterValue.class, parameterDef);
			int number = parameters.size();
			BigInteger upperMultiplicity = MultiplicityUtils.getUpperMultiplicity(parameterDef);
			if (!MultiplicityUtils.isInfinite(upperMultiplicity))
			{
				if (number > upperMultiplicity.intValue() && upperMultiplicity.intValue() == 1)
				{
					for (int i = 0; i < number - 1; i++)
					{
						IEcucCleaningAction action = new EcucCleaningActionImpl(
							MetaModelUtils.getAbsoluteQualifiedName(parameters.get(i)),
							ECleaningProblemsType.PARAMETERS_EXCEED,
							Messages.ModuleConfigurationCleaner_Parameter, new SyncActionDelete(
								parameters.get(i)));
						cleaningActions.add(action);
					}

				}
			}

		}
	}

	/**
	 * Reference values synchronization
	 * 
	 * @param owners
	 * @param containerDef
	 */
	private void performRefValuesSync(List<GContainer> owners, GContainerDef containerDef)
	{
		for (GContainer container: owners)
		{
			if (container.gGetReferenceValues() != null)
			{
				for (GConfigReferenceValue value: container.gGetReferenceValues())
				{
					if (value.gGetDefinition() == null || value.gGetDefinition().eIsProxy())
					{
						IEcucCleaningAction action = new EcucCleaningActionImpl(
							MetaModelUtils.getAbsoluteQualifiedName(container),
							ECleaningProblemsType.DEFINITION_IS_MISSING,
							Messages.ModuleConfigurationCleaner_Reference, new SyncActionDelete(
								value));
						cleaningActions.add(action);
					}
					else
					{
						GConfigReferenceValue aValue = mmService.getGenericFactory().createReferenceValue(
							value.gGetDefinition());
						if (aValue.eClass() != value.eClass())
						{
							IEcucCleaningAction action = new EcucCleaningActionImpl(
								MetaModelUtils.getAbsoluteQualifiedName(container),
								ECleaningProblemsType.DEFINITION_HAS_BEEN_CHANGED,
								Messages.ModuleConfigurationCleaner_Reference,
								new SyncActionDelete(value));
							cleaningActions.add(action);
						}

					}
				}
			}
		}
		// List<GConfigReference> revValConfList =
		// ecucModel.collectReferenceDefs((GParamConfContainerDef)
		// containerDef);
		for (GConfigReference refValueDef: ((GParamConfContainerDef) containerDef).gGetReferences())
		{
			ESplitableList<GConfigReferenceValue> references = ecucModel.getSplitedReferences(
				owners, GConfigReferenceValue.class, refValueDef);

			int number = references.size();
			BigInteger upperMultiplicity = MultiplicityUtils.getUpperMultiplicity(refValueDef);
			if (!MultiplicityUtils.isInfinite(upperMultiplicity))
			{
				if (number > upperMultiplicity.intValue() && upperMultiplicity.intValue() == 1)
				{
					for (int i = 0; i < number - 1; i++)
					{
						IEcucCleaningAction action = new EcucCleaningActionImpl(
							MetaModelUtils.getAbsoluteQualifiedName(references.get(i)),
							ECleaningProblemsType.REFERENCE_EXCEED,
							Messages.ModuleConfigurationCleaner_Reference, new SyncActionDelete(
								references.get(i)));
						cleaningActions.add(action);
					}
				}
			}
		}
	}

	/**
	 * @param containerDef
	 * @param splitedContainers
	 */
	private void syncChildren(GContainerDef containerDef,
		List<SplitedEntry<GContainer>> splitedContainers)
	{
		if (splitedContainers != null && !splitedContainers.isEmpty())
		{
			// Collection<ESplitableList<GContainer>> splitedMap =
			// splitedContainers.values();
			Collection<ESplitableList<GContainer>> splitedCollection = new ArrayList<ESplitableList<GContainer>>();
			for (SplitedEntry<GContainer> splitedEntry: splitedContainers)
			{
				splitedCollection.add(splitedEntry.getSplitableList());
			}
			performSync(splitedContainers, containerDef);
			Iterator<ESplitableList<GContainer>> iterator = splitedCollection.iterator();
			if (containerDef instanceof GParamConfContainerDef)
			{
				while (iterator.hasNext())
				{
					ESplitableList<GContainer> contList = iterator.next();
					performParametersSync(contList, containerDef);
					performRefValuesSync(contList, containerDef);
				}
			}

		}
	}

	/**
	 * @return the cleaningActions
	 */
	public List<IEcucCleaningAction> getCleaningActions()
	{
		return cleaningActions;
	}

	/**
	 * @param cleaningActions
	 *        the cleaningActions to set
	 */
	public void setCleaningActions(List<IEcucCleaningAction> cleaningActions)
	{
		this.cleaningActions = cleaningActions;
	}
}
