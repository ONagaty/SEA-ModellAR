/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870 Oct 30, 2009 2:34:43 PM </copyright>
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
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;

import eu.cessar.ct.runtime.ecuc.internal.CessarPluginActivator;
import eu.cessar.ct.runtime.ecuc.internal.Messages;
import eu.cessar.ct.runtime.ecuc.internal.cfs.util.CFSUtils;
import gautosar.gecucparameterdef.GModuleDef;
import gautosar.ggenericstructure.ginfrastructure.GARPackage;

/**
 * 
 */
public class CFSWrapperARPackageFileStore extends AbstractCFSFileStore
{
	private String qName;

	/**
	 * @param name
	 * @param uri
	 */
	public CFSWrapperARPackageFileStore(String name, URI uri)
	{
		super(name, uri);
		qName = uri.getPath();
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
	 * @see eu.cessar.ct.runtime.ecuc.internal.cfs.CFSFileStore#childNames(int, org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	public String[] childNames(int options, IProgressMonitor monitor) throws CoreException
	{

		Map<String, List<GARPackage>> map = getEcucModel().getArPackagesWithModuleDefs(qName);

		Set<String> keySet = map.keySet();
		List<String> list = new ArrayList<String>();
		// add sub packages
		for (String packName: keySet)
		{
			if (packName != null)
			{
				list.add(packName);
			}
		}
		List<GModuleDef> moduleDefs = getEcucModel().getModuleDefsFromPackage(qName);
		for (GModuleDef md: moduleDefs)
		{
			// add direct module defs
			if (md.gGetShortName() != null)
			{
				list.add(md.gGetShortName());
			}
		}

		String[] result = new String[list.size()];
		list.toArray(result);
		return result;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.runtime.ecuc.internal.cfs.CFSFileStore#getChild(java.lang.String)
	 */
	@Override
	public IFileStore getChild(String name)
	{
		Assert.isNotNull(name);
		// check that the short name is valid
		if (!new Path(name).isValidSegment(name))
		{
			CessarPluginActivator.getDefault().logError(Messages.InvalidShortName, name);
			return EFS.getNullFileSystem().getStore(new Path(name));
		}
		String childQName = qName + "/" + name;

		// null or empty String short name
		if (name == null)
		{
			CessarPluginActivator.getDefault().logError(Messages.UnsetShortName, childQName);
			return EFS.getNullFileSystem().getStore(new Path(name));
		}

		// get store type
		ECFSTypeEnum storeType = CFSUtils.getCFSWrapperStoreType(getProject(), childQName);

		if (storeType.equals(ECFSTypeEnum.WRAPPER_ARPACKAGE))
		{
			return new CFSWrapperARPackageFileStore(name, CFSUtils.getChildURI(toURI(), name));
		}

		if (storeType.equals(ECFSTypeEnum.WRAPPER_MODULE_DEFINITION))
		{
			return new CFSWrapperModDefFileStore(name, CFSUtils.getChildURI(toURI(), name));
		}

		// UNKNOWN store type
		return EFS.getNullFileSystem().getStore(new Path(name));

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
