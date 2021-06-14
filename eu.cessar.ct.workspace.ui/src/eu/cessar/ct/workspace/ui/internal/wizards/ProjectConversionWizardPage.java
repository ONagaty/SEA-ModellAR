/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 *
 * File created by uid95513<br/>
 * Feb 9, 2015 1:13:08 PM
 *
 * </copyright>
 */
package eu.cessar.ct.workspace.ui.internal.wizards;

import org.artop.aal.common.metamodel.AutosarReleaseDescriptor;
import org.artop.aal.common.preferences.IAutosarPreferenceConstants;
import org.artop.aal.workspace.preferences.IAutosarWorkspacePreferences;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.sphinx.emf.workspace.ui.wizards.groups.BasicMetaModelVersionGroup;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;

import eu.cessar.ct.core.platform.EProjectVariant;

/**
 * Class defines the page content used in ProjectConversionWizard
 *
 * @author uid95513
 *
 *         %created_by: uid95513 %
 *
 *         %date_created: Mon Feb 16 15:39:46 2015 %
 *
 *         %version: 2 %
 */
public class ProjectConversionWizardPage extends WizardPage
{
	private static final String PROJECT_CONVERSION_WIZARD_PAGE = "eu.cessar.ct.workspace.ui.project_conversion_wizard_page_context"; //$NON-NLS-1$
	private ConfigVariantModeGroup configVariantModeGroup;
	private BasicMetaModelVersionGroup<AutosarReleaseDescriptor> metaModelVersionGroup;

	/**
	 * @param pageName
	 */
	public ProjectConversionWizardPage(String pageName)
	{
		super(pageName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void createControl(Composite parent)
	{
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout(1, false);

		composite.setLayout(layout);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		PlatformUI.getWorkbench().getHelpSystem().setHelp(composite, PROJECT_CONVERSION_WIZARD_PAGE);

		createMetaModelVersionGroup(composite);
		createConfigVariantModeGroup(composite);

		setControl(composite);

		Dialog.applyDialogFont(getControl());

	}

	/**
	 * @param parent
	 */
	private void createConfigVariantModeGroup(Composite parent)
	{
		configVariantModeGroup = new ConfigVariantModeGroup();
		configVariantModeGroup.createContent(parent, 1);
	}

	private void createMetaModelVersionGroup(Composite parent)
	{
		metaModelVersionGroup = new BasicMetaModelVersionGroup<AutosarReleaseDescriptor>(
			"BasicMetaModelVersionGroup", AutosarReleaseDescriptor.INSTANCE, //$NON-NLS-1$
			IAutosarWorkspacePreferences.AUTOSAR_RELEASE,
			IAutosarPreferenceConstants.AUTOSAR_RELEASE_PREFERENCE_PAGE_ID);

		metaModelVersionGroup.setMetaModelVersionLabel(Messages.label_metaModelReleaseCT);
		metaModelVersionGroup.createContent(parent, 3, false);
	}

	/**
	 * Returns the constant associated with the configuration mode selected by the user in the wizard page.
	 *
	 * @return a constant associated with the configuration mode. Can be one of the following:
	 *         <ul>
	 *         <li><code>EProjectVariant.DEVELOPMENT</code></li>
	 *         <li><code>EProjectVarian.PRODUCTION</code></li>
	 *         </ul>
	 *         The default value is <code>PlatformConstants.OTHER</code>.
	 */
	public EProjectVariant getConfigVariant()
	{
		Combo combo = configVariantModeGroup.getCfgVariantCombo();
		int index = combo.getSelectionIndex();

		if (index != -1)
		{
			String item = combo.getItem(index);
			return EProjectVariant.valueOf(item);
		}
		return EProjectVariant.DEVELOPMENT;
	}

	/**
	 * @return Gets the metamodel version descriptor that corresponds to the wizard selection.
	 */
	public AutosarReleaseDescriptor getMetaModelVersionDescriptor()
	{
		return metaModelVersionGroup.getMetaModelVersionDescriptor();
	}

}
