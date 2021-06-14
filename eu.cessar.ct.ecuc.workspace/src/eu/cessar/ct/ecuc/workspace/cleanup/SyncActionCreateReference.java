/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by aurel_avramescu Aug 16, 2010 5:38:09 PM </copyright>
 */
package eu.cessar.ct.ecuc.workspace.cleanup;

import gautosar.gecucdescription.GContainer;
import gautosar.gecucparameterdef.GConfigReference;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.sphinx.emf.util.EcorePlatformUtil;
import org.eclipse.sphinx.emf.util.WorkspaceTransactionUtil;

/**
 * @author aurel_avramescu
 * 
 *         Action that create a specified number of reference values
 */
public class SyncActionCreateReference extends AbstractSyncAction
{

	private GContainer container;
	private GConfigReference reference;
	private int number;

	/**
	 * Constructor
	 * 
	 * @param container
	 *        parent container
	 * @param reference
	 *        reference definition
	 * @param number
	 *        number of references that must be created
	 */
	public SyncActionCreateReference(GContainer container, GConfigReference reference, int number)
	{
		this.container = container;
		this.reference = reference;
		this.number = number;
	}

	/**
	 * Create the reference value
	 */
	public void execute()
	{
		try
		{
			WorkspaceTransactionUtil.executeInWriteTransaction(
				TransactionUtil.getEditingDomain(EcorePlatformUtil.getResource(EcorePlatformUtil.getFile((container)))),
				new Runnable()
				{
					public void run()
					{
						for (int i = 0; i < number; i++)
						{

							createReference(container, reference);

						}
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
	 * @see eu.cessar.ct.ecuc.workspace.cleanup.ISyncAction#getOwner()
	 */
	public EObject getOwner()
	{
		return container;
	}

}
