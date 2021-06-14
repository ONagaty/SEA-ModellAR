/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 27.06.2012 14:37:53 </copyright>
 */
package eu.cessar.ct.sdk;

/**
 * 
 * @author uidl6458
 * 
 */
public interface IPostBuildContext extends Comparable
{

	/**
	 * @return
	 */
	public String getName();

	/**
	 * @return
	 */
	public int getID();

	/**
	 * @return
	 */
	public boolean isInUse();

}
