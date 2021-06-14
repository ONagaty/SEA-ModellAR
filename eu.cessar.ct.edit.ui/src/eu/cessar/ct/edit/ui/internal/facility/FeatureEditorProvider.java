/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidl6458 Mar 5, 2010 7:17:05 PM </copyright>
 */
package eu.cessar.ct.edit.ui.internal.facility;

import java.util.Collections;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

import eu.cessar.ct.edit.ui.facility.AbstractEClassFragmentEditorProvider;
import eu.cessar.ct.edit.ui.facility.IModelFragmentEditor;
import eu.cessar.ct.edit.ui.facility.IModelFragmentEditorProvider;
import eu.cessar.ct.edit.ui.facility.IModelFragmentFeatureEditor;

/**
 * @author uidl6458
 *
 */
public class FeatureEditorProvider extends AbstractEClassFragmentEditorProvider
{
	private SingularDefinitionElement element;
	private EStructuralFeature feature;
	private EObject inputObject;

	/**
	 * @param element
	 * @param inputObject
	 * @param clz
	 * @param feature
	 */
	public FeatureEditorProvider(SingularDefinitionElement element, EClass clz, EStructuralFeature feature,
		EObject inputObject)
	{
		super(clz);
		this.element = element;
		this.inputObject = inputObject;
		this.feature = feature;
	}

	/**
	 * @param input
	 */
	public void setInputObject(EObject input)
	{
		inputObject = input;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.edit.ui.IModelFragmentEditorProvider#createEditor()
	 */
	public IModelFragmentEditor createEditor()
	{
		IModelFragmentFeatureEditor newEditor = element.createNewEditor();
		if (newEditor != null)
		{
			newEditor.setInputClass(getInputClass());
			newEditor.setInputFeature(feature);
			newEditor.setEditorProvider(this);
			newEditor.setInput(inputObject);
		}
		return newEditor;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.edit.ui.IModelFragmentEditorProvider#getCategories()
	 */
	public String[] getCategories()
	{
		return new String[] {element.getCategory()};
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.edit.ui.IModelFragmentEditorProvider#getEditedFeatures()
	 */
	public List<EStructuralFeature> getEditedFeatures()
	{
		return Collections.singletonList(feature);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.edit.ui.facility.IModelFragmentEditorProvider#isMetaEditor()
	 */
	public boolean isMetaEditor()
	{
		// not a meta editor
		return false;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.edit.ui.IModelFragmentEditorProvider#getPriority()
	 */
	public int getPriority()
	{
		// TODO Auto-generated method stub
		return element.getPriority();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.edit.ui.facility.IModelFragmentEditorProvider#getId()
	 */
	public String getId()
	{
		return element.getId();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.edit.ui.facility.AbstractEClassFragmentEditorProvider#createCopy(org.eclipse.emf.ecore.EClass)
	 */
	@Override
	protected IModelFragmentEditorProvider createCopy(EClass other)
	{
		return new FeatureEditorProvider(element, other, feature, inputObject);
	}

}
