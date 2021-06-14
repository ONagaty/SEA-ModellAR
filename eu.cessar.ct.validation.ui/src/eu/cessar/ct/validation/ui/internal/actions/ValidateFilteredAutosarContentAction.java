/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidl6870 Jul 1, 2010 4:00:46 PM </copyright>
 */
package eu.cessar.ct.validation.ui.internal.actions;

import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.validation.service.ModelValidationService;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;

import eu.cessar.ct.core.mms.MetaModelUtils;
import eu.cessar.ct.core.platform.util.PlatformUtils;
import eu.cessar.ct.validation.preferences.ValidationPreferencesAccessor;
import eu.cessar.ct.validation.ui.internal.jobs.ValidateAutosarContent;
import eu.cessar.ct.validation.ui.internal.wizards.FilteredAutosarValidationWizard;
import eu.cessar.ct.validation.ui.internal.wizards.FilteredSelectionUtils;
import eu.cessar.req.Requirement;

/**
 * Validate AUTOSAR content action implementation
 *
 * @author uidl6870
 *
 */
@Requirement(
	reqID = "18353")
public class ValidateFilteredAutosarContentAction implements IObjectActionDelegate
{
	/** currently selected item in the application UI */
	protected IStructuredSelection currentSelection;
	/** parent shell */
	protected Shell parentShell;

	@Override
	public void run(IAction action)
	{
		final Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();

		if (currentSelection == null)
		{
			return; // nothing to do
		}
		Object obj = currentSelection.getFirstElement();
		IProject project = MetaModelUtils.getProject(obj);
		boolean hideFilteredValidationWizardFlag = ValidationPreferencesAccessor.getHideFilteredValidationWizardFlag(project);
		if (hideFilteredValidationWizardFlag)
		{
			// wizard should not be shown

			List<Object> selectionsHistory = FilteredSelectionUtils.getSelectionsHistory(project);
			ValidateAutosarContent validateAutosarContent;
			if (selectionsHistory == null)
			{ // if there are no selected objects, all objects under current selection are selected
				validateAutosarContent = new ValidateAutosarContent(currentSelection, parentShell);
			}
			else
			{
				validateAutosarContent = new ValidateAutosarContent(selectionsHistory, parentShell, currentSelection);
			}
			validateAutosarContent.run(null);

		}
		else
		{
			// wizard is shown
			startValidationWizard(shell, project);
		}

	}

	/**
	 * Return the selected objects.
	 *
	 * @param progressMonitor
	 * @return the selected objects
	 */
	protected List<Object> getObjectsFromSelection(final IProgressMonitor progressMonitor)
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

	private void startValidationWizard(Shell shell, IProject project)
	{
		FilteredAutosarValidationWizard wizard = new FilteredAutosarValidationWizard(currentSelection, project);
		WizardDialog wizardDialog = new WizardDialog(shell, wizard);
		wizardDialog.setPageSize(500, 500);
		wizardDialog.setBlockOnOpen(true);
		wizardDialog.open();
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
	 * @see org.eclipse.ui.IObjectActionDelegate#setActivePart(org.eclipse.jface.action.IAction,
	 * org.eclipse.ui.IWorkbenchPart)
	 */
	@Override
	public void setActivePart(IAction action, IWorkbenchPart targetPart)
	{
		parentShell = targetPart.getSite().getWorkbenchWindow().getShell();

	}
}
