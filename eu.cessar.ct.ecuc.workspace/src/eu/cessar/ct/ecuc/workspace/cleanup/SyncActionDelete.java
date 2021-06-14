/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by aurel_avramescu Aug 16, 2010 5:41:23 PM </copyright>
 */
package eu.cessar.ct.ecuc.workspace.cleanup;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.sphinx.emf.util.EcorePlatformUtil;
import org.eclipse.sphinx.emf.util.WorkspaceTransactionUtil;

import eu.cessar.ct.ecuc.workspace.internal.Messages;

/**
 * @author aurel_avramescu
 * 
 *         ISyncAction implementation that delete an EObject
 */
public class SyncActionDelete implements ISyncAction
{

	private EObject object;

	/**
	 * Constructor
	 * 
	 * @param object
	 */
	public SyncActionDelete(EObject object)
	{
		this.object = object;
	}

	/**
	 * Delete the object
	 */
	public void execute()
	{
		try
		{
			WorkspaceTransactionUtil.executeInWriteTransaction(
				TransactionUtil.getEditingDomain(EcorePlatformUtil.getResource(EcorePlatformUtil.getFile((object)))),
				new Runnable()
				{
					public void run()
					{
						EcoreUtil.delete(object, true);
					}
				}, "");
		}
		catch (OperationCanceledException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (ExecutionException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.ecuc.workspace.cleanup.ISyncAction#getActionName()
	 */
	public String getActionName()
	{
		return Messages.SyncAction_Delete;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.ecuc.workspace.cleanup.ISyncAction#getOwner()
	 */
	public EObject getOwner()
	{
		return object.eContainer();
	}

}
