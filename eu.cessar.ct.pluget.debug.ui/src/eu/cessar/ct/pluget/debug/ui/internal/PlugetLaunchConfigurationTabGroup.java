package eu.cessar.ct.pluget.debug.ui.internal;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.ui.AbstractLaunchConfigurationTabGroup;
import org.eclipse.debug.ui.CommonTab;
import org.eclipse.debug.ui.ILaunchConfigurationDialog;
import org.eclipse.debug.ui.ILaunchConfigurationTab;
import org.eclipse.jdt.debug.ui.launchConfigurations.JavaArgumentsTab;
import org.eclipse.jdt.debug.ui.launchConfigurations.JavaJRETab;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;

import eu.cessar.ct.pluget.debug.PlugetDebugConstants;
import eu.cessar.ct.runtime.CessarRuntimeUtils;

public class PlugetLaunchConfigurationTabGroup extends AbstractLaunchConfigurationTabGroup
{

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.ui.ILaunchConfigurationTabGroup#createTabs(org.eclipse.debug.ui.ILaunchConfigurationDialog,
	 *      java.lang.String)
	 */
	public void createTabs(final ILaunchConfigurationDialog dialog, final String mode)
	{
		ILaunchConfigurationTab[] tabs = new ILaunchConfigurationTab[] {new SelectPlugetTab(),
			new JavaArgumentsTab(), new JavaJRETab(), new CommonTab()};
		setTabs(tabs);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.debug.ui.AbstractLaunchConfigurationTabGroup#setDefaults(org.eclipse.debug.core.ILaunchConfigurationWorkingCopy)
	 */
	@Override
	public void setDefaults(final ILaunchConfigurationWorkingCopy configuration)
	{
		super.setDefaults(configuration);
		configuration.setAttribute(ILaunchConfiguration.ATTR_SOURCE_LOCATOR_ID,
			PlugetDebugConstants.PLUGET_DEBUG_SOURCE_LOCATOR_ID);
		configuration.setAttribute(IJavaLaunchConfigurationConstants.ATTR_VM_ARGUMENTS, CessarRuntimeUtils.getVMArgs());
	}
}
