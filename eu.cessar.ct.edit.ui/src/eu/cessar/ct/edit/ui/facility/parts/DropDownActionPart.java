/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Mar 4, 2010 11:26:07 AM </copyright>
 */
package eu.cessar.ct.edit.ui.facility.parts;

import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.menus.IMenuService;

import eu.cessar.ct.core.platform.ui.IdentificationUtils;
import eu.cessar.ct.edit.ui.facility.IModelFragmentEditor;
import eu.cessar.ct.edit.ui.facility.parts.editor.AbstractEditorPart;
import eu.cessar.ct.edit.ui.internal.CessarPluginActivator;

/**
 * @author uidl6458
 * 
 */
public class DropDownActionPart extends AbstractEditorPart implements IActionPart, IMenuListener
{
	private Control action;
	private ActionsMenuManager menuManager;

	/**
	 * @param editor
	 */
	public DropDownActionPart(IModelFragmentEditor editor)
	{
		super(editor);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.facility.parts.IActionPart.IActionPart2#getMenuManager()
	 */
	public ActionsMenuManager getMenuManager()
	{
		if (menuManager == null)
		{
			menuManager = new ActionsMenuManager(getEditor());

			menuManager.add(new Separator("additions")); //$NON-NLS-1$
			menuManager.setRemoveAllWhenShown(true);
			menuManager.addMenuListener(this);
		}
		return menuManager;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.action.IMenuListener#menuAboutToShow(org.eclipse.jface.action.IMenuManager)
	 */
	public void menuAboutToShow(IMenuManager manager)
	{
		IMenuService service = (IMenuService) PlatformUI.getWorkbench().getService(IMenuService.class);
		service.populateContributionManager(menuManager, "popup:" + getEditor().getTypeId()); //$NON-NLS-1$
		getEditor().populateActionPart(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.IEditorPart#createContents(org.eclipse.swt.widgets.Composite)
	 */
	public Control createContents(Composite parent)
	{
		ToolBar toolBar = new ToolBar(parent, SWT.FLAT);
		toolBar.setBackground(parent.getBackground());
		ToolItem item = new ToolItem(toolBar, SWT.DROP_DOWN);
		item.setImage(CessarPluginActivator.getDefault().getImage(CessarPluginActivator.EXECUTE_ACTION_ICON_ID));
		item.addSelectionListener(new SelectionAdapter()
		{
			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
			 */
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				if (action != null && !(action.isDisposed()))
				{
					Menu menu = getMenuManager().createContextMenu(action);
					action.setMenu(menu);
					action.getMenu().setVisible(true);
				}
			}
		});
		action = toolBar;
		return action;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.IEditorPart#refresh()
	 */
	public void refresh()
	{
		if (action != null && !action.isDisposed())
		{
			action.setData(IdentificationUtils.KEY_NAME, getEditor().getInstanceId()
				+ IdentificationUtils.ACTION_PART_ID);
		}
		// if (menuManager != null)
		// {
		// menuManager.update();
		// // menuManager.update(true);
		// // menuManager.updateAll(true);
		// }
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.facility.parts.IModelFragmentEditorPart#dispose()
	 */
	public void dispose()
	{
		if (action != null)
		{
			action.dispose();
			action = null;
		}
		if (menuManager != null)
		{
			menuManager.dispose();
			menuManager = null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.facility.parts.IModelFragmentEditorPart#setEnabled(boolean)
	 */
	public void setEnabled(boolean enabled)
	{
		if (action != null)
		{
			action.setEnabled(enabled);
		}

	}
}
