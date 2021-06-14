/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidg4020<br/>
 * Mar 3, 2014 11:09:12 AM
 * 
 * </copyright>
 */
package eu.cessar.ct.runtime.ecuc.ui.internal.actions;

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
import org.eclipse.core.variables.VariablesPlugin;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import eu.cessar.ct.cid.bindings.PlugetBinding;
import eu.cessar.ct.cid.model.Artifact;
import eu.cessar.ct.cid.model.IArtifactBinding;
import eu.cessar.ct.runtime.ecuc.ui.internal.CessarPluginActivator;
import eu.cessar.ct.runtime.ecuc.ui.internal.wizards.PlugetSelectionPage.IPluget;
import eu.cessar.ct.runtime.ecuc.ui.internal.wizards.PlugetSelectionPage.PlugetArtifact;
import eu.cessar.ct.runtime.ecuc.ui.internal.wizards.PlugetSelectionPage.PlugetFile;
import eu.cessar.ct.runtime.ecuc.ui.internal.wizards.PlugetWizard;
import eu.cessar.ct.runtime.ecuc.util.RunPlugetUtils;
import eu.cessar.ct.sdk.logging.ILogger;
import eu.cessar.ct.sdk.logging.LoggerFactory;
import eu.cessar.ct.sdk.runtime.ExecutionService;
import eu.cessar.ct.sdk.runtime.ICessarTaskManager;
import eu.cessar.req.Requirement;

/**
 * Action for opening the wizard for new cessar file.
 * 
 */
@Requirement(
	reqID = "REQ_RUN_SELECTED_PLUGET#1")
public class RunPlugetAction implements IObjectActionDelegate
{
	private IProject project;
	private ILogger logger = LoggerFactory.getLogger();

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IActionDelegate#run(org.eclipse.jface.action.IAction)
	 */
	public void run(IAction action)
	{
		IWorkbench workbench = PlatformUI.getWorkbench();
		IWorkbenchWindow activeWorkbenchWindow = workbench.getActiveWorkbenchWindow();
		if ((activeWorkbenchWindow != null) && (project != null))
		{

			PlugetWizard wizard = new PlugetWizard(project);

			Shell shell = activeWorkbenchWindow.getShell();
			if (shell != null)
			{
				WizardDialog dialog = new WizardDialog(shell, wizard);
				int open = dialog.open();
				if (open == IDialogConstants.CANCEL_ID)
				{
					return;
				}
				if (open == IDialogConstants.OK_ID)
				{
					final IPluget pluget = wizard.getPluget();

					final String introducedValues = wizard.getIntroducedValues();

					final String[] arguments = createArguments(introducedValues, shell);
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

								runPluget(monitor, project, pluget, arguments);
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

	}

	/**
	 * Create arguments based on introduced values
	 * 
	 * @param introducedValues
	 * @param shell
	 */
	private String[] createArguments(String introducedValues, Shell shell)
	{
		String[] arguments = null;
		try
		{
			String values = VariablesPlugin.getDefault().getStringVariableManager().performStringSubstitution(
				introducedValues);
			arguments = DebugPlugin.parseArguments(values);
		}
		catch (CoreException e)
		{
			ErrorDialog.openError(shell, "Cessar-CT", "Error occured while evaluating the input", //$NON-NLS-1$//$NON-NLS-2$
				e.getStatus());
		}
		return arguments;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IActionDelegate#selectionChanged(org.eclipse.jface.action.IAction,
	 * org.eclipse.jface.viewers.ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection)
	{
		project = null;
		if ((selection instanceof StructuredSelection))
		{
			Object element = ((IStructuredSelection) selection).getFirstElement();
			if (null != element)
			{
				project = RunPlugetUtils.INSTANCE.getProject(element);
			}
		}
		action.setEnabled(null != project);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IObjectActionDelegate#setActivePart(org.eclipse.jface.action.IAction,
	 * org.eclipse.ui.IWorkbenchPart)
	 */
	public void setActivePart(IAction action, IWorkbenchPart targetPart)
	{
		// TODO Auto-generated method stub

	}

	/**
	 * Run a pluget depending on the {@link pluget} type. Can be an {@link PlugetArtifact} or an {@link PlugetFile}
	 * 
	 * @param monitor
	 *        used to display progress
	 * @param currentProject
	 *        project of the pluget
	 * @param pluget
	 *        the pluget to be run
	 * @param arguments
	 *        to be used when the pluget is run
	 */
	public void runPluget(IProgressMonitor monitor, IProject currentProject, IPluget pluget, String[] arguments)
	{
		IFile pFile;
		String name = pluget.getName();

		if (pluget.isArtifact())
		{
			PlugetArtifact plugetArtifact = (PlugetArtifact) pluget;
			Artifact artifact = plugetArtifact.getArtifact();
			IArtifactBinding binding = artifact.getConcreteBinding();
			PlugetBinding plugetBinding = (PlugetBinding) binding;
			// execute pluget if global
			if (!plugetBinding.isProjectPluget())
			{
				try
				{
					monitor.beginTask("Executing pluget " + name, 1000); //$NON-NLS-1$
					URL plugetLocation = plugetBinding.getPlugetLocation();
					ICessarTaskManager<URL> manager = (ICessarTaskManager<URL>) ExecutionService.createManager(
						currentProject, ExecutionService.TASK_TYPE_PLUGET_EXTERNAL);
					manager.initialize(plugetLocation);

					IStatus status = manager.execute(true, arguments, monitor);
					if (!status.isOK())
					{
						logger.info(status.getMessage());
					}
				}
				catch (MalformedURLException e)
				{
					CessarPluginActivator.getDefault().logError(e);
					logger.error(e.getMessage());
				}
				catch (URISyntaxException e)
				{
					CessarPluginActivator.getDefault().logError(e);
					logger.error(e.getMessage());
				}
				finally
				{
					monitor.done();
				}
				return;
			}

			pFile = plugetBinding.getPlugetFile();

		}
		else
		{
			PlugetFile plugetFile = (PlugetFile) pluget;
			pFile = plugetFile.getPlugetFile();
		}

		// run file pluget
		monitor.beginTask("Executing pluget " + name, 1000); //$NON-NLS-1$
		ICessarTaskManager<IFile> manager = (ICessarTaskManager<IFile>) ExecutionService.createManager(currentProject,
			ExecutionService.TASK_TYPE_PLUGET);
		manager.initialize(pFile);
		IStatus status = manager.execute(false, arguments, new NullProgressMonitor());
		monitor.done();
		if (!status.isOK())
		{
			logger.info(status.getMessage());
		}

	}

}
