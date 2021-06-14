/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidg4020<br/>
 * Mar 3, 2014 10:06:07 AM
 *
 * </copyright>
 */
package eu.cessar.ct.runtime.ecuc.ui.internal.wizards;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.wizard.Wizard;

import eu.cessar.ct.cid.model.Artifact;
import eu.cessar.ct.runtime.ecuc.ui.internal.menu.HistoryParametersUtils;
import eu.cessar.ct.runtime.ecuc.ui.internal.wizards.PlugetSelectionPage.IPluget;

/**
 * Wizard which let you to select a pluget from the project or external and run it
 *
 * @author uidg4020
 *
 *         %created_by: uid95513 %
 *
 *         %date_created: Fri May  8 12:05:53 2015 %
 *
 *         %version: 7 %
 */
public class PlugetWizard extends Wizard
{

	private IProject project;
	// main page
	private PlugetSelectionPage plugetSelectionPage;
	private PlugetParametersPage plugetParametersPage;
	private String introducedValues;
	private IPluget pluget;

	/**
	 * No arguments constructor
	 */
	public PlugetWizard(IProject project)
	{
		super();
		super.setWindowTitle("Run pluget from selection"); //$NON-NLS-1$
		this.project = project;
	}

	@Override
	public void addPages()
	{

		String[] paramsHistory = HistoryParametersUtils.getParametersHistory(project);
		plugetSelectionPage = new PlugetSelectionPage("plugetWizardPage", project); //$NON-NLS-1$
		addPage(plugetSelectionPage);
		plugetParametersPage = new PlugetParametersPage("plugetParametersPage"); //$NON-NLS-1$
		addPage(plugetParametersPage);
		if (paramsHistory != null)
		{
			plugetParametersPage.setHistory(paramsHistory);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jface.wizard.Wizard#performFinish()
	 */
	@Override
	public boolean performFinish()
	{

		pluget = plugetSelectionPage.getSelectedPluget();
		introducedValues = plugetParametersPage.getIntroducedValues();
		if (introducedValues.length() > 0)
		{
			HistoryParametersUtils.addHistoryParameters(project, introducedValues);
		}
		return true;
	}

/*
 * (non-Javadoc)
 *
 * @see org.eclipse.jface.wizard.Wizard#canFinish()
 */
	@Override
	public boolean canFinish()
	{
		pluget = plugetSelectionPage.getSelectedPluget();
		if (pluget != null)
		{
			plugetParametersPage.updateDescription(plugetSelectionPage.getSelectedPlugetTitle());
			return true;
		}

		return false;
	}

	/**
	 * Return selected pluget. Can be an {@link IFile} if the pluget is from project, else is an {@link Artifact}
	 *
	 * @return selected artifact
	 */
	public IPluget getPluget()
	{
		return pluget;
	}

	/**
	 *
	 * @return selected parameters to run pluget
	 */
	public String getIntroducedValues()
	{
		return introducedValues;
	}

}
