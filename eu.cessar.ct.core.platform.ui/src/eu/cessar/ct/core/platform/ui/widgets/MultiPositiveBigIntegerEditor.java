package eu.cessar.ct.core.platform.ui.widgets;

import java.math.BigInteger;

import eu.cessar.ct.core.platform.util.ERadix;

/**
 * Editor for BigInteger multi values.
 *
 */
public class MultiPositiveBigIntegerEditor extends AbstractMultiEditor<BigInteger>
{
	private ERadix radix = ERadix.DECIMAL;
	private boolean isSetRadixOnUI;
	private SinglePositiveBigIntegerEditor singlePositiveBigIntegerEditor;

	/**
	 * @param handler
	 */
	public MultiPositiveBigIntegerEditor(MultiDatatypeValueHandler<BigInteger> handler)
	{
		super(handler);
	}

	/**
	 *
	 * @param handler
	 * @param radix
	 */
	public MultiPositiveBigIntegerEditor(MultiDatatypeValueHandler<BigInteger> handler, ERadix radix)
	{
		this(handler);
		this.radix = radix;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.core.platform.ui.widgets.IMultiValueDatatypeEditor#createSingleDatatypeEditor()
	 */
	public IDatatypeEditor<BigInteger> createSingleDatatypeEditor()
	{
		singlePositiveBigIntegerEditor = new SinglePositiveBigIntegerEditor(true, radix);
		return singlePositiveBigIntegerEditor;
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
		if (singlePositiveBigIntegerEditor != null)
		{
			singlePositiveBigIntegerEditor.setRadix(radix);
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
