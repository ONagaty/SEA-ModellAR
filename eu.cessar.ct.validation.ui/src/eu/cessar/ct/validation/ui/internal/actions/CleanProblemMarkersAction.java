/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidl6870 Jul 1, 2010 6:28:43 PM </copyright>
 */
package eu.cessar.ct.validation.ui.internal.actions;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.sphinx.emf.validation.ui.actions.BasicCleanProblemMarkersAction;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

/**
 * Clean problem markers action implementation
 *
 * @author uidl6870
 *
 */
public class CleanProblemMarkersAction extends BasicCleanProblemMarkersAction implements IObjectActionDelegate
{

	/** currently selected item in the application UI */
	protected IStructuredSelection currentSelection;
	private boolean cleanProject;

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.ui.IObjectActionDelegate#setActivePart(org.eclipse.jface.action.IAction,
	 * org.eclipse.ui.IWorkbenchPart)
	 */
	public void setActivePart(IAction action, IWorkbenchPart targetPart)
	{
		// nothing
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.ui.IActionDelegate#run(org.eclipse.jface.action.IAction)
	 */
	public void run(IAction action)
	{
		CleanProblemMarkers cleanProblemMarkers = new CleanProblemMarkers();
		cleanProblemMarkers.setCurrentSelection(currentSelection);
		cleanProblemMarkers.clean(cleanProject);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.ui.IActionDelegate#selectionChanged(org.eclipse.jface.action.IAction,
	 * org.eclipse.jface.viewers.ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection)
	{
		currentSelection = (IStructuredSelection) selection;
	}

	/**
	 * @param clean
	 */
	public void cleanProject(boolean clean)
	{
		cleanProject = clean;
	}
}
