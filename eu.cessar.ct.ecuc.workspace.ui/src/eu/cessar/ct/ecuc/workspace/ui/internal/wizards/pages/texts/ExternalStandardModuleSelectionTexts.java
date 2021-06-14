package eu.cessar.ct.ecuc.workspace.ui.internal.wizards.pages.texts;

import java.util.HashMap;

import eu.cessar.ct.ecuc.workspace.ui.internal.Messages;
import eu.cessar.ct.ecuc.workspace.ui.internal.wizards.pages.AbstractWizardPage;
import eu.cessar.ct.ecuc.workspace.ui.internal.wizards.pages.IPageTextProvider;

public class ExternalStandardModuleSelectionTexts implements IPageTextProvider
{

	public HashMap<String, String> getPageTexts()
	{
		HashMap<String, String> texts = new HashMap<String, String>();
		texts.put(AbstractWizardPage.PAGE_TITLE, Messages.ExternalStandardModuleDefinitionSelectionPage_Title);
		texts.put(AbstractWizardPage.PAGE_DESCRIPTION,
			Messages.ExternalStandardModuleDefinitionSelectionPage_Description);

		texts.put("label", Messages.ExternalStandardModuleDefinitionSelectionPage_Table_Label); //$NON-NLS-1$
		texts.put("ModuleNameColumn", //$NON-NLS-1$
			Messages.ExternalStandardModuleDefinitionSelectionPage_ModuleNameColumn);
		texts.put("ModuleFileColumn", //$NON-NLS-1$
			Messages.ExternalStandardModuleDefinitionSelectionPage_ModuleFileColumn);

		return texts;
	}

}
