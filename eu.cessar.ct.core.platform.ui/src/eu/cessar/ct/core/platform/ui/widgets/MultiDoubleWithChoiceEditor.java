/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870 25.05.2012 14:01:28 </copyright>
 */
package eu.cessar.ct.core.platform.ui.widgets;

import java.util.ArrayList;
import java.util.List;

import eu.cessar.ct.core.platform.util.DoubleUtils;

/**
 * Editor for multiple double values, that allows also choosing from a list of
 * predefined values.
 * 
 * @author uidl6870
 * 
 */
public class MultiDoubleWithChoiceEditor extends AbstractMultiEditor<Double>
{

	/**
	 * @param handler
	 */
	public MultiDoubleWithChoiceEditor(MultiDatatypeValueHandler<Double> handler)
	{
		super(handler);
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.core.platform.ui.widgets.IMultiValueDatatypeEditor#createSingleDatatypeEditor()
	 */
	public IDatatypeEditor<Double> createSingleDatatypeEditor()
	{
		SingleDoubleWithChoiceEditor editor = new SingleDoubleWithChoiceEditor(true);
		editor.setPredefinedValues(getPredefinedValues());

		return editor;
	}

	private List<String> getPredefinedValues()
	{
		List<String> list = new ArrayList<String>();

		list.add(DoubleUtils.NEGATIVE_INFINITY_AS_STRING);
		list.add(DoubleUtils.POSITIVE_INFINITY_AS_STRING);

		return list;
	}

}
