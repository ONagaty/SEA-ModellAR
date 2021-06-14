package eu.cessar.ct.workspace.ui.internal.wizards;

import java.lang.reflect.InvocationTargetException;
import java.net.URI;

import org.artop.aal.workspace.ui.wizards.BasicAutosarProjectWizard;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.sphinx.emf.workspace.loading.ModelLoadManager;
import org.eclipse.sphinx.platform.ui.util.ExtendedPlatformUI;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.undo.WorkspaceUndoUtil;

import eu.cessar.ct.workspace.internal.CessarPluginActivator;
import eu.cessar.ct.workspace.jobs.CreateCessarProjectJob;
import eu.cessar.ct.workspace.ui.internal.Messages;

/**
 * Wizard for creating new Cessar projects
 *
 */
@SuppressWarnings({"deprecation", "restriction"})
public class NewCessarProjectWizard extends BasicAutosarProjectWizard
{
	private CessarProjectWizardPage mainPage;
	private IProject newProject;

	/*
	 * (non-Javadoc)
	 *
	 * @see org.artop.aal.workspace.ui.wizards.BasicAutosarProjectWizard#init(org.eclipse.ui.IWorkbench,
	 * org.eclipse.jface.viewers.IStructuredSelection)
	 */
	@Override
	public void init(IWorkbench workbench, IStructuredSelection currentSelection)
	{
		super.init(workbench, currentSelection);
		setWindowTitle(Messages.label_newProjectWizardTitle);
		setNeedsProgressMonitor(true);
	}

	@Override
	public boolean performFinish()
	{
		mainPage.saveDialogSettings();
		URI location = !mainPage.useDefaults() ? mainPage.getLocationURI() : null;
		final IProject projectHandle = mainPage.getProjectHandle();

		final CreateCessarProjectJob job = new CreateCessarProjectJob(projectHandle, location,
			mainPage.getMetaModelVersionDescriptor());

		// Map<AutosarLibraryIDEnumerator, AutosarLibraryDescriptor> descriptors =
		// mainPage.getMetaModelVersionDescriptor().getAutosarLibraryDescriptors();

		job.getImportedAutosarLibraries().addAll(mainPage.getImportedAutosarLibraryDescriptors());
		// for (AutosarLibraryIDEnumerator libEnum: descriptors.keySet())
		// {
		// if (mainPage.getImportedAutosarLibraryDescriptors() isImport(libEnum))
		// {
		// job.getImportedAutosarLibraries().add(descriptors.get(libEnum));
		// }
		// }
		// job.setImportApplicationInterfaces(mainPage.isImportApplicationInterfaces());
		// job.setImportEcuCParamDef(mainPage.isImportEcuCParamDef());

		job.setConfigVariant(mainPage.getConfigVariant());

		job.setUIInfoAdaptable(WorkspaceUndoUtil.getUIInfoAdapter(getShell()));
		// reveal the project after creation

		try
		{
			// do not run the job as a Platform Job but run it using the Wizard
			// progress

			getContainer().run(false, true, new IRunnableWithProgress()
			{

				public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException
				{
					ISchedulingRule rule = ResourcesPlugin.getWorkspace().getRoot();
					try
					{
						monitor.beginTask(Messages.label_newProjectDescription, 1000);
						Job.getJobManager().beginRule(rule, monitor);
						@SuppressWarnings("restriction")
						IStatus status = job.run(new SubProgressMonitor(monitor, 500));
						if (status.isOK())
						{
							newProject = projectHandle;
							Display display = ExtendedPlatformUI.getDisplay();
							if (display != null)
							{
								display.asyncExec(new Runnable()
								{
									public void run()
									{
										updatePerspective();
										selectAndReveal(newProject,
											PlatformUI.getWorkbench().getActiveWorkbenchWindow());
									}
								});
							}
							ModelLoadManager.INSTANCE.loadProject(newProject, false, false,
								new SubProgressMonitor(monitor, 500));
						}
					}
					finally
					{
						monitor.done();
						Job.getJobManager().endRule(rule);
					}

				}
			});
		}
		catch (InvocationTargetException e)
		{
			CessarPluginActivator.getDefault().logError(e);
			return false;
		}
		catch (InterruptedException e)
		{
			CessarPluginActivator.getDefault().logError(e);
			return false;
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.artop.aal.workspace.ui.wizards.BasicAutosarProjectWizard#addPages()
	 */
	@Override
	public void addPages()
	{
		mainPage = new CessarProjectWizardPage("basicNewProjectPage"); //$NON-NLS-1$
		mainPage.setTitle(Messages.label_newProjectTitle);
		mainPage.setDescription(Messages.label_newProjectDescription);

		addPage(mainPage);
	}
}
