package eu.cessar.ct.ecuc.workspace.ui.internal.wizards.pages.texts;

import java.util.HashMap;

import eu.cessar.ct.ecuc.workspace.ui.internal.Messages;
import eu.cessar.ct.ecuc.workspace.ui.internal.wizards.pages.AbstractWizardPage;
import eu.cessar.ct.ecuc.workspace.ui.internal.wizards.pages.IPageTextProvider;

public class ModuleDefinitionInitializationTexts implements IPageTextProvider
{

	public HashMap<String, String> getPageTexts()
	{
		HashMap<String, String> texts = new HashMap<String, String>();
		texts.put(AbstractWizardPage.PAGE_TITLE, Messages.ModuleDefinitionInitializationPage_Title);
		texts.put(AbstractWizardPage.PAGE_DESCRIPTION,
			Messages.ModuleDefinitionInitializationPage_Description);

		texts.put("ModuleName", //$NON-NLS-1$
			Messages.ModuleDefinitionInitializationPage_ModuleDefinitionShortName);
		texts.put("ExistingPackagesName", //$NON-NLS-1$
			Messages.ModuleDefinitionInitializationPage_ExistingPackages);
		texts.put("createNewPackage", Messages.ModuleDefinitionInitializationPage_NewPackageName); //$NON-NLS-1$
		// texts.put("labelDescription",
		// Messages.ModuleDefinitionInitializationPage_NewPackageCreatedAsChild);

		return texts;
	}

}
