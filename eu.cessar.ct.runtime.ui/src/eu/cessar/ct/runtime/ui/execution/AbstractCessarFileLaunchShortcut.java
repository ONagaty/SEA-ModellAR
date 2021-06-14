package eu.cessar.ct.runtime.ui.execution;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IEditorPart;

import eu.cessar.ct.runtime.ui.internal.CessarPluginActivator;

/**
 *
 *
 */
public abstract class AbstractCessarFileLaunchShortcut extends AbstractCessarLaunchShortcut
{
	private IFile iFile;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.pde.ui.launcher.AbstractLaunchShortcut#getName(org.eclipse.debug.core.ILaunchConfigurationType)
	 */
	@Override
	protected String getName(final ILaunchConfigurationType type)
	{
		if (iFile != null)
		{
			return "Debug " + iFile.getName(); //$NON-NLS-1$
		}
		return super.getName(type);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.cessar.ct.runtime.ecuc.ui.execution.AbstractCessarLaunchShortcut#initializeConfiguration(org.eclipse.debug
	 * .core.ILaunchConfigurationWorkingCopy)
	 */
	@Override
	protected void initializeConfiguration(ILaunchConfigurationWorkingCopy wc)
	{
		super.initializeConfiguration(wc);
		if (iFile != null)
		{
			wc.setAttribute(getFileAttribute(), iFile.getProjectRelativePath().toPortableString());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.runtime.ecuc.ui.execution.AbstractCessarLaunchShortcut#isGoodMatch(org.eclipse.debug.core.
	 * ILaunchConfiguration)
	 */
	@Override
	protected boolean isGoodMatch(ILaunchConfiguration configuration)
	{
		boolean goodMatch = super.isGoodMatch(configuration);
		if (!goodMatch)
		{
			return false;
		}
		if (iFile == null)
		{
			return false;
		}
		String attr;
		try
		{
			attr = configuration.getAttribute(getFileAttribute(), ""); //$NON-NLS-1$
			if (!attr.equals(iFile.getProjectRelativePath().toPortableString()))
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.ui.ILaunchShortcut#launch(org.eclipse.jface.viewers.ISelection, java.lang.String)
	 */
	public void launch(final ISelection selection, final String mode)
	{
		if (selection instanceof IStructuredSelection)
		{
			Object selected = ((IStructuredSelection) selection).getFirstElement();
			assert (selected instanceof IFile);
			iFile = (IFile) selected;
			setProject(iFile.getProject());
		}
		launch(mode);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.ui.ILaunchShortcut#launch(org.eclipse.ui.IEditorPart, java.lang.String)
	 */
	public void launch(final IEditorPart editor, final String mode)
	{
		IProject cessarProject = getProject();
		cessarProject = null;
		iFile = null;
		launch(mode);
	}

}
