/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870<br/>
 * Apr 13, 2013 11:17:37 AM
 * 
 * </copyright>
 */
package eu.cessar.ct.runtime.ecuc.internal.sea;

import eu.cessar.ct.sdk.sea.ISEAModel;

/**
 * Factory for filters used by Sea
 * 
 * @author uidl6870
 * 
 *         %created_by: uidl6870 %
 * 
 *         %date_created: Thu Aug 8 16:03:21 2013 %
 * 
 *         %version: 2 %
 */
public final class SeaFiltersFactory
{

	static
	{
		// just for the sake of code coverage
		new SeaFiltersFactory();
	}

	private SeaFiltersFactory()
	{
		// hide
	}

	/**
	 * Creates a filter that accepts elements whose shortName match the one provided (case sensitive optionally).
	 * 
	 * @param name
	 *        short name
	 * @param ignoreCase
	 *        whether case insensitive
	 * @return the proper filter
	 */
	public static IGIdentifiableFilter getFilterDefinitionByName(String name, boolean ignoreCase)
	{
		return new GIdentifiableFilterByName(name, ignoreCase);
	}

	/**
	 * Creates a filter that accepts elements whose qualified name match the given <code>pathFragment</code> (case
	 * sensitive optionally).
	 * 
	 * @see ISEAModel#pathFragment
	 * @param pathFragment
	 * @param ignoreCase
	 *        whether case insensitive
	 * @return the proper filter
	 */
	public static IGIdentifiableFilter getFilterDefinitionByPathFragment(String pathFragment, boolean ignoreCase)
	{
		return new GIdentifiableFilterByPathFragment(pathFragment, ignoreCase);
	}
}
