/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Oct 9, 2009 2:14:39 PM </copyright>
 */
package eu.cessar.ct.runtime.ecuc.internal;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.transaction.NotificationFilter;
import org.eclipse.emf.transaction.ResourceSetChangeEvent;
import org.eclipse.emf.transaction.ResourceSetListenerImpl;
import org.eclipse.sphinx.emf.util.EcorePlatformUtil;

import eu.cessar.ct.core.platform.nature.CessarNature;
import eu.cessar.ct.runtime.CessarRuntime;
import eu.cessar.ct.runtime.ecuc.IEcucCore;

/**
 * 
 */
public class EcucModelResourceListener extends ResourceSetListenerImpl
{

	public static final EcucModelResourceListener eINSTANCE = new EcucModelResourceListener();

	private EcucModelResourceListener()
	{
		// the result filter is:
		// RESOURCE_LOADED || RESOURCE_UNLOADED ||
		// (NOT_TOUNCH && partOfEcucModel)
		super(NotificationFilter.RESOURCE_LOADED.or(NotificationFilter.RESOURCE_UNLOADED).or(
			NotificationFilter.NOT_TOUCH.and(new EcucDefChangeNotificationFilter())));
	}

	/* (non-Javadoc)
	 * @see org.eclipse.emf.transaction.ResourceSetListenerImpl#isPostcommitOnly()
	 */
	@Override
	public boolean isPostcommitOnly()
	{
		return true;
	}

	/**
	 * Return a list of projects that the notifications from the event refer
	 * 
	 * @param event
	 * @return a list of projects, never null
	 */
	private IProject[] collectProjects(ResourceSetChangeEvent event)
	{
		Set<IProject> result = new HashSet<IProject>();
		List<Notification> notifications = event.getNotifications();
		if (notifications != null)
		{
			for (Notification notification: notifications)
			{
				IFile file = EcorePlatformUtil.getFile(notification.getNotifier());
				if (file != null)
				{
					IProject project = file.getProject();
					if (project.isAccessible() && CessarNature.haveNature(project))
					{
						result.add(project);
					}
				}
			}
		}
		return result.toArray(new IProject[result.size()]);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.emf.transaction.ResourceSetListenerImpl#resourceSetChanged(org.eclipse.emf.transaction.ResourceSetChangeEvent)
	 */
	@Override
	public void resourceSetChanged(ResourceSetChangeEvent event)
	{
		IProject[] projects = collectProjects(event);
		IEcucCore instance = IEcucCore.INSTANCE;
		if (projects != null && projects.length > 0)
		{
			((EcucCoreImpl) instance).modelChanged(projects);
			for (IProject project: projects)
			{
				CessarRuntime.resetModelContainer(project);
			}
		}
	}
}
