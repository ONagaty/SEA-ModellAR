/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidg4449<br/>
 * Feb 25, 2014 11:42:11 AM
 * 
 * </copyright>
 */
package eu.cessar.ct.runtime.internal.execution.pluget;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.resources.IProject;

import eu.cessar.ct.runtime.execution.AbstractJobBasedTaskManager;
import eu.cessar.ct.runtime.execution.CessarTask;
import eu.cessar.ct.runtime.execution.ICessarTaskDescriptor;
import eu.cessar.ct.runtime.internal.CessarPluginActivator;
import eu.cessar.ct.runtime.internal.Messages;

/**
 * Manager of {@link PlugetExternalTask} extends {@link AbstractJobBasedTaskManager}
 * 
 * @author uidg4449
 * 
 *         %created_by: uidg4449 %
 * 
 *         %date_created: Mon Mar 10 09:27:32 2014 %
 * 
 *         %version: 2 %
 */
public class PlugetExternalTaskManager extends AbstractJobBasedTaskManager<URL>
{

	/**
	 * @param descriptor
	 * @param project
	 */
	public PlugetExternalTaskManager(ICessarTaskDescriptor descriptor, IProject project)
	{
		super(descriptor, project);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.runtime.execution.AbstractJobBasedTaskManager#createCessarTask(java.lang.Object,
	 * java.lang.Object)
	 */
	@Override
	protected List<CessarTask<URL>> createCessarTask(URL input, Object parameter)
	{
		String[] arguments;
		if (parameter == null)
		{
			arguments = new String[0];
		}
		else if (parameter instanceof String[])
		{
			arguments = (String[]) parameter;
		}
		else
		{
			// parameter is not of the right type
			CessarPluginActivator.getDefault().logError(Messages.InvalidPlugetParameter, parameter.getClass().getName());
			return Collections.emptyList();
		}
		List<CessarTask<URL>> result = new ArrayList<CessarTask<URL>>();
		result.add(new PlugetExternalTask(this, input.toString(), input, getExecutionLoader().getClassLoader(),
			getProject(), arguments));
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.runtime.execution.AbstractCessarTaskManager#canThisManagerWrite()
	 */
	@Override
	protected boolean canThisManagerWrite()
	{
		return true;
	}

}
