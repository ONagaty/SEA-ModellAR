/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by aurel_avramescu Aug 13, 2010 2:42:15 PM </copyright>
 */
package eu.cessar.ct.ecuc.workspace.ui.actions;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import eu.cessar.ct.ecuc.workspace.cleanup.IEcucCleaningAction;
import eu.cessar.ct.ecuc.workspace.ui.internal.Messages;

/**
 * @author aurel_avramescu
 * 
 */
public class ActionsDecisionPage extends WizardPage
{

	private Composite container;
	private Button check;
	private List<Button> checkButtons;
	protected Table elementsTable;
	private List<IEcucCleaningAction> cleaningActions = new ArrayList<IEcucCleaningAction>(10);
	ActionsReportPage reportPage;
	protected int layoutsOffsets = 5;

	/**
	 * @param pageName
	 */
	public ActionsDecisionPage(String pageName)
	{
		super(pageName);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param pageName
	 * @param title
	 * @param titleImage
	 */
	public ActionsDecisionPage(String pageName, String title, ImageDescriptor titleImage)
	{
		super(pageName, title, titleImage);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param string
	 * @param cleaner
	 */
	public ActionsDecisionPage(String pageName, List<IEcucCleaningAction> cleaningActions)
	{
		super(pageName);
		this.cleaningActions = cleaningActions;
	}

	/**
	 * @param string
	 * @param cleaner
	 */
	public ActionsDecisionPage(String pageName, List<IEcucCleaningAction> cleaningActions,
		ActionsReportPage reportPage)
	{
		super(pageName);
		setTitle(pageName);
		setDescription(Messages.CleanerWizard_InfoMessage1);
		this.cleaningActions = cleaningActions;
		this.reportPage = reportPage;
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
		elementsTable = new Table(container, SWT.NONE);
		elementsTable.setBounds(12, 14, 717, 600);
		elementsTable.setHeaderVisible(true);
		elementsTable.setLinesVisible(true);
		elementsTable.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, true));

		TableColumn tableColumn = new TableColumn(elementsTable, SWT.NONE);
		tableColumn.setWidth(91);
		tableColumn.setText(Messages.ActionsExecutor_Execute);

		TableColumn tableColumn1 = new TableColumn(elementsTable, SWT.NONE);
		tableColumn1.setWidth(50);
		tableColumn1.setText(Messages.ActionsCollector_Action);

		TableColumn tableColumn2 = new TableColumn(elementsTable, SWT.NONE);
		tableColumn2.setWidth(243);
		tableColumn2.setText(Messages.ActionsCollector_Elements);
		TableColumn tableColumn3 = new TableColumn(elementsTable, SWT.NONE);
		tableColumn3.setWidth(123);
		tableColumn3.setText(Messages.ActionsCollector_Type);
		TableColumn tableColumn4 = new TableColumn(elementsTable, SWT.NONE);
		tableColumn4.setWidth(210);
		tableColumn4.setText(Messages.ActionsCollector_Problem);

		for (final IEcucCleaningAction action: cleaningActions)
		{
			TableItem tableItem = new TableItem(elementsTable, SWT.NONE);
			TableEditor editor = new TableEditor(elementsTable);

			Button checkButton = new Button(elementsTable, SWT.CHECK);
			checkButton.pack();
			checkButton.setSelection(true);
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
						action.setSelected(true);
					}
					else
					{
						action.setSelected(false);
					}
					reportPage.addItems(cleaningActions);
					if (reportPage.getElementsTable().getItemCount() < 1)
					{
						setPageComplete(false);
					}
					else
					{
						setPageComplete(true);
					}
				}
			});

			editor.minimumWidth = checkButton.getSize().x;
			editor.horizontalAlignment = SWT.CENTER;
			editor.setEditor(checkButton, tableItem, 0);
			editor = new TableEditor(elementsTable);

			tableItem.setText(1, action.getActionName());
			tableItem.setText(2, action.getElementName());
			tableItem.setText(3, action.getElementType());
			switch (action.getProblemType())
			{
				case DEFINITION_IS_MISSING:
					tableItem.setText(4, Messages.ActionsCollector_MissingDef);
					break;
				case DEFINITION_HAS_BEEN_CHANGED:
					tableItem.setText(4, Messages.ActionsCollector_ChangedDef);
					break;
				case PARAMETERS_EXCEED:
					tableItem.setText(4, Messages.ActionsCollector_ParamMultiExceed);
					break;
				case REFERENCE_EXCEED:
					tableItem.setText(4, Messages.ActionsCollector_RefMultiExceed);
					break;
				case LOWER_MULTIPLICITY_IS_BIGGER:
					tableItem.setText(4, Messages.ActionsExecutor_LowerMultiplicityIsBIgger);
					break;
			}

		}

		check = new Button(container, SWT.CHECK | SWT.LEFT);
		check.setText(Messages.ActionsDecisionPage_CheckUncheck);
		// check.setBounds(22, 165, 115, 30);
		check.setLayoutData(new GridData());
		check.setSelection(true);
		check.addSelectionListener(new SelectionListener()
		{
			public void widgetDefaultSelected(SelectionEvent e)
			{
				// Do nothing
			}

			public void widgetSelected(SelectionEvent e)
			{
				if (((Button) e.getSource()).getSelection())
				{
					for (IEcucCleaningAction action: cleaningActions)
					{
						action.setSelected(true);
					}
					for (Button checkButton: checkButtons)
					{
						checkButton.setSelection(true);
					}
					setPageComplete(true);
				}
				else
				{
					for (IEcucCleaningAction action: cleaningActions)
					{
						action.setSelected(false);
					}
					for (Button checkButton: checkButtons)
					{
						checkButton.setSelection(false);
					}
					setPageComplete(false);
				}
				reportPage.addItems(cleaningActions);

			}
		});

		// container.setSize(789, 204);

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
}
