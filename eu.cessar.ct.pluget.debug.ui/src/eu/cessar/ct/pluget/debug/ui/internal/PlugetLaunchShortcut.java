package eu.cessar.ct.pluget.debug.ui.internal;

import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;

import eu.cessar.ct.pluget.debug.PlugetDebugConstants;
import eu.cessar.ct.pluget.debug.ui.PlugetDebugUIConstants;
import eu.cessar.ct.runtime.ui.execution.AbstractCessarFileLaunchShortcut;

public class PlugetLaunchShortcut extends AbstractCessarFileLaunchShortcut
{

	
	@Override
	protected void initializeConfiguration(ILaunchConfigurationWorkingCopy wc) 
	{
		super.initializeConfiguration(wc);
		wc.setAttribute(PlugetDebugConstants.PLUGET_DEBUG_IS_FILE_LAUNCHER, true);
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.pde.ui.launcher.AbstractLaunchShortcut#getLaunchConfigurationTypeName()
	 */
	/* (non-Javadoc)
	 * @see org.eclipse.pde.ui.launcher.AbstractLaunchShortcut#getLaunchConfigurationTypeName()
	 */
	@Override
	protected String getLaunchConfigurationTypeName()
	{
		return PlugetDebugUIConstants.PLUGET_DEBUG_LAUNCH_CFG_TYPE_NAME;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.runtime.ecuc.ui.execution.AbstractCessarLaunchShortcut#getFileAttribute()
	 */
	@Override
	protected String getFileAttribute() {
		return PlugetDebugConstants.PLUGET_DEBUG_PLUGET;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.runtime.ecuc.ui.execution.AbstractCessarLaunchShortcut#getProjectAttribute()
	 */
	@Override
	protected String getProjectAttribute() {
		return PlugetDebugConstants.PLUGET_DEBUG_PROJECT;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.runtime.ecuc.ui.execution.AbstractCessarLaunchShortcut#getSourceLocatorAttribute()
	 */
	@Override
	protected String getSourceLocatorAttribute() {
		return PlugetDebugConstants.PLUGET_DEBUG_SOURCE_LOCATOR_ID;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.runtime.ecuc.ui.execution.AbstractCessarLaunchShortcut#getClassAttribute()
	 */
	@Override
	protected String getClassAttribute() {
		return null;
	}
}
