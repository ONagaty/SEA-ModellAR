/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidt2045 Aug 20, 2010 12:07:19 PM </copyright>
 */
package eu.cessar.ct.core.platform.ui.prefandprop;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import eu.cessar.ct.core.internal.platform.ui.prefandprop.SWTUtil;

/**
 * Dialog Field containing a single button such as a radio or checkbox button.
 * 
 * @author uidt2045
 * 
 * @Review uidl6458 - 19.04.2012
 */
public class SelectionButtonField extends DialogField
{

	private Button fButton;
	private boolean fIsSelected;
	private int fButtonStyle;

	/**
	 * Creates a selection button. Allowed button styles: SWT.RADIO, SWT.CHECK,
	 * SWT.TOGGLE, SWT.PUSH
	 */
	public SelectionButtonField(int buttonStyle)
	{
		super();
		fIsSelected = false;
		fButtonStyle = buttonStyle;
	}

	// ------- layout helpers

	/*
	 * @see DialogField#doFillIntoGrid
	 */
	@Override
	public Control[] doFillIntoGrid(Composite parent, int nColumns)
	{
		assertEnoughColumns(nColumns);

		Button button = getSelectionButton(parent);
		GridData gd = new GridData();
		gd.horizontalSpan = nColumns;
		gd.horizontalAlignment = GridData.FILL;
		if (fButtonStyle == SWT.PUSH)
		{
			gd.widthHint = SWTUtil.getButtonWidthHint(button);
		}

		button.setLayoutData(gd);

		return new Control[] {button};
	}

	/*
	 * @see DialogField#getNumberOfControls
	 */
	@Override
	public int getNumberOfControls()
	{
		return 1;
	}

	// ------- ui creation

	/**
	 * Returns the selection button widget. When called the first time, the
	 * widget will be created.
	 * 
	 * @param group
	 *        The parent composite when called the first time, or
	 *        <code>null</code> after.
	 */
	public Button getSelectionButton(Composite group)
	{
		if (fButton == null)
		{
			assertCompositeNotNull(group);

			fButton = new Button(group, fButtonStyle);
			fButton.setFont(group.getFont());
			fButton.setText(fLabelText);
			fButton.setEnabled(isEnabled());
			fButton.setSelection(fIsSelected);
			fButton.addSelectionListener(new SelectionListener()
			{
				public void widgetDefaultSelected(SelectionEvent e)
				{
					doWidgetSelected();
				}

				public void widgetSelected(SelectionEvent e)
				{
					doWidgetSelected();
				}
			});
		}
		return fButton;
	}

	/**
	 * 
	 */
	private void doWidgetSelected()
	{
		if (isOkToUse(fButton))
		{
			changeValue(fButton.getSelection());
		}
	}

	/**
	 * @param newState
	 */
	private void changeValue(boolean newState)
	{
		if (fIsSelected != newState)
		{
			fIsSelected = newState;
			dialogFieldChanged();
		}
		else if (fButtonStyle == SWT.PUSH)
		{
			dialogFieldChanged();
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jdt.internal.ui.wizards.dialogfields.DialogField#setLabelText(java.lang.String)
	 */
	@Override
	public void setLabelText(String labeltext)
	{
		fLabelText = labeltext;
		if (isOkToUse(fButton))
		{
			fButton.setText(labeltext);
		}
	}

	/**
	 * Returns the selection state of the button.
	 */
	public boolean isSelected()
	{
		return fIsSelected;
	}

	/**
	 * Sets the selection state of the button.
	 */
	public void setSelection(boolean selected)
	{
		changeValue(selected);
		if (isOkToUse(fButton))
		{
			fButton.setSelection(selected);
		}
	}

	/*
	 * @see DialogField#updateEnableState
	 */
	@Override
	protected void updateEnableState()
	{
		super.updateEnableState();
		if (isOkToUse(fButton))
		{
			fButton.setEnabled(isEnabled());
		}
	}

	/*(non-Javadoc)
	 * @see org.eclipse.jdt.internal.ui.wizards.dialogfields.DialogField#refresh()
	 */
	@Override
	public void refresh()
	{
		super.refresh();
		if (isOkToUse(fButton))
		{
			fButton.setSelection(fIsSelected);
		}
	}
}
