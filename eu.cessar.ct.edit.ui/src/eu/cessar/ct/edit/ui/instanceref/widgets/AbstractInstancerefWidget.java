/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870 May 19, 2010 11:41:20 AM </copyright>
 */
package eu.cessar.ct.edit.ui.instanceref.widgets;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.widgets.Control;

import eu.cessar.ct.core.platform.ui.events.EFocusEvent;
import eu.cessar.ct.core.platform.ui.events.IFocusEventListener;
import eu.cessar.ct.edit.ui.facility.IModelFragmentEditor;

/**
 * @author uidl6870
 * 
 */
public abstract class AbstractInstancerefWidget<T> implements IInstanceRefWidget<T>
{
	private String idPrefix;
	private boolean isReadOnly;
	private boolean isEnabled;

	protected IFocusEventListener eventListener;
	protected final IModelFragmentEditor editor;

	public AbstractInstancerefWidget(IModelFragmentEditor editor)
	{
		this.editor = editor;

	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.edit.ecuc.ui.internal.instancerefs.IInstanceRefEditor#setIdentificationPrefix(java.lang.String)
	 */
	public void setIdentificationPrefix(String prefix)
	{
		this.idPrefix = prefix;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.edit.ecuc.ui.internal.instancerefs.IInstanceRefEditor#getIdentificationPrefix()
	 */
	public String getIdentificationPrefix()
	{
		return idPrefix;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.edit.ecuc.ui.internal.instancerefs.IInstanceRefEditor#setStatusMessage(org.eclipse.core.runtime.IStatus)
	 */
	public void setStatusMessage(IStatus status)
	{
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.edit.ui.instanceref.widgets.IInstanceRefWidget#isReadOnly()
	 */
	public boolean isReadOnly()
	{
		return isReadOnly;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.edit.ui.instanceref.widgets.IInstanceRefWidget#setReadOnly(boolean)
	 */
	public void setReadOnly(boolean isReadOnly)
	{
		this.isReadOnly = isReadOnly;
		doSetReadOnly();

	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.edit.ui.instanceref.widgets.IInstanceRefWidget#setEnabled(boolean)
	 */
	public void setEnabled(boolean enabled)
	{
		this.isEnabled = enabled;
		doSetEnabled(enabled);
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.edit.ui.instanceref.widgets.IInstanceRefWidget#isEnabled()
	 */
	public boolean isEnabled()
	{
		return isEnabled;
	}

	/**
	 * @param enabled
	 * 
	 */
	protected abstract void doSetEnabled(boolean enabled);

	/**
	 * @param isReadOnly2
	 */
	protected abstract void doSetReadOnly();

	protected void addFocusListener(Control control)
	{

		control.addFocusListener(new FocusListener()
		{

			public void focusLost(FocusEvent e)
			{
				if (eventListener != null)
				{
					eventListener.notify(EFocusEvent.FOCUS_OUT);
				}
			}

			public void focusGained(FocusEvent e)
			{
				// do nothing
			}
		});
	}

	protected void addKeyListener(Control ctrl)
	{
		ctrl.addKeyListener(new KeyListener()
		{

			public void keyReleased(KeyEvent e)
			{
				if (e.character == SWT.CR)
				{
					if (eventListener != null)
					{
						eventListener.notify(EFocusEvent.CR);
					}
				}
			}

			public void keyPressed(KeyEvent e)
			{
				// do nothing

			}
		});
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.edit.ui.instanceref.widgets.IInstanceRefWidget#setEventListener(eu.cessar.ct.core.platform.ui.widgets.IEventListener)
	 */
	public void setEventListener(IFocusEventListener listener)
	{
		eventListener = listener;

	}

}
