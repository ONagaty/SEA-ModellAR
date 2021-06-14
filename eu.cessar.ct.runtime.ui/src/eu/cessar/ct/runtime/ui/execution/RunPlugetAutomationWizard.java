/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 *
 * File created by uid95513<br/>
 * Apr 28, 2015 4:30:44 PM
 *
 * </copyright>
 */
package eu.cessar.ct.runtime.ui.execution;

import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import eu.cessar.ct.cid.bindings.PlugetBinding;
import eu.cessar.ct.core.platform.PlatformConstants;
import eu.cessar.ct.runtime.ui.internal.CessarPluginActivator;
import eu.cessar.ct.runtime.ui.internal.Messages;
import eu.cessar.ct.runtime.ui.internal.PlugetAutomationWizard;
import eu.cessar.ct.sdk.logging.LoggerFactory;
import eu.cessar.ct.sdk.runtime.ExecutionService;
import eu.cessar.ct.sdk.runtime.ICessarTaskManager;
import eu.cessar.req.Requirement;

/**
 * Class provides a service for creating the wizard needed for running a pluget
 *
 * @author uid95513
 *
 *         %created_by: uid95513 %
 *
 *         %date_created: Tue Jun 23 14:54:35 2015 %
 *
 *         %version: 4 %
 */
@Requirement(
	reqID = "61760")
public final class RunPlugetAutomationWizard
{
	private static IProject selectedProject;
	private static PlugetBinding selectedPlugetBinding;

	/**
	 * wizard containing the pluget inputs described in .cid file
	 */
	private RunPlugetAutomationWizard()
	{
		// not needed to be instantiated
	}

	/**
	 * @param plugetBinding
	 */
	public static void executeWizardForPluget(PlugetBinding plugetBinding)
	{
		// Get the project selected in Project Explorer
		selectedProject = eu.cessar.ct.core.platform.ui.util.SelectionUtils.getActiveProject(true);
		if (selectedProject != null)
		{
			try
			{
				// make sure the wizard is run only for the CESSAR projects
				if (selectedProject.hasNature(PlatformConstants.CESSAR_NATURE))
				{
					selectedPlugetBinding = plugetBinding;
					createWizardForPluget(plugetBinding);
				}
				else
				{
					// nothing to do; the getActiveProject() will display a message informing that the project does
					// not have the CESSAR nature
				}
			}
			catch (CoreException e)
			{
				CessarPluginActivator.getDefault().logError(e);
			}
		}
		else
		{
			// nothing to do; the getActiveProject() will display a message informing that there is no project
			// selected
		}
	}

	private static void createWizardForPluget(PlugetBinding plugetBinding)
	{
		// create the wizard for this pluget
		PlugetAutomationWizard wizard = new PlugetAutomationWizard(plugetBinding);

		Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
		if (shell != null)
		{

			WizardDialog wizardDialog = new WizardDialog(shell, wizard);

			int open = wizardDialog.open();
			if (open == IDialogConstants.CANCEL_ID)
			{
				return;
			}
			if (open == IDialogConstants.OK_ID)
			{
				final String[] arguments = wizard.getArguments();

				if (arguments == null)
				{
					return;
				}

				// create default monitor and run
				try
				{
					new ProgressMonitorDialog(shell).run(true, true, new IRunnableWithProgress()
					{

						public void run(IProgressMonitor monitor) throws InvocationTargetException,
						InterruptedException
						{
							runPluget(monitor, selectedProject, selectedPlugetBinding, arguments);
						}
					});
				}
				catch (InterruptedException e)
				{
					CessarPluginActivator.getDefault().logError(e);
				}
				catch (InvocationTargetException e)
				{
					CessarPluginActivator.getDefault().logError(e);
				}
			}
		}
	}

	/**
	 * @param monitor
	 *        used to display progress
	 * @param currentProject
	 *        project of the pluget
	 * @param plugetBinding
	 *        binding to the pluget artifact
	 * @param arguments
	 *        pluget input values
	 */
	protected static void runPluget(IProgressMonitor monitor, IProject currentProject, PlugetBinding plugetBinding,
		String[] arguments)
	{
		if (plugetBinding.isProjectPluget())
		{
			executeProjectPluget(monitor, currentProject, plugetBinding, arguments);
		}
		else
			// run global repository pluget
		{
			executeGlobalPluget(monitor, currentProject, plugetBinding, arguments);
		}
	}

	/**
	 * @param monitor
	 *        used to display progress
	 * @param currentProject
	 *        project of the pluget
	 * @param plugetBinding
	 *        binding to the pluget artifact
	 * @param arguments
	 *        pluget input values
	 */
	private static void executeGlobalPluget(IProgressMonitor monitor, IProject currentProject,
		PlugetBinding plugetBinding, String[] arguments)
	{
		String name = plugetBinding.getArtifact().getName();
		try
		{
			monitor.beginTask(Messages.executingPluget + name, 1000);
			URL plugetLocation = plugetBinding.getPlugetLocation();
			@SuppressWarnings("unchecked")
			ICessarTaskManager<URL> manager = (ICessarTaskManager<URL>) ExecutionService.createManager(currentProject,
				ExecutionService.TASK_TYPE_PLUGET_EXTERNAL);
			manager.initialize(plugetLocation);

			IStatus status = manager.execute(true, arguments, monitor);
			if (!status.isOK())
			{
				LoggerFactory.getLogger().info(status.getMessage());
			}
		}
		catch (MalformedURLException e)
		{
			CessarPluginActivator.getDefault().logError(e);
			LoggerFactory.getLogger().error(e.getMessage());
		}
		catch (URISyntaxException e)
		{
			CessarPluginActivator.getDefault().logError(e);
			LoggerFactory.getLogger().error(e.getMessage());
		}
		finally
		{
			monitor.done();
		}

	}

	/**
	 * @param monitor
	 *        used to display progress
	 * @param currentProject
	 *        project of the pluget
	 * @param plugetBinding
	 *        binding to the pluget artifact
	 * @param arguments
	 *        pluget input values
	 */
	private static void executeProjectPluget(IProgressMonitor monitor, IProject currentProject,
		PlugetBinding plugetBinding, String[] arguments)
	{
		String name = plugetBinding.getArtifact().getName();
		IFile plugetFile = plugetBinding.getPlugetFile();

		monitor.beginTask(Messages.executingPluget + name, 1000);
		@SuppressWarnings("unchecked")
		ICessarTaskManager<IFile> manager = (ICessarTaskManager<IFile>) ExecutionService.createManager(currentProject,
			ExecutionService.TASK_TYPE_PLUGET);
		manager.initialize(plugetFile);

		IStatus status = manager.execute(false, arguments, new NullProgressMonitor());
		monitor.done();
		if (!status.isOK())
		{
			LoggerFactory.getLogger().info(status.getMessage());
		}
	}
}
