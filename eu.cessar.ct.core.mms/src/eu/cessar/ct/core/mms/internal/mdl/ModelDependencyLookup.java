/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458<br/>
 * 21.05.2013 14:17:26
 * 
 * </copyright>
 */
package eu.cessar.ct.core.mms.internal.mdl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.FeatureMap;

import eu.cessar.ct.core.mms.EResourceUtils;
import eu.cessar.ct.core.mms.internal.Messages;
import eu.cessar.ct.core.mms.mdl.IModelDependencyLookup2;
import eu.cessar.ct.sdk.utils.MDLFilterAcceptAll;
import eu.cessar.req.Requirement;

/**
 * Implements methods specified by {@code IModelDependencyMatrix} that retrieve information from an EMF model based on
 * the references between its objects.
 * 
 * The implemented functionality includes the retrieval of parents and children linked via containment references and
 * the retrieval of starting points and end points linked via any type of references based on their EClass, and allows
 * tuning of the search process by specifying the maximum allowed number of references to follow and by supplying an
 * acceptance filter for the next search step.
 * 
 * @author uidl6458
 * 
 *         %created_by: uidg4020 %
 * 
 *         %date_created: Mon Dec  9 15:38:23 2013 %
 * 
 *         %version: 4 %
 */
public final class ModelDependencyLookup implements IModelDependencyLookup2
{
	/**
	 * Filter that always accepts the candidate path.
	 */
	private static final IMDLFilter ACCEPT_ALL = new MDLFilterAcceptAll();

	/**
	 * Computes and offers access to model dependency graph information.
	 */
	private IModelDependencyMatrix mdm;

	/**
	 * Default constructor.
	 */
	private ModelDependencyLookup()
	{

	}

	/**
	 * Construct a ModelDependencyLookup from a list of {@code EClass}es.
	 * 
	 * @param classes
	 *        the {@code EClass}es
	 */
	private ModelDependencyLookup(List<EClass> classes)
	{
		this();
		init(classes);
	}

	/**
	 * Construct a ModelDependencyLookup object from a collection of {@code EClassifier}s.
	 * 
	 * @param mmClasses
	 *        the collection of {@code EClassifier}s
	 * @return the ModelDependencyLookup
	 */
	public static ModelDependencyLookup fromClassifiers(Collection<EClassifier> mmClasses)
	{
		List<EClass> lst = new ArrayList<EClass>();

		for (EClassifier classifier: mmClasses)
		{
			if (classifier instanceof EClass)
			{
				lst.add((EClass) classifier);
			}
		}

		return new ModelDependencyLookup(lst);
	}

	/**
	 * @param classes
	 */
	private void init(List<EClass> classes)
	{
		mdm = new ModelDependencyMatrix(classes);
	}

	private static <T> List<T> removeDuplicates(List<T> result)
	{
		Set<T> lhs = new LinkedHashSet<T>();
		lhs.addAll(result);
		result.clear();
		result.addAll(lhs);
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.core.mms.IModelDependencyMatrix#getParent(org.eclipse.emf.ecore.EObject,
	 * org.eclipse.emf.ecore.EClass)
	 */
	@Override
	public EObject getParent(EObject childObject, EClass parentClass)
	{
		return getParent(childObject, parentClass, INFINITE_HOPS);
	}

	private static EObject getParentInfiniteHops(EObject childObject, EClass parentClass)
	{
		EObject current = childObject;

		do
		{
			current = current.eContainer();

			if (null == current)
			{
				return null;
			}
		}
		while (!parentClass.isInstance(current));

		return current;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.core.mms.IModelDependencyMatrix#getParent(org.eclipse.emf.ecore.EObject,
	 * org.eclipse.emf.ecore.EClass, eu.cessar.ct.core.mms.IModelDependencyMatrix.IMDLFilter, int)
	 */
	@Requirement(
		reqID = "REQ_API_MDL#1")
	@Override
	public EObject getParent(EObject childObject, EClass parentClass, int maxHops)
	{
		EObject current = childObject;
		int hops = maxHops;

		if (null == childObject)
		{
			throw new IllegalArgumentException(Messages.MDL_NULL_CHILD_OBJECT);
		}

		if (null == parentClass)
		{
			throw new IllegalArgumentException(Messages.MDL_NULL_PARENT_CLASS);
		}

		if (!mdm.mayContain(parentClass, childObject.eClass()))
		{
			return null;
		}

		if (0 >= hops)
		{
			// by convention, infinite hops
			return getParentInfiniteHops(childObject, parentClass);
		}

		do
		{
			current = current.eContainer();

			hops--;

			if ((0 > hops) || (null == current))
			{
				return null;
			}
		}
		while (!parentClass.isInstance(current));

		return current;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.core.mms.IModelDependencyMatrix#getChildren(org.eclipse.emf.ecore.EObject,
	 * org.eclipse.emf.ecore.EClass)
	 */
	@Override
	public List<EObject> getChildren(EObject parentObject, EClass childClass)
	{
		return getChildren(parentObject, childClass, INFINITE_HOPS);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.core.mms.IModelDependencyMatrix#getChildren(org.eclipse.emf.ecore.EObject,
	 * org.eclipse.emf.ecore.EClass, int)
	 */
	@Override
	public List<EObject> getChildren(EObject parentObject, EClass childClass, int maxHops)
	{
		return getChildren(parentObject, childClass, maxHops, ACCEPT_ALL);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.core.mms.mdm.IModelDependencyLookup#getChildren(org.eclipse.emf.ecore.EObject,
	 * org.eclipse.emf.ecore.EClass, eu.cessar.ct.core.mms.mdm.IModelDependencyLookup.IMDLFilter)
	 */
	@Override
	public List<EObject> getChildren(EObject parentObject, EClass childClass, IMDLFilter filter)
	{
		return getChildren(parentObject, childClass, INFINITE_HOPS, filter);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.core.mms.IModelDependencyMatrix#getChildren(org.eclipse.emf.ecore.EObject,
	 * org.eclipse.emf.ecore.EClass, eu.cessar.ct.core.mms.IModelDependencyMatrix.IMDLFilter, int)
	 */
	@Requirement(
		reqID = "REQ_API_MDL#2")
	@Override
	public List<EObject> getChildren(EObject parentObject, EClass childClass, int maxHops, IMDLFilter filter)
	{
		return getReferredEObjects(parentObject, childClass, maxHops, filter, MDLEligibility.CONTAINMENT);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.core.mms.IModelDependencyLookup#getChildren(org.eclipse.emf.ecore.resource.Resource,
	 * org.eclipse.emf.ecore.EClass)
	 */
	@Override
	public List<EObject> getChildren(Resource resource, EClass childClass)
	{
		return getChildren(resource, childClass, INFINITE_HOPS);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.core.mms.IModelDependencyLookup#getChildren(org.eclipse.emf.ecore.resource.Resource,
	 * org.eclipse.emf.ecore.EClass, int)
	 */
	@Override
	public List<EObject> getChildren(Resource resource, EClass childClass, int maxHops)
	{
		return getChildren(resource, childClass, INFINITE_HOPS, ACCEPT_ALL);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.core.mms.IModelDependencyLookup#getChildren(org.eclipse.emf.ecore.resource.Resource,
	 * org.eclipse.emf.ecore.EClass, eu.cessar.ct.core.mms.IModelDependencyLookup.IMDLFilter)
	 */
	@Override
	public List<EObject> getChildren(Resource resource, EClass childClass, IMDLFilter filter)
	{
		return getChildren(resource, childClass, INFINITE_HOPS, filter);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.core.mms.IModelDependencyLookup#getChildren(org.eclipse.emf.ecore.resource.Resource,
	 * org.eclipse.emf.ecore.EClass, int, eu.cessar.ct.core.mms.IModelDependencyLookup.IMDLFilter)
	 */
	@Override
	public List<EObject> getChildren(Resource resource, EClass childClass, int maxHops, IMDLFilter filter)
	{
		List<EObject> result = new ArrayList<EObject>();
		for (EObject topObject: resource.getContents())
		{
			result.addAll(getChildren(topObject, childClass, maxHops, filter));
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.core.mms.IModelDependencyLookup#getChildren(org.eclipse.core.resources.IProject,
	 * org.eclipse.emf.ecore.EClass)
	 */
	@Override
	public List<EObject> getChildren(IProject project, EClass childClass)
	{
		return getChildren(project, childClass, INFINITE_HOPS);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.core.mms.IModelDependencyLookup#getChildren(org.eclipse.core.resources.IProject,
	 * org.eclipse.emf.ecore.EClass, int)
	 */
	@Override
	public List<EObject> getChildren(IProject project, EClass childClass, int maxHops)
	{
		return getChildren(project, childClass, maxHops, ACCEPT_ALL);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.core.mms.IModelDependencyLookup#getChildren(org.eclipse.core.resources.IProject,
	 * org.eclipse.emf.ecore.EClass, eu.cessar.ct.core.mms.IModelDependencyLookup.IMDLFilter)
	 */
	@Override
	public List<EObject> getChildren(IProject project, EClass childClass, IMDLFilter filter)
	{
		return getChildren(project, childClass, INFINITE_HOPS, filter);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.core.mms.IModelDependencyLookup#getChildren(org.eclipse.core.resources.IProject,
	 * org.eclipse.emf.ecore.EClass, int, eu.cessar.ct.core.mms.IModelDependencyLookup.IMDLFilter)
	 */
	@Override
	public List<EObject> getChildren(IProject project, EClass childClass, int maxHops, IMDLFilter filter)
	{
		Collection<Resource> resources = EResourceUtils.getProjectResources(project);
		List<EObject> result = new ArrayList<EObject>();
		for (Resource resource: resources)
		{
			result.addAll(getChildren(resource, childClass, maxHops, filter));
		}
		return result;
	}

	/**
	 * Check arguments of the
	 * {@link ModelDependencyLookup#getReferredEObjects(EObject, EClass, int, eu.cessar.ct.sdk.utils.IModelDependencyLookup.IMDLFilter, MDLEligibility)}
	 * method.
	 * 
	 * @param parentObject
	 *        the parent/source {@code EObject}
	 * @param childClass
	 *        the child/destination {@code EClass}
	 * @param filter
	 *        the acceptance {@code IMDLFilter}
	 * @param eligibility
	 *        the containment/non-containment eligibility
	 */
	private static void checkGetReferredObjectsArgs(EObject parentObject, EClass childClass, IMDLFilter filter,
		MDLEligibility eligibility)
	{
		if (null == parentObject)
		{
			throw new IllegalArgumentException(
				eligibility == MDLEligibility.CONTAINMENT ? Messages.MDL_NULL_PARENT_OBJECT
					: Messages.MDL_NULL_FROM_OBJECT);
		}

		if (null == childClass)
		{
			throw new IllegalArgumentException(
				eligibility == MDLEligibility.CONTAINMENT ? Messages.MDL_NULL_CHILD_CLASS : Messages.MDL_NULL_TO_CLASS);
		}

		if (null == filter)
		{
			throw new IllegalArgumentException(Messages.MDL_NULL_FILTER);
		}
	}

	/**
	 * Check arguments of the
	 * {@link ModelDependencyLookup#anyRefer(EObject, EClass, int, eu.cessar.ct.sdk.utils.IModelDependencyLookup.IMDLFilter, EObject)}
	 * method.
	 * 
	 * @param parentObject
	 *        the parent/source {@code EObject}
	 * @param childClass
	 *        the child/destination {@code EClass}
	 * @param filter
	 *        the acceptance {@code IMDLFilter}
	 * @param toObject
	 *        the toObject
	 */
	private static void checkGetBackwardReferencesArgs(EObject parentObject, EClass fromClass, IMDLFilter filter,
		EObject toObject)
	{
		checkGetReferredObjectsArgs(parentObject, fromClass, filter, MDLEligibility.NON_CONTAINMENT);

		if (null == toObject)
		{
			throw new IllegalArgumentException(Messages.MDL_NULL_TO_OBJECT);
		}
	}

	/**
	 * Get the target objects of the {@code readRef} reference.
	 * 
	 * @param readRef
	 *        the reference
	 * @return the non-null, possibly empty, list of {@code EObject} targets
	 */
	private static List<EObject> getNewTargets(Object readRef)
	{
		List<EObject> targets = Collections.EMPTY_LIST;
		if (null != readRef)
		{
			if (readRef instanceof List)
			{
				targets = (List<EObject>) readRef;
			}
			else if (readRef instanceof EObject)
			{
				targets = new ArrayList<EObject>(1);
				targets.add((EObject) readRef);
			}
			else if (readRef instanceof FeatureMap)
			{
				// TODO: handle feature maps, ignore for now
			}
			else
			{
				// TODO: handle unknown ref type, ignore for now
			}
		}
		return targets;
	}

	/**
	 * Get the referred objects (both containment and non-containment).
	 * 
	 * Used by {@code getChildren} and {@code getForwardReferences}.
	 * 
	 * @param parentObject
	 *        the parent/source {@code EObject}
	 * @param childClass
	 *        the child/destination {@code EClass}
	 * @param maxHops
	 *        the maximum number of hops (references) to follow
	 * @param filter
	 *        the acceptance {@code IMDLFilter}
	 * @param eligibility
	 *        the containment/non-containment eligibility
	 * @return the non-null, possibly empty, list of referred {@code EObject}s.
	 */
	private List<EObject> getReferredEObjects(EObject parentObject, EClass childClass, int maxHops, IMDLFilter filter,
		MDLEligibility eligibility)
	{
		checkGetReferredObjectsArgs(parentObject, childClass, filter, eligibility);

		boolean infiniteHops = (maxHops <= 0);
		int currentHops = 0;

		List<EObject> result = new ArrayList<EObject>();

		List<EObject> currentParents = new ArrayList<EObject>();
		currentParents.add(parentObject);

		Set<EObject> visited = new HashSet<EObject>();

		do
		{
			List<EObject> currentChildren = new ArrayList<EObject>();

			for (EObject currentParent: currentParents)
			{
				EClass currentParentClass = currentParent.eClass();
				List<EReference> refs = mdm.getReferences(currentParentClass, childClass, eligibility);
				for (EReference ref: refs)
				{
					Object readRef = currentParent.eGet(ref);
					List<EObject> targets = getNewTargets(readRef);

					for (EObject target: targets)
					{
						if ((MDLEligibility.CONTAINMENT != eligibility) && (visited.contains(target)))
						{
							continue;
						}

						visited.add(target);

						if (filter.accept(currentParent, ref, target, currentHops))
						{
							if (childClass.isInstance(target))
							{
								result.add(target);
							}

							currentChildren.add(target);
						}
					}
				}
			}

			currentParents = currentChildren;

			currentHops++;
		}
		while (!currentParents.isEmpty() && (infiniteHops || (currentHops < maxHops)));

		return result;
	}

	/**
	 * Determines if {@code parentObject} has any children (ancestors) of {@code EClass} {@code childClass} that refer
	 * {@code toObject} via at most {@code maxHops} non-containment references, considering the acceptance
	 * {@code filter}.
	 * 
	 * @param parentObject
	 *        the parent object
	 * @param childClass
	 *        the child class
	 * @param maxHops
	 *        the maximum number of non-containment references
	 * @param filter
	 *        the acceptance {@code IMDLFilter}
	 * @param toObject
	 *        the object that may be referred
	 * @return
	 */
	private boolean anyRefer(EObject parentObject, EClass childClass, int maxHops, IMDLFilter filter, EObject toObject)
	{
		boolean infiniteHops = (maxHops <= 0);
		int currentHops = 0;

		List<EObject> currentParents = new ArrayList<EObject>();
		currentParents.add(parentObject);

		Set<EObject> visited = new HashSet<EObject>();

		do
		{
			List<EObject> currentChildren = new ArrayList<EObject>();

			for (EObject currentParent: currentParents)
			{
				EClass currentParentClass = currentParent.eClass();
				List<EReference> refs = mdm.getReferences(currentParentClass, childClass,
					MDLEligibility.NON_CONTAINMENT);
				for (EReference ref: refs)
				{
					Object readRef = currentParent.eGet(ref);
					List<EObject> targets = getNewTargets(readRef);

					for (EObject target: targets)
					{
						if (visited.contains(target))
						{
							continue;
						}

						visited.add(target);

						if (filter.accept(currentParent, ref, target, currentHops))
						{
							if (childClass.isInstance(target))
							{
								if (toObject.equals(target))
								{
									return true;
								}
							}

							currentChildren.add(target);
						}
					}
				}
			}

			currentParents = currentChildren;

			currentHops++;
		}
		while (!currentParents.isEmpty() && (infiniteHops || (currentHops < maxHops)));

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.core.mms.IModelDependencyMatrix#getForwardReferences(org.eclipse.emf.ecore.EObject,
	 * org.eclipse.emf.ecore.EClass)
	 */
	@Override
	public List<EObject> getForwardReferences(EObject fromObject, EClass toClass)
	{
		return getForwardReferences(fromObject, toClass, INFINITE_HOPS);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.core.mms.IModelDependencyMatrix#getForwardReferences(org.eclipse.emf.ecore.EObject,
	 * org.eclipse.emf.ecore.EClass, int)
	 */
	@Override
	public List<EObject> getForwardReferences(EObject fromObject, EClass toClass, int maxHops)
	{
		return getForwardReferences(fromObject, toClass, maxHops, ACCEPT_ALL);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.core.mms.mdm.IModelDependencyLookup#getForwardReferences(org.eclipse.emf.ecore.EObject,
	 * org.eclipse.emf.ecore.EClass, eu.cessar.ct.core.mms.mdm.IModelDependencyLookup.IMDLFilter)
	 */
	@Override
	public List<EObject> getForwardReferences(EObject fromObject, EClass toClass, IMDLFilter filter)
	{
		return getForwardReferences(fromObject, toClass, INFINITE_HOPS, filter);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.core.mms.IModelDependencyMatrix#getForwardReferences(org.eclipse.emf.ecore.EObject,
	 * org.eclipse.emf.ecore.EClass, eu.cessar.ct.core.mms.IModelDependencyMatrix.IMDLFilter, int)
	 */
	@Override
	public List<EObject> getForwardReferences(EObject fromObject, EClass toClass, int maxHops, IMDLFilter filter)
	{
		return getReferredEObjects(fromObject, toClass, maxHops, filter, MDLEligibility.NON_CONTAINMENT);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.core.mms.mdm.IModelDependencyLookup#getBackwardReferences(org.eclipse.core.resources.IProject,
	 * org.eclipse.emf.ecore.EClass, org.eclipse.emf.ecore.EObject)
	 */
	@Override
	public List<EObject> getBackwardReferences(IProject project, EClass fromClass, EObject toObject)
	{
		return getBackwardReferences(project, fromClass, toObject, INFINITE_HOPS);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.core.mms.mdm.IModelDependencyLookup#getBackwardReferences(org.eclipse.core.resources.IProject,
	 * org.eclipse.emf.ecore.EClass, org.eclipse.emf.ecore.EObject, int)
	 */
	@Override
	public List<EObject> getBackwardReferences(IProject project, EClass fromClass, EObject toObject, int maxHops)
	{
		return getBackwardReferences(project, fromClass, toObject, maxHops, ACCEPT_ALL);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.core.mms.mdm.IModelDependencyLookup#getBackwardReferences(org.eclipse.core.resources.IProject,
	 * org.eclipse.emf.ecore.EClass, org.eclipse.emf.ecore.EObject,
	 * eu.cessar.ct.core.mms.mdm.IModelDependencyLookup.IMDLFilter)
	 */
	@Requirement(
		reqID = "REQ_API_MDL#3")
	@Override
	public List<EObject> getBackwardReferences(IProject project, EClass fromClass, EObject toObject, IMDLFilter filter)
	{
		return getBackwardReferences(project, fromClass, toObject, INFINITE_HOPS, filter);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.core.mms.mdm.IModelDependencyLookup#getBackwardReferences(org.eclipse.core.resources.IProject,
	 * org.eclipse.emf.ecore.EClass, org.eclipse.emf.ecore.EObject, int,
	 * eu.cessar.ct.core.mms.mdm.IModelDependencyLookup.IMDLFilter)
	 */
	@Override
	public List<EObject> getBackwardReferences(IProject project, EClass fromClass, EObject toObject, int maxHops,
		IMDLFilter filter)
	{
		Collection<Resource> resources = EResourceUtils.getProjectResources(project);
		List<EObject> result = new ArrayList<EObject>();
		for (Resource resource: resources)
		{
			result.addAll(getBackwardReferences(resource, fromClass, toObject, maxHops, filter));
		}
		return removeDuplicates(result);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.cessar.ct.core.mms.mdm.IModelDependencyLookup#getBackwardReferences(org.eclipse.emf.ecore.resource.Resource,
	 * org.eclipse.emf.ecore.EClass, org.eclipse.emf.ecore.EObject)
	 */
	@Override
	public List<EObject> getBackwardReferences(Resource resource, EClass fromClass, EObject toObject)
	{
		return getBackwardReferences(resource, fromClass, toObject, INFINITE_HOPS);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.cessar.ct.core.mms.mdm.IModelDependencyLookup#getBackwardReferences(org.eclipse.emf.ecore.resource.Resource,
	 * org.eclipse.emf.ecore.EClass, org.eclipse.emf.ecore.EObject, int)
	 */
	@Override
	public List<EObject> getBackwardReferences(Resource resource, EClass fromClass, EObject toObject, int maxHops)
	{
		return getBackwardReferences(resource, fromClass, toObject, maxHops, ACCEPT_ALL);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.cessar.ct.core.mms.mdm.IModelDependencyLookup#getBackwardReferences(org.eclipse.emf.ecore.resource.Resource,
	 * org.eclipse.emf.ecore.EClass, org.eclipse.emf.ecore.EObject,
	 * eu.cessar.ct.core.mms.mdm.IModelDependencyLookup.IMDLFilter)
	 */
	@Override
	public List<EObject> getBackwardReferences(Resource resource, EClass fromClass, EObject toObject, IMDLFilter filter)
	{
		return getBackwardReferences(resource, fromClass, toObject, INFINITE_HOPS, filter);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.cessar.ct.core.mms.mdm.IModelDependencyLookup#getBackwardReferences(org.eclipse.emf.ecore.resource.Resource,
	 * org.eclipse.emf.ecore.EClass, org.eclipse.emf.ecore.EObject, int,
	 * eu.cessar.ct.core.mms.mdm.IModelDependencyLookup.IMDLFilter)
	 */
	@Override
	public List<EObject> getBackwardReferences(Resource resource, EClass fromClass, EObject toObject, int maxHops,
		IMDLFilter filter)
	{
		return getBackwardReferences(resource.getContents(), fromClass, toObject, maxHops, filter);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.core.mms.mdm.IModelDependencyLookup#getBackwardReferences(org.eclipse.emf.ecore.EObject,
	 * org.eclipse.emf.ecore.EClass, org.eclipse.emf.ecore.EObject)
	 */
	@Override
	public List<EObject> getBackwardReferences(EObject context, EClass fromClass, EObject toObject)
	{
		return getBackwardReferences(context, fromClass, toObject, INFINITE_HOPS);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.core.mms.mdm.IModelDependencyLookup#getBackwardReferences(org.eclipse.emf.ecore.EObject,
	 * org.eclipse.emf.ecore.EClass, org.eclipse.emf.ecore.EObject, int)
	 */
	@Override
	public List<EObject> getBackwardReferences(EObject context, EClass fromClass, EObject toObject, int maxHops)
	{
		return getBackwardReferences(context, fromClass, toObject, maxHops, ACCEPT_ALL);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.core.mms.mdm.IModelDependencyLookup#getBackwardReferences(org.eclipse.emf.ecore.EObject,
	 * org.eclipse.emf.ecore.EClass, org.eclipse.emf.ecore.EObject,
	 * eu.cessar.ct.core.mms.mdm.IModelDependencyLookup.IMDLFilter)
	 */
	@Override
	public List<EObject> getBackwardReferences(EObject context, EClass fromClass, EObject toObject, IMDLFilter filter)
	{
		return getBackwardReferences(context, fromClass, toObject, INFINITE_HOPS, filter);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.core.mms.mdm.IModelDependencyLookup#getBackwardReferences(org.eclipse.emf.ecore.EObject,
	 * org.eclipse.emf.ecore.EClass, org.eclipse.emf.ecore.EObject, int,
	 * eu.cessar.ct.core.mms.mdm.IModelDependencyLookup.IMDLFilter)
	 */
	@Requirement(
		reqID = "REQ_API_MDL#4")
	@Override
	public List<EObject> getBackwardReferences(EObject context, EClass fromClass, EObject toObject, int maxHops,
		IMDLFilter filter)
	{
		checkGetBackwardReferencesArgs(context, fromClass, filter, toObject);

		EClass toClass = toObject.eClass();
		if (!mdm.mayRefer(fromClass, toClass))
		{
			return Collections.EMPTY_LIST;
		}

		List<EObject> result = new ArrayList<EObject>();

		List<EObject> fromObjects = getChildren(context, fromClass, filter);
		for (EObject fromObject: fromObjects)
		{
			if (anyRefer(fromObject, toClass, maxHops, filter, toObject))
			{
				result.add(fromObject);
			}
		}
		return removeDuplicates(result);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.core.mms.mdm.IModelDependencyLookup#getBackwardReferences(java.util.Collection,
	 * org.eclipse.emf.ecore.EClass, org.eclipse.emf.ecore.EObject, int,
	 * eu.cessar.ct.core.mms.mdm.IModelDependencyLookup.IMDLFilter)
	 */
	@Override
	public List<EObject> getBackwardReferences(Collection<EObject> contexts, EClass fromClass, EObject toObject,
		int maxHops, IMDLFilter filter)
	{
		List<EObject> result = new ArrayList<EObject>();
		for (EObject context: contexts)
		{
			result.addAll(getBackwardReferences(context, fromClass, toObject, maxHops, filter));
		}
		return removeDuplicates(result);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.core.mms.mdl.IModelDependencyLookup2#getReferrers(org.eclipse.emf.ecore.EClass)
	 */
	@Override
	public List<EClass> getReferrers(EClass clz)
	{
		List<EClass> referrers = new ArrayList<EClass>();
		Collection<EClass> eAllClasses = mdm.getEAllClasses(MDLEligibility.NON_CONTAINMENT);
		for (EClass possibleReferrer: eAllClasses)
		{
			boolean mayRefer = mdm.mayRefer(possibleReferrer, clz);
			if (mayRefer)
			{
				referrers.add(possibleReferrer);
			}
		}
		return referrers;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.core.mms.mdl.IModelDependencyLookup2#getAggregates(org.eclipse.emf.ecore.EClass)
	 */
	@Override
	public List<EClass> getAggregates(EClass clz)
	{
		List<EClass> aggregatesList = new ArrayList<EClass>();
		Collection<EClass> eAllClasses = mdm.getEAllClasses(MDLEligibility.CONTAINMENT);
		for (EClass possibleAggregate: eAllClasses)
		{
			boolean mayContain = mdm.mayContain(possibleAggregate, clz);
			if (mayContain)
			{
				aggregatesList.add(possibleAggregate);
			}
		}
		return aggregatesList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.core.mms.mdl.IModelDependencyLookup2#getPaths(org.eclipse.emf.ecore.EClass,
	 * org.eclipse.emf.ecore.EClass)
	 */
	@Override
	public List<EClass> getFirstPath(EClass fromClass, EClass toClass)
	{
		List<EClass> firstPath = new ArrayList<EClass>();

		EClass eReferenceTypeCurrent = fromClass;
		boolean superTypeOf = fromClass.isSuperTypeOf(toClass);

		// in case the from class is a super type of the to class there are no other classes between
		if (superTypeOf)
		{
			// add the start class
			firstPath.add(eReferenceTypeCurrent);
			// add the toClass
			firstPath.add(toClass);

			return firstPath;
		}

		// in case there is no path between return empty list
		List<EReference> references = mdm.getReferences(fromClass, toClass, MDLEligibility.CONTAINMENT);
		if (references.size() == 0)
		{
			return firstPath;
		}

		// add the start class
		firstPath.add(eReferenceTypeCurrent);

		while (!(superTypeOf || (eReferenceTypeCurrent == toClass)))
		{
			// go deeper

			// get all references to the next class
			references = mdm.getReferences(eReferenceTypeCurrent, toClass, MDLEligibility.CONTAINMENT);
			if (references.size() != 0)
			{

				// parse references
				for (EReference reference: references)
				{

					EReference eReferenceNew = reference;
					EClass eReferenceTypeNew = eReferenceNew.getEReferenceType();
					// do not add to the path a superClass of the current one
					if (!eReferenceTypeCurrent.isSuperTypeOf(eReferenceTypeNew))
					{
						// make sure that the reference is not in the path
						if (!firstPath.contains(eReferenceTypeNew))
						{
							// the new reference type becomes current reference
							eReferenceTypeCurrent = eReferenceTypeNew;
							superTypeOf = eReferenceTypeCurrent.isSuperTypeOf(toClass);
							if (!superTypeOf)
							{
								// current reference added to path
								firstPath.add(eReferenceTypeCurrent);
							}
							break;
						}
					}

				}
			}
			else
			{
				break;
			}
		}
		// add the toClass to the first path
		firstPath.add(toClass);
		return firstPath;

	}
}
