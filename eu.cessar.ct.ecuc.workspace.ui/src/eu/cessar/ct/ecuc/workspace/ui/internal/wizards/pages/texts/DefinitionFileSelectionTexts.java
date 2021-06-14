package eu.cessar.ct.ecuc.workspace.ui.internal.wizards.pages.texts;

import java.util.HashMap;

import eu.cessar.ct.ecuc.workspace.ui.internal.Messages;
import eu.cessar.ct.ecuc.workspace.ui.internal.wizards.pages.AbstractWizardPage;
import eu.cessar.ct.ecuc.workspace.ui.internal.wizards.pages.IPageTextProvider;

public class DefinitionFileSelectionTexts implements IPageTextProvider
{

	public HashMap<String, String> getPageTexts()
	{
		HashMap<String, String> texts = new HashMap<String, String>();
		texts.put(AbstractWizardPage.PAGE_TITLE, Messages.DefinitionFileSelectionPage_Title);
		texts.put(AbstractWizardPage.PAGE_DESCRIPTION,
			Messages.DefinitionFileSelectionPage_Description);

		texts.put("labelLocation", Messages.DefinitionFileSelectionPage_ModuleConfLocation); //$NON-NLS-1$
		texts.put("browseButton", Messages.DefinitionFileSelectionPage_btnBrowse); //$NON-NLS-1$
		texts.put("btnReplaceFile", Messages.DefinitionFileSelectionPage_btnReplaceExisting); //$NON-NLS-1$
		texts.put("btnOverwrite", Messages.DefinitionFileSelectionPage_radioOverwrite); //$NON-NLS-1$
		texts.put("btnAppend", Messages.DefinitionFileSelectionPage_radioAppend); //$NON-NLS-1$
		texts.put("labelFileExtension", Messages.DefinitionFileSelectionPage_EcuExtension); //$NON-NLS-1$

		return texts;
	}

}
