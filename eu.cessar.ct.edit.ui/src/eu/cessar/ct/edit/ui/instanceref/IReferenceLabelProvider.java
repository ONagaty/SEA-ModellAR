/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870 Jun 10, 2010 2:22:33 PM </copyright>
 */
package eu.cessar.ct.edit.ui.instanceref;

import java.util.List;

import org.eclipse.jface.viewers.ILabelProvider;

/**
 * @author uidl6870
 * @param <V>
 * 
 */
public interface IReferenceLabelProvider<V> extends ILabelProvider
{
	public static final String OF_TYPE_CONSTANT = " Of Type: "; //$NON-NLS-1$

	/**
	 * 
	 * @param elem
	 * @return
	 */
	String getTooltip(List<V> elem);
}
