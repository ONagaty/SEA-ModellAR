/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidl6458 Jul 10, 2009 10:16:35 AM </copyright>
 */
package eu.cessar.ct.core.platform.ui.dialogs;

import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import eu.cessar.ct.sdk.IPostBuildContext;

/**
 * Handler used by sections to manipulate values edited inside MultiValueDialog
 *
 * @author uidl6458
 * @param <T>
 *
 * @Review uidl6458 - 19.04.2012
 */
public interface IMultiValueHandler<T>
{

	/**
	 * Return the maximum number of allowed values or -1 if there is no limit
	 *
	 * @return - the maximum number of allowed values
	 */
	public int getMaxValues();

	/**
	 * Return a descriptive name, usually one word, that will be used into the caption of the dialog. The title of the
	 * dialog will be something like:<br/>
	 *
	 * <pre>
	 * &quot;Select values for &quot; + handler.getTypeName()
	 * </pre>
	 *
	 * @param plural
	 *        if true return a plural version of the type, if false return a singular
	 * @return - a descriptive name
	 */
	public String getTypeName(boolean plural);

	/**
	 * Return the current values to be displayed, never null. The list should not be l
	 *
	 * @return - list of values
	 */
	public List<T> getValues();

	/**
	 * Inform the wrapper that the dialog is closed successfully and the new values have to be saved
	 *
	 * @param newValues
	 *
	 * @param values
	 * @return - true if the values are accepted
	 */
	public boolean acceptValues(List<T> newValues);

	/**
	 * Return a Label provider that "knows" how to display the values received by {@link #getValues()}
	 *
	 * @param value
	 * @return - label provider
	 */
	public ILabelProvider getLabelProvider();

	/**
	 * Create the editor
	 *
	 * @param parent
	 * @return - the editor
	 */
	public Control createSingleValueEditor(Composite parent);

	/**
	 * Get the editor value, it could be null
	 *
	 * @return - the value inside the editor
	 */
	public List<T> getEditorValue();

	/**
	 * Set the editor value. The editor should not expect that the value is valid, it could also be null
	 *
	 * @param value
	 */
	public void setEditorValue(T value);

	/**
	 * Clear the value presented into the editor
	 */
	public void clearEditorValue();

	/**
	 * Create the editor
	 *
	 * @param parent
	 * @param isPBFiltering
	 * @param pBContext
	 * @return - the editor
	 */
	public Control createSingleValueEditor(Composite parent, boolean isPBFiltering, IPostBuildContext pBContext);

	/**
	 * Compute list of candidates
	 *
	 * @param isPBFiltering
	 * @param pBContext
	 *
	 * @param parent
	 * @return the list of candidates
	 */
	public Control computeCandidateList(boolean isPBFiltering, IPostBuildContext pBContext);

	/**
	 * Set container object
	 *
	 * @param object
	 */
	public void setInputObject(EObject object);

	/**
	 * Get container object
	 *
	 * @return - the input of the editor
	 */
	public EObject getInputObject();

}
