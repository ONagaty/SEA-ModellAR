package eu.cessar.ct.ecuc.workspace.ui.internal.wizards.pages.texts;

import java.util.HashMap;

import eu.cessar.ct.ecuc.workspace.ui.internal.Messages;
import eu.cessar.ct.ecuc.workspace.ui.internal.wizards.pages.AbstractWizardPage;
import eu.cessar.ct.ecuc.workspace.ui.internal.wizards.pages.IPageTextProvider;

public class ConfigurationFileSelectionTexts implements IPageTextProvider
{

	public HashMap<String, String> getPageTexts()
	{
		HashMap<String, String> texts = new HashMap<String, String>();
		texts.put(AbstractWizardPage.PAGE_TITLE, Messages.ConfigurationFileSelectionPage_Title);
		texts.put(AbstractWizardPage.PAGE_DESCRIPTION,
			Messages.ConfigurationFileSelectionPage_Description);

		texts.put("labelLocation", Messages.ConfigurationFileSelectionPage_ModuleConfLocation); //$NON-NLS-1$
		texts.put("browseButton", Messages.ConfigurationFileSelectionPage_btnBrowse); //$NON-NLS-1$
		texts.put("btnCreateMandatoryElements", //$NON-NLS-1$
			Messages.ConfigurationFileSelectionPage_btnCreateMandatoryElements);
		texts.put("btnCreateOptionalElements", //$NON-NLS-1$
			Messages.ConfigurationFileSelectionPage_btnCreateOptionalElements);
		texts.put("btnUseUpperMultiplicity", //$NON-NLS-1$
			Messages.ConfigurationFileSelectionPage_btnUseUpperMultiplicity);
		texts.put("labelElements", //$NON-NLS-1$
			Messages.ConfigurationFileSelectionPage_UpperMultiplicityElements);
		texts.put("btnReplaceFile", Messages.ConfigurationFileSelectionPage_btnReplaceExisting); //$NON-NLS-1$
		texts.put("btnOverwrite", Messages.ConfigurationFileSelectionPage_radioOverwrite); //$NON-NLS-1$
		texts.put("btnAppend", Messages.ConfigurationFileSelectionPage_radioAppend); //$NON-NLS-1$
		texts.put("labelFileExtension", Messages.ConfigurationFileSelectionPage_EcuExtension); //$NON-NLS-1$

		return texts;
	}

}
