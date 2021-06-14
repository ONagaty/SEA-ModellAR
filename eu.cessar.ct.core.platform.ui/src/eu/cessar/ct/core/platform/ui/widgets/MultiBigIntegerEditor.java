package eu.cessar.ct.core.platform.ui.widgets;

import java.math.BigInteger;

import eu.cessar.ct.core.platform.util.ERadix;

/**
 * Editor for BigInteger multi values.
 * 
 */
public class MultiBigIntegerEditor extends AbstractMultiEditor<BigInteger>
{
	private ERadix radix = ERadix.DECIMAL;
	private boolean isSetRadixOnUI;
	private SingleBigIntegerEditor singleBigIntegerEditor;

	/**
	 * @param handler
	 */
	public MultiBigIntegerEditor(MultiDatatypeValueHandler<BigInteger> handler)
	{
		super(handler);
	}

	/**
	 * 
	 * @param handler
	 * @param radix
	 */
	public MultiBigIntegerEditor(MultiDatatypeValueHandler<BigInteger> handler, ERadix radix)
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
		singleBigIntegerEditor = new SingleBigIntegerEditor(true, radix);
		return singleBigIntegerEditor;
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
		if (singleBigIntegerEditor != null)
		{
			singleBigIntegerEditor.setRadix(radix);
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
