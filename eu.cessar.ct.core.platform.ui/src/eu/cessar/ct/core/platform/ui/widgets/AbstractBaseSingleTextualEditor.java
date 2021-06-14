/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 * 
 * </copyright>
 */
package eu.cessar.ct.core.platform.ui.widgets;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

import eu.cessar.ct.core.platform.ui.events.EFocusEvent;
import eu.cessar.req.Requirement;

/**
 * A base implementation of an editor for textual single values.
 * 
 */
@SuppressWarnings("javadoc")
@Requirement(
	reqID = "REQ_EDIT_PROP#2")
public abstract class AbstractBaseSingleTextualEditor<T> extends AbstractSingleEditor<T>
{
	protected boolean inAcceptData;

	protected boolean escPressed;

	private class TextListener implements Listener
	{
		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.swt.widgets.Listener#handleEvent(org.eclipse.swt.widgets.Event)
		 */
		public void handleEvent(Event e)
		{

			switch (e.type)
			{

				case SWT.KeyUp:

					if (e.character == SWT.CR)
					{
						if (isReadOnly())
						{
							e.doit = false;
						}
						else
						{
							e.doit = doAcceptData();
						}

						if (eventListener != null)
						{
							eventListener.notify(EFocusEvent.CR);
						}
					}

					break;

				case SWT.FocusOut:
					if (escPressed)
					{
						e.doit = false;
						if (eventListener != null)
						{
							eventListener.notify(EFocusEvent.ESC);
						}
					}
					else
					{
						if (isReadOnly())
						{
							e.doit = false;
						}
						else
						{
							e.doit = doAcceptData();
						}
						if (eventListener != null)
						{
							eventListener.notify(EFocusEvent.FOCUS_OUT);
						}
					}
					break;
				default:
					break;
			}

			if (!e.doit)
			{
				T oldInputData = getOldInputData();
				if (oldInputData != null)
				{
					setInputData(oldInputData);
				}
				else
				{
					setInputData(getDefaultData());
				}
			}
		}
	}

	/**
	 * @param acceptNull
	 */
	public AbstractBaseSingleTextualEditor(boolean acceptNull)
	{
		super(acceptNull);
	}

	protected Listener createTextListener()
	{
		return new TextListener();
	}

	/**
	 * @param text2
	 * @return If new data is accepted
	 */
	public boolean doAcceptData()
	{
		if (!inAcceptData)
		{
			inAcceptData = true;
			try
			{
				T oldData = getOldInputData();
				T newData = getDataFromUI();

				// trigger notifications only if new data is introduced
				// in this way wrongly editor's dirty state is avoided
				if (!isSameData(oldData, newData))
				{
					return notifyAcceptData(oldData, newData);
				}

				return true;
			}
			finally
			{
				inAcceptData = false;
			}
		}
		else
		{
			return false;
		}
	}

	/**
	 * Check if the two objects have equal values.
	 * 
	 * @param oldData
	 * @param newData
	 * @return <code>true</code> if data's values are equal and <code>false</code> otherwise.
	 */
	protected abstract boolean isSameData(T oldData, T newData);

	/**
	 * @param parent
	 * @param style
	 * @return The newly created Text
	 */
	protected Text createTextEditor(Composite parent)
	{
		Text text = getToolkit().createText(parent, "", SWT.BORDER | SWT.SINGLE); //$NON-NLS-1$

		return text;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.core.platform.ui.widgets.IDatatypeEditor#setStatusMessage(org.eclipse.core.runtime.IStatus)
	 */
	public final void setStatusMessage(IStatus status)
	{
		// TODO Auto-generated method stub
	}

	/**
	 * @param value
	 * @return The data converted from string
	 */
	protected abstract T convertFromString(String value);

	/**
	 * @param value
	 * @return The string conversion of data
	 */
	protected abstract String convertToString(T value);

}
