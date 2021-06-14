/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidw6647<br/>
 * Jun 5, 2015 10:48:50 AM
 *
 * </copyright>
 */
package eu.cessar.ct.runtime.ecuc.ui.internal.wizards;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.ui.ISharedImages;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.ElementTreeSelectionDialog;
import org.eclipse.ui.model.WorkbenchContentProvider;
import org.eclipse.ui.model.WorkbenchLabelProvider;

import eu.cessar.ct.runtime.ecuc.ui.internal.CessarPluginActivator;
import eu.cessar.ct.runtime.ecuc.ui.internal.Messages;

/**
 * Export Pluget Wizard page
 *
 * @author uidg4449
 *
 *         %created_by: uidg4449 %
 *
 *         %date_created: Mon Jul 6 14:37:43 2015 %
 *
 *         %version: 2 %
 */
public class ExportPlugetPage extends WizardPage
{

	/**
	 * Zip Extension
	 */
	public static final String ZIP = "*.zip"; //$NON-NLS-1$
	/**
	 * Jar Extension
	 */
	public static final String JAR = "*.jar"; //$NON-NLS-1$

	/**
	 * Used to save data in settings
	 */
	public static final String EXPORT_PLUGET_IMPORT_JARS = "Export_Pluget_Import_Jars"; //$NON-NLS-1$
	/**
	 * Used to save data in settings
	 */
	public static final String EXPORT_PLUGET_SRC = "Export_Pluget_SRC"; //$NON-NLS-1$

	/**
	 * Used to save data in settings
	 */
	public static final String EXPORT_PLUGET_LASTNAME = "Export_Pluget_LASTNAME_"; //$NON-NLS-1$

	/**
	 *
	 */
	public static final String EXPORT_PLUGET_VERSION = "_VERSION"; //$NON-NLS-1$

	/**
	 * Pluget extensions
	 */
	private static final String PLUGET = "*.pluget"; //$NON-NLS-1$

	/**
	 * Java extensions
	 */
	private static final String JAVAEXTENSION = ".java"; //$NON-NLS-1$
	/**
	 * Pluget extensions
	 */
	private static final String PLUGETEXTENSION = ".pluget"; //$NON-NLS-1$

	/**
	 * File Separator
	 */
	private static final String FILE_SEPARATOR = System.getProperty("file.separator"); //$NON-NLS-1$ass

	/**
	 * List with the available source folders in the project
	 */
	private TableViewer srcListViewer;
	/**
	 * List with added libraries
	 */
	private TableViewer jarListViewer;

	// private boolean fInitiallySelecting = true;

	private Text plugetDestination;
	private IProject thisProject;
	private List<Object> jarInput;
	private Button deleteSelection;
	private List<String> externalResources;
	private String plugetName;
	private Text plugetVersion;

	// private Button fDestinationBrowseButton;

	/**
	 * @param project
	 * @param plugetFileName
	 * @param pageName
	 * @param selection
	 * @param jarPackage
	 */
	public ExportPlugetPage(IProject project, String plugetFileName, String pageName)
	{
		super(pageName);
		thisProject = project;
		plugetName = plugetFileName;
		setTitle(Messages.EXPORT_PLUGET_WIZARD_TITLE);
		setDescription(Messages.EXPORT_PLUGET_WIZARD_DESCRIPTION);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void createControl(Composite parent)
	{
		initializeDialogUnits(parent);

		Composite composite = new Composite(parent, SWT.FILL);
		composite.setLayout(new GridLayout(1, false));
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		Label label = new Label(composite, SWT.None);
		label.setText(Messages.EXPORT_PLUGET_WIZARD_DESCRIPTION_RESOURCE);
		label.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		createInputGroup(composite);

		new Label(composite, SWT.NONE); // vertical spacer

		Label exportLable = new Label(composite, SWT.FILL);
		exportLable.setText(Messages.EXPORT_PLUGET_WIZARD_DESCRIPTION_OUTPUT);
		exportLable.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		createDestinationGroup(composite);

		createVersionGroup(composite);

		externalResources = new ArrayList<String>();

		setControl(composite);

	}

	/**
	 * Open an appropriate destination browser so that the user can specify a source to import from
	 */
	private void handleDestinationBrowseButtonPressed()
	{
		FileDialog dialog = new FileDialog(getContainer().getShell(), SWT.SAVE);
		dialog.setFilterExtensions(new String[] {PLUGET});

		String string = CessarPluginActivator.getDefault().getDialogSettings().get(EXPORT_PLUGET_LASTNAME + plugetName);
		if (string == null)
		{
			string = ""; //$NON-NLS-1$
		}
		dialog.setFilterPath(string);
		dialog.setFileName(string);

		String selectedFileName = dialog.open();
		if (selectedFileName != null)
		{
			plugetDestination.setText(selectedFileName);
			CessarPluginActivator.getDefault().getDialogSettings().put(EXPORT_PLUGET_LASTNAME + plugetName,
				selectedFileName);
		}
	}

	/**
	 * @param parent
	 */
	private void createDestinationGroup(Composite parent)
	{
		Composite destinationSelectionGroup = new Composite(parent, SWT.NONE);
		destinationSelectionGroup.setLayout(new GridLayout(3, false));
		destinationSelectionGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));

		new Label(destinationSelectionGroup, SWT.NONE).setText(Messages.EXPORT_PLUGET_WIZARD_DESCRIPTION_PLUGET);

		// destination name entry field
		plugetDestination = new Text(destinationSelectionGroup, SWT.SINGLE | SWT.BORDER);
		plugetDestination.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		String string = CessarPluginActivator.getDefault().getDialogSettings().get(EXPORT_PLUGET_LASTNAME + plugetName);
		if (string == null)
		{
			String projectLocation = thisProject.getLocation().toOSString();
			string = projectLocation + FILE_SEPARATOR + plugetName.replace(JAVAEXTENSION, PLUGETEXTENSION);
			CessarPluginActivator.getDefault().getDialogSettings().put(EXPORT_PLUGET_LASTNAME + plugetName, string);
		}
		plugetDestination.setText(string);
		plugetDestination.addModifyListener(new ModifyListener()
		{
			@Override
			public void modifyText(ModifyEvent e)
			{
				getContainer().updateButtons();

			}
		});
		// destination browse button
		Button fDestinationBrowseButton = new Button(destinationSelectionGroup, SWT.PUSH);
		fDestinationBrowseButton.setText(Messages.EXPORT_PLUGET_WIZARD_BROWSE);
		fDestinationBrowseButton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
		// SWTUtil.setButtonDimensionHint(fDestinationBrowseButton);
		fDestinationBrowseButton.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				handleDestinationBrowseButtonPressed();
			}
		});
	}

	private void createVersionGroup(Composite parent)
	{
		Composite plugetVersionComposite = new Composite(parent, SWT.NONE);
		plugetVersionComposite.setLayout(new GridLayout(3, false));
		plugetVersionComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));

		new Label(plugetVersionComposite, SWT.NONE).setText(Messages.EXPORT_PLUGET_WIZARD_VERSION);

		// destination name entry field
		plugetVersion = new Text(plugetVersionComposite, SWT.SINGLE | SWT.BORDER);
		plugetVersion.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		String string = CessarPluginActivator.getDefault().getDialogSettings().get(
			EXPORT_PLUGET_LASTNAME + plugetName + EXPORT_PLUGET_VERSION);

		if (string == null)
		{
			string = ""; //$NON-NLS-1$
		}

		plugetVersion.setText(string);
		plugetVersion.addModifyListener(new ModifyListener()
		{
			@Override
			public void modifyText(ModifyEvent e)
			{
				getContainer().updateButtons();

			}
		});
	}

	/**
	 * Creates the content of the wizard
	 *
	 * @param parent
	 *        the parent control
	 */
	protected void createInputGroup(Composite parent)
	{
		Composite composite = new Composite(parent, SWT.FILL);
		composite.setLayout(new GridLayout(3, false));
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		createSrcSelection(composite);
		createJarSelection(composite);
		createActions(composite);

	}

	/**
	 * Creates the table viewer with the sources
	 *
	 * @param parent
	 */
	private void createSrcSelection(Composite parent)
	{
		srcListViewer = new TableViewer(parent, SWT.CHECK | SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
		srcListViewer.getTable().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		srcListViewer.setContentProvider(new ArrayContentProvider());
		ISharedImages images = JavaUI.getSharedImages();
		final Image image = images.getImage(ISharedImages.IMG_OBJS_PACKFRAG_ROOT);
		srcListViewer.setLabelProvider(new LabelProvider()
		{
			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.jface.viewers.LabelProvider#getImage(java.lang.Object)
			 */
			@Override
			public Image getImage(Object element)
			{

				return image;
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.jface.viewers.LabelProvider#getText(java.lang.Object)
			 */
			@Override
			public String getText(Object element)
			{
				String string = ((IPackageFragmentRoot) element).getPath().lastSegment();
				return string;
			}
		});
		srcListViewer.setInput(getInput());
		updateSelectionSources();
		srcListViewer.addSelectionChangedListener(new ISelectionChangedListener()
		{

			@Override
			public void selectionChanged(SelectionChangedEvent event)
			{
				getContainer().updateButtons();
			}
		});

	}

	/**
	 * Updates the selection in the source list
	 */
	private void updateSelectionSources()
	{
		String[] selectedSources = CessarPluginActivator.getDefault().getDialogSettings().getArray(
			EXPORT_PLUGET_LASTNAME + plugetName + EXPORT_PLUGET_SRC);
		if (selectedSources == null)
		{
			return;
		}
		TableItem[] items = srcListViewer.getTable().getItems();
		for (TableItem tableItem: items)
		{
			Object data = tableItem.getData();
			IResource resource = ((IPackageFragmentRoot) data).getResource();
			String osString = resource.getLocation().toOSString();
			for (String src: selectedSources)
			{
				if (osString.equals(src))
				{
					tableItem.setChecked(true);
				}
			}
		}
	}

	private List<IPackageFragmentRoot> getInput()
	{
		List<IPackageFragmentRoot> sources = new ArrayList<IPackageFragmentRoot>();
		IPackageFragmentRoot[] allPackageFragmentRoots;
		try
		{
			IJavaProject javaProject = JavaCore.create(thisProject);
			allPackageFragmentRoots = javaProject.getAllPackageFragmentRoots();

			for (IPackageFragmentRoot iPackageFragmentRoot: allPackageFragmentRoots)
			{

				if (iPackageFragmentRoot.getKind() == IPackageFragmentRoot.K_SOURCE)
				{
					sources.add(iPackageFragmentRoot);
				}
			}

		}

		catch (JavaModelException e)
		{
			CessarPluginActivator.getDefault().logError(e);
		}
		return sources;
	}

	/**
	 * Creates the tableviewer with the other files
	 *
	 * @param parent
	 */
	private void createJarSelection(Composite parent)
	{
		jarListViewer = new TableViewer(parent, SWT.FILL | SWT.BORDER);
		jarListViewer.getTable().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		jarListViewer.setContentProvider(new ArrayContentProvider());

		jarListViewer.setContentProvider(new ArrayContentProvider());
		jarListViewer.setLabelProvider(new WorkbenchLabelProvider());
		jarInput = new ArrayList<Object>();

		String[] string = CessarPluginActivator.getDefault().getDialogSettings().getArray(
			EXPORT_PLUGET_LASTNAME + plugetName + EXPORT_PLUGET_IMPORT_JARS);
		if (string != null)
		{
			for (String string2: string)
			{
				IFile file = thisProject.getFile(string2.replace(thisProject.getLocation().toOSString(), "")); //$NON-NLS-1$
				if (file != null && file.exists())
				{
					System.out.println(file);
				}
				jarInput.add(file);
			}
		}

		jarListViewer.setInput(jarInput);
		jarListViewer.addSelectionChangedListener(new ISelectionChangedListener()
		{
			@Override
			public void selectionChanged(SelectionChangedEvent event)
			{
				deleteSelection.setEnabled(true);
			}
		});
	}

	/**
	 * Actions to configure the rest
	 *
	 * @param parent
	 */
	private void createActions(Composite parent)
	{
		Composite composite = new Composite(parent, SWT.FILL);
		composite.setLayout(new GridLayout(1, false));
		composite.setLayoutData(new GridData(SWT.RIGHT, SWT.FILL, false, true));

		Button addInternalJar = new Button(composite, SWT.BUTTON1);
		addInternalJar.setText(Messages.EXPORT_PLUGET_WIZARD_INTERNALJAR);
		addInternalJar.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		handelInternalJars(addInternalJar);
		Button addExternalJar = new Button(composite, SWT.BUTTON1);
		addExternalJar.setText(Messages.EXPORT_PLUGET_WIZARD_EXTERNALJAR);
		addExternalJar.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		handelExternalJars(addExternalJar);

		deleteSelection = new Button(composite, SWT.BUTTON1);
		deleteSelection.setText(Messages.EXPORT_PLUGET_WIZARD_DELETE);
		deleteSelection.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		deleteSelection.setEnabled(false);
		handelDelete();
	}

	private void handelDelete()
	{
		deleteSelection.addSelectionListener(new SelectionListener()
		{

			@Override
			public void widgetSelected(SelectionEvent e)
			{
				click();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e)
			{
				click();
			}

			private void click()
			{
				IStructuredSelection selection = (IStructuredSelection) jarListViewer.getSelection();
				Object firstElement = selection.getFirstElement();
				jarInput.remove(firstElement);

				jarListViewer.refresh();
				deleteSelection.setEnabled(false);
			}
		});
	}

	private void handelExternalJars(Button addExternalJar)
	{
		addExternalJar.addSelectionListener(new SelectionListener()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				handleExternalJarsInternal();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e)
			{
				handleExternalJarsInternal();
			}

		});
	}

	/**
	 * Used by the inner class of the handelExternalJars for Checkstyle resons
	 */
	private void handleExternalJarsInternal()
	{
		String lastUsedPath = CessarPluginActivator.getDefault().getDialogSettings().get(EXPORT_PLUGET_IMPORT_JARS);
		if (lastUsedPath == null)
		{
			lastUsedPath = ""; //$NON-NLS-1$
		}
		FileDialog fDialog = new FileDialog(getShell(), SWT.MULTI);
		fDialog.setText(Messages.EXPORT_PLUGET_WIZARD_DIALOG);
		fDialog.setFilterExtensions(new String[] {JAR, ZIP});
		fDialog.setFilterPath(lastUsedPath);
		String res = fDialog.open();
		if (res == null)
		{
			return;
		}
		String[] fileNames = fDialog.getFileNames();
		int nChosen = fileNames.length;
		for (int i = 0; i < nChosen; i++)
		{
			externalResources.add(fDialog.getFilterPath() + FILE_SEPARATOR + fileNames[i]);
			IFile file = thisProject.getFile(fileNames[i]);
			jarInput.add(file);
		}
		CessarPluginActivator.getDefault().getDialogSettings().put(EXPORT_PLUGET_IMPORT_JARS, fDialog.getFilterPath());
		jarListViewer.refresh();
	}

	private void handelInternalJars(Button addInternalJar)
	{
		addInternalJar.addSelectionListener(new SelectionListener()
		{

			@Override
			public void widgetSelected(SelectionEvent e)
			{
				click();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e)
			{
				click();
			}

			private void click()
			{
				ElementTreeSelectionDialog elementTreeSelectionDialog = new ElementTreeSelectionDialog(getShell(),
					new WorkbenchLabelProvider(), new WorkbenchContentProvider());
				elementTreeSelectionDialog.setInput(thisProject.getWorkspace());
				if (elementTreeSelectionDialog.open() == Window.CANCEL)
				{
					return;
				}

				Object[] result = elementTreeSelectionDialog.getResult();
				for (Object object: result)
				{
					if (object instanceof IResource)
					{
						jarInput.add(object);
					}
				}
				jarListViewer.refresh();
			}
		});
	}

	/**
	 * @return the fDestinationNamesCombo
	 */
	public String getDestinationName()
	{
		return plugetDestination.getText();
	}

	/**
	 * @return Returns a list with all the source folders to be included
	 */
	public List<String> getSourceFolders()
	{
		List<String> result = new ArrayList<String>();
		TableItem[] items = srcListViewer.getTable().getItems();
		for (TableItem tableItem: items)
		{
			if (tableItem.getChecked())
			{
				Object data = tableItem.getData();
				IResource resource = ((IPackageFragmentRoot) data).getResource();
				result.add(resource.getLocation().toOSString());
			}
		}
		CessarPluginActivator.getDefault().getDialogSettings().put(
			EXPORT_PLUGET_LASTNAME + plugetName + EXPORT_PLUGET_SRC, result.toArray(new String[0]));
		return result;
	}

	/**
	 * @return Returns a list with all the jars to be included
	 */
	public List<String> getJars()
	{
		List<String> result = new ArrayList<String>();
		for (Object inJar: jarInput)
		{
			if (inJar instanceof IResource)
			{
				if (((IResource) inJar).exists())
				{
					result.add(((IResource) inJar).getLocation().toOSString());
				}
			}
		}
		result.addAll(externalResources);
		CessarPluginActivator.getDefault().getDialogSettings().put(
			EXPORT_PLUGET_LASTNAME + plugetName + EXPORT_PLUGET_IMPORT_JARS, result.toArray(new String[0]));
		return result;
	}

/*
 * (non-Javadoc)
 * 
 * @see org.eclipse.jface.wizard.WizardPage#isPageComplete()
 */
	@Override
	public boolean isPageComplete()
	{
		if (plugetDestination.getText().isEmpty())
		{
			return false;
		}
		if (plugetVersion.getText().isEmpty())
		{
			return false;
		}
		if (getSourceFolders().isEmpty())
		{
			return false;
		}
		return true;
	}

	/**
	 * @return version of the pluget
	 */
	public String getVersion()
	{
		String text = plugetVersion.getText();
		CessarPluginActivator.getDefault().getDialogSettings().put(
			EXPORT_PLUGET_LASTNAME + plugetName + EXPORT_PLUGET_VERSION, text);
		return text;
	}
}
