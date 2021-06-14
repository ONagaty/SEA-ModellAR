package eu.cessar.ct.runtime.ui.internal;

import org.eclipse.jface.wizard.Wizard;

import eu.cessar.ct.cid.bindings.PlugetBinding;
import eu.cessar.req.Requirement;

/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 *
 * File created by uid95513<br/>
 * Apr 28, 2015 11:20:28 AM
 *
 * </copyright>
 */

/**
 * Wizard that is created dynamically with all pluget inputs described by its .cid file. If the pluget inputs are
 * described in the .cid, then this wizard will open a wizard page with dynamically filled content. If the "automation"
 * property from the .cid is set to false or if no description is provided in this file, the wizard will not display the
 * dynamic wizard.
 *
 * @author uid95513
 *
 *         %created_by: uid95513 %
 *
 *         %date_created: Tue Jun 23 14:54:36 2015 %
 *
 *         %version: 5 %
 */
@Requirement(
	reqID = "61760")
public class PlugetAutomationWizard extends Wizard
{
	private PlugetAutomationWizardPage dynamicWizardPage;

	private String plugetName;
	private PlugetBinding plugetBiding;
	private String[] argumentList;

	/**
	 * @param pluget
	 *
	 */
	public PlugetAutomationWizard(PlugetBinding pluget)
	{
		super();
		plugetBiding = pluget;
		plugetName = pluget.getArtifact().getName();
		setWindowTitle(Messages.runPluget);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.wizard.Wizard#performFinish()
	 */
	@Override
	public boolean performFinish()
	{
		// store the user data
		argumentList = dynamicWizardPage.getArguments();
		return true;
	}

	@Override
	public void addPages()
	{
		dynamicWizardPage = new PlugetAutomationWizardPage(
			"theAutomationWizardPage", Messages.runPluget + plugetName, null, plugetBiding); //$NON-NLS-1$
		if (plugetBiding.getArtifact() != null
			&& plugetBiding.getArtifact().getProperties(AutomationConstants.DESCRIPTION_PROPERTY_LABEL).size() > 0)
		{
			// put the description of the pluget from .cid in the wizard
			dynamicWizardPage.setDescription(plugetBiding.getArtifact().getProperties(
				AutomationConstants.DESCRIPTION_PROPERTY_LABEL).get(0).getValue());
		}
		// if no description, put standard description
		else
		{
			dynamicWizardPage.setDescription(Messages.dynamicWizardPageDescription);
		}
		addPage(dynamicWizardPage);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.wizard.Wizard#canFinish()
	 */
	@Override
	public boolean canFinish()
	{
		// disables the finish button until all mandatory inputs are filled - isPageComplete() is called
		return super.canFinish();
	}

	/**
	 * @return a list of string arguments for running the pluget
	 */
	public String[] getArguments()
	{
		return argumentList;
	}
}
