/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Jul 11, 2011 12:06:41 PM </copyright>
 */
package eu.cessar.ct.runtime.ecuc.internal.pm;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.transaction.NotificationFilter;
import org.eclipse.emf.transaction.ResourceSetChangeEvent;
import org.eclipse.emf.transaction.ResourceSetListenerImpl;

import eu.cessar.ct.emfproxy.IEMFProxyEngine;

/**
 * 
 */
public class RuntimeResourceSetListener extends ResourceSetListenerImpl
{

	private final IEMFProxyEngine proxyEngine;

	public RuntimeResourceSetListener(IEMFProxyEngine proxyEngine)
	{
		super(NotificationFilter.RESOURCE_UNLOADED);
		this.proxyEngine = proxyEngine;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.emf.transaction.ResourceSetListenerImpl#isPrecommitOnly()
	 */
	@Override
	public boolean isPrecommitOnly()
	{
		return true;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.emf.transaction.ResourceSetListenerImpl#resourceSetChanged(org.eclipse.emf.transaction.ResourceSetChangeEvent)
	 */
	@Override
	public Command transactionAboutToCommit(ResourceSetChangeEvent event)
	{
		List<Notification> notifications = event.getNotifications();
		Set<Resource> unloadedResources = new HashSet<Resource>();
		for (Notification notification: notifications)
		{
			Object notifier = notification.getNotifier();
			if (notifier instanceof Resource)
			{
				unloadedResources.add((Resource) notifier);
			}
		}
		// remove any cache we might have on the resources
		proxyEngine.clearCacheForResources(unloadedResources);
		return null;
	}
}
