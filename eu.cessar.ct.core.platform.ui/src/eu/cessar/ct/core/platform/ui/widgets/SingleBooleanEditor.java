package eu.cessar.ct.core.platform.ui.widgets;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;

import eu.cessar.ct.core.platform.ui.IdentificationUtils;
import eu.cessar.ct.core.platform.ui.events.EFocusEvent;

/**
 * Editor for a single boolean value.
 * 
 */
public class SingleBooleanEditor extends AbstractSingleEditor<Boolean>
{
	private Button checkButton;

	/**
	 * Creates an instance of the editor.
	 * 
	 * @param acceptNull
	 *        if <code>true</code>, a <code>null</code> value will be accepted .<br>
	 *        if <code>false</code>, the value will be set to the default value <code>Boolean.FALSE</code>.
	 */
	public SingleBooleanEditor(boolean acceptNull)
	{
		super(acceptNull);
	}

	/**
	 * Create only the checkbox part of the editor.
	 * 
	 * @param parent
	 * @return The created checkbox
	 */
	public Button createCheckboxEditor(Composite parent)
	{
		checkButton = getToolkit().createButton(parent, null, SWT.CHECK);

		checkButton.setData(IdentificationUtils.KEY_NAME, getIdentificationPrefix() + "#" //$NON-NLS-1$
			+ IdentificationUtils.CHECK_BUTTON_ID);
		checkButton.addSelectionListener(new SelectionAdapter()
		{
			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
			 */
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				SingleBooleanEditor.this.widgetSelected(e);
			}
		});

		addFocusListener(checkButton);
		// activate editor on enter pressed
		checkButton.addListener(SWT.KeyDown, new Listener()
		{

			public void handleEvent(Event event)
			{
				if (event.character == SWT.CR && eventListener != null)
				{
					eventListener.notify(EFocusEvent.CR);
				}
			}
		});

		doSetReadOnly();

		return checkButton;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.core.platform.ui.widgets.IDatatypeEditor#createEditor(org.eclipse.swt.widgets.Composite)
	 */
	public Control createEditor(Composite parent)
	{
		Composite wrapper = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout(2, false);
		wrapper.setLayout(layout);

		Button checkbox = createCheckboxEditor(wrapper);

		GridData buttonGD = new GridData(SWT.BEGINNING, SWT.CENTER, false, false);
		checkbox.setLayoutData(buttonGD);

		Label spacer = new Label(wrapper, SWT.NONE);
		spacer.setText(""); //$NON-NLS-1$
		GridData spacerGD = new GridData(SWT.FILL, SWT.CENTER, true, false);
		spacer.setLayoutData(spacerGD);

		return wrapper;
	}

	/**
	 * Set the text of the button. It will succeed only if the text is not null and the button is created.
	 * 
	 * @param text
	 */
	public void setText(String text)
	{
		if (text != null && checkButton != null && !checkButton.isDisposed())
		{
			checkButton.setText(text);
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
		return checkButton != null && !checkButton.isDisposed();
	}

	/**
	 * Handles the <code>checkButton</code>'s selection event.
	 * 
	 * @param e
	 *        the received event
	 */
	protected void widgetSelected(SelectionEvent e)
	{
		e.doit = notifyAcceptData(getOldInputData(), getInputData());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.core.platform.ui.widgets.AbstractSingleEditor#getDefaultData()
	 */
	@Override
	protected Boolean getDefaultData()
	{
		return Boolean.FALSE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.core.platform.ui.widgets.AbstractSingleEditor#isNull(java.lang.Object)
	 */
	@Override
	protected boolean isNull(Boolean data)
	{
		return data == null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.core.platform.ui.widgets.AbstractSingleEditor#getDataFromUI()
	 */
	@Override
	protected Boolean getDataFromUI()
	{
		if (isAcceptingNull())
		{
			if (checkButton.getGrayed() && checkButton.getSelection())
			{
				return null;
			}
		}
		return checkButton.getSelection();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.core.platform.ui.widgets.AbstractSingleEditor#setDataToUI(java.lang.Object)
	 */
	@Override
	protected void setDataToUI(Boolean data)
	{
		doSetReadOnly();
		if (data == null)
		{
			checkButton.setGrayed(true);
			checkButton.setSelection(true);
		}
		else
		{
			checkButton.setGrayed(false);
			checkButton.setSelection(data);
		}
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
	 * @see eu.cessar.ct.core.platform.ui.widgets.AbstractSingleEditor#doSetReadOnly()
	 */
	@Override
	protected void doSetReadOnly()
	{
		checkButton.setEnabled(!isReadOnly());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.core.platform.ui.widgets.AbstractSingleEditor#doSetEnabled(boolean)
	 */
	@Override
	protected void doSetEnabled(boolean enabled)
	{
		checkButton.setEnabled(enabled);
	}

}
