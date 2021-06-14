package eu.cessar.ct.ecuc.workspace.ui.internal.wizards.pages.texts;

import java.util.HashMap;

import eu.cessar.ct.ecuc.workspace.ui.internal.Messages;
import eu.cessar.ct.ecuc.workspace.ui.internal.wizards.pages.AbstractWizardPage;
import eu.cessar.ct.ecuc.workspace.ui.internal.wizards.pages.IPageTextProvider;

public class ConfigurationReportTexts implements IPageTextProvider
{

	public HashMap<String, String> getPageTexts()
	{
		HashMap<String, String> texts = new HashMap<String, String>();
		texts.put(AbstractWizardPage.PAGE_TITLE, Messages.ReportPage_Title);
		texts.put(AbstractWizardPage.PAGE_DESCRIPTION, Messages.ReportPage_Description);

		texts.put("generalTitle", Messages.ReportPage_GeneralInformations); //$NON-NLS-1$
		texts.put("projectLabel", Messages.ModuleConfigurationReportPage_Project); //$NON-NLS-1$
		texts.put("moduleDefinitionLabel", Messages.ModuleConfigurationReportPage_ModuleDefinition); //$NON-NLS-1$
		texts.put("moduleConfigurationLabel", Messages.ModuleConfigurationReportPage_ModuleConfName); //$NON-NLS-1$
		texts.put("packageNameLabel", Messages.ModuleConfigurationReportPage_PackageName); //$NON-NLS-1$
		texts.put("fileLocationLabel", Messages.ModuleConfigurationReportPage_OutputFile); //$NON-NLS-1$
		texts.put("createMandatoryLabel", Messages.ModuleConfigurationReportPage_CreateMandatory); //$NON-NLS-1$
		texts.put("createOptionalLabel", Messages.ModuleConfigurationReportPage_CreateOptional); //$NON-NLS-1$
		texts.put("createBasedOnUpperMultiplicityLabel", //$NON-NLS-1$
			Messages.ModuleConfigurationReportPage_CreateBasedOnUpperMultiplicity);
		texts.put("overwriteFileLabel", Messages.ModuleConfigurationReportPage_OverwriteFile); //$NON-NLS-1$
		texts.put("appendToFileLabel", Messages.ModuleConfigurationReportPage_AppendToFile); //$NON-NLS-1$

		return texts;
	}

}
