/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by aurel_avramescu Aug 16, 2010 5:29:02 PM </copyright>
 */
package eu.cessar.ct.ecuc.workspace.cleanup;

import eu.cessar.ct.ecuc.workspace.internal.CessarPluginActivator;
import gautosar.gecucparameterdef.GContainerDef;
import gautosar.ggenericstructure.ginfrastructure.GIdentifiable;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.sphinx.emf.util.EcorePlatformUtil;
import org.eclipse.sphinx.emf.util.WorkspaceTransactionUtil;

/**
 * @author aurel_avramescu
 * 
 *         Action that create a specified number of containers
 */
public class SyncActionCreateContainer extends AbstractSyncAction
{
	private GIdentifiable owner;
	private GContainerDef containerDef;
	private int number;

	/**
	 * Constructor
	 * 
	 * @param owner
	 *        container owner
	 * @param containerDef
	 *        container definition
	 * @param number
	 *        number of containers that must be created
	 */
	public SyncActionCreateContainer(GIdentifiable owner, GContainerDef containerDef, int number)
	{
		this.owner = owner;
		this.containerDef = containerDef;
		this.number = number;
	}

	/**
	 * Create the containers
	 */
	public void execute()
	{
		try
		{
			WorkspaceTransactionUtil.executeInWriteTransaction(
				TransactionUtil.getEditingDomain(EcorePlatformUtil.getResource(EcorePlatformUtil.getFile((owner)))),
				new Runnable()
				{
					public void run()
					{
						try
						{
							for (int i = 0; i < number; i++)
							{

								createContainer(owner, containerDef);

							}
						}
						catch (CoreException e)
						{
							CessarPluginActivator.getDefault().logError(e);
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
		return owner;
	}
}
