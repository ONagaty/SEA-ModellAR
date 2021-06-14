/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870 Nov 9, 2010 11:12:27 AM </copyright>
 */
package eu.cessar.ct.runtime.internal.extension;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;

import eu.cessar.ct.runtime.extension.IExtension;
import eu.cessar.ct.runtime.extension.IExtensionManager;

/**
 * @author uidl6870
 * 
 */
public class ExtensionManager implements IExtensionManager
{

	// temp
	private boolean deployed = false;

	public ExtensionManager()
	{
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.runtime.extension.IExtensionManager#getExtensions(org.eclipse.core.resources.IProject, java.lang.String)
	 */
	public synchronized IExtension[] getExtensions(IProject project, String extPointId)
	{
		List<IExtension> result = new ArrayList<IExtension>();

		if (!deployed)
		{
			IFile[] iFiles = ExtensionFileManager.CED_INSTANCE.getIFiles(project);
			for (IFile iFile: iFiles)
			{
				ExtensionDescriptor extensionDesc = ExtensionDescriptorBuilder.INSTANCE.createExtensionDesc(iFile);
				if (extensionDesc.isValid())
				{
					result.add(extensionDesc.getExtension());
				}
			}
		}
		else
		{
			// TO DO
			IFile[] iFiles = ExtensionFileManager.CES_INSTANCE.getIFiles(project);
		}
		return result.toArray(new IExtension[result.size()]);
	}

}
