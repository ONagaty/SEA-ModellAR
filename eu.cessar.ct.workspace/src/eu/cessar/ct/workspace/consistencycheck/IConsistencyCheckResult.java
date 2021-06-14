/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870 26.07.2012 10:53:51 </copyright>
 */
package eu.cessar.ct.workspace.consistencycheck;

import java.util.List;

import org.eclipse.core.runtime.IStatus;

/**
 * It encapsulates the result of running the consistency check operation on a AUTOSAR XML file
 * 
 * @author uidl6870
 * @param <T>
 * 
 */
public interface IConsistencyCheckResult<T extends IInconsistency>
{
	/**
	 * Returns the identified loading-saving inconsistencies. If no inconsistency is found, an empty list is returned.
	 * 
	 * @return the list of inconsistencies or an empty one
	 */
	public List<T> getInconsistencies();

	/**
	 * Sets the identified inconsistencies during the consistency check.
	 * 
	 * @param inconsistency
	 */
	public void setInconsistencies(List<T> inconsistency);

	/**
	 * Return a status indicating whether the checking operation has finished normally or an error occurred. <br>
	 * Clients should check first the status of the result and only if it is {@link IStatus#OK}, to further ask for the
	 * possible inconsistencies, if any.
	 * 
	 * @return the status
	 */
	public IStatus getStatus();

	/**
	 * Sets the status of the consistency check operation. Must not be called by clients.
	 * 
	 * @param status
	 */
	public void setStatus(IStatus status);

}
