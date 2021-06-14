/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl7321<br/>
 * Feb 24, 2014 5:17:37 PM
 * 
 * </copyright>
 */
package eu.cessar.ct.workspace.consistencycheck;

import eu.cessar.req.Requirement;

/**
 * Interface representing a project consistency check.
 * 
 * @author uidl7321
 * 
 *         %created_by: uidw8762 %
 * 
 *         %date_created: Wed Mar 19 13:40:52 2014 %
 * 
 *         %version: 3 %
 */
@Requirement(
	reqID = "REQ_CHECK#5")
public interface IProjectConsistencyCheck
{

	/**
	 * Returns the unique identifier of this element.
	 * 
	 * @return the unique identifier
	 */
	String getId();

	/**
	 * Returns a suggestive name of the check.
	 * 
	 * @return the name
	 */
	public String getName();

	/**
	 * Returns the entity that actually performs the logic behind the check.
	 * 
	 * @return the checker
	 */
	public IProjectConsistencyChecker getConsistencyChecker();
}
