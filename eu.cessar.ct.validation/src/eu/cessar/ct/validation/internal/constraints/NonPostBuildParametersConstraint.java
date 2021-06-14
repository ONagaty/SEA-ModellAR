/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidg4020<br/>
 * Jun 18, 2014 2:32:41 PM
 *
 * </copyright>
 */
package eu.cessar.ct.validation.internal.constraints;

import eu.cessar.ct.core.mms.EcucMetaModelUtils;
import eu.cessar.ct.core.mms.IEcucMMService;
import eu.cessar.ct.core.mms.IMetaModelService;
import eu.cessar.ct.core.mms.MMSRegistry;
import eu.cessar.ct.core.mms.MetaModelUtils;
import eu.cessar.ct.core.mms.ecuc.EcucContainersComparator;
import eu.cessar.ct.runtime.ecuc.IEcucCore;
import eu.cessar.ct.runtime.ecuc.IEcucModel;
import eu.cessar.ct.runtime.ecuc.util.ESplitableList;
import eu.cessar.ct.runtime.ecuc.util.SplitedEntry;
import eu.cessar.ct.sdk.utils.SplitUtils;
import eu.cessar.ct.validation.internal.Messages;
import gautosar.gecucdescription.GConfigReferenceValue;
import gautosar.gecucdescription.GContainer;
import gautosar.gecucdescription.GModuleConfiguration;
import gautosar.gecucdescription.GParameterValue;
import gautosar.gecucparameterdef.GCommonConfigurationAttributes;
import gautosar.gecucparameterdef.GConfigParameter;
import gautosar.gecucparameterdef.GConfigReference;
import gautosar.gecucparameterdef.GContainerDef;
import gautosar.gecucparameterdef.GParamConfContainerDef;
import gautosar.ggenericstructure.ginfrastructure.GARObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.AbstractModelConstraint;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.ConstraintStatus;

/**
 * Constraint for asserting the way non-post build EcucParameterValues and EcucAbstractReferenceValues are set
 *
 * @author uidg4020
 *
 *
 */
public class NonPostBuildParametersConstraint extends AbstractModelConstraint
{
	/**
	 * The non post build configuration variant values for link time and pre-compile are the same on all metamodels
	 */
	private final String linkTimeVariant = "VARIANT-LINK-TIME"; //$NON-NLS-1$
	private final String preCompileVariant = "VARIANT-PRE-COMPILE"; //$NON-NLS-1$
	/**
	 * The non post build configuration config class values for link time and pre-compile are the same on all metamodels
	 */
	private final String linkConfigClass = "LINK"; //$NON-NLS-1$
	private final String preCompileConfigurationClass = "PRE-COMPILE"; //$NON-NLS-1$

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.emf.validation.AbstractModelConstraint#validate(org.eclipse.emf.validation.IValidationContext)
	 */
	@Override
	public IStatus validate(IValidationContext ctx)
	{
		EObject target = ctx.getTarget();
		IStatus result = null;
		List<IStatus> validateNonPostBuildValues = new ArrayList<>();

		if (target instanceof GContainer)
		{
			GContainer gContainer = (GContainer) target;
			// check if this container is a fragment of a splited container and obtain the merged container if
			// necesssary
			GContainer mergedInstance = SplitUtils.getMergedInstance(gContainer);
			Collection<GContainer> concreteInstances = SplitUtils.getConcreteInstances(mergedInstance);
			if (concreteInstances.size() > 1)
			{
				gContainer = mergedInstance;
			}

			GContainerDef containerDef = gContainer.gGetDefinition();
			EList<GParameterValue> gGetParameterValues = gContainer.gGetParameterValues();
			EList<GConfigReferenceValue> gGetReferenceValues = gContainer.gGetReferenceValues();

			// check for identical containers
			GParamConfContainerDef parentMultipleConfigurationContainer = EcucMetaModelUtils.getParentMultipleConfigurationContainerDef(containerDef);

			List<GContainer> identicalContainers = checkForIdenticalContainers(gContainer,
				parentMultipleConfigurationContainer);
			if (identicalContainers.size() == 0)
			{
				return null;
			}
			for (GParameterValue param: gGetParameterValues)
			{
				GConfigParameter paramDef = param.gGetDefinition();
				boolean validateGCommonConfigurationAttributes = validateGCommonConfigurationAttributes(paramDef,
					gContainer);

				if (validateGCommonConfigurationAttributes)
				{
					IStatus validateNonPostBuildAttributes = validateNonPostBuildGCommonConfigurationAttributes(
						gContainer, identicalContainers, paramDef, param, ctx, Messages.parameterErrorMessage);
					if (validateNonPostBuildAttributes != null)
					{
						validateNonPostBuildValues.add(validateNonPostBuildAttributes);
					}

				}
			}

			for (GConfigReferenceValue ref: gGetReferenceValues)
			{
				GConfigReference referenceDef = ref.gGetDefinition();
				boolean validateGCommonConfigurationAttributes = validateGCommonConfigurationAttributes(referenceDef,
					gContainer);

				if (validateGCommonConfigurationAttributes)
				{
					IStatus validateNonPostBuildAttributes = validateNonPostBuildGCommonConfigurationAttributes(
						gContainer, identicalContainers, referenceDef, ref, ctx, Messages.referenceErrorMessage);
					if (validateNonPostBuildAttributes != null)
					{
						validateNonPostBuildValues.add(validateNonPostBuildAttributes);
					}
				}
			}
			if (validateNonPostBuildValues.size() != 0)
			{
				result = ConstraintStatus.createMultiStatus(ctx, validateNonPostBuildValues);
			}
			return result;
		}
		return null;

	}

	/**
	 *
	 * @param commonConfigurationAttributes
	 *        receives a parameter definition and checks if the definition has config class 'Link time' or 'Pre Compile'
	 *        and if the container holding this parameter is located in a multiple configuration container
	 * @return
	 */
	private boolean validateGCommonConfigurationAttributes(
		GCommonConfigurationAttributes commonConfigurationAttributes, GContainer gContainer)
	{
		EObject eContainer = commonConfigurationAttributes.eContainer();
		GContainerDef containerDef = (GContainerDef) eContainer;
		boolean locatedInMultipleConfigurationContainer = EcucMetaModelUtils.isLocatedInMultipleConfigurationContainer(containerDef);
		if (!locatedInMultipleConfigurationContainer)
		{

			return false;
		}

		IProject project = MetaModelUtils.getProject(commonConfigurationAttributes);
		IMetaModelService modelService = MMSRegistry.INSTANCE.getMMService(project);
		IEcucMMService service = modelService.getEcucMMService();
		Map<String, List<String>> supportedVariants;
		try
		{
			// get config variants
			supportedVariants = service.getSupportedVariants(commonConfigurationAttributes);
		}
		catch (UnsupportedOperationException e)
		{
			// this exception is thrown on 2.1. metamodel and 3.x as there is no implementation for this method
			return false;
		}
		// check if link time or pre compile
		List<String> linkTimeList = supportedVariants.get(linkTimeVariant);
		List<String> preCompileList = supportedVariants.get(preCompileVariant);
		boolean linkTimeConfigClass = linkTimeList != null && linkTimeList.contains(linkConfigClass);
		boolean preCompileConfigClass = preCompileList != null && preCompileList.contains(preCompileConfigurationClass);

		IEcucModel ecucModel = IEcucCore.INSTANCE.getEcucModel(project);
		GModuleConfiguration moduleConfiguration = ecucModel.getModuleCfg(gContainer);
		String implementationConfigVariantAsString;

		implementationConfigVariantAsString = service.getImplementationConfigVariantAsString(moduleConfiguration);
		if (linkTimeConfigClass)
		{
			// check implementation config variant
			if (implementationConfigVariantAsString != null
				&& implementationConfigVariantAsString.equals(linkTimeVariant))
			{
				return true;
			}
		}
		if (preCompileConfigClass)
		{
			// check implementation config variant
			if (implementationConfigVariantAsString != null
				&& implementationConfigVariantAsString.equals(preCompileVariant))
			{
				return true;
			}
		}

		return false;
	}

	/**
	 * Get identical containers to the given one for the given multipleConfigurationContainer. Identical containers have
	 * the same qualified shortName path starting from the mulitpleConfigurationContainer and the same definition
	 *
	 * @param container
	 * @return
	 */
	private List<GContainer> checkForIdenticalContainers(GContainer container,
		GParamConfContainerDef multipleConfigurationContainerDef)
		{

		List<GContainer> identicalContainers = new ArrayList<>();
		GContainer parentMultipleConfigurationContainer = EcucMetaModelUtils.getParentMultipleConfigurationContainer(container);
		if (parentMultipleConfigurationContainer == null)
		{
			return Collections.EMPTY_LIST;
		}
		GContainerDef containerDef = container.gGetDefinition();

		// get all container instances with this definition
		IEcucModel ecucModel = IEcucCore.INSTANCE.getEcucModel(containerDef);
		List<SplitedEntry<GContainer>> splitedContainers = ecucModel.getSplitedContainers(containerDef);

		String commonPath = MetaModelUtils.getRelativeQName(parentMultipleConfigurationContainer, container);

		for (SplitedEntry<GContainer> splitedEntry: splitedContainers)
		{
			ESplitableList<GContainer> splitableList = splitedEntry.getSplitableList();
			GContainer possibleIdenticalContainer = null;
			if (splitableList.size() > 1)
			{
				// if we have a splited container, get the merged instance
				possibleIdenticalContainer = SplitUtils.getMergedInstance(splitableList.get(0));
			}
			else
			{
				possibleIdenticalContainer = splitableList.get(0);
			}
			// check if resides within a multiple configuration container
			GContainer parentMultipleConfigurationContainer2 = EcucMetaModelUtils.getParentMultipleConfigurationContainer(possibleIdenticalContainer);
			if (parentMultipleConfigurationContainer2 != null)
			{

				// check if multiple container definitions are the same
				GContainerDef multipleConfigContainerDef2 = parentMultipleConfigurationContainer2.gGetDefinition();
				if (multipleConfigurationContainerDef == multipleConfigContainerDef2)
				{
					// check if the same relative path to the multiple configuration container
					String commonPath2 = MetaModelUtils.getRelativeQName(parentMultipleConfigurationContainer2,
						possibleIdenticalContainer);
					if (commonPath.equals(commonPath2))
					{
						identicalContainers.add(possibleIdenticalContainer);
					}
				}
			}
		}
		// remove these container from list
		identicalContainers.remove(container);
		return identicalContainers;
		}

	/**
	 * Checks if the values of {@link GCommonConfigurationAttributes} instances are equal within the given containers
	 * and returns error status if they are not.
	 *
	 * @param container
	 * @param configurationAttributeDef
	 * @return
	 */
	private IStatus validateNonPostBuildGCommonConfigurationAttributes(GContainer container,
		List<GContainer> containers, GCommonConfigurationAttributes configurationAttributeDef,
		GARObject attributeValue, IValidationContext ctx, String errorMessage)
	{
		ConstraintStatus status = null;
		List<GContainer> problemContainers = new ArrayList<>();
		for (GContainer identicalContainer: containers)
		{
			EcucContainersComparator comparator = new EcucContainersComparator(configurationAttributeDef);
			int compare = comparator.compare(container, identicalContainer);
			// if different values in identical containers add container to the problem containers list
			if (compare != 0)
			{
				problemContainers.add(identicalContainer);
			}
		}
		if (problemContainers.size() != 0)
		{
			List<GARObject> resultLocus = new ArrayList<>();
			resultLocus.add(attributeValue);
			status = ConstraintStatus.createStatus(ctx, attributeValue, resultLocus, errorMessage, resultLocus);
		}
		return status;
	}
}
