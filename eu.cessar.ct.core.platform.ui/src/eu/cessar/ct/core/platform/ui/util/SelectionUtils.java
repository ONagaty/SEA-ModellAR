/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidl6870 24.07.2012 14:17:14 </copyright>
 */
package eu.cessar.ct.core.platform.ui.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceProxy;
import org.eclipse.core.resources.IResourceProxyVisitor;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.content.IContentDescription;
import org.eclipse.core.runtime.content.IContentType;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.osgi.util.NLS;
import org.eclipse.sphinx.platform.util.ExtendedPlatform;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.model.IWorkbenchAdapter;
import org.eclipse.ui.navigator.CommonNavigator;

import eu.cessar.ct.core.internal.platform.ui.CessarPluginActivator;
import eu.cessar.ct.core.internal.platform.ui.Messages;
import eu.cessar.ct.core.platform.PlatformConstants;

/**
 *
 * {@link IStructuredSelection} related utility methods
 *
 * @author uidl6870
 *
 */
public final class SelectionUtils
{

	private SelectionUtils()
	{
	}

	/**
	 * Collects all the files having one of the specified <code>contentTypes</code>, from the given
	 * <code>selection</code>, where the selection must represent one or multiple {@link IResource}s, into the
	 * <code>collectedFiles</code> set.
	 *
	 * <br>
	 * <br>
	 * Note: if exceptions are raised during the acquisition of the files, statuses with {@link IStatus#ERROR} are
	 * created and added to the returned {@link MultiStatus} status.
	 *
	 * @param selection
	 *        selection containing one or several elements representing {@link IResource}s
	 * @param collectedFiles
	 *        set where to collect the files
	 * @param contentTypes
	 *        accepted content types
	 * @return the status of acquisition activity: <li>{@link Status#OK_STATUS} if the acquisition was successful</li>,
	 *         or <li>a {@link MultiStatus} status having the {@link IStatus#ERROR} severity if exception(s) were raised
	 *         during the acquisition; reasons include:
	 *         <ul>
	 *         <li>One of the accessed resources does not exist.</li>
	 *         <li>One of the accessed resources is a project that is not open.</li>
	 *
	 *         <li>One of the accessed resources does not exist.</li>
	 *         <li>One of the accessed resources could not be read.</li>
	 *         <li>One of the accessed resources is not local.</li>
	 *         <li>The workspace is not in sync with the corresponding location in the local file system and
	 *         {@link ResourcesPlugin#PREF_LIGHTWEIGHT_AUTO_REFRESH} is disabled.</li>
	 *         <li>The corresponding location in the local file system is occupied by a directory.</li>
	 *         </ul>
	 *         </li>
	 */
	public static IStatus collectFilesFromSelection(IStructuredSelection selection, Set<IFile> collectedFiles,
		List<String> contentTypes)
	{
		MultiStatus multiStatus = new MultiStatus(CessarPluginActivator.PLUGIN_ID, IStatus.OK, "", null); //$NON-NLS-1$

		if (isSelectionAtProjectLevel(selection))
		{
			visitProject((IProject) selection.getFirstElement(), collectedFiles, getAcceptedExtensions(contentTypes),
				multiStatus);
		}
		else
		{
			Iterator<?> iterator = selection.iterator();
			while (iterator.hasNext())
			{
				Object object = iterator.next();
				if (!(object instanceof IResource))
				{
					continue;
				}
				IResource resource = (IResource) object;

				if (resource instanceof IContainer)
				{
					try
					{
						IResource[] members = ((IContainer) resource).members();
						doCollectFiles(members, collectedFiles, contentTypes, multiStatus);
					}
					catch (CoreException e)
					{
						IStatus status = new Status(IStatus.ERROR, CessarPluginActivator.PLUGIN_ID, e.getMessage(), e);
						multiStatus.add(status);
					}

				}
				else if (resource instanceof IFile)
				{
					IStatus status = collectFile((IFile) resource, collectedFiles, contentTypes);
					if (!status.isOK())
					{
						multiStatus.add(status);
					}
				}
			}
		}

		if (multiStatus.getChildren().length == 0)
		{
			return Status.OK_STATUS;
		}
		else
		{
			MultiStatus finalStatus = new MultiStatus(CessarPluginActivator.PLUGIN_ID, IStatus.ERROR,
				"Check the child statuses", null); //$NON-NLS-1$
			for (IStatus status: multiStatus.getChildren())
			{
				finalStatus.add(status);
			}
			return finalStatus;
		}
	}

	/**
	 * @param selection
	 * @return
	 */
	private static boolean isSelectionAtProjectLevel(IStructuredSelection selection)
	{
		return selection.getFirstElement() instanceof IProject;
	}

	private static List<String> getAcceptedExtensions(List<String> contentTypeIds)
	{
		List<String> extensions = new ArrayList<String>();
		for (String contentTypeId: contentTypeIds)
		{
			IContentType contentType = Platform.getContentTypeManager().getContentType(contentTypeId);

			String[] fileSpecs = contentType.getFileSpecs(IContentType.FILE_EXTENSION_SPEC);
			extensions.addAll(Arrays.asList(fileSpecs));
		}

		return extensions;
	}

	private static void visitProject(IProject project, final Set<IFile> list, final List<String> extensions,
		MultiStatus multiStatus)
	{
		try
		{
			project.accept(new IResourceProxyVisitor()
			{
				public boolean visit(IResourceProxy proxy) throws CoreException
				{
					if (proxy.getType() == IResource.FILE && isApplicable(proxy))
					{
						IFile ifile = (IFile) proxy.requestResource();

						list.add(ifile);
					}
					return true;
				}

				private boolean isApplicable(IResourceProxy proxy)
				{
					boolean res = false;
					String name = proxy.getName();
					for (String ext: extensions)
					{
						if (name.endsWith(ext))
						{
							res = true;
							break;
						}
					}
					return res;
				}
			}, 0);
		}
		catch (CoreException e)
		{
			IStatus status = new Status(IStatus.ERROR, CessarPluginActivator.PLUGIN_ID, e.getMessage(), e);
			multiStatus.add(status);
		}
	}

	/**
	 * Recursively collects the files that conform to one of the content type ids specified in
	 * <code>contentTypesIds</code>, from given <code>resources</code> and adds them to the <code>collectedFiles</code>
	 * set. <br>
	 * If an exception is raised during accessing one of the resources, an error status is created and added to the
	 * <code>multiStatus</code>
	 *
	 * @param resources
	 * @param collectedFiles
	 * @param contentTypesIds
	 * @param multiStatus
	 */

	private static void doCollectFiles(IResource[] resources, Set<IFile> collectedFiles, List<String> contentTypesIds,
		MultiStatus multiStatus)
	{
		for (IResource resource: resources)
		{
			if (resource instanceof IFile)
			{
				IStatus status = collectFile((IFile) resource, collectedFiles, contentTypesIds);
				if (!status.isOK())
				{
					multiStatus.add(status);
				}
			}
			else if (resource instanceof IContainer)
			{
				try
				{
					doCollectFiles(((IContainer) resource).members(), collectedFiles, contentTypesIds, multiStatus);
				}
				catch (CoreException e)
				{
					IStatus status = new Status(IStatus.ERROR, CessarPluginActivator.PLUGIN_ID, e.getMessage(), e);
					multiStatus.add(status);
				}
			}

		}

	}

	/**
	 * Returns the currently selected project in the Project Explorer, if the project is valid (accessible and with
	 * CESSAR nature). <br>
	 * If no project is selected and there is a single valid project in the workspace, it will be returned.
	 * <p>
	 * Optionally, if there is no valid active project available, prompts a message dialog (possible causes: empty
	 * workspace, project is not accessible or has no CESSAR nature, there is no selection, but several projects
	 * available).
	 *
	 * @param promptMsgDialog
	 *        whether to prompt a message dialog if there is no valid active project
	 *
	 * @return the active project, could be <code>null</code>
	 */
	public static IProject getActiveProject(boolean promptMsgDialog)
	{
		IProject activeProject = null;

		IProject selectedProject = SelectionUtils.getCurrentSelectedProject();
		if (selectedProject == null)
		{
			selectedProject = handleNoProjectSelected(promptMsgDialog);
		}

		if (selectedProject != null)
		{
			boolean valid = checkProject(selectedProject, promptMsgDialog);
			if (valid)
			{
				activeProject = selectedProject;
			}
		}

		return activeProject;
	}

	/**
	 *
	 * @return the currently selected project in the Project Explorer, could be <code>null</code>
	 */
	private static IProject getCurrentSelectedProject()
	{

		// IWorkbench workbench = PlatformUI.getWorkbench();
		// IWorkbenchWindow activeWorkbenchWindow = workbench.getActiveWorkbenchWindow();
		// ISelectionService service = activeWorkbenchWindow.getSelectionService();
		// String id = activeWorkbenchWindow.getActivePage().getActivePartReference().getId();
		// System.out.println(id);
		// IWorkbenchWindow[] workbenchWindows = workbench.getWorkbenchWindows();
		// for (IWorkbenchWindow iWorkbenchWindow: workbenchWindows)
		// {
		// IWorkbenchPage[] pages2 = iWorkbenchWindow.getPages();
		// for (IWorkbenchPage iWorkbenchPage: pages2)
		// {
		// IEditorReference[] editorReferences = iWorkbenchPage.getEditorReferences();
		// for (IEditorReference iEditorReference: editorReferences)
		// {
		// System.out.println(iEditorReference.getPart(restore)getId());
		// }
		//
		// }
		// }
		// IStructuredSelection selection = (IStructuredSelection)
		// service.getSelection(IPageLayout.ID_PROJECT_EXPLORER);

		ISelectionService service = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getSelectionService();
		IStructuredSelection selection = (IStructuredSelection) service.getSelection(IPageLayout.ID_PROJECT_EXPLORER);

		IProject project = getProjectFromSelection(selection);

		return project;
	}

	/**
	 * Returns the project from a IStructuredSelection
	 *
	 * @param selection
	 * @return IProject
	 */
	public static IProject getProjectFromSelection(IStructuredSelection selection)
	{
		IProject project = null;
		if (selection != null && !selection.isEmpty())
		{

			Object selectedElem = selection.getFirstElement();
			if (selectedElem instanceof IProject)
			{
				project = (IProject) selectedElem;
			}
			else if (selectedElem instanceof IResource)
			{
				project = ((IResource) selectedElem).getProject();
			}
			else if (selectedElem instanceof IAdaptable)
			{
				IWorkbenchAdapter adapter = (IWorkbenchAdapter) ((IAdaptable) selectedElem).getAdapter(IWorkbenchAdapter.class);
				if (adapter != null)
				{
					while (project == null)
					{

						Object parent = adapter.getParent(selectedElem);
						if (parent instanceof IAdaptable)
						{
							project = (IProject) ((IAdaptable) parent).getAdapter(IProject.class);
						}
						selectedElem = parent;
					}
				}

			}
		}
		return project;
	}

	/**
	 * Returns <code>true</code> if the project is open and has CESSAR nature, <code>false</code> otherwise. Optionally,
	 * prompts a message dialog giving the reason why the project failed the check.
	 *
	 * @param project
	 *        the project to be verified
	 * @param promptMsgDialog
	 *        whether to prompt a message dialog when necessary
	 * @return whether provided project is accessible and has CESSAR nature
	 */
	private static boolean checkProject(IProject project, boolean promptMsgDialog)
	{
		boolean valid = false;

		if (project.isAccessible())
		{
			boolean hasCessarNature = false;
			try
			{
				hasCessarNature = project.hasNature(PlatformConstants.CESSAR_NATURE);
				valid = true;
			}
			catch (CoreException e)
			{
				CessarPluginActivator.getDefault().logError(e);
			}

			if (!hasCessarNature && promptMsgDialog)
			{
				MessageDialog.openInformation(getShell(), Messages.project_Nature_title,
					NLS.bind(Messages.nature_not_CESSAR, project));
			}
		}
		else
		{
			if (promptMsgDialog)
			{
				MessageDialog.openInformation(getShell(), Messages.project_Not_Opened_title,
					Messages.open_Selected_Project);
			}
		}

		return valid;
	}

	/**
	 * If there is a single project in the workspace, it returns it. Otherwise returns <code>null</code>.
	 *
	 * @param promptMsgDialog
	 *        whether to prompt a message dialog if there is no project or if there are several ones
	 *
	 */
	private static IProject handleNoProjectSelected(boolean promptMsgDialog)
	{
		IProject project = null;
		Collection<IProject> projects = ExtendedPlatform.getProjects(PlatformConstants.CESSAR_NATURE);

		if (projects.isEmpty() && promptMsgDialog)
		{
			MessageDialog.openInformation(getShell(), Messages.no_Projects_title,
				Messages.no_CESSAR_Projects_in_Workspace);
		}
		else if (projects.size() == 1)
		{
			project = projects.iterator().next();
			IViewPart projectExplorer = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(
				IPageLayout.ID_PROJECT_EXPLORER);
			if (projectExplorer instanceof CommonNavigator)
			{
				((CommonNavigator) projectExplorer).selectReveal(new StructuredSelection(project));
			}

		}
		else if (projects.size() > 1 && promptMsgDialog)
		{
			MessageDialog.openInformation(getShell(), Messages.no_Project_Selected_title, Messages.select_Project);
		}

		return project;
	}

	private static Shell getShell()
	{
		return Display.getCurrent().getActiveShell();
	}

	/**
	 * Adds the given <code>file</code> to the <code>collectedFiles</code> set , if its content type is among the
	 * <code>contentTypesIds</code>>.
	 *
	 * @param file
	 * @param collectedFiles
	 * @param contentTypes
	 * @return status indicating whether the file could be accessed or not
	 */

	private static IStatus collectFile(IFile file, Set<IFile> collectedFiles, List<String> contentTypesIds)
	{
		IStatus status = Status.OK_STATUS;
		try
		{
			IContentDescription description = file.getContentDescription();
			if (description != null)
			{
				IContentType fileContentType = description.getContentType();
				if (fileContentType != null)
				{
					String id = fileContentType.getId();
					for (String contentType: contentTypesIds)
					{
						if (contentType.equals(id))
						{
							collectedFiles.add(file);
							status = Status.OK_STATUS;
						}
					}
				}
			}
		}
		catch (CoreException e)
		{
			status = new Status(IStatus.ERROR, CessarPluginActivator.PLUGIN_ID, e.getMessage(), e);
		}

		return status;

	}

}
