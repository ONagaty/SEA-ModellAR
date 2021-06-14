package eu.cessar.ct.ecuc.workspace.ui.internal.wizards;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.osgi.util.NLS;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWizard;

import eu.cessar.ct.core.platform.util.PlatformUtils;
import eu.cessar.ct.ecuc.workspace.jobs.ModuleDefinitionType;
import eu.cessar.ct.ecuc.workspace.ui.internal.Messages;
import eu.cessar.ct.ecuc.workspace.ui.internal.wizards.pages.conversion.ConversionTypePage;
import eu.cessar.ct.ecuc.workspace.ui.internal.wizards.pages.conversion.RefinedModuleConfigSelectionPage;
import eu.cessar.ct.ecuc.workspace.ui.internal.wizards.pages.conversion.RefinedProjModuleDefSelectionPage;
import eu.cessar.ct.ecuc.workspace.ui.internal.wizards.pages.conversion.StandardModuleConfigSelectionPage;
import eu.cessar.ct.sdk.utils.EcucUtils;
import eu.cessar.ct.sdk.utils.SplitUtils;
import eu.cessar.req.Requirement;
import gautosar.gecucdescription.GModuleConfiguration;
import gautosar.gecucparameterdef.GModuleDef;
import gautosar.ggenericstructure.ginfrastructure.GPackageableElement;

/**
 * The Class ModuleDefinitionWizard.
 */
@Requirement(
	reqID = "29619")
public class ConvertConfigurationWizard extends Wizard
{

	/** The standard module configuration selection page. */
	private StandardModuleConfigSelectionPage stdModuleConfigSelectionConversionPage;
	/** The refined module configuration selection page. */
	private RefinedModuleConfigSelectionPage refinedModuleConfigSelectionConversionPage;
	/** The refined module definition */
	private RefinedProjModuleDefSelectionPage refinedProjectModuleDefinitionSelectionPage;
	private ConversionTypePage conversionTypePage;
	private IProject project;
	private boolean projectLoaded = false;

	/**
	 *
	 */
	public ConvertConfigurationWizard()
	{
		super();
		setNeedsProgressMonitor(true);
	}

	/**
	 * We will accept the selection in the workbench to see if we can initialize from it. *
	 *
	 * @param workbench
	 *        the current workbench
	 * @param selection
	 *        the current object selection
	 * @see IWorkbenchWizard#init(IWorkbench, IStructuredSelection)
	 */
	public final void init(@SuppressWarnings("unused") final IWorkbench workbench, final IStructuredSelection selection)
	{
		setWindowTitle(Messages.ConfigurationConversionWizard_Title);
		project = ((IResource) selection.getFirstElement()).getProject();
		// this.selection = selection;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.ecuc.workspace.ui.internal.wizards.AbstractWizard#performFinish()
	 */
	@Override
	public boolean performFinish()
	{
		switch (getConversionType())
		{
			case STANDARD:
				GPackageableElement selectedModule = stdModuleConfigSelectionConversionPage.getSelectedModule();
				Assert.isTrue(selectedModule instanceof GModuleConfiguration);
				GModuleConfiguration moduleConfiguration = (GModuleConfiguration) selectedModule;

				selectedModule = refinedProjectModuleDefinitionSelectionPage.getSelectedModule();
				Assert.isTrue(selectedModule instanceof GModuleDef);
				GModuleDef moduleDef = (GModuleDef) selectedModule;

				Collection<GModuleConfiguration> concreteInstances = getConcreteInstances(moduleConfiguration);
				for (GModuleConfiguration concreteInstance: concreteInstances)
				{
					EcucUtils.convertEcucFromStandardToRefined(concreteInstance, moduleDef);
				}

				break;

			case REFINED:

				GPackageableElement selectedRefModule = refinedModuleConfigSelectionConversionPage.getSelectedModule();
				Assert.isTrue(selectedRefModule instanceof GModuleConfiguration);
				GModuleConfiguration refinedModuleConfiguration = (GModuleConfiguration) selectedRefModule;

				Collection<GModuleConfiguration> concreteRefinedInstances = getConcreteInstances(refinedModuleConfiguration);
				for (GModuleConfiguration refined: concreteRefinedInstances)
				{
					EcucUtils.convertEcucFromRefinedToStandard(refined);
				}
				break;

			default:
				Assert.isTrue(false, "Invalid conversion type !"); //$NON-NLS-1$
				return false;
		}

		return true;
	}

	@SuppressWarnings("static-method")
	private Collection<GModuleConfiguration> getConcreteInstances(GModuleConfiguration config)
	{
		GModuleConfiguration mergedInstance = SplitUtils.getMergedInstance(config);
		Collection<GModuleConfiguration> concreteInstances = SplitUtils.getConcreteInstances(mergedInstance);
		return concreteInstances;
	}

	@Override
	public void addPages()
	{

		conversionTypePage = new ConversionTypePage();
		addPage(conversionTypePage);

		stdModuleConfigSelectionConversionPage = new StandardModuleConfigSelectionPage(project);
		addPage(stdModuleConfigSelectionConversionPage);

		refinedModuleConfigSelectionConversionPage = new RefinedModuleConfigSelectionPage(project);
		addPage(refinedModuleConfigSelectionConversionPage);

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
		result &= conversionTypePage.isPageComplete();
		// result &= moduleDefinitionTypePage.isPageComplete();

		if (result)
		{
			switch (getConversionType())
			{
				case STANDARD:
					result &= stdModuleConfigSelectionConversionPage.isPageComplete();
					if (refinedProjectModuleDefinitionSelectionPage != null)
					{
						result &= refinedProjectModuleDefinitionSelectionPage.isPageComplete();
					}
					break;

				case REFINED:
					result &= refinedModuleConfigSelectionConversionPage.isPageComplete();
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
	// CHECKSTYLE:OFF
	@Override
	public IWizardPage getNextPage(IWizardPage page)
	{
		if (page == conversionTypePage)
		{
			switch (getConversionType())
			{
				case STANDARD:
					return stdModuleConfigSelectionConversionPage;
				case REFINED:
					return refinedModuleConfigSelectionConversionPage;
				default:
					Assert.isTrue(false, "Invalid module definition type !"); //$NON-NLS-1$
					return null;
			}

		}
		if (page == stdModuleConfigSelectionConversionPage)
		{
			GPackageableElement selectedModule = stdModuleConfigSelectionConversionPage.getSelectedModule();
			if (selectedModule != null && selectedModule instanceof GModuleConfiguration)
			{
				refinedProjectModuleDefinitionSelectionPage = new RefinedProjModuleDefSelectionPage(project,
					(GModuleConfiguration) selectedModule);
				addPage(refinedProjectModuleDefinitionSelectionPage);
			}
			return refinedProjectModuleDefinitionSelectionPage;
		}
		if (page == refinedProjectModuleDefinitionSelectionPage)
		{
			return null;
		}
		if (page == refinedModuleConfigSelectionConversionPage)
		{
			return null;
		}// CHECKSTYLE:ON
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
		if (page == stdModuleConfigSelectionConversionPage)
		{
			return conversionTypePage;
		}

		return super.getPreviousPage(page);
	}

	/**
	 * @return the project
	 */
	public IProject getProject()
	{
		if (!projectLoaded)
		{
			loadProject(project);
			projectLoaded = true;
		}
		return project;
	}

	protected void loadProject(final IProject project)
	{
		if (null != project)
		{

			this.project = project;

			// The user has selected a project and navigates to next page.
			// Wait until the selected project is fully loaded.
			IRunnableWithProgress op = new IRunnableWithProgress()
			{
				public void run(final IProgressMonitor monitor) throws InvocationTargetException
				{
					monitor.beginTask(NLS.bind(Messages.Progress_LoadingProject, project.getName()),
						IProgressMonitor.UNKNOWN);
					try
					{
						PlatformUtils.waitForModelLoading(project, null);
					}
					catch (Exception e)
					{
						e.printStackTrace();
					}
					monitor.done();
				}
			};
			try
			{
				getContainer().run(true, false, op);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}

		}
	}

	/**
	 * Gets the module definition type.
	 *
	 * @return the module definition type
	 */
	public ModuleDefinitionType getConversionType()
	{
		return conversionTypePage.getConversionType();
	}

}