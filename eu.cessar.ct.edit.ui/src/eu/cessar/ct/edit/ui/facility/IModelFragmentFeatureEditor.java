/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Mar 2, 2010 8:40:42 PM </copyright>
 */
package eu.cessar.ct.edit.ui.facility;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;

import eu.cessar.ct.edit.ui.facility.splitable.ISplitableContextSingleFeatureEditingManager;

/**
 * @author uidl6458
 */
public interface IModelFragmentFeatureEditor extends IModelFragmentEditor
{

	/**
	 * @param eClass
	 */
	public void setInputClass(EClass eClass);

	/**
	 * @return the input's EClass
	 */
	public EClass getInputClass();

	/**
	 * Sets the edietd feature
	 * 
	 * @param feature
	 */
	public void setInputFeature(EStructuralFeature feature);

	/**
	 * @return the edited feature
	 */
	public EStructuralFeature getInputFeature();

	public ISplitableContextSingleFeatureEditingManager getSplitableContextEditingManager();

}
