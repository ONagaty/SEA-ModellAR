package eu.cessar.ct.runtime.ecuc.ui.internal.menu;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;

import eu.cessar.ct.runtime.ecuc.ui.internal.CessarPluginActivator;
import eu.cessar.ct.sdk.runtime.ExecutionService;
import eu.cessar.ct.sdk.runtime.ICessarTaskManager;

public class RunPlugetClassAction extends AbstractRunPlugetAction
{

	private String plugetClass;
	private IProject project;

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.ui.IActionDelegate#selectionChanged(org.eclipse.jface.action.IAction,
	 * org.eclipse.jface.viewers.ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection)
	{
		plugetClass = null;
		project = null;
		if (selection instanceof IStructuredSelection)
		{
			Object element = ((IStructuredSelection) selection).getFirstElement();
			if (element instanceof ICompilationUnit)
			{
				try
				{
					ICompilationUnit icu = (ICompilationUnit) element;
					IType type = icu.findPrimaryType();
					if (type != null && !Flags.isAbstract(type.getFlags()) && Flags.isPublic(type.getFlags()))
					{
						plugetClass = type.getFullyQualifiedName();
						project = icu.getJavaProject().getProject();
					}
				}
				catch (JavaModelException e)
				{
					// log and ignore
					CessarPluginActivator.getDefault().logError(e);
				}
			}
		}
		if (action != null)
		{
			action.setEnabled(plugetClass != null);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.runtime.ecuc.ui.internal.menu.AbstractRunPlugetAction#runPlugets(org.eclipse.core.runtime.
	 * IProgressMonitor, java.lang.String[])
	 */
	@Override
	protected void runPlugets(IProgressMonitor monitor, String[] arguments)
	{

		monitor.beginTask("Executing pluget " + plugetClass, 1000);

		try
		{

			// @SuppressWarnings("unchecked")
			ICessarTaskManager<String> manager = (ICessarTaskManager<String>) ExecutionService.createManager(project,
				ExecutionService.TASK_TYPE_PLUGET_CLASS);
			manager.initialize(plugetClass);
			manager.execute(false, arguments, new SubProgressMonitor(monitor, 1000));
		}
		finally
		{
			monitor.done();
		}

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.runtime.ecuc.ui.internal.menu.AbstractRunPlugetAction#getProject()
	 */
	@Override
	public IProject getProject()
	{
		// TODO Auto-generated method stub
		if (project != null)
		{
			return project;
		}
		return null;

	}

}
