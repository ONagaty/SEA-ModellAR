package eu.cessar.ct.runtime.ui.execution;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.pde.ui.launcher.AbstractLaunchShortcut;
import org.eclipse.ui.PlatformUI;

import eu.cessar.ct.runtime.ui.internal.CessarPluginActivator;

/**
 * An abstract launch shortcut for CESSAR-CT specific applications.
 *
 */
public abstract class AbstractCessarLaunchShortcut extends AbstractLaunchShortcut
{
	private IProject cessarProject;

	/**
	 *
	 * @return
	 */
	protected abstract String getProjectAttribute();

	/**
	 *
	 * @return
	 */
	protected abstract String getFileAttribute();

	/**
	 *
	 * @return
	 */
	protected abstract String getClassAttribute();

	/**
	 *
	 * @return
	 */
	protected abstract String getSourceLocatorAttribute();

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.pde.ui.launcher.AbstractLaunchShortcut#initializeConfiguration(org.eclipse.debug.core.
	 * ILaunchConfigurationWorkingCopy)
	 */
	@Override
	protected void initializeConfiguration(final ILaunchConfigurationWorkingCopy wc)
	{
		if (cessarProject != null)
		{
			wc.setAttribute(getProjectAttribute(), cessarProject.getName());
		}
		wc.setAttribute(ILaunchConfiguration.ATTR_SOURCE_LOCATOR_ID, getSourceLocatorAttribute());

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.pde.ui.launcher.AbstractLaunchShortcut#isGoodMatch(org.eclipse.debug.core.ILaunchConfiguration)
	 */
	@Override
	protected boolean isGoodMatch(final ILaunchConfiguration configuration)
	{
		if (cessarProject == null)
		{
			return false;
		}
		String attr;
		try
		{
			attr = configuration.getAttribute(getProjectAttribute(), ""); //$NON-NLS-1$
			if (!attr.equals(cessarProject.getName()))
			{
				return false;
			}

		}
		catch (CoreException e)
		{
			CessarPluginActivator.getDefault().logError(e);
		}

		return true;
	}

	/**
	 * @return
	 */
	protected IProject getProject()
	{
		return cessarProject;
	}

	/**
	 * @param cessarProject
	 */
	protected void setProject(IProject cessarProject)
	{
		this.cessarProject = cessarProject;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.pde.ui.launcher.AbstractLaunchShortcut#launch(java.lang.String)
	 */
	@Override
	protected void launch(String mode)
	{
		boolean saveAllEditors = PlatformUI.getWorkbench().saveAllEditors(true);
		if (saveAllEditors)
		{
			super.launch(mode);
		}
	}

}
