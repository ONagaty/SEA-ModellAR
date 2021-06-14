/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidl6870 Mar 29, 2010 2:14:33 PM </copyright>
 */
package eu.cessar.ct.edit.ui.dialogs;

import java.util.List;

import org.eclipse.jface.viewers.ILabelProvider;

import eu.cessar.ct.core.platform.ui.dialogs.IMultiValueHandler;

/**
 * @author uidl6870
 * @param <T>
 *
 */
public interface IReferenceMultiValueHandler<T> extends IMultiValueHandler<T>
{
	/**
	 * Return the list of possible candidates
	 *
	 * @return - list of candidates
	 */
	public List<Object> getCandidates();

	/**
	 * Return the label provider for the tree viewer inside ReferenceSelectionDialog
	 *
	 * @return - label provider
	 */
	public ILabelProvider getTreeLabelProvider();
}
