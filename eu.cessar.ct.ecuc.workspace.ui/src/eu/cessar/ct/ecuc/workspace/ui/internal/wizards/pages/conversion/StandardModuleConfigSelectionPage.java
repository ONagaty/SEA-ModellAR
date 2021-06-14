/**
 *
 */
package eu.cessar.ct.ecuc.workspace.ui.internal.wizards.pages.conversion;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.widgets.Composite;

import eu.cessar.ct.ecuc.workspace.ui.internal.CessarPluginActivator;
import eu.cessar.ct.ecuc.workspace.ui.internal.Messages;
import eu.cessar.ct.ecuc.workspace.ui.internal.providers.StandardModuleConfigurationTableContentProvider;
import eu.cessar.ct.ecuc.workspace.ui.internal.wizards.ConvertConfigurationWizard;
import gautosar.gecucdescription.GModuleConfiguration;

/**
 * The class for standard module configuration selection
 */
public class StandardModuleConfigSelectionPage extends AbstractModuleSelectionPage
{

	/** The provider. */
	private StandardModuleConfigurationTableContentProvider provider;

	/**
	 * Instantiates a new standard module definition selection page.
	 *
	 * @param project
	 *
	 * @param moduleDefinitionType
	 *        the module definition type
	 */
	public StandardModuleConfigSelectionPage(IProject project)
	{
		super(project, "Page ", Messages.ModuleConfigurationColumn); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * eu.cessar.ct.ecuc.workspace.ui.internal.wizards.pages.conversion.AbstractModuleSelectionPage#setProject(org.eclipse
	 * .core.resources.IProject)
	 */
	@Override
	public void setProject(IProject project)
	{

		super.setProject(project);
		initializeProvider(project);
		setContentProvider(provider);
		setLabelProvider(provider);
		setContents();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.wizard.WizardPage#isPageComplete()
	 */
	@Override
	public boolean isPageComplete()
	{
		if (selectedModuleConfiguration == null)
		{
			return false;
		}
		Assert.isTrue(selectedModuleConfiguration instanceof GModuleConfiguration);
		return null != getSelectedModule()
			&& !provider.isDuplicateModule((GModuleConfiguration) selectedModuleConfiguration);

	}

	@Override
	public boolean canFlipToNextPage()
	{
		if (selectedModuleConfiguration == null)
		{
			return false;
		}
		Assert.isTrue(selectedModuleConfiguration instanceof GModuleConfiguration);
		return null != getSelectedModule()
			&& !provider.isDuplicateModule((GModuleConfiguration) selectedModuleConfiguration);
	}

	private StandardModuleConfigurationTableContentProvider initializeProvider(IProject proj)
	{
		project = ((ConvertConfigurationWizard) getWizard()).getProject();
		provider = new StandardModuleConfigurationTableContentProvider(project);

		return provider;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void createControl(Composite parent)
	{
		super.createControl(parent);
		setTitle(Messages.StandardConfigurationSelectionPage_Title);
		setDescription(Messages.StandardConfigurationSelectionPage_Description);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.DialogPage#setVisible(boolean)
	 */
	@Override
	public void setVisible(final boolean visible)
	{
		if (visible)
		{
			project = ((ConvertConfigurationWizard) getWizard()).getProject();

			if (null != project)
			{
				setProject(project);
				IRunnableWithProgress op = new IRunnableWithProgress()
				{
					public void run(final IProgressMonitor monitor) throws InvocationTargetException
					{
						monitor.beginTask(NLS.bind(Messages.Progress_LoadingModuleDefinitions, project.getName()),
							IProgressMonitor.UNKNOWN);
						try
						{
							getShell().getDisplay().syncExec(new Runnable()
							{
								public void run()
								{
									// IProject wizardProject = ((ConvertConfigurationWizard) getWizard()).getProject();
									// Update tree of modules
									provider.setProject(project);
									refreshTable();
								}
							});
						}// CHECKSTYLE:OFF
						catch (Exception e)
						{
							CessarPluginActivator.getDefault().logError(e);
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
					CessarPluginActivator.getDefault().logError(e);
				}
				// CHECKSTYLE:ON
			}
		}
		super.setVisible(visible);
	}

}
