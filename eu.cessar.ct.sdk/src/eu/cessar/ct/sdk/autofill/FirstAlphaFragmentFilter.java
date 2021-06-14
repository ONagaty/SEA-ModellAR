/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870<br/>
 * Jan 31, 2014 11:39:24 AM
 * 
 * </copyright>
 */
package eu.cessar.ct.sdk.autofill;

import gautosar.gecucdescription.GContainer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.eclipse.emf.ecore.resource.Resource;

/**
 * Filter accepting the first fragment in alphabetical order of the containing resource name
 * 
 * @author uidl6870
 * 
 *         %created_by: uidl6870 %
 * 
 *         %date_created: Mon Feb 10 09:11:29 2014 %
 * 
 *         %version: 1 %
 */
public class FirstAlphaFragmentFilter implements IContainerFragmentFilter
{
	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.sdk.autofill.IElementFragmentFilter#getAcceptedFragment(java.util.List)
	 */
	@Override
	public GContainer getAcceptedFragment(List<GContainer> elementFragments)
	{
		if (elementFragments.isEmpty())
		{
			return null;
		}
		else if (elementFragments.size() == 1)
		{
			return elementFragments.get(0);
		}

		List<GContainer> copyList = new ArrayList<GContainer>(elementFragments);
		Collections.sort(copyList, new ContainerFragmentsComparator());

		return copyList.get(0);
	}

	class ContainerFragmentsComparator implements Comparator<GContainer>
	{
		public int compare(GContainer fragment1, GContainer fragment2)
		{
			Resource resource1 = fragment1.eResource();
			Resource resource2 = fragment2.eResource();

			if (resource1 != null && resource2 != null)
			{
				String name1 = resource1.getURI().lastSegment();
				String name2 = resource2.getURI().lastSegment();

				return name1.compareTo(name2);
			}
			else if (resource1 == null && resource2 == null)
			{
				return 0;
			}
			else if (resource1 != null)
			{
				return 1;
			}
			else
			{
				return -1;
			}
		}
	}
}
