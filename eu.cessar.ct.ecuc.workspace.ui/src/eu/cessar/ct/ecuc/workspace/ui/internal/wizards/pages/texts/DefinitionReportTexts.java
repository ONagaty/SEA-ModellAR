package eu.cessar.ct.ecuc.workspace.ui.internal.wizards.pages.texts;

import java.util.HashMap;

import eu.cessar.ct.ecuc.workspace.ui.internal.Messages;
import eu.cessar.ct.ecuc.workspace.ui.internal.wizards.pages.AbstractWizardPage;
import eu.cessar.ct.ecuc.workspace.ui.internal.wizards.pages.IPageTextProvider;

public class DefinitionReportTexts implements IPageTextProvider
{

	public HashMap<String, String> getPageTexts()
	{
		HashMap<String, String> texts = new HashMap<String, String>();
		texts.put(AbstractWizardPage.PAGE_TITLE, Messages.ReportPage_Title);
		texts.put(AbstractWizardPage.PAGE_DESCRIPTION, Messages.ReportPage_Description);

		texts.put("generalTitle", Messages.ReportPage_GeneralInformations); //$NON-NLS-1$
		texts.put("projectLabel", Messages.ModuleDefinitionReportPage_Project); //$NON-NLS-1$
		texts.put("modelVersionLabel", Messages.ModuleDefinitionReportPage_ModelVersion); //$NON-NLS-1$
		texts.put("moduleTypeLabel", Messages.ModuleDefinitionReportPage_ModuleType); //$NON-NLS-1$
		texts.put("moduleDefinitionNameLabel", Messages.ModuleDefinitionReportPage_ModuleDefName); //$NON-NLS-1$
		texts.put("moduleDefinitionLabel", Messages.ModuleDefinitionReportPage_ModuleDefinition); //$NON-NLS-1$
		texts.put("packageNameLabel", Messages.ModuleDefinitionReportPage_PackageName); //$NON-NLS-1$
		texts.put("fileLocationLabel", Messages.ModuleDefinitionReportPage_OutputFile); //$NON-NLS-1$
		texts.put("overwriteFileLabel", Messages.ModuleDefinitionReportPage_OverwriteFile); //$NON-NLS-1$
		texts.put("appendToFileLabel", Messages.ModuleDefinitionReportPage_AppendToFile); //$NON-NLS-1$

		return texts;
	}
}
