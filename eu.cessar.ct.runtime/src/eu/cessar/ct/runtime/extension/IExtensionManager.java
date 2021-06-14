/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870 Nov 9, 2010 10:35:22 AM </copyright>
 */
package eu.cessar.ct.runtime.extension;

import org.eclipse.core.resources.IProject;

import eu.cessar.ct.runtime.internal.extension.ExtensionManager;

/**
 * CESSAR-CT extension manager
 * 
 */
public interface IExtensionManager
{
	public static final String VALIDATION_EXTENSION_ID = "eu.cessar.ct.validation.customValidator"; //$NON-NLS-1$

	public static final String[] EXTENSION_IDS = {VALIDATION_EXTENSION_ID};

	public static final IExtensionManager INSTANCE = new ExtensionManager();

	/**
	 * Get all contributions from the given <code>project</code> for the given
	 * <code>extPointId</code> . If there are no contributions, an empty array
	 * is returned
	 * 
	 * @param extPointId
	 * @return
	 */
	IExtension[] getExtensions(IProject project, String extPointId);
}
