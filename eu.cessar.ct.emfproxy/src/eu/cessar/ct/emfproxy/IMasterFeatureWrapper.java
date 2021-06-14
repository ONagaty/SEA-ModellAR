/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Mar 17, 2010 2:12:48 PM </copyright>
 */
package eu.cessar.ct.emfproxy;

import org.eclipse.emf.ecore.EStructuralFeature;

/**
 * @author uidl6458
 * 
 */
public interface IMasterFeatureWrapper<T> extends IMasterWrapper
{

	/**
	 * Set the feature from the master model that is wrapped by this class
	 * 
	 * @param feature
	 */
	public void setWrappedFeature(EStructuralFeature feature);

	/**
	 * Return the feature from the master model that is wrapped
	 * 
	 * @return
	 */
	public EStructuralFeature getWrappedFeature();

	/**
	 * @return
	 */
	public Class<T> getFeatureClass();

	/**
	 * @return
	 */
	public int getHashCode();

	/**
	 * 
	 * Return true if the wrapper deliver "live" values
	 * 
	 * @return
	 */
	public boolean haveLiveValues();
}
