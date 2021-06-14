/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidu0944 Dec 2, 2011 12:16:57 PM </copyright>
 */
package eu.cessar.ct.core.platform.ui.widgets.breadcrumb;

import org.eclipse.core.resources.IFile;

/**
 * @author uidu0944
 * 
 */
public class BreadcrumbFileChangeEvent
{

	private IFile selectedFile;

	public BreadcrumbFileChangeEvent(IFile selectedFile)
	{
		this.selectedFile = selectedFile;
	}

	/**
	 * @return the selectedFile
	 */
	public IFile getSelectedFile()
	{
		return selectedFile;
	}

}
