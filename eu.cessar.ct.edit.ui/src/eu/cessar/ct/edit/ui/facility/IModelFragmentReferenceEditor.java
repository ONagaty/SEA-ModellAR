/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870 Mar 25, 2010 1:33:24 PM </copyright>
 */
package eu.cessar.ct.edit.ui.facility;

import java.util.List;

import org.eclipse.jface.viewers.ILabelProvider;

/**
 * @author uidl6870
 * 
 */
public interface IModelFragmentReferenceEditor
{
	/**
	 * Return a list of candidates or an empty list if there are no candidates
	 * 
	 * @return
	 */
	public List<Object> getCandidates();

	/**
	 * Return the label provider for the text control that displays multiple
	 * values
	 * 
	 * @return
	 */
	public ILabelProvider getLabelProvider();

}
