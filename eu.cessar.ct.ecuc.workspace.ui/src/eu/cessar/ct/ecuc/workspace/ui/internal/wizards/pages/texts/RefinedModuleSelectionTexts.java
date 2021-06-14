package eu.cessar.ct.ecuc.workspace.ui.internal.wizards.pages.texts;

import java.util.HashMap;

import eu.cessar.ct.ecuc.workspace.ui.internal.Messages;
import eu.cessar.ct.ecuc.workspace.ui.internal.wizards.pages.AbstractWizardPage;
import eu.cessar.ct.ecuc.workspace.ui.internal.wizards.pages.IPageTextProvider;

public class RefinedModuleSelectionTexts implements IPageTextProvider
{

	public HashMap<String, String> getPageTexts()
	{
		HashMap<String, String> texts = new HashMap<String, String>();
		texts.put(AbstractWizardPage.PAGE_TITLE,
			Messages.RefinedModuleDefinitionSelectionPage_Title);
		texts.put(AbstractWizardPage.PAGE_DESCRIPTION,
			Messages.RefinedModuleDefinitionSelectionPage_Description);

		texts.put("label", Messages.RefinedModuleDefinitionSelectionPage_Table_Label); //$NON-NLS-1$
		texts.put("ModuleNameColumn", //$NON-NLS-1$
			Messages.RefinedModuleDefinitionSelectionPage_ModuleNameColumn);
		texts.put("ModuleFileColumn", //$NON-NLS-1$
			Messages.RefinedModuleDefinitionSelectionPage_ModuleFileColumn);

		return texts;
	}

}
