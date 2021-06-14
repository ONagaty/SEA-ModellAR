/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidg3464<br/>
 * Jan 14, 2014 3:20:23 PM
 * 
 * </copyright>
 */
package eu.cessar.ct.runtime.ui.internal.classpath;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckboxCellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;

/**
 * 
 * @author uidg3464
 * 
 *         %created_by: uidg3464 %
 * 
 *         %date_created: Fri Jan 17 10:01:28 2014 %
 * 
 *         %version: 1 %
 */
public class PlugNameEditing extends EditingSupport
{

	private TableViewer viewer;

	/**
	 * @param viewer
	 */
	public PlugNameEditing(TableViewer viewer)
	{
		super(viewer);
		this.viewer = viewer;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.EditingSupport#getCellEditor(java.lang.Object)
	 */
	@Override
	protected CellEditor getCellEditor(Object element)
	{
		return new CheckboxCellEditor(null, SWT.Selection | SWT.READ_ONLY);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.EditingSupport#canEdit(java.lang.Object)
	 */
	@Override
	protected boolean canEdit(Object element)
	{
		// TODO Auto-generated method stub
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.EditingSupport#getValue(java.lang.Object)
	 */
	@Override
	protected Object getValue(Object element)
	{
		PluginModelBaseWrapper plugin = (PluginModelBaseWrapper) element;
		String id = plugin.getPluginModelBase().getPluginBase().getId();
		return id;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.EditingSupport#setValue(java.lang.Object, java.lang.Object)
	 */
	@Override
	protected void setValue(Object element, Object value)
	{
		// TODO Auto-generated method stub

	}

}
