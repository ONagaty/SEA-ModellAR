/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidu0944 Sep 20, 2011 9:26:02 AM </copyright>
 */
package eu.cessar.ct.core.internal.platform.ui.ant.refactoring;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.ant.launching.IAntLaunchConstants;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.CompositeChange;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;

import eu.cessar.ct.core.platform.CESSARPreferencesAccessor;
import eu.cessar.ct.core.platform.ECompatibilityMode;

/**
 * @author uidu0944
 * 
 */
public class LaunchPropertyUpdateChange extends Change
{

	// copied from IExternalToolConstants.ATTR_WORKING_DIRECTORY;
	private static String ATTR_WORKING_DIRECTORY = "org.eclipse.ui.externaltools.ATTR_WORKING_DIRECTORY"; //$NON-NLS-1$

	// copied from IExternalToolConstants.ATTR_LOCATION;
	private static String ATTR_LOCATION = "org.eclipse.ui.externaltools.ATTR_LOCATION"; //$NON-NLS-1$

	private static String CESSAR_ANTUI_REFACT_PROPERTY_UPDATE = "Update Ant \''project.name\'' property to {0}"; //$NON-NLS-1$
	private static String CESSAR_ANTUI_REFACT_PROPERTY_UPDATE_ENTRY = "Update launcher {0}"; //$NON-NLS-1$

	private ILaunchConfiguration fLaunchConfiguration;
	private String fNewBuildfileLocation;
	private String fNewProjectName;
	private static String sNewProjectName;
	private String fNewLaunchConfigurationName;
	private String fOldBuildfileLocation;
	private String fOldProjectName;
	private ILaunchConfigurationWorkingCopy fNewLaunchConfiguration;
	private String fNewConfigContainerName;
	private String launcherName;

	/**
	 * 
	 */
	protected LaunchPropertyUpdateChange(ILaunchConfiguration launchConfiguration,
		String oldBuildFileName, String newBuildfileName, String newProjectName, boolean undo)
		throws CoreException
	{
		fLaunchConfiguration = launchConfiguration;
		fNewLaunchConfiguration = launchConfiguration.getWorkingCopy();
		fNewBuildfileLocation = newBuildfileName;
		fNewProjectName = newProjectName;
		sNewProjectName = newProjectName;
		fOldBuildfileLocation = oldBuildFileName;
		fOldProjectName = fLaunchConfiguration.getAttribute(
			IJavaLaunchConfigurationConstants.ATTR_PROJECT_NAME, (String) null);
		if (fNewBuildfileLocation != null)
		{
			// generate the new configuration name
			String launchConfigurationName = fLaunchConfiguration.getName();
			fNewLaunchConfigurationName = launchConfigurationName.replaceAll(oldBuildFileName,
				newBuildfileName);
			if (launchConfigurationName.equals(fNewLaunchConfigurationName)
				|| (!undo && DebugPlugin.getDefault().getLaunchManager().isExistingLaunchConfigurationName(
					fNewLaunchConfigurationName)))
			{
				fNewLaunchConfigurationName = null;
			}
		}
		launcherName = fNewLaunchConfiguration.getName();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ltk.core.refactoring.Change#getName()
	 */
	@Override
	public String getName()
	{
		return java.text.MessageFormat.format(CESSAR_ANTUI_REFACT_PROPERTY_UPDATE_ENTRY,
			new Object[] {launcherName});
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ltk.core.refactoring.Change#initializeValidationData(org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	public void initializeValidationData(IProgressMonitor pm)
	{
		// do nothing
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ltk.core.refactoring.Change#isValid(org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	public RefactoringStatus isValid(IProgressMonitor pm) throws CoreException,
		OperationCanceledException
	{
		if (fLaunchConfiguration.exists())
		{
			String buildFileLocation = fLaunchConfiguration.getAttribute(ATTR_LOCATION, ""); //$NON-NLS-1$
			if (fOldBuildfileLocation == null
				|| (buildFileLocation.endsWith(fOldBuildfileLocation + '}') || buildFileLocation.endsWith(fOldBuildfileLocation)))
			{
				String projectName = fLaunchConfiguration.getAttribute(
					IJavaLaunchConfigurationConstants.ATTR_PROJECT_NAME, (String) null);
				if (fOldProjectName.equals(projectName))
				{
					return new RefactoringStatus();
				}
				return RefactoringStatus.createWarningStatus(java.text.MessageFormat.format(
					CESSAR_ANTUI_REFACT_PROPERTY_UPDATE,
					new Object[] {fLaunchConfiguration.getName(), fOldProjectName}));
			}
			return RefactoringStatus.createWarningStatus(java.text.MessageFormat.format(
				CESSAR_ANTUI_REFACT_PROPERTY_UPDATE, new Object[] {fLaunchConfiguration.getName(),
					fOldBuildfileLocation}));
		}
		return RefactoringStatus.createFatalErrorStatus(java.text.MessageFormat.format(
			CESSAR_ANTUI_REFACT_PROPERTY_UPDATE, new Object[] {fLaunchConfiguration.getName()}));
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ltk.core.refactoring.Change#perform(org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	public Change perform(IProgressMonitor pm) throws CoreException
	{
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IWorkspaceRoot root = workspace.getRoot();
		IProject project = root.getProject(fNewProjectName);

		if (fNewConfigContainerName != null)
		{
			IContainer container = (IContainer) project.findMember(fNewConfigContainerName);
			fNewLaunchConfiguration.setContainer(container);
		}

		updateLaunchConfigurationBatch(fNewProjectName, project);

		// create the undo change
		return new LaunchPropertyUpdateChange(fNewLaunchConfiguration, fNewBuildfileLocation,
			fOldBuildfileLocation, fOldProjectName, true);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ltk.core.refactoring.Change#getModifiedElement()
	 */
	@Override
	public Object getModifiedElement()
	{
		return fLaunchConfiguration;
	}

	/**
	 * Create a change for each launch configuration which needs to be updated
	 * for this buildfile rename.
	 */
	public static Change createChangesForProjectRename(IProject project, String newProjectName)
		throws CoreException
	{
		String projectName = project.getDescription().getName();

		ILaunchConfiguration[] configs = getAntLaunchConfigurations();

		List<?> changes = createChangesForProjectRename(configs, projectName, newProjectName);
		return createChangeFromList(changes, java.text.MessageFormat.format(
			CESSAR_ANTUI_REFACT_PROPERTY_UPDATE, new Object[] {sNewProjectName}));
	}

	private static ILaunchConfiguration[] getAntLaunchConfigurations() throws CoreException
	{
		ILaunchManager manager = DebugPlugin.getDefault().getLaunchManager();
		// Ant launch configurations
		ILaunchConfigurationType configurationType = manager.getLaunchConfigurationType(IAntLaunchConstants.ID_ANT_LAUNCH_CONFIGURATION_TYPE);
		ILaunchConfiguration[] configs = manager.getLaunchConfigurations(configurationType);
		return configs;
	}

	private void updateLaunchConfigurationBatch(String newPrj, IProject project)
		throws CoreException
	{
		ILaunchManager manager = DebugPlugin.getDefault().getLaunchManager();
		// Ant launch configurations
		ILaunchConfigurationType configurationType = manager.getLaunchConfigurationType(IAntLaunchConstants.ID_ANT_LAUNCH_CONFIGURATION_TYPE);
		ILaunchConfiguration[] configs = manager.getLaunchConfigurations(configurationType);
		if (configs != null && configs.length > 0)
		{
			for (int i = 0; i < configs.length; i++)
			{
				if (newPrj.equals(configs[i].getAttributes().get(
					"org.eclipse.jdt.launching.PROJECT_ATTR")) //$NON-NLS-1$
					&& configs[i].getName().split(" ")[1].equals(fNewLaunchConfiguration.getName().split( //$NON-NLS-1$
						" ")[1])) //$NON-NLS-1$
				{
					updateLaunchConfiguration(configs[i].getWorkingCopy(), project, fNewProjectName);
				}
			}
		}
	}

	/**
	 * Create a change for each launch configuration from the given list which
	 * needs to be updated for this IProject rename.
	 */
	private static List<Change> createChangesForProjectRename(ILaunchConfiguration[] configs,
		String projectName, String newProjectName) throws CoreException
	{
		List<Change> changes = new ArrayList<Change>();
		for (int i = 0; i < configs.length; i++)
		{
			ILaunchConfiguration launchConfiguration = configs[i];
			String launchConfigurationProjectName = launchConfiguration.getAttribute(
				IJavaLaunchConfigurationConstants.ATTR_PROJECT_NAME, (String) null);
			if (projectName.equals(launchConfigurationProjectName))
			{
				LaunchPropertyUpdateChange change = new LaunchPropertyUpdateChange(
					launchConfiguration, null, null, newProjectName, false);
				String newContainerName = computeNewContainerName(launchConfiguration);
				if (newContainerName != null)
				{
					change.setNewContainerName(newContainerName);
				}
				changes.add(change);
			}
		}
		return changes;
	}

	protected void setNewContainerName(String newContainerName)
	{
		fNewConfigContainerName = newContainerName;
	}

	/**
	 * Creates a new container name for the given configuration
	 * 
	 * @param launchConfiguration
	 * @return the new container name
	 */
	private static String computeNewContainerName(ILaunchConfiguration launchConfiguration)
	{
		IFile file = launchConfiguration.getFile();
		if (file != null)
		{
			return file.getParent().getProjectRelativePath().toString();
		}
		return null;
	}

	/**
	 * Take a list of Changes, and return a unique Change, a CompositeChange, or
	 * null.
	 */
	private static Change createChangeFromList(List<?> changes, String changeLabel)
	{
		int nbChanges = changes.size();
		if (nbChanges == 0)
		{
			return null;
		}
		else if (nbChanges == 1)
		{
			return (Change) changes.get(0);
		}
		else
		{
			return new CompositeChange(changeLabel, changes.toArray(new Change[changes.size()]));
		}
	}

	/**
	 * Update the launch configuration with Cessar specific entries
	 * 
	 * @param configuration
	 *        the original launch configuration
	 * @param project
	 *        the project where the ant is located
	 * @return the updated configuration, never null
	 * @throws CoreException
	 *         when configuration cannot be modified
	 */
	protected static ILaunchConfiguration updateLaunchConfiguration(
		ILaunchConfigurationWorkingCopy copy, IProject project, String newProjectName)
		throws CoreException
	{
		// set working directory
		copy.setAttribute(ATTR_WORKING_DIRECTORY, "${workspace_loc:/" //$NON-NLS-1$
			+ project.getName() + "}"); //$NON-NLS-1$

		// set properties
		@SuppressWarnings("unchecked")
		Map<String, String> properties = copy.getAttribute(IAntLaunchConstants.ATTR_ANT_PROPERTIES,
			(Map<String, String>) null);
		if (properties == null)
		{
			properties = new HashMap<String, String>();
		}

		ECompatibilityMode compatibilityMode = CESSARPreferencesAccessor.getCompatibilityMode(project);
		if (compatibilityMode == ECompatibilityMode.NONE)
		{
			properties.put("project.name", newProjectName); //$NON-NLS-1$ 
		}
		else
		{
			properties.put("general.output.dir", "generated"); //$NON-NLS-1$ //$NON-NLS-2$
			properties.put("CESSAR_PROJECT_NAME", newProjectName); //$NON-NLS-1$
			properties.put("project.dir", "${workspace_loc:/" + newProjectName + "}"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		}
		copy.setAttribute(IAntLaunchConstants.ATTR_ANT_PROPERTIES, properties);

		return copy.doSave();
	}
}
