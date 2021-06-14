package eu.cessar.ct.workspace.internal.consistencycheck.impl;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.Status;

import eu.cessar.ct.workspace.consistencycheck.EProjectInconsistencyType;
import eu.cessar.ct.workspace.consistencycheck.ESeverity;
import eu.cessar.ct.workspace.consistencycheck.IConsistencyCheckResult;
import eu.cessar.ct.workspace.consistencycheck.IProjectCheckInconsistency;
import eu.cessar.ct.workspace.consistencycheck.IProjectConsistencyChecker;
import eu.cessar.ct.workspace.internal.CessarPluginActivator;
import eu.cessar.ct.workspace.internal.Messages;

/**
 * Checks if pmbin folder exists inside the project
 * 
 * @author uidu8153
 * 
 *         %created_by: uidu8153 %
 * 
 *         %date_created: Thu Jun 25 08:53:30 2015 %
 * 
 *         %version: 2 %
 */
public class CheckForPmbinFolderVerification implements IProjectConsistencyChecker
{

	@Override
	public List<IConsistencyCheckResult<IProjectCheckInconsistency>> performConsistencyCheck(IProject project)
	{
		List<IConsistencyCheckResult<IProjectCheckInconsistency>> resultList = new ArrayList<IConsistencyCheckResult<IProjectCheckInconsistency>>();

		List<IProjectCheckInconsistency> projectCheckList = getPmbinVerificationCheckResults(project);

		IConsistencyCheckResult<IProjectCheckInconsistency> checkResult = new ProjectConsistencyCheckResult();
		checkResult.setInconsistencies(projectCheckList);
		checkResult.setStatus(Status.OK_STATUS);
		resultList.add(checkResult);

		return resultList;
	}

	/**
	 * Collects pmbin results
	 * 
	 * @param project
	 * @return the results in a List
	 */
	private List<IProjectCheckInconsistency> getPmbinVerificationCheckResults(IProject project)
	{

		List<IProjectCheckInconsistency> checkList = new ArrayList<IProjectCheckInconsistency>();
		ProjectCheckInconsistency projectCheckInconsistency = null;
		try
		{
			final IFolder outputFolder = project.getFolder(Messages.CheckForPmbinFolderVerification_pmbin_FolderName);

			if (!outputFolder.exists())
			{
				projectCheckInconsistency = new ProjectCheckInconsistency();
				projectCheckInconsistency.setSeverity(ESeverity.WARNING);

				addInconsistencyForPmbin(checkList, projectCheckInconsistency);
			}
		}
		catch (Exception e)
		{
			CessarPluginActivator.getDefault().logError(e);
		}
		return checkList;

	}

	/**
	 * Add an inconsistency for the pmbin
	 * 
	 * @param checkList
	 * @param projectCheckInconsistency
	 */
	private void addInconsistencyForPmbin(List<IProjectCheckInconsistency> checkList,
		ProjectCheckInconsistency projectCheckInconsistency)
	{

		projectCheckInconsistency.setInconsistencyType(EProjectInconsistencyType.PMBIN_PROBLEMS);
		projectCheckInconsistency.setMessage(Messages.CheckForPmbinFolderVerification_error_pmbinMissing);
		checkList.add(projectCheckInconsistency);
	}
}
