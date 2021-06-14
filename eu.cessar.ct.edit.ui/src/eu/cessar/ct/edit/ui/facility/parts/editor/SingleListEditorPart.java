/**
 * <copyright>
 *
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 *
 * File created by uidl6870 Mar 15, 2010 2:25:24 PM </copyright>
 */
package eu.cessar.ct.edit.ui.facility.parts.editor;

import java.util.List;

import org.eclipse.emf.common.util.Enumerator;
import org.eclipse.emf.ecore.EEnumLiteral;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Control;

import eu.cessar.ct.core.platform.ui.PlatformUIConstants;
import eu.cessar.ct.core.platform.ui.util.PlatformUIUtils;
import eu.cessar.ct.core.platform.ui.widgets.AbstractSingleEditor;
import eu.cessar.ct.core.platform.ui.widgets.IDatatypeEditor;
import eu.cessar.ct.core.platform.ui.widgets.ListEditor;
import eu.cessar.ct.core.platform.ui.widgets.SingleListEditor;
import eu.cessar.ct.edit.ui.facility.IModelFragmentFeatureEditor;
import eu.cessar.ct.edit.ui.facility.IModelFragmentFeatureEditorValues;
import eu.cessar.ct.edit.ui.internal.facility.editors.DefaultListFeatureEditor;

/**
 * @author uidl6870
 *
 */
public class SingleListEditorPart extends AbstractSingleDatatypeEditorPart<String>
{

	private boolean isCombo = true;

	/**
	 * @param editor
	 */
	public SingleListEditorPart(IModelFragmentFeatureEditor editor)
	{
		super(editor);
	}

	/**
	 * @param editor
	 * @param isCombo
	 */
	public SingleListEditorPart(IModelFragmentFeatureEditor editor, boolean isCombo)
	{
		super(editor);
		this.isCombo = isCombo;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.cessar.ct.edit.ui.facility.parts.editor.AbstractSingleDatatypeEditorPart#setNewData(org.eclipse.emf.ecore.
	 * EObject, java.lang.Object)
	 */
	@Override
	protected void setNewData(EObject input, String newValue)
	{
		if (newValue != null)
		{
			input.eSet(getInputFeature(), getEnumeratorForLiteral(newValue));
		}
		else
		{
			input.eUnset(getInputFeature());
		}
	}

	private Enumerator getEnumeratorForLiteral(String newData)
	{
		Enumerator newValue = null;

		List<EEnumLiteral> enumLiterals = ((DefaultListFeatureEditor) getEditor()).getEnumLiterals();
		for (EEnumLiteral eEnum: enumLiterals)
		{
			if (eEnum.getLiteral().equals(newData))
			{
				newValue = eEnum.getInstance();
				break;
			}
		}

		return newValue;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.facility.parts.editor.AbstractSingleDatatypeEditorPart#createDatatypeEditor()
	 */
	@Override
	protected IDatatypeEditor<String> createDatatypeEditor()
	{
		AbstractSingleEditor<String> singleListEditor = null;

		if (isCombo)
		{
			singleListEditor = new SingleListEditor(true);
			((SingleListEditor) singleListEditor).setAllowedValues(((IModelFragmentFeatureEditorValues) getEditor()).getValues());
		}
		else
		{
			singleListEditor = new ListEditor(true);

			((ListEditor) singleListEditor).setAllowedValues(((IModelFragmentFeatureEditorValues) getEditor()).getValues());
		}
		return singleListEditor;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.facility.parts.editor.AbstractSingleDatatypeEditorPart#getInputData()
	 */
	@Override
	protected String getInputData()
	{
		Object data = getInputObject().eGet(getInputFeature());

		if (data != null)
		{
			return ((Enumerator) data).getLiteral();
		}
		return null;
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
		boolean isSet = getInputObject().eIsSet(getInputFeature());

		if (isSet)
		{
			String inputData = getInputData();
			if (inputData == null)
			{
				return ""; //$NON-NLS-1$
			}

			return inputData;
		}
		return "";
	}

	public Control getControl()
	{
		return control;
	}

}
