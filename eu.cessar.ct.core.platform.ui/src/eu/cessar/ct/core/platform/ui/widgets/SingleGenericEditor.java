package eu.cessar.ct.core.platform.ui.widgets;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;

import eu.cessar.ct.core.platform.ui.IdentificationUtils;
import eu.cessar.ct.core.platform.ui.PlatformUIConstants;

/**
 * Generic editor to be used when no specific editor is available. If it is
 * used, the background color will be changed to RED to highlight a warning.
 * 
 */
public class SingleGenericEditor extends AbstractSingleEditor<Object>
{
	private Text text;

	/**
	 * @param acceptNull
	 */
	public SingleGenericEditor(boolean acceptNull)
	{
		super(acceptNull);
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.core.platform.ui.widgets.IDatatypeEditor#createEditor(org.eclipse.swt.widgets.Composite)
	 */
	/**
	 * creates the Text Control to display editor values. Additionally changes
	 * the text foreground to ORANGE (warning)
	 */
	public Control createEditor(Composite parent)
	{
		text = getToolkit().createText(parent, null, SWT.BORDER);
		text.setBackground(PlatformUIConstants.EDITOR_ERROR_COLOR);
		text.setData(IdentificationUtils.KEY_NAME, getIdentificationPrefix() + "#" //$NON-NLS-1$
			+ IdentificationUtils.TEXT_ID);
		text.setText(PlatformUIConstants.EDITOR_ERROR_MESSAGE);
		text.setEditable(false);
		return text;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.core.platform.ui.widgets.IDatatypeEditor#setStatusMessage(org.eclipse.core.runtime.IStatus)
	 */
	public void setStatusMessage(IStatus status)
	{
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.core.platform.ui.widgets.AbstractSingleEditor#getDataFromUI()
	 */
	/**
	 * @return last successfully stored data in UI
	 */
	@Override
	protected Object getDataFromUI()
	{
		return getOldInputData();
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.core.platform.ui.widgets.AbstractSingleEditor#getDefaultData()
	 */
	/**
	 * @return default data is null
	 */
	@Override
	protected Object getDefaultData()
	{
		return null;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.core.platform.ui.widgets.AbstractSingleEditor#haveUI()
	 */
	@Override
	protected boolean haveUI()
	{
		return text != null && !text.isDisposed();
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.core.platform.ui.widgets.AbstractSingleEditor#isNull(java.lang.Object)
	 */
	@Override
	protected boolean isNull(Object data)
	{
		return data == null;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.core.platform.ui.widgets.AbstractSingleEditor#setDataToUI(java.lang.Object)
	 */
	/**
	 * @param displays
	 *        data in UI
	 */
	@Override
	protected void setDataToUI(Object data)
	{
		if (data != null)
		{
			text.setText(data.toString());
		}
		else
		{
			text.setText(""); //$NON-NLS-1$
		}
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.core.platform.ui.widgets.AbstractSingleEditor#isReadOnly()
	 */
	@Override
	public boolean isReadOnly()
	{
		return false;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.core.platform.ui.widgets.AbstractSingleEditor#setReadOnly(boolean)
	 */
	@Override
	public void setReadOnly(boolean isReadOnly)
	{
		// do nothing, should not be editable
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.core.platform.ui.widgets.AbstractSingleEditor#doSetReadOnly()
	 */
	@Override
	protected void doSetReadOnly()
	{
		// do nothing, should not be editable
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.core.platform.ui.widgets.AbstractSingleEditor#doSetEnabled(boolean)
	 */
	@Override
	protected void doSetEnabled(boolean enabled)
	{
		if (haveUI())
		{
			text.setEnabled(enabled);
		}

	}
}
