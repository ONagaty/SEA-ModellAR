package eu.cessar.ct.edit.ui.facility.expansion;

import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.viewers.ILabelProvider;

import eu.cessar.ct.edit.ui.facility.IModelFragmentEditor;
import eu.cessar.ct.edit.ui.facility.IModelFragmentEditorProvider;

/**
 * Interface corresponding to the expansion of a {@link IModelFragmentEditor}
 * 
 */
public interface IModelFragmentEditorExpansion
{
	/**
	 * Returns a list of editor providers which must be used for the expansion
	 * of the editor.<br>
	 * The given <code>input</code> represents one of the members of the multi
	 * input list retrieved by {@link #getMultiInput()}.<br>
	 * If the given <code>input</code> is <code>null</code>, the input will be
	 * taken from the corresponding master editor, that must be set before
	 * calling this method.
	 * 
	 * @return
	 */
	public List<IModelFragmentEditorProvider> getEditorProviders(EObject input,
		List<IModelFragmentEditor> upperLevelEditors);

	/**
	 * Returns weather the editor can be expanded
	 * 
	 * @return
	 */
	public boolean canExpand();

	/**
	 * Returns whether the editor is meant to edit multiple instances of the
	 * same type
	 */
	public boolean isMultiValueEditor();

	/**
	 * 
	 */
	public void dispose();

	/**
	 * Returns the master editor for which the expansion is created.
	 * 
	 * @return
	 */
	public IModelFragmentEditor getMasterEditor();

	/**
	 * Returns the input corresponding to the expansion of the editor.<br>
	 * For a multi-value editor ({@link #isMultiValueEditor()} returns
	 * <code>true</code>), this method returns a <code>List</code> of
	 * <code>EObject</code> , never <code>null</code>. The list will be derived
	 * from the input set in the method {@link #setInput(EObject)}. The list
	 * will never be null and its manipulation will be reflected into the model
	 * behind.<br>
	 * For a single-value editor, this method returns a single {@link EObject}.
	 * 
	 * 
	 * @return the input corresponding to the expansion of the editor
	 */
	public Object getExpansionInput();

	/**
	 * Creates a new input for this multi value editor. The new input will not
	 * be effectively added; it is the caller responsibility to manipulate the
	 * returned list.
	 * 
	 * @return
	 */
	public EObject createExpansionInputMember();

	/**
	 * @return
	 */
	public ILabelProvider getLabelProvider();

	/**
	 * Sets the expandable state of the expansion editor. The expansion editor
	 * will handle the enablement state of its master editor.
	 * 
	 * @param expanded
	 */
	public void setExpanded(boolean expanded);

	/**
	 * Returns whether the expansion editor is really expanded. This is useful
	 * for different listeners, that are notified by changes in the master
	 * editor of the expansion.
	 * 
	 * @return
	 */
	public boolean isExpanded();

	/**
	 * Returns the type of relation that is established between the the master
	 * editor and the editors provided by expansion
	 * 
	 * @return
	 */
	public EExpansionType getType();

}
