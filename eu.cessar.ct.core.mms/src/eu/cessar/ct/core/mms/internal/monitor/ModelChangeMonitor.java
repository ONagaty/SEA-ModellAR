/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458<br/>
 * 03.09.2013 12:22:20
 * 
 * </copyright>
 */
package eu.cessar.ct.core.mms.internal.monitor;

import java.util.Hashtable;
import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.transaction.TransactionalEditingDomain;

import eu.cessar.ct.core.mms.IModelChangeMonitor;
import eu.cessar.ct.core.mms.MetaModelUtils;
import eu.cessar.ct.core.mms.internal.CessarPluginActivator;
import eu.cessar.ct.core.mms.internal.Messages;
import eu.cessar.ct.core.platform.util.IModelChangeStampProvider;

/**
 * This class provide AUTOSAR metamodel monitoring service.
 * 
 * @author uidl6458
 * 
 *         %created_by: uidu2337 %
 * 
 *         %date_created: Mon Mar 10 15:26:17 2014 %
 * 
 *         %version: 3 %
 */
public final class ModelChangeMonitor implements IModelChangeMonitor
{

	private volatile Map<IProject, IModelChangeStampProvider> stamps;

	/**
	 * Initialize the map
	 */
	private void checkInit()
	{
		if (stamps == null)
		{
			synchronized (this)
			{
				if (stamps == null)
				{
					// create Hashtable instead of HashMap to be able to work in a multi-threaded environment
					stamps = new Hashtable<IProject, IModelChangeStampProvider>();
					ResourcesPlugin.getWorkspace().addResourceChangeListener(new IResourceChangeListener()
					{

						@Override
						public void resourceChanged(IResourceChangeEvent event)
						{
							stamps.remove(event.getResource());
						}

					}, IResourceChangeEvent.PRE_CLOSE | IResourceChangeEvent.PRE_DELETE);
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.cessar.ct.core.mms.monitor.IModelChangeMonitor#getChangeStampProvider(org.eclipse.core.resources.IProject)
	 */
	@Override
	public IModelChangeStampProvider getChangeStampProvider(IProject project)
	{
		checkInit();
		if (project == null)
		{
			return null;
		}
		else
		{
			synchronized (project)
			{
				IModelChangeStampProvider result;
				if (stamps.containsKey(project))
				{
					result = stamps.get(project);
				}
				else
				{
					result = getModelChangeProvider(project);
					stamps.put(project, result);
				}
				return result;
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.core.mms.IModelChangeMonitor#getChangeStampProvider(org.eclipse.emf.ecore.EObject)
	 */
	@Override
	public IModelChangeStampProvider getChangeStampProvider(Object object)
	{
		return getChangeStampProvider(MetaModelUtils.getProject(object));
	}

	/**
	 * @param project
	 * @return
	 */
	private IModelChangeStampProvider getModelChangeProvider(IProject project)
	{
		TransactionalEditingDomain domain = MetaModelUtils.getEditingDomain(project);
		if (domain == null)
		{
			CessarPluginActivator.getDefault().logError(Messages.MONITOR_NO_EDITING_DOMAIN, project);
			return null;
		}
		ResourceSet resourceSet = domain.getResourceSet();
		if (!(resourceSet instanceof IModelChangeStampProvider))
		{
			CessarPluginActivator.getDefault().logError(Messages.RESOURCESET_NOT_IModelChangeStampProvider, resourceSet);
			return null;
		}
		return (IModelChangeStampProvider) resourceSet;
	}
}
