/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 *
 * </copyright>
 */
package eu.cessar.ct.compat.internal.execution.pluget;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.NullProgressMonitor;

import eu.cessar.ct.runtime.CessarRuntime;
import eu.cessar.ct.runtime.execution.AbstractJobBasedTaskManager;
import eu.cessar.ct.runtime.execution.ICessarTaskDescriptor;
import eu.cessar.ct.runtime.execution.IExecutionLoader;
import eu.cessar.ct.runtime.execution.IExecutionSupport;
import eu.cessar.ct.sdk.utils.ModelUtils;

/**
 * @author uidl7321
 * @Review uidl7321 - Apr 11, 2012
 */
public abstract class AbstractCompatPlugetTaskManager<T> extends AbstractJobBasedTaskManager<T>
{

	/**
	 * @param descriptor
	 * @param project
	 */
	public AbstractCompatPlugetTaskManager(ICessarTaskDescriptor descriptor, IProject project)
	{
		super(descriptor, project);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.runtime.execution.AbstractJobBasedTaskManager#doPostExecute()
	 */
	@Override
	protected void doPostExecute()
	{
		super.doPostExecute();
		IExecutionSupport executionSupport = CessarRuntime.getExecutionSupport();
		if (executionSupport != null)
		{
			IExecutionLoader activeExecutionLoader = executionSupport.getActiveExecutionLoader(getProject());
			if (activeExecutionLoader == null)
			{
				ModelUtils.saveResources(ModelUtils.getDirtyResources(getProject()), new NullProgressMonitor());
			}
		}

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.runtime.execution.AbstractJobBasedTaskManager#initPMUtilsVariables()
	 */
	@Override
	protected void initPMUtilsVariables()
	{
		// do nothing, no such variables on AR3
	}

}
