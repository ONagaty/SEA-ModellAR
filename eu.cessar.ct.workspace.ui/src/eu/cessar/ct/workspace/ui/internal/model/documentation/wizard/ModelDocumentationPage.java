/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidt2045 Jul 24, 2012 2:36:58 PM </copyright>
 */
package eu.cessar.ct.workspace.ui.internal.model.documentation.wizard;

import eu.cessar.ct.core.mms.MetaModelUtils;
import eu.cessar.ct.core.platform.ui.util.SelectionUtils;
import eu.cessar.ct.core.platform.util.PlatformUtils;
import eu.cessar.ct.sdk.utils.EcucUtils;
import eu.cessar.ct.sdk.utils.ModelUtils;
import eu.cessar.ct.workspace.ui.internal.CessarPluginActivator;
import gautosar.gecucdescription.GModuleConfiguration;
import gautosar.gecucparameterdef.GModuleDef;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.artop.aal.common.metamodel.AutosarReleaseDescriptor;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
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
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

/**
 * @author uidt2045
 * 
 */
public class ModelDocumentationPage extends WizardPage
{
	private static final String BROWSE = "BROWSE"; //$NON-NLS-1$
	private Button checkAll;
	private Table table;
	private FileDialog fileDialog;
	private Text outputFile;
	private List<GModuleConfiguration> configList;
	private List<Button> checkButtons;
	private IStructuredSelection selection;

	/**
	 * @param pageName
	 * @param list
	 */
	protected ModelDocumentationPage(String pageName, IStructuredSelection selection)
	{
		super(pageName);
		setTitle(ModelDocumentationConstants.MODEL_DOCUMENTATION_PAGE_TITLE);
		this.selection = selection;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
	 */
	public void createControl(Composite parent)
	{
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new GridLayout(1, true));
		container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		setControl(container);

		createTable(container);

		this.configList = getConfigurations(selection);
		populateTable(table, configList);

		// check all button
		createCheckAllButton(container);

		// Output Group
		createOutputGroup(container);

	}

	/**
	 * @param container
	 */
	private void createTable(Composite container)
	{
		table = new Table(container, SWT.V_SCROLL | SWT.H_SCROLL | SWT.BORDER);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		gridData.heightHint = 150;
		gridData.widthHint = 300;
		table.setLayoutData(gridData);

		TableColumn column1 = new TableColumn(table, SWT.NONE);
		column1.setText(ModelDocumentationConstants.EXECUTE);

		TableColumn column2 = new TableColumn(table, SWT.NONE);
		column2.setText(ModelDocumentationConstants.CONFIGURATION_COLUMN);

		TableColumn column3 = new TableColumn(table, SWT.NONE);
		column3.setText(ModelDocumentationConstants.FILE_COLUMN);
	}

	/**
	 * @param list
	 */
	private void populateTable(Table elementsTable, List<GModuleConfiguration> list)
	{
		checkButtons = new ArrayList<Button>();
		int cofigColumn = 1;
		int fileColumn = 2;
		for (GModuleConfiguration conf: list)
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

			editor.setItem(tableItem);
			editor.minimumWidth = checkButton.getSize().x;
			editor.horizontalAlignment = SWT.CENTER;
			editor.setEditor(checkButton, tableItem, 0);

			tableItem.setText(cofigColumn, conf.gGetShortName());
			tableItem.setText(fileColumn,
				ModelUtils.getDefiningFile(conf).getProjectRelativePath().toString());
		}
		for (TableColumn column: elementsTable.getColumns())
		{
			column.pack();
		}
	}

	/**
	 * @param container
	 */
	private void createCheckAllButton(Composite container)
	{
		checkAll = new Button(container, SWT.CHECK);
		checkAll.setText(ModelDocumentationConstants.CHECK_UNCHECK);
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
	}

	/**
	 * @param container
	 */
	private void createOutputGroup(Composite container)
	{
		Group outputGroup = new Group(container, SWT.BORDER_DASH);
		outputGroup.setLayout(new GridLayout(2, false));
		outputGroup.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		outputGroup.setText("Output File"); //$NON-NLS-1$

		// FileDialog
		fileDialog = new FileDialog(getShell());
		String[] filterExtensions = new String[] {"*" + ModelDocumentationConstants.EXTENSIONS}; //$NON-NLS-1$
		String filterPath = Platform.getLocation().toString();
		String fileName = filterPath + "/" + ModelDocumentationConstants.DEFAULT_FILE_NAME //$NON-NLS-1$
			+ ModelDocumentationConstants.EXTENSIONS;

		fileDialog.setFileName(fileName);
		fileDialog.setFilterExtensions(filterExtensions);
		fileDialog.setFilterPath(filterPath);

		outputFile = new Text(outputGroup, SWT.BORDER);
		outputFile.setText(fileName);
		outputFile.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		Button browse = new Button(outputGroup, SWT.PUSH);
		browse.setText(BROWSE);
		browse.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));

		browse.addSelectionListener(new SelectionAdapter()
		{
			/* (non-Javadoc)
			 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
			 */
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				String string = fileDialog.open();
				if (string != null && string.length() > 0)
				{
					outputFile.setText(string.endsWith(ModelDocumentationConstants.EXTENSIONS) ? string
						: string + ModelDocumentationConstants.EXTENSIONS);
				}
			}
		});
	}

	/**
	 * @return
	 */
	public File getOutputFile()
	{
		String text = outputFile.getText();
		// IWorkspace workspace = ResourcesPlugin.getWorkspace();
		// IPath location = Path.fromOSString(text);
		// IFile ifile = workspace.getRoot().getFileForLocation(location);
		return new File(text);
	}

	public Set<GModuleConfiguration> getListOfSelectedMC()
	{
		Set<GModuleConfiguration> result = new LinkedHashSet<GModuleConfiguration>();
		for (int i = 0; i < checkButtons.size(); i++)
		{
			if (checkButtons.get(i).getSelection())
			{
				result.add(configList.get(i));
			}
		}
		return result;
	}

	// TODO this should be extracted in SelectionUtils
	private IProject getProjectFromSelection(IStructuredSelection selection)
	{
		Iterator<?> iterator = selection.iterator();
		while (iterator.hasNext())
		{
			Object selectedElem = iterator.next();
			if (selectedElem instanceof IResource)
			{
				return ((IResource) selectedElem).getProject();
			}
		}
		return null;
	}

	private List<GModuleConfiguration> getConfigurations(IStructuredSelection currentSelection)
	{
		List<GModuleConfiguration> result = new ArrayList<GModuleConfiguration>();

		Set<IFile> collectedFiles = new LinkedHashSet<IFile>();
		final IProject project = getProjectFromSelection(currentSelection);
		if (project != null)
		{
			IRunnableWithProgress runnable = new IRunnableWithProgress()
			{
				public void run(IProgressMonitor monitor) throws InvocationTargetException,
					InterruptedException
				{
					PlatformUtils.waitForModelLoading(project, new NullProgressMonitor());
				}
			};
			try
			{
				getContainer().run(true, false, runnable);
			}
			catch (Exception e)// SUPPRESS CHECKSTYLE OK
			{
				CessarPluginActivator.getDefault().logError(e);
			}

		}
		AutosarReleaseDescriptor autosarRelease = MetaModelUtils.getAutosarRelease(project);
		List<String> contentTypes = autosarRelease.getContentTypeIds();
		IStatus status = SelectionUtils.collectFilesFromSelection(currentSelection, collectedFiles,
			contentTypes);
		for (IFile file: collectedFiles)
		{
			result.addAll(extractModulesFromFile(file, project));
		}
		if (status.getSeverity() == IStatus.ERROR)
		{
			setErrorMessage(ModelDocumentationConstants.ERROR_MESSAGE);

			for (IStatus child: status.getChildren())
			{
				if (child.getSeverity() == IStatus.ERROR)
				{
					CessarPluginActivator.getDefault().logError(child.getException(),
						child.getMessage(), (Object) null);
				}
			}
		}

		return result;
	}

	private List<GModuleConfiguration> extractModulesFromFile(IFile file, IProject project)
	{
		List<GModuleConfiguration> result = new ArrayList<GModuleConfiguration>();
		Collection<GModuleDef> modulesDef = EcucUtils.getAvailableModules(project);
		for (GModuleDef moduleDef: modulesDef)
		{
			Collection<GModuleConfiguration> instances = EcucUtils.getAvailableInstances(project,
				moduleDef);
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

}
