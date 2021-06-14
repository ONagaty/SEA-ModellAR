package eu.cessar.ct.edit.ui.facility;

import java.util.List;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jface.action.Action;
import org.eclipse.sphinx.emf.util.WorkspaceEditingDomainUtil;
import org.eclipse.sphinx.emf.util.WorkspaceTransactionUtil;
import org.eclipse.swt.widgets.Event;

import eu.cessar.ct.edit.ui.internal.CessarPluginActivator;

/**
 * Action for removing all the features edited by an editor.
 * 
 */
public class RemoveEditedFeaturesAction extends Action
{

	private final IModelFragmentEditor editor;

	/**
	 * 
	 * @param editor
	 */
	public RemoveEditedFeaturesAction(IModelFragmentEditor editor)
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
		List<EStructuralFeature> editedFeatures = editor.getEditorProvider().getEditedFeatures();
		EObject input = editor.getInput();
		if (input != null)
		{
			for (EStructuralFeature feature: editedFeatures)
			{
				if (input.eIsSet(feature))
				{
					return true;
				}
			}
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.action.Action#getText()
	 */
	@Override
	public String getText()
	{
		return "Remove values"; //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.action.Action#runWithEvent(org.eclipse.swt.widgets
	 * .Event)
	 */
	@Override
	public void runWithEvent(Event event)
	{
		List<EStructuralFeature> editedFeatures = editor.getEditorProvider().getEditedFeatures();
		final EObject input = editor.getInput();
		if (input != null)
		{
			for (final EStructuralFeature feature: editedFeatures)
			{
				if (input.eIsSet(feature))
				{
					remove(new Runnable()
					{
						public void run()
						{
							input.eUnset(feature);
						}
					}, "Remove values");
				}
			}
			editor.refreshWithNotifications();
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
