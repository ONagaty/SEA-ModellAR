package eu.cessar.ct.ecuc.workspace.ui.internal.wizards.pages.texts;

import java.util.HashMap;

import eu.cessar.ct.ecuc.workspace.ui.internal.Messages;
import eu.cessar.ct.ecuc.workspace.ui.internal.wizards.pages.AbstractWizardPage;
import eu.cessar.ct.ecuc.workspace.ui.internal.wizards.pages.IPageTextProvider;

public class ModuleConfigurationInitializationTexts implements IPageTextProvider
{

	public HashMap<String, String> getPageTexts()
	{
		HashMap<String, String> texts = new HashMap<String, String>();
		texts.put(AbstractWizardPage.PAGE_TITLE,
			Messages.ModuleConfigurationInitializationPage_Title);
		texts.put(AbstractWizardPage.PAGE_DESCRIPTION,
			Messages.ModuleConfigurationInitializationPage_Description);

		texts.put("ModuleName", //$NON-NLS-1$
			Messages.ModuleConfigurationInitializationPage_ModuleConfigurationShortName);
		texts.put("ExistingPackagesName", //$NON-NLS-1$
			Messages.ModuleConfigurationInitializationPage_ExistingPackages);
		texts.put("createNewPackage", Messages.ModuleConfigurationInitializationPage_NewPackageName); //$NON-NLS-1$
		// texts.put("labelDescription",
		// Messages.ModuleConfigurationInitializationPage_NewPackageCreatedAsChild);

		return texts;
	}

}
