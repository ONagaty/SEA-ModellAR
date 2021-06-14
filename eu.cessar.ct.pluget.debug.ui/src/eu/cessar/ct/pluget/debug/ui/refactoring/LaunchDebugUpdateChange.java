/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidu0944 Sep 21, 2011 3:28:15 PM </copyright>
 */
package eu.cessar.ct.pluget.debug.ui.refactoring;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.CompositeChange;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;

import eu.cessar.ct.pluget.debug.PlugetDebugConstants;

/**
 * @author uidu0944
 * 
 */
public class LaunchDebugUpdateChange extends Change
{

	private static String CESSAR_DEBUG_CONFIGURATION = "Update Pluget debug configuration"; //$NON-NLS-1$
	private static String ID_PLUGET_CONFIGURATION_TYPE = "eu.cessar.ct.pluget.debug.ui.launchConfigurationType"; //$NON-NLS-1$

	private String fNewConfigContainerName;
	private ILaunchConfigurationWorkingCopy fNewLaunchConfiguration;
	private ILaunchConfiguration fLaunchConfiguration;
	private String fNewProjectName;

	protected LaunchDebugUpdateChange(ILaunchConfiguration launchConfiguration,
		String newProjectName, boolean undo) throws CoreException
	{
		fLaunchConfiguration = launchConfiguration;
		fNewLaunchConfiguration = launchConfiguration.getWorkingCopy();
		fNewProjectName = newProjectName;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ltk.core.refactoring.Change#getName()
	 */
	@Override
	public String getName()
	{
		// TODO Auto-generated method stub
		return CESSAR_DEBUG_CONFIGURATION;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ltk.core.refactoring.Change#initializeValidationData(org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	public void initializeValidationData(IProgressMonitor pm)
	{
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.eclipse.ltk.core.refactoring.Change#isValid(org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	public RefactoringStatus isValid(IProgressMonitor pm) throws CoreException,
		OperationCanceledException
	{
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ltk.core.refactoring.Change#perform(org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	public Change perform(IProgressMonitor pm) throws CoreException
	{
		fNewLaunchConfiguration.setAttribute(PlugetDebugConstants.PLUGET_DEBUG_PROJECT,
			fNewProjectName);

		fNewLaunchConfiguration.doSave();

		return new LaunchDebugUpdateChange(fNewLaunchConfiguration, fNewProjectName, true);
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
	@SuppressWarnings("deprecation")
	public static Change createChangesForProjectRename(IProject project, String newProjectName)
		throws CoreException
	{
		String projectName = project.getDescription().getName();

		ILaunchConfiguration[] configs = getAntLaunchConfigurations();

		List changes = createChangesForProjectRename(configs, projectName, newProjectName);
		return createChangeFromList(changes, CESSAR_DEBUG_CONFIGURATION);
	}

	private static ILaunchConfiguration[] getAntLaunchConfigurations() throws CoreException
	{
		ILaunchManager manager = DebugPlugin.getDefault().getLaunchManager();
		// Ant launch configurations
		ILaunchConfigurationType configurationType = manager.getLaunchConfigurationType(ID_PLUGET_CONFIGURATION_TYPE);
		ILaunchConfiguration[] configs = manager.getLaunchConfigurations(configurationType);
		return configs;
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
			ILaunchConfiguration launchConfiguration = configs[i]; // launchConfiguration.getAttributes()
			String launchConfigurationProjectName = launchConfiguration.getAttribute(
				PlugetDebugConstants.PLUGET_DEBUG_PROJECT, (String) null);
			if (projectName.equals(launchConfigurationProjectName))
			{
				LaunchDebugUpdateChange change = new LaunchDebugUpdateChange(launchConfiguration,
					newProjectName, false);
				changes.add(change);
			}
		}
		return changes;
	}

	private static Change createChangeFromList(List changes, String changeLabel)
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
			return new CompositeChange(changeLabel,
				(Change[]) changes.toArray(new Change[changes.size()]));
		}
	}

}
