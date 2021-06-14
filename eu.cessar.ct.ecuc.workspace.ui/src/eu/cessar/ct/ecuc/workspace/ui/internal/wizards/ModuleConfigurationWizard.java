package eu.cessar.ct.ecuc.workspace.ui.internal.wizards;

import java.util.ArrayList;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.sphinx.platform.ui.util.ExtendedPlatformUI;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.undo.WorkspaceUndoUtil;

import eu.cessar.ct.core.platform.ui.util.PlatformUIUtils;
import eu.cessar.ct.ecuc.workspace.jobs.CreateModuleConfigurationJob;
import eu.cessar.ct.ecuc.workspace.jobs.EcucJobsUtils;
import eu.cessar.ct.ecuc.workspace.ui.internal.Messages;
import eu.cessar.ct.ecuc.workspace.ui.internal.wizards.pages.IPageTextProvider;
import eu.cessar.ct.ecuc.workspace.ui.internal.wizards.pages.common.ModuleInitializationPage;
import eu.cessar.ct.ecuc.workspace.ui.internal.wizards.pages.common.ProjectSelectionPage;
import eu.cessar.ct.ecuc.workspace.ui.internal.wizards.pages.config.ModuleConfigurationDefSelectionPage;
import eu.cessar.ct.ecuc.workspace.ui.internal.wizards.pages.config.ModuleConfigurationFileSelectionPage;
import eu.cessar.ct.ecuc.workspace.ui.internal.wizards.pages.config.ModuleConfigurationReportPage;
import eu.cessar.ct.ecuc.workspace.ui.internal.wizards.pages.texts.ConfigurationFileSelectionTexts;
import eu.cessar.ct.ecuc.workspace.ui.internal.wizards.pages.texts.ConfigurationReportTexts;
import eu.cessar.ct.ecuc.workspace.ui.internal.wizards.pages.texts.ModuleConfigurationInitializationTexts;
import eu.cessar.ct.ecuc.workspace.ui.internal.wizards.pages.texts.ModuleDefinitionSelectionTexts;
import eu.cessar.ct.ecuc.workspace.ui.internal.wizards.pages.texts.ProjectSelectionTexts;
import gautosar.gecucparameterdef.GModuleDef;

/**
 * 
 * Wizard for creating a new Module Configuration.
 * 
 * @author uidl7321
 * 
 *         %created_by: uidr0537 %
 * 
 *         %date_created: Wed Jun 17 13:27:39 2015 %
 * 
 *         %version: 11 %
 */
public class ModuleConfigurationWizard extends AbstractWizard
{
	@SuppressWarnings("javadoc")
	protected ProjectSelectionPage projectSelectionPage;
	@SuppressWarnings("javadoc")
	protected ModuleConfigurationDefSelectionPage moduleDefinitionSelectionPage;
	@SuppressWarnings("javadoc")
	protected ModuleInitializationPage moduleConfigurationInitializationPage;
	@SuppressWarnings("javadoc")
	protected ModuleConfigurationFileSelectionPage configurationFileSelectionPage;
	@SuppressWarnings("javadoc")
	protected ModuleConfigurationReportPage reportPage;

	private static final String NEW_MODULE_CONFIG_WIZARD = "eu.cessar.ct.workspace.ui.new_module_configuration_wizard_context";
	/**
	 * Job Status
	 */
	protected IStatus jobStatus;

	@Override
	public boolean performFinish()
	{
		// Make wizard result information accessible for asynchronous operation
		final IFile outputFile = getOutputFile();

		CreateModuleConfigurationJob job = new CreateModuleConfigurationJob(getProject(), outputFile, replaceFile(),
			appendToFile());
		job.setModuleDefinition(getModuleDefinition());
		job.setDestinationPackageQName(getDestinationPackageName());
		job.setModuleConfigurationName(getDestinationModuleName());
		job.setCreateMandatoryElements(getCreateMandatory());
		job.setCreateOptionalElements(getCreateOptional());
		job.setCreateBasedOnUpperMultiplicity(getCreateBasedOnUpperMultiplicity(), getMaximumElementsCount());
		job.setUiInfoAdaptable(WorkspaceUndoUtil.getUIInfoAdapter(getShell()));

		// reveal the file after creation
		job.addJobChangeListener(new JobChangeAdapter()
		{
			@Override
			public void done(IJobChangeEvent event)
			{
				if (event.getResult() != null && event.getResult().isOK())
				{
					// Select the newly created file in all parts of the active
					// workbench window's active page
					Display display = ExtendedPlatformUI.getDisplay();
					if (display != null)
					{
						display.asyncExec(new Runnable()
						{
							public void run()
							{
								selectAndReveal(outputFile, PlatformUI.getWorkbench().getActiveWorkbenchWindow());
								PlatformUIUtils.openEditor(outputFile);
							}
						});
					}
				}
			}
		});
		job.schedule();
		return true;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jface.wizard.Wizard#createPageControls(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void createPageControls(Composite pageContainer)
	{
		super.createPageControls(pageContainer);

		PlatformUI.getWorkbench().getHelpSystem().setHelp(getShell(), NEW_MODULE_CONFIG_WIZARD);


	}

	@Override
	protected ArrayList<WizardPage> getWizardPages(IWorkbench workbench, IStructuredSelection selection)
	{
		// Extract the default location of the file to be created, based on
		// selection.
		pages = new ArrayList<WizardPage>();

		projectSelectionPage = new ProjectSelectionPage(selection);
		pages.add(projectSelectionPage);

		moduleDefinitionSelectionPage = new ModuleConfigurationDefSelectionPage(selection);
		pages.add(moduleDefinitionSelectionPage);

		moduleConfigurationInitializationPage = new ModuleInitializationPage(selection);
		pages.add(moduleConfigurationInitializationPage);

		configurationFileSelectionPage = new ModuleConfigurationFileSelectionPage(selection);
		pages.add(configurationFileSelectionPage);

		reportPage = new ModuleConfigurationReportPage();
		pages.add(reportPage);

		return pages;
	}

	@Override
	public final boolean canFinish()
	{
		boolean result = true;
		result &= projectSelectionPage.isPageComplete();
		result &= moduleDefinitionSelectionPage.isPageComplete();
		return result;
	}

	@Override
	public IPageTextProvider getTextProvider(WizardPage page)
	{
		if (page == projectSelectionPage)
		{
			return new ProjectSelectionTexts(false);
		}
		if (page == moduleDefinitionSelectionPage)
		{
			return new ModuleDefinitionSelectionTexts();
		}
		if (page == moduleConfigurationInitializationPage)
		{
			return new ModuleConfigurationInitializationTexts();
		}
		if (page == configurationFileSelectionPage)
		{
			return new ConfigurationFileSelectionTexts();
		}
		if (page == reportPage)
		{
			return new ConfigurationReportTexts();
		}
		return null;
	}

	@Override
	public String getWindowTitle()
	{
		return Messages.ModuleConfigurationWizard_Title;
	}

	@Override
	public IProject getProject()
	{
		IProject currentProject = projectSelectionPage.getSelectedProject();
		if (null == project || (!project.equals(currentProject)))
		{
			loadProject(currentProject);
		}
		project = currentProject;
		return project;
	}

	@Override
	public GModuleDef getModuleDefinition()
	{
		return moduleDefinitionSelectionPage.getSelectedModuleDefinition();
	}

	@Override
	public String getDestinationModuleName()
	{
		if (moduleConfigurationInitializationPage.isPageComplete())
		{
			return moduleConfigurationInitializationPage.getModuleName();
		}
		return getDefaultDestinationModuleName();
	}

	@Override
	public String getDefaultDestinationModuleName()
	{
		String moduleDefName = ""; //$NON-NLS-1$
		GModuleDef moduleDefinition = getModuleDefinition();
		if (null != moduleDefinition)
		{
			moduleDefName = moduleDefinition.gGetShortName();
		}
		return EcucJobsUtils.getDefaultDestinationModuleName(moduleDefName);
	}

	@Override
	public String getDestinationPackageName()
	{
		if (moduleConfigurationInitializationPage.isPageComplete())
		{
			return moduleConfigurationInitializationPage.getPackageName();
		}
		return getDefaultDestinationPackageName();
	}

	@Override
	public String getDefaultDestinationPackageName()
	{
		return EcucJobsUtils.getDefaultDestinationPackageName();
	}

	/**
	 * Returns the selected file from the wizard's page.
	 * 
	 * @return the IFile
	 */
	public IFile getOutputFile()
	{
		if (configurationFileSelectionPage.isPageComplete())
		{
			return configurationFileSelectionPage.getSelectedFile();
		}

		return getDefaultOutputFile();
	}

	/**
	 * Returns the value of the option to create mandatory elements inside the module configuration.
	 * 
	 * @return the value of the option
	 */
	public boolean getCreateMandatory()
	{
		return configurationFileSelectionPage.getCreateMandatory();
	}

	/**
	 * Returns the value of the option to create optional elements inside the module configuration.
	 * 
	 * @return the value of the option
	 */
	public boolean getCreateOptional()
	{
		return configurationFileSelectionPage.getCreateOptional();
	}

	/**
	 * Returns the value of the option to create optional elements inside the module configuration, based on the upper
	 * multiplicity.
	 * 
	 * @return the value of the option
	 */
	public boolean getCreateBasedOnUpperMultiplicity()
	{
		return configurationFileSelectionPage.getCreateBasedOnUpperMultiplicity();
	}

	/**
	 * Returns the maximum optional elements to create inside a module configuration, based on the upper multiplicity.
	 * 
	 * @return the value of the option
	 */

	public int getMaximumElementsCount()
	{
		return configurationFileSelectionPage.getMaximumElementsCount();
	}

	/**
	 * Returns whether to replace the file containing an already existing module configuration.
	 * 
	 * @return the value of the option
	 */
	public boolean replaceFile()
	{
		return configurationFileSelectionPage.replaceFile();
	}

	/**
	 * Returns whether to append to the file containing an already existing module configuration.
	 * 
	 * @return the value of the option
	 */
	public boolean appendToFile()
	{
		return configurationFileSelectionPage.appendToFile();
	}

	@Override
	protected String getDefaultFileExtension()
	{
		// return Messages.ConfigurationFileSelectionPage_EcuExtension;
		return getEcucExtension();
	}
}
