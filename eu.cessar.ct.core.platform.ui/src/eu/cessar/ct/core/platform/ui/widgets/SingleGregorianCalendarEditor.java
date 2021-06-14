package eu.cessar.ct.core.platform.ui.widgets;

import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DateTime;

import eu.cessar.ct.core.internal.platform.ui.CessarPluginActivator;
import eu.cessar.ct.core.platform.ui.IdentificationUtils;

/**
 * Editor for a single GregorianCalendar value.
 * 
 */
public class SingleGregorianCalendarEditor extends AbstractSingleEditor<XMLGregorianCalendar>
{
	private DateTime date;
	private DateTime time;

	/**
	 * Creates an instance of the editor.
	 * 
	 * @param acceptNull
	 *        if <code>true</code>, a <code>null</code> value will be accepted in <code>setInputData(data)</code>.<br>
	 *        if <code>false</code>, the value will be set to the current date and time.
	 */
	public SingleGregorianCalendarEditor(boolean acceptNull)
	{
		super(acceptNull);
	}

	/**
	 * @return
	 */
	protected XMLGregorianCalendar createXMLGregorianCalendar()
	{
		DatatypeFactory newInstance;
		XMLGregorianCalendar calendar = null;
		try
		{
			newInstance = DatatypeFactory.newInstance();
			if (newInstance != null)
			{
				calendar = newInstance.newXMLGregorianCalendar(new GregorianCalendar());
			}
		}
		catch (DatatypeConfigurationException e)
		{
			CessarPluginActivator.getDefault().logError(e);
		}

		return calendar;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.core.platform.ui.widgets.IDatatypeEditor#createEditor(org.eclipse.swt.widgets.Composite)
	 */
	public Control createEditor(Composite parent)
	{
		Composite composite = getToolkit().createComposite(parent);
		GridLayout gridLayout = new GridLayout(2, false);
		gridLayout.marginHeight = 0;
		gridLayout.marginWidth = 0;
		composite.setLayout(gridLayout);

		date = new DateTime(composite, SWT.DATE | SWT.MEDIUM | SWT.BORDER);
		date.setData(IdentificationUtils.KEY_NAME, getIdentificationPrefix() + "#" //$NON-NLS-1$
			+ IdentificationUtils.DATE_ID);
		date.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true, false));

		addFocusListener(date);

		time = new DateTime(composite, SWT.TIME | SWT.MEDIUM | SWT.BORDER);
		time.setData(IdentificationUtils.KEY_NAME, getIdentificationPrefix() + "#" //$NON-NLS-1$
			+ IdentificationUtils.TIME_ID);
		time.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true, false));

		addFocusListener(time);

		date.addSelectionListener(new SelectionAdapter()
		{
			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
			 */
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				SingleGregorianCalendarEditor.this.widgetSelected(e);
			}

		});

		time.addSelectionListener(new SelectionAdapter()
		{

			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
			 */
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				SingleGregorianCalendarEditor.this.widgetSelected(e);
			}
		});

		doSetReadOnly();
		return composite;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.core.platform.ui.widgets.AbstractSingleEditor#haveUI()
	 */
	@Override
	protected boolean haveUI()
	{
		return date != null && time != null && !date.isDisposed() && !time.isDisposed();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.core.platform.ui.widgets.AbstractSingleEditor#getDefaultData()
	 */
	@Override
	protected XMLGregorianCalendar getDefaultData()
	{
		return createXMLGregorianCalendar();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.core.platform.ui.widgets.AbstractSingleEditor#isNull(java.lang.Object)
	 */
	@Override
	protected boolean isNull(XMLGregorianCalendar data)
	{
		return data == null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.core.platform.ui.widgets.AbstractSingleEditor#setDataToUI(java.lang.Object)
	 */
	@Override
	protected void setDataToUI(XMLGregorianCalendar data)
	{
		doSetReadOnly();
		if (data != null)
		{
			date.setDate(data.getYear(), data.getMonth(), data.getDay());
			time.setTime(data.getHour(), data.getMinute(), data.getSecond());
		}
		else
		{
			// TODO: define how null should look like
			Color grayColor = date.getDisplay().getSystemColor(SWT.COLOR_DARK_GRAY);
			date.setForeground(grayColor);
			time.setForeground(grayColor);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.core.platform.ui.widgets.AbstractSingleEditor#getDataFromUI()
	 */
	@Override
	protected XMLGregorianCalendar getDataFromUI()
	{
		XMLGregorianCalendar newData = createXMLGregorianCalendar();
		if (newData != null)
		{
			newData.setDay(date.getDay());
			newData.setMonth(date.getMonth() + 1);
			newData.setYear(date.getYear());
			newData.setHour(time.getHours());
			newData.setMinute(time.getMinutes());
			newData.setSecond(time.getSeconds());
		}
		return newData;
	}

	/**
	 * Handles the <code>date</code> and <code>time</code> selection events.
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
		date.setEnabled(!isReadOnly());
		time.setEnabled(!isReadOnly());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.core.platform.ui.widgets.AbstractSingleEditor#doSetEnabled(boolean)
	 */
	@Override
	protected void doSetEnabled(boolean enabled)
	{
		date.setEnabled(enabled);
		time.setEnabled(enabled);
	}

}
