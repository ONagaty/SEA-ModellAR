/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870 May 7, 2010 9:22:06 AM </copyright>
 */
package eu.cessar.ct.edit.ui.instanceref.widgets;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import eu.cessar.ct.core.platform.ui.events.IFocusEventListener;

/**
 * @author uidl6870
 * 
 */
public interface IInstanceRefWidget<T>
{
	/**
	 * Create the editor
	 * 
	 * @param parent
	 * @return
	 */
	public Control createEditor(Composite parent);

	/**
	 * 
	 * @param data
	 */
	public void setInputData(T data);

	/**
	 * Set the prefix of this widget's ID
	 * 
	 * @param prefix
	 *        the prefix ID to set
	 */
	public void setIdentificationPrefix(String prefix);

	/**
	 * Return the prefix ID of this widget
	 * 
	 * @return
	 */
	public String getIdentificationPrefix();

	/**
	 * Set the status message to the editor.
	 * 
	 * @param level
	 * @param message
	 */
	public void setStatusMessage(IStatus status);

	/**
	 * Returns whether the widget is read-only.
	 * 
	 * @return
	 */
	public boolean isReadOnly();

	/**
	 * Sets the read-only flag to the widget.
	 * 
	 * @param isReadOnly
	 */
	public void setReadOnly(boolean isReadOnly);

	/**
	 * Returns whether the widget is enabled.
	 * 
	 * @return
	 */
	public boolean isEnabled();

	/**
	 * Sets the enabled flag to the widget.
	 * 
	 * @param isReadOnly
	 */
	public void setEnabled(boolean enabled);

	/**
	 * Gives focus to the first control
	 */
	public void setFocus();

	public void setEventListener(IFocusEventListener listener);
}
