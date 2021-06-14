/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870 01.08.2012 15:30:08 </copyright>
 */
package eu.cessar.ct.workspace.internal.consistencycheck.impl;

import java.util.ArrayList;
import java.util.List;

import eu.cessar.ct.workspace.consistencycheck.IProjectCheckInconsistency;

/**
 * 
 * @author uidl6870
 * 
 */
public class ProjectConsistencyCheckResult extends AbstractConsistencyCheckResult<IProjectCheckInconsistency>
{
	private List<IProjectCheckInconsistency> inconsistencies;

	/**
	 * 
	 */
	public ProjectConsistencyCheckResult()
	{
		inconsistencies = new ArrayList<IProjectCheckInconsistency>();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.workspace.consistencycheck.IConsistencyCheckResult#getInconsistencies()
	 */
	@Override
	public List<IProjectCheckInconsistency> getInconsistencies()
	{
		return inconsistencies;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.workspace.consistencycheck.IConsistencyCheckResult#setInconsistencies(java.util.List)
	 */
	@Override
	public void setInconsistencies(List<IProjectCheckInconsistency> inconsistencies)
	{
		this.inconsistencies.addAll(inconsistencies);

	}

}
