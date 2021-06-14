package eu.cessar.ct.ecuc.workspace.ui.internal.wizards.pages.texts;

import java.util.HashMap;

import eu.cessar.ct.ecuc.workspace.ui.internal.Messages;
import eu.cessar.ct.ecuc.workspace.ui.internal.wizards.pages.AbstractWizardPage;
import eu.cessar.ct.ecuc.workspace.ui.internal.wizards.pages.IPageTextProvider;

/**
 *
 * @author uid95768
 *
 *         %created_by: uid95768 %
 *
 *         %date_created: Tue Mar  3 08:08:42 2015 %
 *
 *         %version: 4 %
 */
public class ProjectSelectionTexts implements IPageTextProvider
{

	private boolean forModuleDefWizard;

	/**
	 * @param forModuleDefWizard
	 *        if <code>forModuleDefWizard</code> is <code>true</code>, will retrieve the description for the Module
	 *        Definition wizard, otherwise for the Module Configuration one
	 */
	public ProjectSelectionTexts(boolean forModuleDefWizard)
	{
		this.forModuleDefWizard = forModuleDefWizard;

	}

	public HashMap<String, String> getPageTexts()
	{
		HashMap<String, String> texts = new HashMap<String, String>();
		texts.put(AbstractWizardPage.PAGE_TITLE, Messages.ProjectSelectionPage_Title);
		String desc = ""; //$NON-NLS-1$
		if (forModuleDefWizard)
		{
			desc = Messages.ProjectSelectionPage_Description_for_Module_Def;
		}
		else
		{
			desc = Messages.ProjectSelectionPage_Description_for_Module_Config;
		}
		texts.put(AbstractWizardPage.PAGE_DESCRIPTION, desc);

		texts.put("btnDisplayHidden", Messages.ProjectSelectionPage_ShowHidden); //$NON-NLS-1$
		return texts;
	}
}
