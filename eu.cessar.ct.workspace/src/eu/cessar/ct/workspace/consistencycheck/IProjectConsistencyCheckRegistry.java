/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl7321<br/>
 * Feb 21, 2014 6:28:30 PM
 * 
 * </copyright>
 */
package eu.cessar.ct.workspace.consistencycheck;

import java.util.List;

import eu.cessar.ct.workspace.internal.consistencycheck.ProjectConsistencyCheckRegistry;
import eu.cessar.req.Requirement;

/**
 * Registry that contains details about the available consistency checkers of a project.
 * 
 * @author uidl7321
 * 
 *         %created_by: uidw8762 %
 * 
 *         %date_created: Wed Mar 19 13:40:53 2014 %
 * 
 *         %version: 3 %
 */
@Requirement(
	reqID = "REQ_CHECK#5")
public interface IProjectConsistencyCheckRegistry
{
	/**
	 * Instance of the consistency check registry.
	 */
	public static final IProjectConsistencyCheckRegistry INSTANCE = new ProjectConsistencyCheckRegistry();

	/**
	 * Returns all the registered consistency checks in the plug-in registry.
	 * 
	 * @return the list of consistency checks or an empty one
	 */
	public List<IProjectConsistencyCheck> getRegisteredConsistencyChecks();

	/**
	 * Returns the checker entity for the given <code>project</code> and for the consistency check with the given
	 * <code>id</code>.
	 * 
	 * @param project
	 *        the project on which the consistency checks will be run
	 * @param id
	 *        the unique identifier of the consistency check
	 * @return consistency check or <code>null</code>
	 */
	public IProjectConsistencyCheck getRegisteredConsistencyCheck(String id);

	/**
	 * Returns all the checkers entities for the registered consistency checks.
	 * 
	 * 
	 * @return the list of consistency checkers or an empty one
	 */
	public List<IProjectConsistencyChecker> getConsistencyCheckers();

	/**
	 * Returns the checker entity for the consistency check with the given <code>id</code>.
	 * 
	 * @param id
	 *        the unique identifier of the consistency check
	 * @return consistency checker or <code>null</code>
	 */
	public IProjectConsistencyChecker getConsistencyChecker(String id);

}
