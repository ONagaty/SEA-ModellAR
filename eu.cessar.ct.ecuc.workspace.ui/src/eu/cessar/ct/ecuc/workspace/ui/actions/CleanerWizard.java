/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by aurel_avramescu Aug 13, 2010 2:45:12 PM </copyright>
 */
package eu.cessar.ct.ecuc.workspace.ui.actions;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Composite;

import eu.cessar.ct.ecuc.workspace.cleanup.IEcucCleaningAction;
import eu.cessar.ct.ecuc.workspace.jobs.CleanUpJob;
import eu.cessar.ct.ecuc.workspace.ui.internal.Messages;

/**
 * @author aurel_avramescu
 * 
 */
public class CleanerWizard extends Wizard
{

	protected ActionsDecisionPage firstPage;
	protected ActionsReportPage secondPage;
	private IFile file;
	private List<IEcucCleaningAction> cleaningActions = new ArrayList<IEcucCleaningAction>();
	private String jobType;

	/**
	 * 
	 */
	public CleanerWizard()
	{
		super();
		setNeedsProgressMonitor(true);
	}

	/**
	 * 
	 */
	public CleanerWizard(List<IEcucCleaningAction> actions, IFile file, String jobType)
	{
		super();
		this.cleaningActions = actions;
		setNeedsProgressMonitor(true);
		this.file = file;
		this.jobType = jobType;
	}

	@Override
	public void addPages()
	{
		secondPage = new ActionsReportPage(Messages.CleanerWizard_SecondPage, cleaningActions);
		firstPage = new ActionsDecisionPage(Messages.CleanerWizard_FirstPage, cleaningActions,
			secondPage);

		addPage(firstPage);
		addPage(secondPage);

	}

	/**
	 * The <code>Wizard</code> implementation of this <code>IWizard</code>
	 * method creates all the pages controls using
	 * <code>IDialogPage.createControl</code>. Subclasses should reimplement
	 * this method if they want to delay creating one or more of the pages
	 * lazily. The framework ensures that the contents of a page will be created
	 * before attempting to show it.
	 */
	@Override
	public void createPageControls(Composite pageContainer)
	{
		super.createPageControls(pageContainer);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.wizard.Wizard#performFinish()
	 */
	@Override
	public boolean performFinish()
	{
		CleanUpJob job = new CleanUpJob(jobType, cleaningActions, file);
		job.schedule();
		return true;
	}

}
