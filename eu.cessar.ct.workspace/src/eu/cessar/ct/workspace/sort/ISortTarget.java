/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidt2045 Jul 7, 2011 2:34:05 PM </copyright>
 */
package eu.cessar.ct.workspace.sort;

import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

/**
 * @author uidt2045
 * 
 */
public interface ISortTarget
{
	/**
	 * 
	 * @return
	 */
	public String getLabel();

	/**
	 * @return
	 */
	public Object getImage();

	/**
	 * 
	 */
	public EList<EObject> getObjectsToSort(EObject parent);

	/**
	 * @param parent
	 * @return
	 */
	public EList<EObject> getAllSortableObjects(EObject parent);

	/**
	 * @param o1
	 * @param o2
	 * @return
	 */
	public String getGroupName(EObject candidate);

	/**
	 * @return
	 */
	public List<ISortCriterion> getAvailableSortCriteria();

}
