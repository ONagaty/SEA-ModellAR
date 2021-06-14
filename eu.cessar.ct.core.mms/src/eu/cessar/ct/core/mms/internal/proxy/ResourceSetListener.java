/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458<br/>
 * 06.03.2013 15:51:28
 * 
 * </copyright>
 */
package eu.cessar.ct.core.mms.internal.proxy;

import java.util.Map;
import java.util.WeakHashMap;

import org.eclipse.emf.transaction.NotificationFilter;
import org.eclipse.emf.transaction.ResourceSetChangeEvent;
import org.eclipse.emf.transaction.ResourceSetListenerImpl;
import org.eclipse.emf.transaction.TransactionalEditingDomain;

/**
 * TODO: Please comment this class
 * 
 * @author uidl6458
 * 
 *         %created_by: uidl6458 %
 * 
 *         %date_created: Fri Mar 15 13:19:03 2013 %
 * 
 *         %version: 1 %
 */
public final class ResourceSetListener extends ResourceSetListenerImpl
{

	public static final ResourceSetListener eINSTANCE = new ResourceSetListener();

	private Map<TransactionalEditingDomain, Long> lastChange = new WeakHashMap<TransactionalEditingDomain, Long>();

	private ResourceSetListener()
	{
		// the result filter is:
		// RESOURCE_LOADED || RESOURCE_UNLOADED ||
		// (NOT_TOUNCH && partOfEcucModel)
		super(NotificationFilter.RESOURCE_LOADED.or(NotificationFilter.RESOURCE_UNLOADED).or(
			NotificationFilter.NOT_TOUCH));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.emf.transaction.ResourceSetListenerImpl#isPostcommitOnly()
	 */
	@Override
	public boolean isPostcommitOnly()
	{
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.emf.transaction.ResourceSetListenerImpl#resourceSetChanged(org.eclipse.emf.transaction.
	 * ResourceSetChangeEvent)
	 */
	@Override
	public void resourceSetChanged(ResourceSetChangeEvent event)
	{
		lastChange.put(event.getEditingDomain(), System.currentTimeMillis());
	}

	/**
	 * @param domain
	 * @return
	 */
	public long getLastChangeTime(TransactionalEditingDomain domain)
	{
		Long lastChangedTime = lastChange.get(domain);
		if (lastChangedTime == null)
		{
			return System.currentTimeMillis();
		}
		else
		{
			return lastChangedTime;
		}
	}
}
