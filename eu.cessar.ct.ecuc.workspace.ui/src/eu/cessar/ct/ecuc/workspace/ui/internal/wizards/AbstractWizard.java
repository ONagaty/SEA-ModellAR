package eu.cessar.ct.ecuc.workspace.ui.internal.wizards;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizardContainer;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.osgi.util.NLS;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWizard;
import org.eclipse.ui.wizards.newresource.BasicNewResourceWizard;

import eu.cessar.ct.core.mms.FileExtensionPreferenceAccessor;
import eu.cessar.ct.core.mms.MetaModelUtils;
import eu.cessar.ct.core.platform.util.PlatformUtils;
import eu.cessar.ct.ecuc.workspace.ui.internal.Messages;
import eu.cessar.ct.ecuc.workspace.ui.internal.wizards.pages.IPageTextProvider;
import gautosar.gecucparameterdef.GModuleDef;

/**
 * An abstract class that holds generic implementation for the concrete wizards.
 */
public abstract class AbstractWizard extends BasicNewResourceWizard implements INewWizard,
	IWorkbenchWizard
{
	protected static String FILENAME_EXTENSION_SEPARATOR = "."; //$NON-NLS-1$
	public static final String NAMESPACE = "eu.cessar.ct.edit.ui"; //$NON-NLS-1$
	public static final String KEY_PROJECT_SPECIFIC = "projectSpecific";//$NON-NLS-1$
	public static final String KEY_CONFIG_BSWMD_EXT = "bswmd.extension"; //$NON-NLS-1$
	public static final String KEY_CONFIG_ECUC_EXT = "ecuc.extension"; //$NON-NLS-1$
	public static final String DEFAULT_BSWMD_EXTENSION = "autosar"; //$NON-NLS-1$ 
	public static final String DEFAULT_ECUC_EXTENSION = "ecuconfig"; //$NON-NLS-1$ 

	protected IProject project;
	protected ArrayList<WizardPage> pages;
	protected String windowTitle;

	/**
	 * The class constructor
	 */
	public AbstractWizard()
	{
		setNeedsProgressMonitor(true);
	}

	protected abstract ArrayList<WizardPage> getWizardPages(IWorkbench workbench,
		IStructuredSelection selection);

	public abstract IPageTextProvider getTextProvider(WizardPage page);

	public abstract IProject getProject();

	public abstract String getDestinationModuleName();

	public abstract String getDefaultDestinationModuleName();

	public abstract String getDestinationPackageName();

	public abstract String getDefaultDestinationPackageName();

	protected abstract String getDefaultFileExtension();

	public IFile getDefaultOutputFile()
	{
		// Build a path for the file
		String fileLocation = ""; //$NON-NLS-1$
		IStructuredSelection selection = getSelection();
		if (null != selection && !selection.isEmpty())
		{
			Object selected = selection.getFirstElement();
			if (selected instanceof IProject)
			{
				fileLocation = getDestinationPackageName();
			}
			if (selected instanceof IFolder)
			{
				fileLocation = MetaModelUtils.QNAME_SEPARATOR_STR
					+ ((IFolder) selected).getProjectRelativePath().toString();
			}
			if (selected instanceof IFile)
			{
				fileLocation = MetaModelUtils.QNAME_SEPARATOR_STR
					+ ((IFile) selected).getParent().getProjectRelativePath().toString();
			}
		}

		if (!fileLocation.equals(MetaModelUtils.QNAME_SEPARATOR_STR))
		{
			fileLocation += MetaModelUtils.QNAME_SEPARATOR_STR;
		}

		fileLocation += getDestinationModuleName() + FILENAME_EXTENSION_SEPARATOR
			+ getDefaultFileExtension();
		IPath outputPath = getProject().getFullPath().append(fileLocation);
		return ResourcesPlugin.getWorkspace().getRoot().getFile(outputPath);
	}

	public abstract GModuleDef getModuleDefinition();

	/**
	 * Override the method from super class in order to change the size of the
	 * wizard dialog.
	 * 
	 * @param wizardContainer
	 *        the wizard container, or <code>null</code>
	 * @see org.eclipse.jface.wizard.IWizard#setContainer(IWizardContainer)
	 */
	@Override
	public final void setContainer(final IWizardContainer wizardContainer)
	{
		super.setContainer(wizardContainer);
		if (wizardContainer != null)
		{
			if (wizardContainer instanceof WizardDialog)
			{
				((WizardDialog) wizardContainer).setPageSize(400, 400);

			}
		}
	}

	/**
	 * We will accept the selection in the workbench to see if we can initialize
	 * from it. *
	 * 
	 * @param workbench
	 *        the current workbench
	 * @param selection
	 *        the current object selection
	 * @see IWorkbenchWizard#init(IWorkbench, IStructuredSelection)
	 */
	@Override
	public final void init(final IWorkbench workbench, final IStructuredSelection selection)
	{
		super.init(workbench, selection);
		setWindowTitle(windowTitle);
		pages = getWizardPages(workbench, selection);
	}

	/**
	 * Adding the page to the wizard.
	 */
	@Override
	public final void addPages()
	{
		for (int i = 0; i < pages.size(); i++)
		{
			addPage(pages.get(i));
		}
	}

	/**
	 * Method that query the wizard pages in order to decide if the wizard can
	 * be finished or not.
	 * 
	 * @return <code>true</code> if the wizard can be finished, or
	 *         <code>false</code> otherwise
	 */
	@Override
	public boolean canFinish()
	{
		boolean result = true;
		for (int i = 0; i < pages.size(); i++)
		{
			result &= pages.get(i).isPageComplete();
		}
		return result;
	}

	private String bswmdExtension;

	/**
	 * @return the bswmdExtension
	 */
	public String getBswmdExtension()
	{
		return bswmdExtension;
	}

	/**
	 * @return the ecucExtension
	 */
	public String getEcucExtension()
	{
		return ecucExtension;
	}

	private String ecucExtension;

	protected void loadProject(final IProject project)
	{
		if (null != project)
		{
			if (!project.equals(this.project))
			{
				this.project = project;

				bswmdExtension = FileExtensionPreferenceAccessor.getBswmdExtension(project);
				ecucExtension = FileExtensionPreferenceAccessor.getEcucExtension(project);

				// The user has selected a project and navigates to next page.
				// Wait until the selected project is fully loaded.
				IRunnableWithProgress op = new IRunnableWithProgress()
				{
					public void run(final IProgressMonitor monitor)
						throws InvocationTargetException
					{
						monitor.beginTask(
							NLS.bind(Messages.Progress_LoadingProject, project.getName()),
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
	}

	/**
	 * This method is called when 'Finish' button is pressed in the wizard. We
	 * will create an operation and run it using wizard as execution context.
	 * 
	 * @return <code>true</code> if the wizard operation ended successfully, or
	 *         <code>false</code> if an error occurred
	 * @see org.eclipse.jface.wizard.Wizard#performFinish()
	 */
	@Override
	abstract public boolean performFinish();

	@Override
	abstract public String getWindowTitle();
}