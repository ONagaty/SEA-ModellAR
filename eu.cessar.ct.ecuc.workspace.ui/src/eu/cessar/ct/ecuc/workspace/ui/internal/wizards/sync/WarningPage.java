/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidu8153<br/>
 * May 4, 2016 3:33:04 PM
 *
 * </copyright>
 */
package eu.cessar.ct.ecuc.workspace.ui.internal.wizards.sync;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import eu.cessar.ct.ecuc.workspace.internal.sync.problems.AbstractProblem;
import eu.cessar.ct.ecuc.workspace.ui.internal.SyncMessages;
import eu.cessar.ct.ecuc.workspace.ui.internal.SyncUtil;

/**
 * TODO: Please comment this class
 *
 * @author uidu8153
 *
 *         %created_by: created by %
 *
 *         %date_created: creation date %
 *
 *         %version: version %
 */
public class WarningPage extends WizardPage
{

	private Composite container;

	protected Table elementsTable;
	protected int layoutsOffsets = 5;
	private List<Button> checkButtons;
	private List<AbstractProblem> listOfProblems = new ArrayList<>();
	private List<AbstractProblem> problemsToBeIgnored = new ArrayList<>();

	/**
	 * @param pageName
	 */
	protected WarningPage(String pageName)
	{
		super(pageName);
		setTitle(pageName);
		setDescription(SyncMessages.WarningPage_DESC);

	}

	public void setProblems(List<AbstractProblem> listOfProblems)
	{
		this.listOfProblems = listOfProblems;
	}

	public List<AbstractProblem> getInitialProblems()
	{
		return listOfProblems;
	}

	/**
	 * @param listOfProblemsToBeIgnored
	 */
	public void setProblemsToBeIgnored(List<AbstractProblem> problemsToBeIgnored)
	{
		this.problemsToBeIgnored = problemsToBeIgnored;
	}

	public List<AbstractProblem> getProblemsToBeIgnored()
	{
		return problemsToBeIgnored;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void createControl(Composite parent)
	{
		checkButtons = new ArrayList<>();
		container = new Composite(parent, SWT.NULL);
		setControl(container);
		container.setLayout(getGridLayout(1, false));

		elementsTable = new Table(container, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
		elementsTable.setBounds(12, 14, TableConstants.TABLEWITH, TableConstants.TABLEHEIGHT);
		elementsTable.setHeaderVisible(true);
		elementsTable.setLinesVisible(true);

		GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		gridData.heightHint = TableConstants.TABLEHEIGHT;
		gridData.widthHint = TableConstants.TABLEWITH;
		elementsTable.setLayoutData(gridData);

		TableColumn tableColumn0 = new TableColumn(elementsTable, SWT.NONE);
		tableColumn0.setWidth(TableConstants.COLUMN_CHECK);
		tableColumn0.setText(SyncMessages.ActionsExecutor_Execute);

		TableColumn tableColumn1 = new TableColumn(elementsTable, SWT.NONE);
		tableColumn1.setWidth(TableConstants.COLUMN_ACTION);
		tableColumn1.setText(SyncMessages.ActionsCollector_Action);

		TableColumn tableColumn2 = new TableColumn(elementsTable, SWT.NONE);
		tableColumn2.setWidth(TableConstants.COLUMN_NAME);
		tableColumn2.setText(SyncMessages.ActionsCollector_Elements);

		TableColumn tableColumn3 = new TableColumn(elementsTable, SWT.NONE);
		tableColumn3.setWidth(TableConstants.COLUMN_TYPE);
		tableColumn3.setText(SyncMessages.ActionsCollector_Type);

		TableColumn tableColumn4 = new TableColumn(elementsTable, SWT.NONE);
		tableColumn4.setWidth(TableConstants.COLUMN_LOCATION);
		tableColumn4.setText(SyncMessages.ActionsCollector_Problem);

	}

	/**
	 * This method provide similar GridLayouts to all wizard pages that call it.
	 */
	public GridLayout getGridLayout(int numCols, boolean equalCols)
	{
		GridLayout gridLayout = new GridLayout(numCols, equalCols);
		gridLayout.horizontalSpacing = layoutsOffsets;
		gridLayout.verticalSpacing = layoutsOffsets;
		gridLayout.marginWidth = layoutsOffsets;
		gridLayout.marginTop = layoutsOffsets;
		gridLayout.marginRight = layoutsOffsets;
		gridLayout.marginLeft = layoutsOffsets;
		gridLayout.marginHeight = layoutsOffsets;
		gridLayout.marginBottom = layoutsOffsets;
		return gridLayout;
	}

	/**
	 * @param elementsTable2
	 */
	private static void resizeColumns(Table elementsTable2)
	{
		TableColumn[] columns = elementsTable2.getColumns();
		if (columns[0].getWidth() < TableConstants.COLUMN_CHECK)
		{
			columns[0].setWidth(TableConstants.COLUMN_CHECK);
		}
		if (columns[1].getWidth() < TableConstants.COLUMN_ACTION)
		{
			columns[1].setWidth(TableConstants.COLUMN_ACTION);
		}

		if (columns[2].getWidth() < TableConstants.COLUMN_NAME)
		{
			columns[2].setWidth(TableConstants.COLUMN_NAME);
		}
		if (columns[3].getWidth() < TableConstants.COLUMN_TYPE)
		{
			columns[3].setWidth(TableConstants.COLUMN_TYPE);
		}
		if (columns[4].getWidth() < TableConstants.COLUMN_LOCATION)
		{
			columns[4].setWidth(TableConstants.COLUMN_LOCATION);
		}
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

	@Override
	public IWizardPage getNextPage()
	{
		DecisionPage page = (DecisionPage) super.getNextPage();
		page.setProblems(listOfProblems);
		return page;
	}

	private void populateTable()
	{

		TableItem tableItem;
		TableEditor editor;
		Button checkButton;

		for (AbstractProblem problem: problemsToBeIgnored)
		{
			tableItem = new TableItem(elementsTable, SWT.NONE);
			editor = new TableEditor(elementsTable);

			checkButton = new Button(elementsTable, SWT.CHECK);
			checkButton.pack();
			checkButton.setSelection(false);
			checkButton.setEnabled(false);

			editor.minimumWidth = checkButton.getSize().x;
			editor.horizontalAlignment = SWT.CENTER;
			editor.setEditor(checkButton, tableItem, 0);

			tableItem.setText(1, problem.getProblemType());
			tableItem.setText(2, problem.getQualifiedNameString());
			tableItem.setText(3, SyncUtil.getSelectedObjectType(problem.getElement()));
			tableItem.setText(4, problem.getDescription());

		}

		Arrays.stream(elementsTable.getColumns()).forEach((column) -> {
			column.pack();
		});

		resizeColumns(elementsTable);

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jface.dialogs.DialogPage#setVisible(boolean)
	 */
	@Override
	public void setVisible(boolean visible)
	{
		// TODO Auto-generated method stub
		super.setVisible(visible);

		if (visible)
		{
			elementsTable.removeAll();
			checkButtons.clear();
			for (Control control: elementsTable.getChildren())
			{
				control.dispose();
			}
			populateTable();
			getShell().pack();

		}
	}

}
