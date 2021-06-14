/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 * 
 * </copyright>
 */
package eu.cessar.ct.core.platform.ui.widgets;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Listener;

import eu.cessar.ct.core.platform.ui.IdentificationUtils;

/**
 * Base implementation of an editor for single values, where the values can be directly edited or chosen from a list of
 * predefined values. The last ones shall be passed via {@link #setPredefinedValues(List)}
 */
public abstract class AbstractSingleTextualWithChoiceEditor<T> extends AbstractBaseSingleTextualEditor<T>
{
	private ExtendedCCombo combo;

	private List<String> items;

	/**
	 * @param acceptNull
	 */
	public AbstractSingleTextualWithChoiceEditor(boolean acceptNull)
	{
		super(acceptNull);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.core.platform.ui.widgets.IDatatypeEditor#createEditor(org.eclipse.swt.widgets.Composite)
	 */
	public Control createEditor(Composite parent)
	{
		combo = createComboEditor(parent);
		combo.setData(IdentificationUtils.KEY_NAME, getIdentificationPrefix() + "#" + IdentificationUtils.COMBO_ID); //$NON-NLS-1$

		Listener textListener = createTextListener();
		combo.addListener(SWT.KeyUp, textListener);
		combo.addListener(SWT.FocusOut, textListener);
		combo.addKeyListener(new KeyAdapter()
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
		return combo;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.core.platform.ui.widgets.AbstractSingleEditor#getDataFromUI()
	 */
	@Override
	protected T getDataFromUI()
	{
		return convertFromString(combo.getText());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.core.platform.ui.widgets.AbstractSingleEditor#haveUI()
	 */
	@Override
	public boolean haveUI()
	{
		return combo != null && !combo.isDisposed();
	}

	@Override
	protected void setDataToUI(T data)
	{
		doSetReadOnly();
		combo.setText(convertToString(data));
	}

	/**
	 * @param parent
	 * @param style
	 * @return
	 */
	protected ExtendedCCombo createComboEditor(Composite parent)
	{
		ExtendedCCombo combo = getToolkit().createCCombo(parent, SWT.BORDER);
		combo.setEditable(true);
		if (items == null)
		{
			setPredefinedValues(new ArrayList<String>());
		}
		String[] itemsArray = new String[] {};
		itemsArray = items.toArray(itemsArray);
		combo.setItems(itemsArray);

		return combo;
	}

	/**
	 * Set the list of predefined allowed values, which will be directly accessible in the UI
	 * 
	 * @param values
	 */
	public void setPredefinedValues(List<String> values)
	{
		if (values != null)
		{
			items = new ArrayList<String>(values);

			if (combo != null && !combo.isDisposed())
			{
				String[] itemsArray = new String[] {};
				itemsArray = items.toArray(itemsArray);
				combo.setItems(itemsArray);
			}
		}
		else
		{
			this.items = new ArrayList<String>();
		}
	}

	/**
	 * Returns the list with the predefined values for the editor
	 * 
	 * @return
	 */
	public List<String> getPredefinedValues()
	{
		if (items == null)
		{
			return new ArrayList<String>();
		}
		return items;
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
			combo.setEnabled(!isReadOnly());
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
		combo.setEnabled(enabled);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.core.platform.ui.widgets.AbstractEditor#setFocus()
	 */
	@Override
	public void setFocus()
	{
		if (combo != null && !combo.isDisposed())
		{
			combo.setFocus();
		}
	}

}
