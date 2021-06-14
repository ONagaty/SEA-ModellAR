/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidu0944 Dec 2, 2011 12:15:05 PM </copyright>
 */
package eu.cessar.ct.core.platform.ui.events;

import eu.cessar.ct.core.platform.ui.widgets.breadcrumb.BreadcrumbFileChangeEvent;

/**
 * @author uidu0944
 * 
 */
public interface IBreadcrumbFileChangeListener extends IBreadcrumbChangeListener
{
	void inputFileChanged(BreadcrumbFileChangeEvent event);
}
