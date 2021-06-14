/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidg4020<br/>
 * May 30, 2014 10:39:01 AM
 * 
 * </copyright>
 */
package eu.cessar.ct.sdk.utils;

import java.util.Collection;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;

import eu.cessar.ct.sdk.utils.IModelDependencyLookup.IMDLFilter;

/**
 * Specifies methods that retrieve information from an EMF model based on the references between its objects.
 * 
 * The specified functionality includes the retrieval of parents and children linked via containment references and the
 * retrieval of starting points and end points linked via any type of references based on their EClass, and allows
 * tuning of the search process by specifying the maximum allowed number of references to follow and by supplying an
 * acceptance filter for the next search step.
 * 
 * @author uidg4020
 * 
 *         %created_by: uidg4020 %
 * 
 *         %date_created: Fri May 30 18:55:35 2014 %
 * 
 *         %version: 1 %
 */
public interface IGenericModelDependencyLookup
{

	/**
	 * Computes the list of EObjects of EClass {@code fromClass}, descendants of an element of {@code contexts},
	 * acceptable by {@code filter}, that refer {@code toObject} via at most {@code maxHops} non-containment references.
	 * Note that the filter applies to containment as well as non-containment paths, and the current type of EReference
	 * is indicated by path.isContainment(), where path is the parameter of the {@code IMDLFilter}'s {@code accept}
	 * method. Also note that maxHops only applies to the non-containment part of the search, and hops increase starting
	 * from candidate objects to the target object.
	 * 
	 * @param contexts
	 *        the starting points of search
	 * @param fromClass
	 *        the starting point's EClass
	 * @param toObject
	 *        the destination EObject
	 * @param filter
	 *        the acceptance filter
	 * @param maxHops
	 *        the maximum allowed number of references to follow
	 * @return the non-null, possibly empty, referring objects list
	 */
	public List<EObject> getBackwardReferences(Collection<EObject> contexts, EClass fromClass, EObject toObject,
		int maxHops, IMDLFilter filter);

	/**
	 * Computes list of EObjects of EClass {@code fromClass}, descendants of {@code context}, that refer to
	 * {@code toObject} via non-containment references.
	 * 
	 * @param context
	 *        the search starting point
	 * @param fromClass
	 *        the starting point's EClass
	 * @param toObject
	 *        the destination EObject
	 * @return the non-null, possibly empty, referring objects list
	 */
	public List<EObject> getBackwardReferences(EObject context, EClass fromClass, EObject toObject);

	/**
	 * Computes the list of EObjects of EClass {@code fromClass}, descendants of {@code context}, that refer to
	 * {@code toObject} that are acceptable by {@code filter}. Note that the filter applies to containment as well as
	 * non-containment paths, and the current type of EReference is indicated by path.isContainment(), where path is the
	 * parameter of the {@code IMDLFilter}'s {@code accept} method.
	 * 
	 * @param context
	 *        the search starting point
	 * @param fromClass
	 *        the starting point's EClass
	 * @param toObject
	 *        the destination EObject
	 * @param filter
	 *        the acceptance filter
	 * @param maxHops
	 *        the maximum allowed number of references to follow
	 * @return the non-null, possibly empty, referring objects list
	 */
	public List<EObject> getBackwardReferences(EObject context, EClass fromClass, EObject toObject, IMDLFilter filter);

	/**
	 * Computes the list of EObjects of EClass {@code fromClass}, descendants of {@code context}, that refer to
	 * {@code toObject} via at most {@code maxHops} non-containment references. Note that maxHops only applies to the
	 * non-containment part of the search, and hops increase starting from candidate objects to the target object.
	 * 
	 * @param context
	 *        the search starting point
	 * @param fromClass
	 *        the starting point's EClass
	 * @param toObject
	 *        the destination EObject
	 * @param maxHops
	 *        the maximum allowed number of references to follow
	 * @return the non-null, possibly empty, referring objects list
	 */
	public List<EObject> getBackwardReferences(EObject context, EClass fromClass, EObject toObject, int maxHops);

	/**
	 * Computes the list of EObjects of EClass {@code fromClass}, descendants of {@code context}, acceptable by
	 * {@code filter}, that refer {@code toObject} via at most {@code maxHops} non-containment references. Note that the
	 * filter applies to containment as well as non-containment paths, and the current type of EReference is indicated
	 * by path.isContainment(), where path is the parameter of the {@code IMDLFilter}'s {@code accept} method. Also note
	 * that maxHops only applies to the non-containment part of the search, and hops increase starting from candidate
	 * objects to the target object.
	 * 
	 * @param context
	 *        the starting point of search
	 * @param fromClass
	 *        the starting point's EClass
	 * @param toObject
	 *        the destination EObject
	 * @param filter
	 *        the acceptance filter
	 * @param maxHops
	 *        the maximum allowed number of references to follow
	 * @return the non-null, possibly empty, referring objects list
	 */
	public List<EObject> getBackwardReferences(EObject context, EClass fromClass, EObject toObject, int maxHops,
		IMDLFilter filter);

	/**
	 * Computes list of EObjects of EClass {@code fromClass} inside {@code resource} that refer to {@code toObject} via
	 * non-containment references.
	 * 
	 * @param resource
	 *        the resource to consider for search
	 * @param fromClass
	 *        the starting point's EClass
	 * @param toObject
	 *        the destination EObject
	 * @return the non-null, possibly empty, referring objects list
	 */
	public List<EObject> getBackwardReferences(Resource resource, EClass fromClass, EObject toObject);

	/**
	 * Computes the list of EObjects of EClass {@code fromClass} inside {@code resource} that refer to {@code toObject}
	 * that are acceptable by {@code filter}. Note that the filter applies to containment as well as non-containment
	 * paths, and the current type of EReference is indicated by path.isContainment(), where path is the parameter of
	 * the {@code IMDLFilter}'s {@code accept} method.
	 * 
	 * @param resource
	 *        the resource to consider for search
	 * @param fromClass
	 *        the starting point's EClass
	 * @param toObject
	 *        the destination EObject
	 * @param filter
	 *        the acceptance filter
	 * @return the non-null, possibly empty, referring objects list
	 */
	public List<EObject> getBackwardReferences(Resource resource, EClass fromClass, EObject toObject, IMDLFilter filter);

	/**
	 * Computes the list of EObjects of EClass {@code fromClass} inside {@code resource} that refer to {@code toObject}
	 * via at most {@code maxHops} non-containment references. Note that maxHops only applies to the non-containment
	 * part of the search, and hops increase starting from candidate objects to the target object.
	 * 
	 * @param resource
	 *        the resource to consider for search
	 * @param fromClass
	 *        the starting point's EClass
	 * @param toObject
	 *        the destination EObject
	 * @param maxHops
	 *        the maximum allowed number of references to follow
	 * @return the non-null, possibly empty, referring objects list
	 */
	public List<EObject> getBackwardReferences(Resource resource, EClass fromClass, EObject toObject, int maxHops);

	/**
	 * Computes the list of EObjects of EClass {@code fromClass} inside {@code resource}, acceptable by {@code filter},
	 * that refer {@code toObject} via at most {@code maxHops} non-containment references. Note that the filter applies
	 * to containment as well as non-containment paths, and the current type of EReference is indicated by
	 * path.isContainment(), where path is the parameter of the {@code IMDLFilter}'s {@code accept} method. Also note
	 * that maxHops only applies to the non-containment part of the search, and hops increase starting from candidate
	 * objects to the target object.
	 * 
	 * @param resource
	 *        the resource to consider for search
	 * @param fromClass
	 *        the starting point's EClass
	 * @param toObject
	 *        the destination EObject
	 * @param filter
	 *        the acceptance filter
	 * @param maxHops
	 *        the maximum allowed number of references to follow
	 * @return the non-null, possibly empty, referring objects list
	 */
	public List<EObject> getBackwardReferences(Resource resource, EClass fromClass, EObject toObject, int maxHops,
		IMDLFilter filter);

	/**
	 * Computes the list of children of {@code parentObject} of EClass {@code childClass}, considering containment
	 * references only.
	 * 
	 * This is equivalent to {@code getChildren(parentObject, childClass, INFINITE_HOPS)}.
	 * 
	 * @param parentObject
	 *        the parent EObject
	 * @param childClass
	 *        the children's EClass
	 * @return the non-null, possibly empty list
	 */
	public List<EObject> getChildren(EObject parentObject, EClass childClass);

	/**
	 * Computes the list of children (descendants) of {@code parentObject} of EClass {@code childClass} that are
	 * acceptable by {@code filter}.
	 * 
	 * This is equivalent to {@code getChildren(parentObject, childClass, INFINITE_HOPS, filter)}.
	 * 
	 * @param parentObject
	 *        the parent EObject
	 * @param childClass
	 *        the children's EClass
	 * @param filter
	 *        the acceptance filter
	 * @return the non-null, possibly empty children (descendants) list
	 */
	public List<EObject> getChildren(EObject parentObject, EClass childClass, IMDLFilter filter);

	/**
	 * Computes the list of children of {@code parentObject} of EClass {@code childClass} that are at most at
	 * {@code maxHops} containment references from {@code parentObject}.
	 * 
	 * This is equivalent to {@code getChildren(parentObject, childClass, maxHops, IMDMFilter.acceptAll)}.
	 * 
	 * @param parentObject
	 *        the parent EObject
	 * @param childClass
	 *        the children's EClass
	 * @param maxHops
	 *        the maximum allowed number of containment references to follow; by convention, infinite if <= 0
	 * @return the non-null, possibly empty children (descendants) list
	 */
	public List<EObject> getChildren(EObject parentObject, EClass childClass, int maxHops);

	/**
	 * Computes the list of children of {@code parentObject} of EClass {@code childClass} that are acceptable by
	 * {@code filter} and are at most at {@code maxHops} containment references from {@code parentObject}.
	 * 
	 * @param parentObject
	 *        the parent EObject
	 * @param childClass
	 *        the children's EClass
	 * @param maxHops
	 *        the maximum allowed number of containment references to follow
	 * @param filter
	 *        the acceptance filter
	 * @return the non-null, possibly empty children (descendants) list
	 */
	public List<EObject> getChildren(EObject parentObject, EClass childClass, int maxHops, IMDLFilter filter);

	/**
	 * Computes the list of children of EClass {@code childClass} inside {@code resource}, considering containment
	 * references only.
	 * 
	 * @param resource
	 *        the resource to consider for search
	 * @param childClass
	 *        the children's EClass
	 * @return the non-null, possibly empty list
	 */
	public List<EObject> getChildren(Resource resource, EClass childClass);

	/**
	 * Computes the list of children (descendants) of EClass {@code childClass} inside {@code resource} that are
	 * acceptable by {@code filter}.
	 * 
	 * @param resource
	 *        the resource to consider for search
	 * @param childClass
	 *        the children's EClass
	 * @param filter
	 *        the acceptance filter
	 * @return the non-null, possibly empty children (descendants) list
	 */
	public List<EObject> getChildren(Resource resource, EClass childClass, IMDLFilter filter);

	/**
	 * Computes the list of objects of EClass {@code childClass} inside {@code resource} that are at most at
	 * {@code maxHops} containment references from the top level.
	 * 
	 * This is equivalent to {@code getChildren(parentObject, childClass, maxHops, IMDMFilter.acceptAll)}.
	 * 
	 * @param resource
	 *        the resource to consider for search
	 * @param childClass
	 *        the children's EClass
	 * @param maxHops
	 *        the maximum allowed number of containment references to follow; by convention, infinite if <= 0
	 * @return the non-null, possibly empty children (descendants) list
	 */
	public List<EObject> getChildren(Resource resource, EClass childClass, int maxHops);

	/**
	 * Computes the list of children of EClass {@code childClass} inside {@code resource} that are acceptable by
	 * {@code filter} and are at most at {@code maxHops} containment references from the top level.
	 * 
	 * @param resource
	 *        the resource to consider for search
	 * @param childClass
	 *        the children's EClass
	 * @param maxHops
	 *        the maximum allowed number of containment references to follow
	 * @param filter
	 *        the acceptance filter
	 * @return the non-null, possibly empty children (descendants) list
	 */
	public List<EObject> getChildren(Resource resource, EClass childClass, int maxHops, IMDLFilter filter);

	/**
	 * Computes the list of EObjects of EClass {@code toClass} that are reachable from {@code fromObject} via
	 * non-containment references.
	 * 
	 * This is equivalent to {@code getForwardReferences(fromObject, toClass, INFINITE_HOPS)}.
	 * 
	 * @param fromObject
	 *        the starting EObject
	 * @param toClass
	 *        the destination's EClass
	 * @return the non-null, possibly empty, referenced objects list
	 */
	public List<EObject> getForwardReferences(EObject fromObject, EClass toClass);

	/**
	 * Computes the list of EObjects of EClass {@code toClass} that are reachable from {@code fromObject} and are
	 * acceptable by {@code filter}.
	 * 
	 * This is equivalent to {@code getForwardReferences(fromObject, toClass, INFINITE_HOPS, filter)}.
	 * 
	 * @param fromObject
	 *        the starting EObject
	 * @param toClass
	 *        the destination's EClass
	 * @param filter
	 *        the acceptance filter
	 * @return the non-null, possibly empty, referenced objects list
	 */
	public List<EObject> getForwardReferences(EObject fromObject, EClass toClass, IMDLFilter filter);

	/**
	 * Computes the list of EObjects of EClass {@code toClass} that are reachable from {@code fromObject} via at most
	 * {@code maxHops} non-containment references.
	 * 
	 * This is equivalent to {@code getForwardReferences(fromObject, toClass, maxHops, IMDMFilter.acceptAll)}.
	 * 
	 * @param fromObject
	 *        the starting EObject
	 * @param toClass
	 *        the destination's EClass
	 * @param maxHops
	 *        the maximum allowed number of references to follow
	 * @return the non-null, possibly empty, referenced objects list
	 */
	public List<EObject> getForwardReferences(EObject fromObject, EClass toClass, int maxHops);

	/**
	 * Computes the list of EObjects of EClass {@code toClass}, acceptable by {@code filter}, that are reachable from
	 * {@code fromObject} via at most {@code maxHops} non-containment references.
	 * 
	 * @param fromObject
	 *        the starting EObject
	 * @param toClass
	 *        the destination's EClass
	 * @param filter
	 *        the acceptance filter
	 * @param maxHops
	 *        the maximum allowed number of references to follow
	 * @return the non-null, possibly empty, referenced objects list
	 */
	public List<EObject> getForwardReferences(EObject fromObject, EClass toClass, int maxHops, IMDLFilter filter);

	/**
	 * Retrieves the parent (ancestor) of {@code childObject} of EClass {@code parentClass}.
	 * 
	 * This is equivalent to {@code getParent(childObject, parentClass, INFINITE_HOPS)}. The direct parent can be
	 * obtained by calling {@code getParent(childObject, parentClass, 1)}.
	 * 
	 * @param childObject
	 *        the child EObject
	 * @param parentClass
	 *        the parent's EClass
	 * @return the parent object, or {@code null} if none found
	 */
	public EObject getParent(EObject childObject, EClass parentClass);

	/**
	 * Retrieves the parent (ancestor) of {@code childObject} of EClass {@code parentClass} that is at most at
	 * {@code maxHops} containment references from {@code childObject}.
	 * 
	 * @param childObject
	 *        the child EObject
	 * @param parentClass
	 *        the parent's EClass
	 * @param maxHops
	 *        the maximum allowed number of containment references to follow; by convention, infinite if <= 0
	 * @return the parent object, or {@code null} if none found
	 */
	public EObject getParent(EObject childObject, EClass parentClass, int maxHops);

}
