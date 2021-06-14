/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others.<br/>
 * http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidg3464<br/>
 * Dec 8, 2014 2:28:23 PM
 *
 * </copyright>
 */
package eu.cessar.ct.core.mms;

import java.util.Map;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.IOperationHistory;
import org.eclipse.core.commands.operations.IUndoableOperation;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.transaction.Transaction;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.workspace.AbstractEMFOperation;
import org.eclipse.osgi.util.NLS;
import org.eclipse.sphinx.emf.util.WorkspaceTransactionUtil;

/**
 *
 * @author uidg3464
 *
 *         %created_by: uidg3464 %
 *
 *         %date_created: Tue Dec  9 15:01:23 2014 %
 *
 *         %version: 1 %
 */
public class CessarTransactionUtils
{
	/**
	 * Executes a write operation in a write transaction, and returns an IStatus
	 *
	 * @param editingDomain
	 *        The Transactional Editing domain receiving the transaction .
	 * @param runnable
	 *        The runnable defining the run method .
	 * @param operationLabel
	 *        The label of the operation to execute .
	 * @throws OperationCanceledException
	 *         Thrown when the transaction is cancelled by the user.
	 * @throws ExecutionException
	 *         Thrown when the transaction could not be completed to an Exception.
	 * @return IStatus
	 **/
	public static IStatus executeInWriteTransaction(TransactionalEditingDomain editingDomain, Runnable runnable,
		String operationLabel) throws OperationCanceledException, ExecutionException
	{
		IStatus executeInWriteTransactionStatus = executeInWriteTransaction(editingDomain, runnable, operationLabel,
			WorkspaceTransactionUtil.getOperationHistory(editingDomain),
			WorkspaceTransactionUtil.getDefaultTransactionOptions(), null);
		return executeInWriteTransactionStatus;
	}

	/**
	 * Execute a write operation in a write transaction, and returns an IStatus
	 *
	 * @param editingDomain
	 *        The {@linkplain TransactionalEditingDomain editing domain} receiving the transaction.
	 * @param runnable
	 *        The {@linkplain Runnable runnable} defining the run method.
	 * @param operationLabel
	 *        The label of the operation to execute.
	 * @param operationHistory
	 *        The {@linkplain IOperationHistory operation history} to store the executed operation.
	 * @param transactionOptions
	 *        The options to set the transaction.
	 * @param monitor
	 *        The {@linkplain IProgressMonitor progress monitor} to use during operation execution.
	 * @return
	 * @throws OperationCanceledException
	 *         Thrown when the transaction is canceled by the user.
	 * @throws ExecutionException
	 *         Thrown when the transaction could not be completed to an Exception.
	 * @since 0.7.0
	 **/
	public static IStatus executeInWriteTransaction(TransactionalEditingDomain editingDomain, final Runnable runnable,
		final String operationLabel, IOperationHistory operationHistory, final Map<String, Object> transactionOptions,
		IProgressMonitor monitor) throws OperationCanceledException, ExecutionException
	{
		Assert.isNotNull(editingDomain);
		Assert.isNotNull(runnable);

		String safeLabel = operationLabel == null ? "Unnamed operation" : operationLabel; //$NON-NLS-1$
		IUndoableOperation operation = new AbstractEMFOperation(editingDomain, safeLabel, transactionOptions)
		{
			@Override
			protected IStatus doExecute(IProgressMonitor monitor, IAdaptable info) throws ExecutionException
			{
				try
				{
					runnable.run();
					return Status.OK_STATUS;
				}
				catch (RuntimeException ex)
				{
					if (ex instanceof OperationCanceledException)
					{
						throw (OperationCanceledException) ex;
					}
					else
					{
						throw new ExecutionException(NLS.bind("\"{0}\" transaction failed", operationLabel), ex); //$NON-NLS-1$
					}
				}
			}

			@Override
			public boolean canUndo()
			{
				return transactionOptions.get(Transaction.OPTION_NO_UNDO) != Boolean.TRUE;
			}
		};
		// Perform the execution of the transaction.
		IStatus status = operationHistory.execute(operation, monitor, null);

		if (status.getSeverity() == IStatus.CANCEL)
		{
			throw new OperationCanceledException();
		}
		return status;
	}
}
