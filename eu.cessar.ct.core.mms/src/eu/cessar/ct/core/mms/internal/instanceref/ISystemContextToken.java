/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870<br/>
 * 08.07.2013 10:03:37
 * 
 * </copyright>
 */
package eu.cessar.ct.core.mms.internal.instanceref;

import org.eclipse.emf.ecore.EReference;

/**
 * @author uidl6870
 * 
 *         %created_by: uidl6870 %
 * 
 *         %date_created: Mon Jul 15 17:47:08 2013 %
 * 
 *         %version: 1 %
 */
public interface ISystemContextToken extends IContextToken
{
	/**
	 * @return wrapped reference
	 */
	public EReference getReference();

}
