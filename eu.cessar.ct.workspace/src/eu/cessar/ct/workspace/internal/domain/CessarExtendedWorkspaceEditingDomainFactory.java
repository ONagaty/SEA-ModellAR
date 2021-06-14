/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidl6458<br/>
 * 06.09.2013 11:30:29
 *
 * </copyright>
 */
package eu.cessar.ct.workspace.internal.domain;

import java.util.Collection;

import org.eclipse.core.commands.operations.IOperationHistory;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.transaction.RollbackException;
import org.eclipse.emf.transaction.TransactionalCommandStack;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.impl.AbstractTransactionalCommandStack;
import org.eclipse.emf.transaction.impl.TransactionChangeRecorder;
import org.eclipse.emf.workspace.impl.WorkspaceCommandStackImpl;
import org.eclipse.sphinx.emf.metamodel.IMetaModelDescriptor;
import org.eclipse.sphinx.emf.util.WorkspaceTransactionUtil;
import org.eclipse.sphinx.emf.workspace.domain.factory.ExtendedWorkspaceEditingDomainFactory;

/**
 *
 * @author uidl6458
 *
 *         %created_by: uid95856 %
 *
 *         %date_created: Wed Feb 11 12:04:14 2015 %
 *
 *         %version: 4 %
 */
public class CessarExtendedWorkspaceEditingDomainFactory extends ExtendedWorkspaceEditingDomainFactory
{

	/**
	 *
	 * @author uidl6458
	 *
	 *         %created_by: uid95856 %
	 *
	 *         %date_created: Wed Feb 11 12:04:14 2015 %
	 *
	 *         %version: 4 %
	 */
	protected class CessarExtendedWorkspaceEditingDomain extends ExtendedWorkspaceEditingDomain
	{

		/**
		 * @param adapterFactory
		 * @param commandStack
		 * @param resourceSet
		 */
		public CessarExtendedWorkspaceEditingDomain(AdapterFactory adapterFactory,
			TransactionalCommandStack commandStack, ResourceSet resourceSet)
		{
			super(adapterFactory, commandStack, resourceSet);
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see
		 * org.eclipse.emf.transaction.impl.TransactionalEditingDomainImpl#createChangeRecorder(org.eclipse.emf.ecore
		 * .resource.ResourceSet)
		 */
		@Override
		protected TransactionChangeRecorder createChangeRecorder(ResourceSet rset)
		{
			return new CessarTransactionChangeRecorder(this, rset);
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see org.eclipse.sphinx.emf.workspace.domain.factory.ExtendedWorkspaceEditingDomainFactory.
		 * ExtendedWorkspaceEditingDomain#isReadOnly(org.eclipse.emf.ecore.resource.Resource)
		 */
		@Override
		public boolean isReadOnly(Resource resource)
		{
			return false;
		}

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.sphinx.emf.workspace.domain.factory.ExtendedWorkspaceEditingDomainFactory#createEditingDomain(java
	 * .util.Collection, org.eclipse.emf.ecore.resource.ResourceSet,
	 * org.eclipse.core.commands.operations.IOperationHistory)
	 */
	@Override
	public TransactionalEditingDomain createEditingDomain(Collection<IMetaModelDescriptor> metaModelDescriptors,
		ResourceSet resourceSet, IOperationHistory history)
	{

		// Create new WorkspaceCommandStack and TransactionalEditingDomain using given IOperationHistory and ResourceSet
		WorkspaceCommandStackImpl stack = new WorkspaceCommandStackImpl(history)
		{
			/*
			 * Overridden for passing {@link WorkspaceTransactionUtil#getDefaultTransactionOptions()} rather than
			 * <code>null</code> to {@link WorkspaceCommandStackImpl#execute(Command, Map<?, ?>)}.
			 */
			@SuppressWarnings("restriction")
			@Override
			public void execute(Command command)
			{
				try
				{
					execute(command, WorkspaceTransactionUtil.getDefaultTransactionOptions());
				}
				catch (InterruptedException e)
				{
					// just log it. Note that the transaction is already rolled back,
					// so handleError() will not find an active transaction
					org.eclipse.emf.transaction.internal.Tracing.catching(AbstractTransactionalCommandStack.class,
						"execute", e); //$NON-NLS-1$
					handleError(e);
				}
				catch (RollbackException e)
				{
					// just log it. Note that the transaction is already rolled back,
					// so handleError() will not find an active transaction
					org.eclipse.emf.transaction.internal.Tracing.catching(AbstractTransactionalCommandStack.class,
						"execute", e); //$NON-NLS-1$
					handleError(e);
				}
			}
		};
		TransactionalEditingDomain result = new CessarExtendedWorkspaceEditingDomain(new ComposedAdapterFactory(
			ComposedAdapterFactory.Descriptor.Registry.INSTANCE), stack, resourceSet);

		// Do default initialization
		mapResourceSet(result);

		// Add IEditingDomainProvider adapter which EMF.Edit needs for retrieving EditingDomain from ResourceSet
		resourceSet.eAdapters().add(new AdapterFactoryEditingDomain.EditingDomainProvider(result));

		// Give the specified meta-model descriptors to the newly created editing domain
		((ExtendedWorkspaceEditingDomain) result).getMetaModelDescriptors().addAll(metaModelDescriptors);

		firePostCreateEditingDomain(metaModelDescriptors, result);
		return result;
	}
}
