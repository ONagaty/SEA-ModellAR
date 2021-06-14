/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870<br/>
 * 23.10.2012 14:13:46
 * 
 * </copyright>
 */
package eu.cessar.ct.edit.ui.facility.splitable;

import org.eclipse.emf.ecore.EStructuralFeature;

/**
 * Provides editing strategy for a single feature in the context of a splittable input.
 * 
 * @see ISplitableContextEditingStrategy
 * @author uidl6870
 * 
 *         %created_by: uidl6870 %
 * 
 *         %date_created: Fri Oct 26 10:47:08 2012 %
 * 
 *         %version: 1 %
 */
public interface ISplitableContextFeatureEditingStrategy extends ISplitableContextEditingStrategy
{
	/**
	 * 
	 * @return the feature
	 */
	public EStructuralFeature getFeature();

	/**
	 * 
	 * @param feature
	 */
	public void setFeature(EStructuralFeature feature);
}
