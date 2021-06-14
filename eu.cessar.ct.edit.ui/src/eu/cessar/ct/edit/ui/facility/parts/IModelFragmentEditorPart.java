/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Mar 2, 2010 7:28:20 PM </copyright>
 */
package eu.cessar.ct.edit.ui.facility.parts;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import eu.cessar.ct.edit.ui.facility.IModelFragmentEditor;

/**
 * The root interface in the <i>model fragment editor's parts hierarchy</i>.
 * 
 * @author uidl6458
 */
public interface IModelFragmentEditorPart
{

	/**
	 * @param editor
	 *        the parent editor
	 */
	public void setEditor(IModelFragmentEditor editor);

	/**
	 * Returns the editor of the editor part
	 * 
	 * @return the parent editor
	 */
	public IModelFragmentEditor getEditor();

	/**
	 * Create the content of the editor part in the provided composite. The parent already have a grid layout set.
	 * Please note that at this point the input might not be set
	 * 
	 * @param parent
	 *        the parent composite
	 * 
	 * @return the control created inside the editing area
	 */
	public Control createContents(Composite parent);

	/**
	 * Gives focus to the control. For complex editors (with multiple controls), the focus will be given to the first
	 * control from left to right. To be called after {@link #createContents(Composite)}
	 */
	public void setFocus();

	/**
	 * Refresh the editor with new informations get from the object
	 */
	public void refresh();

	/**
	 * 
	 */
	public void dispose();

	/**
	 * @param enabled
	 *        the new enabled state of the editor part
	 */
	public void setEnabled(boolean enabled);

}
