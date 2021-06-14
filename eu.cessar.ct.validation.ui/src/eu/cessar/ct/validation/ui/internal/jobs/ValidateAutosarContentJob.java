/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidg4020<br/>
 * Oct 14, 2014 3:06:55 PM
 *
 * </copyright>
 */
package eu.cessar.ct.validation.ui.internal.jobs;

import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;

/**
 * Job that triggers the filtered validation of selected objects
 *
 * @author uidg4020
 *
 *         %created_by: uidg4020 %
 *
 *         %date_created: Tue Oct 14 16:56:51 2014 %
 *
 *         %version: 1 %
 */
public class ValidateAutosarContentJob extends Job
{
	private static final String JOB_NAME = "Validate Filtered Autosar Content"; //$NON-NLS-1$
	List<Object> selectedObjects;
	Shell parentShell;
	private IStructuredSelection currentSelection;

	/**
	 * @param selectedObjects
	 *        the objects to perform validation for
	 * @param parentShell
	 *        parent shell for the validation progress monitor
	 */
	public ValidateAutosarContentJob(List<Object> selectedObjects, Shell parentShell)
	{
		super(JOB_NAME);
		this.selectedObjects = selectedObjects;
		this.parentShell = parentShell;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.jobs.Job#run(org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	protected IStatus run(IProgressMonitor monitor)
	{
		parentShell.getDisplay().syncExec(new Runnable()

		{
			@Override
			public void run()
			{
				final ValidateAutosarContent validateAutosarContent = new ValidateAutosarContent(selectedObjects,
					parentShell, currentSelection);
				validateAutosarContent.run(null);
			}
		});
		return Status.OK_STATUS;

	}

	/**
	 * @param selection
	 *        the current selection from the tree editor
	 */
	public void setSelection(IStructuredSelection selection)
	{
		currentSelection = selection;
	}

}
