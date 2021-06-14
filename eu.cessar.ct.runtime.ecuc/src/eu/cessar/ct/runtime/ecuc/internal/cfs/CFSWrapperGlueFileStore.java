/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870 Oct 30, 2009 2:34:10 PM </copyright>
 */
package eu.cessar.ct.runtime.ecuc.internal.cfs;

import java.net.URI;

import org.eclipse.core.filesystem.IFileInfo;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.filesystem.provider.FileInfo;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

/**
 * 
 */
public class CFSWrapperGlueFileStore extends AbstractCFSFileStore
{

	/**
	 * @param name
	 * @param uri
	 */
	public CFSWrapperGlueFileStore(String name, URI uri)
	{
		super(name, uri);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.runtime.ecuc.internal.cfs.CFSFileStore#fetchInfo(int, org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	public IFileInfo fetchInfo(int options, IProgressMonitor monitor) throws CoreException
	{
		FileInfo fileInfo = new FileInfo();
		fileInfo.setExists(true);
		fileInfo.setDirectory(true);
		fileInfo.setName(getName());

		return fileInfo;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.core.filesystem.provider.FileStore#getChild(java.lang.String)
	 */
	@Override
	public IFileStore getChild(String name)
	{
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.core.filesystem.provider.FileStore#childNames(int, org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	public String[] childNames(int options, IProgressMonitor monitor) throws CoreException
	{
		// TODO Auto-generated method stub
		return new String[0];
	}

	/* (non-Javadoc)
	 * @see org.eclipse.core.filesystem.provider.FileStore#getParent()
	 */
	@Override
	public IFileStore getParent()
	{
		// TODO Auto-generated method stub
		return null;
	}

}
