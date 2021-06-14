/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by aurel_avramescu Aug 16, 2010 3:05:28 PM </copyright>
 */
package eu.cessar.ct.ecuc.workspace.jobs;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.sphinx.emf.util.EcorePlatformUtil;
import org.eclipse.sphinx.emf.util.WorkspaceTransactionUtil;

import eu.cessar.ct.core.mms.EResourceUtils;
import eu.cessar.ct.ecuc.workspace.cleanup.IEcucCleaningAction;
import eu.cessar.ct.ecuc.workspace.internal.CessarPluginActivator;
import eu.cessar.ct.ecuc.workspace.internal.Messages;

/**
 * @author aurel_avramescu
 * 
 */
public class CleanUpJob extends Job
{

	private List<IEcucCleaningAction> cleaningActions = new ArrayList<IEcucCleaningAction>();
	private IFile file;
	List<Resource> resources = new ArrayList<Resource>();

	/**
	 * @param name
	 */
	public CleanUpJob(String name, List<IEcucCleaningAction> cleaningActions, IFile file)
	{
		super(name);
		this.cleaningActions = cleaningActions;
		this.file = file;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.core.runtime.jobs.Job#run(org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	protected IStatus run(final IProgressMonitor monitor)
	{
		IStatus result = Status.OK_STATUS;

		final Exception[] error = new CoreException[1];
		try
		{
			monitor.beginTask(Messages.CleanUpJob_CleanUp, IProgressMonitor.UNKNOWN);
			WorkspaceTransactionUtil.executeInWriteTransaction(
				TransactionUtil.getEditingDomain(EcorePlatformUtil.getResource(file)),
				new Runnable()
				{
					public void run()
					{
						try
						{

							for (IEcucCleaningAction action: cleaningActions)
							{
								if (action.isSelected())
								{
									monitor.subTask(Messages.CleanUpJob_DeleteElement
										+ action.getElementName());
									if (!resources.contains(action.getActionOwner().eResource()))
									{
										resources.add(action.getActionOwner().eResource());
									}
									action.execute();
								}
								else
								{
									monitor.subTask(Messages.CleanUpJob_SkipElement
										+ action.getElementName());
								}

							}

						}
						catch (Exception e)
						{
							error[0] = e;
						}

					}
				}, Messages.CleanUpJob_CleaningOperation);

			monitor.done();
		}
		catch (Exception e)
		{
			result = CessarPluginActivator.getDefault().createStatus(e);
		}

		if (null != error[0])
		{
			result = CessarPluginActivator.getDefault().createStatus(error[0]);
		}

		try
		{
			EResourceUtils.saveResources(resources, monitor, true);
		}
		catch (CoreException e)
		{
			CessarPluginActivator.getDefault().logError(e);
		}
		return result;
	}

}
