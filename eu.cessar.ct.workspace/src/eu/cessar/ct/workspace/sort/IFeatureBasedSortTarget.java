/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidt2045 Jul 8, 2011 3:54:53 PM </copyright>
 */
package eu.cessar.ct.workspace.sort;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;

/**
 * @author uidt2045
 * 
 */
public interface IFeatureBasedSortTarget extends ISortTarget
{
	public EClass getType();

	public EReference getFeature();

	public boolean isUsingFeatureName();

	public void setUsingFeatureName(boolean usage);
}
