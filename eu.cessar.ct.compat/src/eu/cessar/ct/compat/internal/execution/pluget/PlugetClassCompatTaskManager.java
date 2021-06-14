/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Feb 1, 2010 10:37:55 AM </copyright>
 */
package eu.cessar.ct.compat.internal.execution.pluget;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.resources.IProject;

import eu.cessar.ct.compat.internal.CessarPluginActivator;
import eu.cessar.ct.runtime.execution.CessarTask;
import eu.cessar.ct.runtime.execution.ICessarTaskDescriptor;

/**
 * @author uidl6458
 * @Review uidl7321 - Apr 11, 2012
 */
public class PlugetClassCompatTaskManager extends AbstractCompatPlugetTaskManager<String>
{

	/**
	 * @param descriptor
	 * @param project
	 */
	public PlugetClassCompatTaskManager(ICessarTaskDescriptor descriptor, IProject project)
	{
		super(descriptor, project);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.runtime.execution.AbstractCessarTaskManager#canThisManagerWrite()
	 */
	@Override
	protected boolean canThisManagerWrite()
	{
		return true;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.runtime.execution.AbstractJobBasedTaskManager2#createCessarTask(java.lang.Object, java.lang.ClassLoader, java.lang.Object)
	 */
	@Override
	protected List<CessarTask<String>> createCessarTask(String input, Object parameter)
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
			CessarPluginActivator.getDefault().logError(
				"Pluget parameters should be null or a String[] but is a {0}", //$NON-NLS-1$
				parameter.getClass().getName());
			return Collections.emptyList();
		}
		List<CessarTask<String>> result = new ArrayList<CessarTask<String>>();
		result.add(new PlugetClassCompatTask(this, input, input,
			getExecutionLoader().getClassLoader(), getProject(), arguments));
		return result;
	}

}
