/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidl6458 Mar 26, 2010 10:41:46 AM </copyright>
 */
package eu.cessar.ct.workspace.ui.internal.wizards;

import java.lang.reflect.InvocationTargetException;

import org.artop.aal.workspace.jobs.CreateNewAutosarFileJob;
import org.artop.aal.workspace.ui.wizards.BasicAutosarFileWizard;
import org.artop.aal.workspace.ui.wizards.pages.NewAutosarFileCreationPage;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.sphinx.platform.ui.util.ExtendedPlatformUI;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.preferences.ScopedPreferenceStore;

import eu.cessar.ct.core.mms.MetaModelUtils;
import eu.cessar.ct.workspace.ui.internal.CessarPluginActivator;
import eu.cessar.ct.workspace.ui.internal.Messages;

/**
 * @author uidl6458
 *
 */
public class NewCessarFileWizard extends BasicAutosarFileWizard
{

	/**
	 *
	 */
	public NewCessarFileWizard()
	{
		// disable Advanced button in the new file wizard
		setDisabledLinking(true);
	}

	/**
	 * Set the value of the disable linking preference
	 *
	 * This preference is used in WizardNewFileCreationPage.class createAdvancedControls method to create the advanced
	 * button
	 *
	 * @param bool
	 */
	private static void setDisabledLinking(boolean bool)
	{
		IPreferenceStore s = new ScopedPreferenceStore(InstanceScope.INSTANCE, Messages.ORG_ECLIPSE_CORE_RESOURCES);
		s.setValue(Messages.NewCessarFileWizard_DISABLE_LINKING_PREFERENCE, bool);
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

		Control[] children = pageContainer.getChildren();

		if (children.length == 1)
		{
			PlatformUI.getWorkbench().getHelpSystem().setHelp(children[0], Messages.NEW_CESSAR_FILE_WIZARD_CONTEXT);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.artop.aal.workspace.ui.wizards.BasicAutosarFileWizard#performFinish()
	 */
	@Override
	public boolean performFinish()
	{
		NewAutosarFileCreationPage page = (NewAutosarFileCreationPage) mainPage;
		final IFile autosarFile = page.getNewFile();
		final String arPackageName = page.getInitialARPackageName();

		final CreateNewAutosarFileJob job = new CreateNewAutosarFileJob(Messages.label_newAUTOSARFileDescription,
			autosarFile, MetaModelUtils.getAutosarRelease(autosarFile.getProject()), arPackageName);

		try
		{
			getContainer().run(false, true, new IRunnableWithProgress()
			{

				public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException
				{
					ISchedulingRule rule = ResourcesPlugin.getWorkspace().getRoot();
					try
					{
						monitor.beginTask(Messages.label_newAUTOSARFileDescription, 1000);
						Job.getJobManager().beginRule(rule, monitor);
						@SuppressWarnings("restriction")
						IStatus status = job.run(new SubProgressMonitor(monitor, 500));
						if (status.isOK())
						{
							// Select the newly created file in all parts of the
							// active workbench window's active page
							Display display = ExtendedPlatformUI.getDisplay();
							if (display != null)
							{
								display.asyncExec(new Runnable()
								{
									public void run()
									{
										selectAndReveal(autosarFile,
											PlatformUI.getWorkbench().getActiveWorkbenchWindow());
									}
								});
							}
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
		finally
		{
			// revert the disable of Advanced button in the new file wizard
			setDisabledLinking(false);
		}

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
		// revert the disable of Advanced button in the new file wizard
		setDisabledLinking(false);

		return super.performCancel();
	}
}
