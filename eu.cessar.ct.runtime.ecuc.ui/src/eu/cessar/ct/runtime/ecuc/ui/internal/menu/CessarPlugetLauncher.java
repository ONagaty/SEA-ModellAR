package eu.cessar.ct.runtime.ecuc.ui.internal.menu;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.eclipse.core.filesystem.URIUtil;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorLauncher;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.PlatformUI;

import eu.cessar.ct.runtime.ecuc.ui.internal.CessarPluginActivator;
import eu.cessar.ct.runtime.ecuc.util.RunPlugetUtils;
import eu.cessar.ct.sdk.runtime.ExecutionService;
import eu.cessar.ct.sdk.runtime.ICessarTaskManager;

/**
 * @author uidt2045
 *
 */
public class CessarPlugetLauncher implements IEditorLauncher
{

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.ui.IEditorLauncher#open(org.eclipse.core.runtime.IPath)
	 */
	public void open(IPath file)
	{

		boolean saveAllEditors = PlatformUI.getWorkbench().saveAllEditors(true);
		if (saveAllEditors)
		{
			final IFile pluget = getIFile(file);
			if (pluget == null)
			{
				return;
			}
			final String[] arguments = PlugetHistoryArgManager.getUserInput(getShell(), getProject(pluget));

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
						runPlugets(monitor, arguments, pluget);
					}
				});
			}
			// SUPPRESS CHECKSTYLE Catch and log everything
			catch (Exception e)
			{
				CessarPluginActivator.getDefault().logError(e);
			}
		}
	}

	/**
	 * find real accessing file if it is reference from other project
	 *
	 * @param originalFile
	 * @param files
	 * @return
	 *
	 */

	private IFile findRealFile(IFile[] files)
	{
		ISelectionService service = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getSelectionService();
		TreeSelection treeSelection = (TreeSelection) service.getSelection();

		if (treeSelection == null)
		{
			return null;
		}

		/* On first create, selection is project */
		if ((treeSelection.getFirstElement() instanceof IProject))
		{

			IProject project = (IProject) treeSelection.getFirstElement();
			List<IFile> plugets = RunPlugetUtils.INSTANCE.getProjectPlugets(project);
			IFile recentlyCreatedPluget = null;

			for (IFile pluget: plugets)
			{

				if (recentlyCreatedPluget == null)
				{
					recentlyCreatedPluget = pluget;
					continue;
				}
				else if (pluget.getLocalTimeStamp() > recentlyCreatedPluget.getLocalTimeStamp())
				{
					recentlyCreatedPluget = pluget;
				}

			}

			return recentlyCreatedPluget;

		}

		/* On double click, selection is IFile with pluget extension */
		IPath selectionFullPath = ((IFile) treeSelection.getFirstElement()).getFullPath();
		for (IFile file: files)
		{
			IPath fileFullPath = file.getFullPath();
			if (fileFullPath.equals(selectionFullPath))
			{
				return file;
			}
		}

		return null;
	}

	/**
	 * @param file
	 * @return
	 */
	private IFile getIFile(IPath location)
	{

		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IFile[] locationURI = workspace.getRoot().findFilesForLocationURI(URIUtil.toURI(location.makeAbsolute()));

		switch (locationURI.length)
		{
			case 0:
				CessarPluginActivator.getDefault().logError(
					"No file could be located into projects for the location {0}", location); //$NON-NLS-1$
				return null;

			case 1:
				return locationURI[0];

			default:

				IFile file = findRealFile(locationURI);
				if (file != null)
				{
					return file;
				}
				else
				{
					CessarPluginActivator.getDefault().logError(
						"Multiple files linked to the same system file, using the first one:", locationURI[0]); //$NON-NLS-1$
					return locationURI[0];
				}

		}
	}

	/**
	 * @return
	 */
	private Shell getShell()
	{
		return Display.getCurrent().getActiveShell();
	}

	/**
	 * @return
	 */
	private IProject getProject(IFile pluget)
	{
		return pluget.getProject();
	}

	private void runPlugets(final IProgressMonitor monitor, final String[] arguments, IFile pluget)
	{
		monitor.beginTask("Executing pluget(s)", 100); //$NON-NLS-1$
		try
		{
			ICessarTaskManager<IFile> manager = (ICessarTaskManager<IFile>) ExecutionService.createManager(
				getProject(pluget), ExecutionService.TASK_TYPE_PLUGET);
			manager.initialize(pluget);
			manager.execute(false, arguments, new SubProgressMonitor(monitor, 1000));

		}
		finally
		{
			monitor.done();
		}
	}
}
