/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl7321<br/>
 * Feb 24, 2014 3:45:13 PM
 * 
 * </copyright>
 */
package eu.cessar.ct.workspace.consistencycheck;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;


/**
 * Implementation of {@link IProjectConsistencyCheckerManager}.
 * 
 * @author uidl7321
 * 
 *         %created_by: uidw8762 %
 * 
 *         %date_created: Wed Mar 19 16:16:32 2014 %
 * 
 *         %version: 1 %
 */
public class ProjectConsistencyCheckerManager implements IProjectConsistencyCheckerManager
{

	private IProjectConsistencyCheckRegistry registry = IProjectConsistencyCheckRegistry.INSTANCE;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.cessar.ct.workspace.consistencycheck.IProjectConsistencyCheckerManager#performConsistencyCheck(org.eclipse
	 * .core.resources.IProject, org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	public List<IConsistencyCheckResult<IProjectCheckInconsistency>> performConsistencyCheck(IProject project,
		IProgressMonitor monitor)
	{
		List<IConsistencyCheckResult<IProjectCheckInconsistency>> result = new ArrayList<IConsistencyCheckResult<IProjectCheckInconsistency>>();

		List<IProjectConsistencyChecker> checkerList = registry.getConsistencyCheckers();

		for (IProjectConsistencyChecker checker: checkerList)
		{
			result.addAll(checker.performConsistencyCheck(project));
		}

		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.cessar.ct.workspace.consistencycheck.IProjectConsistencyCheckerManager#performConsistencyCheck(org.eclipse
	 * .core.resources.IProject, java.lang.String, org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	public List<IConsistencyCheckResult<IProjectCheckInconsistency>> performConsistencyCheck(IProject project,
		String id, IProgressMonitor monitor)
	{
		List<IConsistencyCheckResult<IProjectCheckInconsistency>> result = new ArrayList<IConsistencyCheckResult<IProjectCheckInconsistency>>();

		IProjectConsistencyChecker checker = registry.getConsistencyChecker(id);

		if (checker != null)
		{
			result.addAll(checker.performConsistencyCheck(project));
		}

		return result;
	}
}
