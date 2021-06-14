/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidl6458 Mar 11, 2010 5:43:15 PM </copyright>
 */
package eu.cessar.ct.core.platform.ui.widgets;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;

import eu.cessar.ct.core.platform.ui.IdentificationUtils;
import eu.cessar.ct.core.platform.ui.PlatformUIConstants;
import eu.cessar.ct.core.platform.ui.dialogs.MultiValueDialog;

/**
 * @author uidl6458
 * @param <T>
 *
 * @Review uidl6458 - 19.04.2012
 */
public abstract class AbstractMultiEditor<T> extends AbstractEditor<List<T>> implements IMultiValueDatatypeEditor<T>
{

	/**
	 *
	 */
	protected Text text;
	private MultiValueDialog<T> multiDialog;
	private int maxAllowedValues;
	private List<T> inputData;
	private MultiDatatypeValueHandler<T> handler;
	private Button browseBtn;
	private boolean isReadOnly;
	private boolean isEnabled;

	/**
	 * @param handler
	 */
	public AbstractMultiEditor(MultiDatatypeValueHandler<T> handler)
	{
		super(false);
		this.handler = handler;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.core.platform.ui.widgets.IDatatypeEditor#createEditor(org.eclipse.swt.widgets.Composite)
	 */
	public Control createEditor(Composite parent)
	{
		Composite editor = getToolkit().createComposite(parent);
		GridLayout gl = new GridLayout(2, false);
		gl.marginHeight = 0;
		gl.marginWidth = 0;
		editor.setLayout(gl);

		text = getToolkit().createText(editor, null, SWT.READ_ONLY | SWT.BORDER);
		text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		text.setData(IdentificationUtils.KEY_NAME, getIdentificationPrefix() + "#" + IdentificationUtils.TEXT_ID); //$NON-NLS-1$
		addFocusListener(text);
		addKeyListener(text);

		browseBtn = getToolkit().createButton(editor, "...", SWT.PUSH); //$NON-NLS-1$
		browseBtn.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));
		browseBtn.setData(IdentificationUtils.KEY_NAME, getIdentificationPrefix() + "#" //$NON-NLS-1$
			+ IdentificationUtils.PUSH_BUTTON_ID);
		browseBtn.addSelectionListener(new SelectionAdapter()
		{
			/*
			 * (non-Javadoc)
			 *
			 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
			 */
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				doActivateBrowseDialog();
			}
		});

		addFocusListener(browseBtn);

		setReadOnly(isReadOnly());
		return editor;
	}

	/**
	 *
	 */
	protected void doActivateBrowseDialog()
	{
		handler.setMultiValueDatatypeEditor(this);
		multiDialog = new MultiValueDialog<T>(text.getShell(), handler);

		deliverFocusLost(false);
		multiDialog.open();
		deliverFocusLost(true);

		multiDialog = null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.core.platform.ui.widgets.IDatatypeEditor#setStatusMessage(org.eclipse.core.runtime.IStatus)
	 */
	public void setStatusMessage(IStatus status)
	{
		if (multiDialog != null)
		{
			// deliver the status to the dialog
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.core.platform.ui.widgets.IMultiValueDatatypeEditor#acceptNewValues(java.util.List)
	 */
	public boolean acceptNewValues(List<T> newValues)
	{
		return notifyAcceptData(getInputData(), newValues);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.core.platform.ui.widgets.IMultiValueDatatypeEditor#setMaxValues(int)
	 */
	public void setMaxValues(int maxAllowedValues)
	{
		this.maxAllowedValues = maxAllowedValues;
	}

	/**
	 * @return the maxAllowedValues
	 */
	public int getMaxValues()
	{
		return maxAllowedValues;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.core.platform.ui.widgets.IDatatypeEditor#getInputData()
	 */
	public List<T> getInputData()
	{
		return inputData;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.core.platform.ui.widgets.IDatatypeEditor#setInputData(java.lang.Object)
	 */
	public void setInputData(List<T> data)
	{
		if (data == null)
		{
			inputData = new ArrayList<T>();
		}
		else
		{
			inputData = new ArrayList<T>(data);
		}
		updateUI();
	}

	/**
	 * Update the UI with the new data
	 */
	protected void updateUI()
	{
		if (text != null && !text.isDisposed() && inputData != null)
		{
			ILabelProvider provider = handler.getLabelProvider();
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < inputData.size(); i++)
			{
				sb.append(provider.getText(inputData.get(i)));
				if (i < inputData.size() - 1)
				{
					sb.append(", "); //$NON-NLS-1$
				}
			}
			text.setText(sb.toString());
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.core.platform.ui.widgets.IDatatypeEditor#isSetInputData()
	 */
	public boolean isSetInputData()
	{
		return inputData != null && !inputData.isEmpty();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.core.platform.ui.widgets.IDatatypeEditor#unsetInputData()
	 */
	public void unsetInputData()
	{
		inputData = new ArrayList<T>();
		updateUI();
	}

	/**
	 * @return - true if there is UI
	 */
	public boolean haveUI()
	{
		return text != null && !text.isDisposed() && browseBtn != null && !browseBtn.isDisposed();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.core.platform.ui.widgets.IDatatypeEditor#isReadOnly()
	 */
	public boolean isReadOnly()
	{
		return isReadOnly;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.core.platform.ui.widgets.IDatatypeEditor#setReadOnly(boolean)
	 */
	public void setReadOnly(boolean readOnly)
	{
		this.isReadOnly = readOnly;
		if (haveUI())
		{
			browseBtn.setEnabled(!readOnly);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.core.platform.ui.widgets.IDatatypeEditor#setEnabled(boolean)
	 */
	public void setEnabled(boolean enabled)
	{
		this.isEnabled = enabled;
		if (haveUI())
		{
			text.setEnabled(enabled);
			browseBtn.setEnabled(enabled);
		}

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.core.platform.ui.widgets.IDatatypeEditor#isEnabled()
	 */
	public boolean isEnabled()
	{
		return isEnabled;
	}

	/**
	 * sets the error color to the editor Text
	 */
	protected void setErrorOnTextEditor()
	{
		text.setBackground(PlatformUIConstants.EDITOR_ERROR_COLOR);
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
		}
	}
}
