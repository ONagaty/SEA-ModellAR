/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870 01.08.2012 15:30:08 </copyright>
 */
package eu.cessar.ct.workspace.internal.xmlchecker.impl;

import java.util.ArrayList;
import java.util.List;

import eu.cessar.ct.workspace.internal.consistencycheck.impl.AbstractConsistencyCheckResult;
import eu.cessar.ct.workspace.xmlchecker.IXMLCheckerInconsistency;

/**
 * 
 * @author uidl6870
 * 
 */
public class XMLConsistencyCheckResult extends AbstractConsistencyCheckResult<IXMLCheckerInconsistency>
{
	private List<IXMLCheckerInconsistency> inconsistencies;

	/**
	 * 
	 */
	public XMLConsistencyCheckResult()
	{
		inconsistencies = new ArrayList<IXMLCheckerInconsistency>();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.workspace.consistencycheck.IConsistencyCheckResult#getInconsistencies()
	 */
	@Override
	public List<IXMLCheckerInconsistency> getInconsistencies()
	{
		return inconsistencies;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.workspace.consistencycheck.IConsistencyCheckResult#setInconsistencies(java.util.List)
	 */
	@Override
	public void setInconsistencies(List<IXMLCheckerInconsistency> inconsistencies)
	{
		this.inconsistencies.addAll(inconsistencies);

	}

}
