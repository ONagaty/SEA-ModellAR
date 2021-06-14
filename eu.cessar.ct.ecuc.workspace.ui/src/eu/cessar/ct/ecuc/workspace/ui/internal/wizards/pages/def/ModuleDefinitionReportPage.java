package eu.cessar.ct.ecuc.workspace.ui.internal.wizards.pages.def;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import eu.cessar.ct.core.mms.MetaModelUtils;
import eu.cessar.ct.ecuc.workspace.ui.internal.wizards.ModuleDefinitionWizard;
import eu.cessar.ct.ecuc.workspace.ui.internal.wizards.pages.AbstractWizardPage;
import eu.cessar.ct.ecuc.workspace.ui.internal.wizards.pages.AbstractWizardReportPage;

public class ModuleDefinitionReportPage extends AbstractWizardReportPage
{
	protected Label project;
	protected Label modelVersion;
	protected Label moduleType;
	protected Label moduleDefinitionName;
	protected Label moduleDefinition;
	protected Label packageName;
	protected Label fileLocation;
	protected Label overwriteFile;
	protected Label appendToFile;

	public ModuleDefinitionReportPage()
	{
		super("ModuleConfigurationReportPage"); //$NON-NLS-1$
	}

	@Override
	protected void addControls(Composite container)
	{
		Label generalTitle = new Label(container, SWT.NONE);
		generalTitle.setData(AbstractWizardPage.CONTROL_ID, "generalTitle"); //$NON-NLS-1$
		setTitleStyle(generalTitle);

		Label projectLabel = new Label(container, SWT.NONE);
		projectLabel.setData(AbstractWizardPage.CONTROL_ID, "projectLabel"); //$NON-NLS-1$
		setLabelStyle(projectLabel);
		project = new Label(container, SWT.NONE);
		setTextStyle(project);

		Label modelVersionLabel = new Label(container, SWT.NONE);
		modelVersionLabel.setData(AbstractWizardPage.CONTROL_ID, "modelVersionLabel"); //$NON-NLS-1$
		setLabelStyle(modelVersionLabel);
		modelVersion = new Label(container, SWT.NONE);
		setTextStyle(modelVersion);

		Label moduleTypeLabel = new Label(container, SWT.NONE);
		moduleTypeLabel.setData(AbstractWizardPage.CONTROL_ID, "moduleTypeLabel"); //$NON-NLS-1$
		setLabelStyle(moduleTypeLabel);
		moduleType = new Label(container, SWT.NONE);
		setTextStyle(moduleType);

		Label reportTitle = new Label(container, SWT.NONE);
		reportTitle.setText(getWizard().getWindowTitle());
		setTitleStyle(reportTitle);

		Label moduleDefinitionNameLabel = new Label(container, SWT.NONE);
		moduleDefinitionNameLabel.setData(AbstractWizardPage.CONTROL_ID,
			"moduleDefinitionNameLabel"); //$NON-NLS-1$
		setLabelStyle(moduleDefinitionNameLabel);
		moduleDefinitionName = new Label(container, SWT.NONE);
		setTextStyle(moduleDefinitionName);

		Label moduleDefinitionLabel = new Label(container, SWT.NONE);
		moduleDefinitionLabel.setData(AbstractWizardPage.CONTROL_ID, "moduleDefinitionLabel"); //$NON-NLS-1$
		setLabelStyle(moduleDefinitionLabel);
		moduleDefinition = new Label(container, SWT.NONE);
		setTextStyle(moduleDefinition);

		Label packageNameLabel = new Label(container, SWT.NONE);
		packageNameLabel.setData(AbstractWizardPage.CONTROL_ID, "packageNameLabel"); //$NON-NLS-1$
		setLabelStyle(packageNameLabel);
		packageName = new Label(container, SWT.NONE);
		setTextStyle(packageName);

		Label fileLocationLabel = new Label(container, SWT.NONE);
		fileLocationLabel.setData(AbstractWizardPage.CONTROL_ID, "fileLocationLabel"); //$NON-NLS-1$
		setLabelStyle(fileLocationLabel);
		fileLocation = new Label(container, SWT.NONE);
		setTextStyle(fileLocation);

		Label overwriteFileLabel = new Label(container, SWT.NONE);
		overwriteFileLabel.setData(AbstractWizardPage.CONTROL_ID, "overwriteFileLabel"); //$NON-NLS-1$
		setLabelStyle(overwriteFileLabel);
		overwriteFile = new Label(container, SWT.NONE);
		setTextStyle(overwriteFile);

		Label appendToFileLabel = new Label(container, SWT.NONE);
		appendToFileLabel.setData(AbstractWizardPage.CONTROL_ID, "appendToFileLabel"); //$NON-NLS-1$
		setLabelStyle(appendToFileLabel);
		appendToFile = new Label(container, SWT.NONE);
		setTextStyle(appendToFile);
	}

	@Override
	public void setVisible(final boolean visible)
	{
		if (visible)
		{
			ModuleDefinitionWizard wizard = (ModuleDefinitionWizard) getWizard();

			// Feed the report with data from wizard
			setProject(wizard.getProject().getName());
			setModelVersion(MetaModelUtils.getAutosarRelease(wizard.getProject()).getName());
			setModuleType(wizard.getModuleTypeString());
			setModuleDefinitionName(wizard.getDestinationModuleName());
			setModuleDefinition((null == wizard.getModuleDefinition() ? "" //$NON-NLS-1$
				: MetaModelUtils.getAbsoluteQualifiedName(wizard.getModuleDefinition())));
			setPackageName(wizard.getDestinationPackageName());
			setFileLocation(wizard.getOutputFile().getFullPath().toString());
			setOverwriteFile(wizard.replaceFile());
			setAppendToFile(wizard.appendToFile());
		}
		super.setVisible(visible);
	}

	public void setProject(String project)
	{
		this.project.setText(project);
	}

	public void setModelVersion(String modelVersion)
	{
		this.modelVersion.setText(modelVersion);
	}

	public void setModuleType(String moduleType)
	{
		this.moduleType.setText(moduleType);
	}

	public void setModuleDefinitionName(String moduleDefinitionName)
	{
		this.moduleDefinitionName.setText(moduleDefinitionName);
	}

	public void setModuleDefinition(String moduleDefinition)
	{
		this.moduleDefinition.setText(moduleDefinition);
	}

	public void setPackageName(String packageName)
	{
		this.packageName.setText(packageName);
	}

	public void setFileLocation(String fileLocation)
	{
		this.fileLocation.setText(fileLocation);
	}

	public void setOverwriteFile(boolean overwriteFile)
	{
		this.overwriteFile.setText(new Boolean(overwriteFile).toString());
	}

	public void setAppendToFile(boolean appendToFile)
	{
		this.appendToFile.setText(new Boolean(appendToFile).toString());
	}

}
