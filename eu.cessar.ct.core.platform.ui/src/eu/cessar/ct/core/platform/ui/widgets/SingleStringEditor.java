package eu.cessar.ct.core.platform.ui.widgets;

import eu.cessar.ct.core.platform.util.CompareDataUtils;

/**
 * Editor for a single string value.
 * 
 */
public class SingleStringEditor extends AbstractSingleTextualEditor<String>
{
	/**
	 * Creates an instance of the editor.
	 * 
	 * @param acceptNull
	 *        if <code>true</code>, a <code>null</code> value will be accepted in <code>setInputData(data)</code>.<br>
	 *        if <code>false</code>, the value will be set to the default value empty string
	 */
	public SingleStringEditor(boolean acceptNull)
	{
		super(acceptNull);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.core.platform.ui.widgets.AbstractSingleTextualEditor#convertFromString(java.lang.String)
	 */
	@Override
	protected String convertFromString(String value)
	{
		if (value == null || "".equals(value)) //$NON-NLS-1$
		{
			return null;
		}
		else
		{
			return value;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.core.platform.ui.widgets.AbstractSingleTextualEditor#convertToString(java.lang.Object)
	 */
	@Override
	protected String convertToString(String value)
	{
		if (value != null)
		{
			return value;
		}
		else
		{
			return ""; //$NON-NLS-1$
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
	 * @see eu.cessar.ct.core.platform.ui.widgets.AbstractSingleEditor#getDefaultData()
	 */
	@Override
	protected String getDefaultData()
	{
		return ""; //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.core.platform.ui.widgets.AbstractBaseSingleTextualEditor#isNewData(java.lang.Object,
	 * java.lang.Object)
	 */
	@Override
	protected boolean isSameData(String oldData, String newData)
	{
		return CompareDataUtils.equal(oldData, newData);
	}

}
