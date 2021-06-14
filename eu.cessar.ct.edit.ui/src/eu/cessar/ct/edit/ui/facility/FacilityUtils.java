/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl7321 Mar 18, 2010 12:21:06 PM </copyright>
 */
package eu.cessar.ct.edit.ui.facility;

import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Widget;

import eu.cessar.ct.edit.ui.internal.facility.FacilityConstants;

/**
 * @author uidl7321
 * 
 */
public final class FacilityUtils
{
	// non-instantiable
	private FacilityUtils()
	{
	}

	/**
	 * @param e
	 * @return
	 */
	public static IModelFragmentEditor getEditor(Event e)
	{
		Widget widget = e.widget;

		if (widget instanceof MenuItem)
		{
			MenuItem menuItem = (MenuItem) widget;
			Menu menu = menuItem.getParent();

			// search for root menu
			if (menu != null)
			{
				Menu parentMenu;
				while ((parentMenu = menu.getParentMenu()) != null)
				{
					menu = parentMenu;
				}

				// menu = root
				Object data = menu.getData(FacilityConstants.EDITOR_ID_KEY);
				if (data instanceof IModelFragmentEditor)
				{
					return (IModelFragmentEditor) data;
				}
			}
		}
		return null;
	}
}
