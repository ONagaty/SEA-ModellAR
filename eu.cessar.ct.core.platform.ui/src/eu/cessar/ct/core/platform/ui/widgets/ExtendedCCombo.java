/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidt2045 Aug 13, 2010 1:13:51 PM </copyright>
 */
package eu.cessar.ct.core.platform.ui.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.widgets.Composite;

/**
 * @author uidt2045
 * 
 */
public class ExtendedCCombo extends CCombo
{

	private boolean readOnly;
	private VerifyListener readOnlyVerifyListener;
	private KeyListener keyListener;

	/**
	 * @param parent
	 * @param style
	 */
	public ExtendedCCombo(Composite parent, int style)
	{
		super(parent, style);
		setEditable(false);
		readOnlyVerifyListener = new VerifyListener()
		{

			public void verifyText(VerifyEvent e)
			{
				e.doit = !readOnly;

			}
		};
		addVerifyListener(readOnlyVerifyListener);

		keyListener = new KeyListener()
		{

			public void keyReleased(KeyEvent e)
			{
				// TODO Auto-generated method stub
				if (e.keyCode == SWT.DEL || e.character == SWT.BS)
				{
					// String text= getText();
					e.doit = !readOnly;
				}
			}

			public void keyPressed(KeyEvent e)
			{
				// TODO Auto-generated method stub

			}
		};
		addKeyListener(keyListener);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.widgets.Widget#dispose()
	 */
	@Override
	public void dispose()
	{
		removeVerifyListener(readOnlyVerifyListener);
		super.dispose();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.custom.CCombo#checkSubclass()
	 */
	@Override
	protected void checkSubclass()
	{
		// do nothing
	}

	/**
	 * @param readOnly
	 */
	public void setReadOnly(boolean readOnly)
	{
		checkWidget();
		this.readOnly = readOnly;
	}

	/**
	 * @return true if the combo is read-only, else returns false.
	 */
	public boolean getReadOnly()
	{
		checkWidget();
		return readOnly;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.custom.CCombo#setText(java.lang.String)
	 */
	@Override
	public void setText(String string)
	{
		checkWidget();
		boolean isRo = getReadOnly();
		setReadOnly(false);
		try
		{
			super.setText(string);
		}
		finally
		{
			setReadOnly(isRo);
		}
	}

}
