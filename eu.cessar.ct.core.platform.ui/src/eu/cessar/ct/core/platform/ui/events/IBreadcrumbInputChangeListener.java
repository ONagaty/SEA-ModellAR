/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidu0944 Nov 14, 2011 1:51:26 PM </copyright>
 */
package eu.cessar.ct.core.platform.ui.events;

import eu.cessar.ct.core.platform.ui.widgets.breadcrumb.BreadcrumbInputChangeEvent;

/**
 * @author uidu0944
 * 
 */
public interface IBreadcrumbInputChangeListener extends IBreadcrumbChangeListener
{
	void inputBreadcrumbSelectionChanged(BreadcrumbInputChangeEvent event);
}
