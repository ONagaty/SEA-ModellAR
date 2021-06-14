/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidl6458 Mar 11, 2010 5:30:50 PM </copyright>
 */
package eu.cessar.ct.core.platform.ui.widgets;

import java.util.List;

/**
 * @author uidl6458
 * @param <T>
 *
 */
public interface IMultiValueDatatypeEditor<T> extends IDatatypeEditor<List<T>>
{

	/**
	 * @return - a DataTypeEditor
	 */
	public IDatatypeEditor<T> createSingleDatatypeEditor();

	/**
	 * @param newValues
	 * @return - true if the values should be accepted
	 */
	public boolean acceptNewValues(List<T> newValues);

	/**
	 * @param maxAllowedValues
	 */
	public void setMaxValues(int maxAllowedValues);

	/**
	 * @return - the maxValues
	 */
	public int getMaxValues();

}
