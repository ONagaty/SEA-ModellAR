package eu.cessar.ct.ecuc.workspace.ui.internal.wizards.pages.config;

import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import eu.cessar.ct.core.mms.MetaModelUtils;
import eu.cessar.ct.ecuc.workspace.ui.internal.Messages;
import eu.cessar.ct.ecuc.workspace.ui.internal.wizards.ModuleConfigurationWizard;
import eu.cessar.ct.ecuc.workspace.ui.internal.wizards.pages.AbstractWizardPage;
import eu.cessar.ct.ecuc.workspace.ui.internal.wizards.pages.AbstractWizardReportPage;

public class ModuleConfigurationReportPage extends AbstractWizardReportPage
{
	protected Label project;
	protected Label moduleDefinition;
	protected Label moduleConfiguration;
	protected Label packageName;
	protected Label fileLocation;
	protected Label createMandatory;
	protected Label createOptional;
	protected Label maximumElements;
	protected Label overwriteFile;
	protected Label appendToFile;

	public ModuleConfigurationReportPage()
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

		Label moduleDefinitionLabel = new Label(container, SWT.NONE);
		moduleDefinitionLabel.setData(AbstractWizardPage.CONTROL_ID, "moduleDefinitionLabel"); //$NON-NLS-1$
		setLabelStyle(moduleDefinitionLabel);
		moduleDefinition = new Label(container, SWT.NONE);
		setTextStyle(moduleDefinition);

		Label reportTitle = new Label(container, SWT.NONE);
		reportTitle.setText(getWizard().getWindowTitle());
		setTitleStyle(reportTitle);

		Label moduleConfigurationLabel = new Label(container, SWT.NONE);
		moduleConfigurationLabel.setData(AbstractWizardPage.CONTROL_ID, "moduleConfigurationLabel"); //$NON-NLS-1$
		setLabelStyle(moduleConfigurationLabel);
		moduleConfiguration = new Label(container, SWT.NONE);
		setTextStyle(moduleConfiguration);

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

		Label createMandatoryLabel = new Label(container, SWT.NONE);
		createMandatoryLabel.setData(AbstractWizardPage.CONTROL_ID, "createMandatoryLabel"); //$NON-NLS-1$
		setLabelStyle(createMandatoryLabel);
		createMandatory = new Label(container, SWT.NONE);
		setTextStyle(createMandatory);

		Label createOptionalLabel = new Label(container, SWT.NONE);
		createOptionalLabel.setData(AbstractWizardPage.CONTROL_ID, "createOptionalLabel"); //$NON-NLS-1$
		setLabelStyle(createOptionalLabel);
		createOptional = new Label(container, SWT.NONE);
		setTextStyle(createOptional);

		Label createBasedOnUpperMultiplicityLabel = new Label(container, SWT.NONE);
		createBasedOnUpperMultiplicityLabel.setData(AbstractWizardPage.CONTROL_ID,
			"createBasedOnUpperMultiplicityLabel"); //$NON-NLS-1$
		setLabelStyle(createBasedOnUpperMultiplicityLabel);
		maximumElements = new Label(container, SWT.NONE);
		setTextStyle(maximumElements);

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
			ModuleConfigurationWizard wizard = (ModuleConfigurationWizard) getWizard();

			setProject(wizard.getProject().getName());
			setModuleDefinition(MetaModelUtils.getAbsoluteQualifiedName(wizard.getModuleDefinition()));
			setModuleConfiguration(wizard.getDestinationModuleName());
			setPackageName(wizard.getDestinationPackageName());
			setFileLocation(wizard.getOutputFile().getFullPath().toString());
			setCreateMandatory(wizard.getCreateMandatory());
			setCreateOptional(wizard.getCreateOptional());
			setMaximumElements(wizard.getMaximumElementsCount());
			setOverwriteFile(wizard.replaceFile());
			setAppendToFile(wizard.appendToFile());
		}
		super.setVisible(visible);
	}

	public void setProject(String project)
	{
		this.project.setText(project);
	}

	public void setModuleDefinition(String moduleDefinition)
	{
		this.moduleDefinition.setText(moduleDefinition);
	}

	public void setModuleConfiguration(String moduleConfiguration)
	{
		this.moduleConfiguration.setText(moduleConfiguration);
	}

	public void setPackageName(String packageName)
	{
		this.packageName.setText(packageName);
	}

	public void setFileLocation(String fileLocation)
	{
		this.fileLocation.setText(fileLocation);
	}

	public void setCreateMandatory(boolean create)
	{
		this.createMandatory.setText(String.valueOf(create));
	}

	public void setCreateOptional(boolean create)
	{
		this.createOptional.setText(String.valueOf(create));
	}

	public void setMaximumElements(int maximumElements)
	{
		this.maximumElements.setText(NLS.bind(
			Messages.ModuleConfigurationReportPage_MaximumElements, String.valueOf(maximumElements)));
	}

	public void setOverwriteFile(boolean overwriteFile)
	{
		this.overwriteFile.setText(String.valueOf(overwriteFile));
	}

	public void setAppendToFile(boolean appendToFile)
	{
		this.appendToFile.setText(String.valueOf(appendToFile));
	}

}
