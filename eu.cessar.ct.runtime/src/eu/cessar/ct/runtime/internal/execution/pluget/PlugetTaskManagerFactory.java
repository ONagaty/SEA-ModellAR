/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870 Jan 13, 2010 3:55:37 PM </copyright>
 */
package eu.cessar.ct.runtime.internal.execution.pluget;

import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;

import eu.cessar.ct.core.mms.ICompatibilityService;
import eu.cessar.ct.core.mms.IMetaModelService;
import eu.cessar.ct.core.mms.MMSRegistry;
import eu.cessar.ct.core.platform.CESSARPreferencesAccessor;
import eu.cessar.ct.core.platform.ECompatibilityMode;
import eu.cessar.ct.runtime.execution.AbstractTaskManagerFactory;
import eu.cessar.ct.runtime.execution.ICessarTaskManagerImpl;
import eu.cessar.ct.sdk.runtime.ExecutionService;

/**
 * @author uidl6870
 * 
 */
public class PlugetTaskManagerFactory extends AbstractTaskManagerFactory<IFile>
{

	/* (non-Javadoc)
	 * @see eu.cessar.ct.runtime.execution.ICessarTaskManagerFactory#createManager(org.eclipse.core.resources.IProject, java.util.Map)
	 */
	public ICessarTaskManagerImpl<IFile> createManager(IProject project, Map<String, Object> map)
	{
		ECompatibilityMode compatibilityMode = CESSARPreferencesAccessor.getCompatibilityMode(project);
		if (compatibilityMode == ECompatibilityMode.FULL)
		{// need to locate the compatibility mode task manager
			IMetaModelService mmService = MMSRegistry.INSTANCE.getMMService(project);
			if (mmService != null)
			{
				ICompatibilityService compatibilityService = mmService.getCompatibilityService();
				if (compatibilityService != null)
				{
					String taskName = compatibilityService.getPlugetTaskTypeName();
					return (ICessarTaskManagerImpl<IFile>) ExecutionService.createManager(project,
						taskName, map);
				}
			}
		}
		PlugetTaskManager manager = new PlugetTaskManager(getDescriptor(), project);
		return manager;
	}

}
