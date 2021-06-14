/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidl6870 Oct 14, 2010 4:26:11 PM </copyright>
 */
package eu.cessar.ct.validation.internal.jobs;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.transaction.RunnableWithResult;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.emf.validation.service.IConstraintDescriptor;
import org.eclipse.emf.validation.service.IConstraintFilter;
import org.eclipse.sphinx.emf.util.EObjectUtil;
import org.eclipse.sphinx.emf.util.WorkspaceEditingDomainUtil;
import org.eclipse.sphinx.emf.validation.diagnostic.ExtendedDiagnostician;

import eu.cessar.ct.core.platform.concurrent.AbstractAsyncJob;
import eu.cessar.ct.core.platform.concurrent.AbstractAsyncWorkExecManager;
import gautosar.gecucdescription.GContainer;

/**
 * Job that performs validation of the map.keySet() and informs interested callbacks when the validation is done
 *
 */
public class SystemValidationJob extends AbstractAsyncJob<EObject, Diagnostic>
{

	public static final Object CESSAR_DELAYED_VALIDATOR = new Object();

	private ExtendedDiagnostician diagnostician;

	/**
	 *
	 * @param name
	 *        name of the job
	 * @param managerImpl
	 *        the ValidationManagerImpl instance
	 * @param map
	 *        mapping between EObjects to be validated and the list of interested callbacks
	 */
	public SystemValidationJob(String name, AbstractAsyncWorkExecManager<EObject, Diagnostic> managerImpl)
	{
		super(name, "Validation launched...", managerImpl); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.jobs.Job#run(org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	protected IStatus run(IProgressMonitor monitor)
	{
		diagnostician = new ExtendedDiagnostician();
		diagnostician.setProgressMonitor(monitor);
		IStatus result;
		try
		{
			result = super.run(monitor);
		}
		finally
		{
			diagnostician.setProgressMonitor(null);
		}
		return result;
	}

	@Override
	protected Diagnostic performWork(final EObject eObject) throws InterruptedException
	{
		final Diagnostic[] diagnostic = new Diagnostic[1];
		final TransactionalEditingDomain teD = WorkspaceEditingDomainUtil.getEditingDomain(eObject);
		if (null != teD)
		{
			TransactionUtil.runExclusive(teD, new RunnableWithResult.Impl<EObject>()
			{
				public void run()
				{
					if (eObject instanceof GContainer)
					{
						diagnostic[0] = diagnostician.validate(eObject, new IConstraintFilter()
						{
							@Override
							public boolean accept(IConstraintDescriptor constraint, EObject target)
							{
								boolean accept = true;
								// do not accept to validate sub-containers
								if (target != eObject && target instanceof GContainer)
								{
									accept = false;
								}
								return accept;
							}

						}, EObjectUtil.DEPTH_ONE);
					}
					else
					{
						diagnostic[0] = diagnostician.validate(eObject, EObjectUtil.DEPTH_ZERO);
					}
				}
			});
		}
		return diagnostic[0];

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.jobs.Job#belongsTo(java.lang.Object)
	 */
	@Override
	public boolean belongsTo(Object family)
	{
		return family == CESSAR_DELAYED_VALIDATOR;
	}

}
