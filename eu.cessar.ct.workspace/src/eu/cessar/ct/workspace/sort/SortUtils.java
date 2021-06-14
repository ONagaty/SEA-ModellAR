/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidt2045 Jul 8, 2011 3:18:25 PM </copyright>
 */
package eu.cessar.ct.workspace.sort;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.IOperationHistory;
import org.eclipse.core.commands.operations.IUndoableOperation;
import org.eclipse.core.commands.operations.OperationHistoryFactory;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.command.CommandStack;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.transaction.Transaction;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.impl.TransactionImpl;
import org.eclipse.emf.workspace.AbstractEMFOperation;
import org.eclipse.emf.workspace.IWorkspaceCommandStack;
import org.eclipse.osgi.util.NLS;

import eu.cessar.ct.core.mms.IMetaModelService;
import eu.cessar.ct.core.mms.IMetaModelService.IgnorableFeaturesAplication;
import eu.cessar.ct.core.mms.MMSRegistry;
import eu.cessar.ct.core.mms.MetaModelUtils;
import eu.cessar.ct.workspace.internal.sort.FeatureBasedSortTarget;
import eu.cessar.ct.workspace.internal.sort.SYSSortProviderImpl;

/**
 * @author uidt2045
 * 
 */
public class SortUtils
{

	public static Collection<EStructuralFeature> getIgnorableFeatures(EClass clz)
	{
		IMetaModelService mmService = MMSRegistry.INSTANCE.getMMService(clz);
		return mmService.getIgnorableFeatures(IgnorableFeaturesAplication.Sorting);
	}

	/**
	 * @param rootPackage
	 * @param reference
	 * @return
	 */
	public static EClass getReferedEClass(EPackage rootPackage, EReference reference)
	{
		return reference.getEReferenceType();
	}

	/**
	 * @param editingDomain
	 * @param runnable
	 * @param operationLabel
	 * @throws OperationCanceledException
	 * @throws ExecutionException
	 */
	public static void executeInWriteTransaction(TransactionalEditingDomain editingDomain,
		Runnable runnable, String operationLabel) throws OperationCanceledException,
		ExecutionException
	{
		executeInWriteTransaction(editingDomain, runnable, operationLabel,
			getOperationHistory(editingDomain), getDefaultTransactionOptions(), null);
	}

	/**
	 * @param editingDomain
	 * @param runnable
	 * @param operationLabel
	 * @param operationHistory
	 * @param transactionOptions
	 * @param monitor
	 * @throws OperationCanceledException
	 * @throws ExecutionException
	 */
	private static void executeInWriteTransaction(TransactionalEditingDomain editingDomain,
		final Runnable runnable, final String operationLabel, IOperationHistory operationHistory,
		Map<String, Object> transactionOptions, IProgressMonitor monitor)
		throws OperationCanceledException, ExecutionException
	{
		Assert.isNotNull(editingDomain);
		Assert.isNotNull(runnable);

		String safeLabel = operationLabel == null ? "Unnamed operation" : operationLabel; //$NON-NLS-1$
		IUndoableOperation operation = new AbstractEMFOperation(editingDomain, safeLabel,
			transactionOptions)
		{
			@Override
			protected IStatus doExecute(IProgressMonitor monitor, IAdaptable info)
				throws ExecutionException
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
						throw new ExecutionException(NLS.bind("\"{0}\" transaction failed", //$NON-NLS-1$
							operationLabel), ex);
					}
				}
			}
		};
		// Perform the execution of the transaction.
		IStatus status = operationHistory.execute(operation, monitor, null);

		if (status.getSeverity() == IStatus.CANCEL)
		{
			throw new OperationCanceledException();
		}
	}

	/**
	 * Returns a default set of options which can be used for executing an
	 * operation within a transaction.
	 * 
	 * @return The default options for executing an operation in a transaction.
	 */
	public static Map<String, Object> getDefaultTransactionOptions()
	{
		Map<String, Object> options = new HashMap<String, Object>();
		options.put(TransactionImpl.BLOCK_CHANGE_PROPAGATION, Boolean.TRUE);
		options.put(Transaction.OPTION_NO_VALIDATION, Boolean.TRUE);
		return options;
	}

	/**
	 * @param editingDomain
	 * @return
	 */
	private static IOperationHistory getOperationHistory(TransactionalEditingDomain editingDomain)
	{
		if (editingDomain != null)
		{
			CommandStack commandStack = editingDomain.getCommandStack();
			if (commandStack instanceof IWorkspaceCommandStack)
			{
				return ((IWorkspaceCommandStack) commandStack).getOperationHistory();
			}
		}
		return OperationHistoryFactory.getOperationHistory();
	}

	public static void computeSortTargets(SYSSortProviderImpl sortProvider,
		List<ISortTarget> sortTargets, EClass clz, List<EClass> childClasses)
	{

		EList<EReference> references = clz.getEAllReferences();

		for (EReference eRef: references)
		{
			if (eRef.isMany() && MetaModelUtils.isModelClass(eRef.getEContainingClass()))
			{
				IMetaModelService mmService = MMSRegistry.INSTANCE.getMMService(clz);
				Collection<EClass> allClasses = mmService.getEAllSubClasses(
					eRef.getEReferenceType(), true);

				if (!eRef.getEReferenceType().isAbstract())
				{
					allClasses.add(eRef.getEReferenceType());
				}
				// check to see if there is already a sort target for the same
				// class
				for (EClass subClass: allClasses)
				{
					// create the sortTarget only if there are children that
					// implement that eclass
					if (childClasses.contains(subClass))
					{
						ISortTarget target = createSortTarget(sortProvider, sortTargets, eRef,
							subClass);
						if (target != null)
						{
							sortTargets.add(target);
						}
					}
				}
			}
		}
	}

	/**
	 * @param sortTargets2
	 * @param eRef
	 * @param subClass
	 * @param refToIntermediate
	 */
	private static ISortTarget createSortTarget(SYSSortProviderImpl sortProvider,
		List<ISortTarget> sortTargets, EReference eRef, EClass subClass)
	{
		boolean useFeatureName = false;
		for (ISortTarget sortTarget: sortTargets)
		{
			if (sortTarget instanceof IFeatureBasedSortTarget)
			{
				IFeatureBasedSortTarget fSortTarget = (IFeatureBasedSortTarget) sortTarget;
				if (fSortTarget.getType() == subClass)
				{

					fSortTarget.setUsingFeatureName(true);
					useFeatureName = true;
				}
			}
		}
		IFeatureBasedSortTarget target = new FeatureBasedSortTarget(sortProvider, subClass, eRef);
		target.setUsingFeatureName(useFeatureName);
		return target;
	}
}
