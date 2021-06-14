package eu.cessar.ct.workspace.internal.consistencycheck.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Status;

import eu.cessar.ct.sdk.logging.LoggerFactory;
import eu.cessar.ct.workspace.consistencycheck.EProjectInconsistencyType;
import eu.cessar.ct.workspace.consistencycheck.ESeverity;
import eu.cessar.ct.workspace.consistencycheck.IConsistencyCheckResult;
import eu.cessar.ct.workspace.consistencycheck.IProjectCheckInconsistency;
import eu.cessar.ct.workspace.consistencycheck.IProjectConsistencyChecker;
import eu.cessar.ct.workspace.internal.CessarPluginActivator;
import eu.cessar.ct.workspace.internal.Messages;

/**
 * TODO: Please comment this class
 * 
 * @author uidu8153
 * 
 *         %created_by: uidu8153 %
 * 
 *         %date_created: Tue Jun 23 10:47:03 2015 %
 * 
 *         %version: 1 %
 */
public class CheckForSettingsVerification implements IProjectConsistencyChecker
{

	@Override
	public List<IConsistencyCheckResult<IProjectCheckInconsistency>> performConsistencyCheck(IProject project)
	{
		List<IConsistencyCheckResult<IProjectCheckInconsistency>> resultList = new ArrayList<IConsistencyCheckResult<IProjectCheckInconsistency>>();

		List<IProjectCheckInconsistency> projectCheckList = getSettingsVerificationCheckResults(project);

		IConsistencyCheckResult<IProjectCheckInconsistency> checkResult = new ProjectConsistencyCheckResult();
		checkResult.setInconsistencies(projectCheckList);
		checkResult.setStatus(Status.OK_STATUS);
		resultList.add(checkResult);

		return resultList;
	}

	private List<IProjectCheckInconsistency> getSettingsVerificationCheckResults(IProject project)
	{
		List<IProjectCheckInconsistency> checkList = new ArrayList<IProjectCheckInconsistency>();
		ProjectCheckInconsistency projectCheckInconsistency = null;
		try
		{
			boolean checkifRequiredInformationFound = false;
			final IFolder outputFolder = project.getFolder(Messages.CheckForSettingsVerification_setting_FolderName);

			if (outputFolder.exists())
			{

				checkIfSettingsHasAutosarVersionSetonfiguration(outputFolder, projectCheckInconsistency, checkList,
					checkifRequiredInformationFound);
				checkIfSettingHasConfigurationSet(outputFolder, projectCheckInconsistency, checkList,
					checkifRequiredInformationFound);
			}
			else
			{
				projectCheckInconsistency = new ProjectCheckInconsistency();
				projectCheckInconsistency.setSeverity(ESeverity.WARNING);
				projectCheckInconsistency.setInconsistencyType(EProjectInconsistencyType.SETTINGS_PROBLEMS);
				projectCheckInconsistency.setMessage(Messages.CheckForSettingsVerification_error_settings);
				checkList.add(projectCheckInconsistency);
			}

		}
		catch (Exception e)
		{
			CessarPluginActivator.getDefault().logError(e);
		}
		return checkList;

	}

	/**
	 * Add an inconsistency for the .settings
	 * 
	 * @param checkList
	 * @param projectCheckInconsistency
	 * @param file
	 * @param message
	 */
	private void addInconsistencyForSettings(List<IProjectCheckInconsistency> checkList,
		ProjectCheckInconsistency projectCheckInconsistency, IFile file, String message)
	{
		projectCheckInconsistency.addFile(file);
		projectCheckInconsistency.setInconsistencyType(EProjectInconsistencyType.SETTINGS_PROBLEMS);
		projectCheckInconsistency.setMessage(message);
		checkList.add(projectCheckInconsistency);
	}

	/**
	 * @param outputFolder
	 * @param projectCheckInconsistency
	 * @param checkList
	 * @param checkifAutosarVersionExists
	 */
	private void checkIfSettingsHasAutosarVersionSetonfiguration(IFolder outputFolder,
		ProjectCheckInconsistency projectCheckInconsistency, List<IProjectCheckInconsistency> checkList,
		boolean checkifAutosarVersionExists)
	{
		IFile file = outputFolder.getFile(Messages.CheckForSettingsVerification_artop_Preference);

		InputStream contents;
		try
		{
			contents = file.getContents();
			BufferedReader reader = new BufferedReader(new InputStreamReader(contents));

			String line;

			String regex = "autosar_release=org.artop.aal.autosar" + "\\d.*"; //$NON-NLS-1$ //$NON-NLS-2$
			while ((line = reader.readLine()) != null)
			{

				if (line.matches(regex))
				{
					checkifAutosarVersionExists = true;
				}

			}

			if (!checkifAutosarVersionExists)
			{
				projectCheckInconsistency = new ProjectCheckInconsistency();
				projectCheckInconsistency.setSeverity(ESeverity.WARNING);

				addInconsistencyForSettings(checkList, projectCheckInconsistency, file,
					Messages.CheckForSettingsVerification_error_AutosarVersion);
			}

			reader.close();

		}
		catch (CoreException e)
		{
			LoggerFactory.getLogger().error(e.getMessage());
		}
		catch (IOException e)
		{
			LoggerFactory.getLogger().error(e.getMessage());
		}

	}

	private void checkIfSettingHasConfigurationSet(IFolder outputFolder,
		ProjectCheckInconsistency projectCheckInconsistency, List<IProjectCheckInconsistency> checkList,
		boolean checkifRequiredInformationFound)
	{
		IFile file = outputFolder.getFile(Messages.CheckForSettingsVerification_core_Preferences);

		InputStream contents;
		try
		{
			contents = file.getContents();
			BufferedReader reader = new BufferedReader(new InputStreamReader(contents));

			String line;

			String regex = "configuration.variant=" + "\\w.*"; //$NON-NLS-1$ //$NON-NLS-2$
			while ((line = reader.readLine()) != null)
			{

				if (line.matches(regex))
				{
					checkifRequiredInformationFound = true;
				}

			}

			if (!checkifRequiredInformationFound)
			{
				projectCheckInconsistency = new ProjectCheckInconsistency();
				projectCheckInconsistency.setSeverity(ESeverity.WARNING);

				addInconsistencyForSettings(checkList, projectCheckInconsistency, file,
					Messages.CheckForSettingsVerification_error_ConfigurationVariant);
			}

			reader.close();

		}
		catch (CoreException e)
		{
			LoggerFactory.getLogger().error(e.getMessage());
		}
		catch (IOException e)
		{
			LoggerFactory.getLogger().error(e.getMessage());
		}

	}

}
