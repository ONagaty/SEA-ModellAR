/**
 *
 */
package eu.cessar.ct.ecuc.workspace.ui.internal.wizards.pages.conversion;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.osgi.util.NLS;
import org.eclipse.sphinx.emf.util.EcorePlatformUtil;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;

import eu.cessar.ct.ecuc.workspace.ui.internal.CessarPluginActivator;
import eu.cessar.ct.ecuc.workspace.ui.internal.Messages;
import eu.cessar.ct.ecuc.workspace.ui.internal.providers.RefinedModuleDefinitionTableContentProvider;
import eu.cessar.ct.ecuc.workspace.ui.internal.wizards.ConvertConfigurationWizard;
import eu.cessar.ct.sdk.utils.SplitUtils;
import gautosar.gecucdescription.GModuleConfiguration;
import gautosar.ggenericstructure.ginfrastructure.GPackageableElement;

/**
 * The Class for refined module definition selection
 */
public class RefinedProjModuleDefSelectionPage extends AbstractModuleSelectionPage
{

	/** The provider. */
	private RefinedModuleDefinitionTableContentProvider provider;
	/**
	 * Composite for the user agreement to overwrite files
	 */
	private AgreementCompositeOverwrittenFiles overwriteComposite;
	private GModuleConfiguration selectedConfiguration;

	/**
	 * Instantiates a new standard module definition selection page.
	 *
	 * @param project
	 * @param moduleConfiguration
	 *
	 * @param moduleDefinitionType
	 *        the module definition type
	 */
	public RefinedProjModuleDefSelectionPage(IProject project, GModuleConfiguration moduleConfiguration)
	{
		super(project, "title refined module", Messages.RefinedModuleDefinitionColumn); //$NON-NLS-1$
		selectedConfiguration = moduleConfiguration;
	}

	@Override
	public void setProject(IProject project)
	{

		super.setProject(project);
		initialProvider(project);
		setContentProvider(provider);
		setLabelProvider(provider);
		setContents();

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jface.dialogs.DialogPage#getErrorMessage()
	 */
	@Override
	public String getErrorMessage()
	{
		if (null == getSelectedModule())
		{
			return Messages.RefinedModuleDefinitionSelectionPage_Err_No_ModuleDefinition;
		}
		if (!overwriteComposite.getSelection())
		{
			return Messages.RefinedModuleConfigurationAgreement_Description;
		}
		return super.getErrorMessage();
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
		setTitle(Messages.RefinedModuleDefinitionSelectionPage_Title);
		setDescription(Messages.RefinedModuleDefinitionSelectionPage_Description);

		overwriteComposite = new AgreementCompositeOverwrittenFiles();
		Composite contentComposite = getContentComposite();
		overwriteComposite.createContent(contentComposite);
		overwriteComposite.addSelectionListener(new SelectionAdapter()
		{

			@Override
			public void widgetSelected(SelectionEvent event)
			{
				setPageComplete(isPageComplete());
				getContainer().updateMessage();

			}

		});
	}

	private RefinedModuleDefinitionTableContentProvider initialProvider(IProject proj)
	{

		provider = new RefinedModuleDefinitionTableContentProvider(proj, selectedConfiguration);
		return provider;
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

			// setNextPageDefault(this);
			project = ((ConvertConfigurationWizard) getWizard()).getProject();
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
								// Update tree of modules
								provider.setProject(project);
								// provider.setModuleConfiguration(selectedConfiguration);
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
			}// CHECKSTYLE:ON
		}
		else
		{
			overwriteComposite.resetButtons();
		}
		super.setVisible(visible);
	}

	@Override
	public void updateInfoMessage()
	{
		List<String> configurationsPaths = new ArrayList<String>();
		GModuleConfiguration mergedInstance = SplitUtils.getMergedInstance(selectedConfiguration);
		Collection<GModuleConfiguration> concreteInstances = SplitUtils.getConcreteInstances(mergedInstance);
		for (GModuleConfiguration configInstance: concreteInstances)
		{
			IFile definingFile = EcorePlatformUtil.getFile(configInstance);
			if (definingFile != null)
			{
				String text = definingFile.getFullPath().toString();
				configurationsPaths.add(text);

			}

		}
		overwriteComposite.updateInfoMessage(configurationsPaths);

	}

	/**
	 * @param selectedConfiguration
	 */
	public void setSelectedConfiguration(GModuleConfiguration selectedConfiguration)
	{
		this.selectedConfiguration = selectedConfiguration;
		initialProvider(project);
	}

	@Override
	public boolean isPageComplete()
	{
		GPackageableElement selectedModule = getSelectedModule();
		if (selectedModule == null)
		{
			return false;
		}
		boolean selection = overwriteComposite.getSelection();
		if (!selection)
		{
			return false;
		}
		return true;
	}
}
