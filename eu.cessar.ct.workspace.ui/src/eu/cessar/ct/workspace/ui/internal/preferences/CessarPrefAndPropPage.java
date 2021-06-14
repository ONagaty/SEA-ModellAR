/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidj6860<br/>
 * Dec 9, 2014 9:35:25 AM
 *
 * </copyright>
 */
package eu.cessar.ct.workspace.ui.internal.preferences;

import org.eclipse.core.resources.IProject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;

import eu.cessar.ct.core.mms.FileExtensionPreferenceAccessor;
import eu.cessar.ct.core.platform.CESSARPreferencesAccessor;
import eu.cessar.ct.core.platform.ECompatibilityMode;
import eu.cessar.ct.core.platform.ui.prefandprop.AbstractPreferenceAndPropertyPage;
import eu.cessar.ct.core.platform.ui.widgets.ExtendedCCombo;
import eu.cessar.ct.workspace.ui.internal.wizards.Messages;
import eu.cessar.req.Requirement;

/**
 * Property/Preferences page for CESSAR file extension related settings
 *
 * @author uidj6860
 *
 *         %created_by: uidj6860 %
 *
 *         %date_created: Fri Mar 13 10:04:37 2015 %
 *
 *         %version: 6 %
 */

@Requirement(
	reqID = "26120")
public class CessarPrefAndPropPage extends AbstractPreferenceAndPropertyPage
{
	static final String MD_EXT_COMBO_LABEL_TEXT = "Module Definition extension"; //$NON-NLS-1$
	static final String ECU_CFG_EXT_COMBO_LABEL_TEXT = "ECU configuration extension"; //$NON-NLS-1$

	// page IDs
	private String propertyPageID = "eu.cessar.ct.workspace.ui.propertyPages.cessar"; //$NON-NLS-1$
	private String preferencePageID = "eu.cessar.ct.workspace.ui.prefPages.cessar"; //$NON-NLS-1$

	private Group compModeGroup;
	private Label compatModeInfo;

	// Instance ref group
	private Group extensionsGroup;
	private ExtendedCCombo bswmdExtCombo;
	private ExtendedCCombo ecuCfgExtCombo;
	private Label mdExtComboLabel;
	private Label ecuCfgExtComboLabel;

	/** The parent composite of the property page. */
	// private Composite parentComposite;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.cessar.ct.core.platform.ui.prefandprop.AbstractPreferenceAndPropertyPage#createPreferenceContent(org.eclipse
	 * .swt.widgets.Composite)
	 */
	@Override
	protected Control createPreferenceContent(Composite parent)
	{
		String[] autosarExtensions = FileExtensionPreferenceAccessor.getExtensions(null);

		Composite extComposite = new Group(parent, SWT.NONE);
		extComposite.setLayout(new GridLayout(1, false));
		extComposite.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false));

		// --- BSWMD
		extensionsGroup = new Group(extComposite, SWT.NONE);
		extensionsGroup.setText("Autosar extensions"); //$NON-NLS-1$
		GridLayout gridLayout1 = new GridLayout(2, false);

		GridData gridData1 = new GridData(GridData.FILL, GridData.FILL, true, false);
		gridData1.horizontalSpan = 2;

		extensionsGroup.setLayoutData(gridData1);
		extensionsGroup.setLayout(gridLayout1);

		mdExtComboLabel = new Label(extensionsGroup, SWT.NONE);
		mdExtComboLabel.setText(MD_EXT_COMBO_LABEL_TEXT);

		bswmdExtCombo = new ExtendedCCombo(extensionsGroup, SWT.BORDER);
		bswmdExtCombo.setItems(autosarExtensions);
		bswmdExtCombo.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		bswmdExtCombo.setText("Module Definition extension"); //$NON-NLS-1$
		bswmdExtCombo.select(FileExtensionPreferenceAccessor.getCurrentExtensionIndex(getProject(),
			FileExtensionPreferenceAccessor.KEY_CONFIG_BSWMD_EXT));
		bswmdExtCombo.setEditable(false);

		// ---- ECUC
		ecuCfgExtComboLabel = new Label(extensionsGroup, SWT.NONE);
		ecuCfgExtComboLabel.setText(ECU_CFG_EXT_COMBO_LABEL_TEXT);

		ecuCfgExtCombo = new ExtendedCCombo(extensionsGroup, SWT.BORDER);
		ecuCfgExtCombo.setItems(autosarExtensions);
		ecuCfgExtCombo.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		ecuCfgExtCombo.setText("ECU configuration extension"); //$NON-NLS-1$
		ecuCfgExtCombo.select(FileExtensionPreferenceAccessor.getCurrentExtensionIndex(getProject(),
			FileExtensionPreferenceAccessor.KEY_CONFIG_ECUC_EXT));
		ecuCfgExtCombo.setEditable(false);

		if (isProjectPreferencePage())
		{
			addDisplayFormatGroup(parent);
			initialize();
		}

		return extensionsGroup;
	}

	/**
	 * @param composite
	 */
	private void addDisplayFormatGroup(Composite composite)
	{
		// compatibility mode group
		compModeGroup = new Group(composite, SWT.NONE);
		// compat group only informative
		compModeGroup.setText("Compatibility mode"); //$NON-NLS-1$
		compModeGroup.setFont(composite.getFont());
		compModeGroup.setLayout(new RowLayout(SWT.VERTICAL));
		compModeGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		// group controls

		compatModeInfo = new Label(compModeGroup, SWT.NONE);
	}

	/**
	 *
	 */
	private void initialize()
	{
		IProject project = getProject();

		ECompatibilityMode compatibility = CESSARPreferencesAccessor.getCompatibilityMode(project);

		switch (compatibility)
		{
			case FULL:
				compatModeInfo.setText(Messages.fullCompatibilityModeInfo);
				break;
			case NONE:
				compatModeInfo.setText(Messages.noCompatibilityModeInfo);
				break;
			default:
				break;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.core.platform.ui.prefandprop.AbstractPreferenceAndPropertyPage#getPreferencePageID()
	 */
	@Override
	protected String getPreferencePageID()
	{
		return preferencePageID;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.core.platform.ui.prefandprop.AbstractPreferenceAndPropertyPage#getPropertyPageID()
	 */
	@Override
	protected String getPropertyPageID()
	{
		return propertyPageID;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.core.platform.ui.prefandprop.AbstractPreferenceAndPropertyPage#performOk()
	 */
	@Override
	public boolean performOk()
	{
		IProject project = getProject();
		if (isProjectPreferencePage())
		{
			FileExtensionPreferenceAccessor.setProjectSpecificSettings(project, getFUseProjectSettings());
			FileExtensionPreferenceAccessor.setBswMdExtensionInProject(project, bswmdExtCombo.getText());
			FileExtensionPreferenceAccessor.setEcucExtensionInProject(project, ecuCfgExtCombo.getText());
		}
		else
		{
			if (project != null)
			{
				FileExtensionPreferenceAccessor.setProjectSpecificSettings(project, getFUseProjectSettings());
			}
			else
			{
				FileExtensionPreferenceAccessor.setBswMdExtensionInWS(bswmdExtCombo.getText());
				FileExtensionPreferenceAccessor.setEcucExtensionInWS(ecuCfgExtCombo.getText());
			}

		}

		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.core.platform.ui.prefandprop.AbstractPreferenceAndPropertyPage#initialFUseProjectSettings()
	 */
	@Override
	protected boolean initialFUseProjectSettings()
	{
		return FileExtensionPreferenceAccessor.isProjectSpecificSettings(getProject());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.core.platform.ui.prefandprop.AbstractPreferenceAndPropertyPage#performDefaults()
	 */
	@Override
	protected void performDefaults()
	{
		// if (isProjectPreferencePage())
		// {
		bswmdExtCombo.select(FileExtensionPreferenceAccessor.getDefaultExtensionIndex(FileExtensionPreferenceAccessor.KEY_CONFIG_BSWMD_EXT));
		ecuCfgExtCombo.select(FileExtensionPreferenceAccessor.getDefaultExtensionIndex(FileExtensionPreferenceAccessor.KEY_CONFIG_ECUC_EXT));
		// }
		// else
		// {
		// bswmdExtCombo.select(FileExtensionPreferenceAccessor.getCurrentExtensionIndex(null,
		// FileExtensionPreferenceAccessor.KEY_CONFIG_BSWMD_EXT));
		// ecuCfgExtCombo.select(FileExtensionPreferenceAccessor.getCurrentExtensionIndex(null,
		// FileExtensionPreferenceAccessor.KEY_CONFIG_ECUC_EXT));
		// }
	}
}
