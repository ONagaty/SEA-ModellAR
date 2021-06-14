package eu.cessar.ct.pluget.debug.ui.internal;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;

import eu.cessar.ct.pluget.debug.PlugetDebugConstants;
import eu.cessar.ct.pluget.debug.ui.PlugetDebugUIConstants;
import eu.cessar.ct.runtime.ui.execution.AbstractCessarLaunchConfigurationDelegate;

public class PlugetLaunchConfigurationDelegate extends AbstractCessarLaunchConfigurationDelegate
{
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.pde.ui.launcher.AbstractPDELaunchConfiguration#launch(org.eclipse.debug.core.ILaunchConfiguration, java.lang.String, org.eclipse.debug.core.ILaunch, org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	public void launch(final ILaunchConfiguration configuration, final String mode,
		final ILaunch launch, final IProgressMonitor monitor) throws CoreException
	{
		// String projectName =
		// configuration.getAttribute(PlugetDebugConstants.PLUGET_DEBUG_PROJECT,
		// "");
		// String plugetName =
		// configuration.getAttribute(PlugetDebugConstants.PLUGET_DEBUG_CLASS,
		// "");

		customizeLaunchConfiguration(configuration);

		// updateSourceLocator(projectName, launch);

		super.launch(configuration, mode, launch, monitor);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.pde.ui.launcher.EclipseApplicationLaunchConfiguration#getProgramArguments(org.eclipse.debug.core.ILaunchConfiguration)
	 */
	@Override
	public String[] getProgramArguments(final ILaunchConfiguration configuration)
		throws CoreException
	{
		List<String> programArgs = getInitialProgramArguments(configuration);
		String projectName = configuration.getAttribute(PlugetDebugConstants.PLUGET_DEBUG_PROJECT,
			"");
		String plugetName = configuration.getAttribute(PlugetDebugConstants.PLUGET_DEBUG_PLUGET, "");
		String plugetClassName = configuration.getAttribute(
			PlugetDebugConstants.PLUGET_DEBUG_CLASS, "");
		boolean isFileLauncher = configuration.getAttribute(
			PlugetDebugConstants.PLUGET_DEBUG_IS_FILE_LAUNCHER, true);
		programArgs.add("-project");
		programArgs.add(projectName);
		if (isFileLauncher)
		{
			programArgs.add("-pluget");
			programArgs.add(plugetName);
		}
		else
		{
			programArgs.add("-class");
			programArgs.add(plugetClassName);
		}

		return programArgs.toArray(new String[programArgs.size()]);
	}

	/**
	 * @param configuration
	 * @return
	 * @throws CoreException
	 */
	protected List<String> getInitialProgramArguments(final ILaunchConfiguration configuration)
		throws CoreException
	{
		List<String> programArgs = new ArrayList<String>();
		String[] args = super.getProgramArguments(configuration);
		for (int i = 0; i < args.length; i++)
		{
			programArgs.add(args[i]);
		}
		return programArgs;
	}

	// /**
	// * @param projectStr
	// * @param launch
	// */
	// private void updateSourceLocator(final String projectStr, final ILaunch
	// launch)
	// {
	// if (launch.getSourceLocator() == null)
	// {
	// Activator.getDefault().logWarning("SourceLocator is null");
	//
	// }
	// else if (launch.getSourceLocator() instanceof ISourceLookupDirector)
	// {
	// IProject project =
	// ResourcesPlugin.getWorkspace().getRoot().getProject(projectStr);
	// IJavaProject javaProject = JavaCore.create(project);
	// ISourceLookupDirector director = (ISourceLookupDirector)
	// launch.getSourceLocator();
	// List<ISourceContainer> containers = new ArrayList<ISourceContainer>();
	// Collections.addAll(containers, director.getSourceContainers());
	// containers.add(new JavaProjectSourceContainer(javaProject));
	// director.setSourceContainers(containers.toArray(new
	// ISourceContainer[containers.size()]));
	// }
	// else
	// {
	// Activator.getDefault().logWarning("SourceLocator is an ISourceLookupDirector, is an "
	// + launch.getSourceLocator().getClass().getName());
	// }
	// }

	@Override
	protected String getApplicationAttribute()
	{
		return PlugetDebugUIConstants.PLUGGET_RUNNER_APPLICATION;
	}
}
