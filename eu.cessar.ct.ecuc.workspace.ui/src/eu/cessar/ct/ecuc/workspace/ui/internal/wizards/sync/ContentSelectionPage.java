/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidt2045 Feb 7, 2011 9:57:45 AM </copyright>
 */
package eu.cessar.ct.ecuc.workspace.ui.internal.wizards.sync;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import eu.cessar.ct.core.platform.util.PlatformUtils;
import eu.cessar.ct.ecuc.workspace.internal.SynchronizeConstants;
import eu.cessar.ct.ecuc.workspace.internal.sync.problems.AbstractProblem;
import eu.cessar.ct.ecuc.workspace.sync.SyncAnalizer;
import eu.cessar.ct.ecuc.workspace.ui.internal.CessarPluginActivator;
import eu.cessar.ct.ecuc.workspace.ui.internal.SyncMessages;
import eu.cessar.ct.ecuc.workspace.ui.internal.wizards.pages.AbstractWizardPage;
import eu.cessar.ct.sdk.utils.EcucUtils;
import eu.cessar.ct.sdk.utils.ModelUtils;
import gautosar.gecucdescription.GConfigReferenceValue;
import gautosar.gecucdescription.GContainer;
import gautosar.gecucdescription.GModuleConfiguration;
import gautosar.gecucdescription.GParameterValue;
import gautosar.gecucparameterdef.GModuleDef;
import gautosar.ggenericstructure.ginfrastructure.GARPackage;
import gautosar.ggenericstructure.ginfrastructure.GIdentifiable;

/**
 * @author uidt2045
 *
 */
public class ContentSelectionPage extends WizardPage
{
	private SyncAnalizer analyzer;
	HashMap<String, Object> options;

	private Button checkAll;
	private Composite container;
	private List<Button> checkButtons;
	protected Table elementsTable;

	protected int layoutsOffsets = 5;

	private List<GIdentifiable> processSelection = new ArrayList<GIdentifiable>();
	private List<AbstractProblem> problemsToBeIgnored;
	private ISelection selection;

	// upperMultiplicity options
	protected Button optFlag;
	protected Spinner spinMaximumElementsCount;

	protected ContentSelectionPage(String pageName, ISelection selection)
	{
		super(pageName);

		setTitle(pageName);
		setDescription(SyncMessages.ContentPageDescr);

		this.selection = selection;
		processSelection = new ArrayList<GIdentifiable>();

		analyzer = new SyncAnalizer();
		options = new HashMap<String, Object>();
		options.put(SynchronizeConstants.multOptionalFlag, false);

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
	public void createControl(Composite parent)
	{

		checkButtons = new ArrayList<Button>();
		container = new Composite(parent, SWT.NONE);
		setControl(container);
		container.setLayout(getGridLayout(3, false));
		elementsTable = new Table(container, SWT.V_SCROLL | SWT.H_SCROLL | SWT.BORDER);
		elementsTable.setBounds(12, 14, TableConstants.TABLEWITH, TableConstants.TABLEHEIGHT);
		elementsTable.setHeaderVisible(true);
		elementsTable.setLinesVisible(true);
		GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1);
		gridData.heightHint = TableConstants.TABLEHEIGHT;
		gridData.widthHint = TableConstants.TABLEWITH;
		elementsTable.setLayoutData(gridData);

		TableColumn tableColumn1 = new TableColumn(elementsTable, SWT.NONE);
		tableColumn1.setText(SyncMessages.Selection_Execute);
		tableColumn1.setWidth(TableConstants.COLUMN_ACTION);

		TableColumn tableColumn2 = new TableColumn(elementsTable, SWT.NONE);
		tableColumn2.setText(SyncMessages.Selection_Name);
		tableColumn2.setWidth(TableConstants.COLUMN_NAME);

		TableColumn tableColumn3 = new TableColumn(elementsTable, SWT.NONE);
		tableColumn3.setText(SyncMessages.ActionsCollector_Type);
		tableColumn3.setWidth(TableConstants.COLUMN_TYPE);

		TableColumn tableColumn4 = new TableColumn(elementsTable, SWT.NONE);
		tableColumn4.setText(SyncMessages.Selection_Location);
		tableColumn4.setWidth(TableConstants.COLUMN_LOCATION);

		processSelection = processSelection();
		for (GIdentifiable selectedObj: processSelection)
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
			editor = new TableEditor(elementsTable);

			tableItem.setText(1, ModelUtils.getAbsoluteQualifiedName(selectedObj));
			tableItem.setText(2, getSelectedObjectType(selectedObj));
			tableItem.setText(3, ModelUtils.getDefiningFile(selectedObj).getName());

		}

		// create the upperMultiplicityOptions
		optFlag = new Button(container, SWT.CHECK | SWT.LEFT);
		optFlag.setText("Create instances dictated by upper multiplicity but no more than");
		optFlag.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));
		optFlag.setSelection(false);
		optFlag.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				boolean showOptions = optFlag.getSelection();
				spinMaximumElementsCount.setEnabled(showOptions);
				if (!showOptions)
				{
					spinMaximumElementsCount.setSelection(8);
				}
				options.put(SynchronizeConstants.multOptionalFlag, showOptions);
				options.put(SynchronizeConstants.multOptionalValue, spinMaximumElementsCount.getSelection());
			}
		});

		spinMaximumElementsCount = new Spinner(container, SWT.BORDER | SWT.WRAP);
		GridData spinData = new GridData(SWT.CENTER, SWT.CENTER, false, false);
		spinData.widthHint = 20;
		spinMaximumElementsCount.setLayoutData(spinData);
		spinMaximumElementsCount.setData(AbstractWizardPage.CONTROL_ID, "spinUpperMultiplicityCount"); //$NON-NLS-1$
		spinMaximumElementsCount.setEnabled(false);
		spinMaximumElementsCount.setMinimum(1);
		spinMaximumElementsCount.setMaximum(255);
		spinMaximumElementsCount.setIncrement(1);
		spinMaximumElementsCount.setSelection(8);

		spinMaximumElementsCount.addSelectionListener(new SelectionAdapter()
		{
			/*
			 * (non-Javadoc)
			 *
			 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
			 */
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				options.put(SynchronizeConstants.multOptionalValue, spinMaximumElementsCount.getSelection());
			}
		});

		Label labelElements = new Label(container, SWT.NONE);
		labelElements.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));
		labelElements.setText("elements");

		// the check all button
		checkAll = new Button(container, SWT.CHECK | SWT.LEFT);
		checkAll.setText(SyncMessages.ActionsDecisionPage_CheckUncheck);
		// check.setBounds(22, 165, 115, 30);
		checkAll.setLayoutData(new GridData());
		checkAll.setSelection(true);
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
						checkButton.setSelection(true);
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
		// resize the columns in the table
		for (TableColumn column: elementsTable.getColumns())
		{
			column.pack();
		}
		resizeColumns(elementsTable);
	}

	private void resizeColumns(Table elementsTable2)
	{
		TableColumn[] columns = elementsTable2.getColumns();
		if (columns[0].getWidth() < TableConstants.COLUMN_CHECK)
		{
			columns[0].setWidth(TableConstants.COLUMN_CHECK);
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

	@Override
	public IWizardPage getNextPage()
	{
		List<AbstractProblem> listOfProblems = analizeTheSelectedItems(getSelectedItems());

		WarningPage warningPage = (WarningPage) super.getNextPage();

		warningPage.setProblemsToBeIgnored(problemsToBeIgnored);
		warningPage.setProblems(listOfProblems);
		return warningPage;

	}

	/**
	 * @param processSelection2
	 * @return
	 */
	private List<GIdentifiable> getSelectedItems()
	{
		if (checkAll != null)
		{
			if (checkAll.getSelection())
			{
				return processSelection;
			}
			else
			{
				List<GIdentifiable> result = new ArrayList<GIdentifiable>();
				Integer nr = 0;
				for (Button button: checkButtons)
				{
					if (button.getSelection())
					{
						result.add(processSelection.get(nr));
					}
					nr++;
				}
				return result;
			}
		}
		return Collections.emptyList();
	}

	/**
	 * Analize the given list of GIdentifiables, with the SyncAnalizer
	 *
	 * @return
	 */
	protected List<AbstractProblem> analizeTheSelectedItems(List<GIdentifiable> list)
	{

		List<AbstractProblem> result = new ArrayList<AbstractProblem>();
		problemsToBeIgnored = new ArrayList<AbstractProblem>();

		if (getShell().isVisible())
		{
			for (GIdentifiable obj: list)
			{
				analyzer.analyze(obj, options, result, problemsToBeIgnored);

			}

			return result;
		}
		return Collections.emptyList();
	}

	/**
	 * @param obj
	 * @return
	 */
	private GIdentifiable getObjDef(GIdentifiable obj)
	{
		if (obj instanceof GContainer)
		{
			return ((GContainer) obj).gGetDefinition();
		}
		if (obj instanceof GModuleConfiguration)
		{
			return ((GModuleConfiguration) obj).gGetDefinition();
		}
		if (obj instanceof GConfigReferenceValue)
		{
			return ((GConfigReferenceValue) obj).gGetDefinition();
		}
		if (obj instanceof GParameterValue)
		{
			((GParameterValue) obj).gGetDefinition();
		}
		return null;
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
	 * @param selectedObj
	 * @return
	 */
	private String getSelectedObjectType(GIdentifiable selectedObj)
	{

		if (selectedObj instanceof GModuleConfiguration)
		{
			return SyncMessages.Type_ModuleConfig;
		}
		if (selectedObj instanceof GContainer)
		{
			return SyncMessages.Type_Container;
		}

		return SyncMessages.Type_Unknown;
	}

	/**
	 * @return
	 */
	private List<GIdentifiable> processSelection()
	{
		List<GIdentifiable> result = new ArrayList<GIdentifiable>();
		if (selection instanceof StructuredSelection)
		{
			StructuredSelection sSelection = (StructuredSelection) selection;
			// load the project
			loadTheProjects(sSelection);

			Iterator iterator = sSelection.iterator();
			// process the selection
			iterator = sSelection.iterator();
			while (iterator.hasNext())
			{
				Object nextSelectedObj = iterator.next();
				processSelectedItems(nextSelectedObj, result);
			}
		}
		return result;
	}

	/**
	 * @param listOfProj
	 */
	private void loadTheProjects(StructuredSelection selection)
	{
		List<IProject> listOfProj = new ArrayList<IProject>();
		Iterator iterator = selection.iterator();
		while (iterator.hasNext())
		{
			Object nextSelectedObj = iterator.next();
			if (nextSelectedObj instanceof IResource)
			{
				IProject project = ((IResource) nextSelectedObj).getProject();
				if (!listOfProj.contains(project))
				{
					listOfProj.add(project);
				}
			}
		}
		for (final IProject project: listOfProj)
		{
			IRunnableWithProgress runnable = new IRunnableWithProgress()
			{
				public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException
				{
					PlatformUtils.waitForModelLoading(project, new NullProgressMonitor());
				}
			};
			try
			{
				getContainer().run(true, false, runnable);
			}
			catch (Exception e)
			{
				CessarPluginActivator.getDefault().logError(e);
			}
		}

	}

	/**
	 * @param result
	 * @return
	 */
	private void processSelectedItems(Object selectedObj, List<GIdentifiable> result)
	{
		if (selectedObj instanceof IContainer)
		{
			try
			{
				IResource[] members = ((IContainer) selectedObj).members();
				for (IResource resource: members)
				{
					processSelectedItems(resource, result);
				}
			}
			catch (CoreException e)
			{
				CessarPluginActivator.getDefault().logError(e);
			}
		}
		else
		{
			if (selectedObj instanceof IFile)
			{
				result.addAll(extractModulesFromFile((IFile) selectedObj));
			}
			else
			{
				if (selectedObj instanceof GARPackage)
				{
					processPackageSelection(result, (GARPackage) selectedObj);
				}
				else
				{
					if (selectedObj instanceof GIdentifiable)
					{
						result.add((GIdentifiable) selectedObj);
					}
				}
			}
		}
	}

	/**
	 * @param result
	 * @param selectedObj
	 */
	private void processPackageSelection(List<GIdentifiable> result, GARPackage selectedObj)
	{
		for (EObject child: selectedObj.eContents())
		{
			if (child instanceof GARPackage)
			{
				processPackageSelection(result, (GARPackage) child);
			}
			else
			{
				if (child instanceof GModuleConfiguration)
				{
					result.add((GIdentifiable) child);
				}
			}

		}
	}

	protected List<GIdentifiable> extractModulesFromFile(IFile file)
	{
		IProject project = file.getProject();
		List<GIdentifiable> result = new ArrayList<GIdentifiable>();
		Collection<GModuleDef> modulesDef = EcucUtils.getAvailableModules(project);
		for (GModuleDef moduleDef: modulesDef)
		{
			Collection<GModuleConfiguration> instances = EcucUtils.getAvailableInstances(project, moduleDef);
			for (GModuleConfiguration conf: instances)
			{
				if (ModelUtils.getDefiningFile(conf).equals(file))
				{
					result.add(conf);
				}
			}
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jface.dialogs.DialogPage#setVisible(boolean)
	 */
	@Override
	public void setVisible(boolean visible)
	{
		if (visible)
		{
			if (checkButtons.size() == 0)
			{
				checkAll.setSelection(false);
				checkAll.setEnabled(false);
				setPageComplete(false);
			}
		}
		super.setVisible(visible);
	}

	public SyncAnalizer getAnalizer()
	{
		return analyzer;
	}

}
