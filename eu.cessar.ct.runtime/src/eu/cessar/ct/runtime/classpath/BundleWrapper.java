/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidu0944 Jan 31, 2012 4:46:33 PM </copyright>
 */
package eu.cessar.ct.runtime.classpath;

/**
 * @author uidu0944
 * 
 */
public class BundleWrapper
{

	private String bundleId;

	/**
	 * @return the bundleId
	 */
	public String getBundleId()
	{
		return bundleId;
	}

	/**
	 * @param bundleId
	 *        the bundleId to set
	 */
	public void setBundleId(String bundleId)
	{
		this.bundleId = bundleId;
	}

	/**
	 * @return the dependency
	 */
	public boolean isDependency()
	{
		return dependency;
	}

	/**
	 * @param dependency
	 *        the dependency to set
	 */
	public void setDependency(boolean dependency)
	{
		this.dependency = dependency;
	}

	private boolean dependency;

}
