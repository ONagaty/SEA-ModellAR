/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidu0944 Dec 14, 2011 2:17:59 PM </copyright>
 */
package eu.cessar.ct.core.mms.internal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.SafeRunner;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.transaction.NotificationFilter;
import org.eclipse.emf.transaction.ResourceSetChangeEvent;
import org.eclipse.emf.transaction.ResourceSetListener;
import org.eclipse.emf.transaction.ResourceSetListenerImpl;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.sphinx.emf.util.EcorePlatformUtil;
import org.eclipse.sphinx.emf.util.WorkspaceEditingDomainUtil;

import eu.cessar.ct.core.mms.INotificationCallback;
import eu.cessar.ct.core.mms.INotificationManager;
import eu.cessar.ct.core.mms.MetaModelUtils;
import eu.cessar.ct.core.platform.util.SafeRunnable;

/**
 * @author uidu0944
 * 
 */
public class ModelChangeNotificationManagerImpl implements INotificationManager
{

	/**
	 * Holds the mapping between EditingDomain and interested listeners
	 */
	private Map<IProject, List<INotificationCallback>> map = new HashMap<IProject, List<INotificationCallback>>();

	/**
	 * Holds the mapping between EditingDomain and the listener assigned
	 */
	protected Map<IProject, ResourceSetListener> listenerMap = new HashMap<IProject, ResourceSetListener>();

	/**
	 * Holds the reverse associations, assuming a notification callback may be
	 * registered for different editing domains
	 */
	private Map<INotificationCallback, List<IProject>> callbackMap = new HashMap<INotificationCallback, List<IProject>>();

	/**
	 * Associates a callback with the specified editing domain and, for the
	 * first registering callback, registers the editing domain as a resource
	 * set listener
	 * 
	 * @see eu.cessar.ct.core.mms.INotificationManager#register(org.eclipse.emf.edit.domain.EditingDomain,
	 *      eu.cessar.ct.core.mms.INotificationCallback)
	 */
	public synchronized void register(Object project, INotificationCallback callBack)
	{
		IProject currentResource = (IProject) project;

		if (!map.containsKey(currentResource))
		{
			createChangedListener(currentResource);
			map.put(currentResource, new ArrayList<INotificationCallback>());
		}

		List<INotificationCallback> cbList = map.get(currentResource);
		if (!cbList.contains(callBack))
		{
			cbList.add(callBack);
		}

		if (!callbackMap.containsKey(callBack))
		{
			callbackMap.put(callBack, new ArrayList<IProject>());
		}
		callbackMap.get(callBack).add(currentResource);

	}

	/**
	 * Unregisters a callback from the specified editing domain and removes the
	 * ResourceChangedListener
	 * 
	 * @param callBack
	 */
	public synchronized void unregister(INotificationCallback callBack)
	{
		List<IProject> list = callbackMap.get(callBack);
		if (list != null)
		{
			for (IProject resource: list)
			{
				List<INotificationCallback> callBackList = map.get(resource);
				if (callBackList != null)
				{
					callBackList.remove(callBack);
					if (callBackList.isEmpty())
					{
						map.remove(resource);
						removeChangedListener(resource);
					}
				}
			}
			callbackMap.remove(callBack);
		}
	}

	/**
	 * Dispatch the notifications to all callbacks registered with each editing
	 * domain
	 * 
	 * @see eu.cessar.ct.core.mms.INotificationManager#notify(java.util.List)
	 */
	public void notify(List<Notification> notificationList)
	{
		// none to be notified
		if (map.isEmpty())
		{
			return;
		}
		@SuppressWarnings("rawtypes")
		Collection c = map.values();
		@SuppressWarnings("rawtypes")
		Iterator itr = c.iterator();
		while (itr.hasNext())
		{
			@SuppressWarnings("unchecked")
			List<INotificationCallback> callBackList = (List<INotificationCallback>) itr.next();
			notifyResult(callBackList, notificationList);
		}
	}

	/**
	 * Notifies each callback
	 * 
	 * @param callBackList
	 * @param diagnostic
	 */
	private void notifyResult(List<INotificationCallback> callBackList,
		final List<Notification> notifications)
	{
		for (final INotificationCallback callback: callBackList)
		{
			SafeRunner.run(new SafeRunnable()
			{
				public void run() throws Exception
				{
					callback.modelChanged(notifications);
				}
			});
		}
	}

	/**
	 * Create a resource changed listener
	 * 
	 * @return
	 */
	protected ResourceSetListener createChangedListener(final IProject currentResource)
	{
		NotificationFilter filter = new NotificationFilter.Custom()
		{
			@Override
			public boolean matches(Notification notification)
			{
				if (notification.getNotifier() instanceof Resource)
				{
					IFile file = EcorePlatformUtil.getFile((Resource) notification.getNotifier());
					if (file.getProject() == currentResource)
					{
						return true;
					}
				}
				return false;
			}
		};
		ResourceSetListenerImpl listener = new ResourceSetListenerImpl(filter)
		{
			@Override
			public void resourceSetChanged(final ResourceSetChangeEvent event)
			{
				ModelChangeNotificationManagerImpl.this.notify(event.getNotifications());
			}
		};

		EditingDomain editingDomain = MetaModelUtils.getEditingDomain(currentResource);
		((TransactionalEditingDomain) editingDomain).addResourceSetListener(listener);
		listenerMap.put(currentResource, listener);

		return listener;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.core.mms.internal.NotificationManagerImpl#removeChangedListener(org.eclipse.emf.edit.domain.EditingDomain)
	 */

	protected void removeChangedListener(IProject currentResource)
	{
		EditingDomain editingDomain = WorkspaceEditingDomainUtil.getEditingDomain(currentResource);
		if (editingDomain != null)
		{
			ResourceSetListener resourceChangedListener = listenerMap.get(editingDomain);
			((TransactionalEditingDomain) editingDomain).removeResourceSetListener(resourceChangedListener);
		}
		listenerMap.remove(currentResource);
	}

}
