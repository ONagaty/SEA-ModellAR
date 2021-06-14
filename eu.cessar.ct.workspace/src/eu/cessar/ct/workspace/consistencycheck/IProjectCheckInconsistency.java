/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870 03.08.2012 13:18:34 </copyright>
 */
package eu.cessar.ct.workspace.consistencycheck;

import java.util.List;

import org.eclipse.core.resources.IFile;

import eu.cessar.req.Requirement;

/**
 * The interface encapsulates the data describing an inconsistency that has been identified during the analysis of a
 * project, like duplicate module defs, files of wrong metamodel etc.
 * 
 * @author uidl7321
 */
@Requirement(
	reqID = "REQ_CHECK#5")
public interface IProjectCheckInconsistency extends IInconsistency
{
	/**
	 * Returns the type of the inconsistency
	 * 
	 * @return the type of inconsistency
	 */
	public EProjectInconsistencyType getInconsistencyType();

	/**
	 * Sets the type of inconsistency. Must not be called by clients
	 * 
	 * @param inconsistencyType
	 */
	public void setInconsistencyType(EProjectInconsistencyType inconsistencyType);

	/**
	 * Returns the files where the inconsistency was found
	 * 
	 * @return the list of files or an empty one
	 */
	public List<IFile> getFiles();

	/**
	 * Sets the file where the inconsistency was found. Must not be called by clients.
	 * 
	 * @param files
	 */
	public void addFiles(List<IFile> files);

}
