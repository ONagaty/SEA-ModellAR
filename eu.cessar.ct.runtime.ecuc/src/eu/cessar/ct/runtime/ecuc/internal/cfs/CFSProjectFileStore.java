/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870 Oct 30, 2009 1:03:10 PM </copyright>
 */
package eu.cessar.ct.runtime.ecuc.internal.cfs;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileInfo;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.filesystem.provider.FileInfo;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

import eu.cessar.ct.runtime.ecuc.internal.cfs.util.CFSUtils;

import gautosar.ggenericstructure.ginfrastructure.GARPackage;

public class CFSProjectFileStore extends AbstractCFSFileStore
{

	/**
	 * @param projectName
	 * @param uri
	 */
	public CFSProjectFileStore(String name, URI uri)
	{
		super(name, uri);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.core.filesystem.provider.FileStore#childNames(int, org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	public String[] childNames(int options, IProgressMonitor monitor) throws CoreException
	{
		List<String> list = new ArrayList<String>();

		Map<String, List<GARPackage>> map = getEcucModel().getRootArPackagesWithModuleDefs();
		Set<String> keySet = map.keySet();
		for (String rootPack: keySet)
		{
			// add root ar packages
			list.add(rootPack);
		}

		if (list.size() > 0)
		{
			list.add(CFSUtils.GLUE_FILE_NAME);
		}
		String[] result = new String[list.size()];
		list.toArray(result);
		return result;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.core.filesystem.provider.FileStore#fetchInfo(int, org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	public IFileInfo fetchInfo(int options, IProgressMonitor monitor) throws CoreException
	{
		FileInfo fileInfo = new FileInfo();
		fileInfo.setAttribute(EFS.ATTRIBUTE_HIDDEN, true);
		fileInfo.setDirectory(true);
		fileInfo.setExists(true);
		fileInfo.setName(getName());

		return fileInfo;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.core.filesystem.provider.FileStore#getChild(java.lang.String)
	 */
	@Override
	public IFileStore getChild(String name)
	{
		if (CFSUtils.GLUE_FILE_NAME.equals(name))
		{
			return new CFSWrapperGlueFileStore(name, CFSUtils.getChildURI(toURI(),
				CFSUtils.GLUE_FILE_NAME));
		}
		else
		{
			return new CFSWrapperARPackageFileStore(name, CFSUtils.getChildURI(toURI(), name));
		}
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
