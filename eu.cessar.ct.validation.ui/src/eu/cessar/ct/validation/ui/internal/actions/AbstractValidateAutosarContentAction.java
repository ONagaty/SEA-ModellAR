/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidu2337<br/>
 * Apr 9, 2014 1:13:32 PM
 *
 * </copyright>
 */
package eu.cessar.ct.validation.ui.internal.actions;

import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.validation.service.ModelValidationService;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

import eu.cessar.ct.core.mms.MetaModelUtils;
import eu.cessar.ct.core.platform.util.PlatformUtils;
import eu.cessar.ct.validation.ui.AbstractValidateAutosarContent;
import eu.cessar.req.Requirement;

/**
 * Abstract class for validation actions (file-based and merged).
 *
 * @author uidu2337
 *
 *         %created_by: uidl7321 %
 *
 *         %date_created: Tue Feb 10 13:54:33 2015 %
 *
 *         %version: 8 %
 */
@Requirement(
	reqID = "16009")
public abstract class AbstractValidateAutosarContentAction extends AbstractValidateAutosarContent implements
	IObjectActionDelegate
{
	Shell parentShell;

	/**
	 *
	 */
	public AbstractValidateAutosarContentAction()
	{
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IObjectActionDelegate#setActivePart(org.eclipse.jface.action.IAction,
	 * org.eclipse.ui.IWorkbenchPart)
	 */
	@Override
	public void setActivePart(IAction action, IWorkbenchPart targetPart)
	{
		parentShell = targetPart.getSite().getWorkbenchWindow().getShell();
	}

	/**
	 * Return the selected objects.
	 *
	 * @param progressMonitor
	 * @return the selected objects
	 */
	@Override
	protected List<Object> getSelectedObjects(final IProgressMonitor progressMonitor)
	{
		Object obj = null;
		if (currentSelection != null)
		{
			obj = currentSelection.getFirstElement();
		}
		IProject project = MetaModelUtils.getProject(obj);
		if (project != null)
		{
			PlatformUtils.waitForModelLoading(project, progressMonitor);
		}
		// be sure that the constraints are loaded
		ModelValidationService.getInstance().loadXmlConstraintDeclarations();
		List<Object> selectedObjects = currentSelection.toList();
		return selectedObjects;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IActionDelegate#selectionChanged(org.eclipse.jface.action.IAction,
	 * org.eclipse.jface.viewers.ISelection)
	 */
	@Override
	public void selectionChanged(IAction action, ISelection selection)
	{
		currentSelection = (IStructuredSelection) selection;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.validation.ui.AbstractValidateAutosarContent#getParentShell()
	 */
	@Override
	protected Shell getParentShell()
	{

		return parentShell;
	}
}