/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidu3379 25.09.2012 14:15:45 </copyright>
 */
package eu.cessar.ct.runtime.internal.extension;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResourceProxy;

/**
 * @author uidu3379
 * 
 */
public final class ExtensionFileManager extends AbstractResChangeListener
{
	/**
	 * Extension wrapper for CES and CED extensions. Please add new extensions
	 * here, if needed.
	 * 
	 * @author uidu3379
	 * 
	 */
	private enum ExtensionType
	{
		CES("ces"), CED("ced"); //$NON-NLS-1$ //$NON-NLS-2$

		private String extension;

		private ExtensionType(String extenstion)
		{
			this.extension = extenstion;
		}

		private String getExtenstion()
		{
			return extension;
		}
	}

	public static final ExtensionFileManager CES_INSTANCE = new ExtensionFileManager(ExtensionType.CES);
	public static final ExtensionFileManager CED_INSTANCE = new ExtensionFileManager(ExtensionType.CED);

	private ExtensionType extensionType;

	private ExtensionFileManager(ExtensionType extension)
	{
		this.extensionType = extension;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.runtime.internal.extension.AbstractResChangeListener#isApplicable(org.eclipse.core.resources.IFile)
	 */
	@Override
	protected boolean isApplicable(IFile iFile)
	{
		boolean hasCesExt = iFile.getName().endsWith(extensionType.getExtenstion());

		// TODO: check if it's indeed a jar file
		return hasCesExt;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.runtime.internal.extension.AbstractResChangeListener#isApplicable(org.eclipse.core.resources.IResourceProxy)
	 */
	@Override
	protected boolean isApplicable(IResourceProxy proxy)
	{
		return proxy.getName().endsWith(extensionType.getExtenstion());
	}

}
