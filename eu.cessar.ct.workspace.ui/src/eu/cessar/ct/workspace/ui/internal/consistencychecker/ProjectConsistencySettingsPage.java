/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidw8762 Mon Feb 24 15:21:19 2014 </copyright>
 */
package eu.cessar.ct.workspace.ui.internal.consistencychecker;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;

import eu.cessar.ct.workspace.ui.internal.Messages;
import eu.cessar.ct.workspace.ui.internal.WorkspaceUIConstants;
import eu.cessar.ct.workspace.ui.internal.preferences.CessarProjectConsistencyPrefAndPropPage;
import eu.cessar.req.Requirement;

/**
 *
 * ProjectConsistencySettingsPage class is the implementation for the page where the user makes a selection for which
 * consistency checks to perform on the selected project.
 *
 * @author uidw8762
 *
 *         %created_by: uidu8153 %
 *
 *         %date_created: Tue Jun 23 10:49:56 2015 %
 *
 *         %version: RAUTOSAR~13 %
 *
 */
// CHECKSTYLE:OFF
@Requirement(
	reqID = "REQ_CHECK#5")
public class ProjectConsistencySettingsPage extends WizardPage
// CHECKSTYLE:ON
{
	/** The next pressed flag indicator. */
	private boolean nextPressed;

	/** The check for duplicate module definitions flag indicator. */
	private boolean checkDuplicateModuleDefinitions;

	/** The check for jet verification.(old jar for jet, not compiled jet) */
	private boolean checkJetVerification;

	/** The check for wrong meta-model files flag indicator. */
	private boolean checkWrongMetamodelFiles;

	/** The check for ProjectVerification flag indicator. */
	private boolean checkForProjectVerification;

	/** The check for ClassPathVerification flag indicator. */
	private boolean checkForClassPathVerification;

	/** The check for SettingsVerification flag indicator. */
	private boolean checkForSettingsVerification;

	/** The check for PmbinFolderVerification flag indicator. */
	private boolean checkForPmbinFolderVerification;

	/** The label for detailed description of the duplicate module definition option. */
	private Label labelDupModDefDetail;

	/** The label for detailed description of the check for wrong meta-model files. */
	private Label labelWrongMetamodelDetail;

	/** The label for detailed description of the check for jet verification. */
	private Label labelJetVerificationDetail;

	/** The label for detailed description of the check for Project verification. */
	private Label labelProjectVerification;

	/** The label for detailed description of the check for ClassPath verification. */
	private Label labelClassPathVerification;

	/** The label for detailed description of the check for Settings verification. */
	private Label labelSettingsVerification;

	/** The label for detailed description of the check for pmbin folder verification. */
	private Label labelPmbinFolderVerification;

	/**
	 * Instantiates a new project consistency settings page.
	 *
	 * @param pageName
	 *        the page name
	 * @param project
	 *        the project
	 */
	public ProjectConsistencySettingsPage(String pageName, IProject project)
	{
		super(pageName);
		setTitle(pageName);
		setMessage(Messages.ProjectConsistencySettingsPage_message);

		CessarProjectConsistencyPrefAndPropPage propPage = new CessarProjectConsistencyPrefAndPropPage(project);
		propPage.getProjectConsistencySettings();

		checkDuplicateModuleDefinitions = propPage.isCheckDuplicateModuleDefinitions();
		checkWrongMetamodelFiles = propPage.isCheckForWrongMetamodelFiles();
		checkJetVerification = propPage.isCheckForJetVerification();
		checkForProjectVerification = propPage.isCheckForProjectVerification();
		checkForClassPathVerification = propPage.isCheckForClasspathVerification();
		checkForSettingsVerification = propPage.isCheckForSettingsVerification();
		checkForPmbinFolderVerification = propPage.isCheckForPmbinFolderVerification();

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jface.wizard.WizardPage#getNextPage()
	 */
	@Override
	public IWizardPage getNextPage()
	{
		nextPressed = true;
		return super.getNextPage();
	}

	/**
	 * Next pressed.
	 *
	 * @return true, if successful
	 */
	public boolean nextPressed()
	{
		return nextPressed;
	}

	/**
	 * Creates the check for wrong metamodel/* (non-Javadoc).
	 *
	 * @param parent
	 *        the parent
	 * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void createControl(final Composite parent)
	{
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new GridLayout(1, false));
		container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		Group groupCheckDupModDef = new Group(container, SWT.NONE);
		groupCheckDupModDef.setLayout(new GridLayout(1, false));
		groupCheckDupModDef.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false));
		groupCheckDupModDef.setData(WorkspaceUIConstants.CONTROL_ID, "groupCheckDupModDef"); //$NON-NLS-1$
		createCheckDuplicateModuleDefinitionsControls(container, groupCheckDupModDef);

		Group groupCheckWrongMetamodel = new Group(container, SWT.NONE);
		groupCheckWrongMetamodel.setLayout(new GridLayout(1, false));
		groupCheckWrongMetamodel.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
		groupCheckWrongMetamodel.setData(WorkspaceUIConstants.CONTROL_ID, "groupCheckWrongMetamodel"); //$NON-NLS-1$
		createCheckForWrongMetamodelFilesControls(container, groupCheckWrongMetamodel);

		Group groupCheckJetVerification = new Group(container, SWT.NONE);
		groupCheckJetVerification.setLayout(new GridLayout(1, false));
		groupCheckJetVerification.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
		groupCheckJetVerification.setData(WorkspaceUIConstants.CONTROL_ID, "groupCheckJetVerification"); //$NON-NLS-1$
		createCheckForJetVerificationControls(container, groupCheckJetVerification);

		/** project Verification */
		Group groupCheckProjectVerification = new Group(container, SWT.NONE);
		groupCheckProjectVerification.setLayout(new GridLayout(1, false));
		groupCheckProjectVerification.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
		groupCheckProjectVerification.setData(WorkspaceUIConstants.CONTROL_ID, "groupCheckProjectVerification"); //$NON-NLS-1$

		createCheckForProjectVerificationControls(container, groupCheckProjectVerification);

		/** Classpath Verification */
		Group groupCheckClassPathVerification = new Group(container, SWT.NONE);
		groupCheckClassPathVerification.setLayout(new GridLayout(1, false));
		groupCheckClassPathVerification.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
		groupCheckClassPathVerification.setData(WorkspaceUIConstants.CONTROL_ID, "groupCheckClassPathVerification"); //$NON-NLS-1$

		createCheckForClasspathVerificationControls(container, groupCheckClassPathVerification);

		/** settings Verification */
		Group groupCheckSettingsVerification = new Group(container, SWT.NONE);
		groupCheckSettingsVerification.setLayout(new GridLayout(1, false));
		groupCheckSettingsVerification.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
		groupCheckSettingsVerification.setData(WorkspaceUIConstants.CONTROL_ID, "groupCheckSettingsVerification"); //$NON-NLS-1$

		createCheckForSettingsVerificationControls(container, groupCheckSettingsVerification);

		/** pmbin folder Verification */
		Group groupCheckPmbinFoldeVerification = new Group(container, SWT.NONE);
		groupCheckPmbinFoldeVerification.setLayout(new GridLayout(1, false));
		groupCheckPmbinFoldeVerification.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
		groupCheckPmbinFoldeVerification.setData(WorkspaceUIConstants.CONTROL_ID, "groupCheckPmbinFoldeVerification"); //$NON-NLS-1$

		createCheckForPmbinFolderVerificationControls(container, groupCheckPmbinFoldeVerification);

		setControl(container);
		setPageComplete(true);
	}

	/**
	 * Creates the check duplicate module definitions controls.
	 *
	 * @param composite
	 *        the composite
	 * @param groupCheckDupModDef
	 *        the controls group
	 */
	private void createCheckDuplicateModuleDefinitionsControls(Composite composite, Group groupCheckDupModDef)
	{
		final Button btnCheckDuplicateModuleDefinitions = new Button(groupCheckDupModDef, SWT.CHECK);

		btnCheckDuplicateModuleDefinitions.setText(Messages.ProjectConsistencySettingsPage_check_for_duplicate_module_definitions);
		FontData fontData = btnCheckDuplicateModuleDefinitions.getFont().getFontData()[0];
		Font font = new Font(composite.getDisplay(), new FontData(fontData.getName(), fontData.getHeight(), SWT.BOLD));
		btnCheckDuplicateModuleDefinitions.setFont(font);
		btnCheckDuplicateModuleDefinitions.setSelection(checkDuplicateModuleDefinitions);
		btnCheckDuplicateModuleDefinitions.setData(WorkspaceUIConstants.CONTROL_ID,
			"btnCheckDuplicateModuleDefinitions"); //$NON-NLS-1$
		btnCheckDuplicateModuleDefinitions.addSelectionListener(new SelectionAdapter()
		{
			/*
			 * (non-Javadoc)
			 *
			 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
			 */
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				checkDuplicateModuleDefinitions = btnCheckDuplicateModuleDefinitions.getSelection();
				checkNextButtonStatus();
			}
		});

		labelDupModDefDetail = new Label(groupCheckDupModDef, SWT.NONE);
		labelDupModDefDetail.setText(Messages.ProjectConsistencySettingsPage_check_for_duplicate_module_definitions_description);
		labelDupModDefDetail.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
	}

	/**
	 * Creates the check jetVerification controls.
	 *
	 * @param composite
	 *        the composite
	 * @param groupCheckJetVerification
	 *        the controls group
	 */
	private void createCheckForJetVerificationControls(Composite composite, Group groupCheckJetVerification)
	{
		final Button btnCheckJetVerification = new Button(groupCheckJetVerification, SWT.CHECK);

		btnCheckJetVerification.setText(Messages.ProjectConsistencySettingsPage_check_for_jet_verification);
		FontData fontData = btnCheckJetVerification.getFont().getFontData()[0];
		Font font = new Font(composite.getDisplay(), new FontData(fontData.getName(), fontData.getHeight(), SWT.BOLD));
		btnCheckJetVerification.setFont(font);
		btnCheckJetVerification.setSelection(checkJetVerification);
		btnCheckJetVerification.setData(WorkspaceUIConstants.CONTROL_ID, "btnCheckJetVerification"); //$NON-NLS-1$
		btnCheckJetVerification.addSelectionListener(new SelectionAdapter()
		{
			/*
			 * (non-Javadoc)
			 *
			 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
			 */
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				checkJetVerification = btnCheckJetVerification.getSelection();
				checkNextButtonStatus();
			}
		});

		labelJetVerificationDetail = new Label(groupCheckJetVerification, SWT.NONE);
		labelJetVerificationDetail.setText(Messages.ProjectConsistencySettingsPage_check_for_jet_verification);
		labelJetVerificationDetail.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
	}

	/**
	 * Creates the check for wrong metamodel files controls.
	 *
	 * @param composite
	 *        the composite
	 * @param groupCheckWrongMetamodel
	 *        the group check wrong metamodel
	 */
	private void createCheckForWrongMetamodelFilesControls(Composite composite, Group groupCheckWrongMetamodel)
	{
		final Button btnCheckForWrongMetamodelFiles = new Button(groupCheckWrongMetamodel, SWT.CHECK);
		btnCheckForWrongMetamodelFiles.setText(Messages.ProjectConsistencySettingsPage_check_for_files_of_wrong_metamodel);

		FontData fontData = btnCheckForWrongMetamodelFiles.getFont().getFontData()[0];
		Font font = new Font(composite.getDisplay(), new FontData(fontData.getName(), fontData.getHeight(), SWT.BOLD));
		btnCheckForWrongMetamodelFiles.setFont(font);
		btnCheckForWrongMetamodelFiles.setSelection(checkWrongMetamodelFiles);
		btnCheckForWrongMetamodelFiles.setData(WorkspaceUIConstants.CONTROL_ID, "btnCheckForWrongMetamodelFiles"); //$NON-NLS-1$
		btnCheckForWrongMetamodelFiles.addSelectionListener(new SelectionAdapter()
		{
			/*
			 * (non-Javadoc)
			 *
			 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
			 */
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				checkWrongMetamodelFiles = btnCheckForWrongMetamodelFiles.getSelection();
				checkNextButtonStatus();
			}
		});

		labelWrongMetamodelDetail = new Label(groupCheckWrongMetamodel, SWT.NONE);
		labelWrongMetamodelDetail.setText(Messages.ProjectConsistencySettingsPage_check_for_files_of_wrong_metamodel_description);
		labelWrongMetamodelDetail.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
	}

	/**
	 * Creates the check for Project verification controls.
	 * 
	 * @param composite
	 *        the composite
	 * @param groupCheckProjectVerification
	 * 
	 */
	private void createCheckForProjectVerificationControls(Composite composite, Group groupCheckProjectVerification)
	{
		final Button btnCheckForProjectVerification = new Button(groupCheckProjectVerification, SWT.CHECK);
		btnCheckForProjectVerification.setText(Messages.ProjectConsistencySettingsPage_Check_For_Project_Verification);

		FontData fontData = btnCheckForProjectVerification.getFont().getFontData()[0];
		Font font = new Font(composite.getDisplay(), new FontData(fontData.getName(), fontData.getHeight(), SWT.BOLD));
		btnCheckForProjectVerification.setFont(font);
		btnCheckForProjectVerification.setSelection(checkForProjectVerification);
		btnCheckForProjectVerification.setData(WorkspaceUIConstants.CONTROL_ID, "btnCheckForProjectVerification"); //$NON-NLS-1$
		btnCheckForProjectVerification.addSelectionListener(new SelectionAdapter()
		{
			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
			 */
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				checkForProjectVerification = btnCheckForProjectVerification.getSelection();
				checkNextButtonStatus();
			}
		});

		labelProjectVerification = new Label(groupCheckProjectVerification, SWT.NONE);
		labelProjectVerification.setText(Messages.ProjectConsistencySettingsPage_Check_For_Project_Verification_Desc);
		labelProjectVerification.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
	}

	/**
	 * Creates the check for Classpath verification controls.
	 * 
	 * @param composite
	 *        the composite
	 * @param groupCheckClasspathtVerification
	 * 
	 */
	private void createCheckForClasspathVerificationControls(Composite composite, Group groupCheckClasspathVerification)
	{
		final Button btnCheckForClassspathVerification = new Button(groupCheckClasspathVerification, SWT.CHECK);
		btnCheckForClassspathVerification.setText(Messages.ProjectConsistencySettingsPage_Check_For_Classpath_Verification);

		FontData fontData = btnCheckForClassspathVerification.getFont().getFontData()[0];
		Font font = new Font(composite.getDisplay(), new FontData(fontData.getName(), fontData.getHeight(), SWT.BOLD));
		btnCheckForClassspathVerification.setFont(font);
		btnCheckForClassspathVerification.setSelection(checkForClassPathVerification);
		btnCheckForClassspathVerification.setData(WorkspaceUIConstants.CONTROL_ID, "btnCheckForClasspathVerification"); //$NON-NLS-1$
		btnCheckForClassspathVerification.addSelectionListener(new SelectionAdapter()
		{
			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
			 */
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				checkForClassPathVerification = btnCheckForClassspathVerification.getSelection();
				checkNextButtonStatus();
			}
		});

		labelClassPathVerification = new Label(groupCheckClasspathVerification, SWT.NONE);
		labelClassPathVerification.setText(Messages.ProjectConsistencySettingsPage_Check_For_Classpath_Verification_Desc);
		labelClassPathVerification.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
	}

	/**
	 * Creates the check for Settings verification controls.
	 * 
	 * @param composite
	 *        the composite
	 * @param groupCheckSettingsVerification
	 * 
	 */
	private void createCheckForSettingsVerificationControls(Composite composite, Group groupCheckSettingstVerification)
	{
		final Button btnCheckForSettingsVerification = new Button(groupCheckSettingstVerification, SWT.CHECK);
		btnCheckForSettingsVerification.setText(Messages.ProjectConsistencySettingsPage_Check_For_Settings_Verification);

		FontData fontData = btnCheckForSettingsVerification.getFont().getFontData()[0];
		Font font = new Font(composite.getDisplay(), new FontData(fontData.getName(), fontData.getHeight(), SWT.BOLD));
		btnCheckForSettingsVerification.setFont(font);
		btnCheckForSettingsVerification.setSelection(checkForSettingsVerification);
		btnCheckForSettingsVerification.setData(WorkspaceUIConstants.CONTROL_ID, "btnCheckForSettingsVerification"); //$NON-NLS-1$
		btnCheckForSettingsVerification.addSelectionListener(new SelectionAdapter()
		{
			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
			 */
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				checkForSettingsVerification = btnCheckForSettingsVerification.getSelection();
				checkNextButtonStatus();
			}
		});

		labelSettingsVerification = new Label(groupCheckSettingstVerification, SWT.NONE);
		labelSettingsVerification.setText(Messages.ProjectConsistencySettingsPage_Check_For_Settings_Verification_Desc);
		labelSettingsVerification.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
	}

	/**
	 * Creates the check for pmbin folder controls.
	 * 
	 * @param composite
	 *        the composite
	 * @param groupCheckPmbinFolderVerification
	 * 
	 */
	private void createCheckForPmbinFolderVerificationControls(Composite composite,
		Group groupCheckPmbinFolderVerification)
	{
		final Button btnCheckForPmbinVerification = new Button(groupCheckPmbinFolderVerification, SWT.CHECK);
		btnCheckForPmbinVerification.setText(Messages.ProjectConsistencySettingsPage_Check_For_pmbin_Verification);

		FontData fontData = btnCheckForPmbinVerification.getFont().getFontData()[0];
		Font font = new Font(composite.getDisplay(), new FontData(fontData.getName(), fontData.getHeight(), SWT.BOLD));
		btnCheckForPmbinVerification.setFont(font);
		btnCheckForPmbinVerification.setSelection(checkForPmbinFolderVerification);
		btnCheckForPmbinVerification.setData(WorkspaceUIConstants.CONTROL_ID, "btnCheckForPmbinFolderVerification"); //$NON-NLS-1$
		btnCheckForPmbinVerification.addSelectionListener(new SelectionAdapter()
		{
			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
			 */
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				checkForPmbinFolderVerification = btnCheckForPmbinVerification.getSelection();
				checkNextButtonStatus();
			}
		});

		labelPmbinFolderVerification = new Label(groupCheckPmbinFolderVerification, SWT.NONE);
		labelPmbinFolderVerification.setText(Messages.ProjectConsistencySettingsPage_Check_For_pmbin_Verification_Desc);
		labelPmbinFolderVerification.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jface.wizard.WizardPage#canFlipToNextPage()
	 */
	@Override
	public boolean canFlipToNextPage()
	{
		return checkDuplicateModuleDefinitions || checkWrongMetamodelFiles || checkJetVerification
			|| checkForProjectVerification || checkForClassPathVerification || checkForSettingsVerification
			|| checkForPmbinFolderVerification;
	}

	/**
	 * Check next button status.
	 *
	 * @return true, if successful
	 */
	public boolean checkNextButtonStatus()
	{
		boolean result = false;

		canFlipToNextPage();
		getWizard().getContainer().updateButtons();

		return result;
	}

	/**
	 * Checks if is check for duplicate mod /* (non-Javadoc).
	 *
	 * @return true, if is page complete
	 * @see org.eclipse.jface.wizard.WizardPage#isPageComplete()
	 */
	@Override
	public boolean isPageComplete()
	{
		return true;
	}

	/**
	 * Checks if is check for duplicate module definitions.
	 *
	 * @return true, if is check for duplicate module definitions
	 */
	public boolean isCheckForDuplicateModuleDefinitions()
	{
		return checkDuplicateModuleDefinitions;
	}

	/**
	 * Sets the check for duplicate module definitions.
	 *
	 * @param checkForDuplicateModuleDefinitions
	 *        the checkForDuplicateModuleDefinitions to set
	 */
	public void setCheckForDuplicateModuleDefinitions(boolean checkForDuplicateModuleDefinitions)
	{
		checkDuplicateModuleDefinitions = checkForDuplicateModuleDefinitions;
	}

	/**
	 * Checks if is check for wrong metamodel files.
	 *
	 * @return the checkForWrongMetamodelFiles
	 */
	public boolean isCheckForWrongMetamodelFiles()
	{
		return checkWrongMetamodelFiles;
	}

	/**
	 * Sets the check for wrong metamodel files.
	 *
	 * @param checkForWrongMetamodelFiles
	 *        the checkForWrongMetamodelFiles to set
	 */
	public void setCheckForWrongMetamodelFiles(boolean checkForWrongMetamodelFiles)
	{
		checkWrongMetamodelFiles = checkForWrongMetamodelFiles;
	}

	/**
	 * Checks if the check for jet verification is on.
	 *
	 * @return the checkForJetVerification
	 */
	public boolean isCheckForJetVerification()
	{
		return checkJetVerification;
	}

	/**
	 * Sets the check for jet verification
	 *
	 * @param checkForJetVerification
	 *        the checkForJetVerifications to set
	 */
	public void setCheckForJetVerification(boolean checkForJetVerification)
	{
		checkJetVerification = checkForJetVerification;
	}

	/**
	 * @return the checkForProjectVerification
	 */
	public boolean isCheckForProjectVerification()
	{
		return checkForProjectVerification;
	}

	/**
	 * @param checkForProjectVerification
	 *        the checkForProjectVerification to set
	 */
	public void setCheckForProjectVerification(boolean checkForProjectVerification)
	{
		this.checkForProjectVerification = checkForProjectVerification;
	}

	/**
	 * @return the checkForClassPathVerification
	 */
	public boolean isCheckForClassPathVerification()
	{
		return checkForClassPathVerification;
	}

	/**
	 * @param checkForClassPathVerification
	 *        the checkForClassPathVerification to set
	 */
	public void setCheckForClassPathVerification(boolean checkForClassPathVerification)
	{
		this.checkForClassPathVerification = checkForClassPathVerification;
	}

	/**
	 * @return the checkForSettingsVerification
	 */
	public boolean isCheckForSettingsVerification()
	{
		return checkForSettingsVerification;
	}

	/**
	 * @param checkForSettingsVerification
	 *        the checkForSettingsVerification to set
	 */
	public void setCheckForSettingsVerification(boolean checkForSettingsVerification)
	{
		this.checkForSettingsVerification = checkForSettingsVerification;
	}

	/**
	 * @return the checkForPmbinFolderVerification
	 */
	public boolean isCheckForPmbinFolderVerification()
	{
		return checkForPmbinFolderVerification;
	}

	/**
	 * @param checkForPmbinFolderVerification
	 *        the checkForPmbinFolderVerification to set
	 */
	public void setCheckForPmbinFolderVerification(boolean checkForPmbinFolderVerification)
	{
		this.checkForPmbinFolderVerification = checkForPmbinFolderVerification;
	}

}
