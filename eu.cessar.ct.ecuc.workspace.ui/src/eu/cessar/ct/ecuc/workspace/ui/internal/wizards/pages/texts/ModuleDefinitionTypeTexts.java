package eu.cessar.ct.ecuc.workspace.ui.internal.wizards.pages.texts;

import java.util.HashMap;

import eu.cessar.ct.ecuc.workspace.ui.internal.Messages;
import eu.cessar.ct.ecuc.workspace.ui.internal.wizards.pages.AbstractWizardPage;
import eu.cessar.ct.ecuc.workspace.ui.internal.wizards.pages.IPageTextProvider;

public class ModuleDefinitionTypeTexts implements IPageTextProvider
{

	public HashMap<String, String> getPageTexts()
	{
		HashMap<String, String> texts = new HashMap<String, String>();
		texts.put(AbstractWizardPage.PAGE_TITLE, Messages.ModuleDefinitionTypePage_Title);
		texts.put(AbstractWizardPage.PAGE_DESCRIPTION, Messages.ModuleDefinitionTypePage_Description);

		texts.put("btnImportStandard", Messages.ModuleDefinitionTypePage_radioImport); //$NON-NLS-1$
		texts.put("btnRefineModule", Messages.ModuleDefinitionTypePage_radioRefine); //$NON-NLS-1$
		texts.put("btnCreatePure", Messages.ModuleDefinitionTypePage_radioPure); //$NON-NLS-1$
		texts.put("btnImportExternalStandard", Messages.ModuleDefinitionTypePage_radioImportExternalStandard); //$NON-NLS-1$
		texts.put("btnBrowse", Messages.ModuleDefinitionTypePage_btnBrowse); //$NON-NLS-1$
		texts.put("txtLocation", Messages.ModuleDefinitionTypePage_ModuleConfLocation); //$NON-NLS-1$

		return texts;
	}

}
