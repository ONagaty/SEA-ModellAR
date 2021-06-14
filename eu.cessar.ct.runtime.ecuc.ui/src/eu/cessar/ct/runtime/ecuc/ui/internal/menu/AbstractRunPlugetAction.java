/**
 * 
 */
package eu.cessar.ct.runtime.ecuc.ui.internal.menu;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;

import eu.cessar.ct.runtime.ecuc.ui.internal.CessarPluginActivator;

/**
 * @author uidl6458
 * 
 */
public abstract class AbstractRunPlugetAction implements IObjectActionDelegate
{

	private Shell parentShell;

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IObjectActionDelegate#setActivePart(org.eclipse.jface.action.IAction, org.eclipse.ui.IWorkbenchPart)
	 */
	public void setActivePart(final IAction action, final IWorkbenchPart targetPart)
	{
		parentShell = targetPart.getSite().getWorkbenchWindow().getShell();

	}

	/**
	 * @return
	 */
	protected Shell getShell()
	{
		return parentShell;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IActionDelegate#run(org.eclipse.jface.action.IAction)
	 */
	public void run(final IAction action)
	{
		boolean saveAllEditors = PlatformUI.getWorkbench().saveAllEditors(true);
		if (saveAllEditors)
		{
			final String[] arguments = PlugetHistoryArgManager.getUserInput(getShell(),
				getProject());
			if (arguments == null)
			{
				return;
			}

			try
			{
				new ProgressMonitorDialog(getShell()).run(true, true, new IRunnableWithProgress()
				{
					public void run(final IProgressMonitor monitor)
						throws InvocationTargetException, InterruptedException
					{
						runPlugets(monitor, arguments);
					}
				});
			}
			catch (Exception e)
			{
				CessarPluginActivator.getDefault().logError(e);
			}
		}
	}

	public abstract IProject getProject();

	/**
	 * @param monitor
	 * @param arguments
	 */
	protected abstract void runPlugets(IProgressMonitor monitor, String[] arguments);
}
