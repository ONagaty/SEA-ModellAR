/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870 21.05.2012 11:19:19 </copyright>
 */
package eu.cessar.ct.core.platform.ui.widgets;

import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.widgets.Composite;

import eu.cessar.ct.core.platform.util.ERadix;

/**
 * 
 * An abstract editor for numerical single values, where the values can be
 * edited or chosen from a list of predefined values
 * 
 * @author uidl6870
 * 
 */
public abstract class AbstractSingleNumericalWithChoiceEditor<T> extends
	AbstractSingleTextualWithChoiceEditor<T>
{
	protected VerifyListener verifyListener;
	protected boolean isSetRadixOnUI;
	protected ERadix radix = ERadix.DECIMAL;

	/**
	 * @param acceptNull
	 */
	public AbstractSingleNumericalWithChoiceEditor(boolean acceptNull)
	{
		super(acceptNull);

	}

	@Override
	protected ExtendedCCombo createComboEditor(Composite parent)
	{
		ExtendedCCombo combo = super.createComboEditor(parent);
		verifyListener = new VerifyListener()
		{

			public void verifyText(VerifyEvent e)
			{
				e.doit = isValidPattern(e.text);
			}
		};

		combo.addVerifyListener(verifyListener);
		return combo;
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
