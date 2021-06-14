/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870 26.07.2012 10:42:38 </copyright>
 */
package eu.cessar.ct.workspace.consistencycheck;

import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;

import eu.cessar.req.Requirement;

/**
 * Manager responsible with running all the project consistency checks that are registered in the plug-in registry.<br>
 * The main usage is to discover potential issues like duplicate module defs, files with wrong metamodel etc.
 * 
 * 
 * @author uidl7321
 * 
 */
@Requirement(
	reqID = "REQ_CHECK#5")
public interface IProjectConsistencyCheckerManager
{
	/**
	 * Runs all the registered consistency checks on the given <code>project</code>.
	 * 
	 * @param project
	 *        the project on which to perform the consistency checks
	 * 
	 * @param monitor
	 *        monitor to which progress will be reported
	 * @return a list containing the results of running the check on the files of the given project
	 */
	public List<IConsistencyCheckResult<IProjectCheckInconsistency>> performConsistencyCheck(IProject project,
		IProgressMonitor monitor);

	/**
	 * Runs the consistency check with the given <code>id</code> on the given <code>project</code>.
	 * 
	 * @param project
	 *        the project on which to perform the consistency checks
	 * @param id
	 *        the unique identifier of the consistency check to run
	 * 
	 * @param monitor
	 *        monitor to which progress will be reported
	 * @return a list containing the results of running the check on the files of the given project
	 */
	public List<IConsistencyCheckResult<IProjectCheckInconsistency>> performConsistencyCheck(IProject project,
		String id, IProgressMonitor monitor);

}
