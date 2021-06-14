/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidl6870 06.08.2012 10:27:51 </copyright>
 */
package eu.cessar.ct.workspace.internal.consistencycheck.impl;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;

import eu.cessar.ct.workspace.consistencycheck.EProjectInconsistencyType;
import eu.cessar.ct.workspace.consistencycheck.IProjectCheckInconsistency;

/**
 * Implementation of an {@link IProjectCheckInconsistency}
 *
 * @author uidl7321
 *
 */
public class ProjectCheckInconsistency extends AbstractInconsistency implements IProjectCheckInconsistency
{
	private EProjectInconsistencyType inconsistencyType;
	private List<IFile> files;

	/**
	 *
	 */
	public ProjectCheckInconsistency()
	{
		files = new ArrayList<IFile>();
	}

	public EProjectInconsistencyType getInconsistencyType()
	{
		return inconsistencyType;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * eu.cessar.ct.workspace.consistencycheck.IProjectCheckInconsistency#setInconsistencyType(eu.cessar.ct.workspace
	 * .consistencycheck.EProjectInconsistencyType)
	 */
	public void setInconsistencyType(EProjectInconsistencyType inconsistencyType)
	{
		this.inconsistencyType = inconsistencyType;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.workspace.consistencycheck.IProjectCheckInconsistency#getFiles()
	 */
	@Override
	public List<IFile> getFiles()
	{
		return files;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.workspace.consistencycheck.IProjectCheckInconsistency#setFile(java.io.File)
	 */
	@Override
	public void addFiles(List<IFile> fileList)
	{
		files.addAll(fileList);

	}

	/**
	 * Add a single file to the inconsistency
	 * 
	 * @param fileList
	 */
	public void addFile(IFile fileList)
	{
		files.add(fileList);

	}
}
