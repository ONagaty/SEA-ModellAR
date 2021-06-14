/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 *
 * </copyright>
 */
package eu.cessar.ct.core.platform.ui.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

import eu.cessar.ct.core.platform.ui.IdentificationUtils;

/**
 * An abstract editor for textual single values.
 * 
 * @param <T>
 *
 * @Review uidl6458 - 19.04.2012
 */
public abstract class AbstractSingleTextualEditor<T> extends AbstractBaseSingleTextualEditor<T>
{
	private Text text;

	/**
	 * @param acceptNull
	 */
	public AbstractSingleTextualEditor(boolean acceptNull)
	{
		super(acceptNull);
	}

	/**
	 * @return text
	 */
	protected Text getText()
	{
		return text;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.core.platform.ui.widgets.IDatatypeEditor#createEditor(org.eclipse.swt.widgets.Composite)
	 */
	public Control createEditor(Composite parent)
	{
		text = createTextEditor(parent);
		text.setData(IdentificationUtils.KEY_NAME, getIdentificationPrefix() + "#" + IdentificationUtils.TEXT_ID); //$NON-NLS-1$

		Listener textListener = createTextListener();
		text.addListener(SWT.KeyUp, textListener);
		text.addListener(SWT.FocusOut, textListener);
		text.addKeyListener(new KeyAdapter()
		{
			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.swt.events.KeyAdapter#keyPressed(org.eclipse.swt.events.KeyEvent)
			 */
			@Override
			public void keyPressed(KeyEvent e)
			{
				escPressed = (e.character == SWT.ESC);
			}
		});
		doSetReadOnly();

		return text;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.core.platform.ui.widgets.AbstractSingleEditor#getDataFromUI()
	 */
	@Override
	protected T getDataFromUI()
	{
		return convertFromString(text.getText());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.core.platform.ui.widgets.AbstractSingleEditor#haveUI()
	 */
	@Override
	public boolean haveUI()
	{
		return text != null && !text.isDisposed();
	}

	@Override
	protected void setDataToUI(T data)
	{
		doSetReadOnly();
		text.setText(convertToString(data));
	}

	/**
	 * @param parent
	 * @return text
	 */
	@Override
	protected Text createTextEditor(Composite parent)
	{
		Text text = getToolkit().createText(parent, "", SWT.BORDER | SWT.SINGLE); //$NON-NLS-1$

		return text;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.core.platform.ui.widgets.AbstractSingleEditor#doSetReadOnly()
	 */
	@Override
	protected void doSetReadOnly()
	{
		if (haveUI())
		{
			text.setEditable(!isReadOnly());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.core.platform.ui.widgets.AbstractSingleEditor#doSetEnabled(boolean)
	 */
	@Override
	protected void doSetEnabled(boolean enabled)
	{
		text.setEnabled(enabled);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.core.platform.ui.widgets.AbstractEditor#setFocus()
	 */
	@Override
	public void setFocus()
	{
		if (text != null && !text.isDisposed())
		{
			text.setFocus();
			text.selectAll();
		}
	}

}
