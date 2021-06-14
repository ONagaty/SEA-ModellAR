package eu.cessar.ct.core.platform.ui.widgets;

import java.util.ArrayList;
import java.util.List;

/**
 * Editor for multiple values from a list of allowed values.
 *
 */
public class MultiListEditor extends AbstractMultiEditor<String>
{

	private List<String> values;
	private boolean isCombo = true;

	/**
	 * @param handler
	 */
	public MultiListEditor(MultiDatatypeValueHandler<String> handler)
	{
		super(handler);
	}

	/**
	 * @param handler
	 * @param isCombo
	 */
	public MultiListEditor(MultiDatatypeValueHandler<String> handler, boolean isCombo)
	{
		super(handler);
		this.isCombo = isCombo;
	}

	/**
	 * @param values
	 */
	public void setAllowedValues(List<String> values)
	{
		if (values == null)
		{
			this.values = new ArrayList<String>();
		}
		else
		{
			this.values = new ArrayList<String>(values);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.core.platform.ui.widgets.IMultiValueDatatypeEditor#createSingleDatatypeEditor()
	 */
	public IDatatypeEditor<String> createSingleDatatypeEditor()
	{
		AbstractSingleEditor<String> editor = null;

		if (isCombo)
		{
			editor = new SingleListEditor(isAcceptingNull());
			((SingleListEditor) editor).setAllowedValues(values);
		}
		else
		{
			editor = new ListEditor(isAcceptingNull());
			((ListEditor) editor).setAllowedValues(values);
		}
		return editor;
	}
}
