/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl7321<br/>
 * Feb 21, 2014 5:55:43 PM
 * 
 * </copyright>
 */
package eu.cessar.ct.workspace.consistencycheck;

import java.util.List;

import org.eclipse.core.resources.IProject;

import eu.cessar.req.Requirement;

/**
 * Interface for the implementation of the logic of a consistency check.
 * 
 * @author uidl7321
 * 
 *         %created_by: uidw8762 %
 * 
 *         %date_created: Wed Mar 19 13:40:52 2014 %
 * 
 *         %version: 2 %
 */
@Requirement(
	reqID = "REQ_CHECK#5")
public interface IProjectConsistencyChecker
{

	/**
	 * Performs the check on the given <code>oroject</code> and returns a list of inconsistencies result, with
	 * additional details.
	 * 
	 * @param project
	 *        the project on which the check is run
	 * @param monitor
	 *        the progress monitor
	 * @return list of results or an empty one
	 */
	public List<IConsistencyCheckResult<IProjectCheckInconsistency>> performConsistencyCheck(IProject project);
}
