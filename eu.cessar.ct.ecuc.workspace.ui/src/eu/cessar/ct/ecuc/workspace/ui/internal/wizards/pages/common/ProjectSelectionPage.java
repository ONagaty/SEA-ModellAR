package eu.cessar.ct.ecuc.workspace.ui.internal.wizards.pages.common;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TableColumn;

import eu.cessar.ct.ecuc.workspace.ui.internal.Messages;
import eu.cessar.ct.ecuc.workspace.ui.internal.providers.ProjectsTableContentProvider;
import eu.cessar.ct.ecuc.workspace.ui.internal.wizards.pages.AbstractWizardPage;

/**
 * The "New" wizard page allows setting the container for the new file as well
 * as the file name. The page will only accept file name without the extension
 * OR with the extension that matches the expected one (autosar).
 */

public class ProjectSelectionPage extends AbstractWizardPage
{
	private IStructuredSelection selection;
	private ProjectsTableContentProvider tblProvider;
	private TableViewer tblProjects;
	private Button btnDisplayHidden;

	private IProject selectedProject;

	/**
	 * Constructor for ModuleDefinitionWizardProjectPage.
	 * 
	 * @param selection
	 */
	public ProjectSelectionPage()
	{
		super("ModuleDefinitionWizardProjectPage"); // $NON-NLS$ //$NON-NLS-1$
	}

	public ProjectSelectionPage(IStructuredSelection selection)
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
		container.setLayout(super.getGridLayout(1, false));
		setControl(container);

		tblProjects = new TableViewer(container, SWT.BORDER | SWT.SINGLE | SWT.FULL_SELECTION);
		tblProjects.getTable().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		tblProvider = new ProjectsTableContentProvider();
		tblProjects.setContentProvider(tblProvider);
		tblProjects.setLabelProvider(tblProvider);
		tblProjects.setInput(ResourcesPlugin.getWorkspace().getRoot());
		tblProjects.addSelectionChangedListener(new ISelectionChangedListener()
		{
			public void selectionChanged(SelectionChangedEvent event)
			{
				handleProjectSelection((IStructuredSelection) event.getSelection());
			}
		});
		tblProjects.addDoubleClickListener(new IDoubleClickListener()
		{
			public void doubleClick(DoubleClickEvent event)
			{
				handleProjectSelection((IStructuredSelection) event.getSelection());
				if (isPageComplete())
				{
					getWizard().getContainer().showPage(getNextPage());
				}
			}
		});

		// Get the selected project from wizard and select it in the tree
		if (null != selection && !selection.isEmpty())
		{
			IProject selectedProject = ((IResource) selection.getFirstElement()).getProject();
			if (null != selectedProject)
			{
				tblProjects.setSelection(new StructuredSelection(selectedProject), true);
				tblProjects.refresh();
			}
		}

		TableColumn tc = new TableColumn(tblProjects.getTable(), SWT.NONE);
		tc.setWidth(500);

		btnDisplayHidden = new Button(container, SWT.CHECK);
		btnDisplayHidden.setLayoutData(new GridData());
		btnDisplayHidden.setData(AbstractWizardPage.CONTROL_ID, "btnDisplayHidden"); // $NON-NLS$ //$NON-NLS-1$
		btnDisplayHidden.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				tblProvider.showHiddenProjects(btnDisplayHidden.getSelection());
				tblProjects.refresh();
			}
		});
	}

	protected void handleProjectSelection(IStructuredSelection selection)
	{
		if (null != selection)
		{
			setSelectedProject((IProject) selection.getFirstElement());
		}
		else
		{
			setSelectedProject(null);
		}
		setPageComplete(isPageComplete());
		getContainer().updateMessage();
	}

	@Override
	public boolean isPageComplete()
	{
		return (null != selectedProject);
	}

	@Override
	public String getErrorMessage()
	{
		// Actually this will never be seen, since the wizard cannot continue
		// unless there is a project selected...
		if (null == selectedProject)
		{
			return Messages.ProjectSelectionPage_Err_No_Project;
		}
		return super.getErrorMessage();
	}

	public void setSelectedProject(IProject selectedProject)
	{
		this.selectedProject = selectedProject;
	}

	public IProject getSelectedProject()
	{
		return selectedProject;
	}
}