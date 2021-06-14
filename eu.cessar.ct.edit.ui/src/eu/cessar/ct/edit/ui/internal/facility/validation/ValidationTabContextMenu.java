/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidv3687<br/>
 * Mar 15, 2013 2:21:43 PM
 * 
 * </copyright>
 */
package eu.cessar.ct.edit.ui.internal.facility.validation;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

/**
 * ContextMenu creates a context menu for a Tableviewer and copy to clipboard the selected rows
 * 
 * @author uidv3687
 * 
 *         %created_by: uidl6458 %
 * 
 *         %date_created: Wed Sep 17 14:46:15 2014 %
 * 
 *         %version: 1 %
 */

public class ValidationTabContextMenu
{

	private static final String MENU_ID = "Copy to Clipboard"; //$NON-NLS-1$

	private final Menu menu;

	/**
	 * ContextMenu constructor
	 * 
	 * @param viewer
	 *        providing the context menu
	 */
	public ValidationTabContextMenu(final ValidationTableViewer viewer)
	{
		menu = new Menu(viewer.getTable());
		MenuItem menuItem = new MenuItem(menu, SWT.BUTTON1);
		menuItem.setText(MENU_ID);
		menuItem.addListener(SWT.Selection, new Listener()
		{

			@Override
			public void handleEvent(Event event)
			{
				viewer.copyToClipboard();
			}
		});

		viewer.getControl().setMenu(menu);

	}
}
