/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870 Mar 17, 2010 10:24:11 AM </copyright>
 */
package eu.cessar.ct.edit.ui.facility.parts.editor;

import java.util.List;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import eu.cessar.ct.core.platform.ui.PlatformUIConstants;
import eu.cessar.ct.core.platform.ui.util.PlatformUIUtils;
import eu.cessar.ct.core.platform.ui.widgets.IDatatypeEditor;
import eu.cessar.ct.core.platform.ui.widgets.MultiDatatypeValueHandler;
import eu.cessar.ct.core.platform.ui.widgets.MultiDoubleEditor;
import eu.cessar.ct.core.platform.util.CollectionUtils;
import eu.cessar.ct.edit.ui.facility.IModelFragmentFeatureEditor;

/**
 * @author uidl6870
 * 
 */
public class MultiDoubleEditorPart extends AbstractMultiDatatypeEditorPart<Double>
{

	/**
	 * @param editor
	 */
	public MultiDoubleEditorPart(IModelFragmentFeatureEditor editor)
	{
		super(editor);
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.edit.ui.facility.parts.editor.AbstractSingleDatatypeEditorPart#createDatatypeEditor()
	 */
	@Override
	protected IDatatypeEditor<List<Double>> createDatatypeEditor()
	{
		MultiDatatypeValueHandler<Double> handler = new MultiDatatypeValueHandler<Double>(
			new LabelProvider(), getInputFeature().getName(), getInputFeature().getName());
		MultiDoubleEditor doubleEditor = new MultiDoubleEditor(handler);
		doubleEditor.setMaxValues(getInputFeature().getUpperBound());
		return doubleEditor;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.edit.ui.facility.parts.IEditorPart#getImage()
	 */
	public Image getImage()
	{
		return PlatformUIUtils.getImage(PlatformUIConstants.IMAGE_ID_FLOAT);
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.edit.ui.facility.parts.IEditorPart#getText()
	 */
	public String getText()
	{
		String text = ""; //$NON-NLS-1$
		List<Double> inputData = getInputData();
		text = CollectionUtils.toString(inputData, ", "); //$NON-NLS-1$
		if (text != null)
		{
			return text;
		}

		return ""; //$NON-NLS-1$
	}

}
