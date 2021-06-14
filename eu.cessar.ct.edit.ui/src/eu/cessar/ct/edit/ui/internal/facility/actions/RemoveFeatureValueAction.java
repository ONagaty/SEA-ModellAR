package eu.cessar.ct.edit.ui.internal.facility.actions;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jface.action.Action;
import org.eclipse.sphinx.emf.util.WorkspaceEditingDomainUtil;
import org.eclipse.sphinx.emf.util.WorkspaceTransactionUtil;
import org.eclipse.swt.widgets.Event;

import eu.cessar.ct.edit.ui.facility.IModelFragmentFeatureEditor;
import eu.cessar.ct.edit.ui.internal.CessarPluginActivator;
import eu.cessar.ct.edit.ui.internal.Messages;

/**
 * Action for removing feature values.
 *
 */
public class RemoveFeatureValueAction extends Action
{
	private final IModelFragmentFeatureEditor editor;

	public RemoveFeatureValueAction(IModelFragmentFeatureEditor editor)
	{
		this.editor = editor;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jface.action.Action#isEnabled()
	 */
	@Override
	public boolean isEnabled()
	{
		boolean readOnly = !editor.getEditableStatus().isOK();
		if (readOnly)
		{
			return false;
		}
		EObject inputObject = editor.getInput();
		EStructuralFeature inputFeature = editor.getInputFeature();
		return inputObject != null && inputFeature != null && inputObject.eIsSet(inputFeature);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jface.action.Action#getText()
	 */
	@Override
	public String getText()
	{
		return "Remove value"; //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jface.action.Action#runWithEvent(org.eclipse.swt.widgets.Event)
	 */
	@Override
	public void runWithEvent(Event e)
	{
		// get input object and feature to remove
		final EObject inputObject = editor.getInput();
		final EStructuralFeature inputFeature = editor.getInputFeature();

		if (inputObject != null && inputFeature != null)
		{
			Object featureValue = inputObject.eGet(inputFeature);
			if (featureValue != null)
			{
				remove(new Runnable()
				{
					public void run()
					{
						inputObject.eUnset(inputFeature);
					}
				}, Messages.Command_removeValue);

				editor.refresh();
			}
		}
	}

	/**
	 *
	 * @param runnable
	 * @param label
	 */
	private void remove(Runnable runnable, String label)
	{
		try
		{
			WorkspaceTransactionUtil.executeInWriteTransaction(
				WorkspaceEditingDomainUtil.getEditingDomain(editor.getInput()), runnable, label);
		}
		catch (OperationCanceledException e)
		{
			CessarPluginActivator.getDefault().logError(e);
		}
		catch (ExecutionException e)
		{
			CessarPluginActivator.getDefault().logError(e);
		}
	}
}
