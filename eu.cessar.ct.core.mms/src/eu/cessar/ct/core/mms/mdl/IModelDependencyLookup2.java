/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidg4020<br/>
 * Dec 2, 2013 4:41:00 PM
 * 
 * </copyright>
 */
package eu.cessar.ct.core.mms.mdl;

import java.util.List;

import org.eclipse.emf.ecore.EClass;

import eu.cessar.ct.sdk.utils.IModelDependencyLookup;

/**
 * 
 * Specifies methods that retrieve information from an EMF model based on the references between its objects. Extends
 * {@link IModelDependencyLookup}
 */
public interface IModelDependencyLookup2 extends IModelDependencyLookup
{
	/**
	 * Get all classes that refer clz.
	 * 
	 * @param clz
	 *        - the class to find referrers for
	 * @return list containing all classes that refer clz.
	 */
	public List<EClass> getReferrers(EClass clz);

	/**
	 * Get all classes that have an instance of this clz as member.
	 * 
	 * @param clz
	 *        - member class of the aggregates
	 * @return classes that have this class as member.
	 */
	public List<EClass> getAggregates(EClass clz);

	/**
	 * Get first path from a class {@code fromClass} to another {@code toClass}. The result list includes the
	 * {@code fromClass} and {@code toClass}. If there is no path between this classes an empty list will be returned.
	 * 
	 * @param fromClass
	 *        - class to begin the path from
	 * @param toClass
	 *        - class where the path end
	 * 
	 * @return list containing all classes from the {@code fromClass} to {@code toClass} class, including margins of the
	 *         path. Returns empty list if no path found
	 */
	public List<EClass> getFirstPath(EClass fromClass, EClass toClass);
}
