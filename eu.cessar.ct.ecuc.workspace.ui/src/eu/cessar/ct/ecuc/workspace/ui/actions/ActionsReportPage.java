/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by aurel_avramescu Aug 13, 2010 2:43:25 PM </copyright>
 */
package eu.cessar.ct.ecuc.workspace.ui.actions;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
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
public class ActionsReportPage extends WizardPage
{

	private Composite container;
	private Table elementsTable;
	protected int layoutsOffsets = 5;

	private List<IEcucCleaningAction> cleaningActions = new ArrayList<IEcucCleaningAction>();

	/**
	 * @param pageName
	 */
	public ActionsReportPage(String pageName)
	{
		super(pageName);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 * @param pageName
	 * @param cleaningActions
	 */
	public ActionsReportPage(String pageName, List<IEcucCleaningAction> cleaningActions)
	{
		super(pageName);
		setTitle(pageName);
		setDescription(Messages.CleanerWizard_InfoMessage2);
		this.cleaningActions = cleaningActions;
	}

	/**
	 * @param pageName
	 * @param title
	 * @param titleImage
	 */
	public ActionsReportPage(String pageName, String title, ImageDescriptor titleImage)
	{
		super(pageName, title, titleImage);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
	 */
	public void createControl(Composite parent)
	{
		container = new Composite(parent, SWT.NULL);
		setControl(container);
		container.setLayout(getGridLayout(1, false));
		elementsTable = new Table(container, SWT.NONE);
		elementsTable.setBounds(12, 14, 717, 700);
		elementsTable.setHeaderVisible(true);
		elementsTable.setLinesVisible(true);
		elementsTable.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, true));
		TableColumn tableColumn = new TableColumn(elementsTable, SWT.NONE);
		tableColumn.setWidth(66);
		tableColumn.setText(Messages.ActionsCollector_Action);
		TableColumn tableColumn1 = new TableColumn(elementsTable, SWT.NONE);
		tableColumn1.setWidth(243);
		tableColumn1.setText(Messages.ActionsCollector_Elements);
		TableColumn tableColumn2 = new TableColumn(elementsTable, SWT.NONE);
		tableColumn2.setWidth(155);
		tableColumn2.setText(Messages.ActionsCollector_Type);
		TableColumn tableColumn3 = new TableColumn(elementsTable, SWT.NONE);
		tableColumn3.setWidth(253);
		tableColumn3.setText(Messages.ActionsCollector_Problem);
		addItems(cleaningActions);

		setPageComplete(true);

	}

	/**
	 * 
	 */
	void addItems(List<IEcucCleaningAction> cleaningActions)
	{
		elementsTable.removeAll();
		for (final IEcucCleaningAction action: cleaningActions)
		{
			if (action.isSelected())
			{
				TableItem tableItem = new TableItem(elementsTable, SWT.NONE);
				tableItem.setText(0, action.getActionName());
				tableItem.setText(1, action.getElementName());
				tableItem.setText(2, action.getElementType());
				switch (action.getProblemType())
				{
					case DEFINITION_IS_MISSING:
						tableItem.setText(3, Messages.ActionsCollector_MissingDef);
						break;
					case DEFINITION_HAS_BEEN_CHANGED:
						tableItem.setText(3, Messages.ActionsCollector_ChangedDef);
						break;
					case PARAMETERS_EXCEED:
						tableItem.setText(3, Messages.ActionsCollector_ParamMultiExceed);
						break;
					case REFERENCE_EXCEED:
						tableItem.setText(3, Messages.ActionsCollector_RefMultiExceed);
						break;
					case LOWER_MULTIPLICITY_IS_BIGGER:
						tableItem.setText(3, Messages.ActionsExecutor_LowerMultiplicityIsBIgger);
						break;
				}
			}

		}
	}

	/**
	 * @return the elementsTable
	 */
	public Table getElementsTable()
	{
		return elementsTable;
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
