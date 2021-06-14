/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870<br/>
 * Jan 31, 2014 11:43:04 AM
 * 
 * </copyright>
 */
package eu.cessar.ct.sdk.autofill;

import gautosar.gecucdescription.GContainer;

import java.util.Collections;
import java.util.List;

import org.eclipse.emf.ecore.resource.Resource;

/**
 * Filter that accepts the first fragment that belongs to one of the specified resources
 * 
 * @author uidl6870
 * 
 *         %created_by: uidl6870 %
 * 
 *         %date_created: Mon Feb 10 09:11:26 2014 %
 * 
 *         %version: 1 %
 */
public class AcceptedResourcesFragmentFilter implements IContainerFragmentFilter
{
	private List<Resource> acceptedResources;

	/**
	 * @param acceptedResources
	 */
	public AcceptedResourcesFragmentFilter(List<Resource> acceptedResources)
	{
		this.acceptedResources = (acceptedResources != null) ? acceptedResources : Collections.EMPTY_LIST;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.sdk.autofill.IElementFragmentFilter#getAcceptedFragment(java.util.List)
	 */
	@Override
	public GContainer getAcceptedFragment(List<GContainer> elementFragments)
	{
		GContainer accepted = null;
		for (GContainer fragment: elementFragments)
		{
			Resource resource = fragment.eResource();
			if (acceptedResources.contains(resource))
			{
				accepted = fragment;
				break;
			}
		}

		return accepted;
	}
}
