/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidv3687<br/>
 * Oct 23, 2013 12:57:29 PM
 * 
 * </copyright>
 */
package eu.cessar.ct.core.mms;

import java.util.Comparator;

import org.eclipse.emf.ecore.resource.Resource;

import eu.cessar.req.Requirement;

/**
 * Custom resources comparator. Compare them by file name alphabetical. For Resources with same name, compare the entire
 * URI
 * 
 * @author uidv3687
 * 
 *         %created_by: uidl6458 %
 * 
 *         %date_created: Fri Jan 17 19:13:02 2014 %
 * 
 *         %version: 2 %
 */
@Requirement(
	reqID = "REQ_NFR#CORE#1")
public class ResourcesComparator implements Comparator<Resource>
{

	/**
	 * The singleton instance of this comparator
	 */
	public static final ResourcesComparator INSTANCE = new ResourcesComparator();

	/**
	 * This constructor is protected to avoid instantiation but the to allow subclasses
	 */
	protected ResourcesComparator()
	{
		// avoid instance
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare(Resource o1, Resource o2)
	{
		int result = o1.getURI().lastSegment().compareToIgnoreCase(o2.getURI().lastSegment());
		if (result != 0)
		{
			return result;
		}
		// for Resources with same name, compare the entire URI
		return o1.getURI().toString().compareToIgnoreCase(o2.getURI().toString());
	}

}
