/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870 01.08.2012 15:30:08 </copyright>
 */
package eu.cessar.ct.workspace.internal.consistencycheck.impl;

import org.eclipse.core.runtime.IStatus;

import eu.cessar.ct.workspace.consistencycheck.IConsistencyCheckResult;
import eu.cessar.ct.workspace.consistencycheck.IInconsistency;

/**
 * Abstract implementation of {@link IConsistencyCheckResult}.
 * 
 * @author uidl7321
 * @param <T>
 * 
 */
public abstract class AbstractConsistencyCheckResult<T extends IInconsistency> implements IConsistencyCheckResult<T>
{
	private IStatus status;

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.workspace.xmlchecker.IConsistencyCheckResult#getStatus()
	 */
	public IStatus getStatus()
	{
		return status;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.workspace.xmlchecker.IConsistencyCheckResult#setStatus(org.eclipse.core.runtime.IStatus)
	 */
	public void setStatus(IStatus status)
	{
		this.status = status;

	}

}
