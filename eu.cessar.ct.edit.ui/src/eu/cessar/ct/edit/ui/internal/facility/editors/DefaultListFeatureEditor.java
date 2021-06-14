/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidl6870 Mar 15, 2010 2:23:04 PM </copyright>
 */
package eu.cessar.ct.edit.ui.internal.facility.editors;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EEnumLiteral;
import org.eclipse.emf.ecore.EStructuralFeature;

import eu.cessar.ct.edit.ui.facility.AbstractModelFragmentFeatureEditor;
import eu.cessar.ct.edit.ui.facility.IModelFragmentFeatureEditorValues;
import eu.cessar.ct.edit.ui.facility.parts.IEditorPart;
import eu.cessar.ct.edit.ui.facility.parts.editor.MultiListEditorPart;
import eu.cessar.ct.edit.ui.facility.parts.editor.SingleListEditorPart;

/**
 * @author uidl6870
 *
 */
public class DefaultListFeatureEditor extends AbstractModelFragmentFeatureEditor implements
	IModelFragmentFeatureEditorValues
{
	private List<EEnumLiteral> enumLiterals;
	private List<String> enumValues;

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.facility.AbstractModelEditor#createEditorPart()
	 */
	@Override
	protected IEditorPart createEditorPart()
	{
		storeEnumLiterals();

		if (isMultiValueEditor())
		{
			return new MultiListEditorPart(this);
		}
		else
		{
			return new SingleListEditorPart(this);
		}
	}

	@Override
	public List<String> getValues()
	{
		return Collections.unmodifiableList(enumValues);
	}

	@Override
	public List<EEnumLiteral> getEnumLiterals()
	{
		return Collections.unmodifiableList(enumLiterals);
	}

	/**
	 *
	 */
	private void storeEnumLiterals()
	{
		enumValues = new ArrayList<String>();
		enumLiterals = new ArrayList<EEnumLiteral>();

		EStructuralFeature inputFeature = getInputFeature();
		EEnum eEnum = (EEnum) inputFeature.getEType();
		EList<EEnumLiteral> eLiterals = eEnum.getELiterals();
		enumLiterals.addAll(eLiterals);
		for (EEnumLiteral literal: eLiterals)
		{
			enumValues.add(literal.getLiteral());
		}
	}
}
