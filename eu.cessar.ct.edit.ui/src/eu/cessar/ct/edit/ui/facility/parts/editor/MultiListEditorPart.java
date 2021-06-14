/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidl6870 Mar 15, 2010 2:27:54 PM </copyright>
 */
package eu.cessar.ct.edit.ui.facility.parts.editor;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.Enumerator;
import org.eclipse.emf.ecore.EEnumLiteral;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import eu.cessar.ct.core.platform.ui.PlatformUIConstants;
import eu.cessar.ct.core.platform.ui.util.PlatformUIUtils;
import eu.cessar.ct.core.platform.ui.widgets.IDatatypeEditor;
import eu.cessar.ct.core.platform.ui.widgets.MultiDatatypeValueHandler;
import eu.cessar.ct.core.platform.ui.widgets.MultiListEditor;
import eu.cessar.ct.core.platform.util.CollectionUtils;
import eu.cessar.ct.edit.ui.facility.IModelFragmentFeatureEditor;
import eu.cessar.ct.edit.ui.facility.IModelFragmentFeatureEditorValues;

/**
 * @author uidl6870
 *
 */
public class MultiListEditorPart extends AbstractMultiDatatypeEditorPart<String>
{
	private boolean isCombo = true;

	/**
	 * @param editor
	 */
	public MultiListEditorPart(IModelFragmentFeatureEditor editor)
	{
		super(editor);
	}

	/**
	 * @param editor
	 * @param isCombo
	 */
	public MultiListEditorPart(IModelFragmentFeatureEditor editor, boolean isCombo)
	{
		super(editor);
		this.isCombo = isCombo;
	}

	@Override
	protected void setNewData(EObject input, java.util.List<String> newValue)
	{
		EList<Enumerator> data = (EList<Enumerator>) input.eGet(getInputFeature());
		data.clear();

		if (newValue != null && !newValue.isEmpty())
		{
			data.addAll(getEnumerators(newValue));
		}
	}

	private List<Enumerator> getEnumerators(List<String> newData)
	{
		List<Enumerator> newValues = new ArrayList<Enumerator>();

		List<EEnumLiteral> enumLiterals = ((IModelFragmentFeatureEditorValues) getEditor()).getEnumLiterals();
		for (String data: newData)
		{
			for (EEnumLiteral enumLiteral: enumLiterals)
			{
				if (enumLiteral.getLiteral().equals(data))
				{
					newValues.add(enumLiteral.getInstance());
				}
			}
		}

		return newValues;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.edit.ui.facility.parts.editor.AbstractSingleDatatypeEditorPart#createDatatypeEditor()
	 */
	@Override
	protected IDatatypeEditor<List<String>> createDatatypeEditor()
	{
		MultiDatatypeValueHandler<String> handler = new MultiDatatypeValueHandler<String>(new LabelProvider(),
			getInputFeature().getName(), getInputFeature().getName());
		MultiListEditor listEditor = new MultiListEditor(handler, isCombo);
		listEditor.setMaxValues(getInputFeature().getUpperBound());

		listEditor.setAllowedValues(((IModelFragmentFeatureEditorValues) getEditor()).getValues());
		return listEditor;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.edit.ui.facility.parts.editor.AbstractSingleDatatypeEditorPart#getInputData()
	 */
	@Override
	protected List<String> getInputData()
	{
		List<String> input = new ArrayList<String>();
		List<Enumerator> data = (List<Enumerator>) getInputObject().eGet(getInputFeature());

		if (data != null)
		{
			for (Enumerator eEnum: data)
			{
				if (eEnum != null)
				{
					input.add(eEnum.getLiteral());
				}
			}
		}
		return input;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.edit.ui.facility.parts.IEditorPart#getImage()
	 */
	public Image getImage()
	{
		return PlatformUIUtils.getImage(PlatformUIConstants.IMAGE_ID_ENUMERATION);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see eu.cessar.ct.edit.ui.facility.parts.IEditorPart#getText()
	 */
	public String getText()
	{
		String text;
		List<String> inputData = getInputData();
		text = CollectionUtils.toString(inputData, ", "); //$NON-NLS-1$
		if (text != null)
		{
			return text;
		}

		return ""; //$NON-NLS-1$
	}
}
