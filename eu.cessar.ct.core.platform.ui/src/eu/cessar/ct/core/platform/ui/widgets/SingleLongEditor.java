package eu.cessar.ct.core.platform.ui.widgets;

import eu.cessar.ct.core.platform.util.CompareDataUtils;
import eu.cessar.ct.core.platform.util.ERadix;

/**
 * Editor for a single long value.
 * 
 */
public class SingleLongEditor extends AbstractSingleNumericalEditor<Long>
{
	/**
	 * Creates an instance of the editor.
	 * 
	 * @param acceptNull
	 *        if <code>true</code>, a <code>null</code> value will be accepted in <code>setInputData(data)</code>.<br>
	 *        if <code>false</code>, the value will be set to the default value 0.
	 */
	public SingleLongEditor(boolean acceptNull)
	{
		super(acceptNull);
	}

	/**
	 * Creates an instance of the editor.
	 * 
	 * @param radix
	 * @param acceptNull
	 *        if <code>true</code>, a <code>null</code> value will be accepted in <code>setInputData(data)</code>.<br>
	 *        if <code>false</code>, the value will be set to the default value 0.
	 */
	public SingleLongEditor(boolean acceptNull, ERadix radix)
	{
		this(acceptNull);
		this.radix = radix;
	}

	/**
	 * @param radix
	 *        sets the radix and updates the UI to show that available radix
	 */
	@Override
	public void setRadix(ERadix radix)
	{
		if (haveUI())
		{
			Long dataFromUI = getDataFromUI();
			this.radix = radix;
			isSetRadixOnUI = true;
			setDataToUI(dataFromUI);
		}
		else
		{
			this.radix = radix;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.core.platform.ui.widgets.AbstractSingleNumericalEditor#isValidPattern(java.lang.String)
	 */
	/**
	 * @return true if value matches the long radix
	 */
	@Override
	protected boolean isValidPattern(String value)
	{
		if ("".equals(value)) //$NON-NLS-1$
		{
			return true;
		}
		return radix.getPattern().matcher(value).matches();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.core.platform.ui.widgets.AbstractSingleTextualEditor#doAcceptData()
	 */
	/**
	 * @return true if UI data can be converted to Long; else returns old value from super
	 */
	@Override
	public boolean doAcceptData()
	{
		try
		{
			getDataFromUI();
		}
		catch (NumberFormatException e)
		{
			return false;
		}
		return super.doAcceptData();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.core.platform.ui.widgets.AbstractSingleTextualEditor#convertFromString(java.lang.String)
	 */
	/**
	 * @return String value converted to Long; else null
	 */
	@Override
	protected Long convertFromString(String value)
	{
		if (value == null || "".equals(value)) //$NON-NLS-1$
		{
			return null;
		}
		else
		{
			return Long.valueOf(value, radix.getRadixNumber());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.core.platform.ui.widgets.AbstractSingleTextualEditor#convertToString(java.lang.Object)
	 */
	/**
	 * @return Long value converted to String; else ""
	 */
	@Override
	protected String convertToString(Long value)
	{
		if (value == null)
		{
			return ""; //$NON-NLS-1$
		}
		else
		{

			return Long.toString(value, radix.getRadixNumber());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.core.platform.ui.widgets.AbstractSingleEditor#getDefaultData()
	 */
	/**
	 * @return default value 0 for Long
	 */
	@Override
	protected Long getDefaultData()
	{
		return new Long(0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.core.platform.ui.widgets.AbstractSingleEditor#isNull(java.lang.Object)
	 */
	@Override
	protected boolean isNull(Long data)
	{
		return data == null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.core.platform.ui.widgets.AbstractBaseSingleTextualEditor#isNewData(java.lang.Object,
	 * java.lang.Object)
	 */
	@Override
	protected boolean isSameData(Long oldData, Long newData)
	{
		return CompareDataUtils.equal(oldData, newData);
	}

}
