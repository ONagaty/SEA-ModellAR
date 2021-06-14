/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * </copyright>
 */
package eu.cessar.ct.core.platform.ui.widgets;

/**
 * Abstract editor for a single value.
 * 
 * @Review uidl6458 - 19.04.2012
 */
public abstract class AbstractSingleEditor<T> extends AbstractEditor<T>
{
	private T data;
	private boolean isReadOnly;
	private boolean isEnabled;

	/**
	 * @param acceptNull
	 *        if <code>true</code>, a <code>null</code> value will be accepted .<br>
	 *        if <code>false</code>, the value will be set to the default value
	 */
	public AbstractSingleEditor(boolean acceptNull)
	{
		super(acceptNull);
		if (!acceptNull)
		{
			data = getDefaultData();
		}
	}

	protected abstract T getDefaultData();

	protected abstract boolean isNull(T data);

	/* (non-Javadoc)
	 * @see eu.cessar.ct.core.platform.ui.widgets.IDatatypeEditor#setInputData(java.lang.Object)
	 */
	public final void setInputData(T data)
	{
		if (isNull(data) && !isAcceptingNull())
		{
			data = getDefaultData();
		}
		this.data = data;
		if (haveUI())
		{
			setDataToUI(data);
		}
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.core.platform.ui.widgets.IDatatypeEditor#getInputData()
	 */
	public final T getInputData()
	{
		if (haveUI())
		{
			data = getDataFromUI();
		}
		return data;
	}

	/**
	 * @return
	 */
	public T getOldInputData()
	{
		return data;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.core.platform.ui.widgets.IDatatypeEditor#isSetInputData()
	 */
	public final boolean isSetInputData()
	{
		return !isNull(getInputData());
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.core.platform.ui.widgets.IDatatypeEditor#unsetInputData()
	 */
	public final void unsetInputData()
	{
		setInputData(null);
	}

	/**
	 * @param data2
	 */
	protected abstract void setDataToUI(T data);

	/**
	 * @return
	 */
	protected abstract T getDataFromUI();

	/**
	 * @return
	 */
	protected abstract boolean haveUI();

	protected abstract void doSetReadOnly();

	/* (non-Javadoc)
	 * @see eu.cessar.ct.core.platform.ui.widgets.IDatatypeEditor#isReadOnly()
	 */
	public boolean isReadOnly()
	{
		return isReadOnly;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.core.platform.ui.widgets.IDatatypeEditor#setReadOnly()
	 */
	public void setReadOnly(boolean isReadOnly)
	{
		this.isReadOnly = isReadOnly;
		if (haveUI())
		{
			doSetReadOnly();
		}
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.core.platform.ui.widgets.IDatatypeEditor#isEnabled()
	 */
	public boolean isEnabled()
	{
		return isEnabled;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.core.platform.ui.widgets.IDatatypeEditor#setEnabled(boolean)
	 */
	public void setEnabled(boolean enabled)
	{
		this.isEnabled = enabled;
		if (haveUI())
		{
			doSetEnabled(enabled);
		}

	}

	/**
	 * @param enabled
	 */
	protected abstract void doSetEnabled(boolean enabled);

}
