/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Mar 11, 2010 2:52:48 PM </copyright>
 */
package eu.cessar.ct.core.platform.ui.widgets;

import org.eclipse.core.commands.common.EventManager;
import org.eclipse.core.runtime.SafeRunner;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;

import eu.cessar.ct.core.platform.ui.events.EFocusEvent;
import eu.cessar.ct.core.platform.ui.events.IEditorListener;
import eu.cessar.ct.core.platform.ui.events.IFocusEventListener;
import eu.cessar.ct.core.platform.ui.util.CessarFormToolkit;
import eu.cessar.ct.core.platform.util.SafeRunnable;

/**
 * @author uidl6458
 * 
 * @Review uidl6458 - 19.04.2012
 */
public abstract class AbstractEditor<T> extends EventManager implements IDatatypeEditor<T>
{
	private CessarFormToolkit toolkit;
	private boolean acceptNull;

	private String idPrefix;

	protected IFocusEventListener eventListener;
	private boolean deliverFocusLost = true;

	/**
	 * @param acceptNull
	 */
	public AbstractEditor(boolean acceptNull)
	{
		this.acceptNull = acceptNull;
	}

	/**
	 * @param toolkit
	 *        the toolkit to set
	 */
	public void setToolkit(CessarFormToolkit toolkit)
	{
		this.toolkit = toolkit;
	}

	/**
	 * @return the toolkit
	 */
	public CessarFormToolkit getToolkit()
	{
		if (toolkit == null)
		{
			toolkit = new CessarFormToolkit(Display.getCurrent());
		}
		return toolkit;
	}

	/**
	 * @param listener
	 */
	public void addEditorListener(IEditorListener<T> listener)
	{
		addListenerObject(listener);
	}

	/**
	 * @param listener
	 */
	public void removeEditorListener(IEditorListener<T> listener)
	{
		removeListenerObject(listener);
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.core.platform.ui.widgets.IDatatypeEditor#isAcceptingNull()
	 */
	public boolean isAcceptingNull()
	{
		return acceptNull;
	}

	/**
	 * @param oldData
	 * @param newData
	 * @return
	 */
	protected boolean notifyAcceptData(final T oldData, final T newData)
	{
		Object[] listeners = getListeners();
		for (Object listenerObj: listeners)
		{
			@SuppressWarnings("unchecked")
			final IEditorListener<T> listener = (IEditorListener<T>) listenerObj;
			final boolean[] result = new boolean[] {true};
			SafeRunner.run(new SafeRunnable()
			{

				public void run() throws Exception
				{
					result[0] = listener.acceptData(oldData, newData);
				}
			});
			if (!result[0])
			{
				return false;
			}
		}
		return true;

	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.core.platform.ui.widgets.IDatatypeEditor#addEventListener(eu.cessar.ct.core.platform.ui.widgets.IEventListener)
	 */
	public void setEventListener(IFocusEventListener listener)
	{
		eventListener = listener;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.core.platform.ui.widgets.IDatatypeEditor#shouldDeliverFocusLost()
	 */
	public boolean shouldDeliverFocusLost()
	{
		return deliverFocusLost;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.core.platform.ui.widgets.IDatatypeEditor#deliverFocusLost(boolean)
	 */
	public void deliverFocusLost(boolean deliverFocusLost)
	{
		this.deliverFocusLost = deliverFocusLost;

	}

	protected void addFocusListener(Control ctrl)
	{
		ctrl.addFocusListener(new FocusListener()
		{

			public void focusLost(FocusEvent e)
			{
				if (eventListener != null && shouldDeliverFocusLost())
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
	 * @see eu.cessar.ct.core.platform.ui.widgets.IDatatypeEditor#setIdentificationPrefix(java.lang.String)
	 */
	public void setIdentificationPrefix(String prefix)
	{
		this.idPrefix = prefix;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.core.platform.ui.widgets.IDatatypeEditor#getIdentificationPrefix()
	 */
	public String getIdentificationPrefix()
	{
		return idPrefix;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.core.platform.ui.widgets.IDatatypeEditor#setFocus()
	 */
	public void setFocus()
	{
		// do nothing
	}

}
