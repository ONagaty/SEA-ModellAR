/**
 * <copyright>
 *
 * Copyright (c) Continental AG and others.<br/>
 * http://www.continental-corporation.com All rights reserved.
 *
 * File created by uidg3464<br/>
 * Jun 4, 2014 11:33:49 AM
 *
 * </copyright>
 */
package eu.cessar.ct.edit.ui;

import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.swt.widgets.Composite;

import eu.cessar.ct.edit.ui.facility.IModelFragmentEditor;

/**
 *
 * @author uidg3464
 *
 *         %created_by: uidg3464 %
 *
 *         %date_created: Thu Jun 18 14:49:46 2015 %
 *
 *         %version: 1 %
 */
public interface IEditingComposite
{
	/**
	 * Obtains editor providers for the features and creates the necessary widgets giving the possibility to show or not
	 * the Editors Caption Part
	 *
	 * @param labelsToFeatureMap
	 *        map that contains feature - label pairs
	 * @param features
	 *        features to create editors for. The editors will be added in the order they are in this list
	 * @param eClass
	 *        used when creating the editors
	 * @param showCaption
	 *        Select if the EditorCaption is visible or not
	 * @param flag
	 *        used in order to show the Editor caption or not
	 *
	 */
	public void createContents(Map<EStructuralFeature, String> labelsToFeatureMap, List<EStructuralFeature> features,
		EClass eClass, Boolean showCaption);

	/**
	 * Obtains editor providers for the features and creates the necessary widgets
	 *
	 * @param labelsToFeatureMap
	 *        map that contains feature - label pairs
	 * @param features
	 *        features to create editors for. The editors will be added in the order they are in this list
	 * @param eClass
	 *        used when creating the editors
	 */
	public void createContents(Map<EStructuralFeature, String> labelsToFeatureMap, List<EStructuralFeature> features,
		EClass eClass);

	/**
	 * Create contents for this editor on the provided composite
	 *
	 * @param editor
	 *        The current editor
	 * @param composite
	 *        the Composite on which the Editor is
	 * @param caption
	 *        a custom text for the ICaption part, if null uses default value
	 */
	public void createEditorContents(IModelFragmentEditor editor, Composite composite, String caption);

	/**
	 * Method that initializes the input object. If the selected object is same as previous do nothing, else set the
	 * input for the editors
	 *
	 * @param newInputObject
	 *        -the object to display contents for
	 */
	public void setInput(EObject newInputObject);

	/**
	 * Create a label for an editor and places it a composite
	 *
	 * @param composite
	 *        parent composite to put labels on
	 * @param editor
	 *        the editor to create label for
	 */
	public void createLabel(Composite composite, IModelFragmentEditor editor);

	/**
	 * @return Returns the EditingComposite
	 */
	public Composite getEditorComposite();

	/**
	 * Marks the receiver as visible if the argument is true, and marks it invisible otherwise.
	 *
	 * @param visible
	 */
	public void setVisible(boolean visible);
}
