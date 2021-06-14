/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870<br/>
 * 10.07.2013 11:34:46
 * 
 * </copyright>
 */
package eu.cessar.ct.core.mms.internal.instanceref.impl;

import java.util.List;

import eu.cessar.ct.core.mms.instanceref.IContextValue;
import eu.cessar.ct.core.mms.instanceref.IInstanceReferenceCandidate;
import gautosar.ggenericstructure.ginfrastructure.GIdentifiable;

/**
 * 
 * @author uidl6870
 * 
 *         %created_by: uidl6870 %
 * 
 *         %date_created: Mon Jul 15 18:12:02 2013 %
 * 
 *         %version: 1 %
 */
public class InstanceReferenceCandidate implements IInstanceReferenceCandidate
{
	private final GIdentifiable target;
	private final List<IContextValue> contextValues;
	private final boolean complete;

	/**
	 * @param target
	 * @param contextValues
	 * @param complete
	 */
	public InstanceReferenceCandidate(GIdentifiable target, List<IContextValue> contextValues, boolean complete)
	{
		this.target = target;
		this.contextValues = contextValues;
		this.complete = complete;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.core.mms.instanceref.IInstanceReferenceCandidate#getTarget()
	 */
	@Override
	public GIdentifiable getTarget()
	{
		return target;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.core.mms.instanceref.IInstanceReferenceCandidate#getContextValues()
	 */
	@Override
	public List<IContextValue> getContextValues()
	{
		return contextValues;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.core.mms.instanceref.IInstanceReferenceCandidate#isComplete()
	 */
	@Override
	public boolean isComplete()
	{
		return complete;
	}

}
