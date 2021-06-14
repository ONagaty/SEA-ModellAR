/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidl6870 17.08.2012 13:27:00 </copyright>
 */
package eu.cessar.ct.workspace.ui.internal.xmlchecker;

import java.util.List;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import eu.cessar.ct.workspace.ui.internal.Messages;
import eu.cessar.ct.workspace.ui.internal.WorkspaceUIConstants;
import eu.cessar.ct.workspace.ui.utils.ImportExportUtils;
import eu.cessar.ct.workspace.xmlchecker.IXMLCheckerInconsistency;
import eu.cessar.req.Requirement;

/**
 *
 * Final page presenting the report with the identified loading/serializing inconsistencies.
 *
 * @author uidl6870
 *
 */
public class ReportPage extends WizardPage
{
	private static final String[] COLUMN_TITLES = {
		"File name", "Type", "Severity", "Description", "Expected", "Obtained"}; //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$

	private static final int[] COLUMN_WIDTHS = {100, 80, 60, 100, 140, 140};

	private static final int TABLE_HIGH_HINT = 50;

	private final List<IXMLCheckerInconsistency> inconsistencies;

	private ReportTableViewerComparator comparator;

	/**
	 * @param pageName
	 */
	public ReportPage(String pageName, List<IXMLCheckerInconsistency> inconsistencies)
	{
		super(pageName);

		this.inconsistencies = inconsistencies;
		setTitle(pageName);
		setMessage("Report with loading-saving inconsistencies"); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jface.wizard.WizardPage#isPageComplete()
	 */
	@Override
	public boolean isPageComplete()
	{
		return true;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
	 */
	public void createControl(Composite parent)
	{
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new GridLayout(1, false));
		container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		final TableViewer tableViewer = new TableViewer(container, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL
			| SWT.FULL_SELECTION);

		Table table = tableViewer.getTable();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);

		GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		gridData.heightHint = TABLE_HIGH_HINT;
		table.setLayoutData(gridData);

		createColumns(tableViewer);

		TableColumn[] columns = tableViewer.getTable().getColumns();
		for (int i = 0; i < columns.length; i++)
		{
			columns[i].addSelectionListener(getSelectionAdapter(tableViewer, columns[i], i));
		}

		comparator = new ReportTableViewerComparator();
		tableViewer.setComparator(comparator);

		tableViewer.setContentProvider(new ArrayContentProvider());
		tableViewer.setLabelProvider(new InconsistenciesReportLabelProvider());

		tableViewer.setInput(inconsistencies);

		displayCopyToClipboardControls(container, tableViewer);

		setControl(container);
	}

	/**
	 * Display copy to clipboard controls.
	 *
	 * @param container
	 *        the container
	 * 
	 */
	@Requirement(
		reqID = "16065")
	private static void displayCopyToClipboardControls(Composite composite, final TableViewer tableViewer)
	{
		final Button btnCopyToClipboard = new Button(composite, SWT.PUSH);
		btnCopyToClipboard.setText(Messages.ProjectConsistencyReportPage_button_copy_to_clipboard);

		btnCopyToClipboard.setData(WorkspaceUIConstants.CONTROL_ID, "btnCopyToClipBoard");//$NON-NLS-1$

		btnCopyToClipboard.addSelectionListener(new SelectionAdapter()
		{
			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
			 */
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				// Copy table viewer content to clipboard
				String csvTableContent = ImportExportUtils.exportTableContentToClipboard(tableViewer.getTable());

				if (!csvTableContent.isEmpty())
				{
					Clipboard clipboard = new Clipboard(Display.getCurrent());
					TextTransfer textTransfer = TextTransfer.getInstance();
					clipboard.setContents(new Object[] {csvTableContent}, new Transfer[] {textTransfer});
				}
			}
		});
	}

	private SelectionAdapter getSelectionAdapter(final TableViewer viewer, final TableColumn column, final int index)
	{
		SelectionAdapter selectionAdapter = new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				comparator.setColumn(index);

				int dir = comparator.getDirection();
				viewer.getTable().setSortDirection(dir);
				viewer.getTable().setSortColumn(column);

				viewer.refresh();
			}
		};
		return selectionAdapter;
	}

	/**
	 *
	 * @param tableViewer
	 */
	private void createColumns(TableViewer tableViewer)
	{

		for (int i = 0; i < COLUMN_TITLES.length; i++)
		{
			createTableViewerColumn(tableViewer, COLUMN_TITLES[i], COLUMN_WIDTHS[i]);
		}
	}

	private TableViewerColumn createTableViewerColumn(TableViewer tableViewer, String title, int bound)
	{
		final TableViewerColumn viewerColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		final TableColumn column = viewerColumn.getColumn();
		column.setText(title);
		column.setWidth(bound);
		column.setResizable(true);
		column.setMoveable(true);
		return viewerColumn;
	}

}
