package eu.cessar.ct.runtime.ui.execution;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.ui.AbstractLaunchConfigurationTab;
import org.eclipse.jdt.ui.JavaElementLabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;
import org.eclipse.ui.dialogs.ElementTreeSelectionDialog;
import org.eclipse.ui.dialogs.ISelectionStatusValidator;

import eu.cessar.ct.core.platform.nature.CessarNature;
import eu.cessar.ct.runtime.ui.internal.CessarPluginActivator;

/**
 * An abstract launch configuration tab for CESSAR-CT specific applications.
 *
 */
public abstract class AbstractCessarLaunchConfigurationTab extends AbstractLaunchConfigurationTab
{
	private final static String TAB_NAME = "Main"; //$NON-NLS-1$
	private final static String PROJECT_GROUP_NAME = "&CESSAR-CT project"; //$NON-NLS-1$

	private final static String SELECT_BUTTON_TEXT = "&Browse..."; //$NON-NLS-1$
	private final static String SELECT_PROJ_DIALOG_TITLE = "Project selection"; //$NON-NLS-1$
	private final static String SELECT_PROJ_DIALOG_MSG = "Select a project to constraint your search"; //$NON-NLS-1$

	private static final IStatus SELECT_FILE_DIALOG_OK_STATUS = new Status(IStatus.OK, CessarPluginActivator.PLUGIN_ID,
		""); //$NON-NLS-1$

	private int isProjectAdded;
	private Text fSelectProjText;
	private Button fSelectProjButton;
	private WidgetListener fListener = new WidgetListener();

	protected class WidgetListener implements ModifyListener, SelectionListener
	{

		public void modifyText(final ModifyEvent e)
		{
			updateLaunchConfigurationDialog();
		}

		public void widgetDefaultSelected(final SelectionEvent e)
		{/* do nothing */
		}

		public void widgetSelected(final SelectionEvent e)
		{
			doWidgetSelected(e);
			updateLaunchConfigurationDialog();
		}
	}

	private final class FileTreeProvider implements ITreeContentProvider
	{
		private final List<IFile> files;

		private FileTreeProvider(final List<IFile> files)
		{
			this.files = files;
			isProjectAdded = 0;
		}

		public IResource[] refine(final IResource[] resources)
		{
			List<IResource> result = new ArrayList<IResource>();
			for (IResource res: resources)
			{
				IPath resPath = res.getFullPath();
				for (IFile file: files)
				{
					IPath path = file.getFullPath();
					// put also the parent folders
					if (resPath.isPrefixOf(path))
					{
						result.add(res);
						break;
					}
				}
			}
			return result.toArray(new IResource[result.size()]);
		}

		public Object[] getChildren(final Object parentElement)
		{
			if (parentElement instanceof IContainer)
			{
				try
				{
					return refine(((IContainer) parentElement).members());
				}
				catch (CoreException e)
				{
					CessarPluginActivator.getDefault().logError(e);
					return new IResource[0];
				}
			}
			else
			{
				return new IResource[0];
			}
			// return getElements(parentElement);
		}

		public Object getParent(final Object element)
		{
			if (element instanceof IResource)
			{
				return ((IResource) element).getParent();
			}
			else
			{
				return null;
			}
		}

		public boolean hasChildren(final Object element)
		{
			try
			{
				return (element instanceof IContainer) && ((IContainer) element).members().length > 0;
			}
			catch (CoreException e)
			{
				CessarPluginActivator.getDefault().logError(e);
				return false;
			}
		}

		public Object[] getElements(final Object inputElement)
		{
			if (isProjectAdded != 2 && inputElement instanceof IProject && files.size() > 0)
				// if (inputElement instanceof IProject && jetFiles.size() > 0)
			{
				isProjectAdded++;
				return new IProject[] {(IProject) inputElement};
			}
			else
			{
				return getChildren(inputElement);
			}

		}

		public void dispose()
		{
			// nothing to do
		}

		public void inputChanged(final Viewer viewer, final Object oldInput, final Object newInput)
		{
			// nothing to do
		}
	}

	/**
	 * @return
	 */
	protected String getSelectButtonText()
	{
		return SELECT_BUTTON_TEXT;
	}

	/**
	 * @return
	 */
	protected String getProjectEditorText()
	{
		return fSelectProjText.getText().trim();
	}

	/**
	 * @return
	 */
	protected WidgetListener getListener()
	{
		return fListener;
	}

	/**
	 * @param e
	 */
	protected void doWidgetSelected(SelectionEvent e)
	{
		Object source = e.getSource();
		if (source == fSelectProjButton)
		{
			handleProjectButtonSelected();
		}
	}

	/**
	 *
	 * @return
	 */
	protected abstract String doGetFileEditorText();

	/**
	 *
	 * @param text
	 * @return
	 */
	protected abstract void doSetFileEditorText(String text);

	/**
	 *
	 * @return
	 */
	protected abstract String getGroupName();

	/**
	 *
	 * @return
	 */
	protected abstract String getSelectFileDialogTitle();

	/**
	 *
	 * @return
	 */
	protected abstract String getSelectFileDialogMsg();

	/**
	 *
	 * @return
	 */
	protected abstract IStatus getSelectFileDialogErrStatus();

	/**
	 *
	 * @return
	 */
	protected abstract String getProjectAttribute();

	/**
	 *
	 * @return
	 */
	protected abstract String getFileAttribute();

	/**
	 *
	 */
	protected abstract void collectFiles(List<IFile> files, IProject cessarProject);

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.ui.ILaunchConfigurationTab#createControl(org.eclipse.swt.widgets.Composite)
	 */
	public void createControl(final Composite parent)
	{
		Composite comp = new Composite(parent, SWT.NONE);
		GridLayout gl = new GridLayout(1, false);
		gl.verticalSpacing = 0;
		comp.setLayout(gl);
		GridData gd = new GridData(GridData.FILL_BOTH);
		comp.setLayoutData(gd);

		createProjectGroupEditor(comp);
		createVerticalSpacer(comp, 1);
		Group fileGroupEditor = createFileGroupEditor(comp);
		addContentsToFileGroupEditor(fileGroupEditor);
		setControl(comp);
	}

	/**
	 * @param group
	 */
	protected abstract void addContentsToFileGroupEditor(Group group);

	/**
	 * @param parent
	 */
	protected void createProjectGroupEditor(final Composite parent)
	{
		Font font = parent.getFont();
		Group group = new Group(parent, SWT.NONE);
		group.setText(PROJECT_GROUP_NAME);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		group.setLayoutData(gd);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		group.setLayout(layout);
		group.setFont(font);
		fSelectProjText = new Text(group, SWT.SINGLE | SWT.BORDER);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		fSelectProjText.setLayoutData(gd);
		fSelectProjText.setFont(font);
		fSelectProjText.addModifyListener(fListener);
		fSelectProjButton = createPushButton(group, SELECT_BUTTON_TEXT, null);
		fSelectProjButton.addSelectionListener(fListener);
	}

	/**
	 * @param parent
	 */
	protected Group createFileGroupEditor(final Composite parent)
	{
		Font font = parent.getFont();
		Group group = new Group(parent, SWT.NONE);
		group.setText(getGroupName());
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		group.setLayoutData(gd);
		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		group.setLayout(layout);
		group.setFont(font);
		return group;
	}

	/**
	 * Show a dialog that lets the user select a project. This in turn provides context for the Jet, allowing the user
	 * to key a jet, or constraining the search for jets to the specified project.
	 */
	protected void handleProjectButtonSelected()
	{
		IProject project = chooseProject();
		if (project == null)
		{
			return;
		}
		String projectName = project.getName();
		fSelectProjText.setText(projectName);
	}

	/**
	 *
	 * @return
	 */
	private IProject chooseProject()
	{
		ElementListSelectionDialog dialog = new ElementListSelectionDialog(getShell(), new JavaElementLabelProvider());
		dialog.setTitle(SELECT_PROJ_DIALOG_TITLE);
		dialog.setMessage(SELECT_PROJ_DIALOG_MSG);
		dialog.setElements(collectCESSARProjects());

		// initial selection is the project from the fSelectProjText
		IProject cessarProject = getProjectFromTextEditor();
		if (cessarProject != null)
		{
			dialog.setInitialSelections(new Object[] {cessarProject});
		}
		// get the selection
		if (dialog.open() == Window.OK)
		{
			return (IProject) dialog.getFirstResult();
		}
		else
		{
			return null;
		}
	}

	/**
	 * ???Utility method???
	 *
	 * @return
	 */
	private IProject[] collectCESSARProjects()
	{
		List<IProject> result = new ArrayList<IProject>();
		IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
		for (IProject project: projects)
		{
			if (CessarNature.haveNature(project))
			{
				result.add(project);
			}
		}
		return result.toArray(new IProject[result.size()]);
	}

	/**
	 * @return
	 */
	private IProject getProjectFromTextEditor()
	{
		String prjName = fSelectProjText.getText().trim();
		if (prjName.length() == 0)
		{
			return null;
		}
		else
		{
			IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(prjName);
			if (project != null && project.exists() && project.isOpen())
			{
				return project;
			}
			else
			{
				return null;
			}
		}
	}

	/**
	 * Show a dialog that lets the user select a jet from the current project.
	 */
	protected void handleFileButtonSelected()
	{
		try
		{
			IProject cessarProject = getProjectFromTextEditor();
			if (cessarProject != null)
			{
				cessarProject.refreshLocal(IResource.DEPTH_INFINITE, null);
			}
		}
		catch (CoreException e)
		{
			CessarPluginActivator.getDefault().logError(e);
		}
		IPath filePath = chooseFile();
		if (filePath == null)
		{
			return;
		}
		doSetFileEditorText(filePath.toPortableString());
	}

	/**
	 * @return
	 */
	private IPath chooseFile()
	{
		IProject cessarProject = getProjectFromTextEditor();
		if (cessarProject == null)
		{
			return null;
		}
		List<IFile> files = new ArrayList<IFile>();
		collectFiles(files, cessarProject);

		// initialize dialog for selection
		ElementTreeSelectionDialog dialog = new ElementTreeSelectionDialog(getShell(), new JavaElementLabelProvider(),
			new FileTreeProvider(files));
		dialog.setTitle(getSelectFileDialogTitle());
		dialog.setMessage(getSelectFileDialogMsg());
		dialog.setInput(cessarProject);
		dialog.setAllowMultiple(false);
		dialog.setDoubleClickSelects(true);

		// validate selection
		dialog.setValidator(new ISelectionStatusValidator()
		{
			public IStatus validate(final Object[] selection)
			{
				if (selection == null || selection.length != 1)
				{
					return getSelectFileDialogErrStatus();
				}
				else if (selection[0] instanceof IFile)
				{
					return SELECT_FILE_DIALOG_OK_STATUS;
				}
				else
				{
					return getSelectFileDialogErrStatus();
				}
			}
		});

		// initial selection is the file from the fSelectJetText
		IFile file = getFileFromTextEditor();
		if (file != null)
		{
			dialog.setInitialSelection(file);
		}
		if (dialog.open() == Window.OK)
		{
			return ((IFile) dialog.getFirstResult()).getProjectRelativePath();
		}
		else
		{
			return null;
		}

	}

	/**
	 * @return
	 */
	private IFile getFileFromTextEditor()
	{
		String fileName = doGetFileEditorText();
		if (fileName.length() == 0)
		{
			return null;
		}
		else
		{
			IProject cessarProject = getProjectFromTextEditor();
			if (cessarProject != null)
			{
				IFile file = cessarProject.getFile(new Path(fileName));
				if (file != null && file.exists())
				{
					return file;
				}
			}
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.ui.ILaunchConfigurationTab#getName()
	 */
	public String getName()
	{
		return TAB_NAME;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.ui.ILaunchConfigurationTab#initializeFrom(org.eclipse.debug.core.ILaunchConfiguration)
	 */
	public void initializeFrom(final ILaunchConfiguration configuration)
	{
		try
		{
			fSelectProjText.setText(configuration.getAttribute(getProjectAttribute(), "")); //$NON-NLS-1$

			String fileAttributeValue = configuration.getAttribute(getFileAttribute(), ""); //$NON-NLS-1$
			if (fileAttributeValue != null && !"".equals(fileAttributeValue)) //$NON-NLS-1$
			{
				doSetFileEditorText(fileAttributeValue);
			}
		}
		catch (CoreException e)
		{
			CessarPluginActivator.getDefault().logError(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.debug.ui.ILaunchConfigurationTab#performApply(org.eclipse.debug.core.ILaunchConfigurationWorkingCopy)
	 */
	public void performApply(final ILaunchConfigurationWorkingCopy configuration)
	{
		configuration.setAttribute(getProjectAttribute(), fSelectProjText.getText());
		String fileEditorText = doGetFileEditorText();
		if (fileEditorText != null)
		{
			configuration.setAttribute(getFileAttribute(), fileEditorText);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.debug.ui.ILaunchConfigurationTab#setDefaults(org.eclipse.debug.core.ILaunchConfigurationWorkingCopy)
	 */
	public void setDefaults(final ILaunchConfigurationWorkingCopy configuration)
	{
		configuration.setAttribute(getProjectAttribute(), ""); //$NON-NLS-1$
		configuration.setAttribute(getFileAttribute(), ""); //$NON-NLS-1$
	}

}
