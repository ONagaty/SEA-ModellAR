package eu.cessar.ct.ecuc.workspace.ui.internal.wizards.pages.texts;

import java.util.HashMap;

import eu.cessar.ct.ecuc.workspace.ui.internal.Messages;
import eu.cessar.ct.ecuc.workspace.ui.internal.wizards.pages.AbstractWizardPage;
import eu.cessar.ct.ecuc.workspace.ui.internal.wizards.pages.IPageTextProvider;

public class ModuleDefinitionSelectionTexts implements IPageTextProvider
{

	public HashMap<String, String> getPageTexts()
	{
		HashMap<String, String> texts = new HashMap<String, String>();
		texts.put(AbstractWizardPage.PAGE_TITLE,
			Messages.ModuleConfigurationDefinitionSelectionPage_Title);
		texts.put(AbstractWizardPage.PAGE_DESCRIPTION,
			Messages.ModuleConfigurationDefinitionSelectionPage_Description);
		texts.put(
			"ElementTreeColumn", Messages.ModuleConfigurationDefinitionSelectionPage_ElementTreeColumn); //$NON-NLS-1$
		texts.put(
			"PathTreeColumn", Messages.ModuleConfigurationDefinitionSelectionPage_PathTreeColumn); //$NON-NLS-1$
		return texts;
	}
}
