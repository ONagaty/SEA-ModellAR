/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Jul 20, 2010 8:32:36 PM </copyright>
 */
package eu.cessar.ct.core.platform;

/**
 * Compatibility mode enumeration
 * 
 * @Review uidl6458 - 12.04.2012
 */
public enum ECompatibilityMode
{
	NONE(false, true), //
	FULL(true, false), //
	;

	private boolean compatAPI;
	private boolean newAPI;

	private ECompatibilityMode(boolean compatAPI, boolean newAPI)
	{
		this.compatAPI = compatAPI;
		this.newAPI = newAPI;
	}

	/**
	 * Return true if compatibility API shall be provided by the project, false
	 * otherwise
	 * 
	 * @return
	 */
	public boolean haveCompatibilityAPI()
	{
		return compatAPI;
	}

	/**
	 * Return true if the new API shall be provided by the project, false
	 * otherwise
	 * 
	 * @return
	 */
	public boolean haveNewAPI()
	{
		return newAPI;
	}
}
