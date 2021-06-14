/**
 *
 */
package eu.cessar.ct.ecuc.workspace.ui.internal.wizards.pages.conversion;

import eu.cessar.ct.ecuc.workspace.ui.internal.Messages;
import eu.cessar.ct.ecuc.workspace.ui.internal.wizards.pages.AbstractWizardPage;
import gautosar.ggenericstructure.ginfrastructure.GPackageableElement;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

/**
 * The abstract class for modules table viewer
 */
public abstract class AbstractModuleSelectionPage extends WizardPage
{
	/** The project. */
	protected IProject project;

	/** The table module definitions. */
	protected TableViewer tableModules;

	/** The label provider. */
	protected ITableLabelProvider labelProvider;

	/**
	 * The content provider
	 */
	protected IContentProvider contentProvider;

	/** The selected module definition. */
	protected GPackageableElement selectedModuleConfiguration;

	/** The column name */
	protected String columnName;
	/**
	 * The parent composite for this page
	 */
	protected Composite content;

	/**
	 * Constructor for ModuleDefinitionWizardProjectPage.
	 *
	 * @param project
	 * @param labelProvider
	 * @param contentProvider
	 * @param pageName
	 * @param columnName
	 *
	 */
	public AbstractModuleSelectionPage(IProject project, String pageName, String columnName)
	{
		super(pageName);
		this.project = project;
		this.columnName = columnName;
	}

	/**
	 * Handle module selection.
	 *
	 * @param currentSelection
	 *        the current selection
	 */
	protected void handleModuleSelection(IStructuredSelection currentSelection)
	{
		if (null != currentSelection && !currentSelection.isEmpty()
			&& currentSelection.getFirstElement() instanceof GPackageableElement)
		{
			setSelectedModuleConfiguration((GPackageableElement) currentSelection.getFirstElement());
			updateInfoMessage();
		}
		else
		{
			setSelectedModuleConfiguration(null);
		}
		setPageComplete(isPageComplete());
		getContainer().updateMessage();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.DialogPage#getErrorMessage()
	 */
	@Override
	public String getErrorMessage()
	{
		if (null == getSelectedModule())
		{
			return Messages.StandardModuleConfigurationSelectionPage_Err_No_ModuleConfiguration;
		}
		return super.getErrorMessage();
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

	/**
	 * Sets the project.
	 *
	 * @param project
	 *        the new project
	 */
	public void setProject(IProject project)
	{
		this.project = project;
	}

	/**
	 * Gets the selected module definition.
	 *
	 * @return the selected module definition
	 */
	public GPackageableElement getSelectedModule()
	{
		return selectedModuleConfiguration;
	}

	/**
	 * Sets the selected module definition.
	 *
	 * @param selectedModuleConfiguration
	 *        the new selected module definition
	 */
	public void setSelectedModuleConfiguration(GPackageableElement selectedModuleConfiguration)
	{

		this.selectedModuleConfiguration = selectedModuleConfiguration;
	}

	/**
	 *
	 * @return the parent composite for this page
	 */
	public Composite getContentComposite()
	{
		return content;
	}

	/**
	 * @param config
	 */
	public void updateInfoMessage()
	{
		// nothing to do here. Will be overwritten is specific class
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void createControl(Composite parent)
	{
		content = new Composite(parent, SWT.NULL);
		content.setLayout(new GridLayout(1, false));
		setControl(content);

		Label label = new Label(content, SWT.NULL);
		label.setData(AbstractWizardPage.CONTROL_ID, "label"); //$NON-NLS-1$

		tableModules = new TableViewer(content, SWT.BORDER | SWT.FULL_SELECTION | SWT.SINGLE);
		Table table = tableModules.getTable();
		table.setHeaderVisible(true);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));

		TableColumn tc = new TableColumn(table, SWT.NONE);
		tc.setData(AbstractWizardPage.CONTROL_ID, "ModuleName"); //$NON-NLS-1$
		tc.setText(columnName);
		tc.setWidth(620);

		tableModules.addSelectionChangedListener(new ISelectionChangedListener()
		{
			public void selectionChanged(SelectionChangedEvent event)
			{
				handleModuleSelection((IStructuredSelection) event.getSelection());
			}
		});
		tableModules.addDoubleClickListener(new IDoubleClickListener()
		{
			public void doubleClick(DoubleClickEvent event)
			{
				handleModuleSelection((IStructuredSelection) event.getSelection());
				if (isPageComplete())
				{
					getWizard().getContainer().showPage(getNextPage());
				}
			}
		});


	}

	public void setContents()
	{
		tableModules.setContentProvider(contentProvider);
		tableModules.setLabelProvider(labelProvider);
		tableModules.setInput(ResourcesPlugin.getWorkspace().getRoot());
	}

	/**
	 * @param labelProvider
	 *        the labelProvider to set
	 */
	public void setLabelProvider(ITableLabelProvider labelProvider)
	{
		this.labelProvider = labelProvider;
	}

	/**
	 * @param contentProvider
	 *        the contentProvider to set
	 */
	public void setContentProvider(IContentProvider contentProvider)
	{
		this.contentProvider = contentProvider;
	}

	/**
	 * Refresh the table for modules
	 */
	protected void refreshTable()
	{

		tableModules.refresh();
	}

}
