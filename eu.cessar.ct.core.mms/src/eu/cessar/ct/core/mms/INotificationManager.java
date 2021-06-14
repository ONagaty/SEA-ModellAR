/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidu0944 Dec 5, 2011 11:13:32 AM </copyright>
 */
package eu.cessar.ct.core.mms;

import org.eclipse.core.resources.IProject;
import org.eclipse.emf.ecore.resource.Resource;

import eu.cessar.ct.core.mms.internal.ModelChangeNotificationManagerImpl;
import eu.cessar.ct.core.mms.internal.ResourceChangeNotificationManagerImpl;

/**
 * The notification manager ensures that all entities which have registered as
 * listeners will be notified on resource changes.
 * 
 * @author uidu0944
 * 
 * @Review uidl6458 - 30.03.2012
 * 
 */
public interface INotificationManager
{

	/** the singleton */
	public static final INotificationManager INSTANCE_RES_CHANGE_NOTIF_MGR = new ResourceChangeNotificationManagerImpl();
	public static final INotificationManager INSTANCE_MODEL_CHANGE_NOTIF_MGR = new ModelChangeNotificationManagerImpl();

	/**
	 * Register a callback that will receive notifications when changes occurre
	 * in the specified context. For {@link #INSTANCE_RES_CHANGE_NOTIF_MGR} the
	 * <code>context</code> is an object of type {@link Resource}. For
	 * {@link #INSTANCE_MODEL_CHANGE_NOTIF_MGR} it have to be an object of type
	 * {@link IProject}
	 * 
	 * @param context
	 * @param callback
	 */
	void register(Object context, INotificationCallback callBack);

	/**
	 * Unregisters a callback from the specified editing domain
	 */
	void unregister(INotificationCallback callBack);

}
