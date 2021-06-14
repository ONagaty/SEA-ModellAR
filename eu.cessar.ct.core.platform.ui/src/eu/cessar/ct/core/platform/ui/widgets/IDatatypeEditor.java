/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Mar 11, 2010 2:40:18 PM </copyright>
 */
package eu.cessar.ct.core.platform.ui.widgets;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import eu.cessar.ct.core.platform.ui.events.IEditorListener;
import eu.cessar.ct.core.platform.ui.events.IFocusEventListener;

/**
 * @author uidl6458
 * 
 */
public interface IDatatypeEditor<T>
{

	/**
	 * Create the editor
	 * 
	 * @param parent
	 * @return
	 */
	public Control createEditor(Composite parent);

	/**
	 * @return
	 */
	public boolean isAcceptingNull();

	/**
	 * @param data
	 */
	public void setInputData(T data);

	/**
	 * @return
	 */
	public T getInputData();

	/**
	 * @return
	 */
	public boolean isSetInputData();

	/**
	 * 
	 */
	public void unsetInputData();

	/**
	 * Set the status message to the editor.
	 * 
	 * @param level
	 * @param message
	 */
	public void setStatusMessage(IStatus status);

	/**
	 * Set the identification prefix
	 * 
	 * @param prefix
	 */
	public void setIdentificationPrefix(String prefix);

	/**
	 * Return the identification prefix
	 * 
	 * @return
	 */
	public String getIdentificationPrefix();

	/**
	 * @param listener
	 */
	public void addEditorListener(IEditorListener<T> listener);

	/**
	 * 
	 * @param listener
	 */
	public void setEventListener(IFocusEventListener listener);

	/**
	 * 
	 * @param enablement
	 */
	public void deliverFocusLost(boolean enablement);

	/**
	 * 
	 * @return
	 */
	public boolean shouldDeliverFocusLost();

	/**
	 * @param listener
	 */
	public void removeEditorListener(IEditorListener<T> listener);

	/**
	 * Returns whether the editor is read-only.
	 * 
	 * @return
	 */
	public boolean isReadOnly();

	/**
	 * Sets the read-only flag to the editor.
	 * 
	 * @param isReadOnly
	 */
	public void setReadOnly(boolean isReadOnly);

	/**
	 * Returns whether the editor is enabled.
	 * 
	 * @return
	 */
	public boolean isEnabled();

	/**
	 * Sets the enabled flag to the editor.
	 * 
	 * @param isReadOnly
	 */
	public void setEnabled(boolean enabled);

	/**
	 * Gives focus to the control. For complex editors, the focus is given to
	 * the first control from left to right
	 */
	public void setFocus();

}
