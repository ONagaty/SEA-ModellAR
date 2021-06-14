package eu.cessar.ct.edit.ui.facility.expansion;

import java.util.List;

import org.eclipse.emf.ecore.EObject;

import eu.cessar.ct.edit.ui.facility.IModelFragmentEditor;

/**
 * /** Interface corresponding to the expansion of a
 * {@link IModelFragmentEditor}, for the case in which the editor is a
 * single-value editor.
 * 
 */

public abstract class AbstractSingleModelFragmentEditorExpansion extends
	AbstractModelFragmentEditorExpansion
{
	/**
	 * @param masterEditor
	 * @param featureNamesForExpansion
	 */
	public AbstractSingleModelFragmentEditorExpansion(IModelFragmentEditor masterEditor,
		List<String> featureNamesForExpansion)
	{
		super(masterEditor, featureNamesForExpansion);
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.edit.ui.facility.expansion.AbstractModelFragmentEditorExpansion#isMultiValueEditor()
	 */
	@Override
	public final boolean isMultiValueEditor()
	{
		return false;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.edit.ui.facility.expansion.IModelFragmentEditorExpansion#getExpansionInput()
	 */
	public Object getExpansionInput()
	{
		return doGetExpansionInput();
	}

	/**
	 * 
	 * @return
	 */
	protected abstract EObject doGetExpansionInput();

	/* (non-Javadoc)
	 * @see eu.cessar.ct.edit.ui.facility.expansion.IModelFragmentEditorExpansion#createExpansionInputMember()
	 */
	public final EObject createExpansionInputMember()
	{
		throw new UnsupportedOperationException(
			"Cannot create a new input member for this single-value editor!"); //$NON-NLS-1$
	}

}
