/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidg4020<br/>
 * Oct 10, 2014 9:57:44 AM
 *
 * </copyright>
 */
package eu.cessar.ct.validation.ui.internal.wizards;

import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import eu.cessar.ct.validation.preferences.ValidationPreferencesAccessor;
import eu.cessar.ct.validation.ui.internal.jobs.ValidateAutosarContentJob;
import eu.cessar.ct.validation.ui.internal.wizards.pages.FilteredAutosarValidationFilesSelectionPage;
import eu.cessar.req.Requirement;

/**
 * Wizard for the Filtered Validation
 *
 * @author uidg4020
 *
 *         %created_by: uidj6860 %
 *
 *         %date_created: Wed Nov 12 11:19:02 2014 %
 *
 *         %version: RAUTOSAR~3 %
 */
@Requirement(
	reqID = "18352")
public class FilteredAutosarValidationWizard extends Wizard
{
	/** Wizard pages */
	protected List<WizardPage> pages;
	/** Current selection */
	protected IStructuredSelection selection;
	/** Page for filtered validation */
	protected FilteredAutosarValidationFilesSelectionPage selectionPage;
	IProject project;

	/**
	 * @param selection
	 * @param project
	 */
	public FilteredAutosarValidationWizard(IStructuredSelection selection, IProject project)
	{
		super();

		this.selection = selection;
		this.project = project;
		setWindowTitle(Messages.filteredAutosarValidationWizardTitle);

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jface.wizard.Wizard#addPages()
	 */
	@Override
	public void addPages()
	{
		selectionPage = new FilteredAutosarValidationFilesSelectionPage(selection, project, ""); //$NON-NLS-1$
		addPage(selectionPage);

	}

/*
 * (non-Javadoc)
 *
 * @see org.eclipse.jface.wizard.Wizard#canFinish()
 */
	@Override
	public boolean canFinish()
	{

		boolean canFlipToNextPage = selectionPage.canFlipToNextPage();
		return canFlipToNextPage;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jface.wizard.Wizard#performFinish()
	 */
	@Override
	public boolean performFinish()
	{
		boolean shouldNotDisplayWizard = selectionPage.doNotDisplayWizardInTheFuture();
		if (shouldNotDisplayWizard)
		{
			ValidationPreferencesAccessor.setHideFilteredValidationWizardFlag(project, shouldNotDisplayWizard);
		}
		final List<Object> selectedObjects = selectionPage.getSelectedObjects();
		IWorkbench workbench = PlatformUI.getWorkbench();
		IWorkbenchWindow activeWorkbenchWindow = workbench.getActiveWorkbenchWindow();
		final Shell shell = activeWorkbenchWindow.getShell();
		shell.getDisplay().syncExec(new Runnable()
		{
			@Override
			public void run()
			{
				selectionPage.saveSelectionHistory();
				ValidateAutosarContentJob validateJob = new ValidateAutosarContentJob(selectedObjects, shell);
				validateJob.setSelection(selection);
				validateJob.schedule();
			}
		});

		return true;
	}

}
