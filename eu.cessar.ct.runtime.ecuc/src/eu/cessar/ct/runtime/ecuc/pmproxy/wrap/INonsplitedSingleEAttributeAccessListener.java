/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Jul 12, 2010 10:42:58 AM </copyright>
 */
package eu.cessar.ct.runtime.ecuc.pmproxy.wrap;

import org.eclipse.emf.ecore.EAttribute;

/**
 * 
 */
public interface INonsplitedSingleEAttributeAccessListener
{

	public void notifySingleAttrGet(EAttribute attr, Object result);

	public void notifySingleAttrIsSet(EAttribute attr, boolean result);

	public void preNotifySingleAttrSet(EAttribute attr, Object newValue);

	public void postNotifySingleAttrSet(EAttribute attr, Object newValue);

	public void preNotifySingleAttrUnset(EAttribute attr);

	public void postNotifySingleAttrUnset(EAttribute attr);
}
