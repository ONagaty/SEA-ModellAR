/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870<br/>
 * Jan 29, 2014 2:39:27 PM
 * 
 * </copyright>
 */
package eu.cessar.ct.sdk.autofill;

import java.util.List;

/**
 * Filter used for elements that are candidates for automatic fill. Because elements can be split over several
 * resources, the declared method accepts a list made of an element's fragments (file-based objects) and needs to return
 * one of them or simply<code>null</code> if the respective element should be skipped.
 * 
 * @author uidl6870
 * 
 *         %created_by: uidl6870 %
 * 
 *         %date_created: Mon Feb 10 09:11:32 2014 %
 * 
 *         %version: 1 %
 * @param <T>
 */
public interface IElementFragmentFilter<T>
{

	/**
	 * Decides if an element should be filled and if so, which of the element's fragments. <br>
	 * 
	 * @param elementFragments
	 *        list with the fragments of an element; if the element is not split, the list contains exactly one element
	 * @return the fragment to be used or <code>null</code> if the element should be skipped
	 */
	public T getAcceptedFragment(List<T> elementFragments);
}
