/**
 * <copyright>
 * 
 * Copyright (c) Continental Engineering Services and others.
 * http://www.conti-engineering.com All rights reserved.
 * 
 * File created by uidl6458 Mar 11, 2010 3:21:54 PM </copyright>
 */
package eu.cessar.ct.edit.ui.facility.parts.editor;

import org.eclipse.swt.graphics.Image;

import eu.cessar.ct.core.platform.ui.PlatformUIConstants;
import eu.cessar.ct.core.platform.ui.util.PlatformUIUtils;
import eu.cessar.ct.core.platform.ui.widgets.IDatatypeEditor;
import eu.cessar.ct.core.platform.ui.widgets.SingleBooleanEditor;
import eu.cessar.ct.edit.ui.facility.IModelFragmentFeatureEditor;

/**
 * @author uidl6458
 * 
 */
public class SingleBooleanEditorPart extends AbstractSingleDatatypeEditorPart<Boolean>
{

	/**
	 * @param editor
	 */
	public SingleBooleanEditorPart(IModelFragmentFeatureEditor editor)
	{
		super(editor);
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.edit.ui.facility.parts.AbstractDatatypeEditorPart#createDatatypeEditor()
	 */
	@Override
	protected IDatatypeEditor<Boolean> createDatatypeEditor()
	{
		SingleBooleanEditor singleBooleanEditor = new SingleBooleanEditor(true);
		singleBooleanEditor.setEventListener(getEditor().getEventListener());

		return singleBooleanEditor;
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.edit.ui.facility.parts.IEditorPart#getImage()
	 */
	public Image getImage()
	{
		return PlatformUIUtils.getImage(PlatformUIConstants.IMAGE_ID_BOOLEAN);
	}

	/* (non-Javadoc)
	 * @see eu.cessar.ct.edit.ui.facility.parts.IEditorPart#getText()
	 */
	public String getText()
	{
		boolean isSet = getInputObject().eIsSet(getInputFeature());

		if (isSet)
		{
			Boolean inputData = getInputData();
			if (inputData == null)
			{
				return ""; //$NON-NLS-1$
			}
			return inputData.toString();
		}

		return ""; //$NON-NLS-1$
	}

}
