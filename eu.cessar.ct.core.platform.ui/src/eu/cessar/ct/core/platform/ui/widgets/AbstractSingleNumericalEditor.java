/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * </copyright>
 */
package eu.cessar.ct.core.platform.ui.widgets;

import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

import eu.cessar.ct.core.platform.util.ERadix;

/**
 * An abstract editor for numerical single values.
 * 
 * @Review uidl6458 - 19.04.2012
 */
public abstract class AbstractSingleNumericalEditor<T> extends AbstractSingleTextualEditor<T>
{
	protected VerifyListener verifyListener;
	protected boolean isSetRadixOnUI;
	protected ERadix radix = ERadix.DECIMAL;

	/**
	 * @param acceptNull
	 */
	public AbstractSingleNumericalEditor(boolean acceptNull)
	{
		super(acceptNull);
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.core.platform.ui.widgets.AbstractSingleTextualEditor#createTextEditor(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected Text createTextEditor(Composite parent)
	{
		Text text = super.createTextEditor(parent);
		verifyListener = new VerifyListener()
		{

			public void verifyText(VerifyEvent e)
			{
				e.doit = isValidPattern(e.text);
			}
		};

		text.addVerifyListener(verifyListener);
		return text;
	}

	/**
	 * Checks if the value matches the corresponding pattern for the type of
	 * value to edit.
	 * 
	 * @param value
	 *        the value to check for validity
	 */
	protected abstract boolean isValidPattern(String value);

	public abstract void setRadix(ERadix radix);

	/**
	 * @return radix type
	 */
	public ERadix getRadix()
	{
		return radix;
	}

	/**
	 * Returns whether the user has changed the radix for this editor
	 * 
	 * @return
	 */
	public boolean isSetRadixOnUI()
	{
		return isSetRadixOnUI;
	}

}
