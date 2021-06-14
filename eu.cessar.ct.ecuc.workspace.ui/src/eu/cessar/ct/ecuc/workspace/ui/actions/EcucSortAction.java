/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidt2045 Jul 14, 2011 2:59:52 PM </copyright>
 */
package eu.cessar.ct.ecuc.workspace.ui.actions;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;

import eu.cessar.ct.ecuc.workspace.sort.EcuSortProvider;
import eu.cessar.ct.ecuc.workspace.ui.internal.CessarPluginActivator;
import eu.cessar.ct.workspace.sort.IDirectionalSortCriterion;
import eu.cessar.ct.workspace.sort.ISortProvider;
import eu.cessar.ct.workspace.sort.ISortTarget;
import eu.cessar.ct.workspace.ui.sort.SortDialog;

/**
 * @author uidt2045
 * 
 */
public class EcucSortAction implements IEditorActionDelegate
{
	protected EObject selectedARObject;

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IActionDelegate#run(org.eclipse.jface.action.IAction)
	 */
	public void run(IAction action)
	{
		if (selectedARObject != null)
		{
			final ISortProvider sortProvider = getSortProvider();
			Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
			SortDialog dialog = new SortDialog(shell, sortProvider);
			dialog.setBlockOnOpen(true);
			if (dialog.open() == IDialogConstants.OK_ID)
			{
				final ISortTarget sortTarget = dialog.getSortTarget();
				final List<IDirectionalSortCriterion> criteria = dialog.getSortCriteria();
				final boolean isGrouped = dialog.getGroupedFlag();

				ProgressMonitorDialog progressDialog = new ProgressMonitorDialog(shell);

				try
				{
					progressDialog.run(true, false, new IRunnableWithProgress()
					{

						public void run(final IProgressMonitor monitor)
							throws InvocationTargetException, InterruptedException
						{
							try
							{
								sortProvider.performSort(sortTarget, criteria, isGrouped, monitor);
							}
							catch (OperationCanceledException e)
							{
								InterruptedException ex = new InterruptedException(e.getMessage());
								ex.initCause(e);
								throw ex;
							}
							catch (ExecutionException e)
							{
								throw new InvocationTargetException(e);
							}
						}
					});
				}
				catch (InvocationTargetException e)
				{
					CessarPluginActivator.getDefault().logError(e);
				}
				catch (InterruptedException e)
				{
					// ignored
				}
			}
		}

	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IActionDelegate#selectionChanged(org.eclipse.jface.action.IAction, org.eclipse.jface.viewers.ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection)
	{
		setSelectedObject(null);
		if (selection instanceof IStructuredSelection)
		{
			IStructuredSelection strSelection = (IStructuredSelection) selection;
			if (strSelection.size() == 1)
			{
				setSelectedObject(strSelection.getFirstElement());
			}
		}

	}

	/**
	 * @param object
	 */
	private void setSelectedObject(Object object)
	{
		selectedARObject = null;
		if (object != null)
		{
			if (!(object instanceof EObject))
			{
				object = AdapterFactoryEditingDomain.unwrap(object);
			}
			if (object instanceof EObject)
			{
				selectedARObject = (EObject) object;
			}
		}
	}

	protected ISortProvider getSortProvider()
	{
		return EcuSortProvider.create(
			(TransactionalEditingDomain) AdapterFactoryEditingDomain.getEditingDomainFor(selectedARObject),
			selectedARObject);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IEditorActionDelegate#setActiveEditor(org.eclipse.jface.action.IAction, org.eclipse.ui.IEditorPart)
	 */
	public void setActiveEditor(IAction action, IEditorPart targetEditor)
	{
	}

}
