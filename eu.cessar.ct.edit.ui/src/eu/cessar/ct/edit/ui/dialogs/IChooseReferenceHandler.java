package eu.cessar.ct.edit.ui.dialogs;

import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.viewers.ILabelProvider;

/**
 * @author uidl6870
 * 
 */
public interface IChooseReferenceHandler<T>
{

	/**
	 * Called by the {@link ReferenceSelectionDialog} to get the window title
	 * 
	 * @return a String representing the name of the edited element (short name
	 *         of a feature/a reference value's definition)
	 */
	public String getDialogTitle();

	/**
	 * Return the existing value or <code>null</code> if the value is not set
	 * 
	 * @return
	 */
	public T getReferencedValue();

	/**
	 * Set the value of the reference
	 * 
	 * @param value
	 */
	public void setReferencedValue(T value);

	/**
	 * Return the list of possible candidates
	 * 
	 * @return
	 */
	public List<Object> getCandidates();

	/**
	 * Return the label provider for the tree viewer inside
	 * ReferenceSelectionDialog
	 * 
	 * @return
	 */
	public ILabelProvider getTreeLabelProvider();

	/**
	 * Get container object
	 */
	public EObject getInputObject();
}
