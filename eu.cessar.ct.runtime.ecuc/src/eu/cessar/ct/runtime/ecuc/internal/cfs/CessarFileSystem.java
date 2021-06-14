/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Oct 5, 2009 4:43:21 PM </copyright>
 */
package eu.cessar.ct.runtime.ecuc.internal.cfs;

import java.net.URI;

import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.filesystem.provider.FileSystem;
import org.eclipse.core.runtime.Path;

import eu.cessar.ct.runtime.ecuc.internal.cfs.util.CFSUtils;

/**
 * @author uidl6458
 * 
 */
public class CessarFileSystem extends FileSystem
{
	/* (non-Javadoc)
	 * @see org.eclipse.core.filesystem.provider.FileSystem#getStore(java.net.URI)
	 */
	@Override
	public IFileStore getStore(URI uri)
	{
		if (true)
		{
			return EFS.getNullFileSystem().getStore(uri);
		}
		// get store type
		ECFSTypeEnum storeType = CFSUtils.getCFSType(uri);

		Path path = new Path(uri.getSchemeSpecificPart());
		if (storeType.equals(ECFSTypeEnum.ROOT))
		{
			return new CFSProjectFileStore(path.segment(0), uri);
		}
		else if (storeType.equals(ECFSTypeEnum.WRAPPER_GLUE))
		{
			return new CFSWrapperGlueFileStore(path.lastSegment(), uri);
		}
		else if (storeType.equals(ECFSTypeEnum.WRAPPER_ARPACKAGE))
		{
			return new CFSWrapperARPackageFileStore(path.lastSegment(), uri);
		}
		else if (storeType.equals(ECFSTypeEnum.WRAPPER_MODULE_DEFINITION))
		{
			return new CFSWrapperModDefFileStore(path.lastSegment(), uri);
		}

		return EFS.getNullFileSystem().getStore(uri);
	}
}
