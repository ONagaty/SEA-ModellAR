/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870 03.08.2012 13:18:34 </copyright>
 */
package eu.cessar.ct.workspace.consistencycheck;

/**
 * The interface encapsulates the data describing an XML inconsistency that has been identified during different
 * consistency check actions.
 * 
 * @author uidl6870
 */
public interface IInconsistency
{

	/**
	 * Returns the severity assigned to the inconsistency.
	 * 
	 * @return the severity
	 */
	public ESeverity getSeverity();

	/**
	 * Sets the severity of the inconsistency. Must not be called by clients
	 * 
	 * @param severity
	 */
	public void setSeverity(ESeverity severity);

	/**
	 * Returns a comment giving a brief explanation of the problem
	 * 
	 * @return the description
	 */
	public String getMessage();

	/**
	 * Sets a comment for the inconsistency. Must not be called by clients
	 * 
	 * @param comment
	 */
	public void setMessage(String comment);

}
