/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidw8762 Mon Feb 24 15:21:19 2014 </copyright>
 */
package eu.cessar.ct.workspace.ui.internal.consistencychecker;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;
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

import eu.cessar.ct.workspace.consistencycheck.IConsistencyCheckResult;
import eu.cessar.ct.workspace.consistencycheck.IProjectCheckInconsistency;
import eu.cessar.ct.workspace.ui.internal.Messages;
import eu.cessar.ct.workspace.ui.internal.WorkspaceUIConstants;
import eu.cessar.ct.workspace.ui.utils.ImportExportUtils;
import eu.cessar.req.Requirement;

/**
 * 
 * ProjectConsistencyReportPage represents the report with the identified project inconsistencies.
 * 
 * @author uidw8762
 * 
 *         %created_by: uidw8762 %
 * 
 *         %date_created: Wed Mar 19 16:16:37 2014 %
 * 
 *         %version: 8 %
 * 
 */
// CHECKSTYLE:OFF
@Requirement(
	reqID = "REQ_CHECK#6")
public class ProjectConsistencyReportPage extends WizardPage
// CHECKSTYLE:ON
{
	/** The Constant COLUMN_TITLES. */
	private static final String[] COLUMN_TITLES = {Messages.ProjectConsistencyReportPage_column_severity,
		Messages.ProjectConsistencyReportPage_column_type, Messages.ProjectConsistencyReportPage_column_description,
		Messages.ProjectConsistencyReportPage_column_file_name};

	/** The Constant COLUMN_WIDTHS. */
	private static final int[] COLUMN_WIDTHS = {60, 120, 230, 230};

	/** The Constant TABLE_HIGH_HINT. */
	private static final int TABLE_HIGH_HINT = 50;

	/** The comparator. */
	private ProjectConsistencyReportTableViewerComparator comparator;

	/** The project to be analyzed. */
	private IProject project;

	/** The project inconsistencies results list. */
	private List<IConsistencyCheckResult<IProjectCheckInconsistency>> projectConsistencyCheckResults;

	private TableViewer tableViewer;

	/**
	 * Instantiates a new project consistency report page.
	 * 
	 * @param pageName
	 *        the page name
	 * @param project
	 * @param projectConsistencyCheckResults
	 *        the project consistency check results
	 */
	public ProjectConsistencyReportPage(String pageName, IProject project)
	{
		super(pageName);
		this.project = project;

		setTitle(pageName);
		setMessage(Messages.ProjectConsistencyReportPage_message);
	}

	/**
	 * Gets the project.
	 * 
	 * @return the project
	 */
	public IProject getProject()
	{
		return project;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void createControl(Composite parent)
	{
		final Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new GridLayout(1, false));
		container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		displayReportControls(container);

		setControl(container);
	}

	/**
	 * Display table viewer.
	 */
	private void displayReportControls(Composite composite)
	{
		tableViewer = new TableViewer(composite, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION);
		tableViewer.setData("ControlID", "TableProjectConsistencyReportPage"); //$NON-NLS-1$ //$NON-NLS-2$

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

		comparator = new ProjectConsistencyReportTableViewerComparator();
		tableViewer.setComparator(comparator);

		refreshTableViewer();

		displayCopyToClipboardControls(composite, tableViewer);
	}

	/**
	 * Refresh table viewer.
	 */
	private void refreshTableViewer()
	{
		if (projectConsistencyCheckResults != null)
		{
			tableViewer.setContentProvider(new ArrayContentProvider());
			tableViewer.setLabelProvider(new ProjectInconsistenciesReportLabelProvider());

			List<IProjectCheckInconsistency> checkResults = new ArrayList<IProjectCheckInconsistency>();

			for (IConsistencyCheckResult<IProjectCheckInconsistency> result: projectConsistencyCheckResults)
			{
				checkResults.addAll(result.getInconsistencies());
			}

			tableViewer.setInput(checkResults);
			tableViewer.refresh();
		}
	}

	/**
	 * Display copy to clipboard controls.
	 * 
	 * @param container
	 *        the container
	 */
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

	/**
	 * Gets the selection adapter.
	 * 
	 * @param viewer
	 *        the viewer
	 * @param column
	 *        the column
	 * @param index
	 *        the index
	 * @return the selection adapter
	 */
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
	 * Creates the columns.
	 * 
	 * @param tableViewer
	 *        the table viewer
	 */
	private static void createColumns(TableViewer tableViewer)
	{

		for (int i = 0; i < COLUMN_TITLES.length; i++)
		{
			createTableViewerColumn(tableViewer, COLUMN_TITLES[i], COLUMN_WIDTHS[i]);
		}
	}

	/**
	 * Creates the table viewer column.
	 * 
	 * @param tableViewer
	 *        the table viewer
	 * @param title
	 *        the title
	 * @param bound
	 *        the bound
	 * @return the table viewer column
	 */
	private static TableViewerColumn createTableViewerColumn(TableViewer tableViewer, String title, int bound)
	{
		final TableViewerColumn viewerColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		final TableColumn column = viewerColumn.getColumn();
		column.setText(title);
		column.setWidth(bound);
		column.setResizable(true);
		column.setMoveable(true);
		return viewerColumn;
	}

	/**
	 * Gets the inconsistencies.
	 * 
	 * @return the inconsistencies
	 */
	public List<IConsistencyCheckResult<IProjectCheckInconsistency>> getProjectConsistencyCheckResults()
	{
		return projectConsistencyCheckResults;
	}

	/**
	 * Sets the project consistency check results.
	 * 
	 * @param projectConsistencyCheckResults
	 *        the new project consistency check results
	 */
	public void setProjectConsistencyCheckResults(
		List<IConsistencyCheckResult<IProjectCheckInconsistency>> projectConsistencyCheckResults)
	{
		this.projectConsistencyCheckResults = projectConsistencyCheckResults;
		refreshTableViewer();
	}
}
