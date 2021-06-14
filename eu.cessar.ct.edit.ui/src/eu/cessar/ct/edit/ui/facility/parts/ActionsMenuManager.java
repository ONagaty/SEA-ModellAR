/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl7321 Mar 18, 2010 11:26:46 AM </copyright>
 */
package eu.cessar.ct.edit.ui.facility.parts;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;

import eu.cessar.ct.edit.ui.facility.IModelFragmentEditor;
import eu.cessar.ct.edit.ui.internal.facility.FacilityConstants;

/**
 * Actions menu manager
 * 
 */
public class ActionsMenuManager extends MenuManager
{
	private IModelFragmentEditor editor;

	/**
	 * @param editor
	 */
	public ActionsMenuManager(IModelFragmentEditor editor)
	{
		super();
		this.editor = editor;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.action.MenuManager#createContextMenu(org.eclipse.swt.widgets.Control)
	 */
	@Override
	public Menu createContextMenu(Control parent)
	{
		Menu menu = super.createContextMenu(parent);
		menu.setData(FacilityConstants.EDITOR_ID_KEY, editor);
		return menu;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.action.MenuManager#fill(org.eclipse.swt.widgets.Menu, int)
	 */
	@Override
	public void fill(Menu parent, int index)
	{
		super.fill(parent, index);
		Menu menu = getMenu();
		menu.setData(FacilityConstants.EDITOR_ID_KEY, editor);
	}

	/**
	 * @return
	 */
	public IModelFragmentEditor getEditor()
	{
		return editor;
	}
}
