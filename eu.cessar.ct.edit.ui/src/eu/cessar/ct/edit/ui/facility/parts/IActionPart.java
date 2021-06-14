/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Mar 2, 2010 7:37:32 PM </copyright>
 */
package eu.cessar.ct.edit.ui.facility.parts;

import eu.cessar.ct.edit.ui.facility.IModelFragmentEditor;

/**
 * Common interface of all parts that provide {@link IModelFragmentEditor} specific actions
 * 
 * @author uidl6458
 * 
 */
public interface IActionPart extends IModelFragmentEditorPart
{
	/**
	 * 
	 * @return the menu manager
	 */
	public ActionsMenuManager getMenuManager();
}
