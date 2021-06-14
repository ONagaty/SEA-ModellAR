/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidl6458<br/>
 * 21.05.2013 14:29:26
 *
 * </copyright>
 */
package eu.cessar.ct.sdk.utils;

import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;

/**
 * Specifies methods that retrieve information from an EMF model based on the references between its objects.
 *
 * The specified functionality includes the retrieval of parents and children linked via containment references and the
 * retrieval of starting points and end points linked via any type of references based on their EClass, and allows
 * tuning of the search process by specifying the maximum allowed number of references to follow and by supplying an
 * acceptance filter for the next search step. Also provides Autosar specific methods.
 *
 * @author uidl6458
 *
 *         %created_by: uidg4020 %
 *
 *         %date_created: Fri May 30 18:55:56 2014 %
 *
 *         %version: 2 %
 */
public interface IModelDependencyLookup extends IGenericModelDependencyLookup
{
	/**
	 * Offers a method that indicates acceptance or rejection of the candidate next step in the search process.
	 *
	 * @author uidl6458
	 *
	 */
	public static interface IMDLFilter
	{
		/**
		 * Indicates acceptance or rejection of the candidate next step in the search process.
		 *
		 * @param oldContext
		 *        currently reached EObject
		 * @param path
		 *        candidate reference to follow
		 * @param newContext
		 *        candidate EObject to consider next
		 * @param currentHops
		 *        current number of followed references up to this point
		 * @return indication of acceptance or rejection to further follow this reference
		 */
		public boolean accept(EObject oldContext, EReference path, EObject newContext, int currentHops);
	}

	/**
	 * Conventional value for representing positive infinity.
	 */
	public static final int INFINITE_HOPS = -1;

	/**
	 * Computes the list of children of EClass {@code childClass} inside {@code project}, considering containment
	 * references only.
	 *
	 * @param project
	 *        the project to consider for search
	 * @param childClass
	 *        the children's EClass
	 * @return the non-null, possibly empty list
	 */
	public List<EObject> getChildren(IProject project, EClass childClass);

	/**
	 * Computes the list of children of EClass {@code childClass} inside {@code project} that are at most at
	 * {@code maxHops} containment references from the top level.
	 *
	 * @param project
	 *        the project to consider for search
	 * @param childClass
	 *        the children's EClass
	 * @param maxHops
	 *        the maximum allowed number of containment references to follow; by convention, infinite if <= 0
	 * @return the non-null, possibly empty children (descendants) list
	 */
	public List<EObject> getChildren(IProject project, EClass childClass, int maxHops);

	/**
	 * Computes the list of children (descendants) of EClass {@code childClass} inside {@code project} that are
	 * acceptable by {@code filter}.
	 *
	 * @param project
	 *        the project to consider for search
	 * @param childClass
	 *        the children's EClass
	 * @param filter
	 *        the acceptance filter
	 * @return the non-null, possibly empty children (descendants) list
	 */
	public List<EObject> getChildren(IProject project, EClass childClass, IMDLFilter filter);

	/**
	 * Computes the list of children EClass {@code childClass} inside {@code project} that are acceptable by
	 * {@code filter} and are at most at {@code maxHops} containment references from the top level.
	 *
	 * @param project
	 *        the project to consider for search
	 * @param childClass
	 *        the children's EClass
	 * @param maxHops
	 *        the maximum allowed number of containment references to follow
	 * @param filter
	 *        the acceptance filter
	 * @return the non-null, possibly empty children (descendants) list
	 */
	public List<EObject> getChildren(IProject project, EClass childClass, int maxHops, IMDLFilter filter);

	/**
	 * Computes list of EObjects of EClass {@code fromClass} inside {@code project} that refer to {@code toObject} via
	 * non-containment references.
	 *
	 * For splittable support, please use GARObject toObject = SplitUtils.getMergedInstance((GARObject) toObject) before
	 * the call of this method
	 *
	 * @param project
	 *        the project to consider for search
	 * @param fromClass
	 *        the starting point's EClass
	 * @param toObject
	 *        the destination EObject
	 * @return the non-null, possibly empty, referring objects list
	 */
	public List<EObject> getBackwardReferences(IProject project, EClass fromClass, EObject toObject);

	/**
	 * Computes the list of EObjects of EClass {@code fromClass} inside {@code project} that refer to {@code toObject}
	 * via at most {@code maxHops} non-containment references. Note that maxHops only applies to the non-containment
	 * part of the search, and hops increase starting from candidate objects to the target object.
	 *
	 * For splittable support, please use GARObject toObject = SplitUtils.getMergedInstance((GARObject) toObject) before
	 * the call of this method
	 *
	 * @param project
	 *        the project to consider for search
	 * @param fromClass
	 *        the starting point's EClass
	 * @param toObject
	 *        the destination EObject
	 * @param maxHops
	 *        the maximum allowed number of references to follow
	 * @return the non-null, possibly empty, referring objects list
	 */
	public List<EObject> getBackwardReferences(IProject project, EClass fromClass, EObject toObject, int maxHops);

	/**
	 * Computes the list of EObjects of EClass {@code fromClass} inside {@code project} that refer to {@code toObject}
	 * that are acceptable by {@code filter}. Note that the filter applies to containment as well as non-containment
	 * paths, and the current type of EReference is indicated by path.isContainment(), where path is the parameter of
	 * the {@code IMDLFilter}'s {@code accept} method.
	 *
	 * For splittable support, please use GARObject toObject = SplitUtils.getMergedInstance((GARObject) toObject) before
	 * the call of this method
	 *
	 * @param project
	 *        the project to consider for search
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
	public List<EObject> getBackwardReferences(IProject project, EClass fromClass, EObject toObject, IMDLFilter filter);

	/**
	 * Computes the list of EObjects of EClass {@code fromClass} inside {@code project}, acceptable by {@code filter},
	 * that refer {@code toObject} via at most {@code maxHops} non-containment references. Note that the filter applies
	 * to containment as well as non-containment paths, and the current type of EReference is indicated by
	 * path.isContainment(), where path is the parameter of the {@code IMDLFilter}'s {@code accept} method. Also note
	 * that maxHops only applies to the non-containment part of the search, and hops increase starting from candidate
	 * objects to the target object.
	 *
	 * For splittable support, please use GARObject toObject = SplitUtils.getMergedInstance((GARObject) toObject) before
	 * the call of this method
	 *
	 * @param project
	 *        the project to consider for search
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
	public List<EObject> getBackwardReferences(IProject project, EClass fromClass, EObject toObject, int maxHops,
		IMDLFilter filter);
}
