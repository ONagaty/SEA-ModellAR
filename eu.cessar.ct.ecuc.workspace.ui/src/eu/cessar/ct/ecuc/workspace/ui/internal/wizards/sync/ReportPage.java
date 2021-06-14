/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by aurel_avramescu Aug 13, 2010 2:43:25 PM </copyright>
 */
package eu.cessar.ct.ecuc.workspace.ui.internal.wizards.sync;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import eu.cessar.ct.ecuc.workspace.internal.sync.problems.AbstractProblem;
import eu.cessar.ct.ecuc.workspace.ui.internal.SyncMessages;
import gautosar.gecucdescription.GConfigReferenceValue;
import gautosar.gecucdescription.GContainer;
import gautosar.gecucdescription.GModuleConfiguration;
import gautosar.gecucdescription.GParameterValue;
import gautosar.gecucparameterdef.GConfigParameter;
import gautosar.gecucparameterdef.GConfigReference;
import gautosar.gecucparameterdef.GContainerDef;

/**
 * @author uidt2045
 * 
 */
public class ReportPage extends WizardPage
{

	private Composite container;
	private Table elementsTable;
	protected int layoutsOffsets = 5;

	private List<AbstractProblem> listOfProblems = new ArrayList<AbstractProblem>();

	/**
	 * 
	 * @param pageName
	 * @param problems
	 */
	public ReportPage(String pageName)
	{
		super(pageName);
		setTitle(pageName);
		setDescription(SyncMessages.CleanerWizard_InfoMessage2);
	}

	public void setProblems(List<AbstractProblem> problems)
	{
		this.listOfProblems = problems;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
	 */
	public void createControl(Composite parent)
	{
		container = new Composite(parent, SWT.NULL);
		setControl(container);
		container.setLayout(getGridLayout(1, false));
		elementsTable = new Table(container, SWT.V_SCROLL | SWT.H_SCROLL | SWT.BORDER);
		elementsTable.setBounds(12, 14, TableConstants.TABLEWITH, TableConstants.TABLEHEIGHT);
		elementsTable.setHeaderVisible(true);
		elementsTable.setLinesVisible(true);
		GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		gridData.heightHint = TableConstants.TABLEHEIGHT;
		gridData.widthHint = TableConstants.TABLEWITH;
		elementsTable.setLayoutData(gridData);
		TableColumn tableColumn = new TableColumn(elementsTable, SWT.NONE);
		tableColumn.setWidth(TableConstants.COLUMN_ACTION);
		tableColumn.setText(SyncMessages.ActionsCollector_Action);
		TableColumn tableColumn1 = new TableColumn(elementsTable, SWT.NONE);
		tableColumn1.setWidth(TableConstants.COLUMN_NAME);
		tableColumn1.setText(SyncMessages.ActionsCollector_Elements);
		TableColumn tableColumn2 = new TableColumn(elementsTable, SWT.NONE);
		tableColumn2.setWidth(TableConstants.COLUMN_TYPE);
		tableColumn2.setText(SyncMessages.ActionsCollector_Type);
		TableColumn tableColumn3 = new TableColumn(elementsTable, SWT.NONE);
		tableColumn3.setWidth(TableConstants.COLUMN_LOCATION);
		tableColumn3.setText(SyncMessages.ActionsCollector_Problem);
		addItems(listOfProblems);

		setPageComplete(true);

	}

	/**
	 * 
	 */
	private void addItems(List<AbstractProblem> problemsList)
	{
		elementsTable.removeAll();
		for (final AbstractProblem problem: problemsList)
		{
			if (problem.getChangers() != null && problem.getChangers().size() > 0)
			{
				TableItem tableItem = new TableItem(elementsTable, SWT.NONE);
				tableItem.setText(0, problem.getProblemType());
				tableItem.setText(1, problem.getQualifiedNameString() /*ModelUtils.getAbsoluteQualifiedName((problem.getElement()))*/);
				tableItem.setText(2, getSelectedObjectType(problem.getElement()));
				tableItem.setText(3, problem.getDescription());
			}
		}
		for (TableColumn column: elementsTable.getColumns())
		{
			column.pack();
		}
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

	@Override
	public boolean canFlipToNextPage()
	{
		return false;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.DialogPage#setVisible(boolean)
	 */
	@Override
	public void setVisible(boolean visible)
	{
		super.setVisible(visible);
		if (visible)
		{
			addItems(listOfProblems);
			resizeColumns(elementsTable);
		}
	}

	/**
	 * @param elementsTable2
	 */
	private void resizeColumns(Table elementsTable2)
	{
		TableColumn[] columns = elementsTable2.getColumns();
		if (columns[0].getWidth() < TableConstants.COLUMN_ACTION)
		{
			columns[0].setWidth(TableConstants.COLUMN_ACTION);
		}
		if (columns[1].getWidth() < TableConstants.COLUMN_NAME)
		{
			columns[1].setWidth(TableConstants.COLUMN_NAME);
		}
		if (columns[2].getWidth() < TableConstants.COLUMN_TYPE)
		{
			columns[2].setWidth(TableConstants.COLUMN_TYPE);
		}
		if (columns[3].getWidth() < TableConstants.COLUMN_LOCATION)
		{
			columns[3].setWidth(TableConstants.COLUMN_LOCATION);
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.wizard.WizardPage#isPageComplete()
	 */
	@Override
	public boolean isPageComplete()
	{
		if (isCurrentPage() || elementsTable.getItems().length == 0)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	/**
	 * 
	 */
	public List<AbstractProblem> getProblems()
	{
		return listOfProblems;
	}

	/**
	 * @param selectedObj
	 * @return
	 */
	private String getSelectedObjectType(EObject selectedObj)
	{
		// check for obj from the Configuration
		if (selectedObj instanceof GModuleConfiguration)
		{
			return SyncMessages.Type_ModuleConfig;
		}

		if (selectedObj instanceof GContainer)
		{
			return SyncMessages.Type_Container;
		}
		else if (selectedObj instanceof GParameterValue)
		{
			return SyncMessages.Type_Parameter;
		}
		if (selectedObj instanceof GConfigReferenceValue)
		{
			return SyncMessages.Type_Reference;
		}

		if (selectedObj instanceof GConfigParameter)
		{
			return SyncMessages.Type_Parameter;
		}
		if (selectedObj instanceof GConfigReference)
		{
			return SyncMessages.Type_Reference;
		}
		if (selectedObj instanceof GContainerDef)
		{
			return SyncMessages.Type_Container;
		}
		return SyncMessages.Type_Unknown;
	}
}
