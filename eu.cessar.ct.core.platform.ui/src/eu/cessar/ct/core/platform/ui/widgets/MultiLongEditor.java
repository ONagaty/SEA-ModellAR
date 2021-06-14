/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidt6343 Dec 8, 2010 11:32:12 AM </copyright>
 */
package eu.cessar.ct.core.platform.ui.widgets;

import eu.cessar.ct.core.platform.util.ERadix;

/**
 * @author uidt6343
 * 
 */
public class MultiLongEditor extends AbstractMultiEditor<Long>
{

	private ERadix radix = ERadix.DECIMAL;
	private boolean isSetRadixOnUI;
	private AbstractSingleNumericalEditor<Long> singleLongEditor;

	/**
	 * @param handler
	 */
	public MultiLongEditor(MultiDatatypeValueHandler<Long> handler)
	{
		super(handler);
	}

	/**
	 * 
	 * @param handler
	 * @param radix
	 */
	public MultiLongEditor(MultiDatatypeValueHandler<Long> handler, ERadix radix)
	{
		this(handler);
		this.radix = radix;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.core.platform.ui.widgets.IMultiValueDatatypeEditor#createSingleDatatypeEditor()
	 */
	public IDatatypeEditor<Long> createSingleDatatypeEditor()
	{
		singleLongEditor = new SingleLongEditor(isAcceptingNull(), radix);
		return singleLongEditor;
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
		if (singleLongEditor != null)
		{
			singleLongEditor.setRadix(radix);
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
