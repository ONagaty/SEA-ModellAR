package eu.cessar.ct.ecuc.workspace.ui.internal.wizards;

import java.util.ArrayList;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.sphinx.platform.ui.util.ExtendedPlatformUI;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.undo.WorkspaceUndoUtil;

import eu.cessar.ct.core.mms.MetaModelUtils;
import eu.cessar.ct.core.platform.ui.util.PlatformUIUtils;
import eu.cessar.ct.ecuc.workspace.jobs.CreateModuleDefinitionJob;
import eu.cessar.ct.ecuc.workspace.jobs.ModuleDefinitionType;
import eu.cessar.ct.ecuc.workspace.ui.internal.Messages;
import eu.cessar.ct.ecuc.workspace.ui.internal.wizards.pages.IPageTextProvider;
import eu.cessar.ct.ecuc.workspace.ui.internal.wizards.pages.common.ModuleInitializationPage;
import eu.cessar.ct.ecuc.workspace.ui.internal.wizards.pages.common.ProjectSelectionPage;
import eu.cessar.ct.ecuc.workspace.ui.internal.wizards.pages.def.ExternalStandardModuleDefSelectionPage;
import eu.cessar.ct.ecuc.workspace.ui.internal.wizards.pages.def.ModuleDefinitionFileSelectionPage;
import eu.cessar.ct.ecuc.workspace.ui.internal.wizards.pages.def.ModuleDefinitionReportPage;
import eu.cessar.ct.ecuc.workspace.ui.internal.wizards.pages.def.ModuleDefinitionTypePage;
import eu.cessar.ct.ecuc.workspace.ui.internal.wizards.pages.def.RefinedModuleDefinitionSelectionPage;
import eu.cessar.ct.ecuc.workspace.ui.internal.wizards.pages.def.StandardModuleDefinitionSelectionPage;
import eu.cessar.ct.ecuc.workspace.ui.internal.wizards.pages.texts.DefinitionFileSelectionTexts;
import eu.cessar.ct.ecuc.workspace.ui.internal.wizards.pages.texts.DefinitionReportTexts;
import eu.cessar.ct.ecuc.workspace.ui.internal.wizards.pages.texts.ExternalStandardModuleSelectionTexts;
import eu.cessar.ct.ecuc.workspace.ui.internal.wizards.pages.texts.ModuleDefinitionInitializationTexts;
import eu.cessar.ct.ecuc.workspace.ui.internal.wizards.pages.texts.ModuleDefinitionTypeTexts;
import eu.cessar.ct.ecuc.workspace.ui.internal.wizards.pages.texts.ProjectSelectionTexts;
import eu.cessar.ct.ecuc.workspace.ui.internal.wizards.pages.texts.RefinedModuleSelectionTexts;
import eu.cessar.ct.ecuc.workspace.ui.internal.wizards.pages.texts.StandardModuleSelectionTexts;
import gautosar.gecucparameterdef.GModuleDef;

/**
 * The Class ModuleDefinitionWizard.
 */
public class ModuleDefinitionWizard extends AbstractWizard
{

	/** The autosar package name. */
	protected static String AUTOSAR_PACKAGE_NAME = "/AUTOSAR"; //$NON-NLS-1$

	/** The project selection page. */
	protected ProjectSelectionPage projectSelectionPage;

	/** The module definition type page. */
	protected ModuleDefinitionTypePage moduleDefinitionTypePage;

	/** The standard module definition selection page. */
	protected StandardModuleDefinitionSelectionPage standardModuleDefinitionSelectionPage;

	/** The external standard module definition selection page. */
	protected ExternalStandardModuleDefSelectionPage externalStandardModuleDefinitionSelectionPage;

	/** The refined module definition selection page. */
	protected RefinedModuleDefinitionSelectionPage refinedModuleDefinitionSelectionPage;

	/** The module definition initialization page. */
	protected ModuleInitializationPage moduleDefinitionInitializationPage;

	/** The definition file selection page. */
	protected ModuleDefinitionFileSelectionPage definitionFileSelectionPage;

	/** The report page. */
	protected ModuleDefinitionReportPage reportPage;

	/** The flag refined def. */
	private boolean flagRefinedDef = true;

	/** The default file location. */
	private IPath defaultFileLocation = null;

	private static final String NEW_BSW_MODULE_DEF_WIZARD = "eu.cessar.ct.workspace.ui.new_bsw_module_definitions_wizard_context";

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.ecuc.workspace.ui.internal.wizards.AbstractWizard#performFinish()
	 */
	@Override
	public boolean performFinish()
	{
		// Make wizard result information accessible for asynchronous operation
		final IFile outputFile = getOutputFile();

		CreateModuleDefinitionJob job = new CreateModuleDefinitionJob(getProject(), outputFile, replaceFile(),
			appendToFile());
		job.setModuleDefinitionType(getModuleDefinitionType());
		job.setModuleDefinitionName(getDestinationModuleName());
		job.setModuleDefinition(getModuleDefinition());
		job.setDestinationPackageQName(getDestinationPackageName());

		job.setUiInfoAdaptable(WorkspaceUndoUtil.getUIInfoAdapter(getShell()));

		// reveal the file after creation
		job.addJobChangeListener(new JobChangeAdapter()
		{
			@Override
			public void done(IJobChangeEvent event)
			{
				standardModuleDefinitionSelectionPage.unloadStandardModuleDefResource();

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
	 * @see org.eclipse.jface.wizard.Wizard#performCancel()
	 */
	@Override
	public boolean performCancel()
	{
		switch (getModuleDefinitionType())
		{
			case STANDARD:
				standardModuleDefinitionSelectionPage.unloadStandardModuleDefResource();
				break;

			case REFINED:
				break;

			case PURE_VENDOR:
				break;

			case EXTERNAL_STANDARD:
				externalStandardModuleDefinitionSelectionPage.unloadStandardModuleDefResource();
				break;

			default:
				return super.performCancel();
		}
		return super.performCancel();
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

		PlatformUI.getWorkbench().getHelpSystem().setHelp(getShell(), NEW_BSW_MODULE_DEF_WIZARD);

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.ecuc.workspace.ui.internal.wizards.AbstractWizard#getWizardPages(org.eclipse.ui.IWorkbench,
	 * org.eclipse.jface.viewers.IStructuredSelection)
	 */
	@Override
	protected ArrayList<WizardPage> getWizardPages(IWorkbench workbench, IStructuredSelection selection)
	{
		pages = new ArrayList<WizardPage>();

		projectSelectionPage = new ProjectSelectionPage(selection);
		pages.add(projectSelectionPage);

		moduleDefinitionTypePage = new ModuleDefinitionTypePage();
		pages.add(moduleDefinitionTypePage);

		standardModuleDefinitionSelectionPage = new StandardModuleDefinitionSelectionPage(selection);
		pages.add(standardModuleDefinitionSelectionPage);

		externalStandardModuleDefinitionSelectionPage = new ExternalStandardModuleDefSelectionPage(selection);
		pages.add(externalStandardModuleDefinitionSelectionPage);

		refinedModuleDefinitionSelectionPage = new RefinedModuleDefinitionSelectionPage(selection);
		pages.add(refinedModuleDefinitionSelectionPage);

		moduleDefinitionInitializationPage = new ModuleInitializationPage(selection);
		pages.add(moduleDefinitionInitializationPage);

		definitionFileSelectionPage = new ModuleDefinitionFileSelectionPage(selection);
		pages.add(definitionFileSelectionPage);

		reportPage = new ModuleDefinitionReportPage();
		pages.add(reportPage);

		return pages;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.ecuc.workspace.ui.internal.wizards.AbstractWizard#canFinish()
	 */
	@Override
	public final boolean canFinish()
	{
		boolean result = true;
		result &= projectSelectionPage.isPageComplete();
		result &= moduleDefinitionTypePage.isPageComplete();

		if (result)
		{
			switch (getModuleDefinitionType())
			{
				case STANDARD:
					result &= standardModuleDefinitionSelectionPage.isPageComplete();
					break;

				case REFINED:
					result &= moduleDefinitionInitializationPage.isPageComplete();
					result &= refinedModuleDefinitionSelectionPage.isPageComplete();
					break;

				case PURE_VENDOR:
					// Do nothing
					result &= moduleDefinitionInitializationPage.checkForDuplicateModules();
					break;

				case EXTERNAL_STANDARD:
					result &= externalStandardModuleDefinitionSelectionPage.isPageComplete();
					break;

				default:
					Assert.isTrue(false, "Invalid module definition type !"); //$NON-NLS-1$
					return false;
			}
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.wizard.Wizard#getNextPage(org.eclipse.jface.wizard.IWizardPage)
	 */
	@Override
	public IWizardPage getNextPage(IWizardPage page)
	{
		if (page == moduleDefinitionInitializationPage)
		{
			moduleDefinitionInitializationPage.getTxtModuleName().setEnabled(flagRefinedDef);

			definitionFileSelectionPage.resetTxtLocation();

			return definitionFileSelectionPage;
		}
		if (page == moduleDefinitionTypePage)
		{
			switch (getModuleDefinitionType())
			{
				case STANDARD:
					standardModuleDefinitionSelectionPage.setModuleDefinitionType(getModuleDefinitionType());
					return standardModuleDefinitionSelectionPage;

				case REFINED:
					flagRefinedDef = false;
					return refinedModuleDefinitionSelectionPage;

				case PURE_VENDOR:
					flagRefinedDef = true;
					return moduleDefinitionInitializationPage;

				case EXTERNAL_STANDARD:
					String externalStandardImportFilePath = moduleDefinitionTypePage.getExternalStandardImportFilePath();

					if ((externalStandardImportFilePath != null) && (!externalStandardImportFilePath.isEmpty()))
					{
						externalStandardModuleDefinitionSelectionPage.setExternalStandardImportFilePath(externalStandardImportFilePath);
					}
					externalStandardModuleDefinitionSelectionPage.setModuleDefinitionType(getModuleDefinitionType());

					return externalStandardModuleDefinitionSelectionPage;

				default:
					Assert.isTrue(false, "Invalid module definition type !"); //$NON-NLS-1$
					return null;
			}
		}
		if (page == standardModuleDefinitionSelectionPage)
		{
			definitionFileSelectionPage.resetTxtLocation();

			return definitionFileSelectionPage;
		}
		if (page == externalStandardModuleDefinitionSelectionPage)
		{
			definitionFileSelectionPage.resetTxtLocation();

			return definitionFileSelectionPage;
		}
		if (page == refinedModuleDefinitionSelectionPage)
		{
			moduleDefinitionInitializationPage.getTxtModuleName().setEnabled(flagRefinedDef);
			return moduleDefinitionInitializationPage;
		}
		return super.getNextPage(page);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.wizard.Wizard#getPreviousPage(org.eclipse.jface.wizard.IWizardPage)
	 */
	@Override
	public IWizardPage getPreviousPage(IWizardPage page)
	{
		if (page == moduleDefinitionInitializationPage)
		{
			switch (getModuleDefinitionType())
			{
				case STANDARD:
					Assert.isTrue(false, "Should not get here!"); //$NON-NLS-1$
					return null;

				case REFINED:
					return refinedModuleDefinitionSelectionPage;

				case PURE_VENDOR:
					return moduleDefinitionTypePage;

				case EXTERNAL_STANDARD:
					Assert.isTrue(false, "Should not get here!"); //$NON-NLS-1$
					return null;

				default:
					Assert.isTrue(false, "Invalid module definition type !"); //$NON-NLS-1$
					return null;
			}
		}
		if (page == definitionFileSelectionPage)
		{
			// Must no longer allow Finish if the user goes back, even if we
			// have a name for the file
			switch (getModuleDefinitionType())
			{
				case STANDARD:
					return standardModuleDefinitionSelectionPage;

				case REFINED:

				case PURE_VENDOR:
					return moduleDefinitionInitializationPage;

				case EXTERNAL_STANDARD:
					return externalStandardModuleDefinitionSelectionPage;

				default:
					Assert.isTrue(false, "Invalid module definition type !"); //$NON-NLS-1$
					return null;
			}
		}
		if (page == standardModuleDefinitionSelectionPage)
		{
			return moduleDefinitionTypePage;
		}
		if (page == externalStandardModuleDefinitionSelectionPage)
		{
			return moduleDefinitionTypePage;
		}
		if (page == refinedModuleDefinitionSelectionPage)
		{
			return moduleDefinitionTypePage;
		}

		return super.getPreviousPage(page);
	}

	/**
	 * Gets the module type string.
	 * 
	 * @return the module type string
	 */
	public String getModuleTypeString()
	{
		switch (getModuleDefinitionType())
		{
			case STANDARD:
				return Messages.ModuleDefinitionTypePage_radioImport;

			case REFINED:
				return Messages.ModuleDefinitionTypePage_radioRefine;

			case PURE_VENDOR:
				return Messages.ModuleDefinitionTypePage_radioPure;

			case EXTERNAL_STANDARD:
				return Messages.ModuleDefinitionTypePage_radioImportExternalStandard;

			default:
				Assert.isTrue(false, "Invalid module definition type !"); //$NON-NLS-1$
				return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.cessar.ct.ecuc.workspace.ui.internal.wizards.AbstractWizard#getTextProvider(org.eclipse.jface.wizard.WizardPage
	 * )
	 */
	@Override
	public IPageTextProvider getTextProvider(WizardPage page)
	{
		if (page == projectSelectionPage)
		{
			return new ProjectSelectionTexts(true);
		}
		if (page == moduleDefinitionTypePage)
		{
			return new ModuleDefinitionTypeTexts();
		}
		if (page == standardModuleDefinitionSelectionPage)
		{
			return new StandardModuleSelectionTexts();
		}
		if (page == externalStandardModuleDefinitionSelectionPage)
		{
			return new ExternalStandardModuleSelectionTexts();
		}
		if (page == refinedModuleDefinitionSelectionPage)
		{
			return new RefinedModuleSelectionTexts();
		}
		if (page == moduleDefinitionInitializationPage)
		{
			return new ModuleDefinitionInitializationTexts();
		}
		if (page == definitionFileSelectionPage)
		{
			return new DefinitionFileSelectionTexts();
		}
		if (page == reportPage)
		{
			return new DefinitionReportTexts();
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.ecuc.workspace.ui.internal.wizards.AbstractWizard#getWindowTitle()
	 */
	@Override
	public String getWindowTitle()
	{
		return Messages.ModuleDefinitionWizard_Title;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.ecuc.workspace.ui.internal.wizards.AbstractWizard#getProject()
	 */
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

	/**
	 * Gets the module definition type.
	 * 
	 * @return the module definition type
	 */
	public ModuleDefinitionType getModuleDefinitionType()
	{
		return moduleDefinitionTypePage.getModuleDefinitionType();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.ecuc.workspace.ui.internal.wizards.AbstractWizard#getModuleDefinition()
	 */
	@Override
	public GModuleDef getModuleDefinition()
	{
		switch (getModuleDefinitionType())
		{
			case STANDARD:
				return standardModuleDefinitionSelectionPage.getSelectedModuleDefinition();

			case REFINED:
				return refinedModuleDefinitionSelectionPage.getSelectedModuleDefinition();

			case PURE_VENDOR:
				// Do nothing
				break;

			case EXTERNAL_STANDARD:
				return externalStandardModuleDefinitionSelectionPage.getSelectedModuleDefinition();

			default:
				Assert.isTrue(false, "Invalid module definition type !"); //$NON-NLS-1$
				return null;
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.ecuc.workspace.ui.internal.wizards.AbstractWizard#getDestinationModuleName()
	 */
	@Override
	public String getDestinationModuleName()
	{
		// if (moduleDefinitionInitializationPage.isPageComplete())
		// {
		// return moduleDefinitionInitializationPage.getModuleName();
		// }
		return getDefaultDestinationModuleName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.ecuc.workspace.ui.internal.wizards.AbstractWizard#getDefaultDestinationModuleName()
	 */
	@Override
	public String getDefaultDestinationModuleName()
	{
		String moduleNamePrefix = ""; //$NON-NLS-1$

		switch (getModuleDefinitionType())
		{
			case STANDARD:
				return getModuleDefinition().gGetShortName();

			case REFINED:
				moduleNamePrefix = Messages.ModuleDefinitionInitializationPage_RefinedModuleNamePrefix;
				break;

			case PURE_VENDOR:
				if (moduleDefinitionInitializationPage.getModuleName() == null)
				{
					moduleNamePrefix = Messages.ModuleDefinitionInitializationPage_PureVendorModuleNamePrefix;
				}
				else
				{
					moduleNamePrefix = moduleDefinitionInitializationPage.getModuleName();
				}
				break;

			case EXTERNAL_STANDARD:
				return getModuleDefinition().gGetShortName();

			default:
				Assert.isTrue(false, "Invalid module definition type !"); //$NON-NLS-1$
				return null;
		}
		return moduleNamePrefix + (null == getModuleDefinition() ? "" : getModuleDefinition().gGetShortName()); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.ecuc.workspace.ui.internal.wizards.AbstractWizard#getDestinationPackageName()
	 */
	@Override
	public String getDestinationPackageName()
	{
		if (moduleDefinitionInitializationPage.isPageComplete())
		{
			return moduleDefinitionInitializationPage.getPackageName();
		}
		return getDefaultDestinationPackageName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.ecuc.workspace.ui.internal.wizards.AbstractWizard#getDefaultDestinationPackageName()
	 */
	@Override
	public String getDefaultDestinationPackageName()
	{
		GModuleDef selectedModuleDefinition = null;

		switch (getModuleDefinitionType())
		{
			case STANDARD:
				selectedModuleDefinition = standardModuleDefinitionSelectionPage.getSelectedModuleDefinition();
				return MetaModelUtils.getAbsoluteQualifiedName(selectedModuleDefinition.eContainer());

			case REFINED:
				break;

			case PURE_VENDOR:
				break;

			case EXTERNAL_STANDARD:
				selectedModuleDefinition = externalStandardModuleDefinitionSelectionPage.getSelectedModuleDefinition();
				return MetaModelUtils.getAbsoluteQualifiedName(selectedModuleDefinition.eContainer());

			default:
				Assert.isTrue(false, "Invalid module definition type !"); //$NON-NLS-1$
				return null;
		}
		return Messages.ModuleDefinitionInitializationPage_DefaultPackageName;
	}

	/**
	 * Gets the default file location.
	 * 
	 * @return the default file location
	 */
	public IPath getDefaultFileLocation()
	{
		return defaultFileLocation;
	}

	/**
	 * Gets the output file.
	 * 
	 * @return the output file
	 */
	public IFile getOutputFile()
	{
		if (definitionFileSelectionPage.isPageComplete())
		{
			return definitionFileSelectionPage.getSelectedFile();
		}
		return getDefaultOutputFile();
	}

	/**
	 * Replace file.
	 * 
	 * @return true, if successful
	 */
	public boolean replaceFile()
	{
		return definitionFileSelectionPage.replaceFile();
	}

	/**
	 * Append to file.
	 * 
	 * @return true, if successful
	 */
	public boolean appendToFile()
	{
		return definitionFileSelectionPage.appendToFile();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.ecuc.workspace.ui.internal.wizards.AbstractWizard#getDefaultFileExtension()
	 */
	@Override
	protected String getDefaultFileExtension()
	{
		// return Messages.DefinitionFileSelectionPage_EcuExtension;
		return getBswmdExtension();
	}
}