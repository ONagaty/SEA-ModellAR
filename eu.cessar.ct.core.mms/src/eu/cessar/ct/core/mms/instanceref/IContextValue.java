/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870<br/>
 * 10.07.2013 11:29:56
 * 
 * </copyright>
 */
package eu.cessar.ct.core.mms.instanceref;

import gautosar.ggenericstructure.ginfrastructure.GIdentifiable;

import java.util.List;

/**
 * Wraps the element(s) that can be used to set an instance reference's context
 * 
 * @author uidl6870
 * 
 *         %created_by: uidl6870 %
 * 
 *         %date_created: Mon Jul 15 10:35:08 2013 %
 * 
 *         %version: 1 %
 */
public interface IContextValue
{
	/**
	 * Get the list with elements for the context; the list has at least one element
	 * 
	 * @return list containing the values for the context, never <code>null</code>
	 */
	public List<GIdentifiable> getElements();

	/**
	 * Get the context type
	 * 
	 * @return the context type wrapper
	 */
	public IContextType getContextType();

}
