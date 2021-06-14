/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870 Mar 17, 2010 10:39:48 AM </copyright>
 */
package eu.cessar.ct.edit.ui.facility.parts.editor;

import java.math.BigInteger;

import org.eclipse.swt.graphics.Image;

import eu.cessar.ct.core.platform.ui.PlatformUIConstants;
import eu.cessar.ct.core.platform.ui.util.PlatformUIUtils;
import eu.cessar.ct.core.platform.ui.widgets.IDatatypeEditor;
import eu.cessar.ct.core.platform.ui.widgets.SingleBigIntegerEditor;
import eu.cessar.ct.edit.ui.facility.IModelFragmentFeatureEditor;
import eu.cessar.ct.edit.ui.facility.parts.IIntegerEditorPart;

/**
 * @author uidl6870
 * 
 */
public class SingleUnlimitedIntegerEditorPart extends
	AbstractSingleNumericDatatypeEditorPart<BigInteger> implements IIntegerEditorPart
{
	private SingleBigIntegerEditor editor;

	/**
	 * @param editor
	 */
	public SingleUnlimitedIntegerEditorPart(IModelFragmentFeatureEditor editor)
	{
		super(editor);
	}

/* (non-Javadoc)
 * @see eu.cessar.ct.edit.ui.facility.parts.editor.AbstractSingleDatatypeEditorPart#createDatatypeEditor()
 */
	@Override
	protected IDatatypeEditor<BigInteger> createDatatypeEditor()
	{
		// editor = new SingleIntegerEditor(true, getRadix());
		editor = new SingleBigIntegerEditor(true);
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
		return PlatformUIUtils.getImage(PlatformUIConstants.IMAGE_ID_INTEGER);
	}

/* (non-Javadoc)
 * @see eu.cessar.ct.edit.ui.facility.parts.IEditorPart#getText()
 */
	public String getText()
	{
		BigInteger inputData = getInputData();
		if (inputData == null)
		{
			return "";
		}

		return inputData.toString(getRadix().getRadixNumber());
	}
}
