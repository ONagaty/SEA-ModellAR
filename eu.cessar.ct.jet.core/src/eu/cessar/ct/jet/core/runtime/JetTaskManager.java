/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidl6458 Feb 1, 2010 2:58:38 PM </copyright>
 */
package eu.cessar.ct.jet.core.runtime;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.ecore.EObject;

import eu.cessar.ct.core.platform.CESSARPreferencesAccessor;
import eu.cessar.ct.core.platform.ECompatibilityMode;
import eu.cessar.ct.runtime.CodegenPreferencesAccessor;
import eu.cessar.ct.runtime.ecuc.IEcucCore;
import eu.cessar.ct.runtime.ecuc.IEcucPresentationModel;
import eu.cessar.ct.runtime.execution.AbstractJobBasedTaskManager;
import eu.cessar.ct.runtime.execution.CessarTask;
import eu.cessar.ct.runtime.execution.ICessarTaskDescriptor;
import eu.cessar.ct.sdk.utils.ProjectUtils;
import eu.cessar.req.Requirement;

/**
 * @author uidl6458
 *
 */
public class JetTaskManager extends AbstractJobBasedTaskManager<IFile>
{
	private EObject presentationModel;
	private IFolder jetOutputFolder;

	private static final String KEY_OUTPUT_FOLDER = "output.folder"; //$NON-NLS-1$
	private ECompatibilityMode compatibilityMode;

	private IStatus initStatus;

	/**
	 *
	 * @param descriptor
	 * @param project
	 */
	public JetTaskManager(ICessarTaskDescriptor descriptor, IProject project, Map<String, Object> map)
	{
		super(descriptor, project);

		if (map != null)
		{
			processArgs(map);
		}
		compatibilityMode = CESSARPreferencesAccessor.getCompatibilityMode(project);
	}

	/**
	 *
	 */
	private void processArgs(Map<String, Object> map)
	{
		Object obj = map.get(KEY_OUTPUT_FOLDER);
		if (obj != null)
		{
			jetOutputFolder = (IFolder) obj;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.runtime.execution.AbstractCessarTaskManager#canThisManagerWrite()
	 */
	@Override
	protected boolean canThisManagerWrite()
	{
		return false;
	}

	/**
	 * Generates an {@code IStatus} list from a potentially {@code MultiStatus} {@code IStatus}.
	 *
	 * @param status
	 *        the {@code IStatus}
	 * @return the {@code IStatus} list
	 */
	private static List<IStatus> unwindStatuses(IStatus status)
	{
		List<IStatus> result = new ArrayList<IStatus>();
		IStatus current = status;
		result.add(current);

		for (IStatus nextStatus: current.getChildren())
		{
			result.add(nextStatus);
		}

		return result;
	}

	/**
	 * Handle PM preparation errors.
	 *
	 * Outputs ERROR log messages on the console at generation time.
	 *
	 * @param pmInitStatus
	 *        the PM generation status
	 */
	@Requirement(
		reqID = "REQ_GENERATION#1")
	private void handlePMErrors(IStatus pmInitStatus)
	{
		if (null != pmInitStatus)
		{
			if (Status.OK_STATUS != pmInitStatus)
			{
				for (IStatus status: unwindStatuses(pmInitStatus))
				{
					getLogger().error(status.getMessage());
				}
			}
		}
	}

	/**
	 * @return
	 */
	@Requirement(
		reqID = "REQ_GENERATION#1")
	private EObject getPresentationModel()
	{
		if (presentationModel == null)
		{
			getLogger().info("Preparing presentation model..."); //$NON-NLS-1$
			IEcucPresentationModel pmModel = IEcucCore.INSTANCE.getEcucPresentationModel(getProject());
			presentationModel = pmModel.getPMModelRoot();

			initStatus = pmModel.getInitStatus();
			handlePMErrors(initStatus);

			getLogger().info("Presentation model ready\n"); //$NON-NLS-1$
		}
		return presentationModel;
	}

	@Override
	protected List<CessarTask<IFile>> createCessarTask(IFile input, Object parameter)
	{
		IFolder outputFolder;
		if (jetOutputFolder == null)
		{
			outputFolder = ProjectUtils.getOutputFolder(input.getProject(), input);
		}
		else
		{
			outputFolder = jetOutputFolder;
		}

		List<CessarTask<IFile>> result = new ArrayList<CessarTask<IFile>>();
		switch (compatibilityMode)
		{
			case NONE:
				result.add(new JetTask(this, input.getName(), input, getExecutionLoader().getClassLoader(),
					outputFolder, getPresentationModel(), initStatus));
				break;
			case FULL:
				result.add(new CompatibleJetTask(this, input.getName(), input, getExecutionLoader().getClassLoader(),
					outputFolder, getPresentationModel()));
				break;
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.runtime.execution.AbstractJobBasedTaskManager#doPreExecute()
	 */
	@Override
	protected void doPreExecute() throws CoreException
	{
		super.doPreExecute();
		if (CodegenPreferencesAccessor.isUsingCustomOutputFolder(getProject()))
		{
			try
			{
				// IPath linkFolder =
				// CodegenPreferencesAccessor.getResolvedCustomOutputFolderLink(getProject());
				IPath outputFolder = CodegenPreferencesAccessor.getResolvedCustomOutputFolder(getProject());
				String message = "Generating code into folder " //$NON-NLS-1$
					+ outputFolder;
				IFolder folder = getProject().getParent().getFolder(outputFolder);
				if (folder.isLinked())
				{
					message += " linked to system folder " //$NON-NLS-1$
						+ folder.getLocation().toOSString();
				}
				getLogger().info(message);
			}
			catch (CoreException e)
			{
				getLogger().error("Error while getting ouput folder :" + e.getMessage()); //$NON-NLS-1$
				getLogger().error("Please check project settings"); //$NON-NLS-1$
				throw e;
			}
		}
		else
		{
			getLogger().info("Generating code into individual folders."); //$NON-NLS-1$
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.runtime.execution.AbstractJobBasedTaskManager#doPostExecute()
	 */
	@Override
	protected void doPostExecute()
	{
		super.doPostExecute();
		presentationModel = null;
	}
}
