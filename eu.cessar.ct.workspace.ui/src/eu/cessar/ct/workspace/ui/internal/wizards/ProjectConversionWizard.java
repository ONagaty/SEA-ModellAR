/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 *
 * File created by uid95513<br/>
 * Feb 9, 2015 1:56:21 PM
 *
 * </copyright>
 */
package eu.cessar.ct.workspace.ui.internal.wizards;

import java.lang.reflect.InvocationTargetException;

import org.artop.aal.common.metamodel.AutosarReleaseDescriptor;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.wizard.Wizard;

import eu.cessar.ct.core.platform.EProjectVariant;
import eu.cessar.ct.workspace.jobs.CreateCessarProjectJob;
import eu.cessar.ct.workspace.ui.internal.CessarPluginActivator;
import eu.cessar.ct.workspace.ui.internal.Messages;

/**
 * ProjectConversionWizard is a wizard that provides the possibility to choose the Autosar release and configuration
 * variant for a project, in order to add all CESSAR-CT settings
 *
 * @author uid95513
 *
 *         %created_by: uid95513 %
 *
 *         %date_created: Tue Feb 17 09:57:27 2015 %
 *
 *         %version: 3 %
 */
public class ProjectConversionWizard extends Wizard
{
	private IProject project;
	private ProjectConversionWizardPage mainPage;

	/**
	 * @param project
	 */
	public ProjectConversionWizard(IProject project)
	{
		this.project = project;
	}

	@Override
	public boolean performFinish()
	{
		EProjectVariant projectVariant = mainPage.getConfigVariant();
		AutosarReleaseDescriptor autosarRelease = mainPage.getMetaModelVersionDescriptor();

		final CreateCessarProjectJob job = new CreateCessarProjectJob(project, null, autosarRelease);
		job.setConfigVariant(projectVariant);

		try
		{
			// do not run the job as a Platform Job but run it using the Wizard progress
			getContainer().run(false, true, new IRunnableWithProgress()
			{
				@SuppressWarnings("restriction")
				public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException
				{
					ISchedulingRule rule = ResourcesPlugin.getWorkspace().getRoot();
					try
					{
						monitor.beginTask(Messages.label_ProjectConversionDescription, 1000);
						Job.getJobManager().beginRule(rule, monitor);

						job.run(new SubProgressMonitor(monitor, 500));
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
		mainPage = new ProjectConversionWizardPage("projectConversionPage"); //$NON-NLS-1$
		mainPage.setDescription(Messages.label_ProjectConversionDescription);
		setWindowTitle(Messages.label_ProjectConversionTitle);
		addPage(mainPage);
	}
}
