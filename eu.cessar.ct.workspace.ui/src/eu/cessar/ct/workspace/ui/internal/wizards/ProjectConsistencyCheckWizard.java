/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidw8762 Mon Feb 24 15:21:19 2014 </copyright>
 */
package eu.cessar.ct.workspace.ui.internal.wizards;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Display;

import eu.cessar.ct.workspace.WorkspaceConstants;
import eu.cessar.ct.workspace.consistencycheck.IConsistencyCheckResult;
import eu.cessar.ct.workspace.consistencycheck.IProjectCheckInconsistency;
import eu.cessar.ct.workspace.consistencycheck.IProjectConsistencyCheckerManager;
import eu.cessar.ct.workspace.consistencycheck.ProjectConsistencyCheckerManager;
import eu.cessar.ct.workspace.ui.internal.CessarPluginActivator;
import eu.cessar.ct.workspace.ui.internal.Messages;
import eu.cessar.ct.workspace.ui.internal.consistencychecker.ProjectConsistencyReportPage;
import eu.cessar.ct.workspace.ui.internal.consistencychecker.ProjectConsistencySettingsPage;
import eu.cessar.req.Requirement;

/**
 * ProjectConsistencyCheckWizard is a wizard that provides the possibility to choose the consistency checks to perform
 * and presents a report page with the identified project inconsistencies.
 * 
 * @author uidw8762
 * 
 *         %created_by: uidu8153 %
 * 
 *         %date_created: Tue Jun 23 10:51:10 2015 %
 * 
 *         %version: RAUTOSAR~11 %
 * 
 */
@Requirement(
	reqID = "REQ_CHECK#4")
public class ProjectConsistencyCheckWizard extends Wizard
{
	private ProjectConsistencySettingsPage projectConsistencySettingsPage;
	private ProjectConsistencyReportPage projectConsistencyReportPage;

	private final IProject project;

	/** The project inconsistencies results list. */
	private List<IConsistencyCheckResult<IProjectCheckInconsistency>> projectConsistencyCheckResults;

	/**
	 * Instantiates a new project consistency check wizard.
	 * 
	 * @param project
	 *        the project
	 * @param projectConsistencyCheckResults
	 *        the project consistency check results
	 */
	public ProjectConsistencyCheckWizard(IProject project)
	{
		this.project = project;
		setNeedsProgressMonitor(true);
		setWindowTitle(Messages.ProjectConsistencyCheckWizard_title);
		setHelpAvailable(false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.wizard.Wizard#getNextPage(org.eclipse.jface.wizard.IWizardPage)
	 */
	@Override
	public IWizardPage getNextPage(IWizardPage page)
	{
		if (page == projectConsistencyReportPage)
		{
			getProjectConsistencyCheckResults(projectConsistencySettingsPage.isCheckForDuplicateModuleDefinitions(),
				projectConsistencySettingsPage.isCheckForWrongMetamodelFiles(),
				projectConsistencySettingsPage.isCheckForJetVerification(),
				projectConsistencySettingsPage.isCheckForProjectVerification(),
				projectConsistencySettingsPage.isCheckForClassPathVerification(),
				projectConsistencySettingsPage.isCheckForSettingsVerification(),
				projectConsistencySettingsPage.isCheckForPmbinFolderVerification());

			projectConsistencyReportPage.setProjectConsistencyCheckResults(projectConsistencyCheckResults);
		}
		return super.getNextPage(page);
	}

	/**
	 * Invoke manager and display report.
	 * 
	 * @param checkForDuplicateModuleDefinitions
	 *        the check for duplicate module definitions
	 * @param checkForWrongMetamodelFiles
	 *        the check for wrong meta-model files
	 * @param checkForJetVerification
	 *        the check for JetVerification
	 * @param checkProjectVerification
	 *        the check for Project Verification
	 * @param checkClassPathVerification
	 *        the check for Classpath Verification
	 * @param checkSettingsVerification
	 *        the check for Settings Verification
	 * @param checkPmbinFolderVerification
	 *        the check for pmbin folder Verification
	 */
	private void getProjectConsistencyCheckResults(final boolean checkForDuplicateModuleDefinitions,
		final boolean checkForWrongMetamodelFiles, final boolean checkForJetVerification,
		final boolean checkProjectVerification, final boolean checkClassPathVerification,
		final boolean checkSettingsVerification, final boolean checkPmbinFolderVerification)
	{
		final IProjectConsistencyCheckerManager manager = new ProjectConsistencyCheckerManager();

		if (projectConsistencyCheckResults != null)
		{
			projectConsistencyCheckResults.clear();
			projectConsistencyCheckResults = null;
		}

		projectConsistencyCheckResults = new ArrayList<IConsistencyCheckResult<IProjectCheckInconsistency>>();

		try
		{
			ProgressMonitorDialog monDialog = new ProgressMonitorDialog(Display.getCurrent().getActiveShell());
			monDialog.run(true, true, new IRunnableWithProgress()
			{
				public void run(final IProgressMonitor monitor) throws InvocationTargetException, InterruptedException
				{
					if (checkForDuplicateModuleDefinitions)
					{
						projectConsistencyCheckResults.addAll(manager.performConsistencyCheck(project,
							WorkspaceConstants.CHECK_DUPLICATE_MODULE_DEF_ID, null));
					}

					if (checkForWrongMetamodelFiles)
					{
						projectConsistencyCheckResults.addAll(manager.performConsistencyCheck(project,
							WorkspaceConstants.CHECK_WRONG_MODEL_ID, null));
					}

					if (checkForJetVerification)
					{
						projectConsistencyCheckResults.addAll(manager.performConsistencyCheck(project,
							WorkspaceConstants.CHECK_JET_VERIFICATION_ID, null));
					}

					if (checkProjectVerification)
					{
						projectConsistencyCheckResults.addAll(manager.performConsistencyCheck(project,
							WorkspaceConstants.CHECK_PROJECT_VERIFICATION_ID, null));
					}
					if (checkClassPathVerification)
					{
						projectConsistencyCheckResults.addAll(manager.performConsistencyCheck(project,
							WorkspaceConstants.CHECK_CLASSPATH_VERIFICATION_ID, null));
					}
					if (checkSettingsVerification)
					{
						projectConsistencyCheckResults.addAll(manager.performConsistencyCheck(project,
							WorkspaceConstants.CHECK_SETTINGS_VERIFICATION_ID, null));
					}
					if (checkPmbinFolderVerification)
					{
						projectConsistencyCheckResults.addAll(manager.performConsistencyCheck(project,
							WorkspaceConstants.CHECK_PMBIN_VERIFICATION_ID, null));
					}

				}
			});

			boolean canceled = monDialog.getProgressMonitor().isCanceled();
			if (canceled)
			{
				return;
			}
		}
		catch (Exception e) // SUPPRESS CHECKSTYLE OK
		{
			CessarPluginActivator.getDefault().logError(e);
			return;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.wizard.Wizard#canFinish()
	 */
	@Override
	public boolean canFinish()
	{
		return projectConsistencySettingsPage.canFlipToNextPage();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.wizard.Wizard#addPages()
	 */
	@Override
	public void addPages()
	{
		projectConsistencySettingsPage = new ProjectConsistencySettingsPage(
			Messages.ProjectConsistencySettingsPage_title, project);
		addPage(projectConsistencySettingsPage);

		projectConsistencyReportPage = new ProjectConsistencyReportPage(Messages.ProjectConsistencyReportPage_title,
			project);
		addPage(projectConsistencyReportPage);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.wizard.Wizard#performFinish()
	 */
	@Override
	public boolean performFinish()
	{
		return true;
	}
}
