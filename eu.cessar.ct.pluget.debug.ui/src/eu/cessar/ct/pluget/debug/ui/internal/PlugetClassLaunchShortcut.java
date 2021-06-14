package eu.cessar.ct.pluget.debug.ui.internal;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IEditorPart;

import eu.cessar.ct.pluget.debug.PlugetDebugConstants;
import eu.cessar.ct.pluget.debug.ui.PlugetDebugUIConstants;
import eu.cessar.ct.runtime.ui.execution.AbstractCessarLaunchShortcut;

public class PlugetClassLaunchShortcut extends AbstractCessarLaunchShortcut
{
	private ICompilationUnit classFile;
	
	
	/* (non-Javadoc)
	 * @see eu.cessar.ct.runtime.ecuc.ui.execution.AbstractCessarLaunchShortcut#getFileAttribute()
	 */
	@Override
	protected String getFileAttribute() {
		return null;
	}
	
	/* (non-Javadoc)
	 * @see eu.cessar.ct.runtime.ecuc.ui.execution.AbstractCessarLaunchShortcut#getClassAttribute()
	 */
	@Override
	protected String getClassAttribute() 
	{
		return PlugetDebugConstants.PLUGET_DEBUG_CLASS;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.pde.ui.launcher.AbstractLaunchShortcut#getLaunchConfigurationTypeName()
	 */
	@Override
	protected String getLaunchConfigurationTypeName()
	{
		return PlugetDebugUIConstants.PLUGET_DEBUG_LAUNCH_CFG_TYPE_NAME;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.runtime.ecuc.ui.execution.AbstractCessarLaunchShortcut#getProjectAttribute()
	 */
	@Override
	protected String getProjectAttribute() 
	{
		return PlugetDebugConstants.PLUGET_DEBUG_PROJECT;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.runtime.ecuc.ui.execution.AbstractCessarLaunchShortcut#getSourceLocatorAttribute()
	 */
	@Override
	protected String getSourceLocatorAttribute()
	{
		return PlugetDebugConstants.PLUGET_DEBUG_SOURCE_LOCATOR_ID;

	}
	
	@Override
	protected String getName(ILaunchConfigurationType type) 
	{
		if(classFile != null)
		{
			return "Debug " + classFile.findPrimaryType().getFullyQualifiedName();
		}
		return super.getName(type);
	}
	
	@Override
	protected void initializeConfiguration(ILaunchConfigurationWorkingCopy wc) 
	{
		super.initializeConfiguration(wc);
		if(classFile != null)
		{
			wc.setAttribute(getClassAttribute(), classFile.findPrimaryType().getFullyQualifiedName());
			wc.setAttribute(PlugetDebugConstants.PLUGET_DEBUG_IS_FILE_LAUNCHER, false);
		}
	}
	
	@Override
	protected boolean isGoodMatch(ILaunchConfiguration configuration)
	{
		boolean goodMatch = super.isGoodMatch(configuration);
		if (!goodMatch)
		{
			return false;
		}
		if (classFile == null)
		{
			return false;
		}
		String attr;
		try
		{
			attr = configuration.getAttribute(getClassAttribute(), ""); //$NON-NLS-1$
			if (!attr.equals(classFile.findPrimaryType().getFullyQualifiedName()))
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

	public void launch(ISelection selection, String mode) 
	{
		if (selection instanceof IStructuredSelection)
		{
			Object selected = ((IStructuredSelection) selection).getFirstElement();
			assert(selected instanceof ICompilationUnit);
			ICompilationUnit compilationUnit = (ICompilationUnit) selected;
			IType type = compilationUnit.findPrimaryType();
			try {
				if (type != null && !Flags.isAbstract(type.getFlags())
						&& Flags.isPublic(type.getFlags()))
				{
					classFile = compilationUnit;
					setProject(classFile.getJavaProject().getProject());
				}
			} catch (JavaModelException e) {
				CessarPluginActivator.getDefault().logError(e);
			}
		}
		launch(mode);
	}

	public void launch(IEditorPart editor, String mode) 
	{
		IProject cessarProject = getProject();
		cessarProject = null;
		classFile = null;
		launch(mode);
		
	}

}
