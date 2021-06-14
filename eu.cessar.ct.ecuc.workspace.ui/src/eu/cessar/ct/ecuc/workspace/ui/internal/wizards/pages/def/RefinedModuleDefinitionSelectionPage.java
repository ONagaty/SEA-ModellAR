/**
 * 
 */
package eu.cessar.ct.ecuc.workspace.ui.internal.wizards.pages.def;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;

import eu.cessar.ct.ecuc.workspace.ui.internal.Messages;
import eu.cessar.ct.ecuc.workspace.ui.internal.providers.RefinedModuleDefinitionTreeContentProvider;
import eu.cessar.ct.ecuc.workspace.ui.internal.providers.RefinedModuleDefinitionTreeLabelProvider;
import eu.cessar.ct.ecuc.workspace.ui.internal.wizards.AbstractWizard;
import eu.cessar.ct.ecuc.workspace.ui.internal.wizards.pages.AbstractWizardPage;
import gautosar.gecucparameterdef.GModuleDef;

/**
 * The "New" wizard page allows setting the container for the new file as well
 * as the file name. The page will only accept file name without the extension
 * OR with the extension that matches the expected one (autosar).
 */

public class RefinedModuleDefinitionSelectionPage extends AbstractWizardPage
{
	protected IProject project;

	private TreeViewer treeModuleDefinitions;

	private RefinedModuleDefinitionTreeContentProvider contentProvider;
	private RefinedModuleDefinitionTreeLabelProvider labelProvider;

	private GModuleDef selectedModuleDefinition = null;

	/**
	 * Constructor for RefinedModuleDefinitionSelectionPage.
	 * 
	 * @param selection
	 */
	public RefinedModuleDefinitionSelectionPage()
	{
		super("RefinedModuleDefinitionSelectionPage"); //$NON-NLS-1$
	}

	public RefinedModuleDefinitionSelectionPage(IStructuredSelection selection)
	{
		this();
		this.selection = selection;
	}

	/**
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

		treeModuleDefinitions = new TreeViewer(container, SWT.BORDER | SWT.FULL_SELECTION
			| SWT.SINGLE);
		Tree tree = treeModuleDefinitions.getTree();
		tree.setHeaderVisible(true);
		tree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));

		contentProvider = new RefinedModuleDefinitionTreeContentProvider(project);
		labelProvider = new RefinedModuleDefinitionTreeLabelProvider();
		treeModuleDefinitions.setContentProvider(contentProvider);
		treeModuleDefinitions.setLabelProvider(labelProvider);

		treeModuleDefinitions.setInput(ResourcesPlugin.getWorkspace().getRoot());

		TreeColumn tc = new TreeColumn(tree, SWT.NONE);
		tc.setData(AbstractWizardPage.CONTROL_ID, "ModuleNameColumn"); //$NON-NLS-1$
		tc.setWidth(300);

		tc = new TreeColumn(tree, SWT.NONE);
		tc.setData(AbstractWizardPage.CONTROL_ID, "ModuleFileColumn"); //$NON-NLS-1$
		tc.setWidth(300);

		treeModuleDefinitions.addSelectionChangedListener(new ISelectionChangedListener()
		{
			public void selectionChanged(SelectionChangedEvent event)
			{
				handleModuleSelection((IStructuredSelection) event.getSelection());
			}
		});
		treeModuleDefinitions.addDoubleClickListener(new IDoubleClickListener()
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

	protected void handleModuleSelection(IStructuredSelection selection)
	{
		if (null != selection && !selection.isEmpty()
			&& selection.getFirstElement() instanceof GModuleDef)
		{
			setSelectedModuleDefinition((GModuleDef) selection.getFirstElement());
		}
		else
		{
			setSelectedModuleDefinition(null);
		}
		setPageComplete(isPageComplete());
		getContainer().updateMessage();
	}

	@Override
	public boolean isPageComplete()
	{
		return (null != getSelectedModuleDefinition());
	}

	@Override
	public String getErrorMessage()
	{
		// Actually this will never be seen, since the wizard cannot continue
		// unless there is a module definition selected...
		if (null == getSelectedModuleDefinition())
		{
			return Messages.RefinedModuleDefinitionSelectionPage_Err_No_ModuleDefinition;
		}
		return super.getErrorMessage();
	}

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
					public void run(final IProgressMonitor monitor)
						throws InvocationTargetException
					{
						monitor.beginTask(
							NLS.bind(Messages.Progress_LoadingModuleDefinitions, project.getName()),
							IProgressMonitor.UNKNOWN);
						try
						{
							getShell().getDisplay().syncExec(new Runnable()
							{
								public void run()
								{
									// Update tree of modules
									contentProvider.setProject(project);
									treeModuleDefinitions.refresh();
									treeModuleDefinitions.expandAll();
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
			else
			{
				treeModuleDefinitions.expandAll();
			}
		}
		super.setVisible(visible);
	}

	public IProject getProject()
	{
		return project;
	}

	public void setProject(IProject project)
	{
		this.project = project;
	}

	public GModuleDef getSelectedModuleDefinition()
	{
		return selectedModuleDefinition;
	}

	public void setSelectedModuleDefinition(GModuleDef selectedModuleDefinition)
	{
		this.selectedModuleDefinition = selectedModuleDefinition;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.ecuc.workspace.ui.internal.wizards.pages.AbstractWizardPage#performDefault()
	 */
	@Override
	public void performDefault()
	{
		selectedModuleDefinition = null;
		treeModuleDefinitions.setSelection(null);

	}

}
