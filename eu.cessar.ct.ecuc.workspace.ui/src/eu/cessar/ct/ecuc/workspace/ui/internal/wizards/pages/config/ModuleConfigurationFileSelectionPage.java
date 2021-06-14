package eu.cessar.ct.ecuc.workspace.ui.internal.wizards.pages.config;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;

import eu.cessar.ct.core.mms.MetaModelUtils;
import eu.cessar.ct.ecuc.workspace.ui.internal.Messages;
import eu.cessar.ct.ecuc.workspace.ui.internal.wizards.AbstractWizard;
import eu.cessar.ct.ecuc.workspace.ui.internal.wizards.pages.AbstractWizardPage;
import eu.cessar.ct.ecuc.workspace.ui.internal.wizards.pages.common.ResourceSelectionDialog;

/**
 * This wizard page allows setting the container for the new file as well as the
 * file name. Both the package and file name have predefined default values.
 */

public class ModuleConfigurationFileSelectionPage extends AbstractWizardPage
{
	protected static String FILENAME_EXTENSION_SEPARATOR = "."; //$NON-NLS-1$

	protected Text txtLocation;
	protected String selectedLocation = null;
	protected ResourceSelectionDialog fileSelectionDialog;

	protected Button btnCreateMandatoryElements;
	protected Button btnCreateOptionalElements;
	protected Button btnCreateBasedOnUpperMultiplicity;
	protected Spinner spinMaximumElementsCount;
	protected Button btnReplaceFile;
	protected Button btnOverwrite;
	protected Button btnAppend;

	private Composite cUpperMultiplicity;

	protected Label labelFileExtension;

	protected IProject project;
	protected String moduleName;
	protected String destinationPackageQName = null;

	protected IFile selectedFile;

	/**
	 * Constructor for ModuleConfigurationFileSelectionPage.
	 * 
	 * @param currentSelection
	 * 
	 * @param selection
	 */
	public ModuleConfigurationFileSelectionPage(IStructuredSelection selection)
	{
		super("ModuleFileSelectionPage", selection); //$NON-NLS-1$
	}

	/**
	 * @see AbstractWizardPage#createControl(Composite)
	 * @see AbstractWizardPage#doCreateControl(Composite)
	 */
	@Override
	protected void doCreateControl(Composite parent)
	{
		Composite container = new Composite(parent, SWT.NULL);
		// container.setLayout(super.getGridLayout(1, false));

		container.setLayout(new GridLayout(1, false));

		setControl(container);

		Label labelLocation = new Label(container, SWT.NONE);
		labelLocation.setLayoutData(new GridData());
		labelLocation.setData(AbstractWizardPage.CONTROL_ID, "labelLocation"); //$NON-NLS-1$

		txtLocation = new Text(container, SWT.BORDER);
		txtLocation.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		txtLocation.addModifyListener(new ModifyListener()
		{
			public void modifyText(ModifyEvent e)
			{
				selectedLocation = txtLocation.getText();
				IPath outputPath = getProject().getFullPath().append(selectedLocation);
				selectedFile = ResourcesPlugin.getWorkspace().getRoot().getFile(outputPath);

				updateButtons();
				setPageComplete(isPageComplete());
				getContainer().updateMessage();
			}
		});

		final Button browseButton = new Button(container, SWT.NONE);

		browseButton.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false));
		browseButton.setData(AbstractWizardPage.CONTROL_ID, "browseButton"); //$NON-NLS-1$
		browseButton.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(final SelectionEvent e)
			{
				if (fileSelectionDialog == null)
				{
					// The browse button should not look at the selection, but
					// always display the whole project structure.
					fileSelectionDialog = new ResourceSelectionDialog(browseButton.getShell(), getProject(),
						new StructuredSelection(project), true);
				}
				if (fileSelectionDialog.open() == Window.OK)
				{
					selectedFile = fileSelectionDialog.getSelectedFile();
					selectedLocation = MetaModelUtils.QNAME_SEPARATOR_STR
						+ selectedFile.getProjectRelativePath().toString();
					txtLocation.setText(selectedLocation);

					updateButtons();
					setPageComplete(isPageComplete());
					getContainer().updateMessage();
				}

			}
		});

		btnCreateMandatoryElements = new Button(container, SWT.CHECK);
		btnCreateMandatoryElements.setLayoutData(new GridData());
		btnCreateMandatoryElements.setData(AbstractWizardPage.CONTROL_ID, "btnCreateMandatoryElements"); //$NON-NLS-1$
		btnCreateMandatoryElements.setSelection(true);
		btnCreateMandatoryElements.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				boolean showOptions = btnCreateMandatoryElements.getSelection();
				btnCreateOptionalElements.setEnabled(showOptions);
				btnCreateOptionalElements.setSelection(false);
				btnCreateBasedOnUpperMultiplicity.setEnabled(showOptions);
				btnCreateBasedOnUpperMultiplicity.setSelection(false);
				spinMaximumElementsCount.setEnabled(false);
				spinMaximumElementsCount.setSelection(8);

				setPageComplete(isPageComplete());
				getContainer().updateMessage();
			}
		});

		btnCreateOptionalElements = new Button(container, SWT.CHECK);
		GridData gridCheck = new GridData();
		gridCheck.horizontalIndent = 10;
		btnCreateOptionalElements.setLayoutData(gridCheck);
		btnCreateOptionalElements.setData(AbstractWizardPage.CONTROL_ID, "btnCreateOptionalElements"); //$NON-NLS-1$
		btnCreateOptionalElements.setEnabled(true);
		btnCreateOptionalElements.setSelection(false);

		cUpperMultiplicity = new Composite(container, SWT.FILL);
		GridLayout gridLayout = new GridLayout(3, false);
		cUpperMultiplicity.setLayout(gridLayout);
		GridData gridData = new GridData(SWT.LEFT, SWT.CENTER, true, false);

		cUpperMultiplicity.setLayoutData(gridData);

		btnCreateBasedOnUpperMultiplicity = new Button(cUpperMultiplicity, SWT.CHECK);
		GridData gridCheck2 = new GridData(SWT.LEFT, SWT.CENTER, false, false);
		gridCheck2.horizontalIndent = 5;
		btnCreateBasedOnUpperMultiplicity.setLayoutData(gridCheck2);
		btnCreateBasedOnUpperMultiplicity.setData(AbstractWizardPage.CONTROL_ID, "btnUseUpperMultiplicity"); //$NON-NLS-1$
		btnCreateBasedOnUpperMultiplicity.setEnabled(true);
		btnCreateBasedOnUpperMultiplicity.setSelection(false);
		btnCreateBasedOnUpperMultiplicity.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				boolean showOptions = btnCreateBasedOnUpperMultiplicity.getSelection();
				spinMaximumElementsCount.setEnabled(showOptions);
				if (!showOptions)
				{
					spinMaximumElementsCount.setSelection(8);
				}

				setPageComplete(isPageComplete());
				getContainer().updateMessage();
			}
		});

		spinMaximumElementsCount = new Spinner(cUpperMultiplicity, SWT.BORDER | SWT.WRAP);
		GridData spinData = new GridData(SWT.CENTER, SWT.CENTER, false, false);
		spinData.widthHint = 20;
		spinMaximumElementsCount.setLayoutData(spinData);
		spinMaximumElementsCount.setData(AbstractWizardPage.CONTROL_ID, "spinUpperMultiplicityCount"); //$NON-NLS-1$
		spinMaximumElementsCount.setEnabled(false);
		spinMaximumElementsCount.setMinimum(1);
		spinMaximumElementsCount.setMaximum(255);
		spinMaximumElementsCount.setIncrement(1);
		spinMaximumElementsCount.setSelection(8);

		Label labelElements = new Label(cUpperMultiplicity, SWT.NONE);
		labelElements.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false));
		labelElements.setData(AbstractWizardPage.CONTROL_ID, "labelElements"); //$NON-NLS-1$

		btnReplaceFile = new Button(container, SWT.CHECK);
		GridData fileControls = new GridData();
		fileControls.verticalIndent = 10;
		btnReplaceFile.setLayoutData(fileControls);
		btnReplaceFile.setData(AbstractWizardPage.CONTROL_ID, "btnReplaceFile"); //$NON-NLS-1$
		btnReplaceFile.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				if (btnReplaceFile.getSelection() == true)
				{
					btnOverwrite.setEnabled(true);
					btnAppend.setEnabled(true);
				}
				else
				{
					btnOverwrite.setEnabled(false);
					btnAppend.setEnabled(false);
				}
				updateButtons();
				setPageComplete(isPageComplete());
				getContainer().updateMessage();
			}
		});

		btnOverwrite = new Button(container, SWT.RADIO);
		GridData gridRadio1 = new GridData();
		gridRadio1.horizontalIndent = 10;
		btnOverwrite.setLayoutData(gridRadio1);
		btnOverwrite.setData(AbstractWizardPage.CONTROL_ID, "btnOverwrite"); //$NON-NLS-1$
		btnOverwrite.setEnabled(false);

		btnAppend = new Button(container, SWT.RADIO);
		GridData gridRadio2 = new GridData();
		gridRadio2.horizontalIndent = 10;
		btnAppend.setLayoutData(gridRadio2);
		btnAppend.setData(AbstractWizardPage.CONTROL_ID, "btnAppend"); //$NON-NLS-1$
		btnAppend.setEnabled(false);

		labelFileExtension = new Label(container, SWT.NONE);
		labelFileExtension.setLayoutData(new GridData());
		labelFileExtension.setData(AbstractWizardPage.CONTROL_ID, "labelFileExtension"); //$NON-NLS-1$
		labelFileExtension.setVisible(false);

	}

	@Override
	public boolean isPageComplete()
	{
		IFile file = getSelectedFile();
		if (null == file)
		{
			return false;
		}
		if (file.exists() && false == btnReplaceFile.getSelection())
		{
			return false;
		}
		return true;
	}

	@Override
	public String getErrorMessage()
	{
		IFile file = getSelectedFile();
		if (null == file)
		{
			return Messages.DefinitionFileSelectionPage_Err_No_FileSelected;
		}
		if (file.exists() && false == btnReplaceFile.getSelection())
		{
			return Messages.DefinitionFileSelectionPage_Err_Existing_File;
		}
		return super.getErrorMessage();
	}

	@Override
	public void setVisible(final boolean visible)
	{
		if (visible)
		{
			AbstractWizard wizard = (AbstractWizard) getWizard();

			// set the size of the shell
			getShell().setSize(cUpperMultiplicity.getSize().x + 65, getShell().getSize().y);

			setProject(wizard.getProject());
			setModuleName(wizard.getDestinationModuleName());
			setDestinationPackageQName(wizard.getDestinationPackageName());
			String baseLocation = getDestinationPackageQName();

			IStructuredSelection selection = getSelection();
			if (null != selection && !selection.isEmpty())
			{
				Object selected = selection.getFirstElement();
				if (selected instanceof IFolder)
				{
					baseLocation = MetaModelUtils.QNAME_SEPARATOR_STR
						+ ((IFolder) selected).getProjectRelativePath().toString();
				}
				if (selected instanceof IFile)
				{
					baseLocation = MetaModelUtils.QNAME_SEPARATOR_STR
						+ ((IFile) selected).getParent().getProjectRelativePath().toString();
				}
			}

			selectedLocation = baseLocation;
			if (!selectedLocation.equals(MetaModelUtils.QNAME_SEPARATOR_STR))
			{
				selectedLocation += MetaModelUtils.QNAME_SEPARATOR_STR;
			}
			selectedLocation += getModuleName() + FILENAME_EXTENSION_SEPARATOR + wizard.getEcucExtension() /*
																											 * labelFileExtension
																											 */;

			txtLocation.setText(selectedLocation);
			btnCreateMandatoryElements.setSelection(true);
			updateButtons();
		}
		super.setVisible(visible);
	}

	public void setProject(IProject project)
	{
		this.project = project;
	}

	public IProject getProject()
	{
		return project;
	}

	public String getModuleName()
	{
		return moduleName;
	}

	public void setModuleName(String moduleName)
	{
		this.moduleName = moduleName;
	}

	public String getDestinationPackageQName()
	{
		return destinationPackageQName;
	}

	public void setDestinationPackageQName(String destinationPackageQName)
	{
		this.destinationPackageQName = destinationPackageQName;
	}

	public String getFileLocation()
	{
		return selectedLocation;
	}

	public IFile getSelectedFile()
	{
		return selectedFile;
	}

	public void resetSelectedFile()
	{
		selectedFile = null;
	}

	public boolean getCreateMandatory()
	{
		return btnCreateMandatoryElements.getSelection();
	}

	public boolean getCreateOptional()
	{
		return btnCreateOptionalElements.getSelection();
	}

	public boolean getCreateBasedOnUpperMultiplicity()
	{
		return btnCreateBasedOnUpperMultiplicity.getSelection();
	}

	public int getMaximumElementsCount()
	{
		return spinMaximumElementsCount.getSelection();
	}

	public boolean replaceFile()
	{
		return (btnReplaceFile.isEnabled() ? btnOverwrite.getSelection() : false);
	}

	public boolean appendToFile()
	{
		return (btnReplaceFile.isEnabled() ? btnAppend.getSelection() : false);
	}

	private void updateButtons()
	{
		if (null != selectedFile && selectedFile.exists())
		{
			btnReplaceFile.setSelection(true);
			btnReplaceFile.setEnabled(true);
			btnOverwrite.setEnabled(true);
			btnAppend.setEnabled(true);
		}
		else
		{
			btnReplaceFile.setSelection(false);
			btnReplaceFile.setEnabled(false);
			btnOverwrite.setEnabled(false);
			btnAppend.setEnabled(false);
		}
		btnOverwrite.setSelection(false);
		btnAppend.setSelection(true);
	}
}
