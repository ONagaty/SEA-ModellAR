/**
 * 
 */
package eu.cessar.ct.ecuc.workspace.ui.internal.wizards.pages.def;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import eu.cessar.ct.core.mms.EResourceUtils;
import eu.cessar.ct.ecuc.workspace.jobs.ModuleDefinitionType;
import eu.cessar.ct.ecuc.workspace.ui.internal.Messages;
import eu.cessar.ct.ecuc.workspace.ui.internal.providers.StandardModuleDefinitionTableContentProvider;
import eu.cessar.ct.ecuc.workspace.ui.internal.wizards.AbstractWizard;
import eu.cessar.ct.ecuc.workspace.ui.internal.wizards.pages.AbstractWizardPage;
import gautosar.gecucparameterdef.GModuleDef;

/**
 * The Class StandardModuleDefinitionSelectionPage.
 */
public class StandardModuleDefinitionSelectionPage extends AbstractWizardPage
{

	/** The project. */
	protected IProject project;

	/** The table module definitions. */
	private TableViewer tableModuleDefinitions;

	/** The provider. */
	private StandardModuleDefinitionTableContentProvider provider;

	/** The selected module definition. */
	private GModuleDef selectedModuleDefinition;

	/** The module definition type. */
	private ModuleDefinitionType moduleDefinitionType;

	/**
	 * Constructor for ModuleDefinitionWizardProjectPage.
	 * 
	 */
	public StandardModuleDefinitionSelectionPage()
	{
		super("StandardModuleDefinitionSelectionPage"); //$NON-NLS-1$
	}

	/**
	 * Instantiates a new standard module definition selection page.
	 * 
	 * @param selection
	 *        the selection
	 */
	public StandardModuleDefinitionSelectionPage(IStructuredSelection selection)
	{
		this();
		this.selection = selection;
	}

	/**
	 * Instantiates a new standard module definition selection page.
	 * 
	 * @param selection
	 *        the selection
	 * @param moduleDefinitionType
	 *        the module definition type
	 */
	public StandardModuleDefinitionSelectionPage(IStructuredSelection selection,
		ModuleDefinitionType moduleDefinitionType)
	{
		this();
		this.selection = selection;
		this.moduleDefinitionType = moduleDefinitionType;
		provider.setModuleDefinitionType(moduleDefinitionType);
	}

	/**
	 * Do create control.
	 * 
	 * @param parent
	 *        the parent
	 * @see AbstractWizardPage#createControl(Composite)
	 * @see AbstractWizardPage#doCreateControl(Composite)
	 */
	@Override
	protected void doCreateControl(Composite parent)
	{
		Composite container = new Composite(parent, SWT.NULL);
		container.setLayout(getGridLayout(1, false));
		setControl(container);

		Label label = new Label(container, SWT.NULL);
		label.setData(AbstractWizardPage.CONTROL_ID, "label"); //$NON-NLS-1$

		tableModuleDefinitions = new TableViewer(container, SWT.BORDER | SWT.FULL_SELECTION | SWT.SINGLE);
		Table table = tableModuleDefinitions.getTable();
		table.setHeaderVisible(true);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));

		provider = new StandardModuleDefinitionTableContentProvider(project);
		tableModuleDefinitions.setContentProvider(provider);
		tableModuleDefinitions.setLabelProvider(provider);
		tableModuleDefinitions.setInput(ResourcesPlugin.getWorkspace().getRoot());

		TableColumn tc = new TableColumn(table, SWT.NONE);
		tc.setData(AbstractWizardPage.CONTROL_ID, "ModuleNameColumn"); //$NON-NLS-1$
		tc.setWidth(300);

		tc = new TableColumn(table, SWT.NONE);
		tc.setData(AbstractWizardPage.CONTROL_ID, "ModuleFileColumn"); //$NON-NLS-1$
		tc.setWidth(300);

		tableModuleDefinitions.addSelectionChangedListener(new ISelectionChangedListener()
		{
			public void selectionChanged(SelectionChangedEvent event)
			{
				handleModuleSelection((IStructuredSelection) event.getSelection());
			}
		});
		tableModuleDefinitions.addDoubleClickListener(new IDoubleClickListener()
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

	/**
	 * Handle module selection.
	 * 
	 * @param currentSelection
	 *        the current selection
	 */
	protected void handleModuleSelection(IStructuredSelection currentSelection)
	{
		if (null != currentSelection && !currentSelection.isEmpty()
			&& currentSelection.getFirstElement() instanceof GModuleDef)
		{
			setSelectedModuleDefinition((GModuleDef) currentSelection.getFirstElement());
		}
		else
		{
			setSelectedModuleDefinition(null);
		}
		setPageComplete(isPageComplete());
		getContainer().updateMessage();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.wizard.WizardPage#isPageComplete()
	 */
	@Override
	public boolean isPageComplete()
	{
		return null != getSelectedModuleDefinition() && !provider.isDuplicateModule(getSelectedModuleDefinition());

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.DialogPage#getErrorMessage()
	 */
	@Override
	public String getErrorMessage()
	{
		// Actually this will never be seen, since the wizard cannot continue
		// unless there is a module definition selected...
		if (null == getSelectedModuleDefinition())
		{
			return Messages.StandardModuleDefinitionSelectionPage_Err_No_ModuleDefinition;
		}
		return super.getErrorMessage();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.DialogPage#setVisible(boolean)
	 */
	@Override
	public void setVisible(final boolean visible)
	{
		if (visible)
		{
			setNextPageDefault(this);
			IProject wizardProject = ((AbstractWizard) getWizard()).getProject();

			if (null != wizardProject && !wizardProject.equals(project))
			{
				setProject(wizardProject);
				IRunnableWithProgress op = new IRunnableWithProgress()
				{
					public void run(final IProgressMonitor monitor) throws InvocationTargetException
					{
						monitor.beginTask(NLS.bind(Messages.Progress_LoadingModuleDefinitions, project.getName()),
							IProgressMonitor.UNKNOWN);
						try
						{
							getShell().getDisplay().syncExec(new Runnable()
							{
								public void run()
								{
									// Update tree of modules
									provider.setProject(project);
									tableModuleDefinitions.refresh();
								}
							});
						}
						catch (Exception e)
						{
							e.printStackTrace();
						}
						monitor.done();
					}
				};
				try
				{
					getContainer().run(true, false, op);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		}
		super.setVisible(visible);
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
	public GModuleDef getSelectedModuleDefinition()
	{
		return selectedModuleDefinition;
	}

	/**
	 * Sets the selected module definition.
	 * 
	 * @param selectedModuleDefinition
	 *        the new selected module definition
	 */
	public void setSelectedModuleDefinition(GModuleDef selectedModuleDefinition)
	{
		if (selectedModuleDefinition == null)
		{
			this.selectedModuleDefinition = null;

		}
		else
		{
			Resource res = selectedModuleDefinition.eResource();
			Collection<Resource> resources = EResourceUtils.getProjectResources(project);
			if (resources.contains(res))
			{
				this.selectedModuleDefinition = null;
			}
			else
			{
				this.selectedModuleDefinition = selectedModuleDefinition;
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.ecuc.workspace.ui.internal.wizards.pages.AbstractWizardPage#performDefault()
	 */
	@Override
	public void performDefault()
	{
		selectedModuleDefinition = null;
	}

	/**
	 * Unloads the resource that was loaded in order to initialize the Standard Module Definitions (EcucParamDef.arxml).
	 */
	public void unloadStandardModuleDefResource()
	{
		provider.unloadStandardModuleDefinitionResource();
	}

	/**
	 * Gets the module definition type.
	 * 
	 * @return the moduleDefinitionType
	 */
	public ModuleDefinitionType getModuleDefinitionType()
	{
		return moduleDefinitionType;
	}

	/**
	 * Sets the module definition type.
	 * 
	 * @param moduleDefinitionType
	 *        the moduleDefinitionType to set
	 */
	public void setModuleDefinitionType(ModuleDefinitionType moduleDefinitionType)
	{
		this.moduleDefinitionType = moduleDefinitionType;
		provider.setModuleDefinitionType(moduleDefinitionType);
	}

	@Override
	public boolean canFlipToNextPage()
	{
		return null != getSelectedModuleDefinition() && !provider.isDuplicateModule(getSelectedModuleDefinition());
	}
}
