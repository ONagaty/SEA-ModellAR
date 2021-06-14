/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870 Nov 9, 2010 11:19:17 AM </copyright>
 */
package eu.cessar.ct.runtime.internal.extension;

import org.eclipse.core.resources.IFolder;

import eu.cessar.ct.runtime.extension.IExtension;

/**
 * @author uidl6870
 * 
 */
public class ExtensionDescriptor
{
	/**
	 * Unique id set at .ced file creation
	 */
	private String uuid;

	/**
	 * Folder specified as java source folder
	 */
	private IFolder source;

	/**
	 * The id of the extension point
	 */
	private String extensionPointID;

	/**
	 * The specified extension
	 */
	private IExtension extension;

	public String getUUID()
	{

		return uuid;
	}

	public void setUUID(String uuid)
	{
		this.uuid = uuid;
	}

	public IFolder getSourceFolder()
	{
		return source;
	}

	public void setSourceFolder(IFolder folder)
	{
		source = folder;
	}

	public String getExtensionPointID()
	{

		return extensionPointID;
	}

	public void setExtensionPointID(String extensionPointID)
	{
		this.extensionPointID = extensionPointID;
	}

	public IExtension getExtension()
	{
		return extension;
	}

	public void setExtension(IExtension extension)
	{
		this.extension = extension;
	}

	public boolean isValid()
	{
		return uuid != null && source != null && extension != null;
	}
}
