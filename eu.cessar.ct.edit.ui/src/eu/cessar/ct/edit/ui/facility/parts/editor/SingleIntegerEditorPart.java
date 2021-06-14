/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others. http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870 Mar 16, 2010 9:03:06 AM </copyright>
 */
package eu.cessar.ct.edit.ui.facility.parts.editor;

import org.eclipse.swt.graphics.Image;

import eu.cessar.ct.core.platform.ui.PlatformUIConstants;
import eu.cessar.ct.core.platform.ui.util.PlatformUIUtils;
import eu.cessar.ct.core.platform.ui.widgets.IDatatypeEditor;
import eu.cessar.ct.core.platform.ui.widgets.SingleIntegerEditor;
import eu.cessar.ct.core.platform.util.ERadix;
import eu.cessar.ct.edit.ui.facility.IModelFragmentFeatureEditor;
import eu.cessar.ct.edit.ui.facility.parts.IIntegerEditorPart;

/**
 * @author uidl6870
 * 
 */
public class SingleIntegerEditorPart extends AbstractSingleNumericDatatypeEditorPart<Integer> implements
	IIntegerEditorPart
{
	private SingleIntegerEditor editor;

	/**
	 * @param editor
	 */
	public SingleIntegerEditorPart(IModelFragmentFeatureEditor editor)
	{
		super(editor);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.facility.parts.editor.AbstractSingleDatatypeEditorPart#createDatatypeEditor()
	 */
	@Override
	protected IDatatypeEditor<Integer> createDatatypeEditor()
	{
		// editor = new SingleIntegerEditor(true, getRadix());
		editor = new SingleIntegerEditor(true);
		return editor;
	}

	/*
	 * (non-Javadoc)
	 * 
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.facility.parts.IEditorPart#getImage()
	 */

	public Image getImage()
	{
		return PlatformUIUtils.getImage(PlatformUIConstants.IMAGE_ID_INTEGER);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.cessar.ct.edit.ui.facility.parts.IEditorPart#getText()
	 */
	public String getText()
	{
		Integer inputData = getInputData();
		if (inputData == null)
		{
			return ""; //$NON-NLS-1$
		}

		ERadix radix = getRadix();
		if (radix != null)
		{
			return Integer.toString(inputData, radix.getRadixNumber());
		}
		else
		{
			return Integer.toString(inputData);
		}

	}
}
