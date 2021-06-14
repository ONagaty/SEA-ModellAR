/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870 Mar 17, 2010 10:58:59 AM </copyright>
 */
package eu.cessar.ct.edit.ui.facility.parts.editor;

import java.util.List;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import eu.cessar.ct.core.platform.ui.PlatformUIConstants;
import eu.cessar.ct.core.platform.ui.util.PlatformUIUtils;
import eu.cessar.ct.core.platform.ui.widgets.IDatatypeEditor;
import eu.cessar.ct.core.platform.ui.widgets.MultiDatatypeValueHandler;
import eu.cessar.ct.core.platform.ui.widgets.MultiGenericEditor;
import eu.cessar.ct.core.platform.util.CollectionUtils;
import eu.cessar.ct.edit.ui.facility.IModelFragmentFeatureEditor;

/**
 * @author uidl6870
 * 
 */
public class MultiPrimitiveEditorPart extends AbstractMultiDatatypeEditorPart<Object>
{

	/**
	 * @param editor
	 */
	public MultiPrimitiveEditorPart(IModelFragmentFeatureEditor editor)
	{
		super(editor);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.edit.ui.facility.parts.editor.AbstractSingleDatatypeEditorPart#createDatatypeEditor()
	 */
	@Override
	protected IDatatypeEditor<List<Object>> createDatatypeEditor()
	{
		MultiDatatypeValueHandler<Object> handler = new MultiDatatypeValueHandler<Object>(
			new LabelProvider(), getInputFeature().getName(), getInputFeature().getName());
		MultiGenericEditor genericEditor = new MultiGenericEditor(handler);
		genericEditor.setMaxValues(getInputFeature().getUpperBound());
		return genericEditor;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.edit.ui.facility.parts.IEditorPart#getImage()
	 */
	public Image getImage()
	{
		return PlatformUIUtils.getImage(PlatformUIConstants.IMAGE_ID_PRIMITIVE);
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.edit.ui.facility.parts.IEditorPart#getText()
	 */
	public String getText()
	{
		String text;
		List<Object> inputData = getInputData();
		text = CollectionUtils.toString(inputData, ", "); //$NON-NLS-1$
		if (text != null)
		{
			return text;
		}

		return ""; //$NON-NLS-1$
	}

}
