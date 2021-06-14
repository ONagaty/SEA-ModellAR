/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by aurel_avramescu Aug 13, 2010 2:42:15 PM </copyright>
 */
package eu.cessar.ct.ecuc.workspace.ui.internal.wizards.sync;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import eu.cessar.ct.ecuc.workspace.internal.SynchronizeConstants;
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
public class DecisionPage extends WizardPage
{

	private Composite container;
	private Button checkAll;
	private List<Button> checkButtons;
	protected Table elementsTable;
	protected int layoutsOffsets = 5;
	private List<AbstractProblem> listOfProblems = new ArrayList<AbstractProblem>();

	/**
	 * @param pageName
	 * @param processSelection
	 */
	public DecisionPage(String pageName)
	{
		super(pageName);
		setTitle(pageName);
		setDescription(SyncMessages.DecisionPageDescr);
		// TODO Auto-generated constructor stub
	}

	public void setProblems(List<AbstractProblem> listOfProblems)
	{
		this.listOfProblems = listOfProblems;
	}

	public List<AbstractProblem> getInitialProblems()
	{
		return listOfProblems;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
	 */
	public void createControl(Composite parent)
	{
		checkButtons = new ArrayList<Button>();
		container = new Composite(parent, SWT.NULL);
		setControl(container);
		// container.setSize(113, 100);
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

		// checkAll button
		checkAll = new Button(container, SWT.CHECK | SWT.LEFT);
		checkAll.setText(SyncMessages.ActionsDecisionPage_CheckUncheck);
		// check.setBounds(22, 165, 115, 30);
		checkAll.setLayoutData(new GridData());

		checkAll.addSelectionListener(new SelectionListener()
		{
			public void widgetDefaultSelected(SelectionEvent e)
			{
				// Do nothing
			}

			public void widgetSelected(SelectionEvent e)
			{
				if (((Button) e.getSource()).getSelection())
				{
					for (Button checkButton: checkButtons)
					{
						if (checkButton.isEnabled())
						{
							checkButton.setSelection(true);
						}
					}
					setPageComplete(true);
				}
				else
				{
					for (Button checkButton: checkButtons)
					{
						checkButton.setSelection(false);
					}
					setPageComplete(false);
				}
			}
		});

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
			elementsTable.removeAll();
			checkButtons.clear();
			for (Control c: elementsTable.getChildren())
			{
				c.dispose();
			}
			populateTable();
			getShell().pack();

			boolean checkAlFlag = false;
			for (Button b: checkButtons)
			{
				if (b.isEnabled())
				{
					checkAlFlag = true;
					break;
				}
			}
			if (checkAlFlag)
			{
				checkAll.setSelection(true);
			}
			else
			{
				checkAll.setSelection(false);
				checkAll.setEnabled(false);
			}

		}
	}

	/**
	 * 
	 */
	private void populateTable()
	{
		for (AbstractProblem problem: listOfProblems)
		{
			TableItem tableItem = new TableItem(elementsTable, SWT.NONE);
			TableEditor editor = new TableEditor(elementsTable);

			Button checkButton = new Button(elementsTable, SWT.CHECK);
			checkButton.pack();
			if (problem.getProblemType().equals(SynchronizeConstants.invalidEnumType)
				|| problem.getProblemType().equals(SynchronizeConstants.upperMultiplicityType))
			{
				checkButton.setSelection(false);
				checkButton.setEnabled(false);
			}
			else
			{
				checkButton.setSelection(true);
			}
			checkButtons.add(checkButton);
			checkButton.addSelectionListener(new SelectionListener()
			{
				public void widgetDefaultSelected(SelectionEvent e)
				{
					// Do nothing
				}

				public void widgetSelected(SelectionEvent e)
				{
					if (((Button) e.getSource()).getSelection())
					{
						setPageComplete(true);
						checkAll.setSelection(true);
						for (Button checkButton: checkButtons)
						{
							if (!checkButton.getSelection())
							{
								checkAll.setSelection(false);
								break;
							}
						}
					}
					else
					{
						setPageComplete(false);
						checkAll.setSelection(false);
						for (Button checkButton: checkButtons)
						{
							if (checkButton.getSelection())
							{
								setPageComplete(true);
								break;
							}
						}
					}
				}
			});

			editor.minimumWidth = checkButton.getSize().x;
			editor.horizontalAlignment = SWT.CENTER;
			editor.setEditor(checkButton, tableItem, 0);

			tableItem.setText(1, problem.getProblemType());
			tableItem.setText(2, problem.getQualifiedNameString() /*ModelUtils.getAbsoluteQualifiedName(problem.getElement())*/);
			tableItem.setText(3, getSelectedObjectType(problem.getElement()));
			tableItem.setText(4, problem.getDescription());
		}
		for (TableColumn column: elementsTable.getColumns())
		{
			column.pack();
		}
		resizeColumns(elementsTable);
	}

	/**
	 * @param elementsTable2
	 */
	private void resizeColumns(Table elementsTable2)
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

	@Override
	public IWizardPage getNextPage()
	{
		ReportPage page = (ReportPage) super.getNextPage();
		page.setProblems(getSelectedProblems());
		return page;
	}

	public List<AbstractProblem> getSelectedProblems()
	{
		if (checkAll != null)
		{
			if (checkAll.getSelection())
			{
				return listOfProblems;
			}
			else
			{
				List<AbstractProblem> result = new ArrayList<AbstractProblem>();
				Integer nr = 0;
				for (Button button: checkButtons)
				{
					if (button.getSelection())
					{
						result.add(listOfProblems.get(nr));
					}
					nr++;
				}
				return result;
			}
		}
		return Collections.emptyList();
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

	/* (non-Javadoc)
	 * @see org.eclipse.jface.wizard.WizardPage#isPageComplete()
	 */
	@Override
	public boolean isPageComplete()
	{
		for (Button b: checkButtons)
		{
			if (b.getSelection() && isCurrentPage())
			{
				return true;
			}
		}
		return false;
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
