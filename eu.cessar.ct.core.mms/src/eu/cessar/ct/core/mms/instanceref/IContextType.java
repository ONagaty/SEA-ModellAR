/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870<br/>
 * 12.07.2013 14:26:03
 * 
 * </copyright>
 */
package eu.cessar.ct.core.mms.instanceref;

import org.eclipse.emf.ecore.EClass;

/**
 * Wrapper around a instance reference's context type
 * 
 * @author uidl6870
 * 
 *         %created_by: uidl6870 %
 * 
 *         %date_created: Mon Jul 15 10:35:07 2013 %
 * 
 *         %version: 1 %
 */
public interface IContextType
{
	/**
	 * @return the type of the context
	 */
	public EClass getType();

	/**
	 * @return whether the context allows infinite instances
	 */
	public boolean allowsInfiniteInstances();

	/**
	 * @return whether this is the first context
	 */
	public boolean isRoot();
}
