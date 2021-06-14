package eu.cessar.ct.ecuc.workspace.ui.internal.wizards.pages.def;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import eu.cessar.ct.core.mms.CompatPreferenceAccessor;
import eu.cessar.ct.core.mms.FileExtensionPreferenceAccessor;
import eu.cessar.ct.core.platform.CESSARPreferencesAccessor;
import eu.cessar.ct.core.platform.ECompatibilityMode;
import eu.cessar.ct.ecuc.workspace.jobs.ModuleDefinitionType;
import eu.cessar.ct.ecuc.workspace.ui.internal.wizards.pages.AbstractWizardPage;
import eu.cessar.ct.ecuc.workspace.ui.internal.wizards.pages.common.ProjectSelectionPage;
import eu.cessar.ct.runtime.ecuc.IEcucCore;
import eu.cessar.ct.runtime.ecuc.IEcucModel;

// TODO: Auto-generated Javadoc
/**
 * The Class ModuleDefinitionTypePage.
 */
public class ModuleDefinitionTypePage extends AbstractWizardPage
{

	/** The btn import standard. */
	private Button btnImportStandard;

	/** The btn refine module. */
	private Button btnRefineModule;

	/** The btn create pure. */
	private Button btnCreatePure;

	/** The btn import external standard. */
	private Button btnImportExternalStandard;

	/** The label location. */
	Label labelLocation;

	/** The txt location. */
	private Text txtLocation;

	/** The btn browse. */
	private Button btnBrowse;

	/** The external standard file import location. */
	private String externalStandardImportFilePath = null;

	/**
	 * Constructor for ModuleConfigurationFileSelectionPage.
	 * 
	 */
	public ModuleDefinitionTypePage()
	{
		super("ModuleDefinitionWizardProjectPage"); //$NON-NLS-1$
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
		container.setLayout(super.getGridLayout(1, false));
		setControl(container);

		new Label(container, SWT.NONE);
		btnImportStandard = new Button(container, SWT.RADIO);
		GridData gridRadio1 = new GridData();
		gridRadio1.horizontalIndent = 10;
		btnImportStandard.setLayoutData(gridRadio1);
		btnImportStandard.setData(AbstractWizardPage.CONTROL_ID, "btnImportStandard"); //$NON-NLS-1$
		btnImportStandard.setEnabled(true);
		btnImportStandard.setSelection(true);
		btnImportStandard.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				setPageComplete(isPageComplete());
				getContainer().updateMessage();
				updateFileBrowseControls(btnImportExternalStandard.getSelection());
			}
		});

		new Label(container, SWT.NONE);
		btnRefineModule = new Button(container, SWT.RADIO);
		GridData gridRadio2 = new GridData();
		gridRadio2.horizontalIndent = 10;
		btnRefineModule.setLayoutData(gridRadio2);
		btnRefineModule.setData(AbstractWizardPage.CONTROL_ID, "btnRefineModule"); //$NON-NLS-1$
		btnRefineModule.setEnabled(true);

		btnRefineModule.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				setPageComplete(isPageComplete());
				getContainer().updateMessage();
				updateFileBrowseControls(btnImportExternalStandard.getSelection());
			}
		});

		new Label(container, SWT.NONE);
		btnCreatePure = new Button(container, SWT.RADIO);
		GridData gridRadio3 = new GridData();
		gridRadio3.horizontalIndent = 10;
		btnCreatePure.setLayoutData(gridRadio3);
		btnCreatePure.setData(AbstractWizardPage.CONTROL_ID, "btnCreatePure"); //$NON-NLS-1$
		btnCreatePure.setEnabled(true);
		btnCreatePure.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				setPageComplete(isPageComplete());
				getContainer().updateMessage();
				updateFileBrowseControls(btnImportExternalStandard.getSelection());
			}
		});

		new Label(container, SWT.NONE);
		btnImportExternalStandard = new Button(container, SWT.RADIO);
		GridData gridRadio4 = new GridData();
		gridRadio4.horizontalIndent = 10;
		btnImportExternalStandard.setLayoutData(gridRadio4);
		btnImportExternalStandard.setData(AbstractWizardPage.CONTROL_ID, "btnImportExternalStandard"); //$NON-NLS-1$
		btnImportExternalStandard.setEnabled(true);
		btnImportExternalStandard.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				setPageComplete(isPageComplete());
				getContainer().updateMessage();
				updateFileBrowseControls(btnImportExternalStandard.getSelection());
			}
		});

		labelLocation = new Label(container, SWT.NONE);
		GridData gridLabelLocation = new GridData();
		gridLabelLocation.horizontalIndent = 25;
		gridLabelLocation.verticalIndent = 20;
		labelLocation.setLayoutData(gridLabelLocation);
		labelLocation.setData(AbstractWizardPage.CONTROL_ID, "txtLocation"); //$NON-NLS-1$

		txtLocation = new Text(container, SWT.BORDER);
		GridData gridTextLocation = new GridData(SWT.FILL, SWT.CENTER, true, false);
		gridTextLocation.horizontalIndent = 25;
		txtLocation.setLayoutData(gridTextLocation);
		txtLocation.setEnabled(true);
		txtLocation.setEditable(false);
		txtLocation.addModifyListener(new ModifyListener()
		{
			@Override
			public void modifyText(final ModifyEvent e)
			{
				checkNextButtonStatus();
			}
		});

		btnBrowse = new Button(container, SWT.PUSH);
		GridData gridBrowseButton = new GridData(SWT.RIGHT, SWT.CENTER, true, false);
		gridBrowseButton.horizontalIndent = 25;
		btnBrowse.setLayoutData(gridBrowseButton);
		btnBrowse.setData(AbstractWizardPage.CONTROL_ID, "btnBrowse"); //$NON-NLS-1$
		btnBrowse.setEnabled(true);
		btnBrowse.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent event)
			{
				FileDialog openFileDialog = new FileDialog(btnBrowse.getShell(), SWT.SINGLE);
				ProjectSelectionPage page = (ProjectSelectionPage) getPreviousPage();
				IProject project = page.getSelectedProject();

				List<String> dialogFilterNames = new ArrayList<String>();

				String[] dialogFilterExtensions = FileExtensionPreferenceAccessor.getExtensions(project);

				for (int i = 0; i < dialogFilterExtensions.length; i++)
				{
					dialogFilterExtensions[i] = "*." + dialogFilterExtensions[i]; //$NON-NLS-1$ 
					dialogFilterNames.add(dialogFilterExtensions[i]);
				}
				openFileDialog.setFilterNames(dialogFilterNames.toArray(new String[dialogFilterNames.size()]));
				openFileDialog.setFilterExtensions(dialogFilterExtensions);
				openFileDialog.setFilterIndex(0);

				String externalFilePath = openFileDialog.open();

				if (externalFilePath != null)
				{
					setExternalStandardImportFilePath(externalFilePath);
					txtLocation.setText(externalFilePath);
					setExternalStandardImportFilePath(externalFilePath);
					checkNextButtonStatus();
				}
			}
		});

		updateFileBrowseControls(btnImportExternalStandard.getSelection());
	}

	/**
	 * Gets the module definition type.
	 * 
	 * @return the module definition type
	 */
	public ModuleDefinitionType getModuleDefinitionType()
	{
		if (btnImportStandard.getSelection())
		{
			return ModuleDefinitionType.STANDARD;
		}
		if (btnRefineModule.getSelection())
		{
			return ModuleDefinitionType.REFINED;
		}
		if (btnImportExternalStandard.getSelection())
		{
			return ModuleDefinitionType.EXTERNAL_STANDARD;
		}
		return ModuleDefinitionType.PURE_VENDOR;
	}

	private void checkNextButtonStatus()
	{
		canFlipToNextPage();
		getWizard().getContainer().updateButtons();
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
			setNextPageDefault(this);
		}
		btnRefineModule.setEnabled(supportdRefinementConcept());
		super.setVisible(visible);
	}

	@Override
	public boolean canFlipToNextPage()
	{
		if (btnImportExternalStandard.getSelection())
		{
			return (externalStandardImportFilePath != null) && (!externalStandardImportFilePath.isEmpty());
		}
		else
		{
			return true;
		}

	}

	/**
	 * Supportd refinement concept.
	 * 
	 * @return true, if successful
	 */
	private boolean supportdRefinementConcept()
	{
		IWizard iWizard = getWizard();
		ProjectSelectionPage page = (ProjectSelectionPage) getPreviousPage();

		// check if there are module definitions to be refined
		if (page != null && page.getSelectedProject() != null)
		{
			IEcucModel ecucModel = IEcucCore.INSTANCE.getEcucModel(page.getSelectedProject());
			if (ecucModel.getAllModuleDefs().size() == 0)
			{
				return false;
			}
		}

		if (iWizard.getPreviousPage(this) instanceof ProjectSelectionPage)
		{
			IProject project = page.getSelectedProject();
			if (CESSARPreferencesAccessor.getCompatibilityMode(project).equals(ECompatibilityMode.NONE))
			{
				return true;
			}
			else
			{
				// We are in compatibility mode and now we need to look at the
				// useRefinementConcept preference
				return CompatPreferenceAccessor.isUseRefinement(project);
			}

		}
		return false;
	}

	/**
	 * Update buttons.
	 * 
	 * @param status
	 *        the status
	 */
	private void updateFileBrowseControls(boolean status)
	{
		labelLocation.setEnabled(status);
		txtLocation.setEnabled(status);
		btnBrowse.setEnabled(status);
	}

	/**
	 * Gets the external standard import file path.
	 * 
	 * @return the externalStandardImportFile
	 */
	public String getExternalStandardImportFilePath()
	{
		return externalStandardImportFilePath;
	}

	/**
	 * Sets the external standard import file path.
	 * 
	 * @param externalStandardImportFilePath
	 *        the externalStandardImportFilePath to set
	 */
	public void setExternalStandardImportFilePath(String externalStandardImportFilePath)
	{
		this.externalStandardImportFilePath = externalStandardImportFilePath;
	}

}
