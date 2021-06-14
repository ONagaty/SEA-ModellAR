/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870<br/>
 * Apr 13, 2013 8:14:37 PM
 * 
 * </copyright>
 */
package eu.cessar.ct.sdk.sea.util;

/**
 * It holds the options for a SEA model
 * 
 * @author uidl6870
 * 
 *         %created_by: uidl6870 %
 * 
 *         %date_created: Wed Jun 17 18:43:05 2015 %
 * 
 *         %version: 2 %
 */
public interface ISeaOptions
{

	/**
	 * @return the installed error handler to which exceptional cases are reported
	 */
	public ISEAErrorHandler getErrorHandler();

	/**
	 * @return whether to perform save automatically
	 */
	public boolean isAutoSave();

	/**
	 * @return whether to perform write operations inside a write transaction
	 */
	public boolean isReadOnly();

	/**
	 * @see SEAModelUtil#REUSE_FRAGMENT
	 * @return the value for the preference
	 */
	public boolean reuseFragment();
}
