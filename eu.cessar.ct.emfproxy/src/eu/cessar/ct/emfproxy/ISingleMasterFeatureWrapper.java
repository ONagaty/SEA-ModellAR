/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Mar 17, 2010 2:16:07 PM </copyright>
 */
package eu.cessar.ct.emfproxy;

/**
 * @author uidl6458
 * 
 */
public interface ISingleMasterFeatureWrapper<T> extends IMasterFeatureWrapper<T>
{

	/**
	 * @return
	 */
	public T getValue();

	/**
	 * @param newValue
	 */
	public void setValue(Object newValue);

	/**
	 * @return
	 */
	public boolean isSetValue();

	/**
	 * 
	 */
	public void unsetValue();

}
