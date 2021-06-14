/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidt2045 Sep 2, 2011 9:45:57 AM </copyright>
 */
package eu.cessar.ct.edit.ui.facility.composition;

import eu.cessar.ct.edit.ui.IEditingFacility;
import eu.cessar.ct.edit.ui.facility.IModelFragmentEditorProvider;
import eu.cessar.ct.edit.ui.internal.facility.composition.IEditorCompositionProvider;

/**
 * @author uidt2045
 * 
 *         Interface that encapsulates the name and the input of a composition
 *         of editors
 */
public interface ICompositionCategory<T>
{
	/**
	 * Return the name of the composition
	 * 
	 * @return
	 */
	public String getName();

	/**
	 * Return the complete name of the composition <br>
	 * NOTE: an usage scenario is when the {@link IEditingFacility} returns 2 or
	 * more compositions having the same input {@link #getInput()}. This can
	 * occur only for compositions of {@link ECompositionType#SYSTEM} type
	 * 
	 * @return
	 */
	public String getFullName();

	/**
	 * Return the input of the composition, based on which the
	 * {@link IModelFragmentEditorProvider} where computed, by the
	 * {@link IEditorCompositionProvider}
	 * 
	 * @return
	 */
	public T getInput();

}
