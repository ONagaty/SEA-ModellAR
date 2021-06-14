package eu.cessar.ct.edit.ui.facility.expansion;

import java.util.List;

import org.eclipse.emf.ecore.EObject;

import eu.cessar.ct.edit.ui.facility.IModelFragmentEditor;

/**
 * /** Interface corresponding to the expansion of a
 * {@link IModelFragmentEditor}, for the case in which the editor is a
 * multi-value editor.
 * 
 */

public abstract class AbstractMultiModelFragmentEditorExpansion extends
	AbstractModelFragmentEditorExpansion
{

	/**
	 * @param masterEditor
	 * @param featureNamesForExpansion
	 */
	public AbstractMultiModelFragmentEditorExpansion(IModelFragmentEditor masterEditor,
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
		return true;
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
	protected abstract List<EObject> doGetExpansionInput();

}
