package eu.cessar.ct.core.platform.ui.widgets;

import eu.cessar.ct.core.platform.util.CompareDataUtils;
import eu.cessar.ct.core.platform.util.ERadix;

/**
 * Editor for a single integer value.
 * 
 */
public class SingleIntegerEditor extends AbstractSingleNumericalEditor<Integer>
{
	/**
	 * Creates an instance of the editor.
	 * 
	 * @param acceptNull
	 *        if <code>true</code>, a <code>null</code> value will be accepted in <code>setInputData(data)</code>.<br>
	 *        if <code>false</code>, the value will be set to the default value 0.
	 */
	public SingleIntegerEditor(boolean acceptNull)
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
	public SingleIntegerEditor(boolean acceptNull, ERadix radix)
	{
		this(acceptNull);
		this.radix = radix;
	}

	/**
	 * @param radix
	 */
	@Override
	public void setRadix(ERadix radix)
	{
		if (haveUI())
		{
			Integer dataFromUI = getDataFromUI();
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
	@Override
	protected Integer convertFromString(String value)
	{
		if (value == null || "".equals(value)) //$NON-NLS-1$
		{
			return null;
		}
		else
		{
			return Integer.valueOf(value, radix.getRadixNumber());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.core.platform.ui.widgets.AbstractSingleTextualEditor#convertToString(java.lang.Object)
	 */
	@Override
	protected String convertToString(Integer value)
	{
		if (value == null)
		{
			return ""; //$NON-NLS-1$
		}
		else
		{

			return Integer.toString(value, radix.getRadixNumber());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.core.platform.ui.widgets.AbstractSingleEditor#getDefaultData()
	 */
	@Override
	protected Integer getDefaultData()
	{
		return new Integer(0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.core.platform.ui.widgets.AbstractSingleEditor#isNull(java.lang.Object)
	 */
	@Override
	protected boolean isNull(Integer data)
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
	protected boolean isSameData(Integer oldData, Integer newData)
	{
		return CompareDataUtils.equal(oldData, newData);
	}

}
