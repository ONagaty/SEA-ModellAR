/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870<br/>
 * 10.10.2012 13:35:16
 * 
 * </copyright>
 */
package eu.cessar.ct.edit.ui.facility.parts;

/**
 * The interface defines an entity that has associated documentation available.
 * 
 * @author uidl6870
 * 
 *         %created_by: uidl6870 %
 * 
 *         %date_created: Tue Oct 16 13:28:26 2012 %
 * 
 *         %version: 1 %
 */
public interface IDocumentationOwner
{

	/**
	 * 
	 * @return the associated documentation (if any)
	 */
	public String getDocumentation();
}
