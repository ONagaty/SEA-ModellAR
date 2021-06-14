/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870<br/>
 * 10.07.2013 11:20:40
 * 
 * </copyright>
 */
package eu.cessar.ct.core.mms.instanceref;

import gautosar.ggenericstructure.ginfrastructure.GIdentifiable;

import java.util.List;

/**
 * Wraps a candidate for configuring an instance reference
 * 
 * @author uidl6870
 * 
 *         %created_by: uidl6870 %
 * 
 *         %date_created: Mon Jul 15 10:35:08 2013 %
 * 
 *         %version: 1 %
 */
public interface IInstanceReferenceCandidate
{
	/**
	 * @return the candidate for instance reference's target
	 */
	public GIdentifiable getTarget();

	/**
	 * Get the list with context values towards the target candidate.
	 * 
	 * @return list with candidates for the instance reference's contexts
	 */
	public List<IContextValue> getContextValues();

	/**
	 * Returns whether this candidate completely configures the instance reference
	 * 
	 * @return <code>true</code> if all instance reference's contexts can be configured by choosing this candidate,
	 *         <code>false</code> otherwise
	 */
	public boolean isComplete();

}
