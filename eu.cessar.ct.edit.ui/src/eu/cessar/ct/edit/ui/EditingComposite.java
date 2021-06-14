/**
 * <copyright>
 *
 * Copyright (c) Continental AG and others.<br/>
 * http://www.continental-corporation.com All rights reserved.
 *
 * File created by uidg4020<br/>
 * Apr 1, 2014 2:19:47 PM
 *
 * </copyright>
 */
package eu.cessar.ct.edit.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;

import eu.cessar.ct.core.platform.ui.util.CessarFormToolkit;
import eu.cessar.ct.edit.ui.facility.IModelFragmentEditor;
import eu.cessar.ct.edit.ui.facility.IModelFragmentEditorProvider;
import eu.cessar.ct.edit.ui.facility.IModelFragmentFeatureEditor;
import eu.cessar.ct.edit.ui.facility.parts.EEditorPart;
import eu.cessar.ct.edit.ui.facility.parts.ICaptionPart;
import eu.cessar.ct.edit.ui.facility.parts.IModelFragmentEditorPart;
import eu.cessar.ct.edit.ui.utils.EditUtils;

/**
 *
 * This class creates a composite and adds editor contents on it for the features received.
 *
 * @author uidg4020
 *
 */
public class EditingComposite implements IEditingComposite
{
	private EObject inputObject;
	private Composite editorComposite;
	private CessarFormToolkit toolkit;
	private List<Control> controls = new ArrayList<>();
	private GridData gd;
	private List<IModelFragmentEditor> editorsList = new ArrayList<>();
	private Map<EStructuralFeature, String> labelsToFeaturesMap = new TreeMap<>();

	/**
	 * Creates an composite with a single editors column, which grabs excess horizontal space
	 *
	 * @param parent
	 *        parent composite
	 * @param toolkit
	 *        toolkit to be used
	 */
	public EditingComposite(Composite parent, CessarFormToolkit toolkit)
	{
		this(parent, toolkit, 1);
	}

	/**
	 * Creates an composite with a single editors column, which allows to choose if excess horizontal space is grabbed
	 *
	 * @param parent
	 *        parent composite
	 * @param toolkit
	 *        toolkit to be used
	 * @param numColumns
	 *        number of columns of the layout
	 * @param grabExcessHorizontalSpace
	 *        true if the created composite should grab excess horizontal space
	 */
	public EditingComposite(Composite parent, CessarFormToolkit toolkit, boolean grabExcessHorizontalSpace)
	{

		this(parent, toolkit, grabExcessHorizontalSpace, 1);
	}

	/**
	 * Creates an composite having a given editors column number, which grabs excess horizontal space
	 *
	 * @param parent
	 *        parent composite
	 * @param toolkit
	 *        toolkit to be used
	 * @param numColumns
	 *        editor columns number
	 */
	public EditingComposite(Composite parent, CessarFormToolkit toolkit, int numColumns)
	{
		this(parent, toolkit, true, numColumns);
	}

	/**
	 * Creates an composite having a given editors column number, which allows to choose if excess horizontal space is
	 * grabbed
	 *
	 * @param parent
	 * @param toolkit
	 * @param grabExcessHorizontalSpace
	 * @param numColumns
	 */
	public EditingComposite(Composite parent, CessarFormToolkit toolkit, boolean grabExcessHorizontalSpace,
		int numColumns)
	{
		GridLayout layout = new GridLayout(4 * numColumns, false);
		this.toolkit = toolkit;
		editorComposite = toolkit.createComposite(parent, SWT.None);
		layout.verticalSpacing = 1;
		editorComposite.setLayout(layout);
		gd = new GridData(SWT.FILL, SWT.None, grabExcessHorizontalSpace, false);
		editorComposite.setLayoutData(gd);
		editorComposite.setBackgroundMode(SWT.INHERIT_DEFAULT);

	}

	/**
	 * @param toolkit
	 * @param sharedEditorComposite
	 *        Returns the composite used for the Editing composite
	 */
	public EditingComposite(CessarFormToolkit toolkit, Composite sharedEditorComposite)
	{
		this.toolkit = toolkit;
		editorComposite = sharedEditorComposite;
	}

	/**
	 * Remove last editing element from composite
	 */
	public void removeLastElement()
	{
		// labelsToFeaturesMap.remove(addedFeature);
		IModelFragmentEditor editor = editorsList.get(editorsList.size() - 1);

		Set<EStructuralFeature> featuresSet = labelsToFeaturesMap.keySet();
		Iterator<EStructuralFeature> features = featuresSet.iterator();

		while (features.hasNext())
		{
			EStructuralFeature feature = features.next();
			String name = feature.getName();
			String instanceId = editor.getInstanceId();
			if (instanceId.toLowerCase().contains(name.toLowerCase()))
			{
				features.remove();
				break;
			}
		}
		editor.getPart(EEditorPart.CAPTION).getEditor().getInstanceId();
		editor.dispose();

		// editorComposite.getChildren()[20].dispose();

		Control[] children = editorComposite.getChildren();
		children[children.length - 1].dispose();
		editorsList.remove(editor);
		editorComposite.pack();

		// refresh();
	}

	/**
	 * @param newFeature
	 * @param name
	 * @param showCaption
	 *        Add an editing element at the end of editing composite
	 */
	public void addOneEntryAtTheEnd(EStructuralFeature newFeature, String name, boolean showCaption)
	{
		labelsToFeaturesMap.put(newFeature, name);
		newFeature.setLowerBound(1);
		List<EStructuralFeature> oneFeature = Arrays.asList(newFeature);
		List<IModelFragmentEditorProvider> editorProviders = IEditingFacility.eINSTANCE.getSimpleEditorProviders(
			newFeature.getEContainingClass(), oneFeature, IEditingFacility.EDITOR_ALL);

		for (IModelFragmentEditorProvider editorProvider: editorProviders)
		{

			IModelFragmentEditor editor = editorProvider.createEditor();
			editorsList.add(editor);
			editor.setFormToolkit(toolkit);
			if (showCaption)
			{

				createEditorContents(editor, editorComposite, name);
			}
			else
			{
				// createLabel(editorComposite, editor);

				EStructuralFeature feature = ((IModelFragmentFeatureEditor) editor).getInputFeature();
				String label = labelsToFeaturesMap.get(feature);

				createEditorContents(editor, editorComposite, label);

			}

		}

		editorComposite.pack();
		IModelFragmentEditor editor = editorsList.get(editorsList.size() - 1);
		editor.setInput(inputObject);
		editor.getPart(EEditorPart.CAPTION).refresh();
		editor.setEnabled(true);
		editor.refresh();

	}

	public void createContents(Map<EStructuralFeature, String> labelsToFeatureMap, List<EStructuralFeature> features,
		EClass eClass)
	{
		createContents(labelsToFeatureMap, features, eClass, false);
	}

	public void createContents(Map<EStructuralFeature, String> labelsToFeatureMap, List<EStructuralFeature> features,
		EClass eClass, Boolean showCaption)
	{
		List<EStructuralFeature> oneFeature = new ArrayList<>();
		List<IModelFragmentEditorProvider> editorProviders = new ArrayList<>();
		labelsToFeaturesMap = labelsToFeatureMap;

		for (EStructuralFeature feature: features)
		{
			oneFeature = new ArrayList<>();
			oneFeature.add(feature);
			List<IModelFragmentEditorProvider> simpleEditorProviders = IEditingFacility.eINSTANCE.getSimpleEditorProviders(
				eClass, oneFeature, IEditingFacility.EDITOR_ALL);
			editorProviders.addAll(simpleEditorProviders);

		}

		for (IModelFragmentEditorProvider editorProvider: editorProviders)
		{

			IModelFragmentEditor editor = editorProvider.createEditor();
			editorsList.add(editor);
			editor.setFormToolkit(toolkit);
			if (showCaption)
			{

				createEditorContents(editor, editorComposite, null);
			}
			else
			{
				// createLabel(editorComposite, editor);

				EStructuralFeature feature = ((IModelFragmentFeatureEditor) editor).getInputFeature();
				String label = labelsToFeaturesMap.get(feature);

				createEditorContents(editor, editorComposite, label);

			}

		}
		editorComposite.pack();

	}

	public void createEditorContents(IModelFragmentEditor editor, Composite composite, String caption)
	{
		IModelFragmentEditorPart captionPart = editor.getPart(EEditorPart.CAPTION);
		Control control = captionPart.createContents(composite);
		gd = new GridData(SWT.BEGINNING, SWT.TOP, false, false);
		control.setLayoutData(gd);
		controls.add(control);

		if (caption != null)
		{
			((ICaptionPart) captionPart).setCaption(caption);
		}
		// rationale: increasing the number of columns inside the main composite will break the layout of hierarchical
		// editors
		Composite resourcesAndValidationComposite = toolkit.createComposite(composite);
		resourcesAndValidationComposite.setLayout(new GridLayout(2, false));

		control = editor.getPart(EEditorPart.RESOURCES).createContents(resourcesAndValidationComposite);
		gd = new GridData(SWT.BEGINNING, SWT.TOP, false, false);
		control.setLayoutData(gd);
		controls.add(control);

		// control = editor.getPart(EEditorPart.VALIDATION).createContents(resourcesAndValidationComposite);
		// gd = new GridData(SWT.BEGINNING, SWT.TOP, false, false);
		// control.setLayoutData(gd);

		control = editor.getPart(EEditorPart.ACTION).createContents(composite);
		gd = new GridData(SWT.BEGINNING, SWT.TOP, false, false);
		control.setLayoutData(gd);
		controls.add(control);

		control = editor.getPart(EEditorPart.EDITING_AREA).createContents(composite);
		Object data = control.getLayoutData();
		if (!(data instanceof GridData))
		{
			gd = new GridData(SWT.FILL, SWT.TOP, true, false);
			control.setLayoutData(gd);
		}
		controls.add(control);
	}

	public void setInput(EObject newInputObject)
	{

		if (newInputObject != null)
		{
			// if the input is the same do nothing
			if (inputObject == newInputObject)
			{
				return;
			}

			inputObject = newInputObject;
			for (IModelFragmentEditor editor: editorsList)
			{
				editor.setInput(inputObject);
				editor.getPart(EEditorPart.CAPTION).refresh();
				editor.setEnabled(true);
				editor.refresh();
				// EditUtils.setCaptionFontAndColor(text, isSet, isMnd);
			}
		}
		else
		{
			for (IModelFragmentEditor editor: editorsList)
			{
				inputObject = null;
				editor.setInput(inputObject);
				editor.setEnabled(false);
				editor.refresh();
				editor.getPart(EEditorPart.CAPTION).refresh();
				editor.getPart(EEditorPart.EDITING_AREA).setEnabled(false);

			}
		}
	}

	public void createLabel(Composite composite, IModelFragmentEditor editor)
	{
		if (editor instanceof IModelFragmentFeatureEditor)
		{

			EStructuralFeature feature = ((IModelFragmentFeatureEditor) editor).getInputFeature();
			String label = labelsToFeaturesMap.get(feature);
			if (label == null)
			{
				label = ""; //$NON-NLS-1$
			}

			// toolkit.createCLabel(composite, label);
			Text textLable = toolkit.createText(composite, label);
			EditUtils.setCaptionFontAndColor(textLable, true, false);
			textLable.setEditable(false);
		}
	}

	public Composite getEditorComposite()
	{
		return editorComposite;
	}

	/**
	 * Refresh the editors
	 */
	public void refresh()
	{
		for (IModelFragmentEditor imfe: editorsList)
		{
			imfe.refresh();
		}
	}

	/**
	 * @return Get all the IModelFragmentEditors in this composite
	 */
	public List<IModelFragmentEditor> getEditors()
	{
		return editorsList;
	}

	/**
	 *
	 * @param eFeature
	 * @return returns the IModelFragmentEditor for the eFeature in this composite In case it doesn't exit, it returns
	 *         null
	 */
	public IModelFragmentEditor getEditor(EStructuralFeature eFeature)
	{
		for (IModelFragmentEditor iModelFragmentEditor: editorsList)
		{
			if (iModelFragmentEditor.getEditorProvider().getEditedFeatures().contains(eFeature))
			{
				return iModelFragmentEditor;
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.edit.ui.IEditingComposite#setVisible(boolean)
	 */
	@Override
	public void setVisible(boolean visible)
	{
		for (Control c: controls)
		{
			c.setVisible(visible);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.edit.ui.IEditingComposite#setEnabled(boolean)
	 */

	public void setEnabled(boolean enable)
	{
		for (Control c: controls)
		{
			c.setEnabled(enable);
		}
	}

	/**
	 *
	 */
	public void dispose()
	{
		editorComposite.dispose();
	}

	/**
	 * @return the inputObject
	 */
	public EObject getInputObject()
	{
		return inputObject;
	}
}
