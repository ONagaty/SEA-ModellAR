/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870 21.05.2012 11:25:46 </copyright>
 */
package eu.cessar.ct.core.platform.ui.widgets;

import java.util.List;
import java.util.regex.Pattern;

import eu.cessar.ct.core.platform.util.CompareDataUtils;
import eu.cessar.ct.core.platform.util.DoubleUtils;
import eu.cessar.ct.core.platform.util.ERadix;

/**
 * Editor for a single double value, where the value can also be chosen from a list of predefined special values. The
 * last ones shall be passed via {@link #setPredefinedValues(List)} method
 * 
 * @author uidl6870
 * 
 */
public class SingleDoubleWithChoiceEditor extends AbstractSingleNumericalWithChoiceEditor<Double>
{
	private static final String VALID_EXPR = "(-?[0-9]*(\\.[0-9]*)?([Ee]([+\\-]?)[0-9]*)?)|INF|-INF|NaN|Inf|-Inf"; //$NON-NLS-1$

	private static final Pattern DOUBLE_PATTERN = Pattern.compile(VALID_EXPR);

	/**
	 * @param acceptNull
	 */
	public SingleDoubleWithChoiceEditor(boolean acceptNull)
	{
		super(acceptNull);
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

		return DOUBLE_PATTERN.matcher(value).matches();
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
			Double dataFromUI = getDataFromUI();
			if (dataFromUI != null)
			{
				Double.parseDouble(dataFromUI.toString());
			}
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
	protected Double convertFromString(String value)
	{
		if (value == null || "".equals(value)) //$NON-NLS-1$
		{
			return null;
		}
		else
		{
			return DoubleUtils.convertFromString(value);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.core.platform.ui.widgets.AbstractSingleTextualEditor#convertToString(java.lang.Object)
	 */
	@Override
	protected String convertToString(Double value)
	{
		if (value == null)
		{
			return ""; //$NON-NLS-1$
		}
		else
		{
			return DoubleUtils.convertToString(value);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.core.platform.ui.widgets.AbstractSingleEditor#getDefaultData()
	 */
	@Override
	protected Double getDefaultData()
	{
		return new Double(0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.core.platform.ui.widgets.AbstractSingleEditor#isNull(java.lang.Object)
	 */
	@Override
	protected boolean isNull(Double data)
	{
		return data == null;
	}

	/**
	 * @param radix
	 */
	@Override
	public void setRadix(ERadix radix)
	{
		if (haveUI())
		{
			Double dataFromUI = getDataFromUI();
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
	 * @see eu.cessar.ct.core.platform.ui.widgets.AbstractBaseSingleTextualEditor#isNewData(java.lang.Object,
	 * java.lang.Object)
	 */
	@Override
	protected boolean isSameData(Double oldData, Double newData)
	{
		return CompareDataUtils.equal(oldData, newData);
	}

}
