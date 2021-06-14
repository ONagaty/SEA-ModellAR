/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870<br/>
 * Jan 31, 2014 11:21:01 AM
 * 
 * </copyright>
 */
package eu.cessar.ct.sdk.autofill;

import java.util.List;

import org.eclipse.emf.ecore.resource.Resource;

/**
 * Factory for fragments filters
 * 
 * @author uidl6870
 * 
 *         %created_by: uidl6870 %
 * 
 *         %date_created: Mon Feb 10 09:11:30 2014 %
 * 
 *         %version: 1 %
 */
public final class FragmentFilterFactory
{
	/**
	 * the singleton
	 */
	public static final FragmentFilterFactory INSTANCE = new FragmentFilterFactory();

	private FragmentFilterFactory()
	{
		// hide
	}

	/**
	 * @see FirstAlphaFragmentFilter
	 * @return the filter
	 */
	public static IContainerFragmentFilter newFirstAlphaFragmentFilter()
	{
		return new FirstAlphaFragmentFilter();
	}

	/**
	 * @see AcceptedResourcesFragmentFilter
	 * @param acceptedResources
	 *        list with the accepted EMF resources
	 * @return the filter
	 */
	public static IContainerFragmentFilter newOnlyAcceptedResourcesFragmentFilter(List<Resource> acceptedResources)
	{
		return new AcceptedResourcesFragmentFilter(acceptedResources);
	}

}
