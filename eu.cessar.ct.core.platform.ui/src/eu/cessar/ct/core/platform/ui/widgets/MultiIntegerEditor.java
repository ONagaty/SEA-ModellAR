package eu.cessar.ct.core.platform.ui.widgets;

import eu.cessar.ct.core.platform.util.ERadix;

/**
 * Editor for multiple integer values.
 * 
 */
public class MultiIntegerEditor extends AbstractMultiEditor<Integer>
{
	private ERadix radix = ERadix.DECIMAL;
	private boolean isSetRadixOnUI;
	private SingleIntegerEditor singleIntegerEditor;

	/**
	 * @param handler
	 */
	public MultiIntegerEditor(MultiDatatypeValueHandler<Integer> handler)
	{
		super(handler);
	}

	/**
	 * 
	 * @param handler
	 * @param radix
	 */
	public MultiIntegerEditor(MultiDatatypeValueHandler<Integer> handler, ERadix radix)
	{
		this(handler);
		this.radix = radix;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.core.platform.ui.widgets.IMultiValueDatatypeEditor#createSingleDatatypeEditor()
	 */
	public IDatatypeEditor<Integer> createSingleDatatypeEditor()
	{
		singleIntegerEditor = new SingleIntegerEditor(isAcceptingNull(), radix);
		return singleIntegerEditor;
	}

	/**
	 * 
	 * @param radix
	 */
	public void setRadix(ERadix radix)
	{
		this.radix = radix;
		if (haveUI())
		{
			isSetRadixOnUI = true;
			updateUI();
		}
		if (singleIntegerEditor != null)
		{
			singleIntegerEditor.setRadix(radix);
		}
	}

	/**
	 * 
	 * @return
	 */
	public ERadix getRadix()
	{
		return radix;
	}

	/**
	 * @return
	 */
	public boolean isSetRadixOnUI()
	{
		return isSetRadixOnUI;
	}

}
