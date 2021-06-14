/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidt6343 Dec 8, 2010 11:08:15 AM </copyright>
 */
package eu.cessar.ct.edit.ui.facility.parts.editor;

import org.eclipse.swt.graphics.Image;

import eu.cessar.ct.core.platform.ui.PlatformUIConstants;
import eu.cessar.ct.core.platform.ui.util.PlatformUIUtils;
import eu.cessar.ct.core.platform.ui.widgets.IDatatypeEditor;
import eu.cessar.ct.core.platform.ui.widgets.SingleLongEditor;
import eu.cessar.ct.edit.ui.facility.IModelFragmentFeatureEditor;
import eu.cessar.ct.edit.ui.facility.parts.IIntegerEditorPart;

/**
 * @author uidt6343
 * 
 */
public class SingleLongEditorPart extends AbstractSingleNumericDatatypeEditorPart<Long> implements
	IIntegerEditorPart
{
	private SingleLongEditor editor;

	/**
	 * @param editor
	 */
	public SingleLongEditorPart(IModelFragmentFeatureEditor editor)
	{
		super(editor);
	}

/* (non-Javadoc)
 * @see eu.cessar.ct.edit.ui.facility.parts.editor.AbstractSingleDatatypeEditorPart#createDatatypeEditor()
 */
	@Override
	protected IDatatypeEditor<Long> createDatatypeEditor()
	{
		// editor = new SingleIntegerEditor(true, getRadix());
		editor = new SingleLongEditor(true);
		return editor;
	}

	/* (non-Javadoc)
	* @see eu.cessar.ct.edit.ui.facility.parts.editor.AbstractSingleDatatypeEditorPart#refresh()
	*/
	@Override
	public void refresh()
	{
		if (editor != null)
		{
			editor.setRadix(getRadix());
			updateRadixLabel();
		}
		super.refresh();
	}

/* (non-Javadoc)
 * @see eu.cessar.ct.edit.ui.facility.parts.IEditorPart#getImage()
 */
	public Image getImage()
	{
		return PlatformUIUtils.getImage(PlatformUIConstants.IMAGE_ID_LONG);
	}

/* (non-Javadoc)
 * @see eu.cessar.ct.edit.ui.facility.parts.IEditorPart#getText()
 */
	public String getText()
	{
		boolean isSet = getInputObject().eIsSet(getInputFeature());

		if (isSet)
		{
			Long inputData = getInputData();
			if (inputData == null)
			{
				return ""; //$NON-NLS-1$
			}
			return Long.toString(inputData, getRadix().getRadixNumber());
		}
		else
		{
			return ""; //$NON-NLS-1$
		}
	}
}