package eu.cessar.ct.runtime.ecuc.ui.internal.menu;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.core.runtime.content.IContentType;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.ide.IDE;

import eu.cessar.ct.core.platform.util.ResourceUtils;
import eu.cessar.ct.runtime.CessarRuntime;
import eu.cessar.ct.sdk.runtime.ExecutionService;
import eu.cessar.ct.sdk.runtime.ICessarTaskManager;

/**
 * 
 * This class implements the action behind the <code>Run pluget</code> pop-up
 * menu entry, action that is enabled on <code>+</code> (1 or multiple
 * selection), where a selection represents a jar file having
 * <code>*.pluget</code> extension
 */
public class RunPlugetFileAction extends AbstractRunPlugetAction
{
	private ISelection currentSelection;

	/**
	 * @param monitor
	 * @param arguments
	 */
	@Override
	protected void runPlugets(final IProgressMonitor monitor, final String[] arguments)
	{

		List<IFile> files = extractFilesFromSelection();

		Map<IProject, List<IFile>> map = ResourceUtils.separateByProject(files);

		monitor.beginTask("Executing pluget(s)", 1000 * map.size());

		try
		{

			for (IProject project: map.keySet())
			{
				@SuppressWarnings("unchecked")
				ICessarTaskManager<IFile> manager = (ICessarTaskManager<IFile>) ExecutionService.createManager(
					project, ExecutionService.TASK_TYPE_PLUGET);
				manager.initialize(map.get(project));
				manager.execute(false, arguments, new SubProgressMonitor(monitor, 1000));
			}
		}
		finally
		{
			monitor.done();
		}

	}

	/**
	 * Extract files having *.pluget extension form current selection and
	 * populate internal list
	 */
	private List<IFile> extractFilesFromSelection()
	{
		List<IFile> result = new ArrayList<IFile>();
		if (currentSelection instanceof IStructuredSelection)
		{
			IStructuredSelection strSel = (IStructuredSelection) currentSelection;
			collectFiles(result, strSel.toArray());
		}
		return result;
	}

	/**
	 * 
	 * @param selElements
	 */
	private void collectFiles(List<IFile> result, final Object[] selElements)
	{
		for (int i = 0; i < selElements.length; i++)
		{
			if (selElements[i] instanceof IFile)
			{
				IFile file = (IFile) selElements[i];
				IContentType contentType = IDE.getContentType(file);
				if (contentType != null
					&& CessarRuntime.PLUGET_CONTENT_TYPE_ID.equals(contentType.getId()))
				{
					result.add(file);
				}
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IActionDelegate#selectionChanged(org.eclipse.jface.action.IAction, org.eclipse.jface.viewers.ISelection)
	 */
	public void selectionChanged(final IAction action, final ISelection selection)
	{
		currentSelection = selection;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.runtime.ecuc.ui.internal.menu.AbstractRunPlugetAction#getProject()
	 */
	@Override
	public IProject getProject()
	{
		// TODO Auto-generated method stub
		List<IFile> list = extractFilesFromSelection();
		if (list != null)
		{
			return list.get(0).getProject();
		}
		return null;
	}

}
