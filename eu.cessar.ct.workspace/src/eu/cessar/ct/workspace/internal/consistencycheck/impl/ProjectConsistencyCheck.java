/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl7321<br/>
 * Feb 24, 2014 5:21:49 PM
 * 
 * </copyright>
 */
package eu.cessar.ct.workspace.internal.consistencycheck.impl;

import eu.cessar.ct.workspace.consistencycheck.IProjectConsistencyCheck;
import eu.cessar.ct.workspace.consistencycheck.IProjectConsistencyChecker;
import eu.cessar.ct.workspace.internal.consistencycheck.contrib.ProjectConsistencyCheckElemDef;

/**
 * Implementation of {@link IProjectConsistencyCheck}.
 * 
 * @author uidl7321
 * 
 *         %created_by: uidl7321 %
 * 
 *         %date_created: Tue Mar  4 17:33:32 2014 %
 * 
 *         %version: 2 %
 */
public class ProjectConsistencyCheck implements IProjectConsistencyCheck
{
	private ProjectConsistencyCheckElemDef elemDef;

	/**
	 * 
	 * @param elemDef
	 */
	public ProjectConsistencyCheck(ProjectConsistencyCheckElemDef elemDef)
	{
		this.elemDef = elemDef;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.workspace.consistencycheck.IProjectConsistencyCheck#getName()
	 */
	@Override
	public String getName()
	{
		return elemDef.getName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.workspace.consistencycheck.IProjectConsistencyCheck#getConsistencyChecker()
	 */
	@Override
	public IProjectConsistencyChecker getConsistencyChecker()
	{
		return elemDef.createCheckerClass();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.workspace.consistencycheck.IProjectConsistencyCheck#getId()
	 */
	@Override
	public String getId()
	{
		return elemDef.getId();
	}

}
