/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidu0944 Nov 14, 2011 1:51:52 PM </copyright>
 */
package eu.cessar.ct.core.platform.ui.widgets.breadcrumb;

import org.eclipse.jface.viewers.ISelection;

/**
 * @author uidu0944
 * 
 */
public class BreadcrumbInputChangeEvent
{

	private ISelection selection;

	public BreadcrumbInputChangeEvent(ISelection selection)
	{
		this.selection = selection;
	}

	/**
	 * @return the selected EObject
	 */
	public ISelection getSelection()
	{
		return selection;
	}

}
