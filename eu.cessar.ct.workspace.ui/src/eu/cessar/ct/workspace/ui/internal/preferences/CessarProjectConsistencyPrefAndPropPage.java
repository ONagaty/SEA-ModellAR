/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidw8762<br/>
 * Feb 27, 2014 9:57:17 AM
 * 
 * </copyright>
 */
package eu.cessar.ct.workspace.ui.internal.preferences;

import org.eclipse.core.resources.IProject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.forms.events.ExpansionAdapter;
import org.eclipse.ui.forms.events.ExpansionEvent;
import org.eclipse.ui.forms.widgets.ExpandableComposite;

import eu.cessar.ct.core.platform.ui.prefandprop.AbstractPreferenceAndPropertyPage;
import eu.cessar.ct.core.platform.ui.util.CessarFormToolkit;
import eu.cessar.ct.workspace.consistencycheck.EProjectInconsistencyType;
import eu.cessar.ct.workspace.internal.preferences.ProjectConsistencyPreferencesAccessor;
import eu.cessar.ct.workspace.ui.internal.Messages;

/**
 * CessarProjectConsistencyPrefAndPropPage class.
 * 
 * @author uidw8762
 * 
 *         %created_by: uidu8153 %
 * 
 *         %date_created: Thu Jun 25 08:53:38 2015 %
 * 
 *         %version: RAUTOSAR~9 %
 */
public class CessarProjectConsistencyPrefAndPropPage extends AbstractPreferenceAndPropertyPage
{
	private static final String PROPERTY_PAGE_ID = "eu.cessar.ct.workspace.ui.CessarProjectConsistencyPropertyPage"; //$NON-NLS-1$
	private static final String PREFERENCE_PAGE_ID = "eu.cessar.ct.workspace.ui.CessarProjectConsistencyPreferencePage"; //$NON-NLS-1$

	/** The check button for duplicate module definitions. */
	private Button btnCheckDuplicateModuleDefinitions;

	/** The check button for wrong meta-model files. */
	private Button btnCheckForWrongMetamodelFiles;

	/** The check button for jetVerification. */
	private Button btnCheckForJetVerification;

	/** The check button for projectVerification. */
	private Button btnCheckForProjectVerification;

	/** The check button for classPathVerification. */
	private Button btnCheckForClassPathVerification;

	/** The check button for settingsVerification. */
	private Button btnCheckForSettingsVerification;

	/** The check button for presentation Model (pmbin)FolderVerification. */
	private Button btnCheckForPmbinFolderVerification;

	/** The check duplicate module definitions flag indicator. */
	private boolean checkDuplicateModuleDefinitions;

	/** The check for wrong metamodel files flag indicator. */
	private boolean checkForWrongMetamodelFiles;

	/** The check for JetVerification flag indicator. */
	private boolean checkForJetVerification;

	/** The check for ProjectVerification flag indicator. */
	private boolean checkForProjectVerification;

	/** The check for ClasspathVerification flag indicator. */
	private boolean checkForClasspathVerification;

	/** The check for SettingsVerification flag indicator. */
	private boolean checkForSettingsVerification;

	/** The check for PmbinFolderVerification flag indicator. */
	private boolean checkForPmbinFolderVerification;

	/** The parent composite of the property page. */
	private Composite parentComposite;

	private IProject project;

	/**
	 * Instantiates a new CESSAR project consistency preferences and properties page.
	 */
	public CessarProjectConsistencyPrefAndPropPage()
	{
		super();
	}

	/**
	 * Instantiates a new CESSAR project consistency preferences and properties page.
	 * 
	 * @param project
	 *        the project
	 */
	public CessarProjectConsistencyPrefAndPropPage(IProject project)
	{
		super();
		this.project = project;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.cessar.ct.core.platform.ui.prefandprop.AbstractPreferenceAndPropertyPage#createPreferenceContent(org.eclipse
	 * .swt.widgets.Composite)
	 */
	@Override
	protected Control createPreferenceContent(Composite composite)
	{
		Composite parent = new Composite(composite, SWT.FILL);
		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		parent.setLayout(layout);
		parent.setFont(parent.getFont());
		parentComposite = composite;
		getProjectConsistencySettings();
		setProjectConsistencyControls(parent, checkDuplicateModuleDefinitions, checkForWrongMetamodelFiles,
			checkForJetVerification, checkForProjectVerification, checkForClasspathVerification,
			checkForSettingsVerification, checkForPmbinFolderVerification);

		return parent;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.core.platform.ui.prefandprop.AbstractPreferenceAndPropertyPage#getPreferencePageID()
	 */
	@Override
	protected String getPreferencePageID()
	{
		return PREFERENCE_PAGE_ID;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.core.platform.ui.prefandprop.AbstractPreferenceAndPropertyPage#getPropertyPageID()
	 */
	@Override
	protected String getPropertyPageID()
	{
		return PROPERTY_PAGE_ID;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.core.platform.ui.prefandprop.AbstractPreferenceAndPropertyPage#performOk()
	 */
	@Override
	public boolean performOk()
	{
		if (getProject() != null)
		{
			ProjectConsistencyPreferencesAccessor.setProjectSpecific(getProject(), useProjectSettings());
		}
		setProjectConsistencyTypePreference(getProject());

		return true;
	}

	/**
	 * Gets the current project.
	 * 
	 * @return the current project
	 */
	public IProject getCurrentProject()
	{
		return getProject();
	}

/*
 * (non-Javadoc)
 * 
 * @see eu.cessar.ct.core.platform.ui.prefandprop.AbstractPreferenceAndPropertyPage#performDefaults()
 */
	@Override
	protected void performDefaults()
	{
		btnCheckDuplicateModuleDefinitions.setSelection(false);
		btnCheckForClassPathVerification.setSelection(false);
		btnCheckForJetVerification.setSelection(false);
		btnCheckForPmbinFolderVerification.setSelection(false);
		btnCheckForProjectVerification.setSelection(false);
		btnCheckForWrongMetamodelFiles.setSelection(false);
		btnCheckForSettingsVerification.setSelection(false);

		// setProjectConsistencyControls(parentComposite, false, false, false, false, false, false, false);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.core.platform.ui.prefandprop.AbstractPreferenceAndPropertyPage#initialFUseProjectSettings()
	 */
	@Override
	protected boolean initialFUseProjectSettings()
	{
		return ProjectConsistencyPreferencesAccessor.getProjectSpecific(getProject());
	}

	/**
	 * Project consistency preferences.
	 * 
	 * @param parent
	 *        the parent
	 */
	public void getProjectConsistencySettings()
	{
		// Get workspace preferences
		checkDuplicateModuleDefinitions = false;
		checkForWrongMetamodelFiles = false;
		checkForJetVerification = false;
		checkForProjectVerification = false;
		checkForClasspathVerification = false;
		checkForSettingsVerification = false;
		checkForPmbinFolderVerification = false;

		EProjectInconsistencyType projectInconsistencyType;

		projectInconsistencyType = getProjectConsistencyTypePreference(project,
			eu.cessar.ct.workspace.consistencycheck.EProjectInconsistencyType.DUPLICATE_MODULE_DEF);
		checkDuplicateModuleDefinitions = (projectInconsistencyType == eu.cessar.ct.workspace.consistencycheck.EProjectInconsistencyType.DUPLICATE_MODULE_DEF);

		projectInconsistencyType = getProjectConsistencyTypePreference(project,
			eu.cessar.ct.workspace.consistencycheck.EProjectInconsistencyType.WRONG_MODEL);
		checkForWrongMetamodelFiles = (projectInconsistencyType == eu.cessar.ct.workspace.consistencycheck.EProjectInconsistencyType.WRONG_MODEL);

		projectInconsistencyType = getProjectConsistencyTypePreference(project,
			eu.cessar.ct.workspace.consistencycheck.EProjectInconsistencyType.JET_PROBLEMS);
		checkForJetVerification = (projectInconsistencyType == eu.cessar.ct.workspace.consistencycheck.EProjectInconsistencyType.JET_PROBLEMS);

		projectInconsistencyType = getProjectConsistencyTypePreference(project,
			EProjectInconsistencyType.PROJECT_PROBLEMS);
		checkForProjectVerification = (projectInconsistencyType == eu.cessar.ct.workspace.consistencycheck.EProjectInconsistencyType.PROJECT_PROBLEMS);

		projectInconsistencyType = getProjectConsistencyTypePreference(project,
			EProjectInconsistencyType.CLASSPATH_PROBLEMS);
		checkForClasspathVerification = (projectInconsistencyType == eu.cessar.ct.workspace.consistencycheck.EProjectInconsistencyType.CLASSPATH_PROBLEMS);

		projectInconsistencyType = getProjectConsistencyTypePreference(project,
			EProjectInconsistencyType.SETTINGS_PROBLEMS);
		checkForSettingsVerification = (projectInconsistencyType == eu.cessar.ct.workspace.consistencycheck.EProjectInconsistencyType.SETTINGS_PROBLEMS);

		projectInconsistencyType = getProjectConsistencyTypePreference(project,
			EProjectInconsistencyType.PMBIN_PROBLEMS);
		checkForPmbinFolderVerification = (projectInconsistencyType == eu.cessar.ct.workspace.consistencycheck.EProjectInconsistencyType.PMBIN_PROBLEMS);
	}

	/**
	 * Sets the project consistency controls.
	 * 
	 * @param parent
	 *        the parent
	 * @param isCheckForDupModDef
	 *        the check for duplicate module definitions flag indicator
	 * @param isCheckForWrongMetamodelFiles
	 *        the check for wrong meta-model files flag indicator
	 * @param checkJetVerification
	 */
	private void setProjectConsistencyControls(final Composite parent, boolean isCheckForDupModDef,
		boolean isCheckForWrongMetamodelFiles, boolean isCheckJetVerification, boolean isCheckProjectVerification,
		boolean isCheckClassPathVerification, boolean isCheckSettingsVerification,
		boolean isCheckPmbinFolderVerification)
	{

		ExpandableComposite projectConsistencyExpandableComposite = CessarFormToolkit.DEFAULT.createExpandableComposite(
			parent, ExpandableComposite.TWISTIE);
		projectConsistencyExpandableComposite.setText(Messages.projectChecker_workspace_preferences_title);
		projectConsistencyExpandableComposite.setBackground(parent.getBackground());
		GridData gridData = new GridData(SWT.FILL, SWT.NONE, true, false);
		projectConsistencyExpandableComposite.setLayoutData(gridData);
		projectConsistencyExpandableComposite.setExpanded(true);

		Composite compositeForExpansion = CessarFormToolkit.DEFAULT.createPlainComposite(
			projectConsistencyExpandableComposite, SWT.None);
		projectConsistencyExpandableComposite.setClient(compositeForExpansion);
		GridLayout gridLayout = new GridLayout(1, false);
		compositeForExpansion.setLayout(gridLayout);

		projectConsistencyExpandableComposite.addExpansionListener(new ExpansionAdapter()
		{
			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * org.eclipse.ui.forms.events.ExpansionAdapter#expansionStateChanged(org.eclipse.ui.forms.events.ExpansionEvent
			 * )
			 */
			@Override
			public void expansionStateChanged(ExpansionEvent e)
			{
				parent.getParent().pack(true);
			}
		});

		Group projectConsistencyGroup = new Group(compositeForExpansion, SWT.NONE);
		projectConsistencyGroup.setText(Messages.projectChecker_workspace_preferences_title);
		projectConsistencyGroup.setLayout(new GridLayout(1, false));
		projectConsistencyGroup.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false));

		createCheckDuplicateModuleDefinitionsControls(compositeForExpansion, projectConsistencyGroup,
			isCheckForDupModDef);

		Group groupCheckWrongMetamodel = new Group(projectConsistencyGroup, SWT.NONE);
		groupCheckWrongMetamodel.setLayout(new GridLayout(1, false));
		groupCheckWrongMetamodel.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));

		createCheckWrongMetamodelFilesControls(compositeForExpansion, groupCheckWrongMetamodel,
			isCheckForWrongMetamodelFiles);

		Group groupCheckJetVerification = new Group(projectConsistencyGroup, SWT.NONE);
		groupCheckJetVerification.setLayout(new GridLayout(1, false));
		groupCheckJetVerification.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));

		createCheckJetVerificationControls(compositeForExpansion, groupCheckJetVerification, isCheckJetVerification);

		/** Project Verification */
		Group groupCheckProjectVerification = new Group(projectConsistencyGroup, SWT.NONE);
		groupCheckProjectVerification.setLayout(new GridLayout(1, false));
		groupCheckProjectVerification.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));

		createCheckProjectVerificationControls(compositeForExpansion, groupCheckProjectVerification,
			isCheckProjectVerification);

		/** Classpath Verification */
		Group groupCheckClasspathVerification = new Group(projectConsistencyGroup, SWT.NONE);
		groupCheckClasspathVerification.setLayout(new GridLayout(1, false));
		groupCheckClasspathVerification.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));

		createCheckClassPathVerificationControls(compositeForExpansion, groupCheckClasspathVerification,
			isCheckClassPathVerification);

		/** Settings Verification */
		Group groupCheckSettingsVerification = new Group(projectConsistencyGroup, SWT.NONE);
		groupCheckSettingsVerification.setLayout(new GridLayout(1, false));
		groupCheckSettingsVerification.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));

		createCheckSettingsVerificationControls(compositeForExpansion, groupCheckSettingsVerification,
			isCheckSettingsVerification);

		/** pmbin folder Verification */
		Group groupCheckPmbinFolderVerification = new Group(projectConsistencyGroup, SWT.NONE);
		groupCheckPmbinFolderVerification.setLayout(new GridLayout(1, false));
		groupCheckPmbinFolderVerification.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));

		createCheckPmFolderVerificationControls(compositeForExpansion, groupCheckPmbinFolderVerification,
			isCheckPmbinFolderVerification);
	}

	/**
	 * Creates the check duplicate modules definitions controls.
	 * 
	 * @param composite
	 *        the composite
	 * @param group
	 *        the group
	 * @param isCheckForDupModDef
	 *        the check for duplicate module definitions flag indicator
	 */
	private void createCheckDuplicateModuleDefinitionsControls(Composite composite, Group group,
		boolean isCheckForDupModDef)
	{
		Group groupCheckDupModDef = new Group(group, SWT.NONE);

		groupCheckDupModDef.setLayout(new GridLayout(1, false));
		groupCheckDupModDef.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false));

		btnCheckDuplicateModuleDefinitions = new Button(groupCheckDupModDef, SWT.CHECK);

		btnCheckDuplicateModuleDefinitions.setText(Messages.ProjectConsistencySettingsPage_check_for_duplicate_module_definitions);
		btnCheckDuplicateModuleDefinitions.setSelection(isCheckForDupModDef);

		FontData fontData = btnCheckDuplicateModuleDefinitions.getFont().getFontData()[0];
		Font font = new Font(composite.getDisplay(), new FontData(fontData.getName(), fontData.getHeight(), SWT.BOLD));
		btnCheckDuplicateModuleDefinitions.setFont(font);

		Label labelDupModDefDetail = new Label(groupCheckDupModDef, SWT.NONE);
		labelDupModDefDetail.setText(Messages.ProjectConsistencySettingsPage_check_for_duplicate_module_definitions_description);
		labelDupModDefDetail.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
	}

	/**
	 * Creates the check wrong metamodel files controls.
	 * 
	 * @param composite
	 *        the composite
	 * @param group
	 *        the group
	 * @param isCheckForWrongMetamodelFiles
	 *        the is check for wrong metamodel files
	 */
	private void createCheckWrongMetamodelFilesControls(Composite composite, Group group,
		boolean isCheckForWrongMetamodelFiles)
	{
		btnCheckForWrongMetamodelFiles = new Button(group, SWT.CHECK);
		btnCheckForWrongMetamodelFiles.setText(Messages.ProjectConsistencySettingsPage_check_for_files_of_wrong_metamodel);
		btnCheckForWrongMetamodelFiles.setSelection(isCheckForWrongMetamodelFiles);

		FontData fontData = btnCheckForWrongMetamodelFiles.getFont().getFontData()[0];
		Font font = new Font(composite.getDisplay(), new FontData(fontData.getName(), fontData.getHeight(), SWT.BOLD));
		btnCheckForWrongMetamodelFiles.setFont(font);

		Label labelWrongMetamodelDetail = new Label(group, SWT.NONE);
		labelWrongMetamodelDetail.setText(Messages.ProjectConsistencySettingsPage_check_for_files_of_wrong_metamodel_description);
		labelWrongMetamodelDetail.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
	}

	/**
	 * Creates the check jetVerification controls.
	 * 
	 * @param composite
	 *        the composite
	 * @param group
	 *        the group
	 * @param isCheckForWrongMetamodelFiles
	 *        the is check for wrong metamodel files
	 */
	private void createCheckJetVerificationControls(Composite composite, Group group, boolean isCheckJetVerification)
	{
		btnCheckForJetVerification = new Button(group, SWT.CHECK);
		btnCheckForJetVerification.setText(Messages.ProjectConsistencySettingsPage_check_for_jet_verification);
		btnCheckForJetVerification.setSelection(isCheckJetVerification);

		FontData fontData = btnCheckForJetVerification.getFont().getFontData()[0];
		Font font = new Font(composite.getDisplay(), new FontData(fontData.getName(), fontData.getHeight(), SWT.BOLD));
		btnCheckForJetVerification.setFont(font);

		Label labelJetVerificationDetail = new Label(group, SWT.NONE);
		labelJetVerificationDetail.setText(Messages.ProjectConsistencySettingsPage_check_for_jet_verification_description);
		labelJetVerificationDetail.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
	}

	/**
	 * Creates the check projectVerification controls.
	 * 
	 * @param composite
	 *        the composite
	 * @param group
	 *        the group
	 * @param isCheckProjectVerification
	 * 
	 */
	private void createCheckProjectVerificationControls(Composite composite, Group group,
		boolean isCheckProjectVerification)
	{
		btnCheckForProjectVerification = new Button(group, SWT.CHECK);
		btnCheckForProjectVerification.setText(Messages.ProjectConsistencySettingsPage_Check_For_Project_Verification);
		btnCheckForProjectVerification.setSelection(isCheckProjectVerification);

		FontData fontData = btnCheckForProjectVerification.getFont().getFontData()[0];
		Display display = composite.getDisplay();
		String fontName = fontData.getName();
		int fontHeight = fontData.getHeight();
		FontData data = new FontData(fontName, fontHeight, SWT.BOLD);

		Font font = new Font(display, data);
		btnCheckForProjectVerification.setFont(font);

		Label labelProjectVerificationDetail = new Label(group, SWT.NONE);
		labelProjectVerificationDetail.setText(Messages.ProjectConsistencySettingsPage_Check_For_Project_Verification_Desc);
		labelProjectVerificationDetail.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
	}

	/**
	 * Creates the check classPathVerification controls.
	 * 
	 * @param composite
	 *        the composite
	 * @param group
	 *        the group
	 * @param isCheckClassPathVerification
	 * 
	 */
	private void createCheckClassPathVerificationControls(Composite composite, Group group,
		boolean isCheckClassPathVerification)
	{
		btnCheckForClassPathVerification = new Button(group, SWT.CHECK);
		btnCheckForClassPathVerification.setText(Messages.ProjectConsistencySettingsPage_Check_For_Classpath_Verification);
		btnCheckForClassPathVerification.setSelection(isCheckClassPathVerification);

		FontData fontData = btnCheckForClassPathVerification.getFont().getFontData()[0];
		Display display = composite.getDisplay();
		String fontName = fontData.getName();
		int fontHeight = fontData.getHeight();
		FontData data = new FontData(fontName, fontHeight, SWT.BOLD);

		Font font = new Font(display, data);

		btnCheckForClassPathVerification.setFont(font);

		Label labelClassPathVerificationDetail = new Label(group, SWT.NONE);
		labelClassPathVerificationDetail.setText(Messages.ProjectConsistencySettingsPage_Check_For_Classpath_Verification_Desc);
		labelClassPathVerificationDetail.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
	}

	/**
	 * Creates the check classPathVerification controls.
	 * 
	 * @param composite
	 *        the composite
	 * @param group
	 *        the group
	 * @param isCheckSettingsVerification
	 * 
	 */
	private void createCheckSettingsVerificationControls(Composite composite, Group group,
		boolean isCheckSettingsVerification)
	{
		btnCheckForSettingsVerification = new Button(group, SWT.CHECK);
		btnCheckForSettingsVerification.setText(Messages.ProjectConsistencySettingsPage_Check_For_Settings_Verification);
		btnCheckForSettingsVerification.setSelection(isCheckSettingsVerification);

		FontData fontData = btnCheckForSettingsVerification.getFont().getFontData()[0];
		Display display = composite.getDisplay();
		String fontName = fontData.getName();
		int fontHeight = fontData.getHeight();
		FontData data = new FontData(fontName, fontHeight, SWT.BOLD);

		Font font = new Font(display, data);
		btnCheckForSettingsVerification.setFont(font);

		Label labelSettingsVerificationDetail = new Label(group, SWT.NONE);
		labelSettingsVerificationDetail.setText(Messages.ProjectConsistencySettingsPage_Check_For_Settings_Verification_Desc);
		labelSettingsVerificationDetail.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
	}

	/**
	 * Creates the check classPathVerification controls.
	 * 
	 * @param composite
	 *        the composite
	 * @param group
	 *        the group
	 * @param isCheckPmFolderVerification
	 * 
	 */
	private void createCheckPmFolderVerificationControls(Composite composite, Group group,
		boolean isCheckPmFolderVerification)
	{
		btnCheckForPmbinFolderVerification = new Button(group, SWT.CHECK);
		btnCheckForPmbinFolderVerification.setText(Messages.ProjectConsistencySettingsPage_Check_For_pmbin_Verification);
		btnCheckForPmbinFolderVerification.setSelection(isCheckPmFolderVerification);

		FontData fontData = btnCheckForPmbinFolderVerification.getFont().getFontData()[0];
		Display display = composite.getDisplay();
		String fontName = fontData.getName();
		int fontHeight = fontData.getHeight();
		FontData data = new FontData(fontName, fontHeight, SWT.BOLD);

		Font font = new Font(display, data);
		btnCheckForPmbinFolderVerification.setFont(font);

		Label labelPmbinverificationDetail = new Label(group, SWT.NONE);
		labelPmbinverificationDetail.setText(Messages.ProjectConsistencySettingsPage_Check_For_pmbin_Verification_Desc);
		labelPmbinverificationDetail.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
	}

	/**
	 * Sets the project consistency type preference.
	 * 
	 * @param iProject
	 *        the new project consistency type preference
	 */
	private void setProjectConsistencyTypePreference(IProject iProject)
	{
		ProjectConsistencyPreferencesAccessor.resetWSPreferences();

		if (iProject != null)
		{
			if (ProjectConsistencyPreferencesAccessor.getProjectSpecific(iProject))
			{
				ProjectConsistencyPreferencesAccessor.resetProjectPreferences(iProject);
			}
		}
		if (btnCheckDuplicateModuleDefinitions.getSelection())
		{
			ProjectConsistencyPreferencesAccessor.setEProjectInconsistencyType(iProject,
				EProjectInconsistencyType.DUPLICATE_MODULE_DEF);
		}
		if (btnCheckForWrongMetamodelFiles.getSelection())
		{
			ProjectConsistencyPreferencesAccessor.setEProjectInconsistencyType(iProject,
				EProjectInconsistencyType.WRONG_MODEL);
		}
		if (btnCheckForJetVerification.getSelection())
		{
			ProjectConsistencyPreferencesAccessor.setEProjectInconsistencyType(iProject,
				EProjectInconsistencyType.JET_PROBLEMS);
		}
		if (btnCheckForProjectVerification.getSelection())
		{
			ProjectConsistencyPreferencesAccessor.setEProjectInconsistencyType(iProject,
				EProjectInconsistencyType.PROJECT_PROBLEMS);
		}
		if (btnCheckForClassPathVerification.getSelection())
		{
			ProjectConsistencyPreferencesAccessor.setEProjectInconsistencyType(iProject,
				EProjectInconsistencyType.CLASSPATH_PROBLEMS);
		}
		if (btnCheckForSettingsVerification.getSelection())
		{
			ProjectConsistencyPreferencesAccessor.setEProjectInconsistencyType(iProject,
				EProjectInconsistencyType.SETTINGS_PROBLEMS);
		}
		if (btnCheckForPmbinFolderVerification.getSelection())
		{
			ProjectConsistencyPreferencesAccessor.setEProjectInconsistencyType(iProject,
				EProjectInconsistencyType.PMBIN_PROBLEMS);
		}

	}

	/**
	 * Gets the project consistency type preference.
	 * 
	 * @param proj
	 *        the project
	 * @param projectInconsistencyType
	 * @return the project consistency type preference
	 */
	public EProjectInconsistencyType getProjectConsistencyTypePreference(IProject proj,
		EProjectInconsistencyType projectInconsistencyType)
	{

		EProjectInconsistencyType value = eu.cessar.ct.workspace.consistencycheck.EProjectInconsistencyType.NONE;

		switch (projectInconsistencyType)
		{
			case DUPLICATE_MODULE_DEF:
				value = ProjectConsistencyPreferencesAccessor.getEProjectInconsistencyType(proj != null ? proj
					: getProject(),
					eu.cessar.ct.workspace.consistencycheck.EProjectInconsistencyType.DUPLICATE_MODULE_DEF);
				break;

			case WRONG_MODEL:
				value = ProjectConsistencyPreferencesAccessor.getEProjectInconsistencyType(proj != null ? proj
					: getProject(), eu.cessar.ct.workspace.consistencycheck.EProjectInconsistencyType.WRONG_MODEL);
				break;
			case JET_PROBLEMS:
				value = ProjectConsistencyPreferencesAccessor.getEProjectInconsistencyType(proj != null ? proj
					: getProject(), eu.cessar.ct.workspace.consistencycheck.EProjectInconsistencyType.JET_PROBLEMS);
				break;
			case PROJECT_PROBLEMS:
				value = ProjectConsistencyPreferencesAccessor.getEProjectInconsistencyType(proj != null ? proj
					: getProject(), eu.cessar.ct.workspace.consistencycheck.EProjectInconsistencyType.PROJECT_PROBLEMS);
				break;
			case CLASSPATH_PROBLEMS:
				value = ProjectConsistencyPreferencesAccessor.getEProjectInconsistencyType(proj != null ? proj
					: getProject(),
					eu.cessar.ct.workspace.consistencycheck.EProjectInconsistencyType.CLASSPATH_PROBLEMS);
				break;
			case SETTINGS_PROBLEMS:
				value = ProjectConsistencyPreferencesAccessor.getEProjectInconsistencyType(proj != null ? proj
					: getProject(), eu.cessar.ct.workspace.consistencycheck.EProjectInconsistencyType.SETTINGS_PROBLEMS);
				break;
			case PMBIN_PROBLEMS:
				value = ProjectConsistencyPreferencesAccessor.getEProjectInconsistencyType(proj != null ? proj
					: getProject(), eu.cessar.ct.workspace.consistencycheck.EProjectInconsistencyType.PMBIN_PROBLEMS);
				break;
			default:
				value = eu.cessar.ct.workspace.consistencycheck.EProjectInconsistencyType.NONE;
		}
		return value;
	}

	/**
	 * @return the checkDuplicateModuleDefinitions
	 */
	public boolean isCheckDuplicateModuleDefinitions()
	{
		return checkDuplicateModuleDefinitions;
	}

	/**
	 * @param checkDuplicateModuleDefinitions
	 *        the checkDuplicateModuleDefinitions to set
	 */
	public void setCheckDuplicateModuleDefinitions(boolean checkDuplicateModuleDefinitions)
	{
		this.checkDuplicateModuleDefinitions = checkDuplicateModuleDefinitions;
	}

	/**
	 * @return the checkForWrongMetamodelFiles
	 */
	public boolean isCheckForWrongMetamodelFiles()
	{
		return checkForWrongMetamodelFiles;
	}

	/**
	 * @param checkForWrongMetamodelFiles
	 *        the checkForWrongMetamodelFiles to set
	 */
	public void setCheckForWrongMetamodelFiles(boolean checkForWrongMetamodelFiles)
	{
		this.checkForWrongMetamodelFiles = checkForWrongMetamodelFiles;
	}

	/**
	 * @return the checkForJetVerification
	 */
	public boolean isCheckForJetVerification()
	{
		return checkForJetVerification;
	}

	/**
	 * @param checkForJetVerification
	 *        the checkForJetVerification to set
	 */
	public void setCheckForJetVerification(boolean checkForJetVerification)
	{
		this.checkForJetVerification = checkForJetVerification;
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
	 * @return the checkForClasspathVerification
	 */
	public boolean isCheckForClasspathVerification()
	{
		return checkForClasspathVerification;
	}

	/**
	 * @param checkForClasspathVerification
	 *        the checkForClasspathVerification to set
	 */
	public void setCheckForClasspathVerification(boolean checkForClasspathVerification)
	{
		this.checkForClasspathVerification = checkForClasspathVerification;
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
	public void setCheckForPmFolderVerification(boolean checkForPmbinFolderVerification)
	{
		this.checkForPmbinFolderVerification = checkForPmbinFolderVerification;
	}

}
