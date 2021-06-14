/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6870 Mar 12, 2010 6:26:00 PM </copyright>
 */
package eu.cessar.ct.edit.ui.facility.parts.editor;

import java.util.List;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import eu.cessar.ct.core.platform.ui.PlatformUIConstants;
import eu.cessar.ct.core.platform.ui.util.PlatformUIUtils;
import eu.cessar.ct.core.platform.ui.widgets.IDatatypeEditor;
import eu.cessar.ct.core.platform.ui.widgets.MultiDatatypeValueHandler;
import eu.cessar.ct.core.platform.ui.widgets.MultiStringEditor;
import eu.cessar.ct.core.platform.util.CollectionUtils;
import eu.cessar.ct.edit.ui.facility.IModelFragmentFeatureEditor;

/**
 * @author uidl6870
 * 
 */
public class MultiStringEditorPart extends AbstractMultiDatatypeEditorPart<String>
{

	/**
	 * @param editor
	 */
	public MultiStringEditorPart(IModelFragmentFeatureEditor editor)
	{
		super(editor);
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.edit.ui.facility.parts.editor.AbstractSingleDatatypeEditorPart#createDatatypeEditor()
	 */
	@Override
	protected IDatatypeEditor<List<String>> createDatatypeEditor()
	{
		MultiDatatypeValueHandler<String> handler = new MultiDatatypeValueHandler<String>(
			new LabelProvider(), getInputFeature().getName(), getInputFeature().getName());
		MultiStringEditor stringEditor = new MultiStringEditor(handler);
		stringEditor.setMaxValues(getInputFeature().getUpperBound());
		return stringEditor;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.edit.ui.facility.parts.IEditorPart#getImage()
	 */
	public Image getImage()
	{
		return PlatformUIUtils.getImage(PlatformUIConstants.IMAGE_ID_STRING);
	}

	/* (non-Javadoc)
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
