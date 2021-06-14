/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870 Mar 16, 2010 11:49:36 AM </copyright>
 */
package eu.cessar.ct.edit.ui.facility.parts.editor;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.sphinx.emf.util.WorkspaceEditingDomainUtil;
import org.eclipse.sphinx.emf.util.WorkspaceTransactionUtil;

import eu.cessar.ct.core.platform.ui.util.CessarFormToolkit;
import eu.cessar.ct.edit.ui.facility.IModelFragmentEditor;
import eu.cessar.ct.edit.ui.facility.parts.IModelFragmentEditorPart;
import eu.cessar.ct.edit.ui.internal.CessarPluginActivator;

/**
 * Base implementation of a {@link IModelFragmentEditorPart}
 * 
 * @author uidl6870
 * 
 */
public abstract class AbstractEditorPart implements IModelFragmentEditorPart
{

	private IModelFragmentEditor editor;

	/**
	 * @param editor
	 *        model fragment editor that owns the part
	 */
	public AbstractEditorPart(IModelFragmentEditor editor)
	{
		this.editor = editor;
	}

	/**
	 * @param editor
	 *        the parent editor
	 */
	public void setEditor(IModelFragmentEditor editor)
	{
		this.editor = editor;
	}

	/**
	 * @return the parent editor
	 */
	public IModelFragmentEditor getEditor()
	{
		return editor;
	}

	/**
	 * @return the input object
	 */
	protected EObject getInputObject()
	{
		return getEditor().getInput();
	}

	/**
	 * @return the editing domain which the input object belongs to
	 */
	protected TransactionalEditingDomain getEditingDomain()
	{
		return WorkspaceEditingDomainUtil.getEditingDomain(getInputObject());
	}

	/**
	 * @param runnable
	 *        the runnable to execute
	 * @param label
	 *        label of the operation
	 * @return <code>true</code> if the change has been performed, <code>false</code> otherwise
	 */
	protected boolean performChangeWithChecks(Runnable runnable, String label)
	{
		try
		{
			WorkspaceTransactionUtil.executeInWriteTransaction(getEditingDomain(), runnable, label);
			return true;
		}
		catch (OperationCanceledException e)
		{
			refresh();
			return false;
		}
		catch (ExecutionException e)
		{
			CessarPluginActivator.getDefault().logError(e);
			return false;
		}
	}

	/**
	 * @return the CESSAR form toolkit
	 */
	protected CessarFormToolkit getFormToolkit()
	{
		return getEditor().getFormToolkit();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.facility.parts.IModelFragmentEditorPart#setEnabled(boolean)
	 */
	@Override
	public void setEnabled(boolean enabled)
	{
		// by default, do nothing
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.facility.parts.IModelFragmentEditorPart#setFocus()
	 */
	public void setFocus()
	{
		// by default, do nothing
	}

}
