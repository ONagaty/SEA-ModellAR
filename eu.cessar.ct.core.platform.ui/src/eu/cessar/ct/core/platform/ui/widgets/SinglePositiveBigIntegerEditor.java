package eu.cessar.ct.core.platform.ui.widgets;

import java.math.BigInteger;

import eu.cessar.ct.core.platform.util.CompareDataUtils;
import eu.cessar.ct.core.platform.util.ERadix;

/**
 * Editor for a single BigInteger value.
 *
 */
public class SinglePositiveBigIntegerEditor extends AbstractSingleNumericalEditor<BigInteger>
{
	/**
	 * Creates an instance of the editor.
	 *
	 * @param acceptNull
	 *        if <code>true</code>, a <code>null</code> value will be accepted in <code>setInputData(data)</code>.<br>
	 *        if <code>false</code>, the value will be set to the default value <code>BigInteger.ZERO</code>
	 */
	public SinglePositiveBigIntegerEditor(boolean acceptNull)
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
	public SinglePositiveBigIntegerEditor(boolean acceptNull, ERadix radix)
	{
		this(acceptNull);
		this.radix = radix;
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
			BigInteger dataFromUI = getDataFromUI();
			int signum = dataFromUI.signum();
			if (signum == -1)
			{
				return false;
			}
		}
		catch (NumberFormatException e)
		{
			return false;
		}
		return super.doAcceptData();
	}

	@Override
	public boolean isSameData(BigInteger oldData, BigInteger newData)
	{
		return CompareDataUtils.equal(oldData, newData);

	}

	/**
	 * @param radix
	 */
	@Override
	public void setRadix(ERadix radix)
	{
		if (haveUI())
		{
			BigInteger dataFromUI = getDataFromUI();
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
	 * @see eu.cessar.ct.core.platform.ui.widgets.AbstractSingleTextualEditor#convertFromString(java.lang.String)
	 */
	@Override
	protected BigInteger convertFromString(String value)
	{
		if (value == null || "".equals(value)) //$NON-NLS-1$
		{
			return null;
		}
		else
		{
			return new BigInteger(value, radix.getRadixNumber());
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.core.platform.ui.widgets.AbstractSingleTextualEditor#convertToString(java.lang.Object)
	 */
	@Override
	protected String convertToString(BigInteger value)
	{
		if (value == null)
		{
			return ""; //$NON-NLS-1$
		}
		else
		{
			return value.toString(radix.getRadixNumber());
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.core.platform.ui.widgets.AbstractSingleEditor#isNull(java.lang.Object)
	 */
	@Override
	protected boolean isNull(BigInteger data)
	{
		return data == null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.core.platform.ui.widgets.AbstractSingleEditor#getDefaultData()
	 */
	@Override
	protected BigInteger getDefaultData()
	{
		return BigInteger.ZERO;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.core.platform.ui.widgets.AbstractSingleNumericalEditor#isValidPattern(java.lang.String)
	 */
	@Override
	protected boolean isValidPattern(String value)
	{
		return radix.getPattern().matcher(value).matches();
	}
}
