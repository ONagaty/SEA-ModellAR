/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870<br/>
 * 28.05.2013 10:55:55
 * 
 * </copyright>
 */
package eu.cessar.ct.sdk.sea.util;

import java.util.List;

import org.eclipse.emf.common.util.EList;

import eu.cessar.req.Requirement;

/**
 * List used by the SEA
 * 
 * @author uidl6870
 * 
 *         %created_by: uidl6870 %
 * 
 *         %date_created: Mon Feb 10 14:21:57 2014 %
 * 
 *         %version: 2 %
 * @param <E>
 */
@Requirement(
	reqID = "REQ_API#SEA#8")
public interface ISEAList<E> extends EList<E>
{

	/**
	 * Works just like described in {@link List#add(Object)}, the only difference is that, <b>in case the owner of the
	 * list is split</b>, the element <b> <code>e</code> will be appended at the end of active fragment's list</b>. This
	 * implies that it is NOT guaranteed that retrieving the last element from the list right after calling
	 * {@link #add(Object)}, the added element is obtained!
	 */
	@Override
	public boolean add(E e);

}
