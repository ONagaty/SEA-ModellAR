/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidu0944 Jul 24, 2012 5:06:47 PM </copyright>
 */
package eu.cessar.ct.workspace.ui.logging.hyperlink;

import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.emf.common.ui.viewer.IViewerProvider;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.console.IHyperlink;

import eu.cessar.ct.edit.ui.utils.CessarURIEditorInput;
import eu.cessar.ct.edit.ui.utils.EditUtils;
import eu.cessar.ct.sdk.utils.ModelUtils;
import eu.cessar.ct.sdk.utils.SplitUtils;
import eu.cessar.ct.workspace.ui.internal.CessarPluginActivator;
import eu.cessar.req.Requirement;
import gautosar.ggenericstructure.ginfrastructure.GARObject;

/**
 * @author uidu0944
 *
 */
@Requirement(
	reqID = "201715")
public class AutosarHyperLink implements IHyperlink
{

	private String qualifiedNameOfObject;
	private IProject project;

	/**
	 *
	 * A link to an AUTOSAR object.
	 *
	 * @param qualifiedName
	 *        of the linked object(s)
	 *
	 * @param project
	 *        where the elements are placed
	 */
	public AutosarHyperLink(String qualifiedName, IProject project)
	{
		qualifiedNameOfObject = qualifiedName;
		this.project = project;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.console.IHyperlink#linkEntered()
	 */
	public void linkEntered()
	{
		// do nothing here
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.console.IHyperlink#linkExited()
	 */
	public void linkExited()
	{
		// do nothing here
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.console.IHyperlink#linkActivated()
	 */
	public void linkActivated()
	{
		List<EObject> listOfObjects = ModelUtils.getEObjectsWithQualifiedName(project, qualifiedNameOfObject);

		if (listOfObjects != null && !listOfObjects.isEmpty())
		{
			if (listOfObjects.size() == 1)
			{
				EditUtils.openEditor(listOfObjects.get(0), true);
			}
			else
			{
				IWorkbenchWindow activeWorkbenchWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
				if (activeWorkbenchWindow != null && activeWorkbenchWindow.getActivePage() != null)
				{
					IWorkbenchPage page = activeWorkbenchWindow.getActivePage();

					try
					{
						IEditorInput editorInput = new CessarURIEditorInput(project);
						IEditorPart editorPart = page.openEditor(editorInput, EditUtils.EDITOR_ID_CESSAR_MODEL, true,
							IWorkbenchPage.MATCH_INPUT | IWorkbenchPage.MATCH_ID);

						if (editorPart instanceof IViewerProvider
							&& (((IViewerProvider) editorPart).getViewer()) != null)
						{
							GARObject mergedInstance = SplitUtils.getMergedInstance((GARObject) listOfObjects.get(0));
							StructuredSelection structuredSelection = new StructuredSelection(mergedInstance);
							((IViewerProvider) editorPart).getViewer().setSelection(structuredSelection, false);
						}
					}
					catch (PartInitException e)
					{
						CessarPluginActivator.getDefault().logError(e);
					}
				}
			}
		}
	}
}
