/**
 *
 */
package eu.cessar.ct.ecuc.workspace.ui.internal.wizards.pages.conversion;

import eu.cessar.ct.ecuc.workspace.ui.internal.CessarPluginActivator;
import eu.cessar.ct.ecuc.workspace.ui.internal.Messages;
import eu.cessar.ct.ecuc.workspace.ui.internal.providers.RefinedModuleConfigurationTableContentProvider;
import eu.cessar.ct.ecuc.workspace.ui.internal.wizards.ConvertConfigurationWizard;
import eu.cessar.ct.ecuc.workspace.ui.internal.wizards.pages.AbstractWizardPage;
import eu.cessar.ct.sdk.utils.SplitUtils;
import gautosar.gecucdescription.GModuleConfiguration;
import gautosar.ggenericstructure.ginfrastructure.GPackageableElement;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.osgi.util.NLS;
import org.eclipse.sphinx.emf.util.EcorePlatformUtil;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

/**
 * The Page for refined module configurations
 */
public class RefinedModuleConfigSelectionPage extends WizardPage
{
	/** The project. */
	private IProject project;

	/** The table module definitions. */
	private TableViewer tableModules;

	/** The label provider. */
	private ITableLabelProvider labelProvider;

	/**
	 * The content provider
	 */
	private IContentProvider contentProvider;
	// private StandardModuleConfigurationTableContentProvider provider;

	/** The selected module definition. */
	private GPackageableElement selectedModuleConfiguration;

	/** The content provider. */
	private RefinedModuleConfigurationTableContentProvider provider;

	private AgreementCompositeOverwrittenFiles overwriteComposite;

	/**
	 * Instantiates a new standard module definition selection page.
	 *
	 * @param project
	 *
	 * @param moduleDefinitionType
	 *        the module definition type
	 */
	public RefinedModuleConfigSelectionPage(IProject project)
	{
		super("Page"); //$NON-NLS-1$
		this.project = project;
		initializeProvider(project);
		setContentProvider(provider);

		setLabelProvider(provider);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jface.wizard.WizardPage#isPageComplete()
	 */
	@Override
	public boolean isPageComplete()
	{
		if (selectedModuleConfiguration == null)
		{
			return false;
		}
		Assert.isTrue(selectedModuleConfiguration instanceof GModuleConfiguration);
		boolean selection = overwriteComposite.getSelection();
		if (!selection)
		{
			return false;
		}
		return null != getSelectedModule() && !provider.isDuplicateModule((GModuleConfiguration) getSelectedModule());

	}

	@Override
	public boolean canFlipToNextPage()
	{
		return false;
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
		if (!overwriteComposite.getSelection())
		{
			return Messages.RefinedModuleConfigurationAgreement_Description;
		}
		return super.getErrorMessage();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void createControl(Composite parent)
	{

		setTitle(Messages.RefinedConfigurationSelectionPage_Title);
		setDescription(Messages.RefinedConfigurationSelectionPage_Description);
		Composite container = new Composite(parent, SWT.NULL);
		container.setLayout(new GridLayout(1, false));
		setControl(container);

		Label label = new Label(container, SWT.NULL);
		label.setData(AbstractWizardPage.CONTROL_ID, "label"); //$NON-NLS-1$

		tableModules = new TableViewer(container, SWT.BORDER | SWT.FULL_SELECTION | SWT.SINGLE);
		Table table = tableModules.getTable();
		table.setHeaderVisible(true);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		TableColumn tc = new TableColumn(table, SWT.NONE);
		//		tc.setData(AbstractWizardPage.CONTROL_ID, "ModuleNameColumn"); //$NON-NLS-1$
		tc.setWidth(200);
		tc.setText(Messages.ModuleConfigurationColumn);
		tc = new TableColumn(table, SWT.NONE);
		//		tc.setData(AbstractWizardPage.CONTROL_ID, "ModuleFileColumn"); //$NON-NLS-1$
		tc.setWidth(200);
		tc.setText(Messages.CurrentModuleDefinitionColumn);
		tc = new TableColumn(table, SWT.NONE);
		//		tc.setData(AbstractWizardPage.CONTROL_ID, "ModuleFileColumn"); //$NON-NLS-1$
		tc.setWidth(200);
		tc.setText(Messages.StandardModuleDefinitionColumn);
		tableModules.setContentProvider(contentProvider);
		tableModules.setLabelProvider(labelProvider);
		tableModules.setInput(ResourcesPlugin.getWorkspace().getRoot());

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

		overwriteComposite = new AgreementCompositeOverwrittenFiles();
		overwriteComposite.createContent(container);
		overwriteComposite.addSelectionListener(new SelectionAdapter()
		{

			@Override
			public void widgetSelected(SelectionEvent event)
			{
				setPageComplete(isPageComplete());
				getContainer().updateMessage();

			}

		});

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
	private void refreshTable()
	{

		tableModules.refresh();
	}

	/**
	 * @param config
	 */
	public void updateInfoMessage(GModuleConfiguration config)
	{
		List<String> configurationsPaths = new ArrayList<String>();
		GModuleConfiguration mergedInstance = SplitUtils.getMergedInstance(config);
		Collection<GModuleConfiguration> concreteInstances = SplitUtils.getConcreteInstances(mergedInstance);
		for (GModuleConfiguration configInstance: concreteInstances)
		{
			IFile definingFile = EcorePlatformUtil.getFile(configInstance);
			if (definingFile != null)
			{
				String text = definingFile.getFullPath().toString();
				configurationsPaths.add(text);

			}

		}
		overwriteComposite.updateInfoMessage(configurationsPaths);

	}

	/**
	 * Handle module selection.
	 *
	 * @param currentSelection
	 *        the current selection
	 */
	private void handleModuleSelection(IStructuredSelection currentSelection)
	{
		if (null != currentSelection && !currentSelection.isEmpty()
			&& currentSelection.getFirstElement() instanceof GPackageableElement)
		{
			GModuleConfiguration firstElement = (GModuleConfiguration) currentSelection.getFirstElement();
			setSelectedModuleConfiguration(firstElement);
			updateInfoMessage(firstElement);
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
	 * @see org.eclipse.jface.dialogs.DialogPage#setVisible(boolean)
	 */
	@Override
	public void setVisible(final boolean visible)
	{
		if (visible)
		{
			project = ((ConvertConfigurationWizard) getWizard()).getProject();

			setProject(project);
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
								refreshTable();
							}
						});
					}
					// CHECKSTYLE:OFF
					catch (Exception e)
					{
						CessarPluginActivator.getDefault().logError(e);
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
				CessarPluginActivator.getDefault().logError(e);
			}
			// CHECKSTYLE:ON

		}
		else
		{
			overwriteComposite.resetButtons();
		}
		super.setVisible(visible);
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
	 * Gets the project.
	 *
	 * @return the project
	 */
	public IProject getProject()
	{
		return project;
	}

	private RefinedModuleConfigurationTableContentProvider initializeProvider(IProject proj)
	{
		provider = new RefinedModuleConfigurationTableContentProvider(proj);
		return provider;
	}
}
