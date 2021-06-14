/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidl6870<br/>
 * 18.04.2013 16:28:04
 *
 * </copyright>
 */
package eu.cessar.ct.validation.internal.constraints;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.artop.aal.gautosar.services.splitting.Splitable;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.validation.AbstractModelConstraint;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.ConstraintStatus;
import org.eclipse.osgi.util.NLS;
import org.eclipse.sphinx.emf.util.EcorePlatformUtil;

import eu.cessar.ct.core.mms.CessarEObjectComparator;
import eu.cessar.ct.core.mms.EObjectLookupUtils;
import eu.cessar.ct.core.mms.EcucMetaModelUtils;
import eu.cessar.ct.core.mms.IEcucMMService;
import eu.cessar.ct.core.mms.IMetaModelService;
import eu.cessar.ct.core.mms.MMSRegistry;
import eu.cessar.ct.core.mms.MetaModelUtils;
import eu.cessar.ct.core.mms.ecuc.EcucContainersComparator;
import eu.cessar.ct.core.mms.splittable.SplitableUtils;
import eu.cessar.ct.core.platform.util.StringUtils;
import eu.cessar.ct.sdk.utils.ModelUtils;
import eu.cessar.ct.validation.internal.Messages;
import eu.cessar.req.Requirement;
import gautosar.gecucdescription.GConfigReferenceValue;
import gautosar.gecucdescription.GContainer;
import gautosar.gecucdescription.GModuleConfiguration;
import gautosar.gecucdescription.GParameterValue;
import gautosar.gecucparameterdef.GCommonConfigurationAttributes;
import gautosar.gecucparameterdef.GConfigParameter;
import gautosar.gecucparameterdef.GConfigReference;
import gautosar.gecucparameterdef.GContainerDef;
import gautosar.gecucparameterdef.GParamConfContainerDef;
import gautosar.gecucparameterdef.GParamConfMultiplicity;
import gautosar.ggenericstructure.ginfrastructure.GARObject;
import gautosar.ggenericstructure.ginfrastructure.GReferrable;

/**
 * Constraint for asserting the correctness of the way a target object is split.
 *
 * @author uidl6870
 *
 *         %created_by: uidl7321 %
 *
 *         %date_created: Thu Apr 9 14:47:09 2015 %
 *
 *         %version: 9 %
 */
@Requirement(
	reqID = "28830")
public class SplittableValidityConstraint extends AbstractModelConstraint
{
	private static final String UUID_FEATURE_NAME = "uuid"; //$NON-NLS-1$

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.validation.internal.constraints.AbstractSplittableValidityConstraint#validate(org.eclipse.emf.
	 * validation .IValidationContext)
	 */
	@Override
	public IStatus validate(IValidationContext ctx)
	{
		if (!isApplicable(ctx.getTarget()))
		{
			return ctx.createSuccessStatus();
		}

		/**
		 * mapping between resources in which the fragments of the split object is stored and the respective fragments
		 */
		Map<Resource, List<? extends GReferrable>> resourceToSplitObjects = new HashMap<>();
		// populate the map
		collectPossibleFragments(resourceToSplitObjects, ctx.getTarget());

		// it happens for non-Identifiable elements (to be revised)
		if (resourceToSplitObjects.isEmpty())
		{
			return ctx.createSuccessStatus();
		}

		// target object's qualified name is unique, no need to further investigate
		if (isQualifiedNameUnique(resourceToSplitObjects))
		{
			return ctx.createSuccessStatus();
		}
		else
		{
			return doValidateTargetFragment(ctx, resourceToSplitObjects);
		}
	}

	/**
	 * Validates the target fragment against the rest of the fragments.
	 *
	 * @param ctx
	 * @param resourceToSplitObjects
	 *        mapping between resources in which the fragments of the split object are stored and the respective
	 *        fragments
	 * @return final status
	 */
	private static IStatus doValidateTargetFragment(IValidationContext ctx,
		Map<Resource, List<? extends GReferrable>> resourceToSplitObjects)
	{
		// check if the object is split in a wrong way
		IStatus status = checkSplitInSameResource(ctx, resourceToSplitObjects);
		if (!status.isOK())
		{
			return status;
		}

		// check if fragments' type match
		status = checkInstancesType(ctx, resourceToSplitObjects);
		if (!status.isOK())
		{
			return status;
		}

		IStatus splitStatus = ctx.createSuccessStatus();
		// if splitting not allowed, create an error status, but continue the check as there could be inconsistencies
		// among the fragments
		if (!Utility.allowsSplitting(ctx.getTarget()))
		{
			splitStatus = ctx.createFailureStatus(
				NLS.bind(Messages.splitted_notAllowed, new Object[] {getShortName(ctx.getTarget()),
					resourceToSplitObjects.size(), getResourceNames(resourceToSplitObjects)}));
		}

		IStatus consistencyStatus = ctx.createSuccessStatus();
		if (Utility.isEcuc(ctx))
		{
			consistencyStatus = checkConsistencyOnEcuc(ctx, resourceToSplitObjects);
		}
		else
		{
			consistencyStatus = checkConsistencyOnSystem(ctx, resourceToSplitObjects);
		}

		return combineStatuses(ctx, splitStatus, consistencyStatus);
	}

	/**
	 * @param target
	 *        EObject on which validation was requested
	 * @return the rest of split object's fragments (apart from target), against which the target will be validated
	 */
	private static List<EObject> getRestOfFragments(EObject target,
		Map<Resource, List<? extends GReferrable>> resourceToSplitObjects)
	{
		List<EObject> l = new ArrayList<>();
		for (Resource res: resourceToSplitObjects.keySet())
		{
			List<? extends GReferrable> list = resourceToSplitObjects.get(res);

			Iterator<? extends GReferrable> iterator = list.iterator();
			while (iterator.hasNext())
			{
				GReferrable fragment = iterator.next();
				if (target == fragment)
				{
					continue;
				}
				l.add(fragment);
			}
		}
		return l;
	}

	/**
	 * @return whether the qualified name of the target object is unique, by checking the internal map
	 */
	private static boolean isQualifiedNameUnique(Map<Resource, List<? extends GReferrable>> resourceToSplitObjects)
	{
		int size = resourceToSplitObjects.size();
		// only one resource
		if (size == 1)
		{
			// only one object
			Set<Resource> keySet = resourceToSplitObjects.keySet();
			List<? extends GReferrable> list = resourceToSplitObjects.get(keySet.iterator().next());
			if (list.size() == 1)
			{
				return true;
			}
		}

		return false;
	}

	/**
	 * @param ctx
	 * @param resourceToSplitObjects
	 *        mapping between resources in which the fragments of the split object are stored and the respective
	 *        fragments
	 * @return {@link Status#OK_STATUS} or a failure status if there is more than one element with same qualified name
	 *         in target's resource
	 */
	private static IStatus checkSplitInSameResource(IValidationContext ctx,
		Map<Resource, List<? extends GReferrable>> resourceToSplitObjects)
	{
		EObject target = ctx.getTarget();
		List<? extends GReferrable> list = resourceToSplitObjects.get(target.eResource());
		// several objects in the same resource found
		if (list != null && list.size() > 1)
		{
			String qualifiedName = getShortName(target);
			return ctx.createFailureStatus(
				NLS.bind(Messages.splitted_sameResource, qualifiedName, getResourceName(target)));
		}

		return Status.OK_STATUS;
	}

	/**
	 * @param ctx
	 * @param resourceToSplitObjects
	 *        mapping between resources in which the fragments of the split object are stored and the respective
	 *        fragments
	 * @return {@link Status#OK_STATUS} if the fragments have the same type or a failure status with a proper message
	 *         otherwise; could be a multi-status
	 */
	private static IStatus checkInstancesType(IValidationContext ctx,
		Map<Resource, List<? extends GReferrable>> resourceToSplitObjects)
	{
		List<IStatus> errStatuses = new ArrayList<>();

		EObject target = ctx.getTarget();
		EClass targetType = target.eClass();
		for (EObject fragment: getRestOfFragments(target, resourceToSplitObjects))
		{
			if (targetType != fragment.eClass())
			{
				IStatus errStatus = ctx.createFailureStatus(NLS.bind(Messages.splitted_differentEClass,
					new Object[] {getShortName(target), getResourceName(target), getResourceName(fragment)}));
				errStatuses.add(errStatus);

			}
		}

		return getResultingStatus(ctx, errStatuses);
	}

	/**
	 * @param ctx
	 * @param errStatuses
	 * @return {@link Status#OK_STATUS} if <code>errStatuses</code> list is empty; otherwise, a multi-status
	 */
	private static IStatus getResultingStatus(IValidationContext ctx, List<IStatus> errStatuses)
	{
		int errSize = errStatuses.size();
		if (errSize == 1)
		{
			return errStatuses.get(0);
		}
		else if (errSize > 1)
		{
			return ConstraintStatus.createMultiStatus(ctx, errStatuses);
		}

		return Status.OK_STATUS;
	}

	private static IStatus combineStatuses(IValidationContext ctx, IStatus status1, IStatus status2)
	{
		IStatus finalStatus = ctx.createSuccessStatus();
		if (status1.isOK())
		{
			finalStatus = status2;
		}
		else
		{
			if (status2.isOK())
			{
				finalStatus = status1;
			}
			else
			{
				List<IStatus> statuses = new ArrayList<>();
				statuses.add(status1);
				statuses.add(status2);
				finalStatus = ConstraintStatus.createMultiStatus(ctx, statuses);
			}
		}

		return finalStatus;
	}

	/**
	 *
	 * @param object
	 * @return the name of the <code>object</code>'s resource
	 */
	private static String getResourceName(EObject object)
	{
		return object.eResource().getURI().lastSegment();
	}

	/**
	 * @return comma concatenated resources' name (in alphabetical order)
	 */
	private static String getResourceNames(Map<Resource, List<? extends GReferrable>> resourceToSplitObjects)
	{
		List<String> names = new ArrayList<>();
		Set<Resource> set = resourceToSplitObjects.keySet();

		for (Resource res: set)
		{
			names.add(res.getURI().lastSegment());
		}
		Collections.sort(names);

		String resourceNames = StringUtils.concat(names.toArray(new String[names.size()]), ", "); //$NON-NLS-1$
		return resourceNames;
	}

	/**
	 * @param obj
	 *        element to be checked
	 * @return whether the element is of GReferrable type
	 */
	private static boolean isApplicable(EObject obj)
	{
		return obj instanceof GReferrable && obj instanceof Splitable;
	}

	/**
	 * @param map
	 *        map to be populated
	 *
	 * @param target
	 *        EObject's whose merged object's fragments (itself included) are to be collected in the provided map
	 */
	private static void collectPossibleFragments(Map<Resource, List<? extends GReferrable>> map, EObject target)
	{
		IProject project = getProject(target);

		Collection<Resource> resources = EcorePlatformUtil.getResourcesInModels(project,
			MetaModelUtils.getAutosarRelease(project), false);
		String qualifiedName = ModelUtils.getAbsoluteQualifiedName(target);
		for (Resource resource: resources)
		{
			List<GReferrable> eObjectsWithQualifiedName = new ArrayList<>();

			List<EObject> list = EObjectLookupUtils.getEObjectsWithQName(resource, qualifiedName);
			for (EObject eObject: list)
			{
				if (eObject instanceof GReferrable)
				{
					eObjectsWithQualifiedName.add((GReferrable) eObject);
				}
			}

			if (!eObjectsWithQualifiedName.isEmpty())
			{
				map.put(resource, eObjectsWithQualifiedName);
			}
		}
	}

	/**
	 * @param eObject
	 * @return passed object's short name
	 */
	private static String getShortName(EObject eObject)
	{
		GReferrable ref = ((GReferrable) eObject);
		return ref.gGetShortName();
	}

	/**
	 * @param object
	 * @return the project
	 */
	private static IProject getProject(Object object)
	{
		if (object instanceof IContainer)
		{
			return ((IContainer) object).getProject();
		}
		else
		{
			IFile file = EcorePlatformUtil.getFile(object);
			if (file != null)
			{
				return file.getProject();
			}
			else
			{
				return null;
			}
		}
	}

	/**
	 * Perform consistency check validation between the target fragment and the rest of the fragments, at feature level
	 *
	 * @param ctx
	 * @param resourceToSplitObjects
	 *        mapping between resources in which the fragments of the split object are stored and the respective
	 *        fragments
	 * @return status indicating the consistency at feature level
	 */
	private static IStatus checkConsistencyOnSystem(IValidationContext ctx,
		Map<Resource, List<? extends GReferrable>> resourceToSplitObjects)
	{
		List<IStatus> errStatuses = new ArrayList<>();

		EObject target = ctx.getTarget();
		for (EObject fragment: getRestOfFragments(target, resourceToSplitObjects))
		{
			for (EStructuralFeature feat: fragment.eClass().getEAllStructuralFeatures())
			{
				if (!Utility.shallValidateFeature(feat))
				{
					continue;
				}

				if (!target.eIsSet(feat) || !fragment.eIsSet(feat))
				{
					continue;
				}

				if (Utility.getComparator(feat).compare(target, fragment) != 0)
				{
					String featName = StringUtils.toTitleCase(feat.getName());
					String errMsg = NLS.bind(Messages.splitted_inconsistentValues_System, new Object[] {
						getShortName(target), getResourceName(target), getResourceName(fragment), featName});
					errStatuses.add(ctx.createFailureStatus(errMsg));
				}
			}
		}

		return getResultingStatus(ctx, errStatuses);
	}

	/**
	 * Perform Ecuc specific verifications, at definition and at parameter and reference definition level
	 *
	 * @param ctx
	 * @param resourceToSplitObjects
	 *        mapping between resources in which the fragments of the split object are stored and the respective
	 *        fragments
	 * @return
	 */
	private static IStatus checkConsistencyOnEcuc(IValidationContext ctx,
		Map<Resource, List<? extends GReferrable>> resourceToSplitObjects)
	{
		IStatus status = doValidateDefinition(ctx, resourceToSplitObjects);
		if (!status.isOK())
		{
			return status;
		}

		if (Utility.isTargetContainer(ctx))
		{
			status = doValidateContainer(ctx, (GContainer) ctx.getTarget(), resourceToSplitObjects);
		}

		return status;

	}

	/**
	 * Checks the consistency between target container against the other fragments, at parameter and reference values
	 * level
	 *
	 * @param ctx
	 * @param target
	 * @param resourceToSplitObjects
	 *        mapping between resources in which the fragments of the split object are stored and the respective
	 *        fragments
	 * @return the status, could be a multi-status
	 */
	private static IStatus doValidateContainer(IValidationContext ctx, GContainer target,
		Map<Resource, List<? extends GReferrable>> resourceToSplitObjects)
	{
		List<IStatus> errStatuses = new ArrayList<>();

		GContainerDef definition = target.gGetDefinition();
		if (definition instanceof GParamConfContainerDef)
		{
			collectReferenceConsistencyErrors(errStatuses, ctx, target, resourceToSplitObjects);

			collectParameterConsistencyErrors(errStatuses, ctx, target, resourceToSplitObjects);
		}

		return getResultingStatus(ctx, errStatuses);
	}

	/**
	 * Checks the consistency at parameter values level, between target fragment and the other fragments
	 *
	 * @param errStatuses
	 *        list where to collect the problems found
	 * @param ctx
	 * @param target
	 *        target fragment
	 * @param resourceToSplitObjects
	 *        mapping between resources in which the fragments of the split object are stored and the respective
	 *        fragments
	 */
	private static void collectParameterConsistencyErrors(List<IStatus> errStatuses, IValidationContext ctx,
		GContainer target, Map<Resource, List<? extends GReferrable>> resourceToSplitObjects)
	{
		GContainerDef definition = target.gGetDefinition();

		EList<GConfigParameter> paramDefs = ((GParamConfContainerDef) definition).gGetParameters();
		for (GConfigParameter paramDef: paramDefs)
		{
			if (!Utility.shallValidateAttribute(paramDef))
			{
				continue;
			}
			// before proceeding with the comparison, check whether target has the parameter set
			if (!Utility.isSetParameter(target, paramDef))
			{
				continue;
			}

			EcucContainersComparator comparator = new EcucContainersComparator(paramDef);
			List<EObject> restOfFragments = getRestOfFragments(target, resourceToSplitObjects);
			for (int i = 0; i < restOfFragments.size(); i++)
			{
				GContainer fragmentA = (GContainer) restOfFragments.get(i);
				if (!Utility.isSetParameter(target, paramDef))
				{
					continue;
				}

				for (int j = i + 1; j < restOfFragments.size(); j++)
				{
					GContainer fragmentB = (GContainer) restOfFragments.get(j);

					if (comparator.compare(fragmentB, fragmentA) != 0)
					{
						String errMsg = NLS.bind(Messages.splitted_inconsistentValues_Ecuc_param,
							new Object[] {getShortName(target), getResourceName(fragmentB), getResourceName(fragmentA),
								paramDef.gGetShortName()});
						errStatuses.add(ctx.createFailureStatus(errMsg));
					}
				}
			}
		}
	}

	/**
	 * Checks the consistency at reference values level, between target fragment and the other fragments
	 *
	 * @param errStatuses
	 *        list where to collect the problems found
	 * @param ctx
	 * @param target
	 *        target fragment
	 * @param resourceToSplitObjects
	 *        mapping between resources in which the fragments of the split object are stored and the respective
	 *        fragments
	 */
	private static void collectReferenceConsistencyErrors(List<IStatus> errStatuses, IValidationContext ctx,
		GContainer target, Map<Resource, List<? extends GReferrable>> resourceToSplitObjects)
	{
		GContainerDef definition = target.gGetDefinition();
		EList<GConfigReference> referenceDefs = ((GParamConfContainerDef) definition).gGetReferences();
		for (GConfigReference referenceDef: referenceDefs)
		{
			if (!Utility.shallValidateAttribute(referenceDef))
			{
				continue;
			}

			// before proceeding with the comparison, check whether target has the reference set
			if (!Utility.isSetReference(target, referenceDef))
			{
				continue;
			}

			Comparator<GContainer> comparator = Utility.getComparator(referenceDef);
			List<EObject> restOfFragments = getRestOfFragments(target, resourceToSplitObjects);
			for (int i = 0; i < restOfFragments.size(); i++)
			{
				GContainer fragmentA = (GContainer) restOfFragments.get(i);
				if (!Utility.isSetReference(fragmentA, referenceDef))
				{
					continue;
				}

				for (int j = i + 1; j < restOfFragments.size(); j++)
				{
					GContainer fragmentB = (GContainer) restOfFragments.get(j);

					if (comparator.compare(fragmentB, fragmentA) != 0)
					{
						String errMsg = NLS.bind(Messages.splitted_inconsistentValues_Ecuc_ref,
							new Object[] {getShortName(target), getResourceName(fragmentB), getResourceName(fragmentA),
								referenceDef.gGetShortName()});
						errStatuses.add(ctx.createFailureStatus(errMsg));
					}
				}
			}
		}
	}

	/**
	 * Checks the definition of the target Ecuc element against the other fragments' definition
	 *
	 * @param ctx
	 * @param resourceToSplitObjects
	 *        mapping between resources in which the fragments of the split object are stored and the respective
	 *        fragments
	 * @return the status, could be a multi-status
	 */
	private static IStatus doValidateDefinition(IValidationContext ctx,
		Map<Resource, List<? extends GReferrable>> resourceToSplitObjects)
	{
		EObject target = ctx.getTarget();

		List<IStatus> errStatuses = new ArrayList<>();
		for (EObject fragment: getRestOfFragments(target, resourceToSplitObjects))
		{
			if (target == fragment)
			{
				continue;
			}

			IStatus defStatus = checkDefinition(ctx, target, fragment);
			if (!defStatus.isOK())
			{
				errStatuses.add(defStatus);
			}
		}

		return getResultingStatus(ctx, errStatuses);
	}

	/**
	 * Asserts that the definition of the given Ecuc fragments match.
	 *
	 * @param targetFragment
	 *        the fragment object being validated
	 * @param fragment
	 *        the fragment object to check against
	 * @return the status
	 */
	private static IStatus checkDefinition(IValidationContext ctx, EObject targetFragment, EObject fragment)
	{
		IStatus status = Status.OK_STATUS;
		GARObject targetDef = EcucMetaModelUtils.getDefinition(targetFragment);
		GARObject fragmentDef = EcucMetaModelUtils.getDefinition(fragment);

		if (targetDef == null || fragmentDef == null || (targetFragment instanceof GARObject
			&& !SplitableUtils.INSTANCE.hasDefinition((GARObject) targetFragment, fragmentDef)))
		{
			String errMsg = NLS.bind(Messages.splitted_differentDefinition, new Object[] {getShortName(targetFragment),
				getResourceName(targetFragment), getResourceName(fragment)});

			status = ctx.createFailureStatus(errMsg);
		}

		return status;
	}

	/**
	 * @param target
	 * @return the meta-model service to be used
	 */
	private static IMetaModelService getMMService(EObject target)
	{
		IMetaModelService mmService = MMSRegistry.INSTANCE.getMMService(getProject(target));
		return mmService;
	}

	private final static class Utility
	{
		private static Comparator<EObject> getComparator(EStructuralFeature feat)
		{
			return new CessarEObjectComparator(feat);
		}

		private static Comparator<GContainer> getComparator(GConfigReference referenceDef)
		{
			return new EcucContainersComparator(referenceDef);
		}

		private static boolean isSetParameter(GContainer c, GConfigParameter def)
		{
			EList<GParameterValue> parameterValues = c.gGetParameterValues();
			for (GParameterValue parameterValue: parameterValues)
			{
				if (SplitableUtils.INSTANCE.hasDefinition(parameterValue, def))
				{
					return true;
				}
			}
			return false;
		}

		private static boolean isSetReference(GContainer c, GConfigReference def)
		{
			EList<GConfigReferenceValue> referenceValues = c.gGetReferenceValues();
			for (GConfigReferenceValue referenceValue: referenceValues)
			{
				if (SplitableUtils.INSTANCE.hasDefinition(referenceValue, def))
				{
					return true;
				}
			}

			return false;
		}

		/**
		 * @return whether the target EObject is a module configuration or a container
		 */
		private static boolean isEcuc(IValidationContext ctx)
		{
			EObject target = ctx.getTarget();
			return target instanceof GModuleConfiguration || target instanceof GContainer;
		}

		private static boolean isTargetContainer(IValidationContext ctx)
		{
			EObject target = ctx.getTarget();
			return target instanceof GContainer;
		}

		/**
		 * @param target
		 *        EObject to be checked
		 * @return whether the given EObject's EClass allows splitting
		 */
		private static boolean allowsSplitting(EObject target)
		{
			boolean allowsSplitting = getMMService(target).isSplitable(target.eClass());

			return allowsSplitting;
		}

		/**
		 * Decides whether validation of attribute values of the given definition is needed.
		 *
		 * @param attr
		 *        definition of a parameter/references values
		 * @return <code>true</code> in one of the cases:
		 *         <li>1) splitting of a container's parameter/references' list not allowed
		 *         <li>2) splitting of the list is allowed, but the multiplicity allows only one value; <br>
		 *         <code>false</code> otherwise
		 */
		private static boolean shallValidateAttribute(GCommonConfigurationAttributes attr)
		{
			IMetaModelService mmService = getMMService(attr);
			IEcucMMService ecucMMService = mmService.getEcucMMService();

			EReference feature = null;
			if (attr instanceof GConfigParameter)
			{
				feature = ecucMMService.getParameterValuesFeature();
			}
			else
			{
				feature = ecucMMService.getReferenceValuesFeature();
			}

			boolean allowSplitting = mmService.isSplitableReference(feature);

			BigInteger upper = mmService.getEcucMMService().getUpperMultiplicity((GParamConfMultiplicity) attr,
				BigInteger.ONE, true);
			boolean isMulti = (upper.compareTo(new BigInteger("1")) != 0); //$NON-NLS-1$

			if (allowSplitting && isMulti)
			{
				return false;
			}

			return true;
		}

		/**
		 * @param feat
		 * @return whether passed feature is of interest
		 */
		@Requirement(
			reqID = "287992")
		private static boolean shallValidateFeature(EStructuralFeature feat)
		{
			if (feat.isTransient() || feat.isVolatile())
			{
				return false;
			}
			if (feat instanceof EReference && ((EReference) feat).isContainment())
			{
				return false;
			}

			// we want to UUID feature not to be validated as it has not (depending on interpretation of the AUTOSAR
			// specification) to be unique in the merged model. (see requirement 287992)
			if (feat.getName().equals(UUID_FEATURE_NAME))
			{
				return false;
			}

			return true;
		}
	}
}
