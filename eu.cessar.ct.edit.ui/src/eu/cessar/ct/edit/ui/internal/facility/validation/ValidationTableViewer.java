/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidv3687<br/>
 * Feb 22, 2013 2:07:13 PM
 * 
 * </copyright>
 */
package eu.cessar.ct.edit.ui.internal.facility.validation;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.sphinx.emf.util.EObjectUtil;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import eu.cessar.ct.edit.ui.internal.CessarPluginActivator;
import eu.cessar.ct.validation.CessarValidationMarkerManagerDelegate;
import eu.cessar.req.Requirement;

/**
 * Provider of the TableViewer for the Validation Properties's tab
 * 
 * @author uidv3687
 * 
 *         %created_by: uidl6458 %
 * 
 *         %date_created: Wed Sep 17 14:46:16 2014 %
 * 
 *         %version: 1 %
 */
@SuppressWarnings("restriction")
@Requirement(
	reqID = "REQ_EDIT_PROP#VALIDATION#1")
public class ValidationTableViewer extends TableViewer
{
	private EValidationTableColumn[] columnName = new EValidationTableColumn[] {EValidationTableColumn.DESCRIPTION,
		EValidationTableColumn.OBJECT, EValidationTableColumn.URI, EValidationTableColumn.RESOURCE,
		EValidationTableColumn.EOBJECT_TYPE};
	// private EValidationTableColumn[] columnName = new EValidationTableColumn[] {EValidationTableColumn.DESCRIPTION,
	// EValidationTableColumn.OBJECT, EValidationTableColumn.URI, EValidationTableColumn.EOBJECT_TYPE};

	private ValidationTableViewerComparator comparator;

	/**
	 * @param parent
	 */
	public ValidationTableViewer(Composite parent)
	{
		this(parent, null);
	}

	/**
	 * @param parent
	 * @param columnName
	 */
	public ValidationTableViewer(Composite parent, EValidationTableColumn[] columnName)
	{
		super(parent, SWT.BORDER | SWT.FILL | SWT.MULTI | SWT.FULL_SELECTION);

		// if columnName is null it will not change the column names
		if (columnName != null)
		{
			this.columnName = columnName;
		}

		// created the table
		createTableViewer();
	}

	/**
	 * set the Table input with the latest markers
	 * 
	 * @param selection
	 * @param showChildrenMarkers
	 */
	public void updateMarkers(EObject selection, boolean showChildrenMarkers)
	{
		// get validation depth
		int depthValidation = showChildrenMarkers ? EObjectUtil.DEPTH_INFINITE : EObjectUtil.DEPTH_ZERO;

		IMarker[] markers = new IMarker[0];
		try
		{
			markers = CessarValidationMarkerManagerDelegate.getValidationMarkersList(selection, depthValidation);
		}
		catch (CoreException e)
		{
			CessarPluginActivator.getDefault().logError(e);
		}
		// add markers as table input
		setInput(markers);
	}

	/**
	 * @param composite
	 */
	private void createTableViewer()
	{

		GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		// gridData.heightHint = 150;

		getTable().setLayoutData(gridData);

		getTable().setHeaderVisible(true);
		getTable().setLinesVisible(true);

		createTableViewerColumns();

		TableColumn[] columns = getTable().getColumns();
		for (int i = 0; i < columns.length; i++)
		{
			columns[i].addSelectionListener(getSelectionAdapter(this, columns[i], i));
		}

		// add key listener to the table
		getTable().addKeyListener(new KeyAdapter()
		{
			@Override
			public void keyPressed(KeyEvent e)
			{
				// nothing to do
			}

			@Override
			public void keyReleased(KeyEvent e)
			{
				if (e.stateMask == SWT.CTRL && e.keyCode == 'c')
				{
					copyToClipboard();
				}
			}
		});

		comparator = new ValidationTableViewerComparator();
		comparator.setSortOrder(columnName[0], true);
		setComparator(comparator);

		setLabelProvider(new ValidationMarkerLabelProvider(columnName));
		setContentProvider(new ArrayContentProvider());
	}

	/**
	 * @param tableViewer
	 * @param tableColumn
	 * @param i
	 * @return SelectionAdapter
	 */
	private SelectionListener getSelectionAdapter(final TableViewer viewer, final TableColumn column, final int index)
	{
		SelectionAdapter selectionAdapter = new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				boolean asc = comparator.isAscending();
				asc = !asc;

				comparator.setSortOrder(columnName[index], asc);

				if (asc)
				{
					viewer.getTable().setSortDirection(SWT.UP);
				}
				else
				{
					viewer.getTable().setSortDirection(SWT.DOWN);
				}
				viewer.getTable().setSortColumn(column);

				viewer.refresh();
			}
		};
		return selectionAdapter;
	}

	private void createTableViewerColumns()
	{
		for (int i = 0; i < columnName.length; i++)
		{
			TableViewerColumn viewerColumn = new TableViewerColumn(this, SWT.NONE);
			final TableColumn column = viewerColumn.getColumn();
			column.setText(columnName[i].getName());
			column.setWidth(columnName[i].getWidth());
			column.setResizable(true);
			column.setMoveable(true);
		}
	}

	/**
	 * Copy the selections to clipboard in Excel format
	 */
	public void copyToClipboard()
	{
		int[] indices = getTable().getSelectionIndices();
		StringBuilder sb = new StringBuilder();

		for (int iter: indices)
		{
			TableItem item = getTable().getItem(iter);
			sb.append(toExcelFormat(item));
		}

		TextTransfer textTransfer = TextTransfer.getInstance();
		Clipboard cb = new Clipboard(Display.getDefault());
		cb.setContents(new Object[] {sb.toString()}, new Transfer[] {textTransfer});
	}

	private static String toExcelFormat(TableItem item)
	{
		return item.getText(0) + "\t" + item.getText(1) + "\t" + item.getText(2) + "\t" + item.getText(3) //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			+ "\t" + item.getText(4) + "\t" + item.getText(5) + System.getProperty("line.separator"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}

	/**
	 * Updated the table header names
	 * 
	 * @param columnName
	 */
	@SuppressWarnings("hiding")
	public void updateColumns(EValidationTableColumn[] columnName)
	{

		// get the current table listeners
		TableColumn[] columns = getTable().getColumns();
		for (TableColumn c: columns)
		{
			Listener[] columnListener = c.getListeners(SWT.Selection);
			if (columnListener != null)
			{
				// removes all the selection listeners
				for (Listener listener: columnListener)
				{
					c.removeListener(SWT.Selection, listener);
				}
			}
			c.dispose();
		}

		// Sets the new column names
		this.columnName = columnName;

		createTableViewerColumns();

		// adds new selection listeners to the table
		columns = getTable().getColumns();
		for (int i = 0; i < columns.length; i++)
		{
			columns[i].addSelectionListener(getSelectionAdapter(this, columns[i], i));
		}

		// add key listener to the table
		getTable().addKeyListener(new KeyAdapter()
		{
			@Override
			public void keyPressed(KeyEvent e)
			{
				// nothing to do, we handle the event on key released
			}

			@Override
			public void keyReleased(KeyEvent e)
			{
				if (e.stateMask == SWT.CTRL && e.keyCode == 'c')
				{
					copyToClipboard();
				}
			}
		});

		setLabelProvider(new ValidationMarkerLabelProvider(columnName));
	}
}
