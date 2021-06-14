package eu.cessar.ct.core.platform.ui.widgets;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import eu.cessar.ct.core.platform.ui.IdentificationUtils;

/**
 * Editor for a list of allowed values.
 *
 */
public class ListEditor extends AbstractSingleEditor<String>
{
	private org.eclipse.swt.widgets.List list;
	private List<String> items;

	/**
	 * Creates an instance of the editor.
	 *
	 * @param acceptNull
	 *        if <code>true</code>, a <code>null</code> value will be accepted in <code>setInputData(data)</code>.<br>
	 *        if <code>false</code>, the value will be set to the first one from the list of allowed values.
	 */
	public ListEditor(boolean acceptNull)
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
		list = getToolkit().createList(parent, SWT.BORDER);
		list.setData(IdentificationUtils.KEY_NAME, getIdentificationPrefix() + "#" //$NON-NLS-1$
			+ IdentificationUtils.LIST_ID);
		String[] itemsArray = new String[] {};
		if (items == null)
		{
			setAllowedValues(new ArrayList<String>());
		}

		itemsArray = items.toArray(itemsArray);
		list.setItems(itemsArray);

		if (itemsArray.length > 0)
		{
			list.select(0);
		}

		list.addSelectionListener(new SelectionAdapter()
		{
			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
			 */
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				ListEditor.this.widgetSelected(e);
			}
		});

		addFocusListener(list);
		addKeyListener(list);

		doSetReadOnly();
		return list;
	}

	/**
	 * Handles the <code>list</code>'s selection event.
	 *
	 * @param e
	 *        the received event
	 */
	protected void widgetSelected(SelectionEvent e)
	{
		e.doit = notifyAcceptData(getOldInputData(), getInputData());
	}

	/**
	 * @param values
	 */
	public void setAllowedValues(List<String> values)
	{
		if (values != null)
		{
			items = new ArrayList<String>(values);
			if (isAcceptingNull())
			{
				items.add(0, ""); //$NON-NLS-1$
			}
			if (list != null && !list.isDisposed())
			{
				String[] itemsArray = new String[] {};
				itemsArray = items.toArray(itemsArray);
				list.setItems(itemsArray);
			}
		}
		else
		{
			items = new ArrayList<String>();
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.core.platform.ui.widgets.AbstractSingleEditor#haveUI()
	 */
	@Override
	protected boolean haveUI()
	{
		return list != null && !list.isDisposed();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.core.platform.ui.widgets.AbstractSingleEditor#getDefaultData()
	 */
	@Override
	protected String getDefaultData()
	{
		if (items != null && !items.isEmpty())
		{
			return items.get(0);
		}
		return ""; //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.core.platform.ui.widgets.AbstractSingleEditor#setDataToUI(java.lang.Object)
	 */
	@Override
	protected void setDataToUI(String data)
	{
		String defaultData = data;
		if (data == null)
		{
			defaultData = getDefaultData();
		}
		int index = items.indexOf(defaultData);
		if (index >= 0)
		{
			list.select(index);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.core.platform.ui.widgets.AbstractSingleEditor#getDataFromUI()
	 */
	@Override
	protected String getDataFromUI()
	{
		int index = list.getSelectionIndex();
		if ((index == -1) || ((index == 0) && isAcceptingNull()))
		{
			return null;
		}
		else
		{
			return items.get(index);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.core.platform.ui.widgets.AbstractSingleEditor#isNull(java.lang.Object)
	 */
	@Override
	protected boolean isNull(String data)
	{
		return data == null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.core.platform.ui.widgets.IDatatypeEditor#setStatusMessage(org.eclipse.core.runtime.IStatus)
	 */
	public void setStatusMessage(IStatus status)
	{
		// TODO Auto-generated method stub
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.core.platform.ui.widgets.AbstractSingleEditor#doSetEnabled(boolean)
	 */
	@Override
	protected void doSetEnabled(boolean enabled)
	{
		list.setEnabled(enabled);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.core.platform.ui.widgets.AbstractEditor#setFocus()
	 */
	@Override
	public void setFocus()
	{
		if (list != null && !list.isDisposed())
		{
			list.setFocus();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.core.platform.ui.widgets.AbstractSingleEditor#doSetReadOnly()
	 */
	@Override
	protected void doSetReadOnly()
	{
		// TODO Auto-generated method stub
	}
}
