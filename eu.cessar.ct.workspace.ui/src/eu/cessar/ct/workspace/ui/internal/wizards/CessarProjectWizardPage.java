/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidl7321 Sep 6, 2012 12:06:21 PM </copyright>
 */
package eu.cessar.ct.workspace.ui.internal.wizards;

import org.artop.aal.workspace.ui.wizards.pages.NewAutosarProjectCreationPage;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.PlatformUI;

import eu.cessar.ct.core.platform.EProjectVariant;
import eu.cessar.req.Requirement;

/**
 * @author uidl7321
 *
 */
@Requirement(
	reqID = "REQ_COMPAT#1")
public class CessarProjectWizardPage extends NewAutosarProjectCreationPage
{

	/**
	 *
	 */
	private static final String NEW_CESSAR_PROJECT_WIZARD = "eu.cessar.ct.workspace.ui.new_cessar_project_wizard_context";
	private Label autosarReleaseMessageLabel;
	private ConfigVariantModeGroup configVariantModeGroup;

	/**
	 * @param pageName
	 */
	public CessarProjectWizardPage(String pageName)
	{
		super(pageName);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.sphinx.emf.workspace.ui.wizards.pages.NewModelProjectCreationPage#createControl(org.eclipse.swt.widgets
	 * .Composite)
	 */
	@Override
	public void createControl(Composite parent)
	{
		super.createControl(parent);

		Control[] children = parent.getChildren();

		if (children.length == 1)
		{
			PlatformUI.getWorkbench().getHelpSystem().setHelp(children[0], NEW_CESSAR_PROJECT_WIZARD);
		}

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.artop.aal.workspace.ui.wizards.pages.NewAutosarProjectCreationPage#createAdditionalControls(org.eclipse.swt
	 * .widgets.Composite)
	 */
	@Override
	protected void createAdditionalControls(Composite parent)
	{
		createMetaModelVersionGroup(parent);

		configVariantModeGroup = new ConfigVariantModeGroup();
		configVariantModeGroup.createContent(parent, 1);

		createXMLImportCheckBoxes(parent);
		createAutosarReleaseMessage(parent);

		Dialog.applyDialogFont(getControl());
	}

	/**
	 * Create an informative part, related to the selected Autosar release
	 *
	 * @param parent
	 * @return updated autosarReleaseMessageLabel
	 */
	protected Label createAutosarReleaseMessage(Composite parent)
	{
		autosarReleaseMessageLabel = new Label(parent, SWT.None);
		GridData gridData = new GridData(SWT.FILL, SWT.BEGINNING, true, true);
		// gridData.heightHint = 40;
		autosarReleaseMessageLabel.setLayoutData(gridData);

		// String autosarReleaseMessage = computeAutosarReleaseMessage(getMetaModelVersionDescriptor());
		autosarReleaseMessageLabel.setText(Messages.autosarReleaseMessage);

		return autosarReleaseMessageLabel;
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

}
