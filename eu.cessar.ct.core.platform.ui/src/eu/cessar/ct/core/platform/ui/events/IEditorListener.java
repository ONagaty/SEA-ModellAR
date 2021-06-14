/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Mar 11, 2010 3:08:52 PM </copyright>
 */
package eu.cessar.ct.core.platform.ui.events;

/**
 * @author uidl6458
 * 
 */
public interface IEditorListener<T>
{

	public boolean acceptData(T oldData, T newData);

}
